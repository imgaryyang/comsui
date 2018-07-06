package com.zufangbao.earth.handler.impl;

import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.matryoshka.productCategory.ProductCategoryStatus;
import com.suidifu.matryoshka.service.ProductCategoryService;
import com.suidifu.xcode.exception.XcodeException;
import com.suidifu.xcode.handler.XcodeServerHandler;
import com.suidifu.xcode.pojo.SourceRepository;
import com.suidifu.xcode.service.ObjectEhCacheService;
import com.suidifu.xcode.service.SourceRepositoryPersistenceService;
import com.zufangbao.earth.handler.CustomizeScriptModelHandler;
import com.zufangbao.earth.model.CustomizeScriptModel;
import com.zufangbao.earth.web.controller.customizeScript.CustomizeScriptControllerSpec;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.gluon.spec.global.GlobalMsgSpec;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.Md5Util;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("customizeScriptModelHandler")
public class CustomizeScriptModelHandlerImpl implements CustomizeScriptModelHandler {
	@Autowired
	private JsonViewResolver jsonViewResolver;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	@Qualifier("sourceRepositoryDBService")
	private SourceRepositoryPersistenceService sourceRepositoryDBService;

	@Autowired
	@Qualifier("sourceRepositoryRedisService")
	private SourceRepositoryPersistenceService sourceRepositoryRedisService;
	private static final Log logger = LogFactory.getLog(CustomizeScriptModelHandler.class);

	@Autowired
	private ObjectEhCacheService objectEhCacheService;

	@Autowired
	private XcodeServerHandler xcodeServerHandler;
//	private CacheManager CacheManager;

	@Override
	public String updateScript(CustomizeScriptModel model, String script, String author) throws XcodeException {

		String preProcessInterfaceUrl = model.getPreProcessInterfaceUrl();
		String compileImport = null == model.getCompileImport() ? "" : model.getCompileImport();
		String version = Md5Util.encode(script + compileImport + preProcessInterfaceUrl);
		ProductCategory productCategory = productCategoryService.getByPreUrl(preProcessInterfaceUrl);
		if (null == productCategory) {
			logger.error("CustomizeScriptModelHandler #updateScript 无效脚本[" + preProcessInterfaceUrl + "]");
			return jsonViewResolver.errorJsonResult("请先将[" + preProcessInterfaceUrl + "]脚本生效");
		}
		if (productCategoryService.checkScriptVersion(productCategory.getUuid(), version)) {
			logger.warn("CustomizeScriptModelHandler #updateScript 脚本未变更");
			return jsonViewResolver.jsonResult(GlobalCodeSpec.CODE_FAILURE, "脚本未变更", null, null);
		}
		productCategory.setScriptMd5Version(version);
		productCategory.setStatus(ProductCategoryStatus.VALID);
		productCategoryService.saveOrUpdate(productCategory);

		SourceRepository sourceRepository = sourceRepositoryDBService.getByBusinessType(preProcessInterfaceUrl);
		if (null == sourceRepository) {
			logger.error("CustomizeScriptModelHandler #updateScript 非法businessType：[" + preProcessInterfaceUrl + "]");
			return jsonViewResolver.errorJsonResult("请检查[" + preProcessInterfaceUrl + "]是否为合法处理类型");
		}

		String oldVersion = sourceRepository.getSignature();
		Date now = new Date();
		sourceRepository.setSourceCode(script);
		sourceRepository.setSignature(version);
		sourceRepository.setAuthor(author);
		sourceRepository.setLastModifyTime(now);
		sourceRepository.setCompileImport(compileImport);

		xcodeServerHandler.saveOrUpdate(sourceRepository);
//		sourceRepositoryDBService.saveOrUpdate(sourceRepository);
//		sourceRepositoryRedisService.saveOrUpdate(sourceRepository);

//		CacheManager.getCache("sr_obj").evict(oldVersion);
		objectEhCacheService.delete(oldVersion);

		Map<String, Object> data = new HashMap<>();
		data.put(CustomizeScriptControllerSpec.INFO.INFO_KEY, GlobalMsgSpec.MSG_SUC);
		data.put(CustomizeScriptControllerSpec.INFO.LAST_MODIFY, DateUtils.format(now,DateUtils.LONG_DATE_FORMAT));
		data.put(CustomizeScriptControllerSpec.INFO.AUTHOR, author);
		return jsonViewResolver.sucJsonResult(data);
	}

	@Override
	public String creatNewScript(CustomizeScriptModel model, String script, String author) throws XcodeException {
		String uuid = UUIDUtil.randomUUID();
		String preProcessInterfaceUrl = model.getPreProcessInterfaceUrl();
		String compileImport = null == model.getCompileImport() ? "" : model.getCompileImport();
		String version = Md5Util.encode(script + compileImport + preProcessInterfaceUrl);
		ProductCategory productCategory = new ProductCategory(model.getProductLv1Code(), model.getProductLv1Name(),
				model.getProductLv2Code(), model.getProductLv2Name(), model.getProductLv3Code(),
				model.getProductLv3Name(), preProcessInterfaceUrl, script, ProductCategoryStatus.VALID, version);
		productCategory.setUuid(uuid);
		if (StringUtils.isNotEmpty(model.getDelayTaskConfigUuid())) {
			productCategory.setDelayTaskConfigUuid(model.getDelayTaskConfigUuid());
		}
		productCategoryService.saveOrUpdate(productCategory);

		Date now=new Date();
		SourceRepository sourceRepository = new SourceRepository();
		sourceRepository.setSourceCode(script);
		sourceRepository.setBusinessType(preProcessInterfaceUrl);
		sourceRepository.setSignature(version);
		sourceRepository.setAuthor(author);
		sourceRepository.setAddTime(now);
		sourceRepository.setLastModifyTime(now);
		sourceRepository.setCompileImport(compileImport);
		sourceRepositoryDBService.saveOrUpdate(sourceRepository);
		sourceRepositoryRedisService.saveOrUpdate(sourceRepository);
		Map<String, Object> data = new HashMap<>();
		data.put(CustomizeScriptControllerSpec.INFO.INFO_KEY, GlobalMsgSpec.MSG_SUC);
		data.put(CustomizeScriptControllerSpec.INFO.LAST_MODIFY, DateUtils.format(now,DateUtils.LONG_DATE_FORMAT));
		data.put(CustomizeScriptControllerSpec.INFO.AUTHOR, author);
		return jsonViewResolver.sucJsonResult(data);
	}

	@Override
	public Map<String, Map<String, Map<String, String>>> getProductCategoryTree(
			List<ProductCategory> productCategoryList) {
		Map<String, Map<String, Map<String, String>>> result = new HashMap<>();
		if (CollectionUtils.isEmpty(productCategoryList)) {
			logger.error("CustomizeScriptModelHandler #getProductCategoryTree 无脚本配置信息");
			return result;
		}
		for (ProductCategory productCategory : productCategoryList) {
			if (productCategory.getStatus() == ProductCategoryStatus.VALID) {
				Map<String, Map<String, String>> cataLv2 = getCataLv2(result, productCategory.getProductLv1Name());
				Map<String, String> cataLv3 = getCataLv3(cataLv2, productCategory.getProductLv2Name());
				cataLv3.put(productCategory.getProductLv3Name(), productCategory.getPreProcessInterfaceUrl());
			}
		}
		return result;
	}

	private Map<String, Map<String, String>> getCataLv2(Map<String, Map<String, Map<String, String>>> result,
			String key) {
		if (CollectionUtils.isEmpty(result.get(key))) {
			result.put(key, new HashMap<>());
		}
		return result.get(key);
	}

	private Map<String, String> getCataLv3(Map<String, Map<String, String>> result, String key) {
		if (CollectionUtils.isEmpty(result.get(key))) {
			result.put(key, new HashMap<>());
		}
		return result.get(key);
	}
}
