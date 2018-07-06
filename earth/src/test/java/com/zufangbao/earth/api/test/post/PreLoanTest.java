package com.zufangbao.earth.api.test.post;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umpay.api.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

//@RunWith(SpringRunner.class)
@Slf4j
public class PreLoanTest {
	
	String requestNO = UUID.randomUUID().toString(); 


    private static final String privateKeyString = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMyYDfS68R/TqZT/CcKtU+2a+/sA+cG0JKs9R/nTC9gNbZtyzAwQrdEBQAKXzl3co9CwzZdiIgRX6ALKag/lYoKezY7UifF07vFyTM2gfpuP1ORh5Dt854n2do5+ovBYaLq+zq9NAlRktvcWAI+uVsgzEKADr4aRLiRapfZ71gU7AgMBAAECgYAWsHY7wc+j2/FemLoYYOiB3UI8n+sS1EuMwgsNZZ5Wo4aYSq7eV6svFphmsTctqZ5xMmpac4OaP7V3OcNxZ9r4tqhMANq00mNwNMCzcsPZ/wXE2+jGLy54OatuT908imRqLE9zLR5OxRtVc3nm5MHTidn8cr666pMzzKSDUkyzoQJBAOcp7Jfw+zXJh+PqgmgV0WGJDMs7Rk9juI7Qo0bFf6Lih7jXSL6iVAYqiJYX3Ts9ZjZVP117Id+lHcHIPjev7FcCQQDik1ZlgGNCGH/zgx1O+gWtP3KmqgLYiMUkdHduH8kzurthfR/zD2pXJenSWnQ5BsZQOhlVFH5KL+YOf+Mjmh+9AkEAyPMV/CN9jY1qtwNmZ6sHwD0ORSF7BoqOpn/SYDRRtzwrddCYKTgdyNpyr9+A7v15/CNxGQdwM+Vqj8lN5MTmswJAHl5BJjmfFCzUeX8JXpyERkRKyavf1cX/JnO1zjzUauqMUvTCY4GdbzDVtiwJh9swmXAwFQc6JhdlbmwVtZ/iwQJBAL9Vihy19dm+T9OXiutV1gS1abIsBwjeyZbx+jBrm+PJQCLkzZlFXELFg7z7qR3Iwrgh/CvWd7mo2Zv5SKzoncs=";
    private static final String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMmA30uvEf06mU/wnCrVPtmvv7APnBtCSrPUf50wvYDW2bcswMEK3RAUACl85d3KPQsM2XYiIEV+gCymoP5WKCns2O1InxdO7xckzNoH6bj9TkYeQ7fOeJ9naOfqLwWGi6vs6vTQJUZLb3FgCPrlbIMxCgA6+GkS4kWqX2e9YFOwIDAQAB";
    
    
    private String mer_code = "paipai";
    private String produt_code = "G31700";
    private String merchantCredit_no = "c9sxb35e-c4b6-4a3f-b4f6-c7ebae8a0004" ; //商户授信编号
    private String merchantContract_no = "3fe9db97-27fb-4e53-9975-2684306ca004";//商户借款合同编号
    private String request_no = Math.round(Math.random()*1000000)+"";
    private String vedaContract_no = "24f8e93e-7649-4424-8e32-32fad78d8d57";
    private String merchantLoanOrder_no ="DISB20180413000075897004";
    
    
    
  //授信申请接口
    @Test
    public void creditApplay() {
    	try {
	         String url = "http://cbhb.5veda.net/preloan/api/credit/v1/credit-application/"+ mer_code+ "/"+produt_code;
	    	//String url = "http://127.0.0.1:8740/preloan/api/credit/v1/cretdit-application/paipai/1";
	    	Map<String, Object> requestBody = new HashMap<>();
	    	requestBody.put("requestNo",request_no);
	    	requestBody.put("productCode", produt_code);
	    	requestBody.put("merchantCreditNo", merchantCredit_no);
	    	requestBody.put("roleType", "0");
	    	requestBody.put("callbackUrl", "http://www.baidu.com");
// 基础信息段
	       Map<String, Object> baseInfoSectionMap = new HashMap<>();
	       baseInfoSectionMap.put("name","tom");
	       baseInfoSectionMap.put("certificateType", "2");
	       baseInfoSectionMap.put("certificateNo", "421024199401021155");
	       baseInfoSectionMap.put("certificateIndate", "2028-01-01");
	       baseInfoSectionMap.put("mobile", "15671625758");
	       baseInfoSectionMap.put("email", "15671625566@163.com");
	       baseInfoSectionMap.put("gender", "1");
	       baseInfoSectionMap.put("birthDate", "2007-01-02");
	       baseInfoSectionMap.put("permanentAddress", "杭州");
	       baseInfoSectionMap.put("applyAmount", "12000.21");
//教育信息段
       
	       Map<String, Object> eduInfoSectionMap = new HashMap<>();
	       eduInfoSectionMap.put("highestDiploma", "1");
	       eduInfoSectionMap.put("highestDegree", "1");
//职业信息段
	       Map<String, Object> jobInfoSectionMap = new HashMap<>();
	       jobInfoSectionMap.put("jobCategory", "1");
	       jobInfoSectionMap.put("job", "1");
	       jobInfoSectionMap.put("jobTitle", "1");
	       jobInfoSectionMap.put("organizationName", "杭州随地付网络技术有限公司");
	       jobInfoSectionMap.put("organizationTel","15671556698");
	       jobInfoSectionMap.put("organizationIndustry", "1");
	       jobInfoSectionMap.put("annualIncome", "155555.62");
	       jobInfoSectionMap.put("organizationStartYear", "1991");
	       jobInfoSectionMap.put("organizationPostalcode", "434155");
	       jobInfoSectionMap.put("organizationAddress", "西湖区");
	       jobInfoSectionMap.put("organizationProvince", "浙江省");
	       jobInfoSectionMap.put("organizationCity", "杭州市");
//婚姻信息段
	       Map<String, Object> marriageInfoSectionMap = new HashMap<>();
	       marriageInfoSectionMap.put("maritalStatus", "1");
	       marriageInfoSectionMap.put("spouseName", "tony");
	       marriageInfoSectionMap.put("spouseMobile", "15677445589");
	       marriageInfoSectionMap.put("spouseCertificateType", "0");
	       marriageInfoSectionMap.put("spouseCertificateNo","421024199801012255");
	       marriageInfoSectionMap.put("spouseCertificateIndate", "2022-01-05");
	       marriageInfoSectionMap.put("spouseOrganizationName","suidifu");
//居住信息段
	       Map<String,Object> residenceInfoSectionMap = new HashMap<>();
	       residenceInfoSectionMap.put("residenceProvince", "浙江省");
	       residenceInfoSectionMap.put("residenceCity", "杭州市");
	       residenceInfoSectionMap.put("residenceAddress", "西湖");
	       residenceInfoSectionMap.put("residencePostalcode", "434410");
	       residenceInfoSectionMap.put("residenceCondition", "2");
	       residenceInfoSectionMap.put("residenceTel", "4555698");
//银行卡信息段
	      Map<String, Object> bankcardInfoSectionMap = new HashMap<>();
	      bankcardInfoSectionMap.put("bankcardNo","46454554135");
	      bankcardInfoSectionMap.put("bankName", "中国银行");
	      bankcardInfoSectionMap.put("bankBranchName", "杭州分行");
	      bankcardInfoSectionMap.put("bankBranchProvince","浙江");
	      bankcardInfoSectionMap.put("bankBranchCity", "杭州");
	      bankcardInfoSectionMap.put("bankcardBindingMobile", "15671556669");
//联系人信息段
	      List<Map<String, Object>> contactInfoSection = new ArrayList<>();
	      Map<String, Object> contactInfoSectionMap1 = new HashMap<>();
	      contactInfoSectionMap1.put("relationship","1");
	      contactInfoSectionMap1.put("name", "tom");
	      contactInfoSectionMap1.put("mobile", "15671554478");
	      contactInfoSectionMap1.put("certificateType", "0");
	      contactInfoSectionMap1.put("certificateNo", "421024199511225566");
	      contactInfoSectionMap1.put("certificateIndate", "2022-11-20");
	      contactInfoSectionMap1.put("organizationName", "suidifu");
	      contactInfoSection.add(contactInfoSectionMap1);
      
	      Map<String, Object> contactInfoSectionMap2 = new HashMap<>();
	      contactInfoSectionMap2.put("relationship","2");
	      contactInfoSectionMap2.put("name", "tom");
	      contactInfoSectionMap2.put("mobile", "15671554478");
	      contactInfoSectionMap2.put("certificateType", "0");
	      contactInfoSectionMap2.put("certificateNo", "421024199511225566");
	      contactInfoSectionMap2.put("certificateIndate", "2022-11-20");
	      contactInfoSectionMap2.put("organizationName", "suidifu");
	      contactInfoSection.add(contactInfoSectionMap2);
	//保单信息段
	      Map<String, Object> policyInfoSectionMap = new HashMap<>();
	      policyInfoSectionMap.put("policyCorporationName", "中国平安");
	      policyInfoSectionMap.put("policyStatus", "0");
	      policyInfoSectionMap.put("policyPaymentWay", "0");
	      policyInfoSectionMap.put("policyValidDate", "2018-01-01");
	      policyInfoSectionMap.put("policyPaymentExpire", "3");
	      policyInfoSectionMap.put("policyPaymentStatus", "0");
	      policyInfoSectionMap.put("policyAnnualPaymentAmount", "5151.12");
	//公基金信息段
	      Map<String, Object> providentFundInfoSectionMap = new HashMap<>();
	      providentFundInfoSectionMap.put("providentFundIdentityConsistent", "0");
	      providentFundInfoSectionMap.put("providentFundAccountStatus", "0");
	      providentFundInfoSectionMap.put("providentFundPaymentStatus", "0");
	      providentFundInfoSectionMap.put("providentFundPaymentRecord", "0");
	      providentFundInfoSectionMap.put("providentFundMonthBaseNum", "1515.12");
	      
	      requestBody.put("baseInfoSection",baseInfoSectionMap);
	      requestBody.put("eduInfoSection",eduInfoSectionMap);
	      requestBody.put("jobInfoSection",jobInfoSectionMap);
	      requestBody.put("marriageInfoSection",marriageInfoSectionMap);
	      requestBody.put("residenceInfoSection",residenceInfoSectionMap);
	      requestBody.put("bankcardInfoSection", bankcardInfoSectionMap);
	      requestBody.put("contactInfoSection",contactInfoSection);
	      requestBody.put("policyInfoSection",policyInfoSectionMap);
	      requestBody.put("providentFundInfoSection",  providentFundInfoSectionMap);
	      String result = httpUtils(url,requestBody);
	      System.out.println("-------------------------");
	      System.out.println(result);
	      System.out.println("-------------------------");
      }catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    
  //授信查询接口
    @Test
    public void creditApplaySelect(){

        try {
         String url = "http://cbhb.5veda.net/preloan/api/credit/v1/credit-application/query/"+ mer_code+ "/"+produt_code;
          //  String url = "http://127.0.0.1:8740/preloan/api/credit/v1/credit-application/query/paipai/1";

            Map<String,Object> requestBody = new HashMap<>();
            requestBody.put("requestNo",request_no);
            requestBody.put("productCode",produt_code);
            requestBody.put("merchantCreditNo",merchantCredit_no);

            String result = httpUtils(url, requestBody);

            System.out.println("-------------------------");
            System.out.println(result);
            System.out.println("-------------------------");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    
    
    
    
    //用信申请接口
    @Test
    public void testSave(){
        try {

           String url = "http://cbhb.5veda.net/preloan/api/asset/v1/contract/"+ mer_code+ "/"+produt_code;
           // String url = "http://127.0.0.1:8740/preloan/api/asset/v1/contract/paipai/1";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("requestNo", request_no);
            requestBody.put("productCode",produt_code );
            requestBody.put("merchantCreditNo",merchantCredit_no );
            requestBody.put("merchantContractNo", merchantContract_no);
            requestBody.put("vedaCreditNo", "SX183341644752023552");
            requestBody.put("txType", "0");
            requestBody.put("principalSum", "1200.00");
            requestBody.put("interestSum", "10.12");
            requestBody.put("costSum", "1210.12");
            requestBody.put("signDate", "2018-01-01");
            requestBody.put("effectiveDate", "2018-01-01");
            requestBody.put("valueDate", "2018-11-01");
            requestBody.put("dueDate", "2018-0-01");
            requestBody.put("interestRate", "0.1");
            requestBody.put("interestRateCycle", "0");
            requestBody.put("payInterestCycle", "0");
            requestBody.put("repaymentMod", "0");
            requestBody.put("repaymentPeriodsCount", "3");
            requestBody.put("intendedUse", "1");
            requestBody.put("cooperationPlatformProductName", "融汇");
            requestBody.put("cooperationPlatformGrade", "100");
            requestBody.put("callbackUrl", "http://www.baidu.com");

            List<Map<String, Object>> loanInfoSection = new ArrayList<>();
            Map<String, Object> loanInfoSectionMap = new HashMap<>();
            loanInfoSectionMap.put("payeeType","0");
            loanInfoSectionMap.put("payeeBankAccountNo","123456");
            loanInfoSectionMap.put("payeeBankAccountName","tom");
            loanInfoSectionMap.put("payeeBankCode","cb1221");
            loanInfoSectionMap.put("payeeBankName","杭州银行");
            loanInfoSectionMap.put("payeeBankCnapsCode","123456");
            loanInfoSectionMap.put("payeeBankProvince","浙江");
            loanInfoSectionMap.put("payeeBankCity","杭州");
            loanInfoSectionMap.put("loanAmount","1000.12");
            loanInfoSection.add(loanInfoSectionMap);

            List<Map<String, Object>> externalNoSection = new ArrayList<>();
            Map<String, Object> externalNoSectionMap = new HashMap<>();
            externalNoSectionMap.put("supervisionReportBusinessNo", "123");
            externalNoSection.add(externalNoSectionMap);
            requestBody.put("loanInfoSection",loanInfoSection);
            requestBody.put("externalNoSection",externalNoSection);

            String result = httpUtils(url,requestBody);

            System.out.println("-------------------------");
            System.out.println(result);
            System.out.println("-------------------------");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    
    
//用信查询接口
    @Test
    public void testSelect(){

        try {
           String url = "http://cbhb.5veda.net/preloan/api/asset/v1/contract/query/"+ mer_code+ "/"+produt_code;
            //String url = "http://127.0.0.1:8740/preloan/api/asset/v1/contract/query/paipai/1";

            Map<String,Object> requestBody = new HashMap<>();
            requestBody.put("requestNo",request_no);
            requestBody.put("productCode","G31700");
            requestBody.put("merchantContractNo",merchantContract_no);

            String result = httpUtils(url, requestBody);

            System.out.println("-------------------------");
            System.out.println(result);
            System.out.println("-------------------------");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    
    

    
    
 // 放款申请接口
    @Test
    public void loanApplay(){
    	try{
    		String url = "http://cbhb.5veda.net/preloan/api/remittance/v1/remittance-application/"+ mer_code+ "/"+produt_code;
            //String url = "http://127.0.0.1:8740/preloan/api/remittance/v1/remittance-application/paipai/1";
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("requestNo",request_no);
           // System.out.println("requestNO:" + requestNO);
            requestBody.put("productCode", produt_code);
            requestBody.put("merchantLoanOrderNo", merchantLoanOrder_no);
            requestBody.put("merchantContractNo", merchantContract_no);
            requestBody.put("vedaContractNo", "24f8e93e-7649-4424-8e32-32fad78d8d57");
            requestBody.put("loanAmount", "1200");
            requestBody.put("loanStrategy", "0");
            requestBody.put("remark", "das");
            requestBody.put("auditor", "tom");
            requestBody.put("auditTime", "2018-02-01 12:12:12");
            
            List<Map<String, Object>> loanDetail = new ArrayList<>();
            Map<String, Object> loanDetailMap = new HashMap<>();
            loanDetailMap.put("detailRecordNo", "00001");
            loanDetailMap.put("payeeType", "0");
            loanDetailMap.put("payeeBankAccountNo", "6216600100005555337");
            loanDetailMap.put("payeeBankAccountName", "张三");
            loanDetailMap.put("payeeBankCode", "C10920");
            loanDetailMap.put("payeeBankName", "杭州银行");
            loanDetailMap.put("payeeBankCnapsCode", "");
            loanDetailMap.put("payeeCertificateNo", "");
            loanDetailMap.put("payeeBankProvince", "");
            loanDetailMap.put("payeeBankCity", "");
            loanDetailMap.put("loanAmount", "1200.00");
            loanDetailMap.put("planExeDate", "2018-01-01 12:01:54");
            loanDetail.add(loanDetailMap);
            requestBody.put("loanDetail", loanDetail);
            
            String result = httpUtils(url, requestBody);
            System.out.println("---------------------");
            System.out.println(result);
            System.out.println("---------------------");
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail();
    		
    	}
    }
    
    
    
    
  //放款查询接口
  @Test
  public void loanApplaySelect(){
	  try{
		  
	  String url = "http://cbhb.5veda.net/preloan/api/remittance/v1/remittance-application/query/"+ mer_code+ "/"+produt_code;
//          String url = "http://127.0.0.1:8740/preloan/api/remittance/v1/remittance-application/query/paipai/1";
          Map<String, Object> requestBody = new HashMap<>();
          requestBody.put("requestNo",request_no);
          requestBody.put("productCode", produt_code);
          requestBody.put("merchantContractNo",merchantContract_no );
          requestBody.put("merchantLoanOrderNo",merchantLoanOrder_no );
          //requestBody.put("vedaContractNo", vedaContract_no);
          //requestBody.put("remittanceApplicationUuid","751ba5c1-3461-45a8-b665-b4c27ca259ee");
          String result = httpUtils(url, requestBody);
	      System.out.println("------------------");
	      System.out.println(result);
	      System.out.println("-------------------");
	      Map maps = JSON.parseObject(result);
	      Map data = JSON.parseObject((String) maps.get("data"));
	      System.out.println(data.get("vedaContractNo"));
	  } catch (Exception e) {
  		e.printStackTrace();
  		Assert.fail();
  		
  	}
  } 
    
  private String catchVedaContractNo() throws Exception{
		  String url = "http://cbhb.5veda.net/preloan/api/remittance/v1/remittance-application/query/"+ mer_code+ "/"+produt_code;
//	          String url = "http://127.0.0.1:8740/preloan/api/remittance/v1/remittance-application/query/paipai/1";
	          Map<String, Object> requestBody = new HashMap<>();
	          requestBody.put("requestNo",request_no);
	          requestBody.put("productCode", produt_code);
	          requestBody.put("merchantContractNo",merchantContract_no );
	          requestBody.put("merchantLoanOrderNo",merchantLoanOrder_no );
	          //requestBody.put("vedaContractNo", vedaContract_no);
	          //requestBody.put("remittanceApplicationUuid","751ba5c1-3461-45a8-b665-b4c27ca259ee");
	          String result = httpUtils(url, requestBody);
	          Map maps = JSON.parseObject(result);
		      Map data = JSON.parseObject((String) maps.get("data"));
		      System.out.println(data.get("vedaContractNo"));
		      return data.get("vedaContractNo").toString();
  }

  //还款计划推送接口
  @Test
  public void repaymentPlanPush(){
  	try{
  		
  		String url = "http://cbhb.5veda.net/preloan/api/asset/v1/assetSet/"+ mer_code+ "/"+produt_code;
          //String url = "http://127.0.0.1:8740/preloan/api/asset/v1/assetSet/paipai/1";
          Map<String, Object> requestBody = new HashMap<>();
      	requestBody.put("requestNo",request_no);
      	requestBody.put("productCode", produt_code);
      	requestBody.put("merchantContractNo", merchantContract_no);
//      	requestBody.put("vedaContractNo", this.catchVedaContractNo());
      	requestBody.put("callbackUrl", "http://www.baidu.com");
      	
      	//costdetail
      	List<Map<String,Object>> costDetail = new ArrayList<>();
      	Map<String,Object> costDetailMap = new HashMap<>();
      	costDetailMap.put("costDetailType", "0");
      	costDetailMap.put("costDetailAmount", "1200");
      	costDetail.add(costDetailMap);
      	
      	Map<String,Object> costDetailMap1 = new HashMap<>();
      	costDetailMap1.put("costDetailType", "1");
      	costDetailMap1.put("costDetailAmount", "10.12");
      	costDetail.add(costDetailMap1);
      	
      	//asset
      	List<Map<String,Object >> assetSet = new ArrayList<>();
      	Map<String, Object> assetSetMap = new HashMap<>();
      	assetSetMap.put("merchantRepaymentPlanNo", "201804120000001_001");
      	assetSetMap.put("repaymentDate", "2018-12-12");
      	assetSetMap.put("costDetail", costDetail);
      	assetSet.add(assetSetMap);
      	
      	
      	
      	requestBody.put("assetSet", assetSet);
      	String result = httpUtils(url, requestBody);
      	
      	System.out.println("-------------------------");
      	System.out.println(result);
      	System.out.println("-------------------------");
      	
      	
  		} catch (Exception e) {
	            e.printStackTrace();
	            Assert.fail();
      }
  }
  
 //还款计划查询接口
  @Test
  public void repaymentPlanSelect(){
  	try{
        String url = "http://cbhb.5veda.net/preloan/api/asset/v1/assetSet/query/"+ mer_code+ "/"+produt_code;
       // String url = "http://127.0.0.1:8740/preloan/api/asset/v1/assetSet/query/paipai/1";
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("requestNo", request_no);
        requestBody.put("productCode", produt_code);
        requestBody.put("merchantContractNo", merchantContract_no);
        
        String result = httpUtils(url, requestBody);
	      System.out.println("------------------");
	      System.out.println(result);
	      System.out.println("-------------------");
        
        
  	} catch (Exception e) {
  		e.printStackTrace();
  		Assert.fail();
  		
  	}
  	
  }
  
    

    private String httpUtils(String url, Map<String,Object> requestBody) throws Exception{
        HttpPost httpPost = new HttpPost(url);

        String envelopOriginal = "01234567012345670123456701234567";

        PublicKey publicKeyEnvelop  = KeyUtil.getPublicKey(publicKeyString);
        String envelop = KeyUtil.rsaEncrypt(envelopOriginal, publicKeyEnvelop);

        String request = JSON.toJSONString(requestBody);
        String requestDecrypted = KeyUtil.aesEncrypt(request, envelopOriginal);

        PrivateKey privateKey = KeyUtil.getPrivate(privateKeyString);
        String sign = KeyUtil.sign(requestDecrypted, privateKey);

        httpPost.setHeader(HeaderConstant.ENVELOPE, envelop);
        httpPost.setHeader(HeaderConstant.SIGN, sign);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new ByteArrayEntity(requestDecrypted.getBytes()));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);
        
//        System.out.println(EntityUtils.toString(closeableHttpResponse.getEntity()));
        String response = 	EntityUtils.toString(closeableHttpResponse.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(response);
        String data = jsonObject.getString("data");
        if (! StringUtil.isEmpty(data)) {
        	String deData = KeyUtil.aesDescrypt(data,envelopOriginal);
        	jsonObject.put("data", deData);
        }
        return jsonObject.toJSONString();
    }
    /**
     * 将byte[] 转换成字符串
     */
    public static String byte2Hex(byte[] srcBytes) {
       StringBuilder hexRetSB = new StringBuilder();
       for (byte b : srcBytes) {
          String hexString = Integer.toHexString(0x00ff & b);
          hexRetSB.append(hexString.length() == 1 ? 0 : "").append(hexString);
       }
       return hexRetSB.toString();
    }
    /**
    * 随机生成秘钥 64bit 密钥长度为16位
    */
   public static String getKey() {
       Random random = new Random();
       random.setSeed(System.currentTimeMillis());
       byte[] bys = new byte[8];
       random.nextBytes(bys);
       String s = byte2Hex(bys);
       System.out.println("秘钥 ： " + s);
       return s;
   }
}