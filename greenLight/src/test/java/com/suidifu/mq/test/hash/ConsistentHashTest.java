package com.suidifu.mq.test.hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.alibaba.fastjson.JSON;
import com.suidifu.mq.hash.ConsistentHash;

public class ConsistentHashTest {
	
	private String serviceName = "helloservice";

	@Test
	public void testGenerateHashKey() {
		
		int serviceCount = 8;
		
		Map<Integer,String> queueUuidMap = new HashMap<Integer,String>();
		
		ArrayList<RabbitTemplate> rabbitTemplateList = new ArrayList<RabbitTemplate>(serviceCount);
		for (int i = 1; i <= serviceCount; i++) {
			RabbitTemplate rabbitTemplate = new RabbitTemplate();
			rabbitTemplate.setQueue(String.format("queue-%s-request-%s", serviceName, i));
			rabbitTemplate.setReplyAddress(String.format("queue-%s-reply-%s", serviceName, i));
			rabbitTemplate.setRoutingKey(String.format("queue-%s-request-%s", serviceName, i));
			rabbitTemplateList.add(rabbitTemplate);
		}
		
		ConsistentHash consistentHash = new ConsistentHash(rabbitTemplateList);
		
		while(queueUuidMap.keySet().size() <=8) {
			
			String uuid = UUID.randomUUID().toString();
			
			RabbitTemplate rabbitTemplate  = consistentHash.getNode(uuid);
			
			int index = indexRabbitTemplate(rabbitTemplateList, rabbitTemplate);
			
			if(index != -1) {
				
				queueUuidMap.put(index, uuid);
			}
		}
		
		System.out.println(JSON.toJSON(queueUuidMap));
		
	}
	private int indexRabbitTemplate(List<RabbitTemplate> rabbitTemplateList,RabbitTemplate rabbitTemplate) {
		
		for (int index = 0; index<rabbitTemplateList.size();index++) {
			
			RabbitTemplate item = rabbitTemplateList.get(index);
			
			if(item.getRoutingKey().equals(rabbitTemplate.getRoutingKey())) {
				
				return index;
			}
		}
		return -1;
	}

}
