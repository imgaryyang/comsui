package com.suidifu.mq.consumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ErrorHandler;

/**
 * 
 * @author lisf
 *
 */
public class ConsumerErrorHandler implements ErrorHandler {
	private static Log LOG = LogFactory.getLog(ConsumerErrorHandler.class);

	@Override
	public void handleError(Throwable t) {
		LOG.error("ConsumerErrorHandler.error", causeChainTop(t));
	}

	private Throwable causeChainTop(Throwable t) {
		Throwable cause = null;
		if (t == null || (cause = t.getCause()) == null)
			return t;
		while (cause.getCause() != null) {
			cause = cause.getCause();
		}
		return cause;
	}

}
