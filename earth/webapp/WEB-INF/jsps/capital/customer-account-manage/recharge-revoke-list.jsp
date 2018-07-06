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
<title>充值撤销 - 五维金融金融管理平台</title>
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
									<c:forEach var="item" items="${customerTypeList }">
										<option value="${item.ordinal}"><fmt:message key="${item.key}" /></option>
									</c:forEach>
								</select>
							</span>
							<span class="item beginend-datepicker">
								<jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
									<jsp:param value="group" name="type" />
									<jsp:param value="startDate" name="paramName1" />
									<jsp:param value="endDate" name="paramName2" />
									<jsp:param value="起始日期" name="placeHolder1" />
									<jsp:param value="终止日期" name="placeHolder2" />
									<jsp:param value="true" name="pickTime" />
									<jsp:param value="true" name="formatToMinimum" />
									<jsp:param value="true" name="formatToMaximum" />
								</jsp:include>
							</span>
							<span class="item vertical-line"></span>
							<span class="item">
								<select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
									<option value="key">撤销单号</option>
									<option value="key">账户名称</option>
									<option value="key">账户编号</option>
									<option value="key">充值金额</option>
									<option value="key">充值单号</option>
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
					<table class="data-list" data-action="${ctx}/capital/customer-account-manage/recharge-revoke-list/query" data-autoload="true">
						<thead>
							<tr>
								<th width="15%">撤销单号</th>
								<th width="15%">账户编号</th>
								<th>账户名称</th>
								<th>客户类型</th>
								<th>信托项目编号</th>
								<th>充值单号</th>
								<th>
									<a data-paramname="createTime" class="sort none">
                                        创建时间<i class="icon"></i>
                                    </a>
								</th>
								<th>
									<a data-paramname="bookingAmount" class="sort none">
                                        充值金额<i class="icon"></i>
                                    </a>
								</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
							    {% _.each(list, function(item, index){ %}
							        <tr>
							            <td><a href="${ctx}/capital/customer-account-manage/recharge-revoke-list/detail#/capital/account/recharge-revoke/{%= item.sourceDocumentUuid %}/detail">{%= item.sourceDocumentRevokeNo %}</a></td>
							            <td><a href="${ctx}/capital/customer-account-manage/virtual-account-list/detail#/capital/account/virtual-acctount/{%= item.virtualAccountUuid %}/detail">{%= item.virtualAccountNo %}</a></td>
										<td>{%= item.virtualAccountName %}</td>
							            <td>{%= item.customerTypeName %}</td>
							            <td><a href="${ctx}/financialContract/new-financialContract#/financial/contract/{%= item.financialContractUuid %}/detail">{%= item.financialContractNo %}</a></td>
										<td><a href="${ctx}/capital/customer-account-manage/deposit-receipt-list/account-detail#/capital/account/deposit-receipt/{%= item.rechargeSourceDocumentUuid %}/detail">{%= item.sourceDocumentNo %}</a></td>
										<td>{%= new Date(item.createTime).format('yyyy-MM-dd HH:mm:ss') %}</td>
										<td>{%= (item.bookingAmount).formatMoney(2, '') %}</td>
							            <td>{%= item.summary %}</td>
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
	<script src="${ctx.resource}/js/bootstrap-modal-trigger.js"></script>
</body>
</html>

