<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.zufangbao.gluon.spec.global.GlobalCodeSpec"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>

<fmt:setBundle basename="ApplicationMessage" />
<fmt:setLocale value="zh_CN" />

<%@ include file="/WEB-INF/include/meta.jsp"%>
<%@ include file="/WEB-INF/include/css-newlayout.jsp"%>
<title>扣款订单列表 - 五维金融金融管理平台</title>
</head>
<body>

	<%@ include file="/WEB-INF/include/header.jsp"%>

	<div class="web-g-main">

		<%@ include file="/WEB-INF/include/aside.jsp"%>
		<div class="content">
			<div class="scroller">
				<div class="lookup-params">
					<div class="inner clearfix">
						<input type="hidden" name="filter" value="true" class="real-value">
						<span class="item"> 
							<select name="financialContractIds" class="form-control real-value selectpicker" multiple data-title="信托合同项目"  data-actions-box="true">
								<c:forEach var="item" items="${financialContracts}">
									<option value="${item.uuid }">${item.contractName}(${item.contractNo })</option>
								</c:forEach>
							</select>
						</span>
						<span class="item">
							<select name="repaymentType" class="form-control real-value selectpicker" multiple data-title="订单类型" data-actions-box="true">
								<c:forEach var="item" items="${repaymentType}">
									<option selected value="${item.ordinal }"><fmt:message key="${item.key}"/></option>
								</c:forEach>
							</select>
						</span>
						<span class="item">
							<select name="executionStatus" class="form-control real-value selectpicker" multiple data-title="订单状态" data-actions-box="true">
								<c:forEach var="item" items="${orderStatus}">
									<option selected value="${item.ordinal }"><fmt:message key="${item.key}"/></option>
								</c:forEach>
							</select>
						</span>
						<span class="item beginend-datepicker"> 
							<jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
								<jsp:param value="group" name="type" />
								<jsp:param value="createStartDate" name="paramName1" />
								<jsp:param value="createEndDate" name="paramName2" />
								<jsp:param value="创建起始日期" name="placeHolder1" />
								<jsp:param value="创建终止日期" name="placeHolder2" />
								<jsp:param value="true" name="pickTime"/>
								<jsp:param value="true" name="formatToMinimum"/>
								<jsp:param value="true" name="formatToMaximum"/>
							</jsp:include>
						</span>
						<span class="item vertical-line"></span> 
						<span class="item">
							<select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
								<option value="customerName">客户姓名</option>
								<option value="loanContractNo">合同编号</option>
								<option value="deudctNo">订单编号</option>
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
					<table class="data-list" 
						data-action="./application/query" 
						data-autoload="true">
						<thead>
							<tr>
								<th width="15%">订单编号</th>
								<th>合同编号</th>
								<th width="15%">客户名称</th>
								<th>创建时间</th>
								<th>订单类型</th>
								<th>
									<a data-paramname="planDeductAmount" class="sort none">
										扣款金额<i class="icon"></i>
									</a>
								</th>
								<th>
									<a data-paramname="deductApplicationDate" class="sort none">
										状态变更时间<i class="icon"></i>
									</a>
								</th>
								<th>状态</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
								{% _.each(list, function(item, index) { %}
									<tr>
										<td><a href="./application/detail/{%= item.deudctNo %}">{%= item.deudctNo %}</a></td>
										<td>{%= item.loanContractNo %}</td>
										<td>{%= item.customerName %}</td>
										<td>{%= item.createTime %}</td>
										<td>{%= item.repaymentType %}</td>
										<td>{%= (+item.planDeductAmount).formatMoney(2,'') %}</td>
										<td>{%= new Date(item.statusModifyTime).format("yyyy-MM-dd") %}</td>
										<td>
											{% if(item.deductStatus =='异常'){ %}
												<span class="color-warning">异常</span>
											{%	}else if(item.deductStatus == '失败'){ %}
												<span class="color-danger">失败</span>
											{%  }else{  %}
												{%= item.deductStatus%}
											{%  }  %}
										</td>
										<td>{%= item.remark %}</td>
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
		            <jsp:param value="true" name="statistical"/>
	            </jsp:include>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
</body>
</html>
