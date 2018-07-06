package com.suidifu.matryoshka.prePosition.impl;

import com.suidifu.matryoshka.Model.HandlerErrorMapSpec;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.prePosition.PrePositionHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

/**
 * Created by zhenghangbo on 11/05/2017.
 */
public abstract class AbstractPrePositionHandler implements PrePositionHandler{


    @Autowired
    private ProductCategoryCacheHandler productCategoryCacheHandler;

    @Autowired
    @Qualifier("normalPreSandboxHandler")
    private com.suidifu.matryoshka.handler.SandboxDataSetHandler sandboxDataSetHandler;



    @Override
    public Integer prePositionDefaultTaskHandler(String prePositionUrl,
                                                 Map<String, String> requestParams,
                                                 Map<String, String> delayPostRequestParams, Log logger) {

        ProductCategory productCategory = productCategoryCacheHandler.get(prePositionUrl, true);

	    if (null == productCategory) {
		    return HandlerErrorMapSpec.ErrorCode.API_NOT_FOUND;
        }

        CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);

	    boolean evaluate = services.evaluate(sandboxDataSetHandler, requestParams, delayPostRequestParams, logger);

	    if (!evaluate) {
		    return HandlerErrorMapSpec.ErrorCode.INVALID_PARAMS;
        }
        return HandlerErrorMapSpec.ErrorCode.SUCCESS;
    }
}