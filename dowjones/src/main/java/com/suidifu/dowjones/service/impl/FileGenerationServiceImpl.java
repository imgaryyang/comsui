package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.model.PositionSheet;
import com.suidifu.dowjones.service.FileGenerationService;
import com.suidifu.dowjones.utils.*;
import com.suidifu.dowjones.vo.request.FileParameter;
import com.suidifu.dowjones.vo.request.FingerPrinterParameter;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FileGenerationServiceImpl implements FileGenerationService {

    @Autowired
    private GenericJdbcSupport genericJdbcSupport;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private HTTPCallBack httpCallBack;

    @Resource
    private transient FTPUtils ftpUtils;

    @Override
    public void generateFileReport(String financialContractUuid, Date doDate)
            throws IOException {

        if (StringUtils.isEmpty(financialContractUuid) || doDate == null) {
            return;
        }

//        String sql1 =
//                "select * from spark_schedule_job where taskId = 3 and financialContractUuid='" + financialContractUuid
//                        + "'";
//
//        List<FingerPrinterParameter> results = genericJdbcSupport.queryForList(sql1, FingerPrinterParameter.class);
//
//        if (CollectionUtils.isEmpty(results)) {
//            return;
//        }

//        FingerPrinterParameter fingerPrinterParameter = results.get(0);

        PositionSheet positionSheet = new PositionSheet();

        positionSheet.setDate(DateUtils.getDateFormatYYMMDD(doDate));

        // 放款数据
        String sql = "select sum(actual_count) AS count, sum(actual_amount) AS amount"
                + " from daily_remittance where financial_contract_uuid = '" + financialContractUuid + "'"
                + " and create_date = '" + DateUtils.getDateFormatYYMMDD(doDate) + "'"
                + " group by financial_contract_uuid, create_date";
        List<Map<String, Object>> remittanceResults = genericJdbcSupport.queryForList(sql);
        if (CollectionUtils.isNotEmpty(remittanceResults)) {
            positionSheet.setRemittanceCount(remittanceResults.get(0).get("count") == null ? null : remittanceResults.get(0).get("count").toString());
            positionSheet.setRemittanceAmount(remittanceResults.get(0).get("amount") == null ? null : remittanceResults.get(0).get("amount").toString());
        }

        // 线上还款
        sql = "select * from daily_actual_repayment where"
                + " financial_contract_uuid = '" + financialContractUuid + "'"
                + " and business_type=0 and create_date='" + DateUtils.getDateFormatYYMMDD(doDate) + "'";
        List<Map<String, Object>> onlineRepaymentResults = genericJdbcSupport.queryForList(sql);
        if (CollectionUtils.isNotEmpty(onlineRepaymentResults)) {
            int num = 1;
            for (Map<String, Object> item : onlineRepaymentResults) {
                positionSheet.fillPaymentChannelData(item, num);
                num++;
            }
        }

        log.info("Get online actual data end.");

        //线下还款
        sql = "select sum(count) as count,"
                + " sum(loan_asset_principal) as loan_asset_principal,"
                + " sum(loan_asset_interest) as loan_asset_interest,"
                + " sum(loan_service_fee) as loan_service_fee,"
                + " sum(loan_tech_fee) as loan_tech_fee,"
                + " sum(loan_other_fee) as loan_other_fee,"
                + " sum(overdue_fee_penalty) as overdue_fee_penalty,"
                + " sum(overdue_fee_obligation) as overdue_fee_obligation,"
                + " sum(overdue_fee_service) as overdue_fee_service,"
                + " sum(overdue_fee_other) as overdue_fee_other"
                + " from daily_actual_repayment where financial_contract_uuid = '" + financialContractUuid + "'"
                + " and business_type in (1,2) and create_date='" + DateUtils.getDateFormatYYMMDD(doDate) + "'"
                + " group by financial_contract_uuid , create_date";
        List<Map<String, Object>> offlineRepaymentResults = genericJdbcSupport.queryForList(sql);
        log.info("Get offline data end.");

        if (CollectionUtils.isNotEmpty(offlineRepaymentResults)) {
            Map<String, Object> item = offlineRepaymentResults.get(0);
            positionSheet.fillOfflineRepaymentData(item);
        }
        positionSheet.fillDeductData();

//        FileParameter fileParameter = new FileParameter();
//        fileParameter.setFilePath(fingerPrinterParameter.getPath());
//        fileParameter.setFileName(DateUtils.getDateFormatYYMMDD(doDate).replace("-", "")
//                + "." + Constants.CSV);
//
//        String fileName = fileParameter.getFileName();
        String header = "日期,资金追加,"
                + "当日放款笔数,当日放款金额,"
                + "当日扣款笔数,扣款金额,扣款本金,扣款利息,扣款服务费,扣款技术服务费,扣款其它费用,扣款罚息,扣款逾期服务费,扣款逾期其它费用,"
                + "线下还款笔数,线下还款金额,线下还款本金,线下还款利息,线下还款服务费,线下还款技术服务费,线下还款其它费用,线下还款罚息,线下还款逾期违约金,线下还款逾期服务费,线下还款逾期其它费用,"
                + "第一通道名称,第一通道笔数,第一通道金额,第一通道本金,第一通道利息,第一通道服务费,第一通道技术服务费,第一通道其它费用,第一通道罚息,第一通道逾期违约金,第一通道逾期服务费,第一通道逾期其它费用,"
                + "第二通道名称,第二通道笔数,第二通道金额,第二通道本金,第二通道利息,第二通道服务费,第二通道技术服务费,第二通道其它费用,第二通道罚息,第二通道逾期违约金,第二通道逾期服务费,第二通道逾期其它费用,"
                + "第三通道名称,第三通道笔数,第三通道金额,第三通道本金,第三通道利息,第三通道服务费,第三通道技术服务费,第三通道其它费用,第三通道罚息,第三通道逾期违约金,第三通道逾期服务费,第三通道逾期其它费用,"
                + "第四通道名称,第四通道笔数,第四通道金额,第四通道本金,第四通道利息,第四通道服务费,第四通道技术服务费,第四通道其它费用,第四通道罚息,第四通道逾期违约金,第四通道逾期服务费,第四通道逾期其它费用,"
                + "第五通道名称,第五通道笔数,第五通道金额,第五通道本金,第五通道利息,第五通道服务费,第五通道技术服务费,第五通道其它费用,第五通道罚息,第五通道逾期违约金,第五通道逾期服务费,第五通道逾期其它费用";

        String deleteSql = "delete from t_return_summary where create_date = '" + positionSheet.getDate()
            + "' and financial_contract_uuid = '" + financialContractUuid + "'";
        this.genericJdbcSupport.executeSQL(deleteSql, new HashMap<>(0));

        String insert_sql = "INSERT INTO t_return_summary (financial_contract_uuid, create_date, capital_addition, remittance_count, remittance_amount, deduct_count , deduct_amount, deduct_principal, deduct_interest, deduct_service_fee, deduct_tech_fee , deduct_other_fee, deduct_overdue_fee_penalty, deduct_overdue_fee_obligation, deduct_overdue_fee_service, deduct_overdue_fee_other , offline_repayment_count, offline_repayment_amount, offline_repayment_principal, offline_repayment_interest, offline_repayment_service_fee , offline_repayment_tech_fee, offline_repayment_other_fee, offline_repayment_overdue_fee_penalty, offline_repayment_overdue_fee_obligation, offline_repayment_overdue_fee_service , offline_repayment_overdue_fee_other, fst_payment_channel_name, fst_payment_channel_count, fst_payment_channel_amount, fst_payment_channel_principal , fst_payment_channel_interest, fst_payment_channel_service_fee, fst_payment_channel_tech_fee, fst_payment_channel_other_fee, fst_payment_channel_overdue_fee_penalty , fst_payment_channel_overdue_fee_obligation, fst_payment_channel_overdue_fee_service, fst_payment_channel_overdue_fee_other, snd_payment_channel_name, snd_payment_channel_count , snd_payment_channel_amount, snd_payment_channel_principal, snd_payment_channel_interest, snd_payment_channel_service_fee, snd_payment_channel_tech_fee , snd_payment_channel_other_fee, snd_payment_channel_overdue_fee_penalty, snd_payment_channel_overdue_fee_obligation, snd_payment_channel_overdue_fee_service, snd_payment_channel_overdue_fee_other , trd_payment_channel_name, trd_payment_channel_count, trd_payment_channel_amount, trd_payment_channel_principal, trd_payment_channel_interest , trd_payment_channel_service_fee, trd_payment_channel_tech_fee, trd_payment_channel_other_fee, trd_payment_channel_overdue_fee_penalty, trd_payment_channel_overdue_fee_obligation , trd_payment_channel_overdue_fee_service, trd_payment_channel_overdue_fee_other, fth_payment_channel_name, fth_payment_channel_count, fth_payment_channel_amount , fth_payment_channel_principal, fth_payment_channel_interest, fth_payment_channel_service_fee, fth_payment_channel_tech_fee, fth_payment_channel_other_fee , fth_payment_channel_overdue_fee_penalty, fth_payment_channel_overdue_fee_obligation, fth_payment_channel_overdue_fee_service, fth_payment_channel_overdue_fee_other, fifth_payment_channel_name , fifth_payment_channel_count, fifth_payment_channel_amount, fifth_payment_channel_principal, fifth_payment_channel_interest, fifth_payment_channel_service_fee , fifth_payment_channel_tech_fee, fifth_payment_channel_other_fee, fifth_payment_channel_overdue_fee_penalty, fifth_payment_channel_overdue_fee_obligation, fifth_payment_channel_overdue_fee_service , fifth_payment_channel_overdue_fee_other) VALUES (:financialContractUuid, :createDate,:capitalAddition,:remittanceCount,:remittanceAmount,:deductCount,:deductAmount,:deductPrincipal,:deductInterest,:deductServiceFee,:deductTechFee,:deductOtherFee,:deductOverdueFeePenalty,:deductOverdueFeeObligation,:deductOverdueFeeService,:deductOverdueFeeOther,:offlineRepaymentCount,:offlineRepaymentAmount,:offlineRepaymentPrincipal,:offlineRepaymentInterest,:offlineRepaymentServiceFee,:offlineRepaymentTechFee,:offlineRepaymentOtherFee,:offlineRepaymentOverdueFeePenalty,:offlineRepaymentOverdueFeeObligation,:offlineRepaymentOverdueFeeService,:offlineRepaymentOverdueFeeOther,:fstPaymentChannelName,:fstPaymentChannelCount,:fstPaymentChannelAmount,:fstPaymentChannelPrincipal,:fstPaymentChannelInterest,:fstPaymentChannelServiceFee,:fstPaymentChannelTechFee,:fstPaymentChannelOtherFee,:fstPaymentChannelOverdueFeePenalty,:fstPaymentChannelOverdueFeeObligation,:fstPaymentChannelOverdueFeeService,:fstPaymentChannelOverdueFeeOther,:sndPaymentChannelName,:sndPaymentChannelCount,:sndPaymentChannelAmount,:sndPaymentChannelPrincipal,:sndPaymentChannelInterest,:sndPaymentChannelServiceFee,:sndPaymentChannelTechFee,:sndPaymentChannelOtherFee,:sndPaymentChannelOverdueFeePenalty,:sndPaymentChannelOverdueFeeObligation,:sndPaymentChannelOverdueFeeService,:sndPaymentChannelOverdueFeeOther,:trdPaymentChannelName,:trdPaymentChannelCount,:trdPaymentChannelAmount,:trdPaymentChannelPrincipal,:trdPaymentChannelInterest,:trdPaymentChannelServiceFee,:trdPaymentChannelTechFee,:trdPaymentChannelOtherFee,:trdPaymentChannelOverdueFeePenalty,:trdPaymentChannelOverdueFeeObligation,:trdPaymentChannelOverdueFeeService,:trdPaymentChannelOverdueFeeOther,:fthPaymentChannelName,:fthPaymentChannelCount,:fthPaymentChannelAmount,:fthPaymentChannelPrincipal,:fthPaymentChannelInterest,:fthPaymentChannelServiceFee,:fthPaymentChannelTechFee,:fthPaymentChannelOtherFee,:fthPaymentChannelOverdueFeePenalty,:fthPaymentChannelOverdueFeeObligation,:fthPaymentChannelOverdueFeeService,:fthPaymentChannelOverdueFeeOther,:fifthPaymentChannelName,:fifthPaymentChannelCount,:fifthPaymentChannelAmount,:fifthPaymentChannelPrincipal,:fifthPaymentChannelInterest,:fifthPaymentChannelServiceFee,:fifthPaymentChannelTechFee,:fifthPaymentChannelOtherFee,:fifthPaymentChannelOverdueFeePenalty,:fifthPaymentChannelOverdueFeeObligation,:fifthPaymentChannelOverdueFeeService,:fifthPaymentChannelOverdueFeeOther)";

        Map<String, Object> params = new HashMap<>(90);
        params.put("financialContractUuid", financialContractUuid);
        params.put("createDate", positionSheet.getDate());
        params.put("capitalAddition", positionSheet.getCapitalAddition());
        params.put("remittanceCount", positionSheet.getRemittanceCount());
        params.put("remittanceAmount", positionSheet.getRemittanceAmount());
        params.put("deductCount", positionSheet.getDeductCount());
        params.put("deductAmount", positionSheet.getDeductAmount());
        params.put("deductPrincipal", positionSheet.getDeductPrincipal());
        params.put("deductInterest", positionSheet.getDeductInterest());
        params.put("deductServiceFee", positionSheet.getDeductServiceFee());
        params.put("deductTechFee", positionSheet.getDeductTechFee());
        params.put("deductOtherFee", positionSheet.getDeductOtherFee());
        params.put("deductOverdueFeePenalty", positionSheet.getDeductOverdueFeePenalty());
        params.put("deductOverdueFeeObligation", positionSheet.getDeductOverdueFeeObligation());
        params.put("deductOverdueFeeService", positionSheet.getDeductOverdueFeeService());
        params.put("deductOverdueFeeOther", positionSheet.getDeductOverdueFeeOther());
        params.put("offlineRepaymentCount", positionSheet.getOfflineRepaymentCount());
        params.put("offlineRepaymentAmount", positionSheet.getOfflineRepaymentAmount());
        params.put("offlineRepaymentPrincipal", positionSheet.getOfflineRepaymentPrincipal());
        params.put("offlineRepaymentInterest", positionSheet.getOfflineRepaymentInterest());
        params.put("offlineRepaymentServiceFee", positionSheet.getOfflineRepaymentServiceFee());
        params.put("offlineRepaymentTechFee", positionSheet.getOfflineRepaymentTechFee());
        params.put("offlineRepaymentOtherFee", positionSheet.getOfflineRepaymentOtherFee());
        params.put("offlineRepaymentOverdueFeePenalty", positionSheet.getOfflineRepaymentOverdueFeePenalty());
        params.put("offlineRepaymentOverdueFeeObligation", positionSheet.getOfflineRepaymentOverdueFeeObligation());
        params.put("offlineRepaymentOverdueFeeService", positionSheet.getOfflineRepaymentOverdueFeeService());
        params.put("offlineRepaymentOverdueFeeOther", positionSheet.getOfflineRepaymentOverdueFeeOther());
        params.put("fstPaymentChannelName", positionSheet.getFstPaymentChannelName());
        params.put("fstPaymentChannelCount", positionSheet.getFstPaymentChannelCount());
        params.put("fstPaymentChannelAmount", positionSheet.getFstPaymentChannelAmount());
        params.put("fstPaymentChannelPrincipal", positionSheet.getFstPaymentChannelPrincipal());
        params.put("fstPaymentChannelInterest", positionSheet.getFstPaymentChannelInterest());
        params.put("fstPaymentChannelServiceFee", positionSheet.getFstPaymentChannelServiceFee());
        params.put("fstPaymentChannelTechFee", positionSheet.getFstPaymentChannelTechFee());
        params.put("fstPaymentChannelOtherFee", positionSheet.getFstPaymentChannelOtherFee());
        params.put("fstPaymentChannelOverdueFeePenalty", positionSheet.getFstPaymentChannelOverdueFeePenalty());
        params.put("fstPaymentChannelOverdueFeeObligation", positionSheet.getFstPaymentChannelOverdueFeeObligation());
        params.put("fstPaymentChannelOverdueFeeService", positionSheet.getFstPaymentChannelOverdueFeeService());
        params.put("fstPaymentChannelOverdueFeeOther", positionSheet.getFstPaymentChannelOverdueFeeOther());
        params.put("sndPaymentChannelName", positionSheet.getSndPaymentChannelName());
        params.put("sndPaymentChannelCount", positionSheet.getSndPaymentChannelCount());
        params.put("sndPaymentChannelAmount", positionSheet.getSndPaymentChannelAmount());
        params.put("sndPaymentChannelPrincipal", positionSheet.getSndPaymentChannelPrincipal());
        params.put("sndPaymentChannelInterest", positionSheet.getSndPaymentChannelInterest());
        params.put("sndPaymentChannelServiceFee", positionSheet.getSndPaymentChannelServiceFee());
        params.put("sndPaymentChannelTechFee", positionSheet.getSndPaymentChannelTechFee());
        params.put("sndPaymentChannelOtherFee", positionSheet.getSndPaymentChannelOtherFee());
        params.put("sndPaymentChannelOverdueFeePenalty", positionSheet.getSndPaymentChannelOverdueFeePenalty());
        params.put("sndPaymentChannelOverdueFeeObligation", positionSheet.getSndPaymentChannelOverdueFeeObligation());
        params.put("sndPaymentChannelOverdueFeeService", positionSheet.getSndPaymentChannelOverdueFeeService());
        params.put("sndPaymentChannelOverdueFeeOther", positionSheet.getSndPaymentChannelOverdueFeeOther());
        params.put("trdPaymentChannelName", positionSheet.getTrdPaymentChannelName());
        params.put("trdPaymentChannelCount", positionSheet.getTrdPaymentChannelCount());
        params.put("trdPaymentChannelAmount", positionSheet.getTrdPaymentChannelAmount());
        params.put("trdPaymentChannelPrincipal", positionSheet.getTrdPaymentChannelPrincipal());
        params.put("trdPaymentChannelInterest", positionSheet.getTrdPaymentChannelInterest());
        params.put("trdPaymentChannelServiceFee", positionSheet.getTrdPaymentChannelServiceFee());
        params.put("trdPaymentChannelTechFee", positionSheet.getTrdPaymentChannelTechFee());
        params.put("trdPaymentChannelOtherFee", positionSheet.getTrdPaymentChannelOtherFee());
        params.put("trdPaymentChannelOverdueFeePenalty", positionSheet.getTrdPaymentChannelOverdueFeePenalty());
        params.put("trdPaymentChannelOverdueFeeObligation", positionSheet.getTrdPaymentChannelOverdueFeeObligation());
        params.put("trdPaymentChannelOverdueFeeService", positionSheet.getTrdPaymentChannelOverdueFeeService());
        params.put("trdPaymentChannelOverdueFeeOther", positionSheet.getTrdPaymentChannelOverdueFeeOther());
        params.put("fthPaymentChannelName", positionSheet.getFthPaymentChannelName());
        params.put("fthPaymentChannelCount", positionSheet.getFthPaymentChannelCount());
        params.put("fthPaymentChannelAmount", positionSheet.getFthPaymentChannelAmount());
        params.put("fthPaymentChannelPrincipal", positionSheet.getFthPaymentChannelPrincipal());
        params.put("fthPaymentChannelInterest", positionSheet.getFthPaymentChannelInterest());
        params.put("fthPaymentChannelServiceFee", positionSheet.getFthPaymentChannelServiceFee());
        params.put("fthPaymentChannelTechFee", positionSheet.getFthPaymentChannelTechFee());
        params.put("fthPaymentChannelOtherFee", positionSheet.getFthPaymentChannelOtherFee());
        params.put("fthPaymentChannelOverdueFeePenalty", positionSheet.getFthPaymentChannelOverdueFeePenalty());
        params.put("fthPaymentChannelOverdueFeeObligation", positionSheet.getFthPaymentChannelOverdueFeeObligation());
        params.put("fthPaymentChannelOverdueFeeService", positionSheet.getFthPaymentChannelOverdueFeeService());
        params.put("fthPaymentChannelOverdueFeeOther", positionSheet.getFthPaymentChannelOverdueFeeOther());
        params.put("fifthPaymentChannelName", positionSheet.getFifthPaymentChannelName());
        params.put("fifthPaymentChannelCount", positionSheet.getFifthPaymentChannelCount());
        params.put("fifthPaymentChannelAmount", positionSheet.getFifthPaymentChannelAmount());
        params.put("fifthPaymentChannelPrincipal", positionSheet.getFifthPaymentChannelPrincipal());
        params.put("fifthPaymentChannelInterest", positionSheet.getFifthPaymentChannelInterest());
        params.put("fifthPaymentChannelServiceFee", positionSheet.getFifthPaymentChannelServiceFee());
        params.put("fifthPaymentChannelTechFee", positionSheet.getFifthPaymentChannelTechFee());
        params.put("fifthPaymentChannelOtherFee", positionSheet.getFifthPaymentChannelOtherFee());
        params.put("fifthPaymentChannelOverdueFeePenalty", positionSheet.getFifthPaymentChannelOverdueFeePenalty());
        params.put("fifthPaymentChannelOverdueFeeObligation", positionSheet.getFifthPaymentChannelOverdueFeeObligation());
        params.put("fifthPaymentChannelOverdueFeeService", positionSheet.getFifthPaymentChannelOverdueFeeService());
        params.put("fifthPaymentChannelOverdueFeeOther", positionSheet.getFifthPaymentChannelOverdueFeeOther());

        this.genericJdbcSupport.executeSQL(insert_sql, params);

//        String fullPath = fileUtils.saveWithHeader(header, Collections.singletonList(positionSheet), fileParameter);
//        log.info("Save file{} end. ", fullPath);
//
//        String uploadPath = Constants.FTP_PATH + fingerPrinterParameter.getDataStreamUuid() + "/";
//
//        log.info("Ready to upload file {} , upload path {}", fileName, uploadPath);
//        ftpUtils.uploadFile(new File(fullPath), uploadPath);
//
//        log.info("Ready to post info {} ", fileName);
//
//        httpCallBack.post(Collections.singletonList(fileName),
//                fingerPrinterParameter.getDataStreamUuid());

    }
}