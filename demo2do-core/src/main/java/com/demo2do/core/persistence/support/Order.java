/**
 * 
 */
package com.demo2do.core.persistence.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author downpour
 *
 */
public class Order {
	
	private List<String> sentences = new ArrayList<String>();
	
	/**
	 * The default constructor
	 */
	public Order() {
		
	}
	
	/**
	 * 
	 * @param key
	 * @param type
	 */
	public Order(String key, String type) {
		this.add(key, type);
	}
	
	/**
	 * 
	 * @param key
	 * @param type
	 * @return
	 */
	public Order add(String key, String type) {
		this.sentences.add(" " + key + " " + type);
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public String getSentence() {
		if(this.sentences.isEmpty()) {
			return "";
		}
		return " ORDER BY" + StringUtils.join(this.sentences, ",");
	}

}
