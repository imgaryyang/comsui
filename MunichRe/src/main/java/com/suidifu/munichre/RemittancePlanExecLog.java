package com.suidifu.munichre;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.munichre.config.MqProducer;
import com.suidifu.munichre.util.AbsUtils;
import com.zufangbao.canal.core.rowprocesser.event.AbstractRowEvents;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("t_remittance_plan_exec_log")
public class RemittancePlanExecLog extends AbstractRowEvents {
    private static final Logger LOG = LoggerFactory.getLogger(RemittancePlanExecLog.class);

    @Resource(name = "mqMunichreProducer")
    private MqProducer producer;

    @Value("${journal.voucher.record.bean}")
    private String bean;

    @Value("${journal.voucher.record.remittance.method}")
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

    @Value("${tencent.abs.record.fundsRemittance.method}")
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

    @Override
    public void onInsert(List<Column> afterColumnsList) {
        onChange(afterColumnsList, Boolean.TRUE, Boolean.FALSE, -1);
    }

    @Override
    public void onUpdate(List<Column> beforeColumnsList, List<Column> afterColumnsList) {
        String columnName = null;
        String columnValue = null;
        int before_type = -1;
        for (Column column : beforeColumnsList) {
            columnName = column.getName();
            columnValue = column.getValue();
            if ("execution_status".equalsIgnoreCase(columnName))
                before_type = getInteger(columnValue);
        }
        onChange(afterColumnsList, Boolean.FALSE, Boolean.TRUE, before_type);
    }

    @Override
    public void onDelete(List<Column> beforeColumnsList) {

    }

    private void onChange(List<Column> afterColumnsList, boolean insert, boolean update, int before_type) {
        String columnName = null;
        String columnValue = null;
        int after_type = -1;
        int reverse_status = -1;
        boolean update_status = Boolean.FALSE;
        Request request = newRequest();
        Request absRequest = newAbsRequest();
        String financial_contract_uuid = null;
        for (Column column : afterColumnsList) {
            columnName = column.getName();
            columnValue = column.getValue();
            if ("exec_req_no".equalsIgnoreCase(columnName)) {
                request.setUuid(columnValue);
                absRequest.setUuid(columnValue);
            }
            if ("execution_status".equalsIgnoreCase(columnName)) {
                after_type = getInteger(columnValue);
            }
            if ("reverse_status".equalsIgnoreCase(columnName)) {
                reverse_status = getInteger(columnValue);
            }
            if ("financial_contract_uuid".equalsIgnoreCase(columnName)) {
                financial_contract_uuid = columnValue;
            }
        }
        // 交易状态限制为：2成功/3失败/5撤销
        if (after_type != 2 && after_type != 5 && after_type != 3) {
            return;
        }
        if (StringUtils.isEmpty(request.getUuid()))
            return;
        if (after_type == 2) {
            sendMessage(request);
            if (AbsUtils.notAbsFinancialContract(fcUuidList, financial_contract_uuid)) {
                return;
            }
            sendAbsMessage(absRequest);
            return;
        } else {
            if (reverse_status == 2 || reverse_status == 3) {
                sendMessage(request);
                return;
            }
        }
        if (insert || update_status) {
            sendMessage(request);
            return;
        }
    }

    private void sendMessage(Request request) {
        Object[] params = new Object[4];
        params[0] = request.getUuid();
        params[1] = path;
        params[2] = "1".equals(useSubDir);
        params[3] = true;
        request.setParams(params);
        producer.rpc().sendAsync(request);
        LOG.info("RemittancePlanExecLog is changed reqNo[" + request.getUuid() + "]");
    }

    private void sendAbsMessage(Request request) {
        Object[] params = new Object[6];
        params[0] = request.getUuid();
        params[1] = absPath;
        params[2] = true;
        params[3] = projectNo;
        params[4] = agencyId;
        params[5] = fcUuidList.split(",");
        request.setParams(params);
        producer.rpc().sendAsync(request);
        LOG.info("RemittancePlanExecLog is changed, sendAbsMessage. reqNo [" + request.getUuid() + "]");
    }

    private Request newRequest() {
        Request request = new Request();
        request.setBean(bean);
        request.setMethod(method);
        return request;
    }

    private Request newAbsRequest() {
        Request request = new Request();
        request.setBean(absBean);
        request.setMethod(absMethod);
        return request;
    }
}
