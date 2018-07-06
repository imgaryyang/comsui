package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.report.exception.ExportException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.DailyReturnListExcel;
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
@SuppressWarnings("rawtypes")
public class ReportWrapper11 extends ReportBaseWrapper implements IReportWrapper<HashMap> {

	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Override
	public ExportEventLogModel wrap(HashMap paramsBean, HttpServletRequest request, HttpServletResponse response,
			ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {
		
		Long financialContractId = Long.valueOf((String) paramsBean.get("financialContractId"));
		FinancialContract financialContract = financialContractService.getFinancialContractById(financialContractId);
		if(financialContract == null) {
			throw new ExportException(GlobalCodeSpec.CODE_FAILURE, "所选项目不存在！");
		}
		
		Map<String, Object> params = buildParams(paramsBean, financialContract.getFinancialContractUuid());
		Date startTime = (Date) params.get("startTime");
		Date endTime = (Date) params.get("endTime");
		String sql = getCachedSql("reportWrapper11", params);
		String fileName = "还款清单"+"_"+DateUtils.format(startTime, YYYY_MM_DD) +"-"+DateUtils.format(DateUtils.addDays(endTime,-1), YYYY_MM_DD)+".zip";
		ZipOutputStream zip = openZipOutputStream(response, fileName);
		PrintWriter printWriter = putNextZipEntry(zip, "还款清单");
		
		exportEventLogModel.recordStartLoadDataTime();
		
		ExportableRowCallBackHandler<DailyReturnListExcel> callBack = new ExportableRowCallBackHandler<DailyReturnListExcel>(DailyReturnListExcel.class, printWriter, new ReportVOBuilder<DailyReturnListExcel>() {
			@Override
			public DailyReturnListExcel buildRow(ResultSet rs) throws SQLException {
				return buildDailyReturnListExcel(financialContract, rs);
			}
		});

		if(params.get("paymentGateways") != null){
			genericDaoSupport.query(sql, params, callBack);
		}

		closeZipOutputStream(zip, response);
		
		exportEventLogModel.recordAfterLoadDataComplete(callBack);
		
		String startDateString = (String) paramsBean.get("startDateString");
		String endDateString = (String) paramsBean.get("endDateString");
		String paymentGateways = (String) paramsBean.get("paymentGateways");
		List<PaymentInstitutionName> paymentGatewayEnums = EnumUtil.fromOrdinals(PaymentInstitutionName.class, paymentGateways);
		StringBuffer selectString = extractSelectString(startDateString,endDateString,paymentGatewayEnums, financialContract);
		SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.ONLINEBILLEXPORTDAILYRETURNLIST,LogOperateType.EXPORT);
		log.setRecordContent("线上支付单，导出当日还款清单，导出记录"+callBack.getResultSize()+"条。筛选条件："+selectString);
		systemOperateLogService.save(log);
		return exportEventLogModel;
	}

	private StringBuffer extractSelectString(String startDate, String endDate,List<PaymentInstitutionName> paymentGatewayEnums, FinancialContract financialContract) {
		StringBuffer selectString = new StringBuffer();
		if (StringUtils.isNotEmpty(startDate)) {
			selectString.append("发生时间【" + startDate +"-"+endDate+"】");
		}
		if (financialContract != null) {
			selectString.append("，信托合同【" + financialContract.getContractNo() + "】");
		}
		if (CollectionUtils.isNotEmpty(paymentGatewayEnums)) {
			selectString.append("，通道");
			for (PaymentInstitutionName paymentInstitutionName : paymentGatewayEnums) {
				selectString.append("【" + paymentInstitutionName.getChineseMessage() + "】");
			}
		}
		return selectString;
	}

	private Map<String, Object> buildParams(HashMap paramsBean, String financialContractUuid) {
		String startDateString = (String) paramsBean.get("startDateString");
		String endDateString = (String) paramsBean.get("endDateString");
		Date startTime = getDate(startDateString);
		Date endTime = DateUtils.addDays(getDate(endDateString), 1);
		String paymentGateways = (String) paramsBean.get("paymentGateways");
		List<PaymentInstitutionName> paymentGatewayEnums = EnumUtil.fromOrdinals(PaymentInstitutionName.class, paymentGateways);
		List<Integer> paymentGatewayEnumOrdinals = CollectionUtils.isEmpty(paymentGatewayEnums)? null:paymentGatewayEnums.stream().map(Enum::ordinal).collect(Collectors.toList());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("paymentGateways", paymentGatewayEnumOrdinals);
		params.put("financialContractUuid", financialContractUuid);
		return params;
	}
	
	private Date getDate(String Date) {
		if (StringUtils.isEmpty(Date) || DateUtils.asDay(Date) == null) {
			return DateUtils.parseDate(DateUtils.today(), YYYY_MM_DD);
		}
		return DateUtils.parseDate(Date, YYYY_MM_DD);
	}
	
	private DailyReturnListExcel buildDailyReturnListExcel(FinancialContract financialContract, ResultSet rs) throws SQLException {
		DailyReturnListExcel vo = new DailyReturnListExcel();
		if(rs.getTimestamp("completeTime") != null){
			vo.setDebitDate(DateUtils.format(rs.getTimestamp("completeTime")));
		}
		vo.setActualRepaymentAmount(BigDecimal.ZERO.toString());
		DeductApplicationExecutionStatus executionStatus = EnumUtil.fromOrdinal(DeductApplicationExecutionStatus.class, rs.getInt("executionStatus"));
		if(executionStatus == DeductApplicationExecutionStatus.SUCCESS){
			vo.setActualRepaymentAmount(rs.getString("actualTotalAmount"));
			
			BigDecimal actualPrinciple = rs.getBigDecimal("actualPrinciple");
			BigDecimal actualInterest = rs.getBigDecimal("actualInterest");
			BigDecimal actualLoanServiceFee = rs.getBigDecimal("actualLoanServiceFee");
			BigDecimal actualTechFee = rs.getBigDecimal("actualTechFee");
			BigDecimal actualOtherFee = rs.getBigDecimal("actualOtherFee");
			BigDecimal actualPenalty = rs.getBigDecimal("actualPenalty");
			BigDecimal actualOverDueFeeObligation = rs.getBigDecimal("actualOverDueFeeObligation");
			BigDecimal actualOverDueFeeServiceFee = rs.getBigDecimal("actualOverDueFeeServiceFee");
			BigDecimal actualOverDueFeeOtherFee = rs.getBigDecimal("actualOverDueFeeOtherFee");
			
			vo.setPaidUpPrincipalValue(actualPrinciple.toString());
			vo.setPaidUpInterestValue(actualInterest.toString());
			vo.setPaidUpLoanServiceFee(actualLoanServiceFee.toString());
			vo.setPaidUpTechMaintenanceFee(actualTechFee.toString());
			vo.setPaidUpOtherFee(actualOtherFee.toString());
			vo.setPaidUpOverduePenalty(actualPenalty.toString());
			vo.setPaidUpOverdueDefaultFee(actualOverDueFeeObligation.toString());
			vo.setPaidUpOverdueServiceFee(actualOverDueFeeServiceFee.toString());
			vo.setPaidUpOverdueOtherFee(actualOverDueFeeOtherFee.toString());
			
			BigDecimal totalActualFee = actualPrinciple.add(actualInterest)
					.add(actualLoanServiceFee).add(actualTechFee)
					.add(actualOtherFee).add(actualPenalty)
					.add(actualOverDueFeeObligation)
					.add(actualOverDueFeeServiceFee).add(actualOverDueFeeOtherFee);
			vo.setAssetFairValue(totalActualFee.toString());
		}
		vo.setFinancialAccountNo(financialContract.getCapitalAccount().getAccountNo());
		vo.setRepaymentName(rs.getString("cpBankAccountHolder"));
		vo.setRepaymentAccountNo(rs.getString("cpBankCardNo"));
		vo.setPlanRepaymentDate(DateUtils.format(rs.getDate("assetRecycleDate")));
		
		vo.setPlanAssetPrincipal(rs.getString("assetPrincipalValue"));
		vo.setPlanAssetInterest(rs.getString("assetInterestValue"));
		
		String chargeArray = rs.getString("chargeArray");
		//chargeArray 7项费用依次为 贷款服务费、其他费用、技术服务费、罚息、逾期违约金、逾期服务费、逾期其他费用
		String[] charges = StringUtils.split(chargeArray, ",");
		if(ArrayUtils.isNotEmpty(charges)) {
			vo.setLoanServiceFee(charges[0]);
			vo.setLoanOtherFee(charges[1]);
			vo.setLoanTechFee(charges[2]);
			vo.setTotalOverdueFee(add(charges[3], charges[4], charges[5], charges[6]).toString());
		}
		
		vo.setRepaymentStatus(executionStatus.getChineseMessage());
		vo.setRemark(rs.getString("executionRemark"));
		vo.setBank(rs.getString("cpBankName"));
		vo.setDeductSendDate(DateUtils.format(rs.getTimestamp("createTime"),"yyyy-MM-dd HH:mm:ss"));
		vo.setLastModifyTime(DateUtils.format(rs.getTimestamp("lastModifiedTime"), "yyyy-MM-dd HH:mm:ss"));
		vo.setContractNo(rs.getString("contractNo"));
		vo.setUniqueId(rs.getString("uniqueId"));

		vo.fillPaymentGateway(rs.getInt("paymentGateway"));
		
		return vo;
	}
	
}
