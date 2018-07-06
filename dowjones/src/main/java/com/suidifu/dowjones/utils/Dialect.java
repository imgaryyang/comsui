/**
 *
 */
package com.suidifu.dowjones.utils;

/**
 * @author Downpour
 */
public interface Dialect {

    public String getSearchLimitString(String sentence, int offset, int limit, boolean putValue);

}
