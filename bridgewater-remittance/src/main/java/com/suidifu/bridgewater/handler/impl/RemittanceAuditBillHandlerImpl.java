package com.suidifu.bridgewater.handler.impl;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.bridgewater.handler.RemittanceAuditBillHandler;
import com.suidifu.bridgewater.util.CSVUtils;
import com.suidifu.bridgewater.util.FileUtils;
import com.suidifu.bridgewater.util.ZipUtil;
import com.zufangbao.sun.entity.bank.Bank;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLog;
import com.zufangbao.sun.yunxin.service.BankService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanExecLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component("remittanceAuditBillHandler")
public class RemittanceAuditBillHandlerImpl implements RemittanceAuditBillHandler {

	private static final Log logger = LogFactory.getLog(RemittanceAuditBillHandlerImpl.class);

	@Value("#{config['remittance.auditbill.filePath']}")
	private String AUDIT_BILL_FILE_PATH = "";

	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private IRemittancePlanExecLogService remittancePlanExecLogService;
	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;
	@Autowired
	IRemittancePlanService iRemittancePlanService;
	@Autowired
	private BankService bankService;

	@Override
	public void generateAuditBill(String financialContractUuid, Date date) {

		String dateStr = DateUtils.format(date, "yyyy-MM-dd");
		Date startDate = DateUtils.parseDate(dateStr, "yyyy-MM-dd");
		Date endDate = DateUtils.addDays(startDate, 1);

		FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);

		if (null == financialContract) {
			logger.warn("RemittanceAuditBillHandler#generateAuditBill fail,financialContract is null!");
			return;
		}

		String filePath = AUDIT_BILL_FILE_PATH + financialContract.getContractNo() + "_" + dateStr + "/";

		if (!FileUtils.createDir(filePath)) {
			logger.warn("RemittanceAuditBillHandler#createDir fail!");
			return;
		}

		String fileNameStat = financialContract.getContractNo() + "_" + dateStr + "_" + "统计";
		String fileNameDetail = financialContract.getContractNo() + "_" + dateStr + "_" + "明细";

		List<RemittancePlanExecLog> remittancePlanExecLogs = remittancePlanExecLogService.getRemittacncePlanExecLogsBy(financialContractUuid, startDate, endDate);
		Map<String, Bank> bankMap = bankService.getCachedBanks();
		BigDecimal totalAmount = BigDecimal.ZERO;
		int totalCount = 0;

		List exportDataDetail = new ArrayList<Map>();
		for (RemittancePlanExecLog execLog : remittancePlanExecLogs) {

			if (ExecutionStatus.SUCCESS.equals(execLog.getExecutionStatus())) {

				RemittanceApplication remittanceApplication = iRemittanceApplicationService.getUniqueRemittanceApplicationByUuid(execLog
						.getRemittanceApplicationUuid());
				RemittancePlan remittancePlan = iRemittancePlanService.getUniqueRemittancePlanByUuid(execLog.getRemittancePlanUuid());

				Map row = new LinkedHashMap<String, String>();
				row.put("1", remittanceApplication.getRequestNo());
				row.put("2", remittancePlan.getBusinessRecordNo());
				row.put("3", execLog.getRemittanceApplicationUuid());
				row.put("4", DateUtils.format(execLog.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				row.put("5", "成功");
				row.put("6", "");
				row.put("7", execLog.getPlannedAmount());
				row.put("8", "");
				row.put("9", execLog.getCpBankCardNo());
				row.put("10", execLog.getCpBankAccountHolder());
				row.put("11", bankMap.get(execLog.getCpBankCode()).getBankName());
				row.put("12", execLog.getPgAccountNo());
				row.put("13", execLog.getExecRspNo());
				row.put("14", execLog.getTransactionSerialNo());
				row.put("15", "贷");
				row.put("16", DateUtils.format(execLog.getCompletePaymentDate(), "yyyy-MM-dd HH:mm:ss"));
				exportDataDetail.add(row);

				totalAmount = totalAmount.add(execLog.getPlannedAmount());
				totalCount++;
			}
		}

		LinkedHashMap datailHeader = new LinkedHashMap();
		datailHeader.put("1", "商户订单号");
		datailHeader.put("2", "商户明细号");
		datailHeader.put("3", "五维订单号");
		datailHeader.put("4", "交易发生时间");
		datailHeader.put("5", "订单状态");
		datailHeader.put("6", "冲账标记");
		datailHeader.put("7", "交易金额");
		datailHeader.put("8", "手续费");
		datailHeader.put("9", "收款人账号");
		datailHeader.put("10", "收款人姓名");
		datailHeader.put("11", "收款人开户行");
		datailHeader.put("12", "付款账号");
		datailHeader.put("13", "五维交易号");
		datailHeader.put("14", "银行流水号");
		datailHeader.put("15", "流水借贷标记");
		datailHeader.put("16", "银行流水入账时间");

		CSVUtils.createCSVFile(exportDataDetail, datailHeader, filePath, fileNameDetail);

		//生成统计csv
		List exportDataStat = new ArrayList<Map>();
		Map statRow = new LinkedHashMap<String, String>();

		statRow.put("1", totalCount);
		statRow.put("2", totalAmount);
		statRow.put("3", "0");
		statRow.put("4", dateStr);

		exportDataStat.add(statRow);

		LinkedHashMap statHeader = new LinkedHashMap();
		statHeader.put("1", "总笔数");
		statHeader.put("2", "总金额");
		statHeader.put("3", "总手续费");
		statHeader.put("4", "清算日期");

		CSVUtils.createCSVFile(exportDataStat, statHeader, filePath, fileNameStat);

		//压缩zip
		//String zipPath = AUDIT_BILL_FILE_PATH;
		//String zipFileName = financialContract.getContractNo() + "_" + dateStr + ".zip";

		String sourcePath = filePath;
		String zipPath = AUDIT_BILL_FILE_PATH + financialContract.getContractNo() + "_" + dateStr + ".zip";
		try {
			ZipUtil.createZip(sourcePath, zipPath);
			//ZipUtil.zip(filePath, zipPath, zipFileName);
		} catch (Exception e) {
			logger.warn("RemittanceAuditBillHandler#create zip file fail!");
			e.printStackTrace();
		}
	}

}
