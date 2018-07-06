package com.suidifu.dowjones.dao;

import com.suidifu.dowjones.config.SparkProperties;
import com.suidifu.dowjones.utils.SQLUtils;
import com.suidifu.dowjones.vo.enumeration.RepurchaseMode;
import com.suidifu.dowjones.vo.request.QueryParameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.*;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/7 <br>
 * @time: 下午12:17 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Repository
@Slf4j
public class BaseDAO implements Serializable {
    private static final int PARTITION_NUMBER = 20;
    @Resource
    private transient SparkSession sparkSession;

    @Resource
    private transient SparkProperties sparkProperties;

    protected String[] initPredicates(String table) {
        String sql = SQLUtils.wrapperSQL("select min(id) AS min,max(id) AS max from " + table);

        List<Row> rows = loadDataFromTable(sql).collectAsList();

        if (rows.get(0).get(0) == null) {
            log.info("data is empty");
            return new String[]{};
        }
        long minValue = Long.parseLong(rows.get(0).get(0).toString());
        long maxValue = Long.parseLong(rows.get(0).get(1).toString());

        // 一个条件表示一个分区
        String[] predicates = new String[PARTITION_NUMBER];

        long offset = (maxValue - minValue) / PARTITION_NUMBER;
        predicates[0] = "id>=" + minValue + " and id<" + offset;
        for (int i = 1; i < PARTITION_NUMBER; i++) {
            if (i == PARTITION_NUMBER - 1) {
                predicates[i] = "id>=" + offset * i + " and id<=" + maxValue;
                continue;
            }
            predicates[i] = "id>=" + offset * i + " and id<" + offset * (i + 1);
        }

        return predicates;
    }

    protected String[] initPredicatesWithTableName(String table, String tableAlias) {
        String sql = SQLUtils.wrapperSQL("select min(id) AS min,max(id) AS max from " + table);

        List<Row> rows = loadDataFromTable(sql).collectAsList();
        if (rows.get(0).get(0) == null) {
            log.info("data is empty");
            return new String[]{};
        }
        long minValue = Long.parseLong(rows.get(0).get(0).toString());
        long maxValue = Long.parseLong(rows.get(0).get(1).toString());

        // 一个条件表示一个分区
        String[] predicates = new String[PARTITION_NUMBER];

        long offset = (maxValue - minValue) / PARTITION_NUMBER;
        predicates[0] = tableAlias + ".id>=" + minValue + " and " + tableAlias + ".id<" + offset;
        for (int i = 1; i < PARTITION_NUMBER; i++) {
            if (i == PARTITION_NUMBER - 1) {
                predicates[i] = tableAlias + ".id>=" + offset * i + " and " + tableAlias + ".id<=" + maxValue;
                continue;
            }
            predicates[i] = tableAlias + ".id>=" + offset * i + " and " + tableAlias + ".id<" + offset * (i + 1);
        }

        return predicates;
    }

    protected String[] initPredicates(String table, String financialContractUuid) {
        log.info("sql is:{}", "select min(id) AS min,max(id) AS max from " +
                table + " where financial_contract_uuid='" + financialContractUuid + "'");
        String sql = SQLUtils.wrapperSQL("select min(id) AS min,max(id) AS max from " +
                table + " where financial_contract_uuid='" + financialContractUuid + "'");

        List<Row> rows = loadDataFromTable(sql).collectAsList();
        if (rows.get(0).get(0) == null) {
            log.info("data is empty");
            return new String[]{};
        }
        long minValue = Long.parseLong(rows.get(0).get(0).toString());
        long maxValue = Long.parseLong(rows.get(0).get(1).toString());

        if (minValue == maxValue) {
            return new String[]{"id=" + minValue};
        }
        // 一个条件表示一个分区
        String[] predicates = new String[PARTITION_NUMBER];

        long offset = (maxValue - minValue) / PARTITION_NUMBER;
        log.info("\n\nminValue is:{}\n\nmaxValue is:{}\n\noffset is:{}\n\n", minValue, maxValue, offset);
        for (int i = 0; i < PARTITION_NUMBER; i++) {
            if (i == PARTITION_NUMBER - 1) {
                predicates[i] = "id>=" + (minValue + offset * i) + " and id<=" + maxValue;
                continue;
            }
            predicates[i] = "id>=" + (minValue + offset * i) + " and id<" + (minValue + offset * (i + 1));
        }

        return predicates;
    }

    protected String[] initPredicates(String table, String financialContractUuid, String caluseColumnName) {
        String sql = SQLUtils.wrapperSQL("select min(id) AS min,max(id) AS max from " +
                table + " where " + caluseColumnName + "='" + financialContractUuid + "'");

        List<Row> rows = loadDataFromTable(sql).collectAsList();
        if (rows.get(0).get(0) == null) {
            log.info("data is empty");
            return new String[]{};
        }
        long minValue = Long.parseLong(rows.get(0).get(0).toString());
        long maxValue = Long.parseLong(rows.get(0).get(1).toString());

        // 一个条件表示一个分区
        String[] predicates = new String[PARTITION_NUMBER];

        long offset = (maxValue - minValue) / PARTITION_NUMBER;
        log.info("\n\nminValue is:{}\n\nmaxValue is:{}\n\noffset is:{}\n\n", minValue, maxValue, offset);
        for (int i = 0; i < PARTITION_NUMBER; i++) {
            if (i == PARTITION_NUMBER - 1) {
                predicates[i] = "id>=" + (minValue + offset * i) + " and id<=" + maxValue;
                continue;
            }
            predicates[i] = "id>=" + (minValue + offset * i) + " and id<" + (minValue + offset * (i + 1));
        }

        return predicates;
    }

    protected String[] initPredicates(String table, QueryParameter queryParameter) {
        String sql = SQLUtils.wrapperSQL("select min(id) AS min,max(id) AS max from " +
                table + " where ledger_book_no='" + queryParameter.getLedgerBookNO() +
                "' and second_account_uuid='" + queryParameter.getAccountNO() + "'");

        log.info("sql is:{}", sql);
        List<Row> rows = loadDataFromTable(sql).collectAsList();
        if (rows.get(0).get(0) == null) {
            log.info("data is empty");
            return new String[]{};
        }
        long minValue = Long.parseLong(rows.get(0).get(0).toString());
        long maxValue = Long.parseLong(rows.get(0).get(1).toString());

        // 一个条件表示一个分区
        String[] predicates = new String[PARTITION_NUMBER];

        long offset = (maxValue - minValue) / PARTITION_NUMBER;
        log.info("\n\nminValue is:{}\n\nmaxValue is:{}\n\noffset is:{}\n\n", minValue, maxValue, offset);
        for (int i = 0; i < PARTITION_NUMBER; i++) {
            if (i == PARTITION_NUMBER - 1) {
                predicates[i] = "id>=" + (minValue + offset * i) + " and id<=" + maxValue;
                continue;
            }
            predicates[i] = "id>=" + (minValue + offset * i) + " and id<" + (minValue + offset * (i + 1));
        }

        return predicates;
    }

    protected Dataset<Row> loadDataFromTable(String sql) {
        return sparkSession.read().jdbc(sparkProperties.getUrl(), sql, initConnectionProperties());
    }

    protected Dataset<Row> loadDataFromTable(String sql, String[] predicates) {
        if (predicates == null) {
            return sparkSession.read().jdbc(sparkProperties.getUrl(), sql, initConnectionProperties());
        } else {
            return sparkSession.read().jdbc(sparkProperties.getUrl(), sql, predicates, initConnectionProperties());
        }
    }

    private Properties initConnectionProperties() {
        Properties connectionProperties = new Properties();
        connectionProperties.put("driver", "com.mysql.jdbc.Driver");
        connectionProperties.put("user", sparkProperties.getUser());
        connectionProperties.put("password", sparkProperties.getPassword());
        return connectionProperties;
    }

    public void saveData2Table(Dataset<Row> rows, String sql, SaveMode saveMode) {
        rows.write().mode(saveMode).jdbc(sparkProperties.getUrl(), sql, initConnectionProperties());
    }

    /**
     * 合同状态: 0:放款中,1:未生效,2:已生效, 3:异常中止, 4:回购中, 5:已回购 ,6:违约,7:完成
     *
     * @param repurchaseMode 回购模式
     * @param df             数据集
     * @return 过滤结果数据集
     */
    protected Dataset<Row> filterRepurchaseMode(RepurchaseMode repurchaseMode, Dataset<Row> df) {
        switch (repurchaseMode) {
            case M1:
                df.filter("state=2 or state=4 or state=6");
                break;
            case M2:
                df.filter("state=2");
                break;
            default://M3
                df.filter("state=4 or state=5 or state=6");
                break;
        }

        return df.drop(new Column("state"));
    }

    /**
     * 宽限日
     *
     * @param financialContractUuid 信托合同ID
     * @return 宽限日天数
     */
    protected int getGraceDay(String financialContractUuid) {
        Integer repaymentTerm = 0;
        String sql = "SELECT adva_repayment_term AS repaymentTerm" +
                " FROM FINANCIAL_CONTRACT" +
                " WHERE financial_contract_uuid ='" +
                financialContractUuid + "'";

        Dataset<Row> rows = loadDataFromTable(SQLUtils.wrapperSQL(sql));

        if (rows.count() != 0) {
            repaymentTerm = ((Row[]) rows.take(1))[0].getAs("repaymentTerm");
            return repaymentTerm != null ? repaymentTerm : 0;
        }

        return repaymentTerm;
    }
}