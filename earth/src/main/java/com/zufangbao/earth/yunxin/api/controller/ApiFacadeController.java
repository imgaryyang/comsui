package com.zufangbao.earth.yunxin.api.controller;

import com.zufangbao.earth.yunxin.api.BaseApiController;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api")
public class ApiFacadeController extends BaseApiController{
	
	@RequestMapping(value = "")
	public @ResponseBody String apiFacade(HttpServletResponse response){
		return signErrorResult(response, ApiResponseCode.API_NOT_FOUND);
	}
	
	@RequestMapping(value = "{apiType}")
	public @ResponseBody String apiTypeFacade(HttpServletResponse response){
		return signErrorResult(response, ApiResponseCode.API_NOT_FOUND);
	}

	@RequestMapping(value = "query")
	public @ResponseBody String queryFacade(HttpServletResponse response){
		return signErrorResult(response, ApiResponseCode.API_NOT_FOUND);
	}
	
	@RequestMapping(value = "modify")
	public @ResponseBody String modifyFacade(HttpServletResponse response){
		return signErrorResult(response, ApiResponseCode.API_NOT_FOUND);
	}

	@RequestMapping(value = "command")
	public @ResponseBody String commandFacade(HttpServletResponse response){
		return signErrorResult(response, ApiResponseCode.API_NOT_FOUND);
	}
	
}