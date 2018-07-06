package com.zufangbao.earth.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.zufangbao.earth.api.GatewayType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractType;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.RepaymentExecutionState;

public class EnumUtilTest {

	@Test
	public void test_getKVMap(){
		
		//空
		Map<Object, Integer> map = EnumUtil.getKVMap(null);
		Assert.assertEquals(Collections.EMPTY_MAP, map);
		
		//非枚举类
		map = EnumUtil.getKVMap(FinancialContract.class);
		Assert.assertEquals(Collections.EMPTY_MAP, map);
		
		//枚举类，无getChineseMessage方法
		map = EnumUtil.getKVMap(GatewayType.class);
		Assert.assertEquals(Collections.EMPTY_MAP, map);
		
		//枚举类，有getChineseMessage方法
		map = EnumUtil.getKVMap(FinancialContractType.class);
		Assert.assertEquals(new Integer(0), map.get("消费贷款"));
		Assert.assertEquals(new Integer(1), map.get("小企业微贷"));
		
		List<Map<String, Object>> l1 = EnumUtil.getKVListIncludes(RepaymentExecutionState.class, Arrays.asList(RepaymentExecutionState.INVALID));
		Assert.assertEquals(1, l1.size());
		List<Map<String, Object>> l2 = EnumUtil.getKVListExcludes(RepaymentExecutionState.class, Arrays.asList(RepaymentExecutionState.INVALID));
		Assert.assertEquals(8, l2.size());
	}
}
