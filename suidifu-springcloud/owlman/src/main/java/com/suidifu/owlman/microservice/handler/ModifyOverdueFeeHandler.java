package com.suidifu.owlman.microservice.handler;

import com.zufangbao.sun.api.model.modify.ModifyOverdueParams;

/**
 * Created by MieLongJun on 18-1-26.
 * <p>
 * from com.zufangbao.wellsfargo.yunxin.handler.ModifyOverdueFeeNewHandler
 */
public interface ModifyOverdueFeeHandler {

    void modifyOverdueFeeSaveLog(String contractUuid, ModifyOverdueParams modifyOverDueFeeDetail, int priority) throws Exception;

}
