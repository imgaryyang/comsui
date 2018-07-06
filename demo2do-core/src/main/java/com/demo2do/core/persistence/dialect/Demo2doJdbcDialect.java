/**
 * 
 */
package com.demo2do.core.persistence.dialect;


/**
 * @author Downpour
 */
public class Demo2doJdbcDialect implements Dialect {
		
	/* (non-Javadoc)
	 * @see com.demo2do.core.search.dialect.SearchDialect#getSearchLimitString(java.lang.String, int, int, boolean)
	 */
	public String getSearchLimitString(String sentence, int offset, int limit, boolean putValue) {
		boolean hasOffset = offset > 0;
		StringBuffer sb = new StringBuffer(sentence.length() + 20).append(sentence);
		if (putValue) {
			return sb.append(hasOffset ? " limit " + offset + ", " + limit : " limit " + limit).toString();
		}
		return sb.append(hasOffset ? " limit :beginIndex, :maxResult" : " limit :maxResult").toString();
	}

}
