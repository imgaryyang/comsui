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
	<title>充值单列表 - 五维金融金融管理平台</title>

	<style type="text/css">
		span.important {
			color: red;
		}
		p.tip{
			margin: 0px;
			color: #666;
		}
		.data-list a.disabled {
			color: #999!important;
		}
	</style>

	<script type="template/script" id="EchargeCancelDialogTmpl">
		<div class="cancel-default">
			<div class="tip-area">
				<p class="tip" style="margin-top:10px;">你确定作废<span class="important">{%= rechargeSourceDocumentNo %}</span>充值账单吗？</p>
				<p class="tip">此操作会将<span class="important">{%= bookingAmount %}</span>元从<span class="important">{%= virtualAccountName %}</span>账户中转至云信专户</p>
			</div>
			<div class="remark-area">
				<input style="width: 285px; margin: 10px auto 0px auto" id="remark" type="text" class="form-control" placeholder="原因备注(选填)">
			</div>
		</div>
	</script>

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
                                <select class="form-control real-value" name="sourceDocumentStatus" >
                                    <option value="-1">状态</option>
                                    <c:forEach var="item" items="${sourceDocumentStatusList }">
                                        <option value="${item.ordinal}"><fmt:message key="${item.key}" /></option>
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
                                    <option value="key">客户名称</option>
                                    <option value="key">账户编号</option>
                                    <option value="key">充值金额</option>
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
					<table class="data-list large-columns" data-action="${ctx}/capital/customer-account-manage/deposit-receipt-list/query" data-autoload="true">
						<thead>
							<tr>
								<th width="13%">充值单号</th>
								<th width="13%">账户编号</th>
								<th>账户名称</th>
								<th>客户类型</th>
								<th width="14%">贷款合同编号</th>
								<th>信托项目编号</th>
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
								<th width="14%">机构流水</th>
								<th>状态</th>
								<th width="50">操作</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
							    {% _.each(list, function(item, index){ %}
							        <tr data-uuid='{%=item.sourceDocumentUuid %}' 
					                	data-sourcedocumentno='{%= item.sourceDocumentNo %}'
					                	data-virtualaccountname='{%= item.virtualAccountName %}'
					                	data-virtualaccountno='{%= item.virtualAccountNo %}'
					                	data-bookingamount='{%= (item.bookingAmount).formatMoney(2, '') %}'
								    >
							            <td>
							            	<a href="${ctx}/capital/customer-account-manage/deposit-receipt-list/account-detail#/capital/account/deposit-receipt/{%= item.sourceDocumentUuid %}/detail">{%= item.sourceDocumentNo %}</a>
							            </td>
							            <td>
							            	<a href="${ctx}/capital/customer-account-manage/virtual-account-list/detail#/capital/account/virtual-acctount/{%= item.virtualAccountUuid %}/detail">{%= item.virtualAccountNo %}</a>
							            </td>
										<td>{%= item.virtualAccountName %}</td>
							            <td>{%= item.customerTypeName %}</td>
							            <td>
							            	<a href="${ctx}/contracts#/data/contracts/detail?id={%= item.contractId %}">{%= item.contractNo %}</a>
							            </td>
							            <td>
							            	<a href="${ctx}/financialContract/new-financialContract#/financial/contract/{%= item.financialContractUuid %}/detail">{%= item.financialContractNo %}</a>
							            </td>
										<td>{%= new Date(item.createTime).format('yyyy-MM-dd HH:mm:ss') %}</td>
										<td>{%= (item.bookingAmount).formatMoney(2, '') %}</td>
							            <td>{%= item.summary %}</td>
							            <td><a href="${ctx}/capital/account-manager/cash-flow-audit/show#hostAccountNo={%= item.accountNo %}&keyWords={%= item.institutions %}">{%= item.institutions %}</a></td>
										<td>
											{% if(item.sourceDocumentStatusName == '充值撤销'){ %}
												<span class="color-danger">{%= item.sourceDocumentStatusName %}</span>
											{% } else { %}
												{%= item.sourceDocumentStatusName %}
											{% } %}
										</td>
							            <td>
											<sec:authorize ifAnyGranted="ROLE_SUPER_USER">
											{% if(item.sourceDocumentStatusName != '充值撤销'){ %}
							                	<a class='echarge-cancel'>
							                		作废
							                	</a>
											{% } %}
											</sec:authorize>
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

	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>

	<script src="${ctx.resource}/js/bootstrap-modal-trigger.js"></script>
</body>
</html>
