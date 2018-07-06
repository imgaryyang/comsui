package com.suidifu.mq.test.hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.suidifu.mq.hash.ConsistentHash;
import com.suidifu.mq.hash.HashFunction;
import com.suidifu.mq.hash.hashfunction.MurmurHashFunction;
import com.suidifu.mq.rpc.CodecType;
import com.suidifu.mq.rpc.client.ConsistentHashRPCClient;

@RunWith(BlockJUnit4ClassRunner.class)
public class TestHash {

	private int serviceCount = 20;
	private String serviceName = "helloservice";
	private ConsistentHashRPCClient rpcClient;
	private int businessListSize = 1000;
	private HashMap<String, ArrayList<String>> businessMap;
	private HashFunction hashFunction;
	private long time;

	@Before
	public void init() {
		time = System.currentTimeMillis();
		hashFunction = new MurmurHashFunction();
		//hashFunction = new Md5HashFunction();
		init_rpc_client();
		init_business_map();
	}

	@After
	public void after() {
		System.out.println("耗时：" + (System.currentTimeMillis() - time));
	}

	// 测试分组后的id列表是否进入同一个队列
	@Test
	public void test() {
		Iterator<Entry<String, ArrayList<String>>> iterator = businessMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, ArrayList<String>> entry = iterator.next();
			// String groupkey = entry.getKey();
			ArrayList<String> queueList = entry.getValue();
			String expected_queue = null;
			int i = 0;
			for (String uuid : queueList) {
				String queue = this.rpcClient.rabbitTemplate(uuid).getRoutingKey();
				if (i == 0)
					expected_queue = queue;
				Assert.assertEquals("没有进入同一个队列", expected_queue, queue);
				i++;
			}
		}
	}

	// 初始化kyro rpc
	private void init_rpc_client() {
		ArrayList<RabbitTemplate> rabbitTemplateList = new ArrayList<RabbitTemplate>(serviceCount);
		for (int i = 1; i <= serviceCount; i++) {
			RabbitTemplate rabbitTemplate = new RabbitTemplate();
			rabbitTemplate.setQueue(String.format("queue-%s-request-%s", serviceName, i));
			rabbitTemplate.setReplyAddress(String.format("queue-%s-reply-%s", serviceName, i));
			rabbitTemplate.setRoutingKey(String.format("queue-%s-request-%s", serviceName, i));
			rabbitTemplateList.add(rabbitTemplate);
		}
		this.rpcClient = new ConsistentHashRPCClient(CodecType.KYRO, new ConsistentHash(rabbitTemplateList, hashFunction));
	}

	// 切分group,相同队列切分在一组
	private void init_business_map() {
		businessMap = new HashMap<String, ArrayList<String>>();
		for (int i = 0; i < businessListSize; i++) {
			String uuid = i + "-qd2jmod21nfop121nmd2";
			String groupkey = this.rpcClient.selectQueue(uuid);
			ArrayList<String> queueList = businessMap.get(groupkey);
			if (queueList == null)
				businessMap.put(groupkey, new ArrayList<String>());
			else
				queueList.add(uuid);
		}
	}
}
