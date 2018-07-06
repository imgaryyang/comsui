package com.zufangbao.earth.util;


import org.junit.Assert;
import org.junit.Test;

import com.zufangbao.sun.utils.AgeUtil;

public class AgeUtilTest {

	@Test
	public void test_getgetAgeByIdCardNum(){
	String birthString = "341222199311202423";
	int age = AgeUtil.getAgeByIdCardNum(birthString);
	System.out.println(age);
	Assert.assertEquals(23, age);
	}
	
	@Test
	public void test_getgetAgeByIdCardNum_err1(){
		String birthString = "";
		int age = AgeUtil.getAgeByIdCardNum(birthString);
		System.out.println(age);
		Assert.assertEquals(0, age);
	}
	
	@Test
	public void test_getgetAgeByIdCardNum_err2(){
		String birthString = "34122219931120242";
		int age = AgeUtil.getAgeByIdCardNum(birthString);
		System.out.println(age);
		Assert.assertEquals(0, age);
	}
	
	@Test
	public void test_getgetAgeByIdCardNum_err4(){
		String birthString = "341222000000002423";
		int age = AgeUtil.getAgeByIdCardNum(birthString);
		System.out.println(age);
		Assert.assertEquals(2014, age);
	}
	
	@Test
	public void test_getgetAgeByIdCardNum_err3(){
		try {
			String birthString = "341222299311202423";
			int age = AgeUtil.getAgeByIdCardNum(birthString);
			System.out.println(age);
		} catch (Exception e) {
			Assert.assertEquals("年龄不能超过当前日期", e.getMessage());
		}
	}
}
