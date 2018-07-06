package com.suidifu.owlman.microservice.annotation.validator;

import com.suidifu.owlman.microservice.annotation.RequiredOnlyOneFieldNotEmpty;
import com.suidifu.owlman.microservice.model.ModifyRepaymentInformation;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:上午12:53 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
public class ModifyRepaymentInformationValidator implements ConstraintValidator<RequiredOnlyOneFieldNotEmpty, ModifyRepaymentInformation> {
    @Override
    public void initialize(RequiredOnlyOneFieldNotEmpty constraintAnnotation) {
        //just to override
    }

    @Override
    public boolean isValid(ModifyRepaymentInformation modifyRepaymentInformation,
                           ConstraintValidatorContext context) {
        return !StringUtils.isEmpty(modifyRepaymentInformation.getUniqueId()) ||
                !StringUtils.isEmpty(modifyRepaymentInformation.getContractNo());
    }
}