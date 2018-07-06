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
<title>担保补足 - 五维金融金融管理平台</title>

<script type="script/template" id="guaranteeExcel">
    <thead>
        <tr>
            <th>序号</th>
            <th>uniqueid</th>
            <th>补差单号</th>
            <th>还款期号</th>
            <th>应还日期</th>
            <th>补差截止日</th>
            <th>商户编号</th>
            <th>本息金额</th>
            <th>差异天数</th>
            <th>发生时间</th>
            <th>补差金额</th>
            <th>补差状态</th>
        </tr>
    </thead>
    <tbody>
        {% _.each(list, function(item, index) { %}
            <tr>
                <td>{%= item.index %}</td>
                <td>{%= item.uniqueId %}</td>
                <td>{%= item.orderNo %}</td>
                <td>{%= item.singleLoanContractNo %}</td>
                <td>{%= item.assetRecycleDate %}</td>
                <td>{%= item.dueDate %}</td>
                <td>{%= item.appId %}</td>
                <td>{%= item.monthFee %}</td>
                <td>{%= item.overDueDays %}</td>
                <td>{%= item.modifyTime %}</td>
                <td>{%= item.totalRent %}</td>
                <td>{%= item.guaranteeStatus %}</td>
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
			<div class="scroller">
				<div class="lookup-params">
					<div class="inner clearfix">
						<span class="item" style="width: 127px;"> 
							<select name="financialContractIds" class="form-control real-value selectpicker" multiple data-title="信托合同项目"  data-actions-box="true">
								<c:forEach var="item" items="${financialContracts}">
									<option  value="${item.id }">${item.contractName}(${item.contractNo })</option>
								</c:forEach>
							</select>
						</span>
						<span class="item">
							<select name="guaranteeStatus" class="form-control real-value">
								<option value="-1">担保状态</option>
								<c:forEach var="item" items="${GuaranteeStatus}">
									<option value="${item.ordinal() }" <c:if test="${guaranteeOrderModel.guaranteeStatus eq item.ordinal() }">selected</c:if>>
										<fmt:message key="${item.key}" />
									</option>
								</c:forEach>
							</select>
						</span>
						<span class="item beginend-datepicker">
							<jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
								<jsp:param value="group" name="type" />
								<jsp:param value="startDate" name="paramName1" />
								<jsp:param value="endDate" name="paramName2" />
								<jsp:param value="请输入计划还款起始日期" name="placeHolder1" />
								<jsp:param value="请输入计划还款终止日期" name="placeHolder2" />
								<jsp:param value="${guaranteeOrderModel.startDate}" name="paramValue1"/>
			                	<jsp:param value="${guaranteeOrderModel.endDate}" name="paramValue2"/>
							</jsp:include>
						</span>
						<span class="item beginend-datepicker">
							<jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
								<jsp:param value="group" name="type" />
								<jsp:param value="dueStartDate" name="paramName1" />
								<jsp:param value="dueEndDate" name="paramName2" />
								<jsp:param value="请输入担保截止起始日期" name="placeHolder1" />
								<jsp:param value="请输入担保截止终止日期" name="placeHolder2" />
								<jsp:param value="${guaranteeOrderModel.dueStartDate}" name="paramValue1"/>
			                	<jsp:param value="${guaranteeOrderModel.dueEndDate}" name="paramValue2"/>
							</jsp:include>
						</span>
						<span class="item vertical-line"></span>
						<span class="item">
							<select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
								<option value="orderNo">担保补足号</option>
								<option value="singleLoanContractNo">还款编号</option>
								<option value="appId">商户编号</option>
							</select>
							<input type="text" name="keyWords" class="form-control real-value input-key-words" placeholder="请输入关键字..." >
						</span>
						<span class="item">
							<button id="lookup" class="btn btn-primary">
								查询
							</button>
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
					                		data-action="${ctx}/guarantee/order/export-excel" 
					                		data-preview-action="${ctx}/guarantee/order/preview-export-excel" 
					                		data-preview-template="#guaranteeExcel"
					                		data-toggle="export">
					                		担保汇总表
					                	</a>
					                </li>
					            </ul>
		             		</div>
			            </span>
					</div>
				</div>
				<div class="table-area">
					<table class="data-list large-columns" data-action="./order/search" data-autoload="true">
						<thead>
							<tr>
								<th>担保补足号</th>
								<th>还款编号</th>
								<th>
									<a data-paramname="assetRecycleDate" class="sort none">
										计划还款日期<i class="icon"></i>
									</a>
								</th>
								<th>
									<a data-paramname="dueDate" class="sort none">
										担保截止日<i class="icon"></i>
									</a>
								</th>
								<th>商户编号</th>
								<th>
									<a data-paramname="assetInitialValue" class="sort none">
										计划还款金额<i class="icon"></i>
									</a>
								</th>
								<th>
									<a data-paramname="modifyTime" class="sort none">
										发生时间<i class="icon"></i>
									</a>
								</th>
								<th>
									<a data-paramname="totalRent" class="sort none">
										担保金额<i class="icon"></i>
									</a>
								</th>
								<th>担保状态</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
							    {% _.each(list, function(order, index) { %}
							        <tr data="${order.repaymentBillId }">
							        	<td><a href="${ctx}/guarantee/order/{%= order.id %}/guarantee-detail">{%= order.orderNo %}</a></td>
							        	<td>
							        		
											<a href="${ctx}/assets/{%= order.assetSet.id %}/detail#/assets/{%= order.assetSet.id %}/detail">{%= order.assetSet.singleLoanContractNo %}</a>
										</td>
							        	<td>{%= new Date(order.assetSet.assetRecycleDate).format('yyyy-MM-dd') %}</td>
							        	<td>{%= new Date(order.dueDate).format('yyyy-MM-dd') %}</td>
							        	<td>{%= order.assetSet.contract.app.appId %}</td>
							        	<td>{%= (+order.assetSet.assetInitialValue).formatMoney(2, '') %}</td>
							        	<td>{%= order.modifyTime %}</td>
							        	<td>{%= (+order.totalRent).formatMoney(2, '') %}</td>
							        	<td>
							        		{% if(order.assetSet.guaranteeStatusMsg =='待补足'){ %}
												<span class="color-warning">待补足</span>
											{%	}else if(order.assetSet.guaranteeStatusMsg == '担保作废'){ %}
												<span class="color-danger">担保作废</span>
											{%  }else{  %}
												{%= order.assetSet.guaranteeStatusMsg%}
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
