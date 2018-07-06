package com.zufangbao.earth.web.controller.notice;

import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.notice.Notice;
import com.zufangbao.sun.entity.notice.NoticeStatus;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.NoticeService;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zsh
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml"})
@TransactionConfiguration
@WebAppConfiguration(value="webapp")
@Transactional
public class NoticeControllerTest extends AbstractTransactionalJUnit4SpringContextTests{
	@Autowired
	private NoticeController noticeController;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;
	
	@Test
	@Sql("classpath:/test/yunxin/notice/testAddNotice.sql")
	public void testAddNotice(){
		MockHttpServletRequest request =  new MockHttpServletRequest();
		Principal principal = new Principal();
		Notice notice = new Notice();
		notice.setContent("内容123");
		notice.setTitle("标题123");
		String response = noticeController.addNotice(principal, notice, request);
		notice = noticeService.list(Notice.class,new Filter().addEquals("title","标题123")).get(0);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("保存公告成功",result.getMessage());
		List<SystemOperateLog> systemOperateLogVOList = systemOperateLogService.getLogsByUuid(notice.getNoticeUuid());
		Assert.assertEquals(1, systemOperateLogVOList.size());
	}


    @Test
	@Sql("classpath:/test/yunxin/notice/testAddNotice.sql")
	public void testQueryNoticePage(){
	    Page page = new Page();
	    page.setEveryPage(5);
		String response = noticeController.queryNoticePage(page, false);	
		Result result = JsonUtils.parse(response,Result.class);
		List<Notice> noticeList = JsonUtils.parseArray(result.getData().get("list").toString(),Notice.class);
		Assert.assertEquals("标题3",noticeList.get(0).getTitle());
		Assert.assertEquals(4,noticeList.size());
		
		page = new Page();
	    page.setEveryPage(2);
		response = noticeController.queryNoticePage(page, false);	
		result = JsonUtils.parse(response,Result.class);
		noticeList = JsonUtils.parseArray(result.getData().get("list").toString(),Notice.class);
		Assert.assertEquals(2,noticeList.size());
	}

    @Test
	@Sql("classpath:/test/yunxin/notice/testAddNotice.sql")
	public void testEditNotice(){
		MockHttpServletRequest request =  new MockHttpServletRequest();
		Principal principal = new Principal();
		Notice notice = new Notice();
		
		notice.setNoticeUuid("dafd068f-7975-4320-b960-e3049d3ca846");
		notice.setContent("内容123");
		notice.setTitle("标题123");
		String response = noticeController.editNotice(principal, notice, request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("已发布公告和作废公告不允许编辑",result.getMessage());
		
		notice.setNoticeUuid("cc422abc-df26-431e-a0dd-caaf56d0bfac");
		notice.setContent("内容123");
		notice.setTitle("标题123");
		response = noticeController.editNotice(principal, notice, request);
		result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("已发布公告和作废公告不允许编辑",result.getMessage());
		
		notice.setNoticeUuid("9de257f7-b6cb-405d-8b9e-2328f4ea53fc");
		response = noticeController.editNotice(principal, notice, request);
		notice = noticeService.list(Notice.class,new Filter().addEquals("noticeUuid","9de257f7-b6cb-405d-8b9e-2328f4ea53fc")).get(0);
		result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("编辑公告成功",result.getMessage());
		Assert.assertEquals("标题123",notice.getTitle());
		Assert.assertEquals("内容123",notice.getContent());
		List<SystemOperateLog> systemOperateLogList = systemOperateLogService.getLogsByUuid(notice.getNoticeUuid());
		Assert.assertEquals(LogFunctionType.EDITNOTICE,systemOperateLogList.get(0).getLogFunctionType());
	}

    @Test
	@Sql("classpath:/test/yunxin/notice/testAddNotice.sql")
	public void testCancelNotice(){
		MockHttpServletRequest request =  new MockHttpServletRequest();
		Principal principal = new Principal();
		String uuid = "9de257f7-b6cb-405d-8b9e-2328f4ea53fc";
		String response = noticeController.cancelNotice(principal,uuid, "",request);
		Notice notice = noticeService.list(Notice.class,new Filter().addEquals("noticeUuid","9de257f7-b6cb-405d-8b9e-2328f4ea53fc")).get(0);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("作废公告成功",result.getMessage());
		Assert.assertEquals(NoticeStatus.INVALID,notice.getNoticeStatus());
		List<SystemOperateLog> systemOperateLogList = systemOperateLogService.getLogsByUuid(notice.getNoticeUuid());
		Assert.assertEquals(LogFunctionType.CANCELNOTICE,systemOperateLogList.get(0).getLogFunctionType());	
	}


    @Test
	@Sql("classpath:/test/yunxin/notice/testAddNotice.sql")
	public void testReleaseNotice(){
		MockHttpServletRequest request =  new MockHttpServletRequest();
		Principal principal = new Principal();
		Notice notice = new Notice();
		notice.setNoticeUuid("9de257f7-b6cb-405d-8b9e-2328f4ea53fc");
		notice.setContent("内容123");
		notice.setTitle("标题123");
		String response = noticeController.releaseNotice(principal,notice, request);
		notice = noticeService.list(Notice.class,new Filter().addEquals("noticeUuid","9de257f7-b6cb-405d-8b9e-2328f4ea53fc")).get(0);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("发布公告成功",result.getMessage());
		Assert.assertEquals(NoticeStatus.RELEASED,notice.getNoticeStatus());
		Assert.assertEquals("标题123",notice.getTitle());
		Assert.assertEquals("内容123",notice.getContent());
		List<SystemOperateLog> systemOperateLogList = systemOperateLogService.getLogsByUuid(notice.getNoticeUuid());
		Assert.assertEquals(LogFunctionType.RELEASENOTICE,systemOperateLogList.get(0).getLogFunctionType());	
	}


    @Test
	@Sql("classpath:/test/yunxin/notice/testAddNotice.sql")
	public void testQueryReleasedNoticePage(){
		Page page = new Page();
		page.setEveryPage(5);
		String respose = noticeController.queryReleasedNoticePage(page,2);
		Result result = JsonUtils.parse(respose,Result.class);
		List<Notice> noticeList = JsonUtils.parseArray(result.getData().get("list").toString(),Notice.class);
		int size = JsonUtils.parse(result.getData().get("size").toString(),Integer.class);
		Assert.assertEquals(2, noticeList.size());
		Assert.assertEquals(2, size);
	}


    @Test
	@Sql("classpath:/test/yunxin/notice/testAddNotice.sql")
	public void testQueryReleasedNoticeList(){
		String respose = noticeController.queryReleasedNoticeList();
		Result result = JsonUtils.parse(respose,Result.class);
		List<Notice> noticeList = JsonUtils.parseArray(result.getData().get("list").toString(),Notice.class);
		Assert.assertEquals(2, noticeList.size());
	}
}
