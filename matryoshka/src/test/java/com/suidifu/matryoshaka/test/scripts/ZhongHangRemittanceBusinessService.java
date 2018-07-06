package com.suidifu.matryoshaka.test.scripts;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SEND_RECEIVE_CODE_FOR_REMITTANCE;
import org.apache.commons.logging.Log;

import java.util.Map;

public class ZhongHangRemittanceBusinessService implements CustomizeServices {

	@Override
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest,
	                        Map<String, String> postRequest, Log logger) {

		try {

			String productCode = preRequest.get(SEND_RECEIVE_CODE_FOR_REMITTANCE.PRODUCT_CODE);

			FinancialContract financialContract = sandboxDataSetHandler.validFinancialContractForZhongHang(productCode);

			if (financialContract == null) {

				postRequest.put("errMsg", "信托产品代码错误");

				return false;
			}

			postRequest.put(ZhonghangResponseMapSpec.FINANCIALCONTRACT, financialContract.getFinancialContractUuid());

			postRequest.put(ZhonghangResponseMapSpec.FINANCIAL_CONTRACT, JsonUtils.toJsonString(financialContract));

			return true;

		} catch (Exception e) {

			e.printStackTrace();

			postRequest.put("errMsg", "系统错误:" + e.getMessage());

			return false;
		}
	}

}
