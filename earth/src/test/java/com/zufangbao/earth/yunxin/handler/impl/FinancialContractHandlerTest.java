package com.zufangbao.earth.yunxin.handler.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.codehaus.janino.ExpressionEvaluator;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import com.zufangbao.earth.yunxin.handler.CreateException;
import com.zufangbao.earth.yunxin.handler.FinancialContractHandler;
import com.zufangbao.earth.yunxin.handler.ModifyException;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfiguration;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.service.AccountService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.CnEnTranslation;
import com.zufangbao.sun.yunxin.entity.model.CreateFinancialContractModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractBasicInfoModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractRemittanceInfoModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractRepaymentInfoModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml"})
@Transactional
public class FinancialContractHandlerTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private FinancialContractHandler financialContractHandler;

    @Autowired
    private LedgerBookService ledgerBookService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private FinancialContractService financialContractService;
    
	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;
	
	@Autowired
	private SessionFactory  sessionFactory;
	
	@Autowired
	protected GenericDaoSupport genericDaoSupport;


    @Test
    @Sql("classpath:test/test4editFinancialContractBasicInfo.sql")
    public void testCreateNewFinancialContract() {
        try {
            CreateFinancialContractModel model = new CreateFinancialContractModel();
            financialContractHandler.createNewFinancialContract(model, null);
        } catch (CreateException e) {
            Assert.assertEquals("请求参数错误！", e.getMessage());
        }

        try {
            CreateFinancialContractModel model = new CreateFinancialContractModel();
            model.setFinancialContractName("trustsContractName1");
            model.setAdvaStartDate("2016-11-21");
            model.setAccountName("accountName1");
            model.setAccountNo("accountNo1");
            model.setBankName("中国银行");
            model.setRemittanceStrategyMode(3);
            model.setAssetPackageFormat(1);
            model.setAdvaRepaymentTerm(5);
            financialContractHandler.createNewFinancialContract(model, null);
        } catch (CreateException e) {
            Assert.assertEquals("请求参数错误！", e.getMessage());
        }

//		try {
//			CreateFinancialContractModel model = new CreateFinancialContractModel();
//			model.setFinancialContractName("trustsContractName1");
//			model.setAdvaStartDate("2016-11-21");
//			model.setAccountName("accountName1");
//			model.setAccountNo("accountNo1");
//			model.setBankName("中国银行");
//			model.setAssetPackageFormat(1);
//			model.setAdvaRepaymentTerm(5);
//			model.setFinancialContractNo("trustsContractNo1");
//			model.setCompanyUuid("a02c02b9-6f98-11e6-bf08-00163e002839");
//			model.setAppId(1l);
//			model.setRemittanceStrategyMode(1);
//			model.setAppAccounts("[{\"accountNo\":\"dhg\",\"accountName\":\"gh\",\"bankName\":\"工商银行\"}]");
//			financialContractHandler.createNewFinancialContract(model);
//		}catch (CreateException e) {
//			Assert.assertEquals("请求参数错误！", e.getMessage());
//		}
		
		try {
			CreateFinancialContractModel model = new CreateFinancialContractModel();
			model.setFinancialContractName("trustsContractName1");
			model.setAdvaStartDate("2016-11-21");
			model.setAccountName("accountName1");
			model.setAccountNo("accountNo1");
			model.setBankName("中国银行");
			model.setAssetPackageFormat(1);
			model.setAdvaRepaymentTerm(5);
			model.setFinancialContractNo("trustsContractNo1");
			model.setCompanyUuid("a02c02b9-6f98-11e6-bf08-00163e002839");
			model.setAppId(1l);
			model.setRemittanceStrategyMode(1);
			financialContractHandler.createNewFinancialContract(model, null);
		}catch (CreateException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		try {
			CreateFinancialContractModel model = new CreateFinancialContractModel();
			model.setFinancialContractName("trustsContractName1");
			model.setAdvaStartDate("2016-11-21");
			model.setAccountName("accountName1");
			model.setAccountNo("accountNo1");
			model.setBankName("中国银行");
			model.setRemittanceStrategyMode(3);
			model.setAssetPackageFormat(1);
			model.setAdvaRepaymentTerm(5);
			model.setAdvaRepoTerm(6);
			model.setFinancialContractNo("trustsContractNo1");
			model.setCompanyUuid("a02c02b9-6f98-11e6-bf08-00163e002839");
			model.setAppId(1l);
			financialContractHandler.createNewFinancialContract(model, null);
		}catch (CreateException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		CreateFinancialContractModel model = new CreateFinancialContractModel();
		model.setFinancialContractName("trustsContractName1");
		model.setAdvaStartDate("2016-11-21");
		model.setAccountName("accountName1");
		model.setAccountNo("accountNo1");
		model.setBankName("中国银行");
		model.setRemittanceStrategyMode(3);
		model.setAssetPackageFormat(1);
		model.setAdvaRepaymentTerm(4);
		model.setAdvaRepoTerm(6);
		model.setFinancialContractNo("trustsContractNo1");
		model.setCompanyUuid("a02c02b9-6f98-11e6-bf08-00163e002839");
		model.setAppId(1l);
		FinancialContract financialContract = financialContractHandler.createNewFinancialContract(model, null);
		Assert.assertEquals("trustsContractNo1", financialContract.getContractNo());
		Assert.assertEquals(0, financialContract.getAdvaMatuterm());
		Assert.assertEquals(6, financialContract.getAdvaRepoTerm());
		
		
//		model.setCompanyUuid("uuid1");
//		financialContract = financialContractHandler.createNewFinancialContract(model);
//		Assert.assertEquals(null, ledgerBookService.getBookByBookNo(financialContract.getLedgerBookNo()));
		
//		model.setCompanyUuid("a02c02b9-6f98-11e6-bf08-00163e002839");
//		model.setAppId(2l);
//		financialContract = financialContractHandler.createNewFinancialContract(model);
//		Assert.assertEquals(null, financialContract.getApp());
		
		
		model.setAdvaMatuterm(6);
		model.setRemittanceStrategyMode(1);
	    model.setAppAccounts("[{\"accountNo\":\"dhg\",\"accountName\":\"gh\",\"bankName\":\"工商银行\"}]");
		financialContract = financialContractHandler.createNewFinancialContract(model, null);
		Assert.assertEquals("gh", accountService.getAccountBy(financialContract.getAppAccountUuidList().get(0)).getAccountName());
		Assert.assertEquals(6, financialContract.getAdvaMatuterm());
		
	}
	
	@Test
	@Sql("classpath:test/test4editFinancialContractBasicInfo.sql")
	public void testModifyFinancialContractBasicInfo(){
		try{
			FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
			financialContractHandler.modifyFinancialContractBasicInfo(null,financialContract);
		}catch (ModifyException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		try{
			ModifyFinancialContractBasicInfoModel model = new ModifyFinancialContractBasicInfoModel();
			model.setFinancialContractNo("trustsContractNo1");
			financialContractHandler.modifyFinancialContractBasicInfo(model,null);
		}catch (ModifyException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		try{
			ModifyFinancialContractBasicInfoModel model = new ModifyFinancialContractBasicInfoModel();
			model.setFinancialContractNo("trustsContractNo1");
			model.setFinancialContractName("trustsContractName1");
			model.setCompanyUuid("uuid1");
			model.setAppId(1l);
			model.setAdvaStartDate("2016-11-21");
			model.setAccountName("accountName1");
			model.setAccountNo("accountNo1");
	//		model.setBankName("中国银行");
			FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
			financialContractHandler.modifyFinancialContractBasicInfo(model,financialContract);
			Assert.assertEquals(null, financialContract);
		}catch (ModifyException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		ModifyFinancialContractBasicInfoModel model = new ModifyFinancialContractBasicInfoModel();
		model.setFinancialContractNo("trustsContractNo1");
		model.setFinancialContractName("trustsContractName1");
		model.setCompanyUuid("uuid1");
		model.setAppId(1l);
		model.setAdvaStartDate("2016-11-21");
		model.setAccountName("accountName1");
		model.setAccountNo("accountNo1");
		model.setBankName("交通银行");
		FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
		financialContract = financialContractHandler.modifyFinancialContractBasicInfo(model,financialContract);
		Assert.assertEquals("交通银行", financialContract.getCapitalAccount().getBankName());
		
	}
	
	@Test
	@Sql("classpath:test/test4editFinancialContractBasicInfo.sql")
	public void testModifyFinancialContractBasicInfo1(){
		ModifyFinancialContractBasicInfoModel model = new ModifyFinancialContractBasicInfoModel();
		model.setFinancialContractNo("trustsContractNo1");
		model.setFinancialContractName("trustsContractName1");
		model.setCompanyUuid("uuid1");
		model.setAppId(1l);
		model.setAdvaStartDate("2016-11-21");
		model.setAccountName("accountName1");
		model.setAccountNo("accountNo1");
		model.setBankName("交通银行");

		FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e28");
		financialContract = financialContractHandler.modifyFinancialContractBasicInfo(model,financialContract);
		Assert.assertEquals("交通银行", financialContract.getCapitalAccount().getBankName());
		
	}
	
	@Test
	@Sql("classpath:test/test4editFinancialContractBasicInfo.sql")
	public void testModifyFinancialContractRemittanceInfo(){
		try{
			FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
			financialContractHandler.modifyFinancialContractRemittanceInfo(null,financialContract);
		}catch (ModifyException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		try{
			ModifyFinancialContractRemittanceInfoModel model = new ModifyFinancialContractRemittanceInfoModel();
			financialContractHandler.modifyFinancialContractRemittanceInfo(model,null);
		}catch (ModifyException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		try{
			ModifyFinancialContractRemittanceInfoModel model = new ModifyFinancialContractRemittanceInfoModel();
			model.setRemittanceStrategyMode(3);
			FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
			financialContract = financialContractHandler.modifyFinancialContractRemittanceInfo(model,financialContract);
		}catch (ModifyException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		try{
			ModifyFinancialContractRemittanceInfoModel model = new ModifyFinancialContractRemittanceInfoModel();
			model.setRemittanceStrategyMode(1);
			FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
			financialContract = financialContractHandler.modifyFinancialContractRemittanceInfo(model,financialContract);
		}catch (ModifyException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		try{
			ModifyFinancialContractRemittanceInfoModel model = new ModifyFinancialContractRemittanceInfoModel();
			model.setRemittanceStrategyMode(1);
			model.setTransactionLimitPerDay(new BigDecimal(7));
			model.setTransactionLimitPerTranscation(new BigDecimal(7));
			model.setAppAccounts("[{\"accountNo\":\"dhg\",\"accountName\":\"gh\",\"bankName\":\"工商银行\"}]");
			FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
			financialContract = financialContractHandler.modifyFinancialContractRemittanceInfo(model,financialContract);
		}catch (ModifyException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		ModifyFinancialContractRemittanceInfoModel model = new ModifyFinancialContractRemittanceInfoModel();
		model.setTransactionLimitPerDay(new BigDecimal(7));
		model.setTransactionLimitPerTranscation(new BigDecimal(7));
		model.setRemittanceStrategyMode(0);
		model.setAppAccounts("[{\"accountNo\":\"dhg\",\"accountName\":\"gh\",\"bankName\":\"工商银行\"}]");
		FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
		financialContract = financialContractHandler.modifyFinancialContractRemittanceInfo(model,financialContract);
		Assert.assertEquals("gh", accountService.getAccountBy(financialContract.getAppAccountUuidList().get(0)).getAccountName());		
		
		model.setAppAccounts("[{\"accountNo\":\"dhg\",\"accountName\":\"gh\",\"bankName\":\"工商银行\",\"uuid\":\"uuid_5\"}]");
		financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e28");
		financialContract = financialContractHandler.modifyFinancialContractRemittanceInfo(model,financialContract);
		Assert.assertEquals("gh", accountService.getAccountBy(financialContract.getAppAccountUuidList().get(0)).getAccountName());		
	}
	
	@Test
	@Sql("classpath:test/test4editFinancialContractBasicInfo.sql")
	public void testModifyFinancialContractRepaymentInfo(){
		try{
			financialContractHandler.modifyFinancialContractRepaymentInfo(null,null);
		}catch (ModifyException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		try{
			FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
			financialContractHandler.modifyFinancialContractRepaymentInfo(null,financialContract);
		}catch (ModifyException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		try{
			ModifyFinancialContractRepaymentInfoModel model = new ModifyFinancialContractRepaymentInfoModel();
			financialContractHandler.modifyFinancialContractRepaymentInfo(model,null);
		}catch (ModifyException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		try{
			ModifyFinancialContractRepaymentInfoModel model = new ModifyFinancialContractRepaymentInfoModel();
			model.setAdvaRepaymentTerm(-1);
			FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
			financialContractHandler.modifyFinancialContractRepaymentInfo(model,financialContract);
		}catch (ModifyException e) {
			Assert.assertEquals("请求参数错误！", e.getMessage());
		}
		
		try{
			ModifyFinancialContractRepaymentInfoModel model = new ModifyFinancialContractRepaymentInfoModel();
			model.setAdvaRepaymentTerm(5);
			model.setAdvaRepoTerm(6);
			model.setAssetPackageFormat(1);
			FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
			financialContractHandler.modifyFinancialContractRepaymentInfo(model,financialContract);
			}catch (ModifyException e) {
				Assert.assertEquals("请求参数错误！", e.getMessage());
			}
		
		ModifyFinancialContractRepaymentInfoModel model = new ModifyFinancialContractRepaymentInfoModel();
		model.setAdvaRepaymentTerm(5);
		model.setAdvaRepoTerm(8);
		model.setAssetPackageFormat(1);
		FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
		financialContract = financialContractHandler.modifyFinancialContractRepaymentInfo(model,financialContract);
		Assert.assertEquals(5, financialContract.getAdvaRepaymentTerm());
		Assert.assertEquals(8, financialContract.getAdvaRepoTerm());
		
		model.setAdvaMatuterm(8);
		financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
		financialContract = financialContractHandler.modifyFinancialContractRepaymentInfo(model,financialContract);
		Assert.assertEquals(8, financialContract.getAdvaMatuterm());		
	}
	
	
	@Test
	public void test_AI(){
		
		
		String text = "（ 本 金+ 利   息)*罚息     利率*天数".replaceAll(" ", "");
		AssetSet as = new AssetSet();
		as.setAssetPrincipalValue(new BigDecimal("1000.04"));
		as.setAssetInterestValue(new BigDecimal("100.03"));
		FinancialContract fc = new FinancialContract();
		Contract contract = new Contract();
		contract.setPenaltyInterest(new BigDecimal("0.001"));
		Map<String, Object> ormMap = new LinkedHashMap<String, Object>(){{
			put("本金", as.getAssetPrincipalValue());
			put("利息", as.getAssetInterestValue());
			put("罚息利率", contract.getPenaltyInterest());
			put("天数", 121);
			put("sin", "Math.sin");
			put("（", "(");
			put("）", ")");
		}};
		
		/*String[] text_s = text.split(" ");
		String expressions = "";
		for (String s : text_s) {
			if(ormMap.containsKey(s)){
				expressions += ormMap.get(s).toString();
			}else{
				expressions += s;
			}
		}
		System.out.println(expressions);*/
		
		for (Map.Entry<String, Object> mmm : ormMap.entrySet()) {
			text = text.replaceAll(mmm.getKey(), mmm.getValue().toString());
		}
		System.out.println(text);
		// 创建脚本引擎管理器  
	    ScriptEngineManager sem = new ScriptEngineManager();  
	    // 创建一个处理JavaScript的脚本引擎  
	    ScriptEngine engine = sem.getEngineByExtension("js");  
	    try {  
	        // 执行js公式  
	    	Object sObject = engine.eval(text);  // 精确度
	        System.out.println(sObject.toString());  
	    } catch (ScriptException ex) {
	        ex.printStackTrace();  
	    }  
		
		
	    System.out.println((new BigDecimal("1000.04").add(new BigDecimal("100.03"))).multiply(new BigDecimal("0.001")).multiply(new BigDecimal("121")));  
		
		
	}


    @Test
    @Ignore("")
    public void test_AI_001() {
//		String text = "( 本 金+ 利   息)*罚息     利率*天数*10/100".replaceAll(" ", "");
        String text = "11111/1*罚息利率".replaceAll(" ", "");
        System.out.println(text);
        AviatorEvaluator.setOption(Options.ALWAYS_USE_DOUBLE_AS_DECIMAL, true);
        Expression compiledExp = AviatorEvaluator.compile(text);

        AssetSet as = new AssetSet();
        as.setAssetPrincipalValue(new BigDecimal("1000.04"));
        as.setAssetInterestValue(new BigDecimal("100.03"));
        FinancialContract fc = new FinancialContract();
        Contract contract = new Contract();
        contract.setPenaltyInterest(new BigDecimal("0.001"));

        Map<String, Object> ormMap = new HashMap<String, Object>() {{
            put("本金", as.getAssetPrincipalValue());
            put("利息", as.getAssetInterestValue());
            put("罚息利率", contract.getPenaltyInterest());
            put("天数", 121);
        }};

        Map<String, Object> ormMap002 = new HashMap<String, Object>();
        ormMap002.put("本金", as.getAssetPrincipalValue());
        ormMap002.put("利息", as.getAssetInterestValue());
        ormMap002.put("罚息利率", contract.getPenaltyInterest());
        ormMap002.put("天数", 121);
        ormMap002.put("%", "/100");

//		BigDecimal result = (BigDecimal) AviatorEvaluator.execute(text, ormMap);

        BigDecimal result = BigDecimal.ZERO;


        Object obj = compiledExp.execute(ormMap002);

        result = new BigDecimal(obj.toString()).setScale(2, RoundingMode.HALF_UP);
//		if(obj instanceof Long){
//			result = new BigDecimal((Long)obj );
//		}else if(obj instanceof BigDecimal){
//			result = (BigDecimal) obj;
//		}else {
//			result = new BigDecimal(obj.toString());
//		}
        System.out.println(result);
        System.out.println((new BigDecimal("1000.04").add(new BigDecimal("100.03"))).multiply(new BigDecimal("0.001")).multiply(new BigDecimal("121")));
    }

    @Test
    @Ignore("")
    public void test_AI_spel() {

        String text = "( 本 金+ 利   息)*罚息     利率*天数*10%".replaceAll(" ", "");
        AssetSet as = new AssetSet();
        as.setAssetPrincipalValue(new BigDecimal("1000.04"));
        as.setAssetInterestValue(new BigDecimal("100.03"));
        FinancialContract fc = new FinancialContract();
        Contract contract = new Contract();
        contract.setPenaltyInterest(new BigDecimal("0.001"));

        Map<String, Object> ormMap002 = new HashMap<String, Object>();
        ormMap002.put("本金", as.getAssetPrincipalValue().toString());
        ormMap002.put("利息", as.getAssetInterestValue().toString());
        ormMap002.put("罚息利率", contract.getPenaltyInterest().toString());
        ormMap002.put("天数", 121);


        ExpressionParser parser = new SpelExpressionParser();
        org.springframework.expression.Expression exp = parser.parseExpression("(12+12)*2.01");
        /*BigDecimal amount  = (BigDecimal) exp.getValue();
        System.out.println(amount);*/
    }

	@Test
	public void test_translation(){

		String text = "( 本 金+ 利   息)*罚息     利率*逾期天数*10%";
		Assert.assertEquals("(principal+interest)*penaltyInterest*overdueDay*10/100", (CnEnTranslation.convertPenaltyCnToEn(text)));

		text = "(principal+interest)*penaltyInterest*overdueDay*10/100";
		Assert.assertEquals("(本金+利息)*罚息利率*逾期天数*10%",(CnEnTranslation.convertPenaltyEnToCn(text)));

	}

	@Test
	@Ignore
	public void test(){
		AviatorEvaluator.setOption(Options.ALWAYS_USE_DOUBLE_AS_DECIMAL, true);
		Expression compiledExp = AviatorEvaluator.compile("700*0.05%");
		BigDecimal result = new BigDecimal(compiledExp.execute().toString());
		System.out.println(result);
	}
    @Test
    @Ignore("")
    public void test_compile() {
        AviatorEvaluator.setOption(Options.ALWAYS_USE_DOUBLE_AS_DECIMAL, true);
        Expression compiledExp = AviatorEvaluator.compile("700*0.05%");
        BigDecimal result = new BigDecimal(compiledExp.execute().toString());
        System.out.println(result);
    }

    @Test
    @Ignore("")
    public void test_janino() {
        try {

            ExpressionEvaluator ee = new ExpressionEvaluator();

            List<String> paramNameList = new ArrayList<String>();
            paramNameList.add("a");
            paramNameList.add("b");
            paramNameList.add("c");

            String[] paramName = new String[paramNameList.size()];
            paramName = paramNameList.toArray(paramName);

            List<Class> paramsClassList = new ArrayList<Class>();
            paramsClassList.add(BigDecimal.class);
            paramsClassList.add(BigDecimal.class);
            paramsClassList.add(BigDecimal.class);

            Class[] paramsClass = (Class[]) paramsClassList.toArray(new Class[paramsClassList.size()]);

            List<Object> paramsValueList = new ArrayList<Object>();
            paramsValueList.add(new BigDecimal("11.2"));
            paramsValueList.add(new BigDecimal("22.3"));
            paramsValueList.add(new BigDecimal("33.4"));

            Object[] paramsValue = (Object[]) paramsValueList.toArray(new Object[paramsValueList.size()]);

            ee.setParameters(paramName, paramsClass);
            ee.setExpressionType(BigDecimal.class);
            ee.cook("a + b");
            BigDecimal result = (BigDecimal) ee.evaluate(paramsValue);
            System.out.println(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
  	@Sql("classpath:test/test_editFinancialContractBasicInfo.sql")
  	public void testmodifyFinancialContractConfiguration(){
      	try{
  			financialContractHandler.modifyFinancialContractConfiguration("",FinancialContractConfigurationCode.ALLOW_MODIFY_REMITTANCE_APPLICATION.getCode(),null);
  		}catch (ModifyException e) {
  			Assert.assertEquals("请求参数错误！", e.getMessage());
  		}
      	try{
  			financialContractHandler.modifyFinancialContractConfiguration(null,FinancialContractConfigurationCode.ALLOW_MODIFY_REMITTANCE_APPLICATION.getCode(),null);
  		}catch (ModifyException e) {
  			Assert.assertEquals("请求参数错误！", e.getMessage());
  		}

    	financialContractHandler.modifyFinancialContractConfiguration("d2812bc5-5057-4a91-b3fd-9019506f0499",FinancialContractConfigurationCode.ALLOW_MODIFY_REMITTANCE_APPLICATION.getCode(),0);
    	
    	FinancialContractConfiguration financialContractConfiguration = financialContractConfigurationService.load(FinancialContractConfiguration.class, 4L);
    	String content = financialContractConfiguration.getContent();
    	
    	Assert.assertEquals("0",content);
    	

      	     	
    }
      	

}