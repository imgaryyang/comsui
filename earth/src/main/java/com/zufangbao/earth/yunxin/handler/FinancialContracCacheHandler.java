package com.zufangbao.earth.yunxin.handler;

import com.zufangbao.sun.entity.financial.FinancialContract;

import java.util.List;
import java.util.Map;

public interface FinancialContracCacheHandler {
    List<FinancialContract> getAvailableFinancialContractList(Long princialId);

    void cacheEvict(Long princialId);

    void allCacheEvict();

    Map<String, String> getFinancialContractConfigurationList(String financialContractUuid);
}
