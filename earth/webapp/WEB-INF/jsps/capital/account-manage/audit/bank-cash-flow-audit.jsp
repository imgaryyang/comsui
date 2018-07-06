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
<title>银行现金流列表 - 五维金融金融管理平台</title>

<style>
	.rich-dropdown-menu li > a {
	    min-height: 67px;;
	}
</style>

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
								<select class="form-control real-value selectpicker rich-dropdown-menu" data-show-content="false" name="hostAccountNo" data-size="5" title="信托项目">
									<c:forEach var="item" items="${financialContracts }">
										<option 
											data-content='<div class="selectpicker-content"><div class="identification"><img src="${ctx.resource}/images/bank-logo/bank_${fn:toLowerCase(item.capitalAccount.bankCode)}.png"></div><div class="content"><div class="title">${item.contractName }</div><div class="title">${item.capitalAccount.markedAccountNo }</div><div class="subtitle">(${item.capitalAccount.bankName})</div></div></div>' 
											value="${item.capitalAccount.accountNo}">${item.contractName}(${item.contractNo })</option>
									</c:forEach>
								</select>
							</span> 
							<span class="item"> 
								<select class="form-control real-value" name="accountSide" >
									<option value="-1">借贷标记</option>
                					<c:forEach var="item" items="${accountSideList }">
										<option value="${item.ordinal}"><fmt:message key="${item.key }"/></option>
									</c:forEach>
								</select>
							</span>
							<span class="item">
								<select class="form-control real-value" name="auditStatus" >
									<option value="-1">对账状态</option>
									<c:forEach var="item" items="${auditStatusList }">
										<option value="${item.ordinal}"><fmt:message key="${item.key }"/></option>
									</c:forEach>
								</select>
							</span>
							<span class="item beginend-datepicker">
								<jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
									<jsp:param value="group" name="type" />
									<jsp:param value="true" name="pickTime"/>
									<jsp:param value="tradeStartTime" name="paramName1" />
									<jsp:param value="tradeEndTime" name="paramName2" />
									<jsp:param value="入账时间起始" name="placeHolder1" />
									<jsp:param value="入账时间终止" name="placeHolder2" />
									<jsp:param value="true" name="formatToMinimum" />
									<jsp:param value="true" name="formatToMaximum" />
								</jsp:include>
							</span>
							<span class="item vertical-line"></span>
							<span class="item">
								<select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
									<option value="cashFlowNo">流水号</option>
									<option value="accountNo">银行账户号</option>
									<option value="accountName">账户姓名</option>
									<option value="transactionRemark">摘要内容</option>
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
					<table class="data-list data-list-cashflow" data-action="${ctx}/capital/account-manager/cash-flow-audit/query" data-autoload="true">
						
						<thead>
							<tr>
								<th width="15%">流水号</th>
								<th>借贷标志</th>
								<th>
									<a data-paramname="transactionAmount" class="sort none">
										交易金额<i class="icon"></i>
									</a>
								</th>
								<th>
									<a data-paramname="balance" class="sort none">
										瞬时余额<i class="icon"></i>
									</a>
								</th>
								<th>银行账号号</th>
								<th>账户姓名</th>
								<th>
									<a data-paramname="transactionTime" class="sort none">
										入账时间<i class="icon"></i>
									</a>
								</th>
								<th>摘要内容</th>
								<th>对账状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
					            <td>{%= obj.bankSequenceNo %}</td>
								<td>{%= obj.accountSideMsg %}</td>
								<td>{%= (+obj.transactionAmount).formatMoney(2,"") %}</td>
								<td>{%= (+obj.balance).formatMoney(2,"") %}</td>
								<td>{%= obj.counterAccountNo %}</td>
								<td>{%= obj.counterAccountName %}</td>
								<td>{%= new Date(obj.transactionTime).format('yyyy-MM-dd HH:mm:ss') %}</td>
					            <td>{%= obj.remark %}</td>
								<td>
									{% if(obj.auditStatusMsg =='部分平账'){ %}
											<span class="color-warning">部分平账</span>
									{%	}else if(obj.auditStatusMsg == '存疑'){ %}
											<span class="color-danger">存疑</span>
									{%  }else{  %}
											{%= obj.auditStatusMsg%}
									{%  }  %}
								</td>                                         
					            <td>
									<sec:authorize ifAnyGranted="ROLE_SUPER_USER">
									{% if(obj.canBeDeposited ){ %}
						                <button class="btn btn-basic btn-primary btn-recharge">充值</button>
									{% } %}
									</sec:authorize>
									<a class="expand-bill" href="${ctx}/capital/account-manager/cash-flow-audit/show-deposit-result?cashFlowUuid= {%= obj.cashFlowUuid %}">
										<i class="icon icon-expand">
										</i>
									</a>
								</td>
							</script>
						
							<script type="script/template" id="CashBillTmpl">
							  <td colspan="100">
							    <div class="bill-card">
							      <h3 class="hd">
							  		<span style="color:#666;">存疑金额：</span><span class="color-danger doubt-amount">{%= obj.doubtAmount %}</span>
								  </h3>
							      <div class="bd">
							        <table class="bill-table suspect-table" border="0">
							          	<thead>
								            <tr>
									            <th>充值单号</th>
									            <th>账户编号</th>
									            <th>客户姓名</th>
									            <th>客户类型</th>
									            <th>贷款合同编号</th>
									            <th>信托产品名称</th>
									            <th>账户余额</th>
									            <th>充值金额</th>
									            <th>备注</th>
									            <th>状态</th>
									            {% if(obj.isRecharge){ %}
										            <th>操作</th>
										        {% } %}
								            </tr>
							            </thead>
							          	<tbody class="record-list">
							            </tbody>
							        </table>
							    </div>
								<sec:authorize ifAnyGranted="ROLE_SUPER_USER">
							    {% if (obj.isRecharge && obj.doubtAmount > 0 && obj.canBeDeposited) { %}
								    <div class="ft clearfix text-align-center">
								    	<a class="add-bill" style="display: inline;" href="javascript: void 0;">+  新增充值账单</a>
								    </div>
							    {% } %}
								</sec:authorize>
							    </div>
							  </td>
							</script>

							<script type="script/template" id='CashBillItemTmpl'>
								<td>{%= obj.showData.depositNo %}</td>
								<td>{%= obj.showData.virtualAccountNo %}</td>
								<td>{%= obj.showData.customerName %}</td>
								<td>{%= obj.showData.customerTypeMsg %}</td>
								<td>{%= obj.showData.contractNo %}</td>
								<td>{%= obj.showData.financialContractName %}</td>
								<td>{%= (+obj.balance).formatMoney(2, '') %}</td>
								<td class="color-success">
									<div class="validate-input-container">
										{% if(obj.isRecharge && obj.isCreated){ %}
											<input type="text" name="depositAmount" class="color-success" value="{%= obj.depositAmount %}">
										{% }else { %}
											{%= (+obj.depositAmount).formatMoney(2, '') %}
										{% } %}
									</div>
								</td>
								<td>
									<div class="validate-input-container">
										{% if(obj.isRecharge && obj.isCreated){ %}
											<input type="text" name="remark" value="{%= obj.remark %}">
										{% }else { %}
											{%= obj.remark %}
										{% } %}
									</div>
								</td>
								<td>
									{%= obj.sourceDocumentStatusEnum %}
								</td>
								<sec:authorize ifAnyGranted="ROLE_SUPER_USER">
								{% if(obj.isRecharge){ %}
									<td>
										{% if(obj.isCreated) { %}
											<button class="btn btn-basic btn-success">提交</button>
										{% } else { %}
											<button class="btn btn-basic btn-danger">作废</button>
										{% } %}
									</td>
								{% } %}
								</sec:authorize>
							</script>
						</tbody>
					</table>
				</div>
			</div>
			<div class="operations">
				<button data-action="/report/export" type="button" class="btn export-excel">导出银行流水</button>
				<i class="icon icon-help" style="margin-left:22px;" data-title="由于业务数据量过大，时间跨度最长为3*24小时整；<br/>若需更多数据，可联系数据维护人员。"></i>
				<jsp:include page="/WEB-INF/include/plugins/assembly.jsp">
		            <jsp:param value="page-control" name="type"/>
		            <jsp:param value="true" name="advanced"/>
	            </jsp:include>
			</div>
		</div>
	</div>
	
	<script type="script/template" id='AddBillDialogTmpl'>
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close close-dialog" aria-label="关闭" data-dismiss="modal">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="dialoglabel">新增充值账单</h4>
				</div>
				<div class="modal-body form-wrapper">
					<form class="form adapt" novalidate="novalidate">
						<div class="field-row">
							<label for="" class="field-title">信托项目</label>
							<div class="field-value">
								<select class="form-control real-value" name="financialContractName" >
									<c:forEach var="item" items="${financialContracts }">
										<option value="${item.financialContractUuid}">${item.contractName }</option>
									</c:forEach>
								</select>
	                        </div>
						</div>
						<div class="field-row">
							<label for="" class="field-title">客户类型</label>
							<div class="field-value">
								<select class="form-control real-value" name="customerTypeMsg" >
									<c:forEach var="item" items="${customerTypeList }">
										<option value="${item.ordinal}"><fmt:message key="${item.key}" /></option>
									</c:forEach>
								</select>
	                        </div>
						</div>
						<div class="field-row">
							<label for="" class="field-title">虚户搜索</label>
							<div class="field-value">
								<input type="text" name="keyWord" class="form-control" style="width:310px;" autocomplete="off">
								<div class="extend-form auto-complete-list" style="display: none; position: static; min-width: auto;">
								</div>
	                        </div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default close-dialog" data-dismiss="modal">关闭</button>
					<button type="button" id="submitbutton" name="submitbutton"
						type="submit" class="btn btn-success submit">提交</button>
				</div>
			</div>
		</div>
	</script>

	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>

</body>
</html>