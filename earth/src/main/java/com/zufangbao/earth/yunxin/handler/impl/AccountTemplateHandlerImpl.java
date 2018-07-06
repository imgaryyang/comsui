package com.zufangbao.earth.yunxin.handler.impl;

import com.suidifu.xcode.exception.XcodeException;
import com.suidifu.xcode.pojo.SourceRepository;
import com.suidifu.xcode.service.SourceRepositoryPersistenceService;
import com.zufangbao.earth.report.wrapper.ICreateLedgerBookWrapper;
import com.zufangbao.earth.yunxin.handler.AccountTemplateHandler;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.exception.LedgerItemCreateException;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbookv2.enums.EventType;
import com.zufangbao.sun.ledgerbookv2.service.AccountTemplateService;
import com.zufangbao.sun.ledgerbookv2.service.LedgerBookBatchService;
import com.zufangbao.sun.ledgerbookv2.service.LedgerItemServiceV2;
import com.zufangbao.sun.ledgerbookv2.service.LedgerTableNameService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.*;

/**
 * Created by zj on 17-10-25.
 */
@Component("accountTemplateHandler")
public class AccountTemplateHandlerImpl implements AccountTemplateHandler{


    private static Log logger = LogFactory.getLog(AccountTemplateHandlerImpl
            .class);

    @Autowired
    private FinancialContractService  financialContractService;

    @Autowired
    private LedgerBookService ledgerBookService;

    @Autowired
    private AccountTemplateService accountTemplateService;

    @Autowired
    private SourceRepositoryPersistenceService sourceRepositoryDBService;

    @Autowired
    private LedgerItemServiceV2 ledgerItemServiceV2;

    @Autowired
    private LedgerTableNameService ledgerTableNameService;

    @Autowired
    private LedgerBookBatchService ledgerBookBatchService;
    @Autowired
    private ICreateLedgerBookWrapper iCreateLedgerBookWrapper;

    @Override
    public void addTemplateByLedgerbookNo(String financialContractUUID) {
        if (StringUtils.isEmpty(financialContractUUID)){
            return;
        }
        //校验当前场景表中是否有存在的账本
        try{
            FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUUID);
            if (null == financialContract){
                logger.info("#not find financialContract by " +
                        "financialContractUUID:"+financialContractUUID);
                return;
            }
            LedgerBook book = ledgerBookService.getBookByBookNo(financialContract
                    .getLedgerBookNo());
            if (null == book){
                logger.info("#not find ledgerbook by " +
                        "ledgerbookno:"+financialContract.getLedgerBookNo());
                return;
            }
            /*默认开启创建表*/
//            if (StringUtils.isEmpty(book.getLedgerBookVersion())){
//                logger.info("#LedgerBookVersion is OFF!!!");
//                return;
//            }
            boolean flag= accountTemplateService.getByLedgerBookNO
                    (book.getLedgerBookNo());
            if (flag){
                createAcountTemplate(book.getLedgerBookNo(),financialContract);
            }
        }catch (Exception e){
            logger.info("#自动依赖账本生成脚本关联出错[financialContractUUID" +
                    ":"+financialContractUUID+"]"+ExceptionUtils.getFullStackTrace(e));
        }
    }

    private void createAcountTemplate(String ledgerBookNo,FinancialContract
            financialContract){
        List<Map<String,Object>> accountTemplates = null;

        try{
            accountTemplates  = generateAccountTemplateValue
                    (ledgerBookNo);
        }catch (Exception e){
            logger.info("#query DB sourceRepository Excepton by "+
                    ExceptionUtils.getFullStackTrace(e));
        }
        try{
            //create ledger_book_shelf
            ledgerItemServiceV2.createLedgerBookShelfByLedgerBookNo
                    (getCreateTableSQL("createShelfSql1",financialContract.getContractNo()));
            //create ledger_book_batch
            ledgerBookBatchService.createBatchTableByFinancialContractNo
                    (getCreateTableSQL("createBatchSql1",financialContract.getContractNo()));
            //create t_summary_journal
            ledgerBookBatchService.createSummaryJournalByFinancialContractNo
                    (getCreateTableSQL("createSummaryJournalSql1",financialContract.getContractNo()));
            //给新的信托合同创建关联脚本
            accountTemplateService.insertOrUpdate(accountTemplates);
            //对tablename_mapping进行添加操作
            ledgerTableNameService.insertOrUPdate(ledgerBookNo,financialContract
                    .getContractNo());
        }catch (Exception e){
            logger.info("create ledger_book_shelf or ledger_book_batch or t_summary_journal " +
                    "Exception ledgerBookNo:["+ledgerBookNo+"] And " +
                    "FinancialContractNo:["+financialContract.getContractNo()
                    +"]"+ExceptionUtils.getFullStackTrace(e));
            return;
        }
    }

    private String getCreateTableSQL(String cacheId,String financialContractNo) throws Exception {
        String createSql= iCreateLedgerBookWrapper.getCreateSqlByTableName(cacheId, financialContractNo);
        if (StringUtils.isBlank(createSql)){
            throw new LedgerItemCreateException("Create table sql is null !!!");
        }
        return createSql;
    }

    private List<Map<String,Object>> generateAccountTemplateValue(String ledgerBookNo) throws XcodeException {
        List<Map<String, Object>> sqlParamlist = new ArrayList<>();
        List<SourceRepository> allTemplateList = getAllTemplateByEventType();
        if (!CollectionUtils.isEmpty(allTemplateList)){
            for (SourceRepository sr : allTemplateList){
                Map<String, Object> hashMap = new HashMap<>();
                if (sr != null){
                    String event= sr.getBusinessType();
                    EventType eventType = EventType.valueOf(event);
                    hashMap.put("uuid",UUID.randomUUID().toString());
                    hashMap.put("eventType",eventType.ordinal());
                    hashMap.put("ledgerBookNo",ledgerBookNo);
                    hashMap.put("defaultDate",new Date());
                    hashMap.put("templateSignature",sr.getSignature());
                }
                sqlParamlist.add(hashMap);
            }
        }
        return sqlParamlist;
    }

    private List<SourceRepository> getAllTemplateByEventType() throws XcodeException {
        List<String> events = new ArrayList<>();
        for (Map.Entry<String,Object> eventTypes : eventTypeMap.entrySet()){
            events.add(eventTypes.getValue().toString());
        }

        return sourceRepositoryDBService.getByeventTypeList(events);
    }
    //value是EventType的枚举类型, key是String类行的key
    private final static Map<String,Object> eventTypeMap= new
            HashMap<String,Object>(){
        {
            put("RecoverReceivableLoanAssetVirtualAccount", EventType
                    .RECOVER_RECEIVABLE_LOAN_ASSET_VIRTUAL_ACCOUNT);
            put("RecoverReceivableLoanAsset",EventType
                    .RECOVER_RECEIVABLE_LOAN_ASSET);
            put("RecoverRreceivableGuranttee",EventType
                    .RECOVER_RECEIVABLE_GURANTTEE);
            put("RepurchaseOrderWriteOff",EventType
                    .REPURCHASE_ORDER_WRITE_OFF);
            put("ClearingVoucherWriteOff",EventType
                    .CLEARING_VOUCHER_WRITE_OFF);
            put("ReveivableInadvanceOrderWriteOff",EventType
                    .REVEIVABLE_INADVANCE_ORDER_WRITE_OFF);
            put("BookCompensatoryRemittanceVirtualAccountV2",EventType
                    .BOOK_COMPENSATORY_REMITTANCE_VIRTUAL_ACCOUNT_V2);
            put("RecoverEachFrozenCapitalAmount",EventType
                    .RECOVER_EACH_FROZEN_CAPITAL_AMOUNT);
            put("RemittanceLoans",EventType.REMITTANCE_LOANS);

            put("BookLoanAssetV2Pre",EventType.BOOK_LOAN_ASSET_V2_PRE);

            put("BookLoanAssetV2Before",EventType
                    .BOOK_LOAN_ASSET_V2_BEFORE);
            put("Repurchase_Order_V1",EventType.REPURCHASE_ORDER_V1);

            put("Repurchase_Order_V2",EventType.REPURCHASE_ORDER_V2);

            put("Repurchase_Order_V3",EventType.REPURCHASE_ORDER_V3);

            put("Repurchase_Order_V4",EventType.REPURCHASE_ORDER_V4);

            put("Refresh_Receivable_Overdue_Fee",EventType
                    .REFRESH_RECEIVABLE_OVERDUE_FEE);

            put("Classify_Receivable_Loan_Asset_To_Overdue",EventType
                    .CLASSIFY_RECEIVABLE_LOAN_ASSET_TO_OVERDUE);

            put
                    ("Book_Receivable_Load_Guarantee_And_Assets_Sold_For_Repurchase",EventType.BOOK_RECEIVABLE_LOAD_GUARANTEE_AND_ASSETS_SOLD_FOR_REPURCHASE);

            put("Roll_Back_Ledgers_And_Write_Off_Assetset",EventType
                    .ROLL_BACK_LEDGERS_AND_WRITE_OFF_ASSETSET);

            put("Deposit_Independent_Account_Assets",EventType
                    .DEPOSIT_INDEPENDENT_ACCOUNT_ASSETS);

            put("transfer_tmp_deposit_to_virtual_account",EventType.TRANSFER_TMP_DEPOSIT_TO_VIRTUAL_ACCOUNT);

            put("refresh_receivable_mutable_fee",EventType.REFRESH_RECEIVABLE_MUTABLE_FEE);

            put("clearing_assetsv1",EventType.CLEARING_ASSETSV1);

            put("clearing_assetsv2",EventType.CLEARING_ASSETSV2);

            put("accrue",EventType.ACCRUE);

            put("book_ledgers_with_washing",EventType.BOOK_LEDGERS_WITH_WASHING);

            put("book_single_asset_with_batch_uuid",EventType.BOOK_SINGLE_ASSET_WITH_BATCH_UUID);
            
            put("handle_refund_receivable_loan_asset",EventType.HANDLE_REFUND_RECEIVABLE_LOAN_ASSET);
            
            put("ledger_bank_saving_refund_asset_1",EventType.LEDGER_BANK_SAVING_REFUND_ASSET_1);
            
            put("ledger_bank_saving_refund_asset_2",EventType.LEDGER_BANK_SAVING_REFUND_ASSET_2);
        }
    };
}
