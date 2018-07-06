package com.demo2do.core;

/**
 * 
 * @author Downpour
 */
public class BusinessException extends RuntimeException {
	
	private static final long serialVersionUID = -2931549392530749973L;
	
	private String targetURL;
	
	/**
	 * 
	 * @param message
	 */
	public BusinessException(String message) {
		super(message);
	}
	
	/**
	 * The full constructor
	 * 
	 * @param message
	 * @param targetURL
	 */
	public BusinessException(String message, String targetURL) {
		super(message);
		this.targetURL = targetURL;
	}
	
	/**
	 * @return the targetURL
	 */
	public String getTargetURL() {
		return targetURL;
	}

}
