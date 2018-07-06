/* Created on 2006-7-21 */
package com.demo2do.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * String related utilities
 *
 * @author Downpour
 */
public abstract class StringUtils {
	
    public static final String EMPTY = "";

    /**
     * Replace place holder with parameters
     *
     * @param template
     * @param values
     * @return
     */
    public static String replacePlaceHolder(String template, Map<String, String> valueMap) {

        if (template.indexOf("${") == -1) {
            return template;
        }

        while (true) {
            int start = template.indexOf("${");
            int end = template.indexOf("}", start);

            if (start != -1 && end != -1) {
                String temp = template.substring(start + 2, end);

                if (valueMap.keySet().contains(temp)) { // contains the dynamic string then replace
                    template = template.substring(0, start) + (String) valueMap.get(temp) + template.substring(end + 1);
                } else {
                    template = template.substring(0, start) + template.substring(end + 1);
                }

            } else {
                break;
            }
        }

        return template;
    }

    /**
     * Determine a template has a placeHolder
     *
     * @param template
     * @return
     */
    public static boolean hasPlaceHolder(String template) {
        if (template.indexOf("${") == -1) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Determine a string is template string
     * 
     * @param string
     * @return
     */
    public static boolean isTempateString(String string) {
    	if (string.indexOf("{") == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Connect strings together
     *
     * @param parts
     * @return
     */
    public static String connect(String... parts) {
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(part);
        }
        return sb.toString();
    }

	/**
	 * camelize seperated strings together
	 * 
	 * @param parts
	 * @return
	 */
	public static String camelize(String... parts) {
		StringBuilder sb = new StringBuilder();
		for (String part : parts) {
			sb.append(org.apache.commons.lang.StringUtils.capitalize(part));
		}
		return org.apache.commons.lang.StringUtils.uncapitalize(sb.toString());
	}

    /**
     * Convert camel naming to any naming using seperator
     *
     * @param template
     * @param seperator
     * @return
     */
    public static String convertCamel(String template, String seperator) {
        StringBuffer result = new StringBuffer();
        if (template != null && template.length() > 0) {
            result.append(template.substring(0, 1).toLowerCase());
            for (int i = 1; i < template.length(); i++) {
                String s = template.substring(i, i + 1);
                if (s.equals(s.toUpperCase()) && !s.equals(seperator)) {
                    result.append(seperator);
                    result.append(s.toLowerCase());
                } else {
                    result.append(s);
                }
            }
        }
        return result.toString();
    }

    /**
     * 
     * @param string
     * @param length
     * @return
     */
    public static String left(String string, int length) {
        return org.apache.commons.lang.StringUtils.left(string, length);
    }
    
    /**
     * 
     * @param string
     * @param separator
     * @return
     */
    public static String[] split(String string, String separator) {
    	return org.apache.commons.lang.StringUtils.split(string, separator);
    }

    /**
     * 
     * @param uri
     * @param param
     * @return
     */
    public static String findParameterFromURI(String uri, String param) {
    	for(String nameValuePair : org.apache.commons.lang.StringUtils.split(uri.substring(uri.indexOf("?") + 1), "&")) {
    		if(nameValuePair.startsWith(param + "=")) {
    			return nameValuePair.substring(nameValuePair.indexOf("=") + 1);
    		}
    	}
    	return null;
    }
    
    /**
     * 
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
    	return org.apache.commons.lang.StringUtils.isEmpty(string);
    }
    
    /**
     * @param queryString
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
    	String[] queries = StringUtils.split(queryString, "&");
    	Map<String, String> params = new HashMap<String, String>(queries.length);
    	for(String query : queries) {
    		String[] param = StringUtils.split(query, "=");
    		String key = param[0];
    		String value = param.length < 2 ? "" : param[1];
      		if(params.containsKey(param[0])) {
        		params.put(key,  StringUtils.connect(params.get(key), ",", value));
       		} else {
       			params.put(key, value);
       		}
    	}
    	return params;
    }
    
    /**
     * @param params
     * @return
     */
    public static String toQueryString(Map<String, String> params) {
    	List<String> queries = new ArrayList<String>();
    	for(Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator(); iterator.hasNext();) {
    		Map.Entry<String, String> entry = iterator.next();
    		queries.add(StringUtils.connect(entry.getKey(), "=", entry.getValue()));
    	}
    	return org.apache.commons.lang.StringUtils.join(queries, "&");
    }

    /**
     * ellipse a string like 'aaa...' if it is longer than limit
     * 
     * @param str
     * @param limit
     * @return
     */
    public static String ellipseString(String str, int limit) {
    	if(str != null && str.length() > limit) {
			return str.substring(0, limit) + "...";
		}
		return str;
    }
    
}
