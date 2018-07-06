package com.suidifu.owlman.microservice.annotation.validator;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.owlman.microservice.annotation.RepeatedRepayScheduleNo;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucher;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucherDetail;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午7:18 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
public class RepeatedScheduleNoValidator implements ConstraintValidator<RepeatedRepayScheduleNo, BusinessPaymentVoucher> {
    @Override
    public void initialize(RepeatedRepayScheduleNo constraintAnnotation) {
        //just to override
    }

    @Override
    public boolean isValid(BusinessPaymentVoucher voucher,
                           ConstraintValidatorContext context) {
        List<String> repayScheduleNoList = new ArrayList<>();
        List<BusinessPaymentVoucherDetail> details = JsonUtils.parseArray(voucher.getDetail(), BusinessPaymentVoucherDetail.class);
        if (details == null) {
            return true;
        }
        for (BusinessPaymentVoucherDetail businessPaymentVoucherDetail : details) {
            String repayScheduleNo = businessPaymentVoucherDetail.getRepayScheduleNo();
            if (!StringUtils.isEmpty(repayScheduleNo)) {
                if (repayScheduleNoList.contains(repayScheduleNo)) {
                    return false;
                }
                repayScheduleNoList.add(repayScheduleNo);
            }
        }
        return true;
    }
}