package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.AgeUtil;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.SensitiveInfoUtils;
import com.zufangbao.sun.yunxin.entity.ContractStateAdaptaterView;
import com.zufangbao.sun.yunxin.entity.RepaymentWay;
import com.zufangbao.sun.yunxin.entity.excel.LoanContractDetailCheckExcelVO;
import com.zufangbao.sun.yunxin.entity.model.ContractQueryModel;
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
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;


@Component
public class ReportWrapper1 extends ReportBaseWrapper implements IReportWrapper<ContractQueryModel> {

	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	@Override
	public ExportEventLogModel wrap(ContractQueryModel queryModel,
			HttpServletRequest request, HttpServletResponse response,
			ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {
		
		if (CollectionUtils.isEmpty(queryModel.getFinancialContractUuidList())) {
			return exportEventLogModel;
		}
		if (CollectionUtils.isEmpty(queryModel.getContractStatusAdaptaterViewList())) {
			return exportEventLogModel;
		}

		Map<String, Object> params = buildParams(queryModel);
		String sql = getCachedSql("reportWrapper1", params);
		
		exportEventLogModel.recordStartLoadDataTime();

		List<FinancialContract> financialContracts = financialContractService.getFinancialContractsByUuids(queryModel.getFinancialContractUuidList());
		Map<String, FinancialContract> financialContractsMap = financialContracts.stream().collect(Collectors.toMap(FinancialContract::getFinancialContractUuid, fc -> fc));
		
		String fileName = String.format("贷款信息表_%s.zip", DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS"));
		ZipOutputStream zip = openZipOutputStream(response, fileName);
		PrintWriter printWriter = putNextZipEntry(zip, "贷款信息表");
		
		ExportableRowCallBackHandler<LoanContractDetailCheckExcelVO> callBack = new ExportableRowCallBackHandler<LoanContractDetailCheckExcelVO>(LoanContractDetailCheckExcelVO.class, printWriter, new ReportVOBuilder<LoanContractDetailCheckExcelVO>() {
			@Override
			public LoanContractDetailCheckExcelVO buildRow(ResultSet rs) throws SQLException {
				FinancialContract financialContract = financialContractsMap.get(rs.getString("financial_contract_uuid"));
				return buildLoanContractDetailCheckExcelVO(financialContract, rs, queryModel.getExportTags());
			}
		});
		
		genericDaoSupport.query(sql, params, callBack);
		
		closeZipOutputStream(zip, response);
		
		exportEventLogModel.recordAfterLoadDataComplete(callBack);

		StringBuffer selectString = extractSelectString(queryModel);
		SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.EXPORTCONTRACT,LogOperateType.EXPORT);
		log.setRecordContent("导出贷款合同，导出记录"+callBack.getResultSize()+"条。"+"筛选条件："+selectString);
		systemOperateLogService.save(log);
		return exportEventLogModel;
	}

	private StringBuffer extractSelectString(ContractQueryModel queryModel) {
		StringBuffer selectString = financialContractService.selectFinancialContract(queryModel.getFinancialContractUuidList());
		List<ContractStateAdaptaterView> contractStates = queryModel.getContractStatusAdaptaterViewList();
		if (CollectionUtils.isNotEmpty(contractStates)) {
			selectString.append("，合同状态");
			for (ContractStateAdaptaterView contractState : contractStates) {
				selectString.append("【" + contractState.getChineseMessage() + "】");
			}
		}
		if (StringUtils.isNotEmpty(queryModel.getStartDateString())) {
			selectString.append("，生效起始日期【" + queryModel.getStartDateString() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getEndDateString())) {
			selectString.append("，生效终止日期【" + queryModel.getEndDateString() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getContractNo())) {
			selectString.append("，贷款合同编号【" + queryModel.getContractNo() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getCustomerName())) {
			selectString.append("，贷款人【" + queryModel.getCustomerName() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getUniqueId())) {
			selectString.append("，贷款合同唯一识别码【" + queryModel.getUniqueId() + "】");
		}
		return selectString;
	}

	private Map<String, Object> buildParams(ContractQueryModel queryModel) {
		List<ContractStateAdaptaterView> contractStatesAdaptater = queryModel.getContractStatusAdaptaterViewList();
		if(contractStatesAdaptater.contains(ContractStateAdaptaterView.INVALIDATE)) {
			contractStatesAdaptater.remove(ContractStateAdaptaterView.INVALIDATE);
		}
		List<String> financialContractUuids = queryModel.getFinancialContractUuidList();
		List<Integer> stateAdaptaterList = contractStatesAdaptater.stream().map(s -> s.ordinal()).collect(Collectors.toList());
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("financialContractUuids", financialContractUuids);
		paramMap.put("stateAdaptaterList", stateAdaptaterList);
		paramMap.put("contractNo", queryModel.getContractNo());
		paramMap.put("startDate", queryModel.getStartDate());
		paramMap.put("endDate", queryModel.getEndDate());
		paramMap.put("customerName", queryModel.getCustomerName());
		paramMap.put("uniqueId", queryModel.getUniqueId());
		paramMap.put("exportTags", queryModel.getExportTags());
		return paramMap;
	}
	
	private LoanContractDetailCheckExcelVO buildLoanContractDetailCheckExcelVO(FinancialContract financialContract, ResultSet rs, Boolean exportTags) throws SQLException {
		LoanContractDetailCheckExcelVO vo = new LoanContractDetailCheckExcelVO();
		vo.setUniqueId(rs.getString("uniqueId"));
		vo.setFinancialContractNo(financialContract.getContractNo());
		vo.setAppName(financialContract.getApp().getAppId());
		vo.setAccountNo(SensitiveInfoUtils.desensitizationString(financialContract.getCapitalAccount().getAccountNo()));
		vo.setContractNo(rs.getString("contractNo"));
//		vo.setCpBankAccountHolder(rs.getString("cpBankAccountHolder"));
		vo.setCustomerName(rs.getString("payerName"));
		vo.setLoanDate(rs.getString("beginDate"));
		vo.setTotalAmount(rs.getString("totalPrincipal"));

		vo.setTotalInterest(rs.getString("totalInterest"));
		vo.setDueDate(rs.getString("dueDate"));

//		String dataArray = rs.getString("dataArray");
//		//dataArray 2项数据依次为 到期日期、贷款利息总额
//		String[] datas = StringUtils.split(dataArray, ",");
//		if(ArrayUtils.isNotEmpty(datas)) {
//			vo.setDueDate(datas[0]);
//			vo.setTotalInterest(datas[1]);
//		}

		String restFeeArray = rs.getString("restFeeArray");
		//restFeeArray 2项数据依次为 剩余本金、剩余利息
		String[] restFees = StringUtils.split(restFeeArray, ",");
		if(ArrayUtils.isNotEmpty(restFees)) {
			vo.setRestSumPrincipal(restFees[0]);
			vo.setRestSumInterest(restFees[1]);
		}

		vo.setPeriods(rs.getInt("periods"));
		int repaymentWayInt = rs.getInt("repaymentWay");
		RepaymentWay repaymentWay = EnumUtil.fromOrdinal(RepaymentWay.class, repaymentWayInt);
		if(repaymentWay != null) {
			vo.setRepaymentWay(repaymentWay.getChineseMessage());
		}
		DecimalFormat df = new DecimalFormat("0.00%");
		df.setMinimumFractionDigits(2);
		vo.setInterestRate(df.format(rs.getBigDecimal("interestRate")));
		vo.setPenaltyInterest(df.format(rs.getBigDecimal("penaltyInterest")));
		vo.setLoanOverdueEndDay(financialContract.getLoanOverdueEndDay());
		vo.setRepaymentGraceTerm(financialContract.getAdvaRepaymentTerm());
		
		String idCardNumStr = rs.getString("idCardNum");
		if(!StringUtils.isEmpty(idCardNumStr)){
			vo.setIdCardNum(SensitiveInfoUtils.desensitizationStringOfBirthDay(idCardNumStr));
			vo.setAge(AgeUtil.getAgeByIdCardNum(idCardNumStr));
		}
		vo.setBank(rs.getString("bank"));
		vo.setProvince(rs.getString("province"));
		vo.setCity(rs.getString("city"));
		
		String payAcNoStr = rs.getString("payAcNo");
		if(!StringUtils.isEmpty(payAcNoStr)){
			vo.setPayAcNo(SensitiveInfoUtils.desensitizationString(payAcNoStr));
		}
		vo.setBeginDate(rs.getString("beginDate"));

		if (exportTags) {
			vo.setTags(rs.getString("tags"));
		}
		return vo;
	}
	
}
