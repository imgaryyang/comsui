package com.suidifu.jpmorgan.handler;

import java.math.BigDecimal;
import java.util.Date;

public interface DistributeStatHandler {
	
	public void distributeStat(Date date, String paymentChannelUuid, BigDecimal amount);
}
