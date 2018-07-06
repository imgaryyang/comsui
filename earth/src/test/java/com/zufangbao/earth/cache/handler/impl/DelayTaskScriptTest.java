package com.zufangbao.earth.cache.handler.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;

/**
 * Created by fanxiaofan on 2017/4/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/local/applicationContext-*.xml" })
@Transactional
@Rollback(true)
public class DelayTaskScriptTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;

	@Autowired
    private StringRedisTemplate stringRedisTemplate;

	@Test
	@Sql("classpath:test/yunxin/productCategory/test_script_DelayTaskScriptTest.sql")
	public void test_script_DelayTaskScriptTest() {
		String[] delayTaskConfigUuids = { "598ef68c-3927-11e7-ac39-2b4f0a474890",
				"8ffcaf61-3940-11e7-ab82-525400dbb013", "374da0c4-3935-11e7-952e-ba77244e1da4",
				"402fd71a-393b-11e7-bf99-00163e002839", "8aec6aa6-46ac-11e7-881a-b208103af10b" };
		for (String delayTaskConfigUuid : delayTaskConfigUuids) {
			System.out.println(delayTaskConfigUuids);
	    	stringRedisTemplate.delete("sr:"+delayTaskConfigUuid);

			DelayTaskServices delayTaskServices = (DelayTaskServices) delayTaskConfigCacheHandler
					.getCompiledObjectDelayTaskConfigUuid(delayTaskConfigUuid);
			if(null==delayTaskServices){
				System.out.println(delayTaskConfigUuid);
				Assert.fail();
			}
		}
	}

}