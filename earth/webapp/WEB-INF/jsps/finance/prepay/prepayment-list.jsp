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
<title>提前还款列表 - 五维金融金融管理平台</title>
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
							<select name="financialContractUuids" class="form-control real-value selectpicker" multiple data-title="信托合同项目"  data-actions-box="true">
								<c:forEach var="item" items="${financialContracts}">
									<option value="${item.uuid}">${item.contractName}(${item.contractNo })</option>
								</c:forEach>
							</select>
						</span>
						<span class="item" style="width: 127px;"> 
							<select name="appIds" class="form-control real-value selectpicker" multiple data-title="合作商户" data-actions-box="true">
								<c:forEach var="item" items="${apps}">
									<option selected value="${item.id }">${item.name}</option>
								</c:forEach>
							</select>
						</span>
						<span class="item">
			              <jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
			                <jsp:param value="false" name="calendarbtn"/>
			                <jsp:param value="true" name="clearbtn"/>
			                <jsp:param value="" name="paramName1"/>
			                <jsp:param value="提前还款日期" name="placeHolder1"/>
			              </jsp:include>
			            </span>
			            <span class="item">
							<select class="form-control real-value selectpicker" name="paymentStatusOrdinals" multiple data-title="还款状态"  data-actions-box="true">
								<c:forEach var="payment_status" items="${paymentStatusList }">
									<option selected value="${payment_status.ordinal() }">
										<fmt:message key="${payment_status.key}"></fmt:message>
									</option>
								</c:forEach>
							</select>
						</span>
			            <span class="item">
							<select class="form-control real-value selectpicker" name="prepaymentTypes" multiple data-title="提前还款类型"  data-actions-box="true">
								<c:forEach var="payment_status" items="${prepaymentTypes }">
									<option selected value="${payment_status.ordinal() }">
										<fmt:message key="${payment_status.key}"></fmt:message>
									</option>
								</c:forEach>
							</select>
						</span>
						<span class="item vertical-line"></span>
						<span class="item">
							<select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
								<option value="singleLoanContractNo">还款编号</option>
								<option value="contractNo">合同编号</option>
								<option value="customerName">客户姓名</option>
							</select>
							<input type="text" name="keyWords" class="form-control real-value input-key-words" placeholder="请输入关键字..." >
						</span>
			            <span class="item">
			                <button id="lookup" class="btn btn-primary">查询</button>
			            </span>
					</div>
				</div>
	
				<div class="table-area">
					<table class="data-list large-columns" data-action="${ctx}/assets/prepayment/query" data-autoload="true">
						<thead>
							<tr>
								<th>还款编号</th>
								<th>合同编号</th>
								<th>客户姓名</th>
								<th>提前还款日期</th>
								<th>提前还款金额</th>
								<th>实际还款金额</th>
								<th>还款状态</th>
								<th>提前还款类型</th>
								<th>还款单状态</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
							    {% _.each(list, function(item, index){ %}
									<tr>
										<td><a href="/assets/{%= item.assetSetId %}/detail#/assets/{%= item.assetSetId %}/detail">{%= item.singleLoanContractNo %}</a></td>
										<td>{%= item.contractNo %}</td>
										<td>{%= item.customerName %}</td>
										<td>{%= new Date(item.assetRecycleDate).format("yyyy-MM-dd HH:mm:ss") %}</td>
										<td>{%= item.assetPrincipalValue %}</td>
										<td>{%= item.actualAmount %}</td>
										<td>{%= item.paymentStatus %}</td>
										<td>{%= item.prepaymentType %}</td>
										<td>{%= item.activeStatus %}</td>
										<td></td>
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
</body>
</html>
