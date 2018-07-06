package com.zufangbao.earth.yunxin.handler;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.datasync.BusinessLogsModel;
import com.zufangbao.sun.yunxin.entity.datasync.OperateMode;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wq on 17-5-23.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class RepaymentPlanHandlerTest {
    @Autowired
    private RepaymentPlanHandler repaymentPlanHandler;


    @Autowired
    private ContractService contractService;

    @Autowired
    protected GenericDaoSupport genericDaoSupport;
    
    @Test
    @Sql("classpath:test/yunxin/repaymentPlan/test_save_business_log.sql")
    public void test(){
        AssetSet assetSet=genericDaoSupport.load(AssetSet.class,(long)2);
        List<AssetSet> assetSetList = new ArrayList<>();
        assetSetList.add(assetSet);
        Contract contract= contractService.load(Contract.class,(long)2);
        BusinessLogsModel model = new BusinessLogsModel(OperateMode.CHANGE, 1, new Date(), null);
        repaymentPlanHandler.saveBusinessLogs(contract,assetSetList, model);
        model = new BusinessLogsModel(OperateMode.INSERT, null, null, null);
        repaymentPlanHandler.saveBusinessLogs(contract,assetSetList, model);
        model = new BusinessLogsModel(OperateMode.UPDATE, null, new Date(), null);
        repaymentPlanHandler.saveBusinessLogs(contract,assetSetList, model);
        model = new BusinessLogsModel(OperateMode.UPDATE, 1, null, null);
        repaymentPlanHandler.saveBusinessLogs(contract,assetSetList, model);
        model = new BusinessLogsModel(OperateMode.UPDATE, 1, null, 0);
        repaymentPlanHandler.saveBusinessLogs(contract,assetSetList, model);

    }
    
}
