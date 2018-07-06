package com.suidifu.bridgewater.controller.api.remittance;

import com.suidifu.bridgewater.controller.base.v2.BaseApiController;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 中航放款接口
 */
@Controller
@RequestMapping("/pre/api")
public class ZhongHangRemittanceController extends BaseApiController {

	private final String REMITTANCE = "/remittance/zhonghang/remittance-application";

	@Value("#{config['remittance_pre_api']}")
	private String REMITTANCE_PRE_API = "";

	@RequestMapping(value = REMITTANCE, method = RequestMethod.POST)
	public String zhonghangRemittanceApplicationPreProcessing(HttpServletRequest request, HttpServletResponse response, @ModelAttribute RemittanceCommandModel
			commandModel) {

		return "forward:/" + REMITTANCE_PRE_API;

	}

}