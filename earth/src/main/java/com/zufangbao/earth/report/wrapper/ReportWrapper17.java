package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.gluon.api.swissre.institutionrecon.ThirdPartVoucherDetailModelCopy;
import com.zufangbao.gluon.api.swissre.institutionrecon.ThirdPartVoucherRepayDetailModelCopy;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.excel.ThirdPartyPayVoucherVO;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VerificationStatus;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogIssueStatus;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogStatus;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyVoucherCommandLogSpec;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.api.util.IpUtils;
import com.zufangbao.wellsfargo.yunxin.model.ThirdPartyVoucherCommandLogQueryModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

@Component
public class ReportWrapper17 extends ReportBaseWrapper
		implements IReportWrapper<ThirdPartyVoucherCommandLogQueryModel> {

	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	private final DecimalFormat decimalFormat=new DecimalFormat(",###,##0.00");

	@Override
	public ExportEventLogModel wrap(ThirdPartyVoucherCommandLogQueryModel paramsBean, HttpServletRequest request,
			HttpServletResponse response, ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {
		
		
		Map<String, Object> params = buildParams(paramsBean);

		String sql = getCachedSql("reportWrapper17", params);
		exportEventLogModel.recordStartLoadDataTime();

		// 加载数据
		String fileName = "第三方支付凭证表" + "_" + DateUtils.format(new Date(), "yyyyMMdd_HHmmss");
		ZipOutputStream zip = openZipOutputStream(response, fileName + ".zip");
		PrintWriter printWriter = putNextZipEntry(zip, fileName);

		ExportableRowCallBackHandlerList<ThirdPartyPayVoucherVO> callBack = new ExportableRowCallBackHandlerList<ThirdPartyPayVoucherVO>(
				ThirdPartyPayVoucherVO.class, printWriter, new ReportVOBuilderList<ThirdPartyPayVoucherVO>() {
					@Override
					public List<ThirdPartyPayVoucherVO> buildRow(ResultSet rs) throws SQLException {
						return buildCashFlowExcelVO(rs);
					}
				});

		genericDaoSupport.query(sql, params, callBack);
		// 写出报表
		closeZipOutputStream(zip, response);
		exportEventLogModel.recordAfterLoadDataComplete(callBack);

		StringBuffer selectString = extractSelectString(paramsBean);
		SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.EXPORTTHIRDPARTYVOUCHER,LogOperateType.EXPORT);
		log.setRecordContent("导出第三方支付凭证，导出记录"+callBack.getResultSize()+"条。"+"筛选条件："+selectString);
		systemOperateLogService.save(log);
		
		return exportEventLogModel;
	}
	
	private StringBuffer extractSelectString(ThirdPartyVoucherCommandLogQueryModel queryModel) {
		StringBuffer selectString = financialContractService.selectFinancialContract(queryModel.getFinancialContractUuidList());
		if (queryModel.getTranscationGatewayEnum() != null) {
			selectString.append("，交易网关【" + queryModel.getTranscationGatewayEnum().getChineseMessage() + "】");
		}
		if (queryModel.getVoucherLogStatusEnum() != null) {
			selectString.append("，校验状态【" + queryModel.getVoucherLogStatusEnum().getChineseMessage() + "】");
		}
		if (queryModel.getVoucherLogIssueStatusEnum() != null) {
			selectString.append("，核销状态【" + queryModel.getVoucherLogIssueStatusEnum().getChineseMessage() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getStartTime())) {
			selectString.append("，起始时间【" + queryModel.getStartTime() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getEndTime())) {
			selectString.append("，终止时间【" + queryModel.getEndTime() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getVoucherUuid())) {
			selectString.append("，凭证编号【" + queryModel.getVoucherUuid() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getTradeUuid())) {
			selectString.append("，交易请求号【" + queryModel.getTradeUuid() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getReceivableAccountNo())) {
			selectString.append("，清算账号【" + queryModel.getReceivableAccountNo() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getPaymentName())) {
			selectString.append("，机构账户名【" + queryModel.getPaymentName() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getPaymentAccountNo())) {
			selectString.append("，机构账户号【" + queryModel.getPaymentAccountNo() + "】");
		}
		return selectString;
	}

	private List<ThirdPartyPayVoucherVO> buildCashFlowExcelVO(ResultSet rs) throws SQLException {

		String voucherUuid = rs.getString("voucher_uuid");
		String voucherNo=rs.getString("voucher_no");
		String tradeUuid=rs.getString("trade_uuid");
		String createTime = rs.getTimestamp("create_time") != null
				? DateUtils.format(rs.getTimestamp("create_time"), DateUtils.LONG_DATE_FORMAT) : StringUtils.EMPTY;
		String statusModifyTime = rs.getTimestamp("status_modify_time") != null
				? DateUtils.format(rs.getTimestamp("status_modify_time"), DateUtils.LONG_DATE_FORMAT)
				: StringUtils.EMPTY;
		String thirdPartVoucherContent = rs.getString("third_part_voucher_content");
		int voucherLogStatusInt = rs.getInt("voucher_log_status");
		int voucherLogIssueStatusInt = rs.getInt("voucher_log_issue_status");
		String batchNo = rs.getString("batch_no");
		String errMsg = rs.getString("error_message");
		List<ThirdPartyPayVoucherVO> voList = wiredThirdPartyPayVoucherVO(thirdPartVoucherContent, voucherUuid,voucherNo,tradeUuid,
				createTime, statusModifyTime, voucherLogStatusInt, voucherLogIssueStatusInt, batchNo, errMsg);
		return voList;
	}

	private List<ThirdPartyPayVoucherVO> wiredThirdPartyPayVoucherVO(String thirdPartVoucherContent, String voucherUuid,String voucherNo,String tradeUuid,
			String createTime, String statusModifyTime, int voucherLogStatusInt, int voucherLogIssueStatusInt, String batchNo, String errMsg) {

		List<ThirdPartyPayVoucherVO> voList = new ArrayList<>();
		if (StringUtils.isEmpty(thirdPartVoucherContent))
			return voList;
		ThirdPartVoucherDetailModelCopy detailModel = JsonUtils.parse(thirdPartVoucherContent,
				ThirdPartVoucherDetailModelCopy.class);
		if (detailModel == null || StringUtils.isEmpty(detailModel.getRepayDetailList()))
			return voList;
		List<ThirdPartVoucherRepayDetailModelCopy> copys = JsonUtils.parseArray(detailModel.getRepayDetailList(),
				ThirdPartVoucherRepayDetailModelCopy.class);
		if (CollectionUtils.isEmpty(copys))
			return voList;
		for (ThirdPartVoucherRepayDetailModelCopy copy : copys) {
			if (copy == null)
				continue;
			String repaymentPlanNo = copy.getRepaymentPlanNo();
			String outerRepaymentPlanNo = copy.getOuterRepaymentPlanNo();
			ThirdPartyPayVoucherVO vo = new ThirdPartyPayVoucherVO(voucherUuid,voucherNo,tradeUuid,
					getVoucherLogStatusMessage(voucherLogStatusInt),
					getVoucherLogIssueStatusMessage(voucherLogIssueStatusInt), createTime, statusModifyTime,
					repaymentPlanNo, outerRepaymentPlanNo, getAssetRecycleDate(repaymentPlanNo), formatBigDecimal(copy.getPrincipal()),
					formatBigDecimal(copy.getInterest()), formatBigDecimal(copy.getServiceCharge()),
					formatBigDecimal(copy.getMaintenanceCharge()), formatBigDecimal(copy.getOtherCharge()),
					formatBigDecimal(copy.getPenaltyFee()), formatBigDecimal(copy.getLatePenalty()),
					formatBigDecimal(copy.getLateFee()), formatBigDecimal(copy.getLateFee()),
					formatBigDecimal(copy.getAmount()), batchNo, errMsg);
			voList.add(vo);
		}
		return voList;
	}

	private String getVoucherLogStatusMessage(int voucherLogStatusInt) {
		return ThirdPartyVoucherCommandLogSpec.VoucherLogStatusToVerificationStatus
				.getOrDefault(EnumUtil.fromOrdinal(VoucherLogStatus.class, voucherLogStatusInt),
						VerificationStatus.UNCHECK)
				.getChineseMessage();
	}

	private String getVoucherLogIssueStatusMessage(int voucherLogIssueStatusInt) {
		VoucherLogIssueStatus voucherLogIssueStatus = EnumUtil.fromOrdinal(VoucherLogIssueStatus.class,
				voucherLogIssueStatusInt);
		return voucherLogIssueStatus != null ? voucherLogIssueStatus.getChineseMessage() : StringUtils.EMPTY;
	}

	private String formatBigDecimal(BigDecimal bigDecimal) {
		if (null == bigDecimal)
			return StringUtils.EMPTY;
		return	decimalFormat.format(bigDecimal);
	}

	private String getAssetRecycleDate(String repaymentPlanNo) {
		if (StringUtils.isEmpty(repaymentPlanNo))
			return StringUtils.EMPTY;
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentPlanNo(repaymentPlanNo);
		if (null == assetSet)
			return StringUtils.EMPTY;
		Date assetRecycleDate = assetSet.getAssetRecycleDate();
		if (null == assetRecycleDate)
			return StringUtils.EMPTY;
		return DateUtils.format(assetRecycleDate, DateUtils.DATE_FORMAT);
	}

	private Map<String, Object> buildParams(ThirdPartyVoucherCommandLogQueryModel paramsBean) {
		List<VoucherLogStatus> voucherLogStatus = ThirdPartyVoucherCommandLogSpec.VerificationStatusToVoucherLogStatus
				.get(paramsBean.getVoucherLogStatusEnum());
		Map<String, Object> params = new HashMap<>();
		if (CollectionUtils.isNotEmpty(paramsBean.getFinancialContractUuidList())) {
			params.put("financialContractUuids", paramsBean.getFinancialContractUuidList());
		}
		if (null != paramsBean.getTranscationGateway())
			params.put("transcationGateway", paramsBean.getTranscationGateway());
		if (CollectionUtils.isNotEmpty(voucherLogStatus)){
			List<Integer> voucherLogStatusIntegerList=voucherLogStatus.stream().filter(b->b!=null).map(VoucherLogStatus::getOrdinal).collect(Collectors.toList());
			params.put("voucherLogStatus", voucherLogStatusIntegerList);
		}
		if (null != paramsBean.getVoucherLogIssueStatus())
			params.put("voucherLogIssueStatus", paramsBean.getVoucherLogIssueStatus());
		if (StringUtils.isNotEmpty(paramsBean.getReceivableAccountNo()))
			params.put("receivableAccountNo", "%" + paramsBean.getReceivableAccountNo() + "%");
		if (StringUtils.isNotEmpty(paramsBean.getPaymentName()))
			params.put("paymentName", "%" + paramsBean.getPaymentName() + "%");
		if (StringUtils.isNotEmpty(paramsBean.getPaymentAccountNo()))
			params.put("paymentAccountNo", "%" + paramsBean.getPaymentAccountNo() + "%");
		if (StringUtils.isNotEmpty(paramsBean.getVoucherUuid()))
			params.put("voucherUuid", "%" + paramsBean.getVoucherUuid() + "%");
		if (StringUtils.isNotEmpty(paramsBean.getTradeUuid()))
			params.put("tradeUuid", "%" + paramsBean.getTradeUuid() + "%");
		if (null != paramsBean.getStartTimeDate())
			params.put("startTime", paramsBean.getStartTimeDate());
		if (null != paramsBean.getEndTimeDate())
			params.put("endTime", paramsBean.getEndTimeDate());
		return params;
	}

}
