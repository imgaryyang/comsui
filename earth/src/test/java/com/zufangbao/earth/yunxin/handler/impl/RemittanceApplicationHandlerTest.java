package com.zufangbao.earth.yunxin.handler.impl;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.wellsfargo.yunxin.handler.RemittancetApplicationHandler;

/**
 * @author 作者 zhenghangbo
 * @version 创建时间：Dec 5, 2016 11:00:07 AM
 * 类说明
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml"})
@Transactional
public class RemittanceApplicationHandlerTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private RemittancetApplicationHandler remittancetApplicationHandler;


    @Test
    @Sql("classpath:test/yunxin/remittanceApplication/testGetSortByCreateDateRemittanceApplication.sql")
    public void testGetSortByCreateDateRemittanceApplication() {
        String contractUniqueId = "547083b0-0c32-42b4-862e-653e85d944fa";
        Date beginingLoanDate = remittancetApplicationHandler.getFirstLoanDateInRemittance(contractUniqueId);

        assertEquals("2016-09-01", DateUtils.format(beginingLoanDate));
    }

    @Test
    @Sql("classpath:test/yunxin/remittanceApplication/testGetAnySuccessRemittance.sql")
    public void testGetAnySuccessRemittance() {
        RemittanceApplication remittanceApplication = remittancetApplicationHandler.getAnySuccessRemittance("547083b0-0c32-42b4-862e-653e85d944fa");
        assertEquals("547083b0-0c32-42b4-862e-653e85d944fa", remittanceApplication.getContractUniqueId());
    }

    @Test
    @Sql("classpath:test/yunxin/remittanceApplication/testGetAnySuccessRemittance.sql")
    public void testGetAnyRemittanceList() {
        List<RemittanceApplication> remittanceApplications = remittancetApplicationHandler.getAnyRemittanceList("547083b0-0c32-42b4-862e-653e85d944fa");
        assertEquals("547083b0-0c32-42b4-862e-653e85d944fa", remittanceApplications.get(0).getContractUniqueId());
    }
}

