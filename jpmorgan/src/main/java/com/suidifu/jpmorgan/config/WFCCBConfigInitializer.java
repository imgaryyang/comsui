package com.suidifu.jpmorgan.config;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WFCCBConfigInitializer {
	
    @Value("#{config['iniCount']}")
    private String iniCount;

	private static final Log logger = LogFactory.getLog(WFCCBConfigInitializer.class);
	
	private AtomicInteger count = new AtomicInteger(0);
	
	@PostConstruct
	public void configInitialization() throws Exception {
		
		if(! StringUtils.isEmpty(iniCount)) {
			int wfccbLastCount = Integer.parseInt(iniCount);
			
			count = new AtomicInteger(wfccbLastCount);
			
		}
		
	}

	public int getNextCount() {
		return count.getAndIncrement();
	}

	
}

