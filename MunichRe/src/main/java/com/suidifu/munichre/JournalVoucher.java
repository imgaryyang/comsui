package com.suidifu.munichre;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.munichre.config.MqProducer;
import com.zufangbao.canal.core.rowprocesser.event.AbstractRowEvents;

/**
 * 财务凭证表监控
 *
 * @author lisf
 */
@Component("journal_voucher")
public class JournalVoucher extends AbstractRowEvents {
    private static final Logger LOG = LoggerFactory.getLogger(JournalVoucher.class);

    @Resource(name = "mqMunichreProducer")
    private MqProducer producer;

    @Value("${journal.voucher.type}")
    private String type;

    @Value("${journal.voucher.consumer.bean}")
    private String bean;

    @Value("${journal.voucher.consumer.method}")
    private String method;
    // change start
    @Value("${journal.voucher.record.bean}")
    private String recordBean;

    @Value("${journal.voucher.record.repayment.method}")
    private String recordMethod;

    @Value("${journal.voucher.record.repurchase.method}")
    private String repurchaseRecord;

    @Value("${journal.voucher.record.path}")
    private String path;

    /**
     * 是否使用子目录保存 1：使用 其它：不使用
     */
    @Value("${journal.voucher.record.subDir}")
    private String useSubDir;
    // change end

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
            if ("journal_voucher_type".equalsIgnoreCase(columnName))
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
        int status = -1;
        int after_type = -1;
        boolean update_status = Boolean.FALSE;
        String jvUuid = "";
        Request request = newRequest();
        for (Column column : afterColumnsList) {
            columnName = column.getName();
            columnValue = column.getValue();
            if ("journal_voucher_uuid".equalsIgnoreCase(columnName)) {
                request.setUuid(columnValue);
                jvUuid = columnValue;
            }
            if ("status".equalsIgnoreCase(columnName)) {
                status = getInteger(columnValue);
                if (column.getUpdated())
                    update_status = Boolean.TRUE;
            }
            if ("journal_voucher_type".equalsIgnoreCase(columnName))
                after_type = getInteger(columnValue);
        }
        if (StringUtils.isEmpty(request.getUuid()) || status != 1 || !indexOf(after_type))
            return;
        if (before_type == 3 && after_type == 9) {
            sendMessage(request);
            return;
        }
        if (insert || update_status) {
            sendMessage(request);
            // change start
            if (after_type == 10) {// JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE
                sendDairyRepurchaseRecordMessage(jvUuid);
            } else {
                sendDairyRecordMessage(jvUuid);
            }
            // change end
            return;
        }
    }

    // change start
    private void sendDairyRecordMessage(String jvUuid) {
        Request recordRequest = new Request();
        recordRequest.setBean(recordBean);
        recordRequest.setMethod(recordMethod);
        Object[] params = new Object[4];
        params[0] = jvUuid;
        params[1] = path;
        params[2] = "1".equals(useSubDir);
        params[3] = true;
        recordRequest.setParams(params);
        producer.rpc().sendAsync(recordRequest);
        LOG.info("JournalVoucher is changed uuid[" + jvUuid + "]");
    }
    // change end

    // 回购记录对账文件
    private void sendDairyRepurchaseRecordMessage(String jvUuid) {
        Request repurchaseRecordRequest = new Request();
        repurchaseRecordRequest.setBean(recordBean);
        repurchaseRecordRequest.setMethod(repurchaseRecord);
        boolean isSubDir = "1".equals(useSubDir);
        Object[] params = new Object[4];
        params[0] = jvUuid;
        params[1] = path;
        params[2] = isSubDir;
        params[3] = true;
        repurchaseRecordRequest.setParams(params);
        producer.rpc().sendAsync(repurchaseRecordRequest);
        LOG.info("JournalVoucher is changed JournalVoucherUuid: [" + jvUuid + "] path: [" + path + "] isSubDir: ["
                + isSubDir + "]");
    }

    private void sendMessage(Request request) {
        List<String> list = new ArrayList<String>(1);
        list.add(request.getUuid());
        request.setParams(new Object[]{list});
        producer.rpc().sendAsync(request);
        LOG.info(request.getUuid());
    }

    private Request newRequest() {
        Request request = new Request();
        request.setBean(bean);
        request.setMethod(method);
        return request;
    }

    private boolean indexOf(int _type) {
        return type.indexOf("," + _type + ",") >= 0;
    }
}
