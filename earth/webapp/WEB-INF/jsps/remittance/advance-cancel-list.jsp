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
<title>代付撤销单列表 - 五维金融金融管理平台</title>
</head>
<body>
	<%@ include file="/WEB-INF/include/header.jsp"%>
	<div class="web-g-main">
		<%@ include file="/WEB-INF/include/aside.jsp"%>
		<div class="content">
			<div class="scroller">
				<div class="lookup-params">
					<div class="inner clearfix">
						<span class="item">
							<select name="financialContractUuids" class="form-control real-value selectpicker" multiple data-actions-box="true" selectedTextFormat="static" data-title="信托合作项目">
								<c:forEach var="item" items="${financialContracts}">
									<option value="${item.financialContractUuid}">${item.contractName}(${item.contractNo})</option>
								</c:forEach>
							</select>
						</span>
						<span class="item">
							<select name="paymentInstitutionOrdinals" class="form-control real-value selectpicker"
							multiple data-actions-box="true" selectedTextFormat="static" data-title="放款通道">
								<c:forEach var="item" items="${paymentInstitutionNames}">
									<option value="${item.ordinal()}" selected ><fmt:message key='${item.key}'/></option>
								</c:forEach>
							</select>
						</span>
						<span class="item beginend-datepicker">
							<jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
								<jsp:param value="group" name="type" />
								<jsp:param value="startDate" name="paramName1" />
								<jsp:param value="endDate" name="paramName2" />
								<jsp:param value="创建日期起始" name="placeHolder1" />
								<jsp:param value="创建日期终止" name="placeHolder2" />
							</jsp:include>
						</span>
						<span class="item vertical-line"></span>
						<span class="item">
							<select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
								<option value="channelCashFlowNo">通道流水号</option>
								<option value="returnedAccountNo">退回账户</option>
							</select>
							<input type="text" name="keyWords" class="form-control real-value input-key-words" placeholder="请输入关键字..." >
						</span> 
						<span class="item">
							<button id="lookup" class="btn btn-primary">
								查询
							</button>
						</span>
					</div>
				</div>	
				<div class="table-area">
					<table class="data-list" data-action="${ctx}/capital/refundbill/query" data-autoload="true">
						<thead>
							<tr>
								<th width="20%">撤销单号</th>
								<th width="20%">通道流水号</th>
								<th>通道名称</th>
								<th>
									<a data-paramname="createTime" class="sort none">
										发生时间<i class="icon"></i>
									</a>
								</th>
								<th>退回账户</th>
								<th>交易类型</th>
								<th>
									<a data-paramname="amount" class="sort none">
										金额<i class="icon"></i>
									</a>
								</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
								{% _.each(list,function(item,index) { %}
									<tr>
										<td>{%= item.remittanceRefundBillUuid%}</td>
										<td>{%= item.channelCashFlowNo%}</td>
										<td>{%= item.paymentChannelName%}</td>
										<td>{%= new Date(item.createTime).format('yyyy-MM-dd HH:mm:ss')%}</td>
										<td>{%= item.hostAccountNo%}</td>
										<td>冲账</td>
										<td>{%= (+item.amount).formatMoney(2,'')%}</td>
									</tr>
								{% }) %}
							</script>
						</tbody>
					</table>
				</div>				
			</div>

			<div class="operations">
				<jsp:include page="/WEB-INF/include/plugins/assembly.jsp">
		            <jsp:param value="page-control" name="type"/>
		            <jsp:param value="true" name="advanced"/>
		            <jsp:param value="true" name="statistical"/>
	            </jsp:include>
			</div>	
		</div>
	</div>
	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
</body>
</html>>
