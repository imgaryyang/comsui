package com.suidifu.morganstanley.handler.voucher.impl;

import com.suidifu.morganstanley.handler.voucher.ActivePaymentVoucherHandler;
import com.suidifu.morganstanley.model.request.voucher.ActivePaymentVoucher;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.gluon.exception.voucher.CashFlowNotExistException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.contract.ContractActiveSourceDocumentMapper;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.FilenameUtils;
import com.zufangbao.sun.yunxin.api.ActivePaymentVoucherLogService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.sun.yunxin.entity.api.ActivePaymentVoucherLog;
import com.zufangbao.sun.yunxin.entity.api.VoucherPayer;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.entity.model.voucher.AssetInfoQueryModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.ContractInfoQueryModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateAccountInfoModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateAssetInfoModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateBaseModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateContractInfoModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateSubmitModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.RecordLogCore;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraChargeService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.ActivePaymentVoucherDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailCheckState;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailImporter;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentImporter;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentResource;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.Voucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VoucherHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentResourceService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
import com.zufangbao.wellsfargo.yunxin.handler.ContractApiHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.FINANCIAL_PRODUCT_CODE_ERROR;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.CONTRACT_NOT_EXIST;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.CUSTOMER_NOT_UNIQUE;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.NO_SUCH_CASH_FLOW;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.NO_SUCH_RECEIVABLE_ACCOUNT_NO;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.NO_SUCH_VOUCHER;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.NO_SUCH_VOUCHER_TYPE;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.REPEAT_REQUEST_NO;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.UNSUPPORTED_FILE_TYPE;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.VOUCHER_AMOUNT_ERROR;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.VOUCHER_CAN_NOT_CANCEL;
import static com.zufangbao.gluon.spec.morganstanley.GlobalSpec4MorganStanley.ErrorMsg4Voucher.BANK_TRANSACTION_NO_EXIST_EXCEPTION_MSG;

/**
 * 主动付款凭证接口
 *
 * @author louguanyang
 */
@Component("activePaymentVoucherHandler")
@Log4j2
public class ActivePaymentVoucherHandlerImpl implements ActivePaymentVoucherHandler {
    @Autowired
    private ActivePaymentVoucherLogService activePaymentVoucherLogService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private FinancialContractService financialContractService;
    @Autowired
    private CashFlowHandler cashFlowHandler;
    @Autowired
    private SourceDocumentService sourceDocumentService;
    @Autowired
    private SourceDocumentDetailService sourceDocumentDetailService;
    @Autowired
    private VirtualAccountService virtualAccountService;
    @Autowired
    private SourceDocumentResourceService sourceDocumentResourceService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ContractAccountService contractAccountService;
    @Autowired
    private SystemOperateLogService systemOperateLogService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;
    @Autowired
    private CashFlowService cashFlowService;
    @Autowired
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Autowired
    private VoucherHandler voucherHandler;
    @Autowired
    private RecordLogCore recordLogCore;
    @Autowired
    private ContractApiHandler contractApiHandler;
    @Value("${voucherPath}")
    private String voucherPath = "";
    private String[] extensions = {FilenameUtils.SUFFIX_PNG, FilenameUtils.SUFFIX_JPG, FilenameUtils.SUFFIX_PDF, FilenameUtils.SUFFIX_PNG_UPCASE, FilenameUtils
            .SUFFIX_JPG_UPCASE, FilenameUtils.SUFFIX_PDF_UPCASE};

    @Override
    public void saveFileToService(MultipartHttpServletRequest fileRequest, String requestNo, String voucherNo) throws IOException {
        Iterator<String> fileNames = fileRequest.getFileNames();
        while (fileNames.hasNext()) {
            String fileName = fileNames.next();
            List<MultipartFile> files = fileRequest.getFiles(fileName);
            processFilesAndSavePath(files, voucherNo, requestNo);
        }
    }

    private void processFilesAndSavePath(List<MultipartFile> files, String voucherNo, String requestNo) throws IOException {
        for (MultipartFile multipartFile : files) {
            String tempFilePath = saveFileToServiceReturnPath(multipartFile);
            SourceDocumentResource resource = new SourceDocumentResource(voucherNo, requestNo, tempFilePath);
            sourceDocumentResourceService.save(resource);
            log.info("save file to service success, file path:" + tempFilePath);
        }
    }

    private String saveFileToServiceReturnPath(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        if (!FilenameUtils.isExtension(originalFilename, extensions)) {
            throw new ApiException(UNSUPPORTED_FILE_TYPE);
        }
        String filePath = voucherPath + File.separator + DateUtils.today() + File.separator;
        String tempFilename = UUID.randomUUID().toString() + FilenameUtils.EXTENSION_SEPARATOR + FilenameUtils.getExtension(originalFilename);
        File temp = new File(filePath, tempFilename);
        FileUtils.writeByteArrayToFile(temp, multipartFile.getBytes());
        return temp.getPath();
    }

    private void createSourceDocumentDetails(Contract contract, String sourceDocumentUuid,
                                             ActivePaymentVoucher model, String voucherUuid) {
        VoucherType voucherType = VoucherType.createActivePaymentVoucherType(model.getVoucherType());
        if (voucherType == null) {
            throw new ApiException(NO_SUCH_VOUCHER_TYPE);
        }
        String contractUniqueId = contract.getUniqueId();
        VoucherPayer payer = VoucherPayer.LOANER;

        List<ActivePaymentVoucherDetail> voucherDetailList = model.getDetailModel();
        String requestNo = model.getRequestNo();
        String receivableAccountNo = model.getReceivableAccountNo();
        String paymentAccountNo = model.getPaymentAccountNo();
        String paymentName = model.getPaymentName();
        String paymentBank = model.getPaymentBank();
        String bankTransactionNo = model.getBankTransactionNo();
        for (ActivePaymentVoucherDetail detail : voucherDetailList) {
            String repaymentPlanNo = detail.getRepaymentPlanNo();
            BigDecimal amount = detail.getAmount();
            SourceDocumentDetail sourceDocumentDetail = SourceDocumentDetailImporter.createActivePaymentVoucherDetail(sourceDocumentUuid, contractUniqueId,
                    repaymentPlanNo, amount, requestNo, voucherType.getKey(), payer, receivableAccountNo, paymentAccountNo, paymentName, paymentBank, contract
                            .getFinancialContractUuid());
            sourceDocumentDetail.setSecondNo(bankTransactionNo);//一级编号requestNo，二级编号打款流水号bankTransactionNo
            sourceDocumentDetail.setCheckState(SourceDocumentDetailCheckState.CHECK_SUCCESS);
            sourceDocumentDetail.setDetailAmount(detail.getDetailAmountMap());
            sourceDocumentDetail.setVoucherUuid(voucherUuid);
            sourceDocumentDetailService.saveOrUpdate(sourceDocumentDetail);
            log.info("生成主动付款凭证明细, sourceDocumentDetailUUID:" + sourceDocumentDetail.getUuid());
        }
    }

    /**
     * 校验单条凭证的明细字段
     *
     * @param ledgerBookNo      账本编号
     * @param detail            主动付款凭证明细
     * @param repaymentPlanNo   还款计划编号
     * @param assetUuid         还款计划UUID
     */
    private void checkDetail(String ledgerBookNo, ActivePaymentVoucherDetail detail, String repaymentPlanNo, String assetUuid, String
            repayScheduleNo, Integer currentPeriod) {
        Map<String, BigDecimal> repaymentPlanSnapshot = ledgerBookStatHandler.unrecovered_asset_snapshot(ledgerBookNo, assetUuid, true);
        Map<String, BigDecimal> detailAmountMap = detail.getDetailAmountMap();
        for (String key : detailAmountMap.keySet()) {
            BigDecimal requestAmount = detailAmountMap.getOrDefault(key, BigDecimal.ZERO);
            if (AmountUtils.equals(BigDecimal.ZERO, requestAmount)) {
                continue;
            }
            BigDecimal amountInDB = repaymentPlanSnapshot.getOrDefault(key, BigDecimal.ZERO);
            if (requestAmount.compareTo(amountInDB) > 0) {
                String errorAmountName = detail.getDetailAmountName(key);
                log.error(String.format("检查主动付款凭证明细金额失败,商户还款计划编号,%s,还款计划编号 %s,期数,%d, %s, %s", repayScheduleNo, repaymentPlanNo, currentPeriod, key, "校验错误"));
                throw new ApiException(ApiResponseCode.VOUCHER_DETAIL_AMOUNT_HAS_ERROR, "主动付款凭证明细字段金额有误, " + errorAmountName + ":" + requestAmount + "应为:" +
                        amountInDB + ".该明细[商户还款计划],[还款计划编号],[期数]分别为：[" + repayScheduleNo + "],[" + repaymentPlanNo + "],[" + currentPeriod + "]");
            }
        }
    }

    private void isDetailAmountTooLarge(BigDecimal voucherDetailAmount, BigDecimal unrecoveredAmount) {
        if (voucherDetailAmount.compareTo(unrecoveredAmount) > 0) {

            log.error("检查主动付款凭证明细金额失败, 金额大于未还金额");
            throw new ApiException(ApiResponseCode.VOUCHER_DETAIL_AMOUNT_TOO_LARGE);
        }
    }

    /**
     * 明细未传且金额与应还未还金额一致时，无须校验明细
     *
     * @param amount
     * @param voucherDetailAmount 凭证明细金额
     * @param unRecoveredAmount   还款计划应还未还金额
     * @return true:无须校验明细;false:需校验明细
     */
    private boolean noNeedCheckDetail(BigDecimal amount, BigDecimal voucherDetailAmount, BigDecimal unRecoveredAmount) {
        if (AmountUtils.equals(BigDecimal.ZERO, voucherDetailAmount)) {
            if (AmountUtils.equals(amount, unRecoveredAmount)) {
                return true;
            } else {
                log.error("检查主动付款凭证明细金额失败, 明细未传");
                throw new ApiException(ApiResponseCode.VOUCHER_DETAIL_AMOUNT_IS_NULL);
            }
        }
        return false;
    }

    private List<CashFlow> getUnattachedCashFlow(String paymentAccountNo, String paymentName, BigDecimal voucherAmount, String bankTransactionNo) {
        if (voucherService.countVoucherBySecondNo(bankTransactionNo) != 0) {
            throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), BANK_TRANSACTION_NO_EXIST_EXCEPTION_MSG);
        }
        List<CashFlow> cashFlowList = cashFlowHandler.getUnattachedCashFlowListBy(paymentAccountNo, paymentName, voucherAmount);
        if (CollectionUtils.isEmpty(cashFlowList)) {
            throw new ApiException(NO_SUCH_CASH_FLOW);
        }
        return cashFlowList;
    }

    @Override
    public SourceDocument attachedSourceDocument(FinancialContract financialContract, Contract contract, List<CashFlow> cashFlowList, String bankTransactionNo, String comment, String voucherUuid, BigDecimal planBookingAmount) {
        if (cashFlowList == null || CollectionUtils.isEmpty(cashFlowList) || cashFlowList.get(0) == null || cashFlowList.size() != 1) {
            return null;
        }
        CashFlow cashFlow = cashFlowList.get(0);
        // FIXME 新增 contract 相关查询条件
        List<SourceDocument> sourceDocuments = sourceDocumentService.getDepositReceipt(cashFlow.getUuid(), SourceDocumentStatus.SIGNED, contract.getUuid(),
                financialContract.getUuid());
        SourceDocument sourceDocument = null;
        if (CollectionUtils.isNotEmpty(sourceDocuments) && sourceDocuments.size() == 1) {
            sourceDocument = sourceDocuments.get(0);
        }
        if (sourceDocument == null) {
            String financialContractUuid = financialContract.getUuid();
            String customerUuid = contract.getCustomerUuid();
            String contractUuid = contract.getUuid();
            VirtualAccount virtualAccount = virtualAccountService.create_if_not_exist_virtual_account(customerUuid, financialContractUuid, contractUuid);
            // 新增一个 充值金额
            sourceDocument = SourceDocumentImporter.createActivePaymentVoucherSourceDocument(financialContract, contract, cashFlow, virtualAccount, planBookingAmount, voucherUuid);
        }
        sourceDocument.setPlanBookingAmount(planBookingAmount);
        sourceDocument.setVoucherUuid(voucherUuid);
        sourceDocument.setOutlierSerialGlobalIdentity(bankTransactionNo);
        sourceDocument.setOutlierBreif(comment);
        sourceDocumentService.saveOrUpdate(sourceDocument);
        return sourceDocument;
    }

    /**
     * 校验收款账户
     *
     * @param receivableAccountNo 收款账号
     * @param financialContract   信托合同
     */
    private void checkReceivableAccountNo(String receivableAccountNo, FinancialContract financialContract) {
        if (StringUtils.isEmpty(receivableAccountNo)) {
            return;
        }
        if (null == financialContract) {
            throw new ApiException(FINANCIAL_CONTRACT_NOT_EXIST);
        }
        String accountNo = financialContract.getCapitalAccount().getAccountNo();
        if (!StringUtils.equals(receivableAccountNo, accountNo)) {
            throw new ApiException(NO_SUCH_RECEIVABLE_ACCOUNT_NO);
        }
    }

    @Override
    public void checkRequestNoAndSaveLog(ActivePaymentVoucher model, String ip) {
        List<ActivePaymentVoucherLog> logs = activePaymentVoucherLogService.getLogByRequestNo(model.getRequestNo());
        if (CollectionUtils.isNotEmpty(logs)) {
            throw new ApiException(REPEAT_REQUEST_NO);
        }
    }

    /**
     * 撤销主动付款凭证接口
     *
     * @param financialContract
     * @param bankTransactionNo
     */
    @Override
    public void undoActivePaymentVoucher(FinancialContract financialContract, String bankTransactionNo) {
        if (financialContract == null) {
            throw new ApiException(FINANCIAL_CONTRACT_NOT_EXIST);
        }
        Voucher voucher = voucherService.getValidVoucher(bankTransactionNo, financialContract.getUuid());
        if (voucher == null) {
            throw new ApiException(NO_SUCH_VOUCHER);
        }
        String voucherUuid = voucher.getUuid();
        String voucherNo = voucher.getVoucherNo();
        String firstNo = voucher.getFirstNo();
        int count = sourceDocumentDetailService.countSourceDocumentDetailList(voucherUuid);
        if (count == 0) {
            throw new ApiException(NO_SUCH_VOUCHER);
        }
        int successCount = sourceDocumentDetailService.countSuccessSourceDocumentDetailList(voucherUuid);
        if (successCount > 0) {
            throw new ApiException(VOUCHER_CAN_NOT_CANCEL);
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        voucherHandler.cancelSourceDocumentAndCashFlow(voucherUuid, bankTransactionNo, cashFlowUuid);
        cancelSourceDocumentDetails(voucherUuid, voucherNo, firstNo);
    }

    private void cancelSourceDocumentDetails(String voucherUuid, String voucherNo, String firstNo) {
        sourceDocumentDetailService.cancelDetails(voucherUuid);
        voucherService.cancelVoucher(voucherUuid);
        List<SourceDocumentResource> resource = sourceDocumentResourceService.getResourceByVoucherNo(voucherNo, firstNo);
        for (SourceDocumentResource res : resource) {
            File file = new File(res.getPath());
            FileUtils.deleteQuietly(file);
            sourceDocumentResourceService.delete(res);
        }
    }

    @Override
    public VoucherCreateBaseModel searchAccountInfoByContractNo(String contractNo) {
        Contract contract = contractService.getContractByContractNo(contractNo);
        if (null == contract) {
            return null;
        }
        ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
        if (null == contractAccount) {
            return null;
        }
        List<AssetSet> assetSetList = repaymentPlanService.get_all_unclear_and_open_asset_set_list(contract.getUuid());
        if (CollectionUtils.isEmpty(assetSetList)) {
            return null;
        }
        return new VoucherCreateBaseModel(contract, contractAccount, assetSetList);
    }

    @Override
    public List<ContractAccount> searchAccountInfoByName(String name) {
        return contractAccountService.getTheEfficientContractAccountListByPayerName(name);
    }


    @Override
    public void updateActiveVoucherComment(Long voucherId, String comment, Principal principal, String ip) {
        Voucher voucher = voucherService.load(Voucher.class, voucherId);
        String updateComment = voucher.getComment();
        if (voucher.isInvalid()) {
            throw new ApiException("当前凭证已作废");
        }
        voucher.setComment(comment);
        voucherService.update(voucher);

        String sourceDocumentUuid = voucher.getSourceDocumentUuid();
        if (StringUtils.isNotBlank(sourceDocumentUuid)) {
            SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(voucher.getSourceDocumentUuid());
            sourceDocument.setOutlierBreif(comment);
            sourceDocumentService.update(sourceDocument);
        }
        SystemOperateLog systemOperateLog = null;
        try {
            systemOperateLog = recordLogCore.insertNormalRecordLog(
                    principal.getId(), ip,
                    LogFunctionType.EDIT_ACTIVE_PAYMENT_VOUCHER_COMMENT, LogOperateType.UPDATE,
                    voucher);
        } catch (Exception e) {
            log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
        }
        systemOperateLog.setKeyContent(voucher.getUuid());
        StringBuilder recordContent = new StringBuilder();
        recordContent.append("修改备注，由【 ").append(updateComment).
                append(" 】修改为【 ").append(comment).append(" 】");
        systemOperateLog.setRecordContent(recordContent.toString());
        systemOperateLogService.saveOrUpdate(systemOperateLog);
    }

    @Override
    public List<String> getActiveVoucherResource(Long voucherId) {
        Voucher voucher = voucherService.load(Voucher.class, voucherId);
        if (null == voucher) {
            return Collections.emptyList();
        }
        String voucherNo = voucher.getVoucherNo();
        String firstNo = voucher.getFirstNo();
        List<SourceDocumentResource> list = sourceDocumentResourceService.getResourceByVoucherNo(voucherNo, firstNo);
        return list.stream().filter(Objects::nonNull).map(SourceDocumentResource::getPath).collect(Collectors.toList());
    }

    @Override
    public String uploadSingleFileReturnUUID(MultipartFile file) throws IOException {
        String tempFilePath = saveFileToServiceReturnPath(file);
        SourceDocumentResource sourceDocumentResource = new SourceDocumentResource(tempFilePath);
        sourceDocumentResourceService.save(sourceDocumentResource);
        return sourceDocumentResource.getUuid();
    }

    /**
     * 获得还款计划明细金额
     */
    private VoucherCreateAssetInfoModel get_asset_info_model(AssetSet assetSet, String contractUniqueId, String contractUuid) {
        BigDecimal overduePenaltyFee = repaymentPlanExtraChargeService.calcPenaltyFee(assetSet);
        Map<String, BigDecimal> amountMap = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSet.getAssetUuid());
        return new VoucherCreateAssetInfoModel(assetSet, overduePenaltyFee, amountMap, contractUniqueId, contractUuid);
    }

    /**
     * 获取还款账户信息
     */
    @Override
    public List<VoucherCreateAccountInfoModel> getContractAccountInfoModelListBy(String paymentAccountNo) {
        try {
            if (StringUtils.isEmpty(paymentAccountNo)) {
                return Collections.emptyList();
            }
            return contractAccountService.getFirstFiveContractAccountInfoModel(paymentAccountNo);
        } catch (Exception e) {
            log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
            return Collections.emptyList();
        }
    }

    /**
     * 保存主动付款凭证
     */
    @Override
    public void save(VoucherCreateSubmitModel model, Principal principal, String ipAddress) {
        String requestNo = UUID.randomUUID().toString();

        String financialContractUuid = model.getFinancialContractUuid();

        CashFlow cashFlow = null;

        String cashFlowUuid = model.getCashFlowUuid();

        if (StringUtils.isNotBlank(cashFlowUuid)) {
            cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
        }

        Map<String, List<VoucherCreateAssetInfoModel>> assetInfoMap = getAssetInfoMap(model.getAssetInfoModelList());

        Voucher voucher = saveActivePaymentVoucher(principal, model, cashFlow, requestNo, financialContractUuid, null, SourceDocumentDetailCheckState.UNCOMMITTED,
                ipAddress);

        String receivableAccountNo = "";

        if (StringUtils.isNotBlank(financialContractUuid)) {
            FinancialContract financialContract = financialContractService.getCacheableFinancialContracBy(financialContractUuid);
            if (financialContract != null) {
                receivableAccountNo = financialContract.getCapitalAccount().getAccountNo();
            }
        }

        createDetailsAndUpdateConnectionRelation(financialContractUuid, assetInfoMap, Collections.emptyList(), model, requestNo, SourceDocumentDetailCheckState.CHECK_SUCCESS, model.getResourceUuidArray(), voucher, receivableAccountNo);
    }

    /**
     * 提交主动付款凭证
     */
    @Override
    public List<ContractActiveSourceDocumentMapper> submit(Principal principal, VoucherCreateSubmitModel model, String ip) {
        String financialContractUuid = model.getFinancialContractUuid();

        FinancialContract financialContract = financialContractService.getCacheableFinancialContracBy(financialContractUuid);

        if (financialContract == null) {
            throw new GlobalRuntimeException("信托合同不存在！");
        }

        if (model.getVoucherAmount().compareTo(model.getRepaymentPlanAmount()) != 0) {
            throw new GlobalRuntimeException("凭证金额与明细金额不匹配！");
        }
        validateRepaymentPlanAndAmountDetails(model);
        //生成requestNo
        String requestNo = UUID.randomUUID().toString();

        String cashFlowUuid = model.getCashFlowUuid();

        cashFlowService.connectVoucher(cashFlowUuid, model.getBankTransactionNo());

        String newBankTransactionNo = cashFlowService.searchBankTransactionNo(cashFlowUuid);

        if (StringUtils.isEmpty(newBankTransactionNo) || !newBankTransactionNo.equals(model.getBankTransactionNo())) {
            throw new GlobalRuntimeException("流水已关联凭证!");
        }

        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);

        Map<String, List<VoucherCreateAssetInfoModel>> assetInfoMap = getAssetInfoMap(model.getAssetInfoModelList());

        String receivableAccountNo = financialContract.getCapitalAccount().getAccountNo();

        Voucher voucher = saveActivePaymentVoucher(principal, model, cashFlow, requestNo, financialContractUuid, receivableAccountNo,
                SourceDocumentDetailCheckState.CHECK_SUCCESS, ip);

        List<SourceDocument> sourceDocuments = saveSourceDocument(model, cashFlow, assetInfoMap, financialContractUuid, voucher);

        createDetailsAndUpdateConnectionRelation(financialContractUuid, assetInfoMap, sourceDocuments, model, requestNo, SourceDocumentDetailCheckState
                .CHECK_SUCCESS, model.getResourceUuidArray(), voucher, receivableAccountNo);

        List<ContractActiveSourceDocumentMapper> mappers = new ArrayList<>();

        if (cashFlow != null && cashFlow.isIssuing()) {
            //部分充值则不发送充值消息
            return mappers;
        }
        for (SourceDocument sourceDocument : sourceDocuments) {

            ContractActiveSourceDocumentMapper mapper = new ContractActiveSourceDocumentMapper(sourceDocument.getRelatedContractUuid(), sourceDocument.getUuid(),
                    cashFlowUuid);

            mappers.add(mapper);

        }

        return mappers;
    }

    private Map<String, List<VoucherCreateAssetInfoModel>> getAssetInfoMap(List<VoucherCreateAssetInfoModel> assetInfoModels) {
        Map<String, List<VoucherCreateAssetInfoModel>> assetInfoMap = new HashMap<>();
        for (VoucherCreateAssetInfoModel assetInfoModel : assetInfoModels) {
            String contractUuid = assetInfoModel.getContractUuid();
            List<VoucherCreateAssetInfoModel> voucherCreateAssetInfoModels = new ArrayList<>();
            if (assetInfoMap.containsKey(contractUuid)) {
                voucherCreateAssetInfoModels = assetInfoMap.get(contractUuid);
            }
            voucherCreateAssetInfoModels.add(assetInfoModel);
            assetInfoMap.put(contractUuid, voucherCreateAssetInfoModels);
        }
        return assetInfoMap;
    }

    /**
     * 保存sourceDocument
     */
    private List<SourceDocument> saveSourceDocument(VoucherCreateSubmitModel model, CashFlow cashFlow, Map<String, List<VoucherCreateAssetInfoModel>> assetInfoMap,
                                                    String financialContractUuid, Voucher voucher) {
        FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
        List<SourceDocument> sourceDocuments = new ArrayList<>();
        for (String contractUuid : assetInfoMap.keySet()) {
            Contract contract = contractService.getContract(contractUuid);
            List<VoucherCreateAssetInfoModel> assetInfoModels = assetInfoMap.get(contractUuid);
            BigDecimal planBookingAmount = BigDecimal.ZERO;
            for (VoucherCreateAssetInfoModel assetInfoModel : assetInfoModels) {
                planBookingAmount = planBookingAmount.add(assetInfoModel.getAmount());
            }
            SourceDocument sourceDocument = attachedSourceDocument(financialContract, contract, Arrays.asList(cashFlow), model.getBankTransactionNo(), model
                    .getComment(), voucher.getUuid(), planBookingAmount);
            sourceDocuments.add(sourceDocument);
        }
        return sourceDocuments;
    }

    /**
     * 校验还款计划明细金额
     */
    private void validateRepaymentPlanAndAmountDetails(VoucherCreateSubmitModel model) {
        for (VoucherCreateAssetInfoModel assetInfoModel : model.getAssetInfoModelList()) {
            AssetSet repaymentPlan = repaymentPlanService.getActiveRepaymentPlanByRepaymentCode(assetInfoModel.getSingleLoanContractNo());
            if (null == repaymentPlan) {
                throw new RuntimeException("还款计划已作废或不存在");
            }
            VoucherCreateAssetInfoModel defaultAssetInfoModel = get_asset_info_model(repaymentPlan, assetInfoModel.getContractUniqueId(), assetInfoModel
                    .getContractUuid());

            if (assetInfoModel.getPrincipalValueBD().compareTo(defaultAssetInfoModel.getPrincipalValueBD()) > 0 ||
                    assetInfoModel.getInterestValueBD().compareTo(defaultAssetInfoModel.getInterestValueBD()) > 0 ||
                    assetInfoModel.getServiceChargeBD().compareTo(defaultAssetInfoModel.getServiceChargeBD()) > 0 ||
                    assetInfoModel.getMaintenanceChargeBD().compareTo(defaultAssetInfoModel.getMaintenanceChargeBD()) > 0 ||
                    assetInfoModel.getOtherChargeBD().compareTo(defaultAssetInfoModel.getOtherChargeBD()) > 0 ||
                    assetInfoModel.getOverduePenaltyFeeBD().compareTo(defaultAssetInfoModel.getOverduePenaltyFeeBD()) > 0 ||
                    assetInfoModel.getOverdueObligationFeeBD().compareTo(defaultAssetInfoModel.getOverdueObligationFeeBD()) > 0 ||
                    assetInfoModel.getOverdueServiceChargeBD().compareTo(defaultAssetInfoModel.getOverdueServiceChargeBD()) > 0 ||
                    assetInfoModel.getOverdueOtherChargeBD().compareTo(defaultAssetInfoModel.getOverdueOtherChargeBD()) > 0) {
                throw new RuntimeException("填入的还款计划某项明细金额大于还款计划默认明细金额");
            }
        }
    }

    /**
     * 生成主动付款凭证明细
     */
    private void createSourceDocumentDetails(String financialContractUuid, Map<String, List<VoucherCreateAssetInfoModel>> assetInfoMap, List<SourceDocument>
            sourceDocuments, VoucherCreateSubmitModel model, String requestNo, SourceDocumentDetailCheckState checkState, String receivableAccountNo, String
                                                     voucherUuid) {
        if (CollectionUtils.sizeIsEmpty(assetInfoMap)) {
            return;
        }
        for (String contractUuid : assetInfoMap.keySet()) {
            String sourceDocumentUuid = null;
            for (SourceDocument sourceDocument : sourceDocuments) {
                if (sourceDocument.getRelatedContractUuid().equals(contractUuid)) {
                    sourceDocumentUuid = sourceDocument.getUuid();
                    break;
                }
            }
            for (VoucherCreateAssetInfoModel assetInfoModel : assetInfoMap.get(contractUuid)) {
                String contractUniqueId = assetInfoModel.getContractUniqueId();
                BigDecimal amount = assetInfoModel.getAmount();
                VoucherPayer payer = VoucherPayer.LOANER;
                SourceDocumentDetail sourceDocumentDetail = SourceDocumentDetailImporter.createActivePaymentVoucherDetailWithAssetInfoModel(sourceDocumentUuid,
                        contractUniqueId, assetInfoModel.getSingleLoanContractNo(), amount, requestNo, model.getVoucherTypeEnum().getKey(), payer,
                        receivableAccountNo, model.getPaymentAccountNo(), model.getPaymentName(), model.getPaymentBank(), financialContractUuid, assetInfoModel);
                sourceDocumentDetail.setSecondNo(model.getBankTransactionNo());//一级编号requestNo，二级编号打款流水号bankTransactionNo
                sourceDocumentDetail.setCheckState(checkState);
                sourceDocumentDetail.setVoucherUuid(voucherUuid);
                sourceDocumentDetailService.saveOrUpdate(sourceDocumentDetail);
                log.info("生成主动付款凭证明细, sourceDocumentDetailUUID:" + sourceDocumentDetail.getUuid());
            }
        }
    }

    /**
     * 保存主动付款凭证
     */
    private Voucher saveActivePaymentVoucher(Principal principal,
                                             VoucherCreateSubmitModel model,
                                             CashFlow cashFlow,
                                             String requestNo,
                                             String financialContractUuid,
                                             String receivableAccountNo,
                                             SourceDocumentDetailCheckState checkState,
                                             String ipAddress) {
        BigDecimal voucherAmount = model.getVoucherAmount();
        BigDecimal repaymentPlanAmount = model.getRepaymentPlanAmount();
        String secondType = model.getVoucherTypeEnum().getKey();
        String bankTransactionNo = model.getBankTransactionNo();
        String paymentAccountNo = model.getPaymentAccountNo();
        String paymentName = model.getPaymentName();
        String paymentBank = model.getPaymentBank();
        String voucherUuid;
        String comment = model.getComment();
        String cashFlowUuid = null;
        Date transactionTime = null;
        if (cashFlow != null) {
            cashFlowUuid = cashFlow.getUuid();
            transactionTime = cashFlow.getTransactionTime();
        }
        //新生成凭证
        Voucher voucher = Voucher.createActivePaymentVoucher(financialContractUuid, voucherAmount, requestNo, secondType, bankTransactionNo, receivableAccountNo,
                paymentAccountNo, paymentName, paymentBank, checkState, cashFlowUuid, transactionTime, comment,repaymentPlanAmount);
        if (voucher != null) {
            voucherService.saveOrUpdate(voucher);
            voucherUuid = voucher.getUuid();
            saveActiveVoucherLog(principal, voucherUuid, ipAddress, checkState, LogFunctionType.ACTIVE_PAYMENT_VOUCHER_CREATE, LogOperateType.ADD);
        }
        return voucher;
    }

    /**
     * 创建新的凭证详情并更新资源文件与凭证的关系
     */
    private void createDetailsAndUpdateConnectionRelation(String financialContractUuid, Map<String, List<VoucherCreateAssetInfoModel>> assetInfoMap,
                                                          List<SourceDocument> sourceDocuments, VoucherCreateSubmitModel model, String requestNo,
                                                          SourceDocumentDetailCheckState checkState,
                                                          List<String> resourceUuids, Voucher voucher, String receivableAccountNo) {
        //新生成凭证详情
        createSourceDocumentDetails(financialContractUuid, assetInfoMap, sourceDocuments, model, requestNo, checkState, receivableAccountNo, voucher.getUuid());
        if (resourceUuids != null) {
            // 更新资源文件与凭证的关联关系
            sourceDocumentResourceService.updateResourceConnectionRelationWithVoucherNo(resourceUuids, voucher.getVoucherNo(), requestNo);
        }
    }

    /**
     * 记录log
     */
    private void saveActiveVoucherLog(Principal principal,
                                      String voucherUuid,
                                      String ipAddress,
                                      SourceDocumentDetailCheckState checkState,
                                      LogFunctionType functionType,
                                      LogOperateType operateType) {
        String operateMsg = getOperateMessage(operateType);
        String checkMsg = getCheckMessage(checkState);
        Voucher voucher = voucherService.get_voucher_by_uuid(voucherUuid);

        try {
            SystemOperateLog log = recordLogCore.insertNormalRecordLog(
                    principal.getId(), ipAddress,
                    functionType, operateType,
                    voucher);
            log.setKeyContent(voucherUuid);
            log.setRecordContent((operateMsg + "主动付款凭证，凭证编号为【 " + voucher.getVoucherNo() + " 】，凭证状态为【" + checkMsg + "】"));
            systemOperateLogService.saveOrUpdate(log);
        } catch (Exception e) {
            log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
        }
    }

    private String getOperateMessage(LogOperateType operateType) {
        String operateMsg = "";
        if (operateType == LogOperateType.ADD) {
            operateMsg = "创建";
        } else if (operateType == LogOperateType.UPDATE) {
            operateMsg = "编辑";
        }
        return operateMsg;
    }

    private String getCheckMessage(SourceDocumentDetailCheckState checkState) {
        String checkMsg = "";
        if (checkState == SourceDocumentDetailCheckState.CHECK_SUCCESS) {
            checkMsg = "未核销";
        } else if (checkState == SourceDocumentDetailCheckState.UNCOMMITTED) {
            checkMsg = "未提交";
        } else if (checkState == SourceDocumentDetailCheckState.CHECK_FAILS) {
            checkMsg = "校验失败";
        } else if (checkState == SourceDocumentDetailCheckState.UNCHECKED) {
            checkMsg = "未校验";
        }
        return checkMsg;
    }

    /**
     * 获取贷款合同及其相应的数据
     */
    @Override
    public List<VoucherCreateContractInfoModel> getContractInfoModelList(ContractInfoQueryModel queryModel) {
        try {
            List<VoucherCreateContractInfoModel> models = contractService.getContractInfoBy(queryModel.getCustomerName(), queryModel.getFinancialContractUuid());
            for (VoucherCreateContractInfoModel model : models) {
                model.setFinancialContractName(queryModel.getFinancialContractName());
                model.setFinancialContractNo(queryModel.getFinancialContractNo());
            }
            return models;
        } catch (Exception e) {
            log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
            return Collections.emptyList();
        }
    }

    /**
     * 获取还款计划相应信息
     */
    @Override
    public List<VoucherCreateAssetInfoModel> getAssetInfoModelList(AssetInfoQueryModel queryModel) {
        List<VoucherCreateContractInfoModel> contractInfoModels = queryModel.getContractInfoModelList();
        List<VoucherCreateAssetInfoModel> models = new ArrayList<>();
        for (VoucherCreateContractInfoModel contractInfoModel : contractInfoModels) {
            List<AssetSet> assetSets = repaymentPlanService.getAssetBy(queryModel.getFinancialContractUuid(), contractInfoModel.getContractUuid());
            List<VoucherCreateAssetInfoModel> assetInfoModels = assetSets.stream()
                    .filter(Objects::nonNull)
                    .map(a -> get_asset_info_model(a, contractInfoModel.getContractUniqueId(), contractInfoModel.getContractUuid()))
                    .collect(Collectors.toList());
            models.addAll(assetInfoModels);
        }
        return models;
    }

    @Override
    public List<ContractActiveSourceDocumentMapper> submitActivePaymentVoucher(ActivePaymentVoucher model, MultipartHttpServletRequest fileRequest)
            throws IOException {
        List<ActivePaymentVoucherDetail> details = model.getDetailModel();
        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(model.getFinancialContractNo());
        if(financialContract == null){
            log.error("financialContract  is null");
            throw new ApiException(FINANCIAL_PRODUCT_CODE_ERROR);
        }
        checkReceivableAccountNo(model.getReceivableAccountNo(), financialContract);
        String ledgerBookNo = financialContract.getLedgerBookNo();
        String bankTransactionNo = model.getBankTransactionNo();
        Map<String, List<ActivePaymentVoucherDetail>> detailMap = checkVoucherAmountAndReturnDetailMap(ledgerBookNo, model.getVoucherAmount(), details, financialContract);

        List<CashFlow> cashFlowList = getUnattachedCashFlow(model.getPaymentAccountNo(), model.getPaymentName(), model.getVoucherAmount(), bankTransactionNo);
        String voucherUuid = null;
        String cashFlowUuid = null;
        Date transactionTime = null;
        boolean cashFlowPartIssuing = false;
        if (CollectionUtils.isNotEmpty(cashFlowList) && cashFlowList.size() == 1) {
            CashFlow cashFlow = cashFlowList.get(0);
            cashFlowUuid = cashFlow.getUuid();
            transactionTime = cashFlow.getTransactionTime();
            cashFlow.setStringFieldOne(bankTransactionNo);
            cashFlowService.connectVoucher(cashFlowUuid, bankTransactionNo);
            String stringFieldOne = cashFlowService.searchBankTransactionNo(cashFlowUuid);
            if (!stringFieldOne.equals(bankTransactionNo)) {
                throw new CashFlowNotExistException();
            }
            cashFlowPartIssuing = cashFlow.isIssuing();
        }

        Voucher voucher = Voucher.createActivePaymentVoucher(financialContract.getUuid(), model.getVoucherAmount(), model.getRequestNo(),
                VoucherType.createActivePaymentVoucherType(model.getVoucherType()).getKey(), bankTransactionNo, model.getReceivableAccountNo(), model.getPaymentAccountNo(),
                model.getPaymentName(), model.getPaymentBank(),
                SourceDocumentDetailCheckState.CHECK_SUCCESS, cashFlowUuid, transactionTime, null,model.getVoucherAmount());
        if (voucher != null) {
            voucherService.saveOrUpdate(voucher);
            voucherUuid = voucher.getUuid();
            log.info("生成主动付款凭证, voucherUuid:" + voucherUuid);
        }
        List<ContractActiveSourceDocumentMapper> mappers = createSourceDocumentAndDetails(model, financialContract,
                bankTransactionNo, detailMap, cashFlowList, voucherUuid);
        this.saveFileToService(fileRequest, model.getRequestNo(), voucherUuid);
        if (cashFlowPartIssuing) {
            return Collections.emptyList();
        } else {
            return mappers;
        }
    }

    private List<ContractActiveSourceDocumentMapper> createSourceDocumentAndDetails(
            ActivePaymentVoucher model, FinancialContract financialContract, String bankTransactionNo,
            Map<String, List<ActivePaymentVoucherDetail>> detailMap, List<CashFlow> cashFlowList, String voucherUuid) {
        List<ContractActiveSourceDocumentMapper> mappers = new ArrayList<>();
        for (String contractUuid : detailMap.keySet()) {
            List<ActivePaymentVoucherDetail> detailList = detailMap.get(contractUuid);
            BigDecimal planBookingAmount = BigDecimal.ZERO;
            for (ActivePaymentVoucherDetail detail : detailList) {
                planBookingAmount = planBookingAmount.add(detail.getAmount());
            }
            Contract contract = contractService.getContract(contractUuid);
            SourceDocument sourceDocument = attachedSourceDocument(financialContract, contract, cashFlowList, bankTransactionNo, StringUtils.EMPTY, voucherUuid,
                    planBookingAmount);
            String sourceDocumentUuid = sourceDocument == null ? "" : sourceDocument.getSourceDocumentUuid();
            createDetails(contract, sourceDocumentUuid, model, voucherUuid, detailList, financialContract);
            ContractActiveSourceDocumentMapper mapper = new ContractActiveSourceDocumentMapper(contractUuid, sourceDocumentUuid, sourceDocument == null ? "" :
                    sourceDocument.getOutlierDocumentUuid());
            mappers.add(mapper);
        }
        return mappers;
    }

    /**
     * 获取MD5的商户还款计划编号
     *
     * @return
     */
    private String getRepayScheduleNoMd5(FinancialContract financialContract, String repayScheduleNo) {
        if (StringUtils.isEmpty(financialContract.getContractNo()) || StringUtils.isEmpty(repayScheduleNo)) {
            return "";
        }
        return repaymentPlanService.getRepayScheduleNoMD5(financialContract.getContractNo(), repayScheduleNo, com.zufangbao.sun.utils.StringUtils.EMPTY);
    }

    private Map<String, List<ActivePaymentVoucherDetail>> checkVoucherAmountAndReturnDetailMap(String ledgerBookNo, BigDecimal voucherAmount, List<ActivePaymentVoucherDetail> details, FinancialContract financialContract) {
        Map<String, List<ActivePaymentVoucherDetail>> detailMap = new HashMap<>();
        BigDecimal sum = BigDecimal.ZERO;
        for (ActivePaymentVoucherDetail detail : details) {
            sum = sum.add(detail.getAmount());
        }
        if (voucherAmount.compareTo(sum) != 0) {
            throw new ApiException(VOUCHER_AMOUNT_ERROR);
        }
        //根据用户身份证号校验还款计划是否为同一人名下
        String customerAccount = "";
        for (ActivePaymentVoucherDetail detail : details) {
            String repayScheduleNo = detail.getRepayScheduleNo();
            String repaymentPlanNo = detail.getRepaymentPlanNo();
            Integer currentPeriod = detail.getCurrentPeriod();
            Contract contract = contractApiHandler.getContractBy(detail.getUniqueId(), detail.getContractNo());
            if (contract == null) {
                throw new ApiException(CONTRACT_NOT_EXIST);
            }
            if (StringUtils.isEmpty(customerAccount)) {
                customerAccount = contract.getCustomer().getAccount();
            } else {
                if (!contract.getCustomer().getAccount().equals(customerAccount)) {
                    throw new ApiException(CUSTOMER_NOT_UNIQUE);
                }
            }
            AssetSet repaymentPlan = null;

            if (!StringUtils.isEmpty(repayScheduleNo) && !StringUtils.isEmpty(financialContract.getContractNo())) {
                String repayScheduleNoMD5 = repaymentPlanService.getRepayScheduleNoMD5(financialContract.getContractNo(), repayScheduleNo, com.zufangbao.sun.utils
                        .StringUtils.EMPTY);
                ArrayList<String> repayScheduleNoMd5List = new ArrayList<>();
                repayScheduleNoMd5List.add(repayScheduleNoMD5);
                List<AssetSet> assetSetsList = repaymentPlanService.getAssetSetListByRepayScheduleNoMD5(repayScheduleNoMd5List);
                if (CollectionUtils.isEmpty(assetSetsList)) {
                    repaymentPlan = null;
                } else {
                    repaymentPlan = assetSetsList.get(0);
                }
                if (repaymentPlan == null) {
                    throw new ApiException(ApiResponseCode.REPAYMENT_CODE_NOT_IN_CONTRACT);
                }
                if (!(contract.getUuid().equals(repaymentPlan.getContractUuid()))) {
                    throw new ApiException(ApiResponseCode.REPAYMENT_CODE_NOT_IN_CONTRACT);
                }
                if (!StringUtils.isEmpty(repaymentPlan.getSingleLoanContractNo())) {
                    detail.setRepaymentPlanNo(repaymentPlan.getSingleLoanContractNo());
                }
            } else if (!StringUtils.isEmpty(repaymentPlanNo)) {
                repaymentPlan = repaymentPlanService.get_repaymentPlan(repaymentPlanNo, contract.getUuid());
                if (repaymentPlan == null) {
                    throw new ApiException(ApiResponseCode.REPAYMENT_CODE_NOT_IN_CONTRACT);
                }
            } else if (!(currentPeriod == null)) {
                List<Integer> currentPeriodList = new ArrayList<>();
                currentPeriodList.add(currentPeriod);
                repaymentPlan = repaymentPlanService.getByContractNoAndCurrentPeriod(contract, currentPeriodList).get(0);
                if (repaymentPlan == null) {
                    throw new ApiException(ApiResponseCode.REPAYMENT_CODE_NOT_IN_CONTRACT);
                }
                if (!StringUtils.isEmpty(repaymentPlan.getSingleLoanContractNo())) {
                    detail.setRepaymentPlanNo(repaymentPlan.getSingleLoanContractNo());
                }
            }
            String assetUuid = repaymentPlan.getAssetUuid();
            BigDecimal unRecoveredAmount = ledgerBookStatHandler.unrecovered_asset_snapshot(ledgerBookNo, assetUuid, contract.getCustomerUuid(), true);
            log.info("voucherDetailAmount {}", detail.getAmount());
            log.info("unrecoveredAmount {}", unRecoveredAmount);
            isDetailAmountTooLarge(detail.getAmount(), unRecoveredAmount);

            List<ActivePaymentVoucherDetail> detailList = new ArrayList<>();
            if (detailMap.containsKey(contract.getUuid())) {
                detailList = detailMap.get(contract.getUuid());
            }
            String contractUuid = contract.getUuid();
            detailList.add(detail);
            detailMap.put(contractUuid, detailList);

            if (noNeedCheckDetail(detail.getAmount(), detail.detailAmount(), unRecoveredAmount)) {
                continue;
            }
            checkDetail(ledgerBookNo, detail, repaymentPlanNo, assetUuid, repayScheduleNo, currentPeriod);


        }
        return detailMap;
    }

    private void createDetails(Contract contract, String sourceDocumentUuid,
                               ActivePaymentVoucher model, String voucherUuid, List<ActivePaymentVoucherDetail> details, FinancialContract financialContract) {
        VoucherType voucherType = VoucherType.createActivePaymentVoucherType(model.getVoucherType());
        if (voucherType == null) {
            throw new ApiException(NO_SUCH_VOUCHER_TYPE);
        }
        String contractUniqueId = contract.getUniqueId();
        VoucherPayer payer = VoucherPayer.LOANER;

        String requestNo = model.getRequestNo();
        String receivableAccountNo = model.getReceivableAccountNo();
        String paymentAccountNo = model.getPaymentAccountNo();
        String paymentName = model.getPaymentName();
        String paymentBank = model.getPaymentBank();
        String bankTransactionNo = model.getBankTransactionNo();
        for (ActivePaymentVoucherDetail detail : details) {
            String repaymentPlanNo = detail.getRepaymentPlanNo();
            BigDecimal amount = detail.getAmount();
            SourceDocumentDetail sourceDocumentDetail = SourceDocumentDetailImporter.createActivePaymentVoucherDetail(
                    sourceDocumentUuid, contractUniqueId, repaymentPlanNo, amount, requestNo, voucherType.getKey(),
                    payer, receivableAccountNo, paymentAccountNo, paymentName, paymentBank,
                    contract.getFinancialContractUuid());
            sourceDocumentDetail.setSecondNo(bankTransactionNo);// 一级编号requestNo，二级编号打款流水号bankTransactionNo
            sourceDocumentDetail.setCheckState(SourceDocumentDetailCheckState.CHECK_SUCCESS);
            sourceDocumentDetail.setDetailAmount(detail.getDetailAmountMap());
            sourceDocumentDetail.setVoucherUuid(voucherUuid);
            String repayScheduleNoMd5 = getRepayScheduleNoMd5(financialContract, detail.getRepayScheduleNo());
            sourceDocumentDetail.setRepayScheduleNo(repayScheduleNoMd5);
            sourceDocumentDetail.setCurrentPeriod(detail.getCurrentPeriod());
            sourceDocumentDetail.setOuterRepaymentPlanNo(detail.getRepayScheduleNo());
            sourceDocumentDetailService.saveOrUpdate(sourceDocumentDetail);
            log.info("生成主动付款凭证明细, sourceDocumentDetailUUID:" + sourceDocumentDetail.getUuid());
        }
    }
}