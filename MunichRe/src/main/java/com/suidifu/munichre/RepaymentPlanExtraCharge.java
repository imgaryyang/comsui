package com.suidifu.munichre;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.munichre.config.MqProducer;
import com.zufangbao.canal.core.rowprocesser.event.AbstractRowEvents;
import com.zufangbao.gluon.opensdk.DateUtils;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("asset_set_extra_charge")
public class RepaymentPlanExtraCharge extends AbstractRowEvents {
    private static final Logger LOG = LoggerFactory.getLogger(RemittancePlanExecLog.class);

    @Resource(name = "mqMunichreProducer")
    private MqProducer producer;

    @Value("${journal.voucher.record.bean}")
    private String bean;

    @Value("${journal.voucher.record.assetSetChangeRecordByAssetUuid.method}")
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
    private String fileType;
    @Value("${tencent.abs.record.projectNo}")
    private String projectNo;
    @Value("${tencent.abs.record.agencyId}")
    private String agencyId;
    @Value("${tencent.abs.record.fcNoList:null}")
    private String fcNoList;

    private static List<String> MUTABLE_FEE_TYPES = Arrays.asList("SND_UNEARNED_LOAN_ASSET_OTHER_FEE", "SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE", "SND_UNEARNED_LOAN_ASSET_TECH_FEE");
    private static List<String> OVERDUE_FEE_TYPES = Arrays.asList("SND_RECIEVABLE_LOAN_PENALTY", "TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION",
            "TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE", "TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE");

    @Override
    public void onInsert(List<Column> afterColumnsList) {
        onChange(afterColumnsList, Boolean.TRUE, Boolean.FALSE);
    }

    @Override
    public void onUpdate(List<Column> beforeColumnsList, List<Column> afterColumnsList) {
        onChange(afterColumnsList, Boolean.FALSE, Boolean.TRUE);
    }

    private void onChange(List<Column> afterColumnsList, boolean insert, boolean update) {
        String columnName = null;
        String columnValue = null;

        String accountName = "";
        String assetSetUuid = "";
        String lastModifiedTime = "";
        Request request = newRequest();
        Request absRequest = newAbsRequest();
        for (Column column : afterColumnsList) {
            columnName = column.getName();
            columnValue = column.getValue();
            if ("asset_set_uuid".equalsIgnoreCase(columnName)) {
                request.setUuid(columnValue);
                assetSetUuid = columnValue;
            }
            if ("second_account_name".equalsIgnoreCase(columnName)) {
                accountName = columnValue;
            }
            if ("last_modify_time".equalsIgnoreCase(columnName)) {
                Date date = DateUtils.parseDate(columnValue);
                lastModifiedTime = DateUtils.format(date, "yyyy-MM-dd HH:mm:ss");
            }
            if ("third_account_name".equalsIgnoreCase(columnName)) {
                accountName = columnValue;
            }
        }
        if (insert && OVERDUE_FEE_TYPES.contains(accountName)) {
            sendMessage(request);
            return;
        }
        if (update && (OVERDUE_FEE_TYPES.contains(accountName) || MUTABLE_FEE_TYPES.contains(accountName))) {
            // 费用变更时记录变更ABS文件
            sendMessage(absRequest, assetSetUuid, "", lastModifiedTime);
            sendMessage(request);
            return;
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
        LOG.info("RepaymentPlanExtraCharge is changed assetSetUuid [" + request.getUuid() + "]");
    }

    private Request newRequest() {
        Request request = new Request();
        request.setBean(bean);
        request.setMethod(method);
        return request;
    }

    private void sendMessage(Request request, String assetSetUuid, String contractUuid, String lastModifiedTime) {
        Object[] params = new Object[5];
        params[0] = Integer.valueOf(fileType);
        params[1] = assetSetUuid;
        params[2] = contractUuid;
        params[3] = fcNoList.split(",");
        params[4] = lastModifiedTime;
        request.setParams(params);
        producer.rpc().sendAsync(request);
        LOG.info("RepaymentPlanExtraCharge is changed assetSetUuid [" + assetSetUuid + "]");
    }

    private Request newAbsRequest() {
        Request request = new Request();
        request.setBean(absBean);
        request.setMethod(absMethod);
        return request;
    }
}
