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
		.form-wrapper .field-row .field-value {
			text-align: center;
		}
		.form-wrapper .form .form-control {
			min-width: 285px;
		}
		.data-list a.disabled {
			color: #999!important;
		}
	</style>
	<title>余额支付单 - 五维金融金融管理平台</title>
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
								<select class="form-control real-value" name="status" >
									<option value="-1">全部状态</option>
									<c:forEach var="item" items="${journalVoucherStatus}">
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
									<option value="key">支付单号</option>
									<option value="key">账户名称</option>
									<option value="key">账户编号</option>
									<option value="key">支付金额</option>
									<option value="key">还款编号</option>
									<option value="key">订单编号</option>
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
					<table class="data-list large-columns" data-action="${ctx}/capital/customer-account-manage/payment-order-list/query" data-autoload="true">
						
						<thead>
							<tr>
								<th style="width: 12%">支付单号</th>
								<th>账户编号</th>
								<th>账户名称</th>
								<th style="width: 12%">贷款合同编号</th>
								<th style="width: 12%">还款编号</th>
								<th>订单编号</th>
								<th>信托项目名称</th>
								<th>
									<a data-paramname="createdDate" class="sort none">
                                        创建时间<i class="icon"></i>
                                    </a>
								</th>
								<th>
									<a data-paramname="bookingAmount" class="sort none">
                                        支付金额<i class="icon"></i>
                                    </a>
								</th>
								<th style="width: 50px">凭证</th>
								<th>状态</th>
								<th width="50">操作</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
							    {% _.each(list, function(item, index){ %}
							        <tr 
							        	data-source-document-uuid="{%= item.sourceDocumentUuid %}"
							        	data-source-document-no="{%= item.sourceDocumentNo %}"
							        	data-uuid="{%= item.journalVoucherUuid %}">
							            <td>
							            	<a class="journal-voucher-no" 
							            		href="${ctx}/capital/customer-account-manage/payment-order-list/detail#/capital/account/payment-order/{%= item.journalVoucherUuid %}/detail">
							            		{%= item.journalVoucherNo %}
							            	</a>
							            </td>
										<td>
											<a href="${ctx}/capital/customer-account-manage/virtual-account-list/detail#/capital/account/virtual-acctount/{%= item.virtualAccountUuid %}/detail">{%= item.virtualAccountNo %}</a>
										</td>
							            <td>{%= item.virtualAccountName %}</td>
							            <td>
								            <a href="${ctx}/contracts/detail?id={%= item.contractId %}">{%= item.contractNo %}</a>
							            </td>
							            <td>
							            	<a href="${ctx}/assets/{%= item.assetsetId %}/detail#/assets/{%= item.assetsetId %}/detail">{%= item.assetSetNo %}</a>
							            </td>
										<td>
											{% if(item.orderTypeName == '结算单') { %}
												<a href="${ctx}/payment-manage/order/{%= item.orderId %}/detail">{%= item.orderNo %}</a>
											{% }else { %}
												<a href="${ctx}/guarantee/order/{%= item.orderId %}/guarantee-detail">{%= item.orderNo %}</a>
											{% } %}
										</td>
										<td>
											<a href="${ctx}/financialContract/new-financialContract#/financial/contract/{%= item.financialContractUuid %}/detail">{%= item.financialContractNo %}</a>
										</td>
										<td>{%= new Date(item.createTime).format('yyyy-MM-dd HH:mm:ss') %}</td>
										<td class='booking-amount'>{%= (item.amount).formatMoney(2, '') %}</td>
										<td class="voucher">
											{% if (item.sourceDocumentNo) { %}
												<a href="#">{%=item.sourceDocumentNo %}</a>
											{% } %}
										</td>
										<td>
											
											{% if(item.statusName == '已退款'){ %}
												<span class="color-danger">{%= item.statusName %}</span>
											{% } else { %}
												{%= item.statusName %}
											{% } %}
										</td>
							            <td>
											<sec:authorize ifAnyGranted="ROLE_SUPER_USER">
											{% if(item.canRefund){ %}
												<a href="javascript: void 0;" class="refund">退款</a>
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
	<script type="template/script" id="RefundSuccessTmpl">
		<div class="cancel-success" >
			<p class="tip" style="margin-top:10px;">余额支付单<span class="color-danger">{%= journalVoucherNo %}</span>退款成功</p>
			<p class="tip">请及时对凭证<span class="color-danger">{%= sourceDocumentNo %}</span>进行销账处理！</p>
		</div>
	</script>
	<script type="script/templete" id='RefundTmpl'>

	    <form class="form adapt" novalidate="novalidate">
	        <div class="field-row">
				<div class="field-value">
					<span>退款<span class='color-danger'>{%= bookingAmount %}</span>至<span class='color-danger'>{%= counterPartyName %}</span>客户账户<span class='color-danger'>{%= counterPartyAccount %}</span>?</span>
				</div>
	        </div>
	        <div class="field-row">
	            <div class="field-value">
	                <input style="width: 285px; margin: 20px auto 0px auto" type="text" id='appendix' name='appendix' class="form-control real-value" placeholder='原因备注（选填）'>
	            </div>
	        </div>

	    </form>
	</script>
	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
	<script src="${ctx.resource}/js/bootstrap-modal-trigger.js"></script>
</body>
</html>

