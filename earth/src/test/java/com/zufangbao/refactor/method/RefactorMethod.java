package com.zufangbao.refactor.method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.cucumber.method.model.BusinessAmountModel;
import com.zufangbao.cucumber.method.model.RepaymentDetailsModel;
import com.zufangbao.cucumber.method.model.RepurchaseDetailModel;
import com.zufangbao.earth.api.test.post.BaseApiTestPost;
import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.gluon.util.tests.AutoTestUtils;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageContent;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.ActivePaymentVoucherDetail;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

public class RefactorMethod extends BaseApiTestPost {
    public void deleteAllCashFlow(){
        PoolConfiguration poolProperties = new PoolProperties();

		String url = "jdbc:mysql://192.168.0.159:40110/yunxin?useUnicode=true&autoReconnect=true&characterEncoding=UTF-8";
		poolProperties.setUrl(url);
		poolProperties.setUsername("root");
		poolProperties.setPassword("123456");
		poolProperties.setDriverClassName("com.mysql.jdbc.Driver");
		DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
		AutoTestUtils.dbExecute("refactorSql/deleteAllCashFlow.sql", dataSource);
	}
    /**
     *
     * 导入资产包
     * @param totalAmount
     * @param productCode
     * @param uniqueId
     * @param repaymentAccountNo
     * @param interest
     * @param loanCustomerNo
     * @param firstRepaymentDate
     * @param secondRepaymentDate
     * @param thirdRepaymentDate
     */
    public void importAssetPackage(String totalAmount,String productCode,String uniqueId,String repaymentAccountNo,String interest,String loanCustomerNo,String firstRepaymentDate,String secondRepaymentDate,String thirdRepaymentDate,String loanCustomerName){
        for (int index = 0; index < 1; index++) {
            ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
            importAssetPackageContent.setThisBatchContractsTotalNumber(1);
            importAssetPackageContent.setThisBatchContractsTotalAmount(totalAmount);
            importAssetPackageContent.setFinancialProductCode(productCode);

            List<ContractDetail> contracts = new ArrayList<ContractDetail>();
            ContractDetail contractDetail = new ContractDetail();
            contractDetail.setUniqueId(uniqueId);
            contractDetail.setLoanContractNo(uniqueId);

            contractDetail.setLoanCustomerNo(loanCustomerNo);
            contractDetail.setLoanCustomerName(loanCustomerName);
            contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
            contractDetail.setIDCardNo("320301198502169142");
            contractDetail.setBankCode("C10103");
            contractDetail.setBankOfTheProvince("110000");
            contractDetail.setBankOfTheCity("110100");
            contractDetail.setRepaymentAccountNo(repaymentAccountNo);
            contractDetail.setLoanTotalAmount(totalAmount);
            contractDetail.setLoanPeriods(3);
            contractDetail.setEffectDate(com.demo2do.core.utils.DateUtils.format(com.demo2do.core.utils.DateUtils.addMonths(new Date(), -2)));
            contractDetail.setExpiryDate(DateUtils.format(DateUtils.addMonths(new Date(), 2)));
            contractDetail.setLoanRates("0.156");
            contractDetail.setInterestRateCycle(1);
            contractDetail.setPenalty("0.0005");
            contractDetail.setRepaymentWay(2);

            List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

            ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail1.setRepayScheduleNo(UUID.randomUUID().toString());
            repaymentPlanDetail1.setRepaymentPrincipal(String.valueOf(Double.valueOf(totalAmount)/3));
            repaymentPlanDetail1.setRepaymentInterest(interest);
            repaymentPlanDetail1.setRepaymentDate(firstRepaymentDate);
            repaymentPlanDetail1.setOtheFee(interest);
            repaymentPlanDetail1.setTechMaintenanceFee(interest);
            repaymentPlanDetail1.setLoanServiceFee(interest);
            repaymentPlanDetails.add(repaymentPlanDetail1);

            ImportRepaymentPlanDetail repaymentPlanDetail2= new ImportRepaymentPlanDetail();
            repaymentPlanDetail2.setRepayScheduleNo(UUID.randomUUID().toString());
            repaymentPlanDetail2.setRepaymentPrincipal(String.valueOf(Double.valueOf(totalAmount)/3));
            repaymentPlanDetail2.setRepaymentInterest(interest);
            repaymentPlanDetail2.setRepaymentDate(secondRepaymentDate);
            repaymentPlanDetail2.setOtheFee(interest);
            repaymentPlanDetail2.setTechMaintenanceFee(interest);
            repaymentPlanDetail2.setLoanServiceFee(interest);
            repaymentPlanDetails.add(repaymentPlanDetail2);

            ImportRepaymentPlanDetail repaymentPlanDetail3= new ImportRepaymentPlanDetail();
            repaymentPlanDetail3.setRepayScheduleNo(UUID.randomUUID().toString());
            repaymentPlanDetail3.setRepaymentPrincipal(String.valueOf(Double.valueOf(totalAmount)/3));
            repaymentPlanDetail3.setRepaymentInterest(interest);
            repaymentPlanDetail3.setRepaymentDate(thirdRepaymentDate);
            repaymentPlanDetail3.setOtheFee(interest);
            repaymentPlanDetail3.setTechMaintenanceFee(interest);
            repaymentPlanDetail3.setLoanServiceFee(interest);
            repaymentPlanDetails.add(repaymentPlanDetail3);

            contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

            contracts.add(contractDetail);
            importAssetPackageContent.setContractDetails(contracts);

            String param =  JsonUtils.toJsonString(importAssetPackageContent);
            System.out.println(param);
            Map<String,String> params =new HashMap<String,String>();
            params.put("fn", "200004");
            params.put("requestNo", UUID.randomUUID().toString());
            params.put("importAssetPackageContent", param);

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("merId", "t_test_zfb");
            headers.put("secret", "123456");
            String signContent = ApiSignUtils.getSignCheckContent(params);
            String sign = ApiSignUtils.rsaSign(signContent, privateKey);
            System.out.println(sign);
            headers.put("sign", sign);

            try {
                String sr = PostTestUtil.sendPost(IMPORT_PACKAGE, params, headers);
//                System.out.println( "===========" + index + sr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * 查询该合同下的某一期还款计划
     * @param uniqueId
     * @param index
     * @return
     */
    public String queryRepaymentPlan(String productCode,String uniqueId,int index){
        String repaymentNumber = "";
        Map<String, String> requestParams1 = new HashMap<String, String>();
        requestParams1.put("fn", "100001");
        requestParams1.put("productCode", productCode);
        requestParams1.put("contractNo", "");
        requestParams1.put("requestNo", UUID.randomUUID().toString());
        requestParams1.put("uniqueId", uniqueId);
        try {
            String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1, getIdentityInfoMap(requestParams1));
            Result result = JsonUtils.parse(sr, Result.class);
            Result responsePacket =  JsonUtils.parse((String) result.get("responsePacket"),Result.class);
            JSONArray jsonArray = (JSONArray) responsePacket.get("repaymentPlanDetails");
            JSONObject jo = jsonArray.getJSONObject(index);
            repaymentNumber = (String) jo.get("repaymentNumber");
//            System.out.println(sr+1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return repaymentNumber;
    }


    /**
     *
     * 还款订单提交(2个明细)
     * @param orderRequestNo
     * @param productCode
     * @param orderAmount
     * @param firstUniqueId
     * @param secondUniqueId
     * @param firstRepaymentPlanNo
     * @param secondRepaymentPlanNo
     * @param firstRepaymentWay
     * @param secondRepaymentWay
     * @param firstDetailTotalAmount
     * @param secondDetailTotalAmount
     * @param firstDetailPrincipal
     * @param secondDetailPrincipal
     * @param firstDetailInterest
     * @param secondDetailInterest
     * @param firstFeeType
     * @param secondFeeType
     */
    public String applyRepaymentOderForTwoDetails(String orderRequestNo, String productCode, String orderAmount, String firstUniqueId,
                                                  String secondUniqueId, String firstRepaymentPlanNo, String secondRepaymentPlanNo, Long firstRepaymentWay, Long secondRepaymentWay,
                                                  String firstDetailTotalAmount, String secondDetailTotalAmount, String firstDetailPrincipal, String secondDetailPrincipal,
                                                  String firstDetailInterest, String secondDetailInterest,
                                                  Long firstFeeType, Long secondFeeType){
        String result ="";
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("orderRequestNo", orderRequestNo);
        requestParams.put("orderUniqueId",orderRequestNo);
        requestParams.put("transType","0");
        requestParams.put("financialContractNo",productCode);
        requestParams.put("orderAmount",orderAmount);
        List<RepaymentDetailsModel> repaymentDetailsModelList=new ArrayList<RepaymentDetailsModel>();
        List<BusinessAmountModel> businessAmountModelList=new ArrayList<BusinessAmountModel>();

        RepaymentDetailsModel repaymentDetailsModel1 = new RepaymentDetailsModel();
        repaymentDetailsModel1.setContractNo("");
        repaymentDetailsModel1.setContractUniqueId(firstUniqueId);
        repaymentDetailsModel1.setRepaymentWay(firstRepaymentWay);
        repaymentDetailsModel1.setRepaymentBusinessNo(firstRepaymentPlanNo);
        repaymentDetailsModel1.setPlannedDate("");
        repaymentDetailsModel1.setDetailsTotalAmount(new BigDecimal(firstDetailTotalAmount));
        BusinessAmountModel businessAmountModel1 = new BusinessAmountModel();
        businessAmountModel1.setFeeType((firstFeeType*1000));//费用类型，2000到2003都为回购费用
        businessAmountModel1.setActualAmount(new BigDecimal(firstDetailPrincipal));//金额
        BusinessAmountModel businessAmountModel2 = new BusinessAmountModel();
        businessAmountModel2.setFeeType((firstFeeType*1000)+1);//费用类型，2000到2003都为回购费用
        businessAmountModel2.setActualAmount(new BigDecimal(firstDetailInterest));//金额
        BusinessAmountModel businessAmountModel3 = new BusinessAmountModel();
        businessAmountModel3.setFeeType((firstFeeType*1000)+2);//费用类型，2000到2003都为回购费用
        businessAmountModel3.setActualAmount(new BigDecimal(firstDetailInterest));//金额
        BusinessAmountModel businessAmountModel4 = new BusinessAmountModel();
        businessAmountModel4.setFeeType((firstFeeType*1000)+3);//费用类型，2000到2003都为回购费用
        businessAmountModel4.setActualAmount(new BigDecimal(firstDetailInterest));//金额
        BusinessAmountModel businessAmountModel5 = new BusinessAmountModel();
        businessAmountModel5.setFeeType((firstFeeType*1000)+4);//费用类型，2000到2003都为回购费用
        businessAmountModel5.setActualAmount(new BigDecimal(firstDetailInterest));//金额
        businessAmountModelList.add(businessAmountModel1);
        businessAmountModelList.add(businessAmountModel2);
        businessAmountModelList.add(businessAmountModel3);
        businessAmountModelList.add(businessAmountModel4);
        businessAmountModelList.add(businessAmountModel5);
        repaymentDetailsModel1.setDetailsAmountJsonList(businessAmountModelList);
        repaymentDetailsModelList.add(repaymentDetailsModel1);


        RepaymentDetailsModel repaymentDetailsModel2 = new RepaymentDetailsModel();
        repaymentDetailsModel2.setContractNo("");
        repaymentDetailsModel2.setContractUniqueId(secondUniqueId);
        repaymentDetailsModel2.setRepaymentWay(secondRepaymentWay);
        repaymentDetailsModel2.setRepaymentBusinessNo(secondRepaymentPlanNo);
        repaymentDetailsModel2.setDetailsTotalAmount(new BigDecimal(secondDetailTotalAmount));
        List<BusinessAmountModel> businessAmountModelList2=new ArrayList<>();
        BusinessAmountModel businessAmountModel6 = new BusinessAmountModel();
        businessAmountModel6.setFeeType((secondFeeType*1000));//费用类型，2000到2003都为回购费用
        businessAmountModel6.setActualAmount(new BigDecimal(secondDetailPrincipal));//金额
        BusinessAmountModel businessAmountModel7 = new BusinessAmountModel();
        businessAmountModel7.setFeeType((secondFeeType*1000)+1);//费用类型，2000到2003都为回购费用
        businessAmountModel7.setActualAmount(new BigDecimal(secondDetailInterest));//金额
        BusinessAmountModel businessAmountModel8 = new BusinessAmountModel();
        businessAmountModel8.setFeeType((secondFeeType*1000)+2);//费用类型，2000到2003都为回购费用
        businessAmountModel8.setActualAmount(new BigDecimal(secondDetailInterest));//金额
        BusinessAmountModel businessAmountModel9 = new BusinessAmountModel();
        businessAmountModel9.setFeeType((secondFeeType*1000)+3);//费用类型，2000到2003都为回购费用
        businessAmountModel9.setActualAmount(new BigDecimal(secondDetailInterest));//金额
        businessAmountModelList2.add(businessAmountModel6);
        businessAmountModelList2.add(businessAmountModel7);
        businessAmountModelList2.add(businessAmountModel8);
        businessAmountModelList2.add(businessAmountModel9);
        repaymentDetailsModel2.setDetailsAmountJsonList(businessAmountModelList2);
        repaymentDetailsModelList.add(repaymentDetailsModel2);

        String s= JSON.toJSONString(repaymentDetailsModelList, SerializerFeature.DisableCircularReferenceDetect);
        requestParams.put("repaymentOrderDetail",s);
        try {
            result = PostTestUtil.sendPost(REPAYMENTORDER, requestParams, getIdentityInfoMap(requestParams));//192.168.0.204
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 还款订单提交(1个明细)
     * @param orderRequestNo
     * @param productCode
     * @param orderAmount
     * @param firstUniqueId
     * @param firstRepaymentPlanNo
     * @param firstRepaymentWay
     * @param firstDetailTotalAmount
     * @param firstDetailPrincipal
     * @param firstDetailInterest
     * @param firstFeeType
     * @return
     */
    public String applyRepaymentOderForOneDetail(String orderRequestNo, String orderUniqueId, String transType,String productCode, String orderAmount, String firstUniqueId,
                                                  String firstRepaymentPlanNo, Long firstRepaymentWay,
                                                  String firstDetailTotalAmount, String firstDetailPrincipal,
                                                  String firstDetailInterest, Long firstFeeType, String repayScheduleNo){
        String result ="";
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("orderRequestNo", orderRequestNo);
        requestParams.put("orderUniqueId",orderUniqueId);
        requestParams.put("transType",transType);
        requestParams.put("financialContractNo",productCode);
        requestParams.put("orderAmount",orderAmount);
        List<RepaymentDetailsModel> repaymentDetailsModelList=new ArrayList<RepaymentDetailsModel>();
        List<BusinessAmountModel> businessAmountModelList=new ArrayList<BusinessAmountModel>();

        RepaymentDetailsModel repaymentDetailsModel1 = new RepaymentDetailsModel();
        repaymentDetailsModel1.setContractNo("");
        repaymentDetailsModel1.setContractUniqueId(firstUniqueId);
        repaymentDetailsModel1.setRepaymentWay(firstRepaymentWay);
        repaymentDetailsModel1.setRepaymentBusinessNo(firstRepaymentPlanNo);
        repaymentDetailsModel1.setRepayScheduleNo(repayScheduleNo);
        repaymentDetailsModel1.setPlannedDate("");
        repaymentDetailsModel1.setDetailsTotalAmount(new BigDecimal(firstDetailTotalAmount));
        BusinessAmountModel businessAmountModel1 = new BusinessAmountModel();
        businessAmountModel1.setFeeType((firstFeeType*1000));//费用类型，2000到2003都为回购费用
        businessAmountModel1.setActualAmount(new BigDecimal(firstDetailPrincipal));//金额
        BusinessAmountModel businessAmountModel2 = new BusinessAmountModel();
        businessAmountModel2.setFeeType((firstFeeType*1000)+1);//费用类型，2000到2003都为回购费用
        businessAmountModel2.setActualAmount(new BigDecimal(firstDetailInterest));//金额
        BusinessAmountModel businessAmountModel3 = new BusinessAmountModel();
        businessAmountModel3.setFeeType((firstFeeType*1000)+2);//费用类型，2000到2003都为回购费用
        businessAmountModel3.setActualAmount(new BigDecimal(firstDetailInterest));//金额
        BusinessAmountModel businessAmountModel4 = new BusinessAmountModel();
        businessAmountModel4.setFeeType((firstFeeType*1000)+3);//费用类型，2000到2003都为回购费用
        businessAmountModel4.setActualAmount(new BigDecimal(firstDetailInterest));//金额
        BusinessAmountModel businessAmountModel5 = new BusinessAmountModel();
        businessAmountModel5.setFeeType((firstFeeType*1000)+4);//费用类型，2000到2003都为回购费用
        businessAmountModel5.setActualAmount(new BigDecimal(firstDetailInterest));//金额
        businessAmountModelList.add(businessAmountModel1);
        businessAmountModelList.add(businessAmountModel2);
        businessAmountModelList.add(businessAmountModel3);
        businessAmountModelList.add(businessAmountModel4);
        businessAmountModelList.add(businessAmountModel5);
        repaymentDetailsModel1.setDetailsAmountJsonList(businessAmountModelList);
        repaymentDetailsModelList.add(repaymentDetailsModel1);

        String s= JSON.toJSONString(repaymentDetailsModelList, SerializerFeature.DisableCircularReferenceDetect);
        requestParams.put("repaymentOrderDetail",s);
        try {
            result = PostTestUtil.sendPost(REPAYMENTORDER, requestParams, getIdentityInfoMap(requestParams));//192.168.0.204
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 回购
     * @param productCode
     * @param uniqueId
     * @param principal
     * @param interest
     */
    public void applyRepurchase(String productCode,String uniqueId,String principal,String interest,String totalAmount) {
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("requestNo", UUID.randomUUID().toString());
            requestParams.put("batchNo", UUID.randomUUID().toString());//选填
            requestParams.put("transactionType", "0");//交易类型，0提交，1撤销
            requestParams.put("financialContractNo", productCode);//信托合同代码,信托合同需支持回购
            requestParams.put("reviewer", "fanteng");//审核人
            requestParams.put("reviewTimeString", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));//审核时间

            List<RepurchaseDetailModel> list = new ArrayList<>();
            RepurchaseDetailModel repurchaseDetailModel = new RepurchaseDetailModel();
            repurchaseDetailModel.setUniqueId(uniqueId);
            repurchaseDetailModel.setContractNo("");//选填
            repurchaseDetailModel.setPrincipal(new BigDecimal(principal));//选填
            repurchaseDetailModel.setInterest(new BigDecimal(interest));//选填
            repurchaseDetailModel.setPenaltyInterest(new BigDecimal(interest));//选填
            repurchaseDetailModel.setRepurchaseOtherFee(new BigDecimal(interest));//选填
            repurchaseDetailModel.setAmount(new BigDecimal(totalAmount));//选填

            list.add(repurchaseDetailModel);
            String repurchaseDetail = JsonUtils.toJsonString(list);
            requestParams.put("repurchaseDetail", repurchaseDetail);
            try {
                String sr = PostTestUtil.sendPost(REPURCHASE, requestParams, getIdentityInfoMap(requestParams));
                System.out.println(sr);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    /**
     * 订单支付
     * @param paymentNo
     * @param orderUniqueId
     * @param productCode
     * @param payWay
     * @param paymentAccountNo
     * @param paymentAccountName
     * @param paymentGateWay
     * @param tradeUuid
     * @param amount
     * @param transactionTime
     * @return
     */
    public String payOrder(String paymentNo,String orderUniqueId,String productCode,String payWay,String paymentAccountNo,
                         String paymentAccountName,String paymentGateWay,String tradeUuid,String amount,String transactionTime){
        String result = "";
        Map<String,String> requestParams = new HashMap<>();
        requestParams.put("requestNo", UUID.randomUUID().toString()); //请求编号
        requestParams.put("paymentNo",paymentNo); //商户支付编号
        requestParams.put("orderUniqueId",orderUniqueId); //商户订单号
        requestParams.put("financialContractNo",productCode); //信托产品代码
        requestParams.put("payWay",payWay); //支付方式 0：线下转账  2：线上代扣  3：商户代扣
        requestParams.put("paymentAccountNo",paymentAccountNo); //付款银行帐户号
        requestParams.put("paymentAccountName",paymentAccountName); //付款银行帐户名称
        requestParams.put("paymentBankCode","C10104"); //付款银行名称编码
        requestParams.put("paymentProvinceCode","620000"); //付款方所在地省编码
        requestParams.put("paymentCityCode","141100"); //付款方所在地市编码
        requestParams.put("idCardNum","421182199204114115"); //付款方身份证
        requestParams.put("mobile","17682481004"); //付款方手机号
        requestParams.put("paymentGateWay",paymentGateWay); //支付通道
        requestParams.put("tradeUuid",tradeUuid); //通道交易号
        requestParams.put("amount",amount); //交易金额
        requestParams.put("transactionTime",transactionTime); //交易时间


        try {
            result = PostTestUtil.sendPost(PAYMENTORDERONLINE, requestParams, getIdentityInfoMap(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 变更还款计划-单期
     * @param uniqueId
     * @param PlanAmount
     * @param interest
     * @param PlanDate
     * @param assetType
     * @param requestReason
     * @return
     */
    public String modifyRepaymentPlanForOne(String uniqueId, String PlanAmount,String interest ,String PlanDate,Integer assetType,String requestReason,String repayScheduleNo,String contractNo) {
        String result = null;
        List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
        RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
        model.setAssetInterest(new BigDecimal(interest));
        model.setAssetPrincipal(new BigDecimal(PlanAmount));
        model.setMaintenanceCharge(new BigDecimal(interest));
        model.setServiceCharge(new BigDecimal(interest));
        model.setOtherCharge(new BigDecimal(interest));
        model.setRepayScheduleNo(repayScheduleNo);
        model.setAssetType(assetType);
        model.setAssetRecycleDate(PlanDate);
        requestDataList.add(model);

        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "200001");
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("contractNo",contractNo);
        requestParams.put("requestReason", requestReason);
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("requestData", JsonUtils.toJsonString(requestDataList));
        try {
            result = PostTestUtil.sendPost(MODIFY_REPAYMENT, requestParams,getIdentityInfoMap(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 变更还款计划-两期
     * @param uniqueId
     * @param firstPlanAmount
     * @param secondPlanAmount
     * @param interest
     * @param firstPlanDate
     * @param secondPlanDate
     * @param firstAssetType
     * @param secondAssetType
     * @param requestReason
     * @return
     */
    public String modifyRepaymentPlanForTwo(String uniqueId, String firstPlanAmount, String secondPlanAmount, String interest,String firstPlanDate, String secondPlanDate,Integer firstAssetType,Integer secondAssetType,String requestReason,String firstRepayScheduleNo,String secondRepayScheduleNo) {
        String result = null;
        List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
        RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
        model.setAssetInterest(new BigDecimal(interest));
        model.setAssetPrincipal(new BigDecimal(firstPlanAmount));
        model.setMaintenanceCharge(new BigDecimal(interest));
        model.setServiceCharge(new BigDecimal(interest));
        model.setOtherCharge(new BigDecimal(interest));
        model.setRepayScheduleNo(firstRepayScheduleNo);
        model.setAssetType(firstAssetType);
        model.setAssetRecycleDate(firstPlanDate);
        requestDataList.add(model);

        RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
        model2.setAssetInterest(new BigDecimal(interest));
        model2.setAssetPrincipal(new BigDecimal(secondPlanAmount));
        model2.setMaintenanceCharge(new BigDecimal(interest));
        model2.setServiceCharge(new BigDecimal(interest));
        model2.setOtherCharge(new BigDecimal(interest));
        model2.setRepayScheduleNo(secondRepayScheduleNo);
        model2.setAssetType(secondAssetType);
        model2.setAssetRecycleDate(secondPlanDate);

        requestDataList.add(model2);
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "200001");
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("requestReason", requestReason);
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("requestData", JsonUtils.toJsonString(requestDataList));
        try {
            result = PostTestUtil.sendPost(MODIFY_REPAYMENT, requestParams, getIdentityInfoMap(requestParams));
            System.out.println(result);
            System.out.println("333##############################333");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 扣款
     * @param financialProductCode
     * @param uniqueId
     * @param totalAmount
     * @param payAcNo
     * @param repaymentType
     * @param repaymentPlanNo
     * @param principal
     * @param interest
     * @param overDueFee
     * @param totalOverdueFee
     * @return
     */
    public String dedcutRepaymentPlan(String financialProductCode,String uniqueId,String totalAmount,String payAcNo,String repaymentType,String repaymentPlanNo,String principal,String interest,String overDueFee,String totalOverdueFee) {
        String result = "";
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300001");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("deductId",  UUID.randomUUID().toString());
        requestParams.put("financialProductCode", financialProductCode);
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("apiCalledTime", DateUtils.format(new Date()));
        requestParams.put("amount",  totalAmount);
        requestParams.put("bankCode","C10104");
        requestParams.put("payerName","秦萎超");
        requestParams.put("payAcNo",payAcNo);
        requestParams.put("idCardNum","472578425753257425");
        requestParams.put("provinceCode","620000");
        requestParams.put("cityCode","141100");
        requestParams.put("repaymentType", repaymentType);
        requestParams.put("mobile", "15317312520");
        requestParams.put("repaymentDetails", "[{'loanFee':"+interest+",'otherFee':"+interest+",'repaymentAmount':"+totalAmount+",'repaymentInterest':"+interest+",'repaymentPlanNo':'"+repaymentPlanNo+"','repaymentPrincipal':"+principal+",'techFee':"+interest+",'overDueFeeDetail':{"
				+ "'penaltyFee':"+overDueFee+",'latePenalty':"+overDueFee+",'lateFee':"+overDueFee+",'lateOtherCost':"+overDueFee+",'totalOverdueFee':"+totalOverdueFee+"}}]");
        try {
            result = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 查询该合同下有效的还款计划
     * @param uniqueId
     * @return
     */
    public String queryRepaymentPlanCount(String uniqueId){
        String repaymentPlanCount = "";
        Map<String, String> requestParams1 = new HashMap<String, String>();
        requestParams1.put("fn", "100001");
        requestParams1.put("contractNo", "");
        requestParams1.put("requestNo", UUID.randomUUID().toString());
        requestParams1.put("uniqueId", uniqueId);
        try {
            String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1,getIdentityInfoMap(requestParams1));
            Result result = JsonUtils.parse(sr, Result.class);
            Result responsePacket =  JsonUtils.parse((String) result.get("responsePacket"),Result.class);
            JSONArray repaymentPlanDetails =  (JSONArray) responsePacket.get("repaymentPlanDetails");
            repaymentPlanCount = repaymentPlanDetails.size()+"";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return repaymentPlanCount;
    }

    /**
     * 主动付款凭证-一条明细
     * @param transactionType
     * @param voucherType
     * @param bankTransactionNo
     * @param voucherAmount
     * @param financialContractNo
     * @param uniqueId
     * @param repaymentPlanNo
     * @param repayScheduleNo
     * @param detailAmount
     * @param principal
     * @param interest
     * @param paymentName
     * @param paymentAccountNo
     * @throws IOException
     */
    public String activePaymentVoucherForOneDetail(String transactionType,String voucherType,String bankTransactionNo,String voucherAmount,String financialContractNo,
                                                 String uniqueId,String contractNo,String repaymentPlanNo,String repayScheduleNo,String detailAmount,String principal,
                                                 String interest,String paymentName,String paymentAccountNo,String receivableAccountNo) throws IOException {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn","300004");
        requestParams.put("transactionType",transactionType);
        requestParams.put("requestNo",UUID.randomUUID().toString());
        requestParams.put("voucherType",voucherType);//5,6
        requestParams.put("paymentBank","中国工商银行");
        requestParams.put("bankTransactionNo",bankTransactionNo);
        requestParams.put("voucherAmount",voucherAmount);
        requestParams.put("financialContractNo",financialContractNo);

        ActivePaymentVoucherDetail detail= new ActivePaymentVoucherDetail() ;
        detail.setUniqueId(uniqueId);
        detail.setContractNo(contractNo);
        detail.setRepaymentPlanNo(repaymentPlanNo);
		detail.setRepayScheduleNo(repayScheduleNo);
        detail.setAmount(new BigDecimal(detailAmount));
        detail.setPrincipal(new BigDecimal(principal));
        detail.setInterest(new BigDecimal(interest));
        detail.setServiceCharge(new BigDecimal(interest));
        detail.setMaintenanceCharge(new BigDecimal(interest));
        detail.setOtherCharge(new BigDecimal(interest));

        requestParams.put("detail", JsonUtils.toJsonString(Arrays.asList(detail)));
        requestParams.put("paymentName",paymentName);
        requestParams.put("paymentAccountNo",paymentAccountNo);
        requestParams.put("receivableAccountNo",receivableAccountNo);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(AVTIVE_VOUCHER);
        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        reqEntity.setCharset(Charset.forName(HTTP.UTF_8));
        for (Map.Entry<String, String> e : requestParams.entrySet()) {
            StringBody stringBody =  new StringBody(e.getValue(), contentType);
            reqEntity.addPart(e.getKey(), stringBody);
        }
        httppost.setEntity(reqEntity.build());
        String signContent = ApiSignUtils.getSignCheckContent(requestParams);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        httppost.addHeader("merId", TEST_MERID);
        httppost.addHeader("secret", TEST_SECRET);
        httppost.addHeader("sign", sign);
        System.out.println(sign);
        HttpResponse response = client.execute(httppost);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        System.out.println(requestParams);
        System.out.println(result);
        return result;
    }


    /**
     * 主动付款凭证-两条明细
     * @param transactionType
     * @param voucherType
     * @param bankTransactionNo
     * @param voucherAmount
     * @param financialContractNo
     * @param firstUniqueId
     * @param firstRepaymentPlanNo
     * @param detailAmount
     * @param principal
     * @param interest
     * @param secondUniqueId
     * @param secondRepaymentPlanNo
     * @param paymentName
     * @param paymentAccountNo
     * @return
     * @throws IOException
     */
    public String activePaymentVoucherForTwoDetails(String transactionType,String voucherType,String bankTransactionNo,String voucherAmount,String financialContractNo,
                                                  String firstUniqueId,String firstRepaymentPlanNo,String detailAmount,String principal,String interest,
                                                  String secondUniqueId,String secondRepaymentPlanNo,String paymentName,String paymentAccountNo) throws IOException {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn","300004");
        requestParams.put("transactionType",transactionType);
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("voucherType",voucherType);//5,6
        requestParams.put("paymentBank", "中国工商银行");
        requestParams.put("bankTransactionNo",bankTransactionNo);
        requestParams.put("voucherAmount",voucherAmount);
        requestParams.put("financialContractNo",financialContractNo);

        ActivePaymentVoucherDetail detail= new ActivePaymentVoucherDetail() ;
        detail.setUniqueId(firstUniqueId);
        detail.setRepaymentPlanNo(firstRepaymentPlanNo);
        detail.setAmount(new BigDecimal(detailAmount));
        detail.setPrincipal(new BigDecimal(principal));
        detail.setInterest(new BigDecimal(interest));
        detail.setServiceCharge(new BigDecimal(interest));
        detail.setMaintenanceCharge(new BigDecimal(interest));
        detail.setOtherCharge(new BigDecimal(interest));

		ActivePaymentVoucherDetail detail2= new ActivePaymentVoucherDetail() ;
		detail2.setUniqueId(secondUniqueId);
		detail2.setRepaymentPlanNo(secondRepaymentPlanNo);
		detail2.setAmount(new BigDecimal(detailAmount));
		detail2.setPrincipal(new BigDecimal(principal));
		detail2.setInterest(new BigDecimal(interest));
		detail2.setServiceCharge(new BigDecimal(interest));
		detail2.setMaintenanceCharge(new BigDecimal(interest));
		detail2.setOtherCharge(new BigDecimal(interest));
        requestParams.put("detail", JsonUtils.toJsonString(Arrays.asList(detail,detail2)));
        requestParams.put("paymentName", paymentName);
        requestParams.put("paymentAccountNo", paymentAccountNo);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(AVTIVE_VOUCHER);
        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        reqEntity.setCharset(Charset.forName(HTTP.UTF_8));
        for (Map.Entry<String, String> e : requestParams.entrySet()) {
            StringBody stringBody =  new StringBody(e.getValue(), contentType);
            reqEntity.addPart(e.getKey(), stringBody);
        }
        httppost.setEntity(reqEntity.build());
        String signContent = ApiSignUtils.getSignCheckContent(requestParams);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        httppost.addHeader("merId", TEST_MERID);
        httppost.addHeader("secret", TEST_SECRET);
        httppost.addHeader("sign", sign);
        System.out.println(sign);
        HttpResponse response = client.execute(httppost);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        System.out.println(requestParams);
        System.out.println(result);
        return result;
    }

    /**
     * 申请提前还款
     * @param uniqueId
     * @param contractNo
     * @param assetInitialValue
     * @param assetPrincipal
     * @param assetRecycleDate
     * @param interest
     * @param type
     * @return
     */
    public String applyPrepaymentPlan(String uniqueId,String contractNo,String assetRecycleDate,String assetInitialValue,String assetPrincipal,String interest,String type){
        String result = null;
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "200002");
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("contractNo", contractNo);
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("assetRecycleDate", assetRecycleDate);
        requestParams.put("assetInitialValue", assetInitialValue);
        requestParams.put("assetPrincipal", assetPrincipal);
        requestParams.put("assetInterest", interest);
        requestParams.put("serviceCharge", interest);
        requestParams.put("maintenanceCharge", interest);
        requestParams.put("otherCharge", interest);
        requestParams.put("type", type);
        requestParams.put("payWay","0");
        try {
            result = PostTestUtil.sendPost(PREPAYMENT, requestParams,getIdentityInfoMap(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 浮动费用接口
     * @param productCode
     * @param uniqueId
     * @param contractNo
     * @param repaymentPlanNo
     * @param repayScheduleNo
     * @param reasonCode
     * @param approvedTime
     * @param firstFeeType
     * @param secondFeeType
     * @param thirdFeeType
     * @param fourthFeeType
     * @param firstInterest
     * @param secondInterest
     * @param thirdInterest
     * @param fourthInterest
     * @return
     */
    public String mutableFee(String productCode,String uniqueId,String contractNo,String repaymentPlanNo,String repayScheduleNo,
                             String reasonCode,String approvedTime,Integer firstFeeType,Integer secondFeeType,Integer thirdFeeType,Integer fourthFeeType,
                             String firstInterest,String secondInterest,String thirdInterest,String fourthInterest) {
        String result = "";
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("requestNo",UUID.randomUUID().toString());
        requestParams.put("financialProductCode",productCode);
        requestParams.put("uniqueId",uniqueId);
        requestParams.put("contractNo",contractNo);
        requestParams.put("repaymentPlanNo",repaymentPlanNo);
		requestParams.put("repayScheduleNo",repayScheduleNo);
        requestParams.put("reasonCode",reasonCode);
        requestParams.put("approver","樊肖繁");
        requestParams.put("approvedTime",approvedTime);
        requestParams.put("comment","TestInterface");
        MutableFeeDetail detail = new MutableFeeDetail(firstFeeType,new BigDecimal(firstInterest));
        MutableFeeDetail detail1 = new MutableFeeDetail(secondFeeType,new BigDecimal(secondInterest));
        MutableFeeDetail detail2 = new MutableFeeDetail(thirdFeeType,new BigDecimal(thirdInterest));
        MutableFeeDetail detail3 = new MutableFeeDetail(fourthFeeType,new BigDecimal(fourthInterest));
        List<MutableFeeDetail> details=new ArrayList<>();
        details.add(detail);
        details.add(detail1);
        details.add(detail2);
        details.add(detail3);
        net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(details);
        requestParams.put("details", jsonArray.toString());
        try {
            result = PostTestUtil.sendPost(MUTABLE_FEE, requestParams, getIdentityInfoMap(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
