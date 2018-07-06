<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="now" class="java.util.Date" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>

<fmt:setBundle basename="ApplicationMessage" />
<fmt:setLocale value="zh_CN" />

<%@ include file="/WEB-INF/include/meta.jsp"%>
<%@ include file="/WEB-INF/include/css-newlayout.jsp"%>
<title>贷款规模管理 - 五维金融金融管理平台</title>

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
									<option value="${item.financialContractUuid}">${item.contractName}(${item.contractNo })</option>
								</c:forEach>
							</select>
						</span> 
						<span class="item beginend-datepicker"> 
							<jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
								<jsp:param value="group" name="type" />
								<jsp:param value="startDateString" name="paramName1" />
								<jsp:param value="endDateString" name="paramName2" />
								<jsp:param value="" name="paramValue1"/>
			             	   	<jsp:param value="" name="paramValue2"/>
								<jsp:param value="请输入查询起始日期" name="placeHolder1" />
								<jsp:param value="请输入查询终止日期" name="placeHolder2" />
							</jsp:include>
						</span> 
						<span class="item">
							<button id="lookup" class="btn btn-primary">查询</button>
						</span>
					</div>
				</div>
				<div class="table-area">
					<table class="data-list" data-action="./loans/query" data-autoload="false">
						<thead>
							<tr>
								<th>信托产品代码</th>
								<th>信托合同名称</th>
								<th>期初</th>
								<th>本期新增</th>
								<th>本期减少</th>
								<th>期末</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
								{% _.each(list, function(loansModel, index){ %}
									<tr>
										<td>{%= loansModel.contractNo %}</td>
										<td>{%= loansModel.contractName %}</td>
										<td>{%= loansModel.beginningLoans %}</td>
										<td>{%= loansModel.newLoans %}</td>
										<td>{%= loansModel.reduceLoans %}</td>
										<td>{%= loansModel.endingLoans %}</td>
									</tr> 
								{% }); %}
							</script>
						</tbody>
					</table>
				</div>
			</div>
			<div class="operations">
				<button data-action="./loans/exprot" type="button" class="btn export-excel">导出报表</button>
				<%-- <jsp:include page="/WEB-INF/include/plugins/assembly.jsp">
					<jsp:param value="page-control" name="type" />
					<jsp:param value="true" name="advanced" />
				</jsp:include> --%>
			</div>
		</div>
	</div>

		<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
</body>
</html>