package com.suidifu.giotto.handler.impl;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.model.*;
import com.suidifu.giotto.service.FastService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**FastHandlerImpl
 * 
 * @author zfj
 * @date  2017/6/1
 */
@Component(value = "fastHandler")
public class FastHandlerImpl implements FastHandler {

	private static final Log logger = LogFactory.getLog(FastHandlerImpl.class);

    @Autowired private FastService fastCacheService;
    @Autowired private FastService fastDBService;
    @Autowired private GenericDaoSupport genericDaoSupport;

	@Override
	public <T> List<T> getListByKeyList(FastKey keyEnum, List<String> keyValueList, Class<T> clazz, boolean isDirtyRead) throws GiottoException {
		if (CollectionUtils.isEmpty(keyValueList)) {
			return Collections.emptyList();
		}
		List<T> tList = new ArrayList<>();
		if (isDirtyRead) {
			tList = dirtyReadList(keyEnum, keyValueList, clazz);
		} else {
			tList = directReadList(keyEnum, keyValueList, clazz);
		}
		return tList;
	}

	private <T> List<T> directReadList(FastKey keyEnum, List<String> keyValueList, Class<T> clazz) throws GiottoException {
		long startTime = System.currentTimeMillis();
		Map<String, List<T>> tDBMap=fastDBService.getByKeyList(keyEnum, keyValueList, clazz);
		logger.info("db get list used [" + (System.currentTimeMillis() - startTime) + "]ms");
		List<T> resultList = new ArrayList<>();
		List<FastCacheObject> cacheList = new ArrayList<>();
		long start1 = System.currentTimeMillis();
		for (Map.Entry<String, List<T>> entry : tDBMap.entrySet()) {
			List<T> tDBList = entry.getValue();
			fastCacheService.delByKey(keyEnum, entry.getKey());
			if (CollectionUtils.isEmpty(tDBList)) {
				continue;
			}
			resultList.addAll(tDBList);
			for (int i = 0; i < tDBList.size(); i++) {
//				fastCacheService.add((FastCacheObject) tDBList.get(i));
				cacheList.add((FastCacheObject) tDBList.get(i));
			}
		}
		long start = System.currentTimeMillis();
		logger.info("direct read list for used [" + (start - start1) + "]ms");
		logger.info("cache list size : " + cacheList.size());
		fastCacheService.addList(cacheList);
		long end = System.currentTimeMillis();
		logger.info("redis add list use["+(end-start)+"]ms");
		logger.info("direct read list[" + (end - startTime) + "]ms");
		return resultList;
	}

	private <T> List<T> dirtyReadList(FastKey keyEnum, List<String> keyValueList, Class<T> clazz) throws GiottoException {
		long start = System.currentTimeMillis();
		Map<String, List<T>> tCacheMap = fastCacheService.getByKeyList(keyEnum, keyValueList, clazz);
		long end = System.currentTimeMillis();
		logger.info("redis get list use["+(end-start)+"]ms");
		if (CollectionUtils.isEmpty(tCacheMap)) {
			return directReadList(keyEnum, keyValueList, clazz);
		}
		List<T> resultList = new ArrayList<>();
		List<String> needDbValList = new ArrayList<>();
		for (Map.Entry<String, List<T>> entry : tCacheMap.entrySet()) {
			List<T> cacheList = entry.getValue();
			if (CollectionUtils.isEmpty(cacheList)) {
				needDbValList.add(entry.getKey());
			} else {
				resultList.addAll(cacheList);
			}
		}
		resultList.addAll(directReadList(keyEnum, needDbValList, clazz));
		logger.info("dirty read list used [" + (System.currentTimeMillis() - start) + "]ms");
		return resultList;
	}

	@Override
	public <T> List<T> getByKeyList(FastKey keyEnum, String keyValue, Class<T> clazz, boolean isDirtyRead) throws GiottoException {
		if (StringUtils.isBlank(keyValue)) {
//			throw new GiottoException("get error : keyValue is empty.");
			return Collections.emptyList();
        }
		List<T> tList = new ArrayList<T>();
		if (isDirtyRead) {
			tList=dirtyRead(keyEnum,keyValue,clazz);
		} else {
			tList=directRead(keyEnum,keyValue,clazz);
		}
		return tList;
	}


	private <T> List<T> directRead(FastKey keyEnum, String keyValue, Class<T> clazz) throws GiottoException {
		List<T> tDBList=fastDBService.getByKey(keyEnum, keyValue, clazz);
		fastCacheService.delByKey(keyEnum, keyValue);
		if (CollectionUtils.isEmpty(tDBList)) {
			return Collections.emptyList();
		}
		for (int i = 0; i < tDBList.size(); i++) {
			fastCacheService.add((FastCacheObject) tDBList.get(i));
		}
		return tDBList;
	}


	private <T> List<T> dirtyRead(FastKey keyEnum, String keyValue, Class<T> clazz) throws GiottoException {
		List<T> tCachedList = fastCacheService.getByKey(keyEnum, keyValue, clazz);
		if (CollectionUtils.isEmpty(tCachedList)) {
			List<T> tDBList=directRead(keyEnum,keyValue,clazz);
			return tDBList;
		}
		return tCachedList;
	}

	@Override
	public <T> T getByKey(FastKey keyEnum, String keyValue, Class<T> clazz, boolean isDirtyRead) throws GiottoException {
		if (StringUtils.isBlank(keyValue)) {
//            throw new GiottoException("get error : keyValue is empty.");
			return null;
        }
		List<T> tList = null;
		if (isDirtyRead) {
			tList=dirtyRead(keyEnum,keyValue,clazz);
		} else {
			tList=directRead(keyEnum,keyValue,clazz);
		}
		if (CollectionUtils.isEmpty(tList)) {
			return null;
		}
		return tList.get(0);
	}
	
	@Override
	public void delByKey(FastKey keyEnum, String keyValue, boolean isOnlyDelCache) throws GiottoException {
        if (StringUtils.isBlank(keyValue)) {
            throw new GiottoException("del error : keyValue is empty.");
        }
        if (!isOnlyDelCache) {
            fastDBService.delByKey(keyEnum, keyValue);
		}
        fastCacheService.delByKey(keyEnum, keyValue);
    }

	@Override
	public void add(FastCacheObject fastObject, boolean isOnlyAddCache) throws GiottoException {
        if (null == fastObject) {
            throw new GiottoException("save error : empty.");
        }
        if (!isOnlyAddCache) {
			fastDBService.add(fastObject);
		}
        fastCacheService.add(fastObject);
	}
	
//	@Override
	public int update(String updateSql, Map<String, Object> paramMap, FastCacheObject fastCacheObject) throws GiottoException {
		if (StringUtils.isBlank(updateSql)) {
			throw new GiottoException("update error : update sql is empty.");
		}
        if (null == paramMap) {
            throw new GiottoException("update error : paramMap is empty.");
        }
        if (null == fastCacheObject) {
    	   genericDaoSupport.executeSQL(updateSql, paramMap);
           return 1;
        }

        int count = fastDBService.updateInDB(updateSql, paramMap, fastCacheObject);

       //检查要更新的数据在缓存和db中是否一致
//       int count = genericDaoSupport.queryForInt(fastCacheObject.obtainQueryCheckMD5Sql(updateSql), paramMap);
       //一致则db
       //不管是否一致都更新缓存
	   FastCacheObject itemInDB = null;
	   if (fastCacheObject instanceof FastAssetSet) {
		   List<FastAssetSet> itemInDBList =fastDBService.getByKey(fastCacheObject.getColumnName(),
                		   fastCacheObject.getColumnValue(), FastAssetSet.class);
          if (!CollectionUtils.isEmpty(itemInDBList)) {
        	   itemInDB = itemInDBList.get(0);
           }
	   } else if (fastCacheObject instanceof FastContract) {
		   	List<FastContract> itemInDBList =fastDBService.getByKey(fastCacheObject.getColumnName(),
                		   fastCacheObject.getColumnValue(), FastContract.class);
		   	if (!CollectionUtils.isEmpty(itemInDBList)) {
        	   itemInDB = itemInDBList.get(0);
		   	}
	   } else if (fastCacheObject instanceof FastCustomer) {
		   List<FastCustomer> itemInDBList =fastDBService.getByKey(fastCacheObject.getColumnName(),
                		   fastCacheObject.getColumnValue(), FastCustomer.class);
          if (!CollectionUtils.isEmpty(itemInDBList)) {
        	   itemInDB = itemInDBList.get(0);
           }
	   } else if(fastCacheObject instanceof FastRepaymentOrderItem){
    	   List<FastRepaymentOrderItem> itemInDBList =fastDBService.getByKey(fastCacheObject.getColumnName(),
                		   fastCacheObject.getColumnValue(), FastRepaymentOrderItem.class);
          if (!CollectionUtils.isEmpty(itemInDBList)) {
        	   itemInDB = itemInDBList.get(0);
           }
	    } else {
	    	throw new GiottoException("no matching model: ");
	   }
	   
	   if (null != itemInDB) {
		   fastCacheService.add(itemInDB);
	   }
       return count;
	}
	/**
	 * 
	 */
	@Override
	public <T> List<T> getByKeyList(Map<FastKey, String> keyValueMap, Class<T> clazz, boolean isDirtyRead) throws GiottoException {
		if(keyValueMap == null){
			return	Collections.emptyList();
		}
		List<T>fastAccountList = new ArrayList<T>();
		
		if(!isDirtyRead){
			//先从DB中查
			fastAccountList = fastDBService.getByKey(keyValueMap, clazz);
			if(CollectionUtils.isEmpty(fastAccountList)){
				return Collections.emptyList();
			}
			for (int i = 0; i < fastAccountList.size(); i++) {
				fastCacheService.addNormal((FastCacheObject)fastAccountList.get(i));
			}
			return fastAccountList;
		}
		// 先从缓存中查询数据
		fastAccountList = fastCacheService.getByKey(keyValueMap, clazz);
		// 如果没有的话 再从DB中取
		if(CollectionUtils.isEmpty(fastAccountList)){
			fastAccountList = fastDBService.getByKey(keyValueMap, clazz);
			if(CollectionUtils.isEmpty(fastAccountList)){
				return Collections.emptyList();
			}
			for (int i = 0; i < fastAccountList.size(); i++) {
				fastCacheService.addNormal((FastCacheObject)fastAccountList.get(i));
			}
		}
		return fastAccountList;
	}

	@Override
	public <T> T getByKey(Map<FastKey, String> keyValueMap, Class<T> clazz, boolean isDirtyRead) throws GiottoException {
		if(keyValueMap == null){
			return null;
		}
		List<T>fastAccountList = new ArrayList<T>();
		if(!isDirtyRead){
			//先从数据库查  然后放进缓存里
			fastAccountList = fastDBService.getByKey(keyValueMap, clazz);
			if(CollectionUtils.isEmpty(fastAccountList)){
				return null;
			}
			//查询出的数据 加入缓存中
			fastCacheService.addNormal((FastCacheObject)fastAccountList.get(0));
			return fastAccountList.get(0);
		}
		// 先从缓存中查询数据
		fastAccountList = fastCacheService.getByKey(keyValueMap, clazz);
		// 如果没有的话 再从DB中取
		if(CollectionUtils.isEmpty(fastAccountList)){
			fastAccountList = fastDBService.getByKey(keyValueMap, clazz);
		    if(CollectionUtils.isEmpty(fastAccountList)){
		    	return null;
		    }
		}
		//查询出的数据 加入缓存中
		fastCacheService.addNormal((FastCacheObject)fastAccountList.get(0));
		return fastAccountList.get(0);
	}
}
