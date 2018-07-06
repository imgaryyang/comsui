/**
 * 
 */
package com.demo2do.core.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author Downpour
 */
public class Result {

	private String code;
	
	private String message;
	
	private Map<String, Object> data;
	
	/**
	 * The default constructor
	 */
	public Result() {
		this.code = "1";
		this.data = new HashMap<String, Object>();
	}
	
	/**
	 * 
	 * @param code
	 */
	public Result(String code) {
		this.code = code;
		this.data = new HashMap<String, Object>();
	}
	
	/**
	 * Initialize with code and message
	 * 
	 * @param code
	 * @param message
	 * @return
	 */
	public Result initialize(String code, String message) {
		this.code = code;
		this.message = message;
		return this;
	}
	
	/**
	 * Mark with success flag
	 * 
	 * @return
	 */
	public Result success() {
		this.code = "0";
		return this;
	}
	
	/**
	 * Mark with fail flag
	 * 
	 * @return
	 */
	public Result fail() {
		this.code = "1";
		return this;
	}
	
	/**
	 * Add message for result
	 * 
	 * @param message
	 * @return
	 */
	public Result message(String message) {
		this.message = message;
		return this;
	}
	
	/**
	 * Add data for result
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Result data(String key, Object value) {
		this.data.put(key, value);
		return this;
	}

	/**
	 * Determine whether the status is valid
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isValid() {
		return StringUtils.equals(this.code, "0");
	}
	
	/**
	 * Get data according to key
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return this.data.get(key);
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @return the data
	 */
	public Map<String, Object> getData() {
		return data;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
}
