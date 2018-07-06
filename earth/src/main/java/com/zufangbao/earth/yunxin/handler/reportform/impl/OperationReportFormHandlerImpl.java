package com.zufangbao.earth.yunxin.handler.reportform.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zufangbao.earth.yunxin.handler.reportform.OperationReportFormHandler;
import com.zufangbao.sun.entity.daily.BusinessType;
import com.zufangbao.sun.entity.daily.DailyActualRepayment;
import com.zufangbao.sun.entity.daily.DailyGuarantee;
import com.zufangbao.sun.entity.daily.DailyPartRepayment;
import com.zufangbao.sun.entity.daily.DailyPlanRepayment;
import com.zufangbao.sun.entity.daily.DailyPreRepayment;
import com.zufangbao.sun.entity.daily.DailyRemittance;
import com.zufangbao.sun.entity.daily.DailyRepurchase;
import com.zufangbao.sun.entity.daily.PlanStyle;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.DailyActualRepaymentService;
import com.zufangbao.sun.service.DailyGuaranteeService;
import com.zufangbao.sun.service.DailyPartRepaymentService;
import com.zufangbao.sun.service.DailyPlanRepaymentService;
import com.zufangbao.sun.service.DailyPreRepaymentService;
import com.zufangbao.sun.service.DailyRemittanceService;
import com.zufangbao.sun.service.DailyRepurchaseService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.OnAccountStatus;
import com.zufangbao.sun.yunxin.entity.audit.ContractStatCache;
import com.zufangbao.sun.yunxin.entity.audit.DeductPlanStatCache;
import com.zufangbao.sun.yunxin.entity.excel.DailyActualRepaymentExcelVO;
import com.zufangbao.sun.yunxin.entity.excel.DailyGuaranteeExcelVO;
import com.zufangbao.sun.yunxin.entity.excel.DailyPartRepaymentExcelVO;
import com.zufangbao.sun.yunxin.entity.excel.DailyPlanRepaymentExcelVO;
import com.zufangbao.sun.yunxin.entity.excel.DailyPreRepaymentExcelVO;
import com.zufangbao.sun.yunxin.entity.excel.DailyRemittanceExcelVO;
import com.zufangbao.sun.yunxin.entity.excel.DailyRepurchaseExcelVO;
import com.zufangbao.sun.yunxin.entity.model.reportform.DailyActualRepaymentModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.OperationDataExportModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.OperationDataQueryModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.PaymentGatewayAmountDetail;
import com.zufangbao.sun.yunxin.handler.DailyActualRepaymentHandler;
import com.zufangbao.sun.yunxin.service.ContractStatCacheService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraChargeService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.audit.DeductPlanStatCacheService;
import com.zufangbao.sun.yunxin.service.audit.RemittancePlanStatCacheService;

@Component("operationReportFormHandler")
public class OperationReportFormHandlerImpl implements OperationReportFormHandler{
	
	@Autowired
	private DeductPlanStatCacheService deductPlanStatCacheService;
	
	@Autowired
	private RemittancePlanStatCacheService remittancePlanStatCacheService;
	
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;
	
	@Autowired
	private ContractStatCacheService contractStatCacheService;
	
	@Autowired
	private RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;
	
	@Autowired
	private DailyRemittanceService dailyRemittanceService;
	
	@Autowired
	private DailyPlanRepaymentService dailyPlanRepaymentService;
	
	@Autowired
	private DailyActualRepaymentService dailyActualRepaymentService;
	
	@Autowired
	private DailyPreRepaymentService dailyPreRepaymentService;
	
	@Autowired
	private DailyPartRepaymentService dailyPartRepaymentService;
	
	@Autowired
	private DailyGuaranteeService dailyGuaranteeService;
	
	@Autowired
	private DailyRepurchaseService dailyRepurchaseService;
	
	@Autowired
	private DailyActualRepaymentHandler dailyActualRepaymentHandler;
	
	
	
	@Override
	public Map<String, Object> queryTotalAmount(OperationDataQueryModel queryModel) {
		if (queryModel == null || !queryModel.validParameter()) {
			return Collections.EMPTY_MAP;
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		BigDecimal applicationAmount = BigDecimal.ZERO;//计划订单总额
		BigDecimal planAmount = BigDecimal.ZERO;//放款单总额
		BigDecimal assetPrincipal = BigDecimal.ZERO;//导入资产包本金总额
		
		BigDecimal planRepaymentAmount = BigDecimal.ZERO;//应还日还款计划应还总额
		BigDecimal planRepaymentPrincipal = BigDecimal.ZERO;//应还日还款计划应还本金总额
		BigDecimal notOverdueRepaymentAmount = BigDecimal.ZERO;//宽限期内还款计划应还总额
		BigDecimal notOverdueRepaymentPrincipal = BigDecimal.ZERO;//宽限期内还款计划应还本金总额
		BigDecimal unConfirmedRepaymentAmount = BigDecimal.ZERO;//待确认还款计划应还总额
		BigDecimal unConfirmedRepaymentPrincipal = BigDecimal.ZERO;//待确认还款计划应还本金总额
		BigDecimal overdueRepaymentAmount = BigDecimal.ZERO;//已逾期还款计划应还总额
		BigDecimal overdueRepaymentPrincipal = BigDecimal.ZERO;//已逾期还款计划应还本金总额
		
		BigDecimal actualRepaymentAmount = BigDecimal.ZERO;//实际还款总额
		BigDecimal actualRepaymentPrincipal = BigDecimal.ZERO;//实际还款本金
		
		BigDecimal guaranteeAmount = BigDecimal.ZERO;//担保总额
		BigDecimal repurchaseAmount = BigDecimal.ZERO;//回购总额
		BigDecimal repurchasePrincipal = BigDecimal.ZERO;//回购本金
		
		int applicationCount = 0;//计划订单笔数
		int planCount = 0;//放款单笔数
		int planRepaymentCount = 0;//应还日还款计划笔数
		int notOverdueRepaymentCount = 0;//宽限期内还款计划笔数
		int unConfirmedRepaymentCount = 0;//待确认还款计划笔数
		int overdueRepaymentCount = 0;//已逾期还款计划笔数
		int actualRepaymentCount = 0;//实际还款笔数
		int guaranteeCount = 0;//担保笔数
		int repurchaseCount = 0;//回购笔数
		
		
		List<DailyRemittance> dailyRemittanceList = dailyRemittanceService.queryDailyRemittanceListBy(queryModel,null);
		for (DailyRemittance dailyRemittance : dailyRemittanceList) {
			applicationAmount = applicationAmount.add(dailyRemittance.getApplicationAmount());
			planAmount = planAmount.add(dailyRemittance.getPlanAmount());
			assetPrincipal = assetPrincipal.add(dailyRemittance.getAssetPrincipal());
			applicationCount = applicationCount + dailyRemittance.getApplicationCount();
			planCount = planCount + dailyRemittance.getPlanCount();
		}
		
		List<DailyPlanRepayment> dailyPlanRepaymentList = dailyPlanRepaymentService.queryDailyPlanRepaymentListBy(queryModel,null,null);
		for (DailyPlanRepayment dailyPlanRepayment : dailyPlanRepaymentList) {
			if (dailyPlanRepayment.getPlanStyle() == null) {
				continue;
			}else if (dailyPlanRepayment.getPlanStyle() == PlanStyle.PLAN) {
				planRepaymentAmount = planRepaymentAmount.add(dailyPlanRepayment.getAmount());
				planRepaymentPrincipal = planRepaymentPrincipal.add(dailyPlanRepayment.getAssetPrincipalValue());
				planRepaymentCount = planRepaymentCount + dailyPlanRepayment.getCount();
			}else if (dailyPlanRepayment.getPlanStyle() == PlanStyle.NOTOVERDUE) {
				notOverdueRepaymentAmount = notOverdueRepaymentAmount.add(dailyPlanRepayment.getAmount());
				notOverdueRepaymentPrincipal = notOverdueRepaymentPrincipal.add(dailyPlanRepayment.getAssetPrincipalValue());
				notOverdueRepaymentCount = notOverdueRepaymentCount + dailyPlanRepayment.getCount();
			}else if (dailyPlanRepayment.getPlanStyle() == PlanStyle.UNCONFIRMED) {
				unConfirmedRepaymentAmount = unConfirmedRepaymentAmount.add(dailyPlanRepayment.getAmount());
				unConfirmedRepaymentPrincipal = unConfirmedRepaymentPrincipal.add(dailyPlanRepayment.getAssetPrincipalValue());
				unConfirmedRepaymentCount = unConfirmedRepaymentCount + dailyPlanRepayment.getCount();
			}else if (dailyPlanRepayment.getPlanStyle() == PlanStyle.OVERDUE) {
				overdueRepaymentAmount = overdueRepaymentAmount.add(dailyPlanRepayment.getAmount());
				overdueRepaymentPrincipal = overdueRepaymentPrincipal.add(dailyPlanRepayment.getAssetPrincipalValue());
				overdueRepaymentCount = overdueRepaymentCount + dailyPlanRepayment.getCount();
			}
		}
		
		List<DailyActualRepayment> dailyActualRepaymentList = dailyActualRepaymentService.queryDailyActualRepaymentListBy(queryModel, null, null);
		for (DailyActualRepayment dailyActualRepayment : dailyActualRepaymentList) {
			actualRepaymentAmount = actualRepaymentAmount.add(dailyActualRepayment.getAmount());
			actualRepaymentPrincipal = actualRepaymentPrincipal.add(dailyActualRepayment.getLoanAssetPrincipal());
			actualRepaymentCount = actualRepaymentCount + dailyActualRepayment.getCount();
		}
		
		List<DailyGuarantee> dailyGuaranteeList = dailyGuaranteeService.queryDailyGuaranteeListBy(queryModel, null);
		for (DailyGuarantee dailyGuarantee : dailyGuaranteeList) {
			guaranteeAmount = guaranteeAmount.add(dailyGuarantee.getAmount());
			guaranteeCount = guaranteeCount + dailyGuarantee.getCount();
		}
		
		List<DailyRepurchase> dailyRepurchaseList = dailyRepurchaseService.queryDailyRepurchaseListBy(queryModel, null);
		for (DailyRepurchase dailyRepurchase : dailyRepurchaseList) {
			repurchaseAmount = repurchaseAmount.add(dailyRepurchase.getAmount());
			repurchasePrincipal = repurchasePrincipal.add(dailyRepurchase.getRepurchasePrincipal());
			repurchaseCount = repurchaseCount + dailyRepurchase.getCount();
		}
		
		data.put("applicationAmount", applicationAmount);
		data.put("planAmount", planAmount);
		data.put("assetPrincipal", assetPrincipal);
		data.put("planRepaymentAmount", planRepaymentAmount);
		data.put("planRepaymentPrincipal", planRepaymentPrincipal);
		data.put("notOverdueRepaymentAmount", notOverdueRepaymentAmount);
		data.put("notOverdueRepaymentPrincipal", notOverdueRepaymentPrincipal);
		data.put("unConfirmedRepaymentAmount", unConfirmedRepaymentAmount);
		data.put("unConfirmedRepaymentPrincipal", unConfirmedRepaymentPrincipal);
		data.put("overdueRepaymentAmount", overdueRepaymentAmount);
		data.put("overdueRepaymentPrincipal", overdueRepaymentPrincipal);
		data.put("actualRepaymentAmount", actualRepaymentAmount);
		data.put("actualRepaymentPrincipal", actualRepaymentPrincipal);
		data.put("guaranteeAmount", guaranteeAmount);
		data.put("repurchaseAmount", repurchaseAmount);
		data.put("repurchasePrincipal", repurchasePrincipal);
		data.put("applicationCount", applicationCount);
		data.put("planCount", planCount);
		data.put("planRepaymentCount", planRepaymentCount);
		data.put("notOverdueRepaymentCount", notOverdueRepaymentCount);
		data.put("unConfirmedRepaymentCount", unConfirmedRepaymentCount);
		data.put("overdueRepaymentCount", overdueRepaymentCount);
		data.put("actualRepaymentCount", actualRepaymentCount);
		data.put("guaranteeCount", guaranteeCount);
		data.put("repurchaseCount", repurchaseCount);
		return data;
	}
	
	@Override
	public HSSFWorkbook buildExcel(OperationDataExportModel exportModel) {
		HSSFWorkbook workBook = new HSSFWorkbook();
		if (exportModel == null || exportModel.getOperationDataQueryModel() == null) {
			return workBook;
		}
		FinancialContract fc = financialContractService.getFinancialContractBy(exportModel.getOperationDataQueryModel().getFinancialContractUuid());
		if (fc == null) {
			return workBook;
		}
		if (exportModel.getRemittance()) {
			workBook = addRemittanceSheet(workBook, fc, exportModel);
		}
		if (exportModel.getPlanRepayment()) {
			workBook = addPlanRepaymentSheet(workBook, fc, exportModel, PlanStyle.PLAN);
		}
		if (exportModel.getNotOverduePlanRepayment()) {
			workBook = addPlanRepaymentSheet(workBook, fc, exportModel, PlanStyle.NOTOVERDUE);
		}
		if (exportModel.getUnconfirmedPlanRepayment()) {
			workBook = addPlanRepaymentSheet(workBook, fc, exportModel, PlanStyle.UNCONFIRMED);
		}
		if (exportModel.getOverduePlanRepayment()) {
			workBook = addPlanRepaymentSheet(workBook, fc, exportModel, PlanStyle.OVERDUE);
		}
		if (exportModel.getTotalActualRepayment()) {
			workBook = addTotalActualRepaymentSheet(workBook, fc, exportModel);
		}
		if (exportModel.getOnlineRepayment()) {
			workBook = addActualRepaymentSheet(workBook, fc, exportModel, BusinessType.ONLINEREPAYMENT);
		}
		if (exportModel.getOfflineRepayment()) {
			workBook = addActualRepaymentSheet(workBook, fc, exportModel, BusinessType.OFFLINEREPAYMENT);
		}
		if (exportModel.getOfflinePayment()) {
			workBook = addActualRepaymentSheet(workBook, fc, exportModel, BusinessType.OFFLINEPAYMENT);
		}
		if (exportModel.getPreRepayment()) {
			workBook = addPreRepaymentSheet(workBook, fc, exportModel);
		}
		if (exportModel.getPartRepayment()) {
			workBook = addPartRepaymentSheet(workBook, fc, exportModel);
		}
		if (exportModel.getGuarantee()) {
			workBook = addGuaranteeSheet(workBook, fc, exportModel);
		}
		if (exportModel.getRepurchase()) {
			workBook = addRepurchaseSheet(workBook, fc, exportModel);
		}
		return workBook;
	}


	private HSSFWorkbook addPartRepaymentSheet(HSSFWorkbook workBook, FinancialContract fc,OperationDataExportModel exportModel) {
		List<DailyPartRepayment> list = dailyPartRepaymentService.queryDailyPartRepaymentListBy(exportModel.getOperationDataQueryModel(), null);
		List<DailyPartRepaymentExcelVO> excelVOs = new ArrayList<DailyPartRepaymentExcelVO>();
		for (DailyPartRepayment repayment : list) {
			DailyPartRepaymentExcelVO excelVO = new DailyPartRepaymentExcelVO(repayment, fc);
			excelVOs.add(excelVO);
		}
		ExcelUtil<DailyPartRepaymentExcelVO> excelUtil = new ExcelUtil<DailyPartRepaymentExcelVO>(DailyPartRepaymentExcelVO.class);
		return excelUtil.exportDataToHSSFWork(workBook, excelVOs, "部分还款");
	}

	private HSSFWorkbook addPreRepaymentSheet(HSSFWorkbook workBook, FinancialContract fc,OperationDataExportModel exportModel) {
		List<DailyPreRepayment> list = dailyPreRepaymentService.queryDailyPreRepaymentListBy(exportModel.getOperationDataQueryModel(), null);
		List<DailyPreRepaymentExcelVO> excelVOs = new ArrayList<DailyPreRepaymentExcelVO>();
		for (DailyPreRepayment repayment : list) {
			DailyPreRepaymentExcelVO excelVO = new DailyPreRepaymentExcelVO(repayment, fc);
			excelVOs.add(excelVO);
		}
		ExcelUtil<DailyPreRepaymentExcelVO> excelUtil = new ExcelUtil<DailyPreRepaymentExcelVO>(DailyPreRepaymentExcelVO.class);
		return excelUtil.exportDataToHSSFWork(workBook, excelVOs, "提前还款");
	}

	private HSSFWorkbook addTotalActualRepaymentSheet(HSSFWorkbook workBook, FinancialContract fc,OperationDataExportModel exportModel) {
		List<DailyActualRepaymentModel> list = dailyActualRepaymentHandler.queryDailyActualRepaymentModel(exportModel.getOperationDataQueryModel(), null, null);
		List<DailyActualRepaymentExcelVO> excelVOs = new ArrayList<DailyActualRepaymentExcelVO>();
		for (DailyActualRepaymentModel repayment : list) {
			DailyActualRepaymentExcelVO excelVO = new DailyActualRepaymentExcelVO(repayment, fc);
			excelVOs.add(excelVO);
		}
		ExcelUtil<DailyActualRepaymentExcelVO> excelUtil = new ExcelUtil<DailyActualRepaymentExcelVO>(DailyActualRepaymentExcelVO.class);
		return excelUtil.exportDataToHSSFWork(workBook, excelVOs, "实际还款总额");
	}

	private HSSFWorkbook addActualRepaymentSheet(HSSFWorkbook workBook, FinancialContract fc,OperationDataExportModel exportModel, BusinessType businessType) {
		List<DailyActualRepayment> list = dailyActualRepaymentService.queryDailyActualRepaymentListBy(exportModel.getOperationDataQueryModel(), businessType, null);
		List<DailyActualRepaymentExcelVO> excelVOs = new ArrayList<DailyActualRepaymentExcelVO>();
		for (DailyActualRepayment repayment : list) {
			DailyActualRepaymentExcelVO excelVO = new DailyActualRepaymentExcelVO(repayment, fc);
			excelVOs.add(excelVO);
		}
		ExcelUtil<DailyActualRepaymentExcelVO> excelUtil = new ExcelUtil<DailyActualRepaymentExcelVO>(DailyActualRepaymentExcelVO.class);
		return excelUtil.exportDataToHSSFWork(workBook, excelVOs, businessType.getChineseMessage());
	}

	private HSSFWorkbook addRepurchaseSheet(HSSFWorkbook workBook, FinancialContract fc,OperationDataExportModel exportModel) {
		List<DailyRepurchase> list = dailyRepurchaseService.queryDailyRepurchaseListBy(exportModel.getOperationDataQueryModel(), null);
		List<DailyRepurchaseExcelVO> excelVOs = new ArrayList<DailyRepurchaseExcelVO>();
		for (DailyRepurchase repurchase : list) {
			DailyRepurchaseExcelVO excelVO = new DailyRepurchaseExcelVO(repurchase, fc);
			excelVOs.add(excelVO);
		}
		ExcelUtil<DailyRepurchaseExcelVO> excelUtil = new ExcelUtil<DailyRepurchaseExcelVO>(DailyRepurchaseExcelVO.class);
		return excelUtil.exportDataToHSSFWork(workBook, excelVOs, "回购");
	}

	private HSSFWorkbook addGuaranteeSheet(HSSFWorkbook workBook, FinancialContract fc,OperationDataExportModel exportModel) {
		List<DailyGuarantee> list = dailyGuaranteeService.queryDailyGuaranteeListBy(exportModel.getOperationDataQueryModel(), null);
		List<DailyGuaranteeExcelVO> excelVOs = new ArrayList<DailyGuaranteeExcelVO>();
		for (DailyGuarantee guarantee : list) {
			DailyGuaranteeExcelVO excelVO = new DailyGuaranteeExcelVO(guarantee, fc);
			excelVOs.add(excelVO);
		}
		ExcelUtil<DailyGuaranteeExcelVO> excelUtil = new ExcelUtil<DailyGuaranteeExcelVO>(DailyGuaranteeExcelVO.class);
		return excelUtil.exportDataToHSSFWork(workBook, excelVOs, "担保");
	}

	private HSSFWorkbook addPlanRepaymentSheet(HSSFWorkbook workBook, FinancialContract fc,OperationDataExportModel exportModel, PlanStyle planStyle) {
		List<DailyPlanRepayment> list = dailyPlanRepaymentService.queryDailyPlanRepaymentListBy(exportModel.getOperationDataQueryModel(), planStyle, null);
		List<DailyPlanRepaymentExcelVO> excelVOs = new ArrayList<DailyPlanRepaymentExcelVO>();
		for (DailyPlanRepayment repayment : list) {
			DailyPlanRepaymentExcelVO excelVO = new DailyPlanRepaymentExcelVO(repayment, fc);
			excelVOs.add(excelVO);
		}
		ExcelUtil<DailyPlanRepaymentExcelVO> excelUtil = new ExcelUtil<DailyPlanRepaymentExcelVO>(DailyPlanRepaymentExcelVO.class);
		return excelUtil.exportDataToHSSFWork(workBook, excelVOs, planStyle.getChineseMessage()+"计划还款");
	}

	private HSSFWorkbook addRemittanceSheet(HSSFWorkbook workBook, FinancialContract fc, OperationDataExportModel exportModel) {
		List<DailyRemittance> list = dailyRemittanceService.queryDailyRemittanceListBy(exportModel.getOperationDataQueryModel(), null);
		List<DailyRemittanceExcelVO> excelVOs = new ArrayList<DailyRemittanceExcelVO>();
		for (DailyRemittance remittance : list) {
			DailyRemittanceExcelVO excelVO = new DailyRemittanceExcelVO(remittance, fc);
			excelVOs.add(excelVO);
		}
		ExcelUtil<DailyRemittanceExcelVO> excelUtil = new ExcelUtil<DailyRemittanceExcelVO>(DailyRemittanceExcelVO.class);
		return excelUtil.exportDataToHSSFWork(workBook, excelVOs, "放款");
	}
	
	
	
	
//	@Override
//	public List<OperationalDataPageShowModel> query(OperationDataQueryModel queryModel, Page page) {
//		
//		List<OperationalDataPageShowModel> models = new ArrayList<>();
//		
//		List<String> financialContractUuids = queryModel.getFinancialContractUuidList();
//		
//		if(CollectionUtils.isEmpty(financialContractUuids) || StringUtils.isBlank(queryModel.getQueryDate())) {
//			
//			return Collections.emptyList();
//		}
//		
//		Date date = DateUtils.parseDate(queryModel.getQueryDate());
//		
//		List<FinancialContract> financialContracts = financialContractService.getFinancialContractByUuidAndPage(financialContractUuids, page);
//		
//		if(CollectionUtils.isEmpty(financialContracts)) {
//			return Collections.emptyList();
//			
//		}
//		//获得所有扣款监控信息
//		List<DeductPlanStatCache> deductPlanStatCaches = deductPlanStatCacheService.queryDeductPlanStatCacheList(financialContractUuids, date);
//		//获得所有放款监控信息
//		List<ContractStatCache> contractStatCaches = contractStatCacheService.getContractStatCacheList(financialContractUuids, date);
//		System.out.println(contractStatCaches.size());
//		for(ContractStatCache cache : contractStatCaches) {
//			System.out.println(cache.getPlanAmount());
//		}
//		for(FinancialContract financialContract : financialContracts) {
//			
//			String financialContractUuid = financialContract.getUuid();
//			//获得放款信息
//			List<ContractStatCache> contractStatCacheList = getContractStatCacheList(contractStatCaches, financialContractUuid);
//			//获得实际还款总额
//			BigDecimal actualRepaymentAmount = getActualPaymentPlan(date, financialContract, financialContractUuid);
//			//获得计划还款信息
//			List<OperationDataAssetDetail> assetDetailList = repaymentPlanService.getAssetDetailListByFinancialContractUuidAndAssetRecycleDate(financialContractUuid, date);
//			//获得每个扣款通道对应的扣款监控信息
//			Map<Integer, List<DeductPlanStatCache>> paymentGatewayMap = getPaymentGatewayMap(deductPlanStatCaches, financialContractUuid);
//			//获得每个扣款通道的扣款明细
//			List<PaymentGatewayAmountDetail> amountDetailList = getAmountDetailList(paymentGatewayMap);
//			//生成展示模型
//			OperationalDataPageShowModel model = new OperationalDataPageShowModel(financialContract, contractStatCacheList, assetDetailList, amountDetailList, date, actualRepaymentAmount);
//			
//			models.add(model);
//			
//		}
//		
//		return models;
//	}

	private List<ContractStatCache> getContractStatCacheList(List<ContractStatCache> contractStatCaches,
			String financialContractUuid) {
		if(CollectionUtils.isEmpty(contractStatCaches)) {
			return Collections.emptyList();
		}
		List<ContractStatCache> contractStatCacheList = new ArrayList<>();
		
		for(ContractStatCache cache : contractStatCaches) {
			
			if(cache.getFinancialContractUuid().equals(financialContractUuid)) {
				
				contractStatCacheList.add(cache);
				
			}
		}
		 
		return contractStatCacheList;
	}

	private BigDecimal getActualPaymentPlan(Date date, FinancialContract financialContract,
			String financialContractUuid) {
		List<AssetSet> actualPaymentPlan = repaymentPlanService.getAssetSetByFinancialContractUuidAndActualRecycleDate(financialContractUuid, date);
		
		BigDecimal actualRepaymentAmount = BigDecimal.ZERO;
		
		for(AssetSet assetSet : actualPaymentPlan) {
			
			if(assetSet.isPaidOff()){
				
				actualRepaymentAmount = actualRepaymentAmount.add(assetSet.getAmount());
				
			}else if(assetSet.getOnAccountStatus() == OnAccountStatus.PART_WRITE_OFF){
				
				BigDecimal ledgerBookAmount = ledgerBookStatHandler.get_banksaving_amount_of_asset(financialContract.getLedgerBookNo(),assetSet.getAssetUuid());
				
				actualRepaymentAmount = actualRepaymentAmount.add(ledgerBookAmount);
				
			}
			
		}
		return actualRepaymentAmount;
	}

	private List<PaymentGatewayAmountDetail> getAmountDetailList(
			Map<Integer, List<DeductPlanStatCache>> paymentGatewayMap) {
		List<PaymentGatewayAmountDetail> amountDetailList = new ArrayList<>();
		
		for(Integer key : paymentGatewayMap.keySet()) {
			String paymentGatewayName = EnumUtil.fromOrdinal(PaymentInstitutionName.class, key).getChineseMessage();
			Integer actualRepaymentNumber = 0;
			BigDecimal actualTotalFee = BigDecimal.ZERO;
			BigDecimal actualPrincipal = BigDecimal.ZERO;
			BigDecimal actualInterest = BigDecimal.ZERO;
			BigDecimal actualLoanServiceFee = BigDecimal.ZERO;
			BigDecimal actualTechFee = BigDecimal.ZERO;
			BigDecimal actualOtherFee = BigDecimal.ZERO;
			BigDecimal actualOverduePenalty = BigDecimal.ZERO;
			BigDecimal actualOverdueDefaultFee = BigDecimal.ZERO;
			BigDecimal actualOverdueServiceFee = BigDecimal.ZERO;
			BigDecimal actualOverdueOtherFee = BigDecimal.ZERO;
			
			List<DeductPlanStatCache> list = paymentGatewayMap.get(key);
			
			if(list != null && CollectionUtils.isNotEmpty(list)) {
				for(DeductPlanStatCache cache : list) {
					actualRepaymentNumber = (int) (actualRepaymentNumber + cache.getSucNum());
					actualTotalFee = actualTotalFee.add(cache.getActualTotalFee());
					actualPrincipal = actualPrincipal.add(cache.getActualPrincipal());
					actualInterest = actualInterest.add(cache.getActualInterest());
					actualLoanServiceFee = actualLoanServiceFee.add(cache.getActualLoanServiceFee());
					actualTechFee = actualTechFee.add(cache.getActualTechFee());
					actualOtherFee = actualOtherFee.add(cache.getActualOtherFee());
					actualOverduePenalty = actualOverduePenalty.add(cache.getActualOverduePenalty());
					actualOverdueDefaultFee = actualOverdueDefaultFee.add(cache.getActualOverdueDefaultFee());
					actualOverdueServiceFee = actualOverdueServiceFee.add(cache.getActualOverdueServiceFee());
					actualOverdueOtherFee = actualOverdueOtherFee.add(cache.getActualOverdueOtherFee());
					
				}
				
			}
			
			PaymentGatewayAmountDetail amountDetail = new PaymentGatewayAmountDetail(paymentGatewayName, actualRepaymentNumber, 
					actualTotalFee, 
					actualPrincipal, actualInterest, 
					actualLoanServiceFee, actualTechFee, 
					actualOtherFee, actualOverduePenalty, 
					actualOverdueDefaultFee, actualOverdueServiceFee, 
					actualOverdueOtherFee);
			
			amountDetailList.add(amountDetail);
		}
		return amountDetailList;
	}

	private Map<Integer, List<DeductPlanStatCache>> getPaymentGatewayMap(List<DeductPlanStatCache> deductPlanStatCaches,
			String financialContractUuid) {
		
		if(CollectionUtils.isEmpty(deductPlanStatCaches)) {
			return Collections.emptyMap();
		}
		
		List<DeductPlanStatCache> deductCacheList = deductPlanStatCaches.stream()
				.filter(d -> d.getFinancialContractUuid().equals(financialContractUuid))
				.collect(Collectors.toList());

		Map<Integer, List<DeductPlanStatCache>> paymentGatewayMap = new HashMap<>();
		
		for(DeductPlanStatCache cache :deductCacheList) {
			
			Integer paymentGatewayOrdinal = cache.getPaymentGateway();
			
			List<DeductPlanStatCache> temp_cache_list = new ArrayList<>();
			
			if(paymentGatewayMap.containsKey(paymentGatewayOrdinal)) {
				
				temp_cache_list = paymentGatewayMap.get(paymentGatewayOrdinal);
				
			}
				
			temp_cache_list.add(cache);
			
			paymentGatewayMap.put(paymentGatewayOrdinal, temp_cache_list);
			
		}
		return paymentGatewayMap;
	}



}
