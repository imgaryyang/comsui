package com.zufangbao.earth.report.wrapper;


import com.zufangbao.sun.exception.LedgerItemCreateException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zj on 17-12-6.
 */
@Component
public class CreateLedgerBookWrapper extends ReportBaseWrapper implements ICreateLedgerBookWrapper {
    @Override
    public String getCreateSqlByTableName(String id, String tabelName) throws Exception {

        if (StringUtils.isBlank(tabelName)){
            throw new LedgerItemCreateException("tableName not is null !!!");
        }
        if (StringUtils.isBlank(id)){
            throw new LedgerItemCreateException("query CreateTableSqlCache id not is null !!!");
        }
        Map<String, Object> paramMap = new HashMap<>();
       String tabelNameNew = tabelName.replace("-", "_");
        paramMap.put("tableName",tabelNameNew);

        return getCachedSql(id, paramMap);
    }
}
