package com.suidifu.owlman.microservice.annotation;


import com.suidifu.owlman.microservice.annotation.validator.TransactionDateErrorValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午10:48 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TransactionDateErrorValidator.class)
@Documented
public @interface TransactionDateError {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}