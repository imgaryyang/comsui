package com.suidifu.bridgewater.handler.mielong;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.bridgewater.api.test.post.BaseApiTestPost;
import com.suidifu.bridgewater.handler.IRemittanceApplicationHandler;
import com.suidifu.giotto.handler.FastDataWithTemperatureHandler;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.service.relation.RemittanceRelationCacheService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationDetailService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml"})
@Transactional
@Rollback(false)
public class AbstractNotRollBackBaseTest extends BaseApiTestPost{
	@Autowired
	protected IRemittanceApplicationService iRemittanceApplicationService;
	@Autowired
	protected IRemittanceApplicationHandler iRemittanceApplicationHandler;
	@Autowired
	protected IRemittanceApplicationDetailService iRemittanceApplicationDetailService;
	@Autowired
	protected IRemittancePlanService iRemittancePlanService;
	@Autowired
	protected FastHandler fastHandler;
	@Autowired
	protected StringRedisTemplate stringRedisTemplate;
	@Autowired
	protected RemittanceRelationCacheService remittanceRelationCacheService;
	@Autowired
	protected FastDataWithTemperatureHandler fastDataWithTemperatureHandler;
	@Autowired
	protected GenericDaoSupport genericDaoSupport;
}
