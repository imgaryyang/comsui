package com.zufangbao.earth.report.wrapper;


import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zufangbao.earth.util.IpUtil;
import com.zufangbao.sun.entity.tag.TagConfigVO;
import com.zufangbao.sun.entity.tag.TagIdentityMap;
import com.zufangbao.sun.handler.tag.TagOpsHandler;
import com.zufangbao.sun.service.tag.TagService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.AgeUtil;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.*;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentManagementExcelVO;
import com.zufangbao.sun.yunxin.entity.model.assetset.AssetSetQueryModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;

@Component
public class ReportWrapper5 extends ReportBaseWrapper implements IReportWrapper<AssetSetQueryModel> {

	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private TagOpsHandler tagOpsHandler;

	@Override
	public ExportEventLogModel wrap(AssetSetQueryModel paramsBean, HttpServletRequest request,
			HttpServletResponse response, ExportEventLogModel exportEventLogModel,Principal principal) throws Exception {

		if(CollectionUtils.isEmpty(paramsBean.getFinancialContractIdList())) {
			return exportEventLogModel;
		}
		
		exportEventLogModel.recordStartLoadDataTime();
		
		List<FinancialContract> financialContracts = financialContractService
				.getFinancialContractsByIds(paramsBean.getFinancialContractIdList());
		Map<String, FinancialContract> financialContractsMap = financialContracts.stream()
				.collect(Collectors.toMap(FinancialContract::getFinancialContractUuid, fc -> fc));

		Map<String, Object> params = buildParams(paramsBean,financialContractsMap);
		String sql = getCachedSql("reportWrapper5", params);
		
		String fileName = String.format("还款管理表_%s.zip", DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS"));
		ZipOutputStream zip = openZipOutputStream(response, fileName);
		PrintWriter printWriter = putNextZipEntry(zip, "还款管理表");
		
		ExportableRowCallBackHandler<RepaymentManagementExcelVO> callBack = new ExportableRowCallBackHandler<RepaymentManagementExcelVO>(RepaymentManagementExcelVO.class, printWriter, new ReportVOBuilder<RepaymentManagementExcelVO>() {
			@Override
			public RepaymentManagementExcelVO buildRow(ResultSet rs) throws SQLException {
				FinancialContract financialContract = financialContractsMap.get(rs.getString("financialContractUuid"));
				return buildGuaranteeOrderExcel(financialContract, rs, paramsBean.getExportTags());
			}
		});
		
		genericDaoSupport.query(sql, params, callBack);
		
		closeZipOutputStream(zip, response);
		
		exportEventLogModel.recordAfterLoadDataComplete(callBack);
		
		StringBuffer selectString = extractSelectString(paramsBean,new ArrayList<String>(financialContractsMap.keySet()));
		SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtil.getIpAddress(request),LogFunctionType.EXPORTREPAYEMNTPLAN,LogOperateType.EXPORT);
		log.setRecordContent("导出还款管理表，导出记录"+callBack.getResultSize()+"条。"+"筛选条件："+selectString);
		systemOperateLogService.save(log);

		return exportEventLogModel;
	}

	private StringBuffer extractSelectString(AssetSetQueryModel queryModel, List<String> financialContractUuids) {
		StringBuffer selectString = financialContractService.selectFinancialContract(financialContractUuids);
		if (StringUtils.isNotEmpty(queryModel.getStartDate())) {
			selectString.append("，计划还款起始日【" + queryModel.getStartDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getEndDate())) {
			selectString.append("，计划还款终止日【" + queryModel.getEndDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getActualRecycleStartDate())) {
			selectString.append("，实际还款起始日【" + queryModel.getActualRecycleStartDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getActualRecycleEndDate())) {
			selectString.append("，实际还款终止日【" + queryModel.getActualRecycleEndDate() + "】");
		}
		List<RepaymentExecutionState> repaymentExecutionStates = queryModel.getRepaymentExecutionStateList();
		if (CollectionUtils.isNotEmpty(repaymentExecutionStates)) {
			selectString.append("，还款状态");
			for (RepaymentExecutionState state : repaymentExecutionStates) {
				selectString.append("【" + state.getChineseMessage() + "】");
			}
		}
		List<AuditOverdueStatus> AuditOverdueStatusList = queryModel.getAuditOverDueStatusEnumList();
		if (CollectionUtils.isNotEmpty(AuditOverdueStatusList)) {
			selectString.append("，逾期状态");
			for (AuditOverdueStatus auditOverdueStatus : AuditOverdueStatusList) {
				selectString.append("【" + auditOverdueStatus.getChineseMessage() + "】");
			}
		}
		if (StringUtils.isNotBlank(queryModel.getSingleLoanContractNo())) {
			selectString.append("，还款编号【" + queryModel.getSingleLoanContractNo() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getContractNo())) {
			selectString.append("，贷款合同编号【" + queryModel.getContractNo() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getCustomerName())) {
			selectString.append("，客户姓名【" + queryModel.getCustomerName() + "】");
		}
		return selectString;
	}
	
	private Map<String,Object> buildParams(AssetSetQueryModel assetSetQueryModel,Map<String, FinancialContract> financialContractsMap){
		Map<String, Object> paramters = new HashMap<String, Object>();
		paramters.put("financialContractUuids", financialContractsMap.keySet());
		paramters.put("repaymentStatusList", assetSetQueryModel.getPaymentStatusOrdinalList());
		paramters.put("overdueStatusList", assetSetQueryModel.getAuditOverDueStatusEnumList().stream().map(t ->t.ordinal()).collect(Collectors.toList()));
		paramters.put("singleLoanContractNo", assetSetQueryModel.getSingleLoanContractNo());
		paramters.put("outerRepaymentPlanNo", assetSetQueryModel.getOuterRepaymentPlanNo());
		paramters.put("startDate", assetSetQueryModel.getStartDateValue());
		paramters.put("endDate", assetSetQueryModel.getEndDateValue());
		paramters.put("actualStartDate", assetSetQueryModel.getActualRecycleStartDateValue());
		paramters.put("actualEndDate", assetSetQueryModel.getActualRecycleEndDateValue());
		paramters.put("contractNo", assetSetQueryModel.getContractNo());
		paramters.put("customerName", assetSetQueryModel.getCustomerName());
		paramters.put("exportTags", assetSetQueryModel.getExportTags());
		return paramters;
	}
	private  RepaymentManagementExcelVO buildGuaranteeOrderExcel(FinancialContract financialContract ,ResultSet rs, Boolean exportTags) throws SQLException {
		RepaymentManagementExcelVO re=new RepaymentManagementExcelVO();
		re.setUniqueId(rs.getString("uniqueId"));
		re.setSingleLoanContractNo(rs.getString("singleLoanContractNo"));
		re.setOuterRepaymentPlanNo(rs.getString("outerRepaymentPlanNo"));
		re.setContractNo(rs.getString("contractNo"));
		re.setFinancialContractNo(financialContract.getContractNo());
		re.setFinancialProjectName(financialContract.getContractName());
		re.setCustomerName(rs.getString("customerName"));
		re.setAssetRecycleDate(rs.getString("assetRecycleDate"));
		re.setCurrentPeriod(rs.getInt("currentPeriod"));
		re.setAllPeriods(rs.getInt("allPeriods"));
		re.setAssetPrincipalValue(rs.getString("assetPrincipalValue"));
		re.setAssetInterestValue(rs.getString("assetInterestValue"));
		re.setAssetInitialValue(rs.getString("assetInitialValue"));
		String chargeArray = rs.getString("chargeArray");
		
		//chargeArray 7项费用依次为 贷款服务费、其他费用、技术服务费、罚息、逾期违约金、逾期服务费、逾期其他费用
				String[] charges = StringUtils.split(chargeArray, ",");
				if(ArrayUtils.isNotEmpty(charges)) {
					re.setLoanServiceFee(charges[0]);
					re.setOtherFee(charges[1]);
					re.setTechMaintenanceFee(charges[2]);
					re.setOverduePenalty(charges[3]);
					re.setOverdueDefaultFee(charges[4]);
					re.setOverdueServiceFee(charges[5]);
					re.setOverdueOtherFee(charges[6]);
					re.setAssetInitialValue(rs.getBigDecimal("assetInitialValue").add(this.getExpense(charges)).toString());
					re.setTotalOverdueFee(this.getTotalOverdueFee(charges).toString());
				}
			
		re.setOverDueDays(getOverDueDays(rs));
		re.setAuditOverdueDays(getAuditOverdueDays(financialContract.getLoanOverdueStartDay(),rs));
		re.setAmount(rs.getString("amount"));
		if(isPaidOff(rs)) {
			re.setActualAmount(rs.getString("amount"));
		}
		
		String actualChargeArray = rs.getString("actualChargeArray");
		String[] actualCharges = StringUtils.split(actualChargeArray, ",");
		if(actualCharges != null&& actualCharges.length>0) {
			String actualAmount = add(actualCharges).toString();
			re.setActualAmount(actualAmount);
		}
		
		if(StringUtils.isNotEmpty(rs.getString("actualRecycleDate"))){
			re.setActualRecycleDate(DateUtils.format(rs.getDate("actualRecycleDate"), "yyyy-MM-dd"));
		}
		re.setRefundAmount(rs.getString("refundAmount"));
		re.setPaymentStatus(getRepaymentExecutionState(rs));
		re.setGuaranteeStatus(EnumUtil.fromOrdinal(GuaranteeStatus.class, rs.getInt("guaranteeStatus")).getChineseMessage());
		re.setComment(rs.getString("comment"));
		
		String idCardNumStr = rs.getString("idCardNum");
		if(!StringUtils.isEmpty(idCardNumStr)){
			re.setAge(AgeUtil.getAgeByIdCardNum(idCardNumStr));
		}
		re.setProvince(rs.getString("province"));

		if (exportTags) {
			re.setTags(rs.getString("tags"));
		}
		return re;
	}
	
	private BigDecimal getExpense(String[] charges) {
		BigDecimal tmpLoanServiceFee = new BigDecimal(charges[0]);
		BigDecimal tmpLoanTechFee = new BigDecimal(charges[2]);
		BigDecimal tmpLoanOtherFee = new BigDecimal(charges[1]);
		return tmpLoanServiceFee.add(tmpLoanTechFee)
				.add(tmpLoanOtherFee);
	}
	
	private BigDecimal getTotalOverdueFee(String[] charges) {
		BigDecimal tmpOverdueFeePenalty = new BigDecimal(charges[3]);
		BigDecimal tmpOverdueFeeObligation = new BigDecimal(charges[4]);
		BigDecimal tmpOverdueFeeService = new BigDecimal(charges[5]);
		BigDecimal tmpOverdueFeeOther = new BigDecimal(charges[6]);
		return tmpOverdueFeePenalty.add(tmpOverdueFeeObligation)
				.add(tmpOverdueFeeService).add(tmpOverdueFeeOther);
	}
	
	private boolean isPaidOff(ResultSet resultSet) throws SQLException {
		return resultSet.getInt("assetStatus") == AssetClearStatus.CLEAR.ordinal()&& resultSet.getInt("onAccountStatus") == OnAccountStatus.WRITE_OFF.ordinal();
	}
	
	private int getAuditOverdueDays(int loanOverdueStartDay, ResultSet  resultSet) throws SQLException {
		Date endDate = DateUtils.getToday();
		if (isClearAssetSet(resultSet)) {
			endDate =resultSet.getDate("actualRecycleDate");
		}
		if (resultSet.getInt("overdueStatus")== AuditOverdueStatus.UNCONFIRMED.ordinal()) {
			int result = DateUtils.compareTwoDatesOnDay(resultSet.getDate("assetRecycleDate"),endDate) + loanOverdueStartDay;
			return result < 0 ? Math.abs(result) + 1: 0;
		}
		if (resultSet.getInt("overdueStatus")== AuditOverdueStatus.OVERDUE.ordinal()) {
			return DateUtils.distanceDaysBetween(resultSet.getDate("overdueDate"), endDate) + 1;
		}
		return 0;
	}

	/**
	 * 获取物理逾期起始日
	 */
	public Date getOverdueStartDate(int loanOverdueStartDay, ResultSet  resultSet) throws SQLException {
		Date assetRecycleDate = resultSet.getDate("assetRecycleDate");
		return DateUtils.addDays(assetRecycleDate, loanOverdueStartDay);
	}

	private boolean isClearAssetSet(ResultSet  resultSet) throws SQLException {
		return resultSet.getInt("assetStatus") == AssetClearStatus.CLEAR.ordinal();
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
	
	 private  String getRepaymentExecutionStateCode(ResultSet  resultSet) throws SQLException{	
		 	int executingStatus=resultSet.getInt("executingStatus");
			if(executingStatus==ExecutingStatus.UNEXECUTED.ordinal()){
				if(resultSet.getInt("timeInterval")==TimeInterval.IMMATURE.ordinal()){
					return "X00";
				}
				return "0X1";
			}
			
			if(executingStatus==ExecutingStatus.PROCESSING.ordinal()){
				if(resultSet.getInt("deductionStatus")==DeductionStatus.LOCAL_PROCESSING.ordinal() || resultSet.getInt("deductionStatus")==DeductionStatus.OPPOSITE_PROCESSING.ordinal()){
					return "1X1";
				}
				return "0X1";
			}
			
			return "XX"+executingStatus;
	 }
	 private  String getRepaymentExecutionState(ResultSet resultSet) throws SQLException{
			if(resultSet == null){
				return "";
			}
			return RepaymentExecutionStateMapUtil.getRepaymentExecutionStateMap().get(getRepaymentExecutionStateCode(resultSet)).getChineseMessage();
		}
	 
}
