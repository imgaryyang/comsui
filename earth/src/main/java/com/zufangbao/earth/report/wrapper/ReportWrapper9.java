package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.report.exception.ExportException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.remittance.CashFlowExportModel;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLogExportModel;
import com.zufangbao.sun.yunxin.entity.remittance.ReverseStatus;
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
public class ReportWrapper9 extends ReportBaseWrapper implements IReportWrapper<HashMap> {

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Override
	public ExportEventLogModel wrap(HashMap paramsBean, HttpServletRequest request, HttpServletResponse response,
			ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {

		Map<String, Object> params = checkAndBuildParams(paramsBean);
		// Ajax 请求不提供下载
		if (StringUtils.isEmpty(request.getHeader("X-Requested-With"))) {
			String sql_rel = getCachedSql("reportWrapper9_rel", params);
			String sql_rcf = getCachedSql("reportWrapper9_rcf", params);
			ZipOutputStream zip = openZipOutputStream(response, (String) params.get("fileName"));

			// 加载数据
			exportEventLogModel.recordStartLoadDataTime();
			PrintWriter printWriter = putNextZipEntry(zip, "线上代付单");
			ExportableRowCallBackHandler<RemittancePlanExecLogExportModel> callBack_rel = new ExportableRowCallBackHandler<RemittancePlanExecLogExportModel>(
					RemittancePlanExecLogExportModel.class, printWriter,
					new ReportVOBuilder<RemittancePlanExecLogExportModel>() {
						@Override
						public RemittancePlanExecLogExportModel buildRow(ResultSet rs) throws SQLException {
							return bulidRemittancePlanExecLogExportModel(rs);
						}
					});
			genericDaoSupport.query(sql_rel, params, callBack_rel);

			printWriter = putNextZipEntry(zip, "通道流水");
			ExportableRowCallBackHandler<CashFlowExportModel> callBack_rcf = new ExportableRowCallBackHandler<CashFlowExportModel>(
					CashFlowExportModel.class, printWriter, new ReportVOBuilder<CashFlowExportModel>() {
						@Override
						public CashFlowExportModel buildRow(ResultSet rs) throws SQLException {
							return bulidCashFlowExportModel(rs);
						}
					});
			genericDaoSupport.query(sql_rcf, params, callBack_rcf);
			
			exportEventLogModel.recordAfterLoadDataComplete(callBack_rel.getResultSize() + callBack_rcf.getResultSize());
			// 写出报表
			closeZipOutputStream(zip, response);
		}

		return exportEventLogModel;
	}

	private CashFlowExportModel bulidCashFlowExportModel(ResultSet rs) throws SQLException {
		CashFlowExportModel model = new CashFlowExportModel();
		model.setCashFlowUuid(rs.getString("cashFlowUuid"));
		model.setCounterAccountName(rs.getString("counterAccountName"));
		model.setCounterAccountNo(rs.getString("counterAccountNo"));
		model.setTransactionAmount(rs.getBigDecimal("transactionAmount"));
		Date transactionTime = rs.getTimestamp("transactionTime");
		if (transactionTime != null) {
			model.setTransactionTime(DateUtils.format(transactionTime, DateUtils.LONG_DATE_FORMAT));
		}
		model.setTransactionVoucherNo(rs.getString("tradeUuid"));
		model.setRemark(rs.getString("remark"));
		model.setAccountSideUse(EnumUtil.fromOrdinal(AccountSide.class, rs.getInt("accountSide")));

		return model;
	}

	private RemittancePlanExecLogExportModel bulidRemittancePlanExecLogExportModel(ResultSet rs) throws SQLException {
		RemittancePlanExecLogExportModel vo = new RemittancePlanExecLogExportModel();
		vo.setExecReqNo(rs.getString("execReqNo"));
		vo.setExecRspNo(rs.getString("execRspNo"));
		vo.setPlannedAmount(rs.getBigDecimal("plannedAmount"));
		Date lastModifiedTime = rs.getTimestamp("lastModifiedTime");
		Date createTime = rs.getTimestamp("createTime");
		if(lastModifiedTime != null) {
			vo.setLastModifiedTime(DateUtils.format(lastModifiedTime, DateUtils.LONG_DATE_FORMAT));
			if(createTime != null){
				vo.setCreateTime(DateUtils.format(createTime, DateUtils.LONG_DATE_FORMAT));
				vo.setOffsetDay(DateUtils.compareTwoDatesOnDay(lastModifiedTime,createTime)+"");
			}
		}

		vo.setCpBankCardNo(rs.getString("cpBankCardNo"));
		vo.setCpIdNumber(rs.getString("cpIdNumber"));

		vo.setReverseStatusUseEnum(EnumUtil.fromOrdinal(ReverseStatus.class, rs.getInt("reverseStatus")));
		vo.setExecutionStatusUseEnum(EnumUtil.fromOrdinal(ExecutionStatus.class, rs.getInt("executionStatus")));
		return vo;
	}

	private Map<String, Object> checkAndBuildParams(HashMap paramsBean) throws ExportException {

		String startDate = (String) paramsBean.get("startDate");
		String endDate = (String) paramsBean.get("endDate");
		Date fromDate = (null == startDate) ? null : DateUtils.parseDate(startDate, "yyyy-MM-dd HH:mm:ss");
		Date toDate = (null == endDate) ? null : DateUtils.parseDate(endDate, "yyyy-MM-dd HH:mm:ss");
		toDate = (toDate == null) ? new Date() : toDate;
		String msg = checkDate(fromDate, toDate);
		if (!StringUtils.isEmpty(msg)) {
			throw new ExportException(GlobalCodeSpec.CODE_FAILURE, msg);
		}

		String financialContractUuid = (String) paramsBean.get("financialContractUuid");
		FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
		if (financialContract == null) {
			throw new ExportException(GlobalCodeSpec.CODE_FAILURE, "筛选条件有误，请重新选择。");
		}

		String hostAccountNo = financialContract.getCapitalAccount().getAccountNo();
		if(StringUtils.isEmpty(hostAccountNo)) {
			throw new ExportException(GlobalCodeSpec.CODE_FAILURE, "筛选条件有误，请重新选择。");
		}
		
		String fileName = getFileName(fromDate, toDate, financialContract.getContractName());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("financialContractUuid", financialContractUuid);
		params.put("fromDate", fromDate);
		params.put("toDate", toDate);
		params.put("hostAccountNo", hostAccountNo);
		params.put("fileName", fileName);

		return params;
	}

	private String checkDate(Date begin, Date end) {
		if (begin == null) {
			return "筛选条件有误，请重新选择。";
		}
		long diff = end.getTime() - begin.getTime();
		if (diff < 0 || diff > 7 * 24 * 1000 * 60 * 60) { // 时间间隔为负OR超过7天
			return "筛选条件有误，请重新选择。";
		}
		return "";
	}

	private String getFileName(Date fromDate, Date toDate, String contractName) {
		return String.format("%s_%s_%s 放款流水对账.zip", DateUtils.format(fromDate, "yyyyMMddHHmmss"),
				DateUtils.format(toDate, "yyyyMMddHHmmss"), contractName);

	}

}
