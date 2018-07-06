<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>

<fmt:setBundle basename="ApplicationMessage" />
<fmt:setLocale value="zh_CN" />

<%@ include file="/WEB-INF/include/meta.jsp"%>
<%@ include file="/WEB-INF/include/css-newlayout.jsp"%>
<title>用户详情 - 五维金融金融管理平台</title>
</head>
<body>

    <%@ include file="/WEB-INF/include/header.jsp"%>
    <div class="web-g-main">
        <%@ include file="/WEB-INF/include/aside.jsp"%>
        <div class="content">
            <div class="position-map">
                <div class="pull-left">
                    <a href="${ctx}/show-user-list"
                        class="back btn btn-default">&lt;&lt; 返回</a>
                     当前位置:
                     <span class="item"><a href="javascript: window.history.go(-1);" class="deep-gray-color">用户管理</a> &gt;</span>
                    <span class="item current">用户详情</span>
                </div>
                <div class="pull-right">
                    <c:if test="${principal.thruDate == null}">
                        <a href="${ctx}/edit-user-role/${principal.id}" class="btn btn-primary edit-user-role">编辑</a>
                    </c:if>
                </div>
            </div>
            <div class="form-wrapper">
                <form id="editUserForm">
                    <fieldset class="fieldset-group" style="padding-bottom: 0px;">
                        <h5 class="hd">用户信息</h5>
                        <div class="bd">
                            <div class="field-row">
                                <label class="field-title">用户名:</label>
                                <div style="margin: 8px 2px;">
                                  ${principal.name}
                                </div>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset class="fieldset-group" style="padding-bottom: 0px;">
                        <h5 class="hd">基础信息</h5>
                        <div class="bd">
                            <div class="field-row">
                                <label for="" class="field-title">真实名字:</label>
                                <div style="margin: 8px 2px;">
                                  ${principal.tUser.name}
                                </div>
                            </div>
                            <div class="field-row">
                                <label for="" class="field-title">联系邮箱:</label>
                                <div class="field-value">
                                    <div style="margin: 8px 2px;">
                                      ${principal.tUser.email}
                                    </div>
                                </div>
                            </div>
                            <div class="field-row">
                                <label for="" class="field-title">联系电话:</label>
                                <div class="field-value">
                                    <div style="margin: 8px 2px;">
                                      ${principal.tUser.phone}
                                    </div>
                                </div>
                            </div>
                            <div class="field-row">
                                <label for="" class="field-title">所属公司:</label>
                                <div class="field-value">
                                    <div style="margin: 8px 2px;">
                                      ${principal.tUser.company.fullName}
                                    </div>
                                </div>
                            </div>
                            <div class="field-row">
                                <label for="" class="field-title">备注:</label>
                                <div class="field-value">
                                    <div style="margin: 8px 2px;">
                                      ${principal.tUser.remark}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset class="fieldset-group">
                        <h5 class="hd">权限信息</h5>
                        <div class="bd">
                            <div class="field-row">
                                <label for="" class="field-title">用户角色:</label>
                                <div class="field-value">
                                    <div style="margin: 8px 2px;">
                                      ${principal.authority}
                                    </div>
                                </div>
                            </div>
                            <div class="field-row hide">
                                <label for="" class="field-title">用户分组:</label>
                                <div class="field-value">
                                    <div style="margin: 8px 2px;">
                                      lisi
                                    </div>
                                </div>
                            </div>
                            <div class="field-row">
                                <label for="" class="field-title">业务权限:</label>
                                <div class="field-value">
                                    <jsp:include page="/WEB-INF/jsps/principal/include/bind-table-list.jsp">
                                        <jsp:param value="detail" name="type"/>
                                    </jsp:include>
                                </div>
                          </div>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
  <%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
</body>
</html>