//package com.zufangbao.earth.yunxin.api.controller;
//
//
//import com.zufangbao.earth.yunxin.api.model.query.AccountTradeDetailModel;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * @author penghk
// */
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:/local/applicationContext-*.xml", "classpath:/local/DispatcherServlet.xml"})
//@WebAppConfiguration(value = "webapp")
//public class QueryApiControllerTest {
//
//    @Autowired
//    QueryApiController queryApiController;
//
//    @Test
////    @Sql("classpath:test/yunxin/api/testqueryAccountTradeDetail.sql")
//    public void TestqueryAccountTradeDetailDirectBank() {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//        accountTradeDetailModel.setRequestNo(String.valueOf(System.currentTimeMillis()));
//
////        accountTradeDetailModel.setProductCode("G32000");//contractNo  云南信托普惠7号用钱宝单一资金信托
////        accountTradeDetailModel.setPaymentInstitutionName(7);//
////        accountTradeDetailModel.setStartTime("2017-12-10 00:00:00"); //yyyy-MM-dd HH:mm:SS
////        accountTradeDetailModel.setEndTime("2017-12-13 00:00:00");
//
//        accountTradeDetailModel.setCapitalAccountNo("");
//        accountTradeDetailModel.setProductCode("WB123");//contractNo  云南信托普惠7号用钱宝单一资金信托
//        accountTradeDetailModel.setPaymentInstitutionName(7);//
//        accountTradeDetailModel.setStartTime("2017-12-29 17:40:00"); //yyyy-MM-dd HH:mm:SS
//        accountTradeDetailModel.setEndTime("2017-12-19 17:41:00");
//
//
//        accountTradeDetailModel.setAccountSide("1");
//        accountTradeDetailModel.setPage(1);
//
//        String responseStr = queryApiController.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//        System.out.println(responseStr);
//
//    }
//
//    @Test
//    @Transactional
//    public void TestqueryAccountTradeDetailDirectBank1() {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//        accountTradeDetailModel.setRequestNo(String.valueOf(System.currentTimeMillis()));
//        accountTradeDetailModel.setProductCode("HZ110099");//contractNo  云南信托普惠7号用钱宝单一资金信托
//        accountTradeDetailModel.setPaymentInstitutionName(2);//
//        accountTradeDetailModel.setCapitalAccountNo("6224080600234");//
//        accountTradeDetailModel.setStartTime("2017-12-29 17:53:00"); //yyyy-MM-dd HH:mm:SS
//        accountTradeDetailModel.setEndTime("2017-12-29 17:54:00");
////        accountTradeDetailModel.setAccountSide("1");
//        accountTradeDetailModel.setPage(1);
//
//        String responseStr = queryApiController.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//        System.out.println(responseStr);
//
//    }
//
//}
