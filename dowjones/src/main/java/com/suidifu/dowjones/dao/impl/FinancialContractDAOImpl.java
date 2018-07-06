package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.dao.FinancialContractDAO;
import com.suidifu.dowjones.utils.GenericJdbcSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @author veda
 * @date 28/12/2017
 */
@Component("financialContractDAO")
public class FinancialContractDAOImpl implements FinancialContractDAO {

    @Autowired
    private GenericJdbcSupport genericDaoSupport;

    @Override
    public List<String> getAllFinancialContractUuid() {
        String sql = "select financial_contract_uuid from financial_contract";
        return genericDaoSupport.queryForSingleColumnList(sql, new HashMap<>(), String.class);
    }
}
