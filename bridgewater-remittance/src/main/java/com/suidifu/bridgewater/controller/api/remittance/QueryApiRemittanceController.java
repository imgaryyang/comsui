package com.suidifu.bridgewater.controller.api.remittance;

import com.alibaba.fastjson.JSON;
import com.suidifu.bridgewater.api.model.RemittanceQueryResult;
import com.suidifu.bridgewater.api.model.RemittanceResultBatchPackModel;
import com.suidifu.bridgewater.controller.BaseApiController;
import com.suidifu.bridgewater.handler.IRemittanceQueryApiHandler;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_remittance_function_point;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.spec.earth.v3.QueryOpsFunctionCodes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class QueryApiRemittanceController extends BaseApiController {
	
	private static final Log logger = LogFactory
			.getLog(QueryApiRemittanceController.class);
	
	
	@Autowired
	IRemittanceQueryApiHandler iRemittanceQueryApiHandler;
	@Value("#{config['remittance.batchquery.size']}")
	private int REMITTANCE_BATCH_QUERY_SIZE = 30;

	//TODO 1、看能不能合并中航和云信查询和合并
	// 2 读取缓存
	/**
	 * 放款结果批量查询
	 */
	@RequestMapping(value = "", params = { ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS
			+ QueryOpsFunctionCodes.BATCH_QUERY_REMITTANCE_RESULT }, method = RequestMethod.POST)
	public @ResponseBody String batchQueryRemittanceResult(@ModelAttribute RemittanceResultBatchPackModel batchQueryModel,
			HttpServletResponse response, HttpServletRequest request) {

		try {
			String oppositeKeyWord = "[requestNo=" + batchQueryModel.getRequestNo() + "]";
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.RECV_QUERY_REQUEST_FROM_OUTLIER_SYSTEM + oppositeKeyWord + GloableLogSpec.RawData(JSON.toJSONString(batchQueryModel)));
			if(!batchQueryModel.isValid()) {
                return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, batchQueryModel.getCheckFailedMsg());
                
            }
			if(batchQueryModel.getRemittanceResultBatchQueryModelList().size() > this.REMITTANCE_BATCH_QUERY_SIZE) {
				return signErrorResult(response, ApiResponseCode.BEYOND_QUERY_API_REMITTANCE_COUNT, "批量请求条数不能超过"
									 + this.REMITTANCE_BATCH_QUERY_SIZE +"条");
			}
			//批量查询放款结果

			List<RemittanceQueryResult> remittanceQueryResultList = iRemittanceQueryApiHandler.apiRemittanceResultBatchQuery(batchQueryModel);
			return signSucResult(response, "remittanceQueryResultList", remittanceQueryResultList);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#queryRemittanceResult occur error [requestNo : "+  batchQueryModel.getRequestNo()  +" ]!");
			return signErrorResult(response, e);
		}

	}
	/**

	 * 放款结果批量查询

	 */
	@RequestMapping(value = "/remittance_status_list", params = { ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS
			+ QueryOpsFunctionCodes.BATCH_QUERY_REMITTANCE_RESULT }, method = RequestMethod.POST)
	public @ResponseBody String batchQueryRemittanceResultForZhonghang(@ModelAttribute RemittanceResultBatchPackModel batchQueryModel,
			HttpServletResponse response, HttpServletRequest request) {

		try {
			String oppositeKeyWord = "[requestNo=" + batchQueryModel.getRequestNo() + "]";
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.RECV_QUERY_REQUEST_FROM_OUTLIER_SYSTEM + oppositeKeyWord + GloableLogSpec.RawData(JSON.toJSONString(batchQueryModel)));
			if(!batchQueryModel.isValid()) {
                return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, batchQueryModel.getCheckFailedMsg());
                
            }
			if(batchQueryModel.getRemittanceResultBatchQueryModelList().size() > this.REMITTANCE_BATCH_QUERY_SIZE) {
				return signErrorResult(response, ApiResponseCode.BEYOND_QUERY_API_REMITTANCE_COUNT, "批量请求条数不能超过"
									 + this.REMITTANCE_BATCH_QUERY_SIZE +"条");
			}
			//批量查询放款结果

			List<RemittanceQueryResult> remittanceQueryResultList = iRemittanceQueryApiHandler.apiRemittanceResultBatchQuery(batchQueryModel);
			return signSucResult(response, "remittanceQueryResultList", remittanceQueryResultList);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#queryRemittanceResult occur error [requestNo : "+  batchQueryModel.getRequestNo()  +" ]!");
			return signErrorResult(response, e);
		}

	}
	
}