package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.report.model.Report13AssetSetModel;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.AssetClearStatus;
import com.zufangbao.sun.yunxin.entity.OnAccountStatus;
import com.zufangbao.sun.yunxin.entity.RepaymentWay;
import com.zufangbao.sun.yunxin.entity.excel.ProjectInformationExeclVo;
import com.zufangbao.sun.yunxin.entity.model.ProjectInformationQueryModel;
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
public class ReportWrapper13 extends ReportBaseWrapper implements IReportWrapper<ProjectInformationQueryModel> {

	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Override
	public ExportEventLogModel wrap(ProjectInformationQueryModel paramsBean, HttpServletRequest request,
			HttpServletResponse response, ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {

		if (CollectionUtils.isEmpty(paramsBean.getFinancialContractUuidList())) {
			return exportEventLogModel;
		}

		Map<String, Object> params = buildParams(paramsBean);

		String sql = getCachedSql("reportWrapper13_assetMap", params);
		exportEventLogModel.recordStartLoadDataTime();
		// 加载数据
		String fileName = "项目信息表" + "_" + DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") + ".zip";
		Map<Long, List<Report13AssetSetModel>> assetSetMap = getGroupedAssetSetMap(sql, params);
		ZipOutputStream zip = openZipOutputStream(response, fileName);
		PrintWriter printWriter = putNextZipEntry(zip, "项目信息表");

		sql = getCachedSql("reportWrapper13_base", params);
		ExportableRowCallBackHandler<ProjectInformationExeclVo> callBack = new ExportableRowCallBackHandler<ProjectInformationExeclVo>(
				ProjectInformationExeclVo.class, printWriter, new ReportVOBuilder<ProjectInformationExeclVo>() {
					@Override
					public ProjectInformationExeclVo buildRow(ResultSet rs) throws SQLException {
						return buildProjectInformationExeclVo(rs, assetSetMap);
					}
				});

		genericDaoSupport.query(sql, params, callBack);
		// 写出报表
		closeZipOutputStream(zip, response);
		exportEventLogModel.recordAfterLoadDataComplete(callBack);

		return exportEventLogModel;
	}

	private ProjectInformationExeclVo buildProjectInformationExeclVo(ResultSet rs,
			Map<Long, List<Report13AssetSetModel>> assetSetMap) throws SQLException {
		ProjectInformationExeclVo execlVo = new ProjectInformationExeclVo();
		execlVo.setContractNo(rs.getString("contractNo"));
		execlVo.setLoanRate(rs.getString("interestRate"));
		execlVo.setLoanAmount(rs.getString("totalAmount"));
		execlVo.setLoanType(EnumUtil.fromOrdinal(RepaymentWay.class, rs.getInt("repaymentWay")).getChineseMessage());
		execlVo.setEffectDate(getTimeStr(rs.getTimestamp("beginDate")));
		execlVo.setRepaymentCycle(rs.getInt("paymentFrequency"));
		execlVo.setCustomerName(rs.getString("customerName"));
		execlVo.setExpectTerminalDate(rs.getString("maxAssetRecycleDate"));

		Long contractId = rs.getLong("contractId");
		List<Report13AssetSetModel> assetSets = assetSetMap.getOrDefault(contractId, Collections.emptyList());
		String actualTermainalDate = getActualTermainalDate(assetSets);
		if (actualTermainalDate != null) {
			execlVo.setActualTermainalDate(actualTermainalDate);
		}

		String periods = rs.getString("periods");
		Date today = DateUtils.getToday();
		Report13AssetSetModel currentAssetSet = selectCurrentAssetSet(assetSets, today, periods);
		execlVo.setRepaymentSchedule(calcRepaymentProgress(assetSets, today, currentAssetSet));
		if (currentAssetSet != null) {
			execlVo.setCurrentPeriodRepaymentAmount(currentAssetSet.getAssetPrincipalValueStr());
			execlVo.setCurrentPeriodRepaymentInterest(currentAssetSet.getAssetInterestValueStr());
			execlVo.setRepaymentSituation(isPaidOff(currentAssetSet) ? "已还" : "未还");
			execlVo.setRepaymentDate(currentAssetSet.getAssetRecycleDateStr());
		}
		return execlVo;
	}

	private String calcRepaymentProgress(List<Report13AssetSetModel> assetSets, Date today,
			Report13AssetSetModel currentAssetSet) {
		int allPeriods = assetSets.size();
		int executed_period_number = assetSets.size();
		if (currentAssetSet != null) {
			executed_period_number = countExecutedPeriodNumber(today, currentAssetSet);
		}
		return executed_period_number + "/" + allPeriods;
	}

	private int countExecutedPeriodNumber(Date today, Report13AssetSetModel assetSet) {
		Date assetRecycleDate = assetSet.getAssetRecycleDate();
		int currentPeriod = assetSet.getCurrentPeriod();
		return assetRecycleDate.equals(today) ? currentPeriod : currentPeriod - 1;
	}

	private Report13AssetSetModel selectCurrentAssetSet(List<Report13AssetSetModel> assetSetModels, Date reference_date,
			String periods) {
		for (Report13AssetSetModel assetSetModel : assetSetModels) {
			if (isPaidOff(assetSetModel)) {
				continue;
			}
			if (isCurrentAssetSet(reference_date, assetSetModel, periods)) {
				return assetSetModel;
			}
		}
		return null;
	}

	private boolean isPaidOff(Report13AssetSetModel assetSetModel) {
		return assetSetModel.getAssetStatus() == AssetClearStatus.CLEAR.ordinal()
				&& assetSetModel.getOnAccountStatus() == OnAccountStatus.WRITE_OFF.ordinal();
	}

	private boolean isCurrentAssetSet(Date reference_date,
			Report13AssetSetModel assetSetModel, String periods) {
		if (StringUtils.equals(periods, assetSetModel.getCurrentPeriodStr())) {
			return true;
		}
		Date assetRecycleDate = assetSetModel.getAssetRecycleDate();
		return reference_date.before(assetRecycleDate) || assetRecycleDate.equals(reference_date);
	}

	private String getActualTermainalDate(List<Report13AssetSetModel> assetSets) {
		if (CollectionUtils.isEmpty(assetSets)) {
			return null;
		}
		Optional<Report13AssetSetModel> optional = assetSets.stream().max((a1, a2) -> Integer.compare(a1.getCurrentPeriod(), a2.getCurrentPeriod()));
		if(!optional.isPresent()) {
			return null;
		}
		Report13AssetSetModel model = optional.get();
		if(model.getAssetStatus() == AssetClearStatus.CLEAR.ordinal()) {
			return model.getActualRecycleDateStr();
		}
		return null;
	}

	private String getTimeStr(Date date) {
		return null == date ? StringUtils.EMPTY : DateUtils.format(date, "yyyy-MM-dd");
	}

	private Map<Long, List<Report13AssetSetModel>> getGroupedAssetSetMap(String sql,
			Map<String, Object> paramMap) {
		List<Report13AssetSetModel> list = genericDaoSupport.queryForList(sql, paramMap, Report13AssetSetModel.class);
		return list.stream().collect(Collectors.groupingBy(Report13AssetSetModel::getContractId));
	}

	private Map<String, Object> buildParams(ProjectInformationQueryModel paramsBean) {
		Map<String, Object> params = new HashMap<>();
		params.put("financialContractUuidList", paramsBean.getFinancialContractUuidList());

		if (!StringUtils.isEmpty(paramsBean.getContractNo())) {
			params.put("contractNo", paramsBean.getContractNo());
		}
		if (!StringUtils.isEmpty(paramsBean.getCustomerName())) {
			params.put("customerName", paramsBean.getCustomerName());
		}
		if (paramsBean.getLoanEffectStartDate() != null) {
			params.put("loanEffectStartDate", paramsBean.getLoanEffectStartDate());
		}
		if (paramsBean.getLoanEffectEndDate() != null) {
			params.put("loanEffectEndDate", paramsBean.getLoanEffectEndDateAddOneDay());
		}
		if (paramsBean.getLoanExpectTerminateStartDate() != null) {
			params.put("loanExpectTerminateStartDate", paramsBean.getLoanExpectTerminateStartDate());
		}
		if (paramsBean.getLoanExpectTerminateEndDate() != null) {
			params.put("loanExpectTerminateEndDate", paramsBean.getLoanExpectTerminateEndDateAddOneDay());
		}
		return params;
	}

}
