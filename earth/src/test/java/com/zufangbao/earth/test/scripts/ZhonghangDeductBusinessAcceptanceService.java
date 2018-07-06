/**
 *
 */
package com.zufangbao.earth.test.scripts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.snapshot.ContractSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.sun.api.model.deduct.PaymentChannelAndSignUpInfo;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.v2.SignUp;
import com.zufangbao.sun.yunxin.entity.v2.SignUpStatus;
import com.zufangbao.sun.yunxin.handler.ValidatorHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author wukai 中航的扣款业务受理服务
 */
@Component("zhonghangDeductBusinessAcceptanceService")
public class ZhonghangDeductBusinessAcceptanceService implements CustomizeServices {

	@Override
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest,
			Map<String, String> postRequest, Log logger) {

		try {

			String financialProductCode = (String)preRequest.getOrDefault("financialProductCode", StringUtils.EMPTY);

			String accountName = (String)preRequest.getOrDefault("accountName", StringUtils.EMPTY);

			String accountNo = (String)preRequest.getOrDefault("accountNo", StringUtils.EMPTY);
			
			 String amount = (String) preRequest.getOrDefault("amount", StringUtils.EMPTY);

			FinancialContract financialContract = sandboxDataSetHandler
					.getFinancialContractByProductCode(financialProductCode);

			if (financialContract == null) {

				postRequest.put("errorMsg", "信托合同不存在");

				return false;
			}

			String financialContractUuid = (financialContract.getFinancialContractUuid());
			postRequest.put("financialContractUuid", financialContractUuid);
			String contractNo = (String)preRequest.get("contractNo");

			String uniqueId = (String)preRequest.get("uniqueId");

			SandboxDataSet sandboxDataSetForContract = sandboxDataSetHandler
					.get_sandbox_by_contract_uniqueId_contractNo(uniqueId, contractNo);

			if (null == sandboxDataSetForContract) {

				postRequest.put("errorMsg", "获取贷款合同有误");

				return false;
			}

			if (null == sandboxDataSetForContract.getContractSnapshot()) {

				postRequest.put("errorMsg", "贷款合同不存在");

				return false;
			}

			ContractSnapshot contractSnapshot = sandboxDataSetForContract.getContractSnapshot();

			if (!StringUtils.equals(uniqueId, contractSnapshot.getUniqueId())) {

				postRequest.put("errorMsg", "合同uniqueId和db中不一致");

				return false;
			}

			if (!StringUtils.equals(contractNo, contractSnapshot.getContractNo())) {

				postRequest.put("errorMsg", "合同contractNo和db中不一致");
				//
				return false;
			}

			String repaymentDeatilInfos = (String)preRequest.get("repaymentDetails");

			List<RepaymentDetail> repaymentDetails = JsonUtils.parseArray(repaymentDeatilInfos,
					RepaymentDetail.class);

			if (CollectionUtils.isEmpty(repaymentDetails) || repaymentDetails.size() != 1) {

				postRequest.put("errorMsg", "还款计划明细为空或者数目不为1");

				return false;
			}

			ValidatorHandler validatorHandler = sandboxDataSetHandler.getValidatorHandler();
			
			List<RepaymentDetail> details = new ArrayList<RepaymentDetail>();
			
			int currentPeriod = repaymentDetails.get(0).getCurrentPeriod();

			if (9999 != currentPeriod) {
				
				details.addAll(repaymentDetails);

				SandboxDataSet sandboxDataSetForAssetset = sandboxDataSetHandler.get_sandbox_for_asset_set(
						contractSnapshot.getUuid(), currentPeriod, AssetSetActiveStatus.OPEN.ordinal());

				if (null == sandboxDataSetForAssetset) {

					postRequest.put("errorMsg", "解析还款计划报错");

					return false;
				}

				String assetSetJson = sandboxDataSetForAssetset.getExtraData().get("assetSet");

				AssetSet assetSet = JsonUtils.parse(assetSetJson, AssetSet.class);

				if (null == assetSet) {

					postRequest.put("errorMsg", "还款计划不存在");

					return false;
				}
			}else{
				
				Contract contract = sandboxDataSetHandler.getContractByContract_uniqueId_contractNo(uniqueId, contractNo);
				
	
				for (RepaymentDetail repaymentDetail:repaymentDetails
						) {
					validatorHandler.checkRepaymentDetail(repaymentDetail, financialContract.getContractNo(), contract);
					details.add(repaymentDetail);
				}
				
			}
			
			SandboxDataSet sandboxDataSet = sandboxDataSetHandler.getSandbox_for_signup(accountName, accountNo,
					SignUpStatus.SUCCESS, financialProductCode);

			Map<String, String> extraData = sandboxDataSet.getExtraData();


			List<PaymentChannelSummaryInfo> paymentChannelSummaryInfos = validatorHandler
	                    .getCurrentUsingPaymentChannel(financialContract.getFinancialContractUuid(), null,null);
			String signUpJsonString =  extraData.get("signUps");

			List<SignUp> signUpList =  JsonUtils.parseArray(signUpJsonString, SignUp.class);



				List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfos = (List<PaymentChannelAndSignUpInfo>) sandboxDataSetHandler.buildPaymentChannelAndSignUpInfos(paymentChannelSummaryInfos, signUpList);

			if (CollectionUtils.isEmpty(paymentChannelAndSignUpInfos)) {

				postRequest.put("errorMsg", "获取不到对应的通道信息");

				return false;
			}
			//校验还款明细金额和总额是否相等
			validatorHandler.checkRepaymentDetailAmount(amount, repaymentDetails);
	
//				Contract contract = sandboxDataSetHandler.getContractByContract_uniqueId_contractNo(uniqueId, contractNo);
//	
//				List<RepaymentDetail> details = new ArrayList<RepaymentDetail>();
//	
//				for (RepaymentDetail repaymentDetail:repaymentDetails
//						) {
//					validatorHandler.checkRepaymentDetail(repaymentDetail, financialContract.getContractNo(), contract);
//					details.add(repaymentDetail);
//				}

			postRequest.put("paymentChannelAndSignUpInfoString", JsonUtils.toJsonString(paymentChannelAndSignUpInfos));

			postRequest.put("repaymentDetails", JsonUtils.toJsonString(details));

		} catch (Exception e) {

			logger.error("#ZhonghangDeductBusinessAcceptanceService# preRequest[" + JsonUtils.toJsonString(preRequest)
					+ "],occur exception with stack trace[" + ExceptionUtils.getFullStackTrace(e) + "] ");

			postRequest.put("errorMsg", e.getMessage());
			
			return false;
		}
		return true;
	}

}
