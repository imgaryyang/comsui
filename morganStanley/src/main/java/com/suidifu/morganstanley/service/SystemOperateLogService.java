package com.suidifu.morganstanley.service;

import com.suidifu.morganstanley.model.request.MutableFee;

/**
 * Created by hwr on 17-10-23.
 */
public interface SystemOperateLogService {
    void saveMutableFeeSystemLog(String assetUuid, MutableFee mutableFee, String ip, Long userId);
}
