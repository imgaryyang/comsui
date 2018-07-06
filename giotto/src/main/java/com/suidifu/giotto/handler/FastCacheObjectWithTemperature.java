/**
 * 
 */
package com.suidifu.giotto.handler;

import com.suidifu.giotto.model.FastCacheObject;
import com.suidifu.giotto.util.SqlAndParamTuple;

/**
 * @author wukai
 *
 */
public abstract class FastCacheObjectWithTemperature extends FastCacheObject{
	
	/**
	 * 数据温度
	 * @return
	 */
	public abstract DataTemperature temperature();

	/**
	 * 带版本锁更新
	 * @param oldVersion
	 * @return
	 */
	public abstract SqlAndParamTuple updateSqlAndParamTupleWithVersionLock(String oldVersion);
	
	/**
	 * 带版本锁更新后的校验
	 * @param oldVersion
	 * @return
	 */
	public abstract SqlAndParamTuple checkAfterUpdateSqlAndParamTuple();
	
	/**
	 * 更新
	 * 
	 */
	public abstract SqlAndParamTuple updateSqlAndParamTuple();
}
