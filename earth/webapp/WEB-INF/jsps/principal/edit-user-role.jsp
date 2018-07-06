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
<title>编辑用户 - 五维金融金融管理平台</title>

<style>
	@media (min-width: 768px) {
		.table-modal .modal-dialog, .form-modal .modal-dialog {
		    width: 750px;
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
			            <span class="item current">编辑用户</span>
					</div>
				</div>
		
				<div class="form-wrapper">
			        <form class="form">
			          	<input type="hidden" name="principalId" value="${principal.id}" >
						<fieldset class="fieldset-group">
							<h5 class="hd">用户信息</h5>
							<div class="bd">
								<div class="field-row">
									<label class="field-title">用户名</label>
									<div style="font-weight: bold;margin: 8px 2px;">
                  						${principal.name}
	                  				</div>
								</div>
								 <div class="field-row hide">
				                  	<div style="font-size: 12px;color: #66512c;margin-left: 55px;">
					              		<i class="glyphicon glyphicon-info-sign"></i>
					              		<span class="alert-warning">长度为6-16位、英文字母开头、可选择英文字母数字以及下划线组合</span>
				                  	</div>
				                </div>
							</div>
						</fieldset>
						<fieldset class="fieldset-group">
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
										<input class="form-control real-value middle" id="realname" name="realname" value="${principal.tUser.name}" type="text">
									</div>
								</div>
								<div class="field-row">
									<label class="field-title">联系邮箱</label>
									<div class="field-value">
										<input class="form-control real-value middle" id="email" name="email" value="${principal.tUser.email}" type="text">
									</div>
								</div>
								<div class="field-row">
									<label for="" class="field-title">联系电话</label>
									<div class="field-value">
										<input class="form-control real-value middle" id="phone" name="phone" value="${principal.tUser.phone}" type="text">
									</div>
								</div>
								<div class="field-row">
									<label for="" class="field-title require">所属公司</label>
									<div class="field-value">
										<select class="form-control real-value" name="companyId">
											<option value="">请选择</option>
											<c:forEach items="${companyList}" var="item">
												<option ${principal.tUser.company.id eq item.id ? 'selected' : ''} value="${item.id}">${item.fullName}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="field-row">
									<div class="field-title">备注</div>
									<div class="field-value">
										<textarea id="remark" name="remark" class="multiline-input form-control real-value" cols="15" rows="5">${principal.tUser.remark}</textarea>
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
												<option ${principal.authority eq item ? 'selected' : ''} value="${item}">${item}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="field-row bind-view  ${principal.authority == 'ROLE_SUPER_USER' ? 'hide' : '' }" >
									<label for="" class="field-title">业务权限</label>
									<div class="field-value">
										<a href="#" class="btn btn-default add-bind">
											<i class="glyphicon glyphicon-plus"></i>
											新增绑定
										</a>
										<jsp:include page="/WEB-INF/jsps/principal/include/bind-table-list.jsp">
											<jsp:param value="edit" name="type"/>
                                        </jsp:include>
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

	<script id="businessBindTmpl" type="javascript/template">
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
	                            	<input type="hidden" name="principalId" value="${principal.id}" class="form-control real-value">
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
	                                <span class="item hide">
	                                    <select name="bindState" class="form-control real-value small">
				                            <option value="0" selected>未绑定</option>
				                        </select>
	                                </span>
	                                <span class="item">
	                                    <button id="lookup" class="btn btn-primary">查询</button>
	                                </span>
	                            </div>
	                            <div class="bd">
	                                <table class="data-list" data-action="${ctx}/bind-financial-contract/query" data-autoload="true">
                                    	
	                                </table>
	                            </div>
	                            <div class="ft">
	                                <a href="#" class="pull-left bind-all-item">批量绑定</a>
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
	</script>

	<script type="script/template" id="template2">
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
			    <tr data-id="{%= item.id %}">
			        <td class=""><input class="check-box" type="checkbox" name="" id=""></td>
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
			        	{% if(item.bindState == 0){ %}
			            	<a href="javascript:void(0)" class="bind">绑定</a>
			        	{% }else { %}
			        		<a href="javascript:void(0)" class="unbind" >解除绑定</a>
			        	{% } %}
			        </td>
			    </tr>
			{% }) %}
    	</tbody>
	</script>

	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>

</body>