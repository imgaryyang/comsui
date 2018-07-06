package com.suidifu.morganstanley.service;

import com.suidifu.morganstanley.model.request.MutableFee;
import com.zufangbao.gluon.exception.ApiException;

/**
 * Created by hwr on 17-10-23.
 */
public interface MutableFeeLogService {
    /**
     * 校验请求编号是否唯一
     * @param requestNo 请求编号
     * @throws ApiException 若请求编号不唯一，抛出异常:请求编号重复
     */
    void checkByRequestNo(String requestNo) throws ApiException;

    void saveMutableFeeLog(MutableFee mutableFee, String ip, String resultMsg);
}
