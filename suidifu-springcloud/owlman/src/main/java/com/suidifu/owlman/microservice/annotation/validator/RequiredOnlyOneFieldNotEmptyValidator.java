package com.suidifu.owlman.microservice.annotation.validator;

import com.suidifu.owlman.microservice.annotation.RequiredOnlyOneFieldNotEmpty;
import com.suidifu.owlman.microservice.model.ThirdPartVoucher;
import com.suidifu.owlman.microservice.model.ThirdPartVoucherDetail;
import com.suidifu.owlman.microservice.model.ThirdPartVoucherRepayDetail;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author wangjianhua
 */
public class RequiredOnlyOneFieldNotEmptyValidator implements ConstraintValidator<RequiredOnlyOneFieldNotEmpty, ThirdPartVoucher> {
    @Override
    public void initialize(RequiredOnlyOneFieldNotEmpty requiredOnlyOneFieldNotEmpty) {
        //just to override
    }

    @Override
    public boolean isValid(ThirdPartVoucher voucher,
                           ConstraintValidatorContext constraintValidatorContext) {

        List<ThirdPartVoucherDetail> details = JsonUtils.parseArray(voucher.getDetailList(), ThirdPartVoucherDetail.class);
        if (CollectionUtils.isEmpty(details)) {
            return true;
        }
        for (ThirdPartVoucherDetail detailModel : details) {
            if (CollectionUtils.isEmpty(detailModel.getRepayDetailList())) {
                return true;
            }
            for (ThirdPartVoucherRepayDetail repayDetailModel : detailModel.getRepayDetailList()) {
                if (StringUtils.isEmpty(repayDetailModel.getRepayScheduleNo())
                        && StringUtils.isEmpty(repayDetailModel.getRepaymentPlanNo())
                        && (repayDetailModel.getCurrentPeriod() == null)) {
                    return false;
                }
            }
        }
        return true;
    }
}

