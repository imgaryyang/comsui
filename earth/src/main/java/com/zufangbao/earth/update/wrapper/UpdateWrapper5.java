package com.zufangbao.earth.update.wrapper;

import com.zufangbao.earth.report.util.FreemarkerUtil;
import com.zufangbao.earth.update.model.UpdateWrapperModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.zufangbao.earth.update.util.UpdateConfiguration.UPDATE_CODES_WRAPPER_FILE_MAPPER;

/**
 * Created by whb on 17-5-2.
 */
@Component
public class UpdateWrapper5 extends IUpdateBaseWraper implements IUpdateWrapper<UpdateWrapperModel>  {

    @Autowired
    private UpdateSqlCacheManager updateSqlCacheManager;

    private final String updateThirdPartyVoucherCommandLog="updateThirdPartyVoucherCommandLog";
    private final String updateThirdPartyTransactionRecord="updateThirdPartyTransactionRecord";
    private final String updateThirdPartyPayAuditResult="updateThirdPartyPayAuditResult";
    private final String updateDeductApplicationAndDeductPlan="updateDeductApplicationAndDeductPlan";
    private final String thirdPartyVoucherCommandLog="thirdPartyVoucherCommandLog";
    private final String journalVoucher="journalVoucher";
    private final String updateJournalVoucher="updateJournalVoucher";
    private final String updateSourceDocument="updateSourceDocument";
    private final String updateSourceDocumentDetail="updateSourceDocumentDetail";

    @Override
    public String wrap(UpdateWrapperModel paramsBean) throws Exception {
        StringBuffer montageSql = new StringBuffer();
        Map<String, Object> sqlMap = updateSqlCacheManager.getSqlParam().get(UPDATE_CODES_WRAPPER_FILE_MAPPER.get("5"));
        Map<String, Object> slSqlMap = this.getParamMap(paramsBean);

        // 从模板中获取update语句
        String updateSql1 = (String) sqlMap.get(updateThirdPartyVoucherCommandLog);
        String updateSql2 = (String) sqlMap.get(updateThirdPartyTransactionRecord);
        String updateSql3 = (String) sqlMap.get(updateThirdPartyPayAuditResult);
        String updateSql4 = (String) sqlMap.get(updateDeductApplicationAndDeductPlan);
        String updateSql5 = (String) sqlMap.get(updateJournalVoucher);
        String updateSql6 = (String) sqlMap.get(updateSourceDocument);
        String updateSql7 = (String) sqlMap.get(updateSourceDocumentDetail);

        // 从模板中获取查询语句进行填充和执行
        List<Map<String, Object>> param1 = this.getSql(thirdPartyVoucherCommandLog , slSqlMap, sqlMap);
        if (null == param1 || 0==param1.size()) {
            return null;
        }
        String tradeUuid=param1.get(0).get("tradeUuid").toString();
        if(param1.get(0).get("repaymentNoJsonList")!=null){
            String repaymentNoJsonList=param1.get(0).get("repaymentNoJsonList").toString();
            String repaymentNos=repaymentNoJsonList.substring(1,repaymentNoJsonList.length()-1);
            slSqlMap.put("relatedBillContractNoLv3",repaymentNos);
        }
        slSqlMap.put("tradeUuid",tradeUuid);

        montageSql.append(FreemarkerUtil.process(updateSql1, slSqlMap));
        montageSql.append(FreemarkerUtil.process(updateSql2, slSqlMap));
        montageSql.append(FreemarkerUtil.process(updateSql3, slSqlMap));
        montageSql.append(FreemarkerUtil.process(updateSql4, slSqlMap));

        // 从模板中获取查询语句进行填充和执行
        List<Map<String, Object>> param2 = this.getSql(journalVoucher , slSqlMap, sqlMap);
        if (null == param2 || 0==param2.size()) {
            return null;
        }
        slSqlMap.putAll(param2.get(0));

        montageSql.append(FreemarkerUtil.process(updateSql5, slSqlMap));
        montageSql.append(FreemarkerUtil.process(updateSql6, slSqlMap));
        montageSql.append(FreemarkerUtil.process(updateSql7, slSqlMap));

        return montageSql.toString();
    }
}
