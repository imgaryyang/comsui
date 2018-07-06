package com.suidifu.owlman.microservice.annotation.validator;


import com.suidifu.owlman.microservice.annotation.RequiredOnlyOneFieldNotEmpty;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucherDetail;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午7:04 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
public class BusinessPaymentVoucherDetailValidator implements ConstraintValidator<RequiredOnlyOneFieldNotEmpty, BusinessPaymentVoucherDetail> {
    @Override
    public void initialize(RequiredOnlyOneFieldNotEmpty constraintAnnotation) {
        //just to override
    }

    @Override
    public boolean isValid(BusinessPaymentVoucherDetail detail,
                           ConstraintValidatorContext context) {
        return !StringUtils.isEmpty(detail.getRepayScheduleNo()) ||
                !StringUtils.isEmpty(detail.getRepaymentPlanNo()) || (detail.getCurrentPeriod() != null);
    }
}
