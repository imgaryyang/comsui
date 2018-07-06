package com.suidifu.matryoshka.handler.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.suidifu.matryoshka.cache.ProductCategoryCache;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.ProductCategorySourceCodeCompilerHandler;
import com.suidifu.matryoshka.productCategory.Product3lvl;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.matryoshka.service.ProductCategoryService;
import com.suidifu.xcode.exception.XcodeException;
import com.suidifu.xcode.handler.SourceCodeCompilerHandler;
import com.suidifu.xcode.handler.XcodeServerHandler;
import com.suidifu.xcode.pojo.SourceRepository;
import com.suidifu.xcode.service.SourceRepositoryPersistenceService;
import com.suidifu.xcode.util.CompilerContainer;
import com.zufangbao.gluon.exception.apis.ApiNotExistException;
import com.zufangbao.sun.entity.perInterface.ProductCategoryCacheSpec;
import com.zufangbao.sun.utils.StringUtils;

/**
 * 外部产品类型配置缓存 Created by louguanyang on 2017/4/20.
 */
public abstract class AbstractProductCategoryCacheHandlerImpl implements ProductCategoryCacheHandler {

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private XcodeServerHandler xcodeServerHandler;

	@Autowired
	private SourceRepositoryPersistenceService sourceRepositoryDBService;

	@Autowired
	private SourceRepositoryPersistenceService sourceRepositoryRedisService;

	private final static long TIMEOUT = 1000 * 60 * 60 * 24 * 5;

	private static final Log LOGGER = LogFactory.getLog(AbstractProductCategoryCacheHandlerImpl.class);

	@Override
	public ProductCategory get(String preProcessUrl, boolean isRecursively) {
		if (null == preProcessUrl) {
			throw new ApiNotExistException();
		}
		Cache cache = getProductCategoryCache();
		ProductCategoryCache productCategoryCache = cache.get(preProcessUrl, ProductCategoryCache.class);
		if (null != productCategoryCache && !productCategoryCache.needUpdate(TIMEOUT)) {
			return productCategoryCache.getProductCategory();
		}
		ProductCategory productCategory = productCategoryService.getByPreUrl(preProcessUrl);
		if (null != productCategory) {
			cache.put(preProcessUrl, new ProductCategoryCache(productCategory));
			return productCategory;
		}
		if (isRecursively) {
			Product3lvl product3lvl = transPreProcessUrl2Product3lvl(preProcessUrl);
			productCategory = productCategoryService.getByAllLvCode(product3lvl);
			if (null != productCategory) {
				cache.put(preProcessUrl, new ProductCategoryCache(productCategory));
				return productCategory;
			}

		}
		throw new ApiNotExistException();
	}

	private Cache getProductCategoryCache() {
		return cacheManager.getCache(ProductCategoryCacheSpec.CACHE_KEY);
	}

	private String removePerfix(String path) {
		while (path.startsWith("/")) {
			path = path.substring(1);
		}
		return path;
	}

	private Product3lvl transPreProcessUrl2Product3lvl(String preProcessUrl) {
		preProcessUrl = removePerfix(preProcessUrl);
		String lvCode1 = "";
		String lvCode2 = "";
		StringBuilder lvCode3 = new StringBuilder("");
		String[] productLvs = preProcessUrl.split("/");
		for (int index = 0; index < productLvs.length; index++) {
			String lvCode = productLvs[index];
			if (0 == index) {
				lvCode2 = lvCode;
				continue;
			}
			if (1 == index) {
				lvCode1 = lvCode;
				continue;
			}
			if (lvCode3.length() == 0) {
				lvCode3.append(lvCode);
			} else {
				lvCode3.append("/").append(lvCode);
			}
		}
		return new Product3lvl(lvCode1, lvCode2, lvCode3.toString());
	}

	@Override
	public Object getScript(ProductCategory productCategory) {
		try {
			if (null == productCategory) {
				LOGGER.warn("productCategory is null");
				return null;
			}
			String businessType = productCategory.getPreProcessInterfaceUrl();
			String scriptMD5Version = productCategory.getScriptMd5Version();
			register(businessType, new ProductCategorySourceCodeCompilerHandler());
			return xcodeServerHandler.getNewest(businessType, scriptMD5Version);
		} catch (XcodeException e) {
			e.printStackTrace();
			LOGGER.warn("xcodeServerHandler getNewest has error, error msg:" + e.getMessage());
			return null;
		}
	}

	@Override
	public void register(String businessType, SourceCodeCompilerHandler sourceCodeCompilerHandler) {
		if (CompilerContainer.compilerContainer.containsKey(businessType)) {
			return;
		}
		xcodeServerHandler.register(businessType, sourceCodeCompilerHandler);
	}

	@Override
	public void clearAll() {
		Cache cache = getProductCategoryCache();
		if (cache != null) {
			cache.clear();
		}
	}

	@Override
	public void clearByUrl(String url) {
		Cache cache = cacheManager.getCache(ProductCategoryCacheSpec.CACHE_KEY);
		if (null != cache.get(url)) {
			cache.evict(url);
			try {
				SourceRepository sr = sourceRepositoryDBService.getByBusinessType(url);
				if (null != sr) {
					sourceRepositoryRedisService.saveOrUpdate(sr);
				}
			} catch (XcodeException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	@Deprecated
	public void clearByScriptMD5Version(String scriptMD5Version) {
		Cache cache = getProductCategoryCache();
		if (null != cache.get(scriptMD5Version)) {
			cache.evict(scriptMD5Version);
		}
	}

	@Override
	public ProductCategory get(String lvL2Code, String reasonCode) {
		if (StringUtils.isEmpty(lvL2Code) || StringUtils.isEmpty(reasonCode)) {
			throw new ApiNotExistException();
		}
		Cache cache = getProductCategoryCache();
		if (null == cache) {
			LOGGER.warn("ProductCategoryCache not config, Read ProductCategory【get_by_lvL2Code_reasonCode】from DB!");
			return productCategoryService.get_by_lvL2Code_reasonCode(lvL2Code, reasonCode);
		}
		String key = lvL2Code + reasonCode;
		ProductCategoryCache productCategoryCache = cache.get(key, ProductCategoryCache.class);
		if (null != productCategoryCache && !productCategoryCache.needUpdate(TIMEOUT)) {
			return productCategoryCache.getProductCategory();
		}
		ProductCategory productCategory = productCategoryService.get_by_lvL2Code_reasonCode(lvL2Code, reasonCode);
		if (null != productCategory) {
			LOGGER.info("Read ProductCategory【get_by_lvL2Code_reasonCode】from DB, Save to ProductCategoryCache!");
			return saveProductCategoryInCache(cache, productCategory);
		}
		LOGGER.warn("Not Find ProductCategory from ProductCategoryCache OR DB");
		throw new ApiNotExistException();
	}

	@Override
	public ProductCategory get(Product3lvl product3lvl) {
		if (product3lvl == null || product3lvl.isEmpty()) {
			throw new ApiNotExistException();
		}
		Cache cache = getProductCategoryCache();
		if (null == cache) {
			LOGGER.warn("ProductCategoryCache not config, Read ProductCategory【getByAllLvCode】from DB!");
			return productCategoryService.getByAllLvCode(product3lvl);
		}
		String key = product3lvl.shortName();
		ProductCategoryCache productCategoryCache = cache.get(key, ProductCategoryCache.class);
		if (null != productCategoryCache && !productCategoryCache.needUpdate(TIMEOUT)) {
			return productCategoryCache.getProductCategory();
		}
		ProductCategory productCategory = productCategoryService.getByAllLvCode(product3lvl);
		if (null != productCategory) {
			LOGGER.info("Read ProductCategory【getByAllLvCode】from DB, Save to ProductCategoryCache!");
			return saveProductCategoryInCache(cache, productCategory);
		}
		LOGGER.warn("Not Find ProductCategory from ProductCategoryCache OR DB");
		throw new ApiNotExistException();
	}

	private ProductCategory saveProductCategoryInCache(Cache cache, ProductCategory productCategory) {
		ProductCategoryCache prCache = new ProductCategoryCache(productCategory);
		Product3lvl product3lvl = productCategory.transfer();

		cache.put(productCategory.getPreProcessInterfaceUrl(), prCache);
		cache.put(productCategory.getProductLv2Code() + productCategory.getStringFieldOne(), prCache);
		cache.put(product3lvl.shortName(), prCache);
		return productCategory;
	}

}
