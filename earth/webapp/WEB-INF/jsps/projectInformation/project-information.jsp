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
<title>项目信息列表 - 五维金融金融管理平台</title>
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
							<select name="financialContractUuids" class="form-control real-value selectpicker" multiple data-title="信托合同项目"  data-actions-box="true">
								<c:forEach var="item" items="${financialContracts}">
									<option value="${item.financialContractUuid}">${item.contractName}(${item.contractNo })</option>
								</c:forEach>
							</select>
						</span> 
						<span class="item beginend-datepicker"> 
							<jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
								<jsp:param value="group" name="type" />
								<jsp:param value="loanEffectStartDate" name="paramName1" />
								<jsp:param value="loanEffectEndDate" name="paramName2" />
								<jsp:param value="请输入生效起始日期" name="placeHolder1" />
								<jsp:param value="请输入生效终止日期" name="placeHolder2" />
							</jsp:include>
						</span> 
						<span class="item beginend-datepicker"> 
							<jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
								<jsp:param value="group" name="type" />
								<jsp:param value="loanExpectTerminateStartDate" name="paramName1" />
								<jsp:param value="loanExpectTerminateEndDate" name="paramName2" />
								<jsp:param value="请输入预计终止起始日期" name="placeHolder1" />
								<jsp:param value="请输入预计终止终止日期" name="placeHolder2" />
							</jsp:include>
						</span>
						<span class="item vertical-line"></span>
						<span class="item">
							<select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
								<option value="contractNo">贷款合同编号</option>
								<option value="underlyingAsset">资产编号</option>
								<option value="customerName">客户姓名</option>
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
					<table class="data-list large-columns" data-action="./project-information/query" data-autoload="true">
						<thead>
							<tr>
								<th>贷款合同编号</th>
								<th>资产编号</th>
								<th>
									<a data-paramname="loanRate" class="sort none">
										贷款利率<i class="icon"></i>
									</a>
								</th>
								<th>
									<!-- <a data-paramname="effectDate" class="sort none">
										生效日期<i class="icon"></i>
									</a> -->
									生效日期
								</th>
								<th>
									<!-- <a data-paramname="expectTerminalDate" class="sort none">
										预计终止日期<i class="icon"></i>
									</a> -->
									预计终止日期
								</th>
								<th>
									<!-- <a data-paramname="actualTermainalDate" class="sort none">
										实际终止日期<i class="icon"></i>
									</a> -->
									实际终止日期
								</th>
								<th>还款进度</th>
								<th>贷款方式</th>
								<th>还款周期</th>
								<th>
									<!-- <a data-paramname="repaymentDate" class="sort none">
										还款日期<i class="icon"></i>
									</a> -->
									还款日期
								</th>
								<th>客户姓名</th>
								<th>
									<a data-paramname="loanAmount" class="sort none">
										贷款总额<i class="icon"></i>
									</a>
								</th>
								<th>
									<!-- <a data-paramname="currentPeriodRepaymentAmount" class="sort none">
										本期还款金额<i class="icon"></i>
									</a> -->
									本期还款金额
								</th>
								<th>
									<!-- <a data-paramname="currentPeriodRepaymentInterest" class="sort none">
										本期还款利息<i class="icon"></i>
									</a> -->
									本期还款利息
								</th>
								<th>还款情况</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
							    {% _.each(list, function(projectInformationShowVO, index) { %}
							        <tr>
										<td>
											<a href="${ctx}/contracts#/data/contracts/detail?id={%= projectInformationShowVO.contractId %}">
												{%= projectInformationShowVO.contractNo %}
											</a>
										</td>
										<td>{%= projectInformationShowVO.assetNo %}</td>
										<td>{%= (+projectInformationShowVO.loanRate * 100).formatPercent(2) %}</td>
										<td>{%= new Date(projectInformationShowVO.effectDate).format('yyyy-MM-dd') %}</td>
                                        <td>{%= new Date(projectInformationShowVO.expectTerminalDate).format('yyyy-MM-dd') %}</td>
                                        <td>{%= new Date(projectInformationShowVO.actualTermainalDate).format('yyyy-MM-dd') %}</td>
										<td>{%= projectInformationShowVO.repaymentSchedule %}</td>
										<td>{%= projectInformationShowVO.loanType %}</td>
										<td>{%= projectInformationShowVO.repaymentCycle %}月付</td>
										<td>{%= new Date(projectInformationShowVO.repaymentDate).format('yyyy-MM-dd') %}</td>
                                        <td>{%= projectInformationShowVO.customerName %}</td>
                                        <td>{%= (+projectInformationShowVO.loanAmount).formatMoney(2, '') %}</td>
                                        <td>{%= (+projectInformationShowVO.currentPeriodRepaymentAmount).formatMoney(2, '') %}</td>
                                        <td>{%= (+projectInformationShowVO.currentPeriodRepaymentInterest).formatMoney(2, '') %}</td>
                                        <td>{%= projectInformationShowVO.repaymentSituation%}</td>
							        </tr>
							    {% }); %}
						    </script>
						</tbody>
					</table>
				</div>
			</div>
			<div class="operations">
					<button data-action="./project-information/export-excel" type="button" class="btn export-excel">导出项目信息</button>
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
