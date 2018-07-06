package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.report.exception.ExportException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.excel.CashFlowExcelVO;
import com.zufangbao.sun.yunxin.entity.model.BankReconciliationQueryModel;
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
public class ReportWrapper12 extends ReportBaseWrapper implements IReportWrapper<BankReconciliationQueryModel> {

	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Override
	public ExportEventLogModel wrap(BankReconciliationQueryModel paramsBean, HttpServletRequest request,
			HttpServletResponse response, ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {

		Map<String, Object> params = checkAndBuildParams(paramsBean);

		String sql = getCachedSql("reportWrapper12", params);
		exportEventLogModel.recordStartLoadDataTime();

		// 加载数据
		String fileName = "银行流水" + "_" + DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS");
		ZipOutputStream zip = openZipOutputStream(response, fileName + ".zip");
		PrintWriter printWriter = putNextZipEntry(zip, fileName);

		ExportableRowCallBackHandler<CashFlowExcelVO> callBack = new ExportableRowCallBackHandler<CashFlowExcelVO>(
				CashFlowExcelVO.class, printWriter, new ReportVOBuilder<CashFlowExcelVO>() {
					@Override
					public CashFlowExcelVO buildRow(ResultSet rs) throws SQLException {
						return buildCashFlowExcelVO(rs);
					}
				});

		genericDaoSupport.query(sql, params, callBack);
		// 写出报表
		closeZipOutputStream(zip, response);
		exportEventLogModel.recordAfterLoadDataComplete(callBack);

		return exportEventLogModel;
	}

	private CashFlowExcelVO buildCashFlowExcelVO(ResultSet rs) throws SQLException {
		CashFlowExcelVO cashFlowExcelVO = new CashFlowExcelVO();
		cashFlowExcelVO.setBankSequenceNo(rs.getString("bankSequenceNo"));
		cashFlowExcelVO
				.setAccountSide(EnumUtil.fromOrdinal(AccountSide.class, rs.getInt("accountSide")).getChineseMessage());
		cashFlowExcelVO.setTransactionAmount(rs.getString("transactionAmount"));
		cashFlowExcelVO.setBalance(rs.getString("balance"));
		cashFlowExcelVO.setCounterAccountNo(rs.getString("counterAccountNo"));
		cashFlowExcelVO.setCounterAccountName(rs.getString("counterAccountName"));
		cashFlowExcelVO.setTransactionTime(rs.getTimestamp("transactionTime") == null ? StringUtils.EMPTY
				: DateUtils.format(rs.getTimestamp("transactionTime"), DateUtils.LONG_DATE_FORMAT));
		cashFlowExcelVO.setRemark(rs.getString("remark"));

		return cashFlowExcelVO;
	}

	private Map<String, Object> checkAndBuildParams(BankReconciliationQueryModel paramsBean) throws ExportException {

		if (!paramsBean.valid_4export_csv()) {
			throw new ExportException(GlobalCodeSpec.CODE_FAILURE, paramsBean.getCheckErrorMsg());
		}

		Map<String, Object> params = new HashMap<String, Object>();
		if (null != paramsBean.getAccountSideEnum()) {
			params.put("accountSide", paramsBean.getAccountSide());
		}
		if (null != paramsBean.getAuditStatusEnum()) {
			params.put("auditStatus", paramsBean.getAuditStatus());

		}
		params.put("hostAccountNo", paramsBean.getHostAccountNo());
		params.put("bankSequenceNo", paramsBean.getCashFlowNo());
		params.put("counterAccountNo", paramsBean.getAccountNo());
		params.put("counterAccountName", paramsBean.getAccountName());
		if(StringUtils.isNotBlank(paramsBean.getTransactionRemark())) {
			params.put("remark", "%"+paramsBean.getTransactionRemark()+"%");
		}
		if (null != paramsBean.parseTradeStartTime()) {
			params.put("startTime", paramsBean.parseTradeStartTime());
		}
		if (null != paramsBean.parseTradeEndTime()) {
			params.put("endTime", DateUtils.addOneSecond(paramsBean.parseTradeEndTime()));
		}
		return params;
	}

}
