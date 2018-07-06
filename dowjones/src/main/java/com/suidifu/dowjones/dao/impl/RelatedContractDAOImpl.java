package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.dao.RelatedContractDAO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/6 <br>
 * @time: 下午10:30 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Repository
@Slf4j
public class RelatedContractDAOImpl extends BaseDAO implements RelatedContractDAO, Serializable {

    @Override
    public Dataset<Row> getRelatedContract(String idNumber) {
        if (StringUtils.isBlank(idNumber)) {
            return null;
        }

        String sql = "(SELECT " +
                "con.id                     AS id," +
                "con.contract_no            AS contractNo," +
                "con.interest_rate          AS interestRate," +
                "con.begin_date             AS beginDate," +
                "con.end_date               AS endDate," +
                "con.periods                AS periods," +
                "con.payment_frequency      AS paymentFrequency," +
                "cu.name                    AS customerName," +
                "con.total_amount           AS totalAmount," +
                "con.state                  AS state," +
                "fc.contract_name           AS financialContractName," +
                "fc.financial_contract_uuid AS financialContractUuid," +
                "con.create_time AS createTime," +
                //"va.virtual_account_no      AS virtualAccountNo," +
                //"va.virtual_account_uuid    AS virtualAccountUuid," +
                "cu.source                  AS customerNo" +
                " FROM  customer cu" +
                //" INNER JOIN  virtual_account va ON cu.customer_uuid = va.owner_uuid" +
                " INNER JOIN contract con ON cu.id = con.customer_id" +
                " INNER JOIN FINANCIAL_CONTRACT fc ON con.financial_contract_uuid = fc.financial_contract_uuid" +
                " WHERE 1 = 1" +
                //" AND cu.account='" + idNumber + "'" +
                " AND cu.customer_type = 0" +
                ") temp";

        return loadDataFromTable(sql, initPredicates("contract"));
    }
}