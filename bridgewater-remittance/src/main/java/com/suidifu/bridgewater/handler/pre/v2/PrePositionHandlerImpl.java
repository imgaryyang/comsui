package com.suidifu.bridgewater.handler.pre.v2;

import com.suidifu.matryoshka.prePosition.impl.AbstractPrePositionHandler;
import org.springframework.stereotype.Component;

/**
 * Created by zhenghangbo on 12/05/2017.
 */

@Component("normalPrePositionHandlerNoTransaction")
public class PrePositionHandlerImpl extends AbstractPrePositionHandler {

    @Override
    public Integer prePositionCustomizeTaskHandler(String prePositionUrl, String reservedColumn) {
        return null;
    }
}
