package com.zufangbao.cucumber.method;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.cucumber.method.model.*;
import com.zufangbao.earth.api.test.post.BaseApiTestPost;
import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageContent;
import com.zufangbao.sun.api.model.modify.ModifyOverDueFeeDetail;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherDetailModel;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherRepayDetailModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.BusinessPaymentVoucherDetail;

import java.math.BigDecimal;
import java.util.*;

public class BaseTestMethod extends commandUrl {

    private String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

    /**
     * 查询还款计划接口
     *@param url
     * */
    public void queryLoan(String url,String uniqueId){
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn","100001");
        requestParams.put("uniqueId",uniqueId);
        requestParams.put("contractNo","");
        requestParams.put("requestNo",UUID.randomUUID().toString());
        try {
            String sr = PostTestUtil.sendPost(url, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 查询扣款接口
     *@param url
     * */
    public void queryDeduct(String url,String uniqueId,String deductId){
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn","100002");
        requestParams.put("requestNo",UUID.randomUUID().toString());
        requestParams.put("deductId",deductId);
        requestParams.put("uniqueId",uniqueId);
        requestParams.put("contractNo","");
        try {
            String sr = PostTestUtil.sendPost(url, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 查询还款清单接口
     *@param url
     *
     * */
    public void queryRepayment(String url,String financialContractNo,String queryStartDate,String queryEndDate){
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn","100003");
        requestParams.put("financialContractNo",financialContractNo);
        requestParams.put("requestNo",UUID.randomUUID().toString());
        requestParams.put("queryStartDate",queryStartDate);
        requestParams.put("queryEndDate",queryEndDate);
        try {
            String sr = PostTestUtil.sendPost(url, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 批量查询还款计划接口
     * @param url
     * */
    public void queryRepaymentPlan(String url,String uniqueId,String productCode,String date){
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn","100005");
        requestParams.put("requestNo",UUID.randomUUID().toString());
        requestParams.put("uniqueIds","[{'uniqueId':'"+uniqueId+"'}]");
        requestParams.put("productCode",productCode);
        requestParams.put("planRepaymentDate",date);
        try {
            String sr = PostTestUtil.sendPost(url, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 资产包批量查询接口
     * @param url
     * */
    public void assetPackageBatchQuery(String url){
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn","100006");
        requestParams.put("requestNo","");
        requestParams.put("deudctIdList","");
        requestParams.put("repaymentPlanCodeList","");
        requestParams.put("repaymentType","");
        try {
            String sr = PostTestUtil.sendPost(url, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 批量扣款状态查询接口
     * @param url
     * */
    public void batchQueryDeduct(String url){
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn","100007");
        requestParams.put("productCode","");
        requestParams.put("requestNo","");
        requestParams.put("uniqueIds","");
        requestParams.put("contractNos","");
        requestParams.put("startTime","");
        requestParams.put("endTime","");
        try {
            String sr = PostTestUtil.sendPost(url, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * 凭证查询接口
     * @param url
     * */
    public void documentQuery(String url){
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn","100008");
        requestParams.put("requestNo","");
        requestParams.put("batchRequestNo","");
        requestParams.put("bankTransactionNo","");
        try {
            String sr = PostTestUtil.sendPost(url, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 查询该合同下第index期还款计划
     *
     * @param uniqueId
     * @param index
     * @return
     */
    public String queryFirstRepaymentPlan(String uniqueId, int index) {
        String repaymentNumber = "";
        Map<String, String> requestParams1 = new HashMap<String, String>();
        requestParams1.put("fn", "100001");
        requestParams1.put("contractNo", "");
        requestParams1.put("requestNo", UUID.randomUUID().toString());
        requestParams1.put("uniqueId", uniqueId);
        try {
            String sr = PostTestUtil.sendPost(OuterInternat_queryUrl, requestParams1, new BaseApiTestPost().getIdentityInfoMap(requestParams1));
            Result result = JsonUtils.parse(sr, Result.class);
            JSONArray repaymentPlanDetails = (JSONArray) result.get("repaymentPlanDetails");
            JSONObject jo = repaymentPlanDetails.getJSONObject(index);
            repaymentNumber = (String) jo.get("repaymentNumber");
            System.out.println(sr + 1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return repaymentNumber;
    }


    /**
     * 查询该合同下有效的还款计划
     *
     * @param uniqueId
     * @return
     */
    public String queryRepaymentPlanCount(String uniqueId) {
        String repaymentPlanCount = "";
        Map<String, String> requestParams1 = new HashMap<String, String>();
        requestParams1.put("fn", "100001");
        requestParams1.put("contractNo", "");
        requestParams1.put("requestNo", UUID.randomUUID().toString());
        requestParams1.put("uniqueId", uniqueId);
        try {
            String sr = PostTestUtil.sendPost(OuterInternat_queryUrl, requestParams1, new BaseApiTestPost().getIdentityInfoMap(requestParams1));
            Result result = JsonUtils.parse(sr, Result.class);
            JSONArray repaymentPlanDetails = (JSONArray) result.get("repaymentPlanDetails");
            repaymentPlanCount = repaymentPlanDetails.size() + "";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return repaymentPlanCount;
    }


    /**
     * 根据查询时间进行资产包批量查询
     *
     * @param startTime
     * @param endTime
     * @param productCode
     * @return
     */
    public String BatchQueryAssertPackage(String startTime, String endTime, String productCode) {
        String result = "";
        Map<String, String> requestParams1 = new HashMap<String, String>();
        requestParams1.put("fn", "100007");
        requestParams1.put("requestNo", UUID.randomUUID().toString());//请求唯一标识
        requestParams1.put("productCode", productCode);
        requestParams1.put("startTime", startTime);
        requestParams1.put("endTime", endTime);
        try {
            result = PostTestUtil.sendPost("http://192.168.0.204/api/query", requestParams1, new BaseApiTestPost().getIdentityInfoMap(requestParams1));
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据batchRequest or banktranscation进行查询
     *
     * @param batchRequest
     * @param banktranscation
     * @return
     */
//    public void queryThirdVoucherBybatchRequest(String url, String batchRequest, String banktranscation) {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("fn", "100008");
//        params.put("requestNo", UUID.randomUUID().toString());//请求唯一编号
//        params.put("batchRequestNo", batchRequest);//交易请求编号
//        params.put("bankTransactionNo", banktranscation);//银行流水号
//
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("merId", "t_test_zfb");
//        headers.put("secret", "123456");
//        String signContent = ApiSignUtils.getSignCheckContent(params);
//        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
//        System.out.println(sign);
//        headers.put("sign", sign);
//
//        try {
//            String sr = PostTestUtil.sendPost(url, params, headers);
//            System.out.println("===========" + sr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 变更还款计划接口
     * */
    public String changeRepayment(String url,String uniqueId,String amount,String requestReason){
        String result=null;
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn","200001");
        requestParams.put("uniqueId",uniqueId);//贷款合同唯一编号
        requestParams.put("contractNo","");//贷款合同编号
        requestParams.put("requestNo",UUID.randomUUID().toString());//请求唯一编号
        requestParams.put("requestReason",requestReason);//1项目结清，2提前部分还款，3错误   请求原因
        List<ChangeRepayMentModel> list=new ArrayList<ChangeRepayMentModel>();
        ChangeRepayMentModel changeRepayMentModel=new ChangeRepayMentModel();
        changeRepayMentModel.setAssetRecycleDate(DateUtils.format(new Date(),"yyyy-MM-dd"));//计划还款日期
        changeRepayMentModel.setAssetPrincipal(new BigDecimal(amount));//计划还款本金
        changeRepayMentModel.setAssetInterest(new BigDecimal("0"));//计划还款利息
//        changeRepayMentModel.setServiceCharge(new BigDecimal("0"));
//        changeRepayMentModel.setMaintenanceCharge(new BigDecimal("0"));
//        changeRepayMentModel.setOtherCharge(new BigDecimal("0"));
        changeRepayMentModel.setAssetType(0);//0tiqian ,1zhenghcnag
        list.add(changeRepayMentModel);
        String requestData=JsonUtils.toJsonString(list);
        requestParams.put("requestData",requestData);//具体变更内容
        try {
           result = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }



//    public void changeRepaymentPlan(String url,String uniqueId,String contractNo,String requestReason,String type){
//        Map<String,String>requestParams = new HashMap<String,String>();
//        requestParams.put("fn", "200001");
//        requestParams.put("uniqueID",uniqueId);
//        requestParams.put("contractNo",contractNo);
//        requestParams.put("requestNo",UUID.randomUUID().toString());
//        requestParams.put("requestReason",requestReason);
//        requestParams.put("requestData", "[{'assetRecycleDate':'2017-07-04','assetPrincipal':'500','assetInterest':'0','serviceCharge':'0','maintenanceCharge':'0','otherCharge':'0','assetType':"+type+"},{'assetRecycleDate':'2017-09-02','assetPrincipal':'500','assetInterest':'0','serviceCharge':'0','maintenanceCharge':'0','otherCharge':'0','assetType':"+type+"}]");//0 提前 1 正常
//
//        try {
//            String sr = PostTestUtil.sendPost(url , requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
//            System.out.println(sr);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }




    /**
     * 申请提前还款
     *
     * @param uniqueId
     * @param assetInitialValue
     * @param assetPrincipal
     * @param assetRecycleDate
     * @return
     */
    public String applyPrepaymentPlan(String uniqueId, String assetInitialValue, String assetPrincipal, String assetRecycleDate) {
        String result = null;
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "200002");
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("contractNo", "");//贷款合同编号
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("assetRecycleDate", assetRecycleDate);//计划还款日期,yyyy-MM-dd
        requestParams.put("assetInitialValue", assetInitialValue);//还款金额
        requestParams.put("type", "0");//提前还款类型
        requestParams.put("payWay", "0");//还款方式，0:代扣;1:主动还款;2:委托转付;3:代偿
        requestParams.put("assetPrincipal", assetPrincipal);//提前还款本金
        requestParams.put("assetInterest", null);//提前还款利息
        requestParams.put("serviceCharge", null);//贷款服务费
        requestParams.put("maintenanceCharge", null);//技术维护费
        requestParams.put("otherCharge", null);//提前还款其他费用
        //requestParams.put("hasDeducted", "0");//申请当日要执行的还款计划的还款状态标记位，0,1

        try {
            result = PostTestUtil.sendPost(OuterInternat_modifyUrl, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 变更还款信息接口
     *
     * @param url
     * @param uniqueId
     * @param bankAccount
     */
    public void changeRepaymentInfo(String url,String uniqueId,String bankAccount) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "200003");
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("contractNo", "");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("bankCode", "C10102");//银行代码
        requestParams.put("bankAccount", bankAccount);//银行帐号
        requestParams.put("bankName", "宁波银行dzz");
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
     * 导入资产包
     *
     * @param totalAmount
     * @param productCode
     * @param uniqueId
     * @param repaymentAccountNo
     * @param amount
     * @param firstRepaymentDate
     * @param secondRepaymentDate
     * @param thirdRepaymentDate
     */
    public void importAssetPackage(String url, String totalAmount, String productCode, String uniqueId, String repaymentAccountNo, String amount, String firstRepaymentDate, String secondRepaymentDate, String thirdRepaymentDate) {
        for (int index = 0; index < 1; index++) {
            ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
            importAssetPackageContent.setFinancialProductCode(productCode);//信托产品代码
            importAssetPackageContent.setThisBatchContractsTotalNumber(1);//批次合同总条数
            importAssetPackageContent.setThisBatchContractsTotalAmount(totalAmount);//批次合同本金总额

            List<ContractDetail> contracts = new ArrayList<ContractDetail>();
            ContractDetail contractDetail = new ContractDetail();
            contractDetail.setUniqueId(uniqueId);//贷款合同唯一识别编号
            contractDetail.setLoanContractNo(uniqueId);//贷款合同编号

            contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());//贷款客户编号
            contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());//标的资产编号
            contractDetail.setLoanCustomerName("王宝");//贷款客户姓名,王宝
            contractDetail.setIDCardNo("320301198502169142");//贷款客户身份证号码,320301198502169142
            contractDetail.setBankCode("C10103");//还款账户开户行编号,C10105 宝付C10103
            contractDetail.setBankOfTheProvince("110000");//开户行所在省编号110000
            contractDetail.setBankOfTheCity("110100");//开户行所在市编号110100
            contractDetail.setRepaymentAccountNo("6228480444455553330");//还款账户号,"621485571210652" + repaymentAccountNo ，中金代扣6222020200002432，宝付6228480444455553333
            contractDetail.setLoanTotalAmount(totalAmount);//贷款本金总额
            contractDetail.setLoanPeriods(1);//贷款期数
            contractDetail.setEffectDate(DateUtils.format(new Date()));//设定生效日期
            contractDetail.setExpiryDate("2099-01-01");//设定到期日期
            contractDetail.setLoanRates("0.156");//贷款利率
            contractDetail.setInterestRateCycle(1);//利率周期
            contractDetail.setPenalty("0.0005");//罚息
            contractDetail.setRepaymentWay(2);//还款方法

            List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

            ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail1.setRepaymentPrincipal(amount);//还款本金
            repaymentPlanDetail1.setRepaymentInterest("0");//还款利息
            repaymentPlanDetail1.setRepaymentDate(firstRepaymentDate);//还款日期
            repaymentPlanDetail1.setOtheFee("0");//其他费用
            repaymentPlanDetail1.setTechMaintenanceFee("0");//技术服务费
            repaymentPlanDetail1.setLoanServiceFee("0");//贷款服务费
            repaymentPlanDetails.add(repaymentPlanDetail1);

            contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

            contracts.add(contractDetail);
            importAssetPackageContent.setContractDetails(contracts);

            String param = JsonUtils.toJsonString(importAssetPackageContent);
            System.out.println(param);
            Map<String, String> params = new HashMap<String, String>();
            params.put("fn", "200004");
            params.put("requestNo", UUID.randomUUID().toString());
            params.put("importAssetPackageContent", param);
            try {
                String sr = PostTestUtil.sendPost(url, params, new BaseApiTestPost().getIdentityInfoMap(params));
                System.out.println("===========" + sr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 更新逾期费用明细接口
     *
     * @param url
     */
    public void updateOverdueDetails(String url,String uniqueId,String RepaymentPlanNo) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "200005");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        ModifyOverDueFeeDetail modifyOverDueFeeDetail = new ModifyOverDueFeeDetail();
        modifyOverDueFeeDetail.setContractUniqueId(uniqueId);//贷款合同唯一识别编号
        modifyOverDueFeeDetail.setRepaymentPlanNo(RepaymentPlanNo);//还款计划编号
        modifyOverDueFeeDetail.setOverDueFeeCalcDate("2017-8-9");//逾期罚息计算日
        modifyOverDueFeeDetail.setPenaltyFee("0");//罚息
        modifyOverDueFeeDetail.setLatePenalty("0");//逾期违约金
        modifyOverDueFeeDetail.setLateFee("0");//逾期服务费
        modifyOverDueFeeDetail.setLateOtherCost("0");//逾期其他费用
        modifyOverDueFeeDetail.setTotalOverdueFee("0");//逾期费用合计

        List<ModifyOverDueFeeDetail> list = new ArrayList<>();
        list.add(modifyOverDueFeeDetail);
        String modifyOverDueFeeDetails = JsonUtils.toJsonString(list);
        requestParams.put("modifyOverDueFeeDetails", modifyOverDueFeeDetails);//逾期费用修改参数
        try {
            String result = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 费用浮动接口
     *
     * @param url
     * @param amount
     */
    public void floatingCharges(String url,String amount,String uniqueId,String produce,String repaymentPlanNo) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("financialProductCode", produce);
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("contractNo", "");
        requestParams.put("repaymentPlanNo", repaymentPlanNo);//还款计划
        requestParams.put("Details", "[{'feeType':'2','amount':'"+amount+"'},{'feeType':'3','amount':'50'},{'feeType':'4','amount':'50'}]");
        requestParams.put("reasonCode", "1");
        requestParams.put("approver", "戴智智");
        requestParams.put("approvedTime", "");
        requestParams.put("comment", "更改完成");
        try {
            String result = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 变更还款计划新版接口
     * @param url
     * */
    public void newChangeRepaymentPlan(String url){
        Map<String,String> requestParams=new HashMap<String,String>();
        requestParams.put("channelCode","zhongan");
        requestParams.put("serviceCode","");
        requestParams.put("uniqueId","");
        requestParams.put("contractNo","");
        requestParams.put("requestNo","");
        List<NewChangeRepaymentPlanModel> list=new ArrayList<>();

        NewChangeRepaymentPlanModel newChangeRepaymentPlanModel =new NewChangeRepaymentPlanModel();
        newChangeRepaymentPlanModel.setAssetRecycleDate("");
        newChangeRepaymentPlanModel.setAssetPrincipal(new BigDecimal(""));
        newChangeRepaymentPlanModel.setAssetInterest(new BigDecimal(""));
        newChangeRepaymentPlanModel.setServiceCharge(new BigDecimal(""));
        newChangeRepaymentPlanModel.setMaintenanceCharge(new BigDecimal(""));
        newChangeRepaymentPlanModel.setOtherCharge(new BigDecimal(""));
        newChangeRepaymentPlanModel.setAssetType(0);

        list.add(newChangeRepaymentPlanModel);
        String sr=JsonUtils.toJsonString(list);
        requestParams.put("requestData",sr);
        try {
            String result = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    /**
     * 扣款
     *
     * @param repaymentNumber
     * @param uniqueId
     * @param productCode
     * @param amount
     * @param repaymentType
     */
    public void deductRepaymentPlan(String url,String repaymentNumber, String uniqueId, String productCode, String amount, String repaymentType) {
        /**/
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300001");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("deductId", UUID.randomUUID().toString());
        requestParams.put("financialProductCode", productCode);
        requestParams.put("uniqueId", uniqueId);//贷款合同唯一编号
        requestParams.put("apiCalledTime", DateUtils.format(new Date()));//接口调用时间
        requestParams.put("amount", amount);//扣款金额
        requestParams.put("repaymentType", repaymentType);//1、正常  0、提前划扣  2逾期
        requestParams.put("mobile", "13777847783");
        requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':" + amount + ",'repaymentInterest':0.00,'repaymentPlanNo':'" + repaymentNumber + "','repaymentPrincipal':" + amount + ",'techFee':0.00,'overDueFeeDetail':{" + "'totalOverdueFee':0.00}}]");
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
            String param = JsonUtils.toJsonString(requestParams);
            System.out.println("扣款信息：" + param);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 放款
     *
     * @param productCode
     * @param uniqueId
     * @param amount
     */
    public void makeLoan(String productCode, String uniqueId, String amount, String url) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300002");
        requestParams.put("requestNo", UUID.randomUUID().toString());//请求唯一标识
        requestParams.put("productCode", productCode);//信托产品代码
        requestParams.put("uniqueId", uniqueId);//贷款合同唯一编号
        requestParams.put("contractNo", uniqueId);//贷款合同编号
        requestParams.put("contractAmount", amount);//贷款合同本金
        requestParams.put("plannedRemittanceAmount", amount);//计划放款金额
        requestParams.put("auditorName", "auditorName1");//审核人
        requestParams.put("auditTime", "2016-08-20 00:00:00");//审核时间
        requestParams.put("remark", "交易备注");//放款备注
        requestParams.put("remittanceStrategy", "0");//放款策略
        String bankCardNo = "6214855712106520";//'bankCode':'C10102'     中金bankCardNo = "6214855712106520"  宝付6228480444455553333  广银联尾号是0就都可以 交易方银行卡号

        requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'" + amount + "','bankCode':'C10103','bankCardNo':'" + bankCardNo + "','bankAccountHolder':'王宝','bankProvince':'310000','bankCity':'310100','bankName':'中国农业银行','idNumber':'idNumber1'}]");
        //宝付
        //requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'" + amount + "','bankCode':'C10102','bankCardNo':'" + bankCardNo + "','bankAccountHolder':'尹邦凤','bankProvince':'310000','bankCity':'310100','bankName':'中国农业银行','idNumber':'idNumber1'}]");

        //requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'" + amount + "','bankCode':'C10102','bankCardNo':'" + bankCardNo + "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
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
    public void merchantVoucher(String url, String uniqueId, String RepaymentPlanNo, String amount) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300003");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("transactionType", "0");//交易类型
        requestParams.put("voucherType", "7");//凭证类型
        requestParams.put("voucherAmount", amount);//凭证金额
        requestParams.put("financialContractNo", "G31700");//信托产品代码
        requestParams.put("receivableAccountNo", "600000000001");//收款银行账户号
        requestParams.put("paymentAccountNo", "1001133419006708197");//付款银行账户号
        requestParams.put("bankTransactionNo", UUID.randomUUID().toString());//付款银行流水号
        requestParams.put("paymentName", "上海拍拍贷金融信息服务有限公司");//付款银行帐户名称
        requestParams.put("paymentBank", "宁波银行dzz");//付款银行名称

        List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
        BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
        detail.setUniqueId(uniqueId);//贷款合同唯一标识
        detail.setRepaymentPlanNo(RepaymentPlanNo);//还款计划编号
        detail.setAmount(new BigDecimal(amount));//金额
        detail.setPayer(1);//付款人,0:贷款人，1:商户
        detail.setPrincipal(new BigDecimal(amount));//还款本金
        detail.setInterest(new BigDecimal("0"));//还款利息
        detail.setServiceCharge(new BigDecimal("0"));//贷款服务费
        detail.setMaintenanceCharge(new BigDecimal("0"));//技术维护费
        detail.setOtherCharge(new BigDecimal("0"));//其他费用
        detail.setPenaltyFee(new BigDecimal("0"));//罚息
        detail.setLatePenalty(new BigDecimal("0"));//逾期违约金
        detail.setLateFee(new BigDecimal("0"));//逾期服务费
        detail.setLateOtherCost(new BigDecimal("0"));//逾期其他费用
        detail.setTransactionTime("2017-05-31 00:00:00");//还款时间0001-01-01 00:00:00
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
     * 主动付款凭证接口
     *
     * @param url
     * */
    public void activePaymentVoucher(String url,String uniqueId,String amount,String repaymentPlanNo){
        Map<String,String> requestParams=new HashMap<String,String>();
        requestParams.put("fn","300004");
        requestParams.put("requestNo",UUID.randomUUID().toString());
        requestParams.put("uniqueId",uniqueId);
        requestParams.put("contractNo","");
        requestParams.put("transactionType","0");//0:提交操作，1:撤销操作
        requestParams.put("voucherType","5");//5主动付款，6他人代付
        requestParams.put("voucherAmount",amount);//凭证金额
        requestParams.put("receivableAccountNo","600000000001");//收款银行帐户号
        requestParams.put("paymentAccountNo","1001133419006708197");//付款银行帐户号
        requestParams.put("bankTransactionNo",UUID.randomUUID().toString());//付款银行流水号
        requestParams.put("paymentName","上海拍拍贷金融信息服务有限公司");
        requestParams.put("paymentBank","宁波银行dzz");
        List<ActivePaymentVoucherDetailModel> list=new ArrayList<>();
        ActivePaymentVoucherDetailModel activePaymentVoucherDetailModel=new ActivePaymentVoucherDetailModel();
        activePaymentVoucherDetailModel.setRepaymentPlanNo(repaymentPlanNo);//还款计划编号
        activePaymentVoucherDetailModel.setAmount(new BigDecimal(amount));//金额
//        activePaymentVoucherDetailModel.setPrincipal(new BigDecimal(""));
//        activePaymentVoucherDetailModel.setInterest(new BigDecimal(""));
//        activePaymentVoucherDetailModel.setServiceCharge(new BigDecimal(""));
//        activePaymentVoucherDetailModel.setMaintenanceCharge(new BigDecimal(""));
//        activePaymentVoucherDetailModel.setOtherCharge(new BigDecimal(""));
//        activePaymentVoucherDetailModel.setPenaltyFee(new BigDecimal(""));
//        activePaymentVoucherDetailModel.setLatePenalty(new BigDecimal(""));
//        activePaymentVoucherDetailModel.setLateFee(new BigDecimal(""));
//        activePaymentVoucherDetailModel.setLateOtherCost(new BigDecimal(""));
        list.add(activePaymentVoucherDetailModel);

        String detail=JsonUtils.toJsonString(list);
        requestParams.put("detail",detail);//当前凭证付款明细
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 取消贷款接口
     * @param url
     * @param productCode
     * @param uniqueId
     * */
    public void cancalLoan(String url,String productCode,String uniqueId){
        Map<String,String> requestParams=new HashMap<String,String>();
        requestParams.put("fn","300005");
        requestParams.put("requestNo",UUID.randomUUID().toString());
        requestParams.put("productCode",productCode);
        requestParams.put("uniqueId",uniqueId);
        requestParams.put("contractNo","");
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *回购申请接口
     * @param url
     * */
    public void repurchaseApplication(String url,String productCode,String uniqueId,String batchNo){
        Map<String,String> requestParams=new HashMap<>();
        requestParams.put("requestNo",UUID.randomUUID().toString());
        requestParams.put("batchNo",batchNo);//选填
        requestParams.put("transactionType","0");//交易类型，0提交，1撤销
        requestParams.put("financialContractNo",productCode);//信托合同代码,信托合同需支持回购
        requestParams.put("reviewer","");//审核人
        requestParams.put("reviewTimeString",DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));//审核时间

        List<RepurchaseDetailModel> list=new ArrayList<>();
        RepurchaseDetailModel repurchaseDetailModel=new RepurchaseDetailModel();
        repurchaseDetailModel.setUniqueId(uniqueId);
        repurchaseDetailModel.setContractNo("");//选填
        repurchaseDetailModel.setPrincipal(new BigDecimal("0"));//选填
        repurchaseDetailModel.setInterest(new BigDecimal("0"));//选填
        repurchaseDetailModel.setPenaltyInterest(new BigDecimal("0"));//选填
        repurchaseDetailModel.setRepurchaseOtherFee(new BigDecimal("0"));//选填
        repurchaseDetailModel.setAmount(new BigDecimal("0"));//选填

        list.add(repurchaseDetailModel);
        String repurchaseDetail=JsonUtils.toJsonString(list);
        requestParams.put("repurchaseDetail",repurchaseDetail);
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
    public void MakeVOucher(String url, String transactionRequestNo, String uniqueId, String RepaymentPlanNo, String amount,String financialContractNo) {
        List<ThirdPartVoucherDetailModel> models = new ArrayList<ThirdPartVoucherDetailModel>();
        String VoucherNo = UUID.randomUUID().toString();//凭证编号
        for (int i = 1; i <= 1; i++) {
            for (int y = 1; y <= 1; y++) {
                ThirdPartVoucherDetailModel model = new ThirdPartVoucherDetailModel();
                model.setVoucherNo(VoucherNo);//凭证编号:查重（凭证唯一标识，不允许存在两张编号相同的凭证）
                model.setBankTransactionNo(UUID.randomUUID().toString());//交易流水号
                model.setTransactionRequestNo(transactionRequestNo);//交易请求号
                model.setTransactionTime(com.demo2do.core.utils.DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));//交易发起时间
                model.setTransactionGateway(5);//交易网关，3新浪支付，1宝付，0银联0广州 4中金 5易极付
                model.setCurrency(0);//交易币种
                model.setAmount(new BigDecimal(amount));//交易金额
                model.setContractUniqueId(uniqueId);//贷款合同唯一编号
                model.setCompleteTime(DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));//交易完成时间
                model.setReceivableAccountNo("12345tyui");//收款银行帐户号
                model.setPaymentBank("23erdzz10");//来往机构
                model.setPaymentName("qwertyu");//付款帐户名称
                model.setPaymentAccountNo("23r4t5y6i8o9");//付款帐户号
                model.setCustomerName("dzz");//客户姓名
                model.setCustomerIdNo("1234567890-");//身份证号码
                model.setComment("213456");//备注

                ThirdPartVoucherRepayDetailModel detailModel = new ThirdPartVoucherRepayDetailModel();
                detailModel.setRepaymentPlanNo(RepaymentPlanNo);//还款计划编号
                detailModel.setPrincipal(new BigDecimal(amount));//还款本金
                detailModel.setInterest(BigDecimal.ZERO);//还款利息
                detailModel.setServiceCharge(new BigDecimal("0"));//贷款服务费
                detailModel.setMaintenanceCharge(new BigDecimal("0"));//技术维护费
                detailModel.setOtherCharge(new BigDecimal("0"));//其他费用
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
        params.put("financialContractNo", financialContractNo);
        params.put("detailList", content);

//		Map<String, String> headers = new HashMap<String, String>();
//		headers.put("merId", "t_test_zfb");
//		headers.put("secret", "123456");
//		String signContent = ApiSignUtils.getSignCheckContent(params);
//		String sign = ApiSignUtils.rsaSign(signContent, privateKey);
//		headers.put("sign", sign);

        try {
            String sr = PostTestUtil.sendPost(url, params, new BaseApiTestPost().getIdentityInfoMap(params));
            System.out.println(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 制造第三方凭证，其中包含三个还款计划
     *
     * @param url
     * @param transactionRequestNo
     * @param uniqueId
     * @return
     */
    public void MakeVOucher2(String url, String transactionRequestNo, String uniqueId,String productCode,String totalamount,String amount) {
        List<ThirdPartVoucherDetailModel> models = new ArrayList<ThirdPartVoucherDetailModel>();
        String VoucherNo = UUID.randomUUID().toString();//凭证编号
        for (int i = 1; i <= 1; i++) {
            for (int y = 1; y <= 1; y++) {
                ThirdPartVoucherDetailModel model = new ThirdPartVoucherDetailModel();
                model.setVoucherNo(VoucherNo);//凭证编号:查重（凭证唯一标识，不允许存在两张编号相同的凭证）
                model.setBankTransactionNo(UUID.randomUUID().toString());//交易流水号
                model.setTransactionRequestNo(transactionRequestNo);//交易请求号
                model.setTransactionTime(com.demo2do.core.utils.DateUtils.format(new Date()));//交易发起时间
                model.setTransactionGateway(0);//交易网关，3新浪支付，1宝付，0银联0广州 4中金
                model.setCurrency(0);//交易币种
                model.setAmount(new BigDecimal(totalamount));//交易金额
                model.setContractUniqueId(uniqueId);//贷款合同唯一编号
                model.setCompleteTime("2017-6-30 00:00:00");//交易完成时间
                model.setReceivableAccountNo("12345tyui");//收款银行帐户号
                model.setPaymentBank("dzz9");//来往机构
                model.setPaymentName("dzz2");//付款帐户名称
                model.setPaymentAccountNo("23r4t5y6i8o9");//付款帐户号
                model.setCustomerName("dzz2");//客户姓名
                model.setCustomerIdNo("1234567890-");//身份证号码
                model.setComment("213456");//备注

                ThirdPartVoucherRepayDetailModel detailModel = new ThirdPartVoucherRepayDetailModel();
                // 查询first还款编号
                String RepaymentPlanNo1 = queryFirstRepaymentPlan(uniqueId, 0);
                System.out.println("查询还款编号1:" + RepaymentPlanNo1);
                detailModel.setRepaymentPlanNo(RepaymentPlanNo1);//还款计划编号
                detailModel.setPrincipal(new BigDecimal(amount));//还款本金
                detailModel.setInterest(BigDecimal.ZERO);//还款利息
                detailModel.setServiceCharge(BigDecimal.ZERO);//贷款服务费
                detailModel.setMaintenanceCharge(BigDecimal.ZERO);//技术维护费
                detailModel.setOtherCharge(BigDecimal.ZERO);//其他费用
                detailModel.setPenaltyFee(BigDecimal.ZERO);//罚息
                detailModel.setLatePenalty(BigDecimal.ZERO);//逾期违约金
                detailModel.setLateFee(BigDecimal.ZERO);//逾期服务费
                detailModel.setLateOtherCost(BigDecimal.ZERO);//逾期其他费用
                detailModel.setAmount(new BigDecimal(amount));//明细金额总和
                List<ThirdPartVoucherRepayDetailModel> detailModels = new ArrayList<ThirdPartVoucherRepayDetailModel>();
                detailModels.add(detailModel);

				ThirdPartVoucherRepayDetailModel detailModel2 = new ThirdPartVoucherRepayDetailModel();
				// 查询second还款编号
				String RepaymentPlanNo2=queryFirstRepaymentPlan(uniqueId,1);
				System.out.println("查询还款编号2:"+RepaymentPlanNo2);
				detailModel2.setRepaymentPlanNo(RepaymentPlanNo2);//还款计划编号
				detailModel2.setPrincipal(new BigDecimal(amount));//还款本金
				detailModel2.setInterest(BigDecimal.ZERO);//还款利息
				detailModel2.setServiceCharge(BigDecimal.ZERO);//贷款服务费
				detailModel2.setMaintenanceCharge(BigDecimal.ZERO);//技术维护费
				detailModel2.setOtherCharge(BigDecimal.ZERO);//其他费用
				detailModel2.setPenaltyFee(BigDecimal.ZERO);//罚息
				detailModel2.setLatePenalty(BigDecimal.ZERO);//逾期违约金
				detailModel2.setLateFee(BigDecimal.ZERO);//逾期服务费
				detailModel2.setLateOtherCost(BigDecimal.ZERO);//逾期其他费用
				detailModel2.setAmount(new BigDecimal(amount));//明细金额总和
				detailModels.add(detailModel2);

				ThirdPartVoucherRepayDetailModel detailModel3 = new ThirdPartVoucherRepayDetailModel();
				// 查询third还款编号
				System.out.println("查询还款编号:");
				String RepaymentPlanNo3=queryFirstRepaymentPlan(uniqueId,2);
				detailModel3.setRepaymentPlanNo(RepaymentPlanNo3);//还款计划编号
				detailModel3.setPrincipal(new BigDecimal(amount));//还款本金
				detailModel3.setInterest(BigDecimal.ZERO);//还款利息
				detailModel3.setServiceCharge(BigDecimal.ZERO);//贷款服务费
				detailModel3.setMaintenanceCharge(BigDecimal.ZERO);//技术维护费
				detailModel3.setOtherCharge(BigDecimal.ZERO);//其他费用
				detailModel3.setPenaltyFee(BigDecimal.ZERO);//罚息
				detailModel3.setLatePenalty(BigDecimal.ZERO);//逾期违约金
				detailModel3.setLateFee(BigDecimal.ZERO);//逾期服务费
				detailModel3.setLateOtherCost(BigDecimal.ZERO);//逾期其他费用
				detailModel3.setAmount(new BigDecimal(amount));//明细金额总和\
				detailModels.add(detailModel3);

                model.setRepayDetailList(detailModels);
                models.add(model);


            }
        }

        String content = com.suidifu.hathaway.util.JsonUtils.toJsonString(models);
        System.out.println(content);
        Map<String, String> params = new HashMap<String, String>();
        params.put("fn", "300006");
        params.put("requestNo", UUID.randomUUID().toString());
        params.put("financialContractNo", productCode);
        params.put("detailList", content);

//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("merId", "t_test_zfb");
//        headers.put("secret", "123456");
//        String signContent = ApiSignUtils.getSignCheckContent(params);
//        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
//        headers.put("sign", sign);

        try {
            String sr = PostTestUtil.sendPost(url, params, new BaseApiTestPost().getIdentityInfoMap(params));
            System.out.println(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 制造资产包，其中包含三个还款计划
     *
     * @param url
     * @param totalAmount
     * @param productCode
     * @param uniqueId
     * @param repaymentAccountNo
     * @param amount
     * @param firstRepaymentDate
     * @param secondRepaymentDate
     * @return
     */
    public void importAssetPackage2(String url, String totalAmount, String productCode, String uniqueId, String repaymentAccountNo, String amount, String firstRepaymentDate, String secondRepaymentDate, String thirdRepaymentDate) {
        for (int index = 0; index < 1; index++) {
            ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
            importAssetPackageContent.setFinancialProductCode(productCode);//信托产品代码
            importAssetPackageContent.setThisBatchContractsTotalNumber(1);//批次合同总条数
            importAssetPackageContent.setThisBatchContractsTotalAmount(totalAmount);//批次合同本金总额

            List<ContractDetail> contracts = new ArrayList<ContractDetail>();
            ContractDetail contractDetail = new ContractDetail();
            contractDetail.setUniqueId(uniqueId);//贷款合同唯一识别编号
            contractDetail.setLoanContractNo(uniqueId);//贷款合同编号

            contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());//贷款客户编号
            contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());//标的资产编号
            contractDetail.setLoanCustomerName("王宝");//贷款客户姓名
            contractDetail.setIDCardNo("320301198502169142");//贷款客户身份证号码
            //contractDetail.setBankCode("C10105");//还款账户开户行编号
            contractDetail.setBankCode("C10103");//还款账户开户行编号

            contractDetail.setBankOfTheProvince("110000");//开户行所在省编号
            contractDetail.setBankOfTheCity("110100");//开户行所在市编号
            //contractDetail.setRepaymentAccountNo("621485571210652" + repaymentAccountNo);
            contractDetail.setRepaymentAccountNo("6228480444455553330"); //还款账户号  宝付6228480444455553333
            contractDetail.setLoanTotalAmount(totalAmount);//贷款本金总额
            contractDetail.setLoanPeriods(3);//贷款期数
            contractDetail.setEffectDate(DateUtils.format(new Date()));//设定生效日期
            contractDetail.setExpiryDate("2099-01-01");//设定到期日期
            contractDetail.setLoanRates("0.156");//贷款利率
            contractDetail.setInterestRateCycle(1);//利率周期
            contractDetail.setPenalty("0.0005");//罚息
            contractDetail.setRepaymentWay(1);//还款方法

            List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

            ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail1.setRepaymentPrincipal(amount);//还款本金
            repaymentPlanDetail1.setRepaymentInterest("0.00");//还款利息
            repaymentPlanDetail1.setRepaymentDate(firstRepaymentDate);//还款日期
            repaymentPlanDetail1.setOtheFee("0.00");//其他费用
            repaymentPlanDetail1.setTechMaintenanceFee("0.00");//技术服务费
            repaymentPlanDetail1.setLoanServiceFee("0.00");//贷款服务费
            repaymentPlanDetails.add(repaymentPlanDetail1);

            ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail2.setRepaymentPrincipal(amount);
            repaymentPlanDetail2.setRepaymentInterest("0.00");
            repaymentPlanDetail2.setRepaymentDate(secondRepaymentDate);
            repaymentPlanDetail2.setOtheFee("0.00");
            repaymentPlanDetail2.setTechMaintenanceFee("0.00");
            repaymentPlanDetail2.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail2);

            ImportRepaymentPlanDetail repaymentPlanDetail3 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail3.setRepaymentPrincipal(amount);
            repaymentPlanDetail3.setRepaymentInterest("0.00");
            repaymentPlanDetail3.setRepaymentDate(thirdRepaymentDate);
            repaymentPlanDetail3.setOtheFee("0.00");
            repaymentPlanDetail3.setTechMaintenanceFee("0.00");
            repaymentPlanDetail3.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail3);

            contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

            contracts.add(contractDetail);
            importAssetPackageContent.setContractDetails(contracts);

            String param = JsonUtils.toJsonString(importAssetPackageContent);
            System.out.println(param);
            Map<String, String> params = new HashMap<String, String>();
            params.put("fn", "200004");
            params.put("requestNo", UUID.randomUUID().toString());
            params.put("importAssetPackageContent", param);

//            Map<String, String> headers = new HashMap<String, String>();
//            headers.put("merId", "t_test_zfb");
//            headers.put("secret", "123456");
//            String signContent = ApiSignUtils.getSignCheckContent(params);
//            String sign = ApiSignUtils.rsaSign(signContent, privateKey);
//            System.out.println(sign);
//            headers.put("sign", sign);

            try {
                String sr = PostTestUtil.sendPost(url, params, new BaseApiTestPost().getIdentityInfoMap(params));
                System.out.println("===========" + index + sr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * 新增放款回调的补充机制
     *
     * @param url
     * @param uniqueId
     * @param amount
     * @param notifyUrl
     * @return
     */
    public void LoanCallBack(String url, String uniqueId, String amount, String notifyUrl) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300002");
        requestParams.put("requestNo", UUID.randomUUID().toString());//请求唯一标识
        requestParams.put("productCode", "G31700");//信托产品代码
        requestParams.put("uniqueId", uniqueId);//贷款合同唯一编号
        requestParams.put("contractNo", uniqueId);//贷款合同编号
        requestParams.put("contractAmount", amount);//贷款合同本金
        requestParams.put("plannedRemittanceAmount", amount);//计划放款金额
        requestParams.put("clearingAccount", "");//委托代扣清算号(先放后扣放款策略下必填)
        requestParams.put("auditorName", "dzz");//审核人
        requestParams.put("auditTime", "2016-08-20 00:00:00");//审核时间
        requestParams.put("remark", "交易备注");//放款备注
        requestParams.put("remittanceStrategy", "0");//放款策略
        requestParams.put("notifyUrl", notifyUrl);//回调地址,放款结果通知回调地址
        String bankCardNo = "6214855712106520";//交易方银行卡号
        requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'" + amount + "','plannedDate':'2016-08-20 00:00:00','bankCode':'C10102','bankCardNo':'" + bankCardNo + "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 制造第三方凭证，其中包含三个还款计划
     *
     * @param url
     * @param transactionRequestNo
     * @param uniqueId
     * @return
     */
//    public void MakeVOucher3(String url, String transactionRequestNo, String uniqueId,String productCode) {
//        List<ThirdPartVoucherDetailModel> models = new ArrayList<ThirdPartVoucherDetailModel>();
//        String VoucherNo = UUID.randomUUID().toString();//凭证编号
//        for (int i = 1; i <= 1; i++) {
//            for (int y = 1; y <= 1; y++) {
//                ThirdPartVoucherDetailModel model = new ThirdPartVoucherDetailModel();
//                model.setVoucherNo(VoucherNo);//凭证编号:查重（凭证唯一标识，不允许存在两张编号相同的凭证）
//                model.setBankTransactionNo(UUID.randomUUID().toString());//交易流水号
//                model.setTransactionRequestNo(transactionRequestNo);//交易请求号
//                model.setTransactionTime(com.demo2do.core.utils.DateUtils.format(new Date()));//交易发起时间
//                model.setTransactionGateway(1);//交易网关交易网关，3新浪支付，1宝付，0银联0广州 4中金
//                model.setCurrency(0);//交易币种
//                model.setAmount(new BigDecimal("1300"));//交易金额
//                model.setContractUniqueId(uniqueId);//贷款合同唯一编号
//                model.setCompleteTime("2017-6-30 00:00:00");//交易完成时间
//                model.setReceivableAccountNo("12345tyui");//收款银行帐户号
//                model.setPaymentBank("dzz10");//来往机构
//                model.setPaymentName("dzz2");//付款帐户名称
//                model.setPaymentAccountNo("23r4t5y6i8o9");//付款帐户号
//                model.setCustomerName("dzz2");//客户姓名
//                model.setCustomerIdNo("1234567890-");//身份证号码
//                model.setComment("213456");//备注
//
//                ThirdPartVoucherRepayDetailModel detailModel = new ThirdPartVoucherRepayDetailModel();
//                // 查询first还款编号
//                String RepaymentPlanNo1 = queryFirstRepaymentPlan(uniqueId, 0);
//                System.out.println("查询还款编号:" + RepaymentPlanNo1);
//                detailModel.setRepaymentPlanNo(RepaymentPlanNo1);//还款计划编号
//                detailModel.setPrincipal(new BigDecimal("300"));//还款本金
//                detailModel.setInterest(BigDecimal.ZERO);//还款利息
//                detailModel.setServiceCharge(BigDecimal.ZERO);//贷款服务费
//                detailModel.setMaintenanceCharge(BigDecimal.ZERO);//技术维护费
//                detailModel.setOtherCharge(BigDecimal.ZERO);//其他费用
//                detailModel.setPenaltyFee(BigDecimal.ZERO);//罚息
//                detailModel.setLatePenalty(BigDecimal.ZERO);//逾期违约金
//                detailModel.setLateFee(BigDecimal.ZERO);//逾期服务费
//                detailModel.setLateOtherCost(BigDecimal.ZERO);//逾期其他费用
//                detailModel.setAmount(new BigDecimal("300"));//明细金额总和
//                List<ThirdPartVoucherRepayDetailModel> detailModels = new ArrayList<ThirdPartVoucherRepayDetailModel>();
//                detailModels.add(detailModel);
//
//                ThirdPartVoucherRepayDetailModel detailModel2 = new ThirdPartVoucherRepayDetailModel();
//                // 查询second还款编号
//                String RepaymentPlanNo2 = queryFirstRepaymentPlan(uniqueId, 1);
//                System.out.println("查询还款编号:" + RepaymentPlanNo2);
//                detailModel2.setRepaymentPlanNo(RepaymentPlanNo2);//还款计划编号
//                detailModel2.setPrincipal(new BigDecimal("500"));//还款本金
//                detailModel2.setInterest(BigDecimal.ZERO);//还款利息
//                detailModel2.setServiceCharge(BigDecimal.ZERO);//贷款服务费
//                detailModel2.setMaintenanceCharge(BigDecimal.ZERO);//技术维护费
//                detailModel2.setOtherCharge(BigDecimal.ZERO);//其他费用
//                detailModel2.setPenaltyFee(BigDecimal.ZERO);//罚息
//                detailModel2.setLatePenalty(BigDecimal.ZERO);//逾期违约金
//                detailModel2.setLateFee(BigDecimal.ZERO);//逾期服务费
//                detailModel2.setLateOtherCost(BigDecimal.ZERO);//逾期其他费用
//                detailModel2.setAmount(new BigDecimal("500"));//明细金额总和
//                detailModels.add(detailModel2);
//
//                ThirdPartVoucherRepayDetailModel detailModel3 = new ThirdPartVoucherRepayDetailModel();
//                // 查询third还款编号
//                String RepaymentPlanNo3 = queryFirstRepaymentPlan(uniqueId, 2);
//                System.out.println("查询还款编号:" + RepaymentPlanNo3);
//                detailModel3.setRepaymentPlanNo(RepaymentPlanNo3);//还款计划编号
//                detailModel3.setPrincipal(new BigDecimal("500"));//还款本金
//                detailModel3.setInterest(BigDecimal.ZERO);//还款利息
//                detailModel3.setServiceCharge(BigDecimal.ZERO);//贷款服务费
//                detailModel3.setMaintenanceCharge(BigDecimal.ZERO);//技术维护费
//                detailModel3.setOtherCharge(BigDecimal.ZERO);//其他费用
//                detailModel3.setPenaltyFee(BigDecimal.ZERO);//罚息
//                detailModel3.setLatePenalty(BigDecimal.ZERO);//逾期违约金
//                detailModel3.setLateFee(BigDecimal.ZERO);//逾期服务费
//                detailModel3.setLateOtherCost(BigDecimal.ZERO);//逾期其他费用
//                detailModel3.setAmount(new BigDecimal("500"));//明细金额总和\
//                detailModels.add(detailModel3);
//
//                model.setRepayDetailList(detailModels);
//                models.add(model);
//
//
//            }
//        }
//
//        String content = com.suidifu.hathaway.util.JsonUtils.toJsonString(models);
//        System.out.println(content);
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("fn", "300006");
//        params.put("requestNo", UUID.randomUUID().toString());
//        params.put("financialContractNo", productCode);
//        params.put("detailList", content);
//
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("merId", "t_test_zfb");
//        headers.put("secret", "123456");
//        String signContent = ApiSignUtils.getSignCheckContent(params);
//        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
//        headers.put("sign", sign);
//
//        try {
//            String sr = PostTestUtil.sendPost(url, params, headers);
//            System.out.println(sr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 制造第三方凭证，其中包含三个还款计划
     *
     * @param url
     * @param transactionRequestNo
     * @param uniqueId
     * @return
     */
//    public void MakeVOucher4(String url, String transactionRequestNo, String uniqueId,String productCode) {
//        List<ThirdPartVoucherDetailModel> models = new ArrayList<ThirdPartVoucherDetailModel>();
//        String VoucherNo = UUID.randomUUID().toString();//凭证编号
//        for (int i = 1; i <= 1; i++) {
//            for (int y = 1; y <= 1; y++) {
//                ThirdPartVoucherDetailModel model = new ThirdPartVoucherDetailModel();
//                model.setVoucherNo(VoucherNo);//凭证编号:查重（凭证唯一标识，不允许存在两张编号相同的凭证）
//                model.setBankTransactionNo(UUID.randomUUID().toString());//交易流水号
//                model.setTransactionRequestNo(transactionRequestNo);//交易请求号
//                model.setTransactionTime(com.demo2do.core.utils.DateUtils.format(new Date()));//交易发起时间
//                model.setTransactionGateway(4);//交易网关，3新浪支付，1宝付，0银联0广州 4中金
//                model.setCurrency(0);//交易币种
//                model.setAmount(new BigDecimal("20"));//交易金额
//                model.setContractUniqueId(uniqueId);//贷款合同唯一编号
//                model.setCompleteTime("2017-6-30 00:00:00");//交易完成时间
//                model.setReceivableAccountNo("12345tyui");//收款银行帐户号
//                model.setPaymentBank("dzz11");//来往机构
//                model.setPaymentName("dzz2");//付款帐户名称
//                model.setPaymentAccountNo("23r4t5y6i8o9");//付款帐户号
//                model.setCustomerName("dzz2");//客户姓名
//                model.setCustomerIdNo("1234567890-");//身份证号码
//                model.setComment("213456");//备注
//
//                ThirdPartVoucherRepayDetailModel detailModel = new ThirdPartVoucherRepayDetailModel();
//                // 查询first还款编号
//                String RepaymentPlanNo1 = queryFirstRepaymentPlan(uniqueId, 0);
//                System.out.println("查询还款编号1:" + RepaymentPlanNo1);
//                detailModel.setRepaymentPlanNo(RepaymentPlanNo1);//还款计划编号
//                detailModel.setPrincipal(new BigDecimal("0"));//还款本金
//                detailModel.setInterest(BigDecimal.ZERO);//还款利息
//                detailModel.setServiceCharge(BigDecimal.ZERO);//贷款服务费
//                detailModel.setMaintenanceCharge(BigDecimal.ZERO);//技术维护费
//                detailModel.setOtherCharge(BigDecimal.ZERO);//其他费用
//                detailModel.setPenaltyFee(BigDecimal.ZERO);//罚息
//                detailModel.setLatePenalty(BigDecimal.ZERO);//逾期违约金
//                detailModel.setLateFee(BigDecimal.ZERO);//逾期服务费
//                detailModel.setLateOtherCost(BigDecimal.ZERO);//逾期其他费用
//                detailModel.setAmount(new BigDecimal("0"));//明细金额总和
//                List<ThirdPartVoucherRepayDetailModel> detailModels = new ArrayList<ThirdPartVoucherRepayDetailModel>();
//                detailModels.add(detailModel);
//
//                ThirdPartVoucherRepayDetailModel detailModel2 = new ThirdPartVoucherRepayDetailModel();
//                // 查询second还款编号
//                String RepaymentPlanNo2 = queryFirstRepaymentPlan(uniqueId, 1);
//                System.out.println("查询还款编号2:" + RepaymentPlanNo2);
//                detailModel2.setRepaymentPlanNo(RepaymentPlanNo2);//还款计划编号
//                detailModel2.setPrincipal(new BigDecimal("20"));//还款本金
//                detailModel2.setInterest(BigDecimal.ZERO);//还款利息
//                detailModel2.setServiceCharge(BigDecimal.ZERO);//贷款服务费
//                detailModel2.setMaintenanceCharge(BigDecimal.ZERO);//技术维护费
//                detailModel2.setOtherCharge(BigDecimal.ZERO);//其他费用
//                detailModel2.setPenaltyFee(BigDecimal.ZERO);//罚息
//                detailModel2.setLatePenalty(BigDecimal.ZERO);//逾期违约金
//                detailModel2.setLateFee(BigDecimal.ZERO);//逾期服务费
//                detailModel2.setLateOtherCost(BigDecimal.ZERO);//逾期其他费用
//                detailModel2.setAmount(new BigDecimal("20"));//明细金额总和
//                detailModels.add(detailModel2);
//
//                //ThirdPartVoucherRepayDetailModel detailModel3 = new ThirdPartVoucherRepayDetailModel();
//                // 查询third还款编号
////				String RepaymentPlanNo3=queryFirstRepaymentPlan(uniqueId,2);
////				System.out.println("查询还款编号:"+RepaymentPlanNo3);
////				detailModel3.setRepaymentPlanNo(RepaymentPlanNo3);//还款计划编号
////				detailModel3.setPrincipal(new BigDecimal("500"));//还款本金
////				detailModel3.setInterest(BigDecimal.ZERO);//还款利息
////				detailModel3.setServiceCharge(BigDecimal.ZERO);//贷款服务费
////				detailModel3.setMaintenanceCharge(BigDecimal.ZERO);//技术维护费
////				detailModel3.setOtherCharge(BigDecimal.ZERO);//其他费用
////				detailModel3.setPenaltyFee(BigDecimal.ZERO);//罚息
////				detailModel3.setLatePenalty(BigDecimal.ZERO);//逾期违约金
////				detailModel3.setLateFee(BigDecimal.ZERO);//逾期服务费
////				detailModel3.setLateOtherCost(BigDecimal.ZERO);//逾期其他费用
////				detailModel3.setAmount(new BigDecimal("500"));//明细金额总和\
////				detailModels.add(detailModel3);
//
//                model.setRepayDetailList(detailModels);
//                models.add(model);
//
//
//            }
//        }
//
//        String content = com.suidifu.hathaway.util.JsonUtils.toJsonString(models);
//        System.out.println(content);
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("fn", "300006");
//        params.put("requestNo", UUID.randomUUID().toString());
//        params.put("financialContractNo", productCode);
//        params.put("detailList", content);
//
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("merId", "t_test_zfb");
//        headers.put("secret", "123456");
//        String signContent = ApiSignUtils.getSignCheckContent(params);
//        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
//        headers.put("sign", sign);
//
//        try {
//            String sr = PostTestUtil.sendPost(url, params, headers);
//            System.out.println(sr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 还款订单接口测试
     * @param url
     * @param uniqueId1
     * @param amount
     * */
    public void repaymentOrder(String url,String uniqueId1,String uniqueId2,String amount,String repaymentNumber,String repaymentNumber1,String productCode){
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("orderRequestNo",UUID.randomUUID().toString());//订单请求编号
        requestParams.put("orderUniqueId",UUID.randomUUID().toString()+"dzz");//商户订单号，可选填
        requestParams.put("transType","0");//交易类型，0提交，1撤销
        requestParams.put("financialContractNo",productCode);//信托产品代码
        requestParams.put("orderAmount","600.06");//订单总金额，订单总金额等于明细总金额相加

        List<RepaymentDetailsModel> repaymentDetailsModelList=new ArrayList<RepaymentDetailsModel>();
        List<BusinessAmountModel> businessAmountModelList=new ArrayList<BusinessAmountModel>();
        //第一个贷款合同-yongqianbao
        for (int i=0;i<1;i++){
            RepaymentDetailsModel repaymentDetailsModel=new RepaymentDetailsModel();
            repaymentDetailsModel.setContractNo("");//与uniqueId二选一
            repaymentDetailsModel.setContractUniqueId(uniqueId1);//贷款合同编号
            repaymentDetailsModel.setRepaymentWay(1000l);//还款方式,1000表示本金
            repaymentDetailsModel.setRepaymentBusinessNo(repaymentNumber);//还款方式为2003时,BizNo为回购单号；其它均为还款计划编号
            repaymentDetailsModel.setPlannedDate("2017-7-30 00:00:00");//还款时间
            repaymentDetailsModel.setDetailsTotalAmount(new BigDecimal("300.03"));//明细总金额，明细总金额等于费用明细相加
            for (int j=0;j<3;j++){//j最多只能到8，
                BusinessAmountModel businessAmountModel=new BusinessAmountModel();
                businessAmountModel.setFeeType(1000l+j);//费用类型，2000到2003都为回购费用
                businessAmountModel.setActualAmount(new BigDecimal("100.1"));//金额
                businessAmountModelList.add(businessAmountModel);
            }
            repaymentDetailsModel.setDetailsAmountJsonList(businessAmountModelList);
            repaymentDetailsModelList.add(repaymentDetailsModel);
        }
       //第二个贷款合同-paipaidai
        for (int i=0;i<1;i++){
            RepaymentDetailsModel repaymentDetailsModel=new RepaymentDetailsModel();
            repaymentDetailsModel.setContractNo(uniqueId2);//与uniqueId二选一
            repaymentDetailsModel.setContractUniqueId(uniqueId2);//贷款合同编号
            repaymentDetailsModel.setRepaymentWay(1000l);//还款方式
            repaymentDetailsModel.setRepaymentBusinessNo(repaymentNumber1);//还款方式为2003时,BizNo为回购单号；其它均为还款计划编号
            repaymentDetailsModel.setPlannedDate("2017-7-30 00:00:00");//还款时间
            repaymentDetailsModel.setDetailsTotalAmount(new BigDecimal("300.03"));//明细总金额，明细总金额等于费用明细相加
            for (int j=0;j<3;j++){//j最多只能到8，
                BusinessAmountModel businessAmountModel=new BusinessAmountModel();
                businessAmountModel.setFeeType(1000l+j);//费用类型，2000到2003都为回购费用
                businessAmountModel.setActualAmount(new BigDecimal("100.1"));//金额
                businessAmountModelList.add(businessAmountModel);
            }
            repaymentDetailsModel.setDetailsAmountJsonList(businessAmountModelList);
            repaymentDetailsModelList.add(repaymentDetailsModel);
        }
        String s=JsonUtils.toJsonString(repaymentDetailsModelList);
        requestParams.put("repaymentOrderDetail",s);// 还款业务明细
        System.out.println("请求参数："+JsonUtils.toJsonString(requestParams));
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
