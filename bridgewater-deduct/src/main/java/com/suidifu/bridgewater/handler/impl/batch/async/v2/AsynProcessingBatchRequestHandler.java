package com.suidifu.bridgewater.handler.impl.batch.async.v2;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchDeductApplication;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchProcessStatus;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.DeductPlanApplicationCheckLog;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.ProcessDeductResultFailType;
import com.suidifu.bridgewater.api.service.batch.v2.BatchDeductApplicationService;
import com.suidifu.bridgewater.api.service.batch.v2.DeductPlanApplicationCheckLogService;
import com.suidifu.bridgewater.api.util.BatchDeductItemHelper;
import com.suidifu.bridgewater.api.util.DeductPlanApplicationCheckLogHelper;
import com.suidifu.bridgewater.deduct.notify.handler.batch.v2.DeductNotifyJobServer;
import com.suidifu.bridgewater.handler.batch.async.v2.IAsynProcessingBatchRequestHandler;
import com.suidifu.bridgewater.handler.batch.v2.SingleDeductDelegateHandler;
import com.suidifu.bridgewater.model.v2.BatchDeductItem;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_function_point;

/**
 * @author wukai
 * 异步批量扣款文件处理
 */
@Component("asynProcessingBatchRequestHandler")
public class AsynProcessingBatchRequestHandler implements IAsynProcessingBatchRequestHandler{

	private static Log log = LogFactory.getLog(AsynProcessingBatchRequestHandler.class);

	@Autowired
	private DeductPlanApplicationCheckLogService deductPlanApplicationCheckLogService;

	@Autowired
	private SingleDeductDelegateHandler singleDeductDelegateHandler;

	@Autowired
	private BatchDeductApplicationService batchDeductApplicationService;

	@Value("#{config['notifyserver.groupCacheJobQueueMap_group0']}")
	private String groupNameForBatchDeduct;

	@Autowired
	private DeductNotifyJobServer deductNotifyJobServer;

	@Override
    @Async(value="batchDeductAsyncExecutor")
	public void processingBatchRequest(String batchDeductApplicationUuid,String  merId, String secret){

		String oppositeKeyWord="[batchDeductApplicationUuid="+batchDeductApplicationUuid+"]";

		log.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.PROCESSING_BATCH_DEDUCT_REQUEST_FROM_OUTLIER + oppositeKeyWord + "[BEGIN]" );

		BatchDeductApplication batchDeductApplication = batchDeductApplicationService.getBatchDeductApplicationBy(batchDeductApplicationUuid);

		String batchFilePath = batchDeductApplication.getFilePath();

		File file = new File(batchFilePath);

		if(!file.exists()){
			log.error("#processingBatchRequest# no file exist with batchFilePath["+batchFilePath+"]");
			return;
		}

//		开始处理批处理扣款文件

		batchDeductApplicationService.updateBatchDeductApplicationStatus(batchDeductApplication.getId(), BatchProcessStatus.Processing, null, null, 0, null);

		LineIterator lt = null;

		int lineNumber = 0;

		String exceptionUuid = UUID.randomUUID().toString();

		boolean occurException = false;

		String errorMsg = null;

		try {

			lt  = FileUtils.lineIterator(file, "UTF-8");

			while(lt.hasNext()){

				String content = lt.next();

				lineNumber++;

				processSingleLineContent(batchDeductApplication,content,lineNumber, merId,  secret);
			}

		} catch (Exception e) {

			occurException = true;

			log.error("#processingBatchRequest# occur exception with stack trace["+ExceptionUtils.getFullStackTrace(e)+"],with file path["+batchFilePath+"],with exceptionUuid["+exceptionUuid+"]");
			;

			errorMsg = "excpetionUuid["+exceptionUuid+"],msg["+e.getMessage()+"]";


		}finally{

			batchDeductApplicationService.updateBatchDeductApplicationStatus(batchDeductApplication.getId(), BatchProcessStatus.Done, null, lineNumber, null, occurException? errorMsg :null);

			if(null != lt){

				LineIterator.closeQuietly(lt);
			}
			log.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.PROCESSING_BATCH_DEDUCT_REQUEST_FROM_OUTLIER + oppositeKeyWord + "[END]" );

		}

	}
	private void processSingleLineContent(BatchDeductApplication batchDeductApplication,String oneLineContent,int lineNumber,String  merId, String secret){

		BatchDeductItem batchDeductItem = new BatchDeductItem();

		String itemExceptoinUuid = UUID.randomUUID().toString();

		try{

			batchDeductItem = BatchDeductItemHelper.buildBatchDeductItem(oneLineContent,lineNumber);

			batchDeductItem.validate();

		}catch(Exception e){

			log.error("#processOneLineContent# validate parameters occur exception ],with itemExceptoinUuid["+itemExceptoinUuid+"],with statck trace["+ExceptionUtils.getFullStackTrace(e)+"]");

			String failReason = "errorMsg["+e.getMessage()+"]+one line content["+oneLineContent+"],line number["+lineNumber+"],itemExceptoinUuid["+itemExceptoinUuid+"]";

			logDeductItemWhenOccurException(batchDeductApplication,

					batchDeductItem, failReason);

			return;
		}

		try{
		//调用单次扣款
			singleDeductDelegateHandler.pushDeductJob(deductNotifyJobServer, batchDeductApplication, batchDeductItem, merId, secret,groupNameForBatchDeduct);
		}catch(Exception e){

			log.error("#pushDeductJob#  occur exception with stack trace["+ExceptionUtils.getFullStackTrace(e)+"],with itemExceptoinUuid["+itemExceptoinUuid+"]");

			String failReason = "exception message["+e.getMessage()+"],one line content["+oneLineContent+"],line number["+lineNumber+"],itemExceptoinUuid["+itemExceptoinUuid+"]";

			logDeductItemWhenOccurException(batchDeductApplication, batchDeductItem, failReason);

		}

	}

	private void logDeductItemWhenOccurException(
			BatchDeductApplication batchDeductApplication,
			BatchDeductItem batchDeductItem, String failReason) {

		DeductPlanApplicationCheckLog deductPlanApplicationCheckLog = DeductPlanApplicationCheckLogHelper.buildErrorDeductPlanApplicationCheckLog(batchDeductApplication, batchDeductItem, failReason, ProcessDeductResultFailType.VALIDATE_FAIL);

		deductPlanApplicationCheckLogService.save(deductPlanApplicationCheckLog);

		batchDeductApplication.setFailCount(batchDeductApplication.getFailCount()+1);
		batchDeductApplication.setActualCount(batchDeductApplication.getActualCount()+1);

		batchDeductApplicationService.update(batchDeductApplication);;
	}
}
