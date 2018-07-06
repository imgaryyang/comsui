package com.zufangbao.earth.yunxin.handler;

import com.zufangbao.sun.entity.notice.Notice;

import javax.servlet.http.HttpServletRequest;

public interface NoticeHandler {
    void addNotice(Long principalId, Notice notice, HttpServletRequest request) throws Exception;

    /**
	 * @param principalId
	 * @param notice   需要添加的公告
	 * @param request
	 * @param fn       0：编辑   1：发布  2：作废
	 */
    void updateNotice(Long principalId, Notice notice, HttpServletRequest request, int fn) throws Exception;
}
