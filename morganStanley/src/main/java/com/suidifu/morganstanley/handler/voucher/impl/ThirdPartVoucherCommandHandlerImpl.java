package com.suidifu.morganstanley.handler.voucher.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.BusinessException;
import com.suidifu.morganstanley.handler.voucher.ThirdPartVoucherCommandHandler;
import com.suidifu.morganstanley.model.request.voucher.ThirdPartVoucher;
import com.suidifu.morganstanley.model.request.voucher.ThirdPartVoucherDetail;
import com.suidifu.morganstanley.model.request.voucher.ThirdPartVoucherRepayDetail;
import com.zufangbao.gluon.api.swissre.institutionrecon.request.model.CommonCounterCashFlow;
import com.zufangbao.gluon.api.swissre.institutionrecon.request.model.CommonLocalTransactionCommand;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.sun.api.model.deduct.IsTotal;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.ThirdPartVoucherCommandLogService;
import com.zufangbao.sun.service.ThirdPartyVoucherBatchService;
import com.zufangbao.sun.utils.*;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationDetail;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.CommonAuditResultRepaymentDetail;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartPayVoucherSpec;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogIssueStatus;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogStatus;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPayExecStatus;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPayVoucherBatch;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyVoucherCommandLog;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.ONE_BATCH_REPEATE_VOUCHER_NO_OR_TRANSCATION_REQUEST_NO;

@Log4j2
@Component(value = "ThirdPartVoucherCommandHandler")
public class ThirdPartVoucherCommandHandlerImpl implements ThirdPartVoucherCommandHandler {

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private FinancialContractService financialContractService;

    @Autowired
    private ThirdPartVoucherCommandLogService thirdPartVoucherCommandLogService;

    @Autowired
    private DeductApplicationService deductApplicationService;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private DeductApplicationDetailService deductApplicationDetailService;

    @Autowired
    private DeductPlanService deductPlanService;

    @Autowired
    private ThirdPartyVoucherBatchService thirdPartVoucherBatchService;
    @Value("${thirdPartyCommandLogHistorySavePath}")
    private String savePath = "";

    @Override
    public void commandRequestLogicValidate(ThirdPartVoucher model) {

        //financial_contract_no需要有效
        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(model.getFinancialContractNo());
        if (financialContract == null) {
            throw new ApiException(FINANCIAL_CONTRACT_NOT_EXIST);
        }
        List<ThirdPartVoucherDetail> detailModelList = model.getDetailListParseJson();

        checkRepeateOneBatchTranscationRequestNoAndVoucherNo(detailModelList);


    }

    private void checkRepeateOneBatchTranscationRequestNoAndVoucherNo(
            List<ThirdPartVoucherDetail> detailModelList) {
        int distinctTranscationRequestNoSize = detailModelList.stream().map(detail -> detail.getTransactionRequestNo()).distinct().collect(Collectors.toList())
                .size();
        int distinctVoucherNoSize = detailModelList.stream().map(detail -> detail.getVoucherNo()).distinct().collect(Collectors.toList()).size();
        if (detailModelList.size() != distinctTranscationRequestNoSize || detailModelList.size() != distinctVoucherNoSize) {
            throw new ApiException(ONE_BATCH_REPEATE_VOUCHER_NO_OR_TRANSCATION_REQUEST_NO);
        }
    }

    @Override
    public Map<String, Object> generateThirdPartVoucherCommandLog(ThirdPartVoucher model, String ipAddress) {

        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(
                model.getFinancialContractNo());
        List<ThirdPartVoucherDetail> detailModelList = model.getDetailListParseJson();
        String batchUuid = model.getBatchUuid();
        createThirdPartVoucherPayBatch(model, financialContract);

        Map<String, Object> errmap = new HashMap<String, Object>();
        for (ThirdPartVoucherDetail detail : detailModelList) {
            ThirdPartyVoucherCommandLog commandLog =
                    thirdPartVoucherCommandLogService.getThirdPartyVoucherLogByTradeuuid(
                            detail.getTransactionRequestNo());
            //构造请求参数map
            String versionNo = UUIDUtil.randomUUID();
            Map<String,String> detailMap =getThirdPartVoucherDetail(model,financialContract,versionNo);
            //数据库中查不到log即为保存,否则做更新操作
            if (null == commandLog) {
                log.info("插入ThirdPartyVoucherDetail: " + commandLog);
                commandLog = new ThirdPartyVoucherCommandLog(detailMap, ipAddress, financialContract.getFinancialContractUuid(), financialContract.getContractNo(), versionNo);
                try {
                    thirdPartVoucherCommandLogService.save(commandLog);
                } catch (Exception e) {
                    log.error("保存commandlog错误");
                    throw new GlobalRuntimeException("保存commandlog错误" + e.getMessage());
                }

            } else {
                //如果没有验证成功
                if (!VoucherLogStatus.SUCCESS.equals(commandLog.getVoucherLogStatus())) {
                    saveHistoryLogAndUpdateLog(commandLog, detail.getTransactionRequestNo());
                    //更新第三方凭证中的明细及版本号
                    commandLog.updateRepay(detailMap, ipAddress, financialContract, batchUuid, versionNo);
                    try {
                        thirdPartVoucherCommandLogService.update(commandLog);
                    } catch (Exception e) {
                        log.error("更新log错误");
                        throw new GlobalRuntimeException("更新log错误" + e.getMessage());
                    }

                } else {
                    //验证成功
                    errmap.put(detail.getTransactionRequestNo(), "该凭证已经校验成功");
                }
            }
        }
        return errmap;
    }

    private void createThirdPartVoucherPayBatch(ThirdPartVoucher model, FinancialContract financialContract) {
        Map<String,String> voucher = new HashMap<>();
        voucher.put("batchUuid", model.getBatchUuid());
        voucher.put("requestNo", model.getRequestNo());
        int listSize = model.getDetailListParseJson().size();
        voucher.put("detailListSize", String.valueOf(listSize));
        ThirdPartyPayVoucherBatch thirdPartVoucherBatch = new ThirdPartyPayVoucherBatch(voucher, financialContract);
        thirdPartVoucherBatchService.save(thirdPartVoucherBatch);
    }

    public void reverseCastDeductInfoAndWriteOff(String logJsonContent, String cashFlowJsonContent) {


        CommonCounterCashFlow cashFlowInfo = JsonUtils.parse(cashFlowJsonContent, CommonCounterCashFlow.class);
        CommonLocalTransactionCommand transcationVoucherInfo = JsonUtils.parse(cashFlowJsonContent, CommonLocalTransactionCommand.class);

        createDeductInfo(cashFlowInfo, transcationVoucherInfo);
    }

    private void createDeductInfo(CommonCounterCashFlow cashFlowInfo, CommonLocalTransactionCommand transcationVoucherInfo) {

        DeductApplication dedcutApplication = createAndSaveDeductApplication(transcationVoucherInfo, cashFlowInfo);
        createDeductApplicationDetailInfo(transcationVoucherInfo, cashFlowInfo, dedcutApplication);

        createAndSaveDeductPlan(transcationVoucherInfo, cashFlowInfo, dedcutApplication);

    }

    private DeductPlan createAndSaveDeductPlan(CommonLocalTransactionCommand transcationVoucherInfo, CommonCounterCashFlow cashFlowInfo, DeductApplication
            dedcutApplication) {

        DeductPlan deductPlan = new DeductPlan();
        deductPlanService.save(deductPlan);
        return deductPlan;

    }

    private void createDeductApplicationDetailInfo(CommonLocalTransactionCommand transcationVoucherInfo,
                                                   CommonCounterCashFlow cashFlowInfo, DeductApplication deductApplication) {
        List<CommonAuditResultRepaymentDetail> repaymentDetailInfos = (List<CommonAuditResultRepaymentDetail>) JsonUtils.parse(transcationVoucherInfo
                .getRepayDetailList(), CommonAuditResultRepaymentDetail.class);
        for (CommonAuditResultRepaymentDetail repaymentDetailInfo : repaymentDetailInfos) {
            createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE, IsTotal.DETAIL,
                    repaymentDetailInfo.getPrincipal());
            createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_INTEREST, IsTotal.DETAIL,
                    repaymentDetailInfo.getInterest());
            createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE, IsTotal.DETAIL,
                    repaymentDetailInfo.getMaintenanceCharge());
            createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE, IsTotal.DETAIL,
                    repaymentDetailInfo.getServiceCharge());
            createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE, IsTotal.DETAIL,
                    repaymentDetailInfo.getOtherCharge());

            createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo, ChartOfAccount.SND_RECIEVABLE_LOAN_PENALTY, IsTotal.DETAIL,
                    repaymentDetailInfo.getPenaltyFee());
            createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo, ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION, IsTotal.DETAIL,
                    repaymentDetailInfo.getLatePenalty());
            createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo, ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE, IsTotal.DETAIL,
                    repaymentDetailInfo.getLateFee());
            createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo, ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE, IsTotal.DETAIL,
                    repaymentDetailInfo.getLateOtherCost());
            createSingleDeductApplicationDetailTotalOverDueFee(deductApplication, repaymentDetailInfo, IsTotal.DETAIL, repaymentDetailInfo.calcTotalOverDueFee());
            createSingleDeductApplicationDetailTotalReceivableAmount(deductApplication, repaymentDetailInfo, IsTotal.TOTAL, repaymentDetailInfo.getAmount());
        }

    }

    private void createSingleDeductApplicationDetail(DeductApplication deductApplication,
                                                     CommonAuditResultRepaymentDetail repaymentDetailInfo, String chartString, IsTotal isTotal, BigDecimal amount) {

        //金额小于等于零不生成明细记录
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            return;
        }
        AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetailInfo.getRepaymentPlanNo());
        DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication, repaymentDetailInfo, isTotal, amount, repaymentPlan
                .getAssetUuid());
        deductApplicationDetail.copyTAccount(ChartOfAccount.EntryBook().get(chartString));
        deductApplicationDetailService.save(deductApplicationDetail);
    }

    private void createSingleDeductApplicationDetailTotalReceivableAmount(DeductApplication deductApplication,
                                                                          CommonAuditResultRepaymentDetail repaymentDetail, IsTotal total, BigDecimal
                                                                                  caclAccountReceivableAmount) {

        //金额小于等于零不生成明细记录
        if (caclAccountReceivableAmount.compareTo(BigDecimal.ZERO) < 1) {
            return;
        }
        AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());

        DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication, repaymentDetail, total, caclAccountReceivableAmount,
                repaymentPlan.getAssetUuid());
        deductApplicationDetail.setFirstAccountName(ExtraChargeSpec.TOTAL_RECEIVABEL_AMOUNT);
        deductApplicationDetailService.save(deductApplicationDetail);
    }

    private void createSingleDeductApplicationDetailTotalOverDueFee(DeductApplication deductApplication, CommonAuditResultRepaymentDetail repaymentDetail, IsTotal
            isTotal, BigDecimal totalOverdueFee) {
        //金额小于等于零不生成明细记录
        if (totalOverdueFee.compareTo(BigDecimal.ZERO) < 1) {
            return;
        }
        AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());
        DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication, repaymentDetail, isTotal, totalOverdueFee, repaymentPlan
                .getAssetUuid());
        deductApplicationDetail.setFirstAccountName(ExtraChargeSpec.TOTAL_OVERDUE_FEE);
        deductApplicationDetailService.save(deductApplicationDetail);

    }

    private DeductApplication createAndSaveDeductApplication(CommonLocalTransactionCommand transcationVoucherInfo,
                                                             CommonCounterCashFlow cashFlowInfo) {

        DeductApplication deductApplication = new DeductApplication();
        deductApplicationService.save(deductApplication);
        return deductApplication;
    }

    @Override
    public Map<String, Object> getThirdPartVoucherRepayDetailList(String tradeUuid) {
        //log.info("---save path : " + savePath);
        File file = new File(savePath + File.separator + tradeUuid);
        File[] detail = file.listFiles();
        List<String> detailList = new ArrayList<String>();
        Map<String, Object> resMap = new HashMap<String, Object>(3);
        if (null != detail && detail.length > 0) {
            for (int i = 0; i < detail.length; i++) {
                String fileName = detail[i].getName();
                detailList.add(fileName.substring(0, fileName.indexOf(".")));
            }

            resMap.put("versionNameList", detailList);
            String detailFirstName = detailList.get(0);
            try {
//				ThirdPartVoucherRepayDetailModel model = redThirdPartVoucherRepayDetail(tradeUuid,detailFirstName);
                ThirdPartyVoucherCommandLog commandLog = redThirdPartVoucherRepayDetail(tradeUuid, detailFirstName);
                ThirdPartVoucherDetail repayModel =
                        JsonUtils.parse(commandLog.getThirdPartVoucherContent(), ThirdPartVoucherDetail.class);


                List<ThirdPartVoucherRepayDetail> repayDetailModelList = repayModel.getRepayDetailList();
                if (!CollectionUtils.isEmpty(repayDetailModelList)) {
                    resMap.put("historyDetail", repayDetailModelList.get(0));
                } else {
                    resMap.put("historyDetail", null);
                }

//			}catch (Exception e){
//				log.error("读取还款明细错误");
//				throw new BusinessSystemException("读取还款明细错误"+e.getMessage());
//			}
//			try {
//				ThirdPartyVoucherCommandLog log = thirdPartVoucherCommandLogService.getThirdPartyVoucherLogByTradeuuid(tradeUuid);
                resMap.put("voucher", commandLog);
            } catch (Exception e) {
                log.error("读取第三凭证错误");
                throw new GlobalRuntimeException("读取第三方凭证错误" + e.getMessage());
            }
            return resMap;
        } else {
            resMap.put("versionNameList", null);
            resMap.put("historyDetail", null);
            resMap.put("voucher", null);
            return resMap;
        }


    }


    @Override
    public ThirdPartyVoucherCommandLog redThirdPartVoucherRepayDetail(String tradeUuid, String versionName) {
        String filestart = savePath + File.separator + tradeUuid;
        String reddatail;
        String filePath = filestart + File.separator + versionName + ".txt";
        try {
            reddatail = FileUtils.readFileToString(new File(filePath), FilenameUtils.UTF_8);
        } catch (IOException e) {
            log.error("无法从配置路径中读取该版本的历史还款明细");
            throw new BusinessException("该版本历史还款信息读取错误：" + e.getMessage());
        }
        ThirdPartyVoucherCommandLog detailHistory = JsonUtils.parse(reddatail, ThirdPartyVoucherCommandLog.class);
        return detailHistory;
    }

    @Override
    public Map<String, Object> getRepayDetailAndVoucher(String tradeUuid, String versionName) {
        Map<String, Object> result = new HashMap<String, Object>();
        ThirdPartyVoucherCommandLog commandLog = redThirdPartVoucherRepayDetail(tradeUuid, versionName);
//		ThirdPartyVoucherCommandLog log =
//				thirdPartVoucherCommandLogService.getThirdPartyVoucherLogByTradeuuid(tradeUuid);
        ThirdPartVoucherDetail repayModel =
                JsonUtils.parse(commandLog.getThirdPartVoucherContent(), ThirdPartVoucherDetail.class);


        List<ThirdPartVoucherRepayDetail> repayDetailModelList = repayModel.getRepayDetailList();
        if (!CollectionUtils.isEmpty(repayDetailModelList)) {
            result.put("historydetail", repayDetailModelList.get(0));
        } else {
            result.put("historydetail", null);
        }

        result.put("voucher", commandLog);
        return result;
    }


    @Override
    public void saveHistoryLogAndUpdateLog(ThirdPartyVoucherCommandLog commandLog, String tradeUuid) {
        //取得历史还款明细并保存格式为 /配置路径/tradeuuid/版本 + time 的文件
        String detail = JsonUtils.toJSONString(commandLog);
        String filename = "版本" + DateUtils.getNowFullDateTime() + ".txt";
        try {
            FileUtils.write(new File(savePath + File.separator + tradeUuid + File.separator + filename), detail, FilenameUtils.UTF_8);
        } catch (IOException e) {
            log.error("无法保存该历史还款明细至配置路径中");
            throw new BusinessException("无法保存该历史还款明细至配置路径中：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> reLoadThirdPartVoucherCommandLog(String traduuid) {
        Map<String, Object> errmap = new HashMap<String, Object>();
        ThirdPartyVoucherCommandLog commandLog =
                thirdPartVoucherCommandLogService.getThirdPartyVoucherLogByTradeuuid(traduuid);
        if (null == commandLog) {
            errmap.put(traduuid, "err:未上传过，无法重新核销！");
        } else {
            //如果没有核销成功
            if (!VoucherLogIssueStatus.HAS_ISSUED.equals(commandLog.getVoucherLogIssueStatus())) {
                //重置第三方凭证中versionNo VoucherLogStatus RetiedTimes
                String versionno = UUIDUtil.randomUUID();
                commandLog.setRetriedTimes(0);
                commandLog.setVoucherLogStatus(null);
                commandLog.setVersionNo(versionno);
                ThirdPartVoucherDetail model = JsonUtils.parse(commandLog.getThirdPartVoucherContent(), ThirdPartVoucherDetail.class);
                model.setVersionNo(versionno);
                commandLog.setThirdPartVoucherContent(JsonUtils.toJSONString(model));
                commandLog.setVoucherLogIssueStatus(VoucherLogIssueStatus.NOT_ISSUED);
                commandLog.setExecutionStatus(ThirdPartyPayExecStatus.SUCCESS);
                try {
                    thirdPartVoucherCommandLogService.update(commandLog);
                } catch (Exception e) {
                    log.error("更新commandlog错误");
                    throw new GlobalRuntimeException("更新commandlog错误" + e.getMessage());
                }
            } else {
                //已核销成功
                errmap.put(traduuid, "err:该凭证已核销成功");
            }
        }
        return errmap;
    }

    /**
     * 封装请求参数Map
     * @param voucher
     * @return
     */
    private Map<String, String> getThirdPartVoucherDetail(ThirdPartVoucher voucher, FinancialContract financialContract, String versionNo) {
        Map<String,String> detailMap = new HashMap<>();
        detailMap.put("batchUuid", voucher.getBatchUuid());
        detailMap.put("requestNo", voucher.getRequestNo());
        List<ThirdPartVoucherDetail> detailModel = voucher.getDetailListParseJson();
        for (ThirdPartVoucherDetail detail : detailModel){

            detailMap.put("voucherNo", detail.getVoucherNo());
            detailMap.put("transactionRequestNo", detail.getTransactionRequestNo());
            detailMap.put("batchNo", detail.getBatchNo());
            detailMap.put("bankTransactionNo", detail.getBankTransactionNo());
            detailMap.put("contractUniqueId", detail.getContractUniqueId());
            detailMap.put("amount", String.valueOf(detail.getAmount()));
            detailMap.put("transactionTime", detail.getTransactionTime());
            detailMap.put("completeTime", detail.getCompleteTime());
            detailMap.put("transactionGateway", String.valueOf(detail.getTransactionGateway()));
            detailMap.put("currency", String.valueOf(detail.getCurrency()));
            detailMap.put("receivableAccountNo", detail.getReceivableAccountNo());
            detailMap.put("paymentBank", detail.getPaymentBank());
            detailMap.put("paymentName", detail.getPaymentName());
            detailMap.put("paymentAccountNo", detail.getPaymentAccountNo());
            detailMap.put("customerName", detail.getCustomerName());
            detailMap.put("customerIdNo", detail.getCustomerIdNo());
            detailMap.put("comment", detail.getComment());
            detailMap.put("createTime", String.valueOf(detail.getCreateTime()));

            PaymentInstitutionName gateWay = ThirdPartPayVoucherSpec.ThirdPartPayAPIGatewayTransferMap.get(detail.getTransactionGateway());
            //转换detailMap中机构为系统内标准机构编号
            detail.setTransactionGateway(gateWay.ordinal());
            detail.setVersionNo(versionNo);
            //detailMap.put("versionNo", versionNo);
            detailMap.put("repayDetailList", JSON.toJSONString(detail.getRepayDetailList()));

            detailMap.put("detail", JSON.toJSONString(detail, SerializerFeature.WriteDateUseDateFormat));

            List<ThirdPartVoucherRepayDetail> repayDetails = detail.getRepayDetailList();
            List<Object> currentPeriodList = new ArrayList<>();
            List<String> repayScheduleNoList = new ArrayList<>();
            List<String> repaymentPlanNoList = new ArrayList<>();
            List<String> repayScheduleMd5List = new ArrayList<>();
            for (ThirdPartVoucherRepayDetail repayDetail : repayDetails){
                if (repayDetail.getCurrentPeriod() != null)
                    currentPeriodList.add(repayDetail.getCurrentPeriod());
                else currentPeriodList.add("");
                if (StringUtils.isNotEmpty(repayDetail.getRepayScheduleNo()))
                    repayScheduleNoList.add(repayDetail.getRepayScheduleNo());
                else repayScheduleNoList.add("");
                if (StringUtils.isNotEmpty(repayDetail.getRepaymentPlanNo()))
                    repaymentPlanNoList.add(repayDetail.getRepaymentPlanNo());
                else repaymentPlanNoList.add("");
                String repayScheduleMd5 = repaymentPlanService.getRepayScheduleNoMD5(financialContract.getContractNo(), repayDetail.getRepayScheduleNo(), StringUtils.EMPTY);
                if (StringUtils.isNotEmpty(repayScheduleMd5))
                    repayScheduleMd5List.add(repayScheduleMd5);
                else repayScheduleMd5List.add("");
            }

            String currentPeriodListString = JsonUtils.toJsonString(currentPeriodList);
            String repayScheduleNoListString = JsonUtils.toJsonString(repayScheduleNoList);
            String repaymentPlanNoListString = JsonUtils.toJsonString(repaymentPlanNoList);
            String repayScheduleMd5ListString = JsonUtils.toJsonString(repayScheduleMd5List);
            detailMap.put("currentPeriodList", currentPeriodListString);
            detailMap.put("repayScheduleNoList", repayScheduleNoListString);
            detailMap.put("repaymentPlanNoList", repaymentPlanNoListString);
            detailMap.put("repayScheduleMd5List", repayScheduleMd5ListString);
        }
        return detailMap;
    }
}
