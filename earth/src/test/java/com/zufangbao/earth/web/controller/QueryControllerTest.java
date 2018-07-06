package com.zufangbao.earth.web.controller;

import com.zufangbao.earth.yunxin.api.controller.QueryApiController;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dafuchen
 *         2017/12/20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml",
        "classpath:/DispatcherServlet.xml" })
@WebAppConfiguration(value="webapp")
@Slf4j
@Transactional()
public class QueryControllerTest {

    @Autowired
    private QueryApiController queryApiController;

    @Autowired
    HttpServletRequest httpServletRequest;

//    @Autowired
//    HttpServletResponse httpServletResponse;
//
//    @Test
//    public void queryAccountTradeDetailDirectBankTest() {
//        AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//        accountTradeDetailModel.setAccountSide("0");
//        accountTradeDetailModel.setStartTime("2016-01-01 23:59:59");
//        accountTradeDetailModel.setEndTime("2017-12-31 00:00:00");
//        accountTradeDetailModel.setProductCode("HZ110099");
//        accountTradeDetailModel.setCapitalAccountNo("6224080600234");
//        accountTradeDetailModel.setRequestNo(Long.toString(System.currentTimeMillis()));
//        accountTradeDetailModel.setPage(6);
//        accountTradeDetailModel.setPaymentInstitutionName(2);
//        String response = queryApiController.queryAccountTradeDetailDirectBank(accountTradeDetailModel,
//                httpServletRequest, httpServletResponse);
//        log.info(response);
//    }
}
