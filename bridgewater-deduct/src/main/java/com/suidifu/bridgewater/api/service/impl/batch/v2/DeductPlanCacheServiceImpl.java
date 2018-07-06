package com.suidifu.bridgewater.api.service.impl.batch.v2;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.suidifu.bridgewater.api.service.batch.v2.DeductPlanCacheService;
import com.zufangbao.gluon.util.Assert;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;

@Service("DeductPlanCacheService")
public class DeductPlanCacheServiceImpl implements DeductPlanCacheService {
	
	private static final String BATCH_DEDUCT_TO_DEDUCT_PLAN_MAPPING_PREFIX = "bda:dp:";

	private static final Log logger = LogFactory.getLog(DeductPlanCacheServiceImpl.class);

	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private DeductPlanService deductPlanService;


	@Override
	public void pushDeductPlanUuidsToCache(String batchDeductApplicationUuid, List<String> deductPlanUuid) {
		
		try {

			Assert.notBlank(batchDeductApplicationUuid);;
			
			Assert.notEmpty(deductPlanUuid);;
			
			String batchDeductApplicationKey = getBatchDeductApplicationKey(batchDeductApplicationUuid);

			BoundListOperations<String, String> ops = redisTemplate.boundListOps(batchDeductApplicationKey);
				
			for (String dpUuid : deductPlanUuid) {
				
				ops.rightPush(dpUuid);
			}
			
			ops.expire(7, TimeUnit.DAYS);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("occur error when saveDeductPlan in cache!");
		}
		
	}

	private String getBatchDeductApplicationKey(String batchDeductApplicationUuid) {
		return BATCH_DEDUCT_TO_DEDUCT_PLAN_MAPPING_PREFIX+batchDeductApplicationUuid;
	}

	@Override
	public List<String> popAllDeductPlanUuidResult(String batchDeductApplicationUuid) {
		
		Assert.notBlank(batchDeductApplicationUuid);

		String batchDeductApplicationKey = getBatchDeductApplicationKey(batchDeductApplicationUuid);
		
		List<String> deductPlanUuidList = redisTemplate.opsForList().range(batchDeductApplicationKey, 0, -1);
		
		if(CollectionUtils.isEmpty(deductPlanUuidList)) {

			return deductPlanService.getDeductPlanUuidsByBatchDeductApplicationUuid(batchDeductApplicationUuid);
		}
	
		return deductPlanUuidList;
		
	}
	
}
