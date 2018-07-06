package com.zufangbao.earth.yunxin.api.v2;

import com.suidifu.matryoshka.prePosition.impl.AbstractPrePositionHandler;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("normalPrePositionHandler")
public class NormalPrePositionHandler extends AbstractPrePositionHandler {

    @Override
    public Integer prePositionDefaultTaskHandler(String prePositionUrl, Map<String, String> requestParams, Map<String, String> delayPostRequestParams, Log logger) {
        return null;
    }

    @Override
    public Integer prePositionCustomizeTaskHandler(String prePositionUrl,
                                                   String reservedColumn) {
        // TODO Auto-generated method stub
        return null;
    }

}
