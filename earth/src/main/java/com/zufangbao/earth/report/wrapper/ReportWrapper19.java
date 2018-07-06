package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.report.exception.ExportException;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.audit.AuditJob;
import com.zufangbao.sun.yunxin.entity.audit.SystemBillType;
import com.zufangbao.sun.yunxin.entity.excel.BeneficiaryAuditResultOfCounter;
import com.zufangbao.sun.yunxin.entity.excel.BeneficiaryAuditResultOfIssued;
import com.zufangbao.sun.yunxin.entity.excel.BeneficiaryAuditResultOfLocal;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.sun.yunxin.service.audit.AuditJobService;
import com.zufangbao.wellsfargo.api.util.IpUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Component
@SuppressWarnings("rawtypes")
public class ReportWrapper19 extends ReportBaseWrapper implements IReportWrapper<HashMap> {

	@Autowired
	private AuditJobService auditJobService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Override
	public ExportEventLogModel wrap(HashMap paramsBean, HttpServletRequest request, HttpServletResponse response,
			ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {

		Map<String, Object> params = checkAndBuildParams(paramsBean);
		String auditJobUuid = (String) paramsBean.get("auditJobUuid");
		AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
		// Ajax 请求不提供下载
		if (StringUtils.isEmpty(request.getHeader("X-Requested-With"))) {
			String sql_local = getCachedSql("reportWrapper19_local", params);
			String sql_counter = getCachedSql("reportWrapper19_counter", params);
			String sql_issued = getCachedSql("reportWrapper19_issued", params);
			ZipOutputStream zip = openZipOutputStream(response, (String) params.get("fileName"));

			// 加载数据
			exportEventLogModel.recordStartLoadDataTime();
			PrintWriter printWriter = putNextZipEntry(zip, "代收本端多账数据");
			ExportableRowCallBackHandler<BeneficiaryAuditResultOfLocal> local = new ExportableRowCallBackHandler<BeneficiaryAuditResultOfLocal>(
					BeneficiaryAuditResultOfLocal.class, printWriter,
					new ReportVOBuilder<BeneficiaryAuditResultOfLocal>() {
						@Override
						public BeneficiaryAuditResultOfLocal buildRow(ResultSet rs) throws SQLException {
							return bulidBeneficiaryAuditResultOfLocal(rs);
						}
					});
			if(params.get("merchantNo") != null && params.get("paymentGateway") != null
					&& params.get("startTime") != null && params.get("endTime") != null){
				genericDaoSupport.query(sql_local, params, local);
			}

			printWriter = putNextZipEntry(zip, "代收对端多账数据");
			ExportableRowCallBackHandler<BeneficiaryAuditResultOfCounter> counter = new ExportableRowCallBackHandler<BeneficiaryAuditResultOfCounter>(
					BeneficiaryAuditResultOfCounter.class, printWriter, new ReportVOBuilder<BeneficiaryAuditResultOfCounter>() {
						@Override
						public BeneficiaryAuditResultOfCounter buildRow(ResultSet rs) throws SQLException {
							return bulidBeneficiaryAuditResultOfCounter(rs);
						}
					});
			if( params.get("paymentGateway") != null && params.get("merchantNo") != null
					&& params.get("startTime") != null && params.get("endTime") != null){
				genericDaoSupport.query(sql_counter, params, counter);
			}
			
			printWriter = putNextZipEntry(zip, "代收平账数据");
			ExportableRowCallBackHandler<BeneficiaryAuditResultOfIssued> issued = new ExportableRowCallBackHandler<BeneficiaryAuditResultOfIssued>(
					BeneficiaryAuditResultOfIssued.class, printWriter, new ReportVOBuilder<BeneficiaryAuditResultOfIssued>() {
						@Override
						public BeneficiaryAuditResultOfIssued buildRow(ResultSet rs) throws SQLException {
							return bulidBeneficiaryAuditResultOfIssued(rs);
						}
					});
			if(params.get("merchantNo") != null && params.get("paymentGateway") != null
					&& params.get("startTime") != null && params.get("endTime") != null){
				genericDaoSupport.query(sql_issued, params, issued);
			}
			
			exportEventLogModel.recordAfterLoadDataComplete(local.getResultSize() + counter.getResultSize() + issued.getResultSize());
			// 写出报表
			closeZipOutputStream(zip, response);

			SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.EXPORTAUDITRESULT,LogOperateType.EXPORT);
			log.setRecordContent("代收-第三方机构，任务编号："+auditJob.getAuditJobNo()+"，导出代收本端多账"+local.getResultSize()+"条记录、代收对端多账"+counter.getResultSize()+"条记录、代收平账数据"+issued.getResultSize()+"条记录。");
			systemOperateLogService.save(log);
		}

		return exportEventLogModel;
	}


	private BeneficiaryAuditResultOfLocal bulidBeneficiaryAuditResultOfLocal(ResultSet rs) throws SQLException {
		BeneficiaryAuditResultOfLocal vo = new BeneficiaryAuditResultOfLocal();
		
		vo.setSystemBillIdentity(rs.getString("systemBillIdentity"));
		SystemBillType systemBillType = SystemBillType.fromOrdinal(rs.getInt("systemBillType"));
		if (systemBillType != null) {
			vo.setSystemBillTypeName(systemBillType.getChineseName());
		}
		vo.setCounterPartyAccountName(rs.getString("counterPartyAccountName"));
		vo.setCounterPartyAccountNo(rs.getString("counterPartyAccountNo"));
		vo.setSystemBillPlanAmount(rs.getBigDecimal("systemBillPlanAmount"));
		vo.setCashFlowAccountSide("收入");
		vo.setCashFlowSettle("");
		vo.setTradeUuid(rs.getString("tradeUuid"));
		Date systemBillOccurDate = rs.getTimestamp("systemBillOccurDate");
		if (systemBillOccurDate != null) {
			vo.setSystemBillOccurDate(DateUtils.format(systemBillOccurDate, DateUtils.LONG_DATE_FORMAT));
			
		}
	
		DeductApplicationExecutionStatus transExcutionStatus = DeductApplicationExecutionStatus.fromOrdinal(rs.getInt("transExcutionStatus"));
		if (transExcutionStatus != null) {
			vo.setTransExcutionStatusName(transExcutionStatus.getChineseMessage());
		}
		
		return vo;
	}
	
	private BeneficiaryAuditResultOfCounter bulidBeneficiaryAuditResultOfCounter(ResultSet rs) throws SQLException {
		BeneficiaryAuditResultOfCounter vo = new BeneficiaryAuditResultOfCounter();
		
		vo.setCashFlowSequenceNo(rs.getString("cashFlowSequenceNo"));
		vo.setCashFlowFstMerchantNo(rs.getString("cashFlowFstMerchantNo"));
		vo.setCounterPartyAccountNo(rs.getString("counterPartyAccountNo") + "");
		vo.setCounterPartyAccountName(rs.getString("counterPartyAccountName"));
		vo.setCashFlowTransactionAmount(rs.getBigDecimal("cashFlowTransactionAmount"));
		vo.setCashFlowAccountSide(rs.getInt("cashFlowAccountSide"));
		vo.setTradeUuid(rs.getString("tradeUuid"));
		
		Date cashFlowSettleDate = rs.getTimestamp("cashFlowSettleDate");
		if (cashFlowSettleDate != null) {
			vo.setCashFlowSettleDate(DateUtils.format(cashFlowSettleDate, DateUtils.LONG_DATE_FORMAT));
			
		}
		vo.setBillStatus("");
		vo.setCashFlowRemark(rs.getString("cashFlowRemark"));
		
		return vo;
	}
	
	private BeneficiaryAuditResultOfIssued bulidBeneficiaryAuditResultOfIssued(ResultSet rs) throws SQLException {
		BeneficiaryAuditResultOfIssued vo = new BeneficiaryAuditResultOfIssued();
		
		vo.setSystemBillIdentity(rs.getString("systemBillIdentity"));
		SystemBillType systemBillType = SystemBillType.fromOrdinal(rs.getInt("systemBillType"));
		if (systemBillType != null) {
			vo.setSystemBillTypeName(systemBillType.getChineseName());
		}
		vo.setCounterPartyAccountName(rs.getString("counterPartyAccountName"));
		vo.setCounterPartyAccountNo(rs.getString("counterPartyAccountNo"));
		vo.setSystemBillPlanAmount(rs.getBigDecimal("systemBillPlanAmount"));
		vo.setCashFlowAccountSide("收入");
		vo.setTradeUuid(rs.getString("tradeUuid"));
		Date systemBillOccurDate = rs.getTimestamp("systemBillOccurDate");
		if (systemBillOccurDate != null) {
			vo.setSystemBillOccurDate(DateUtils.format(systemBillOccurDate, DateUtils.LONG_DATE_FORMAT));
			
		}
	
		DeductApplicationExecutionStatus transExcutionStatus = DeductApplicationExecutionStatus.fromOrdinal(rs.getInt("transExcutionStatus"));
		if (transExcutionStatus != null) {
			vo.setTransExcutionStatusName(transExcutionStatus.getChineseMessage());
		}
		
		return vo;
	}

	private Map<String, Object> checkAndBuildParams(HashMap paramsBean) throws ExportException {
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		String auditJobUuid = (String) paramsBean.get("auditJobUuid");
		AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
		if (auditJob != null&&auditJob.isVaild()) {
			params.put("paymentChannelUuid", auditJob.getPaymentChannelServiceUuid());
			params.put("startTime", auditJob.getStartTime());
			params.put("endTime", auditJob.getEndTime());
			params.put("paymentGateway", auditJob.getPaymentInstitution().ordinal());
			params.put("merchantNo", auditJob.getMerchantNo());
			params.put("pgClearingAccount", auditJob.getPgClearingAccount());
		}

		String fileName = getFileName();
		
		params.put("fileName", fileName);

		return params;
	}

	private String getFileName() {
		return String.format("对账结果_" + DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") + ".zip");
	}

}
