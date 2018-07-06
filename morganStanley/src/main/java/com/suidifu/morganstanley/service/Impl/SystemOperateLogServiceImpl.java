package com.suidifu.morganstanley.service.Impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.morganstanley.model.request.MutableFee;
import com.suidifu.morganstanley.service.SystemOperateLogService;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeDetail;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeReasonCode;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hwr on 17-10-23.
 */
@Service("SystemOperateLogV2Service")
public class SystemOperateLogServiceImpl extends
        GenericServiceImpl<SystemOperateLog> implements SystemOperateLogService {
    @Override
    public void saveMutableFeeSystemLog(String assetUuid, MutableFee mutableFee, String ip, Long userId) {
        StringBuffer recordContent = new StringBuffer();
        recordContent.append("浮动费用变更：还款计划编号：").append(mutableFee.getRepaymentPlanNo()).append(";");
        MutableFeeReasonCode reason = MutableFeeReasonCode.fromValue(Integer.valueOf(mutableFee.getReasonCode()));
        String reasonMsg = reason.getChineseMessage() + ";";
        recordContent.append("变更原因：").append(reasonMsg);
        List<MutableFeeDetail> mutableFeeDetails = mutableFee.getDetailList();
        for (MutableFeeDetail detail : mutableFeeDetails) {
            String detailStr = detail.getMutableFeeType().getMsg() + "：" + detail.getAmount() + ";";
            recordContent.append(detailStr);
        }
        String keyContext = (null == mutableFee.getUniqueId() ? mutableFee.getContractNo()
                : mutableFee.getUniqueId());
        SystemOperateLog log = SystemOperateLog.createLog(userId, recordContent.toString(), ip,
                LogFunctionType.MUTABLE_FEE, LogOperateType.UPDATE, assetUuid, keyContext);
        log.setRecordContentDetail(mutableFee.toString());

        this.save(log);
    }
}
