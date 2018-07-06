package com.suidifu.bridgewater.handler.mielong;

import com.demo2do.core.utils.JsonUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RemittanceRedisTest extends AbstractNotRollBackBaseTest {

	@Test
	public void test11() {
		List<String> list = Arrays.asList("x423453543", "x32523523523523");
		stringRedisTemplate.opsForValue().set("35252352323523_ra", JsonUtils.toJsonString(list));
		String listString = stringRedisTemplate.opsForValue().get("35252352323523_ra");
		List<String> list2 = JsonUtils.parseArray(listString, String.class);
		System.out.println(list2);
	}

	@Test
	public void test12() {
		String[] stringList = {"1x423453543", "1x32523523523523"};
		stringRedisTemplate.opsForSet().add("111222_ra", stringList);
		Set<String> setString = stringRedisTemplate.opsForSet().members("111222_ra");

		System.out.println(setString);
	}

}
