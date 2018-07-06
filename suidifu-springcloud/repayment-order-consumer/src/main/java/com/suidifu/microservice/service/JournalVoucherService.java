package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.owlman.microservice.enumation.JournalVoucherCompleteness;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.model.JournalAccount;
import com.zufangbao.sun.entity.account.AccountSide;
import java.math.BigDecimal;
import java.util.List;

public interface JournalVoucherService extends GenericService<JournalVoucher> {
    JournalVoucher getNoLapseJournalVoucherBy(String sourceDocumentDetailUuid);

    void lapseJournalVoucherBy(String sourceDocumentUuid);

    List<JournalVoucher> getJournalVoucherBy(String orderUuid, String itemUuid);

//    	JournalVoucher getJournalVoucherByVoucherUUID(String journalVoucherUUID);
//
//	void save(JournalVoucher journalVoucher);
//	int issueVourcher(String journalVoucherUUID);
//	void issueVourcher(JournalVoucher journalVoucher);
//	boolean exists(String notificationRecordUuid);
//
//	List<JournalVoucher> getJournalVoucherByCashFlowSerialNoAndJournalAccount(String cashFlowSerialNo, JournalAccount journalAccount);
//
//	List<JournalVoucher> getJournalVoucherBySourceDocumentCashFlowSerialNoAndJournalAccount(String source_document_cash_flow_serial_no, JournalAccount
//            journalAccount);
//	int countJournalVoucherBySourceDocumentCashFlowSerialNoAndJournalAccount(String source_document_cash_flow_serial_no, JournalAccount journalAccount);
//
//	JournalVoucher getJournalVoucherByNotificationUUIDAndJournalAccount(String notificationRecordUuid, JournalAccount journalAccount);
//	BigDecimal getBookingAmountSumOfIssueJournalVoucherBy(String billingPlanUuid, Long companyId, JournalAccount journalAccount);
//	BigDecimal getBookingAmountSumOfIssueJournalVoucherBy(String billingPlanUuid, String cashFlowUuid, JournalAccount journalAccount);
//	BigDecimal getIssuedAmountByIssueJV(String cashFlowUuid, JournalAccount journalAccount);
	BigDecimal getIssuedAmountByIssueJVAndJVType(String cashFlowUuid, JournalAccount journalAccount, JournalVoucherType journalVoucherType);
//	List<JournalVoucher> getIssuedJVListBy(String cashFlowUuid, JournalAccount journalAccount);
//	List<String> getBillUuidsInDebitIssuedJVListBy(String cashFlowUuid, Collection<String> billingPlanUuids);
//	void lapseJournalVoucher(JournalVoucher journalVoucher);
//	void lapseJournalVoucherList(List<JournalVoucher> journalVoucherList);
//	Set<String> getJournalVoucherUuidsFrom(List<JournalVoucher> journalVoucherList);
//	Set<String> getBillingPlanUuidsFrom(List<JournalVoucher> journalVoucherList);
//	JournalVoucher getIssuedJournalVoucherBy(String journalVoucherUuid, JournalAccount journalAccount);
//	Date getLastPaidTimeByIssuedJournalVoucherAndJournalAccount(String billingPlanUuid, Long companyId, JournalAccount journalAccount);
//
//	boolean existCashFlow(String cashFlowUid);
//
//	void saveCashFlow(AppArriveRecord appArriveRecord);
//
//	JournalVoucher getJournalVoucherByCashFlowUid(String cashFlowUid);
//
//	boolean isExistNotificationByCashFlow(AppArriveRecord cashFlow);
//
//	List<JournalVoucher> getNotificationByCashFlow(AppArriveRecord cashFlow);
//
//	boolean isJournalVoucherComplete(JournalVoucher notification, AppArriveRecord cashFlow);
//
//	List<JournalVoucher> getVouchersByBillingPlanUidAndIssued(String billingPlanUuid, Long companyId);
//
//	int fillBusinessVoucherUuid(String businessUuid, String businessVoucherTypeUuid, List<JournalVoucher> journalVouchers);
//
//	JournalVoucher refreshCompletenes(JournalVoucher journalVoucher);
//
	List<JournalVoucher> getJournalVoucherBy(String cashFlowUuid, AccountSide accountSide, JournalVoucherCompleteness completeness, Long companyId,
                                             JournalVoucherStatus status);
//
//	//public JournalVoucher getInForceJournalVoucherBy(String billingPlanUuid, String cashFlowUuid, String businessVoucherType);
//
//	List<JournalVoucher> getInForceJournalVoucherListBy(JournalVoucherQueryModel jvqModel, Date startIssuedDate, Date endIssuedDate);
//
//	List<JournalVoucher> getInForceJournalVoucherListBy(String billingPlanUuid, String businessVoucherType);
//
//	JournalVoucher getInForceJournalVoucherBy(Long companyId, String billingPlanUuid, String sourceDocumentIdentity, String businessVoucherType);
//	List<JournalVoucher> getJournalVoucherBy(String sourceDocumentUuid, AccountSide accountSide, JournalVoucherStatus journalVoucherStatus, Long companyId);
//	boolean existsJVWithSourceDocumentUuid(String sourceDocumentUuid, Long companyId, AccountSide accountSide, String billingPlanUuid);
//	JournalVoucher createIssuedJournalVoucherBySourceDocument(String billingPlanUuid, SourceDocument sourceDocument, String businessVoucherTypeUuid, BigDecimal
//            bookingAmount, String businessVoucherUuid, JournalVoucherType journalVoucherType, FinancialContract financialContract, Contract contract, AssetSet
//            assetSet, String orderNo);
//	JournalVoucher createIssuedJournalVoucherByCashFlow(String billingPlanUuid, AppArriveRecord appArriveRecord, String businessVoucherTypeUuid, BigDecimal
//            bookingAmount, String businessVoucherUuid, JournalVoucherCheckingLevel journalVoucherCheckingLevel);
//	Set<String> extractSourceDocumentUuid(List<JournalVoucher> journalVoucherList);
//	List<JournalVoucher> getJournalVoucherListBy(JournalVoucherSearchModel searchModel, int begin, int max);
//
//	List<JournalVoucher> getJournalVoucherBySourceDocumentUuid(String sourceDocumentUuid);
//
//	List<JournalVoucher> lapse_issued_jvs_of_cash_flow(String cashFlowUuid, AccountSide accountSide);
//
//	JournalVoucher getInforceDepositJournalVoucher(Long companyId, String sourceDocumentUuid);
//	List<JournalVoucher> getInforceThirdPartyDeductJournalVoucher(Long companyId, String billingPlanUuid);
//	List<JournalVoucher> getInforceVirtualAccountTransferRepaymentPlanJV(Long companyId, String billingPlanUuid);
//	List<JournalVoucher> getJournalVoucherList(PaymentOrderModel paymentOrderModel, Page page);
//	int count(PaymentOrderModel paymentOrderModel);
//
//	List<JournalVoucher> getJournalVoucherRefundList(PaymentOrderModel paymentOrderModel, Page page);
//	int countJournalVoucherRefund(PaymentOrderModel paymentOrderModel);
//
//	JournalVoucher getJournalVoucherRufund(String counterPartyAccount);
//
//
//	String gengerateQueryJournalVoucherSentence(ThirdPartVoucherQueryModel queryModel, Map<String, Object> params);
//
//	JournalVoucher getInforceJournalVoucher(String SourceDocumentDetailUuid);
//
//	List<JournalVoucher> getJournalVoucherListByOrderNo(String orderNo);
//
//	/**
//	 * 根据还款计划Uuid查找余额支付单
//	 * @param assetSetUuid
//	 */
//    List<JournalVoucher> getBalancePaymentBillBy(String assetSetUuid);
//
//	/**
//	 * 根据还款计划Uuid查找第三方扣款凭证
//	 */
//    List<JournalVoucher> getThirdPartyDeductVoucher(String assetSetUuid);
//
//	void updateJournalVoucherHasDataSyncLog(String jvUuid);
//
//	List<String> get_not_sync_journal_voucher(String financialContractUuid);
//
//	void transfer_and_fill_compensate_active_payment_info_after_auto_remittance(Long companyId, String assetSetUuid, SourceDocumentDetail sourceDocumentDetail,
//                                                                                String SourceDocumentNo, JournalVoucherType journalVoucherType,
//                                                                                JournalVoucherResovler resolver, SourceDocument sourceDocument);
//
//	String get_journal_voucher_uuid_by_source_document_detail_uuid(String sourceDocumentDetailUuid);
//
//	List<JournalVoucher> getRepaymentHistoryJournalVoucher(String assetSetUuid);
//
//	JournalVoucher getJournalVoucherBySourceDocumentUuidAndCashFlowUuid(String sourceDocumentUuid, String cashFlowUuid, String billingPlanUuid);
//
//	List<JournalVoucher> getPaymentRecordsJournalVoucher(String assetSetUuid);
//
//	List<JournalVoucher> getSuccessfulPaymentRecordsJournalVoucher(String assetSetUuid);
//
//	List<JournalVoucher> getJournalVoucherListByTypeAndAssetSetUuid(String assetSetUuid);
//
//	void transfer_and_fill_compensate_active_payment_info_after_auto_remittance_repayment_order(Long companyId,
//                                                                                                String assetSetUuid, JournalVoucherType journalVoucherType, JournalVoucherResovler resolver, Date actualRecycleTime, String cashFlowIdentity);
//
//	List<JournalVoucher> getJournalVoucherListByType(String billingPlanUuid);
//
//	JournalVoucher getJournalVoucherBySourceDocumentUuidAndType(String sourceDocumentUuid, String sourceDocumentDetailUuid, String billingPlanUuid);
//
//	List<JournalVoucher> getJournalVoucherListByTypeAndBillingPlanUuid(String billingPlanUuid, JournalVoucherType journalVoucherType);
//
//	int getMinIdAfterTime(Date time);
//
//	List<String> getJournalVoucherIdByTimeAndId(int id, Date startTime, Date endTime);
//
//	List<String> getJVUuidBySourceDocumentUuid(List<String> SDUuids);
//
//	List<String> getJVUuidByFinancialContractUuidAndTime(String fcUuid, Date startTime, Date endTime);
//
//	List<JournalVoucher> getJournalVoucherByItemUuid(String orderUuid, String ItemUuid);
//
//	void  lapseJournalVoucherBySourceDocumentUuid(String sourceDocumentUuid);
//
//	List<JournalVoucher> getJournalVouchersByContractUuidAndType(String contractUuid, List<JournalVoucherType> journalVoucherTypeList);
//
//	List<JournalVoucher> getJournalVouchersByRepaymentRecordQueryModel(RepaymentRecordQueryModel repaymentRecordQueryModel, Page page);
//
//	int getCountRepaymentRecords(RepaymentRecordQueryModel repaymentRecordQueryModel);
//
//	List<JournalVoucher> getJournalVouchersByCashFlowUuidAndType(String cashFlowUuid, JournalVoucherStatus status, JournalVoucherType journalVoucherType);
//
//	List<JournalVoucher> getJournalVouchersBySourceDocumentUuidAndType(String sourceDocumentUuid,JournalVoucherStatus status,JournalVoucherType journalVoucherType);
//
//	List<String> getCashFlowUuidList(String sourceDocumentUuid,JournalVoucherStatus status,JournalVoucherType journalVoucherType);
//
//	List<String> getTotalReceivableBillsUuidList(String sourceDocumentUuid,JournalVoucherStatus status,JournalVoucherType journalVoucherType);
//
//	List<JournalVoucher> getJournalVoucherListByBacthUuid(String bacthUuid);

}