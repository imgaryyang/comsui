package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.dao.OverdueAnalyzeDAO;
import com.suidifu.dowjones.vo.enumeration.RepurchaseMode;
import com.suidifu.dowjones.vo.request.InputParameter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

//now() = 2017-11-30
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class ContractReportServiceTest {

    @Autowired
    private OverdueAnalyzeDAO overdueAnalyzeDAO;

    private InputParameter buildInputParameter(String financialContractUuid, String overdueStage, boolean includeUnconfirmed, boolean includeRepurchase, int periodDays) {
        InputParameter inputParameter = new InputParameter();
        inputParameter.setFinancialContractUuid(financialContractUuid);
        inputParameter.setOverdueStage(overdueStage);
        inputParameter.setIncludeUnconfirmed(includeUnconfirmed);
        inputParameter.setIncludeRepurchase(includeRepurchase);
        inputParameter.setPeriodDays(periodDays);
        return inputParameter;
    }

    private void assertArray(String[] a, String... args) {
        Arrays.sort(a);
        Arrays.sort(args);
        Assert.assertTrue(Arrays.equals(a, args));
    }

    //121_1_1.逾期未偿 已逾期
    @Test
    @Sql("classpath:test/contract_report_dao_impl_test_ww1.sql")
    public void testx1_1() {
        InputParameter inputParameter = buildInputParameter("d2812bc5-5057-4a91-b3fd-9019506f0499", "0", false, true, 30);
        String[] uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertArray(uuids, "008634e0-9ec1-4272-ba4d-e7458fabe5b7");
    }

    //121_1_2.逾期未偿 待确认
    @Test
    @Sql("classpath:test/contract_report_dao_impl_test_ww1.sql")
    public void testx1_2() {
        InputParameter inputParameter = buildInputParameter("d2812bc5-5057-4a91-b3fd-9019506f0499", "0", false, true, 30);
        String[] uuids = overdueAnalyzeDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        assertArray(uuids, "008634e0-9ec1-4272-ba4d-e7458fabe5b7");
    }

}
