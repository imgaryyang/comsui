package com.suidifu.morganstanley.handler.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.SignatureUtils;
import com.suidifu.morganstanley.handler.TransferBillNoSession;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.transfer.TransferApplicationReqModel;
import com.zufangbao.sun.entity.bank.Bank;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.geography.entity.City;
import com.zufangbao.sun.geography.entity.Province;
import com.zufangbao.sun.geography.service.CityService;
import com.zufangbao.sun.geography.service.ProvinceService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.transfer.TransferTransactionType;
import com.zufangbao.sun.yunxin.service.BankService;
import com.zufangbao.sun.yunxin.service.TransferBillService;

@Component("transferBillNoSession")
public class TransferBillNoSessionImpl implements TransferBillNoSession {
	
	private static final Log logger = LogFactory.getLog(TransferBillNoSessionImpl.class);
	
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
    private BankService bankService;
	@Autowired
    private ProvinceService provinceService;
    @Autowired
    private CityService cityService; 
	@Autowired
	private TransferBillService transferBillService;

	@Override
	public String receiveTransferBillCallback(HttpServletRequest request) {
		
		return null;
	}
	
	@Override
	public void transferInfoCheck(TransferApplicationReqModel reqModel) throws ApiException{
		
		logger.info("transferInfo check-- [requestNo="+reqModel.getRequestNo()+",orderUniqueId="+reqModel.getOrderNo()+"]");
		
		//校验请求编号唯一
		Integer uniqueRequsetNo = transferBillService.queryUniqueRequsetNo(reqModel.getRequestNo());
		if(uniqueRequsetNo != null && uniqueRequsetNo > 0){
			throw new ApiException(ApiResponseCode.REPEAT_REQUEST_NO);
		}
		//校验信托合同
		FinancialContract  financialContract = financialContractService.getUniqueFinancialContractBy(reqModel.getProductCode());
		if(financialContract == null){
			throw new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST);
		}
		//商户订单号 唯一性 
		String md5Signature = SignatureUtils.makeMD5Signature(reqModel.getProductCode(),reqModel.getOrderNo());
		String orderNoByMd5 = transferBillService.queryUniqueOrderNoByMd5(md5Signature);
		if(StringUtils.isNotEmpty(orderNoByMd5)){
			throw new ApiException(ApiResponseCode.MERCHANT_ORDERNO_REPEAD);
		}
		//校验 银行code
		if(StringUtils.isNotEmpty(reqModel.getCpBankCode())){
			Bank bank =  bankService.getCachedBanks().get(reqModel.getCpBankCode());
			if(bank == null){
				throw new ApiException(ApiResponseCode.NO_MATCH_BANK);
			}
		}
		//校验省市 code
		if(StringUtils.isNotEmpty(reqModel.getCpBankProvince())){
			Province province = provinceService.getProvinceByCode(reqModel.getCpBankProvince());
			if(province == null){
				throw new ApiException(ApiResponseCode.NO_MATCH_PROVINCE);
			}
		}
		
		if(StringUtils.isNotEmpty(reqModel.getCpBankCity())){
			City city = cityService.getCityByCityCode(reqModel.getCpBankCity());
			if(city == null){
				throw new ApiException(ApiResponseCode.NO_MATCH_CITY);
			}
		}
		//检验交易类型
		TransferTransactionType transactionType = reqModel.getTransactionTypeEnum();
		if(transactionType == null){
			throw new ApiException(ApiResponseCode.ORDER_PAY_WAY_ERROR);
		}
		if(TransferTransactionType.ENCHASHMENT==transactionType){
			//验证虚户与产品代码的匹配关系
			throw new ApiException(ApiResponseCode.ORDER_PAY_WAY_ERROR);
		}
	}
	
	

}
