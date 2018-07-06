package com.suidifu.jpmorgan.handler;

import java.util.Map;

public interface JobHandler {

	void run(Map<String, Object> workingParms);
}
