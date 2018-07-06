package com.zufangbao.earth.yunxin.web.controller;

import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.SecretKeyHandler;
import com.zufangbao.earth.service.RequestLogService;
import com.zufangbao.earth.service.SecretKeyService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.yunxin.entity.SecretKey;
import com.zufangbao.sun.yunxin.entity.model.RequestShowModel;
import com.zufangbao.sun.yunxin.entity.model.SecretKeyShowModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/keystore")
public class SecretKeyController extends BaseController{

	@Autowired
	private SecretKeyService secretKeyService;
	@Autowired
	private SecretKeyHandler secretKeyHandler;
	@Autowired
	private RequestLogService requestLogService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String showSecretKey(@Secure Principal principal, @RequestParam(value = "principalId", required = false) Long principalId) {
		try {
			Map<String, Object> data = new HashMap<>();
			List<SecretKeyShowModel> secretKeyModelList = secretKeyService.getSecretKeyModelList(principalId == null ? principal.getId() : principalId);
			data.put("list", secretKeyModelList);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
	
	@RequestMapping(value="",method=RequestMethod.POST)
	public String createSecretKey(@Secure Principal principal,@RequestParam("title") String title,
			   @RequestParam("publicKey") String publicKey,@RequestParam(value ="principalId",required = false)Long principalId){
		try {
		    Long id = principalId == null ? principal.getId() : principalId;
			Integer count = secretKeyService.countSecretKeyBy(id);
			if(count == null){
				return jsonViewResolver.errorJsonResult("数据错误");
			}
			if(count>=SecretKeyService.PUBLIC_KEY_UPPER_LIMIT){
				return jsonViewResolver.errorJsonResult("达到公钥数量上限");
			}
			secretKeyHandler.createSecretKey(id, title, publicKey);
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("增加秘钥失败，请联系管理员！");
		}
	}
	
	@RequestMapping(value="/{publicKeyUuid}/delete",method=RequestMethod.DELETE)
	public String deleteSecretKey(@PathVariable(value = "publicKeyUuid") String publicKeyUuid){
		try {
			SecretKey secretKey = secretKeyService.getSecretKeyByUuid(publicKeyUuid);
			secretKeyService.delete(SecretKey.class, secretKey.getId());
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("删除秘钥失败，请联系管理员！");
		}
	}
	
	@RequestMapping(value = "/{publicKeyUuid}/log", method = RequestMethod.GET)
	public String showRequsetList(@PathVariable(value = "publicKeyUuid") String publicKeyUuid, Page page) {
		try {
			Map<String, Object> data = new HashMap<>();
			List<RequestShowModel> RequestModelList = requestLogService.getRequestModelList(publicKeyUuid, page);
			int size = requestLogService.countRequestLog(publicKeyUuid);
			data.put("size", size);
			data.put("list", RequestModelList);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
	
	
	
	
	
	
	
	
	
}
