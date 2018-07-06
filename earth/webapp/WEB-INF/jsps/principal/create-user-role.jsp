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
<title>新建用户 - 五维金融金融管理平台</title>
<style>
	@media (min-width: 768px) {
		.table-modal .modal-dialog, .form-modal .modal-dialog {
		    width: 720px;
		}
	}
	
</style>
</head>
<body>
	<%@ include file="/WEB-INF/include/header.jsp"%>

	<div class="web-g-main">
		<%@ include file="/WEB-INF/include/aside.jsp"%>
		<div class="content">
			<div class="scroller">
				<div class="position-map">
					<div class="pull-left">
						<a href="javascript:window.history.go(-1);"
							class="back btn btn-default">&lt;&lt; 返回</a>
						 当前位置:
						 <span class="item"><a href="javascript: window.history.go(-1);" class="deep-gray-color">用户管理</a> &gt;</span>
			            <span class="item current">新建用户</span>
					</div>
				</div>
		
				<div class="form-wrapper">
			        <form id="editUserForm" method="post" class="form">
			          	<input type="hidden" name="principalId" value="${principal.id}" >
						<fieldset class="fieldset-group" style="padding-bottom: 0px;">
							<h5 class="hd">用户信息</h5>
							<div class="bd">
								<div class="field-row">
									<label class="field-title require">用户名</label>
									<input class="form-control real-value" name="username" type="text">
								</div>
								 <div class="field-row">
				                  	<div style="font-size: 12px;color: #66512c;margin-left: 55px;">
					              		<i class="glyphicon glyphicon-info-sign"></i>
					              		<span class="alert-warning">长度为6-16位、英文字母开头、可选择英文字母数字以及下划线组合</span>
				                  	</div>
				                </div>
							</div>
						</fieldset>
						<fieldset class="fieldset-group" style="padding-bottom: 0px;">
							<div style="margin:10px 0">
								<span class="hd drawer-prve">基础信息</span>
								<a href="javascript:void(0)" class="drawer">
									<span class="msg">收起</span>
									<i class="icon icon-up-down active"></i>
								</a>
							</div>
							<div class="bd">
								<div class="field-row">
									<label for="" class="field-title require">真实名字</label>
									<div class="field-value">
										<input class="form-control real-value" id="" name="realname" value="" type="text">
									</div>
								</div>
								<div class="field-row">
									<label class="field-title">联系邮箱</label>
									<div class="field-value">
										<input class="form-control real-value middle" id="email" name="email" value="" type="text">
									</div>
								</div>
								<div class="field-row">
									<label for="" class="field-title">联系电话</label>
									<div class="field-value">
										<input class="form-control real-value middle" id="phone" name="phone" value="" type="text">
									</div>
								</div>
								<div class="field-row">
									<label for="" class="field-title require">所属公司</label>
									<div class="field-value">
										<select class="form-control real-value" name="companyId">
											<option value="">请选择</option>
											<c:forEach items="${companyList}" var="item">
												<option value="${item.id}">${item.fullName}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="field-row">
									<div class="field-title">备注</div>
									<div class="field-value">
										<textarea id="remark" name="remark" class="multiline-input form-control real-value" cols="20" rows="10"></textarea>
									</div>
								</div>
							</div>
						</fieldset>
						<fieldset class="fieldset-group">
							<div style="margin:10px 0">
								<span class="hd drawer-prve">权限信息</span>
								<a href="javascript:void(0)" class="drawer">
									<span class="msg">收起</span>
									<i class="icon icon-up-down active"></i>
								</a>
							</div>
							<div class="bd">
								<div class="field-row">
									<label class="field-title require">用户角色</label>
									<div class="field-value">
										<select class="form-control real-value" id="role" name="role">
											<option value="">请选择</option>
											<c:forEach items="${resources.keySet()}" var="item">
												<option value="${item}">${item}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="field-row hide">
									<label for="" class="field-title">业务权限</label>
									<div class="field-value">
										<a href="#" class="btn btn-default add-bind">
											<i class="glyphicon glyphicon-plus"></i>
											添加
										</a>
										<div style="margin-top: 15px;">
										    <div class="table without-alternative-bg">
										        <div class="block has-bind">
										            <div class="bd">
										                <table class="data-list" data-action="">
									                        <thead>
									                            <tr>
								                                    <th class="hide"><input class="check-box check-box-all" type="checkbox" name="" id="">全选</th>
									                                <th>信托产品代码</th>
									                                <th>信托合同名称</th>
									                                <th>信托商户名称</th>
									                                <th>信托合同类型</th>
									                                <th>状态</th>
								                                    <th width="60">操作</th>
									                            </tr>
									                        </thead>
									                        <tbody>
									                        	<tr class="nomore"><td style="text-align: center;" colspan="10">暂无权限</td></tr>
									                        </tbody>
										                    <script type="script/template" class="bind-item-view">
						                                        {% if(item != null){ %}
							                                        <td class="hide"><input class="check-box" type="checkbox" name="" id=""></td>
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
							                                        <td width="60">
						                                                <a href="javascript:void(0)" class="remove" data-id="{%= item.id %}">移除</a>
							                                        </td>
						                                        {% } %}
										                    </script>
										                </table>
										            </div>
										            <div class="ft hide">
									                    <a href="#" class="pull-left">批量移除</a>
										            </div>
										        </div>
										    </div>
										</div>
									</div>
								</div>
							</div>
						</fieldset>
						<fieldset class="fieldset-group">
							<div class="field-row">
								<div class="field-title"></div>
								<div class="field-value">
									<button type="button" id="submit" class="btn btn-primary submit">提交</button>
								</div>
							</div>
						</fieldset>
			        </form>
		        </div>
			</div>
		</div>
	</div>

	<div class="modal fade form-modal large-form-modal" id="businessBindTmpl">
		<div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close close-dialog" aria-label="关闭" data-dismiss="modal">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="dialoglabel">业务绑定</h4>
                </div>
            	<div class="modal-body">
					<div class="text-align-left">
	                    <div class="table">
	                        <div class="block bind-list">
	                            <div class="lookup-params" style="padding: 0 0 10px; background: transparent;">
	                                <span class="item">
	                                    <input class="form-control real-value small" type="text" name="financialContractNo" placeholder="输入合同编号">
	                                </span>
	                                <span class="item">
	                                    <input class="form-control real-value small" type="text" name="financialContractName" placeholder="输入合同名称">
	                                </span>
	                                <span class="item">
	                                    <select name="appId" class="form-control real-value small">
				                            <option value="">信托商户名称</option>
				                            <c:forEach items="${appList}" var="item">
												<option  value="${item.id}">${item.name}</option>
											</c:forEach>
				                        </select>
	                                </span>
	                                <span class="item">
	                                    <select name="financialContractType" class="form-control real-value small">
				                            <option value="">信托合同类型</option>
				                             <c:forEach items="${financialContractTypeList}" var="item">
												<option  value="${item.ordinal}"><fmt:message key="${item.key }"/></option>
											</c:forEach>
				                        </select>
	                                </span>
	                                <span class="item">
	                                    <select name="bindState" class="form-control real-value small">
				                            <option value="-1">绑定状态</option>
				                            <option value="1">绑定</option>
				                            <option value="0">未绑定</option>
				                        </select>
	                                </span>
	                                <span class="item">
	                                    <button id="lookup" class="btn btn-primary">查询</button>
	                                </span>
	                            </div>
	                            <div class="bd">
	                                <table class="data-list" data-action="${ctx}/bind-financial-contract/query" data-autoload="true">
	                                    <script type="script/template" class="template">
	                                    	<thead>
		                                        <tr>
		                                            <th class=""><input class="check-box check-box-all" type="checkbox" name="" id="">全选</th>
		                                            <th>信托产品代码</th>
		                                            <th>信托合同名称</th>
		                                            <th>信托商户名称</th>
		                                            <th>信托合同类型</th>
		                                            <th>状态</th>
		                                            <th width="60">操作</th>
		                                        </tr>
                                    		</thead>
                                    		<tbody>
		                                        {% _.each(list, function(item, index) { %}
	                                                <tr data-id="{%= item.get('id')%}">
			                                            <td class=""><input class="check-box" type="checkbox" name="" id=""></td>
			                                            <td>{%= item.get('contractNo') %}</td>
			                                            <td>{%= item.get('contractName')%}</td>
			                                            <td>{%= item.get('appName') %}</td>
			                                            <td>{%= item.get('financialContractType') %}</td>
			                                            <td>
			                                            	{% if(item.get('bindState') == 0){ %}
			                                            		{% if(item.get('isAdd')){ %}
			                                            			待绑定
			                                            		{% }else { %}
			                                            			<span class="color-danger">未绑定</span>
			                                            		{% } %}
			                                            	{% }else { %}
			                                            		已绑定
			                                            	{% } %}
			                                            </td>
			                                            <td width="60">
			                                            	{% if(item.get('bindState') == 0){ %}
			                                                	<a href="javascript:void(0)" class="bind">添加</a>
			                                            	{% }else { %}
			                                            		<a href="javascript:void(0)" class="unbind" >移除</a>
			                                            	{% } %}
			                                            </td>
			                                        </tr>
	                                            {% }) %}
                                    		</tbody>
	                                    </script>
	                                </table>
	                            </div>
	                            <div class="ft">
	                                <a href="#" class="pull-left">批量添加</a>
	                                <jsp:include page="/WEB-INF/include/plugins/assembly.jsp">
							            <jsp:param value="page-control" name="type"/>
							            <jsp:param value="true" name="advanced"/>
							        </jsp:include>
	                            </div>
	                        </div>
	                    </div>
                	</div>
				</div>
            </div>
        </div>
	</div>
	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>

</body>