<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>

  <fmt:setBundle basename="ApplicationMessage" />
  <fmt:setLocale value="zh_CN" />

  <%@ include file="/WEB-INF/include/meta.jsp"%>
  <%@ include file="/WEB-INF/include/css-newlayout.jsp"%>
  <title>出错了！ - 五维金融金融管理平台</title>
</head>
<body>

  <%@ include file="/WEB-INF/include/header.jsp"%>
  
  <div class="web-g-main">

	<div class="aside-box"></div>
    <div class="content" style="background: #f4f4f4;">
    	<div class="clearfix">
		   	<h3 class="pull-left btn-warning" style="color: #333;margin-left: 20px; font-size: 18px;line-height: 36px;">
		   		<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
		   		${not empty message?message:'系统繁忙请联系管理员！！！'}
		   	</h3>
    	</div>
        <%@ include file="/WEB-INF/include/footer.jsp"%>
    </div>

  </div>
  <%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
</body>
</html>
