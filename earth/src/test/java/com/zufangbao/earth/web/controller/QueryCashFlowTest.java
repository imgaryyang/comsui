//package com.zufangbao.earth.web.controller;
//
//import com.zufangbao.earth.yunxin.api.controller.QueryApiController;
//import com.zufangbao.earth.yunxin.api.model.query.AccountTradeDetailModel;
//import cucumber.api.java.cs.A;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Date;
//
///**
// * @author dafuchen
// *         2018/1/8
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//        "classpath:/local/applicationContext-*.xml",
//        "classpath:/DispatcherServlet.xml" })
//
//@WebAppConfiguration(value="webapp")
//@Transactional
//public class QueryCashFlowTest {
//    @Autowired
//    private QueryApiController queryApiController;
//    @Autowired
//    private HttpServletRequest httpServletRequest;
//    @Autowired
//    private HttpServletResponse httpServletResponse;
//    @Test
//    public void testCashFlowNot() {
//        AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//        accountTradeDetailModel.setRequestNo(new Date().toString());
//        accountTradeDetailModel.setProductCode("HZ110999");
//        accountTradeDetailModel.setPaymentInstitutionName(2);
//        accountTradeDetailModel.setCapitalAccountNo("31600700009000356");
//        accountTradeDetailModel.setPage(1);
//        accountTradeDetailModel.setAccountSide("0");
//        accountTradeDetailModel.setStartTime("2018-1-8 00:00:00");
//        accountTradeDetailModel.setEndTime("2018-1-9 14:29:35");
//
//        queryApiController.queryAccountTradeDetailDirectBank(accountTradeDetailModel, httpServletRequest, httpServletResponse);
//    }
//
//    @Test
//    public void testCashFlowNotIn() {
//        AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//        accountTradeDetailModel.setRequestNo(new Date().toString());
//        accountTradeDetailModel.setProductCode("HZ110999");
//        accountTradeDetailModel.setPaymentInstitutionName(2);
//        accountTradeDetailModel.setCapitalAccountNo("31600700009000356");
//        accountTradeDetailModel.setPage(3);
//        accountTradeDetailModel.setStartTime("2018-1-8 14:30:33");
//        accountTradeDetailModel.setEndTime("2018-1-9 15:29:35");
//
//        queryApiController.queryAccountTradeDetailDirectBank(accountTradeDetailModel, httpServletRequest, httpServletResponse);
//    }
//}
