package com.suidifu.owlman.microservice.annotation.validator;

import com.suidifu.owlman.microservice.annotation.RequiredOnlyOneFieldNotEmpty;
import com.suidifu.owlman.microservice.model.ModifyRepaymentPlan;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:上午1:52 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
public class ModifyRepaymentPlanValidator implements ConstraintValidator<RequiredOnlyOneFieldNotEmpty, ModifyRepaymentPlan> {
    @Override
    public void initialize(RequiredOnlyOneFieldNotEmpty constraintAnnotation) {
        //just to override
    }

    @Override
    public boolean isValid(ModifyRepaymentPlan modifyRepaymentPlan,
                           ConstraintValidatorContext context) {
        return !StringUtils.isEmpty(modifyRepaymentPlan.getUniqueId()) ||
                !StringUtils.isEmpty(modifyRepaymentPlan.getContractNo());
    }
}