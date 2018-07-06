/**
 *
 */
package com.zufangbao.earth.web.controller.customizeScript;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Secure;
import com.suidifu.matryoshka.customize.CustomizeServicesBuilder;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.matryoshka.productCategory.ProductCategoryStatus;
import com.suidifu.matryoshka.service.ProductCategoryService;
import com.suidifu.xcode.pojo.SourceRepository;
import com.suidifu.xcode.service.SourceRepositoryPersistenceService;
import com.zufangbao.earth.handler.CustomizeScriptModelHandler;
import com.zufangbao.earth.model.CustomizeScriptModel;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.customizeScript.CustomizeScriptControllerSpec.INFO;
import com.zufangbao.earth.web.controller.customizeScript.CustomizeScriptControllerSpec.REQUEST;
import com.zufangbao.earth.web.controller.customizeScript.CustomizeScriptControllerSpec.URL;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.commons.compiler.CompileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 脚本热编译在线管理
 *
 * @author fanxiaofan 2017-07-13
 *
 */
@Controller
@RequestMapping(URL.NAME)
@MenuSetting(URL.MENU)
public class CustomizeScriptController extends BaseController {
	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	@Qualifier("sourceRepositoryDBService")
	private SourceRepositoryPersistenceService sourceRepositoryDBService;

	@Autowired
	private CustomizeScriptModelHandler customizeScriptModelHandler;

	private static final Log logger = LogFactory.getLog(CustomizeScriptController.class);

	/**
	 * 提交前置接口脚本 支持修改和新增
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @param script
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = URL.SUBMIT_PRE_SCRIPT, method = RequestMethod.POST)
	public @ResponseBody String submitPreScript(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute CustomizeScriptModel model, @RequestParam(value = REQUEST.PARAM_SCRIPT) String script,
			@Secure Principal principal) {
		try {
			String preProcessInterfaceUrl = model.getPreProcessInterfaceUrl();
			if (StringUtils.isEmpty(model.getPreProcessInterfaceUrl()) || StringUtils.isEmpty(script)) {
				logger.error("CustomizeScriptController #submitPreScript 缺少必要参数");
				return jsonViewResolver.errorJsonResult("缺少必要参数");
			}
			if (productCategoryService.check_by_pre_url(preProcessInterfaceUrl, null)) {
				return customizeScriptModelHandler.updateScript(model, script, principal.getName());
			} else {
				return customizeScriptModelHandler.creatNewScript(model, script, principal.getName());
			}
		} catch (Exception e) {
			logger.error("CustomizeScriptController #submitPreScript occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
	 * 前置接口生效
	 *
	 * @param request
	 * @param response
	 * @param preProcessInterfaceUrl
	 * @return
	 */
	@RequestMapping(value = URL.ENABLE_PRE_SCRIPT, method = RequestMethod.POST)
	public @ResponseBody String enablePreScript(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = REQUEST.PARAM_PRE_INTERFACE, required = true) String preProcessInterfaceUrl) {
		try {
			ProductCategory productCategory = productCategoryService.getByPreUrl(preProcessInterfaceUrl,
					ProductCategoryStatus.INVALID);
			if (null == productCategory) {
				logger.error("CustomizeScriptController #enablePreScript 脚本[" + preProcessInterfaceUrl + "]已生效");
				return jsonViewResolver.errorJsonResult("脚本[" + preProcessInterfaceUrl + "]已生效");
			}
			productCategoryService.open(productCategory.getUuid());
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("CustomizeScriptController #enablePreScript occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
	 * 前置接口失效
	 *
	 * @param request
	 * @param response
	 * @param preProcessInterfaceUrl
	 * @return
	 */
	@RequestMapping(value = URL.UNABLE_PRE_SCRIPT, method = RequestMethod.POST)
	public @ResponseBody String unablePreScript(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = REQUEST.PARAM_PRE_INTERFACE, required = true) String preProcessInterfaceUrl) {
		try {
			ProductCategory productCategory = productCategoryService.getByPreUrl(preProcessInterfaceUrl,
					ProductCategoryStatus.VALID);
			if (null == productCategory) {
				logger.error("CustomizeScriptController #unablePreScript 脚本[" + preProcessInterfaceUrl + "]尚未生效");
				return jsonViewResolver.errorJsonResult("脚本[" + preProcessInterfaceUrl + "]尚未生效");
			}
			productCategoryService.close(productCategory.getUuid());
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("CustomizeScriptController #unablePreScript occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
	 * 获取全部脚本配置目录
	 *
	 * @return
	 */
	@RequestMapping(value = URL.GET_CATALOG, method = RequestMethod.POST)
	@MenuSetting(URL.SUB_MENU)
	public @ResponseBody String getCatalog() {
		Map<String, Object> data = new HashMap<>();
		try {
			List<ProductCategory> productCategoryList = productCategoryService.getAll();
			if (CollectionUtils.isEmpty(productCategoryList)) {
				logger.error("CustomizeScriptController #getCatalog 无脚本配置信息");
				return errorSignResult("无脚本配置信息");
			}
			Map<String, Map<String, Map<String, String>>> result = customizeScriptModelHandler.getProductCategoryTree(productCategoryList);
			data.put(INFO.CATA_INFO_SIZE, result.size());
			data.put(INFO.CATA_INFO_LIST, result);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			logger.error("CustomizeScriptController #getCatalog occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
	 * 获取脚本、依赖包及其变更信息
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = URL.GET_SOURCE, method = RequestMethod.POST)
	public @ResponseBody String getSource(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<>();
		try {
			String request_url = request.getRequestURL().toString();
			String[] split = request_url.split(URL.NAME);
			String url = split[1];
			SourceRepository sourceRepository = sourceRepositoryDBService.getByBusinessType(url);

			if (null == sourceRepository) {
				logger.error("CustomizeScriptController #getSource 非法businessType：[" + url + "]");
				return jsonViewResolver.errorJsonResult("请检查[" + url + "]是否为合法处理类型");
			}
			data.put(INFO.SOURCE_INFO_AUTHOR, sourceRepository.getAuthor());
			data.put(INFO.SOURCE_INFO_LAST_MODIFY_TIME, sourceRepository.getLastModifyTime());
			data.put(INFO.SOURCE_INFO_SCRIPT, sourceRepository.getSourceCode());
			data.put(INFO.SOURCE_INFO_COMPILE_IMPORT, "");
			if (StringUtils.isNotEmpty(sourceRepository.getCompileImport())) {
				data.put(INFO.SOURCE_INFO_COMPILE_IMPORT, sourceRepository.getCompileImport());
			}
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			logger.error("CustomizeScriptController #getSource occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
	 * 脚本页面试运行
	 *
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = URL.RUN_SOURCE, method = RequestMethod.POST)
	public @ResponseBody String runSource(HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> data = new HashMap<>();
		try {
			HashMap<String, String> allParameters = getAllParameters(request);
			String script = allParameters.getOrDefault(REQUEST.PRE_SCRIPT, "");
			String compileImport = allParameters.getOrDefault(REQUEST.COMPILE_IMPORT, "");
			CustomizeServicesBuilder builder = new CustomizeServicesBuilder();
			builder.build(script, compileImport);

			data.put(INFO.INFO_KEY, "编译成功");
			return jsonViewResolver.sucJsonResult(data);
		} catch (CompileException | IllegalAccessException | InstantiationException | IOException ex) {
			logger.error("CustomizeScriptController #runSource 编译错误：" + ex.getMessage());
			ex.printStackTrace();
//			data.put(INFO.INFO_KEY, ex.getMessage());
			return jsonViewResolver.errorJsonResult(ex.getMessage());
		} catch (Exception e) {
			logger.error("CustomizeScriptController #runSource occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	private HashMap<String, String> getAllParameters(HttpServletRequest request) {
		HashMap<String, String> parameters = new HashMap<>();
		if (request == null)
			return parameters;
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			String paramValues = request.getParameter(paramName);
			parameters.put(paramName, paramValues);
		}
		return parameters;
	}
}
