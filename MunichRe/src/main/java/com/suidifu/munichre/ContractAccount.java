package com.suidifu.munichre;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.munichre.config.MqProducer;
import com.zufangbao.canal.core.rowprocesser.event.AbstractRowEvents;
import com.zufangbao.gluon.opensdk.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component("contract_account")
public class ContractAccount extends AbstractRowEvents {
    private static final Logger LOG = LoggerFactory.getLogger(RemittancePlanExecLog.class);

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

    @Override
    public void onInsert(List<Column> afterColumnsList) {
    }

    @Override
    public void onUpdate(List<Column> beforeColumnsList, List<Column> afterColumnsList) {
        String columnName = null;
        String columnValue = null;
        Date src_thruDate = null;
        Date tar_thruDate = null;
        String contractId = "";
        for (Column column : beforeColumnsList) {
            columnName = column.getName();
            columnValue = column.getValue();
            if ("thru_date".equalsIgnoreCase(columnName)) {
                src_thruDate = DateUtils.parseDate(columnValue);
            }
        }
        for (Column column : afterColumnsList) {
            columnName = column.getName();
            columnValue = column.getValue();
            if ("thru_date".equalsIgnoreCase(columnName)) {
                tar_thruDate = DateUtils.parseDate(columnValue);
            }
            if ("contract_id".equalsIgnoreCase(columnName)) {
                contractId = columnValue;
            }
        }

        Request request = newRequest();
        request.setUuid(contractId);
        if (null != tar_thruDate && tar_thruDate.before(src_thruDate)) {
            sendMessage(request);
            return;
        }

        sendMessage(request);
        return;
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
        LOG.info("ContractAccount is changed contractId [" + request.getUuid() + "]");
    }

    private Request newRequest() {
        Request request = new Request();
        request.setBean(bean);
        request.setMethod(method);
        return request;
    }
}
