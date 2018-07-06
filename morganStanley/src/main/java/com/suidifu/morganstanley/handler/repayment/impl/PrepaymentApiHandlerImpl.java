package com.suidifu.morganstanley.handler.repayment.impl;

import com.suidifu.morganstanley.handler.repayment.PrepaymentApiHandler;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentApplication;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author louguanyang at 2017/10/17 16:51
 * @mail louguanyang@hzsuidifu.com
 */
@Component("PrepaymentApiHandler")
public class PrepaymentApiHandlerImpl implements PrepaymentApiHandler {
    @Resource
    private PrepaymentApplicationService prepaymentApplicationService;

    @Override
    public void checkRequestNo(String requestNo, String ip) {
        List<PrepaymentApplication> applications = prepaymentApplicationService.checkRequestNo(requestNo);
        if (CollectionUtils.isNotEmpty(applications)) {
            throw new ApiException(ApiMessage.REPEAT_REQUEST_NO);
        }
    }

}
