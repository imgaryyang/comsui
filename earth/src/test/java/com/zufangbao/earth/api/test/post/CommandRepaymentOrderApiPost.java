package com.zufangbao.earth.api.test.post;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zufangbao.cucumber.method.model.BusinessAmountModel;
import com.zufangbao.cucumber.method.model.RepaymentDetailsModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by FanT on 2017/6/20.
 */
public class CommandRepaymentOrderApiPost extends BaseApiTestPost{
    @Test
    public void repaymentOderTest(){
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("orderRequestNo", UUID.randomUUID().toString());
        requestParams.put("orderUniqueId","pufa0000000018");
        requestParams.put("transType","0");
        requestParams.put("financialContractNo","G31700");
        requestParams.put("orderAmount","100080");
//        requestParams.put("orderNotifyUrl","http://101.52.128.166/callback/deduct-callback");
        List<RepaymentDetailsModel> repaymentDetailsModelList=new ArrayList<RepaymentDetailsModel>();
        List<BusinessAmountModel> businessAmountModelList=new ArrayList<BusinessAmountModel>();

            RepaymentDetailsModel repaymentDetailsModel1 = new RepaymentDetailsModel();
            repaymentDetailsModel1.setContractNo("");
            repaymentDetailsModel1.setContractUniqueId("overFD110");
//            repaymentDetailsModel1.setContractNo("妹妹你大胆的往前走701");
            repaymentDetailsModel1.setRepaymentWay(5001l);
            repaymentDetailsModel1.setRepaymentBusinessNo("ZC181805065943891968");
//            repaymentDetailsModel1.setRepayScheduleNo("ZC176093094524207104");
            repaymentDetailsModel1.setPlannedDate("");
            repaymentDetailsModel1.setDetailsTotalAmount(new BigDecimal("100080"));
            BusinessAmountModel businessAmountModel1 = new BusinessAmountModel();
            businessAmountModel1.setFeeType(1000l);//费用类型，2000到2003都为回购费用
            businessAmountModel1.setActualAmount(new BigDecimal("100000"));//金额
            BusinessAmountModel businessAmountModel2 = new BusinessAmountModel();
            businessAmountModel2.setFeeType(1001l);//费用类型，2000到2003都为回购费用
            businessAmountModel2.setActualAmount(new BigDecimal("20"));//金额
            BusinessAmountModel businessAmountModel3 = new BusinessAmountModel();
            businessAmountModel3.setFeeType(1002l);//费用类型，2000到2003都为回购费用
            businessAmountModel3.setActualAmount(new BigDecimal("20"));//金额
            BusinessAmountModel businessAmountModel4 = new BusinessAmountModel();
            businessAmountModel4.setFeeType(1003l);//费用类型，2000到2003都为回购费用
            businessAmountModel4.setActualAmount(new BigDecimal("20"));//金额
            BusinessAmountModel businessAmountModel5 = new BusinessAmountModel();
            businessAmountModel5.setFeeType(1004l);//费用类型，2000到2003都为回购费用
            businessAmountModel5.setActualAmount(new BigDecimal("20"));//金额
//            BusinessAmountModel businessAmountModel6 = new BusinessAmountModel();
//            businessAmountModel6.setFeeType(1005l);//费用类型，2000到2003都为回购费用
//            businessAmountModel6.setActualAmount(new BigDecimal("10"));//金额
//            BusinessAmountModel businessAmountModel7 = new BusinessAmountModel();
//            businessAmountModel7.setFeeType(1006l);//费用类型，2000到2003都为回购费用
//            businessAmountModel7.setActualAmount(new BigDecimal("10"));//金额
//            BusinessAmountModel businessAmountModel8 = new BusinessAmountModel();
//            businessAmountModel8.setFeeType(1007l);//费用类型，2000到2003都为回购费用
//            businessAmountModel8.setActualAmount(new BigDecimal("10"));//金额
//            BusinessAmountModel businessAmountModel9 = new BusinessAmountModel();
//            businessAmountModel9.setFeeType(1008l);//费用类型，2000到2003都为回购费用
//            businessAmountModel9.setActualAmount(new BigDecimal("10"));//金额

            businessAmountModelList.add(businessAmountModel1);
            businessAmountModelList.add(businessAmountModel2);
            businessAmountModelList.add(businessAmountModel3);
            businessAmountModelList.add(businessAmountModel4);
            businessAmountModelList.add(businessAmountModel5);
//            businessAmountModelList.add(businessAmountModel6);
//            businessAmountModelList.add(businessAmountModel7);
//            businessAmountModelList.add(businessAmountModel8);
//            businessAmountModelList.add(businessAmountModel9);

            repaymentDetailsModel1.setDetailsAmountJsonList(businessAmountModelList);
            repaymentDetailsModelList.add(repaymentDetailsModel1);



//            RepaymentDetailsModel repaymentDetailsModel2 = new RepaymentDetailsModel();
//            repaymentDetailsModel2.setContractNo("");
//            repaymentDetailsModel2.setContractUniqueId("overFD85");
//            repaymentDetailsModel2.setRepaymentWay(3002l);
//            repaymentDetailsModel2.setRepaymentBusinessNo("ZC176099788822134784");
////            repaymentDetailsModel2.setPlannedDate("2017-09-14 00:00:00");
//            repaymentDetailsModel2.setDetailsTotalAmount(new BigDecimal("100000"));
//            List<BusinessAmountModel> businessAmountModelList2=new ArrayList<BusinessAmountModel>();
//            BusinessAmountModel businessAmountModel6 = new BusinessAmountModel();
//            businessAmountModel6.setFeeType(1000l);//费用类型，2000到2003都为回购费用
//            businessAmountModel6.setActualAmount(new BigDecimal("100000"));//金额
//            BusinessAmountModel businessAmountModel7 = new BusinessAmountModel();
//            businessAmountModel7.setFeeType(1001l);//费用类型，2000到2003都为回购费用
//            businessAmountModel7.setActualAmount(new BigDecimal("0"));//金额
//            BusinessAmountModel businessAmountModel8 = new BusinessAmountModel();
//            businessAmountModel8.setFeeType(1002l);//费用类型，2000到2003都为回购费用
//            businessAmountModel8.setActualAmount(new BigDecimal("0"));//金额
//            BusinessAmountModel businessAmountModel9 = new BusinessAmountModel();
//            businessAmountModel9.setFeeType(1003l);//费用类型，2000到2003都为回购费用
//            businessAmountModel9.setActualAmount(new BigDecimal("0"));//金额
//            BusinessAmountModel businessAmountModel10 = new BusinessAmountModel();
//            businessAmountModel10.setFeeType(1004l);//费用类型，2000到2003都为回购费用
//            businessAmountModel10.setActualAmount(new BigDecimal("0"));//金额
//            businessAmountModelList2.add(businessAmountModel6);
//            businessAmountModelList2.add(businessAmountModel7);
//            businessAmountModelList2.add(businessAmountModel8);
//            businessAmountModelList2.add(businessAmountModel9);
//            businessAmountModelList2.add(businessAmountModel10);
//            repaymentDetailsModel2.setDetailsAmountJsonList(businessAmountModelList2);
//            repaymentDetailsModelList.add(repaymentDetailsModel2);
////
//        RepaymentDetailsModel repaymentDetailsModel3 = new RepaymentDetailsModel();
//        repaymentDetailsModel3.setContractNo("");
//        repaymentDetailsModel3.setContractUniqueId("overFD26");
//        repaymentDetailsModel3.setRepaymentWay(2001l);
//        repaymentDetailsModel3.setRepaymentBusinessNo("ZC156180756229533696");
////        repaymentDetailsModel3.setPlannedDate("2017-11-22 00:00:00");
//        repaymentDetailsModel3.setDetailsTotalAmount(new BigDecimal("100080"));
//        List<BusinessAmountModel> businessAmountModelList3=new ArrayList<BusinessAmountModel>();
//        BusinessAmountModel businessAmountModel11 = new BusinessAmountModel();
//        businessAmountModel11.setFeeType(1000l);//费用类型，2000到2003都为回购费用
//        businessAmountModel11.setActualAmount(new BigDecimal("100000"));//金额
//        BusinessAmountModel businessAmountModel12 = new BusinessAmountModel();
//        businessAmountModel12.setFeeType(1001l);//费用类型，2000到2003都为回购费用
//        businessAmountModel12.setActualAmount(new BigDecimal("20"));//金额
//        BusinessAmountModel businessAmountModel13 = new BusinessAmountModel();
//        businessAmountModel13.setFeeType(1002l);//费用类型，2000到2003都为回购费用
//        businessAmountModel13.setActualAmount(new BigDecimal("20"));//金额
//        BusinessAmountModel businessAmountModel14 = new BusinessAmountModel();
//        businessAmountModel14.setFeeType(1003l);//费用类型，2000到2003都为回购费用
//        businessAmountModel14.setActualAmount(new BigDecimal("20"));//金额
//        BusinessAmountModel businessAmountModel15 = new BusinessAmountModel();
//        businessAmountModel15.setFeeType(1004l);//费用类型，2000到2003都为回购费用
//        businessAmountModel15.setActualAmount(new BigDecimal("20"));//金额
//        businessAmountModelList3.add(businessAmountModel11);
//        businessAmountModelList3.add(businessAmountModel12);
//        businessAmountModelList3.add(businessAmountModel13);
//        businessAmountModelList3.add(businessAmountModel14);
//        businessAmountModelList3.add(businessAmountModel15);
//        repaymentDetailsModel3.setDetailsAmountJsonList(businessAmountModelList3);
//        repaymentDetailsModelList.add(repaymentDetailsModel3);

            String s= JSON.toJSONString(repaymentDetailsModelList, SerializerFeature.DisableCircularReferenceDetect);

            requestParams.put("repaymentOrderDetail",s);




        try {
            String sr = PostTestUtil.sendPost(REPAYMENTORDER, requestParams, getIdentityInfoMap(requestParams));//192.168.0.204
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
