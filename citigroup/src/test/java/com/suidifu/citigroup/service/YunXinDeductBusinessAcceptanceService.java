package com.suidifu.citigroup.service;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.coffer.entity.cmb.UnionPayBankCodeMap;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.gluon.exception.voucher.ValidatorException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_function_point;
import com.zufangbao.gluon.util.ApiMessageUtil;
import com.zufangbao.sun.api.model.deduct.PaymentChannelAndSignUpInfo;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.yunxin.entity.deduct.DeductGatewayMapSpec;
import com.zufangbao.sun.yunxin.handler.ValidatorHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qinweichao on 2017/8/1 云信扣款业务受理服务
 * 
 */

@Component("yunxinDeductBusinessAcceptanceService")
public class YunXinDeductBusinessAcceptanceService implements CustomizeServices {

	@Override
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest,
			Map<String, String> postRequest, Log logger) {
        try {

            String financialProductCode = (String)preRequest.getOrDefault("financialProductCode", StringUtils.EMPTY);
            String uniqueId = preRequest.getOrDefault("uniqueId", StringUtils.EMPTY);
            String contractNo = preRequest.getOrDefault("contractNo", StringUtils.EMPTY);
            String requestNo = preRequest.getOrDefault("requestNo", StringUtils.EMPTY);
            String bankCode = preRequest.getOrDefault("bankCode", StringUtils.EMPTY);
            String provinceCode = preRequest.getOrDefault("provinceCode", StringUtils.EMPTY);
            String cityCode = preRequest.getOrDefault("cityCode", StringUtils.EMPTY);
            String gateway = preRequest.getOrDefault("gateway", StringUtils.EMPTY);
            String deductApplicationUuid = preRequest.getOrDefault("deductApplicationUuid", StringUtils.EMPTY);
            String bankName = null;
            String standardBankCode = preRequest.getOrDefault("bankCode", StringUtils.EMPTY);
            String oppositeKeyWord = "[requestNo=" + requestNo + ";";

            Contract contract = sandboxDataSetHandler.getContractByContract_uniqueId_contractNo(uniqueId, contractNo);
            if (contract == null) {
                logger.info(GloableLogSpec.AuditLogHeaderSpec()
                        + bridgewater_deduct_function_point.CHECK_AND_SAVE_DEDUCT_INFO_BEFORE_PORCESSING
                        + oppositeKeyWord + "FAILED[CONTRACT NOT EXIST]");
                postRequest.put("errorMsg", "贷款合同不存在");
                return false;
            }

            FinancialContract financialContract = sandboxDataSetHandler
                    .getFinancialContractByProductCode(financialProductCode);
            if (financialContract == null
                    || !financialContract.getFinancialContractUuid().equals(contract.getFinancialContractUuid())) {
                logger.info(GloableLogSpec.AuditLogHeaderSpec()
                        + bridgewater_deduct_function_point.CHECK_AND_SAVE_DEDUCT_INFO_BEFORE_PORCESSING
                        + oppositeKeyWord + "FAILED[FINANCIAL_PRODUCT_CODE_ERROR]");
                postRequest.put("errorMsg", "贷款合同与信托合同不匹配");
                return false;
            }

            String repaymentDetails = (String) preRequest.getOrDefault("repaymentDetails", StringUtils.EMPTY);
            List<RepaymentDetail> repaymentDetailList = JsonUtils.parseArray(repaymentDetails, RepaymentDetail.class);
            if (CollectionUtils.isEmpty(repaymentDetailList)) {
                logger.info(GloableLogSpec.AuditLogHeaderSpec()
                        + bridgewater_deduct_function_point.CHECK_AND_SAVE_DEDUCT_INFO_BEFORE_PORCESSING
                        + oppositeKeyWord + "FAILED[REPAYMENT_DETAIL_ERROR]");
                postRequest.put("errorMsg", "扣款明细格式有误");
                return false;
            }
            Integer repaymentType = Integer.valueOf(preRequest.get("repaymentType"));
            if (repaymentType == null) {
                postRequest.put("errorMsg", "还款类型格式有误");
                return false;
            }

            ValidatorHandler validatorHandler = sandboxDataSetHandler.getValidatorHandler();
            List<RepaymentDetail> details = new ArrayList<RepaymentDetail>();
            for (RepaymentDetail repaymentDetail:repaymentDetailList
                 ) {
                validatorHandler.checkRepaymentDetail(repaymentDetail, financialContract.getContractNo(), contract);
                details.add(repaymentDetail);
            }


            validatorHandler.checkRepaymentInContract(contract, repaymentDetailList);

            validatorHandler.checkExistDeductApplicationInRepaymentPlans(repaymentType, repaymentDetailList);

            String apiCalledTime = (String) preRequest.getOrDefault("apiCalledTime", StringUtils.EMPTY);
            validatorHandler.checkRepaymetApiCalledTime(repaymentDetailList, repaymentType, apiCalledTime,
                    financialContract.getLoanOverdueStartDay());

            String amount = (String) preRequest.getOrDefault("amount", StringUtils.EMPTY);
            validatorHandler.checkRepaymentDetailAmount(amount, repaymentDetailList);

            validatorHandler.checkAccontReceiveAmount(financialProductCode, repaymentType, repaymentDetailList,
                    contract, financialContract);

            validatorHandler.checkDeductAmount(amount, repaymentType, repaymentDetailList, contract,
                    financialContract);

            Map<String, String> bankInfoMap = validatorHandler.checkPaymentAccountInfo(bankCode, standardBankCode, bankName, provinceCode, cityCode, contract);
            bankCode = bankInfoMap.get("bankCode");
            bankName = bankInfoMap.get("bankName");
            standardBankCode= bankInfoMap.get("standardBankCode");
            provinceCode = bankInfoMap.get("provinceCode");
            cityCode = bankInfoMap.get("cityCode");

            List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfos = new ArrayList<PaymentChannelAndSignUpInfo>();
            List<PaymentChannelSummaryInfo> paymentChannelSummaryInfos = validatorHandler
                    .getCurrentUsingPaymentChannel(financialContract.getFinancialContractUuid(), bankCode, new BigDecimal(amount));
            for (PaymentChannelSummaryInfo pci : paymentChannelSummaryInfos) {
               BigDecimal trasncationLimitPerTransaction = null==pci.getTrasncationLimitPerTransaction()?new BigDecimal(amount).divide(new BigDecimal(10000)):pci.getTrasncationLimitPerTransaction();
                pci.setTrasncationLimitPerTransaction(trasncationLimitPerTransaction);
                PaymentChannelAndSignUpInfo channelAndSignUpInfo = new PaymentChannelAndSignUpInfo();
                    channelAndSignUpInfo.setPaymentChannelSummaryInfo(pci);
                    channelAndSignUpInfo.setStdBankCode(standardBankCode);
                    channelAndSignUpInfo.setBankCode(UnionPayBankCodeMap.BANK_CODE_MAP.get(bankCode));
                    channelAndSignUpInfo.setBankName(bankName);
                    channelAndSignUpInfo.setProvinceCode(provinceCode);
                    channelAndSignUpInfo.setCityCode(cityCode);

                    if (StringUtils.isNotEmpty(gateway)) {
                        PaymentInstitutionName paymentInstitutionName = DeductGatewayMapSpec.DEDUCT_GATEWAY_MAP.get(gateway);
                        if (paymentInstitutionName.ordinal() == pci.getPaymentGateway()) {
                            paymentChannelAndSignUpInfos.add(0, channelAndSignUpInfo);
                        }
                    }else {
                        paymentChannelAndSignUpInfos.add(channelAndSignUpInfo);
                    }
            }

            if (CollectionUtils.isEmpty(paymentChannelAndSignUpInfos)){
                postRequest.put("errorMsg", "未找到通道信息");
                return false;
            }

            validatorHandler.TryLockAssetset(deductApplicationUuid, repaymentDetailList);

			postRequest.put("paymentChannelAndSignUpInfoString", JsonUtils.toJsonString(paymentChannelAndSignUpInfos));
			postRequest.put("financialContractUuid", financialContract.getFinancialContractUuid());
			postRequest.put("repaymentDetails", JsonUtils.toJsonString(details));
            postRequest.put("deductMode","MULTI_CHANNEL_MODE");

			return true;
		} catch (Exception e) {
            e.printStackTrace();

            logger.info("exception with"+e.getMessage());

            if (e instanceof ValidatorException){
                postRequest.put("errorMsg", ApiMessageUtil.getMessage(((ValidatorException) e).getCode()));
            }else {
                postRequest.put("errorMsg", e.getMessage());
            }

		}
		return false;
	}

}
