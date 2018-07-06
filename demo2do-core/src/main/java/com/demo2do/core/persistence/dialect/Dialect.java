/**
 * 
 */
package com.demo2do.core.persistence.dialect;

/**
 * @author Downpour
 */
public interface Dialect {

	public String getSearchLimitString(String sentence, int offset, int limit, boolean putValue);

}
