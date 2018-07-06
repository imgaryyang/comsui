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
<title>接口线上支付单详情 - 五维金融金融管理平台</title>

</head>
<body>

	<%@ include file="/WEB-INF/include/header.jsp"%>

	<div class="web-g-main">

		<%@ include file="/WEB-INF/include/aside.jsp"%>

		<div class="content">
			<input type="hidden" name="overdueStatus" value="${assetSetModel.assetSet.overdueStatus.ordinal}">
			<div class="scroller">
				<div class="position-map">
					<div class="pull-left">
						<button type="button" onclick="window.history.go(-1);" class="btn btn-default">&lt;&lt; 返回</button>
			            当前位置:
			            <span class="item current">接口线上支付单详情</span>
					</div>
				</div>
				
				<div class="col-layout-detail">
					<div class="top">
						<div class="block">
							<h5 class="hd">支付信息</h5>
							<div class="bd">
								<div class="col">
									<p>付款方名称 ：${detailModel.payerName}</p>
									<p>付款方开户行 ：${detailModel.bankName}</p>
									<p>银行账户号 ：${detailModel.bankAccountNo}</p>
									<p>发生时间 ：${detailModel.occurTiem}</p>
									<p>发生金额 ：
										<fmt:formatNumber type='number' value='${detailModel.occurAmount}' pattern='#,##0.00#'/>
									</p>
								</div>

								<div class="col">
									<p>支付单号 ：${ detailModel.deductPlanNo}</p>
									<p>支付接口编号 ：${detailModel.paymentInterfaceNo}</p>
									<p>支付状态 ：${detailModel.paymentStatus}</p>
									<p>备注 ：${detailModel.remark}</p>
								</div>

								<div class="col">
									<p>支付机构 ：${ detailModel.paymentInstitution}</p>
									<p>支付机构流水号 ：${detailModel.paymentInstitutionFlowNo}</p>
									<p>支付方式 ：${detailModel.paymentWay}</p>
									<p>绑定号 ：${detailModel.bindId}</p>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="table-layout-detail">
					<jsp:include page="/WEB-INF/include/system-operate-log.jsp">
						<jsp:param value="${detailModel.deductPlanNo}" name="objectUuid" />
					</jsp:include>
				</div>
			

			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
	
</body>
</html>
