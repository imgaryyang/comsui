package com.suidifu.morganstanley.controller.api.transfer;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.TransferBillNoSession;
import com.suidifu.morganstanley.handler.TransferBillProcessHandler;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.transfer.TransferApplicationReqModel;
import com.zufangbao.sun.api.model.transfer.TransferBillQueryModel;
import com.zufangbao.sun.yunxin.service.DictionaryService;

@Controller
@RequestMapping(URL_API_V3)
public class CommandApiTransferApplicationController extends BaseApiController{

	private static final Log logger = LogFactory.getLog(CommandApiTransferApplicationController.class);
	

	@Autowired
	private TransferBillNoSession transferBillNoSession;
	@Autowired
	private TransferBillProcessHandler transferBillProcessHandler;
	@Autowired
	private DictionaryService dictionaryService;
	
	
	@RequestMapping(value = "/transfer" , method = RequestMethod.POST)
	public @ResponseBody String createTransferBill(@ModelAttribute TransferApplicationReqModel reqModel,
			HttpServletRequest request, HttpServletResponse response){
		String oppositeKeyWord="[requestNo="+reqModel.getRequestNo()+",orderUniqueId="+reqModel.getOrderNo()+"]";
		logger.info(GloableLogSpec.AuditLogHeaderSpec() + "request create TransferBill "+oppositeKeyWord);
		try {
			//为空校验
			if(!reqModel.isValid()){
				logger.info("requestNo:["+reqModel.getRequestNo()+"] 参数校验失败:["+reqModel.getCheckFailedMsg()+"]");
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, reqModel.getCheckFailedMsg());
			}
			//数据校验
			transferBillNoSession.transferInfoCheck(reqModel);
			//处理请求
			String result = transferBillProcessHandler.processTransferInfo(reqModel);
			if(StringUtils.isEmpty(result)){
				return signErrorResult(response, new ApiException(ApiResponseCode.INVALID_PARAMS));
			}
			String privateKey = dictionaryService.getPlatformPrivateKey();
		    return ApiSignUtils.signAndReturnResult(response, result, privateKey);
			
		} catch (ApiException apiException) {
			return signErrorResult(response, apiException);
		} catch (Exception e){
			e.printStackTrace();
			return signErrorResult(response, e);
		}
	}
	
	@RequestMapping(value = "/queryTransfer" , method = RequestMethod.POST)
	public @ResponseBody String queryTransferBill(@ModelAttribute TransferBillQueryModel queryModel, HttpServletRequest request, HttpServletResponse response){
		String oppositeKeyWord="[requestNo="+queryModel.getRequestNo()+",orderUniqueId="+queryModel.getOrderNo()+",orderUuid="+queryModel.getOrderUuid()+"]";
		logger.info(GloableLogSpec.AuditLogHeaderSpec() + "request query TransferBill## "+oppositeKeyWord);
		try {
			//为空校验
			if(!queryModel.isValid()){
				logger.info("requestNo:["+queryModel.getRequestNo()+"] 参数校验失败:["+queryModel.getCheckFailedMsg()+"]");
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, queryModel.getCheckFailedMsg());
			}
			String result = transferBillProcessHandler.queryTranferBill(queryModel);
			String privateKey = dictionaryService.getPlatformPrivateKey();
		    return ApiSignUtils.signAndReturnResult(response, result, privateKey);
			
		} catch (ApiException apiException) {
			return signErrorResult(response, apiException);
		} catch (Exception e){
			e.printStackTrace();
			return signErrorResult(response, e);
		}
	}
	
	
	
}
