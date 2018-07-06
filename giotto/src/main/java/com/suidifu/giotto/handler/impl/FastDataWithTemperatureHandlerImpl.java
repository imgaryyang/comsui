package com.suidifu.giotto.handler.impl;

import java.util.List;

import com.suidifu.giotto.exception.GiottoException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.giotto.handler.DataTemperature;
import com.suidifu.giotto.handler.FastCacheObjectWithTemperature;
import com.suidifu.giotto.handler.FastDataWithTemperatureHandler;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.model.FastCacheObject;
import com.suidifu.giotto.service.FastService;
import com.suidifu.giotto.util.SqlAndParamTuple;

@Component("fastDataWithTemperatureHandler")
public class FastDataWithTemperatureHandlerImpl extends FastHandlerImpl implements FastDataWithTemperatureHandler {
	
	@Autowired private FastService fastCacheService;
	
    @Autowired private FastService fastDBService;
    
    @Autowired
    private GenericDaoSupport genericDaoSupport;
    
    @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
	private static final Log logger = LogFactory.getLog(FastDataWithTemperatureHandlerImpl.class);

	@Override
	public <T extends FastCacheObjectWithTemperature> T getByKey(FastKey keyEnum, String keyValue, Class<T> clazz) {

		try {
			//先取之前的数据温度
			List<T> dataInCacheList =  fastCacheService.getByKey(keyEnum, keyValue, clazz);

			if(CollectionUtils.isNotEmpty(dataInCacheList)) {

                logger.info("get by REDIS,FastKey:["+keyEnum.getKey(keyValue)+"].");

                return dataInCacheList.get(0);
            }

			List<T> dataInDbList = fastDBService.getByKey(keyEnum, keyValue, clazz);

			if(CollectionUtils.isEmpty(dataInDbList)) {

                return null;
            }

			logger.info("get by DB,FastKey:["+keyEnum.getKey(keyValue)+"].");

			T data = dataInDbList.get(0);

			FastCacheObjectWithTemperature dataWithTemperature = data;

			if(dataWithTemperature.temperature() == DataTemperature.FROZEN) {

                logger.info("append to CACHE,FastKey:["+keyEnum.getKey(keyValue)+"].");

                fastCacheService.add((FastCacheObject)data);
            }

			return data;
		} catch (GiottoException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateDataWithVersionLock(FastCacheObjectWithTemperature fastCacheObject, String oldVersion) {
		
		SqlAndParamTuple sqlAndParamTuple = fastCacheObject.updateSqlAndParamTupleWithVersionLock(oldVersion);

		genericDaoSupport.executeSQL(sqlAndParamTuple.sql, sqlAndParamTuple.paramMap);
		
		SqlAndParamTuple checkSqlAndParamTuple = fastCacheObject.checkAfterUpdateSqlAndParamTuple();
		
		List<String> uuids = genericDaoSupport.queryForSingleColumnList(checkSqlAndParamTuple.sql, checkSqlAndParamTuple.paramMap, String.class);
		if(CollectionUtils.isEmpty(uuids)) {
			return false;
		}
		//write to redis
		try {
			if(fastCacheObject.temperature() == DataTemperature.FROZEN) {

                fastCacheService.add(fastCacheObject);
            }
		} catch (GiottoException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	@Override
	public boolean updateData(FastCacheObjectWithTemperature fastCacheObject) {
		
		//write to db
		
		SqlAndParamTuple sqlAndParamTuple = fastCacheObject.updateSqlAndParamTuple();
		
		genericDaoSupport.executeSQL(sqlAndParamTuple.sql	, sqlAndParamTuple.paramMap);
				
		//write to redis
		try {
			if(fastCacheObject.temperature() == DataTemperature.FROZEN) {

                fastCacheService.add(fastCacheObject);
            }
		} catch (GiottoException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
