package com.roav.yunxin.test;

import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.account.DepositeAccountType;
import com.zufangbao.sun.ledgerbook.*;
import com.zufangbao.sun.ledgerbookv2.enums.EventType;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.ledgerbookv2.service.LedgerItemServiceV2;
import com.zufangbao.sun.ledgerbookv2.utils.GeneralAccountTemplateHelperForTest;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.utils.tests.LedgerBookTestUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
/**
 * Created by MieLongJun on 18-3-8.
 */
public class TestForLedgerBookV2HandlerImpl extends TestBase{
    @Autowired
    private LedgerBookV2Handler ledgerBookV2Handler;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    public LedgerBookTestUtils ledgerBookTestUtils;

    @Autowired
    private LedgerBookService ledgerBookService;

    @Autowired
    private LedgerItemServiceV2 ledgerItemServiceV2;

    @Autowired
    private GeneralAccountTemplateHelperForTest generalAccountTemplateHelperForTest;

    @Test
    @Sql({ "classpath:test/yunxin/test_recover_asset_mullit_accounts_twice.sql" })
    public void testPartialCarryOver() {

        generalAccountTemplateHelperForTest.createTemplateBy("yunxin_ledger_book",
                EventType.RECOVER_RECEIVABLE_LOAN_ASSET);

        String ledgerBookNo = "yunxin_ledger_book";
        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
        AssetSet loan_asset = repaymentPlanService.load(AssetSet.class, 1L);
        AssetCategory assetCategory = AssetConvertor.convertAnMeiTuLoanAssetToAssetCategory(loan_asset);
        LedgerTradeParty ledgerTradeParty = new LedgerTradeParty("1", "2");
        String jvUuid = "jvUuid";
        String bvUuid = "bvUuid";
        String sdUuid = "sdUuid";

        DepositeAccountInfo bankinfo = new DepositeAccountInfo(ChartOfAccount.SND_BANK_SAVING_NFQ_ZSYH,
                ledgerTradeParty, DepositeAccountType.UINON_PAY);
        // recover twice
        try {
            ledgerBookV2Handler.recover_receivable_loan_asset(new LedgerBookCarryOverContextWithAsset(book, loan_asset,
                    jvUuid, bvUuid, sdUuid, new BigDecimal("900"), bankinfo, true, null, new Date(), ""));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        // recover rest
        try {
            ledgerBookV2Handler.recover_receivable_loan_asset(
                    new LedgerBookCarryOverContextWithAsset(book, loan_asset, jvUuid, bvUuid, sdUuid,
                            new BigDecimal("203"), bankinfo, true, new HashMap<String, BigDecimal>(), new Date(), ""));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        List<LedgerItem> ledgersForTest = new ArrayList<LedgerItem>();

        List<LedgerItem> receivable_asset_ledgers = ledgerItemServiceV2.get_booked_ledgers_of_asset_in_taccounts(
                Arrays.asList(ChartOfAccount.FST_RECIEVABLE_ASSET), book, loan_asset.getAssetUuid());
        List<LedgerItem> bank_saving_asset_ledgers = ledgerItemServiceV2.get_booked_ledgers_of_asset_in_taccounts(
                Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, loan_asset.getAssetUuid());

        ledgersForTest.addAll(receivable_asset_ledgers);
        ledgersForTest.addAll(bank_saving_asset_ledgers);

        HashMap<String, BigDecimal> creditAccountNames = new HashMap<String, BigDecimal>();
        LedgerBookTestUtils.buildMapping(creditAccountNames,
                ChartOfAccount.TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE + AccountSide.CREDIT.getAlias(),
                loan_asset.getAssetInitialValue());
        LedgerBookTestUtils.buildMapping(creditAccountNames,
                ChartOfAccount.SND_REVENUE_INTEREST + AccountSide.CREDIT.getAlias(),
                loan_asset.getAssetInterestValue());
        LedgerBookTestUtils.buildMapping(creditAccountNames,
                ChartOfAccount.SND_DEFERRED_INCOME_INTEREST_ESTIMATE + AccountSide.CREDIT.getAlias(),
                loan_asset.getAssetInterestValue());

        HashMap<String, BigDecimal> debitAccountNames = new HashMap<String, BigDecimal>();
        LedgerBookTestUtils.buildMapping(debitAccountNames,
                ChartOfAccount.TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE + AccountSide.DEBIT.getAlias(),
                loan_asset.getAssetInitialValue());
        LedgerBookTestUtils.buildMapping(debitAccountNames,
                ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION + AccountSide.DEBIT.getAlias(),
                loan_asset.getAssetInitialValue());
        LedgerBookTestUtils.buildMapping(debitAccountNames,
                ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE + AccountSide.DEBIT.getAlias(),
                loan_asset.getAssetInterestValue());
        LedgerBookTestUtils.buildMapping(debitAccountNames,
                ChartOfAccount.SND_RECIEVABLE_LOAN_PENALTY + AccountSide.DEBIT.getAlias(),
                loan_asset.getAssetInterestValue());
        LedgerBookTestUtils.buildMapping(debitAccountNames,
                ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE + AccountSide.DEBIT.getAlias(),
                loan_asset.getAssetInterestValue());

        boolean isReceiVableZero = ledgerItemServiceV2.is_zero_balanced_account(ChartOfAccount.FST_RECIEVABLE_ASSET,
                book, assetCategory);
        assertTrue(isReceiVableZero);
        BigDecimal bankAmount = ledgerItemServiceV2
                .get_balance_of_account(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetCategory);
        assertEquals(0, new BigDecimal("1103").compareTo(bankAmount));

    }

    @Test
    @Sql("classpath:test/yunxin/test_recover_asset_mullit_accounts_twice.sql")
    public void testRecoverReceivableLoanAsset() {

        generalAccountTemplateHelperForTest.createTemplateBy("yunxin_ledger_book",
                EventType.RECOVER_RECEIVABLE_LOAN_ASSET);

        LedgerBook book = ledgerBookService.getBookByBookNo("yunxin_ledger_book");
        AssetSet loan_asset = repaymentPlanService.load(AssetSet.class, 1L);
        AssetCategory assetCategory = AssetConvertor.convertAnMeiTuLoanAssetToAssetCategory(loan_asset);
        LedgerTradeParty ledgerTradeParty = new LedgerTradeParty("1", "2");
        String jvUuid = "jvUuid";
        String bvUuid = "bvUuid";
        String sdUuid = "sdUuid";

        DepositeAccountInfo bankinfo = new DepositeAccountInfo(ChartOfAccount.SND_BANK_SAVING_NFQ_ZSYH,
                ledgerTradeParty, DepositeAccountType.UINON_PAY);
        // recover twice
        try {
            ledgerBookV2Handler.recover_receivable_loan_asset(new LedgerBookCarryOverContextWithAsset(book, loan_asset,
                    jvUuid, bvUuid, sdUuid, new BigDecimal("900"), bankinfo, true, null, new Date(), ""));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        // recover rest
        try {
            ledgerBookV2Handler.recover_receivable_loan_asset(
                    new LedgerBookCarryOverContextWithAsset(book, loan_asset, jvUuid, bvUuid, sdUuid,
                            new BigDecimal("203"), bankinfo, true, new HashMap<String, BigDecimal>(), new Date(), ""));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        List<LedgerItem> ledgersForTest = new ArrayList<LedgerItem>();

        List<LedgerItem> receivable_asset_ledgers = ledgerItemServiceV2.get_booked_ledgers_of_asset_in_taccounts(
                Arrays.asList(ChartOfAccount.FST_RECIEVABLE_ASSET), book, loan_asset.getAssetUuid());
        List<LedgerItem> bank_saving_asset_ledgers = ledgerItemServiceV2.get_booked_ledgers_of_asset_in_taccounts(
                Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, loan_asset.getAssetUuid());

        ledgersForTest.addAll(receivable_asset_ledgers);
        ledgersForTest.addAll(bank_saving_asset_ledgers);

        HashMap<String, BigDecimal> creditAccountNames = new HashMap<String, BigDecimal>();
        LedgerBookTestUtils.buildMapping(creditAccountNames,
                ChartOfAccount.TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE + AccountSide.CREDIT.getAlias(),
                loan_asset.getAssetInitialValue());
        LedgerBookTestUtils.buildMapping(creditAccountNames,
                ChartOfAccount.SND_REVENUE_INTEREST + AccountSide.CREDIT.getAlias(),
                loan_asset.getAssetInterestValue());
        LedgerBookTestUtils.buildMapping(creditAccountNames,
                ChartOfAccount.SND_DEFERRED_INCOME_INTEREST_ESTIMATE + AccountSide.CREDIT.getAlias(),
                loan_asset.getAssetInterestValue());

        HashMap<String, BigDecimal> debitAccountNames = new HashMap<String, BigDecimal>();
        LedgerBookTestUtils.buildMapping(debitAccountNames,
                ChartOfAccount.TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE + AccountSide.DEBIT.getAlias(),
                loan_asset.getAssetInitialValue());
        LedgerBookTestUtils.buildMapping(debitAccountNames,
                ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION + AccountSide.DEBIT.getAlias(),
                loan_asset.getAssetInitialValue());
        LedgerBookTestUtils.buildMapping(debitAccountNames,
                ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE + AccountSide.DEBIT.getAlias(),
                loan_asset.getAssetInterestValue());
        LedgerBookTestUtils.buildMapping(debitAccountNames,
                ChartOfAccount.SND_RECIEVABLE_LOAN_PENALTY + AccountSide.DEBIT.getAlias(),
                loan_asset.getAssetInterestValue());
        LedgerBookTestUtils.buildMapping(debitAccountNames,
                ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE + AccountSide.DEBIT.getAlias(),
                loan_asset.getAssetInterestValue());

        boolean isReceiVableZero = ledgerItemServiceV2.is_zero_balanced_account(ChartOfAccount.FST_RECIEVABLE_ASSET,
                book, assetCategory);
        assertTrue(isReceiVableZero);
        BigDecimal bankAmount = ledgerItemServiceV2
                .get_balance_of_account(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetCategory);
        assertEquals(0, new BigDecimal("1103").compareTo(bankAmount));

    }
}
