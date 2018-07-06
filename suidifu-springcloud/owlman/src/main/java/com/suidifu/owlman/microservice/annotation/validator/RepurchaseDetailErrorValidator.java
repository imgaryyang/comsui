package com.suidifu.owlman.microservice.annotation.validator;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.owlman.microservice.annotation.RepurchaseDetailError;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucher;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucherDetail;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.List;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午7:52 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
public class RepurchaseDetailErrorValidator implements ConstraintValidator<RepurchaseDetailError, BusinessPaymentVoucher> {
    @Override
    public void initialize(RepurchaseDetailError constraintAnnotation) {
        //just to override
    }

    @Override
    public boolean isValid(BusinessPaymentVoucher voucher,
                           ConstraintValidatorContext context) {
        List<BusinessPaymentVoucherDetail> details = JsonUtils.parseArray(voucher.getDetail(), BusinessPaymentVoucherDetail.class);
        if (details == null) {
            return true;
        }

        for (BusinessPaymentVoucherDetail detail : details) {
            if (Integer.valueOf(VoucherType.REPURCHASE.getOrdinal()).equals(voucher.getVoucherType())) {
                BigDecimal detailTotalAmount = AmountUtils.addAll(
                        detail.getInterest(),
                        detail.getPrincipal(),
                        detail.getPenaltyFee(),
                        detail.getOtherCharge());
                BigDecimal totalAmount = detail.getAmount();

                if (detailTotalAmount.compareTo(BigDecimal.ZERO) > 0 &&
                        detailTotalAmount.compareTo(totalAmount) != 0) {
                    return false;
                }
            }
        }

        return true;
    }
}