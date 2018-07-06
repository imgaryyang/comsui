package com.zufangbao.earth.web.controller.trade;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.handler.impl.trade.ExternalTradeFileAnalyzeFactory;
import com.zufangbao.earth.handler.impl.trade.FileAnalyzaFactory;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.trade.*;
import com.zufangbao.sun.exception.ExternalTradeBatchBindCashFlowException;
import com.zufangbao.sun.handler.trade.ExternalTradeBatchCashHandler;
import com.zufangbao.sun.handler.trade.ExternalTradeBatchHandler;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.trade.ExternalTradeBatchCashService;
import com.zufangbao.sun.service.trade.ExternalTradeBatchDetailService;
import com.zufangbao.sun.service.trade.ExternalTradeBatchService;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zufangbao.earth.handler.impl.trade.ExternalTradeFileAnalyzeFactory.*;

@RestController
@RequestMapping("trade/external-batch")
@MenuSetting("menu-capital")
public class ExternalTradeBatchController extends BaseController {

	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	private ExternalTradeBatchHandler externalTradeBatchHandler;

	@Autowired
	private ExternalTradeBatchDetailService externalTradeBatchDetailService;

	@Autowired
	private ExternalTradeBatchService externalTradeBatchService;

	@Autowired
	private ExternalTradeBatchCashService externalTradeBatchCashService;

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private ExternalTradeFileAnalyzeFactory analyzeFactory;

	@Autowired
	private ExternalTradeBatchCashHandler externalTradeBatchCashHandler;

	@Autowired
	private CashFlowService cashFlowService;

	@Autowired
	private FileAnalyzaFactory fileAnalyseFactory;

	@Autowired
	private PrincipalService principalService;

	private static final Log logger = LogFactory.getLog(ExternalTradeBatchController.class);

	@RequestMapping(value = "")
	@MenuSetting("submenu-external-trade-batch")
	public ModelAndView showExternalTradeBatchPage(@Secure Principal principal) {
		return new ModelAndView("index");
	}

	@RequestMapping("/optionData")
	public @ResponseBody String getOptionDataForExternalTradeBatchPage(@Secure Principal principal) {
		try {
			Map<String, Object> data = new HashMap<>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			data.put("queryAppModels", queryAppModels);
			data.put("tradeBatchState", ExternalTradeBatchMapSpec.tradeBatchStateShowStringMap);
			data.put("arrivalState", ExternalTradeBatchMapSpec.arrivalStateShowStringMap);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect,
					SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#getOptionDataForExternalTradeBatchPage occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

	@RequestMapping(value = "/query")
	public @ResponseBody String queryExternalTradeBatchList(ExternalTradeBatchQueryModel externalTradeBatchQueryModel,
			Page page) {
		try {
			Map<Long, FinancialContract> financialContractMap = financialContractService.getAllFinancialContractMap();
			int total = externalTradeBatchService.count(externalTradeBatchQueryModel);

			List<ExternalTradeBatch> externalTradeBatchs = externalTradeBatchService.getExternalTradeBatchListBy(externalTradeBatchQueryModel, page);
			List<ExternalTradeBatchDisplayModel> list = new ArrayList<>();
			for (ExternalTradeBatch externalTradeBatch : externalTradeBatchs) {
				Long financialContractId = externalTradeBatch.getFinancialContractId();
				FinancialContract financialContract = financialContractMap.getOrDefault(financialContractId, new FinancialContract());
				Integer flag = getButtonFlag(externalTradeBatch);
				list.add(new ExternalTradeBatchDisplayModel(externalTradeBatch, financialContract,flag));
			}

			Map<String, Object> data = new HashMap<>();
			data.put("list", list);
			data.put("size", total);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			logger.error("#queryExternalTradeBatchList occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

	@RequestMapping(value = "/detail/optionData")
	public @ResponseBody String getOptionDataForExternalTradeBatchDetail() {
		try {
			Map<String, Object> data = new HashMap<>();

			data.put("tradeAuditResult", ExternalTradeBatchMapSpec.tradeAuditResultShowStringMap);
			data.put("cashFlowAuditResult", ExternalTradeBatchMapSpec.cashFlowAuditResultShowStringMap);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#getOptionDataForExternalTradeBatchDetail occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

	@RequestMapping(value = "/detail")
	public @ResponseBody String externalTradeBatchDetail(String externalTradeBatchUuid) {
		try {
			ExternalTradeBatch externalTradeBatch = externalTradeBatchService.getUniqueExternalTradeBatchBy(externalTradeBatchUuid);
			if(externalTradeBatch == null) {
				return jsonViewResolver.errorJsonResult("回盘数据不存在！");
			}
			Long financialContractId = externalTradeBatch.getFinancialContractId();
			FinancialContract financialContract = financialContractService.getFinancialContractById(financialContractId);
			//0.作废 1.结清 2.both
			Integer flag = getButtonFlag(externalTradeBatch);
			ExternalTradeBatchDisplayModel batchInfo = new ExternalTradeBatchDisplayModel(externalTradeBatch, financialContract,flag);

			List<ExternalTradeBatchCash> batchCashFlows = externalTradeBatchService.getAssociatedCashFlowsBy(externalTradeBatchUuid);
			BigDecimal totalCashFlow = getTotalCashFlow(batchCashFlows);


			String hostAccountNo = financialContract.getCapitalAccount().getAccountNo();

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("batchInfo", batchInfo);
			data.put("batchCashFlows", batchCashFlows);
			data.put("totalCashFlow", totalCashFlow);
			data.put("hostAccountNo", hostAccountNo);
			data.put("flag", flag);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			logger.error("#externalTradeBatchDetail occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

	private Integer getButtonFlag(ExternalTradeBatch externalTradeBatch) {
		if(externalTradeBatch.getActiveStatus() == ExternalTradeActiveStatus.INVALID) return -1;
		if(externalTradeBatch.getCashFlowAuditResult()==ExternalTradeCashFlowAuditResult.UNCLEAR){
			if(externalTradeBatch.getDetailVerifyResultSummary()==ExternalTradeVerifyResult.NOT_PASS) return 0;
			else if(externalTradeBatch.getTradeAuditProgress()==ExternalTradeAuditProgress.UNCHECK) return 2;
			else if(externalTradeBatch.getTradeAuditProgress()==ExternalTradeAuditProgress.CHECKED) return 1;
		}
		return -1;
	}



	@RequestMapping(value = "/importData",method=RequestMethod.POST)
	public @ResponseBody String importData(@Secure Principal principal,@RequestParam MultipartFile file,@RequestParam String financialContractNo,HttpServletRequest request) {
		try {
			FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(financialContractNo);
			if(financialContract == null) {
				return jsonViewResolver.errorJsonResult("项目不存在！");
			}
			String savePath = analyzeFactory.getUploadPath() + financialContract.getContractNo() + "/storage/";
			InputStream ins=file.getInputStream();
			String fileName = file.getOriginalFilename();
			File localFile=new File(savePath+fileName);
			if (localFile.exists()) {
				return jsonViewResolver.errorJsonResult("文件已存在,无法进行导入！");
			}

			Map<String, String> fileInfos = analyzeFactory.analyzeFileName(fileName, CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN);

			String productNo = fileInfos.get(CONST_FILE_INFO_PRODUCT_CODE);
			String batchNo = fileInfos.get(CONST_FILE_INFO_BATCH_NO);
			String tradeDate = fileInfos.get(CONST_FILE_INFO_TRADE_DATE);
			String operatorName = principal.getName();
            String ipAddress = IpUtil.getIpAddress(request);

			if(!StringUtils.equals(productNo, financialContractNo)){
				return jsonViewResolver.errorJsonResult("回盘文件信息与所选项目不符！");
			}
			List<ExternalTradeBatchDetailModel> list= analyzeFactory.analyzeDetail(ins, CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN);
			if(CollectionUtils.isEmpty(list)){
				return jsonViewResolver.errorJsonResult("回盘文件内容解析异常！");
			}
			externalTradeBatchHandler.importDataForExternalTradeBatch(list, productNo, batchNo, operatorName, ipAddress, tradeDate);

			this.saveFile(file, savePath);//保存文件

			return jsonViewResolver.sucJsonResult();
		} catch ( CommonException e) {
			String message=e.getErrorMsg();
			logger.error("ExternalTradeBatchController文件导入失败,失败原因为:"+message);
			return jsonViewResolver.errorJsonResult(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ExternalTradeBatchController:#importData occur error.");
			return jsonViewResolver.errorJsonResult(IMPORT_ERROR);
		}
	}

	@RequestMapping(value = "/detail/query")
	public @ResponseBody String queryExternalTradeBatchDetailList(
			ExternalTradeBatchDetailQueryModel queryModel, Page page) {
		try {
	    	ExternalTradeBatch externalTradeBatch = externalTradeBatchService.getUniqueExternalTradeBatchBy(queryModel.getExternalTradeBatchUuid());
			if(externalTradeBatch == null) {
				return jsonViewResolver.errorJsonResult("回盘数据不存在！");
			}
			Map<Long, FinancialContract> financialContractMap = financialContractService.getAllFinancialContractMap();
			int total = externalTradeBatchDetailService.count(queryModel);

			List<ExternalTradeBatchDetail> externalTradeBatchDetails = externalTradeBatchDetailService.getExternalTradeBatchDetailListBy(queryModel, page);
			List<ExternalTradeBatchDetailDisplayModel> list = new ArrayList<>();

			Map<ExternalTradeType, List<String>> externalTradeBatchCashBankSequence = externalTradeBatchCashHandler.getExternalTradeBatchCashBankSequence(queryModel.getExternalTradeBatchUuid());
			for (ExternalTradeBatchDetail detail : externalTradeBatchDetails) {
				Long financialContractId = detail.getFinancialContractId();
				FinancialContract financialContract = financialContractMap.getOrDefault(financialContractId, new FinancialContract());
				List<String> sequenceNoList = externalTradeBatchCashBankSequence.get(detail.getTradeType());
				String sequenceNos = StringUtils.join(sequenceNoList, ",");
				list.add(new ExternalTradeBatchDetailDisplayModel(detail, financialContract, sequenceNos));
			}

			Map<String, Object> data = new HashMap<>();
			data.put("list", list);
			data.put("size", total);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			logger.error("#queryExternalTradeBatchDetailList occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

	@RequestMapping(value = "/invalid")
	@MenuSetting("submenu-external-trade")
	public @ResponseBody String invalidExternalTradeBatch(String externalTradeBatchUuid){
		try {
			if (!externalTradeBatchHandler.invalidExternalTradeBatch(externalTradeBatchUuid)) {
				return jsonViewResolver.errorJsonResult("作废失败");
			}
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("#invalidExternalTradeBatch occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

	@RequestMapping(value = "/settle")
	@MenuSetting("submenu-external-trade")
	public @ResponseBody String settleExternalTradeBatch(String externalTradeBatchUuid){
		try {
			if (!externalTradeBatchHandler.settleExternalTradeBatch(externalTradeBatchUuid)) {
				return jsonViewResolver.errorJsonResult("结清失败");
			}
			ExternalTradeBatch externalTradeBatch=externalTradeBatchService.getUniqueExternalTradeBatchBy(externalTradeBatchUuid);
			String contractNo = externalTradeBatch.getFinancialProductCode();
			String externalBatchNo=externalTradeBatch.getExternalBatchNo();

			String savePath = analyzeFactory.getUploadPath() + contractNo + "/result/";
			if(!fileAnalyseFactory.doReturnFtpFile(externalTradeBatchUuid, savePath, contractNo,externalBatchNo)){
				return jsonViewResolver.errorJsonResult(RETURN_FILE_ERROE);
			}
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("#settleExternalTradeBatch occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

	private void saveFile(MultipartFile file,String savePath) throws IllegalStateException, IOException{
		if(!isTwoInValid(file,savePath)){
			return;
		}
		File fileTmp = new File(savePath);
		judeDirExists(fileTmp);
		File aimFile=new File(savePath+file.getOriginalFilename());
		judeFileExists(aimFile);
		file.transferTo(aimFile);
	}

	// 判断文件是否存在
	private void judeFileExists(File file) {

		if (!file.exists()) {
			logger.info("file not exists, create it ...");
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("ExternalTradeBatchHandlerImpl：文件创建失败！");
			}
		}

	}

	// 判断文件夹是否存在
	private void judeDirExists(File file) {

		if (!file.exists() && !file.isDirectory()) { 		// 目录不存在，需要创建
			logger.info("dir not exists, create it ...");
			file.mkdirs();  									//创建目录
		}
	}

	private boolean isTwoInValid(Object o,Object two) {
        return !(o == null || two == null);
	}

	@RequestMapping(value = "/cash/query", method = RequestMethod.GET)
	public @ResponseBody String queryCashFlowForExternalTradeBatch(
			ExternalTradeBatchCashQueryModel queryModel, Page page) {
		try {
			String externalTradeBatchUuid = queryModel.getExternalTradeBatchUuid();
			ExternalTradeBatch externalTradeBatch = externalTradeBatchService.getUniqueExternalTradeBatchBy(externalTradeBatchUuid);
			if(externalTradeBatch == null) {
				return jsonViewResolver.errorJsonResult("回盘数据不存在！");
			}
			String financialContractUuid = externalTradeBatch.getFinancialContractUuid();
			FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
			if(financialContract == null) {
				return jsonViewResolver.errorJsonResult("回盘文件所属项目不存在！");
			}
			String hostAccountNo = financialContract.getCapitalAccount().getAccountNo();
			queryModel.setHostAccountNo(hostAccountNo);

			List<CashFlow> cashFlows = cashFlowService.getCashFlowList(queryModel, page);
			int size = cashFlowService.count(queryModel);

			List<ExternalTradeBatchCashDisplayModel> list = new ArrayList<ExternalTradeBatchCashDisplayModel>();
			for (CashFlow cf : cashFlows) {
				list.add(new ExternalTradeBatchCashDisplayModel(cf));
			}

			List<JSONObject> transferDetails = getUnbindCashFlowTransferDetails(externalTradeBatchUuid);

			Map<String, Object> tradeTypes = new HashMap<>();
			tradeTypes.put("0",ExternalTradeType.THIRD_PARTY_DEDUCT.getChineseMessage() );
			tradeTypes.put("2",ExternalTradeType.CASH.getChineseMessage());
			Map<String, Object> data = new HashMap<>();
			data.put("list", list);
			data.put("size", size);
			data.put("tradeTypes", tradeTypes);
			data.put("transferDetails", transferDetails);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			logger.error("#queryCashFlowForExternalTradeBatch occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

	private BigDecimal getTotalCashFlow(List<ExternalTradeBatchCash> list) {
		BigDecimal totalCashFlow = BigDecimal.ZERO;
		for (ExternalTradeBatchCash externalTradeBatchCash : list) {
			totalCashFlow = totalCashFlow.add(externalTradeBatchCash.getTradeAmount());
		}
		return totalCashFlow;
	}

	private List<JSONObject> getUnbindCashFlowTransferDetails(
			String externalTradeBatchUuid) {
		List<ExternalTradeBatchDetail> batchTransferDetails = externalTradeBatchDetailService.getDetailsByTradeType(externalTradeBatchUuid, ExternalTradeType.TRANSFER);

		List<JSONObject> transferDetails = new ArrayList<JSONObject>();
		for (ExternalTradeBatchDetail detail : batchTransferDetails) {
			if(StringUtils.isNotBlank(detail.getCashFlowSerialNo())) {
				continue;
			}
			if(detail.getTradeAuditResult() != ExternalTradeAuditResult.NOT_PASS) {
				continue;
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("externalTradeBatchDetailUuid", detail.getExternalTradeBatchDetailUuid());
			jsonObject.put("externalRecordUniqueId", detail.getExternalRecordUniqueId());
			jsonObject.put("tradeAmount", detail.getAmount());
			jsonObject.put("counterAccountNo", detail.getCpBankCardNo());
			jsonObject.put("counterAccountName", detail.getCpName());
			transferDetails.add(jsonObject);
		}
		return transferDetails;
	}

	@RequestMapping(value = "/cash/bind", method = RequestMethod.POST)
	public @ResponseBody String bindCashFlowForExternalTradeBatch(
			@Secure Principal principal, String externalTradeBatchUuid,
			String cashFlowInfos, HttpServletRequest request) {
		try {
			List<JSONObject> cashFlowInfoList = JsonUtils.parseArray(cashFlowInfos, JSONObject.class);
			if(CollectionUtils.isEmpty(cashFlowInfoList)) {
				return jsonViewResolver.errorJsonResult("至少选择一条流水进行关联！");
			}

			ExternalTradeBatch externalTradeBatch = externalTradeBatchService.getUniqueExternalTradeBatchBy(externalTradeBatchUuid);
			if(externalTradeBatch == null) {
				return jsonViewResolver.errorJsonResult("回盘数据不存在！");
			}

			externalTradeBatchHandler.bindCashFlows(principal.getId(),
                    IpUtil.getIpAddress(request), externalTradeBatch,
                    cashFlowInfoList);

			return jsonViewResolver.sucJsonResult();
		} catch (ExternalTradeBatchBindCashFlowException cfe) {
			logger.error("#bindCashFlowForExternalTradeBatch occur error, reason［"+cfe.getMessage()+"］");
			return jsonViewResolver.errorJsonResult(cfe.getMessage());
		} catch (Exception e) {
			logger.error("#bindCashFlowForExternalTradeBatch occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE);
		}
	}

	@RequestMapping(value = "/cash/unbind", method = RequestMethod.POST)
	public @ResponseBody String unbindCashFlowForExternalTradeBatch(
			@Secure Principal principal, String externalTradeBatchUuid,
			String cashFlowUuid, HttpServletRequest request) {
		try {
			ExternalTradeBatch externalTradeBatch = externalTradeBatchService.getUniqueExternalTradeBatchBy(externalTradeBatchUuid);
			if(externalTradeBatch == null) {
				return jsonViewResolver.errorJsonResult("回盘数据不存在！");
			}
			if(externalTradeBatch.getCashFlowAuditResult() == ExternalTradeCashFlowAuditResult.CLEAR) {
				return jsonViewResolver.errorJsonResult("回盘批次已结清，不能解绑流水！");
			}

			int unbindSize = externalTradeBatchHandler.unbindCashFlow(
                    principal.getId(), IpUtil.getIpAddress(request),
                    externalTradeBatch, cashFlowUuid);

			if(unbindSize < 1) {
				jsonViewResolver.errorJsonResult("流水解绑失败！");
			}
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("#unbindCashFlowForExternalTradeBatch occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE);
		}
	}

}
