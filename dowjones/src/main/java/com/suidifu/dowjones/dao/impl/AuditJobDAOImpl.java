package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.dao.AuditJobDAO;
import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.utils.SQLUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author xwq
 */
@Repository
@Slf4j
public class AuditJobDAOImpl extends BaseDAO implements AuditJobDAO {
    @Override
    public Dataset<Row> getOnTheWayMoneyData() {
        String yesterdayDate = getYesterdayDate();
        if (StringUtils.isBlank(yesterdayDate)) {
            return null;
        }

        String sql = "SELECT" +
                "  result.system_bill_plan_amount" +
                "  FROM" +
                "  	 audit_job job" +
                "  WHERE DATE(create_time) <= '" + yesterdayDate + "'" +
                "    AND (clearing_status = '0' OR clearing_status = '1')" +
                "  LEFT JOIN " +
                "    Beneficiary_audit_result result" +
                "  ON result.fst_merchant_no = job.merchant_no" +
                "    AND result.payment_gateway = job.payment_institution" +
                "    AND result.result_time >= job.start_time" +
                "    AND result.result_time <= job.end_time" +
                "    AND result.result_code = job.audit_result" +
                "    AND (" +
                "    		(" +
                "    			(result.snd_merchant_no IS NULL OR result.snd_merchant_no = '')" +
                "    			AND" +
                "    			(job.pg_clearing_account IS NULL OR job.pg_clearing_account = '')" +
                "    		)" +
                "    		OR" +
                "    		(result.snd_merchant_no = job.pg_clearing_account)" +
                "    	)";

        String[] predicates = initPredicates("audit_job");

        return loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates).
                groupBy("financialContractUuid").count();
    }

    private String getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.addDays(new Date(), -1));
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }
}
