package com.suidifu.giotto.service.impl;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.keyenum.FastAccountTemplateKeyEnum;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.model.FastCacheObject;
import com.suidifu.giotto.model.FastRepaymentOrderItem;
import com.suidifu.giotto.service.FastService;
import com.suidifu.giotto.util.SqlAndParamTuple;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * FastDBServiceImpl
 *
 * @author whb
 * @date 2017/5/26
 */
@Component(value = "fastDBService")
public class FastDBServiceImpl implements FastService {

    @Autowired private GenericDaoSupport genericDaoSupport;

    @Override
    public <T> Map<String, List<T>> getByKeyList(FastKey keyEnum, List<String> keyValueList, Class<T> clazz) throws GiottoException {
        if (CollectionUtils.isEmpty(keyValueList)) {
            return Collections.emptyMap();
        }
        try {
            SqlAndParamTuple sqlAndParamTuple = keyEnum.getSqlAndParamByList(keyValueList, false);
            List<T> resultList = genericDaoSupport.queryForList(
                    sqlAndParamTuple.sql, sqlAndParamTuple.paramMap, clazz);
            Map<String, List<T>> resultMap = new HashMap<>();
            for (T t : resultList) {
                if (t instanceof FastRepaymentOrderItem) {
                    FastRepaymentOrderItem orderItem = (FastRepaymentOrderItem) t;
                    if (resultMap.containsKey(orderItem.obtainQueryListKeyValue())) {
                        List<T> tList = resultMap.get(orderItem.obtainQueryListKeyValue());
                        tList.add(t);
                        resultMap.put(orderItem.obtainQueryListKeyValue(), tList);
                    } else {
                        List<T> tList = new ArrayList<>();
                        tList.add(t);
                        resultMap.put(orderItem.obtainQueryListKeyValue(), tList);
                    }
                } else {
                    break;
                }
            }
            return resultMap;
        } catch (Exception e) {
            throw new GiottoException("get by db error : " + ExceptionUtils.getFullStackTrace(e));
        }
    }

    @Override
    public <T> List<T> getByKey(FastKey keyEnum, String keyValue, Class<T> clazz) throws GiottoException {
        try {
            SqlAndParamTuple sqlAndParamTuple = keyEnum.getSqlAndParam(keyValue, false);
            return genericDaoSupport.queryForList(
                    sqlAndParamTuple.sql, sqlAndParamTuple.paramMap, clazz);
        } catch (Exception e) {
            throw new GiottoException("get by db error : " + ExceptionUtils.getFullStackTrace(e));
        }
    }

    @Override
    public void delByKey(FastKey keyEnum, String keyValue) throws GiottoException {
        try {
            SqlAndParamTuple sqlAndParamTuple = keyEnum.getSqlAndParam(keyValue, true);
            genericDaoSupport.executeSQL(
                    sqlAndParamTuple.sql, sqlAndParamTuple.paramMap);
        } catch (Exception e) {
            throw new GiottoException("del by db error : " + e.getMessage());
        }
    }

    @Override
    public void add(FastCacheObject fastObject) throws GiottoException {
        SqlAndParamTuple addAndParam = fastObject.obtainInsertSqlAndParam();
        try {
            genericDaoSupport.executeSQL(addAndParam.sql, addAndParam.paramMap);
        } catch (Exception e) {
            throw new GiottoException("save by db error : " + e.getMessage());
        }
    }

    @Override
    public void addList(List<FastCacheObject> fastCacheObjectList) throws GiottoException {
        throw new GiottoException("batch save by db not support");
    }

    @Override
    public int updateInDB(String updateSql, Map<String, Object> paramMap, FastCacheObject fastCacheObject) throws GiottoException {
        try {
            if (null == fastCacheObject) {
                genericDaoSupport.executeSQL(updateSql, paramMap);
                return 1;
            }
            int count = genericDaoSupport.queryForInt(fastCacheObject.obtainQueryCheckMD5Sql(updateSql), paramMap);
            if (count > 0) {
//                genericDaoSupport.executeSQL(fastCacheObject.obtainUpdateCheckMD5Sql(updateSql), paramMap);
                genericDaoSupport.executeSQL(updateSql, paramMap);
            }
            return count;
        } catch (Exception e) {
            throw new GiottoException("update by db error : " + e.getMessage());
        }
    }

	@Override
	public <T> List<T> getByKey(Map<FastKey, String> map, Class<T> clazz) throws GiottoException {
		SqlAndParamTuple sqlAndParamTuple = FastAccountTemplateKeyEnum.LEDGER_BOOK_NO.getSqlAndParam(map, false);
		try {
			return genericDaoSupport.queryForList(sqlAndParamTuple.sql, sqlAndParamTuple.paramMap, clazz);
		} catch (Exception e) {
			throw new GiottoException("get by db error :"+e.getMessage());
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}

    @Override
    public void addNormal(FastCacheObject fastObject) {

    }
}
