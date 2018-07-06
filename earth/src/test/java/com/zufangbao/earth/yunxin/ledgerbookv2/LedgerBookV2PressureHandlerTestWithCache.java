package com.zufangbao.earth.yunxin.ledgerbookv2;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import com.zufangbao.earth.BaseTest;
import com.zufangbao.sun.ledgerbook.AccountDispersor;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.AssetConvertor;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.DuplicateAssetsException;
import com.zufangbao.sun.ledgerbook.InvalidLedgerException;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbookv2.dictionary.LedgerConstant;
import com.zufangbao.sun.ledgerbookv2.entity.AccountTemplate;
import com.zufangbao.sun.ledgerbookv2.entity.BusinessScenarioDefinition;
import com.zufangbao.sun.ledgerbookv2.enums.EventType;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.ledgerbookv2.handler.impl.LedgerTemplateEvaluator;
import com.zufangbao.sun.ledgerbookv2.handler.impl.LedgerTemplateParser;
import com.zufangbao.sun.ledgerbookv2.service.AccountTemplateService;
import com.zufangbao.sun.ledgerbookv2.service.BusinessScenarioDefinitionService;
import com.zufangbao.sun.ledgerbookv2.service.LedgerBookBatchService;
import com.zufangbao.sun.ledgerbookv2.service.LedgerItemServiceV2;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.utils.tests.LedgerBookTestUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetExtraCharge;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanExtraChargeHandler;
import com.zufangbao.sun.yunxin.service.AssetValuationDetailService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraChargeService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.silverpool.ledgerbookv2.template.GeneralAccountTemplateHelperForTestWithCache;

/**
 * 账本压力测试工具
 * 
 * @author wukai
 *
 */
@Transactional
@Rollback(false)
public class LedgerBookV2PressureHandlerTestWithCache extends BaseTest {

	@Autowired
	private LedgerBookV2Handler ledgerBookV2Handler;

	@Autowired
	private LedgerItemServiceV2 ledgerItemServiceV2;

	@Autowired
	private BusinessScenarioDefinitionService businessScenarioDefinitionService;

	@Autowired
	private AccountTemplateService accountTemplateService;

	@Autowired
	private LedgerItemService ledgerItemService;
	@Autowired
	private LedgerBookHandler anMeiTuLedgerBookHandler;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;

	@Autowired
	public LedgerBookTestUtils ledgerBookTestUtils;

	@Autowired
	private AssetValuationDetailService assetValuationDetailService;

	private String LedgerBookNo = "YUNXIN_AMEI_ledger_book";
	private String ledgerBookOrgnizationId = "14";

	@Autowired
	private LedgerBookBatchService ledgerBookBatchService;

	@Autowired
	private LedgerBookService ledgerBookService;

	@Autowired
	private GeneralAccountTemplateHelperForTestWithCache generalAccountTemplateHelperForTest;
	
	@Autowired
	private RepaymentPlanExtraChargeHandler repaymentPlanExtraChargeHandler;
	
	@Autowired
	private LedgerTemplateEvaluator ledgerTemplateEvaluator;
	
	@Autowired
	private LedgerTemplateParser defaultLedgerTemplateParser;
	
	@Before
	public void setUp(){
		
	}


	public void testPressureForBookLoanAssets(int count) {
		
		System.out.println("开始对登记账本进行压力测试！！！！");
		
		long startTimeForOldVersion = System.currentTimeMillis();
		
		for (int i = 0; i < count; i++) {
			
			test_book_loan_asset_v2();
		}
		long oldVersionConsumeTime = System.currentTimeMillis()-startTimeForOldVersion;
		
		long startTimeForNewVersion = System.currentTimeMillis();
		
		for (int i = 0; i < count; i++) {
			
			test_book_loan_asset_v2_new();
		}
		long newVersionConsumeTime = System.currentTimeMillis()-startTimeForNewVersion;
		
		System.out.println(String.format("结束对登记账本进行压力测试！！！！,老版本执行[%s]次，总花费[%s]ms，平均花费[%s]ms，新版本执行[%s]次，总花费[%s]ms,平均花费[%s]ms",count,oldVersionConsumeTime,oldVersionConsumeTime/1.0/count,count,newVersionConsumeTime,newVersionConsumeTime/1.0/count));

	}
	@Test
	/**
	 * @see EventType#BOOK_LOAN_ASSETS 
	 * 压力测试对比
	 */
	@Sql({
	"classpath:test/yunxin/ledgerbookv2/ledgerbookv2handler/test_book_loan_asset_v2.sql" })
	public void testPressureForBookLoanAssets() throws Exception {
		
//		generalAccountTemplateHelperForTest.createTemplateBy(LedgerBookNo, EventType.BOOK_LOAN_ASSETS);
		
		generalAccountTemplateHelperForTest.syncSourceRepository(EventType.CLEARING_VOUCHER_WRITE_OFF);
		
//		testPressureForBookLoanAssets(1);
//		testPressureForBookLoanAssets(10);
//		testPressureForBookLoanAssets(50);
//		testPressureForBookLoanAssets(100);
//		testPressureForBookLoanAssets(500);
//		testPressureForBookLoanAssets(1000);
	
	}
	
	public void test_book_loan_asset_v2() throws DuplicateAssetsException {
		
		AssetSet loan_asset = repaymentPlanService.load(AssetSet.class, 1L);
		LedgerBook book = new LedgerBook(LedgerBookNo, ledgerBookOrgnizationId);
		LedgerTradeParty party = new LedgerTradeParty();
		party.setFstParty(ledgerBookOrgnizationId);
		party.setSndParty(loan_asset.getContract().getCustomer()
				.getCustomerUuid()
				+ "");
		BigDecimal loanFee = new BigDecimal("10");
		BigDecimal techFee = new BigDecimal("20");
		BigDecimal otherFee = new BigDecimal("30");
		AssetSetExtraCharge loanExtraCharge = new AssetSetExtraCharge(
				loan_asset.getAssetUuid(), loanFee,
				ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE);
		AssetSetExtraCharge techExtraCharge = new AssetSetExtraCharge(
				loan_asset.getAssetUuid(), techFee,
				ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE);
		AssetSetExtraCharge otherExtraCharge = new AssetSetExtraCharge(
				loan_asset.getAssetUuid(), otherFee,
				ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE);
		repaymentPlanExtraChargeService.save(loanExtraCharge);
		repaymentPlanExtraChargeService.save(techExtraCharge);
		repaymentPlanExtraChargeService.save(otherExtraCharge);
		try {
		
			Map<String, LedgerItem> Ledgers = generate_ledgers_for_new_asset(book, loan_asset, party);
		} catch (Exception e) {
			
			e.printStackTrace();
			
			fail();
		}
		
	}
	
	public final static HashMap<String, String> book_loan_asset_fee_and_defferred_income_service_table = new HashMap<String, String>() {
		{
			put(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_PRINCIPLE, ChartOfAccount.SND_LONGTERM_LIABILITY_ABSORB_SAVING);
			put(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_INTEREST, ChartOfAccount.SND_DEFERRED_INCOME_INTEREST_ESTIMATE);
			put(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE,ChartOfAccount.TRD_DEFERRED_INCOME_LOAN_SERVICE_FEE);
			put(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE, ChartOfAccount.TRD_DEFERRED_INCOME_LOAN_TECH_FEE);
			put(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE, ChartOfAccount.TRD_DEFERRED_INCOME_LOAN_OTHER_FEE);
		}
	};
	private Map<String, LedgerItem> generate_ledgers_for_new_asset(LedgerBook book, AssetSet loan_asset,
			LedgerTradeParty party) {
		BigDecimal unearnAmount = ledgerItemService.get_balance_of_account(Arrays.asList(ChartOfAccount.FST_UNEARNED_LOAN_ASSET), book.getLedgerBookNo(), loan_asset.getAssetUuid());
		if(BigDecimal.ZERO.compareTo(unearnAmount)!=0)
		{
			throw new DuplicateAssetsException();
		}
			
		AssetCategory assetCategory=AssetConvertor.convertAnMeiTuLoanAssetToAssetCategory(loan_asset);
		Map<String,BigDecimal> loanMap = repaymentPlanExtraChargeHandler.getLoanFeeBy(loan_asset.getAssetUuid());
		loanMap.put(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_PRINCIPLE, loan_asset.getAssetPrincipalValue());
		loanMap.put(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_INTEREST, loan_asset.getAssetInterestValue());

		AccountDispersor dispersor=new AccountDispersor();
		for (Map.Entry<String,BigDecimal> loanFeeEntry:loanMap.entrySet()){
			String account = loanFeeEntry.getKey();
			BigDecimal amount = loanFeeEntry.getValue();
			String counterAccount = book_loan_asset_fee_and_defferred_income_service_table.get(account);
			if(StringUtils.isEmpty(counterAccount)){
				throw new InvalidLedgerException();
			}
			if(amount==null || amount.compareTo(BigDecimal.ZERO)==0){
				continue;
			}
			dispersor.dispers(account,amount , AccountSide.DEBIT);
			dispersor.dispers(counterAccount, amount, AccountSide.CREDIT);
		}
		
		Map<String,LedgerItem> Ledgers=ledgerItemService.book_M_Debit_M_Credit_ledgers(book,assetCategory,party,dispersor.dispersTable());
		
		return Ledgers;
	}
	public void test_book_loan_asset_v2_new() throws DuplicateAssetsException {
		
		AssetSet loan_asset = repaymentPlanService.load(AssetSet.class, 1L);
		LedgerBook book = new LedgerBook(LedgerBookNo, ledgerBookOrgnizationId);
		LedgerTradeParty party = new LedgerTradeParty();
		party.setFstParty(ledgerBookOrgnizationId);
		party.setSndParty(loan_asset.getContract().getCustomer()
				.getCustomerUuid()
				+ "");
		BigDecimal loanFee = new BigDecimal("10");
		BigDecimal techFee = new BigDecimal("20");
		BigDecimal otherFee = new BigDecimal("30");
		AssetSetExtraCharge loanExtraCharge = new AssetSetExtraCharge(
				loan_asset.getAssetUuid(), loanFee,
				ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE);
		AssetSetExtraCharge techExtraCharge = new AssetSetExtraCharge(
				loan_asset.getAssetUuid(), techFee,
				ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE);
		AssetSetExtraCharge otherExtraCharge = new AssetSetExtraCharge(
				loan_asset.getAssetUuid(), otherFee,
				ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE);
		repaymentPlanExtraChargeService.save(loanExtraCharge);
		repaymentPlanExtraChargeService.save(techExtraCharge);
		repaymentPlanExtraChargeService.save(otherExtraCharge);
		
		String ledgerBookNo = LedgerBookNo;

		Map<String, Object> dataFrame = new HashMap<String, Object>();

		AssetCategory assetCategory = AssetConvertor
				.convertAnMeiTuLoanAssetToAssetCategory(loan_asset);

		Map<String, BigDecimal> loanMap = repaymentPlanExtraChargeHandler
				.getLoanFeeBy(loan_asset.getAssetUuid());

		dataFrame.put(LedgerConstant.Event_Type_BooK_Loan_Asset.ASSET_CATEGORY,
				assetCategory);
		dataFrame.put(
				LedgerConstant.Event_Type_BooK_Loan_Asset.LEDGER_TRADE_PARTY,
				party);
		dataFrame.put(LedgerConstant.Event_Type_BooK_Loan_Asset.ASSET_SET,
				loan_asset);

		dataFrame.putAll(loanMap);
		
		AccountTemplate accountTemplate = accountTemplateService
				.getByLegerBookNoAndEventType(ledgerBookNo, EventType.BOOK_LOAN_ASSETS);

		if (null == accountTemplate) {

			throw new RuntimeException(
					"#generateAndSaveLedgerItems# no ledger template provider !");
		}

		BusinessScenarioDefinition businessScenarioDefinition = accountTemplate
				.getScenario();

		String scripts = businessScenarioDefinition.getTemplate();
		
		String signature = businessScenarioDefinition.getTemplateSignature();
		;

		List<Map<String, Object>> ledgerItemParamMapList = new ArrayList<Map<String, Object>>();

		dataFrame.put(LedgerConstant.Event_Type.BATCH_SERIAL_UUID, UUID
				.randomUUID().toString());
		dataFrame.put(LedgerConstant.Event_Type.LEDGER_BOOK_NO, ledgerBookNo);
		
		long startTime = System.currentTimeMillis();

		
		List<Map<String, Object>> result = ledgerTemplateEvaluator
				.evaluateResult(dataFrame, scripts,signature);
		
		System.out.println("模版解析时间:"+(System.currentTimeMillis()-startTime));
		
	}

}
