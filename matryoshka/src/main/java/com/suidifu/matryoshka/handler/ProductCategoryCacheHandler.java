package com.suidifu.matryoshka.handler;

import com.suidifu.matryoshka.productCategory.Product3lvl;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.xcode.handler.SourceCodeCompilerHandler;

/**
 *
 * Created by louguanyang on 2017/4/20.
 */
public interface ProductCategoryCacheHandler {

    ProductCategory get(String preProcessUrl, boolean isRecursively);

    ProductCategory get(String lvL2Code,String Reason);

    ProductCategory get(Product3lvl product3lvl);

    Object getScript(ProductCategory productCategory);

    void register(String businessType, SourceCodeCompilerHandler sourceCodeCompilerHandler);

    void clearAll();

    void clearByUrl(String url);

	void clearByScriptMD5Version(String scriptMD5Version);
}
