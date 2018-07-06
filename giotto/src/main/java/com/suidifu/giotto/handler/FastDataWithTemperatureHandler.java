/**
 * 
 */
package com.suidifu.giotto.handler;

import com.suidifu.giotto.keyenum.FastKey;

/**
 * @author wukai
 *
 */
public interface FastDataWithTemperatureHandler extends FastHandler {

	public <T extends FastCacheObjectWithTemperature> T getByKey(FastKey keyEnum, String keyValue, Class<T> clazz);
	
	public boolean updateDataWithVersionLock(FastCacheObjectWithTemperature fastCacheObject,String oldVersion);

	boolean updateData(FastCacheObjectWithTemperature fastCacheObject);
}
