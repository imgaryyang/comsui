package com.suidifu.owlman.microservice.annotation.validator;

import com.suidifu.owlman.microservice.annotation.RepeatedRepayScheduleNo;
import com.suidifu.owlman.microservice.model.ActivePaymentVoucher;
import com.suidifu.owlman.microservice.model.ActivePaymentVoucherDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwr on 17-10-19.
 */
@Slf4j
public class InvalidRepayScheduleNovalidator implements ConstraintValidator<RepeatedRepayScheduleNo, ActivePaymentVoucher> {
    @Override
    public void initialize(RepeatedRepayScheduleNo repeatedRepayScheduleNo) {

    }

    @Override
    public boolean isValid(ActivePaymentVoucher activePaymentVoucher, ConstraintValidatorContext constraintValidatorContext) {
        List<String> repaySchedulNoList = new ArrayList<>();
        for (ActivePaymentVoucherDetail detai : activePaymentVoucher.getDetailModel()) {
            String repayScheduleNo = detai.getRepayScheduleNo();
            if (!StringUtils.isEmpty(repayScheduleNo)) {
                if (repaySchedulNoList.contains(repayScheduleNo)) {
                    log.error("#activePaymentVoucher 商户还款计划编号[repayScheduleNo] 不能重复!!!");
                    return false;
                }
                repaySchedulNoList.add(repayScheduleNo);
            }
        }
        return true;
    }
}
