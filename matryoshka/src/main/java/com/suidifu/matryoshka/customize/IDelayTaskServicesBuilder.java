package com.suidifu.matryoshka.customize;

import org.codehaus.commons.compiler.CompileException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by louguanyang on 2017/5/4.
 */
public interface IDelayTaskServicesBuilder extends IBaseServicesBuilder {
	List<String> DELAY_TASK_IMPORT_PACKAGE = Arrays.asList(new String[] { "com.demo2do.core.entity.Result",
			"com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler","org.apache.commons.logging.Log" });

	// DelayTaskServices build(String script) throws CompileException,
	// IllegalAccessException, InstantiationException, IOException;
	DelayTaskServices build(String script, String sourceFilePath)
			throws CompileException, IllegalAccessException, InstantiationException, IOException;
}
