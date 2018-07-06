package com.suidifu.owlman.microservice.annotation.validator;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.owlman.microservice.annotation.RepeatedRepaymentPlanNo;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucher;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucherDetail;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午8:34 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
public class RepeatedRepaymentPlanNoValidator implements ConstraintValidator<RepeatedRepaymentPlanNo, BusinessPaymentVoucher> {
    @Override
    public void initialize(RepeatedRepaymentPlanNo constraintAnnotation) {
        //just to override
    }

    @Override
    public boolean isValid(BusinessPaymentVoucher voucher,
                           ConstraintValidatorContext context) {
        List<BusinessPaymentVoucherDetail> details = JsonUtils.parseArray(voucher.getDetail(), BusinessPaymentVoucherDetail.class);
        if (details == null) {
            return true;
        }
        List<String> repaymentPlanNoList = new ArrayList<>();
        for (BusinessPaymentVoucherDetail detail : details) {
            String repaymentPlanNo = detail.getRepaymentPlanNo();
            if (repaymentPlanNoList.contains(repaymentPlanNo)) {
                return false;
            } else {
                if (StringUtils.isNotEmpty(repaymentPlanNo)) {
                    repaymentPlanNoList.add(repaymentPlanNo);
                }
            }
        }

        return true;
    }
}