package com.suidifu.bridgewater.handler.impl.single.v2;

import java.math.BigDecimal;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.StringUtils;
import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.suidifu.bridgewater.handler.single.v2.DeductApplicationStandardHandler;
import com.suidifu.bridgewater.model.v2.StandardDeductModel;
import com.zufangbao.gluon.api.jpmorgan.JpmorganApiHelper;
import com.zufangbao.gluon.api.jpmorgan.enums.AccountSide;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.api.deduct.SourceType;
import com.zufangbao.sun.yunxin.entity.remittance.CertificateType;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;

/**
 * Created by zhenghangbo on 03/05/2017.
 */
@Component("deductApplicationStandardHandler")
public class DeductApplicationStandardHandlerImpl implements DeductApplicationStandardHandler {


	 private static final  boolean SEND_TO_OPPOSITE_SUCCESS =true;
	    private static final  boolean SEND_TO_OPPOSITE_FAIL =false;

	    public static final String PRE_API_TRIGGER = "neibu";

	    private static final Log logger = LogFactory.getLog(DeductApplicationStandardHandlerImpl.class);
	    @Autowired
	    private JpmorganApiHelper jpmorganApiHelper;
	    @Autowired
	    private DeductApplicationService deductApplicationService;
	    @Autowired
	    private FinancialContractService financialContractService;
	    @Autowired
	    private DeductApplicationBusinessHandler deductApplicationBusinessHandler;
	    @Autowired
	    private PaymentChannelInformationHandler paymentChannelInformationHandler;
	    @Autowired
	    private DeductPlanService deductPlanService;


	    @Override
	    public  void dataPreCheckAndFillInformation(StandardDeductModel deductModel){

//	        if(!deductModel.isValid())
//	        {
//	            throw new ApiException(ApiResponseCode.INVALID_PARAMS);
//	        }
	    	
			// 请求号校验
			if (existRequest(deductModel)) {
				throw new ApiException(ApiResponseCode.REPEAT_REQUEST_NO);
			}
			// 商户提交的扣款id唯一性校验
			if (existDeduct(deductModel)) {
				throw new ApiException(ApiResponseCode.REPEAT_DEDUCT_ID);
			}
	    	//delete
	        String  financialContractCode =deductModel.getFinancialProductCode();
	        if(!StringUtils.isEmpty(financialContractCode)){
	            FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(financialContractCode);
	            if(financialContract == null){
	                throw new ApiException(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR);
	            }
	            deductModel.setFinancialContractUuid(financialContract.getFinancialContractUuid());
	        }

	    }
	    
		private boolean existRequest(StandardDeductModel standardDeductModel) {
			return deductApplicationService.getDeductApplicationByDeductRequestNo(standardDeductModel.getRequestNo()) != null;
		}
		
		private boolean existDeduct(StandardDeductModel standardDeductModel) {
			return deductApplicationService.getDeductApplicationByDeductId(standardDeductModel.getDeductId()) != null;
		}

	    @Override
	    public DeductPlan createDeductPlanAndDeductApplicationByRule(HttpServletRequest request, StandardDeductModel deductModel, String creatorName) {

	         String merId = request.getHeader(ApiConstant.PARAMS_MER_ID);

	        DeductApplication deductApplication = convertDeductApplication(request, deductModel, creatorName);
	        String deductApplicationUuid = deductApplication.getDeductApplicationUuid();

	        //fill repaymentInfo(暂时为空)
	        DeductPlan deductPlan = convertToDeductPlan(creatorName,deductApplicationUuid,deductModel);

	        deductPlanService.save(deductPlan);
	        return deductPlan;
	    }

	    private DeductApplication convertDeductApplication(HttpServletRequest request, StandardDeductModel deductModel, String creatorName) {
	        DeductApplication deductApplication = new DeductApplication(deductModel.getDeductApplicationUuid(),deductModel.getApiCalledTime(),
	                deductModel.getRequestNo(),deductModel.getDeductId(),deductModel.getDeductAmount(),deductModel.getMobile(),
	                deductModel.getStandardPaymentInstitutionName()  ,creatorName, IpUtil.getIpAddress(request),deductModel.getFinancialProductCode(),deductModel.getFinancialContractUuid(),
	                deductModel.getBatchDeductApplicationUuid(), deductModel.getBatchDeductId(), deductModel.getNotifyUrl());

	        deductApplicationService.save(deductApplication);
	        return deductApplication;
	    }
	    
	    

	    private DeductPlan convertToDeductPlan(String creatorName,String deductApplicationUuid, StandardDeductModel deductModel ) {

	        String standardBankCode = deductModel.getStandardBankCode();
	        String financialContractUuid = deductModel.getFinancialContractUuid();
	        PaymentChannelSummaryInfo paymentChannelSummaryInfo = paymentChannelInformationHandler.getHandleredPaymentChannelInfo(deductModel.getStandardPaymentInstitutionName(),financialContractUuid);
	        if(paymentChannelSummaryInfo == null){
	            throw new ApiException(ApiResponseCode.CHANNEL_NOT_FOUND);
	        }
	        String paymentChannelUuid = paymentChannelSummaryInfo.getChannelServiceUuid();
	        int transactionType = AccountSide.DEBIT.ordinal();
	        String cpBankCode = standardBankCode;
	        String cpBankCardNo = deductModel.getDeductAccountNo();
	        String cpBankAccountHolder = deductModel.getAccountHolderName();
	        String cpIdNumber = deductModel.getIDCardNo();
	        String cpBankProvince = deductModel.getCpBankProvince();
	        String cpBankCity = deductModel.getCpBankCity();
	        String cpBankName = deductModel.getAccountBankName();
	        BigDecimal plannedTotalAmount = new BigDecimal(deductModel.getDeductAmount());

	        PaymentInstitutionName gateway = PaymentInstitutionName.fromValue(paymentChannelSummaryInfo.getPaymentGateway());
	        
	        return new DeductPlan(UUID.randomUUID().toString(), deductApplicationUuid, null, financialContractUuid,
	                null, null, gateway, paymentChannelUuid, null, null, transactionType, null, cpBankCode,
	                cpBankCardNo, cpBankAccountHolder, CertificateType.ID_CARD, cpIdNumber, cpBankProvince, cpBankCity,
	                cpBankName, null, plannedTotalAmount, null, creatorName,deductModel.getMobile(), null, SourceType.INTERFACEONLINEDEDUCT,
	                deductModel.getBatchDeductApplicationUuid(), deductModel.getBatchDeductId());

	    }

		@Override
		public DeductApplication createDeductApplicationByRule(HttpServletRequest request, StandardDeductModel deductModel,
				String creatorName) {
				
		       DeductApplication deductApplication = convertDeductApplication(request, deductModel, creatorName);
		       
		       return deductApplication;
		}
		
		@Override
		public DeductApplication createDeductApplicationByNewRule(String IpAddress , StandardDeductModel deductModel,
				String creatorName) {
				
//		       DeductApplication deductApplication = convertDeductApplicationNew(IpAddress, deductModel, creatorName);
//		       
//		       return deductApplication;
			return null;
		}
	}
