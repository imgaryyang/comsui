package com.suidifu.dowjones.exception;

import com.suidifu.dowjones.utils.Constants;
import com.suidifu.dowjones.vo.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 响应对象处理器
 */
@Component
@Slf4j
public class ResponseHandler {
    public BaseResponse getBaseResponse(ResponseStatus responseStatus) {
        BaseResponse baseResponse = new BaseResponse();
        if (responseStatus != null) {
            baseResponse.setMessage(responseStatus.getMessage());
            baseResponse.setCode(responseStatus.getCode());
            baseResponse.setData(Constants.NULL_DATA);
        }

        return baseResponse;
    }
}