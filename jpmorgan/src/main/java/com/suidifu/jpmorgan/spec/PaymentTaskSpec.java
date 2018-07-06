package com.suidifu.jpmorgan.spec;

/**
 * 
 * @author zjm
 *
 */
public class PaymentTaskSpec {

	public static final int PEEK_IDLE_LIMIT = 100;
	
	public static final int UPDATE_TRY_TIMES = 3;
		
	public static final int DEFAULT_SENDING_TIMEOUT_MINUTES = 10;
	
	public static final String MSG_PEEKIDLE_ERROR = ",peek idle task error!";
	
	public static final String MSG_OCCUPY_FAIL = ",occupy failed, orderUuid :";
	
	public static final String MSG_SENTOUT_FAIL = ",sent out failed, orderUuid :";
	
	public static final String MSG_CANNOT_OCCUPY_ERROR = "CAN NOT OCCUPY!";
}
