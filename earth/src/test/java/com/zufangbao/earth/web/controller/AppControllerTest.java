package com.zufangbao.earth.web.controller;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.web.controller.app.AppController;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.AppService;
import com.zufangbao.sun.yunxin.entity.model.AppInfoShow;
import com.zufangbao.sun.yunxin.entity.model.AppQueryModel;
import com.zufangbao.sun.yunxin.entity.model.CreateAppModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@WebAppConfiguration(value="webapp")
@Transactional
public class AppControllerTest {
	@Autowired
	private AppController appController;
	@Autowired
	private AppService appService;

	@Test
	@Sql("classpath:/test/yunxin/app/test4AppController.sql")
	public void test_searchApp(){
		String resultStr = appController.searchApp(null, null);
		Result result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("查询错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		AppQueryModel queryModel = new AppQueryModel();
		resultStr = appController.searchApp(queryModel, null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		Assert.assertEquals(8, result.get("size"));
		
		Page page = new Page(0,5);
		resultStr = appController.searchApp(queryModel, page);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		Assert.assertEquals(8, result.get("size"));
		List<App> apps = JSON.parseArray(result.get("list").toString(), App.class);
		Assert.assertEquals(5, apps.size());
		
		AppQueryModel queryModel1 = new AppQueryModel();
		queryModel1.setAppId("nongfenqi1");
		queryModel1.setAppName("测试分期1");
		queryModel1.setCompanyFullName("fullName1");
		resultStr = appController.searchApp(queryModel1, null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		Assert.assertEquals(1, result.get("size"));
		apps = JSON.parseArray(result.get("list").toString(), App.class);
		Assert.assertEquals(new Long(1), apps.get(0).getId());
	}
	
	@Test
	@Sql("classpath:/test/yunxin/app/test4AppController.sql")
	public void test_createApp(){
		Principal principal = new Principal();
		MockHttpServletRequest request = new MockHttpServletRequest();
		String resultStr = "";
		Result result = null;
		
		resultStr = appController.createApp(null, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		CreateAppModel queryModel = new CreateAppModel();
		queryModel.setAppId("test1");
		resultStr = appController.createApp(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		queryModel = new CreateAppModel();
		queryModel.setAppId("nongfenqi1");
		queryModel.setAppName("测试1");
		queryModel.setCompanyFullName("随地付");
		queryModel.setAddress("杭州");
		resultStr = appController.createApp(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("商户简称或商户代码已存在！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		queryModel = new CreateAppModel();
		queryModel.setAppId("test1");
		queryModel.setAppName("测试1");
		queryModel.setCompanyFullName("随地付");
		queryModel.setAddress("杭州");
		resultStr = appController.createApp(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("成功",  result.getMessage());
		Assert.assertEquals("0",  result.getCode());
		App app = appService.getApp("test1");
		Assert.assertNotNull(app);
		
	}
	
	@Test
	@Sql("classpath:/test/yunxin/app/test4AppController_modify.sql")
	public void test_modifyApp(){
		Principal principal = new Principal();
		MockHttpServletRequest request = new MockHttpServletRequest();
		String resultStr = "";
		Result result = null;
		
		resultStr = appController.modifyApp(null, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		CreateAppModel queryModel1 =new CreateAppModel();
		queryModel1.setAppId("test1");
		resultStr = appController.modifyApp(queryModel1, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());

		CreateAppModel queryModel2 =new CreateAppModel();
		queryModel2.setId(1l);
		queryModel2.setAppName("优帕克");
		queryModel2.setAppId("wewqewq");
		queryModel2.setCompanyFullName("fullName1");
		queryModel2.setAddress("上海");
		resultStr = appController.modifyApp(queryModel2, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("商户简称或商户代码已存在！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
			
		CreateAppModel queryModel3 =new CreateAppModel();
		queryModel3.setId(3l);
		queryModel3.setAppId("Name1");
		queryModel3.setAppName("啦啦啦");
		queryModel3.setCompanyFullName("fullName1");
		queryModel3.setAddress("上海");
		resultStr = appController.modifyApp(queryModel3, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("数据有误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		CreateAppModel queryModel =new CreateAppModel();
		queryModel.setId(1l);
		queryModel.setAppName("好123");
		queryModel.setAppId("xiaoyu");
		queryModel.setCompanyFullName("fullName1");
		queryModel.setAddress("上海");
		resultStr = appController.modifyApp(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("成功",  result.getMessage());
		Assert.assertEquals("0",  result.getCode());
		List<App> app = appService.loadAll(App.class);
		Assert.assertEquals(app.get(0).getName(),"好123");
		
	}
	
	@Test
	@Sql("classpath:/test/yunxin/app/test4AppController_getDetails.sql")
	public void test_detail(){
		String resultStr = "";
		Result result = null;
		String appId = null;
		resultStr = appController.detail(appId);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("信托商户信息详情获取失败！！！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		appId = "fdagfadg";
		resultStr = appController.detail(appId);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertNull(result.get("app"));
		Assert.assertEquals("0",  result.getCode());
		
		appId = "xiaoyu";
		resultStr = appController.detail(appId);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		List<AppInfoShow> shows1 = JSON.parseArray(result.get("appInfoShows").toString(), AppInfoShow.class);
		Assert.assertEquals(2,shows1.size());
		Assert.assertEquals("资产方",shows1.get(0).getRole());
		Assert.assertEquals("VACC27438CADB442A6A0",shows1.get(0).getVirtualAccountNo());
		Assert.assertEquals("资产方",shows1.get(1).getRole());
		Assert.assertEquals("VACC27438CADB442A6A0",shows1.get(1).getVirtualAccountNo());
		
		appId = "suidifu";
		resultStr = appController.detail(appId);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		List<AppInfoShow> shows2 = JSON.parseArray(result.get("appInfoShows").toString(), AppInfoShow.class);
		Assert.assertEquals(2,shows2.size());
		Assert.assertEquals("资金方",shows2.get(0).getRole());
		Assert.assertEquals("",shows2.get(0).getVirtualAccountNo());
		Assert.assertEquals("其他合作商户",shows2.get(1).getRole());
		Assert.assertEquals("",shows2.get(1).getVirtualAccountNo());
		
	}
}
