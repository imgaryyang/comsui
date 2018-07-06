package com.suidifu.watchman;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author louguanyang at 2018/1/9 10:53
 * @mail louguanyang@hzsuidifu.com
 */
@org.jetbrains.annotations.TestOnly
@CacheConfig(cacheNames = "test")
public interface BankRepository extends JpaRepository<Bank, Long> {

    @Cacheable()
    @org.jetbrains.annotations.TestOnly
    public Bank findByBankCode(String name);

}
