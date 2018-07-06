package com.zufangbao.testAPIWuBo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.testAPIWuBo.testAPI.models.ContractDetail;
import com.zufangbao.testAPIWuBo.testAPI.models.ImportAssetPackageContent;
import com.zufangbao.testAPIWuBo.testAPI.models.ImportRepaymentPlanDetail;

import java.util.*;

/**
 * Created by Cool on 2017/7/28.
 */
public class TestMethodsZH {
    public void importAssetPackage1(String url,String totalAmount,String productCode,String uniqueId,String repaymentAccountNo,String amount,String firstRepaymentDate,String secondRepaymentDate,String thirdRepaymentDate){
        for (int index = 0; index < 1; index++) {
            ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
            importAssetPackageContent.setThisBatchContractsTotalNumber(1);
            importAssetPackageContent.setThisBatchContractsTotalAmount(totalAmount);
            importAssetPackageContent.setFinancialProductCode(productCode);

            List<ContractDetail> contracts = new ArrayList<ContractDetail>();
            ContractDetail contractDetail = new ContractDetail();
            contractDetail.setUniqueId(uniqueId);
            contractDetail.setLoanContractNo(uniqueId);
            contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
            contractDetail.setLoanCustomerName("王宝");
//            contractDetail.setSubjectMatterassetNo("193754893758");
            contractDetail.setIDCardNo("320301198502169142");
            contractDetail.setMobile("18069847420");
            contractDetail.setBankCode("ABC");//
//            contractDetail.setBankOfTheProvince("110000");
//            contractDetail.setBankOfTheCity("110100");
            contractDetail.setRepaymentAccountNo("6228480444455553333");
            contractDetail.setLoanTotalAmount(totalAmount);
            contractDetail.setLoanPeriods(3);
            contractDetail.setEffectDate(DateUtils.format(new Date()));
            contractDetail.setExpiryDate("2099-01-01");
            contractDetail.setLoanRates("4.38");
            contractDetail.setInterestRateCycle(3);
            contractDetail.setPenalty("6.57");
            contractDetail.setRepaymentWay(1);

            List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

            ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail1.setRepaymentPrincipal(amount);
            repaymentPlanDetail1.setRepaymentInterest("0.00");
            repaymentPlanDetail1.setRepaymentDate(firstRepaymentDate);
            repaymentPlanDetail1.setOtheFee("0.00");
            repaymentPlanDetail1.setTechMaintenanceFee("0.00");
            repaymentPlanDetail1.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail1);

            ImportRepaymentPlanDetail repaymentPlanDetail2= new ImportRepaymentPlanDetail();
            repaymentPlanDetail2.setRepaymentPrincipal(amount);
            repaymentPlanDetail2.setRepaymentInterest("0.00");
            repaymentPlanDetail2.setRepaymentDate(secondRepaymentDate);
            repaymentPlanDetail2.setOtheFee("0.00");
            repaymentPlanDetail2.setTechMaintenanceFee("0.00");
            repaymentPlanDetail2.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail2);

            ImportRepaymentPlanDetail repaymentPlanDetail3= new ImportRepaymentPlanDetail();
            repaymentPlanDetail3.setRepaymentPrincipal(amount);
            repaymentPlanDetail3.setRepaymentInterest("0.00");
            repaymentPlanDetail3.setRepaymentDate(thirdRepaymentDate);
            repaymentPlanDetail3.setOtheFee("0.00");
            repaymentPlanDetail3.setTechMaintenanceFee("0.00");
            repaymentPlanDetail3.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail3);

//            ImportRepaymentPlanDetail repaymentPlanDetail4= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail4.setRepaymentPrincipal("25367.24");
//            repaymentPlanDetail4.setRepaymentInterest("1182.31");
//            repaymentPlanDetail4.setRepaymentDate("2018-06-14");
//            repaymentPlanDetail4.setOtheFee("0.00");
//            repaymentPlanDetail4.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail4.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail4);
//
//
//            ImportRepaymentPlanDetail repaymentPlanDetail5= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail5.setRepaymentPrincipal("24935.70");
//            repaymentPlanDetail5.setRepaymentInterest("1613.85");
//            repaymentPlanDetail5.setRepaymentDate("2018-07-14");
//            repaymentPlanDetail5.setOtheFee("0.00");
//            repaymentPlanDetail5.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail5.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail5);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail6= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail6.setRepaymentPrincipal("24346.64");
//            repaymentPlanDetail6.setRepaymentInterest("2202.91");
//            repaymentPlanDetail6.setRepaymentDate("2018-08-14");
//            repaymentPlanDetail6.setOtheFee("0.00");
//            repaymentPlanDetail6.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail6.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail6);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail7= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail7.setRepaymentPrincipal("23542.58");
//            repaymentPlanDetail7.setRepaymentInterest("3006.97");
//            repaymentPlanDetail7.setRepaymentDate("2018-09-14");
//            repaymentPlanDetail7.setOtheFee("0.00");
//            repaymentPlanDetail7.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail7.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail7);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail8= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail8.setRepaymentPrincipal("22445.04");
//            repaymentPlanDetail8.setRepaymentInterest("4104.51");
//            repaymentPlanDetail8.setRepaymentDate("2018-10-14");
//            repaymentPlanDetail8.setOtheFee("0.00");
//            repaymentPlanDetail8.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail8.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail8);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail9= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail9.setRepaymentPrincipal("20946.89");
//            repaymentPlanDetail9.setRepaymentInterest("5602.66");
//            repaymentPlanDetail9.setRepaymentDate("2018-11-14");
//            repaymentPlanDetail9.setOtheFee("0.00");
//            repaymentPlanDetail9.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail9.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail9);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail10= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail10.setRepaymentPrincipal("18901.92");
//            repaymentPlanDetail10.setRepaymentInterest("7647.63");
//            repaymentPlanDetail10.setRepaymentDate("2018-12-14");
//            repaymentPlanDetail10.setOtheFee("0.00");
//            repaymentPlanDetail10.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail10.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail10);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail11= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail11.setRepaymentPrincipal("16110.53");
//            repaymentPlanDetail11.setRepaymentInterest("10439.02");
//            repaymentPlanDetail11.setRepaymentDate("2019-01-14");
//            repaymentPlanDetail11.setOtheFee("0.00");
//            repaymentPlanDetail11.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail11.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail11);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail12= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail12.setRepaymentPrincipal("12300.29");
//            repaymentPlanDetail12.setRepaymentInterest("14249.26");
//            repaymentPlanDetail12.setRepaymentDate("2019-02-14");
//            repaymentPlanDetail12.setOtheFee("0.00");
//            repaymentPlanDetail12.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail12.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail12);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail13= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail13.setRepaymentPrincipal("7099.31");
//            repaymentPlanDetail13.setRepaymentInterest("19450.17");
//            repaymentPlanDetail13.setRepaymentDate("2019-03-14");
//            repaymentPlanDetail13.setOtheFee("0.00");
//            repaymentPlanDetail13.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail13.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail13);

            contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

            contracts.add(contractDetail);

//            ContractDetail contractDetail1 = new ContractDetail();
//            contractDetail1.setUniqueId(uniqueId+"1");
//            contractDetail1.setLoanContractNo(uniqueId+"1");
//            contractDetail1.setLoanCustomerNo(UUID.randomUUID().toString());
//            contractDetail1.setLoanCustomerName("WUBO");
//            contractDetail1.setSubjectMatterassetNo(UUID.randomUUID().toString());
//            contractDetail1.setIDCardNo("320301198502169142");
//            contractDetail1.setMobile("17682481004");
//            contractDetail1.setBankCode("RCC");//C10105 C10403
//            contractDetail1.setBankOfTheProvince("110000");
//            contractDetail1.setBankOfTheCity("110100");
//            contractDetail1.setRepaymentAccountNo("6214855712112350");
//            contractDetail1.setLoanTotalAmount(totalAmount);
//            contractDetail1.setLoanPeriods(3);
//            contractDetail1.setEffectDate(DateUtils.format(new Date()));
//            contractDetail1.setExpiryDate("2099-01-01");
//            contractDetail1.setLoanRates("0");
//            contractDetail1.setInterestRateCycle(1);
//            contractDetail1.setPenalty("0.0005");
//            contractDetail1.setRepaymentWay(2);

            List<ImportRepaymentPlanDetail> repaymentPlanDetails1 = new ArrayList<ImportRepaymentPlanDetail>();

//            ImportRepaymentPlanDetail repaymentPlanDetail4 = new ImportRepaymentPlanDetail();
//            repaymentPlanDetail4.setRepaymentPrincipal(amount);
//            repaymentPlanDetail4.setRepaymentInterest("0.00");
//            repaymentPlanDetail4.setRepaymentDate(firstRepaymentDate);
//            repaymentPlanDetail4.setOtheFee("0.00");
//            repaymentPlanDetail4.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail4.setLoanServiceFee("0.00");
//            repaymentPlanDetails1.add(repaymentPlanDetail4);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail5= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail5.setRepaymentPrincipal(amount);
//            repaymentPlanDetail5.setRepaymentInterest("0.00");
//            repaymentPlanDetail5.setRepaymentDate(secondRepaymentDate);
//            repaymentPlanDetail5.setOtheFee("0.00");
//            repaymentPlanDetail5.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail5.setLoanServiceFee("0.00");
//            repaymentPlanDetails1.add(repaymentPlanDetail5);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail6= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail6.setRepaymentPrincipal(amount);
//            repaymentPlanDetail6.setRepaymentInterest("0.00");
//            repaymentPlanDetail6.setRepaymentDate(thirdRepaymentDate);
//            repaymentPlanDetail6.setOtheFee("0.00");
//            repaymentPlanDetail6.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail6.setLoanServiceFee("0.00");
//            repaymentPlanDetails1.add(repaymentPlanDetail6);

//            contractDetail1.setRepaymentPlanDetails(repaymentPlanDetails1);
//            contracts.add(contractDetail1);
            importAssetPackageContent.setContractDetails(contracts);

            String param =  JsonUtils.toJsonString(importAssetPackageContent);
            System.out.println(param);
            Map<String,String> params =new HashMap<String,String>();
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
                String sr = PostTestUtil.sendPost(url, params, new BaseApiTestPost().getIdentityInfoMapForZHONGHANG(params));
                System.out.println( "===========" + index + sr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void importAssetPackage2(String url,String totalAmount,String productCode,String uniqueId,String repaymentAccountNo,String amount,String firstRepaymentDate,String secondRepaymentDate,String thirdRepaymentDate){
        for (int index = 0; index < 1; index++) {
            ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
            importAssetPackageContent.setThisBatchContractsTotalNumber(1);
            importAssetPackageContent.setThisBatchContractsTotalAmount(totalAmount);
            importAssetPackageContent.setFinancialProductCode(productCode);

            List<ContractDetail> contracts = new ArrayList<ContractDetail>();
            ContractDetail contractDetail = new ContractDetail();
            contractDetail.setUniqueId(uniqueId);
            contractDetail.setLoanContractNo(uniqueId);
            contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
            contractDetail.setLoanCustomerName("ui");
//            contractDetail.setSubjectMatterassetNo("193754893758");
            contractDetail.setIDCardNo("340862189009097871");
            contractDetail.setMobile("17682481004");
            contractDetail.setBankCode("ABC");//C10105 C10403
//            contractDetail.setBankOfTheProvince("110000");
//            contractDetail.setBankOfTheCity("110100");
            contractDetail.setRepaymentAccountNo("62284843789576934577");
            contractDetail.setLoanTotalAmount(totalAmount);
            contractDetail.setLoanPeriods(3);
            contractDetail.setEffectDate(DateUtils.format(new Date()));
            contractDetail.setExpiryDate("2099-01-01");
            contractDetail.setLoanRates("4.38");
            contractDetail.setInterestRateCycle(3);
            contractDetail.setPenalty("6.57");
            contractDetail.setRepaymentWay(1);

            List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

            ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail1.setRepaymentPrincipal(amount);
            repaymentPlanDetail1.setRepaymentInterest("0.00");
            repaymentPlanDetail1.setRepaymentDate(firstRepaymentDate);
            repaymentPlanDetail1.setOtheFee("0.00");
            repaymentPlanDetail1.setTechMaintenanceFee("0.00");
            repaymentPlanDetail1.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail1);

            ImportRepaymentPlanDetail repaymentPlanDetail2= new ImportRepaymentPlanDetail();
            repaymentPlanDetail2.setRepaymentPrincipal(amount);
            repaymentPlanDetail2.setRepaymentInterest("0.00");
            repaymentPlanDetail2.setRepaymentDate(secondRepaymentDate);
            repaymentPlanDetail2.setOtheFee("0.00");
            repaymentPlanDetail2.setTechMaintenanceFee("0.00");
            repaymentPlanDetail2.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail2);

            ImportRepaymentPlanDetail repaymentPlanDetail3= new ImportRepaymentPlanDetail();
            repaymentPlanDetail3.setRepaymentPrincipal(amount);
            repaymentPlanDetail3.setRepaymentInterest("0.00");
            repaymentPlanDetail3.setRepaymentDate(thirdRepaymentDate);
            repaymentPlanDetail3.setOtheFee("0.00");
            repaymentPlanDetail3.setTechMaintenanceFee("0.00");
            repaymentPlanDetail3.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail3);

//            ImportRepaymentPlanDetail repaymentPlanDetail4= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail4.setRepaymentPrincipal("25367.24");
//            repaymentPlanDetail4.setRepaymentInterest("1182.31");
//            repaymentPlanDetail4.setRepaymentDate("2018-06-14");
//            repaymentPlanDetail4.setOtheFee("0.00");
//            repaymentPlanDetail4.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail4.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail4);
//
//
//            ImportRepaymentPlanDetail repaymentPlanDetail5= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail5.setRepaymentPrincipal("24935.70");
//            repaymentPlanDetail5.setRepaymentInterest("1613.85");
//            repaymentPlanDetail5.setRepaymentDate("2018-07-14");
//            repaymentPlanDetail5.setOtheFee("0.00");
//            repaymentPlanDetail5.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail5.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail5);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail6= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail6.setRepaymentPrincipal("24346.64");
//            repaymentPlanDetail6.setRepaymentInterest("2202.91");
//            repaymentPlanDetail6.setRepaymentDate("2018-08-14");
//            repaymentPlanDetail6.setOtheFee("0.00");
//            repaymentPlanDetail6.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail6.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail6);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail7= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail7.setRepaymentPrincipal("23542.58");
//            repaymentPlanDetail7.setRepaymentInterest("3006.97");
//            repaymentPlanDetail7.setRepaymentDate("2018-09-14");
//            repaymentPlanDetail7.setOtheFee("0.00");
//            repaymentPlanDetail7.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail7.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail7);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail8= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail8.setRepaymentPrincipal("22445.04");
//            repaymentPlanDetail8.setRepaymentInterest("4104.51");
//            repaymentPlanDetail8.setRepaymentDate("2018-10-14");
//            repaymentPlanDetail8.setOtheFee("0.00");
//            repaymentPlanDetail8.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail8.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail8);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail9= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail9.setRepaymentPrincipal("20946.89");
//            repaymentPlanDetail9.setRepaymentInterest("5602.66");
//            repaymentPlanDetail9.setRepaymentDate("2018-11-14");
//            repaymentPlanDetail9.setOtheFee("0.00");
//            repaymentPlanDetail9.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail9.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail9);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail10= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail10.setRepaymentPrincipal("18901.92");
//            repaymentPlanDetail10.setRepaymentInterest("7647.63");
//            repaymentPlanDetail10.setRepaymentDate("2018-12-14");
//            repaymentPlanDetail10.setOtheFee("0.00");
//            repaymentPlanDetail10.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail10.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail10);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail11= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail11.setRepaymentPrincipal("16110.53");
//            repaymentPlanDetail11.setRepaymentInterest("10439.02");
//            repaymentPlanDetail11.setRepaymentDate("2019-01-14");
//            repaymentPlanDetail11.setOtheFee("0.00");
//            repaymentPlanDetail11.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail11.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail11);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail12= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail12.setRepaymentPrincipal("12300.29");
//            repaymentPlanDetail12.setRepaymentInterest("14249.26");
//            repaymentPlanDetail12.setRepaymentDate("2019-02-14");
//            repaymentPlanDetail12.setOtheFee("0.00");
//            repaymentPlanDetail12.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail12.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail12);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail13= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail13.setRepaymentPrincipal("7099.31");
//            repaymentPlanDetail13.setRepaymentInterest("19450.17");
//            repaymentPlanDetail13.setRepaymentDate("2019-03-14");
//            repaymentPlanDetail13.setOtheFee("0.00");
//            repaymentPlanDetail13.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail13.setLoanServiceFee("0.00");
//            repaymentPlanDetails.add(repaymentPlanDetail13);

            contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

            contracts.add(contractDetail);

//            ContractDetail contractDetail1 = new ContractDetail();
//            contractDetail1.setUniqueId(uniqueId+"1");
//            contractDetail1.setLoanContractNo(uniqueId+"1");
//            contractDetail1.setLoanCustomerNo(UUID.randomUUID().toString());
//            contractDetail1.setLoanCustomerName("WUBO");
//            contractDetail1.setSubjectMatterassetNo(UUID.randomUUID().toString());
//            contractDetail1.setIDCardNo("320301198502169142");
//            contractDetail1.setMobile("17682481004");
//            contractDetail1.setBankCode("RCC");//C10105 C10403
//            contractDetail1.setBankOfTheProvince("110000");
//            contractDetail1.setBankOfTheCity("110100");
//            contractDetail1.setRepaymentAccountNo("6214855712112350");
//            contractDetail1.setLoanTotalAmount(totalAmount);
//            contractDetail1.setLoanPeriods(3);
//            contractDetail1.setEffectDate(DateUtils.format(new Date()));
//            contractDetail1.setExpiryDate("2099-01-01");
//            contractDetail1.setLoanRates("0");
//            contractDetail1.setInterestRateCycle(1);
//            contractDetail1.setPenalty("0.0005");
//            contractDetail1.setRepaymentWay(2);

            List<ImportRepaymentPlanDetail> repaymentPlanDetails1 = new ArrayList<ImportRepaymentPlanDetail>();

//            ImportRepaymentPlanDetail repaymentPlanDetail4 = new ImportRepaymentPlanDetail();
//            repaymentPlanDetail4.setRepaymentPrincipal(amount);
//            repaymentPlanDetail4.setRepaymentInterest("0.00");
//            repaymentPlanDetail4.setRepaymentDate(firstRepaymentDate);
//            repaymentPlanDetail4.setOtheFee("0.00");
//            repaymentPlanDetail4.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail4.setLoanServiceFee("0.00");
//            repaymentPlanDetails1.add(repaymentPlanDetail4);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail5= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail5.setRepaymentPrincipal(amount);
//            repaymentPlanDetail5.setRepaymentInterest("0.00");
//            repaymentPlanDetail5.setRepaymentDate(secondRepaymentDate);
//            repaymentPlanDetail5.setOtheFee("0.00");
//            repaymentPlanDetail5.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail5.setLoanServiceFee("0.00");
//            repaymentPlanDetails1.add(repaymentPlanDetail5);
//
//            ImportRepaymentPlanDetail repaymentPlanDetail6= new ImportRepaymentPlanDetail();
//            repaymentPlanDetail6.setRepaymentPrincipal(amount);
//            repaymentPlanDetail6.setRepaymentInterest("0.00");
//            repaymentPlanDetail6.setRepaymentDate(thirdRepaymentDate);
//            repaymentPlanDetail6.setOtheFee("0.00");
//            repaymentPlanDetail6.setTechMaintenanceFee("0.00");
//            repaymentPlanDetail6.setLoanServiceFee("0.00");
//            repaymentPlanDetails1.add(repaymentPlanDetail6);

//            contractDetail1.setRepaymentPlanDetails(repaymentPlanDetails1);
//            contracts.add(contractDetail1);
            importAssetPackageContent.setContractDetails(contracts);

            String param =  JsonUtils.toJsonString(importAssetPackageContent);
            System.out.println(param);
            Map<String,String> params =new HashMap<String,String>();
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
                String sr = PostTestUtil.sendPost(url, params, new BaseApiTestPost().getIdentityInfoMapForZHONGHANG(params));
                System.out.println( "===========" + index + sr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 查询该合同下第i期还款计划
     * @param uniqueId
     * @return
     */
    public String query_i_RepaymentPlan(String url,String uniqueId,int i){
        String repaymentNumber = "";
        Map<String, String> requestParams1 = new HashMap<String, String>();
        requestParams1.put("fn", "100001");
        requestParams1.put("contractNo", "");
        requestParams1.put("requestNo", UUID.randomUUID().toString());
        requestParams1.put("uniqueId", uniqueId);
        try {
            String sr = PostTestUtil.sendPost(url, requestParams1,new BaseApiTestPost().getIdentityInfoMapForZHONGHANG(requestParams1));
            Result result = JsonUtils.parse(sr, Result.class);
            JSONArray repaymentPlanDetails =  (JSONArray) result.get("repaymentPlanDetails");
            JSONObject jo = repaymentPlanDetails.getJSONObject(i);
            repaymentNumber = (String) jo.get("repaymentNumber");
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return repaymentNumber;
    }
    /**
     * 扣款新接口
     * @param repaymentNumber
     * @param uniqueId
     * @param productCode
     * @param repaymentType
     */
    public void deductRepaymentPlanNEW(String url,String repaymentNumber,String uniqueId, String productCode, String repaymentTotalamount,String repaymentPrincipal ,String repaymentType){
		/**/
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300001");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("deductId",  UUID.randomUUID().toString());
        requestParams.put("financialProductCode", productCode);
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("apiCalledTime", DateUtils.format(new Date()));
        requestParams.put("amount",  repaymentTotalamount);
        requestParams.put("repaymentType", repaymentType);//1、正常  0、提前划扣
		requestParams.put("payerName", "王宝");
		requestParams.put("bankCode", "C10103");
		requestParams.put("mobile","13777847783");
		requestParams.put("idCardNum","320301198502169142");
        requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':'"+repaymentTotalamount+"','repaymentInterest':0.00,'repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':"+repaymentPrincipal+",'techFee':0.00,'overDueFeeDetail':{"
        //requestParams.put("repaymentDetails", "[{'loanFee':1.00,'otherFee':1.00,'repaymentAmount':'"+repaymentTotalamount+"','repaymentInterest':1.00,'repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':"+repaymentPrincipal+",'techFee':1.00,'overDueFeeDetail':{"
                //			+ "'penaltyFee':1.00,'latePenalty':1.00,'lateFee':1.00,'lateOtherCost':1.00,'totalOverdueFee':4.00"
                + "'totalOverdueFee':0.00}}]");
        try {
            String sr = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMapForZHONGHANG(requestParams));
            System.out.println(sr);
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
        requestParams.put("payerName", "章光");
        requestParams.put("bankCode", "103100000026");//银行代码
        requestParams.put("bankAccount", bankAccount);//银行帐号
        requestParams.put("bankName", "中国农业银行");
//        requestParams.put("bankProvince", "210000");
//        requestParams.put("bankCity", "210200");
        requestParams.put("idCardNum","132032512010000359");
        requestParams.put("mobile","13258446545");
//        requestParams.put("repaymentChannel", null);//还款通道
        try {
            String result = PostTestUtil.sendPost(url, requestParams, new BaseApiTestPost().getIdentityInfoMapForZHONGHANG(requestParams));
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
