package com.suidifu.matryoshka.delayPosition;

import com.demo2do.core.entity.Result;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;

/**
 * Created by zhenghangbo on 12/05/2017.
 */
public interface DelayPositionHandler {

    public void delayPositionDefaultTaskHandler(Result delayPostResult, ProductCategory productCategory, DelayProcessingTaskCacheHandler delayProcessingTaskHandler, SandboxDataSetHandler sandboxDataSetHandler);

}
