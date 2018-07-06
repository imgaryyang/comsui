package com.suidifu.owlman.microservice.annotation.validator;

import com.suidifu.owlman.microservice.annotation.RequiredOnlyOneFieldNotEmpty;
import com.suidifu.owlman.microservice.model.MutableFee;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by hwr on 17-10-17.
 */
public class MutableFeeValidator implements ConstraintValidator<RequiredOnlyOneFieldNotEmpty, MutableFee> {
    @Override
    public void initialize(RequiredOnlyOneFieldNotEmpty requiredOnlyOneFieldNotEmpty) {
        //just to override
    }

    @Override
    public boolean isValid(MutableFee mutableFee, ConstraintValidatorContext constraintValidatorContext) {
        return !StringUtils.isEmpty(mutableFee.getUniqueId()) || !StringUtils.isEmpty(mutableFee.getContractNo());
    }
}