package com.zufangbao.earth.yunxin.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.sun.yunxin.exception.DictionaryNotExsitException;
import com.zufangbao.sun.yunxin.service.DictionaryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

		"classpath:/local/applicationContext-*.xml",
})
public class DictionaryServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private DictionaryService dictionaryService;
	
	@Test
	@Sql("classpath:test/yunxin/dictionary/GetDictionaryByCode.sql")
	public void testGetDictionaryByCode() {
		String content = "http://192.168.24.52:86/SendSMSFor5D/Send";
		String code = DictionaryCode.SMS_URL.getCode();
		Dictionary dictionary = null;
		try {
			dictionary = dictionaryService.getDictionaryByCode(code);
		} catch (DictionaryNotExsitException e) {
			Assert.fail();
		}
		Assert.assertNotNull(dictionary);
		Assert.assertEquals(code, dictionary.getCode());
		Assert.assertEquals(content, dictionary.getContent());
	}
	
	@Test
	@Sql("classpath:test/yunxin/dictionary/GetDictionaryByCode_Null.sql")
	public void testGetDictionaryByCode_Null() {
		List<Dictionary> all = dictionaryService.loadAll(Dictionary.class);
		Assert.assertTrue(CollectionUtils.isEmpty(all));
		Dictionary dictionary = null;
		try {
			dictionary = dictionaryService.getDictionaryByCode("");
			Assert.fail();
		} catch (DictionaryNotExsitException e) {
			Assert.assertNull(dictionary);
		}
	}
	@Test
	@Sql("classpath:test/yunxin/dictionary/UpdateAllowedSendStatus.sql")
	public void test_UpdateAllowedSendStatus() {
		List<Dictionary> all = dictionaryService.loadAll(Dictionary.class);
//		Assert.assertEquals("test/true", all.get(0).getContent());
		dictionaryService.updateAllowedSendStatus(false);
		List<Dictionary> all1 = dictionaryService.loadAll(Dictionary.class);
		Assert.assertEquals("true", all1.get(0).getContent());
		
	}
	@Test
	@Sql("classpath:test/yunxin/dictionary/GetPlatformDeductPrivateKey.sql")
	public void test_GetPlatformDeductPrivateKey() {
		String result = dictionaryService.getPlatformDeductPrivateKey();
		Assert.assertEquals("test/send", result);
	}
	@Test
	@Sql("classpath:test/yunxin/dictionary/GetSmsAllowSendFlag.sql")
	public void test_GetSmsAllowSendFlag() {
		boolean result = dictionaryService.getSmsAllowSendFlag();
		Assert.assertEquals(true, result);
	}
}
