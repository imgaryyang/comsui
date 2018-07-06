package com.suidifu.owlman.microservice.annotation;


import com.suidifu.owlman.microservice.annotation.validator.ActivepaymentVoucherRepeatedCurrentPeriodValidator;
import com.suidifu.owlman.microservice.annotation.validator.RepeatedCurrentPeriodValidator;
import com.suidifu.owlman.microservice.annotation.validator.ThirdPartVoucherCurrentPeriodValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午7:36 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {RepeatedCurrentPeriodValidator.class,
        ActivepaymentVoucherRepeatedCurrentPeriodValidator.class,
        ThirdPartVoucherCurrentPeriodValidator.class})
@Documented
public @interface RepeatedCurrentPeriod {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}