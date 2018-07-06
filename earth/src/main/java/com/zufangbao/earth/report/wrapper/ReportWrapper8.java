package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.AppService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.excel.RepurchaseDetailExcelVO;
import com.zufangbao.sun.yunxin.entity.model.repurchase.RepurchaseQueryModel;
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
public class ReportWrapper8 extends ReportBaseWrapper implements IReportWrapper<RepurchaseQueryModel> {

	@Autowired
	private SystemOperateLogService systemOperateLogService;
	@Autowired
	private AppService appService;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Override
	public ExportEventLogModel wrap(RepurchaseQueryModel paramsBean, HttpServletRequest request,
			HttpServletResponse response, ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {

		if (CollectionUtils.isEmpty(paramsBean.getFinancialContractUuidList())) {
			return exportEventLogModel;
		}
		
		Map<String, Object> params = buildParams(paramsBean);
		String sql = getCachedSql("reportWrapper8", params);
		
		exportEventLogModel.recordStartLoadDataTime();
		
		// 加载数据
		ZipOutputStream zip = openZipOutputStream(response, getFileName("回购信息表", "zip"));
		PrintWriter printWriter = putNextZipEntry(zip, "回购信息表");
		
		ExportableRowCallBackHandler<RepurchaseDetailExcelVO> callBack = new ExportableRowCallBackHandler<RepurchaseDetailExcelVO>(
				RepurchaseDetailExcelVO.class, printWriter, new ReportVOBuilder<RepurchaseDetailExcelVO>() {
					@Override
					public RepurchaseDetailExcelVO buildRow(ResultSet rs) throws SQLException {
						return buildRepurchaseShowModel(rs);
					}
				});

		genericDaoSupport.query(sql, params, callBack);
		
		// 写出报表
		closeZipOutputStream(zip, response);
		
		exportEventLogModel.recordAfterLoadDataComplete(callBack);
		
		StringBuffer selectString = extractSelectString(paramsBean);
		SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.REPURCHASEDOCEXPORT,LogOperateType.EXPORT);
		log.setRecordContent("导出回购汇总表，导出记录"+callBack.getResultSize()+"条。"+"筛选条件："+selectString);
		systemOperateLogService.save(log);

		return exportEventLogModel;
	}

	private StringBuffer extractSelectString(RepurchaseQueryModel queryModel) {
		StringBuffer selectString = financialContractService.selectFinancialContract(queryModel.getFinancialContractUuidList());
		App app = appService.getAppById(queryModel.getAppId());
		if (app != null) {
			selectString.append("，商户名称【" + app.getName() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getRepoStartDate())) {
			selectString.append("，回购起始日【" + queryModel.getRepoStartDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getRepoEndDate())) {
			selectString.append("，回购截止日【" + queryModel.getRepoEndDate() + "】");
		}
		List<RepurchaseStatus> repurchaseStatus = queryModel.getRepurchaseStatusEnumList();
		if (CollectionUtils.isNotEmpty(repurchaseStatus)) {
			selectString.append("，回购状态");
			for (RepurchaseStatus status : repurchaseStatus) {
				selectString.append("【" + status.getChineseMessage() + "】");
			}
		}
		if (StringUtils.isNotBlank(queryModel.getContractNo())) {
			selectString.append("，贷款合同编号【" + queryModel.getContractNo() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getCustomerName())) {
			selectString.append("，客户姓名【" + queryModel.getCustomerName() + "】");
		}
		return selectString;
	}

	private Map<String, Object> buildParams(RepurchaseQueryModel paramsBean) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("fcList", paramsBean.getFinancialContractUuidList());
		
		Long appId = paramsBean.getAppId();
		if (appId != null && !appId.equals(-1L)) {
			paramMap.put("appId", paramsBean.getAppId());
		}
		if (StringUtils.isNotBlank(paramsBean.getContractNo())) {
			paramMap.put("contractNo", paramsBean.getContractNo());
		}
		if (StringUtils.isNotBlank(paramsBean.getBatchNo())) {
			paramMap.put("batchNo", paramsBean.getBatchNo());
		}
		if (StringUtils.isNotBlank(paramsBean.getCustomerName())) {
			paramMap.put("customerName", paramsBean.getCustomerName());
		}
		if (paramsBean.getRepoStartDateValue() != null) {
			paramMap.put("repoStartDate", paramsBean.getRepoStartDateValue());
		}
		if (paramsBean.getRepoEndDateValue() != null) {
			paramMap.put("repoEndDate", paramsBean.getRepoEndDateValue());
		}
		
		List<Integer> repurchaseStatus = paramsBean.getRepurchaseStatusEnumList().stream().map(s -> s.ordinal()).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(repurchaseStatus)) {
			paramMap.put("repurchaseStatusList", repurchaseStatus);
		}
		return paramMap;
	}
	
	private RepurchaseDetailExcelVO buildRepurchaseShowModel(ResultSet rs) throws SQLException {
		RepurchaseDetailExcelVO vo = new RepurchaseDetailExcelVO();
		vo.setUniqueId(rs.getString("uniqueId"));
		vo.setRepurchaseDocUuid(rs.getString("repurchaseDocUuid"));
		vo.setContractNo(rs.getString("contractNo"));
		vo.setBatchNo(rs.getString("batchNo"));
		vo.setRepoStartDate(rs.getString("repoStartDate"));
		vo.setRepoEndDate(rs.getString("repoEndDate"));
		vo.setAppName(rs.getString("appName"));
		vo.setCustomerName(rs.getString("customerName"));
		vo.setAmount(rs.getBigDecimal("amount"));
		vo.setRepurchasePrincipal(rs.getBigDecimal("repurchasePrincipal"));
		vo.setRepurchaseInterest(rs.getBigDecimal("repurchaseInterest"));
		vo.setRepurchasePenalty(rs.getBigDecimal("repurchasePenalty"));
		vo.setRepurchaseOtherCharges(rs.getBigDecimal("repurchaseOtherCharges"));
		vo.setRepoDays(rs.getInt("repoDays"));
		vo.setCreatTime(rs.getTimestamp("createTime"));
		RepurchaseStatus repurchaseStatus = EnumUtil.fromOrdinal(RepurchaseStatus.class, rs.getInt("repoStatus")) ;
		if(repurchaseStatus != null) {
			vo.setRepoStatus(repurchaseStatus.getChineseMessage());
		}
		return vo;
	}
	
	private String getFileName(String fileName,String format) {
		return String.format("%s_%s.%s", fileName,DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") ,format);
	}

}
