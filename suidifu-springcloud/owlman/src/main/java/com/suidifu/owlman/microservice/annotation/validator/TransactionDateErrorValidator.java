package com.suidifu.owlman.microservice.annotation.validator;

import com.suidifu.owlman.microservice.annotation.TransactionDateError;
import com.zufangbao.sun.utils.DateUtils;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午10:49 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
public class TransactionDateErrorValidator implements ConstraintValidator<TransactionDateError, String> {
    @Override
    public void initialize(TransactionDateError constraintAnnotation) {
        //just to override
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.equals(value, DateUtils.DATE_FORMAT_0001_01_01) || StringUtils.isEmpty(value);
    }
}