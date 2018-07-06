package com.suidifu.matryoshka.prePosition;

import org.apache.commons.logging.Log;

import java.util.Map;

/**
 * Created by zhenghangbo on 11/05/2017.
 */
public interface PrePositionHandler {

    public Integer prePositionDefaultTaskHandler(String prePositionUrl,
        Map<String, String> requestParams,
        Map<String, String> delayPostRequestParams, Log logger);

    public Integer prePositionCustomizeTaskHandler(String prePositionUrl, String reservedColumn);

}