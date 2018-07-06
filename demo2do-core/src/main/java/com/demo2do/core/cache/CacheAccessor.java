/**
 * 
 */
package com.demo2do.core.cache;


/**
 * @author Downpour
 */
public interface CacheAccessor {

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(String key);
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object evaluate(String key);

}
