<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.zufangbao.gluon.spec.global.GlobalCodeSpec"%>
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
<title>担保清算 - 五维金融金融管理平台</title>

<script type="script/template" id="settlementExcel">
    <thead>
        <tr>
            <th>uniqueid</th>
            <th>结清单号</th>
            <th>回款单号</th>
            <th>应还日期</th>
            <th>清算截止日</th>
            <th>商户编号</th>
            <th>本息金额</th>
            <th>差异天数</th>
            <th>差异罚息</th>
            <th>发生时间</th>
            <th>结清金额</th>
            <th>状态</th>
            <th>备注</th>
        </tr>
    </thead>
    <tbody>
        {% _.each(list, function(item, index) { %}
            <tr>
                <td>{%= item.uniqueId %}</td>
                <td>{%= item.settleOrderNo %}</td>
                <td>{%= item.repaymentNo %}</td>
                <td>{%= item.recycleDate %}</td>
                <td>{%= item.dueDate %}</td>
                <td>{%= item.appId %}</td>
                <td>{%= item.principalAndInterestAmount %}</td>
                <td>{%= item.overdueDays %}</td>
                <td>{%= item.overduePenalty %}</td>
                <td>{%= item.modifyTime %}</td>
                <td>{%= item.settlementAmount %}</td>
                <td>{%= item.settlementStatus %}</td>
                <td>{%= item.comment %}</td>
            </tr>
        {% }) %}
    </tbody>
</script>

</head>
<body>

	<%@ include file="/WEB-INF/include/header.jsp"%>

	<div class="web-g-main">
		<%@ include file="/WEB-INF/include/aside.jsp"%>
		<div class="content">
			<div class="lookup-params">
				<div class="inner">
					<input type="hidden" name="filter" value="true">
					<span class="item" style="width: 127px;"> 
						<select name="financialContractIds" class="form-control real-value selectpicker" multiple data-title="信托合同项目" data-actions-box="true">
							<c:forEach var="item" items="${financialContracts}">
								<option value="${item.id }">${item.contractName}(${item.contractNo })</option>
							</c:forEach>
						</select>
					</span>
					<span class="item">
						<select name="settlementStatus" id="settlementStatus" class="form-control real-value">
							<option value="-1">清算状态</option>
							<c:forEach var="item" items="${settlementStatusList}">
								<option value="${item.ordinal}">
									<fmt:message key="${item.key}" />
								</option>
							</c:forEach>
						</select>
					</span>
					<span class="item vertical-line"></span>
					<span class="item">
						<select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
							<option value="settlementOrderNo">清算单号</option>
							<option value="repaymentNo">还款编号</option>
							<option value="appNo">商户编号</option>
						</select>
						<input type="text" name="keyWords" class="form-control real-value input-key-words" placeholder="请输入关键字..." >
					</span>
					<span class="item">
		              <button type="button" id="lookup" class="btn btn-primary">查询</button>
		            </span>
		            <span class="item">
	             		<div class="dropdown dropdown-export-excel">
			            	<a 
			            		class="dropdown-toggle btn" 
			            		href="javascript: void 0;" 
			            		data-toggle="dropdown">
				              	<span class="btn-text">导出Excel</span>
			            	</a>
			                <ul class="dropdown-menu" role="menu">
				                <li>
				                	<a 
				                		href="#" 
				                		class="export-excel" 
				                		data-action="${ctx}/settlement-order/settle/export_settlement_excel" 
				                		data-preview-action="${ctx}/settlement-order/settle/preview_export_settlement_excel" 
				                		data-preview-template="#settlementExcel"
				                		data-toggle="export">
				                		清算汇总表
				                	</a>
				                </li>
				            </ul>
	             		</div>
		            </span>
		            <sec:authorize ifAnyGranted="ROLE_SUPER_USER">
			            <span class="item">
		             		<div class="dropdown dropdown-export-excel">
				            	<a 
				            		class="dropdown-toggle btn" 
				            		href="javascript: void 0;" 
				            		data-toggle="dropdown">
					              	<span class="btn-text">批量</span>
				            	</a>
				                <ul class="dropdown-menu" role="menu">
					                <li>
					                	<a href="#" id="batchSubmit">提交</a>
					                </li>
					                <li>
					                	<a href="#" id="batchSettlement">清算</a>
					                </li>
					            </ul>
		             		</div>
			            </span>
					</sec:authorize>
				</div>
			</div>
			<div class="table-area">
				<table class="data-list large-columns" data-action="./settlement-order/query" data-autoload="true">
					<thead>
						<tr>
							<th>序号</th>
							<th>清算单号</th>
							<th>还款编号</th>
							<th>
								<a data-paramname="assetRecycleDate" class="sort none">
									计划还款日期<i class="icon"></i>
								</a>
							</th>
							<th>
								<a data-paramname="dueDate" class="sort none">
									清算截止日<i class="icon"></i>
								</a>
							</th>
							<th>商户编号</th>
							<th>
								<a data-paramname="assetInitialValue" class="sort none">
									计划还款金额<i class="icon"></i>
								</a>
							</th>
							<th>
								<a data-paramname="overdueDays" class="sort none">
									逾期天数<i class="icon"></i>
								</a>
							</th>
							<th>
								<a data-paramname="overduePenalty" class="sort none">
									逾期费用合计<i class="icon"></i>
								</a>
							</th>
							<th>
								<a data-paramname="lastModifyTime" class="sort none">
									发生时间<i class="icon"></i>
								</a>
							</th>
							<th>
								<a data-paramname="settlementAmount" class="sort none">
									清算金额<i class="icon"></i>
								</a>
							</th>
							<th>清算状态</th>
							<th>备注</th>
						</tr>
					</thead>
					<tbody>
						<script type="script/template" id="tableFieldTmpl">
							{% _.each(list, function(settlementOrder, index){ %}
								<tr data="{%= settlementOrder.settlementOrderUuid %}">
								    <td>
								    	<input type="checkbox" {%= settlementOrder.assetSet.settlementStatusMsg == '已清算' ? 'DISABLED':'' %}>
								    </td>
								    <td> <a href="${ctx}/settlement-order/settle/{%= settlementOrder.id %}/detail" class="hover-no-text-decoration" data-target="#detailDialog" data-toggle="modal" title="详情">
								    	{%= settlementOrder.settleOrderNo %}
								    	</a>
								    </td>
								    <td>{%= settlementOrder.assetSet.singleLoanContractNo %}</td>
								    <td>{%= settlementOrder.assetSet.assetRecycleDate %}</td>
								    <td>{%= new Date(settlementOrder.dueDate).format('yyyy-MM-dd') %}</td>
								    <td>{%= settlementOrder.assetSet.contract.app.appId %}</td>
								    <td>{%= (+settlementOrder.assetSet.assetInitialValue).formatMoney(2,'') %}</td>
								    <td>{%= settlementOrder.overdueDays %}</td>
								    <td>{%= (+settlementOrder.overduePenalty).formatMoney(2,'') %}</td>
								    <td>{%= settlementOrder.lastModifyTime %}</td>
								    <td>{%= (+settlementOrder.settlementAmount).formatMoney(2,'') %}</td>
								    <td>
								    	{%	if(settlementOrder.assetSet.settlementStatusMsg == '待结清'){ %}
												<span class="color-warning">待结清</span>
										{%  }else{  %}
						            			{%= settlementOrder.assetSet.settlementStatusMsg%}
										{%  }  %}
								    </td>
								    <td>{%= settlementOrder.comment %}</td>
								</tr>
							{% }); %}
						</script>
					</tbody>
				</table>
			</div>

			<div class="operations">
				<jsp:include page="/WEB-INF/include/plugins/assembly.jsp">
		            <jsp:param value="page-control" name="type"/>
		            <jsp:param value="false" name="advanced"/>
	            </jsp:include>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
    <script src="${ctx.resource}/js/bootstrap-modal-trigger.js"></script>

	<script type="text/javascript">
		// $("#submit").click(function() {
		// 	var data = $("#params").serialize();
		// 	window.location.href = "${ctx}/settlement-order/settle/export_settlement_excel?" + data;
		// });

		$("#batchSubmit").click(function() {
			var $checkedBox = $("tbody input[type='checkbox']:checked");
			if ($checkedBox.length <= 0) {
				return false;
			}
			var array = [];
			$checkedBox.each(function() {
				var voucherUuid = $(this).parents("tr").attr("data");
				array.push(voucherUuid);
			});
			$.post('${ctx}/settlement-order/batch-submit', {
				settlementOrderUuids : JSON.stringify(array)
			}, function(data) {
				window.location.reload();
			})
		});
		$("#batchSettlement").click(function() {
			var $checkedBox = $("tbody input[type='checkbox']:checked");
			if ($checkedBox.length <= 0) {
				return false;
			}
			var array = [];
			$checkedBox.each(function() {
				var voucherUuid = $(this).parents("tr").attr("data");
				array.push(voucherUuid);
			});
			$.post('${ctx}/settlement-order/batch-settlement', {
				settlementOrderUuids : JSON.stringify(array)
			}, function(data) {
				window.location.reload();
			})
		});
	</script>
</body>
</html>
