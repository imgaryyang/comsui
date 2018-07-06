package com.zufangbao.testAPIWuBo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherDetailModel;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherRepayDetailModel;
import com.zufangbao.testAPIWuBo.testAPI.models.ActivePaymentVoucherDetial;
import com.zufangbao.testAPIWuBo.testAPI.models.ModifyOverDueFeeDetail;
import com.zufangbao.testAPIWuBo.testAPI.models.RepurchaseDetail;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.BusinessPaymentVoucherDetail;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.*;

@ContextConfiguration(locations = {
        "classpath:/context/applicationContext-*.xml",
        "classpath:/local/applicationContext-*.xml" })
public class TestMethod extends BaseApiTestPost {

    private String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

    QueryDeuctInfoTestMethod queryDeuctInfoTestMethod = new QueryDeuctInfoTestMethod();
    PostTestUtilhuaruitest postTestUtilhuaruitest = new PostTestUtilhuaruitest();

    public void changeRepaymentPlan(String uniqueId, String contractNo, String requestReason, String type) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "200001");
        requestParams.put("uniqueID", uniqueId);
        requestParams.put("contractNo", contractNo);
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("requestReason", requestReason);
        //requestParams.put("requestData", "[{'assetRecycleDate':'2017-07-10','assetPrincipal':'1000','assetInterest':'0.00','serviceCharge':'0.00','maintenanceCharge':'0.00','otherCharge':'0.00','assetType':" + type + "},{'assetRecycleDate':'2017-09-10','assetPrincipal':'1000','assetInterest':'0.00','serviceCharge':'0.00','maintenanceCharge':'0.00','otherCharge':'0.00','assetType':" + type + "},{'assetRecycleDate':'2017-10-10','assetPrincipal':'1000','assetInterest':'0.00','serviceCharge':'0.00','maintenanceCharge':'0.00','otherCharge':'0.00','assetType':" + type + "}]");//0 提前 1 正常
        //requestParams.put("requestData", "[{'assetRecycleDate':'2017-12-01','assetPrincipal':'100','assetInterest':'60','serviceCharge':'0','maintenanceCharge':'0','otherCharge':'0','assetType':"+type+"}]");//0 提前 1 正常
        requestParams.put("requestData", "[{'assetRecycleDate':'2018-01-19','assetPrincipal':'2000','repayScheduleNo':'data00009','assetInterest':'0','serviceCharge':'0','maintenanceCharge':'0','otherCharge':'0','assetType':" + type + "}]");//0 提前 1 正常

        try {
            String sr = PostTestUtil.sendPost(URL_MODIFY_REPAYMENT_PLAN, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 费用浮动接口
     *
     * @param amount
     */
    public void floatingCharges(String amount, String uniqueId, String produce, String repaymentPlanNo) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("financialProductCode", produce);
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("contractNo", "");
        requestParams.put("repaymentPlanNo", repaymentPlanNo);//还款计划
        requestParams.put("Details", "[{'feeType':'2','amount':'" + amount + "'},{'feeType':'3','amount':'50'},{'feeType':'4','amount':'50'}]");
        requestParams.put("reasonCode", "1");
        requestParams.put("approver", "wubo");
        requestParams.put("approvedTime", "");
        requestParams.put("comment", "更改完成");
        try {
            String result = PostTestUtil.sendPost(ChangeUrl, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 变更还款信息接口
     *
     * @param url
     * @param uniqueId
     * @param bankAccount
     */
    public void changeRepaymentInfo(String url, String uniqueId, String bankAccount) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "200003");
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("contractNo", "");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("payerName", "测试007");
        requestParams.put("bankCode", "testBank");//银行代码
        requestParams.put("bankAccount", bankAccount);//银行帐号
        requestParams.put("bankName", "中国工商银行");
        requestParams.put("bankProvince", "110000");
        requestParams.put("bankCity", "110100");
//        requestParams.put("repaymentChannel", null);//还款通道
        try {
            String result = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 更新逾期费用明细接口
     *
     * @param modifyUrlTest
     * @param uniqueId
     * @param repaymentNo
     */
    public void updateOverdueDetails(String modifyUrlTest, String uniqueId, String repaymentNo, String overDueFeeCalcDate) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "200005");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        ModifyOverDueFeeDetail modifyOverDueFeeDetail = new ModifyOverDueFeeDetail();
        modifyOverDueFeeDetail.setContractUniqueId(uniqueId);//贷款合同唯一识别编号
        modifyOverDueFeeDetail.setRepayScheduleNo("remhhhh1");//商户还款计划编号
//        modifyOverDueFeeDetail.setRepaymentPlanNo(repaymentNo);//还款计划编号
        modifyOverDueFeeDetail.setOverDueFeeCalcDate(overDueFeeCalcDate);//逾期罚息计算日
        modifyOverDueFeeDetail.setPenaltyFee("100");//罚息
        modifyOverDueFeeDetail.setLatePenalty("10");//逾期违约金
        modifyOverDueFeeDetail.setLateFee("10");//逾期服务费
        modifyOverDueFeeDetail.setLateOtherCost("10");//逾期其他费用
        modifyOverDueFeeDetail.setTotalOverdueFee("130");//逾期费用合计

        ModifyOverDueFeeDetail modifyOverDueFeeDetail1 = new ModifyOverDueFeeDetail();
        modifyOverDueFeeDetail1.setContractUniqueId(uniqueId);//贷款合同唯一识别编号
        modifyOverDueFeeDetail1.setRepayScheduleNo("remhhhh2");//商户还款计划编号
//        modifyOverDueFeeDetail1.setRepaymentPlanNo(repaymentNo);//还款计划编号
        modifyOverDueFeeDetail1.setOverDueFeeCalcDate(overDueFeeCalcDate);//逾期罚息计算日
        modifyOverDueFeeDetail1.setPenaltyFee("101");//罚息
        modifyOverDueFeeDetail1.setLatePenalty("10");//逾期违约金
        modifyOverDueFeeDetail1.setLateFee("10");//逾期服务费
        modifyOverDueFeeDetail1.setLateOtherCost("10");//逾期其他费用
        modifyOverDueFeeDetail1.setTotalOverdueFee("131");//逾期费用合计

        ModifyOverDueFeeDetail modifyOverDueFeeDetail2 = new ModifyOverDueFeeDetail();
        modifyOverDueFeeDetail2.setContractUniqueId(uniqueId);//贷款合同唯一识别编号
        modifyOverDueFeeDetail2.setRepayScheduleNo("remhhhh3");//商户还款计划编号
//        modifyOverDueFeeDetail2.setRepaymentPlanNo(repaymentNo);//还款计划编号
        modifyOverDueFeeDetail2.setOverDueFeeCalcDate(overDueFeeCalcDate);//逾期罚息计算日
        modifyOverDueFeeDetail2.setPenaltyFee("100");//罚息
        modifyOverDueFeeDetail2.setLatePenalty("10");//逾期违约金
        modifyOverDueFeeDetail2.setLateFee("10");//逾期服务费
        modifyOverDueFeeDetail2.setLateOtherCost("10");//逾期其他费用
        modifyOverDueFeeDetail2.setTotalOverdueFee("130");//逾期费用合计

        List<ModifyOverDueFeeDetail> list = new ArrayList<>();
        list.add(modifyOverDueFeeDetail);
        list.add(modifyOverDueFeeDetail1);
        list.add(modifyOverDueFeeDetail2);
        String modifyOverDueFeeDetails = JsonUtils.toJsonString(list);
        requestParams.put("modifyOverDueFeeDetails", modifyOverDueFeeDetails);//逾期费用修改参数
        try {
            String result = PostTestUtil.sendPost(modifyUrlTest, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 商户付款凭证
     *
     * @param url
     * @param uniqueId
     * @param RepaymentPlanNo
     * @param amount
     */
    public void merchantVoucher(String url, String productCode,String uniqueId, String RepaymentPlanNo,String repayScheduleNo, String amount) {
        Map<String, String> requestParams = new HashMap<String, String>();
        String bankTransactionNo = UUID.randomUUID().toString();
        System.out.println("付款银行流水号:"+bankTransactionNo);
//        requestParams.put("fn",null);
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("transactionType","0");
        requestParams.put("voucherType", "0");//凭证类型
        requestParams.put("voucherAmount", amount);//凭证金额
//        requestParams.put("financialContractNo", productCode);//信托产品代码
//        requestParams.put("receivableAccountNo", "600000000001");//收款银行账户号
//        requestParams.put("paymentAccountNo", "1001133419006708197");//付款银行账户号
//        requestParams.put("bankTransactionNo", bankTransactionNo);//付款银行流水号
//        requestParams.put("paymentName", "wb");//付款银行帐户名称
//        requestParams.put("paymentBank", "wb");//付款银行名称

        requestParams.put("financialContractNo", productCode);//信托产品代码
        requestParams.put("receivableAccountNo", "361531531");//收款银行账户号
        requestParams.put("paymentAccountNo", "1001133419006708197");//付款银行账户号
        requestParams.put("bankTransactionNo",bankTransactionNo);//付款银行流水号
        requestParams.put("paymentName", "wb");//付款银行帐户名称
        requestParams.put("paymentBank", "wb");//付款银行名称


        List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
        BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
        detail.setUniqueId(uniqueId);//贷款合同唯一标识
        detail.setRepaymentPlanNo(RepaymentPlanNo);//还款计划编号
        detail.setAmount(new BigDecimal(amount));//金额
        detail.setPayer(1);//付款人,0:贷款人，1:商户
        detail.setPrincipal(new BigDecimal(amount));//还款本金
//        detail.setInterest(new BigDecimal("0"));//还款利息
//        detail.setServiceCharge(new BigDecimal("0"));//贷款服务费
//        detail.setMaintenanceCharge(new BigDecimal("0"));//技术维护费
//        detail.setOtherCharge(new BigDecimal("0"));//其他费用
//        detail.setPenaltyFee(new BigDecimal("0"));//罚息
//        detail.setLatePenalty(new BigDecimal("0"));//逾期违约金
//        detail.setLateFee(new BigDecimal("0"));//逾期服务费
//        detail.setLateOtherCost(new BigDecimal("0"));//逾期其他费用
        detail.setTransactionTime("2018-02-27 22:44:00");//还款时间0001-01-01 00:00:00
        detail.setRepayScheduleNo(repayScheduleNo);
        details.add(detail);

        String jsonString = JsonUtils.toJsonString(details);
        requestParams.put("detail", jsonString);
        System.out.println(jsonString);
        String jsonString1 = JsonUtils.toJsonString(requestParams);
        System.out.println(jsonString1);


        String sr = null;
        try {
            sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sr);
    }

    /**
     * 商户付款凭证 三个还款计划
     *
     * @param url
     * @param uniqueId
     * @param amount
     */
    public void merchantVoucher1(String url, String uniqueId, String totalAmount, String amount) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300003");
        requestParams.put("requestNo", UUID.randomUUID().toString());
//        requestParams.put("transactionType", "0");//交易类型
        requestParams.put("voucherType", "0");//凭证类型 7代偿 3差额划拨
        requestParams.put("voucherAmount", totalAmount);//凭证金额
        requestParams.put("financialContractNo", "WB123");//信托产品代码
        requestParams.put("receivableAccountNo", "831952389159132");//收款银行账户号
        requestParams.put("paymentAccountNo", "1001133419006708197");//付款银行账户号
        requestParams.put("bankTransactionNo", UUID.randomUUID().toString());//付款银行流水号
        requestParams.put("paymentName", "wb");//付款银行帐户名称
        requestParams.put("paymentBank", "wb");//付款银行名称

        String RepaymentPlanNo1 = queryDeuctInfoTestMethod.query_i_RepaymentPlan(uniqueId, 0);
        String RepaymentPlanNo2 = queryDeuctInfoTestMethod.query_i_RepaymentPlan(uniqueId, 1);
        String RepaymentPlanNo3 = queryDeuctInfoTestMethod.query_i_RepaymentPlan(uniqueId, 2);

        List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
        BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
        detail1.setUniqueId(uniqueId);//贷款合同唯一标识
        detail1.setRepaymentPlanNo(RepaymentPlanNo1);//还款计划编号
        detail1.setAmount(new BigDecimal(amount));//金额
        detail1.setPayer(1);//付款人,0:贷款人，1:商户
        detail1.setPrincipal(new BigDecimal(amount));//还款本金
//        detail1.setInterest(new BigDecimal("0"));//还款利息
//        detail1.setServiceCharge(new BigDecimal("0"));//贷款服务费
//        detail1.setMaintenanceCharge(new BigDecimal("0"));//技术维护费
//        detail1.setOtherCharge(new BigDecimal("0"));//其他费用
        detail1.setInterest(new BigDecimal("0.00"));//还款利息
        detail1.setServiceCharge(new BigDecimal("0.00"));//贷款服务费
        detail1.setMaintenanceCharge(new BigDecimal("0.00"));//技术维护费
        detail1.setOtherCharge(new BigDecimal("0.00"));//其他费用
        detail1.setPenaltyFee(new BigDecimal("0"));//罚息
        detail1.setLatePenalty(new BigDecimal("0"));//逾期违约金
        detail1.setLateFee(new BigDecimal("0"));//逾期服务费
        detail1.setLateOtherCost(new BigDecimal("0"));//逾期其他费用
        detail1.setTransactionTime("2017-07-06 00:00:00");//还款时间0001-01-01 00:00:00
        details.add(detail1);

        BusinessPaymentVoucherDetail detail2 = new BusinessPaymentVoucherDetail();
        detail2.setUniqueId(uniqueId);//贷款合同唯一标识
        detail2.setRepaymentPlanNo(RepaymentPlanNo2);//还款计划编号
        detail2.setAmount(new BigDecimal(amount));//金额
        detail2.setPayer(1);//付款人,0:贷款人，1:商户
        detail2.setPrincipal(new BigDecimal(amount));//还款本金
//        detail2.setInterest(new BigDecimal("0"));//还款利息
//        detail2.setServiceCharge(new BigDecimal("0"));//贷款服务费
//        detail2.setMaintenanceCharge(new BigDecimal("0"));//技术维护费
//        detail2.setOtherCharge(new BigDecimal("0"));//其他费用
        detail2.setInterest(new BigDecimal("0.00"));//还款利息
        detail2.setServiceCharge(new BigDecimal("0.00"));//贷款服务费
        detail2.setMaintenanceCharge(new BigDecimal("0.00"));//技术维护费
        detail2.setOtherCharge(new BigDecimal("0.00"));//其他费用
        detail2.setPenaltyFee(new BigDecimal("0"));//罚息
        detail2.setLatePenalty(new BigDecimal("0"));//逾期违约金
        detail2.setLateFee(new BigDecimal("0"));//逾期服务费
        detail2.setLateOtherCost(new BigDecimal("0"));//逾期其他费用
        detail1.setTransactionTime("2017-07-06 00:00:00");//还款时间0001-01-01 00:00:00
        details.add(detail2);

        BusinessPaymentVoucherDetail detail3 = new BusinessPaymentVoucherDetail();
        detail3.setUniqueId(uniqueId);//贷款合同唯一标识
        detail3.setRepaymentPlanNo(RepaymentPlanNo3);//还款计划编号
        detail3.setAmount(new BigDecimal(amount));//金额
        detail3.setPayer(1);//付款人,0:贷款人，1:商户
        detail3.setPrincipal(new BigDecimal(amount));//还款本金
//        detail3.setInterest(new BigDecimal("0"));//还款利息
//        detail3.setServiceCharge(new BigDecimal("0"));//贷款服务费
//        detail3.setMaintenanceCharge(new BigDecimal("0"));//技术维护费
//        detail3.setOtherCharge(new BigDecimal("0"));//其他费用
        detail3.setInterest(new BigDecimal("0.00"));//还款利息
        detail3.setServiceCharge(new BigDecimal("0.00"));//贷款服务费
        detail3.setMaintenanceCharge(new BigDecimal("0.00"));//技术维护费
        detail3.setOtherCharge(new BigDecimal("0.00"));//其他费用
        detail3.setPenaltyFee(new BigDecimal("0"));//罚息
        detail3.setLatePenalty(new BigDecimal("0"));//逾期违约金
        detail3.setLateFee(new BigDecimal("0"));//逾期服务费
        detail3.setLateOtherCost(new BigDecimal("0"));//逾期其他费用
        detail1.setTransactionTime("2017-07-06 00:00:00");//还款时间0001-01-01 00:00:00
        details.add(detail3);

        String jsonString = JsonUtils.toJsonString(details);
        requestParams.put("detail", jsonString);
        System.out.println(jsonString);

        String sr = null;
        try {
            sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sr);
    }

    //﻿商户付款凭证撤销接口
    public void merchantVoucherUndo(String url, String uniqueId, String RepaymentPlanNo, String amount) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("voucherType", "1");//凭证类型
        requestParams.put("voucherAmount", "1000.00");//凭证金额
        requestParams.put("financialContractNo", "WB123");//信托产品代码
        requestParams.put("receivableAccountNo", "831952389159132");//收款银行账户号
        requestParams.put("paymentAccountNo", "1001133419006708197");//付款银行账户号
        requestParams.put("bankTransactionNo","81f8fb5e-740e-4bbf-abc1-9eb3e9c28440");//付款银行流水号
        requestParams.put("paymentName", "wb");//付款银行帐户名称
        requestParams.put("paymentBank", "wb");//付款银行名称

        List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
        BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
        detail.setUniqueId(uniqueId);//贷款合同唯一标识
        detail.setRepaymentPlanNo(RepaymentPlanNo);//还款计划编号
        detail.setAmount(new BigDecimal("1000.00"));//金额
        detail.setPayer(1);//付款人,0:贷款人，1:商户
        detail.setPrincipal(new BigDecimal("1000"));//还款本金
        detail.setInterest(new BigDecimal("0"));//还款利息
        detail.setServiceCharge(new BigDecimal("0"));//贷款服务费
        detail.setMaintenanceCharge(new BigDecimal("0"));//技术维护费
        detail.setOtherCharge(new BigDecimal("0"));//其他费用
        detail.setPenaltyFee(new BigDecimal("0"));//罚息
        detail.setLatePenalty(new BigDecimal("0"));//逾期违约金
        detail.setLateFee(new BigDecimal("0"));//逾期服务费
        detail.setLateOtherCost(new BigDecimal("0"));//逾期其他费用
        detail.setTransactionTime("2017-07-24 00:00:00");//还款时间0001-01-01 00:00:00
        details.add(detail);

        String jsonString = JsonUtils.toJsonString(details);
        requestParams.put("detail", jsonString);
        System.out.println(jsonString);

        String sr = null;
        try {
            sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sr);
    }


    /**
     * 制造第三方凭证,其中只有一个还款计划
     *
     * @param url
     * @param transactionRequestNo
     * @param uniqueId
     * @param RepaymentPlanNo
     * @param amount
     * @return
     */
    public void MakeVoucher(String url, String transactionRequestNo, String uniqueId, String RepaymentPlanNo, String amount, String productNo) {
        List<ThirdPartVoucherDetailModel> models = new ArrayList<ThirdPartVoucherDetailModel>();
        String VoucherNo = UUID.randomUUID().toString();//凭证编号
        for (int i = 1; i <= 1; i++) {
            for (int y = 1; y <= 1; y++) {
                ThirdPartVoucherDetailModel model = new ThirdPartVoucherDetailModel();
                model.setVoucherNo(VoucherNo);//凭证编号:查重（凭证唯一标识，不允许存在两张编号相同的凭证）
                model.setBankTransactionNo(UUID.randomUUID().toString());//交易流水号
                model.setTransactionRequestNo(transactionRequestNo);//交易请求号
                model.setTransactionTime(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));//交易发起时间
                model.setTransactionGateway(1);//交易网关
                model.setCurrency(4);//交易币种
                model.setAmount(new BigDecimal(amount));//交易金额
                model.setContractUniqueId(uniqueId);//贷款合同唯一编号
                model.setCompleteTime("2017-6-30 00:00:00");//交易完成时间
                model.setReceivableAccountNo("12345tyui");//收款银行帐户号
                model.setPaymentBank("安徽银行");//来往机构
                model.setPaymentName("233333");//付款帐户名称
                model.setPaymentAccountNo("54895206934");//付款帐户号
                model.setCustomerName("wubo");//客户姓名
                model.setCustomerIdNo("1234567890-");//身份证号码
                model.setComment("213456");//备注

                ThirdPartVoucherRepayDetailModel detailModel = new ThirdPartVoucherRepayDetailModel();
                detailModel.setRepaymentPlanNo(RepaymentPlanNo);//还款计划编号
                detailModel.setPrincipal(new BigDecimal(amount));//还款本金
//                detailModel.setInterest(BigDecimal.ZERO);//还款利息
//                detailModel.setServiceCharge(new BigDecimal("0"));//贷款服务费
//                detailModel.setMaintenanceCharge(new BigDecimal("0"));//技术维护费
//                detailModel.setOtherCharge(new BigDecimal("0"));//其他费用
                detailModel.setInterest(new BigDecimal("0.00"));//还款利息
                detailModel.setServiceCharge(new BigDecimal("0.00"));//贷款服务费
                detailModel.setMaintenanceCharge(new BigDecimal("0.00"));//技术维护费
                detailModel.setOtherCharge(new BigDecimal("0.00"));//其他费用
                detailModel.setPenaltyFee(BigDecimal.ZERO);//罚息
                detailModel.setLatePenalty(BigDecimal.ZERO);//逾期违约金
                detailModel.setLateFee(BigDecimal.ZERO);//逾期服务费
                detailModel.setLateOtherCost(BigDecimal.ZERO);//逾期其他费用
                detailModel.setAmount(new BigDecimal(amount));//明细金额总和
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
        params.put("financialContractNo", productNo);
        params.put("detailList", content);
        try {
            String sr = PostTestUtil.sendPost(url, params, new BaseApiTestPost().getIdentityInfoMap(params));
            System.out.println(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testActivePaymentVoucher(String url, String requestNo, String productNo, String voucherAmount, String uniqueId, String principal) {

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("fn", "300004");//功能代码
        requestParams.put("requestNo", requestNo);//请求唯一标识
        requestParams.put("transactionType", "0");//交易类型 0 提交 1 撤销
        requestParams.put("voucherType", "1");//凭证类型 5 主动付款 6他人代付
        requestParams.put("voucherAmount", voucherAmount);//凭证金额
        requestParams.put("receivableAccountNo", "831952389159132");//收款方银行账户号
        requestParams.put("paymentAccountNo", "6214855712106520");//付款银行帐户号
        requestParams.put("bankTransactionNo", "8f3c8a11-1b85-11e8-a60f-5254008f1409");//付款银行流水号
        requestParams.put("paymentName", "王宝");//付款银行帐户名称
        requestParams.put("paymentBank", "招商招商银行");//付款银行名称
        requestParams.put("financialContractNo", productNo);//信托产品代码

        List<ActivePaymentVoucherDetial> detials = new ArrayList<>();//当前凭证付款明细

        ActivePaymentVoucherDetial activePaymentVoucherDetial = new ActivePaymentVoucherDetial();
        String repayPlanNo = queryDeuctInfoTestMethod.query_i_RepaymentPlan(uniqueId, 0);
        activePaymentVoucherDetial.setRepaymentPlanNo(repayPlanNo);//还款计划编号
        activePaymentVoucherDetial.setAmount(new BigDecimal(voucherAmount));//金额
        activePaymentVoucherDetial.setPrincipal(new BigDecimal(principal));//还款本金
        activePaymentVoucherDetial.setPrincipal(new BigDecimal("0"));//还款利息
        activePaymentVoucherDetial.setServiceCharge(new BigDecimal("0"));//贷款服务费
        activePaymentVoucherDetial.setMainternanceCharge(new BigDecimal("0"));//技术维护费
        activePaymentVoucherDetial.setOtherCharge(new BigDecimal("0"));//其他费用
        activePaymentVoucherDetial.setPenaltyFee(new BigDecimal("0"));//罚息
        activePaymentVoucherDetial.setLatePenalty(new BigDecimal("0"));//逾期违约金
        activePaymentVoucherDetial.setLateFee(new BigDecimal("0"));//逾期服务费
        activePaymentVoucherDetial.setLateOtherCost(new BigDecimal("0"));//逾期其他费用
        activePaymentVoucherDetial.setUniqueId(uniqueId);//贷款合同唯一编号（与contractNo选填一个）
        activePaymentVoucherDetial.setContractNo(uniqueId);//贷款合同编号（与uniqueId选填一个）

        detials.add(activePaymentVoucherDetial);
        String jsonString = JsonUtils.toJsonString(detials);
        requestParams.put("detail", jsonString);

        try {
            String result = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testThirdVoucher(String url, String voucherNo, String contractUniqueId, String amount, String principal, int transactionGateway, String repaymentPlanNo) {
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";
        List<ThirdPartVoucherDetailModel> models = new ArrayList<ThirdPartVoucherDetailModel>();
        for (int i = 1; i <= 1; i++) {
            for (int y = 1; y <= 1; y++) {
                ThirdPartVoucherDetailModel model = new ThirdPartVoucherDetailModel();
                model.setVoucherNo(voucherNo);
                model.setBankTransactionNo("123456");
                model.setTransactionRequestNo("20170504121651208");
                model.setTransactionTime(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                model.setTransactionGateway(transactionGateway);//0广银联  1宝付    2民生代扣 3新浪支付 4中金支付  5 先锋支付
                model.setCurrency(0);
                model.setAmount(new BigDecimal("1000"));
                model.setContractUniqueId(contractUniqueId);
                model.setReceivableAccountNo("12345tyui");
                model.setPaymentBank("23erhy");
                model.setPaymentName("qwertyu");
                model.setPaymentAccountNo("23r4t5y6i8o9");
                model.setCustomerName("1234567890");
                model.setCustomerIdNo("1234567890-");
                model.setComment("213456");
                model.setCompleteTime(DateUtils.format(new Date()));

                ThirdPartVoucherRepayDetailModel detailModel = new ThirdPartVoucherRepayDetailModel();
                detailModel.setAmount(new BigDecimal(amount));
                detailModel.setInterest(new BigDecimal("0"));
                detailModel.setLateFee(BigDecimal.ZERO);
                detailModel.setLateOtherCost(BigDecimal.ZERO);
                detailModel.setLatePenalty(BigDecimal.ZERO);
                detailModel.setMaintenanceCharge(new BigDecimal("0"));
                detailModel.setOtherCharge(new BigDecimal("0"));
                detailModel.setPenaltyFee(BigDecimal.ZERO);
                detailModel.setPrincipal(new BigDecimal(principal));
                detailModel.setRepaymentPlanNo(repaymentPlanNo);
                detailModel.setServiceCharge(new BigDecimal("0"));
                List<ThirdPartVoucherRepayDetailModel> detailModels = new ArrayList<ThirdPartVoucherRepayDetailModel>();
                detailModels.add(detailModel);
                model.setRepayDetailList(detailModels);
                models.add(model);
            }
        }
        String content = JsonUtils.toJsonString(models);
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
            String sr = PostTestUtil.sendPost(url, params, headers);
            System.out.println(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 二次放款回调
     */
    @Test
    public void testApiCommandRemittanceFF() {
        String int_url = "http://192.168.0.128:18085/api/command";
        String out_url = "http://remittance.5veda.net/api/command";
        String local_url = "http://192.168.1.147:9092/api/command";
        String uniqueId = UUID.randomUUID().toString();
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300002");
        requestParams.put("requestNo", "wwwbbb123456700000056");//每次请求都需要变动
        requestParams.put("remittanceStrategy", "0");
        requestParams.put("productCode", "WB123");
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("contractNo", uniqueId);
        requestParams.put("plannedRemittanceAmount", "3100");
        requestParams.put("auditorName", "auditorName1");
        requestParams.put("auditTime", "2016-08-20 00:00:00");
        requestParams.put("remark", "交易备注");
        requestParams.put("notifyUrl","http://101.52.128.166/Loan/BatchPaidNotic");
        requestParams.put("remittanceDetails",
                "[{'detailNo':'detailNo2','recordSn':'1','amount':'1500','bankCode':'C10310','bankCardNo':'6217920681962200','bankAccountHolder':'浦发1550343942','bankProvince':'bankProvince2','bankCity':'bankCity2','bankName':'bankName2','idNumber':'idNumber2'}"
            + ",{'detailNo':'detailNo3','recordSn':'3','amount':'1600','bankCode':'C10310','bankCardNo':'6217920681962200','bankAccountHolder':'测试用户2','bankProvince':'bankProvince2','bankCity':'bankCity2','bankName':'bankName2','idNumber':'idNumber2'}"
//            + ",{'detailNo':'detailNo4','recordSn':'3','amount':'49','bankCode':'C10102','bankCardNo':'123456781','bankAccountHolder':'测试用户2','bankProvince':'bankProvince2','bankCity':'bankCity2','bankName':'bankName2','idNumber':'idNumber2'}"
            + "]");
        try {
            String sr = PostTestUtil.sendPost(int_url, requestParams, getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testModifyRemittanceApplication() {
        String int_url = "http://192.168.0.128:18084/api/v2/modify-remittanceApplication";
        String out_url = "http://remittance.5veda.net/api/v2/modify-remittanceApplication";
        String local_url = "http://192.168.1.147:9092/api/v2/modify-remittanceApplication";
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("tradeNo", UUID.randomUUID().toString());
        requestParams.put("financialProductCode", "WB123");
        requestParams.put("remittanceTradeNo", "wwwbbb123456700000056");
        requestParams.put("approver", "auditorName1");
        requestParams.put("approvedTime", "2016-08-20 17:34:33");
        requestParams.put("comment", "ercifk");
        requestParams.put("remittanceDetails",
                "[{'detailNo':'detailNo1','recordSn':'1','amount':'1500','bankCode':'C10102','bankCardNo':'5685968545868860','bankAccountHolder':'汪水','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}" +
//                        "{'detailNo':'detailNo2','recordSn':'2','amount':'800','bankCode':'C10102','bankCardNo':'5685968545868861','bankAccountHolder':'汪水','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}"+
            "]");
            try {
            String sr = PostTestUtil.sendPost(int_url, requestParams, getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 二次放款回调-中航
     */
    @Test
    public void testApiCommandRemittanceZH() {
        String int_url = "http://192.168.0.179:8084/api/command";
        String out_url = "http://remittance.5veda.net/api/command";
        String zhonghang_out_url = "http://avictc.5veda.net:8886/pre/api/remittance/zhonghang/remittance-application";
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300002");
        requestParams.put("requestNo", UUID.randomUUID().toString());//每次请求都需要变动
        requestParams.put("remittanceId",UUID.randomUUID().toString());
        requestParams.put("remittanceStrategy", "0");
        requestParams.put("productCode", "11111111");
        requestParams.put("uniqueId", UUID.randomUUID().toString());
        requestParams.put("contractNo", UUID.randomUUID().toString());
//        requestParams.put("contractAmount","3.33");
        requestParams.put("plannedRemittanceAmount", "3.33");
//        requestParams.put("clearingAccount","");
        requestParams.put("auditorName", "auditorName1");
        requestParams.put("auditTime", "2016-08-20 00:00:00");
        requestParams.put("remark", "交易备注");
        requestParams.put("notifyUrl","www.baidu.com");
        requestParams.put("remittanceDetails",
                "[{'detailNo':'detailNo1','recordSn':'1','amount':'1.11','bankAliasName':'ICBC','bankCardNo':'5685968545868850','bankAccountHolder':'汪水','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'','idNumber':'idNumber1'}"
                        + ",{'detailNo':'detailNo2','recordSn':'1','amount':'22222','bankAliasName':'ICBC','bankCardNo':'123456781','bankAccountHolder':'测试用户2','bankProvince':'bankProvince2','bankCity':'bankCity2','bankName':'','idNumber':'idNumber2'}"
//				+ ",{'detailNo':'detailNo3','recordSn':'3','amount':'1.11','bankCode':'C10102','bankCardNo':'5685968545868856','bankAccountHolder':'测试用户2','bankProvince':'bankProvince2','bankCity':'bankCity2','bankName':'bankName2','idNumber':'idNumber2'}"
                        + "]");
        try {
            String sr = PostTestUtil.sendPost(zhonghang_out_url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testModifyRemittanceApplicationZH() {
        String int_url = "http://192.168.0.128:8084/api/v2/modify-remittanceApplication";
        String out_url = "http://avictc.5veda.net:8886/api/v2/modify-remittanceApplication";
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("tradeNo", UUID.randomUUID().toString());
        requestParams.put("financialProductCode", "11111111");
        requestParams.put("remittanceTradeNo", "8153be98-eba3-42ac-9ddb-33e237310c34");
        requestParams.put("approver", "auditorName1");
        requestParams.put("approvedTime", "2016-08-20 17:34:33");
        requestParams.put("comment", "ercifk");
        requestParams.put("remittanceDetails",
                "[{'detailNo':'detailNo1','recordSn':'1','amount':'22222','bankCode':'C10102','bankCardNo':'5685968545868860','bankAccountHolder':'汪水','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'','idNumber':'idNumber1'}" +
//                            + ",{'detailNo':5veda'detailNo2','recordSn':'2','amount':'1.11','bankCode':'C10102','bankCardNo':'56859685458688','bankAccountHolder':'汪水','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}" +
                        "]");
        try {
            String sr = PostTestUtil.sendPost(out_url, requestParams, getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * 中航-云信 查询扣款接口
     *
     * @param url
     */
    public void queryDeductZH(String url, String uniqueId, String deductId) {
        Map<String, String> requestParams = new HashMap<String, String>();
        Map<String, String> map = new HashMap<>();
        requestParams.put("fn", "100002");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("deductId", deductId);
        requestParams.put("uniqueId", uniqueId);
        if (url.equals("http://192.168.0.128:38083/api/query/deduct_status") || url.equals("http://avictctest.5veda.net:8887/api/query/deduct_status")) {
            requestParams.put("merId", "t_test_zfb");
            requestParams.put("secret", "123456");
            map = new BaseApiTestPost().getIdentityInfoMapForZHONGHANG(requestParams);
        } else {
            map = new BaseApiTestPost().getIdentityInfoMap(requestParams);
        }
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, map);
            System.out.println("响应数据为：" + sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 中航-云信专户实时余额查询接口
     */
    public void accountRealTime(String url, String productCode, String accountNo) {
        Map<String, String> params = new HashMap<>();
        Map<String, String> map = new HashMap<>();

        params.put("requestNo", UUID.randomUUID().toString());
        params.put("productCode", productCode);
        params.put("accountNo", accountNo);//专户帐号，是否与信托产品代码对应
        params.put("fn", "100010");
        if (url.equals("http://avictctest.5veda.net/api/query") || url.equals("http://192.168.0.128:38081/api/query")) {
            map = new BaseApiTestPost().getIdentityInfoMapForZHONGHANG(params);
        } else {
            map = new BaseApiTestPost().getIdentityInfoMap(params);
        }
        try {
            String sr = PostTestUtil.sendPost(url, params, map);
            System.out.println("返回内容为：" + sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 中航-云信专户实时流水查询接口
     */
    public void queryCashFlow(String url, String productCode, String accountNo,String page) {
        Map<String, String> params = new HashMap<>();
        Map<String, String> map = new HashMap<>();

        params.put("fn", "100012");
        params.put("requestNo", UUID.randomUUID().toString());
        params.put("productCode", productCode);
        params.put("capitalAccountNo", accountNo);//专户帐号，是否与信托产品代码对应
        params.put("paymentInstitutionName","11");
        params.put("accountSide","1");
        params.put("startTime","2017-12-13 00:00:00");
        params.put("endTime","2018-02-13 23:59:59");
        params.put("page",page);
        if (url.equals("http://avictctest.5veda.net/api/query") || url.equals("http://192.168.0.128:38081/api/query")) {
            map = new BaseApiTestPost().getIdentityInfoMapForZHONGHANG(params);
        } else {
            map = new BaseApiTestPost().getIdentityInfoMap(params);
        }
        try {
            String sr = PostTestUtil.sendPost(url, params, map);
            System.out.println("返回内容为：" + sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 中航-扣款
     *
     * @param url
     * @param financialProductCode
     * @param uniqueId
     * @param amount
     * @param accountName
     * @param accountNo
     */
    public void deductRepaymentPlanZH(String url, String financialProductCode, String uniqueId, String repaymentNumber, String amount, String accountName, String accountNo) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300001");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("deductId", UUID.randomUUID().toString() + "扣款");
        requestParams.put("financialProductCode", financialProductCode);
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("contractNo", uniqueId);
        requestParams.put("apiCalledTime", "2017-06-19");
        requestParams.put("transType", "0");//0:B2C  1:B2B
        requestParams.put("accountName", accountName);//1
        requestParams.put("accountNo", accountNo);//1
        requestParams.put("notifyUrl", "www.baidu.com");
        requestParams.put("amount", amount);
//        requestParams.put("mobile", "13258446545");
        requestParams.put("repaymentType","0");
        requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':'"+amount+"','totalAmount':'"+amount+"','repaymentInterest':0.00,'repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':"+amount+",'techFee':0.00,'overDueFeeDetail':{"
                //requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':'"+repaymentTotalamount+"','repaymentInterest':0.00,'repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':"+repaymentPrincipal+",'techFee':0.00,'overDueFeeDetail':{"
                //			+ "'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':4.00"
                + "'totalOverdueFee':0.00}}]");
        System.out.println("请求参数为：" + com.zufangbao.sun.utils.JsonUtils.toJsonString(requestParams));
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMapForZHONGHANG(requestParams));
            System.out.println("响应结果为:" + sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deductRepaymentPlanZH1(String url, String financialProductCode, String uniqueId, String repaymentNumber, String amount, String accountName, String accountNo) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300001");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("deductId", UUID.randomUUID().toString() + "扣款");
        requestParams.put("financialProductCode", financialProductCode);
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("contractNo", uniqueId);
        requestParams.put("apiCalledTime", "2017-06-19");
        requestParams.put("transType", "0");//0:B2C  1:B2B
        requestParams.put("accountName", accountName);//1
        requestParams.put("accountNo", accountNo);//1
        requestParams.put("notifyUrl", "www.baidu.com");
        requestParams.put("amount", amount);
        requestParams.put("mobile", "13258446545");
        requestParams.put("repaymentType","0");
        requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':'"+amount+"','totalAmount':'"+amount+"','repaymentInterest':0.00,'repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':"+amount+",'techFee':0.00,'overDueFeeDetail':{"
                //requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':'"+repaymentTotalamount+"','repaymentInterest':0.00,'repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':"+repaymentPrincipal+",'techFee':0.00,'overDueFeeDetail':{"
                //			+ "'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':4.00"
                + "'totalOverdueFee':0.00}}]");
        System.out.println("请求参数为：" + com.zufangbao.sun.utils.JsonUtils.toJsonString(requestParams));
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMapForZHONGHANG(requestParams));
            System.out.println("响应结果为:" + sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 中航-批量扣款查询接口
     *
     * @param url
     */

    public void assetPackageBatchQueryZH(String url, List<String> list) {
        Map<String, String> requestParams = new HashMap<String, String>();
        Map<String, String> map = new HashMap<String, String>();
        requestParams.put("fn", "100006");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("deductIdList", "" + JsonUtils.toJsonString(list) + "");
        requestParams.put("repaymentType", "2");
        if (url.equals("http://avictctest.5veda.net:8887/api/query/deduct_status_list") || url.equals("http://192.168.0.128:38083/api/query/deduct_status_list")) {
            map = new BaseApiTestPost().getIdentityInfoMapForZHONGHANG(requestParams);
        } else {
            map = new BaseApiTestPost().getIdentityInfoMap(requestParams);
        }
        try {
            map = new BaseApiTestPost().getIdentityInfoMapForZHONGHANG(requestParams);
            String sr = PostTestUtil.sendPost(url, requestParams, map);
            System.out.println(sr + 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 云信，中航放款
     *
     * @param productCode
     * @param uniqueId
     * @param amount
     */
    public void makeLoanZH(String productCode, String uniqueId, String amount, String url, String remittanceId) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300002"); //1
        requestParams.put("requestNo", UUID.randomUUID().toString());//请求唯一标识  1
        requestParams.put("productCode", productCode);//信托产品代码  1
        requestParams.put("uniqueId", uniqueId);//贷款合同唯一编号  1
        requestParams.put("contractNo", uniqueId);
        requestParams.put("contractAmount", amount);//贷款合同本金  1
        requestParams.put("plannedRemittanceAmount", amount);//计划放款金额  1
        requestParams.put("auditorName", "auditorName1");//审核人  1
        requestParams.put("auditTime", "2016-08-20 00:00:00");//审核时间  1
        requestParams.put("remark", "recordSn");//放款备注  1
        requestParams.put("remittanceStrategy", "0");  //1
        requestParams.put("notifyUrl", "http://www.mocky.io/v2/5185415ba171ea3a00704eed");//放款回调地址


        if (url.equals("http://192.168.0.128:38084/pre/api/remittance/zhonghang/remittance-application") || url.equals("http://avictc.5veda.net:8886/pre/api/remittance/zhonghang/remittance-application")) {
            requestParams.put("remittanceId", remittanceId); //1
            requestParams.put("clearingAccount", null);
            requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'" + amount + "','plannedDate':'','bankAliasName':'ABC_103267075310','bankCardNo':'6228481198120540005','bankAccountHolder':'WUBO','bankName':'','idNumber':'idNumber233'}]");
        } else {
            String bankCardNo = "6214855712106530";//'bankCode':'C10102'     中金bankCardNo = "6214855712106520"  宝付6228480444455553333  广银联尾号是0就都可以 交易方银行卡号
            requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'" + amount + "','bankCode':'C10102','bankCardNo':'" + bankCardNo + "','bankAccountHolder':'尹邦凤','bankProvince':'310000','bankCity':'310100','bankName':'中国农业银行','idNumber':'idNumber1'}]");
        }
        System.out.println("请求参数为：" + com.zufangbao.sun.utils.JsonUtils.toJsonString(requestParams));
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 云信，中航放款
     *
     * @param productCode
     * @param uniqueId
     * @param amount
     */
    public void makeLoanZH1(String productCode, String uniqueId, String amount, String url, String remittanceId) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300002"); //1
        requestParams.put("requestNo", UUID.randomUUID().toString());//请求唯一标识  1
        requestParams.put("productCode", productCode);//信托产品代码//  1
        requestParams.put("uniqueId", uniqueId);//贷款合同唯一编号  1
        requestParams.put("contractNo", uniqueId);
        requestParams.put("contractAmount", amount);//贷款合同本金  1
        requestParams.put("plannedRemittanceAmount", amount);//计划放款金额  1
        requestParams.put("auditorName", "auditorName1");//审核人  1
        requestParams.put("auditTime", "2016-08-20 00:00:00");//审核时间  1
        requestParams.put("remark", "recordSn");//放款备注  1
        requestParams.put("remittanceStrategy", "0");  //1
        requestParams.put("notifyUrl", "http://www.mocky.io/v2/5185415ba171ea3a00704eed");//放款回调地址

        if (url.equals("http://192.168.0.128:38084/pre/api/remittance/zhonghang/remittance-application") || url.equals("http://avictctest.5veda.net:8886/pre/api/remittance/zhonghang/remittance-application")) {
            requestParams.put("remittanceId", remittanceId); //1
            requestParams.put("clearingAccount", null);
            requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'" + amount + "','plannedDate':'','bankAliasName':'ABC_103267075310','bankCardNo':'6228481198120540000','bankAccountHolder':'WUBO','bankName':'','idNumber':'idNumber233'}]");
        } else {
            String bankCardNo = "6214855712106530";//'bankCode':'C10102'     中金bankCardNo = "6214855712106520"  宝付6228480444455553333  广银联尾号是0就都可以 交易方银行卡号
            requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'" + amount + "','bankCode':'C10102','bankCardNo':'" + bankCardNo + "','bankAccountHolder':'尹邦凤','bankProvince':'310000','bankCity':'310100','bankName':'中国农业银行','idNumber':'idNumber1'}]");
        }
        System.out.println("请求参数为：" + com.zufangbao.sun.utils.JsonUtils.toJsonString(requestParams));
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 中航-云信批量放款状态查询接口,fn=100011
     *
     * @param oriRequestNo 原放款请求号
     * @param uniqueId     贷款合同唯一编号
     */
    public void batchLoanStatusQuery(String url, String oriRequestNo, String uniqueId) {
        Map<String, String> params = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        params.put("fn", "100011");
        params.put("requestNo", UUID.randomUUID().toString());
        params.put("remittanceResultBatchQueryModels", "[{\"oriRequestNo\":\"" + oriRequestNo + "\",\"uniqueId\":\"" + uniqueId + "\"}]");
        if (url.equals("http://avictctest.5veda.net:8886/api/query/remittance_status_list") || url.equals("http://192.168.0.128:38084/api/query/remittance_status_list")) {
            params.put("merId", "t_test_zfb");
            params.put("secret", "123456");
            map = new BaseApiTestPost().getIdentityInfoMapForZHONGHANG(params);
        } else {
            map = new BaseApiTestPost().getIdentityInfoMap(params);

        }
        try {
            String sr = PostTestUtil.sendPost(url, params, map);
            System.out.println("返回内容为：" + sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //中航-签名
    public void testSignUp(String proNo,String opType,String accNo,String tranMaxAmt,String bankAliasName) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("tranType", "27");
        requestParams.put("opType", opType);//操作类型 签约 解约
        requestParams.put("proNo", proNo);
        requestParams.put("productCode", "WB123");
        requestParams.put("bankAliasName", bankAliasName);
//        requestParams.put("bankFullName","中国工商银行股ss份有限公司北京中石化大厦支行");
        requestParams.put("payType", "B2C");
        requestParams.put("accName", "王宝");
        requestParams.put("accNo", accNo);
        requestParams.put("certType", "1");
        requestParams.put("certId", "340862189009097870");
        requestParams.put("phoneNo", "18069847720");
        requestParams.put("proBeg", "20170612");//05201712
        requestParams.put("proEnd", "20180612");//20191019
        requestParams.put("tranMaxAmt", tranMaxAmt);
        requestParams.put("remark", "123321");
        requestParams.put("merId", "systemdeduct");
        requestParams.put("secret", "628c8b28bb6fdf5c5add6f18da47f1a6");
//        requestParams.put("paymentInstitution", "11");//支付通道
        System.out.println(JsonUtils.toJsonString(requestParams));


        try {
            String sr = PostTestUtil.sendPost("http://avictctest.5veda.net/pre/api/zhonghang/zhonghang/sign-up", requestParams, getIdentityInfoMap(requestParams)); //http://avictctest.5veda.net:8888/pre/api/sign-up
//            String sr = PostTestUtil.sendPost("http://192.168.0.116:9090/pre/api/zhonghang/zhonghang/sign-up", requestParams, getIdentityInfoMap(requestParams)); //http://avictctest.5veda.net:8888/pre/api/sign-up

            // avictctest.5veda.net
            System.out.println(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //中航-签约查询
    public void testQuerySignUp(String proNo,String accNo) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("tranType", "02");
        requestParams.put("proNo", proNo);
        requestParams.put("financialCode", "WB123");
        requestParams.put("accName", "ops");
        requestParams.put("accNo", accNo);
        requestParams.put("certType", "1");
        requestParams.put("certId", "340862189009097868");
        requestParams.put("remark", "123321");
        requestParams.put("merId", "systemdeduct");
        requestParams.put("secret", "628c8b28bb6fdf5c5add6f18da47f1a6");
        requestParams.put("paymentInstitution", "11");//支付通道
        System.out.println(JsonUtils.toJsonString(requestParams));


        try {
            String sr = PostTestUtil.sendPost("http://avictctest.5veda.net/pre/api/sign_up/query", requestParams, getIdentityInfoMap(requestParams)); //http://avictctest.5veda.net:8888/pre/api/sign-up
//            String sr = PostTestUtil.sendPost("http://192.168.0.116:9090/pre/api/zhonghang/zhonghang/sign-up", requestParams, getIdentityInfoMap(requestParams)); //http://avictctest.5veda.net:8888/pre/api/sign-up

            // avictctest.5veda.net
            System.out.println(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //回购申请测试方法-单条-明细
    public void testRepurchase(String url, String requestNo, String batchNo, String productCode, String uniqueId, String principal, String amount, String transactionType) {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("requestNo", requestNo);
        requestParams.put("batchNo", "ab16a7d7-d691-49eb-bc4e-b1b6569de3c1");
//        requestParams.put("batchNo", batchNo);
        requestParams.put("transactionType", transactionType);//0 提交 1 撤销
        requestParams.put("financialContractNo", productCode);
        requestParams.put("reviewer", "wubo");
        requestParams.put("reviewTimeString", DateUtils.format(new Date()));
        List<RepurchaseDetail> list = new ArrayList<>();
        RepurchaseDetail repurchaseDetail = new RepurchaseDetail();
        repurchaseDetail.setUniqueId(uniqueId);
        repurchaseDetail.setAmount(new BigDecimal(amount));
        repurchaseDetail.setContractNo("");
        repurchaseDetail.setInterest(new BigDecimal("0"));
        repurchaseDetail.setPenaltyInterest(new BigDecimal("0"));
        repurchaseDetail.setPrincipal(new BigDecimal(principal));
        repurchaseDetail.setRepurchaseOtherFee(new BigDecimal("0"));
        list.add(repurchaseDetail);
//        requestParams.put("repurchaseDetail", JsonUtils.toJsonString(list));
        System.out.println(JsonUtils.toJsonString(requestParams));
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.print(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //回购申请测试方法-多条
    public void testRepurchase1(String url, String requestNo, String batchNo, String productCode, String uniqueId, String principal, String amount, String transactionType) {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("requestNo", requestNo);
        requestParams.put("batchNo", "e440538c-1c6b-4d9a-9c30-152bc211653f");
        requestParams.put("transactionType", transactionType);//0 提交 1 撤销
        requestParams.put("financialContractNo", productCode);
        requestParams.put("reviewer", "wubo");
        requestParams.put("reviewTimeString", DateUtils.format(new Date()));
        List<RepurchaseDetail> list = new ArrayList<>();
        RepurchaseDetail repurchaseDetail = new RepurchaseDetail();
        repurchaseDetail.setUniqueId(uniqueId);
        repurchaseDetail.setAmount(new BigDecimal(amount));
        repurchaseDetail.setContractNo("");
        repurchaseDetail.setInterest(new BigDecimal("0"));
        repurchaseDetail.setPenaltyInterest(new BigDecimal("0"));
        repurchaseDetail.setPrincipal(new BigDecimal(principal));
        repurchaseDetail.setRepurchaseOtherFee(new BigDecimal("0"));
        list.add(repurchaseDetail);

        RepurchaseDetail repurchaseDetail1 = new RepurchaseDetail();
        repurchaseDetail1.setUniqueId("wb9988760315");
        repurchaseDetail1.setAmount(new BigDecimal(amount));
        repurchaseDetail1.setContractNo("");
        repurchaseDetail1.setInterest(new BigDecimal("0"));
        repurchaseDetail1.setPenaltyInterest(new BigDecimal("0"));
        repurchaseDetail1.setPrincipal(new BigDecimal(principal));
        repurchaseDetail1.setRepurchaseOtherFee(new BigDecimal("0"));
        list.add(repurchaseDetail1);
        requestParams.put("repurchaseDetail", JsonUtils.toJsonString(list));
        System.out.println(JsonUtils.toJsonString(requestParams));
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.print(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //订单查询接口
    public void queryRepaymentOrder(String url, String productCode, String orderUniqueIds, String orderUuids) {
        Map<String, String> requestParams1 = new HashMap<String, String>();
        List<String> list = new ArrayList<>();
        list.add("f04a1721-3f2d-49fb-bfe0-734cf5777e83");

        requestParams1.put("requestNo", UUID.randomUUID().toString());
        requestParams1.put("financialProductCode", productCode);
        requestParams1.put("orderUniqueIds", orderUniqueIds);
        requestParams1.put("orderUuids", JsonUtils.toJsonString(list));
        System.out.println(JsonUtils.toJsonString(requestParams1));
        String sr = "";
        try {
            sr = PostTestUtil.sendPost(url, requestParams1, new BaseApiTestPost().getIdentityInfoMap(requestParams1));

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result======" + sr);
    }




    public void  query_voucher(String batchRequestNo,String bankTransactionNo){
        String int_url = "http://192.168.0.128/api/query";
        String out_url = "http://yunxin.5veda.net/api/query";
        Map<String, String> requestParams1 = new HashMap<String, String>();
        requestParams1.put("fn", "100008");
        requestParams1.put("requestNo", UUID.randomUUID().toString());
        requestParams1.put("batchRequestNo",batchRequestNo);
//        requestParams1.put("bankTransactionNo",bankTransactionNo);
        try {
            String sr = PostTestUtil.sendPost(out_url, requestParams1, getIdentityInfoMap(requestParams1));
            System.out.println(sr);
            String a = new String();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 中航-查询该合同下第i期还款计划
     *
     * @param uniqueId
     * @return
     */
    public String query_i_RepaymentPlan(String uniqueId, int i) {
        String int_url = "http://192.168.0.128:38081/api/query";
        String out_url = "http://avictctest.5veda.net/api/query";
        String repaymentNumber = "";
        Map<String, String> requestParams1 = new HashMap<String, String>();
        requestParams1.put("fn", "100001");
        requestParams1.put("contractNo", "");
        requestParams1.put("requestNo", UUID.randomUUID().toString());
        requestParams1.put("uniqueId", uniqueId);
        try {
            String sr = PostTestUtil.sendPost(out_url, requestParams1, getIdentityInfoMapForZHONGHANG(requestParams1));
            Result result = JsonUtils.parse(sr, Result.class);
            JSONArray repaymentPlanDetails = (JSONArray) result.get("repaymentPlanDetails");
            JSONObject jo = repaymentPlanDetails.getJSONObject(i);
            repaymentNumber = (String) jo.get("repaymentNumber");
            System.out.println("---------" + sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return repaymentNumber;
    }

    //中航扣款新
    @Test
    @Deprecated
    public void commandZHDedcutManualApiTestPost() {
        Map<String, String> requestParams = new HashMap<String, String>();

        requestParams.put("fn", "300001");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("deductId", "WUBO1113");
        requestParams.put("financialProductCode", "11111111");
        requestParams.put("uniqueId", "WUBO1234567890008");
        requestParams.put("contractNo", "WUBO1234567890008");
        requestParams.put("apiCalledTime", "2017-07-24");
        requestParams.put("transType", "0");//0:B2C  1:B2B
        requestParams.put("accountName", "WUBO");
        requestParams.put("accountNo", "6214855712106321");
        requestParams.put("notifyUrl", "http://server.ngrok.cc:17865/mock/deduct/single/success");
//		requestParams.put("gateway","");
        requestParams.put("amount", "20");
        requestParams.put("repaymentType", "1");
        requestParams.put("mobile", "18069847720");
        requestParams.put("repaymentDetails", "[{\"currentPeriod\":1,\"loanFee\":0,\"otherFee\":0,\"totalAmount\":20,\"overDueFeeDetail\":{\"lateFee\":0,\"lateOtherCost\":0,\"latePenalty\":0,\"penaltyFee\":0,\"totalOverdueFee\":0},\"repaymentAmount\":0,\"repaymentInterest\":0,\"repaymentPrincipal\":0,\"techFee\":0}]");
//				+ "'totalOverdueFee':0.00}}]");

        System.out.println(JsonUtils.toJsonString(requestParams));
        try {
            //String sr = PostTestUtil.sendPost("http://192.168.0.128:38083/pre/api/DEDUCT/zhonghang/ZH-SingleDeduct", requestParams, getIdentityInfoMapForZHONGHANG(requestParams));
            String sr = PostTestUtil.sendPost("http://avictctest.5veda.net:8887/pre/api/DEDUCT/zhonghang/ZH-SingleDeduct", requestParams, getIdentityInfoMapForZHONGHANG(requestParams));
            System.out.println(sr);
            String a = new String();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}