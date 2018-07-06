package com.suidifu.bridgewater.controller.api.remittance.v2.exception;

/**
 * RemittanceApplication非最终态
 */
public class NonFinalStateException extends RuntimeException {

	public NonFinalStateException() {

	}

	public NonFinalStateException(String message) {
		super(message);
	}

}
