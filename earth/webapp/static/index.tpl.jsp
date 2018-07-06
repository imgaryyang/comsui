<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authentication property="principal" var="currentPrincipal" scope="page" />

<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <%@ include file="/WEB-INF/include/meta.jsp"%>

        <%--
            to do: 
            1. 为了使用缓存的文件，bootstrap，jquery都是引用的旧目录中的文件，之后要改的
            2. 要逐渐移除app-last.css及bootstrap框架
            3. manifest直接内嵌进html而不是去请求一个资源，主要是为了和dev环境兼容
        --%>

        <link href="${ctx.resource}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${ctx.resource}/css/build/app-last.css" rel="stylesheet">

        <link rel="stylesheet" href="${ctx.resource}/vue/element-ui.css">
        <link href="${ctx.resource}/vue/main.css" rel="stylesheet">

        <title>五维金融金融管理平台</title>
    </style>
    </head>
    <body>
        <!--[if lt IE 9]>
            <style>.web-g-main{height:0;overflow:hidden}.lt-ie9-layer,.lt-ie9-box{position:fixed;top:0;left:0;width:100%;height:100%}.lt-ie9-layer{background:#f4f4f4;z-index:9999}.lt-ie9-box{z-index:10000;text-align:center;padding:100px 30px}</style>
            <div class="lt-ie9-layer"></div>
            <div class="lt-ie9-box">
                <h2>抱歉，网站不再支持IE8及更低版本浏览器，请使用新版浏览器</h2>
                <a href="https://www.google.cn/intl/zh-CN/chrome/browser/desktop/" target="_blank">谷歌 Chrome</a>
                &nbsp;&nbsp; 
                <a href="http://www.uc.cn/ucbrowser/download/" target="_blank">UC 浏览器</a>
            </div>
        <![endif]-->

        <div id="app" v-cloak></div>

        <input type="hidden" name="username" value="${currentPrincipal.username}">
        
        <script src="${ctx.resource}/js/vendor/jquery-1.11.0.min.js"></script>
        <script src="${ctx.resource}/js/vendor/bootstrap.min.js"></script>
        <script src="${ctx.resource}/js/mods/scaffold/highcharts.js"></script>
        
        <script src="${ctx.resource}/vue/manifest.js"></script>
        <script src="${ctx.resource}/vue/polyfill.js"></script>
        <script src="${ctx.resource}/vue/lib.js"></script>
        <script src="${ctx.resource}/vue/element-ui.js"></script>
        <script src="${ctx.resource}/vue/main.js"></script>

    </body>
</html>