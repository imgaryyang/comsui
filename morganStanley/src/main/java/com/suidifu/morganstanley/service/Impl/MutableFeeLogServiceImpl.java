package com.suidifu.morganstanley.service.Impl;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.morganstanley.model.request.MutableFee;
import com.suidifu.morganstanley.service.MutableFeeLogService;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.entity.log.MutableFeeLog;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.REPEAT_REQUEST_NO;

/**
 * Created by hwr on 17-10-23.
 */
@Service("mutableFeeLogV2Service")
public class MutableFeeLogServiceImpl extends GenericServiceImpl<MutableFeeLog> implements MutableFeeLogService{
    @Override
    public void checkByRequestNo(String requestNo) throws ApiException {
        Filter filter = new Filter();
        filter.addEquals("requestNo", requestNo);
        List<MutableFeeLog> result = this.list(MutableFeeLog.class, filter);
        if(CollectionUtils.isNotEmpty(result)) {
            throw new ApiException(REPEAT_REQUEST_NO);
        }
    }

    @Override
    public void saveMutableFeeLog(MutableFee mutableFee, String ip, String resultMsg) {
        MutableFeeLog log = new MutableFeeLog();
        log.setIp(ip);
        log.setResultMsg(resultMsg);
        log.setRequestNo(mutableFee.getRequestNo());
        log.setReasonCode(mutableFee.getReasonCode());
        log.setFinancialProductCode(mutableFee.getFinancialProductCode());
        log.setDetails(mutableFee.getDetails());
        log.setRepaymentPlanNo(mutableFee.getRepaymentPlanNo());
        log.setContractUniqueId(StringUtils.isEmpty(mutableFee.getUniqueId()) ? mutableFee.getContractNo()
                : mutableFee.getUniqueId());
        log.setApprover(mutableFee.getApprover());
        String apprTime = StringUtils.isEmpty(mutableFee.getApprovedTime()) ? ""
                : mutableFee.getApprovedTime();
        log.setApprovedTime(DateUtils.parseDate(apprTime, "yyyy-MM-dd"));
        log.setComment(mutableFee.getComment());
        log.setCreateTime(new Date());
        this.save(log);
    }
}
