package com.zufangbao.earth.web.controller;

import com.zufangbao.sun.entity.tag.TagIdentityMap;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.web.controller.tag.TagController;
import com.zufangbao.sun.entity.tag.Tag;
import com.zufangbao.sun.service.tag.TagConfigService;
import com.zufangbao.sun.service.tag.TagService;
import com.zufangbao.sun.yunxin.entity.model.tag.TagQueryModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@WebAppConfiguration(value="webapp")
public class TagControllerTest{
	
	@Autowired
	private TagController tagController;
	
	@Autowired
	private TagService tagService;
	@Autowired
	private TagConfigService tagConfigService;
	
	@Test
	@Sql("classpath:/test/yunxin/tag/test4TagController.sql")
	public void testQuery(){
		String resultStr = tagController.query(null, null);
		Result result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(0, result.get("size"));
		Assert.assertEquals(Collections.emptyList(), result.get("list"));
		
		TagQueryModel tagQueryModel = new TagQueryModel();
		resultStr = tagController.query(tagQueryModel, null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(5, result.get("size"));
		List<Tag> tags = JSON.parseArray(result.get("list").toString(), Tag.class);
		Assert.assertEquals("1111115", tags.get(0).getUuid());
		
		tagQueryModel.setName("逾期1");
		resultStr = tagController.query(tagQueryModel, null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(1, result.get("size"));
		tags = JSON.parseArray(result.get("list").toString(), Tag.class);
		Assert.assertEquals("1111112", tags.get(0).getUuid());
		
		tagQueryModel.setName("逾期");
		Page page = new Page(1,2);
		resultStr = tagController.query(tagQueryModel, page);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(5, result.get("size"));
		tags = JSON.parseArray(result.get("list").toString(), Tag.class);
		Assert.assertEquals(2, tags.size());
		Assert.assertEquals("1111115", tags.get(0).getUuid());
		
	}
	
	@Test
	@Sql("classpath:/test/yunxin/tag/test4TagController.sql")
	public void testDetail() {
		String uuid = "";
		String resultStr = "";
		Result result = null;
		Tag tag = null;
		List<TagIdentityMap> tagIdentityMaps = null;
		
		resultStr = tagController.detail(uuid);
		result = JsonUtils.parse(resultStr, Result.class);
		tag = (Tag) result.get("tag");
		Assert.assertNull(tag);
//		tagConfigs = JSON.parseArray(result.get("tagConfigs").toString(), TagConfig.class);
//		Assert.assertEquals(Collections.emptyList(), tagConfigs);
		
		uuid = "xwq11111";
		resultStr = tagController.detail(uuid);
		result = JsonUtils.parse(resultStr, Result.class);
		tag = (Tag) result.get("tag");
		Assert.assertNull(tag);
//		tagConfigs = JSON.parseArray(result.get("tagConfigs").toString(), TagConfig.class);
//		Assert.assertEquals(Collections.emptyList(), tagConfigs);
		
		uuid = "1111111";
		resultStr = tagController.detail(uuid);
		result = JsonUtils.parse(resultStr, Result.class);
		tag = JSON.parseObject(result.get("tag").toString(),Tag.class);
		Assert.assertEquals("逾期", tag.getName());
//		tagConfigs = JSON.parseArray(result.get("tagConfigs").toString(), TagConfig.class);
//		Assert.assertEquals("sadfadfdfs", tagConfigs.get(0).getUuid());

	}

	@Test
	@Sql("classpath:/test/yunxin/tag/test4TagController_getTagConfigDetail.sql")
	public void testGetTagConfigData() {
		String uuid = "";
		String resultStr = "";
		Result result = null;
		List<TagIdentityMap> tagConfigs= null;

		resultStr = tagController.getTagConfigData(uuid,null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("标签数据获取失败！！！", result.getMessage());

		uuid = "1111111";
		resultStr = tagController.getTagConfigData(uuid,null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(3, result.get("size"));
		tagConfigs = JSON.parseArray(result.get("list").toString(), TagIdentityMap.class);
		Assert.assertEquals("dadfadfdfs", tagConfigs.get(0).getUuid());

		Page page = new Page(1,1);
		resultStr = tagController.getTagConfigData(uuid,page);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(3, result.get("size"));
		tagConfigs = JSON.parseArray(result.get("list").toString(), TagIdentityMap.class);
		Assert.assertEquals(1, tagConfigs.size());
		Assert.assertEquals("dadfadfdfs", tagConfigs.get(0).getUuid());
		
	}
	
	@Test
	@Sql("classpath:/test/yunxin/tag/test4TagController.sql")
	public void testEditTag() {
		String uuid = "";
		String name = "";
		String description = null;
		String deleteUuidJson = "";
		Principal principal = new Principal();
		MockHttpServletRequest request = new MockHttpServletRequest();
		String resultStr = "";
		Result result = null;
		
		
		resultStr = tagController.editTag(uuid,name,description,deleteUuidJson,principal,request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("-1", result.getCode());
		Assert.assertEquals("请输入标签名称！！", result.getMessage());
		
		uuid = "1111115";
		name = "逾期";
		resultStr = tagController.editTag(uuid,name,description,deleteUuidJson,principal,request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("-1", result.getCode());
		Assert.assertEquals("标签名称已存在！！", result.getMessage());
		
		uuid = "1111111";
		name = "逾期";
		description = "爱咋咋地";
		deleteUuidJson = "[\"sadfadfdfs\"]";
		resultStr = tagController.editTag(uuid,name,description,deleteUuidJson,principal,request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0", result.getCode());
		Tag tag = tagService.getTagById(new Long(1));
		Assert.assertEquals("爱咋咋地", tag.getDescription());
		TagIdentityMap tagIdentityMap = tagConfigService.getTagConfigById(new Long(1));
		Assert.assertNull(tagIdentityMap);
	}
	
	@Test
	@Sql("classpath:/test/yunxin/tag/test4TagController.sql")
	public void testCreateTag() {
		String name = "";
		String description = null;
		Principal principal = new Principal();
		MockHttpServletRequest request = new MockHttpServletRequest();
		String resultStr = "";
		Result result = null;
		
		
		resultStr = tagController.createTag(name,description,principal,request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("-1", result.getCode());
		Assert.assertEquals("请输入标签名称！！", result.getMessage());
		
		name = "逾期";
		resultStr = tagController.createTag(name,description,principal,request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("-1", result.getCode());
		Assert.assertEquals("标签名称已存在！！", result.getMessage());
		
		name = "待确认";
		description = "你猜啥意思";
		resultStr = tagController.createTag(name,description,principal,request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0", result.getCode());
		Tag tag = tagService.getTagByUuidOrName(null, "待确认");
		Assert.assertEquals("你猜啥意思", tag.getDescription());
		
	}
	
	@Test
	@Sql("classpath:/test/yunxin/tag/test4TagController.sql")
	public void testDelete() {
		String uuid = "";
		String resultStr = "";
		Result result = null;
		
		
		resultStr = tagController.delete(uuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("-1", result.getCode());
		Assert.assertEquals("标签删除失败！！！", result.getMessage());
		
		uuid = "1111111";
		resultStr = tagController.delete(uuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0", result.getCode());
		Tag tag = tagService.getTagById(new Long(1));
		Assert.assertEquals(new Integer(1), tag.getStatus());
		TagIdentityMap tagIdentityMap = tagConfigService.getTagConfigById(new Long(1));
		Assert.assertEquals(new Integer(1), tagIdentityMap.getStatus());
		
		uuid = "1111117";
		resultStr = tagController.delete(uuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0", result.getCode());
		tag = tagService.getTagByUuidOrName("1111117", null);
		Assert.assertNull(tag);
		tagIdentityMap = tagConfigService.getTagConfigById(new Long(3));
		Assert.assertEquals(new Integer(1), tagIdentityMap.getStatus());
		
		
	}
}
