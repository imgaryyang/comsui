<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="zh-CN">
	<head>
		<fmt:setBundle basename="ApplicationMessage" />
		<fmt:setLocale value="zh_CN" />
		<%@ include file="/WEB-INF/include/meta.jsp"%>
		<%@ include file="/WEB-INF/include/css-newlayout.jsp"%>
		<title>回购详情 - 五维金融金融管理平台</title>
	</style>
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
			            	<span class="item current">回购详情</span>
						</div>
						<div class="pull-right">
							<c:choose>
								<c:when test="${repurchaseDoc.repurchaseStatus.ordinal == 0}">
									<button type="button" class="btn btn-primary nullify">作废</button>
									<button type="button" class="btn btn-primary default">违约</button>
								</c:when>
								<c:when test="${repurchaseDoc.repurchaseStatus.ordinal == 3}">
									<button type="button" class="btn btn-primary activate">激活</button>
								</c:when>
							</c:choose>
						</div>
					</div>
					<div class="col-layout-detail">
						<div class="top">
							<div class="block" style="width: 25%">
								<h5 class="hd">回购信息</h5>
								<div class="bd">
									<div class="col">
										<p>回购单号：${repurchaseDoc.repurchaseDocUuid}</p>
										<p>回购金额：<fmt:formatNumber pattern="#,##0.00#" type="number" value="${repurchaseDoc.amount}" /></p>
										<p>回购截止日：<fmt:formatDate pattern="yyyy-MM-dd" value="${repurchaseDoc.repoEndDate}" /></p>
										<p>发生时间：<fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${repurchaseDoc.creatTime}" /></p>
									</div>
								</div>
							</div>
							<div class="block" style="width: 25%">
								<h5 class="hd">贷款合同信息</h5>
								<div class="bd">
									<div class="col">
										<p>贷款合同编号：<a href="${ctx}/contracts#/data/contracts/detail?id=${contract.id }">${repurchaseDoc.contractNo}</a></p>
										<p>还款期数：${contract.periods}</p>
										<p>生效日期：<fmt:formatDate pattern="yyyy-MM-dd" value="${contract.beginDate}" /></p>
										<p>回购天数：${repurchaseDoc.repoDays}</p>
									</div>
								</div>
							</div>
							<div class="block" style="width: 25%">
								<h5 class="hd">其他</h5>
								<div class="bd">
									<div class="col">
										<p>商户编号：${repurchaseDoc.appName}</p>
										<p>状态：
											<c:if test="${ not empty repurchaseDoc.repurchaseStatus}">
												<fmt:message key="${repurchaseDoc.repurchaseStatus.key}" />
											</c:if>
										</p>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="table-layout-detail">
						<div class="block">
							<h5 class="hd">回购凭证</h5>
							<div class="bd">
								<table>
									<thead>
										<tr>
											<th>凭证编号</th>
											<th>专户账号</th>
											<th>账户姓名</th>
											<th>机构账户号</th>
											<th>凭证金额</th>
											<th>凭证类型</th>
											<th>凭证内容</th>
											<th>凭证来源</th>
											<th>凭证状态</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>
												<a href="${ctx}/voucher/business/detail/${model.id}">
												${model.voucherNo }</a>
											</td>
											<td>${model.receivableAccountNo }</td>
											<td>${model.paymentName }</td>
											<td>${model.paymentAccountNo }</td>
											<td><fmt:formatNumber type="number" pattern="#,##0.00#" value="${model.amount }"/></td>
											<td>${model.voucherType }</td>
											<td></td>
											<td>${model.voucherSource }</td>
											<td>
												<c:choose>
													<c:when test="${model.voucherStatus == '未核销'}">
														<span class="color-warning">未核销</span>
													</c:when>
													<c:when test="${model.voucherStatus == '作废'}">
														<span class="color-danger">作废</span>
													</c:when>
													<c:otherwise>
														${model.voucherStatus }
													</c:otherwise>
											</c:choose>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="block">
	                        <jsp:include page="/WEB-INF/include/system-operate-log.jsp">
	                        	<jsp:param value="${repurchaseDoc.repurchaseDocUuid}" name="objectUuid"/>
	                        </jsp:include>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="modal fade form-modal large-form-modal" id="confirmDialog">
			<div class="modal-dialog">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close close-dialog" aria-label="关闭" data-dismiss="modal">
	                        <span aria-hidden="true">&times;</span>
	                    </button>
	                    <h4 class="modal-title" id="dialoglabel">业务绑定</h4>
	                </div>
	            	<div class="modal-body">
						<span>回购单：<span style="color:red">${repurchaseDoc.repurchaseDocUuid}</span></span><br/>
						<span>回购截止日：<fmt:formatDate pattern="yyyy-MM-dd" value="${repurchaseDoc.repoEndDate}" />，回购金额：<fmt:formatNumber pattern="#,##0.00#" type="number" value="${repurchaseDoc.amount}" /></span><br/>
						<span>你确认将此回购单置为{%= obj.type == 'default' ? '违约' : '作废' %}吗</span>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default cancel-dialog" data-dismiss="modal">取消</button>
		        		<button type="button" class="btn btn-success submit">确定</button>
					</div>
	            </div>
	        </div>
		</div>
		<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
	</body>
</html>