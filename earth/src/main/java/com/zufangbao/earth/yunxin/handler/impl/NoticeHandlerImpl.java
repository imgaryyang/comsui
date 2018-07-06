package com.zufangbao.earth.yunxin.handler.impl;

import com.zufangbao.earth.yunxin.exception.NoticeEditException;
import com.zufangbao.earth.yunxin.handler.NoticeHandler;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.notice.Notice;
import com.zufangbao.sun.entity.notice.NoticeLog;
import com.zufangbao.sun.entity.notice.NoticeStatus;
import com.zufangbao.sun.service.NoticeService;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@Component("noticeHandler")
public class NoticeHandlerImpl implements NoticeHandler{
	@Autowired
	private NoticeService noticeService;

	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;

	private static final int  EDIT_NOTICE = 0;

	private static final int  RELEASE_NOTICE = 1;

	private static final int  CANCEL_NOTICE = 2;

	@Override
	public void addNotice(Long principalId, Notice notice, HttpServletRequest request) throws Exception {
		if(notice == null){
			return;
		}
		notice.setNoticeUuid(UUID.randomUUID().toString());
		notice.setNoticeStatus(NoticeStatus.UNRELEASED);
		notice.setStatusChangeTime(new Date());
		noticeService.addNotice(notice);
		generate_notice_system_operate_log(principalId, request, notice,null,LogFunctionType.ADDNOTICE);
	}

	@Override
	public void updateNotice(Long principalId, Notice notice, HttpServletRequest request, int fn) throws Exception {
		if(notice == null){
			return;
		}
		switch (fn) {
		case EDIT_NOTICE:editNotice(principalId,notice, request);
			break;
		case RELEASE_NOTICE:releaseNotice(principalId, notice, request);
			break;
		case CANCEL_NOTICE:cancelNotice(principalId, notice, request);
			break;
		default:
			break;
		}

	}

	private void editNotice(Long principalId, Notice notice, HttpServletRequest request) throws Exception{
		Notice editNotice = noticeService.getNoticeBy(notice.getNoticeUuid());
		verifyNoticeIsNull(editNotice);
		if(!(editNotice.getNoticeStatus()==NoticeStatus.UNRELEASED)){
			throw new NoticeEditException("已发布公告和作废公告不允许编辑");
		}
		Notice oldNotice = new Notice(editNotice);
		updateNotice(editNotice, null,null,new Date(),notice.getContent(), notice.getTitle());
		generate_notice_system_operate_log(principalId, request, oldNotice,editNotice,LogFunctionType.EDITNOTICE);

	}

	private void cancelNotice(Long principalId, Notice notice, HttpServletRequest request) throws Exception{
		Notice oldNotice = noticeService.getNoticeBy(notice.getNoticeUuid());
		verifyNoticeIsNull(oldNotice);
		//设置备注
		oldNotice.setRemarks(notice.getRemarks());
		updateNotice(oldNotice, NoticeStatus.INVALID, null, new Date(),null,null);
		generate_notice_system_operate_log(principalId, request, oldNotice,null,LogFunctionType.CANCELNOTICE);
	}

	private void releaseNotice(Long principalId, Notice notice, HttpServletRequest request) throws Exception{
		Notice editNotice  =  noticeService.getNoticeBy(notice.getNoticeUuid());
		verifyNoticeIsNull(editNotice);
		Notice oldNotice = new Notice(editNotice);
		updateNotice(editNotice, NoticeStatus.RELEASED, new Date(),new Date(), notice.getContent(), notice.getTitle());
		generate_notice_system_operate_log(principalId,request, oldNotice,editNotice,LogFunctionType.RELEASENOTICE);
	}


	private void generate_notice_system_operate_log(Long principalId,HttpServletRequest request,
			Notice oldNotice,Notice editNotice,LogFunctionType logFunctionType) throws Exception {
		SystemOperateLogRequestParam systemOperateLogRequestParam = null;
		switch (logFunctionType) {
		case ADDNOTICE:systemOperateLogRequestParam = generate_add_notice_system_operate_log_request_param(principalId,request,oldNotice);
			break;
		case CANCELNOTICE:systemOperateLogRequestParam = generate_cancel_notice_system_operate_log_request_param(principalId,request,oldNotice);
			break;
		default:systemOperateLogRequestParam = generate_update_natice_system_operate_log_request_param(principalId,request,
					oldNotice, editNotice, logFunctionType);
			break;
		}
		systemOperateLogHandler.generateSystemOperateLog(systemOperateLogRequestParam);
	}

	private SystemOperateLogRequestParam generate_update_natice_system_operate_log_request_param(Long principalId,
			HttpServletRequest request, Notice oldNotice, Notice editNotice, LogFunctionType logFunctionType) {
		NoticeLog oldNoticeLog = new NoticeLog(oldNotice.getUuid(),oldNotice.getTitle(),oldNotice.getContent(),oldNotice.getReleaseTimeToString(),oldNotice.getStatusChangeTimeToString(),oldNotice.getNoticeStatus().getChineseMessage());
		NoticeLog editNoticeLog = new NoticeLog(editNotice.getUuid(),editNotice.getTitle(),editNotice.getContent(),editNotice.getReleaseTimeToString(),editNotice.getStatusChangeTimeToString(),editNotice.getNoticeStatus().getChineseMessage());
        return new SystemOperateLogRequestParam(principalId, IpUtil.getIpAddress(request),
                oldNotice.getUuid(),logFunctionType, LogOperateType.UPDATE, NoticeLog.class,oldNoticeLog,editNoticeLog,null);
	}

	private SystemOperateLogRequestParam generate_cancel_notice_system_operate_log_request_param(Long principalId,
			HttpServletRequest request, Notice oldNotice) {
		NoticeLog NoticeLog = new NoticeLog(oldNotice.getUuid(),oldNotice.getTitle(),oldNotice.getContent(),oldNotice.getReleaseTimeToString(),oldNotice.getStatusChangeTimeToString(),oldNotice.getNoticeStatus().getChineseMessage(),oldNotice.getRemarks());
        return new SystemOperateLogRequestParam(principalId, IpUtil.getIpAddress(request),
                NoticeLog.getUuid()+NoticeLog.gerenateRemarks(),LogFunctionType.CANCELNOTICE, LogOperateType.INVALIDATE, NoticeLog.class,NoticeLog,null, null);
	}

	private SystemOperateLogRequestParam generate_add_notice_system_operate_log_request_param(Long principalId,
			HttpServletRequest request, Notice oldNotice) {
		NoticeLog NoticeLog = new NoticeLog(oldNotice.getUuid(),oldNotice.getTitle(),oldNotice.getContent(),oldNotice.getReleaseTimeToString(),oldNotice.getStatusChangeTimeToString(),oldNotice.getNoticeStatus().getChineseMessage());
        return new SystemOperateLogRequestParam(principalId, IpUtil.getIpAddress(request),
                oldNotice.getUuid(),LogFunctionType.ADDNOTICE, LogOperateType.ADD, NoticeLog.class,NoticeLog,null, null);
	}



	private void verifyNoticeIsNull(Notice notice){
		if(notice == null){
			throw new NoticeEditException("找不到该公告");
		}
	}


	private void updateNotice(Notice notice,NoticeStatus noticeStatus,Date releaseTime,Date statusChangeTime,String content,String title){
		if(noticeStatus!=null)
			notice.setNoticeStatus(noticeStatus);
		if(releaseTime!=null)
			notice.setReleaseTime(releaseTime);
		if(statusChangeTime!=null){
			notice.setStatusChangeTime(statusChangeTime);
		}
		if(!StringUtils.isEmpty(content))
			notice.setContent(content);
		if(!StringUtils.isEmpty(title))
			notice.setTitle(title);
	}
}
