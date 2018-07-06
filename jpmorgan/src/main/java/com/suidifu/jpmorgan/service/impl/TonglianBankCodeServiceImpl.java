package com.suidifu.jpmorgan.service.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.jpmorgan.entity.TonglianHeshengBank;
import com.suidifu.jpmorgan.service.TonglianBankCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dafuchen
 *         2018/1/19
 */
@Component("tonglianBankCodeService")
public class TonglianBankCodeServiceImpl extends GenericServiceImpl<TonglianHeshengBank>
        implements TonglianBankCodeService {
    private static final Logger logger = LoggerFactory.getLogger(TonglianBankCodeServiceImpl.class);

    @Override
    @Cacheable("tonglianHeshengBank")
    public Map<String, String> getHengshengTonglianBankCode() {
        logger.info("start cache tonglianHeshengBank");
        List<TonglianHeshengBank> tonglianHeshengBankList = this.loadAll(TonglianHeshengBank.class);
        Map<String, String> hengshengTonglianBankCode = new HashMap<>(50);

        for (TonglianHeshengBank tonglianHeshengBank : tonglianHeshengBankList) {
            String heshengCode = tonglianHeshengBank.getHengshengBankCode();
            String tonglianCode = tonglianHeshengBank.getTonglianBankCode();
            hengshengTonglianBankCode.put(heshengCode, tonglianCode);
        }
        return hengshengTonglianBankCode;
    }

    @Override
    @CacheEvict(value = "tonglianHeshengBank", allEntries = true, beforeInvocation=true)
    public void evictCachedBanks() {
        logger.info("evict cache for tonglianHeshengBank");
    }
}
