package com.suidifu.owlman.microservice.annotation.validator;

import com.suidifu.owlman.microservice.annotation.RepeatedCurrentPeriod;
import com.suidifu.owlman.microservice.model.ThirdPartVoucher;
import com.suidifu.owlman.microservice.model.ThirdPartVoucherDetail;
import com.suidifu.owlman.microservice.model.ThirdPartVoucherRepayDetail;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjianhua
 */
public class ThirdPartVoucherCurrentPeriodValidator implements ConstraintValidator<RepeatedCurrentPeriod, ThirdPartVoucher> {
    @Override
    public void initialize(RepeatedCurrentPeriod constraintAnnotation) {
        //just to override
    }

    @Override
    public boolean isValid(ThirdPartVoucher voucher,
                           ConstraintValidatorContext context) {
        List<Integer> currentPeriodList = new ArrayList<>();
        List<ThirdPartVoucherDetail> details = com.zufangbao.sun.utils.JsonUtils.parseArray(voucher.getDetailList(), ThirdPartVoucherDetail.class);
        if (CollectionUtils.isEmpty(details)) {
            return true;
        }
        for (ThirdPartVoucherDetail detailModel : details) {
            if (CollectionUtils.isEmpty(detailModel.getRepayDetailList())) {
                return true;
            }
            for (ThirdPartVoucherRepayDetail repayDetail : detailModel.getRepayDetailList()) {
                Integer currentPeriod = repayDetail.getCurrentPeriod();
                if (currentPeriod != null && currentPeriod != 0 && currentPeriodList.contains(currentPeriod)) {
                    return false;
                }
                currentPeriodList.add(currentPeriod);
            }
        }
        return true;
    }
}
