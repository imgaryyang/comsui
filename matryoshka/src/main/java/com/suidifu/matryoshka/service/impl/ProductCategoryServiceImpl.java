package com.suidifu.matryoshka.service.impl;

import com.demo2do.core.persistence.GenericJdbcSupport;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.matryoshka.productCategory.Product3lvl;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.matryoshka.productCategory.ProductCategoryStatus;
import com.suidifu.matryoshka.service.ProductCategoryService;
import com.zufangbao.sun.utils.StringUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CommonsLog
@Service("productCategorySnapshotService")
public class ProductCategoryServiceImpl extends GenericServiceImpl<ProductCategory> implements ProductCategoryService {
	@Autowired
	private GenericJdbcSupport genericJdbcSupport;

	@Override
	public void close(List<String> uuidList) {
		changeStatus(uuidList, ProductCategoryStatus.INVALID);
	}

	@Override
	public void close(String uuid) {
		close(Collections.singletonList(uuid));
	}

	@Override
	public void open(List<String> uuidList) {
		changeStatus(uuidList, ProductCategoryStatus.VALID);
	}

	@Override
	public void open(String uuid) {
		open(Collections.singletonList(uuid));

	}

	private void changeStatus(List<String> uuidList, ProductCategoryStatus status) {
		if (CollectionUtils.isNotEmpty(uuidList)) {
			String sentence = "UPDATE t_product_category SET status = " + status.ordinal() + " WHERE uuid IN(:uuids)";
			genericJdbcSupport.executeSQL(sentence, "uuids", uuidList);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public ProductCategory getByPreUrl(String preProcessInterfaceUrl, ProductCategoryStatus status) {
		if (StringUtils.isEmpty(preProcessInterfaceUrl)) {
			return null;
		}
		String sentence = "FROM ProductCategory WHERE preProcessInterfaceUrl =:preProcessInterfaceUrl";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("preProcessInterfaceUrl", preProcessInterfaceUrl);

		if (null != status) {
			sentence += " AND status =:status";
			parameters.put("status", status);
		}

		List<ProductCategory> list = genericDaoSupport.searchForList(sentence, parameters);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public ProductCategory getByPreUrl(String preProcessInterfaceUrl) {
		return this.getByPreUrl(preProcessInterfaceUrl, ProductCategoryStatus.VALID);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ProductCategory getByAllLvCode(Product3lvl product3lvl) {
		if (null == product3lvl) {
			return null;
		}
		StringBuffer sentence = new StringBuffer("FROM ProductCategory WHERE status =:status");
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("status", ProductCategoryStatus.VALID);
		if (StringUtils.isNotEmpty(product3lvl.getLv1Code())) {
			sentence.append(" AND productLv1Code =:lv1Code");
			parameters.put("lv1Code", product3lvl.getLv1Code());
		} else {
			sentence.append(" AND productLv1Code IS NULL");
		}
		if (StringUtils.isNotEmpty(product3lvl.getLv2Code())) {
			sentence.append(" AND productLv2Code =:lv2Code");
			parameters.put("lv2Code", product3lvl.getLv2Code());
		} else {
			sentence.append(" AND productLv2Code IS NULL");
		}
		if (StringUtils.isNotEmpty(product3lvl.getLv3Code())) {
			sentence.append(" AND productLv3Code =:lv3Code");
			parameters.put("lv3Code", product3lvl.getLv3Code());
		} else {
			sentence.append(" AND productLv3Code IS NULL");
		}
		List<ProductCategory> list = genericDaoSupport.searchForList(sentence.toString(), parameters);
		if (CollectionUtils.isNotEmpty(list) && list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<ProductCategory> get_all_by_lv_code(Product3lvl product3lvl) {
		if (null == product3lvl) {
			return Collections.emptyList();
		}
		StringBuilder sentence = new StringBuilder("FROM ProductCategory WHERE status =:status");
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("status", ProductCategoryStatus.VALID);
		if (StringUtils.isNotEmpty(product3lvl.getLv1Code())) {
			sentence.append(" AND productLv1Code =:lv1Code");
			parameters.put("lv1Code", product3lvl.getLv1Code());
		}
		if (StringUtils.isNotEmpty(product3lvl.getLv2Code())) {
			sentence.append(" AND productLv2Code =:lv2Code");
			parameters.put("lv2Code", product3lvl.getLv2Code());
		}
		if (StringUtils.isNotEmpty(product3lvl.getLv3Code())) {
			sentence.append(" AND productLv3Code =:lv3Code");
			parameters.put("lv3Code", product3lvl.getLv3Code());
		}
		List<ProductCategory> list = genericDaoSupport.searchForList(sentence.toString(), parameters);
		if (CollectionUtils.isNotEmpty(list)) {
			return list;
		}
		return Collections.emptyList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProductCategory> getAll() {
		List<ProductCategory> list = genericDaoSupport.searchForList("FROM ProductCategory");
		return CollectionUtils.isNotEmpty(list) ? list : Collections.emptyList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public ProductCategory get_by_uuid(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			return null;
		}
		String sentence = "FROM ProductCategory WHERE uuid =:uuid";
		List<ProductCategory> list = genericDaoSupport.searchForList(sentence, "uuid", uuid);
		return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean checkScriptVersion(String uuid, String currentVersion) {
		if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(currentVersion)) {
			return false;
		}
		String sentence = "SELECT scriptMd5Version FROM ProductCategory WHERE uuid =:uuid";
		List<String> list = genericDaoSupport.searchForList(sentence, "uuid", uuid);
		if (CollectionUtils.isNotEmpty(list)) {
			return currentVersion.equals(list.get(0));
		}
		return false;
	}

	@Override
	public ProductCategory get_by_lvL2Code_reasonCode(String lvL2Code, String reasonCode) {
		if (StringUtils.isEmpty(lvL2Code) || StringUtils.isEmpty(reasonCode)) {
			return null;
		}
		String sentence = "FROM ProductCategory WHERE status =:status AND productLv2Code =:productLv2Code AND stringFieldOne =:stringFieldOne";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("status", ProductCategoryStatus.VALID);
		parameters.put("productLv2Code", lvL2Code);
		parameters.put("stringFieldOne", reasonCode);
		List<ProductCategory> list = genericDaoSupport.searchForList(sentence, parameters);
		return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ProductCategory get_by_script_version(String scriptMd5Version) {
		if (StringUtils.isEmpty(scriptMd5Version)) {
			return null;
		}
		String sentence = "FROM ProductCategory WHERE status =:status AND scriptMd5Version =:scriptMd5Version";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("status", ProductCategoryStatus.VALID);
		parameters.put("scriptMd5Version", scriptMd5Version);
		List<ProductCategory> list = genericDaoSupport.searchForList(sentence, parameters);
		return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
	}

	// FIXME remember give an UT
	@Override
	public boolean check_by_pre_url(String preProcessInterfaceUrl, ProductCategoryStatus status) {
		if (StringUtils.isEmpty(preProcessInterfaceUrl)) {
			return false;
		}
		String sentence = "FROM ProductCategory WHERE preProcessInterfaceUrl =:preProcessInterfaceUrl";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("preProcessInterfaceUrl", preProcessInterfaceUrl);
		if (null != status) {
			sentence += " AND status =:status";
			parameters.put("status", status);
		}
		int count = genericDaoSupport.count(sentence, parameters);
		if (count > 0) {
			return true;
		}
		return false;

	}
}
