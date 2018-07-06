package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.RepaymentPlanSpec;
import com.zufangbao.sun.utils.AgeUtil;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.SensitiveInfoUtils;
import com.zufangbao.sun.yunxin.entity.*;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentPlanDetailsExportStyle;
import com.zufangbao.sun.yunxin.entity.model.repaymentPlan.RepaymentPlanQueryModel;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

@Component
public class ReportWrapper3 extends ReportBaseWrapper implements IReportWrapper<RepaymentPlanQueryModel> {

	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	@Override
	public ExportEventLogModel wrap(RepaymentPlanQueryModel paramsBean, HttpServletRequest request,
			HttpServletResponse response, ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {
		
		if(CollectionUtils.isEmpty(paramsBean.getFinancialContractUuidList())) {
			return exportEventLogModel;
		}
		
		Map<String, Object> params = buildParams(paramsBean);
		String sql = getCachedSql("reportWrapper3", params);
		
		exportEventLogModel.recordStartLoadDataTime();
		List<FinancialContract> financialContracts = financialContractService.getFinancialContractsByUuids(paramsBean.getFinancialContractUuidList());
		Map<String, FinancialContract> financialContractsMap = financialContracts.stream().collect(Collectors.toMap(FinancialContract::getFinancialContractUuid, fc -> fc));
		
		String fileName = String.format("还款计划明细汇总表_%s.zip", DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS"));
		ZipOutputStream zip = openZipOutputStream(response, fileName);
		PrintWriter printWriter = putNextZipEntry(zip, "还款计划表");
		
		ExportableRowCallBackHandler<RepaymentPlanDetailsExportStyle> callBack = new ExportableRowCallBackHandler<RepaymentPlanDetailsExportStyle>(RepaymentPlanDetailsExportStyle.class, printWriter, new ReportVOBuilder<RepaymentPlanDetailsExportStyle>() {
			@Override
			public RepaymentPlanDetailsExportStyle buildRow(ResultSet rs) throws SQLException {
				FinancialContract financialContract = financialContractsMap.get(rs.getString("financialContractUuid"));
				return buildRepaymentPlanDetailsExportStyle(financialContract, rs);
			}
		});
		
		genericDaoSupport.query(sql, params, callBack);

		genericDaoSupport.query(sql.replace("FROM asset_set ", "FROM asset_set_history "), params, callBack);

		closeZipOutputStream(zip, response);
		
		exportEventLogModel.recordAfterLoadDataComplete(callBack);
		
		StringBuffer selectString = extractSelectString(paramsBean);
		SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.EXPORTREPAYEMNTPLAN,LogOperateType.EXPORT);
		log.setRecordContent("导出还款计划，导出记录"+callBack.getResultSize()+"条。"+"筛选条件："+selectString);
		systemOperateLogService.save(log);

		return exportEventLogModel;
	}

	private StringBuffer extractSelectString(RepaymentPlanQueryModel queryModel) {
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

		selectPlanStatus(queryModel.getPlanStatus(), selectString);
		selectRepaymentStatus(queryModel.getRepaymentStatus(), selectString);
		selectRepaymentType(queryModel.getRepaymentType(), selectString);

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

	private void selectPlanStatus(int planStatus, StringBuffer selectString) {
		switch (planStatus) {
		case 0:
			selectString.append("，计划状态【正常】");
			break;
		case 1:
			selectString.append("，计划状态【提前还款】");
			break;
		case 2:
			selectString.append("，计划状态【变更提前】");
			break;
		case 3:
			selectString.append("，计划状态【锁定】");
			break;
		case 4:
			selectString.append("，计划状态【已关闭】");
			break;
		default:
			break;
		}
	}

	private void selectRepaymentStatus(int repaymentStatus, StringBuffer selectString) {
		switch (repaymentStatus) {
		case 0:
			selectString.append("，还款状态【未到期】");
			break;
		case 1:
			selectString.append("，还款状态【处理中】");
			break;
		case 2:
			selectString.append("，还款状态【扣款中】");
			break;
		case 3:
			selectString.append("，还款状态【还款成功】");
			break;
		case 4:
			selectString.append("，还款状态【已回购】");
			break;
		case 5:
			selectString.append("，还款状态【回购中】");
			break;
		case 6:
			selectString.append("，还款状态【违约】");
			break;
		case 7:
			selectString.append("，还款状态【作废】");
			break;
		case 8:
			selectString.append("，还款状态【支付中】");
			break;
		default:
			break;
		}

	}

	private void selectRepaymentType(int repaymentType, StringBuffer selectString) {
		switch (repaymentType) {
		case 0:
			selectString.append("，还款类型【正常还款】");
			break;
		case 1:
			selectString.append("，还款类型【提前部分】");
			break;
		case 2:
			selectString.append("，还款类型【提前全额】");
			break;
		case 3:
			selectString.append("，还款类型【逾期还款】");
			break;
		default:
			break;
		}

	}

	private Map<String, Object> buildParams(RepaymentPlanQueryModel queryModel) {
		
		Map<String,Object> params=new HashMap<>();
		params.put("financialContractUuids", queryModel.getFinancialContractUuidList());
		params.put("repaymentPlanStartDate", queryModel.getPlanStartDateValue());
		params.put("repaymentPlanEndDate", queryModel.getPlanEndDateValue());
		params.put("actualRepaymentStartDate", queryModel.getActualStartDateValue());
		params.put("actualRepaymentEndDate", queryModel.getActualEndDateValue());
		params.put("singleLoanContractNo", queryModel.getRepaymentNo());
		params.put("outerRepaymentPlanNo", queryModel.getOuterRepaymentPlanNo());


		params.put("planStatus", queryModel.getPlanStatus());
		params.put("repaymentStatus", queryModel.getRepaymentStatus());
		params.put("repaymentType", queryModel.getRepaymentType());
		
		params.put("contractNo", queryModel.getContractNo());
		params.put("customerName", queryModel.getCustomerName());
		return params;
	}
	
	private RepaymentPlanDetailsExportStyle buildRepaymentPlanDetailsExportStyle(FinancialContract financialContract, ResultSet rs) throws SQLException {
		RepaymentPlanDetailsExportStyle vo = new RepaymentPlanDetailsExportStyle();
		
		vo.setRepaymentNo(rs.getString("repaymentPlanNo"));
		vo.setOuterRepaymentPlanNo(rs.getString("outerRepaymentPlanNo"));
		vo.setAssetRecycleDate(rs.getString("assetRecycleDate"));
		if (StringUtils.isNotEmpty(rs.getString("actualRecycleDate"))) {
			vo.setActualRecycleDate(DateUtils.format(rs.getDate("actualRecycleDate"), "yyyy-MM-dd"));
		}
		
		vo.setAssetInterestValue(rs.getString("planInterest"));
		vo.setAssetPrincipalValue(rs.getString("planPrincipal"));
		
		String repaymentStatusCode = getRepaymentExecutionStateCode(rs);
		RepaymentExecutionState stateEnum = RepaymentExecutionStateMapUtil.getRepaymentExecutionStateMap().get(repaymentStatusCode);
		if(stateEnum != null) {
			vo.setPaymentStatus(stateEnum.getChineseMessage());
		}
		vo.setPlanStatus(getPlanStatus(rs));
		
		vo.setUniqueId(rs.getString("uniqueId"));
		vo.setAppName(financialContract.getApp().getAppId());
		vo.setLoanContractNo(rs.getString("contractNo"));
		vo.setLoanDate(rs.getString("beginDate"));
		vo.setEffectiveDate(rs.getString("beginDate"));
		vo.setCustomerName(rs.getString("customerName"));
		
		if(financialContract != null){
			vo.setFinancialContractNo(financialContract.getContractNo());
			Account account = financialContract.getCapitalAccount();
			if(account != null){
				if (!StringUtils.isEmpty(account.getAccountNo())) {
					vo.setFinancialAccountNo(SensitiveInfoUtils.desensitizationString(account.getAccountNo()));
				}
			}
		}
		
		String idCardNumStr = rs.getString("idCardNum");
		if (!StringUtils.isEmpty(idCardNumStr)) {
			vo.setIdCardNo(SensitiveInfoUtils.desensitizationStringOfBirthDay(idCardNumStr));
			vo.setAge(AgeUtil.getAgeByIdCardNum(idCardNumStr));
		}
		vo.setBankName(rs.getString("bank"));
		vo.setProvince(rs.getString("province"));
		vo.setCity(rs.getString("city"));
		if (!StringUtils.isEmpty(rs.getString("payAcNo"))) {
			vo.setPayAcNo(SensitiveInfoUtils.desensitizationString(rs.getString("payAcNo")));
		}
		
		String chargeArray = rs.getString("chargeArray");
		//chargeArray 3项费用依次为 贷款服务费、其他费用、技术服务费
		String[] charges = StringUtils.split(chargeArray, ",");
		if(ArrayUtils.isNotEmpty(charges)) {
			vo.setLoanServiceFee(charges[0]);
			vo.setOtherExpenses(charges[1]);
			vo.setTechnicalServiceFee(charges[2]);
		}
		return vo;
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
	
	private String getPlanStatus(ResultSet resultSet) throws SQLException {
		
		if (resultSet.getInt("activeStatus") == AssetSetActiveStatus.FROZEN.ordinal())
			return RepaymentPlanSpec.PlanStatusMsg.LOCK;
		if (resultSet.getInt("activeStatus") == AssetSetActiveStatus.INVALID.ordinal())
			return RepaymentPlanSpec.PlanStatusMsg.OFF;
		if (resultSet.getInt("activeStatus") == AssetSetActiveStatus.OPEN.ordinal()) {
			if (resultSet.getInt("planType") == PlanType.NORMAL.ordinal())
				return RepaymentPlanSpec.PlanStatusMsg.NORMAL;
			if (resultSet.getInt("planType") == PlanType.PREPAYMENT.ordinal() && resultSet.getBoolean("canBeRollbacked") == true)
				return RepaymentPlanSpec.PlanStatusMsg.PREREPAYMENT;
			if (resultSet.getInt("planType") == PlanType.PREPAYMENT.ordinal() && resultSet.getBoolean("canBeRollbacked") == false)
				return RepaymentPlanSpec.PlanStatusMsg.MODIFYPRE;
		}
		return "";
	}
	
}
