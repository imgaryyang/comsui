<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html> 
<html lang="zh-CN">
<head>

  <fmt:setBundle basename="ApplicationMessage" />
  <fmt:setLocale value="zh_CN" />

  <%@ include file="/WEB-INF/include/meta.jsp"%>
  <%@ include file="/WEB-INF/include/css.jsp"%>

  <title>五维金融金融管理平台</title>

</head>
<body>
  <header class="web-g-hd login-g-hd">
      <div class="g-bd clearfix">
        <div class="lt">
          <span class="logo">
              <i class="icon"></i>
          </span>
          <c:if test="${not empty ctx.production}">
          	<span class="divider"></span>
          	<span class="sub-title">
	      		<img class="company-logo" src="${ctx.resource}/images/company-logo/${ctx.production}-login.png" alt="">
          	</span>
          </c:if>
        </div>
        <div class="rt">
          <ul class="list-inline hide">
              <li class="inline-item"><a href="">帮助中心</a></li>
              <li class="inline-item divider">|</li>
              <li class="inline-item"><a href="">开发者文档</a></li>
          </ul>
        </div>
      </div>
  </header>
  <div class="web-g-main login-g-main">
      <div class="web-g-bd login-g-bd">
          <div class="banner">
              <h1 class="title">
                <div style="text-align: center; font-size: 30px;">创造</div>
                从无到有的金融市场
              </h1>
              <img src="${ctx.resource}/images/computer.png" alt="" class="computer"> <i class="icon icon-coffer"></i>
          </div>
          <div class="login-area">
              <h2 class="title">登录</h2>
              <form class="login-form" id="login-form" action="${ctx}/j_spring_security_check" method="POST" data-validate="true">
                  <section class="field-row username-field">
                      <label for="" class="input-title">帐号</label>
                      <div class="input-group">
                          <input id="username" type="text" class="form-control" name="j_username" value="">
                          <span class="input-group-addon">
                              <i class="icon icon-input-cancel" id="cancel-icon">&times</i>
                          </span>
                      </div>
                      <div class="tip">
                        <%-- <c:if test="${not empty errorMessage}"><fmt:message key="${errorMessage}" /></c:if> --%>
                      </div>
                  </section>

                  <section class="field-row userpwd-field <c:if test="${not empty errorMessage}">has-error</c:if>">
                      <label for="" class="input-title">密码</label>
                      <div class="input-group">
                          <input id="userpwd" type="password" class="form-control" name="j_password">
                          <span class="input-group-addon">
                              <i class="icon icon-input-cancel" id="cancel-password">&times</i>
                          </span>
                      </div>
                      <div class="tip">
                        <c:if test="${not empty errorMessage}"><fmt:message key="${errorMessage}" /></c:if>
                      </div>
                  </section>

                  <%--
                  <section class="field-row verfiedcode-field hide">
                      <label for="" class="input-title">手机动态验证码</label>
                      <div class="input-group verfied-code">
                          <input type="text" class="form-control">
                          <span class="input-group-btn">
                              <button class="btn btn-default btn-primary btn-outline" type="button">发送验证码</button>
                          </span>
                      </div>
                      <div class="tip"></div>
                  </section>

                  <section class="field-row">
                      <label for="" class="input-title remember-pwd"><input type="checkbox" name="" id="">&ensp;记住动态密码10天</label>
                  </section>
                  --%>

                  <section class="field-row verfiedcode-field <c:if test="${not empty captchaErrorMessage}">has-error</c:if>">
                    <label for="" class="input-title">验证码</label>
                    <div class="input-group verfied-code">
                        <input id="captcha" type="text" class="form-control" maxlength="4" name="j_captcha" value="">
                        <span class="input-group-btn">
                            <button id="refreshVerifyCode" class="btn btn-default btn-primary btn-outline" type="button" style="padding: 0;width: 100px;height: 34px;border: 0;left: -30px;top: -1px;">
                              <img alt="" src="./captcha/captcha-image" width="100%" height="100%">
                            </button>
                        </span>
                    </div >
                    <div class="tip">     
                      <c:if test="${not empty captchaErrorMessage}"><fmt:message key="${captchaErrorMessage}" /></c:if>
                    </div>
                  </section>

                  <button type="submit" id="login" class="btn-login btn btn-primary btn-block">登录</button>

                  <%--
                  <footer class="hide">
                      没有账号？<a href="" class="rightnow-sign">立即注册</a>
                  </footer>
                  --%>
              </form>
          </div>
      </div>
  </div>
  <footer class="web-g-ft login-g-ft">
      <p class="abouts">
          <a href="" class="link">关于</a>
          -
          <a href="" class="link">联系我们</a>
          -
          <a href="" class="link">平台使用协议</a>
      </p>
      <p class="copyright">
          © 2015 杭州随地付网络技术有限公司 版权所有&emsp;&emsp;浙ICP备14021039号-1&emsp;&emsp;增值电信业务许可证：浙B2-20140026
      </p>
  </footer>
  <%@ include file="/WEB-INF/include/script.jsp"%>
  <script src="${ctx.resource}/js/utils/md5.js"></script>
  <script>
      (function() {
          var $form = $('#login-form');
          var $verifyCode = $form.find('#refreshVerifyCode');
          var $username = $form.find("#username");
          var $userpwd = $form.find("#userpwd");
          var $captcha = $form.find('#captcha');
          var $cancelUsername = $form.find('#cancel-icon');
          var $cancelPassword = $form.find('#cancel-password');

          function check() {
              var isUsernameRight = true;
              var isCaptchaRight = true;
              var isUserpwdRight = true;
              
              if (!$username.val().trim()) {
                  showTip($username.closest('.field-row'), '不能为空');
                  isUsernameRight = false;
              } else {
                  hideTip($username.closest('.field-row'));
              }

              if (!$userpwd.val().trim()) {
                  showTip($userpwd.closest('.field-row'), '不能为空');
                  isUserpwdRight = false;
              } else {
                  hideTip($userpwd.closest('.field-row'));
              }

              if (!$captcha.val().trim()) {
                  showTip($captcha.closest('.field-row'), '不能为空');
                  isCaptchaRight = false;
              } else {
                  hideTip($captcha.closest('.field-row'));
              }

              return isUsernameRight && isUserpwdRight && isCaptchaRight;
          }

          function showTip($element, tipTxt) {
              $element.addClass("has-error").find(".tip").html(tipTxt);
          }

          function hideTip($element) {
              $element.removeClass("has-error").remove(".tip");
          }

          function setUsername(username) {
            localStorage.setItem('username', username);
          }


          if ($verifyCode.length > 0) {
              var verifyCodeImgEl = $verifyCode.children().get(0);
              var originalVerifyCodeImgSrc = verifyCodeImgEl.src;

              $verifyCode.on('click', function() {
                  verifyCodeImgEl.src = originalVerifyCodeImgSrc + '?t=' + Date.now();
              });
          }

          $cancelUsername.on('click', function() {
              $username.val('');
          });

          $cancelPassword.on('click', function() {
              $userpwd.val('');
          });

          $form.on('change', '.field-row', function(e) {
              hideTip($(e.currentTarget));
          });

          $form.on('submit', function(e) {
              if (check()) {
                var pwd = $userpwd.val();
                $userpwd.val(md5(pwd));
                setUsername($username.val().trim())
                return true;
              } else {
                e.preventDefault;
                return false;
              }
          });

      })();
  </script>

</body>
</html>
