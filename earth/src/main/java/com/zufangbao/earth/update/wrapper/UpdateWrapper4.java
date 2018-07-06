package com.zufangbao.earth.update.wrapper;

import com.zufangbao.earth.report.util.FreemarkerUtil;
import com.zufangbao.earth.update.model.UpdateWrapperModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.zufangbao.earth.update.util.UpdateConfiguration.UPDATE_CODES_WRAPPER_FILE_MAPPER;

/**
 * Created by whb on 17-4-28.
 */

@Component
public class UpdateWrapper4 extends IUpdateBaseWraper implements IUpdateWrapper<UpdateWrapperModel>  {

    private final String updateOfflineBill="updateOfflineBill";


    @Autowired
    private UpdateSqlCacheManager updateSqlCacheManager;

    @Override
    public String wrap(UpdateWrapperModel paramsBean) throws Exception {
        StringBuffer montageSql = new StringBuffer();
        Map<String, Object> sqlMap = updateSqlCacheManager.getSqlParam().get(UPDATE_CODES_WRAPPER_FILE_MAPPER.get("4"));
        Map<String, Object> slSqlMap = this.getParamMap(paramsBean);

        // 从模板中获取update语句
        String updateSql = (String) sqlMap.get(updateOfflineBill);

        // 从模板中获取查询语句进行填充和执行
        List<Map<String, Object>> param1 = this.getSql("financialContract" , slSqlMap, sqlMap);
        if (null == param1 || 0==param1.size()) {
            return null;
        }
        slSqlMap.putAll(param1.get(0));

        // 判断offlineBillNo在数据库中只有一条数据
        List<Map<String, Object>> param2 = this.getSql("offlineBill" , slSqlMap, sqlMap);
        if (null == param2 || 0== param2.size()) {
            return null;
        }
        if ( 1!=param2.size()) {
            return "offlineBillNo在数据库中有"+param2.size()+"条数据";
        }

        return montageSql.append(FreemarkerUtil.process(updateSql, slSqlMap)).toString();
    }
}
