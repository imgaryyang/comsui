package com.zufangbao.earth.update.wrapper;


public interface IUpdateWrapper<T> {
	/**
	 * 根据查询参数，包装更新语句并输出
	 * @param paramsBean 查询参数bean
	 * @param request 
	 * @param response 
	 * @param exportEventLogModel 
	 * @throws Exception 
	 */
	public String wrap(T paramsBean) throws Exception;
	

	
}