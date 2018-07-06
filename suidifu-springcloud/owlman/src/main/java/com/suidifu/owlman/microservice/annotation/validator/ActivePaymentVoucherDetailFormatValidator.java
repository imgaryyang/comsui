package com.suidifu.owlman.microservice.annotation.validator;

import com.suidifu.owlman.microservice.annotation.DetailFormatError;
import com.suidifu.owlman.microservice.model.ActivePaymentVoucher;
import com.suidifu.owlman.microservice.model.ActivePaymentVoucherDetail;
import com.zufangbao.sun.utils.CheckFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by hwr on 17-10-19.
 */
@Slf4j
public class ActivePaymentVoucherDetailFormatValidator implements ConstraintValidator<DetailFormatError, ActivePaymentVoucher> {
    @Override
    public void initialize(DetailFormatError constraintAnnotation) {
        //just to override
    }

    @Override
    public boolean isValid(ActivePaymentVoucher voucher,
                           ConstraintValidatorContext context) {
        List<ActivePaymentVoucherDetail> details = voucher.getDetailModel();
        if (CollectionUtils.isEmpty(details)) {
            log.error("#activePaymentVoucher details is empty");
            return true;
        }
        if (voucher.getVoucherType() == null) {
            return true;
        }
        if (details.stream().filter(detail -> !isValid(detail)).count() > 0) {
            log.error("#activePaymentVoucher 凭证明细内容错误［detail］，字段格式错误！");
            return false;
        }
        return true;
    }

    private boolean isValid(ActivePaymentVoucherDetail detail) {
        if (BigDecimal.ZERO.compareTo(detail.getAmount()) >= 0) {
            log.error("#activePaymentVoucher amount必须大于0");
            return false;
        }
        if (detailAmount(detail).compareTo(BigDecimal.ZERO) > 0 &&
                detailAmount(detail).compareTo(detail.getAmount()) != 0) {
            log.error("#activePaymentVoucher 凭证明细金额与凭证金额不匹配");
            return false;
        }
        if (!checkDetailAmount(detail)) {
            log.error("#activePaymentVoucher 凭证明细金额格式错误");
            return false;
        }
        return true;
    }

    private BigDecimal detailAmount(ActivePaymentVoucherDetail detail) {
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

    private boolean checkDetailAmount(ActivePaymentVoucherDetail detail) {
        try {
            return CheckFormatUtils.checkRMBCurrencyBigDecimal(detail.getPrincipal()) && CheckFormatUtils.checkRMBCurrencyBigDecimal(detail.getInterest()) &&
                    CheckFormatUtils.checkRMBCurrencyBigDecimal(detail.getServiceCharge()) && CheckFormatUtils.checkRMBCurrencyBigDecimal(detail.getMaintenanceCharge()) &&
                    CheckFormatUtils.checkRMBCurrencyBigDecimal(detail.getOtherCharge()) && CheckFormatUtils.checkRMBCurrencyBigDecimal(detail.getPenaltyFee()) &&
                    CheckFormatUtils.checkRMBCurrencyBigDecimal(detail.getLatePenalty()) && CheckFormatUtils.checkRMBCurrencyBigDecimal(detail.getLateFee()) &&
                    CheckFormatUtils.checkRMBCurrencyBigDecimal(detail.getLateOtherCost());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
