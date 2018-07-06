package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.dao.DailyFirstOverdueRateDAO;
import com.suidifu.dowjones.service.DailyFirstOverdueRateService;
import com.suidifu.dowjones.utils.DateUtils;
import com.suidifu.dowjones.utils.GenericJdbcSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.ObjectName;
import java.math.BigDecimal;
import java.util.*;

import static org.apache.spark.sql.functions.input_file_name;
import static org.apache.spark.sql.functions.sum;

/**
 * Created by zxj on 2018/3/15.
 */
@Service("DailyFirstOverdueRateService")
@Slf4j
public class DailyFirstOverdueRateServiceImpl implements DailyFirstOverdueRateService {
    @Autowired
    private GenericJdbcSupport genericJdbcSupport;
    @Autowired
    private DailyFirstOverdueRateDAO firstOverdueRateDAO;
    @Autowired
    private DailyFirstOverdueRateDAO dailyFirstOverdueRateDAO;

    private List<Object> getUuids(List<Row> list , String key) {
        List<Object> uuids = new ArrayList<>();

        for (Row row: list) {
            uuids.add(row.getAs(key));
        }

        return uuids;
    }

    private void doCalculate(Row financialContractRow, Date yesterday, Boolean isWithGraceDay, int advaRepaymentTerm) {
        if (financialContractRow == null) return;
        String financialContractUuid = financialContractRow.getAs("financial_contract_uuid");
        if (StringUtils.isEmpty(financialContractUuid)) return;
        log.info("financialContractUuid=========" + financialContractUuid);
        //昨天到期的有效的还款计划
        Dataset<Row> yesterdayAssetsetData = firstOverdueRateDAO.getAssetSet(financialContractUuid, yesterday);
        if (yesterdayAssetsetData == null || yesterdayAssetsetData.count() <=0) {
            log.info("yesterdayAssetsetData is empty");
            //前天到期的还款计划在昨天发生的线下还款记录
            offlineRepayment(financialContractUuid, yesterday, isWithGraceDay, advaRepaymentTerm);
            return;
        }
        log.info("yesterdayAssetsetData size is" + yesterdayAssetsetData.count());
        //获取昨天到期的所有还款计划编号
        List<Object> yesterdayAssetUuids = getUuids(yesterdayAssetsetData.collectAsList(), "asset_uuid");
        //获取昨天为止的所有的还款记录
        Dataset<Row> yesterdayJournalVoucherData = firstOverdueRateDAO.getAllRepayment(yesterdayAssetUuids, yesterday);
        
        //获取昨天为止的所有的还款记录的实付记录
        Dataset<Row> yesterdayPaidData = null;
        //实还本金
        BigDecimal loanAssetPrincipal = new BigDecimal(0);
        //实还利息
        BigDecimal loanAssetInterest = new BigDecimal(0);
        if (yesterdayJournalVoucherData != null && yesterdayJournalVoucherData.count() >0) {
            //获取昨天为止的所有的还款记录uuid
            List<Object> yesterdayJournalVoucherUuids = getUuids(yesterdayJournalVoucherData.collectAsList(), "journal_voucher_uuid");
            //获取昨天为止的所有的还款记录的实付 本金 利息
            yesterdayPaidData = firstOverdueRateDAO.getPaidData(yesterdayJournalVoucherUuids);
            if (yesterdayPaidData != null && yesterdayPaidData.count() >0) {
                log.info("yesterdayPaidData size is" + yesterdayPaidData.count());
                //实还总额 本金 利息
                yesterdayPaidData = yesterdayPaidData.groupBy("ledger_book_no").agg(
                    sum("loan_asset_principal"),
                    sum("loan_asset_interest")
                );
                loanAssetPrincipal = yesterdayPaidData.first().getDecimal(1);
                loanAssetInterest = yesterdayPaidData.first().getDecimal(2);
            }
        }
        //应还总额 本金 利息
        yesterdayAssetsetData = yesterdayAssetsetData.groupBy("financial_contract_uuid", "asset_recycle_date").agg(
            sum("asset_principal_value"),
            sum("asset_interest_value")
        );
        //应还本金
        BigDecimal assetPrincipalValue = yesterdayAssetsetData.first().getDecimal(2);
        //应还利息
        BigDecimal assetInterestValue = yesterdayAssetsetData.first().getDecimal(3);

        //未尝总额 本金 利息
        BigDecimal remainingPrincipalValue = assetPrincipalValue.subtract(loanAssetPrincipal);
        BigDecimal remainingInterestValue = assetInterestValue.subtract(loanAssetInterest);

        //包含宽限日 更新 asset_principal_value_1 asset_interest_value_1 remaining_principal_value_1 remaining_interest_value_1
        if (isWithGraceDay) {
            //先查询是否有记录
            log.info("isWithGraceDay === true");
            String sql = "SELECT COUNT(1) FROM t_first_overdue_rate WHERE financial_contract_uuid = :financialContractUuid AND DATE(asset_recycle_date) = :yesterday AND status = 1";
            HashMap<String, Object> searchParams = new HashMap<>();
            searchParams.put("financialContractUuid",financialContractUuid);
            searchParams.put("yesterday",DateUtils.addDays(yesterday, advaRepaymentTerm));
            int item = genericJdbcSupport.queryForInt(sql, searchParams);
            if (item == 0) {
                log.info("INSERT INTO t_first_overdue_rate when isWithGraceDay= true and financialContractUuid =" + financialContractUuid);

                String insertSql = "INSERT INTO" +
                    " t_first_overdue_rate" +
                    " (contract_no, contract_name, financial_contract_uuid, asset_principal_value_1, asset_interest_value_1, remaining_principal_value_1," +
                    " remaining_interest_value_1, asset_recycle_date, create_time, status, user_name, last_modified_time)" +
                    " VALUES (" +
                    " :contract_no, :contract_name, :financial_contract_uuid, :asset_principal_value_1, :asset_interest_value_1, :remaining_principal_value_1," +
                    " :remaining_interest_value_1, :asset_recycle_date, :create_time, :status, :user_name, :last_modified_time"+
                    " )";
                log.info(insertSql);
                Map<String, Object> params = new HashMap();
                params.put("contract_no",financialContractRow.getAs("contract_no"));
                params.put("contract_name",financialContractRow.getAs("contract_name"));
                params.put("financial_contract_uuid",financialContractUuid);
                params.put("asset_principal_value_1",assetPrincipalValue);
                params.put("asset_interest_value_1",assetInterestValue);
                params.put("remaining_principal_value_1",remainingPrincipalValue);
                params.put("remaining_interest_value_1",remainingInterestValue);
                params.put("asset_recycle_date",DateUtils.addDays(yesterday, advaRepaymentTerm));
                params.put("create_time",new Date());
                params.put("status",1);
                params.put("user_name","系统");
                params.put("last_modified_time",new Date());

                genericJdbcSupport.executeSQL(insertSql, params);

            }else {
                log.info("UPDATE t_first_overdue_rate when isWithGraceDay= true and financialContractUuid =" + financialContractUuid);

                String  updateSql = "UPDATE" +
                    " t_first_overdue_rate" +
                    " SET asset_principal_value_1 = :asset_principal_value_1," +
                    " asset_interest_value_1 = :asset_interest_value_1," +
                    " last_modified_time = :last_modified_time," +
                    " remaining_principal_value_1 = :remaining_principal_value_1," +
                    " remaining_interest_value_1 = :remaining_interest_value_1" +
                    " WHERE financial_contract_uuid = :financial_contract_uuid" +
                    " AND DATE(asset_recycle_date) = :yesterday";

                Map<String, Object> updateParams = new HashMap();
                updateParams.put("financial_contract_uuid",financialContractUuid);
                updateParams.put("yesterday",DateUtils.addDays(yesterday, advaRepaymentTerm));
                updateParams.put("asset_principal_value_1",assetPrincipalValue);
                updateParams.put("asset_interest_value_1",assetInterestValue);
                updateParams.put("remaining_principal_value_1",remainingPrincipalValue);
                updateParams.put("remaining_interest_value_1",remainingInterestValue);
                updateParams.put("last_modified_time",new Date());

                genericJdbcSupport.executeSQL(updateSql, updateParams);
            }
        } else {
            String insertSql = "INSERT INTO" +
                " t_first_overdue_rate" +
                " (contract_no, contract_name, financial_contract_uuid, asset_principal_value, asset_interest_value, remaining_principal_value," +
                " remaining_interest_value, asset_recycle_date, create_time, status, user_name, last_modified_time)" +
                " VALUES (" +
                " :contractNo, :contractName, :financialContractUuid, :assetPrincipalValue, :assetInterestValue, :remainingPrincipalValue," +
                " :remainingInterestValue, :assetRecycleDate, :createTime, :status, :user_name, :lastModifiedTime"+
                " )";
            log.info(insertSql);
            Map<String, Object> params = new HashMap();
            params.put("contractNo",financialContractRow.getAs("contract_no"));
            params.put("contractName",financialContractRow.getAs("contract_name"));
            params.put("financialContractUuid",financialContractUuid);
            params.put("assetPrincipalValue",assetPrincipalValue);
            params.put("assetInterestValue",assetInterestValue);
            params.put("remainingPrincipalValue",remainingPrincipalValue);
            params.put("remainingInterestValue",remainingInterestValue);
            params.put("assetRecycleDate",yesterday);
            params.put("createTime",new Date());
            params.put("status",1);
            params.put("user_name","系统");
            params.put("lastModifiedTime",new Date());

            genericJdbcSupport.executeSQL(insertSql, params);
        }

        //前天到期的还款计划在昨天发生的线下还款记录
        offlineRepayment(financialContractUuid, yesterday, isWithGraceDay, advaRepaymentTerm);
    }

    /**
     * 查询前天到期的还款计划在昨天发生的线下还款记录
     * @param financialContractUuid
     * @param yesterday T-1 T-1-a
     * @param isWithGraceDay
     */
    private void offlineRepayment(String financialContractUuid, Date yesterday, Boolean isWithGraceDay, int advaRepaymentTerm) {
        //theDayBeforeYesterday T -2日 或者 T-2-a
        Date theDayBeforeYesterday = DateUtils.substractDays(yesterday, 1);
        //theDayBeforeYesterday到期的有效的还款计划
        Dataset<Row> theDayBeforeYesterdayAssetsetData = firstOverdueRateDAO.getAssetSet(financialContractUuid, theDayBeforeYesterday);
        if (theDayBeforeYesterdayAssetsetData == null || theDayBeforeYesterdayAssetsetData.count() <=0) {
            log.info("theDayBeforeYesterdayAssetsetData is empty");
            return;
        }
        log.info("theDayBeforeYesterdayAssetsetData size is" + theDayBeforeYesterdayAssetsetData.count());

        //前天到期的有效的还款计划的uuid
        List<Object> theDayBeforeYesterdayAssetsetUuids = getUuids(theDayBeforeYesterdayAssetsetData.collectAsList(), "asset_uuid");
        //theDayBeforeYesterday到期的还款计划在某天发生的线下还款记录 包含宽限日 T-1日发生的线下还款
        Date date = isWithGraceDay ? DateUtils.addDays(yesterday, advaRepaymentTerm) : yesterday;
        log.info("offlineRepayment ----- isWithGraceDay =" + isWithGraceDay + "===date=" + date);
        Dataset<Row> offlineRepaymentData = firstOverdueRateDAO.getOfflineRepayment(theDayBeforeYesterdayAssetsetUuids, date);
        if (offlineRepaymentData == null || offlineRepaymentData.count() <=0) {
            log.info("offlineRepaymentData is empty");
            return;
        }
        log.info("offlineRepaymentData size is" + offlineRepaymentData.count());

        //前天到期的还款计划在昨天发生的线下还款记录uuid
        List<Object> theDayBeforeYesterdayJournalVoucherUuids = getUuids(offlineRepaymentData.collectAsList(), "journal_voucher_uuid");
        //前天到期的还款计划在昨天发生的线下还款记录实付 本金 利息
        Dataset<Row> theDayBeforeYesterdayPaidData = firstOverdueRateDAO.getPaidData(theDayBeforeYesterdayJournalVoucherUuids);
        if (theDayBeforeYesterdayPaidData == null || theDayBeforeYesterdayPaidData.count() <=0) {
            log.info("theDayBeforeYesterdayPaidData is empty");
            return;
        }
        log.info("theDayBeforeYesterdayPaidData size is" + theDayBeforeYesterdayPaidData.count());

        //前天到期的还款计划在T-1发生的线下还款记录实付总额 本金 利息
        theDayBeforeYesterdayPaidData = theDayBeforeYesterdayPaidData.groupBy("ledger_book_no").agg(
            sum("loan_asset_principal"),
            sum("loan_asset_interest")
        );
        //线下还款本金总额
        BigDecimal offlineLoanPrincipal = theDayBeforeYesterdayPaidData.first().getDecimal(1);
        //线下还款利息总额
        BigDecimal offlineLoanInterest = theDayBeforeYesterdayPaidData.first().getDecimal(2);
        //存到T-2日的记录里
        Date saveDate =isWithGraceDay ? DateUtils.addDays(theDayBeforeYesterday, advaRepaymentTerm) : theDayBeforeYesterday;
        //包含宽限日
        if (isWithGraceDay) {
            //先查询是否有作废记录
            String sql = "SELECT COUNT(1) FROM t_first_overdue_rate WHERE financial_contract_uuid = :financialContractUuid AND DATE(asset_recycle_date) = :theDayBeforeYesterday AND status = 0";
            HashMap<String, Object> searchParams = new HashMap<>();
            searchParams.put("financialContractUuid",financialContractUuid);
            searchParams.put("theDayBeforeYesterday", DateUtils.substractDays(date, 1));
            int item = genericJdbcSupport.queryForInt(sql, searchParams);

            if (item == 1) {
                //有作废记录
                //更新线下还款本金 利息
                String updateSql = "UPDATE" +
                    " t_first_overdue_rate" +
                    " SET offline_principal_value_1 = :offline_principal_value_1," +
                    " offline_interest_value_1 = :offline_interest_value_1," +
                    " last_modified_time = :last_modified_time" +
                    " WHERE financial_contract_uuid = :financialContractUuid" +
                    " AND status = 1" +
                    " AND DATE(asset_recycle_date) = :theDayBeforeYesterday";

                Map<String, Object> updateParams = new HashMap();
                updateParams.put("financialContractUuid",financialContractUuid);
                updateParams.put("theDayBeforeYesterday",saveDate);
                updateParams.put("offline_principal_value_1",offlineLoanPrincipal);
                updateParams.put("offline_interest_value_1",offlineLoanInterest);
                updateParams.put("last_modified_time",new Date());

                genericJdbcSupport.executeSQL(updateSql, updateParams);
            } else {
                //没有作废的记录 有记录 update status = 0 再insert into status 1 或者 还没有记录 不存
                //作废之前的数据 设置status = 0
                String cancelSql = "UPDATE" +
                    " t_first_overdue_rate" +
                    " SET status = 0," +
                    " last_modified_time = :last_modified_time" +
                    " WHERE financial_contract_uuid = :financial_contract_uuid" +
                    " AND DATE(asset_recycle_date) = :asset_recycle_date" +
                    " AND status = 1";

                Map<String, Object> cancelParams = new HashMap();
                cancelParams.put("financial_contract_uuid",financialContractUuid);
                cancelParams.put("asset_recycle_date",saveDate);
                cancelParams.put("last_modified_time",new Date());

                genericJdbcSupport.executeSQL(cancelSql, cancelParams);

                //复制之前的数据 到新的记录 并修改状态为 status = 1 有效
                String updateSql = "INSERT INTO" +
                    " t_first_overdue_rate" +
                    " (contract_no, contract_name, financial_contract_uuid, asset_principal_value, asset_interest_value, remaining_principal_value," +
                    " remaining_interest_value, offline_principal_value, offline_interest_value,asset_principal_value_1,asset_interest_value_1," +
                    " remaining_principal_value_1,remaining_interest_value_1,offline_principal_value_1,offline_interest_value_1," +
                    " asset_recycle_date, create_time, status, user_name, last_modified_time)" +
                    " SELECT" +
                    " contract_no, contract_name, financial_contract_uuid, asset_principal_value, asset_interest_value, remaining_principal_value," +
                    " remaining_interest_value, offline_principal_value, offline_interest_value," +
                    " asset_principal_value_1,asset_interest_value_1,remaining_principal_value_1,remaining_interest_value_1," +
                    " :offline_principal_value_1 AS offline_principal_value_1, :offline_interest_value_1 AS offline_interest_value_1," +
                    " asset_recycle_date, create_time, 1 AS status, user_name, :last_modified_time AS last_modified_time" +
                    " FROM t_first_overdue_rate" +
                    " WHERE financial_contract_uuid = :financial_contract_uuid" +
                    " AND status = 0" +
                    " AND DATE(asset_recycle_date) = :asset_recycle_date";

                log.info("theDayBeforeYesterday has offline repayment ==== "+ financialContractUuid);

                Map<String, Object> updateParams = new HashMap();
                updateParams.put("offline_principal_value_1",offlineLoanPrincipal);
                updateParams.put("offline_interest_value_1",offlineLoanInterest);
                updateParams.put("financial_contract_uuid",financialContractUuid);
                updateParams.put("asset_recycle_date",saveDate);
                updateParams.put("last_modified_time",new Date());

                genericJdbcSupport.executeSQL(updateSql, updateParams);

            }
        } else {
            //作废之前的数据 status = 0
            String cancelSql = "UPDATE" +
                " t_first_overdue_rate" +
                " SET status = 0" +
                " WHERE financial_contract_uuid = :financial_contract_uuid" +
                " AND DATE(asset_recycle_date) = :asset_recycle_date" +
                " AND status = 1";

            Map<String, Object> cancelParams = new HashMap();
            cancelParams.put("financial_contract_uuid",financialContractUuid);
            cancelParams.put("asset_recycle_date",saveDate);

            genericJdbcSupport.executeSQL(cancelSql, cancelParams);

            //复制之前的数据 到新的记录 并修改状态为 status = 1 有效
            String updateSql = "INSERT INTO" +
                " t_first_overdue_rate" +
                " (contract_no, contract_name, financial_contract_uuid, asset_principal_value, asset_interest_value, remaining_principal_value," +
                " remaining_interest_value, offline_principal_value, offline_interest_value,asset_principal_value_1,asset_interest_value_1," +
                " remaining_principal_value_1,remaining_interest_value_1,offline_principal_value_1,offline_interest_value_1," +
                " asset_recycle_date, create_time, status, user_name, last_modified_time)" +
                " SELECT" +
                " contract_no, contract_name, financial_contract_uuid, asset_principal_value, asset_interest_value, remaining_principal_value," +
                " remaining_interest_value, :offline_principal_value AS offline_principal_value, :offline_interest_value AS offline_interest_value," +
                " asset_principal_value_1,asset_interest_value_1,remaining_principal_value_1,remaining_interest_value_1," +
                " offline_principal_value_1,offline_interest_value_1," +
                " asset_recycle_date, create_time, 1 AS status, user_name, :last_modified_time AS last_modified_time" +
                " FROM t_first_overdue_rate" +
                " WHERE financial_contract_uuid = :financial_contract_uuid" +
                " AND status = 0" +
                " AND DATE(asset_recycle_date) = :asset_recycle_date";

            log.info("theDayBeforeYesterday has offline repayment ==== "+ financialContractUuid);

            Map<String, Object> updateParams = new HashMap();
            updateParams.put("offline_principal_value",offlineLoanPrincipal);
            updateParams.put("offline_interest_value",offlineLoanInterest);
            updateParams.put("financial_contract_uuid",financialContractUuid);
            updateParams.put("asset_recycle_date",saveDate);
            updateParams.put("last_modified_time",new Date());

            genericJdbcSupport.executeSQL(updateSql, updateParams);
        }
    }

    @Override
    public void doFile(String financialContractUuid, Date yesterday) {

        Row financialContract = dailyFirstOverdueRateDAO.getFinancialContract(financialContractUuid);
        if (financialContract == null) {
            log.info("financialcontract data is empty");
            return;
        }
        //不包含宽限日
        doCalculate(financialContract, yesterday, false, 0);

        //包含宽限日
        //宽限日
        int advaRepaymentTerm = financialContract.getAs("adva_repayment_term");
        doCalculate(financialContract, DateUtils.substractDays(yesterday, advaRepaymentTerm), true,
            advaRepaymentTerm);
    }
}
