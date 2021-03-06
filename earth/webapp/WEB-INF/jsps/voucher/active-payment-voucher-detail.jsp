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
<title>主动还款凭证详情- 五维金融金融管理平台</title>
<script type="template/script" id="activeVoucherResourceTmpl">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button class="close close-dialog" aria-label="关闭" data-dismiss="modal" type="button">
					<span aria-hidden="true">x</span>
				</button>
				<h4 class="modal-title" id="dialoglabel">原始图片详情</h4>
			</div>
			<div class="modal-body">
				
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default cancel-dialog" data-dismiss="modal">取消</a>
				<a href="#" class="btn btn-success submit">确认</a>
			</div>
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
				<div class="position-map">
					<div class="pull-left">
						<button type="button" onclick="window.history.go(-1);" class="btn btn-default">&lt;&lt; 返回</button>
			            当前位置:
			            <span class="item current">主动还款凭证详情</span>
					</div>
					<div class="pull-right">
						<c:if test="${detail.hasFails}">
							<button type="button" class="btn btn-default invalid">作废</button>
						</c:if>
						<c:if test="${detail.voucherStatus=='作废'}">
							<button type="button" class="btn btn-default active ">已作废</button>
						</c:if>
					</div>
				</div>

				<div class="col-layout-detail">
					<div class="top">
						<div class="block">
							<h5 class="hd">贷款信息</h5>
							<div class="bd">
								<div class="col">
									<p>合同编号：<a href="${ctx}/contracts#/data/contracts/detail?id=${detail.contract.id}">${detail.contract.contractNo}</a></p>
									<p>贷款客户端编号：${detail.customer.source}</p>
									<p>资产编号：${detail.contract.house.address}</p>
									<p>还款编号：无</p>
								</div>
							</div>
						</div>
						<div class="block">
							<h5 class="hd">凭证信息</h5>
							<div class="bd">
								<div class="col">
									<p>凭证编号：${detail.voucherNo}</p>
									<p>凭证来源：${detail.voucherSource}</p>
									<p>凭证类型：${detail.voucherType}</p>
									<p>凭证金额：
										<fmt:formatNumber type="number" pattern="#,##0.00#" value="${detail.voucherAmount }"/>
									</p>
								</div>
								<div class="col">
									<p>专户账号：${detail.receivableAccountNo}</p>
									<p>往来机构名称：${detail.paymentBank}</p>
									<p>机构账户号：${detail.paymentAccountNo}</p>
									<p>发生时间：${detail.time}</p>
									<p>原始凭证：
										<a href="${ctx}/voucher/active/detail/resource/${voucherId}" class="voucherResource">下载</a>
									</p>
								</div>
								<div class="col">
									<p>凭证状态：${detail.voucherStatus}</p>
									<p>接口请求编号：${detail.requestNo}</p>
									<p>备注：
										<span class="note-show">
											<span class="note-content" style="max-width: 120px;">${detail.comment}</span>
											<c:if test="${detail.voucherStatus!='作废'}">
												<a href="javascript:void(0)"><i class="icon icon-edit-note edit-note"></i></a>
											</c:if>
										</span>
										<span class="note-edit hide">
											<input class="note-input" style="width: 120px;" />
											<a class="save-note" style="margin-left:10px" href="#">保存</a>
										</span>
									</p>
								</div>
							</div>
						</div>
						<div class="block">
							<h5 class="hd">账号信息</h5>
							<div class="bd">
								<div class="col">
									<p>客户名称：${detail.contractAccount.payerName}</p>
									<p>账户开户行：${detail.contractAccount.bank}</p>
									<p>开户行所在地：${detail.contractAccount.province}&nbsp;${detail.contractAccount.city}</p>
									<p>绑定账号：<span class="bank">${detail.contractAccount.payAcNo}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="table-layout-detail">
					<div class="block">
						<h5 class="hd">
							凭证资金单据(银行流水)
						</h5>
						<div class="bd">
							<table class="data-list" data-action="" data-autoload="true">
								<thead>
									<tr>
										<th>支付编号</th>
										<th>付款方开户行</th>
										<th>支付机构流水号</th>
										<th>支付接口编号</th>
										<th>支付机构</th>
										<th>支付金额</th>
										<th>发生时间</th>
										<th>支付方式</th>
										<th>状态</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${not empty detail.cashFlow}">
											<tr>
												<td>${detail.cashFlow.id}</td>
												<td>${detail.cashFlow.counterBankInfo}</td>
												<td>
													${detail.cashFlow.bankSequenceNo}
												</td>
												<td>无</td>
												<td>${detail.cashFlow.counterAccountName}</td>
												<td>
													<fmt:formatNumber pattern="#,##0.00#" value = "${detail.cashFlow.transactionAmount}" type="number"/>
												</td>
												<td>${detail.cashFlow.transactionTime}</td>
												<td>无</td>
												<td>无</td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td colspan="9" align="center">
													没有更多数据
												</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="table-layout-detail voucher-query-list">
					<div class="lookup-params hide">
						<input class="real-value" type="hidden" name="voucherId" value="${voucherId}">
					</div>
					<div class="block">
						<h5 class="hd">凭证业务单据</h5>
						<div class="bd">
							<table class="data-list" data-action="${ctx}/voucher/active/detail/query" data-autoload="true">
								<thead>
									<tr>
										<th>还款编号</th>
										<th>贷款合同编号</th>
										<th>信托项目名称</th>
										<th>计划还款日期</th>
										<th>实际还款日期</th>
										<th>客户姓名</th>
										<th>应还款金额</th>
										<th>已还金额</th>
										<th>凭证类型</th>
										<th>凭证关联金额</th>
										<th>还款状态</th>
										<th>核销状态</th>
										<th>校验状态</th>
										<th>明细备注</th>
										<th width="60">操作</th>
									</tr>
								</thead>
								<tbody>
									<script class="template" type="script/template">
										{% _.each(list, function(item) { %}
												<tr data-source-document-detailId={%= item.sourceDocumentDetailId%}>
													<td>
														{% if(_.isNull(item.repaymentPlanId)){ %}
															{%= item.singleLoanContractNo %}
														{% } else {  %}
															<a href="${ctx}/assets/{%= item.repaymentPlanId %}/detail#/assets/{%= item.repaymentPlanId %}/detail">{%= item.singleLoanContractNo %}</a>
														{% } %}
													</td>
													<td>
														{% if(_.isNull(item.contractId)){ %}
															{%= item.contractNo %}
														{% } else {  %}
															<a href="${ctx}/contracts#/data/contracts/detail?id={%= item.contractId%}">{%= item.contractNo %}</a>
														{% } %}
													</td>
													<td>
														{% if(_.isNull(item.financialContractUuid)){ %}
															{%= item.financialContractName %}
														{% } else {  %}
															<a href="${ctx}/financialContract/new-financialContract#/financial/contract/{%= item.financialContractUuid%}/detail">{%= item.financialContractName %}</a>
														{% } %}
													</td>
													<td>{%= new Date(item.assetRecycleDate).format('yyyy-MM-dd ') %}</td>
													<td>{%= new Date(item.actualRecycleDate).format('yyyy-MM-dd') %}</td>
													<td>{%= item.customerName %}</td>
													<td><span class="color-danger">{%= (+item.assetFairValue).formatMoney(2, '') %}</span></td>
													<td>{%= (+item.payedAmount).formatMoney(2, '') %}</td>
													<td>{%= item.voucherType%}</td>
													<td><span class="color-danger">{%= (+item.detailAmount).formatMoney(2, '') %}</span></td>
													<td>{%= item.paymentStatus %}</td>
													<td>{%= item.detailStatus %}</td>
													<td>{%= item.verifyStatus %}</td>
													<td>{%= item.comment %}</td>
													<td>
														{% if(item.detailStatus == '未核销') { %}
															<a class="writeoff" href="#">核销</a>
														{% } %}
													</td>
												</tr>
										{% }) %}
									</script>
								</tbody>
							</table>
						</div>
						<div class="ft">
							<!-- <button type="button" onclick="javascript:void(0)" class="btn btn-default">一键核销</button> -->
							<jsp:include page="/WEB-INF/include/plugins/assembly.jsp">
					            <jsp:param value="page-control" name="type"/>
					            <jsp:param value="true" name="advanced"/>
					        </jsp:include>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
</body>
</html>