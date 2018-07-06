package com.zufangbao.earth.yunxin.excel;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.earth.api.exception.TransactionDetailApiException;
import com.zufangbao.earth.cache.ApplicationCacheRoot;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.earth.yunxin.unionpay.component.impl.GZUnionPayApiComponentImpl;
import com.zufangbao.earth.yunxin.unionpay.model.TransactionDetailNode;
import com.zufangbao.earth.yunxin.unionpay.model.TransactionDetailQueryInfoModel;
import com.zufangbao.earth.yunxin.unionpay.model.TransactionDetailQueryResult;
import com.zufangbao.earth.yunxin.unionpay.model.basic.IGZUnionPayApiParams;
import com.zufangbao.sun.api.model.deduct.DeductApplicationRepaymentDetail;
import com.zufangbao.sun.entity.financial.*;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.TransferApplicationService;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.*;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBill;
import com.zufangbao.sun.yunxin.entity.model.InterfaceDailyListModel;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.barclays.ThirdPartyAuditBillService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("dailyYunxinPaymentFlowCheckExcel")
public class DailyYunxinPaymentFlowCheckExcel {

	private static final String ACCOUNT_CHECKING_FLOW = "对账流水";
	private static final String GD_UNION_PAY_FLOW = "广东银联流水";
	private static final String ONLINE_PAYMENT_ORDER_DETAIL = "线上支付单详情";
	private static final String INTERFACE_PAYMENT_ORDER_DETAIL = "接口线上支付单详情";
	private static final String QUERY_GZ_UNION_PAY_DEFAULT = "";
	private static final String QUERY_GZ_UNION_PAY_FIRST_PAGE = "1";

	@Autowired
	private TransferApplicationService transferApplicationService;
	@Autowired
	private GZUnionPayApiComponentImpl gZUnionPayHandlerImpl;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;
	@Autowired
	private RepaymentPlanHandler repaymentPlanHandler;
	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private DeductApplicationDetailService deductApplicationDetailService;

	@Autowired
	private PaymentChannelInformationHandler paymentChannelInformationHandler;
	
	@Autowired
	private ThirdPartyAuditBillService thirdPartyAuditBillService;
	@Autowired
	private DeductPlanService deductPlanService;
	
	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailservice;
	
	@Autowired
	private JournalVoucherService journalVoucherService;

	public Map<String,List<String>> gzUnionpayCsvs(Principal principal, String ip, String queryDate, Long financialContractId) {
		
		List<String> flow_csv_lines = fetch_guang_dong_union_flow(queryDate, financialContractId);
		
		List<String> online_payemnt_detail_csvs = create_payment_detail_excel(principal, ip,
				queryDate, financialContractId);
		
		return castFlowResultMap( create_flow_file_name(PaymentInstitutionName.UNIONPAYGZ),flow_csv_lines,ONLINE_PAYMENT_ORDER_DETAIL, online_payemnt_detail_csvs);
	}

	private List<String> fetch_guang_dong_union_flow(String queryDate, Long financialContractId) {
		Date parseQueryDate = DateUtils.parseDate(queryDate, "yyyy-MM-dd");
		Map<PaymentInstitutionName,PaymentChannelInformation> supportedPaymentChannelsMap = getSupportedPaymentChannel(financialContractId);
		
		List<String> csv_lines= null;
		
		if(!isSupportThePaymentGateway(PaymentInstitutionName.UNIONPAYGZ, supportedPaymentChannelsMap)){
			 csv_lines = castModelFlow( DailyPaymentFlowExcel.class, Collections.emptyList());
		}else
		{		
		 csv_lines = fetch_flow_from_GuangDongUnion( parseQueryDate,financialContractId,PaymentInstitutionName.UNIONPAYGZ,supportedPaymentChannelsMap);
		}
		return csv_lines;
	}

	private Map<String, List<String>> castFlowResultMap(  String part1FileName ,List<String> csvs1, String part2FileName,List<String> csv2) {
		 Map<String, List<String>> resultMap = new  HashMap<String, List<String>>();
		 resultMap.put(part1FileName, csvs1);
		 resultMap.put(part2FileName, csv2);
		return resultMap;
	}

	private Map<PaymentInstitutionName,PaymentChannelInformation> getSupportedPaymentChannel(Long financialContractId) {
		FinancialContract financialContract  =  financialContractService.load(FinancialContract.class, financialContractId);
		return getAllPaymentGateWay(financialContract);
		
	}
	
	public Map<String,List<String>> interfaceCheckingCsv(Principal principal, String ip,Date startDate, Date endDate,long  financialContractId, List<PaymentInstitutionName> paymentGateways){
		Map<String, List<String>> resultMap = new  HashMap<String, List<String>>();
		FinancialContract financialContract  =  financialContractService.load(FinancialContract.class, financialContractId);
		Map<PaymentInstitutionName,PaymentChannelInformation> paymentInstitutionNameMap = getAllPaymentGateWay(financialContract);

		// 接口线上支付单详情
		List<DailyPaymentCheckFlowExcel> dailyPaymentCheckFlowExcels = create_interface_payment_detail_csv(principal, ip, startDate,
				endDate, financialContract.getFinancialContractUuid(), paymentGateways);
		List<String>dailyPaymentCheckFlowExcelsFormatCsv =castModelFlow(DailyPaymentCheckFlowExcel.class, dailyPaymentCheckFlowExcels);
		resultMap.put(INTERFACE_PAYMENT_ORDER_DETAIL, dailyPaymentCheckFlowExcelsFormatCsv);

		// 对账流水
		List<PaymentAuditFlowCsv> paymentAuditFlowCsvs = fetchThirdPartyAuditBillCsvFormat(paymentInstitutionNameMap, paymentGateways, startDate, endDate);
		List<String> PaymentAuditFlowFormatCsv = castModelFlow(PaymentAuditFlowCsv.class, paymentAuditFlowCsvs);
		resultMap.put(ACCOUNT_CHECKING_FLOW, PaymentAuditFlowFormatCsv);
		return resultMap;
	}

	private List<PaymentAuditFlowCsv> fetchThirdPartyAuditBillCsvFormat(
		Map<PaymentInstitutionName, PaymentChannelInformation> paymentInstitutionNameMap, List<PaymentInstitutionName> paymentGateways, Date startDate, Date endDate) {
		List<ThirdPartyAuditBill> auditBillFlowNos = new ArrayList<>();
		for (PaymentInstitutionName paymentGateway : paymentGateways) {
			PaymentChannelInformation pci = paymentInstitutionNameMap.get(paymentGateway);
			if (pci == null) {
				continue;
			}
			auditBillFlowNos.addAll(thirdPartyAuditBillService.queryFlowNoByMerchantIdAndPaymentGateway(startDate, endDate, pci.getOutlierChannelName(), paymentGateway));
		}
		return convertToPaymentAuditFlowCsv(auditBillFlowNos);
	}

	private String create_flow_file_name(PaymentInstitutionName paymentGateway) {
		return paymentGateway.getChineseMessage()+ACCOUNT_CHECKING_FLOW;
	}

	private List<String> fetch_flow_from_payment_institution(Date queryDate, long financialContractId, PaymentInstitutionName paymentGateway,
			Map<PaymentInstitutionName, PaymentChannelInformation> paymentInstitutionNameMap) {
		switch (paymentGateway) {
		case UNIONPAYGZ:
			return  fetch_flow_from_GuangDongUnion(queryDate, financialContractId, paymentGateway, paymentInstitutionNameMap);
		case BAOFU:
			return  fetch_flow_from_BaoFu( paymentInstitutionNameMap.get(paymentGateway), queryDate, paymentGateway);
		default:
			return Collections.emptyList();
		}
	}

	@SuppressWarnings("unchecked")
	private List<String> fetch_flow_from_GuangDongUnion(Date queryDate, long financialContractId,PaymentInstitutionName paymentGateway,
			Map<PaymentInstitutionName, PaymentChannelInformation> paymentInstitutionNameMap) {
		PaymentChannelJson supportNFQPaymentChannelParams = getUsedGZUnionPaymentChannel(paymentGateway, paymentInstitutionNameMap);
		if(supportNFQPaymentChannelParams == null){
			return castModelFlow(DailyPaymentFlowExcel.class,Collections.EMPTY_LIST);
		}
		return evaluateGZUnionFlowSheet(DateUtils.format(queryDate) , financialContractId,supportNFQPaymentChannelParams);
	}

	private boolean isSupportThePaymentGateway(PaymentInstitutionName paymentGateway,Map<PaymentInstitutionName,PaymentChannelInformation> paymentInstitutionNameMap) {
		return (paymentInstitutionNameMap.get(paymentGateway) !=null);
	}

	public  List<String> fetch_flow_from_BaoFu(PaymentChannelInformation payemntChannel,Date queryDate, PaymentInstitutionName paymentGateway){
		
		List<ThirdPartyAuditBill> auditBillFlowNos = thirdPartyAuditBillService.queryFlowNoByMerchantIdAndPaymentGateway(queryDate, null, payemntChannel.getOutlierChannelName(), paymentGateway);
		List<PaymentAuditFlowCsv> paymentAuditFlowCsvs = convertToPaymentAuditFlowCsv(auditBillFlowNos);
		
		return castModelFlow( PaymentAuditFlowCsv.class, paymentAuditFlowCsvs);
		
	}
	
	private List<PaymentAuditFlowCsv> convertToPaymentAuditFlowCsv(List<ThirdPartyAuditBill> auditBillFlowNos) {
		return  auditBillFlowNos.stream().map(PaymentAuditFlowCsv::new).collect(Collectors.toList());
	}

	private PaymentChannelJson getUsedGZUnionPaymentChannel(PaymentInstitutionName paymentGateway,
			Map<PaymentInstitutionName, PaymentChannelInformation> paymentInstitutionNameMap) {
		
		if(paymentGateway != PaymentInstitutionName.UNIONPAYGZ){
			return null;
		}
		//配置文件是否配置,农分期需要从配置文件中取到通道配置
		ApplicationCacheRoot cacheRoot = new ApplicationCacheRoot();
		List<PaymentChannelJson> paymentChannelCache= cacheRoot.getPaymentChannels();
		List<PaymentChannelJson> supportPaymentChannelList = paymentChannelCache.stream().filter(hb -> hb.getPaymentChannelUuid().equals(paymentInstitutionNameMap.get(paymentGateway).getDebitPaymentChannelServiceUuid())).collect(Collectors.toList());
		if(!supportPaymentChannelList.isEmpty()){
			return supportPaymentChannelList.get(0);
		}
		return null;
	}

	private Map<PaymentInstitutionName, PaymentChannelInformation> getAllPaymentGateWay(FinancialContract financialContract) {
		List<PaymentChannelInformation> paymentChannelInformations = paymentChannelInformationHandler.getPaymentChannelInformationsBy(financialContract.getFinancialContractUuid(),  BusinessType.SELF, com.zufangbao.sun.yunxin.entity.remittance.AccountSide.DEBIT);
		return  paymentChannelInformations.stream().collect(Collectors.toMap(PaymentChannelInformation::getPaymentInstitutionName,a->a));
	}


	private <T> List<String> castModelFlow(Class<T> persistentClass,List<T> sourceList) {
        ExcelUtil<T> excelUtil = new ExcelUtil<T>(persistentClass);
		return excelUtil.exportDatasToCSV(sourceList);
	}



	@SuppressWarnings("unchecked")
	private List<String> evaluateGZUnionFlowSheet(String queryDate,Long financialContractId,PaymentChannelJson supportPaymentChannel) {
		

		TransactionDetailQueryInfoModel transactionDetailQueryInfoModel = createRequestQueryModelByconfigFile(queryDate, financialContractId, supportPaymentChannel);
		TransactionDetailQueryResult result = new TransactionDetailQueryResult();

		try {
			result = gZUnionPayHandlerImpl.execTransactionDetailQuery(transactionDetailQueryInfoModel);
		} catch (TransactionDetailApiException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		
		return generateGDUnionFlow( result);
		
		 
	}

	private List<String> generateGDUnionFlow( TransactionDetailQueryResult result) {
		ExcelUtil<DailyPaymentFlowExcel> excelUtil = new ExcelUtil<DailyPaymentFlowExcel>(DailyPaymentFlowExcel.class);
		
		List<DailyPaymentFlowExcel> dailyPaymentGDUnionPayFlowExcels = new ArrayList<DailyPaymentFlowExcel>(); 
		
		if (result.getDetailNodes() == null || result.getDetailNodes().size() == 0) {
			return excelUtil.exportDatasToCSV(dailyPaymentGDUnionPayFlowExcels);
		}
		
		dailyPaymentGDUnionPayFlowExcels = convertDailyFlowData(result);
		List<String>  GDFlowCsvData = excelUtil.exportDatasToCSV(dailyPaymentGDUnionPayFlowExcels);
		
		return GDFlowCsvData;
	}

	private List<DailyPaymentFlowExcel> convertDailyFlowData(TransactionDetailQueryResult result) {
		List<DailyPaymentFlowExcel> dailyPaymentGDUnionPayFlowExcels = new ArrayList<DailyPaymentFlowExcel>();
		List<TransactionDetailNode> nodes = result.getDetailNodes();
		
		for (TransactionDetailNode node : nodes) {
			DailyPaymentFlowExcel dailyPaymentGDUnionPayFlowExcel = new DailyPaymentFlowExcel();
			dailyPaymentGDUnionPayFlowExcel.setReqNo(node.getReqNo());
			dailyPaymentGDUnionPayFlowExcel.setSn(node.getSn());
			dailyPaymentGDUnionPayFlowExcel.setSfType(node.getSfType());
			dailyPaymentGDUnionPayFlowExcel.setSettDate(node.getSettDate());
			dailyPaymentGDUnionPayFlowExcel.setReckonAccount('\t'+node
					.getReckonAccount());
			dailyPaymentGDUnionPayFlowExcel.setMsg(node.getErrMsg());
			dailyPaymentGDUnionPayFlowExcel.setCompleteTime(node
					.getCompleteTime());
			dailyPaymentGDUnionPayFlowExcel.setAmount(node.getAmount()
					.toString());
			dailyPaymentGDUnionPayFlowExcel.setAccountName(node
					.getAccountName());
			dailyPaymentGDUnionPayFlowExcel.setAccount('\t'+node.getAccount());
			dailyPaymentGDUnionPayFlowExcels
					.add(dailyPaymentGDUnionPayFlowExcel);
		}
		return dailyPaymentGDUnionPayFlowExcels;
	}
	
	
	

	public Map<String, List<String>> dailyReturnListExcel(Principal principal, String ip,
			String queryDate, Long financialContractId) {
		List<TransferApplication> transferApplications = transferApplicationService.queryTheDateTransferApplicationOrderByStatus(financialContractId, queryDate);
		
		List<String> assetSetUuids = transferApplications.stream().map(tas -> tas.getOrder().getAssetSetUuid()).collect(Collectors.toList());
		Map<String, RepaymentChargesDetail> chargesMap = repaymentPlanHandler.getRepaymentCharges(assetSetUuids);
		
		List<DailyReturnListExcel> dailyReturnListExcels = transferApplications.stream().map(hb -> genDailyReturnListExcel(hb, chargesMap.get(hb.getOrder().getAssetSetUuid()))).collect(Collectors.toList());
		
		
		List<String> transferApplicationUuids = transferApplications.stream().map(
			TransferApplication::getTransferApplicationUuid).collect(Collectors.toList());
		
		ExcelUtil<DailyReturnListExcel> excelUtil = new ExcelUtil<DailyReturnListExcel>(
				DailyReturnListExcel.class);
		Map<String, List<String>> csvs = new HashMap<String, List<String>>();
		List<String>  csvData = excelUtil.exportDatasToCSV(dailyReturnListExcels);
		csvs.put("线上每日还款清单表", csvData);
		
		recordExportLog(principal, ip, transferApplicationUuids, LogFunctionType.ONLINEBILLEXPORTDAILYRETURNLIST);
		return csvs;
	}

	private DailyReturnListExcel genDailyReturnListExcel(TransferApplication transferApplication, RepaymentChargesDetail chargesDetail){

		AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(transferApplication.getOrder().getAssetSetUuid());
		return new DailyReturnListExcel(transferApplication,chargesDetail,assetSet );

	}

	public List<String> dailyInterfaceReturnListExcel(Principal principal, String ip,
			Date startDate, Date endDate,long financialContractId, PaymentInstitutionName paymentGateway) {
		
		FinancialContract financialContract = financialContractService.load(FinancialContract.class, financialContractId);
		
		List<DeductPlan> deductPlans = deductPlanService.queryDeductPlanOrderByStatus(financialContract.getFinancialContractUuid(),paymentGateway,startDate, endDate);
		
		List<String>   deductPlanUuids =  deductPlans.stream().map(hb -> hb.getDeductPlanUuid()).collect(Collectors.toList());
		
		List<InterfaceDailyListModel> interfaceModelList = new ArrayList<InterfaceDailyListModel>();
		for(DeductPlan deductPlan :deductPlans){
			List<DeductApplicationRepaymentDetail> deductApplicationDetails = deductApplicationDetailService.getRepaymentDetailsBy(deductPlan.getDeductApplicationUuid());
			for(DeductApplicationRepaymentDetail deductApplicationDetail :deductApplicationDetails){
				AssetSet repaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(deductApplicationDetail.getAssetSetUuid());
				RepaymentChargesDetail chargesDetail = repaymentPlanHandler.getPlanRepaymentChargesDetail(repaymentPlan);
				String sourceDocumentDetailUuid = sourceDocumentDetailservice.get_source_document_detail_uuid(deductApplicationDetail.getDeductApplicationUuid(), deductApplicationDetail.getDeductApplicationDetailUuid());
				String journalVoucherUuid = journalVoucherService.get_journal_voucher_uuid_by_source_document_detail_uuid(sourceDocumentDetailUuid);
				RepaymentChargesDetail paidUpChargesDetail = new RepaymentChargesDetail(ledgerBookStatHandler.get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(financialContract.getLedgerBookNo(), journalVoucherUuid, repaymentPlan.getAssetUuid()));
				interfaceModelList.add(new InterfaceDailyListModel(deductPlan,repaymentPlan,chargesDetail,paidUpChargesDetail));
			}
		}
		
		List<DailyReturnListExcel> dailyReturnListExcels = interfaceModelList.stream().map(hb -> new DailyReturnListExcel(hb,financialContract)).collect(Collectors.toList());
		
		ExcelUtil<DailyReturnListExcel> excelUtil = new ExcelUtil<DailyReturnListExcel>(DailyReturnListExcel.class);
		List<String>  csvData = excelUtil.exportDatasToCSV(dailyReturnListExcels);
		
		recordExportLog(principal, ip, deductPlanUuids, LogFunctionType.INTERFACEPAYMENTEXPORTDAILYLIST);
		return csvData;
	}

	private void recordExportLog(Principal principal, String ip, List<String> deductPlanUuids,
			LogFunctionType logFunctionType) {
		SystemOperateLogRequestParam param = getSystemOperateLogRequestParam(
				principal, ip, deductPlanUuids,logFunctionType);
		try {
			systemOperateLogHandler.generateSystemOperateLog(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	private SystemOperateLogRequestParam getSystemOperateLogRequestParam(
			Principal principal, String ip,
			List<String> transferApplicationUuids,LogFunctionType functionType) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(
				principal.getId(), ip, null,
				functionType,
				LogOperateType.EXPORT, null, null, null,
				transferApplicationUuids);
		return param;
	}

	private TransactionDetailQueryInfoModel createRequestQueryModelByconfigFile(
			String queryDate, long financialContractId,PaymentChannelJson supportPaymentChannel) {
		TransactionDetailQueryInfoModel model = new TransactionDetailQueryInfoModel();
		String queryForMatDate = DateUtils.format(DateUtils.parseDate(queryDate, "yyyy-MM-dd"), "yyyyMMdd");
		model.setBeginDate(queryForMatDate);
		model.setEndDate(queryForMatDate);
			
		model.setUserName(supportPaymentChannel.getUserName());
		model.setUserPwd(supportPaymentChannel.getUserPassword());
		model.setMerchantId(supportPaymentChannel.getMerchantId());
		model.setApiUrl(supportPaymentChannel.getApiUrl());
		model.setReqNo(UUID.randomUUID().toString());
		model.setPageNum(QUERY_GZ_UNION_PAY_FIRST_PAGE);
		model.setPageSize(QUERY_GZ_UNION_PAY_DEFAULT);
		setGZUnionPayParams(model, supportPaymentChannel);
		return model;
	}
	

	
	private void setGZUnionPayParams(IGZUnionPayApiParams apiParams,
			PaymentChannelJson paymentChannel) {
		apiParams.setApiUrl(paymentChannel.getApiUrl());
		apiParams.setCerFilePath(paymentChannel.getCerFilePath());
		apiParams.setPfxFilePath(paymentChannel.getPfxFilePath());
		apiParams.setPfxFileKey(paymentChannel.getPfxFileKey());
	}

	private List<String> create_payment_detail_excel(Principal principal,
			String ip, String queryDate, Long financialContractId) {
		
		List<TransferApplication> transferApplications = transferApplicationService.queryTheDateTransferApplicationOrderByStatus(financialContractId, queryDate);
		List<DailyPaymentCheckFlowExcel> dailyPaymentCheckFlowExcels = transferApplications.stream().map(this::genDailyPaymentCheckFlowExcel).collect(Collectors.toList());
		List<String> transferApplicationUuids = transferApplications.stream().map(TransferApplication::getTransferApplicationUuid).collect(Collectors.toList());

		List<String> online_payemnt_detail_csvs = generatePaymentDetailCsv(principal, ip, dailyPaymentCheckFlowExcels,DailyPaymentCheckFlowExcel.class,transferApplicationUuids,LogFunctionType.ONLINEBILLEXPORTCHECKING);
		
		return online_payemnt_detail_csvs;
	}

	private DailyPaymentCheckFlowExcel genDailyPaymentCheckFlowExcel(TransferApplication pTr){
		String assetSetUuid = pTr.getOrder().getAssetSetUuid();
		AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
		return new DailyPaymentCheckFlowExcel(pTr, assetSet.getContract());

	}

	private List<DailyPaymentCheckFlowExcel> create_interface_payment_detail_csv(Principal principal, String ip, Date startDate, Date endDate, String financialContractUuid, List<PaymentInstitutionName> paymentGateways) {

		List<DeductPlan> deductPlans = new ArrayList<>();
		for (PaymentInstitutionName paymentGateway:paymentGateways){
			deductPlans.addAll(deductPlanService.queryDeductPlanOrderByStatus(financialContractUuid, paymentGateway, startDate, endDate));
		}
		List<DailyPaymentCheckFlowExcel> dailyPaymentCheckFlowExcels = deductPlans.stream().map(DailyPaymentCheckFlowExcel::new).collect(Collectors.toList());
		List<String> deductPlanUuids = deductPlans.stream().map(DeductPlan::getDeductApplicationUuid).collect(Collectors.toList());
		recordExportLog(principal, ip, deductPlanUuids, LogFunctionType.INTERFACEEXPORTACCOUNTCHECKING);
		return dailyPaymentCheckFlowExcels;

	}

	private <T> List<String> generatePaymentDetailCsv(Principal principal, String ip,
			List<T> sourceDatas,Class<T> persistentClass, List<String> objectUuids,LogFunctionType logFunctionType) {
		
		ExcelUtil<T> excelUtil = new ExcelUtil<T>(persistentClass);
		List<String> paymentDetailsCsvs = excelUtil.exportDatasToCSV(sourceDatas);
		

		return paymentDetailsCsvs;
	}
	
	


}
