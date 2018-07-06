package com.suidifu.matryoshka.service;

import java.util.List;

import com.demo2do.core.service.GenericService;
import com.suidifu.matryoshka.productCategory.Product3lvl;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.matryoshka.productCategory.ProductCategoryStatus;

public interface ProductCategoryService extends GenericService<ProductCategory> {

	void close(List<String> uuidList);

	void close(String uuid);

	void open(List<String> uuidList);

	void open(String uuid);

	ProductCategory getByPreUrl(String preProcessInterfaceUrl, ProductCategoryStatus status);

	ProductCategory getByPreUrl(String preProcessInterfaceUrl);

	ProductCategory getByAllLvCode(Product3lvl product3lvl);

	List<ProductCategory> get_all_by_lv_code(Product3lvl product3lvl);

	List<ProductCategory> getAll();

	ProductCategory get_by_uuid(String uuid);

	ProductCategory get_by_script_version(String scriptMD5Version);

	boolean checkScriptVersion(String uuid, String currentVersion);

	ProductCategory get_by_lvL2Code_reasonCode(String lvL2Code, String reasonCode);

	boolean check_by_pre_url(String preProcessInterfaceUrl, ProductCategoryStatus status);

}
