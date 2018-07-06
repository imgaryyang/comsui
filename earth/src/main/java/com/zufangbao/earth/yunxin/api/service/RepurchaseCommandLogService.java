package com.zufangbao.earth.yunxin.api.service;

import com.demo2do.core.service.GenericService;
import com.zufangbao.earth.yunxin.api.model.command.RepurchaseCommandModel;
import com.zufangbao.earth.yunxin.api.model.logs.RepurchaseCommandLog;
import com.zufangbao.gluon.exception.ApiException;

/**
 * 回购回购申请提交及撤销LogService
 * @author xwq
 *
 */
public interface RepurchaseCommandLogService  extends GenericService<RepurchaseCommandLog> {
	/**
     * 校验请求编号是否唯一
     * @param requestNo 请求编号
     * @throws ApiException 若请求编号不唯一，抛出异常:请求编号重复
     */
	public void checkByRequestNo(String requestNo) throws ApiException;
	/**
     * 校验批次号是否唯一
     * @param batchNo 批次号
	 * @param transactionType TODO
     * @throws ApiException 若批次号不唯一，抛出异常:批次号重复
     */
	public void checkByBatchNo(String batchNo, int transactionType) throws ApiException;
	
	public void saveRepurchaseCommandLog(RepurchaseCommandModel model, String ip);
	
	/**
     * 根据批次号查找回购申请提交记录
     * @param batchNo 批次号
     */
	public RepurchaseCommandLog getRepurchaseCommandLogByBatchNo(String batchNo);


}
