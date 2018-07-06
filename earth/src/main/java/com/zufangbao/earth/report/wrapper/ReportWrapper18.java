package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.deduct.DeductApplicationQeuryModel;
import com.zufangbao.sun.yunxin.entity.excel.DeductApplicationExcelVO;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.api.util.IpUtils;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

@Component
public class ReportWrapper18 extends ReportBaseWrapper implements IReportWrapper<DeductApplicationQeuryModel>{
	
	@Autowired
	private GenericDaoSupport genericDaoSupport;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Override
	public ExportEventLogModel wrap(DeductApplicationQeuryModel queryModel,
			HttpServletRequest request, HttpServletResponse response,
			ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {
		
		if (CollectionUtils.isEmpty(queryModel.getFinancialContractIdList())) {
			return exportEventLogModel;
		}
		if (CollectionUtils.isEmpty(queryModel.getExecutionStatusEnumList())) {
			return exportEventLogModel;
		}
		if (CollectionUtils.isEmpty(queryModel.getRepaymentTypeList())) {
			return exportEventLogModel;
		}

		Map<String, Object> params = buildParams(queryModel);
		String sql = getCachedSql("reportWrapper18", params);
		
		exportEventLogModel.recordStartLoadDataTime();

		String fileName = String.format("扣款订单表_%s.zip", DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS"));
		ZipOutputStream zip = openZipOutputStream(response, fileName);
		PrintWriter printWriter = putNextZipEntry(zip, "扣款订单表");
		
		ExportableRowCallBackHandler<DeductApplicationExcelVO> callBack = new ExportableRowCallBackHandler<DeductApplicationExcelVO>(DeductApplicationExcelVO.class, printWriter, new ReportVOBuilder<DeductApplicationExcelVO>() {
			@Override
			public DeductApplicationExcelVO buildRow(ResultSet rs) throws SQLException {
				return buildLoanContractDetailCheckExcelVO(rs);
			}
		});
		
		genericDaoSupport.query(sql, params, callBack);
		
		closeZipOutputStream(zip, response);
		
		exportEventLogModel.recordAfterLoadDataComplete(callBack);
		
		StringBuffer selectString = extractSelectString(queryModel);
		SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.EXPORTDEDUCTAPPLICATION,LogOperateType.EXPORT);
		log.setRecordContent("导出扣款订单列表，导出记录"+callBack.getResultSize()+"条。"+"筛选条件："+selectString);
		systemOperateLogService.save(log);

		return exportEventLogModel;
	}
	
	private StringBuffer extractSelectString(DeductApplicationQeuryModel queryModel) {
		StringBuffer selectString = financialContractService.selectFinancialContract(queryModel.getFinancialContractIdList());
		List<RepaymentType> repaymentTypes = queryModel.getRepaymentTypeList();
		if (CollectionUtils.isNotEmpty(repaymentTypes)) {
			selectString.append("，订单类型");
			for (RepaymentType repaymentType : repaymentTypes) {
				selectString.append("【" + repaymentType.getChineseMessage() + "】");
			}
		}
		List<DeductApplicationExecutionStatus> executionStatus = queryModel.getExecutionStatusEnumList();
		if (CollectionUtils.isNotEmpty(executionStatus)) {
			selectString.append("，订单状态");
			for (DeductApplicationExecutionStatus status : executionStatus) {
				selectString.append("【" + status.getChineseMessage() + "】");
			}
		}
		if (StringUtils.isNotEmpty(queryModel.getCreateStartDate())) {
			selectString.append("，创建起始时间【" + queryModel.getCreateStartDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getCreateEndDate())) {
			selectString.append("，创建截止时间【" + queryModel.getCreateEndDate() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getCustomerName())) {
			selectString.append("，客户姓名【" + queryModel.getCustomerName() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getLoanContractNo())) {
			selectString.append("，贷款合同编号【" + queryModel.getLoanContractNo() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getDeudctNo())) {
			selectString.append("，订单编号【" + queryModel.getDeudctNo() + "】");
		}
		return selectString;
	}


	private Map<String, Object> buildParams(DeductApplicationQeuryModel queryModel) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("financialContractIdList", queryModel.getFinancialContractIdList());
		
		List<Integer> repaymentTypeList = queryModel.getRepaymentTypeList().stream().map(s -> s.ordinal()).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(repaymentTypeList)) {
			paramMap.put("repaymentTypeList", repaymentTypeList);
		}
		List<Integer> executionStatusList = queryModel.getExecutionStatusEnumList().stream().map(s -> s.ordinal()).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(executionStatusList)) {
			paramMap.put("executionStatusList", executionStatusList);
		}
		if(queryModel.getCreateStartDateValue() != null){
			paramMap.put("startDate", queryModel.getCreateStartDateValue());
		}
		if(queryModel.getCreateEndDateValue() != null){
			paramMap.put("endDate", queryModel.getCreateEndDateValue());
		}
		if(StringUtils.isNotBlank(queryModel.getCustomerName())){
			paramMap.put("customerName", "%" + queryModel.getCustomerName() + "%");
		}
		if(StringUtils.isNotBlank(queryModel.getLoanContractNo())){
			paramMap.put("loanContractNo", "%" + queryModel.getLoanContractNo() + "%");
		}
		if(StringUtils.isNotBlank(queryModel.getDeudctNo())){
			paramMap.put("deductId", "%" + queryModel.getDeudctNo() + "%");
		}
		return paramMap;
	}
	
	private DeductApplicationExcelVO buildLoanContractDetailCheckExcelVO(ResultSet rs) throws SQLException {
		DeductApplicationExcelVO vo = new DeductApplicationExcelVO();
		vo.setDeudctNo(rs.getString("deductId"));
		vo.setLoanContractNo(rs.getString("contractNo"));
		vo.setCustomerName(rs.getString("customerName"));
		vo.setCreateTime(rs.getTimestamp("createTime"));
		
		int repaymentTypeInt = rs.getInt("repaymentType");
		RepaymentType repaymentType = EnumUtil.fromOrdinal(RepaymentType.class, repaymentTypeInt);
		if(repaymentType != null) {
			vo.setRepaymentType(repaymentType.getChineseMessage());
		}
		
		vo.setPlanDeductAmount(rs.getBigDecimal("plannedDeductTotalAmount"));
		
		vo.setStatusModifyTime(rs.getTimestamp("lastModifiedTime"));
		
		int executionStatusInt = rs.getInt("executionStatus");
		DeductApplicationExecutionStatus deductStatus = EnumUtil.fromOrdinal(DeductApplicationExecutionStatus.class, executionStatusInt);
		if(deductStatus != null) {
			vo.setDeductStatus(deductStatus.getChineseMessage());
		}
		
		vo.setRemark(rs.getString("executionRemark"));
		
		return vo;
	}
}
