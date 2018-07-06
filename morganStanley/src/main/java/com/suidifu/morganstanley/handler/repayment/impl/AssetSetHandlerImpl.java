package com.suidifu.morganstanley.handler.repayment.impl;

import com.suidifu.hathaway.job.Priority;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.morganstanley.configuration.bean.weifang.WeiFangProperties;
import com.suidifu.morganstanley.handler.repayment.AssetSetHandler;
import com.suidifu.morganstanley.model.request.repayment.CheckModifyRepaymentPlan;
import com.suidifu.morganstanley.model.request.repayment.ModifyRepaymentPlan;
import com.suidifu.morganstanley.utils.JSONUtils;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.sun.api.model.modify.RepaymentPlanModifyModel;
import com.zufangbao.sun.api.model.repayment.RepaymentPlanModifyDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.log.UpdateAssetLog;
import com.zufangbao.sun.service.UpdateAssetLogService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyReason;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentPlanHandlerV2_0;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.ONLY_ONE_ASSETSET_WHEN_PRE_CLEAR;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/16 <br>
 * Time:下午9:37 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@Component("assetSetHandler")
@Log4j2
public class AssetSetHandlerImpl implements AssetSetHandler {
    @Resource
    private UpdateAssetLogService updateAssetLogService;
    @Resource
    private RepaymentPlanHandlerV2_0 repaymentPlanHandlerV2_0;
    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Autowired
    private WeiFangProperties weiFangProperties;

    @Autowired
    private ProductCategoryCacheHandler productCategoryCacheHandler;

    @Override
    public List<RepaymentPlanModifyDetail> modifyRepaymentPlan(Contract contract,
                                                               Integer oldActiveVersionNo,
                                                               ModifyRepaymentPlan model,
                                                               String ip) {
        // 检验请求的编号是否唯一
        updateAssetLogService.checkByRequestNo(model.getRequestNo());
        List<RepaymentPlanModifyRequestDataModel> requestDataList = JSONUtils.getDetailModel(model.getRequestData(), RepaymentPlanModifyRequestDataModel.class);

        Integer modifyReasonCode = model.getRepaymentPlanModifyReasonCode();

        RepaymentPlanModifyReason modifyReason = modifyReasonCode == null ? null : RepaymentPlanModifyReason.fromValue(modifyReasonCode);

        //检验变更原因为提前结清时，未完成还款计划只能有一条
        isPreClearValid(requestDataList, modifyReason);

        CheckModifyRepaymentPlan.checkInterestAndPrincipalForPayment(model, weiFangProperties.isEnable(), sandboxDataSetHandler, productCategoryCacheHandler);

        String contractUuid = contract.getUuid();

        try {
            return repaymentPlanHandlerV2_0.modifyRepaymentPlan(requestDataList, contractUuid, Priority.High.getPriority(), oldActiveVersionNo, modifyReasonCode, ip);
        } catch (Exception e) {
            log.error("modifyRepaymentPlan has error, msg:{}", ExceptionUtils.getStackTrace(e));
            throw e;
        } finally {
            //保存更新还款计划的log
            saveUpdateAssetLog(contract, model, ip);
        }
    }

    private void saveUpdateAssetLog(Contract contract, ModifyRepaymentPlan model, String ip) {
        UpdateAssetLog updateAssetLog = new UpdateAssetLog();
        BeanUtils.copyProperties(model, updateAssetLog);
        updateAssetLog.setContract(contract);
        updateAssetLog.setIp(ip);
        updateAssetLog.setCreateTime(new Date());

        updateAssetLogService.save(updateAssetLog);
    }

    @Override
    public void hasDeductingAssetIn(List<AssetSet> canUpdateAssetList) {
        //just to over ride
    }

    @Override
    public List<RepaymentPlanModifyDetail> modifyRepaymentPlan(Contract contract, Integer oldActiveVersionNo, RepaymentPlanModifyModel modifyModel, String ip) {
        return null;
    }

    @Override
    public Integer modifyRepaymentPlanManual(Contract contract, List<RepaymentPlanModifyRequestDataModel> models, Date startDate, Integer modifyCode, String ip) {
        return null;
    }

    /**
     * 检验变更原因为提前结清时，未完成还款计划只能有一条
     */
    private void isPreClearValid(List<RepaymentPlanModifyRequestDataModel> requestDataList, RepaymentPlanModifyReason modifyReason) {
        if (modifyReason == RepaymentPlanModifyReason.REASON_1 &&
                !CollectionUtils.isEmpty(requestDataList) && requestDataList.size() > 1) {
            throw new GlobalRuntimeException(ONLY_ONE_ASSETSET_WHEN_PRE_CLEAR);
        }
    }
}