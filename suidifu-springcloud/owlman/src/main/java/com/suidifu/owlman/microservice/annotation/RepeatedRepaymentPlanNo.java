package com.suidifu.owlman.microservice.annotation;


import com.suidifu.owlman.microservice.annotation.validator.ActivePaymentVoucherRepeatedRepaymentPlanNoValidator;
import com.suidifu.owlman.microservice.annotation.validator.RepeatedRepaymentPlanNoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午8:33 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {RepeatedRepaymentPlanNoValidator.class,
        ActivePaymentVoucherRepeatedRepaymentPlanNoValidator.class})
@Documented
public @interface RepeatedRepaymentPlanNo {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}