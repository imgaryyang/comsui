package com.suidifu.owlman.microservice.annotation.validator;

import com.suidifu.owlman.microservice.annotation.RequiredOnlyOneFieldNotEmpty;
import com.suidifu.owlman.microservice.model.ActivePaymentVoucherDetail;
import com.zufangbao.sun.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by hwr on 17-10-20.
 */
@Slf4j
public class ActivePaymentVoucherDetailValidator implements ConstraintValidator<RequiredOnlyOneFieldNotEmpty, ActivePaymentVoucherDetail> {
    @Override
    public void initialize(RequiredOnlyOneFieldNotEmpty requiredOnlyOneFieldNotEmpty) {
        //just to override
    }

    @Override
    public boolean isValid(ActivePaymentVoucherDetail activePaymentVoucherDetail, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(activePaymentVoucherDetail.getRepayScheduleNo()) &&
                StringUtils.isEmpty(activePaymentVoucherDetail.getRepaymentPlanNo()) &&
                (activePaymentVoucherDetail.getCurrentPeriod() == null)) {
            log.error("#activePaymentVoucher 至少选填一种编号[repayScheduleNo，repaymentPlanNo,currentPeriod]");
            return false;
        }
        return true;
    }
}