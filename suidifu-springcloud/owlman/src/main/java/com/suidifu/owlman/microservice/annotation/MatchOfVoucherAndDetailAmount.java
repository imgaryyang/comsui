package com.suidifu.owlman.microservice.annotation;


import com.suidifu.owlman.microservice.annotation.validator.MatchOfVoucherAndDetailAmountValidator;
import com.suidifu.owlman.microservice.annotation.validator.VoucherDetailAndAmountNotMatch;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午11:13 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {MatchOfVoucherAndDetailAmountValidator.class,
        VoucherDetailAndAmountNotMatch.class})
@Documented
public @interface MatchOfVoucherAndDetailAmount {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}