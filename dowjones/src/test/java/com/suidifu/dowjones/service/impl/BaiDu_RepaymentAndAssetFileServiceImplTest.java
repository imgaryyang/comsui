package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.service.BaiDu_RepaymentAndAssetFileService;
import com.suidifu.dowjones.utils.GenericJdbcSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxj on 2018/1/22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class BaiDu_RepaymentAndAssetFileServiceImplTest {
    @Autowired
    private BaiDu_RepaymentAndAssetFileService repaymentAndAssetFileService;
    @Autowired
    private GenericJdbcSupport genericJdbcSupport;

    private String fuuid = "c420576a-4d5b-4d45-b880-2fd5508cc2db";

    private Date date;

    {
        try {
            date = DateUtils.parseDate("2018-01-22", "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Sql("classpath:/test/test4BaiduRepaymentDataAll.sql")//还款数据
    public void doFileData() throws ParseException {

        repaymentAndAssetFileService.doFileData(fuuid, date);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", fuuid);
        parameters.put("createDate", date);

        //还款文件数据
        String repaymentDataSql = "SELECT * FROM bd_repayment_file WHERE financial_contract_uuid = :financialContractUuid"
                + " AND create_date = :createDate"
                + " and contract_unique_id='0487f077-187e-4c86-a12b-15c13f5fefed'"
                + " order by current_period asc ";
        List<Map<String, Object>> repaymentDataList = genericJdbcSupport.queryForList(repaymentDataSql, parameters);

        Assert.assertEquals(2, repaymentDataList.size());

        Map<String, Object> firstRepaymentData = repaymentDataList.get(0);

        Assert.assertEquals("0487f077-187e-4c86-a12b-15c13f5fefed", firstRepaymentData.get("contract_unique_id"));
        Assert.assertEquals(1, firstRepaymentData.get("current_period"));
        Assert.assertEquals("2018-02-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) firstRepaymentData.get("asset_recycle_date")));
        Assert.assertEquals(new BigDecimal("1000.00"), firstRepaymentData.get("asset_principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), firstRepaymentData.get("asset_interest_value"));
        Assert.assertEquals(null, firstRepaymentData.get("charge_amount"));
        Assert.assertEquals(new BigDecimal("1000.00"), firstRepaymentData.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("0.00"), firstRepaymentData.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("0.00"), firstRepaymentData.get("loan_charge_amount"));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) firstRepaymentData.get("actual_recycle_date")));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) firstRepaymentData.get("trade_time")));
        Assert.assertEquals(7, firstRepaymentData.get("journal_voucher_type"));
        Assert.assertEquals(1, firstRepaymentData.get("second_journal_voucher_type"));

        Map<String, Object> secondRepaymentData = repaymentDataList.get(1);

        Assert.assertEquals("0487f077-187e-4c86-a12b-15c13f5fefed", secondRepaymentData.get("contract_unique_id"));
        Assert.assertEquals(2, secondRepaymentData.get("current_period"));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondRepaymentData.get("asset_recycle_date")));
        Assert.assertEquals(new BigDecimal("2000.00"), secondRepaymentData.get("asset_principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), secondRepaymentData.get("asset_interest_value"));
        Assert.assertEquals(null, secondRepaymentData.get("charge_amount"));
        Assert.assertEquals(new BigDecimal("2000.00"), secondRepaymentData.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("0.00"), secondRepaymentData.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("0.00"), secondRepaymentData.get("loan_charge_amount"));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondRepaymentData.get("actual_recycle_date")));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondRepaymentData.get("trade_time")));
        Assert.assertEquals(7, secondRepaymentData.get("journal_voucher_type"));
        Assert.assertEquals(1, secondRepaymentData.get("second_journal_voucher_type"));


        repaymentDataSql = "SELECT * FROM bd_repayment_file WHERE financial_contract_uuid = :financialContractUuid"
                + " AND create_date = :createDate"
                + " and contract_unique_id='acc38ad1-e20b-4c6a-873f-57d71ba1de05'"
                + " order by current_period asc ";
        repaymentDataList = genericJdbcSupport.queryForList(repaymentDataSql, parameters);

        Assert.assertEquals(1, repaymentDataList.size());

        Map<String, Object> thirdRepaymentData = repaymentDataList.get(0);

        Assert.assertEquals("acc38ad1-e20b-4c6a-873f-57d71ba1de05", thirdRepaymentData.get("contract_unique_id"));
        Assert.assertEquals(1, thirdRepaymentData.get("current_period"));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) thirdRepaymentData.get("asset_recycle_date")));
        Assert.assertEquals(new BigDecimal("3000.00"), thirdRepaymentData.get("asset_principal_value"));
        Assert.assertEquals(new BigDecimal("1.00"), thirdRepaymentData.get("asset_interest_value"));
        Assert.assertEquals(new BigDecimal("9.00"), thirdRepaymentData.get("charge_amount"));
        Assert.assertEquals(null, thirdRepaymentData.get("loan_asset_principal"));
        Assert.assertEquals(null, thirdRepaymentData.get("loan_asset_interest"));
        Assert.assertEquals(null, thirdRepaymentData.get("loan_charge_amount"));
        Assert.assertEquals(null, thirdRepaymentData.get("actual_recycle_date"));
        Assert.assertEquals(null, thirdRepaymentData.get("trade_time"));
        Assert.assertEquals(null, thirdRepaymentData.get("journal_voucher_type"));
        Assert.assertEquals(null, thirdRepaymentData.get("second_journal_voucher_type"));

        //资产文件数据
        String assetDataSql = "SELECT * FROM bd_asset_file WHERE financial_contract_uuid = :financialContractUuid AND create_date = :createDate order by id asc";
        List<Map<String, Object>> assetDataList = genericJdbcSupport.queryForList(assetDataSql, parameters);

        Assert.assertEquals(2, assetDataList.size());

        Map<String, Object> firstAssetData = assetDataList.get(0);

        Assert.assertEquals("0487f077-187e-4c86-a12b-15c13f5fefed", firstAssetData.get("contract_unique_id"));
        Assert.assertEquals("WUBO", firstAssetData.get("customer_name"));
        Assert.assertEquals("320301198502169142", firstAssetData.get("customer_account"));
        Assert.assertEquals(null, firstAssetData.get("customer_mobile"));
        Assert.assertEquals(null, firstAssetData.get("customer_marriage"));
        Assert.assertEquals("北京市", firstAssetData.get("province"));
        Assert.assertEquals("北京市", firstAssetData.get("city"));
        Assert.assertEquals(null, firstAssetData.get("iou_status"));
        Assert.assertEquals(3, firstAssetData.get("total_periods"));
        Assert.assertEquals(new BigDecimal("3000.00"), firstAssetData.get("principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), firstAssetData.get("interest_value"));
        Assert.assertEquals(new BigDecimal("0.00"), firstAssetData.get("charge_amount"));
        Assert.assertEquals(new BigDecimal("0.00"), firstAssetData.get("interest_rate"));
        Assert.assertEquals(2, firstAssetData.get("repayment_way"));
        Assert.assertEquals(null, firstAssetData.get("formality_rate"));
        Assert.assertEquals(null, firstAssetData.get("formality"));
        Assert.assertEquals(null, firstAssetData.get("formality_type"));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) firstAssetData.get("loan_date")));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) firstAssetData.get("begin_date")));
        Assert.assertEquals("2099-01-01", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) firstAssetData.get("end_date")));
        Assert.assertEquals(null, firstAssetData.get("payment_day"));
        Assert.assertEquals("2018-02-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) firstAssetData.get("first_version_payment_date")));
        Assert.assertEquals(new BigDecimal("0.00"), firstAssetData.get("last_principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), firstAssetData.get("last_interest_value"));
        Assert.assertEquals(new BigDecimal("0.00"), firstAssetData.get("last_charge_amount"));
        Assert.assertEquals(null, firstAssetData.get("next_asset_recycle_date"));
        Assert.assertEquals(2, firstAssetData.get("clear_period"));
        Assert.assertEquals(0, firstAssetData.get("unclear_period"));
        Assert.assertEquals(null, firstAssetData.get("total_overdue"));
        Assert.assertEquals(null, firstAssetData.get("current_overdue"));

        Map<String, Object> secondAssetData = assetDataList.get(1);

        Assert.assertEquals("acc38ad1-e20b-4c6a-873f-57d71ba1de05", secondAssetData.get("contract_unique_id"));
        Assert.assertEquals("WUBO", secondAssetData.get("customer_name"));
        Assert.assertEquals("320301198502169142", secondAssetData.get("customer_account"));
        Assert.assertEquals(null, secondAssetData.get("customer_mobile"));
        Assert.assertEquals(null, secondAssetData.get("customer_marriage"));
        Assert.assertEquals("北京市", secondAssetData.get("province"));
        Assert.assertEquals("北京市", secondAssetData.get("city"));
        Assert.assertEquals(null, secondAssetData.get("iou_status"));
        Assert.assertEquals(3, secondAssetData.get("total_periods"));
        Assert.assertEquals(new BigDecimal("3000.00"), secondAssetData.get("principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), secondAssetData.get("interest_value"));
        Assert.assertEquals(new BigDecimal("0.00"), secondAssetData.get("charge_amount"));
        Assert.assertEquals(new BigDecimal("0.00"), secondAssetData.get("interest_rate"));
        Assert.assertEquals(2, secondAssetData.get("repayment_way"));
        Assert.assertEquals(null, secondAssetData.get("formality_rate"));
        Assert.assertEquals(null, secondAssetData.get("formality"));
        Assert.assertEquals(null, secondAssetData.get("formality_type"));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondAssetData.get("loan_date")));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondAssetData.get("begin_date")));
        Assert.assertEquals("2099-01-01", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondAssetData.get("end_date")));
        Assert.assertEquals("2018-01-22", secondAssetData.get("payment_day"));
        Assert.assertEquals("2018-02-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondAssetData.get("first_version_payment_date")));
        Assert.assertEquals(new BigDecimal("3000.00"), secondAssetData.get("last_principal_value"));
        Assert.assertEquals(new BigDecimal("1.00"), secondAssetData.get("last_interest_value"));
        Assert.assertEquals(new BigDecimal("9.00"), secondAssetData.get("last_charge_amount"));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondAssetData.get("next_asset_recycle_date")));
        Assert.assertEquals(0, secondAssetData.get("clear_period"));
        Assert.assertEquals(1, secondAssetData.get("unclear_period"));
        Assert.assertEquals(null, secondAssetData.get("total_overdue"));
        Assert.assertEquals(null, secondAssetData.get("current_overdue"));
    }

    @Test
    @Sql("classpath:/test/test4BaiduRepaymentDataAll.sql")//放款数据
    public void doIncrementalFileData() throws ParseException {

        repaymentAndAssetFileService.doIncrementalFileData(fuuid, date);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", fuuid);
        parameters.put("createDate", date);

        //新增还款文件数据
        String repaymentDataSql = "SELECT * FROM bd_incremental_repayment_file" +
                " WHERE financial_contract_uuid = :financialContractUuid" +
                " AND create_date = :createDate" +
                " order by id asc";
        List<Map<String, Object>> repaymentDataList = genericJdbcSupport.queryForList(repaymentDataSql, parameters);

        Assert.assertEquals(6, repaymentDataList.size());

        Map<String, Object> firstRepaymentData = repaymentDataList.get(0);

        Assert.assertEquals("acc38ad1-e20b-4c6a-873f-57d71ba1de05", firstRepaymentData.get("contract_unique_id"));
        Assert.assertEquals(1, firstRepaymentData.get("current_period"));
        Assert.assertEquals("2018-02-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) firstRepaymentData.get("asset_recycle_date")));
        Assert.assertEquals(new BigDecimal("1000.00"), firstRepaymentData.get("asset_principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), firstRepaymentData.get("asset_interest_value"));
        Assert.assertEquals(null, firstRepaymentData.get("charge_amount"));
        Assert.assertEquals(new BigDecimal("0.00"), firstRepaymentData.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("0.00"), firstRepaymentData.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("0.00"), firstRepaymentData.get("loan_charge_amount"));
        Assert.assertEquals(null, firstRepaymentData.get("actual_recycle_date"));
        Assert.assertEquals(null, firstRepaymentData.get("trade_time"));
        Assert.assertEquals(null, firstRepaymentData.get("journal_voucher_type"));
        Assert.assertEquals(null, firstRepaymentData.get("second_journal_voucher_type"));

        Map<String, Object> secondRepaymentData = repaymentDataList.get(1);

        Assert.assertEquals("acc38ad1-e20b-4c6a-873f-57d71ba1de05", secondRepaymentData.get("contract_unique_id"));
        Assert.assertEquals(2, secondRepaymentData.get("current_period"));
        Assert.assertEquals("2018-03-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondRepaymentData.get("asset_recycle_date")));
        Assert.assertEquals(new BigDecimal("1000.00"), secondRepaymentData.get("asset_principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), secondRepaymentData.get("asset_interest_value"));
        Assert.assertEquals(null, secondRepaymentData.get("charge_amount"));
        Assert.assertEquals(new BigDecimal("0.00"), secondRepaymentData.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("0.00"), secondRepaymentData.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("0.00"), secondRepaymentData.get("loan_charge_amount"));
        Assert.assertEquals(null, secondRepaymentData.get("actual_recycle_date"));
        Assert.assertEquals(null, secondRepaymentData.get("trade_time"));
        Assert.assertEquals(null, secondRepaymentData.get("journal_voucher_type"));
        Assert.assertEquals(null, secondRepaymentData.get("second_journal_voucher_type"));

        Map<String, Object> thirdRepaymentData = repaymentDataList.get(2);

        Assert.assertEquals("acc38ad1-e20b-4c6a-873f-57d71ba1de05", thirdRepaymentData.get("contract_unique_id"));
        Assert.assertEquals(3, thirdRepaymentData.get("current_period"));
        Assert.assertEquals("2018-04-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) thirdRepaymentData.get("asset_recycle_date")));
        Assert.assertEquals(new BigDecimal("1000.00"), thirdRepaymentData.get("asset_principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), thirdRepaymentData.get("asset_interest_value"));
        Assert.assertEquals(null, thirdRepaymentData.get("charge_amount"));
        Assert.assertEquals(new BigDecimal("0.00"), thirdRepaymentData.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("0.00"), thirdRepaymentData.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("0.00"), thirdRepaymentData.get("loan_charge_amount"));
        Assert.assertEquals(null, thirdRepaymentData.get("actual_recycle_date"));
        Assert.assertEquals(null, thirdRepaymentData.get("trade_time"));
        Assert.assertEquals(null, thirdRepaymentData.get("journal_voucher_type"));
        Assert.assertEquals(null, thirdRepaymentData.get("second_journal_voucher_type"));

        Map<String, Object> fourthRepaymentData = repaymentDataList.get(3);

        Assert.assertEquals("0487f077-187e-4c86-a12b-15c13f5fefed", fourthRepaymentData.get("contract_unique_id"));
        Assert.assertEquals(1, fourthRepaymentData.get("current_period"));
        Assert.assertEquals("2018-02-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) fourthRepaymentData.get("asset_recycle_date")));
        Assert.assertEquals(new BigDecimal("1000.00"), fourthRepaymentData.get("asset_principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), fourthRepaymentData.get("asset_interest_value"));
        Assert.assertEquals(null, fourthRepaymentData.get("charge_amount"));
        Assert.assertEquals(new BigDecimal("0.00"), fourthRepaymentData.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("0.00"), fourthRepaymentData.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("0.00"), fourthRepaymentData.get("loan_charge_amount"));
        Assert.assertEquals(null, fourthRepaymentData.get("actual_recycle_date"));
        Assert.assertEquals(null, fourthRepaymentData.get("trade_time"));
        Assert.assertEquals(null, fourthRepaymentData.get("journal_voucher_type"));
        Assert.assertEquals(null, fourthRepaymentData.get("second_journal_voucher_type"));

        Map<String, Object> fifthRepaymentData = repaymentDataList.get(4);

        Assert.assertEquals("0487f077-187e-4c86-a12b-15c13f5fefed", fifthRepaymentData.get("contract_unique_id"));
        Assert.assertEquals(2, fifthRepaymentData.get("current_period"));
        Assert.assertEquals("2018-03-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) fifthRepaymentData.get("asset_recycle_date")));
        Assert.assertEquals(new BigDecimal("1000.00"), fifthRepaymentData.get("asset_principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), fifthRepaymentData.get("asset_interest_value"));
        Assert.assertEquals(null, fifthRepaymentData.get("charge_amount"));
        Assert.assertEquals(new BigDecimal("0.00"), fifthRepaymentData.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("0.00"), fifthRepaymentData.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("0.00"), fifthRepaymentData.get("loan_charge_amount"));
        Assert.assertEquals(null, fifthRepaymentData.get("actual_recycle_date"));
        Assert.assertEquals(null, fifthRepaymentData.get("trade_time"));
        Assert.assertEquals(null, fifthRepaymentData.get("journal_voucher_type"));
        Assert.assertEquals(null, fifthRepaymentData.get("second_journal_voucher_type"));

        Map<String, Object> sixthRepaymentData = repaymentDataList.get(5);

        Assert.assertEquals("0487f077-187e-4c86-a12b-15c13f5fefed", sixthRepaymentData.get("contract_unique_id"));
        Assert.assertEquals(3, sixthRepaymentData.get("current_period"));
        Assert.assertEquals("2018-04-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) sixthRepaymentData.get("asset_recycle_date")));
        Assert.assertEquals(new BigDecimal("1000.00"), sixthRepaymentData.get("asset_principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), sixthRepaymentData.get("asset_interest_value"));
        Assert.assertEquals(null, sixthRepaymentData.get("charge_amount"));
        Assert.assertEquals(new BigDecimal("0.00"), sixthRepaymentData.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("0.00"), sixthRepaymentData.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("0.00"), sixthRepaymentData.get("loan_charge_amount"));
        Assert.assertEquals(null, sixthRepaymentData.get("actual_recycle_date"));
        Assert.assertEquals(null, sixthRepaymentData.get("trade_time"));
        Assert.assertEquals(null, sixthRepaymentData.get("journal_voucher_type"));
        Assert.assertEquals(null, sixthRepaymentData.get("second_journal_voucher_type"));


        //新增资产文件数据
        String assetDataSql = "SELECT * FROM bd_incremental_asset_file WHERE financial_contract_uuid = :financialContractUuid AND create_date = :createDate";
        List<Map<String, Object>> assetDataList = genericJdbcSupport.queryForList(assetDataSql, parameters);

        Assert.assertEquals(2, assetDataList.size());

        Map<String, Object> firstAssetData = assetDataList.get(0);

        Assert.assertEquals("acc38ad1-e20b-4c6a-873f-57d71ba1de05", firstAssetData.get("contract_unique_id"));
        Assert.assertEquals("WUBO", firstAssetData.get("customer_name"));
        Assert.assertEquals("320301198502169142", firstAssetData.get("customer_account"));
        Assert.assertEquals(null, firstAssetData.get("customer_mobile"));
        Assert.assertEquals(null, firstAssetData.get("customer_marriage"));
        Assert.assertEquals("北京市", firstAssetData.get("province"));
        Assert.assertEquals("北京市", firstAssetData.get("city"));
        Assert.assertEquals(null, firstAssetData.get("iou_status"));
        Assert.assertEquals(3, firstAssetData.get("total_periods"));
        Assert.assertEquals(new BigDecimal("3000.00"), firstAssetData.get("principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), firstAssetData.get("interest_value"));
        Assert.assertEquals(null, firstAssetData.get("charge_amount"));
        Assert.assertEquals(new BigDecimal("0.00"), firstAssetData.get("interest_rate"));
        Assert.assertEquals(2, firstAssetData.get("repayment_way"));
        Assert.assertEquals(null, firstAssetData.get("formality_rate"));
        Assert.assertEquals(null, firstAssetData.get("formality"));
        Assert.assertEquals(null, firstAssetData.get("formality_type"));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) firstAssetData.get("loan_date")));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) firstAssetData.get("begin_date")));
        Assert.assertEquals("2099-01-01", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) firstAssetData.get("end_date")));
        Assert.assertEquals("2018-02-22", firstAssetData.get("payment_day"));
        Assert.assertEquals("2018-02-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) firstAssetData.get("first_version_payment_date")));
        Assert.assertEquals(new BigDecimal("3000.00"), firstAssetData.get("last_principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), firstAssetData.get("last_interest_value"));
        Assert.assertEquals(null, firstAssetData.get("last_charge_amount"));
        Assert.assertEquals("2018-02-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) firstAssetData.get("next_asset_recycle_date")));
        Assert.assertEquals(0, firstAssetData.get("clear_period"));
        Assert.assertEquals(3, firstAssetData.get("unclear_period"));
        Assert.assertEquals(null, firstAssetData.get("total_overdue"));
        Assert.assertEquals(null, firstAssetData.get("current_overdue"));

        Map<String, Object> secondAssetData = assetDataList.get(1);

        Assert.assertEquals("0487f077-187e-4c86-a12b-15c13f5fefed", secondAssetData.get("contract_unique_id"));
        Assert.assertEquals("WUBO", secondAssetData.get("customer_name"));
        Assert.assertEquals("320301198502169142", secondAssetData.get("customer_account"));
        Assert.assertEquals(null, secondAssetData.get("customer_mobile"));
        Assert.assertEquals(null, secondAssetData.get("customer_marriage"));
        Assert.assertEquals("北京市", secondAssetData.get("province"));
        Assert.assertEquals("北京市", secondAssetData.get("city"));
        Assert.assertEquals(null, secondAssetData.get("iou_status"));
        Assert.assertEquals(3, secondAssetData.get("total_periods"));
        Assert.assertEquals(new BigDecimal("3000.00"), secondAssetData.get("principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), secondAssetData.get("interest_value"));
        Assert.assertEquals(null, secondAssetData.get("charge_amount"));
        Assert.assertEquals(new BigDecimal("0.00"), secondAssetData.get("interest_rate"));
        Assert.assertEquals(2, secondAssetData.get("repayment_way"));
        Assert.assertEquals(null, secondAssetData.get("formality_rate"));
        Assert.assertEquals(null, secondAssetData.get("formality"));
        Assert.assertEquals(null, secondAssetData.get("formality_type"));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondAssetData.get("loan_date")));
        Assert.assertEquals("2018-01-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondAssetData.get("begin_date")));
        Assert.assertEquals("2099-01-01", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondAssetData.get("end_date")));
        Assert.assertEquals("2018-02-22", secondAssetData.get("payment_day"));
        Assert.assertEquals("2018-02-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondAssetData.get("first_version_payment_date")));
        Assert.assertEquals(new BigDecimal("3000.00"), secondAssetData.get("last_principal_value"));
        Assert.assertEquals(new BigDecimal("0.00"), secondAssetData.get("last_interest_value"));
        Assert.assertEquals(null, secondAssetData.get("last_charge_amount"));
        Assert.assertEquals("2018-02-22", com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) secondAssetData.get("next_asset_recycle_date")));
        Assert.assertEquals(0, secondAssetData.get("clear_period"));
        Assert.assertEquals(3, secondAssetData.get("unclear_period"));
        Assert.assertEquals(null, secondAssetData.get("total_overdue"));
        Assert.assertEquals(null, secondAssetData.get("current_overdue"));

    }
}
