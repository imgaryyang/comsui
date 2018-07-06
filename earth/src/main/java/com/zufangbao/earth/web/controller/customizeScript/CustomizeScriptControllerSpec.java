package com.zufangbao.earth.web.controller.customizeScript;

public class CustomizeScriptControllerSpec {
	public static class URL {
		public static final String NAME = "/customize-script/";
		public static final String MENU = "menu-system";
		public static final String SUB_MENU = "submenu-get-pre-script-catalog";

		public static final String SUBMIT_PRE_SCRIPT = "pre-script/submit";
		public static final String ENABLE_PRE_SCRIPT = "pre-script/enable";
		public static final String UNABLE_PRE_SCRIPT = "pre-script/unable";
		public static final String GET_CATALOG = "pre-script/getCatalog";
		public static final String GET_SOURCE = "{productCode}/{channelCode}/{serviceCode}";
		public static final String RUN_SOURCE = "runSource";
	}

	public static class REQUEST {
		public static final String PRE_SCRIPT = "preScript";
		public static final String COMPILE_IMPORT = "compileImport";

		public static final String PARAM_SCRIPT = "script";
		public static final String PARAM_PRE_INTERFACE = "preProcessInterfaceUrl";
	}

	public static class INFO {
		public static final String INFO_KEY = "message";
		public static final String LAST_MODIFY = "modifyDate";
		public static final String AUTHOR = "author";
		
		public static final String SOURCE_INFO_SCRIPT = "script";
		public static final String SOURCE_INFO_LAST_MODIFY_TIME = "lastModifyTime";
		public static final String SOURCE_INFO_AUTHOR = "author";
		public static final String SOURCE_INFO_COMPILE_IMPORT = "compileImport";

		public static final String CATA_INFO_SIZE = "size";
		public static final String CATA_INFO_LIST = "list";

	}

}
