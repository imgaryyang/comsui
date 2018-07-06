package com.suidifu.morganstanley.handler;

import java.util.Map;

import com.zufangbao.sun.api.model.transfer.TransferApplicationReqModel;
import com.zufangbao.sun.api.model.transfer.TransferBillQueryModel;
import com.zufangbao.sun.yunxin.entity.transfer.TransferBill;

public interface TransferBillProcessHandler {
	
	String processTransferInfo(TransferApplicationReqModel reqModel);
	
	String buildContentForNotify(String orderUuid);
	
	Map<String, String> buildRemittanceCommandRequsetMap(TransferBill transferBill);

	String transferRequestApi(String orderUuid);
	
	String processNotifyInfo(Map<String, Object> paramMap);

	void processingTransferBillCallback(String orderUuid);

	String createTransferBill(TransferApplicationReqModel reqModel);

	String queryTranferBill(TransferBillQueryModel queryModel) throws Exception;

}
