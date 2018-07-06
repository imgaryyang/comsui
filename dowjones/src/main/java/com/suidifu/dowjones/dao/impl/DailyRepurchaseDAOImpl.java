package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.dao.DailyRepurchaseDAO;
import com.suidifu.dowjones.utils.DateUtils;
import com.suidifu.dowjones.utils.GenericJdbcSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xwq
 */
@Repository
@Slf4j
public class DailyRepurchaseDAOImpl extends BaseDAO implements DailyRepurchaseDAO, Serializable {
    @Autowired
    private GenericJdbcSupport genericJdbcSupport;

    @Override
    public void saveData(Dataset<Row> rows, String sql) {
        saveData2Table(rows, sql, SaveMode.Append);
    }

    @Override
    public void deleteData(String financialContractUuid, Date date) {

        String createDate = DateUtils.getDateFormatYYMMDD(date);

        if (StringUtils.isBlank(financialContractUuid) || StringUtils.isBlank(createDate)) {
            return;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("createDate", createDate);

        String sql = "DELETE FROM daily_repurchase" +
                "  WHERE financial_contract_uuid = :financialContractUuid" +
                "    AND create_date = :createDate";

        genericJdbcSupport.executeSQL(sql, parameters);

    }
}
