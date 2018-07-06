<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>

<fmt:setBundle basename="ApplicationMessage" />
<fmt:setLocale value="zh-CN" />

<%@ include file="/WEB-INF/include/meta.jsp"%>
<%@ include file="/WEB-INF/include/css-newlayout.jsp"%>
<title>扣款订单详情 - 五维金融金融管理平台</title>

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
			            <span class="item current">扣款单详情</span>
					</div>
				</div>
				
				<div class="col-layout-detail">
					<div class="top">
						<div class="block">
							<h5 class="hd">贷款信息</h5>
							<div class="bd">
								<div class="col">
									<p>合同编号 ：
										<a class="contractNo" 
										href="${ctx}/contracts#/data/contracts/detail?id=${showModel.loanInformation.loanContractId}">${showModel.loanInformation.loanContractNo}</a>
									</p>
									<p>贷款客户编号 ：${showModel.loanInformation.loanCustoemrNo}</p>
									<p>资产编号 ：${showModel.loanInformation.assetSetNo}</p>
									<p>还款编号 ：${showModel.loanInformation.repaymentPlanNos}</p>
								</div>
							</div>
						</div>
						<div class="block">
							<h5 class="hd">扣款信息</h5>
							<div class="bd">
								<div class="col">
									<p>扣款单号 ：${showModel.deductInformation.deductApplicationCode}</p>
									<p>扣款创建日期 ：<fmt:formatDate value="${showModel.deductInformation.deductCreateDate}" pattern="yyyy-MM-dd" /></p>
									<p>扣款状态 ：${showModel.deductInformation.deductStatus}</p>
									<p>还款类型 ：${showModel.deductInformation.repaymentType}</p>
								</div>
								<div class="col">
									<p>扣款金额 ：
										<fmt:formatNumber type='number' pattern='#,##0.00#' value='${showModel.deductInformation.planDeductAmount}' />
									</p>
									<p>实际扣款金额 ：<fmt:formatNumber type='number' pattern='#,##0.00#' value='${showModel.deductInformation.actualDeductAmount}' /></p>
									<p>扣款受理时间 ：<fmt:formatDate value="${showModel.deductInformation.deductReceiveTime}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
									<p>发生时间 ：<fmt:formatDate value="${showModel.deductInformation.deductHappenTime}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
									<p>备注 ：${showModel.deductInformation.remark}</p>
								</div>
							</div>
						</div>
						<div class="block">
							<h5 class="hd">账户信息</h5>
							<div class="bd">
								<div class="col">
									<p>客户名称 ：<span class="customer">${showModel.accountInformation.customerName}</span></p>
									<p>账户开户行 ：${showModel.accountInformation.bank}</p>
									<p>开户行所在地 ：${showModel.accountInformation.addressOfBank}</p>
									<p>绑定账号：${showModel.accountInformation.repaymentAccountNo}</p>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="table-layout-detail">
					<div class="block">
						<h5 class="hd">线上支付记录</h5>
						<div class="bd">
							<table>
								<thead>
									<tr>
										<th>支付编号</th>
										<th>扣款单号</th>
										<th>银行名称</th>
										<th>银行账户号</th>
										<th>代扣金额</th>
										<th>发生时间</th>
										<th>状态</th>
										<th>备注</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${showModel.paymentOrderInformations}">
										<tr>
											<td>${item.paymentOrderNo}</td>
											<td>${item.deductOrderNo}</td>
											<td>${item.bankName}</td>
											<td>${item.bankAccount}</td>
											<td><fmt:formatNumber type='number' pattern='#,##0.00#' value='${item.deductAmount}' /></td>
											<td><fmt:formatDate value="${item.occurTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
											<td>
												<span class="color-danger">${item.repaymentPlanStatus}</span>
											</td>
											<td>${item.remark}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="table-layout-detail">
					<div class="block">
						<h5 class="hd">凭证明细</h5>
						<div class="bd">
							<table>
								<thead>
									<tr>
										<th>凭证编号</th>
										<th>专户账号</th>
										<th>往来机构名称</th>
										<th>账户姓名</th>
										<th>机构账户号</th>
										<th>凭证金额</th>
										<th>清算单号</th>
										<th>代扣类型</th>
										<th>凭证来源</th>
										<th>第三方通道</th>
										<th>凭证状态</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${showModel.thirdPartVoucherDeductShowModels}">
										<tr>
											<td><a href="${ctx}/voucher/thirdParty#/capital/voucher/third-party/${item.voucherUuid}/detail">${item.voucherCode}</a></td>
											<td>${item.specialAccountNo}</td>
											<td>${item.bankAccountName}</td>
											<td>${item.accountPayerName}</td>
											<td>${item.bankAccountNo}</td>
											<td>${item.voucherAmount}</td>
											<td>${item.settleClearNo}</td>
											<td>${item.deductType}</td>
											<td>${item.voucherSource}</td>
											<td>${item.thirdPartPaymentChannel}</td>
											<td>${item.voucherStatus}</td>
										</tr>
									</c:forEach>									
								</tbody>
							</table>
						</div>
					</div>

					<div class="block">
                        <jsp:include page="/WEB-INF/include/system-operate-log.jsp">
                            <jsp:param value="${showModel.deductInformation.deductApplicationCode}" name="objectUuid"/>
                        </jsp:include>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
	
</body>
</html>
