package com.zufangbao.earth.update.util;

import com.zufangbao.earth.update.wrapper.*;

import java.util.HashMap;
import java.util.Map;


public class UpdateConfiguration {
	
	
	@SuppressWarnings("rawtypes")
	public final static Map<String, Class<? extends IUpdateWrapper>> UPDATE_CODES_WRAPPER_BEAN_MAPPER = new HashMap<String, Class<? extends IUpdateWrapper>>() {
		private static final long serialVersionUID = 2550377183423707773L;
		{
			put("1",UpdateWrapper1.class);
			put("2",UpdateWrapper2.class);
			put("3",UpdateWrapper3.class);
			put("4",UpdateWrapper4.class);
			put("5",UpdateWrapper5.class);
			//线上支付单作废
			put("6",UpdateWrapper6.class);
			//线下支付单作废
			put("7", UpdateWrapper7.class);
		}
	};
	
	
	
	/*public final static Map<String, Class<?>> UPDATE_CODES_WRAPPER_PAEAM_MAPPER = new HashMap<String, Class<?>>() {
		private static final long serialVersionUID = 2550377183423707773L;
		{
			put("1",UpdateWrapperModel.class);
		}
	};*/
	
	
	public final static Map<String, String> UPDATE_CODES_WRAPPER_FILE_MAPPER = new HashMap<String, String>() {
		private static final long serialVersionUID = 2550377183423707773L;
		{
			put("1","updateWrapper1.xml");
			put("2","updateWrapper2.xml");
			put("3","updateWrapper3.xml");
			put("4","updateWrapper4.xml");
			put("5","updateWrapper5.xml");
			put("6","updateWrapper6.xml");
			put("7","updateWrapper7.xml");
		}
	};
	
}
