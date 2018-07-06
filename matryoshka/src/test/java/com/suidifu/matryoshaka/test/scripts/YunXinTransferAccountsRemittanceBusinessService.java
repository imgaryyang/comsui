package com.suidifu.matryoshaka.test.scripts;

import java.util.Map;

import org.apache.commons.logging.Log;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;

public class YunXinTransferAccountsRemittanceBusinessService implements CustomizeServices {

	@Override
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest,
	                        Map<String, String> postRequest, Log logger) {

		try {

			SandboxDataSet sandboxDataSet = sandboxDataSetHandler.validFinancialContractForTransferAccounts(preRequest);

			FinancialContract financialContract = (FinancialContract)JsonUtils.parse((String)sandboxDataSet.getExtraData().get(ZhonghangResponseMapSpec.FINANCIAL_CONTRACT), FinancialContract
					.class);

			if (null == financialContract) {

				postRequest.put("errMsg", "信托产品代码错误");

				return false;

			}
			
			postRequest.put(ZhonghangResponseMapSpec.FINANCIAL_CONTRACT, JsonUtils.toJsonString(financialContract));

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
