package com.suidifu.owlman.microservice.annotation;

import lombok.extern.apachecommons.CommonsLog;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 凭证类型注解校验 (只支持Integer类型,默认为非空)
 *
 * @author wjh on 17-11-29.
 */
@Documented
@Inherited
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CheckVoucherType.VoucherTypeValidator.class})
public @interface CheckVoucherType {
    String message() default "凭证类型错误";

    int[] type();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @CommonsLog
    class VoucherTypeValidator implements ConstraintValidator<CheckVoucherType, Integer> {
        private int[] type;

        @Override
        public void initialize(CheckVoucherType annotationValue) {
            type = annotationValue.type();
        }

        @Override
        public boolean isValid(Integer value, ConstraintValidatorContext context) {
            if (value == null) {
                log.error("voucherType is null");
                return false;
            }
            for (int aType : type) {
                if (aType == value) {
                    return true;
                }
            }
            log.error("this voucherType is not supported");
            return false;
        }
    }
}
