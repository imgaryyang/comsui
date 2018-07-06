package com.suidifu.owlman.microservice.annotation.validator;

import com.suidifu.owlman.microservice.annotation.RepeatedRepaymentPlanNo;
import com.suidifu.owlman.microservice.model.ActivePaymentVoucher;
import com.suidifu.owlman.microservice.model.ActivePaymentVoucherDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwr on 17-10-20.
 */
@Slf4j
public class ActivePaymentVoucherRepeatedRepaymentPlanNoValidator implements ConstraintValidator<RepeatedRepaymentPlanNo, ActivePaymentVoucher> {
    @Override
    public void initialize(RepeatedRepaymentPlanNo repeatedRepaymentPlanNo) {

    }

    @Override
    public boolean isValid(ActivePaymentVoucher activePaymentVoucher, ConstraintValidatorContext constraintValidatorContext) {
        BigDecimal detailAmount = BigDecimal.ZERO;
        List<String> repayment_plan_no_list = new ArrayList<>();
        if (activePaymentVoucher.getVoucherAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }
        for (ActivePaymentVoucherDetail detail : activePaymentVoucher.getDetailModel()) {
            String repaymentPlanNo = detail.getRepaymentPlanNo();
            if (repayment_plan_no_list.contains(repaymentPlanNo)) {
                log.error("#activePaymentVoucher repaymentPlanNo 还款计划编号重复");
                return false;
            } else {
                if (StringUtils.isNotEmpty(repaymentPlanNo)) {
                    repayment_plan_no_list.add(repaymentPlanNo);
                }
            }
            detailAmount = detailAmount.add(detail.getAmount());
        }
        return true;
    }
}
