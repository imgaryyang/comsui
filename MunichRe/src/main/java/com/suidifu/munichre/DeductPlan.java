package com.suidifu.munichre;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.munichre.StaticsConfig.TransactionCommandExecutionStatus;
import com.suidifu.munichre.config.MqProducer;
import com.suidifu.munichre.model.ModifyModel;
import com.suidifu.munichre.service.DeductPlanStatCacheService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 财务凭证表监控
 *
 * @author lisf
 */
@Component("t_deduct_plan")
public class DeductPlan extends AbstractRowEvents {
    private static final Logger LOG = LoggerFactory.getLogger(DeductPlan.class);

    @Resource(name = "mqMunichreProducer")
    private MqProducer producer;

    @Value("${journal.voucher.type}")
    private String type;

    @Value("${journal.voucher.consumer.bean}")
    private String bean;

    @Value("${journal.voucher.consumer.method}")
    private String method;

    @Value("${tencent.abs.record.bean}")
    private String absBean;

    @Value("${tencent.abs.record.fundsDeduct.method}")
    private String absMethod;

    @Value("${tencent.abs.record.path}")
    private String absPath;


    @Value("${tencent.abs.record.absFileType.change}")
    private String fileType;
    @Value("${tencent.abs.record.projectNo}")
    private String projectNo;
    @Value("${tencent.abs.record.agencyId}")
    private String agencyId;
    @Value("${tencent.abs.record.fcNoList:null}")
    private String fcUuidList;
    @Autowired
    private DeductPlanStatCacheService deductPlanStatCacheService;

    @Override
    public void onInsert(List<Column> afterColumnsList) {
        try {
            LOG.info("------------------------ run in onInsert method ----------------------");
            onChange(afterColumnsList, Boolean.TRUE, Boolean.FALSE, -1, null);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("#deduct plan stat cache insert error# occur error with exception stack trace[" + ExceptionUtils.getFullStackTrace(e) + "]");
        }
    }

    @Override
    public void onUpdate(List<Column> beforeColumnsList, List<Column> afterColumnsList) {
        LOG.info("------------------------ run in onUpdate method ----------------------");
        try {
            String columnName;
            String columnValue;
            Date beforeCompleteTime = null;
            int beforeStatus = -1;
            for (Column column : beforeColumnsList) {
                columnName = column.getName();
                columnValue = column.getValue();
                if ("execution_status".equalsIgnoreCase(columnName)) {
                    beforeStatus = getInteger(columnValue);
                }
                if ("complete_time".equalsIgnoreCase(columnName)) {
                    beforeCompleteTime = DateUtils.parseDate(columnValue);
                }
            }
            onChange(afterColumnsList, Boolean.FALSE, Boolean.TRUE, beforeStatus, beforeCompleteTime);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("#deduct plan stat cache update error# occur error with exception stack trace[" + ExceptionUtils.getFullStackTrace(e) + "]");
        }
    }

    @Override
    public void onDelete(List<Column> beforeColumnsList) {

    }

    private void onChange(List<Column> afterColumnsList, boolean insert, boolean update, int before_status, Date before_complete_time) {
        LOG.info("------------------------ run in onChange method 11111----------------------");
        String columnName;
        String columnValue;
        String financial_contract_uuid = null;
        String payment_channel_uuid = null;
        Integer payment_gateway = null;
        String pg_account = null;
        String pg_clearing_account = null;
        Date after_complete_time = null;
        Integer after_execution_status = null;
        String deduct_plan_uuid = null;
        BigDecimal planTotalAmount = null;
        String deduct_application_uuid = null;
        BigDecimal actualTotalAmount = null;
        Long deductPlanId = -1L;
        boolean update_status = Boolean.FALSE;
        for (Column column : afterColumnsList) {
            columnName = column.getName();
            columnValue = column.getValue();
            if ("financial_contract_uuid".equalsIgnoreCase(columnName)) {
                financial_contract_uuid = columnValue;
            }
            if ("id".equalsIgnoreCase(columnName)) {
                deductPlanId = Long.valueOf(columnValue);
            }
            if ("payment_channel_uuid".equalsIgnoreCase(columnName)) {
                payment_channel_uuid = columnValue;
            }
            if ("payment_gateway".equalsIgnoreCase(columnName)) {
                payment_gateway = getInteger(columnValue);
            }
            if ("pg_account".equalsIgnoreCase(columnName)) {
                pg_account = columnValue;
            }
            if ("pg_clearing_account".equalsIgnoreCase(columnName)) {
                pg_clearing_account = columnValue;
            }
            if ("complete_time".equalsIgnoreCase(columnName)) {
                after_complete_time = DateUtils.parseDate(columnValue);
            }
            if ("planned_total_amount".equalsIgnoreCase(columnName)) {
                planTotalAmount = getBigDecimal(columnValue);
            }
            if ("execution_status".equalsIgnoreCase(columnName)) {
                after_execution_status = getInteger(columnValue);
                if (column.getUpdated()) {
                    update_status = Boolean.TRUE;
                }
            }
            if ("deduct_plan_uuid".equalsIgnoreCase(columnName)) {
                deduct_plan_uuid = columnValue;
            }
            if ("deduct_application_uuid".equalsIgnoreCase(columnName)) {
                deduct_application_uuid = columnValue;
            }
            if ("actual_total_amount".equalsIgnoreCase(columnName)) {
                actualTotalAmount = getBigDecimal(columnValue);
            }
        }
        if (!update_status && !insert) {
            return;
        }
        if (after_execution_status != TransactionCommandExecutionStatus.SUCCESS.getCode() &&
                after_execution_status != TransactionCommandExecutionStatus.FAIL.getCode()) {
            return;
        }

        if (after_execution_status == TransactionCommandExecutionStatus.SUCCESS.getCode()) {
            if (AbsUtils.notAbsFinancialContract(fcUuidList, financial_contract_uuid)) {
                return;
            }
            Request absRequest = newAbsRequest();
            sendAbsMessage(absRequest, deductPlanId);
        }

        LOG.info("------------------------ run in onChange method 22222----------------------");
        ActualPaymentDetail detail = deductPlanStatCacheService.getDetail(deduct_application_uuid);
        if (detail != null) {
            detail.setActualRepaymentAmount(actualTotalAmount);
        }
        ModifyModel before_modify_model = new ModifyModel(before_complete_time);
        if (before_status == TransactionCommandExecutionStatus.FAIL.getCode()) {
            before_modify_model.before_fail();
        } else if (before_status == TransactionCommandExecutionStatus.SUCCESS.getCode()) {
            before_modify_model.before_succ(planTotalAmount);
        }
        ModifyModel after_modify_model = new ModifyModel(after_complete_time);
        if (after_execution_status == TransactionCommandExecutionStatus.SUCCESS.getCode()) {
            after_modify_model.after_succ(planTotalAmount);
        } else if (after_execution_status == TransactionCommandExecutionStatus.FAIL.getCode()) {
            after_modify_model.after_fail();
        }
        //after
        if (before_modify_model.to_save()) {
            LOG.info("------------------------ run in onChange method before model save----------------------");
            saveOrUpdate(before_modify_model, detail, deduct_plan_uuid, financial_contract_uuid, payment_channel_uuid, pg_clearing_account, payment_gateway, pg_account);
        }
        if (after_modify_model.to_save()) {
            LOG.info("------------------------ run in onChange method after model save----------------------");
            saveOrUpdate(after_modify_model, detail, deduct_plan_uuid, financial_contract_uuid, payment_channel_uuid, pg_clearing_account, payment_gateway, pg_account);
        }

    }

    private void saveOrUpdate(ModifyModel modifyModel, ActualPaymentDetail detail, String deduct_plan_uuid, String financial_contract_uuid, String payment_channel_uuid, String pg_clearing_account, Integer payment_gateway, String pg_account) {
        LOG.info("deduct_plan_uuid:" + deduct_plan_uuid);
        String deductPlanStatCacheUuid = deductPlanStatCacheService.get_deduct_plan_stat_cache_interval_uuid(financial_contract_uuid, payment_channel_uuid, pg_clearing_account, modifyModel.getComplete_time());

        if (StringUtils.isEmpty(deductPlanStatCacheUuid)) {
            LOG.info("deductPlanStatCacheUuid[" + deductPlanStatCacheUuid + "],create_deduct_plan_stat_cache,financial_contract_uuid[" + financial_contract_uuid + "],payment_channel_uuid[" + payment_channel_uuid + "],"
                    + "payment_gateway[" + payment_gateway + "],pg_account[" + pg_account + "],pg_clearing_account[" + pg_clearing_account + "],"
                    + "complete_time[" + modifyModel.getComplete_time() + "],delta_suc_amount[" + modifyModel.getDelta_suc_amount() + "],delta_suc_num[" + modifyModel.getDelta_suc_num() + "],delta_fail_num[" + modifyModel.getDelta_fail_num() + "]");
            deductPlanStatCacheService.create_deduct_plan_stat_cache(financial_contract_uuid, payment_channel_uuid, payment_gateway, pg_account, pg_clearing_account, modifyModel.getComplete_time(), modifyModel.getDelta_suc_amount(), modifyModel.getDelta_suc_num(), modifyModel.getDelta_fail_num(), detail);
        } else {
            LOG.info("deductPlanStatCacheUuid[" + deductPlanStatCacheUuid + "],update_count_amount,financial_contract_uuid[" + financial_contract_uuid + "],payment_channel_uuid[" + payment_channel_uuid + "],"
                    + "payment_gateway[" + payment_gateway + "],pg_account[" + pg_account + "],pg_clearing_account[" + pg_clearing_account + "],"
                    + "complete_time[" + modifyModel.getComplete_time() + "],delta_suc_amount[" + modifyModel.getDelta_suc_amount() + "],delta_suc_num[" + modifyModel.getDelta_suc_num() + "],delta_fail_num[" + modifyModel.getDelta_fail_num() + "]");
            deductPlanStatCacheService.update_count_amount(deductPlanStatCacheUuid, modifyModel.getDelta_suc_amount(), modifyModel.getDelta_suc_num(), modifyModel.getDelta_fail_num(), detail, modifyModel.getFlag());
        }
    }

    private void sendAbsMessage(Request request, Long deductPlanId) {
        Object[] params = new Object[6];
        params[0] = deductPlanId;
        params[1] = absPath;
        params[2] = true;
        params[3] = projectNo;
        params[4] = agencyId;
        params[5] = fcUuidList.split(",");
        request.setParams(params);
        producer.rpc().sendAsync(request);
        LOG.info("DeductPlan is changed deductPlanId [" + deductPlanId + "]");
    }

    private Request newAbsRequest() {
        Request request = new Request();
        request.setBean(absBean);
        request.setMethod(absMethod);
        return request;
    }
}
