package com.suidifu.owlman.microservice.annotation.validator;


import com.suidifu.owlman.microservice.annotation.DetailFormatError;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucher;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucherDetail;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.List;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/17 <br>
 * Time:下午3:33 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
public class DetailFormatErrorValidator implements ConstraintValidator<DetailFormatError, BusinessPaymentVoucher> {
    @Override
    public void initialize(DetailFormatError constraintAnnotation) {
        //just to override
    }

    @Override
    public boolean isValid(BusinessPaymentVoucher voucher,
                           ConstraintValidatorContext context) {
        List<BusinessPaymentVoucherDetail> details = voucher.getBusinessPaymentVoucherDetails();

        return details == null || VoucherType.createRepaymentPlanNotNull(voucher.getVoucherType()) == null || details.stream().filter(detail -> !isValid(detail)).count() <= 0;
    }

    private boolean isValid(BusinessPaymentVoucherDetail detail) {
        return detailAmount(detail).compareTo(BigDecimal.ZERO) >= 0 &&
                detailAmount(detail).compareTo(detail.getAmount()) <= 0;
    }

    private BigDecimal detailAmount(BusinessPaymentVoucherDetail detail) {
        return BigDecimal.ZERO.add(detail.getPrincipal()).
                add(detail.getInterest()).
                add(detail.getServiceCharge()).
                add(detail.getMaintenanceCharge()).
                add(detail.getOtherCharge()).
                add(detail.getPenaltyFee()).
                add(detail.getLatePenalty()).
                add(detail.getLateFee()).
                add(detail.getLateOtherCost());
    }
}