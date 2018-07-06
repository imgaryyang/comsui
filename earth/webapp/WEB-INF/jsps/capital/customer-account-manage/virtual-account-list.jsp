<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>

<fmt:setBundle basename="ApplicationMessage" />
<fmt:setLocale value="zh_CN" />

	<%@ include file="/WEB-INF/include/meta.jsp"%>
	<%@ include file="/WEB-INF/include/css-newlayout.jsp"%>
	<style type="text/css">
		.form-wrapper .form .form-control.middle {
			width: 300px;
		}
	</style>
	<title>客户账户列表 - 五维金融金融管理平台</title>
</head>
<body>

	<%@ include file="/WEB-INF/include/header.jsp"%>

	<div class="web-g-main">
		<%@ include file="/WEB-INF/include/aside.jsp"%>
		<div class="content">
			<div class="scroller">
				<div class="lookup-params">
					<div class="inner clearfix">
                        <div class="pull-left">
                            <input type="hidden" name="filter" value="true">
                            <span class="item">
                            <select name="financialContractUuids" class="form-control real-value selectpicker" multiple data-title="信托合同项目"  data-actions-box="true">
                                <c:forEach var="item" items="${financialContracts}">
                                    <option value="${item.financialContractUuid}">${item.contractName}(${item.contractNo })</option>
                                </c:forEach>
                            </select>
                        </span> 
                            <span class="item"> 
                                <select class="form-control real-value" name="customerType" >
                                    <option value="-1">客户类型</option>
                                    <c:forEach var="item" items="${customerTypeList}">
                                        <option value="${item.ordinal}"><fmt:message key="${item.key}" /></option>
                                    </c:forEach>
                                </select>
                            </span>
                             <span class="item"> 
                                <select class="form-control real-value" name="virtualAccountStatus" >
                                    <option value="-1">账户状态</option>
                                    <c:forEach var="item" items="${virtualAccountStatusList}">
                                        <option value="${item.ordinal}"><fmt:message key="${item.key}" /></option>
                                    </c:forEach>
                                </select>
                            </span>
                            <span class="item vertical-line"></span>
                            <span class="item">
                                <select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
                                    <option value="key">账户名称</option>
                                    <option value="key">账户编号</option>
                                    <option value="key">账户余额</option>
                                </select>
                                <input type="text" name="keyWords" class="form-control real-value input-key-words" placeholder="请输入关键字..." >
                            </span>     
                            <span class="item">
                                <button id="lookup" type="button" class="btn btn-primary">查询</button>
                            </span>
                        </div>
                    </div>
				</div>
				<div class="table-area">
					<table class="data-list" data-action="${ctx}/capital/customer-account-manage/virtual-account-list/query" data-autoload="true">
						
						<thead>
							<tr>
								<th style="width: 10%">账户编号</th>
								<th>账户名称</th>
								<th>客户类型</th>
								<th>信托合同名称</th>
								<th width="15%">贷款合同编号</th>
								<th>
									<a data-paramname="totalBalance" class="sort none">
										账户余额<i class="icon"></i>
									</a>
								</th>
								<th>银行账户名</th>
								<th>
									<a data-paramname="createTime" class="sort none">
										创建时间<i class="icon"></i>
									</a>
								</th>
								<th>账户状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
							    {% _.each(list, function(item, index){ %}
							        <tr data-virtual-accountNo="{%= item.virtualAccountNo %}">
							            <td>
							                <a href="${ctx}/capital/customer-account-manage/virtual-account-list/detail#/capital/account/virtual-acctount/{%= item.virtualAccountUuid %}/detail">{%= item.virtualAccountNo %}</a>
							            </td>
							            <td>{%= item.virtualAccountAlias %}</td>
							            <td>{%= item.customerTypeName %}</td>
							            <td>{%= item.contractName %}</td>
							            <td><a href="${ctx}/contracts/detail?id={%= item.contractId %}">{%= item.contactNo %}</a></td>
							            <td>{%= (item.totalBalance).formatMoney(2, '') %}</td>
							            <td>
											<span class="show-popover"
												data-container="body"
												data-placement="top"
												data-html="true"
												data-trigger="focus" 
												data-toggle="popover">
												{%= item.accountName %}
											</span>
											<div class="hide account">
												<div>
													<div>账户名: {%= item.accountName %}</div>
													<div>账户号: {%= item.accountNo %}</div>
													<div>开户行: {%= item.bankName %}</div>
													<div>所在地: {%= item.location %}</div>
													<div>证件号: {%= item.idCardNo %}</div>
												</div>
											</div>
							            </td>
							            <td>{%= new Date(item.createTime).format('yyyy-MM-dd HH:mm:ss') %}</td>
							            <td>{%= item.virtualAccountStatusName %}</td>
							            <td>
							                <a href="#" class="withdraw"><span class="color-danger"></a>
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
	<script type="template/script" id="WithdrawDialogTmpl">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close close-dialog" aria-label="关闭" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="dialoglabel">提现</h4>
				</div>
				<div class="modal-body form-wrapper">
					<form class="form adapt" novalidate="novalidate">
						<div class="field-row field-row-selectpicker">
                			<label for="" class="field-title">提现卡号</label>
            				<div class="field-value">
            				    <select class="form-control real-value middle" name="">
            						<option value="">请选择</option>
          						</select>
               			 	</div>
            			</div>
            			<div class="field-row field-row-selectpicker">
                			<label for="" class="field-title">持卡人姓名</label>
            				<div class="field-value">
            				    <select class="form-control real-value middle" name="">
            						<option value="">请选择</option>
          						</select>
               			 	</div>
            			</div>
            			<div class="field-row field-row-selectpicker">
                			<label for="" class="field-title">提现方式</label>
            				<div class="field-value">
            				    <select class="form-control real-value middle" name="">
            						<option value="">请选择</option>
          						</select>
               			 	</div>
            			</div>
            			<div class="field-row field-row-selectpicker">
                			<label for="" class="field-title">付款通道</label>
            				<div class="field-value">
            				    <select class="form-control real-value middle" name="">
            						<option value="">请选择</option>
          						</select>
               			 	</div>
            			</div>
						<div class="field-row">
          					<label class="field-title">提现金额</label>
							<div class="field-value">
	                			<input class="form-control real-value middle" name="" placeholder="默认为全部金额">
                			</div>
           				 </div>
						<div class="field-row">
          					<label class="field-title">备注</label>
							<div class="field-value">
	                			<input class="form-control real-value middle" name="" placeholder="选填">
                			</div>
           				 </div>
					</form>
				</div>
				<div class="modal-footer">
	        		<button type="button" class="btn btn-default cancel-dialog" data-dismiss="modal">取消</button>
	       			<button type="button" class="btn btn-success submit">确定</button>
				</div>
			</div>
		</div>
	</script>

	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
	<script src="${ctx.resource}/js/bootstrap-modal-trigger.js"></script>
</body>
</html>