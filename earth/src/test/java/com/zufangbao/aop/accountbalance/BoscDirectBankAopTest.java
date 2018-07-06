package com.zufangbao.aop.accountbalance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zufangbao.earth.aop.accountbalance.BoscDirectBankAop;
import com.zufangbao.earth.yunxin.exception.QueryBalanceException;
import com.zufangbao.earth.yunxin.handler.CapitalHandler;
import com.zufangbao.sun.entity.account.Account;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml",
        "classpath:/DispatcherServlet.xml" })
@WebAppConfiguration
public class BoscDirectBankAopTest {
    @Autowired
    private CapitalHandler capitalHandler;

    @Autowired
    private BoscDirectBankAop boscDirectBankAop;



    @Test
    public void  queryAccountBalanceTest() {
        Account account = new Account();
        account.setAccountNo("31600700009000356");
        account.setBankCode("BOSC");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("usbUuid", "bd741be1-bafa-46b0-b999-11a4f0558bcc");
        account.setAttr(jsonObject.toJSONString());
        try {
            BigDecimal balance = capitalHandler.queryAccountBalance(account);
            System.out.println(balance);
        } catch (QueryBalanceException e) {
            Assert.fail(e.getMessage());
        }
    }
}
