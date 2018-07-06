/**
 * 
 */
package com.zufangbao.earth.yunxin.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.suidifu.coffer.entity.AccountSide;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.yunxin.web.controller.AccountManagementController;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.account.DepositeAccountType;
import com.zufangbao.sun.entity.account.special.account.ClearingExecLog;
import com.zufangbao.sun.entity.account.special.account.ExecLogClearingStatus;
import com.zufangbao.sun.entity.account.special.account.SpecialAccount;
import com.zufangbao.sun.entity.account.special.account.SpecialAccountBasicType;
import com.zufangbao.sun.entity.account.special.account.SpecialAccountFlow;
import com.zufangbao.sun.entity.account.special.account.SpecialAccountType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.EntryLevel;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerBookHandlerImpl;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.yunxin.entity.model.AccountManagementModel;
import com.zufangbao.sun.yunxin.entity.model.AccountManagementShowModel;
import com.zufangbao.sun.yunxin.entity.model.QueryModifyAccrualAccountShowModel;
import com.zufangbao.sun.yunxin.entity.model.SpecialAccountInitializationModel;
import com.zufangbao.sun.yunxin.service.account.ClearingExecLogService;
import com.zufangbao.sun.yunxin.service.account.SpecialAccountFlowService;
import com.zufangbao.sun.yunxin.service.account.SpecialAccountService;
import com.zufangbao.sun.yunxin.service.account.SpecialAccountTypeService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.dst.handler.DstJobClearingAssetReconciliation;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import com.zufangbao.sun.yunxin.entity.model.AssetClearingReconParameters;
import com.zufangbao.wellsfargo.yunxin.handler.SpecialAccountTypeHandler;
import com.zufangbao.wellsfargo.yunxin.handler.impl.DataStatisticsCacheHandlerImpl;
import com.zufangbao.wellsfargo.yunxin.handler.specialaccount.SpecialAccountHandler;
import com.zufangbao.wellsfargo.yunxin.handler.specialaccount.SpecialAccountTaskNoSession;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml",
		"classpath:/local/DispatcherServlet.xml" })
@Rollback(true)
@Transactional()
@WebAppConfiguration(value="webapp")
public class AccountManageMentControllerTest {
	@Autowired
	private SpecialAccountTypeService specialAccountTypeService;
	@Autowired
	private SpecialAccountHandler specialAccountHandler;
	@Autowired
	private SpecialAccountService specialAccountService;
	@Autowired
	private SpecialAccountTaskNoSession specialAccountTaskHandler;
	@Autowired
	private DstJobClearingAssetReconciliation dstJobClearingAssetReconciliation;
	@Autowired
	private ClearingExecLogService clearingExecLogService;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private AccountManagementController accountManagementController;
	@Autowired
	private PrincipalService principalService;
	@Autowired
	private SpecialAccountTypeHandler specialAccountTypeHandler;
	@Autowired
	private SpecialAccountFlowService specialAccountFlowService;
	@Autowired
	private LedgerItemService ledgerItemService;
	@Autowired
	private LedgerBookService ledgerBookService;
	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;
	@Autowired
	private LedgerBookHandler ledgerBookHandher;

	@Test
	public void getSpecialAccountTypeByBasicAccountTypeTest(){
		List<SpecialAccountType> specialAccountTypes  = specialAccountTypeService.getSpecialAccountTypeByBasicAccountType(SpecialAccountBasicType.getTotalSpecialAccountBasicType());
		List<SpecialAccountType> fstLevel = new ArrayList<>();
		List<SpecialAccountType> sndLevel = new ArrayList<>();
		List<SpecialAccountType> trdLevel = new ArrayList<>();
		for(SpecialAccountType specialAccountType: specialAccountTypes){
			switch (specialAccountType.getLevel().ordinal()) {
				case 0 :fstLevel.add(specialAccountType);break;
				case 1 :sndLevel.add(specialAccountType);break;
				case 2 :trdLevel.add(specialAccountType);break;
			}
		}
		
		Assert.assertEquals(specialAccountTypes.size(), 20);
		Assert.assertEquals(fstLevel.size(), 1);
		Assert.assertEquals(sndLevel.size(), 5);
		Assert.assertEquals(trdLevel.size(), 14);
	}
	@Test
	public void createSpecialAccountInitializationModelTest(){
		List<SpecialAccountType> specialAccountTypes  = specialAccountTypeService.getSpecialAccountTypeByBasicAccountType(SpecialAccountBasicType.getTotalSpecialAccountBasicType());
		SpecialAccountInitializationModel model = specialAccountTypeHandler.createSpecialAccountInitializationModel(specialAccountTypes);
		System.out.println("===="+JsonUtils.toJsonString(model));
	}
	
	@Test
	@Sql(value = {"classpath:test/yunxin/specialAccount/initializeSpecialAccountTest.sql"})
	public void initializeSpecialAccountTest(){
		final String financialContractUuid = "financialContractUuid";
		String JTtrdmodelStr = "[{\"accountTypeCode\":\"30000.1000\",\"accountTypeName\":\"计提户-xx\",\"basicAccountType\":5,\"level\":2}]";
		
		String HKtrdmodelStr ="[{\"accountTypeCode\":\"40000.04\",\"accountTypeName\":\"还款本金\",\"basicAccountType\":4,\"level\":2},{\"accountTypeCode\":\"70000.03\",\"accountTypeName\":\"还款利息\",\"basicAccountType\":4,\"level\":2},{\"accountTypeCode\":\"70000.05.01\",\"accountTypeName\":\"贷款服务费\",\"basicAccountType\":4,\"level\":2},"
				+ "{\"accountTypeCode\":\"70000.05.02\",\"accountTypeName\":\"技术维护费\",\"basicAccountType\":4,\"level\":2},{\"accountTypeCode\":\"70000.05.03\",\"accountTypeName\":\"其他费用\",\"basicAccountType\":4,\"level\":2},"
				+ "{\"accountTypeCode\":\"70000.01\",\"accountTypeName\":\"逾期罚息\",\"basicAccountType\":4,\"level\":2},{\"accountTypeCode\":\"70000.06.01\",\"accountTypeName\":\"逾期违约金\",\"basicAccountType\":4,\"level\":2},"
				+ "{\"accountTypeCode\":\"70000.06.02\",\"accountTypeName\":\"逾期服务费\",\"basicAccountType\":4,\"level\":2},{\"accountTypeCode\":\"70000.06.03\",\"accountTypeName\":\"逾期其他费用\",\"basicAccountType\":4,\"level\":2}]";
		
		
		String sndmodelStr =  "[{\"accountTypeCode\":\"60000.1000\",\"accountTypeName\":\"暂存户\",\"basicAccountType\":1,\"level\":1},{\"accountTypeCode\":\"60000.1000.31\",\"accountTypeName\":\"放款户\",\"basicAccountType\":2,\"level\":1},{\"accountTypeCode\":\"50000\",\"accountTypeName\":\"客户账户\",\"basicAccountType\":3,\"level\":1},"
				+ "{\"accountTypeCode\":\"70000\",\"accountTypeName\":\"还款户\",\"basicAccountType\":4,\"level\":1,\"childAccountTypeString\":"+HKtrdmodelStr+"},{\"accountTypeCode\":\"30000\",\"accountTypeName\":\"计提户\",\"basicAccountType\":5,\"level\":1,\"childAccountTypeString\":"+JTtrdmodelStr+"}]";

		String fstmodelStr = "{\"accountTypeCode\":\"60000\",\"accountTypeName\":\"信托专户\",\"basicAccountType\":0,\"level\":0,\"childAccountTypeString\":"+sndmodelStr+"}";

		System.out.println("================"+fstmodelStr);
		
		SpecialAccountInitializationModel model = JsonUtils.parse(fstmodelStr, SpecialAccountInitializationModel.class);
		specialAccountHandler.initializeSpecialAccount(financialContractUuid, model,StringUtils.EMPTY);
		
		List<String> fstFinancialAccount = specialAccountService.getAccountUuid(financialContractUuid, EntryLevel.LVL1, SpecialAccountBasicType.UNKNOWN );
		Assert.assertEquals(fstFinancialAccount.size(), 1);
		List<String> sndPendingAccount = specialAccountService.getAccountUuid(financialContractUuid, EntryLevel.LVL2, SpecialAccountBasicType.PENDING );
		Assert.assertEquals(sndPendingAccount.size(), 1);
		List<String> sndAccrualAccount = specialAccountService.getAccountUuid(financialContractUuid, EntryLevel.LVL2, SpecialAccountBasicType.ACCRUAL );
		Assert.assertEquals(sndAccrualAccount.size(), 1);
		List<String> trdAccrualAccount = specialAccountService.getAccountUuid(financialContractUuid, EntryLevel.LVL3, SpecialAccountBasicType.ACCRUAL );
		Assert.assertEquals(trdAccrualAccount.size(), 1);
		List<String> sndBeneficiaryAccount = specialAccountService.getAccountUuid(financialContractUuid, EntryLevel.LVL2, SpecialAccountBasicType.BENEFICIARY_ACCOUNT );
		Assert.assertEquals(sndBeneficiaryAccount.size(), 1);
		List<String> trdBeneficiaryAccount = specialAccountService.getAccountUuid(financialContractUuid, EntryLevel.LVL3, SpecialAccountBasicType.BENEFICIARY_ACCOUNT );
		Assert.assertEquals(trdBeneficiaryAccount.size(), 9);
	}
	
	@Test
	@Sql(value = {"classpath:test/yunxin/specialAccount/specialAccountForCustomeUpdateAmountTest.sql"})
	public void testHandlerSpecialAccountForCustomerType(){
	
		List<SpecialAccount> specialAccountList = specialAccountService.loadAll(SpecialAccount.class);
		SpecialAccount specialAccount1 = specialAccountList.get(0);
		SpecialAccount specialAccount2 = specialAccountList.get(1);
		Assert.assertEquals(new BigDecimal("100.00"), specialAccount1.getBalance());
		Assert.assertEquals(new BigDecimal("200.00"), specialAccount2.getBalance());
		
		specialAccountTaskHandler.handlerSpecialAccountForCustomerType();
		
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		
		List<SpecialAccount> specialAccountAfterList = specialAccountService.list(SpecialAccount.class, new Filter());
		
		SpecialAccount specialAccountAfter1 = specialAccountAfterList.get(0);
		SpecialAccount specialAccountAfter2 = specialAccountAfterList.get(1);
		Assert.assertEquals(new BigDecimal("500.00"), specialAccountAfter1.getBalance());
		Assert.assertEquals(new BigDecimal("700.00"), specialAccountAfter2.getBalance());
		Assert.assertNotEquals(specialAccount1.getVersion(), specialAccountAfter1.getVersion());
		Assert.assertNotEquals(specialAccount2.getVersion(), specialAccountAfter2.getVersion());
	}
	
	/**
	 * 专户记账
	 */
	@Test
	@Sql(value = {"classpath:test/yunxin/specialAccount/special_acount_recoverTest.sql"})
	public void special_acount_recoverTest(){
		String batchUuid = "batch_uuid";
		List<AssetClearingReconParameters> paramList = new ArrayList<>();
		String financialContractUuid = "financial_contract_uuid_1";
		FinancialContract financialContract = financialContractService.getCacheableFinancialContracBy(financialContractUuid);
		LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(financialContract.getLedgerBookNo());
		
		List<ClearingExecLog> clearingExecLogs = clearingExecLogService.queryNeedToRecordOfNeedClear(JournalVoucherType.JournalVoucherTypeUsedSpecialAccount(), batchUuid);
		for(ClearingExecLog clearingExecLog: clearingExecLogs){
			AssetClearingReconParameters parameters =new AssetClearingReconParameters(clearingExecLog.getUuid(),clearingExecLog.getFinancialContractUuid(),financialContract.getLedgerBookNo(),clearingExecLog.getBatchUuid());
			paramList.add(parameters);
		}
		//fst step(记账小合同,大合同)
		Boolean fstFlag = dstJobClearingAssetReconciliation.contract_clearing(paramList);
		
		Assert.assertEquals(fstFlag, true);
		List<ClearingExecLog> clearingExecLogsOfContract =	clearingExecLogService.queryNeedToRecordByStatus(JournalVoucherType.JournalVoucherTypeUsedSpecialAccount(), batchUuid, ExecLogClearingStatus.CONTRACT_CLEARED);
		Assert.assertEquals(clearingExecLogsOfContract.size(), 4);
		
		AssetCategory assetCategory = new AssetCategory();
		assetCategory.setFstLvLAssetUuid("asset_uuid_1");
		List<LedgerItem> ledgerItems = ledgerItemService.get_all_ledgers_of_asset(ledgerBook, assetCategory);
		Assert.assertEquals(ledgerItems.size(), 28);
		List<LedgerItem> ledgerItemsAll = ledgerItemService.loadAll(LedgerItem.class);
		Assert.assertEquals(ledgerItemsAll.size(), 56);
		
		Map<String, BigDecimal> contractAmountMap=ledgerBookStatHandler.get_contract_amount_by_batch_uuid(ledgerBook.getLedgerBookNo(), batchUuid);
		Map<String, BigDecimal> financialAmountMap=ledgerBookStatHandler.get_financial_amount_by_batch_uuid(ledgerBook.getLedgerBookNo(), batchUuid);
		
		Assert.assertEquals(contractAmountMap.get(ChartOfAccount.TRD_BANK_SAVING_GENERAL_REMITTANCE_PRINCIPAL), new BigDecimal("-9100.00"));
		Assert.assertEquals(contractAmountMap.get(ChartOfAccount.TRD_BANK_SAVING_GENERAL_REMITTANCE_INTEREST), new BigDecimal("-500.00"));
		Assert.assertEquals(contractAmountMap.get(ChartOfAccount.TRD_BANK_SAVING_GENERAL_REMITTANCE_LOAN_ASSET_LOAN_SERVICE_FEE), new BigDecimal("-300.00"));
		Assert.assertEquals(contractAmountMap.get(ChartOfAccount.TRD_BANK_SAVING_GENERAL_REMITTANCE_LOAN_ASSET_TECH_FEE), new BigDecimal("-100.00"));
		Assert.assertEquals(financialAmountMap.get(ChartOfAccount.TRD_BANK_SAVING_GENERAL_PRINCIPAL), new BigDecimal("9100.00"));
		Assert.assertEquals(financialAmountMap.get(ChartOfAccount.TRD_BANK_SAVING_GENERAL_INTEREST), new BigDecimal("500.00"));
		Assert.assertEquals(financialAmountMap.get(ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE), new BigDecimal("300.00"));
		Assert.assertEquals(financialAmountMap.get(ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE), new BigDecimal("100.00"));


		//=============
		

		//snd step(更新大合同)
		Boolean sndFlag = dstJobClearingAssetReconciliation.update_financial_contract_account(paramList);
		
		Assert.assertEquals(sndFlag, true);
		
		SpecialAccount trd_independent_account = specialAccountService.get("b901c09b-ac99-490f-ba59-b6edab5c2775");

		SpecialAccount snd_beneficiary_account = specialAccountService.get("9e2b5c30-bc10-4d49-a285-370cc8fdedfc");
		SpecialAccount trd_beneficiary_account_principal = specialAccountService.get("088aa3f9-d89c-4103-90ae-b78734c0030b");
		SpecialAccount snd_beneficiary_account_interest = specialAccountService.get("ccc73391-6adf-4fcc-94f4-5feefa6a867f");
		SpecialAccount snd_beneficiary_account_service = specialAccountService.get("5194f910-cfe4-4ec1-9fbd-f724470dd556");
		
		Assert.assertEquals(snd_beneficiary_account.getBalance(), new BigDecimal("10000.00"));
		Assert.assertEquals(trd_beneficiary_account_principal.getBalance(), new BigDecimal("9100.00"));
		Assert.assertEquals(snd_beneficiary_account_interest.getBalance(), new BigDecimal("500.00"));
		Assert.assertEquals(snd_beneficiary_account_service.getBalance(), new BigDecimal("300.00"));
		
		AccountManagementModel model = new AccountManagementModel();
		model.setFstLevelContractUuid(financialContractUuid);
		List<SpecialAccountFlow> specialAccountFlows = specialAccountFlowService.loadAll(SpecialAccountFlow.class);
		Assert.assertEquals(specialAccountFlows.size(), 8);
		
		List<SpecialAccountFlow> hostAndcounterAccount = new ArrayList<>();
		List<SpecialAccountFlow> counterAndhostAccount = new ArrayList<>();
		for(SpecialAccountFlow specialAccountFlow : specialAccountFlows){
			if(specialAccountFlow.getHostAccountUuid().equals(trd_independent_account.getUuid())){
				hostAndcounterAccount.add(specialAccountFlow);
			}
			if(specialAccountFlow.getCounterAccountUuid().equals(trd_independent_account.getUuid())){
				counterAndhostAccount.add(specialAccountFlow);
			}
		}
		Assert.assertEquals(hostAndcounterAccount.size(), 4);
		Assert.assertEquals(counterAndhostAccount.size(), 4);
		
		List<ClearingExecLog> clearingExecLogsOfCleared =	clearingExecLogService.queryNeedToRecordByStatus(JournalVoucherType.JournalVoucherTypeUsedSpecialAccount(), batchUuid, ExecLogClearingStatus.FINANCIALCONTRACT_CLEARED);
		
		Assert.assertEquals(clearingExecLogsOfCleared.size(), 4);
		
	}
	

	@Test
	@Sql(value = {"classpath:test/yunxin/specialAccount/specialAccountFlowForQueryTest.sql"})
	public void testQuerySpecialAccountFlowList(){
	
		Page rawpage = new Page();
		rawpage.setBeginIndex(0);
		rawpage.setCurrentPage(1);
		rawpage.setEveryPage(12);
		Principal principal = principalService.load(Principal.class, 1L);
		AccountManagementModel model = new AccountManagementModel();
		model.setAccountSide("[1]");
		model.setCounterAccountName("客户账户");
		model.setFstLevelContractUuid("financial_contract_uuid_1");
		model.setUuid("uuid_1");
		
		String resultString = accountManagementController.querySpecialAccountFlowList(principal, model, rawpage);
		
		Result result = JsonUtils.parse(resultString, Result.class);
		Map<String,Object> map =result.getData();
		JSONArray showModelListArray =  (JSONArray) map.get("list");
		
		List<AccountManagementShowModel> showModelList = JsonUtils.parseArray(JSONObject.toJSONString(showModelListArray),AccountManagementShowModel.class);
		Assert.assertEquals(2, showModelList.size());
		
		AccountManagementShowModel model1 = showModelList.get(0);
		AccountManagementShowModel model2 = showModelList.get(1);
		
		Assert.assertEquals("account_flow_no_1", model1.getAccountFlowNo());
		Assert.assertEquals("借", model1.getAccountSide());
		Assert.assertEquals("注资", model1.getAccountTransType());
		Assert.assertEquals(new BigDecimal("50.00"), model1.getBalance());
		Assert.assertEquals("bankSequenceNo1", model1.getBankSequenceNo());
		Assert.assertEquals("客户账户", model1.getCounterAccountName());
		Assert.assertEquals("remark_1", model1.getRemark());
		Assert.assertEquals(new BigDecimal("200.00"), model1.getTransactionAmount());
		Assert.assertEquals(DateUtils.parseDate("2017-12-25 15:33:13", com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT), model1.getHappenDate());
		
		
		Assert.assertEquals("account_flow_no_2", model2.getAccountFlowNo());
		Assert.assertEquals("借", model2.getAccountSide());
		Assert.assertEquals("放款处理", model2.getAccountTransType());
		Assert.assertEquals(new BigDecimal("70.00"), model2.getBalance());
		Assert.assertEquals("bankSequenceNo2", model2.getBankSequenceNo());
		Assert.assertEquals("客户账户", model2.getCounterAccountName());
		Assert.assertEquals("remark_2", model2.getRemark());
		Assert.assertEquals(new BigDecimal("500.00"), model2.getTransactionAmount());
		Assert.assertEquals(DateUtils.parseDate("2017-12-25 15:34:13", com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT), model2.getHappenDate());
		
	}

	@Test
	//用于初始化生成专户账户初始化数据
	@Sql(value = {"classpath:test/yunxin/specialAccount/initializeSpecialAccount_for_release.sql"})
	public void initializeSpecialAccount_for_release(){
		String financialContractUuid = "";
		List<SpecialAccountType> specialAccountTypes = specialAccountTypeService.getSpecialAccountTypeByBasicAccountType(SpecialAccountBasicType.getTotalSpecialAccountBasicType());
		SpecialAccountInitializationModel model= specialAccountTypeHandler.createSpecialAccountInitializationModel(specialAccountTypes);
		specialAccountHandler.initializeSpecialAccount(financialContractUuid, model,StringUtils.EMPTY);

		List<String> fstFinancialAccount = specialAccountService.getAccountUuid(financialContractUuid, EntryLevel.LVL1, SpecialAccountBasicType.UNKNOWN );
		Assert.assertEquals(fstFinancialAccount.size(), 1);
		List<String> sndPendingAccount = specialAccountService.getAccountUuid(financialContractUuid, EntryLevel.LVL2, SpecialAccountBasicType.PENDING );
		Assert.assertEquals(sndPendingAccount.size(), 1);
		List<String> sndAccrualAccount = specialAccountService.getAccountUuid(financialContractUuid, EntryLevel.LVL2, SpecialAccountBasicType.ACCRUAL );
		Assert.assertEquals(sndAccrualAccount.size(), 1);
		List<String> trdAccrualAccount = specialAccountService.getAccountUuid(financialContractUuid, EntryLevel.LVL3, SpecialAccountBasicType.ACCRUAL );
		Assert.assertEquals(trdAccrualAccount.size(), 1);
		List<String> sndBeneficiaryAccount = specialAccountService.getAccountUuid(financialContractUuid, EntryLevel.LVL2, SpecialAccountBasicType.BENEFICIARY_ACCOUNT );
		Assert.assertEquals(sndBeneficiaryAccount.size(), 1);
		List<String> trdBeneficiaryAccount = specialAccountService.getAccountUuid(financialContractUuid, EntryLevel.LVL3, SpecialAccountBasicType.BENEFICIARY_ACCOUNT );
		Assert.assertEquals(trdBeneficiaryAccount.size(), 9);
	}
	
	@Test
	@Sql(value = {"classpath:test/yunxin/specialAccount/createAccrualAccountShowModelTest.sql"})
	public void createAccrualAccountShowModelTest(){
		List<SpecialAccount> specialAccounts = specialAccountService.getAccrualAccount("financialContractUuid");
		QueryModifyAccrualAccountShowModel model = specialAccountTypeHandler.createAccrualAccountShowModel(specialAccounts);
		
		Assert.assertEquals(model.getParentAccountUuid(), "26f5b7c2-4aa9-43f4-8d71-f8e52df3a9a2");
		
		String childAccountTypeString = model.getChildAccountTypeString();
		SpecialAccountInitializationModel sndSpecialAccountInitializationModel = JsonUtils.parse(childAccountTypeString,SpecialAccountInitializationModel.class);
		List<SpecialAccountInitializationModel> trdSpecialAccountInitializationModel = JsonUtils.parseArray(sndSpecialAccountInitializationModel.getChildAccountTypeString(),SpecialAccountInitializationModel.class);
		
		Assert.assertEquals(sndSpecialAccountInitializationModel.getBasicAccountType(), 5);
		Assert.assertEquals(sndSpecialAccountInitializationModel.getAccountTypeCode(), "30000");
		Assert.assertEquals(sndSpecialAccountInitializationModel.getAccountTypeName(), "计提户");
		Assert.assertEquals(sndSpecialAccountInitializationModel.getLevel(), EntryLevel.LVL2.ordinal());
		
		Assert.assertEquals(trdSpecialAccountInitializationModel.size(), 2);
		Assert.assertEquals(trdSpecialAccountInitializationModel.get(0).getAccountTypeCode(), "30000.1000");
		Assert.assertEquals(trdSpecialAccountInitializationModel.get(0).getBasicAccountType(), 5);
		Assert.assertEquals(trdSpecialAccountInitializationModel.get(1).getAccountTypeCode(), "30000.1000");
		Assert.assertEquals(trdSpecialAccountInitializationModel.get(0).getLevel(), EntryLevel.LVL3.ordinal());
		
	}
//	@Test
//	@Sql(value = {"classpath:test/yunxin/specialAccount/accrueTest.sql"})
//	public void accrueTest(){
//		LedgerBook book = ledgerBookService.getBookByBookNo("ledger_book_no_1");
//		String detailAmountJson= "{SND_UNEARNED_LOAN_ASSET_PRINCIPLE:800.00, SND_UNEARNED_LOAN_ASSET_TECH_FEE:50.00, TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION:null,"
//									+ " TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE:null,SND_UNEARNED_LOAN_ASSET_OTHER_FEE:null, SND_RECIEVABLE_LOAN_PENALTY:null,"
//									+ " SND_UNEARNED_LOAN_ASSET_INTEREST:100.00, TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE:null, SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE:50.00}";
//		Map<String, BigDecimal> detailAmounts = JSON.parseObject(detailAmountJson, new TypeReference<Map<String,BigDecimal> >(){});
//		AssetCategory assetCategory = new AssetCategory("batchUuid");
//		LedgerTradeParty party = new LedgerTradeParty("financialContractUuid","");
//		String accue_account_name="xxx";
//		DepositeAccountInfo depositeAccountInfo = new DepositeAccountInfo(accue_account_name,new LedgerTradeParty(),DepositeAccountType.ACCRUAL);
//		new DataStatisticsCacheHandlerImpl().referesh_entry_book(Arrays.asList(depositeAccountInfo));
//		ledgerBookHandher.accrue(book, detailAmounts, assetCategory, party, depositeAccountInfo);
//
//		List<LedgerItem> ledgerItems= ledgerItemService.list(LedgerItem.class,new Filter());
//		Map<String , BigDecimal> map = new HashMap<>();
//		Set<String> account_extra_keys = LedgerBookHandlerImpl.revenue_and_accrue_table.keySet();
//		List<String> account_names = new ArrayList<String>(account_extra_keys);
//		account_names.add(accue_account_name);
//		for(LedgerItem key : ledgerItems){
//			String lastName = key.lastAccountName();
//			if(account_names.contains(lastName)==false || map.get(lastName) != null){
//				Assert.fail();
//			}
//			map.put(lastName,key.getDebitSubCreditBalance());
//		}
//
//		Assert.assertEquals(5,ledgerItems.size());
//		Assert.assertEquals(map.get(ChartOfAccount.SND_LONGTERM_LIABILITY_BENEFICIARY), new BigDecimal("-800.00"));
//		Assert.assertEquals(map.get(ChartOfAccount.SND_REVENUE_INTEREST), new BigDecimal("-100.00"));
//		Assert.assertEquals(map.get(ChartOfAccount.TRD_REVENUE_INCOME_LOAN_TECH_FEE), new BigDecimal("-50.00"));
//		Assert.assertEquals(map.get(ChartOfAccount.TRD_REVENUE_INCOME_LOAN_SERVICE_FEE), new BigDecimal("-50.00"));
//		Assert.assertEquals(map.get(accue_account_name), new BigDecimal("1000.00"));
//
//	}

  @Test
  @Sql(value = {"classpath:test/yunxin/specialAccount/transfer_revenue_to_accrual_accountTest.sql"})
  public void transfer_revenue_to_accrual_accountTest(){
    String financialContractUuid = "financial_contract_uuid_1";
    BigDecimal transactionAmount = new BigDecimal("400.00");
    SpecialAccount hostAccount = specialAccountService.get("088aa3f9-d89c-4103-90ae-b78734c0030b");
    SpecialAccount counterAccount = specialAccountService.get("df73f660-21f9-4fc6-b653-cdc484f3068f");
    String remark = "测试";

    specialAccountHandler.transfer_revenue_to_accrual_account(financialContractUuid, transactionAmount, hostAccount, counterAccount, remark);

    SpecialAccount specialAccount = specialAccountService.get("971b02e6-79fe-4449-aefa-1845f53672d7");
    SpecialAccount tsndBeneficiaryAccount = specialAccountService.get("9e2b5c30-bc10-4d49-a285-370cc8fdedfc");
    SpecialAccount trdBeneficiaryAccount = specialAccountService.get("088aa3f9-d89c-4103-90ae-b78734c0030b");
    SpecialAccount sndAccrualAccount = specialAccountService.get("ec4a9e56-be35-4e72-a9ef-84b3c8a96d9d");
    SpecialAccount trdAccrualAccount = specialAccountService.get("df73f660-21f9-4fc6-b653-cdc484f3068f");

    List<LedgerItem> ledgerItems= ledgerItemService.list(LedgerItem.class,new Filter());

    List<SpecialAccountFlow> specialAccountFlows = specialAccountFlowService.list(SpecialAccountFlow.class, new Filter());

    Assert.assertEquals(new BigDecimal("1000.00"), specialAccount.getBalance());
    Assert.assertEquals(new BigDecimal("600.00"), tsndBeneficiaryAccount.getBalance());
    Assert.assertEquals(new BigDecimal("600.00"), trdBeneficiaryAccount.getBalance());
    Assert.assertEquals(new BigDecimal("400.00"), sndAccrualAccount.getBalance());
    Assert.assertEquals(new BigDecimal("400.00"), trdAccrualAccount.getBalance());

    Assert.assertEquals(2, ledgerItems.size());

    for(LedgerItem item:ledgerItems){
      if(item.lastAccountName().equals(ChartOfAccount.SND_LONGTERM_LIABILITY_BENEFICIARY)){
        Assert.assertEquals(new BigDecimal("-400.00"),item.getDebitSubCreditBalance());
      }
      if(item.lastAccountName().equals("df73f660-21f9-4fc6-b653-cdc484f3068f")){
        Assert.assertEquals(new BigDecimal("400.00"),item.getDebitSubCreditBalance());
      }
    }

    Assert.assertEquals(2, specialAccountFlows.size());
    for(SpecialAccountFlow  cashFlow: specialAccountFlows){
      if(cashFlow.getHostAccountTypeCode()==trdBeneficiaryAccount.getAccountTypeCode() && cashFlow.getCounterAccountTypeCode() == trdAccrualAccount.getAccountTypeCode()){
        Assert.assertEquals(AccountSide.CREDIT.ordinal(), cashFlow.getAccountSide());
        Assert.assertEquals(new BigDecimal("400.00"), cashFlow.getTransactionAmount());
        Assert.assertEquals(new BigDecimal("1000.00"), cashFlow.getBalance());
        Assert.assertEquals(remark, cashFlow.getRemark());
      }
      if(cashFlow.getCounterAccountTypeCode()==trdBeneficiaryAccount.getAccountTypeCode() && cashFlow.getHostAccountTypeCode() == trdAccrualAccount.getAccountTypeCode()){
        Assert.assertEquals(AccountSide.DEBIT.ordinal(), cashFlow.getTransactionAmount());
        Assert.assertEquals(new BigDecimal("400.00"), cashFlow.getBalance());
        Assert.assertEquals(new BigDecimal("0.00"), cashFlow.getBalance());
        Assert.assertEquals(remark, cashFlow.getRemark());
      }
    }
  }
  
  @Test
  @Sql(value = {"classpath:test/yunxin/specialAccount/addBatchUuidToRecordOfClearingExecLogTest.sql"})
  public void addBatchUuidToRecordOfClearingExecLogTest(){
	  String batchUuid = "batchUuid";
	  String financialContractUuid = "financial_contract_uuid_1";
	  clearingExecLogService.addBatchUuidToRecordOfClearingExecLog(JournalVoucherType.JournalVoucherTypeUsedSpecialAccount(), batchUuid, financialContractUuid);
	  List<ClearingExecLog> clearingExecLogs= clearingExecLogService.queryNeedToRecordByStatus(JournalVoucherType.JournalVoucherTypeUsedSpecialAccount(), batchUuid,ExecLogClearingStatus.CLEARING);
	  
	  Assert.assertEquals(2, clearingExecLogs.size());
	  Assert.assertEquals("batchUuid", clearingExecLogs.get(0).getBatchUuid());
	  Assert.assertEquals(ExecLogClearingStatus.CLEARING.ordinal(), clearingExecLogs.get(0).getClearingStatus());
	  
  }
}
