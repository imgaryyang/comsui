package com.suidifu.matryoshka.service.delayTask.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.matryoshka.delayTask.DelayProcessingTaskConfig;
import com.suidifu.matryoshka.delayTask.enums.DelayTaskConfigStatus;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskConfigDBService;
import com.zufangbao.sun.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by louguanyang on 2017/5/3.
 */
@Service("delayProcessingTaskConfigDBService")
public class DelayProcessingTaskConfigDBServiceImpl extends GenericServiceImpl<DelayProcessingTaskConfig> implements
        DelayProcessingTaskConfigDBService {

    @Override
    public DelayProcessingTaskConfig getValidConfig(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            return null;
        }
        String hql = "from DelayProcessingTaskConfig where uuid=:uuid and status=:status";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("uuid", uuid);
        parameters.put("status", DelayTaskConfigStatus.VALID.getCode());
        List<DelayProcessingTaskConfig> configList = this.genericDaoSupport.searchForList(hql, parameters);
        if(CollectionUtils.isEmpty(configList)){
            return null;
        }
        return configList.get(0);
    }

    public DelayProcessingTaskConfig getByProduct(String productLv1Code,String productLv2Code,String productLv3Code){
        if(StringUtils.isEmpty(productLv1Code)||StringUtils.isEmpty(productLv2Code)||StringUtils.isEmpty(productLv3Code)){
            return null;
        }
        String hql = "from DelayProcessingTaskConfig where productLv1Code=:productLv1Code and productLv2Code=:productLv2Code and " +
                "productLv3Code =:productLv3Code";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("productLv1Code", productLv1Code);
        parameters.put("productLv2Code", productLv2Code);
        parameters.put("productLv3Code", productLv3Code);
        List<DelayProcessingTaskConfig> configList = this.genericDaoSupport.searchForList(hql, parameters);
        if(CollectionUtils.isEmpty(configList)){
            return null;
        }
        return configList.get(0);
    }


}
