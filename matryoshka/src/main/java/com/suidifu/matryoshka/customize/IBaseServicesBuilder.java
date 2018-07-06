package com.suidifu.matryoshka.customize;

import java.util.Arrays;
import java.util.List;

public interface IBaseServicesBuilder {
	public static final String SPLIT_REGEX = ",";
	public static final List<String> BASE_IMPORT_PACKAGE = Arrays.asList(
			new String[] {
					"com.suidifu.matryoshka.handler.SandboxDataSetHandler" 
			});
}
