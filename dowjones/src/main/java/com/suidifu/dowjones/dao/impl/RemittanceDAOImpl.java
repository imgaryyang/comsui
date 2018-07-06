package com.suidifu.dowjones.dao.impl;

import static org.apache.spark.sql.functions.count;
import static org.apache.spark.sql.functions.sum;

import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.dao.RemittanceDAO;
import com.suidifu.dowjones.utils.DateUtils;
import com.suidifu.dowjones.utils.GenericJdbcSupport;
import com.suidifu.dowjones.utils.SQLUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author veda
 * @date 02/01/2018
 */
@Repository
@Component("remittanceDAO")
public class RemittanceDAOImpl extends BaseDAO implements RemittanceDAO, Serializable {

    @Autowired
    private transient GenericJdbcSupport genericJdbcSupport;

    private transient final String ALIAS_COUNT = "count";
    private transient final String ALIAS_TOTAL_AMOUNT = "totalAmount";
    private transient final String ALIAS_TOTAL_PRINCIPAL = "totalPrincipal";
    private transient final String ALIAS_TOTAL_INTEREST = "totalInterest";
    private transient final String ALIAS_TOTAL_LOAN_SERVICE_FEE = "totalLoanServiceFee";

    private transient final String INSERT_SQL = "INSERT INTO `daily_remittance`(`financial_contract_uuid`, `create_date`,  `application_count`, `application_amount`, `plan_count`,`plan_amount`, `actual_count`,`actual_amount`,`asset_amount`,`asset_principal`,`asset_interest`,`asset_loan_service_fee`) VALUES (:financial_contract_uuid, :create_date, :application_count, :application_amount, :plan_count, :plan_amount, :actual_count, :actual_amount, :asset_amount, :asset_principal, :asset_interest, :asset_loan_service_fee);";

    private transient final String UPDATE_SQL = "UPDATE `daily_remittance` SET application_count = :application_count, application_amount = :application_amount, plan_count = :plan_count, plan_amount = :plan_amount, actual_count = :actual_count, actual_amount = :actual_amount, asset_amount = :asset_amount, asset_principal = :asset_principal, asset_interest = :asset_interest, asset_loan_service_fee = :asset_loan_service_fee where financial_contract_uuid = :financial_contract_uuid and create_date = :date;";

    @Override
    public void deleteExistData(String financialContractUuid, Date time) {
        String createDate = DateUtils.getDateFormatYYMMDD(time);

        if (StringUtils.isBlank(financialContractUuid) || StringUtils.isBlank(createDate)) {
            return;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("createDate", createDate);

        String sql = "DELETE FROM daily_remittance" +
            "  WHERE financial_contract_uuid = :financialContractUuid" +
            "    AND create_date = :createDate";

        genericJdbcSupport.executeSQL(sql, parameters);
    }

    @Override
    public Dataset<Row> remittanceApplicationStatistics(String financialContractUuid, Date time) {

        String yesterday = DateUtils.getDateFormatYYMMDD(time);

        if (StringUtils.isBlank(yesterday) || StringUtils.isEmpty(financialContractUuid)) {
            return null;
        }

        String sql = "SELECT"
            + " id AS id,"
            + " financial_contract_uuid as financial_contract_uuid,"
            + " CASE execution_status"
            + " WHEN 2"
            + " THEN planned_total_amount"
            + " ELSE 0"
            + " END"
            + " AS " + ALIAS_TOTAL_AMOUNT
            + " FROM t_remittance_application"
            + " WHERE DATE(create_time) = '" + yesterday + "'"
            + " AND execution_status IN (2, 4)"
            + " AND financial_contract_uuid = '" + financialContractUuid + "'";

        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql));
        ds = ds.groupBy("financial_contract_uuid").agg(
            count("id").as(ALIAS_COUNT),
            sum(ALIAS_TOTAL_AMOUNT).as(ALIAS_TOTAL_AMOUNT)
        );
        ds.printSchema();
        ds.show();

        return ds;
    }

    @Override
    public Dataset<Row> remittancePlanStatistics(String financialContractUuid, Date time) {

        String yesterday = DateUtils.getDateFormatYYMMDD(time);

        if (StringUtils.isBlank(yesterday) || StringUtils.isEmpty(financialContractUuid)) {
            return null;
        }

        String sql = "SELECT"
            + " financial_contract_uuid as financial_contract_uuid,"
            + " id as id,"
            + " planned_total_amount AS " + ALIAS_TOTAL_AMOUNT
            + " FROM t_remittance_plan"
            + " WHERE DATE(last_modified_time) = '" + yesterday + "'"
            + " AND execution_status = 2"
            + " AND financial_contract_uuid = '" + financialContractUuid + "'";

        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql));
        ds = ds.groupBy("financial_contract_uuid").agg(
            count("id").as(ALIAS_COUNT),
            sum(ALIAS_TOTAL_AMOUNT).as(ALIAS_TOTAL_AMOUNT)

        );
        ds.printSchema();
        ds.show();

        return ds;
    }

    @Override
    public Dataset<Row> remittancePlanExecLogStatistics(String financialContractUuid, Date time) {

        String yesterday = DateUtils.getDateFormatYYMMDD(time);

        if (StringUtils.isBlank(yesterday) || StringUtils.isEmpty(financialContractUuid)) {
            return null;
        }

        String sql = "SELECT"
            + " financial_contract_uuid as financial_contract_uuid,"
            + " id AS id,"
            + " actual_total_amount AS " + ALIAS_TOTAL_AMOUNT
            + " FROM t_remittance_plan_exec_log"
            + " WHERE DATE(last_modified_time) = '" + yesterday + "'"
            + " AND execution_status = 2"
            + " AND financial_contract_uuid = '" + financialContractUuid + "'";

        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql));

        ds = ds.groupBy("financial_contract_uuid").agg(
            count("id").as(ALIAS_COUNT),
            sum(ALIAS_TOTAL_AMOUNT).as(ALIAS_TOTAL_AMOUNT)

        );
        ds.printSchema();
        ds.show();

        return ds;
    }


    @Override
    public Dataset<Row> assetStatistics(String financialContractUuid, Date time) {

        String yesterday = DateUtils.getDateFormatYYMMDD(time);

        if (StringUtils.isBlank(yesterday) || StringUtils.isEmpty(financialContractUuid)) {
            return null;
        }

        String sql = "SELECT"
            + "   c.uuid as uuid,"
            + "   c.total_amount                        AS total_amount,"
            + "   a.asset_uuid                          AS asset_uuid,"
            + "   a.asset_principal_value               AS asset_principal_value,"
            + "   a.asset_interest_value                AS asset_interest_value,"
            + "   (SELECT SUM(CASE second_account_name"
            + "     WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE'"
            + "      THEN account_amount"
            + "     ELSE 0 END)"
            + "    FROM asset_set_extra_charge"
            + "    WHERE asset_set_uuid = a.asset_uuid) AS loan_service_fee"
            + " FROM ("
            + "   SELECT"
            + "     uuid,"
            + "     total_amount"
            + "   FROM contract"
            + "   WHERE DATE(begin_date) = '" + yesterday + "'"
            + "    AND financial_contract_uuid = '" + financialContractUuid + "'"
            + "    AND state = 2"
            + " ) c LEFT JOIN asset_set a ON a.contract_uuid = c.uuid";

        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql));
        ds = ds.groupBy("uuid", "total_amount").agg(
            sum("asset_principal_value").as(ALIAS_TOTAL_PRINCIPAL),
            sum("asset_interest_value").as(ALIAS_TOTAL_INTEREST),
            sum("loan_service_fee").as(ALIAS_TOTAL_LOAN_SERVICE_FEE)
        );

        ds = ds.agg(sum("total_amount").as(ALIAS_TOTAL_AMOUNT),
            sum(ALIAS_TOTAL_PRINCIPAL).as(ALIAS_TOTAL_PRINCIPAL),
            sum(ALIAS_TOTAL_INTEREST).as(ALIAS_TOTAL_INTEREST),
            sum(ALIAS_TOTAL_LOAN_SERVICE_FEE).as(ALIAS_TOTAL_LOAN_SERVICE_FEE));

        ds.printSchema();
        ds.show();

        return ds;
    }

    @Override
    public void saveResult(String financialContractUuid, Date time, Row applicationResult, Row planResult,
        Row execLogResult, Row assetResult) {

        String createTime = DateUtils.getDateFormatYYMMDD(time);

        String sql = "select count(id) from daily_remittance where financial_contract_uuid = '" + financialContractUuid
            + "' and create_date = '" + createTime + "' ";

        boolean exist = genericJdbcSupport.queryForInt(sql) > 0;

        String execSql = exist ? UPDATE_SQL : INSERT_SQL;

        Long applicationCount = applicationResult == null ? null : applicationResult.getLong(1);
        BigDecimal applicationAmount = applicationResult == null ? null : applicationResult.getDecimal(2);

        Long planCount = planResult == null ? null : planResult.getLong(1);
        BigDecimal planAmount = planResult == null ? null : planResult.getDecimal(2);

        Long execLogCount = execLogResult == null ? null : execLogResult.getLong(1);
        BigDecimal execLogAmount = execLogResult == null ? null : execLogResult.getDecimal(2);

        BigDecimal assetAmount = assetResult == null ? null : assetResult.getDecimal(0);
        BigDecimal assetPrincipal = assetResult == null ? null : assetResult.getDecimal(1);
        BigDecimal assetInterest = assetResult == null ? null : assetResult.getDecimal(2);
        BigDecimal assetLoanServiceFee = assetResult == null ? null : assetResult.getDecimal(3);

        Map<String, Object> params = new HashMap<>();
        params.put("create_date", createTime);
        params.put("financial_contract_uuid", financialContractUuid);
        params.put("application_count", applicationCount);
        params.put("application_amount", applicationAmount);
        params.put("plan_count", planCount);
        params.put("plan_amount", planAmount);
        params.put("actual_count", execLogCount);
        params.put("actual_amount", execLogAmount);
        params.put("asset_amount", assetAmount);
        params.put("asset_principal", assetPrincipal);
        params.put("asset_interest", assetInterest);
        params.put("asset_loan_service_fee", assetLoanServiceFee);

        if (isAllEmpty(params)){
            return;
        }

        genericJdbcSupport.executeSQL(execSql, params);
    }

    private boolean isAllEmpty(Map<String, Object> params) {
        return params.get("application_count") == null &&
            params.get("application_amount") == null &&
            params.get("plan_count") == null &&
            params.get("plan_amount") == null &&
            params.get("actual_count") == null &&
            params.get("actual_amount") == null &&
            params.get("asset_amount") == null &&
            params.get("asset_principal") == null &&
            params.get("asset_interest") == null &&
            params.get("asset_loan_service_fee") == null;
    }


}
