package com.zufangbao.earth.config.accountbalance;

import com.zufangbao.earth.aop.accountbalance.BoscDirectBankAop;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configurable
@EnableAspectJAutoProxy
public class BoscAccoutBalanceConfig {

    @Bean(name = "boscDirectBankAop")
    public BoscDirectBankAop boscDirectBankAop() {
        return new BoscDirectBankAop();
    }
}
