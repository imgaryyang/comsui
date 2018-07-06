package com.zufangbao.earth.handler;

import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.xcode.exception.XcodeException;
import com.zufangbao.earth.model.CustomizeScriptModel;

import java.util.List;
import java.util.Map;

public interface CustomizeScriptModelHandler {
	String updateScript(CustomizeScriptModel model, String script, String author) throws XcodeException;

	String creatNewScript(CustomizeScriptModel model, String script, String author) throws XcodeException;
	
	Map<String, Map<String, Map<String, String>>> getProductCategoryTree(List<ProductCategory> productCategoryList);
}
