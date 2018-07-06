package com.roav.yunxin.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.demo2do.core.persistence.support.Filter;
import com.suidifu.microservice.model.JournalVoucher;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.account.VirtualAccountFlow;
import com.zufangbao.sun.entity.account.VirtualAccountTransactionType;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repayment.order.DetailPayStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetClearStatus;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.ContractFundingStatus;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import com.zufangbao.sun.yunxin.entity.OnAccountStatus;
import com.zufangbao.sun.yunxin.entity.OrderClearStatus;
import com.zufangbao.sun.yunxin.entity.OrderPaymentStatus;
import com.zufangbao.sun.yunxin.entity.RepaymentPlanType;
import com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

/**
 * Created by MieLongJun on 18-3-7.
 */
public class TestForActive extends TestBase {


    /*
     * 主动还款 核销
     */
    @Test
    @Sql(value = {
            "classpath:test/yunxin/recon_payment_order_active.sql" })
    public void test_check_and_transfer_asstes() {
        String repaymentOrderUuid = "order_uuid_1";
        String repayment_order_item_uuid = "order_detail_uuid_1";
        String financialContractNo = "financial_contract_code_1";
        String ledgerBookNo = "yunxin_ledger_book";
        RepaymentOrderReconciliationParameters reconparams = new RepaymentOrderReconciliationParameters(
                repaymentOrderUuid, repayment_order_item_uuid, financialContractNo, ledgerBookNo, null);
        try {

            repaymentOrderActiveVoucherReconciliation.repaymentOrderRecoverDetails(Arrays.asList(reconparams));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
        RepaymentOrderItem item = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid);

        String borrower_customerUuid = "customerUuid1";

        String asset_uuid_1 = "asset_uuid_1";
        BigDecimal unearned_amount_asset = ledgerBookStatHandler.get_unearned_amount(ledgerBookNo, asset_uuid_1);
        assertEquals(0, BigDecimal.ZERO.compareTo(unearned_amount_asset));
        // assertEquals(0,BigDecimal.ZERO.compareTo(receivble_amount_asset));
        AssetSet asset = repaymentPlanService.getUniqueRepaymentPlanByUuid(asset_uuid_1);
        assertEquals(AssetClearStatus.CLEAR, asset.getAssetStatus());
        assertEquals(RepaymentPlanType.NORMAL, asset.getRepaymentPlanType());
        assertEquals(ExecutingStatus.SUCCESSFUL, asset.getExecutingStatus());
        assertEquals(OnAccountStatus.WRITE_OFF, asset.getOnAccountStatus());
        assertEquals(OrderPaymentStatus.UNEXECUTINGORDER, asset.getOrderPaymentStatus());

        VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(borrower_customerUuid);

        AssetSet asset1 = repaymentPlanService.getUniqueRepaymentPlanByUuid(asset_uuid_1);
        assertEquals(ContractFundingStatus.ALL, asset1.getContractFundingStatus());

        List<JournalVoucher> jounalVoucherList = journalVoucherService.list(JournalVoucher.class, new Filter());
        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);
        assertEquals(1, jounalVoucherList.size());
        JournalVoucher jv = jounalVoucherList.get(0);
        // 资金入账时间
        assertEquals(false, repaymentOrder.getCashFlowTime() == null);
        // 设定还款时间
        assertEquals(true, DateUtils.isSameDay(jv.getTradeTime(), repaymentOrderItemService
                .getRepaymentOrderItemByUuid(repayment_order_item_uuid).getRepaymentPlanTime()));
        // assertEquals(jv.getCashFlowTime(),repaymentOrder.getCashFlowTime());
        assertEquals(item.getAmount(), jv.getBookingAmount());
        assertEquals(JournalVoucherStatus.VOUCHER_ISSUED, jv.getStatus());
        assertEquals(JournalVoucherType.TRANSFER_BILL_INITIATIVE, jv.getJournalVoucherType());
        assertEquals(item.getOrderUuid(), jv.getBusinessVoucherUuid());
        Order order = orderService.getOrder(jv.getRelatedBillContractNoLv4());
        assertEquals(OrderClearStatus.CLEAR, order.getClearingStatus());
        assertEquals(item.getAmount(), order.getTotalRent());

        assertEquals("pay_ac_no_1", jv.getLocalPartyAccount());
        assertEquals("测试用户1", jv.getLocalPartyName());
        assertEquals(virtualAccount.getVirtualAccountNo(), jv.getCounterPartyAccount());
        assertEquals(virtualAccount.getOwnerName(), jv.getCounterPartyName());

        // 校验流水
        List<VirtualAccountFlow> virtualAccountFlowList = virtualAccountFlowService.list(VirtualAccountFlow.class,
                new Filter());
        assertEquals(1, virtualAccountFlowList.size());
        VirtualAccountFlow virtualAccountFlow1 = virtualAccountFlowList.get(0);
        assertEquals(0, new BigDecimal("1500.00").compareTo(virtualAccountFlow1.getTransactionAmount()));
        assertEquals(jounalVoucherList.get(0).getJournalVoucherUuid(), virtualAccountFlow1.getBusinessDocumentUuid());
        assertEquals(virtualAccount.getVirtualAccountNo(), virtualAccountFlow1.getVirtualAccountNo());
        assertEquals(jounalVoucherList.get(0).getJournalVoucherNo(), virtualAccountFlow1.getBusinessDocumentNo());
        assertEquals(AccountSide.CREDIT, virtualAccountFlow1.getAccountSide());
        assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE, virtualAccountFlow1.getTransactionType());
        assertEquals(virtualAccount.getFstLevelContractUuid(), virtualAccountFlow1.getFinancialContractUuid());
        assertEquals(item.getAmount(), virtualAccountFlow1.getTransactionAmount());
        assertEquals(virtualAccount.getVirtualAccountUuid(), virtualAccountFlow1.getVirtualAccountUuid());
        assertEquals(virtualAccount.getVirtualAccountAlias(), virtualAccountFlow1.getVirtualAccountAlias());

        assertEquals(DetailPayStatus.PAID, item.getDetailPayStatus());

        List<LedgerItem> items = ledgerItemService.get_jv_asset_accountName_related_ledgers(ledgerBookNo, asset_uuid_1,
                jv.getJournalVoucherUuid(), Arrays.asList(ChartOfAccount.FST_BANK_SAVING));
        for (LedgerItem ledgerItem : items) {
            assertEquals(repaymentOrderUuid, ledgerItem.getBusinessVoucherUuid());
        }
    }
}
