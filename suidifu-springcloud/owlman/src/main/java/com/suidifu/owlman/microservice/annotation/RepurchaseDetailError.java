package com.suidifu.owlman.microservice.annotation;


import com.suidifu.owlman.microservice.annotation.validator.RepurchaseDetailErrorValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午7:51 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {RepurchaseDetailErrorValidator.class})
@Documented
public @interface RepurchaseDetailError {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}