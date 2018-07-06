/**
 *
 */
package com.suidifu.hathaway.redis.serivice.impl.financial;

import com.suidifu.hathaway.redis.entity.financial.FastFinancialContract;
import com.suidifu.hathaway.redis.serivice.financial.FastFinancialContractService;
import com.suidifu.hathaway.redis.service.impl.GenericRedisServiceImpl;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

/**
 * @author wukai
 */
@Service("fastFinancialContractService")
public class FastFinancialContractServiceImpl extends GenericRedisServiceImpl<FastFinancialContract> implements FastFinancialContractService {

    @Override
    public Type type() {
        return FastFinancialContract.class;
    }


    @Override
    public ClusterOperations<String, String> opsForCluster() {
        return null;
    }
}
