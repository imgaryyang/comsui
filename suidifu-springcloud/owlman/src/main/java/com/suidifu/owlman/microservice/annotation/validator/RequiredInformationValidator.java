package com.suidifu.owlman.microservice.annotation.validator;

import com.suidifu.owlman.microservice.annotation.RequiredOnlyOneFieldNotEmpty;
import com.suidifu.owlman.microservice.model.ActivePaymentVoucher;
import com.zufangbao.sun.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by hwr on 17-10-19.
 */
@Slf4j
public class RequiredInformationValidator implements ConstraintValidator<RequiredOnlyOneFieldNotEmpty, ActivePaymentVoucher> {
    @Override
    public void initialize(RequiredOnlyOneFieldNotEmpty requiredOnlyOneFieldNotEmpty) {
        //just to override
    }

    @Override
    public boolean isValid(ActivePaymentVoucher activePaymentVoucher, ConstraintValidatorContext constraintValidatorContext) {
        if (activePaymentVoucher.getDetailModel().stream().anyMatch(detail ->
                StringUtils.isEmpty(detail.getUniqueId()) == StringUtils.isEmpty(detail.getContractNo()))) {
            log.error("#activePaymentVoucher 请选填其中一种编号[uniqueId，contractNo]");
            return false;
        }
        return true;
    }
}