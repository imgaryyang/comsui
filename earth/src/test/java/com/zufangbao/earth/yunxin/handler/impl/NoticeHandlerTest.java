package com.zufangbao.earth.yunxin.handler.impl;

import com.demo2do.core.persistence.support.Filter;
import com.zufangbao.earth.yunxin.exception.NoticeEditException;
import com.zufangbao.earth.yunxin.handler.NoticeHandler;
import com.zufangbao.sun.entity.notice.Notice;
import com.zufangbao.sun.entity.notice.NoticeStatus;
import com.zufangbao.sun.service.NoticeService;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@Transactional
public class NoticeHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{
	@Autowired 
	private NoticeHandler noticeHandler;
	@Autowired
	private NoticeService NoticeService;
	@Autowired 
	private SystemOperateLogService systemOperateLogService;
	@Test
	@Sql("classpath:/test/yunxin/notice/testAddNotice.sql")
	public void testAddNotice() throws Exception{
		MockHttpServletRequest request = new MockHttpServletRequest();
		Long principalId = 1111l;
	    noticeHandler.addNotice(principalId,null,request);
	    Assert.assertEquals(4, NoticeService.list(Notice.class,new Filter()).size());
	    
	    Notice notice = new Notice("标题5","内容5", new Date(),null,NoticeStatus.UNRELEASED);
	    noticeHandler.addNotice(principalId,notice,request);
	    Assert.assertEquals(5, NoticeService.list(Notice.class,new Filter()).size());
	    List<SystemOperateLog> systemOperateLogVOList = systemOperateLogService.getLogsByUuid(notice.getNoticeUuid());
		Assert.assertEquals(1, systemOperateLogVOList.size());
	}
	
	@Test
	@Sql("classpath:/test/yunxin/notice/testAddNotice.sql")
	public void testUpdateNotice() throws Exception{
		MockHttpServletRequest request = new MockHttpServletRequest();
		Long principalId = 1111l;
		noticeHandler.updateNotice(principalId, null, request, 1);
		
		//notice uuid为空
        Notice notice = new Notice("标题5","内容5", new Date(),null,NoticeStatus.UNRELEASED);
        try{
        	noticeHandler.updateNotice(principalId, notice, request, 1);
        	Assert.fail();
        }catch (Exception e) {
        	Assert.assertTrue(e instanceof NoticeEditException);
        	Assert.assertEquals("找不到该公告",e.getMessage());
		}
        
        //编辑公告
        Notice notice1 = new Notice();
        notice1.setTitle("标题5");
        notice1.setContent("内容5");
        notice1.setNoticeUuid("9de257f7-b6cb-405d-8b9e-2328f4ea53fc");
        noticeHandler.updateNotice(principalId, notice1, request, 0);
        Assert.assertEquals(notice.getTitle(),NoticeService.getNoticeBy("9de257f7-b6cb-405d-8b9e-2328f4ea53fc").getTitle());
        
        //已发布公告不允许编辑
        Notice notice2 = new Notice();
        notice2.setTitle("标题5");
        notice2.setContent("内容5");
        notice2.setNoticeUuid("cc422abc-df26-431e-a0dd-caaf56d0bfac");
        try{
        	noticeHandler.updateNotice(principalId, notice2, request, 0);
        	Assert.fail();
        }catch (Exception e) {
        	Assert.assertTrue(e instanceof NoticeEditException);
        	Assert.assertEquals("已发布公告和作废公告不允许编辑",e.getMessage());
		}
        
        
		//作废公告
		notice1.setNoticeUuid("9de257f7-b6cb-405d-8b9e-2328f4ea53fc");
        noticeHandler.updateNotice(principalId, notice1, request, 2);
        Assert.assertEquals(NoticeStatus.INVALID,NoticeService.getNoticeBy("9de257f7-b6cb-405d-8b9e-2328f4ea53fc").getNoticeStatus());
		
		//发布公告
        notice1.setNoticeUuid("9de257f7-b6cb-405d-8b9e-2328f4ea53fc");
        noticeHandler.updateNotice(principalId, notice1, request, 1);
        Assert.assertEquals(NoticeStatus.RELEASED,NoticeService.getNoticeBy("9de257f7-b6cb-405d-8b9e-2328f4ea53fc").getNoticeStatus());
		
	}
	
}
