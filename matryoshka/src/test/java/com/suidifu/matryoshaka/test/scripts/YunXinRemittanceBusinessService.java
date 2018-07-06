package com.suidifu.matryoshaka.test.scripts;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import org.apache.commons.logging.Log;

import java.util.Map;

public class YunXinRemittanceBusinessService implements CustomizeServices {

	@Override
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest,
	                        Map<String, String> postRequest, Log logger) {

		try {

			SandboxDataSet sandboxDataSet = sandboxDataSetHandler.validFinancialContractAndContractForYunxin(preRequest);

			FinancialContract financialContract = (FinancialContract)JsonUtils.parse((String)sandboxDataSet.getExtraData().get(ZhonghangResponseMapSpec.FINANCIAL_CONTRACT), FinancialContract
					.class);

			if (null == financialContract) {

				postRequest.put("errMsg", "信托产品代码错误");

				return false;

			}
			
			postRequest.put(ZhonghangResponseMapSpec.FINANCIAL_CONTRACT, JsonUtils.toJsonString(financialContract));

			if (financialContract.is_offline_remittance()) {

				postRequest.put("errMsg", "放款模式为线下放款时，不受理放款请求");

				return false;
			}

			Contract contract = (Contract)JsonUtils.parse((String)sandboxDataSet.getExtraData().get(ZhonghangResponseMapSpec.CONTRACT), Contract.class);

			if (contract != null && contract.is_contract_effective()) {

				postRequest.put("errMsg", "贷款合同已生效，不受理放款请求");

				return false;
			}

			return true;

		} catch (Exception e) {

			e.printStackTrace();

			if (e instanceof ApiException) {

				postRequest.put("errMsg", e.getMessage());

			} else {

				postRequest.put("errMsg", "系统错误:" + e.getMessage());

			}
			return false;
		}

	}

}
