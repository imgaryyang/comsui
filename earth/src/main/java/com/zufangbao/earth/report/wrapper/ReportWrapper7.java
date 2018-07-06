package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.AppService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.SettlementStatus;
import com.zufangbao.sun.yunxin.entity.excel.SettlementOrderExcel;
import com.zufangbao.sun.yunxin.entity.model.SettlementOrderQueryModel;
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
public class ReportWrapper7 extends ReportBaseWrapper implements IReportWrapper<SettlementOrderQueryModel> {

	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Autowired
	private AppService appService;

	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Override
	public ExportEventLogModel wrap(SettlementOrderQueryModel paramsBean, HttpServletRequest request,
			HttpServletResponse response, ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {

		if (CollectionUtils.isEmpty(paramsBean.getFinancialContractIdList())) {
			return exportEventLogModel;
		}
		List<FinancialContract> financialContracts = financialContractService.getFinancialContractsByIds(paramsBean.getFinancialContractIdList());
		Map<String, FinancialContract> financialContractUuidMap = financialContracts.stream().collect(Collectors.toMap(FinancialContract::getFinancialContractUuid, fc -> fc));

		Map<String, Object> params = buildParams(paramsBean);
		String sql = getCachedSql("reportWrapper7", params);

		exportEventLogModel.recordStartLoadDataTime();

		// 加载数据
		ZipOutputStream zip = openZipOutputStream(response,
				String.format("结清单详情_%s.zip", DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS")));
		PrintWriter printWriter = putNextZipEntry(zip, "结清单详情");
		ExportableRowCallBackHandler<SettlementOrderExcel> callBack = new ExportableRowCallBackHandler<SettlementOrderExcel>(
				SettlementOrderExcel.class, printWriter, new ReportVOBuilder<SettlementOrderExcel>() {
					@Override
					public SettlementOrderExcel buildRow(ResultSet rs) throws SQLException {
						return buildSettlementOrderExcel(rs);
					}
				});

		genericDaoSupport.query(sql, params, callBack);
		// 写出报表
		closeZipOutputStream(zip, response);
		exportEventLogModel.recordAfterLoadDataComplete(callBack);

		StringBuffer selectString = extractSelectString(paramsBean,new ArrayList<String>(financialContractUuidMap.keySet()));
		SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.EXPORTSETTLEMENTORDER,LogOperateType.EXPORT);
		log.setRecordContent("导出清算汇总表，导出记录"+callBack.getResultSize()+"条。"+"筛选条件："+selectString);
		systemOperateLogService.save(log);

		return exportEventLogModel;
	}

	private StringBuffer extractSelectString(SettlementOrderQueryModel queryModel, List<String> financialContractUuids) {
		StringBuffer selectString = financialContractService.selectFinancialContract(financialContractUuids);
		if (queryModel.getSettlementStatusEnum() != null) {
			selectString.append("，清算状态【" + queryModel.getSettlementStatusEnum().getChineseMessage() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getSettlementOrderNo())) {
			selectString.append("，清算单号【" + queryModel.getSettlementOrderNo() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getRepaymentNo())) {
			selectString.append("，还款编号【" + queryModel.getRepaymentNo() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getAppNo())) {
			selectString.append("，商户编号【" + queryModel.getAppNo() + "】");
		}
		return selectString;
	}

	private SettlementOrderExcel buildSettlementOrderExcel(ResultSet rs) throws SQLException {
		SettlementOrderExcel settlementOrderExcel = new SettlementOrderExcel();
		settlementOrderExcel.setSettleOrderNo(rs.getString("settleOrderNo"));
		settlementOrderExcel.setRepaymentNo(rs.getString("repaymentNo"));
		settlementOrderExcel.setRecycleDate(rs.getTimestamp("recycleDate"));
		settlementOrderExcel.setDueDate(rs.getTimestamp("dueDate"));
		settlementOrderExcel.setUniqueId(rs.getString("uniqueId"));
		settlementOrderExcel.setAppId(getAppId(rs.getLong("appId")));
		settlementOrderExcel.setPrincipalAndInterestAmount(rs.getBigDecimal("principalAndInterestAmount"));
		settlementOrderExcel.setOverdueDays(rs.getInt("overdueDays"));
		settlementOrderExcel.setOverduePenalty(rs.getBigDecimal("overduePenalty"));
		settlementOrderExcel.setModifyTime(rs.getTimestamp("modifyTime"));
		settlementOrderExcel.setSettlementAmount(rs.getBigDecimal("settlementAmount"));
		settlementOrderExcel
				.setSettlementStatus(EnumUtil.fromOrdinal(SettlementStatus.class, rs.getInt("settlementStatus")));
		settlementOrderExcel.setComment(rs.getString("comment"));
		return settlementOrderExcel;
	}

	private Map<String, Object> buildParams(SettlementOrderQueryModel paramsBean) {

		Map<String, Object> paramMap = new HashMap<String, Object>();

		List<String> financialContractIdList = financialContractService
				.getFinancialContractUuidsByIds(paramsBean.getFinancialContractIdList());
		paramMap.put("financialContractIdList", (financialContractIdList.size()) > 0 ? financialContractIdList : -1);

		if (paramsBean.getSettlementStatusEnum() != null) {
			paramMap.put("settlementStatus", paramsBean.getSettlementStatus());
		}
		if (paramsBean.getOuterRepaymentPlanNo() != null) {
			paramMap.put("outerRepaymentPlanNo", paramsBean.getOuterRepaymentPlanNo());
		}
		if (!StringUtils.isEmpty(paramsBean.getSettlementOrderNo())) {
			paramMap.put("settleOrderNo", paramsBean.getSettlementOrderNo());
		}
		if (!StringUtils.isEmpty(paramsBean.getRepaymentNo())) {
			paramMap.put("singleLoanContractNo", paramsBean.getRepaymentNo());
		}

		if (!StringUtils.isEmpty(paramsBean.getAppNo())) {
			Long appId = appId2Id(paramsBean.getAppNo());
			if (appId == null) {
				paramMap.put("appId", -1);
			} else {
				paramMap.put("appId", appId);
			}
		}
		return paramMap;
	}

	private Map<Long, String> appId2IdMap = new HashMap<Long, String>();

	private String getAppId(Long id) {
		if (appId2IdMap.containsKey(id)) {
			return appId2IdMap.get(id);
		} else {
			App app = appService.getAppById(id);
			appId2IdMap.put(app.getId(), app.getAppId());
			return app.getAppId();
		}
	}

	private Long appId2Id(String appNo) {
		App app = appService.getApp(appNo);
		if (null != app) {
			appId2IdMap.put(app.getId(), app.getAppId());
		}
		return (null != app) ? app.getId() : null;
	}

}
