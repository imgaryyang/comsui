package com.zufangbao.earth.api.test.post;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.cucumber.method.model.RepurchaseDetailModel;
import com.zufangbao.sun.utils.DateUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static com.zufangbao.earth.api.test.post.BaseApiTestPost.REPURCHASE;

/**
 * Created by FanT on 2017/6/20.
 */
public class CommandRepurchaseApiPost {
    @Test
    public void testRepurchase() {
        for (int i = 0; i < 1; i++) {
//            String a = i + "";
//            String uniqueId = "造数据" + a;
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("requestNo", UUID.randomUUID().toString());
            requestParams.put("batchNo", UUID.randomUUID().toString());//选填
            requestParams.put("transactionType", "0");//交易类型，0提交，1撤销
            requestParams.put("financialContractNo", "CS0001");//信托合同代码,信托合同需支持回购
            requestParams.put("reviewer", "fanteng");//审核人
            requestParams.put("reviewTimeString", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));//审核时间

            List<RepurchaseDetailModel> list = new ArrayList<>();
            RepurchaseDetailModel repurchaseDetailModel = new RepurchaseDetailModel();
            repurchaseDetailModel.setUniqueId("overFD60");
            repurchaseDetailModel.setContractNo("");//选填
            repurchaseDetailModel.setPrincipal(new BigDecimal("301000"));//选填
            repurchaseDetailModel.setInterest(new BigDecimal("120"));//选填
            repurchaseDetailModel.setPenaltyInterest(new BigDecimal("120"));//选填
            repurchaseDetailModel.setRepurchaseOtherFee(new BigDecimal("120"));//选填
            repurchaseDetailModel.setAmount(new BigDecimal("301360"));//选填

            list.add(repurchaseDetailModel);
            String repurchaseDetail = JsonUtils.toJsonString(list);
            requestParams.put("repurchaseDetail", repurchaseDetail);
            try {
                String sr = PostTestUtil.sendPost(REPURCHASE, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
                System.out.println(sr);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
