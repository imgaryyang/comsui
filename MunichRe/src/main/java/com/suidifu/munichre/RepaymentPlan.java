package com.suidifu.munichre;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.demo2do.core.utils.StringUtils;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.munichre.config.MqProducer;
import com.suidifu.munichre.model.RepaymentPlanModel;
import com.suidifu.munichre.model.TencentAbsTrailModel;
import com.suidifu.munichre.util.AbsUtils;
import com.zufangbao.canal.core.rowprocesser.event.AbstractRowEvents;
import com.zufangbao.gluon.opensdk.DateUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("asset_set")
public class RepaymentPlan extends AbstractRowEvents {

    private static final Logger LOG = LoggerFactory.getLogger(RepaymentPlan.class);

    @Resource(name = "mqMunichreProducer")
    private MqProducer producer;

    @Value("${journal.voucher.record.bean}")
    private String bean;

    @Value("${journal.voucher.record.assetSetChangeRecord.method}")
    private String method;

    @Value("${journal.voucher.record.path}")
    private String path;

    /**
     * 是否使用子目录保存 1：使用 其它：不使用
     */
    @Value("${journal.voucher.record.subDir}")
    private String useSubDir;


    @Value("${tencent.abs.record.bean}")
    private String absBean;

    @Value("${tencent.abs.record.trail.method}")
    private String absMethod;

    @Value("${tencent.abs.record.path}")
    private String absPath;


    @Value("${tencent.abs.record.absFileType.change}")
    private String fileChangeType;
    @Value("${tencent.abs.record.absFileType.repayment}")
    private String fileRepayType;
    @Value("${tencent.abs.record.projectNo}")
    private String projectNo;
    @Value("${tencent.abs.record.agencyId}")
    private String agencyId;
    @Value("${tencent.abs.record.fcNoList:null}")
    private String fcUuidList;

    @Override
    public void onInsert(List<Column> afterColumnsList) {
        String columnName;
        String columnValue;
        int versionNo = -1;
        String requestUuid = "";
        for (Column column : afterColumnsList) {
            columnName = column.getName();
            columnValue = column.getValue();
            if ("version_no".equalsIgnoreCase(columnName)) {
                versionNo = getInteger(columnValue);
            }
            if ("contract_id".equalsIgnoreCase(columnName)) {
                requestUuid = columnValue;
            }
        }
        if (versionNo == 1) {
            return;
        }
        Request request = initRpcRequest(bean, method);
        request.setUuid(requestUuid);
        sendMessage(request);
    }

    @Override
    public void onUpdate(List<Column> beforeColumnsList, List<Column> afterColumnsList) {
        try {
            RepaymentPlanModel beforeModel = buildRepaymentPlanModel(beforeColumnsList);
            RepaymentPlanModel afterModel = buildRepaymentPlanModel(afterColumnsList);

            dailyAsset(beforeModel, afterModel);
            absAssetFile(beforeModel, afterModel);
        } catch (Exception e) {
            LOG.error("RepaymentPlan onUpdate occur error" + ExceptionUtils.getFullStackTrace(e));
        }
    }

    private RepaymentPlanModel buildRepaymentPlanModel(List<Column> columnList) {
        RepaymentPlanModel afterModel = new RepaymentPlanModel();
        String columnName;
        String columnValue;
        for (Column column : columnList) {
            columnName = column.getName();
            columnValue = column.getValue();
            if ("overdue_status".equalsIgnoreCase(columnName)) {
                afterModel.setOverdueStatus(getInteger(columnValue));
            }
            if ("executing_status".equalsIgnoreCase(columnName)) {
                afterModel.setExecutingStatus(getInteger(columnValue));
            }
            if ("asset_fair_value".equalsIgnoreCase(columnName)) {
                afterModel.setAssetFairValue(getBigDecimal(columnValue));
            }
            if ("active_status".equalsIgnoreCase(columnName)) {
                afterModel.setActiveStatus(getInteger(columnValue));
            }
            if ("asset_status".equalsIgnoreCase(columnName)) {
                afterModel.setAssetStatus(getInteger(columnValue));
            }
            if ("contract_id".equalsIgnoreCase(columnName)) {
                afterModel.setContractId(columnValue);
            }
            if ("version_no".equalsIgnoreCase(columnName)) {
                afterModel.setVersionNo(getInteger(columnValue));
            }
            if ("asset_uuid".equalsIgnoreCase(columnName)) {
                afterModel.setAssetSetUuid(columnValue);
            }
            if ("last_modified_time".equalsIgnoreCase(columnName)) {
                Date date = DateUtils.parseDate(columnValue);
                afterModel.setLastModifiedTime(DateUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
            }
            if ("contract_uuid".equalsIgnoreCase(columnName)) {
                afterModel.setContractUuid(columnValue);
            }
            if ("on_account_status".equalsIgnoreCase(columnName)) {
                afterModel.setOnAccountStatus(getInteger(columnValue));
            }
            if ("financial_contract_uuid".equalsIgnoreCase(columnName)) {
                afterModel.setFinancialContractUuid(columnValue);
            }
        }
        return afterModel;
    }

    private void absAssetFile(RepaymentPlanModel beforeModel, RepaymentPlanModel afterModel) {
        String financialContractUuid = afterModel.getFinancialContractUuid();
        if (AbsUtils.notAbsFinancialContract(fcUuidList, financialContractUuid)) {
            return;
        }

        int tarOnAccountStatus = afterModel.getOnAccountStatus();
        if (tarOnAccountStatus == 2 || tarOnAccountStatus == 3) {
            // 有还款（发生实收）时记录还款ABS
            TencentAbsTrailModel tencentAbsTrail = afterModel.convert2TencentAbsTrail(fileRepayType);
            sendAbsMessage(tencentAbsTrail);
        }
        int assetStatus = afterModel.getAssetStatus();
        BigDecimal tarAssetFairValue = afterModel.getAssetFairValue();
        int tarVersionNo = afterModel.getVersionNo();

        if (afterModel.getOverdueStatus() == 2 && assetStatus == 1) {
            // 逾期结清时记录合同变更ABS
            TencentAbsTrailModel tencentAbsTrail = afterModel.convert2TencentAbsTrail(fileChangeType);
            sendAbsMessage(tencentAbsTrail);
        } else if (null != tarAssetFairValue && tarAssetFairValue.compareTo(beforeModel.getAssetFairValue()) != 0) {
            // 利息变化（公允值变化）时记录合同变更ABS
            TencentAbsTrailModel tencentAbsTrail = afterModel.convert2TencentAbsTrail(fileChangeType);
            sendAbsMessage(tencentAbsTrail);
        } else if (tarVersionNo != 0) {
            // 还款计划变化时记录合同变更ABS
            TencentAbsTrailModel tencentAbsTrail = afterModel.convert2TencentAbsTrail(fileChangeType);
            sendAbsMessage(tencentAbsTrail);
        }
    }

    private void dailyAsset(RepaymentPlanModel beforeModel, RepaymentPlanModel afterModel) {
        int srcExecutingStatus = beforeModel.getExecutingStatus();
        int tarExecutingStatus = afterModel.getExecutingStatus();
        Request request = initRpcRequest(bean, method);
        request.setUuid(afterModel.getContractId());
        if (afterModel.getActiveStatus() == 0) {
            if (srcExecutingStatus != afterModel.getExecutingStatus()) {
                sendMessage(request);
                return;
            }
            if (beforeModel.getOverdueStatus() != afterModel.getOverdueStatus()) {
                sendMessage(request);
                return;
            }
            if (beforeModel.getAssetFairValue().compareTo(afterModel.getAssetFairValue()) != 0) {
                sendMessage(request);
                return;
            }
            if (beforeModel.getOnAccountStatus() == 2 || afterModel.getOnAccountStatus() == 3) {
                sendMessage(request);
                return;
            }
        }
        if (tarExecutingStatus != 3 && tarExecutingStatus != 4 && tarExecutingStatus != 5) {
            return;
        }
        if (srcExecutingStatus != tarExecutingStatus) {
            sendMessage(request);
        }
    }

    @Override
    public void onDelete(List<Column> beforeColumnsList) {

    }

    private void sendMessage(Request request) {
        Object[] params = new Object[4];
        params[0] = request.getUuid();
        params[1] = path;
        params[2] = "1".equals(useSubDir);
        params[3] = true;
        request.setParams(params);
        producer.rpc().sendAsync(request);
        LOG.info("RepaymentPlan is changed contractId [" + request.getUuid() + "]");
    }

    private Request initRpcRequest(String beanName, String methodName) {
        if (StringUtils.isEmpty(beanName) || StringUtils.isEmpty(methodName)) {
            String errorMsg = "beanName or methodName is empty, beanName:" + beanName + ", methodName:" + methodName;
            LOG.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        Request request = new Request();
        request.setBean(beanName);
        request.setMethod(methodName);
        return request;
    }

    private void sendAbsMessage(TencentAbsTrailModel tencentAbsTrail) {
        Request request = initRpcRequest(absBean, absMethod);
        Object[] params = tencentAbsTrail.convert2Params(fcUuidList);
        request.setParams(params);
        producer.rpc().sendAsync(request);
        LOG.info("RepaymentPlan is changed, send TencentAbsTrailModel:" + tencentAbsTrail.toString());
    }

}
