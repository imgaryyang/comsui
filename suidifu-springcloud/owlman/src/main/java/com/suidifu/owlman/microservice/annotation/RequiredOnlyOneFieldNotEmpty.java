package com.suidifu.owlman.microservice.annotation;


import com.suidifu.owlman.microservice.annotation.validator.*;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:上午12:51 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {
        ModifyRepaymentPlanValidator.class,
        ModifyRepaymentInformationValidator.class,
        BusinessPaymentVoucherDetailValidator.class,
        MutableFeeValidator.class,
        RequiredInformationValidator.class,
        RequiredOnlyOneFieldNotEmptyValidator.class,
        ActivePaymentVoucherDetailValidator.class})
@Documented
public @interface RequiredOnlyOneFieldNotEmpty {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}