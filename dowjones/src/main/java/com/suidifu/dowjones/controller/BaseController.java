package com.suidifu.dowjones.controller;

import com.suidifu.dowjones.exception.ResponseStatus;
import com.suidifu.dowjones.utils.Constants;
import com.suidifu.dowjones.vo.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.text.MessageFormat;
import java.util.List;

/**
 * User:frankwoo(吴峻申) <br>
 * Date:2017-2-22 <br>
 * Time:11:04 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@Slf4j
public class BaseController {
    public BaseResponse getValidatedResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            BaseResponse baseResponse = new BaseResponse();

            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder stringBuilder = new StringBuilder();
            for (FieldError error : errors) {
                stringBuilder.append(error.getField()).append(":").append(error.getDefaultMessage()).append(" ,");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            String formattedMessage = MessageFormat.format(ResponseStatus.PARAMETER_VALIDATION.getMessage(), stringBuilder);
            baseResponse.setCode(ResponseStatus.PARAMETER_VALIDATION.getCode());
            baseResponse.setMessage(formattedMessage);
            baseResponse.setData(Constants.NULL_DATA);
            return baseResponse;
        }
        return null;
    }
}