<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="com.zufangbao.gluon.spec.global.GlobalCodeSpec"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>

<fmt:setBundle basename="ApplicationMessage" />
<fmt:setLocale value="zh_CN" />

<%@ include file="/WEB-INF/include/meta.jsp"%>
<%@ include file="/WEB-INF/include/css-newlayout.jsp"%>
<title>用户管理 - 五维金融金融管理平台</title>
</head>
<body>
    <%@ include file="/WEB-INF/include/header.jsp"%>

    <div class="web-g-main">

    <%@ include file="/WEB-INF/include/aside.jsp"%>
        <div class="content">
            <div class="scroller">
                <div class="lookup-params">
                    <div class="inner clearfix">
                        <span class="item hide"> 
                            <select name="companyUuids" class="form-control selectpicker" multiple data-title="所属公司"  data-actions-box="true">
                                 <c:forEach items="${companies}" var="item">
                                    <option value="${item.uuid}" selected>${item.fullName}</option>
                                </c:forEach>
                            </select>
                        </span>
                        <span class="item"> 
                            <select name="authorities" class="form-control real-value selectpicker" multiple data-title="用户角色"  data-actions-box="true">
                                <c:forEach items="${authorityMap}" var="item">
                                    <option value="${item.key}" selected>${item.value}</option>
                                </c:forEach>
                            </select>
                        </span> 
                        <span class="item hide"> 
                            <select name="user-team" class="form-control ">
                                <option value="">所属分组</option>
                            </select>
                        </span>
                        <span class="item vertical-line"></span>
                        <span class="item">
                            <select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
                                <option value="id">用户id</option>
                                <option value="accountName">账户名</option>
                            </select>
                            <input type="text" name="keyWords" class="form-control real-value input-key-words" placeholder="请输入关键字..." >
                        </span>
                        <span class="item">
                            <button id="lookup" class="btn btn-primary">查询</button>
                        </span>
                        <a href="${ctx}/create-user-role" class="pull-right btn btn-success add-user">
                            <i class="glyphicon glyphicon-plus"></i>添加用户
                        </a>
                    </div>
                </div>
                <div class="table-area">
                    <table class="data-list" data-action="${ctx}/show-user-list/query" data-autoload="true">
                        <thead>
                            <tr>
                                <th>用户ID</th>
                                <th>用户角色</th>
                                <!-- <th>所属分组</th> -->
                                <th>账户名</th>
                                <th>持有者</th>
                                <th>联系邮箱</th>
                                <th>联系号码</th>
                                <th>所属公司</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <script type="script/template" id="tableFieldTmpl">
                               {% _.each(list, function(item, index) { %}
                                  <tr>
                                    {%= item.id %}
                                    <td>{%= item.id %}</td>
                                    <td>{%= item.role %}</td>
                                    <td>{%= item.accountName %}</td>
                                    <td>{%= item.userName %}</td>
                                    <td>{%= item.email %}</td>
                                    <td>{%= item.number %}</td>
                                    <td>{%= item.company %}</td>

                                    <td>
                                        {%= item.status %}
                                    </td>
                                    <td>
                                      <a href="${ctx}/show-user-role/{%= item.id %}">详情</a>
                                      {% if(item.status != '冻结'){ %}
                                          <a href="javascript:void(0)" class="closeUserBtn" data-id="{%= item.id %}">关闭</a>
                                      {% } %}
                                    </td>
                                  </tr>
                              {% }); %}
                            </script>
                        </tbody>
                    </table>
                </div>
            </div>
           <div class="operations">
                <jsp:include page="/WEB-INF/include/plugins/assembly.jsp">
                    <jsp:param value="page-control" name="type"/>
                    <jsp:param value="true" name="advanced"/>
                </jsp:include>
            </div>
        </div>
    </div>

    <%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
</body>
</html>
