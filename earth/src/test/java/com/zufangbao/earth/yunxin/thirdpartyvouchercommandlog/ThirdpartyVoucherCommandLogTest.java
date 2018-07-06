package com.zufangbao.earth.yunxin.thirdpartyvouchercommandlog;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.BaseTest;
import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherDetailModel;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherRepayDetailModel;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartHttpServletRequest;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zj on 17-8-29.
 */

//@Transactional()
//@TransactionConfiguration(defaultRollback = false)
//@WebAppConfiguration(value="webapp")
public class ThirdpartyVoucherCommandLogTest extends BaseTest {



    /**
     * 第三方凭证 接口测试
     */
    @Test
    public void testThirdPartyVoucherCommandLogInsertOrupdate(){
        MockHttpServletRequest request = new MockMultipartHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Map<String, String> map = new HashMap<>();

        map.put("financialContractNo","G31700");
        map.put("fn","300006");
        map.put("RequestNo","ad27d1f3-f455-4f75-a22a-d315a28c44df");


//        ThirdPartVoucherModel model = new ThirdPartVoucherModel();
//        model.setFinancialContractNo("G31700");
//        model.setFn("300006");
//        model.setRequestNo("ad27d1f3-f455-4f75-a22a-d315a28c44df");
        Date date = new Date();
        ThirdPartVoucherDetailModel detailModel=  new ThirdPartVoucherDetailModel();

        detailModel.setAmount(new BigDecimal(200000));
        detailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        detailModel.setComment("213456");
        detailModel.setCompleteTime(date.toString());
        detailModel.setContractUniqueId("TESTDZZ0821_97");
        detailModel.setCreateTime(new Date());
        detailModel.setCurrency(0);
        detailModel.setCustomerIdNo("1234567890-");
        detailModel.setCustomerName("dzz2");
        detailModel.setPaymentAccountNo("23r4t5y6i8o9");
        detailModel.setPaymentBank("dzz9");
        detailModel.setPaymentName("dzz2");
        detailModel.setReceivableAccountNo("12345tyui");
        detailModel.setTransactionGateway(0);
        detailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        detailModel.setTransactionTime("2017-08-29");
        detailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetailModel> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetailModel details = new ThirdPartVoucherRepayDetailModel();
        details.setAmount(new BigDecimal(100000));
//        details.setCurrentPeriod(1);
        details.setInterest(new BigDecimal(0));
        details.setLateFee(new BigDecimal(0));
        details.setLateOtherCost(new BigDecimal(0));
        details.setLatePenalty(new BigDecimal(0));
        details.setMaintenanceCharge(new BigDecimal(0));
        details.setOtherCharge(new BigDecimal(0));
        details.setPenaltyFee(new BigDecimal(0));
        details.setPrincipal(new BigDecimal(100000));
        details.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        details.setServiceCharge(new BigDecimal(0));
        objects.add(details);

        ThirdPartVoucherRepayDetailModel details2 = new ThirdPartVoucherRepayDetailModel();
        details2.setAmount(new BigDecimal(100000));
//        details2.setCurrentPeriod(1);
        details2.setInterest(new BigDecimal(0));
        details2.setLateFee(new BigDecimal(0));
        details2.setLateOtherCost(new BigDecimal(0));
        details2.setLatePenalty(new BigDecimal(0));
        details2.setMaintenanceCharge(new BigDecimal(0));
        details2.setOtherCharge(new BigDecimal(0));
        details2.setPenaltyFee(new BigDecimal(0));
        details2.setPrincipal(new BigDecimal(100000));
        details2.setRepayScheduleNo("TESTDZZ0821_97DZZ0");
        details2.setServiceCharge(new BigDecimal(0));
        objects.add(details2);

        detailModel.setRepayDetailList(objects);

        String detaiString = JsonUtils.toJsonString(detailModel);
//        model.setDetailList(detaiString);
        map.put("detailList",detaiString);
        System.out.println(detaiString);
//        String tooString = commandApiController.thirdPartVoucherCommandModel(request, response, model);
//        Result result = JsonUtils.parse(tooString, Result.class);

        try {
            PostTestUtil.sendPost("http://192.168.0.128/api/command", map, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
