package com.suidifu.jpmorgan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.suidifu.jpmorgan.entity.BusinessStatus;
import com.suidifu.jpmorgan.entity.CommunicationStatus;
import com.suidifu.jpmorgan.entity.GatewaySlot;
import com.suidifu.jpmorgan.entity.GatewayType;
import com.suidifu.jpmorgan.entity.SlotInfo;
import com.suidifu.jpmorgan.entity.TradeSchedule;
import com.suidifu.jpmorgan.spec.TradeScheduleHandlerSpec;
import com.suidifu.jpmorgan.util.JsoupUtils;
import com.suidifu.jpmorgan.util.UUIDUtil;

public class TradeScheduleTest {

	@Test
	public void testConfig() {
		String updateSlotExeTemplate = "UPDATE trade_schedule SET access_version =:accessVersion, %s_gateway_type =:gatewayType,"
				+ " %s_effective_absolute_time =:effectiveTime, %s_gateway_config =:gatewayConfig, "
				+ "%s_transaction_amount =:transactionAmount "
				+ "WHERE access_version =:preAccessVersion AND tradeUuid =:tradeUuid AND %s_business_status=:inqueueStatus AND %s_communication_status:inqueueStatus ";
		String update = updateSlotExeTemplate.replace("%s", "fst");
	}

	@Test
	public void testJSON() {

		TradeSchedule tradeSchedule = new TradeSchedule();
		tradeSchedule.setBusinessStatus(BusinessStatus.Abandon.ordinal());
		System.out.println(JSON.toJSONString(tradeSchedule));

		TradeSchedule tradeSchedule1 = JSON.parseObject(
				JSON.toJSONString(tradeSchedule), TradeSchedule.class);

		Assert.assertEquals(BusinessStatus.Abandon,
				tradeSchedule1.getBusinessStatus());

	}

	@Test
	public void test1() {
		System.out.println(UUID.randomUUID().toString());
		System.out.println(UUID.randomUUID().toString());
		System.out.println(UUID.randomUUID().toString());
		System.out.println(UUID.randomUUID().toString());
		System.out.println(UUID.randomUUID().toString());
		System.out.println(UUID.randomUUID().toString());
		System.out.println(UUID.randomUUID().toString());
		System.out.println(UUID.randomUUID().toString());
	}

	@Test
	public void test2() {
		Map<String, String> urls = new HashMap<String, String>();
		urls.put(TradeScheduleHandlerSpec.DistributeURLKey,
				"http://192.168.1.109:9090/paymentOrder/distribute");
		urls.put(TradeScheduleHandlerSpec.BusinessStatusUpdateURLKey,
				"http://192.168.1.109:9090/paymentOrder/queryStatus");
		System.out.println(JSON.toJSONString(urls));
	}

	@Test
	public void test3() {
		Result result = new Result();
		GatewaySlot gatewaySlot = new GatewaySlot("11111111",
				GatewayType.SuperBank, new BigDecimal("0.01"),
				CommunicationStatus.Success, new Date(), new Date(),
				new Date(), BusinessStatus.Success, new Date(), "aaaa", "");

		result.success().data("gatewaySlot", gatewaySlot);
		
		String jsonString = JSON.toJSONString(result);
		
		System.out.println(jsonString);
		
		Result result2 = JSON.parseObject(jsonString, Result.class);
		
		Object object = result2.getData().get("gatewaySlot");

		System.out.println(JSON.toJSONString(object));
		GatewaySlot gatewaySlot2 = JSON.parseObject(object.toString(), GatewaySlot.class);
		System.out.println(object);
		
	}
	
	@Test
	public void test4(){
		SlotInfo slotInfo = new SlotInfo("66354a6e-563e-4d6c-97c6-c8b3931aa67c", 1, "e6273fd0-7d71-4208-a6ff-3dc1d3bc41f3", GatewayType.UnionPay, "25821cae-2537-48fa-9722-5fa9d48ba02d", new Date(), new BigDecimal("0.02"));
		System.out.println(JSON.toJSONString(slotInfo));
	}
	
//	@Test
//	public void test5() throws Exception{
//		
//		SlotInfo slotInfo = new SlotInfo("e5f1aec6-c76f-4447-a28c-58ca9015f932", 2, UUID.randomUUID().toString(), GatewayType.SuperBank, "f8bb9956-1952-4893-98c8-66683d25d7ce", new Date(), new BigDecimal("10000"));
//		String post = JsoupUtils.post("http://192.168.1.102:9090/jpmorgan/tradeSchedule/updateSlot", "reqPacket", JSON.toJSONString(slotInfo));
//		System.out.println(post);
//	}
	
	@Test
	public void test() throws Exception {

		long sTime = System.currentTimeMillis();
		Result post = JsoupUtils.post("http://192.168.1.119:9090/jpmorgan/tradeSchedule/queryStatus", "transactionUuid");
		System.out.println("query use time....,."+ (System.currentTimeMillis()-sTime));
		
		System.out.println(JSON.toJSONString(post));

	}

	@Test
	public void test5() throws Exception {
		List<Integer> modPriority = new ArrayList<Integer>() {
			{
				add(3);
				add(5);
				//add(5);
		   }
		};
		
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("mod", modPriority);
		
		String jsonString = JSON.toJSONString(parms);
		System.out.println(jsonString);
		
		Map<String, Object> parms2 = (Map<String, Object>) JSON.parse(jsonString);
		
		List<Integer> modPriority2 = (List<Integer>) parms2.get("mod");
		System.out.println(modPriority2.get(0));
	}
	
	@Test
	public void test6() throws Exception {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
		
		Map<String, String> andCon = new HashMap<String, String>();
		Map<String, String> orCon = new HashMap<String, String>();
		
		andCon.put("f9f04fc2-01eb-4d0d-b512-387bfcca85b4", "Success");
		andCon.put("d5f87eb0-04bc-46ab-87cd-4ea8bd1dc201", "Success");
		
//		orCon.put("f9f04fc2-01eb-4d0d-b512-387bfcca85b4", "Success");
//		orCon.put("d5f87eb0-04bc-46ab-87cd-4ea8bd1dc201", "Success");
		
		map.put("and", andCon);
		map.put("or", orCon);
		
		System.out.println(JSON.toJSONString(map));
		
		String jsonString = JSON.toJSONString(map);
		
		Map<String, Map<String, String>> map1 = JSON.parseObject(jsonString, Map.class);
		
		Map<String, String> andCon1 = map1.get("and");
		
		System.out.println(andCon1);
	}
	
	@Test
	public void test7() {
		
		System.out.println(UUIDUtil.uuid());
		
		System.out.println(BusinessStatus.Success.toString());
	}
	
	@Test
	public void test8() {
		
		List<String> list = new ArrayList<String>();
		list.add("aaaaaaaa");
		list.add("bbbbbbbb");
		list.add("cccccccc");
		list.add("dddddddd");
		
		System.out.println(JSON.toJSONString(list));
	}
	
	@Test
	public void test9() {
		Map<String,String> map = new HashMap<String, String>();
		
		map.put("aa", "fafdsa");
		map.put("bb", "ssss");
		
		
		Map<String,String> map2 = new HashMap<String, String>();
		
		map2.put("ccc", "fasfdsafdas");
		map2.put("ddd", JSON.toJSONString(map));
		
		System.out.println(JSON.toJSONString(map2));
		
	}
	
	@Test
	public void test10 () {
		String ss = "{\"abandon\":[\"9555cd24-dd76-44f7-a967-59e59f266d46\"],\"start\":[\"9555cd24-dd76-44f7-a967-59e59f266d46\"]}";
		Map<String, List<String>> map = JSON.parseObject(ss, Map.class);
		System.out.println(map);
		System.out.println(JSON.toJSONString(map));
	}

	@Test
	public void test11 () {
		
		System.out.println(String.format("%010d", 23));
	}
	
	@Test
	public void test12 () {
		
		String content = "发动机撒开";
		  String regex = "\\((.*?)\\)";
		  Pattern p = Pattern.compile(regex);
		  Matcher m = p.matcher(content);
		  if(m.find(0)) {
			  System.out.println(m.group(1));
		  }
	}
	
	@Test
	public void test13 () {
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("qqq", "www");
		map1.put("eee", "rrr");
		
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("ttt", "yyy");
		map2.put("uuu", "iii");
		
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(map1);
		list.add(map2);
		
		System.out.println(JSON.toJSONString(list));;
		
	}
}
