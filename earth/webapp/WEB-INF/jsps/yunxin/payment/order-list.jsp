
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>

<fmt:setBundle basename="ApplicationMessage" />
<fmt:setLocale value="zh_CN" />

<%@ include file="/WEB-INF/include/meta.jsp"%>
<%@ include file="/WEB-INF/include/css-newlayout.jsp"%>
<title>结算单列表 - 五维金融金融管理平台</title>

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
							<select name="financialContractIds" class="form-control real-value selectpicker" multiple data-title="信托合同项目"  data-actions-box="true">
								<c:forEach var="item" items="${financialContracts}">
									<option value="${item.id }">${item.contractName}(${item.contractNo })</option>
								</c:forEach>
							</select>
						</span>
						<span class="item"> 
							<select name="executingSettlingStatusInt" class="form-control real-value small" id="executingSettlingStatusSelect">
								<option value="-1">结算状态</option>
								<c:forEach var="item" items="${executingSettlingStatusList}">
									<option value="${item.ordinal()}">
										<fmt:message key="${item.key}" />
									</option>
								</c:forEach>
							</select>
						</span>
						<span class="item beginend-datepicker">
							<jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
								<jsp:param value="group" name="type" />
								<jsp:param value="createTimeStartDateString" name="paramName1" />
								<jsp:param value="createTimeEndDateString" name="paramName2" />
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
								<option value="orderNo">结算单号</option>
								<option value="singleLoanContractNo">还款编号</option>
							</select>
							<input type="text" name="keyWords" class="form-control real-value input-key-words" placeholder="请输入关键字..." >
						</span>
						<span class="item">
							<button id="lookup" class="btn btn-primary">查询</button>
						</span>
					</div>
				</div>
				<div class="table-area">
					<table class="data-list"
						data-action="${ctx}/payment-manage/order/query"
						data-autoload="true">
						<thead>
							<tr>
								<th>结算单号</th>
								<th>还款编号</th>
								<th>
									<a data-paramname="assetRecycleDate" class="sort none">
										计划还款日期<i class="icon"></i>
									</a>
								</th>
								<th>
									<a data-paramname="totalRent" class="sort none">
										结算金额（元）<i class="icon"></i>
									</a>
								</th>
								<th>
									<a data-paramname="createTime" class="sort none">
										创建时间<i class="icon"></i>
									</a>
								</th>
								<th>
									<a data-paramname="modifyTime" class="sort none">
										状态变更时间<i class="icon"></i>
									</a>
								</th>
								<th>结算状态</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
								{% var ExecutingSettlingStatus = { CREATE:'已创建', DOING:'处理中', SUCCESS:'成功', FAIL:'失败', CLOSED:'已关闭' }; %}
							    {% _.each(list, function(order, index) { %}
							        <tr>
										<td><a href="${ctx}/payment-manage/order/{%= order.id %}/detail">{%= order.orderNo %}</a></td>
										<td><a href="${ctx}/assets/{%= order.assetSet.id %}/detail#/assets/{%= order.assetSet.id %}/detail">{%= order.singleLoanContractNo %}</a></td>
										<td>{%= new Date(order.assetRecycleDate).format('yyyy-MM-dd') %}</td>
										<td data-order-id="{%= order.id %}">
											<span   class="showPopover"
                                                data-container="body"
                                                data-placement="right"
                                                data-html="true"
                                                data-trigger="focus" 
                                                data-toggle="popover">
												{%= (+order.totalRent).formatMoney(2,'') %}
                                            </span>
										</td>
										<td>{%= new Date(order.createTime).format('yyyy-MM-dd')%}</td>
										<td>{%= new Date(order.modifyTime).format('yyyy-MM-dd') %}</td>
										<td>
											{%	if(order.executingSettlingStatus == 'FAIL'){ %}
													<span class="color-danger">失败</span>
											{%  }else{  %}
													{%= ExecutingSettlingStatus[order.executingSettlingStatus] %}
											{%  }  %}
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
		            <jsp:param value="true" name="statistical"/>
	            </jsp:include>
			</div>
		</div>

	</div>

	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>

</body>

</html>
