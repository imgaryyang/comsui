package com.suidifu.bridgewater.handler.pre.v2;

import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.impl.AbstractDelayPositionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by zhenghangbo on 12/05/2017.
 */
@Component("normalDelayPositionHandler")
public class DelayPositionHandlerImpl extends AbstractDelayPositionHandler {


    @Autowired
    @Qualifier("normalDelayTaskConfigCacheHandler")
    private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;



}
