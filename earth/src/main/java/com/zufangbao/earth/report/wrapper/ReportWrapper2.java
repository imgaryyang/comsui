package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.DateUtils;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.sun.entity.financial.LoanBatch;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentPlanExcelVO;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.LoanBatchService;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.api.util.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.ZipOutputStream;

@Component
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReportWrapper2 extends ReportBaseWrapper implements IReportWrapper<HashMap> {

	@Autowired
	private LoanBatchService loanBatchService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Override
	public ExportEventLogModel wrap(HashMap paramsBean, HttpServletRequest request, HttpServletResponse response,
			ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {

		String sql = getCachedSql("reportWrapper2", paramsBean);
		Long loanBatchId = Long.valueOf((String) paramsBean.get("loanBatchId"));

		exportEventLogModel.recordStartLoadDataTime();
		LoanBatch loanBatch = loanBatchService.load(LoanBatch.class, loanBatchId);
		String fileName = getFileName(DateUtils.asDay(new Date()), loanBatch.getCode());
		ZipOutputStream zip = openZipOutputStream(response, fileName);
		PrintWriter printWriter;

		// 加载数据
		String sql2 = getCachedSql("reportWrapper2_sub", Collections.emptyMap());
		List<Map<String, Object>> contractInfos = genericDaoSupport.queryForList(sql2, "loanBatchId", loanBatchId);
		
		List<Integer> loadDataSizes = new ArrayList<Integer>();
		for (Map<String, Object> contractInfo : contractInfos) {
			printWriter = putNextZipEntry(zip, (String) contractInfo.get("contractNo"));
			ExportableRowCallBackHandler<RepaymentPlanExcelVO> callBack = new ExportableRowCallBackHandler<RepaymentPlanExcelVO>(
					RepaymentPlanExcelVO.class, printWriter, new ReportVOBuilder<RepaymentPlanExcelVO>() {
						@Override
						public RepaymentPlanExcelVO buildRow(ResultSet rs) throws SQLException {
							return buildRepaymentPlanExcelVO(rs, (String) contractInfo.get("contractUniqueId"));
						}
					});
			Long contractId = (Long) contractInfo.get("id");
			genericDaoSupport.query(sql, "contractId", contractId, callBack);
			loadDataSizes.add(callBack.getResultSize());
		}
		
		// 写出报表
		closeZipOutputStream(zip, response);

		exportEventLogModel.recordAfterLoadDataComplete();
		exportEventLogModel.setLoadDataSizes(loadDataSizes.toArray(new Integer[loadDataSizes.size()]));

		SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.EXPORTREPAYEMNTPLAN,LogOperateType.EXPORT);
		log.setRecordContent("导出还款计划，导入批号为【"+loanBatch.getCode()+"】");
		systemOperateLogService.save(log);

		return exportEventLogModel;
	}

	private RepaymentPlanExcelVO buildRepaymentPlanExcelVO(ResultSet rs, String uniqueId) throws SQLException {
		RepaymentPlanExcelVO repaymentPlanExcelVO = new RepaymentPlanExcelVO();
		repaymentPlanExcelVO.setUniqueId(uniqueId);
		repaymentPlanExcelVO.setRepaymentDate((rs.getString("assetRecycleDate")));
		repaymentPlanExcelVO.setRepatriatedEarnings(getNumString(rs.getBigDecimal("assetInterestValue")));
		repaymentPlanExcelVO.setPrincipal(getNumString(rs.getBigDecimal("assetPrincipalValue")));
		return repaymentPlanExcelVO;
	}
	
	private String getNumString(BigDecimal bigDecimal) {
		return (bigDecimal == null ? BigDecimal.ZERO : bigDecimal).toString();
	}

	private String getFileName(Date create_date, String code ) {
		return String.format("%s批次详细还款计划%s.zip", code, DateUtils.format(create_date, "yyyy_MM_dd"));
	}
	
}
