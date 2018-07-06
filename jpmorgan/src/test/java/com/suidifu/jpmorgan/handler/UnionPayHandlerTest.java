package com.suidifu.jpmorgan.handler;

import java.math.BigDecimal;

import org.junit.Test;

import com.gnete.security.crypt.CryptException;
import com.suidifu.jpmorgan.entity.unionpay.IUnionPayApiParams;
import com.suidifu.jpmorgan.entity.unionpay.PaymentDetailInfoModel;
import com.suidifu.jpmorgan.entity.unionpay.RealTimePaymentInfoModel;
import com.suidifu.jpmorgan.entity.unionpay.TransactionResultQueryInfoModel;
import com.suidifu.jpmorgan.exception.TransactionResultQueryApiException;
import com.suidifu.jpmorgan.handler.impl.UnionPayHandlerImpl;


public class UnionPayHandlerTest {

	private IUnionPayHandler handler = new UnionPayHandlerImpl();

	private String apiUrl = "http://59.41.103.98:333/gzdsf/ProcessServlet";

	private String cerFilePath = "src/main/resources/certificate/gzdsf.cer";
	
	private String pfxFilePath = "src/main/resources/certificate/ORA@TEST1.pfx";
	
	private String pfxFileKey = "123456";

	private String num = "0000000000000000003";

	
	//实时代付测试
	@Test
	public void testExecRealTimePayment() throws CryptException {
		RealTimePaymentInfoModel model = getTestRealTimePaymentInfoModel();
		handler.execRealTimePayment(model);
	}
	
	
	//交易结果查询测试
	@Test
	public void testExecTransactionResultQuery() throws TransactionResultQueryApiException{
		TransactionResultQueryInfoModel queryInfoModel = new TransactionResultQueryInfoModel();
		queryInfoModel.setReqNo("rtqxyz2" + num);
		queryInfoModel.setQueryReqNo("xyz00000000000000000026");
		
		queryInfoModel.setUserName("operator");
		queryInfoModel.setUserPwd("operator");
		
		setUnionPayParams(queryInfoModel);
		
		handler.execTransactionResultQuery(queryInfoModel);
	}
	
	private RealTimePaymentInfoModel getTestRealTimePaymentInfoModel(){
		
		RealTimePaymentInfoModel realTimePaymentInfoModel = new RealTimePaymentInfoModel();
		realTimePaymentInfoModel.setUserName("operator");
		realTimePaymentInfoModel.setUserPwd("operator");
		realTimePaymentInfoModel.setMerchantId("001053110000001");
		realTimePaymentInfoModel.setReqNo("xyz00000000000000000026");
		realTimePaymentInfoModel.setBusinessCode("04900");
		realTimePaymentInfoModel.setTotalItem(1);
		realTimePaymentInfoModel.setTotalSum(new BigDecimal("0.1"));
		
		PaymentDetailInfoModel detailInfoModel = new PaymentDetailInfoModel();
		detailInfoModel.setSn("0001");
		detailInfoModel.setBankCode("102");
		detailInfoModel.setAccountNo("605810028220436120");
		detailInfoModel.setAccountName("邹力");
		detailInfoModel.setAmount(new BigDecimal("0.1"));
		detailInfoModel.setRemark("0001-代付");
		
		realTimePaymentInfoModel.setDetailInfo(detailInfoModel);
		
		setUnionPayParams(realTimePaymentInfoModel);
		return realTimePaymentInfoModel;
	}
	
	private void setUnionPayParams(IUnionPayApiParams apiParams) {
		apiParams.setApiUrl(apiUrl);
		apiParams.setCerFilePath(cerFilePath);
		apiParams.setPfxFilePath(pfxFilePath);
		apiParams.setPfxFileKey(pfxFileKey);
	}
	
}
