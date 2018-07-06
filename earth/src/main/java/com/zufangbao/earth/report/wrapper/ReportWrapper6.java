package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.GuaranteeStatus;
import com.zufangbao.sun.yunxin.entity.model.GuaranteeOrderExcel;
import com.zufangbao.sun.yunxin.entity.model.GuaranteeOrderModel;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

@Component
public class ReportWrapper6 extends ReportBaseWrapper implements IReportWrapper<GuaranteeOrderModel> {

	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	@Override
	public ExportEventLogModel wrap(GuaranteeOrderModel paramsBean, HttpServletRequest request,
			HttpServletResponse response, ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {
		
		if(CollectionUtils.isEmpty(paramsBean.getFinancialContractIdList())){
			return exportEventLogModel;
		}
		
		Map<String, Object> params = buildParams(paramsBean);
		String sql = getCachedSql("reportWrapper6", params);
		
		exportEventLogModel.recordStartLoadDataTime();
		
		List<FinancialContract> financialContracts = financialContractService.getFinancialContractsByIds(paramsBean.getFinancialContractIdList());
		Map<Long, FinancialContract> financialContractsMap = financialContracts.stream().collect(Collectors.toMap(FinancialContract::getId, fc -> fc));
		Map<String, FinancialContract> financialContractUuidMap = financialContracts.stream().collect(Collectors.toMap(FinancialContract::getFinancialContractUuid, fc -> fc));

		String fileName = String.format("差额补足单_%s.zip", DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS"));
		ZipOutputStream zip = openZipOutputStream(response, fileName);
		PrintWriter printWriter = putNextZipEntry(zip, "差额补足单");
		
		ExportableRowCallBackHandler<GuaranteeOrderExcel> callBack = new ExportableRowCallBackHandler<GuaranteeOrderExcel>(GuaranteeOrderExcel.class, printWriter, new ReportVOBuilder<GuaranteeOrderExcel>() {
			@Override
			public GuaranteeOrderExcel buildRow(ResultSet rs) throws SQLException {
				FinancialContract financialContract = financialContractsMap.get(rs.getLong("financialContractId"));
				return buildGuaranteeOrderExcel(financialContract,rs);
			}
		});
		
		genericDaoSupport.query(sql, params, callBack);
		
		closeZipOutputStream(zip, response);
		
		exportEventLogModel.recordAfterLoadDataComplete(callBack);
		
		StringBuffer selectString = extractSelectString(paramsBean,new ArrayList<String>(financialContractUuidMap.keySet()));
		SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.GUARANTEEEXPORT,LogOperateType.EXPORT);
		log.setRecordContent("导出担保汇总表，导出记录"+callBack.getResultSize()+"条。"+"筛选条件："+selectString);
		systemOperateLogService.save(log);
		
		return exportEventLogModel;
	}
	
	private StringBuffer extractSelectString(GuaranteeOrderModel queryModel, List<String> financialContractUuids) {
		StringBuffer selectString = financialContractService.selectFinancialContract(financialContractUuids);
		if (queryModel.getGuaranteeStatusEnum() != null) {
			selectString.append("，担保状态【" + queryModel.getGuaranteeStatusEnum().getChineseMessage() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getStartDate())) {
			selectString.append("，计划还款起始日期【" + queryModel.getStartDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getEndDate())) {
			selectString.append("，计划还款终止日期【" + queryModel.getEndDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getDueStartDate())) {
			selectString.append("，担保截止起始日期【" + queryModel.getDueStartDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getDueEndDate())) {
			selectString.append("，担保截止终止日期【" + queryModel.getDueEndDate() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getOrderNo())) {
			selectString.append("，担保补足号【" + queryModel.getOrderNo() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getSingleLoanContractNo())) {
			selectString.append("，还款编号【" + queryModel.getSingleLoanContractNo() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getAppId())) {
			selectString.append("，商户编号【" + queryModel.getAppId() + "】");
		}
		return selectString;
	}
	
	private Map<String, Object> buildParams(GuaranteeOrderModel queryModel) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("financialContractIdList", queryModel.getFinancialContractIdList());
		params.put("outerRepaymentPlanNo", queryModel.getOuterRepaymentPlanNo());
		params.put("orderNo",queryModel.getOrderNo());
		params.put("dueStartDate",queryModel.getDueStartDate_date());
		params.put("dueEndDate",queryModel.getDueEndDate_date());
		if(null!=queryModel.getGuaranteeStatusEnum()){
			params.put("guaranteeStatus",queryModel.getGuaranteeStatusEnum().ordinal());
		}
		params.put("singleLoanContractNo",queryModel.getSingleLoanContractNo());
		params.put("startDate",queryModel.getStartDate_date());
		params.put("endDate",queryModel.getEndDate_date());
		params.put("appId",queryModel.getAppId());
		return params;
	}

	
	private  GuaranteeOrderExcel buildGuaranteeOrderExcel(FinancialContract financialContract ,ResultSet resultSet) throws SQLException {
		GuaranteeOrderExcel ge=new GuaranteeOrderExcel();
		ge.setOrderNo(resultSet.getString("orderNo"));
		ge.setSingleLoanContractNo(resultSet.getString("singleLoanContractNo"));
		ge.setOuterRepaymentPlanNo(resultSet.getString("outerRepaymentPlanNo"));
		Date assetRecycleDate = resultSet.getDate("assetRecycleDate");
		ge.setAssetRecycleDate(assetRecycleDate);
		ge.setDueDate(resultSet.getDate("dueDate"));
		ge.setAppId(financialContract.getApp().getAppId());
		ge.setUniqueId(resultSet.getString("uniqueId"));
		ge.setMonthFee(resultSet.getBigDecimal("assetInitialValue"));
		ge.setOverDueDays(getOverDueDaysByResultSet(resultSet,assetRecycleDate));
		ge.setModifyTime(resultSet.getTimestamp("modifyTime"));
		ge.setTotalRent(resultSet.getBigDecimal("totalRent"));
		ge.setGuaranteeStatus(EnumUtil.fromOrdinal(GuaranteeStatus.class, resultSet.getInt("guaranteeStatus")).getChineseMessage());
		ge.setIndex(resultSet.getRow());
		return ge;
	}
	private int getOverDueDaysByResultSet(ResultSet resultSet,Date assetRecycleDate) throws SQLException{
		String actualRecycleDate=resultSet.getString("actualRecycleDate");
		Date end = 	StringUtils.isNotEmpty(actualRecycleDate)?resultSet.getDate("actualRecycleDate"):new Date();
		int overDueDay = DateUtils.compareTwoDatesOnDay(end, assetRecycleDate);
		return overDueDay > 0 ? overDueDay : 0;
	}

	
}
