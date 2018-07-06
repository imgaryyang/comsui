package com.suidifu.owlman.microservice.annotation.validator;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.owlman.microservice.annotation.RepeatedCurrentPeriod;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucher;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucherDetail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午7:37 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
public class RepeatedCurrentPeriodValidator implements ConstraintValidator<RepeatedCurrentPeriod, BusinessPaymentVoucher> {
    @Override
    public void initialize(RepeatedCurrentPeriod constraintAnnotation) {
        //just to override
    }

    @Override
    public boolean isValid(BusinessPaymentVoucher voucher,
                           ConstraintValidatorContext context) {
        List<Integer> currentPeriodList = new ArrayList<>();
        List<BusinessPaymentVoucherDetail> details = JsonUtils.parseArray(voucher.getDetail(), BusinessPaymentVoucherDetail.class);
        if (details == null) {
            return true;
        }
        for (BusinessPaymentVoucherDetail businessPaymentVoucherDetail : details) {
            Integer currentPeriod = businessPaymentVoucherDetail.getCurrentPeriod();
            if (currentPeriod != null && currentPeriod != 0 && currentPeriodList.contains(currentPeriod)) {
                return false;
            }
            currentPeriodList.add(currentPeriod);
        }
        return true;
    }
}
