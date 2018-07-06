package com.suidifu.barclays.handler;

import java.util.Map;

public interface JobHandler {

	void run(Map<String, Object> workingParms);
}
