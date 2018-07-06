package com.zufangbao.earth.web.controller.notice;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.exception.NoticeEditException;
import com.zufangbao.earth.yunxin.handler.NoticeHandler;
import com.zufangbao.sun.entity.notice.Notice;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.NoticeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsh
 *
 */
@Controller
@RequestMapping("/notice")
@MenuSetting("menu-system")
public class NoticeController extends BaseController {
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private NoticeHandler noticeHandler;
	
	private static final Log 	logger = LogFactory.getLog(NoticeController.class);
	
	private static final int  	EDIT_NOTICE = 0;
	
	private static final int	RELEASE_NOTICE = 1;
	
	private static final int	CANCEL_NOTICE = 2;
	
	@RequestMapping(value="/notice-show")
	@MenuSetting("submenu-notice-manage")
	public  ModelAndView showNoticePage(Page page){
		try {	
			ModelAndView result = new ModelAndView("index");
			return result;		
		} catch (Exception e) {
			logger.error("#showNoticePage# occur error.");
			e.printStackTrace();
			return pageViewResolver.errorSpec();
		}
	}
	
	@RequestMapping(value="/notice-list")
	public  @ResponseBody String queryNoticePage(Page page,boolean isAsc){
		try {	
			List<Notice> noticeList = this.noticeService.getNoticeListOrderByStatusChangeTime(page,isAsc);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("list",noticeList);
			int countNum = noticeService.countNotice();
			data.put("size",countNum);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			logger.error("#queryNoticePage# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询公告列表页错误");
		}
	}

	@RequestMapping(value ="/notice-add",method=RequestMethod.POST)
	public  @ResponseBody String addNotice(@Secure Principal principal,Notice notice,HttpServletRequest request){
		try {	
			noticeHandler.addNotice(principal.getId(), notice, request);
			return jsonViewResolver.jsonResult("保存公告成功");
		} catch (Exception e) {
			logger.error("#addNotice# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("保存公告错误");
		}
	}
	
	@RequestMapping(value ="/notice-edit")
	public  @ResponseBody String editNotice(@Secure Principal principal,Notice notice,HttpServletRequest request){
		try {	
			noticeHandler.updateNotice(principal.getId(), notice, request, EDIT_NOTICE);
			return jsonViewResolver.jsonResult("编辑公告成功");
		} catch (Exception e) {
			logger.error("#editNotice# occur error.");
			e.printStackTrace();
			if(e instanceof NoticeEditException){
				return jsonViewResolver.errorJsonResult(e.getMessage());
			}
			return jsonViewResolver.errorJsonResult("编辑公告错误");
		}
	}
		
	@RequestMapping(value ="/notice-invalid")
	public  @ResponseBody String cancelNotice(@Secure Principal principal,String noticeUuid,String remarks,HttpServletRequest request){
		try {	
			Notice notice = new Notice();
			notice.setNoticeUuid(noticeUuid);
			notice.setRemarks(remarks);
			noticeHandler.updateNotice(principal.getId(),notice, request, CANCEL_NOTICE);
			return jsonViewResolver.jsonResult("作废公告成功");
		} catch (Exception e) {
			logger.error("#cancelNotice# occur error.");
			e.printStackTrace();
			if(e instanceof NoticeEditException){
				return jsonViewResolver.errorJsonResult(e.getMessage());
			}
			return jsonViewResolver.errorJsonResult("作废公告错误");
		}
	}	
	@RequestMapping(value ="/notice-release")
	public  @ResponseBody String releaseNotice(@Secure Principal principal,Notice notice,HttpServletRequest request){
		try {	
			noticeHandler.updateNotice(principal.getId(), notice, request,RELEASE_NOTICE);
			return jsonViewResolver.jsonResult("发布公告成功");
		} catch (Exception e) {
			logger.error("#releaseNotice# occur error.");
			e.printStackTrace();
			if(e instanceof NoticeEditException){
				return jsonViewResolver.errorJsonResult(e.getMessage());
			}
			return jsonViewResolver.errorJsonResult("发布公告错误");
		}
	}	
	@RequestMapping(value="/notice-released-page-list")
	public  @ResponseBody String queryReleasedNoticePage(Page rawpage,int pageNumber){
		try {	
			Page page = rebuildPage(rawpage, pageNumber);

			int size = noticeService.countReleasedNotice();
			List<Notice> noticeList = noticeService.getReleasedNoticeListOrderByReleaseTime(page);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("list",noticeList);
			data.put("size", size);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			logger.error("#queryReleasedNoticePage# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询已发布公告列表页错误");
		}
	}

	private Page rebuildPage(Page rawpage, int pageNumber) {
		Page page = new Page();
		page.setCurrentPage(rawpage.getCurrentPage());
		page.setEveryPage(pageNumber);
		page.setBeginIndex((page.getCurrentPage()-1)*page.getEveryPage());
		return page;
	}
	
	@RequestMapping(value="/notice-released-list")
	public  @ResponseBody String queryReleasedNoticeList(){
		try {	
			List<Notice> noticeList = noticeService.getReleasedNoticeListOrderByReleaseTime();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("list",noticeList);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			logger.error("#queryReleasedNoticeList# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询已发布公告列表错误");
		}
	}
}
