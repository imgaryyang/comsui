package com.suidifu.dowjones.controller;

import com.suidifu.dowjones.service.ABS_FileService;
import com.suidifu.dowjones.service.BaiDu_RepaymentAndAssetFileService;
import com.suidifu.dowjones.service.CashFingerPrinterServiceV2;
import com.suidifu.dowjones.service.DailyActualRepaymentService;
import com.suidifu.dowjones.service.DailyFirstOverdueRateService;
import com.suidifu.dowjones.service.DailyGuaranteeService;
import com.suidifu.dowjones.service.DailyPlanRepaymentService;
import com.suidifu.dowjones.service.DailyRemittanceService;
import com.suidifu.dowjones.service.DailyRepurchaseService;
import com.suidifu.dowjones.service.DailyTaskConst;
import com.suidifu.dowjones.service.FileGenerationService;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DailyTask implements Runnable {

    private DailyActualRepaymentService dailyActualRepaymentService;
    private DailyRepurchaseService dailyRepurchaseService;
    private DailyGuaranteeService dailyGuaranteeService;
    private DailyPlanRepaymentService dailyPlanRepaymentService;
    private DailyRemittanceService dailyRemittanceService;
    private FileGenerationService fileGenerationService;
    private BaiDu_RepaymentAndAssetFileService baiDuRepaymentAndAssetFileService;
    private ABS_FileService absFileService;
    private DailyFirstOverdueRateService firstOverdueRateService;
    private CashFingerPrinterServiceV2 cashFingerPrinterServiceV2;

    private String financialContractUuid;

    private Date date;

    private String taskType;

    DailyTask(DailyActualRepaymentService dailyActualRepaymentService, DailyRepurchaseService dailyRepurchaseService,
        DailyGuaranteeService dailyGuaranteeService, DailyPlanRepaymentService dailyPlanRepaymentService,
        DailyRemittanceService dailyRemittanceService, FileGenerationService fileGenerationService,
        BaiDu_RepaymentAndAssetFileService baiDuRepaymentAndAssetFileService,ABS_FileService abs_fileService,
        DailyFirstOverdueRateService firstOverdueRateService,CashFingerPrinterServiceV2 cashFingerPrinterServiceV2,
        String financialContractUuid, Date date, String taskType) {
        this.dailyActualRepaymentService = dailyActualRepaymentService;
        this.dailyRepurchaseService = dailyRepurchaseService;
        this.dailyGuaranteeService = dailyGuaranteeService;
        this.dailyPlanRepaymentService = dailyPlanRepaymentService;
        this.dailyRemittanceService = dailyRemittanceService;
        this.fileGenerationService = fileGenerationService;
        this.financialContractUuid = financialContractUuid;
        this.baiDuRepaymentAndAssetFileService = baiDuRepaymentAndAssetFileService;
        this.absFileService = abs_fileService;
        this.firstOverdueRateService = firstOverdueRateService;
        this.cashFingerPrinterServiceV2 = cashFingerPrinterServiceV2;

        this.date = date;
        this.taskType = taskType;
    }

    @Override
    public void run() {

        if (needDo(DailyTaskConst.TASK_DAILY_REPURCHASE)) {
            try {
                log.info("Start to execute daily repurchase job, param {},{}", financialContractUuid, date);
                dailyRepurchaseService.executeDailyRepurchaseJob(financialContractUuid, date);
                log.info("End to execute daily repurchase job, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute daily repurchase job");
            }
        }
        if (needDo(DailyTaskConst.TASK_DAILY_GUARANTEE)) {
            try {
                log.info("Start to execute daily guarantee job, param {},{}", financialContractUuid, date);
                dailyGuaranteeService.executeDailyGuaranteeJob(financialContractUuid, date);
                log.info("End to execute daily guarantee job, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute daily guarantee job");
            }
        }
        if (needDo(DailyTaskConst.TASK_DAILY_PLAN_REPAYMENT)) {
            try {
                log.info("Start to execute daily plan repayment job, param {},{}", financialContractUuid, date);
                dailyPlanRepaymentService.executeDailyPlanRepaymentJob(financialContractUuid, date);
                log.info("End to execute daily plan repayment job, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute daily plan repayment job");
            }
        }
        if (needDo(DailyTaskConst.TASK_DAILY_ACTUAL_REPAYMENT)) {
            try {
                log.info("Start to execute daily actual repayment job, param {},{}", financialContractUuid, date);
                dailyActualRepaymentService.executeDailyActualRepaymentJob(financialContractUuid, date);
                log.info("End to execute daily actual repayment job, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute daily actual repayment job");
            }
        }
        if (needDo(DailyTaskConst.TASK_DAILY_REMITTANCE)) {
            try {
                log.info("Start to execute daily remittance job, param {},{}", financialContractUuid, date);
                dailyRemittanceService.doRemittanceData(financialContractUuid, date);
                log.info("End to execute daily remittance job, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute remittance job");
            }
        }
        if (needDo(DailyTaskConst.TASK_DAILY_FILE_GENERATION)) {
            try {
                log.info("Start to execute generate file report job, param {},{}", financialContractUuid, date);
                fileGenerationService.generateFileReport(financialContractUuid, date);
                log.info("End to execute generate file report, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute generate file report job");
            }
        }
        if (needDo(DailyTaskConst.TASK_BD_DAILY_REPAYMENT_AND_ASSET_FILE)) {
            try {
                log.info("Start to execute bd daily repayment and asset file report job, param {},{}", financialContractUuid, date);
                baiDuRepaymentAndAssetFileService.doFileData(financialContractUuid, date);
                log.info("End to execute bd daily repayment and asset file report, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute bd daily repayment and asset file report job");
            }
        }
        if (needDo(DailyTaskConst.TASK_BD_DAILY_INCREMENTAL_REPAYMENT_AND_ASSET_FILE)) {
            try {
                log.info("Start to execute bd daily incremental repayment and asset file report job, param {},{}", financialContractUuid, date);
                baiDuRepaymentAndAssetFileService.doIncrementalFileData(financialContractUuid, date);
                log.info("End to execute bd daily incremental repayment and asset file report, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute bd daily incremental repayment and asset file report job");
            }
        }
        if (needDo(DailyTaskConst.TASK_ABS_ACTUAL_REPAYMENT_FILE)) {
            try {
                log.info("Start to execute ABS_ACTUAL_REPAYMENT_FILE report job, param {},{}", financialContractUuid, date);
                absFileService.doActualRepaymentFileData(date);
                log.info("End to execute ABS_ACTUAL_REPAYMENT_FILE report, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute ABS_ACTUAL_REPAYMENT_FILE report job");
            }
        }
        if (needDo(DailyTaskConst.TASK_ABS_CONTRACT_CHANGE_FILE)) {
            try {
                log.info("Start to execute TASK_ABS_CONTRACT_CHANGE_FILE report job, param {},{}", financialContractUuid, date);
                absFileService.doContractChangeFileData(date);
                log.info("End to execute TASK_ABS_CONTRACT_CHANGE_FILE report, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute TASK_ABS_CONTRACT_CHANGE_FILE report job");
            }
        }
        if (needDo(DailyTaskConst.TASK_ABS_CURRENT_CONTRACT_DETAIL_FILE)) {
            try {
                log.info("Start to execute TASK_ABS_CURRENT_CONTRACT_DETAIL_FILE report job, param {},{}", financialContractUuid, date);
                absFileService.doCurrentContractDetailFileData(date);
                log.info("End to execute TASK_ABS_CURRENT_CONTRACT_DETAIL_FILE report, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute TASK_ABS_CURRENT_CONTRACT_DETAIL_FILE report job");
            }
        }
        if (needDo(DailyTaskConst.TASK_DAILY_FIRST_OVERDUE_RATE)) {
            try {
                log.info("Start to execute TASK_DAILY_FIRST_OVERDUE_RATE report job, param {},{}", financialContractUuid, date);
                firstOverdueRateService.doFile(financialContractUuid, date);
                log.info("End to execute TASK_DAILY_FIRST_OVERDUE_RATE report, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute TASK_DAILY_FIRST_OVERDUE_RATE report job");
            }
        }
        if (needDo(DailyTaskConst.TASK_CASH_FINGER_PRINTER_DEBIT_CASH_FLOW)) {
            try {
                log.info("Start to execute TASK_CASH_FINGER_PRINTER_DEBIT_CASH_FLOW report job, param {},{}", financialContractUuid, date);
                cashFingerPrinterServiceV2.doCashFingerPrinter_DebitCashFlow(financialContractUuid, date);
                log.info("End to execute TASK_CASH_FINGER_PRINTER_DEBIT_CASH_FLOW report, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute TASK_CASH_FINGER_PRINTER_DEBIT_CASH_FLOW report job");
            }
        }
        if (needDo(DailyTaskConst.TASK_CASH_FINGER_PRINTER_REPAYMENT_ORDER_SUMMARY)) {
            try {
                log.info("Start to execute TASK_CASH_FINGER_PRINTER_REPAYMENT_ORDER_SUMMARY report job, param {},{}", financialContractUuid, date);
                cashFingerPrinterServiceV2.doCashFingerPrinter_RepaymentOrderSummary(financialContractUuid, date);
                log.info("End to execute TASK_CASH_FINGER_PRINTER_REPAYMENT_ORDER_SUMMARY report, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute TASK_CASH_FINGER_PRINTER_REPAYMENT_ORDER_SUMMARY report job");
            }
        }
        if (needDo(DailyTaskConst.TASK_CASH_FINGER_PRINTER_ONLINE_PAYMENT_DETAILS_IN_TRANSIT)) {
            try {
                log.info("Start to execute TASK_CASH_FINGER_PRINTER_ONLINE_PAYMENT_DETAILS_IN_TRANSIT report job, param {},{}", financialContractUuid, date);
                cashFingerPrinterServiceV2.doCashFingerPrinter_OnlinePaymentDetailsInTransit(financialContractUuid, date);
                log.info("End to execute TASK_CASH_FINGER_PRINTER_ONLINE_PAYMENT_DETAILS_IN_TRANSIT report, param {},{}", financialContractUuid, date);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Failed to execute TASK_CASH_FINGER_PRINTER_ONLINE_PAYMENT_DETAILS_IN_TRANSIT report job");
            }
        }

    }

    private boolean needDo(int taskType) {
        try {
            return this.taskType.charAt(taskType) == '1';
        } catch (Exception e) {
            return false;
        }
    }
}
