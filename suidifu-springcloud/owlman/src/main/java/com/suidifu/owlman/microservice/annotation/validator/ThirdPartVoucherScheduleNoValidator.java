package com.suidifu.owlman.microservice.annotation.validator;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.owlman.microservice.annotation.RepeatedRepayScheduleNo;
import com.suidifu.owlman.microservice.model.ThirdPartVoucher;
import com.suidifu.owlman.microservice.model.ThirdPartVoucherDetail;
import com.suidifu.owlman.microservice.model.ThirdPartVoucherRepayDetail;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjianhua
 */
public class ThirdPartVoucherScheduleNoValidator implements ConstraintValidator<RepeatedRepayScheduleNo, ThirdPartVoucher> {
    @Override
    public void initialize(RepeatedRepayScheduleNo constraintAnnotation) {
        //just to override
    }

    @Override
    public boolean isValid(ThirdPartVoucher voucher,
                           ConstraintValidatorContext context) {
        List<String> repayScheduleNoList = new ArrayList<>();
        List<ThirdPartVoucherDetail> details = JsonUtils.parseArray(voucher.getDetailList(), ThirdPartVoucherDetail.class);
        if (CollectionUtils.isEmpty(details)) {
            return true;
        }
        for (ThirdPartVoucherDetail detailModel : details) {
            if (CollectionUtils.isEmpty(detailModel.getRepayDetailList())) {
                return true;
            }
            for (ThirdPartVoucherRepayDetail repayDetail : detailModel.getRepayDetailList()) {
                String repayScheduleNo = repayDetail.getRepayScheduleNo();
                if (!StringUtils.isEmpty(repayScheduleNo)) {
                    if (repayScheduleNoList.contains(repayScheduleNo)) {
                        return false;
                    }
                    repayScheduleNoList.add(repayScheduleNo);
                }
            }
        }
        return true;
    }
}
