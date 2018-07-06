package com.suidifu.owlman.microservice.annotation.validator;

import com.suidifu.owlman.microservice.annotation.RepeatedCurrentPeriod;
import com.suidifu.owlman.microservice.model.ActivePaymentVoucher;
import com.suidifu.owlman.microservice.model.ActivePaymentVoucherDetail;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwr on 17-10-19.
 */
@Slf4j
public class ActivepaymentVoucherRepeatedCurrentPeriodValidator implements ConstraintValidator<RepeatedCurrentPeriod, ActivePaymentVoucher> {
    @Override
    public void initialize(RepeatedCurrentPeriod constraintAnnotation)

    {
        //just to override
    }

    @Override
    public boolean isValid(ActivePaymentVoucher voucher,
                           ConstraintValidatorContext context) {
        List<Integer> currentPeriodList = new ArrayList<>();
        /*List<ActivePaymentVoucherDetail> details = JsonUtils.parseArray(voucher.getDetail(), ActivePaymentVoucherDetail.class);
        if (details == null) {
            log.error("#activePaymentVoucher 请求参数解析错误");
            return true;
        }*/
        for (ActivePaymentVoucherDetail activePaymentVoucherDetail : voucher.getDetailModel()) {
            Integer currentPeriod = activePaymentVoucherDetail.getCurrentPeriod();
            if (currentPeriod != null && currentPeriod != 0 && currentPeriodList.contains(currentPeriod)) {
                log.error("#activePaymentVoucher 明细中存在相同的期数[currentPeriod]!!!");
                return false;
            }
            currentPeriodList.add(currentPeriod);
        }
        return true;
    }
}
