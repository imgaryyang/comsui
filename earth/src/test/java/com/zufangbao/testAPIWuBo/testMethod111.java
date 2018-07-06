package com.zufangbao.testAPIWuBo;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherDetailModel;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherRepayDetailModel;
import com.zufangbao.testAPIWuBo.testAPI.models.BusinessAmountModel;
import com.zufangbao.testAPIWuBo.testAPI.models.RepaymentDetailsModel;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wubo on 17-7-3.
 */
public class testMethod111 extends BaseApiTestPost {
    QueryDeuctInfoTestMethod queryDeuctInfoTestMethod = new QueryDeuctInfoTestMethod();
    public void MakeVOucher(String url, String transactionRequestNo, String uniqueId, String RepaymentPlanNo) {
        List<ThirdPartVoucherDetailModel> models = new ArrayList<ThirdPartVoucherDetailModel>();
        String VoucherNo = UUID.randomUUID().toString();//凭证编号
        for (int i = 1; i <= 1; i++) {
            for (int y = 1; y <= 1; y++) {
                ThirdPartVoucherDetailModel model = new ThirdPartVoucherDetailModel();
                model.setVoucherNo(VoucherNo);//凭证编号:查重（凭证唯一标识，不允许存在两张编号相同的凭证）
                model.setBankTransactionNo(UUID.randomUUID().toString());//交易流水号
                model.setTransactionRequestNo(transactionRequestNo);//交易请求号
                model.setTransactionTime(com.demo2do.core.utils.DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));//交易发起时间
                model.setTransactionGateway(1);//交易网关: 0广银联 1宝付 2民生代扣 3新浪支付 4中金支付 5先锋支付 6易极付
                model.setCurrency(0);//交易币种
                model.setAmount(new BigDecimal("1000"));//交易金额
                model.setContractUniqueId(uniqueId);//贷款合同唯一编号
                model.setCompleteTime(DateUtils.format(new Date()));//交易完成时间
                model.setReceivableAccountNo("459977383");//收款银行帐户号
                model.setPaymentBank("C10105");//来往机构
                model.setPaymentName("hhhhhhhhh");//付款帐户名称
                model.setPaymentAccountNo("6214822712106520");//付款帐户号
                model.setCustomerName("wub");//客户姓名
                model.setCustomerIdNo("526899661151598765");//身份证号码
                model.setComment("第三方凭证");//备注

                ThirdPartVoucherRepayDetailModel detailModel = new ThirdPartVoucherRepayDetailModel();
                detailModel.setRepaymentPlanNo(RepaymentPlanNo);//还款计划编号
                detailModel.setPrincipal(new BigDecimal("1000"));//还款本金
                detailModel.setInterest(BigDecimal.ZERO);//还款利息
                detailModel.setServiceCharge(BigDecimal.ZERO);//贷款服务费
                detailModel.setMaintenanceCharge(BigDecimal.ZERO);//技术维护费
                detailModel.setOtherCharge(BigDecimal.ZERO);//其他费用
                detailModel.setPenaltyFee(BigDecimal.ZERO);//罚息
                detailModel.setLatePenalty(BigDecimal.ZERO);//逾期违约金
                detailModel.setLateFee(BigDecimal.ZERO);//逾期服务费
                detailModel.setLateOtherCost(BigDecimal.ZERO);//逾期其他费用
                detailModel.setAmount(new BigDecimal("1000"));//明细金额总和
                List<ThirdPartVoucherRepayDetailModel> detailModels = new ArrayList<ThirdPartVoucherRepayDetailModel>();
                detailModels.add(detailModel);
                model.setRepayDetailList(detailModels);
                models.add(model);
            }
        }

        String content = com.suidifu.hathaway.util.JsonUtils.toJsonString(models);
        System.out.println(content);
        Map<String, String> params = new HashMap<String, String>();
        params.put("fn", "300006");
        params.put("requestNo", UUID.randomUUID().toString());
        params.put("financialContractNo", "G31700");
        params.put("detailList", content);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("merId", "t_test_zfb");
        headers.put("secret", "123456");
        String signContent = ApiSignUtils.getSignCheckContent(params);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.put("sign", sign);

        try {
            String sr = PostTestUtil.sendPost(url, params, new BaseApiTestPost().getIdentityInfoMap(params));
            System.out.println(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //还款订单
    public void  repaymentOderTest(String uniqueId,String repaymentNumber1,String repaymentNumber2,String orderUniqueId){
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("orderRequestNo", UUID.randomUUID().toString());
        requestParams.put("orderUniqueId",orderUniqueId);
        requestParams.put("transType","0");
        requestParams.put("financialContractNo","WB123");
        requestParams.put("orderAmount","1000");
        List<RepaymentDetailsModel> repaymentDetailsModelList=new ArrayList<RepaymentDetailsModel>();
        for (int i = 0; i < 1; i++) {
        List<BusinessAmountModel> businessAmountModelList=new ArrayList<BusinessAmountModel>();
//            repaymentNumber = queryDeuctInfoTestMethod.query_i_RepaymentPlan(uniqueId,0);
            RepaymentDetailsModel repaymentDetailsModel1 = new RepaymentDetailsModel();
            repaymentDetailsModel1.setContractNo("");
            repaymentDetailsModel1.setContractUniqueId(uniqueId);
            repaymentDetailsModel1.setRepaymentWay(5001l);
            repaymentDetailsModel1.setRepaymentBusinessNo(repaymentNumber1);
//            repaymentDetailsModel1.setRepayScheduleNo("rsn00170");
            repaymentDetailsModel1.setPlannedDate("2017-09-16 00:00:00");
            repaymentDetailsModel1.setDetailsTotalAmount(new BigDecimal("1000"));
            BusinessAmountModel businessAmountModel1 = new BusinessAmountModel();
            businessAmountModel1.setFeeType(1000L);//费用类型，2000到2003都为回购费用
            businessAmountModel1.setActualAmount(new BigDecimal("1000"));//金额
            BusinessAmountModel businessAmountModel2 = new BusinessAmountModel();
            businessAmountModel2.setFeeType(1001L);//费用类型，2000到2003都为回购费用
            businessAmountModel2.setActualAmount(new BigDecimal("0"));//金额
            BusinessAmountModel businessAmountModel3 = new BusinessAmountModel();
            businessAmountModel3.setFeeType(1002L);//费用类型，2000到2003都为回购费用
            businessAmountModel3.setActualAmount(new BigDecimal("0"));//金额
            BusinessAmountModel businessAmountModel4 = new BusinessAmountModel();
            businessAmountModel4.setFeeType(1003L);//费用类型，2000到2003都为回购费用
            businessAmountModel4.setActualAmount(new BigDecimal("0"));//金额
//            BusinessAmountModel businessAmountModel5 = new BusinessAmountModel();
//            businessAmountModel5.setFeeType(1004l);//费用类型，2000到2003都为回购费用
//            businessAmountModel5.setActualAmount(new BigDecimal("20"));//金额
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
//            businessAmountModelList.add(businessAmountModel5);
//            businessAmountModelList.add(businessAmountModel6);
//            businessAmountModelList.add(businessAmountModel7);
//            businessAmountModelList.add(businessAmountModel8);
//            businessAmountModelList.add(businessAmountModel9);

            repaymentDetailsModel1.setDetailsAmountJsonList(businessAmountModelList);
            repaymentDetailsModelList.add(repaymentDetailsModel1);

        }

            RepaymentDetailsModel repaymentDetailsModel2 = new RepaymentDetailsModel();
            repaymentDetailsModel2.setContractNo("");
            repaymentDetailsModel2.setContractUniqueId(uniqueId);
            repaymentDetailsModel2.setRepaymentWay(5001l);
            repaymentDetailsModel2.setRepaymentBusinessNo(repaymentNumber2);
            repaymentDetailsModel2.setPlannedDate("2017-7-11 00:00:00");
            repaymentDetailsModel2.setDetailsTotalAmount(new BigDecimal("100"));
            List<BusinessAmountModel> businessAmountModelList2=new ArrayList<BusinessAmountModel>();
            BusinessAmountModel businessAmountModel6 = new BusinessAmountModel();
            businessAmountModel6.setFeeType(1000L);//费用类型，2000到2003都为回购费用
            businessAmountModel6.setActualAmount(new BigDecimal("100"));//金额
            BusinessAmountModel businessAmountModel7 = new BusinessAmountModel();
            businessAmountModel7.setFeeType(1001L);//费用类型，2000到2003都为回购费用
            businessAmountModel7.setActualAmount(new BigDecimal("0"));//金额
            BusinessAmountModel businessAmountModel8 = new BusinessAmountModel();
            businessAmountModel8.setFeeType(1002L);//费用类型，2000到2003都为回购费用
            businessAmountModel8.setActualAmount(new BigDecimal("0"));//金额
            BusinessAmountModel businessAmountModel9 = new BusinessAmountModel();
            businessAmountModel9.setFeeType(1003L);//费用类型，2000到2003都为回购费用
            businessAmountModel9.setActualAmount(new BigDecimal("0"));//金额
            BusinessAmountModel businessAmountModel10 = new BusinessAmountModel();
//            businessAmountModel10.setFeeType(1004l);//费用类型，2000到2003都为回购费用
//            businessAmountModel10.setActualAmount(new BigDecimal("0"));//金额
            businessAmountModelList2.add(businessAmountModel6);
            businessAmountModelList2.add(businessAmountModel7);
            businessAmountModelList2.add(businessAmountModel8);
            businessAmountModelList2.add(businessAmountModel9);
//            businessAmountModelList2.add(businessAmountModel10);
            repaymentDetailsModel2.setDetailsAmountJsonList(businessAmountModelList2);
//            repaymentDetailsModelList.add(repaymentDetailsModel2);
//
//        RepaymentDetailsModel repaymentDetailsModel3 = new RepaymentDetailsModel();
//        repaymentDetailsModel3.setContractNo("");
//        repaymentDetailsModel3.setContractUniqueId("妹妹你大胆的往前走54");
//        repaymentDetailsModel3.setRepaymentWay(2001l);
//        repaymentDetailsModel3.setRepaymentBusinessNo("ZC82563581342527488");
//        repaymentDetailsModel3.setPlannedDate("2017-7-11 00:00:00");
//        repaymentDetailsModel3.setDetailsTotalAmount(new BigDecimal("980"));
//        List<BusinessAmountModel> businessAmountModelList3=new ArrayList<BusinessAmountModel>();
//        BusinessAmountModel businessAmountModel11 = new BusinessAmountModel();
//        businessAmountModel11.setFeeType(1000l);//费用类型，2000到2003都为回购费用
//        businessAmountModel11.setActualAmount(new BigDecimal("900"));//金额
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
            String sr = PostTestUtil.sendPost("http://contra.5veda.net/api/v2/repaymentOrder", requestParams, getIdentityInfoMap(requestParams));
//            String sr = PostTestUtil.sendPost("http://192.168.0.128:7778/api/v2/repaymentOrder", requestParams, getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //订单支付
    public void testPaymentOrder(String financialContractNo,String tradeUuid,String amount){
        Map<String,String> requestParams = new HashMap<>();
        requestParams.put("requestNo",UUID.randomUUID().toString());
        requestParams.put("paymentNo","1521876665940");
        requestParams.put("orderUniqueId","98c1e477a19e4ebaaa500b57881a296f");
//        requestParams.put("orderUuid",UUID.randomUUID().toString());
        requestParams.put("financialContractNo",financialContractNo);
        requestParams.put("payWay","0");
        requestParams.put("paymentAccountNo","436466811053");
        requestParams.put("paymentAccountName","中国银行上海市蒙自路支行");
        requestParams.put("paymentBankCode","");
        requestParams.put("paymentProvinceCode","");
        requestParams.put("paymentCityCode","");
        requestParams.put("idCardNum","");
        requestParams.put("mobile","");
        requestParams.put("paymentGateWay","");
        requestParams.put("tradeUuid",tradeUuid);
        requestParams.put("amount",amount);
        requestParams.put("transactionTime",DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        requestParams.put("receivableAccountNo","");
        requestParams.put("receivableAccountName","");
        requestParams.put("receivableBankCode","");
        requestParams.put("notifyUrl","");

        System.out.println(JsonUtils.toJsonString(requestParams));
        String sr;
        try {
            sr = PostTestUtil.sendPost("http://contra.5veda.net/api/v2/paymentOrder",requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //订单支付查询
    public void testPaymentOrderQuery(String url,String financialContractNo){
        Map<String,String> requestParams = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("98c1e477a19e4ebaaa500b57881a296f");
        requestParams.put("requestNo",UUID.randomUUID().toString());
        requestParams.put("paymentNoList","");
        requestParams.put("orderUniqueIdList", JsonUtils.toJsonString(list));
        requestParams.put("orderUuidList","");
        requestParams.put("financialContractNo",financialContractNo);
        System.out.println(JsonUtils.toJsonString(requestParams));
        String sr = "";
        try {
            sr = com.zufangbao.earth.api.test.post.PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
