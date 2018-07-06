package com.suidifu.owlman.microservice.annotation.validator;

import com.suidifu.owlman.microservice.annotation.MatchOfVoucherAndDetailAmount;
import com.suidifu.owlman.microservice.model.ActivePaymentVoucher;
import com.suidifu.owlman.microservice.model.ActivePaymentVoucherDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * Created by hwr on 17-10-19.
 */
@Slf4j
public class VoucherDetailAndAmountNotMatch implements ConstraintValidator<MatchOfVoucherAndDetailAmount, ActivePaymentVoucher> {
    @Override
    public void initialize(MatchOfVoucherAndDetailAmount matchOfVoucherAndDetailAmount) {
        //just to override
    }

    @Override
    public boolean isValid(ActivePaymentVoucher activePaymentVoucher, ConstraintValidatorContext constraintValidatorContext) {
        BigDecimal detailAmount = BigDecimal.ZERO;
        if (activePaymentVoucher.getVoucherAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }
        if (CollectionUtils.isEmpty(activePaymentVoucher.getDetailModel())) {
            return true;
        }
        for (ActivePaymentVoucherDetail detail : activePaymentVoucher.getDetailModel()) {
            detailAmount = detailAmount.add(detail.getAmount());
        }
        if (activePaymentVoucher.getVoucherAmount().compareTo(detailAmount) != 0) {
            log.error("#activePaymentVoucher 凭证金额与明细总金额不匹配");
            return false;
        }
        return true;
    }
}
