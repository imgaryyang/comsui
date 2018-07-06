package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.AppService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.AgeUtil;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.*;
import com.zufangbao.sun.yunxin.entity.excel.OverDueRepaymentDetailExcelVO;
import com.zufangbao.sun.yunxin.entity.model.repaymentPlan.OverdueAssetQueryModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.api.util.IpUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

@Component
public class ReportWrapper4 extends ReportBaseWrapper implements IReportWrapper<OverdueAssetQueryModel> {

	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Autowired
	private AppService appService;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Override
	public ExportEventLogModel wrap(OverdueAssetQueryModel paramsBean, HttpServletRequest request,
			HttpServletResponse response, ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {

		Map<String, Object> params = buildParams(paramsBean);

		String sql = getCachedSql("reportWrapper4", params);
		exportEventLogModel.recordStartLoadDataTime();

		// 加载数据
		String fileName = "逾期还款明细表" + "_" + DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") + ".zip";
		
		Map<Long, App> appMap = getAppMap();
		ZipOutputStream zip = openZipOutputStream(response, fileName);
		PrintWriter printWriter = putNextZipEntry(zip, "逾期还款明细表");

		ExportableRowCallBackHandler<OverDueRepaymentDetailExcelVO> callBack = new ExportableRowCallBackHandler<OverDueRepaymentDetailExcelVO>(
				OverDueRepaymentDetailExcelVO.class, printWriter, new ReportVOBuilder<OverDueRepaymentDetailExcelVO>() {
					@Override
					public OverDueRepaymentDetailExcelVO buildRow(ResultSet rs) throws SQLException {
						return buildOverDueRepaymentDetailExcelVO(rs, appMap);
					}
				});

		// 查询还款计划表
		genericDaoSupport.query(sql, params, callBack);

        // 查询还款计划历史表
        genericDaoSupport.query(sql.replace("FROM asset_set ", "FROM asset_set_history "), params, callBack);

		// 写出报表
		closeZipOutputStream(zip, response);
		exportEventLogModel.recordAfterLoadDataComplete(callBack);

		StringBuffer selectString = extractSelectString(paramsBean);
		SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.EXPORTREPAYEMNTPLAN,LogOperateType.EXPORT);
		log.setRecordContent("导出逾期还款管理表，导出记录"+callBack.getResultSize()+"条。"+"筛选条件："+selectString);
		systemOperateLogService.save(log);

		return exportEventLogModel;
	}

	private StringBuffer extractSelectString(OverdueAssetQueryModel queryModel) {
		StringBuffer selectString = financialContractService.selectFinancialContract(queryModel.getFinancialContractUuidList());
		if (StringUtils.isNotEmpty(queryModel.getPlanStartDate())) {
			selectString.append("，计划还款起始日【" + queryModel.getPlanStartDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getPlanEndDate())) {
			selectString.append("，计划还款终止日【" + queryModel.getPlanEndDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getActualStartDate())) {
			selectString.append("，实际还款起始日【" + queryModel.getActualStartDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getActualEndDate())) {
			selectString.append("，实际还款终止日【" + queryModel.getActualEndDate() + "】");
		}
		List<RepaymentExecutionState> repaymentExecutionStates = queryModel.getRepaymentExecutionStateList();
		if (CollectionUtils.isNotEmpty(repaymentExecutionStates)) {
			selectString.append("，还款状态");
			for (RepaymentExecutionState repaymentExecutionState : repaymentExecutionStates) {
				selectString.append("【" + repaymentExecutionState.getChineseMessage() + "】");
			}
		}
		List<AuditOverdueStatus> AuditOverdueStatusList = queryModel.getAuditOverDueStatusEnumList();
		if (CollectionUtils.isNotEmpty(AuditOverdueStatusList)) {
			selectString.append("，逾期状态");
			for (AuditOverdueStatus auditOverdueStatus : AuditOverdueStatusList) {
				selectString.append("【" + auditOverdueStatus.getChineseMessage() + "】");
			}
		}
		if (StringUtils.isNotBlank(queryModel.getRepaymentNo())) {
			selectString.append("，还款编号【" + queryModel.getRepaymentNo() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getContractNo())) {
			selectString.append("，贷款合同编号【" + queryModel.getContractNo() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getCustomerName())) {
			selectString.append("，客户姓名【" + queryModel.getCustomerName() + "】");
		}
		return selectString;
	}

	private Map<String, Object> buildParams(OverdueAssetQueryModel overdueAssetQueryModel) {
		Map<String, Object> paramters = new HashMap<String, Object>();
		paramters.put("financialContractUuids", overdueAssetQueryModel.getFinancialContractUuidList());
		List<Integer> overdueStatusList=overdueAssetQueryModel.getAuditOverDueStatusEnumList().stream().map(qa -> qa.getOrdinal()).collect(Collectors.toList());
		paramters.put("overdueStatusList", overdueStatusList);
		List<Integer> repaymentStatusList=overdueAssetQueryModel.getRepaymentExecutionStateList().stream().map(re -> re.getOrdinal()).collect(Collectors.toList());
		filterRepaymentStatusList(repaymentStatusList,paramters);
		paramters.put("repaymentStatusList", repaymentStatusList);
		paramters.put("singleLoanContractNo", overdueAssetQueryModel.getRepaymentNo());
		paramters.put("contractNo", overdueAssetQueryModel.getContractNo());
		paramters.put("customerName", overdueAssetQueryModel.getCustomerName());
		paramters.put("startDate", overdueAssetQueryModel.getPlanStartDateValue());
		paramters.put("endDate", overdueAssetQueryModel.getPlanEndDateValue());
		paramters.put("actualStartDate", overdueAssetQueryModel.getActualStartDateValue());
		paramters.put("actualEndDate", overdueAssetQueryModel.getActualEndDateValue());
		paramters.put("outerRepaymentPlanNo", overdueAssetQueryModel.getOuterRepaymentPlanNo());

		return paramters;
	}

	private void filterRepaymentStatusList(List<Integer> repaymentStatusList,Map<String, Object> paramters) {
		if(repaymentStatusList.contains(RepaymentExecutionState.REPURCHASED.getOrdinal())){
			repaymentStatusList.remove(repaymentStatusList.indexOf(RepaymentExecutionState.REPURCHASED.getOrdinal()));
			paramters.put("haveRepurchased", true);
		}
	}

	private OverDueRepaymentDetailExcelVO buildOverDueRepaymentDetailExcelVO(ResultSet rs, Map<Long, App> appMap) throws SQLException {
		OverDueRepaymentDetailExcelVO excelVo = new OverDueRepaymentDetailExcelVO();
		excelVo.setUniqueId(rs.getString("uniqueId"));
		Long appId = rs.getLong("appId");
		if(appMap.containsKey(appId)) {
			excelVo.setAppName(appMap.get(appId).getName());
		}
		excelVo.setLoanContractNo(rs.getString("contractNo"));
		excelVo.setRepaymentNo(rs.getString("singleLoanContractNo"));
		excelVo.setOuterRepaymentPlanNo(rs.getString("outerRepaymentPlanNo"));
		excelVo.setLoanDate(getDate(rs.getTimestamp("beginDate")));
		excelVo.setCustomerName(rs.getString("customerName"));
		excelVo.setAssetRecycleDate(getDate(rs.getTimestamp("assetRecycleDate")));
		excelVo.setActualRecycleDate(getDate(rs.getTimestamp("actualRecycleDate")));
		excelVo.setCurrentPeriod(rs.getString("currentPeriod") + "/" + rs.getString("allPeriods"));
		excelVo.setAssetPrincipalValue(rs.getString("assetPrincipalValue"));
		excelVo.setAssetInterestValue(rs.getString("assetInterestValue"));
		excelVo.setDaysDifference(getOverDueDays(rs));
		excelVo.setAuditOverdueDays(getAuditOverdueDays(rs));
		excelVo.setAssetFairValue(rs.getString("amount"));
		excelVo.setAssetPrincipalValue(rs.getString("assetPrincipalValue"));
		excelVo.setAssetInterestValue(rs.getString("assetInterestValue"));
		excelVo.setRefundAmount(rs.getString("refundAmount"));
		String repaymentStatusCode = getRepaymentExecutionStateCode(rs);
		RepaymentExecutionState stateEnum = RepaymentExecutionStateMapUtil.getRepaymentExecutionStateMap().get(repaymentStatusCode);
		if(stateEnum != null) {
			excelVo.setRepaymentStatus(stateEnum.getChineseMessage());
		}
		excelVo.setOverDueStatus(EnumUtil.fromOrdinal(AuditOverdueStatus.class, rs.getInt("overdueStatus")).getChineseMessage());
		excelVo.setGuaranteeStatus(getGuaranteeStatusString(rs.getInt("guaranteeStatus")));
		excelVo.setComment(rs.getString("comment"));
		
		String idCardNumStr = rs.getString("idCardNum");
		if(!StringUtils.isEmpty(idCardNumStr)){
			excelVo.setAge(AgeUtil.getAgeByIdCardNum(idCardNumStr));
		}
		excelVo.setProvince(rs.getString("province"));

		String chargeArray = rs.getString("chargeArray");
		// chargeArray 7项费用依次为：贷款服务费、其他费用、技术服务费、罚息、逾期违约金、逾期服务费、逾期其他费用
		String[] charges = StringUtils.split(chargeArray, ",");
		if (ArrayUtils.isNotEmpty(charges)) {
			excelVo.setLoanServiceFee(charges[0]);
			excelVo.setLoanOtherFee(charges[1]);
			excelVo.setLoanTechFee(charges[2]);
			excelVo.setOverdueFeePenalty(charges[3]);
			excelVo.setOverdueFeeObligation(charges[4]);
			excelVo.setOverdueFeeService(charges[5]);
			excelVo.setOverdueFeeOther(charges[6]);
		}

		String actualChargeArray = rs.getString("actualChargeArray");
		/*
		 * actualChargeArray 9项费用依次为： 实收本金、实收利息、实收贷款服务费、实收技术维护费、
		 * 实收其他费用、实收逾期罚息、实收逾期违约金、实收逾期服务费、实收逾期其他费用
		 */
		String[] actualCharges = StringUtils.split(actualChargeArray, ",");
		if (ArrayUtils.isNotEmpty(actualCharges)) {
			String actualAmount = add(actualCharges).toString();
			excelVo.setPaidUpAssetFairValue(actualAmount);
			excelVo.setPaidUpPrincipalValue(actualCharges[0]);
			excelVo.setPaidUpInterestValue(actualCharges[1]);
			excelVo.setPaidUpLoanServiceFee(actualCharges[2]);
			excelVo.setPaidUpLoanTechFee(actualCharges[3]);
			excelVo.setPaidUpLoanOtherFee(actualCharges[4]);
			excelVo.setPaidUpOverdueFeePenalty(actualCharges[5]);
			excelVo.setPaidUpOverdueFeeObligation(actualCharges[6]);
			excelVo.setPaidUpOverdueFeeService(actualCharges[7]);
			excelVo.setPaidUpOverdueFeeOther(actualCharges[8]);
		}else if(isPaidOff(rs)){
			//足额还款，计划应收计实收
			excelVo.setPaidUpAssetFairValue(excelVo.getAssetFairValue());
			excelVo.setPaidUpPrincipalValue(excelVo.getAssetPrincipalValue());
			excelVo.setPaidUpInterestValue(excelVo.getAssetInterestValue());
			excelVo.setPaidUpLoanServiceFee(excelVo.getLoanServiceFee());
			excelVo.setPaidUpLoanTechFee(excelVo.getLoanTechFee());
			excelVo.setPaidUpLoanOtherFee(excelVo.getLoanOtherFee());
			excelVo.setPaidUpOverdueFeePenalty(excelVo.getOverdueFeePenalty());
			excelVo.setPaidUpOverdueFeeObligation(excelVo.getOverdueFeeObligation());
			excelVo.setPaidUpOverdueFeeService(excelVo.getOverdueFeeService());
			excelVo.setPaidUpOverdueFeeOther(excelVo.getOverdueFeeOther());
		}
		
		String outstandingChargeArray = rs.getString("outstandingChargeArray");
		/*
		 * outstandingChargeArray 2项费用依次为： 未偿本金、未偿利息
		 */
		String[] outstandingCharges = StringUtils.split(outstandingChargeArray, ",");
		if (ArrayUtils.isNotEmpty(outstandingCharges)) {
			excelVo.setOutstandingPrincipalValue(outstandingCharges[0]);
			excelVo.setOutstandingInterestValue(outstandingCharges[1]);
		}

		excelVo.setUnPaidPrincipalValue(
				subtract(excelVo.getAssetPrincipalValue(), excelVo.getPaidUpPrincipalValue()).toString());
		excelVo.setUnPaidInterestValue(
				subtract(excelVo.getAssetInterestValue(), excelVo.getPaidUpInterestValue()).toString());
		excelVo.setUnPaidTotalOtherFee(subtract(excelVo.getAssetFairValue(), excelVo.getPaidUpAssetFairValue(),
				excelVo.getUnPaidPrincipalValue(), excelVo.getUnPaidInterestValue()).toString());

		return excelVo;
	}

	private int getOverDueDays(ResultSet resultSet) throws SQLException {
		Date end = new Date();
		if (isClearAssetSet(resultSet)) {
			end = resultSet.getDate("actualRecycleDate");
		}
		Date assetRecycleDate=resultSet.getDate("assetRecycleDate");
		int overDueDay = DateUtils.compareTwoDatesOnDay(end, assetRecycleDate);
		return overDueDay > 0 ? overDueDay : 0;
	}

	private int getAuditOverdueDays(ResultSet resultSet) throws SQLException {
		if (resultSet.getInt("overdueStatus") == AuditOverdueStatus.OVERDUE.ordinal()) {
			Date endDate = DateUtils.getToday();
			if (isClearAssetSet(resultSet)) {
				endDate = resultSet.getDate("actualRecycleDate");
			}
			Date overdueDate=resultSet.getDate("overdueDate");
			return DateUtils.distanceDaysBetween(overdueDate, endDate) + 1;
		}
		return 0;
	}

	private String getGuaranteeStatusString(int guaranteeStatus) throws SQLException {
		switch (EnumUtil.fromOrdinal(GuaranteeStatus.class, guaranteeStatus)) {
		case NOT_OCCURRED:
			return "未发生";
		case WAITING_GUARANTEE:
			return "待补足";
		case HAS_GUARANTEE:
			return "已补足";
		case LAPSE_GUARANTEE:
			return "担保作废";
		default:
			return "";
		}
	}

	private BigDecimal subtract(String minuendStr, String... subtrahendStrs) {
		BigDecimal resultAmount = BigDecimal.ZERO;
		BigDecimal minuend = new BigDecimal(minuendStr);
		if (minuend != null) {
			resultAmount = resultAmount.add(minuend);
		}
		for (String subtrahendStr : subtrahendStrs) {
			if (subtrahendStr  != null) {
				BigDecimal subtrahend = new BigDecimal(subtrahendStr);
				resultAmount = resultAmount.subtract(subtrahend);
			}
		}
		return resultAmount.signum() == 1 ? resultAmount : BigDecimal.ZERO;
	}

	private String getRepaymentExecutionStateCode(ResultSet resultSet)
			throws SQLException {
		int executingStatus = resultSet.getInt("executingStatus");
		if (executingStatus == ExecutingStatus.UNEXECUTED.ordinal()) {
			if (resultSet.getInt("timeInterval") == TimeInterval.IMMATURE
					.ordinal()) {
				return "X00";
			}
			return "X10";
		}

		if (executingStatus == ExecutingStatus.PROCESSING.ordinal()) {
			if (resultSet.getInt("deductionStatus") == DeductionStatus.LOCAL_PROCESSING.ordinal()
					|| resultSet.getInt("deductionStatus") == DeductionStatus.OPPOSITE_PROCESSING.ordinal()) {
				return "1X1";
			}
			return "0X1";
		}

		return "XX" + executingStatus;
	}

	private boolean isClearAssetSet(ResultSet resultSet) throws SQLException {
		return resultSet.getInt("assetStatus") == AssetClearStatus.CLEAR.ordinal();
	}

	private String getDate(Date timestamp) {
		return null == timestamp ? StringUtils.EMPTY : DateUtils.format(timestamp);
	}
	
	private boolean isPaidOff(ResultSet resultSet) throws SQLException {
		return resultSet.getInt("assetStatus") == AssetClearStatus.CLEAR.ordinal()&& resultSet.getInt("onAccountStatus") == OnAccountStatus.WRITE_OFF.ordinal();
	}
	
	private Map<Long, App> getAppMap() {
		List<App> apps = appService.getAllApp();
		return apps.stream().collect(Collectors.toMap(App::getId, app -> app));
	}

}