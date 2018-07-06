package com.zufangbao.earth.service.impl;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.service.FirstOverdueRateService;
import com.zufangbao.sun.entity.firstOverdueRate.FirstOverdueRate;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zxj on 2018/3/19.
 */
@Service("FirstOverdueRateService")
public class FirstOverdueRateImpl extends GenericServiceImpl<FirstOverdueRate> implements FirstOverdueRateService {
    @Autowired
    protected GenericDaoSupport genericDaoSupport;

    @Override
    public List<FirstOverdueRate> queryList(String financialContractUuids, Date date, Page page) {
        if (StringUtils.isEmpty(financialContractUuids)) {
            return Collections.emptyList();
        }
        List<String> financialContractUuidList = JsonUtils.parseArray(financialContractUuids,String.class);

        if (financialContractUuidList == null || financialContractUuidList.size() <= 0) {
            return Collections.emptyList();
        }

        StringBuffer sql = new StringBuffer("FROM FirstOverdueRate WHERE financialContractUuid IN (:financialContractUuid) AND status = 1");
        HashMap<String, Object> params = new HashMap<>();
        params.put("financialContractUuid",financialContractUuidList);

        if (date != null) {
            sql.append(" AND DATE(assetRecycleDate) = :assetRecycleDate");
            params.put("assetRecycleDate",date);
        }
        if(page == null) {
            return this.genericDaoSupport.searchForList(sql.toString(), params);
        }else {
            return this.genericDaoSupport.searchForList(sql.toString(), params, page.getBeginIndex(), page.getEveryPage());
        }
    }

    @Override
    public int countList(String financialContractUuids, Date date) {
        if (StringUtils.isEmpty(financialContractUuids)) {
            return 0;
        }
        List<String> financialContractUuidList = JsonUtils.parseArray(financialContractUuids,String.class);

        if (financialContractUuidList == null || financialContractUuidList.size() <= 0) {
            return 0;
        }

        StringBuffer sql = new StringBuffer("SELECT count(id) FROM FirstOverdueRate WHERE financialContractUuid IN (:financialContractUuid) AND status = 1");
        HashMap<String, Object> params = new HashMap<>();
        params.put("financialContractUuid",financialContractUuidList);

        if ( date != null) {
            sql.append(" AND DATE(assetRecycleDate) = :assetRecycleDate");
            params.put("assetRecycleDate",date);
        }

        return this.genericDaoSupport.searchForInt(sql.toString(), params);
    }

    @Override
    public void update(String financialContractUuid, Date date, String userName) {
        if (StringUtils.isEmpty(financialContractUuid) ||  date == null) return;
        //宽限日
        StringBuffer fcSql = new StringBuffer("SELECT" +
            " adva_repayment_term" +
            " FROM financial_contract" +
            " WHERE financial_contract_uuid = :financialContractUuid");
        List<Integer> advaRepaymentTerms = this.genericDaoSupport.queryForSingleColumnList(fcSql.toString(), "financialContractUuid", financialContractUuid,Integer.class);
        if (advaRepaymentTerms == null) return;
        int advaRepaymentTerm = advaRepaymentTerms.get(0) + 1;
        //不包含宽限日
        updateOfflineLoan(financialContractUuid, date, false, userName, advaRepaymentTerm);
        //包含宽限日
        updateOfflineLoan(financialContractUuid, DateUtils.addDays(date, -advaRepaymentTerm), true, userName, advaRepaymentTerm);
    }

    private void updateOfflineLoan(String financialContractUuid, Date date, Boolean isWithGraceDay, String userName, int advaRepaymentTerm) {
        Date startDate = isWithGraceDay ? DateUtils.addDays(date, advaRepaymentTerm + 1) : DateUtils.addDays(date, 1);
        Date endDate = new Date();
        StringBuffer sql = new StringBuffer("SELECT" +
            " SUM(CASE WHEN lbs.third_account_name IN ('TRD_BANK_SAVING_GENERAL_PRINCIPAL', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSEL_PRINCIPAL') " +
            "   THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END) AS loanAssetPrincipal," +
            " SUM(CASE WHEN lbs.third_account_name IN ('TRD_BANK_SAVING_GENERAL_INTEREST', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST')" +
            "   THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END ) AS loanAssetInterest" +
            " FROM asset_set asset" +
            " LEFT JOIN journal_voucher jv" +
            " ON jv.journal_voucher_type IN (5,9,11,14)" +
            " AND jv.status != 2" +
            " AND DATE(jv.issued_time) >= :start_time" +
            " AND DATE(jv.issued_time) <= :end_time" +
            " AND jv.related_bill_contract_info_lv_3 = asset.asset_uuid" +
            " LEFT JOIN ledger_book_shelf lbs" +
            " ON lbs.journal_voucher_uuid = jv.journal_voucher_uuid" +
            " WHERE asset.financial_contract_uuid = :financial_contract_uuid" +
            " AND asset.active_status = 0" +
            " AND DATE(asset.asset_recycle_date) = :asset_recycle_date" +
            " GROUP BY asset.financial_contract_uuid");
        HashMap<String, Object> params = new HashMap<>();
        params.put("financial_contract_uuid",financialContractUuid);
        params.put("asset_recycle_date",date);
        params.put("start_time", startDate);
        params.put("end_time", endDate);

        List<Map<String, Object>> result = this.genericDaoSupport.queryForList(sql.toString(), params);
        if (result == null || result.size() <= 0) return;
        BigDecimal offlineLoanPrincipal = result.get(0).get("loanAssetPrincipal") == null ? new BigDecimal(0) : (BigDecimal) result.get(0).get("loanAssetPrincipal");
        BigDecimal offlineLoanInterest = result.get(0).get("loanAssetInterest") == null ? new BigDecimal(0) : (BigDecimal) result.get(0).get("loanAssetInterest");

        String updateSql = "UPDATE t_first_overdue_rate" +
            " SET offline_principal_value = :offlineLoanPrincipal," +
            " offline_interest_value = :offlineLoanInterest," +
            " user_name = :user_name," +
            " last_modified_time = :last_modified_time" +
            " WHERE financial_contract_uuid = :financial_contract_uuid" +
            " AND DATE(asset_recycle_date) = :asset_recycle_date" +
            " AND status = 1";

        if (isWithGraceDay) { //包含宽限日
            updateSql = updateSql.replace("offline_principal_value", "offline_principal_value_1");
            updateSql = updateSql.replace("offline_interest_value", "offline_interest_value_1");
        }

        HashMap<String, Object> updateParams = new HashMap<>();
        updateParams.put("financial_contract_uuid",financialContractUuid);
        updateParams.put("asset_recycle_date",date);
        updateParams.put("offlineLoanPrincipal",offlineLoanPrincipal);
        updateParams.put("offlineLoanInterest",offlineLoanInterest);
        updateParams.put("last_modified_time",new Date());
        updateParams.put("user_name",userName);

        this.genericDaoSupport.executeSQL(updateSql, updateParams);
    }

    @Override
    public List<FirstOverdueRate> showHistory(String financialContractUuid, Date date) {
        if (StringUtils.isEmpty(financialContractUuid) || date == null) {
            return Collections.emptyList();
        }
        StringBuffer sql = new StringBuffer("FROM FirstOverdueRate WHERE financialContractUuid = :financialContractUuid AND DATE(assetRecycleDate) = :assetRecycleDate ORDER BY lastModifiedTime DESC");

        HashMap<String, Object> params = new HashMap<>();
        params.put("financialContractUuid",financialContractUuid);
        params.put("assetRecycleDate",date);

        return this.genericDaoSupport.searchForList(sql.toString(),params);
    }

    @Override
    public FirstOverdueRate queryFirstOverdueRate(String financialContractUuid, Date date) {
        if (StringUtils.isEmpty(financialContractUuid) || date == null) {
            return null;
        }
        StringBuffer sql = new StringBuffer("FROM FirstOverdueRate WHERE financialContractUuid =:financialContractUuid AND DATE(assetRecycleDate) = :assetRecycleDate AND status = 1 ORDER BY lastModifiedTime DESC");
        HashMap<String, Object> params = new HashMap<>();
        params.put("financialContractUuid",financialContractUuid);
        params.put("assetRecycleDate",date);
        List<FirstOverdueRate> resultList = this.genericDaoSupport.searchForList(sql.toString(), params);
        if (resultList.size() > 0) {
            return resultList.get(0);
        }
        return null;
    }
}
