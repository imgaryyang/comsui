package com.suidifu.owlman.microservice.annotation.validator;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.owlman.microservice.annotation.MatchOfVoucherAndDetailAmount;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucher;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucherDetail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.List;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午11:14 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
public class MatchOfVoucherAndDetailAmountValidator implements ConstraintValidator<MatchOfVoucherAndDetailAmount, BusinessPaymentVoucher> {
    @Override
    public void initialize(MatchOfVoucherAndDetailAmount constraintAnnotation) {
        //just to override
    }

    @Override
    public boolean isValid(BusinessPaymentVoucher voucher, ConstraintValidatorContext context) {
        BigDecimal detailAmount = BigDecimal.ZERO;
        List<BusinessPaymentVoucherDetail> details = JsonUtils.parseArray(voucher.getDetail(), BusinessPaymentVoucherDetail.class);
        if (details == null) {
            return true;
        }

        for (BusinessPaymentVoucherDetail businessPaymentVoucherDetail : details) {
            detailAmount = detailAmount.add(businessPaymentVoucherDetail.getAmount());
        }

        return voucher.getVoucherAmount().compareTo(detailAmount) == 0;
    }
}