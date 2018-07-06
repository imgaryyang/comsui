<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="dashboard item-todo ${param.show ? '' : 'hide'}"  data-disable-toggle="${param.disableToggle}">
    <div class="inner">
        <sec:authentication property="principal" var="currentPrincipal" scope="page" />
        <div class="hd">
            <p class="name"><strong>${data.getRealnameBy(currentPrincipal.id)}[${currentPrincipal.username}],欢迎您</strong></p>
            <p>您今日所需关注的工作内容如下</p>
        </div>
        <c:set var="canAccessFinancialContractList" value="${data.getCanAccessFinancialContractList(currentPrincipal.id)}"></c:set>
        <div class="bd">
            <div class="projects">
            	<p style="color:#999;">请选择信托项目</p>
                <select id="" class="form-control selectpicker" multiple title="请选择信托项目" data-actions-box="true">
                	<c:forEach var="item" items="${canAccessFinancialContractList}">
						<option value="${item.id}&${item.financialContractUuid}" selected>${item.contractName}(${item.contractNo })</option>
					</c:forEach>
                </select>
            </div>
            <div class="todos">
                <div class="tab-panel">
                    <ul class="nav nav-tabs">
                        <li>
                            <a href="#" data-key="remittance" data-href="${ctx}/welcome/count?type=remittance">放款 <span class="total"></span></a>
                        </li>
                        <li>
                            <a href="#" data-key="repayment" data-href="${ctx}/welcome/count?type=repayment">还款 <span class="total"></span></a>
                        </li>
                        <!-- <li>
                            <a href="#" data-key="refund" data-href="${ctx}/welcome/count?type=refund">退款 <span class="total"></span></a>
                        </li> -->
                    </ul>
                    <div class="tab-content">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="dashboard item-notice hide" >
    <div class="inner" style="position: relative;padding-bottom: 60px; " >
        <div class="hd">
            <p class="name"><strong>公告栏</strong></p>
        </div>
        <table class="tab-panel notice data-list"  
             data-action="${ctx}/notice/notice-released-page-list" data-autoload="true"
             data-autoload="true">
            <tbody>
              
            </tbody>
        </table>
        <div class="operations" style="background: #f1f2f5;position: fixed;right: 0; width: 350px;left: inherit">
            <div class="rt pull-right page-control advanced" data-page_record_num="" >
                <div class="inner">
                    共<span class="total"></span>条
                    <span class="nav" style="margin:0">
                        <a href="javascript: void 0;" class="prev">&lt; 上一页</a>
                        <span class="tip">1/页</span>
                        <a href="javascript: void 0;" class="next">下一页 &gt;</a>
                    </span>
                    <span class="popup-redirect-form">跳转</span>
                </div>
                <div class="redirect-form hide">
                    跳转至<input type="text" class="form-control page-index" name="" id="">页
                    <button class="btn btn-defalut redirect">确定</button>
                </div>
            </div>
        </div>
    </div>  
</div>
<div class="dashboard item-notice-detail hide" id="noticeDetailTmpl" >
    <div class="inner">
        <div class="hd">
            <p class="name"><a href="" class="goback"> &lt;返回</a><strong style="margin-left: 10px">公告栏</strong></p>
        </div>
        <div class="bd">
        </div>
    </div>  
</div>

<script type="template" id='noticeTableTmpl'>
    {% _.each(list, function(item, index) { %}
        <tr style="border:1px solid #dedede" data-mtitle="{%= item.title %}">
            <td>
                <a href="" class='notice-item'>
                    <p>{%= unescape(item.title) %}</p>
                    <div class='color-dim'>{%= new Date(item.releaseTime).format('yyyy-MM-dd HH:mm:ss')%}</div>
					<span class="data-content hide">{%= item.content %}</span>
                </a>
            </td>
        </tr>
    {% }); %}
</script>
