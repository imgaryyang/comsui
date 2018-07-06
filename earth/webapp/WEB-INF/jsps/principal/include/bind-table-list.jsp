<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setBundle basename="ApplicationMessage" />
<fmt:setLocale value="zh_CN" />


<div style="margin-top: 15px;">
    <div class="table without-alternative-bg">
        <div class="block has-bind">
            <div class="lookup-params hide">
                <input type="hidden" name="bindState" value="1" class="form-control real-value">
                <input type="hidden" name="principalId" value="${principal.id}" class="form-control real-value">
            </div>
            <div class="bd">
                <table class="data-list" data-action="${ctx}/bind-financial-contract/query" data-autoload="true">
                    <script type="script/template" class="template">
                        <thead>
                            <tr>
                                <c:if test="${param.type != 'detail'}">
                                    <th class=""><input class="check-box check-box-all" type="checkbox" name="" id="">全选</th>
                                </c:if>
                                <th>信托产品代码</th>
                                <th>信托合同名称</th>
                                <th>信托商户名称</th>
                                <th>信托合同类型</th>
                                <th>状态</th>
                                <c:if test="${param.type != 'detail'}">
                                    <th width="60">操作</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            {% _.each(list, function(item, index) { %}
                                <tr data-id="{%= item.id %}" >
                                    {% if(${param.type != 'detail'}){ %}
                                        <td class=""><input class="check-box" type="checkbox" name="" id=""></td>
                                    {% } %}
                                    <td>{%= item.contractNo %}</td>
                                    <td>{%= item.contractName %}</td>
                                    <td>{%= item.appName %}</td>
                                    <td>{%= item.financialContractType %}</td>
                                    <td>
                                        {% if(item.bindState == 0){ %}
                                            <span class="color-danger">未绑定</span>
                                        {% }else { %}
                                            已绑定
                                        {% } %}
                                    </td>
                                    {% if(${param.type != 'detail'}){ %}
                                        <td width="60">
                                            {% if(item.bindState == 0){ %}
                                                <a href="javascript:void(0)" class="bind">绑定</a>
                                            {% }else { %}
                                                <a href="javascript:void(0)" class="unbind">解除绑定</a>
                                            {% } %}
                                        </td>
                                    {% } %}
                                </tr>
                            {% }) %}
                        </tbody>
                    </script>
                </table>
            </div>
            <div class="ft">
                <c:if test="${param.type != 'detail'}">
                    <a href="#" class="pull-left unbind-all-item">批量解除绑定</a>
                </c:if>
                <jsp:include page="/WEB-INF/include/plugins/assembly.jsp">
                    <jsp:param value="page-control" name="type"/>
                    <jsp:param value="true" name="advanced"/>
                </jsp:include>
            </div>
        </div>
    </div>
</div>
