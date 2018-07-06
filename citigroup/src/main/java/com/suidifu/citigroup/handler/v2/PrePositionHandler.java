package com.suidifu.citigroup.handler.v2;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.suidifu.matryoshka.Model.HandlerErrorMapSpec;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.prePosition.impl.AbstractPrePositionHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;

/**
 * @author apple
 *
 */
@Component("prePositionHandler")
public class PrePositionHandler extends AbstractPrePositionHandler {

		@Autowired
		@Qualifier("normalProductCategoryCacheHandler")
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
					return HandlerErrorMapSpec.ErrorCode.API_NOT_FOUND;
				}

	        return HandlerErrorMapSpec.ErrorCode.SUCCESS;
	    }
	
	
	    public Integer prePositionCustomizeTaskHandler(String prePositionUrl,
			String reservedColumn) {
		// TODO Auto-generated method stub
		return null;
	    }

}
