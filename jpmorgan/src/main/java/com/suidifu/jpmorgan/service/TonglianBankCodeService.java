package com.suidifu.jpmorgan.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.jpmorgan.entity.TonglianHeshengBank;

import java.util.Map;

/**
 * @author dafuchen
 *         2018/1/19
 */
public interface TonglianBankCodeService extends GenericService<TonglianHeshengBank> {
    /**
     * bank表数据（带缓存）
     */
    Map<String, String> getHengshengTonglianBankCode();

    /**
     * 清除bank表数据缓存
     */
    void evictCachedBanks();
}
