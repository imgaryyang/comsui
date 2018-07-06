package com.suidifu.microservice.handler.impl;

import static com.zufangbao.sun.ledgerbook.ChartOfAccount.FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS;
import static com.zufangbao.sun.ledgerbook.ChartOfAccount.SND_LIABILITIES_OF_INDEPENDENT_ACCOUNTS_PENDING;

import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.handler.TemporaryDepositDocHandler;
import com.suidifu.microservice.service.SourceDocumentService;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.refund.TemporaryDepositDoc;
import com.zufangbao.sun.entity.refund.TmpDepositStatus;
import com.zufangbao.sun.entity.refund.TmpDptDocAliveStatus;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.AssetConvertor;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.yunxin.service.refund.TemporaryDepositDocService;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component("temporaryDepositDocHandler")
public class TemporaryDepositDocHandlerImpl implements TemporaryDepositDocHandler {
    @Resource
    private LedgerBookV2Handler ledgerBookV2Handler;
    @Resource
    private LedgerBookHandler ledgerBookHandler;

    @Resource
    private SourceDocumentService sourceDocumentService;
    @Resource
    private TemporaryDepositDocService temporaryDepositDocService;
    @Resource
    private CashFlowService cashFlowService;

    @Override
    public void businessPayCreateTemporaryDepositDoc(String sourceDocumentUuid,
                                                     BigDecimal writeOffAmount,
                                                     VirtualAccount virtualAccount,
                                                     LedgerBook ledgerBook) {
        //判断滞留单是否已经存在
        TemporaryDepositDoc temporaryDepositDocBySourceUuid = temporaryDepositDocService.getTemporaryDepositDocBySourceDocumentUuid(sourceDocumentUuid);
        if (temporaryDepositDocBySourceUuid != null) {
            return;
        }

        SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
        BigDecimal temporaryDepositAmount = sourceDocument.getBookingAmount().subtract(writeOffAmount);

        TemporaryDepositDoc temporaryDepositDoc = new TemporaryDepositDoc(AccountSide.CREDIT,
                temporaryDepositAmount, temporaryDepositAmount, TmpDptDocAliveStatus.CREATE,
                TmpDepositStatus.UNCLEAR, virtualAccount.getOwnerUuid(),
                virtualAccount.getOwnerName(), CustomerType.COMPANY,
                virtualAccount.getUuid(), virtualAccount.getVirtualAccountNo(),
                "", sourceDocument.getFinancialContractUuid(),
                sourceDocument.getVoucherUuid(), sourceDocument.getOutlierDocumentUuid(),
                new Date(), new Date(), "", "",
                sourceDocument.getUuid());
        //保存滞留单
        temporaryDepositDocService.save(temporaryDepositDoc);
        //账本转出多余金额,转入滞留单
        LedgerTradeParty ledgerTradeParty = new LedgerTradeParty(virtualAccount.getOwnerUuid(), ""); //customerUuid
        LedgerTradeParty tempDepositTradeParty = new LedgerTradeParty(virtualAccount.getOwnerUuid(), "");
        String cashFlowUuid = temporaryDepositDoc.getCashFlowUuid();
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
        AssetCategory assetCategory = AssetConvertor.convertTemporayDepositDocAssetCategory(
                temporaryDepositDoc.getUuid(), cashFlow != null ? cashFlow.getCashFlowIdentity() : null);
        if (ledgerBookV2Handler.checkLegderBookVersion(ledgerBook)) {
            log.info("begin to " +
                    "#TemporaryDepositDocHandlerImpl" +
                    "#businessPayCreateTemporaryDepositDoc [AccountTemplate] " +
                    "sourceDocumentUuid:[{}]", sourceDocumentUuid);
            ledgerBookV2Handler.book_compensatory_remittance_virtual_accountV2(ledgerBook,
                    tempDepositTradeParty, ledgerTradeParty, "", "",
                    sourceDocument.getUuid(), temporaryDepositAmount, assetCategory,
                    SND_LIABILITIES_OF_INDEPENDENT_ACCOUNTS_PENDING,
                    FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS);
            log.info("# end !!!");
        }
        ledgerBookHandler.book_compensatory_remittance_virtual_accountV2(ledgerBook,
                tempDepositTradeParty, ledgerTradeParty, "", "",
                sourceDocument.getUuid(), temporaryDepositAmount, assetCategory,
                SND_LIABILITIES_OF_INDEPENDENT_ACCOUNTS_PENDING,
                FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS);
    }
}