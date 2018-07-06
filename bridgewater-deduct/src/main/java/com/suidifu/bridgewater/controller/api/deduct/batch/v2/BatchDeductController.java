package com.suidifu.bridgewater.controller.api.deduct.batch.v2;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.JDBCException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchDeductApplication;
import com.suidifu.bridgewater.api.service.batch.v2.BatchDeductApplicationService;
import com.suidifu.bridgewater.api.util.CharSet;
import com.suidifu.bridgewater.api.util.FileEncodingUtil;
import com.suidifu.bridgewater.controller.BaseApiController;
import com.suidifu.bridgewater.handler.batch.async.v2.IAsynProcessingBatchRequestHandler;
import com.suidifu.bridgewater.model.v2.BatchDeductCommandRequestModel;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_function_point;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.spec.earth.v3.CommandOpsFunctionCodes;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 批量扣款文件上传处理controller
 * 
 * @author zhangjianming
 *
 */
@Controller
@RequestMapping("/api/command")
public class BatchDeductController extends BaseApiController {

	private static final Log logger = LogFactory.getLog(BatchDeductController.class);

	@Value("#{config['batchDeductFilePath']}")
	private String batchDeductFilePath;

	@Value("#{config['batchDeductDuplicateFilePath']}")
	private String batchDeductDuplicateFilePath;
	
	@Autowired
	private BatchDeductApplicationService batchDeductApplicationService;
	
	@Autowired
	private IAsynProcessingBatchRequestHandler asynProcessingBatchRequestHandler;

	@RequestMapping(value = "", params = { ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS
			+ CommandOpsFunctionCodes.COMMAND_BATCH_DEDUCT }, method = RequestMethod.POST)
    @ApiOperation(value = "中航批处理扣款接口", notes = "中航批处理扣款接口")
    @ApiImplicitParams({
	    	@ApiImplicitParam(name = "fn", value = "功能代码",paramType = "query", dataType = "String"),
	    	@ApiImplicitParam(name = "merId", value = "商户代码",paramType = "query", dataType = "String"),
	    	@ApiImplicitParam(name = "secret", value = "商户秘钥",paramType = "query", dataType = "String"),
    })
	

	public @ResponseBody String standardBatchDeduct(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartFile,
			@ModelAttribute BatchDeductCommandRequestModel batchDeductCommandRequestModel) {

		long start = System.currentTimeMillis();
		String oppositeKeyWord = "[requestNo=" + batchDeductCommandRequestModel.getRequestNo() + ",batchDeductApplicationUuid="+batchDeductCommandRequestModel.getBatchDeductApplicationUuid()+"]";
		logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.RECV_BATCH_DEDUCT_REQUEST_FROM_OUTLIER + oppositeKeyWord + GloableLogSpec.RawData(JSON.toJSONString(batchDeductCommandRequestModel)));

		try {
			
			if(!batchDeductCommandRequestModel.isValid()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, batchDeductCommandRequestModel.getCheckFailedMsg());
			}
			// 根据文件名称取文件
			if(null == multipartFile) {
				return signErrorResult(response, ApiResponseCode.ERR_UPLOAD_FILE);
			}
			
			String fileName = multipartFile.getOriginalFilename();
			String localPath = batchDeductFilePath + fileName;
			File localFile = new File(localPath);
			if(localFile.exists()) {
				return signErrorResult(response, ApiResponseCode.ERR_FILE_EXIST);
			}
			//校验md5
			String md5Hex = DigestUtils.md5Hex(multipartFile.getBytes());
			
			if(! batchDeductCommandRequestModel.getMd5().equals(md5Hex)) {
				
				logger.error("#standardBatchDeduct# check file md5 signature fail,expected ["+ batchDeductCommandRequestModel.getMd5()+"],actual is ["+md5Hex+"]");
				
				return signErrorResult(response, ApiResponseCode.ERR_FILE_MD5_NOT_MATCH);
			}
			
			// 上传的文件写入到指定的文件中
			multipartFile.transferTo(localFile);
			
			//校验编码
			String fileEncode = FileEncodingUtil.getFileEncode(localPath);
			if(! CharSet.UTF8.equals(fileEncode)) {
				return signErrorResult(response, ApiResponseCode.ERR_FILE_ENCODING_NOT_CORRECT);
			}  
			BatchDeductApplication batchDeductApplication = null;
			
			try{
			// 保存批次
				batchDeductApplication = new BatchDeductApplication(batchDeductCommandRequestModel.getBatchDeductApplicationUuid(), batchDeductCommandRequestModel.getBatchDeductId(), batchDeductCommandRequestModel.getRequestNo(), localPath, batchDeductCommandRequestModel.getNotifyUrl());
				batchDeductApplicationService.save(batchDeductApplication);
			}catch (JDBCException e) {
				//TODO move file to duplicate dir
				localFile.renameTo(new File(batchDeductDuplicateFilePath+fileName));
				return signErrorResult(response, ApiResponseCode.ERR_BATCH_DEDUCT_APPLICATION_SAVE);
			}
//			if(!saveSuccess) {
//				//删除文件
//				localFile.delete();
//				return signErrorResult(response, ApiResponseCode.ERR_FILE_ACCEPT);
//			}
			String merId = request.getHeader(ApiConstant.PARAMS_MER_ID);
			String secret = request.getHeader(ApiConstant.PARAMS_SECRET);
			
			//异步处理批处理文件
			asynProcessingBatchRequestHandler.processingBatchRequest(batchDeductApplication.getBatchDeductApplicationUuid(),merId,secret);

			long end = System.currentTimeMillis();
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.RECV_BATCH_DEDUCT_REQUEST_FROM_OUTLIER + oppositeKeyWord + "[SUCC] use:" + (end-start) + "ms");

			return signSucResult(response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.RECV_BATCH_DEDUCT_REQUEST_FROM_OUTLIER +oppositeKeyWord  +"[ERR:" + e.getMessage()+"]");
			return signErrorResult(response, e);
		}
	}

}