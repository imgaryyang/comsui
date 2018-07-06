package com.suidifu.bridgewater.controller.api.deduct;


import com.suidifu.bridgewater.api.model.BatchDeductStatusQueryModel;
import com.suidifu.bridgewater.api.model.BatchDeductStatusQueryResult;
import com.suidifu.bridgewater.api.model.DeductQueryResult;
import com.suidifu.bridgewater.api.model.OverdueDeductResultQueryModel;
import com.suidifu.bridgewater.controller.BaseApiController;
import com.suidifu.bridgewater.handler.BatchDeductStatusQureyLogHandler;
import com.suidifu.bridgewater.handler.DeductApplicationLogHandler;
import com.suidifu.bridgewater.handler.DeductQueryApiHandler;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.spec.earth.v3.QueryOpsFunctionCodes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/api/query")
public class QueryApiDeductController extends BaseApiController {
 
	private static final Log logger = LogFactory.getLog(QueryApiDeductController.class);
	
	@Autowired
	private DeductApplicationLogHandler deductApplicationLogHandler;
	
	@Autowired 
	private BatchDeductStatusQureyLogHandler batchDeductStatusQureyLogHandler;
	
	@Autowired
	private DeductQueryApiHandler  deductQueryApiHandler;
	
	//扣款查询接口
	@RequestMapping(value= "/deduct_status" ,params ={ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS + QueryOpsFunctionCodes.QUERY_DEDUCT_RESULT}, method = RequestMethod.POST)
	public @ResponseBody String queryOverdueDeductResultV2(@ModelAttribute OverdueDeductResultQueryModel queryModel, HttpServletResponse response,HttpServletRequest request) {
		try {
			if(!queryModel.isValid()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, queryModel.getCheckFailedMsg());
			}
			
			
		 	//校验请求号，生成扣款查询记录
			deductApplicationLogHandler.checkAndSaveRequest(queryModel,request);
			DeductQueryResult queryResult=null;
			synchronized(CommandApiDeductController.gloableLock)
			{
				queryResult = deductQueryApiHandler.apideductQuery(queryModel,request);
			}
			return signSucResult(response,"deductQueryResult",queryResult);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#queryOverdueDeductResult occur error [requestNo : "+ queryModel.getRequestNo() +" ]!");
			return signErrorResult(response, e);
		}
	}
	
	//批量扣款状态查询接口
	@RequestMapping(value= "/deduct_status_list" ,params ={ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS + QueryOpsFunctionCodes.BATCH_DEUDCT_STATUS}, method = RequestMethod.POST)
	public @ResponseBody String queryBatchDeductStatusV2(@ModelAttribute BatchDeductStatusQueryModel queryModel, HttpServletResponse response,HttpServletRequest request) {
		try {
			if(!queryModel.isValid()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, queryModel.getCheckFailedMsg());
			}
			//校验请求号，生成扣状态查询记录
			batchDeductStatusQureyLogHandler.checkAndSaveRequest(queryModel,request);
			long start = System.currentTimeMillis();
			List<BatchDeductStatusQueryResult> queryResult = deductQueryApiHandler.apiBatchDeductStatusQuery(queryModel,request);
			long end = System.currentTimeMillis();
			logger.info("执行queryBatchDeductStatus的时间："+(end-start)+"[ms]");
			return signSucResult(response,"responseList",queryResult);
	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#queryBatchDeductStatus occur error [requestNo : "+ queryModel.getRequestNo() +" ]!");
			return signErrorResult(response, e);
		}
	}
	
	
	//扣款查询接口
		@RequestMapping(value= "" ,params ={ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS + QueryOpsFunctionCodes.QUERY_DEDUCT_RESULT}, method = RequestMethod.POST)
		public @ResponseBody String queryOverdueDeductResult(@ModelAttribute OverdueDeductResultQueryModel queryModel, HttpServletResponse response,HttpServletRequest request) {
			try {
				if(!queryModel.isValid()) {
					return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, queryModel.getCheckFailedMsg());
				}
				
				
			 	//校验请求号，生成扣款查询记录
				deductApplicationLogHandler.checkAndSaveRequest(queryModel,request);
				DeductQueryResult queryResult=null;
				synchronized(CommandApiDeductController.gloableLock)
				{
					queryResult = deductQueryApiHandler.apideductQuery(queryModel,request);
				}
				return signSucResult(response,"DeductQueryResult",queryResult);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("#queryOverdueDeductResult occur error [requestNo : "+ queryModel.getRequestNo() +" ]!");
				return signErrorResult(response, e);
			}
		}
		
		//批量扣款状态查询接口
		@RequestMapping(value= "" ,params ={ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS + QueryOpsFunctionCodes.BATCH_DEUDCT_STATUS}, method = RequestMethod.POST)
		public @ResponseBody String queryBatchDeductStatus(@ModelAttribute BatchDeductStatusQueryModel queryModel, HttpServletResponse response,HttpServletRequest request) {
			try {
				if(!queryModel.isValid()) {
					return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, queryModel.getCheckFailedMsg());
				}
				//校验请求号，生成扣状态查询记录
				batchDeductStatusQureyLogHandler.checkAndSaveRequest(queryModel,request);
				long start = System.currentTimeMillis();
				List<BatchDeductStatusQueryResult> queryResult = deductQueryApiHandler.apiBatchDeductStatusQuery(queryModel, request);
				long end = System.currentTimeMillis();
				logger.info("执行queryBatchDeductStatus的时间："+(end-start)+"[ms]");
				return signSucResult(response,"responseList",queryResult);
		
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("#queryBatchDeductStatus occur error [requestNo : "+ queryModel.getRequestNo() +" ]!");
				return signErrorResult(response, e);
			}
		}
		
}
