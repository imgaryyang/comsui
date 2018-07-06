package com.suidifu.owlman.microservice.annotation;


import com.suidifu.owlman.microservice.annotation.validator.InvalidRepayScheduleNovalidator;
import com.suidifu.owlman.microservice.annotation.validator.RepeatedScheduleNoValidator;
import com.suidifu.owlman.microservice.annotation.validator.ThirdPartVoucherScheduleNoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午7:16 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {RepeatedScheduleNoValidator.class,
        InvalidRepayScheduleNovalidator.class,
        ThirdPartVoucherScheduleNoValidator.class})
@Documented
public @interface RepeatedRepayScheduleNo {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}