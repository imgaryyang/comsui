package com.suidifu.munichre;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.munichre.config.MqProducer;
import com.suidifu.munichre.util.AbsUtils;
import com.zufangbao.canal.core.rowprocesser.event.AbstractRowEvents;
import com.zufangbao.gluon.opensdk.DateUtils;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("repayment_plan_extra_data")
public class RepaymentPlanExtraData extends AbstractRowEvents {
    private static final Logger LOG = LoggerFactory.getLogger(RemittancePlanExecLog.class);

    @Resource(name = "mqMunichreProducer")
    private MqProducer producer;

    @Value("${tencent.abs.record.bean}")
    private String absBean;

    @Value("${tencent.abs.record.trail.method}")
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
        onChange(afterColumnsList, Boolean.TRUE, Boolean.FALSE);
    }

    @Override
    public void onUpdate(List<Column> beforeColumnsList, List<Column> afterColumnsList) {
        onChange(afterColumnsList, Boolean.FALSE, Boolean.TRUE);
    }

    private void onChange(List<Column> afterColumnsList, boolean insert, boolean update) {
        String columnName;
        String columnValue;

        String assetSetUuid = "";
        String contractUuid = "";
        String lastModifiedTime = "";
        String reasonCode = "";
        String financialContractUuid = null;

        Request request = newRequest();
        for (Column column : afterColumnsList) {
            columnName = column.getName();
            columnValue = column.getValue();
            if ("asset_set_uuid".equalsIgnoreCase(columnName)) {
                assetSetUuid = columnValue;
            }
            if ("last_modify_time".equalsIgnoreCase(columnName)) {
                Date date = DateUtils.parseDate(columnValue);
                lastModifiedTime = DateUtils.format(date, "yyyy-MM-dd HH:mm:ss");
            }
            if ("reason_code".equalsIgnoreCase(columnName)) {
                reasonCode = columnValue;
            }
            if ("contract_uuid".equalsIgnoreCase(columnName)) {
                contractUuid = columnValue;
            }
        }
        if (insert && "0".equals(reasonCode)) {
            // 提前结清时记录变更ABS文件
            if (AbsUtils.notAbsFinancialContract(fcUuidList, financialContractUuid)) {
                return;
            }
            sendMessage(request, assetSetUuid, contractUuid, lastModifiedTime);
            return;
        }
    }

    @Override
    public void onDelete(List<Column> beforeColumnsList) {

    }

    private void sendMessage(Request request, String assetSetUuid, String contractUuid, String lastModifiedTime) {
        Object[] params = new Object[5];
        params[0] = Integer.valueOf(fileType);
        params[1] = assetSetUuid;
        params[2] = contractUuid;
        params[3] = fcUuidList.split(",");
        params[4] = lastModifiedTime;
        request.setParams(params);
        producer.rpc().sendAsync(request);
        LOG.info("RepaymentPlanExtraData is changed assetSetUuid [" + assetSetUuid+ "]");
    }

    private Request newRequest() {
        Request request = new Request();
        request.setBean(absBean);
        request.setMethod(absMethod);
        return request;
    }
}
