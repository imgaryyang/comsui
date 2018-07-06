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
<title>还款列表 - 五维金融金融管理平台</title>

<script type="script/template" id="overDueRepaymentPlan">
    <thead>
        <tr>
            <th>uniqueid</th>
            <th>信托合作商户名称</th>
            <th>贷款合同编号</th>
            <th>还款编号</th>
            <th>放款日期</th>
            <th>贷款客户姓名</th>
            <th>计划还款日期</th>
            <th>实际还款日期</th>
            <th>当前期数</th>
            <th>计划还款本金</th>
            <th>计划还款利息</th>
            <th>计划还款金额</th>
            <th>差异天数</th>
            <th>逾期天数</th>
            <th>逾期费用合计</th>
            <th>应还款金额</th>
            <th>实收金额</th>
            <th>实收本金</th>
            <th>实收利息</th>
            <th>实收贷款服务费</th>
            <th>实收技术维护费</th>
            <th>实收其他费用</th>
            <th>实收罚息</th>
            <th>实收逾期违约金</th>
            <th>实收逾期服务费</th>
            <th>实收逾期其他费用</th>
            <th>退款金额</th>
            <th>还款状态</th>
            <th>逾期状态</th>
            <th>担保状态</th>
            <th>备注</th>
        </tr>
    </thead>
    <tbody>
        {% _.each(list, function(item, index) { %}
            <tr>
                <td>{%= item.uniqueId %}</td>
                <td>{%= item.appName %}</td>
                <td>{%= item.loanContractNo %}</td>
                <td>{%= item.repaymentNo %}</td>
                <td>{%= item.loanDate %}</td>
                <td>{%= item.customerName %}</td>
                <td>{%= item.assetRecycleDate %}</td>
                <td>{%= item.actualRecycleDate %}</td> 
                <td>{%= item.currentPeriod %}</td>
                <td>{%= item.assetPrincipalValue %}</td>
                <td>{%= item.assetInterestValue %}</td>
                <td>{%= item.assetInitialValue %}</td>
                <td>{%= item.daysDifference %}</td>
                <td>{%= item.auditOverdueDays %}</td>
                <td>{%= item.totalOverdueFee %}</td>
                <td>{%= item.assetFairValue %}</td>
                <td>{%= item.paidUpAssetFairValue %}</td>
                <td>{%= item.paidUpPrincipalValue %}</td>
                <td>{%= item.paidUpInterestValue %}</td>
                <td>{%= item.paidUpLoanServiceFee %}</td>
                <td>{%= item.paidUpTechMaintenanceFee %}</td>
                <td>{%= item.paidUpOtherFee %}</td>
                <td>{%= item.paidUpOverduePenalty %}</td>
                <td>{%= item.paidUpOverdueDefaultFee %}</td>
                <td>{%= item.paidUpOverdueServiceFee %}</td>
                <td>{%= item.paidUpOverdueOtherFee %}</td>
                <td>{%= item.refundAmount %}</td>
                <td>{%= item.repaymentStatus %}</td>
                <td>{%= item.overDueStatus %}</td>
                <td>{%= item.guaranteeStatus %}</td>
                <td>{%= item.comment %}</td> 
            </tr>
        {% }) %}
    </tbody>
</script>

<script type="script/template" id="repaymentPlanManagement">
    <thead>
        <tr>
            <th>uniqueid</th>
            <th>还款编号</th>
            <th>贷款合同编号</th>
            <th>信托合约编号</th>
            <th>信托项目名称</th>
            <th>资产编号</th>
            <th>客户姓名</th>
            <th>计划还款日期</th>
            <th>当前期数</th>
            <th>总期数</th>
            <th>计划还款本金</th>
            <th>计划还款利息</th>
            <th>贷款服务费</th>
            <th>技术维护费</th>
            <th>其他费用</th>
            <th>计划还款金额</th>
            <th>差异天数</th>
            <th>逾期天数</th>
            <th>逾期罚息</th>
            <th>逾期违约金</th>
            <th>逾期服务费</th>
            <th>逾期其他费用</th>
            <th>逾期费用合计</th>
            <th>应还款金额</th>
            <th>实际还款金额</th>
            <th>实际还款日期</th>
            <th>退款金额</th>
            <th>还款状态</th>
            <th>担保状态</th>
            <th>备注</th>
        </tr>
    </thead>
    <tbody>
        {% _.each(list, function(item, index) { %}
            <tr>
               	<td>{%= item.uniqueId %}</td>
                <td>{%= item.singleLoanContractNo %}</td>
                <td>{%= item.contractNo %}</td>
                <td>{%= item.financialContractNo %}</td>
                <td>{%= item.financialProjectName %}</td>
                <td>{%= item.assetNo %}</td>
                <td>{%= item.customerName %}</td>
                <td>{%= item.assetRecycleDate %}</td> 
                <td>{%= item.currentPeriod %}</td>
                <td>{%= item.allPeriods %}</td>
                <td>{%= item.assetPrincipalValue %}</td>
                <td>{%= item.assetInterestValue %}</td>
                <td>{%= item.loanServiceFee %}</td>
                <td>{%= item.techMaintenanceFee %}</td>
                <td>{%= item.otherFee %}</td>
                <td>{%= item.assetInitialValue %}</td>
                <td>{%= item.overDueDays %}</td>
                <td>{%= item.auditOverdueDays %}</td>
                <td>{%= item.overduePenalty %}</td>
                <td>{%= item.overdueDefaultFee %}</td>
                <td>{%= item.overdueServiceFee %}</td>
                <td>{%= item.overdueOtherFee %}</td>
                <td>{%= item.totalOverdueFee %}</td>
                <td>{%= item.amount %}</td>
                <td>{%= item.actualAmount %}</td>
                <td>{%= item.actualRecycleDate %}</td>
                <td>{%= item.refundAmount %}</td>
                <td>{%= item.paymentStatus %}</td> 
                <td>{%= item.guaranteeStatus %}</td>
                <td>{%= item.comment %}</td>
            </tr>
        {% }) %}
    </tbody>
</script>

<script type="script/template" id="repaymentPlanDetail">
	<thead>
        <tr>
            <th>uniqueid</th>
            <th>信托合约编号</th>
            <th>信托合作商户名称</th>
            <th>贷款合同编号</th>
            <th>还款编号</th>
            <th>放款日期</th>
            <th>计划还款日期</th>
            <th>实际还款日期</th>
            <th>计划还款利息</th>
            <th>计划还款本金</th>
            <th>信托账户号</th>
            <th>贷款客户姓名</th>
            <th>贷款客户身份证号码</th>
            <th>还款账户开户行名称</th>
            <th>开户行所在省</th>
            <th>开户行所在市</th>
            <th>还款账户号</th>
            <th>生效日期</th>
        </tr>
    </thead>
    <tbody>
        {% _.each(list, function(item, index) { %}
            <tr>
               	<td>{%= item.uniqueId %}</td>
                <td>{%= item.financialContractNo %}</td>
                <td>{%= item.appName %}</td>
                <td>{%= item.loanContractNo %}</td>
                <td>{%= item.repaymentNo %}</td>
                <td>{%= item.loanDate %}</td>
                <td>{%= item.assetRecycleDate %}</td>
                <td>{%= item.actualRecycleDate %}</td> 
                <td>{%= item.assetInterestValue %}</td>
                <td>{%= item.assetPrincipalValue %}</td>
                <td>{%= item.financialAccountNo %}</td>
                <td>{%= item.customerName %}</td>
                <td>{%= item.idCardNo %}</td>
                <td>{%= item.bankName %}</td>
                <td>{%= item.province %}</td>
                <td>{%= item.city %}</td>
                <td>{%= item.payAcNo %}</td>
                <td>{%= item.effectiveDate %}</td>
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
							<select name="financialContractIds" class="form-control real-value selectpicker" multiple data-title="信托合同项目" data-actions-box="true">
								<c:forEach var="item" items="${financialContracts}">
									<option value="${item.id }">${item.contractName}(${item.contractNo })</option>
								</c:forEach>
							</select>
						</span>
						<span class="item beginend-datepicker">
			              <jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
			                <jsp:param value="group" name="type"/>
			                <jsp:param value="false" name="calendarbtn"/>
			                <jsp:param value="true" name="clearbtn"/>
			                <jsp:param value="startDate" name="paramName1"/>
			                <jsp:param value="endDate" name="paramName2"/>
			                <jsp:param value="请输入计划还款起始日期" name="placeHolder1"/>
			                <jsp:param value="请输入计划还款终止日期" name="placeHolder2"/>
			              </jsp:include>
			            </span>
						<span class="item beginend-datepicker">
				            <jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
				                <jsp:param value="group" name="type"/>
				                <jsp:param value="false" name="calendarbtn"/>
				                <jsp:param value="true" name="clearbtn"/>
				                <jsp:param value="actualRecycleStartDate" name="paramName1"/>
				                <jsp:param value="actualRecycleEndDate" name="paramName2"/>
				                <jsp:param value="实际还款起始日" name="placeHolder1"/>
				                <jsp:param value="实际还款终止日" name="placeHolder2"/>
				            </jsp:include>
			            </span>
			            <span class="item">
							<select class="form-control real-value selectpicker" name="paymentStatusOrdinals" multiple data-title="还款状态"  data-actions-box="true">
								<c:forEach var="payment_status" items="${paymentStatusList}">
									<option selected value="${payment_status.ordinal()}"><fmt:message key="${payment_status.key}"></fmt:message></option>
								</c:forEach>
						</select>
						</span>
						<span class="item">
							<select
								class="form-control real-value" name="overDueStatus">
								<option value="">差异状态</option>
								<c:forEach var="overDueStatus" items="${overDueStatusList }">
									<option value="${overDueStatus.ordinal()}"><fmt:message key="${overDueStatus.key}" ></fmt:message></option>
								</c:forEach>
							</select>
						</span>
						<span class="item">
							<select class="form-control real-value selectpicker" name="auditOverDueStatusOrdinals" multiple data-title="逾期状态"  data-actions-box="true">
								<c:forEach var="auditOverdueStatus" items="${auditOverdueStatusList }">
									<option selected value="${auditOverdueStatus.ordinal()}"><fmt:message key="${auditOverdueStatus.key}" ></fmt:message></option>
								</c:forEach>
							</select>
						</span>
						<span class="item vertical-line"></span>
						<span class="item">
							<select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
								<option value="singleLoanContractNo">还款编号</option>
								<option value="contractNo">合同编号</option>
								<option value="customerName">客户姓名</option>
							</select>
							<input type="text" name="keyWords" class="form-control real-value input-key-words" placeholder="请输入关键字..." >
						</span>
			            <span class="item">
			              	<button id="lookup" class="btn btn-primary">查询</button>
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
					                		data-action="${ctx}/assets/exprot-overDue-repayment-plan-detail" 
					                		data-preview-action="${ctx}/assets/preview-exprot-overDue-repayment-plan-detail" 
					                		data-preview-template="#overDueRepaymentPlan"
					                		data-toggle="export">
					                		逾期还款明细表
					                	</a>
					                	<a 
					                		href="#" 
					                		class="export-excel" 
					                		data-action="${ctx}/assets/exprot-repayment-management" 
					                		data-preview-action="${ctx}/assets/preview-exprot-repayment-management" 
					                		data-preview-template="#repaymentPlanManagement"
					                		data-toggle="export">
					                		还款管理表
					                	</a>
					                	<a 
					                		href="#" 
					                		class="export-excel hide" 
					                		data-action="${ctx}/assets/exprot-repayment-plan-detail" 
					                		data-preview-action="${ctx}/assets/preview-exprot-repayment-plan-detail" 
					                		data-preview-template="#repaymentPlanDetail"
					                		data-toggle="export">
					                		还款计划明细汇总表
					                	</a>
					                </li>
					            </ul>
		             		</div>
			            </span>
					</div>
				</div>
	
				<div class="table-area">
					<table class="data-list large-columns" data-action="${ctx}/assets/query" data-autoload="true">
						<thead>
							<tr>
								<th>还款编号</th>
								<th>贷款合同编号</th>
								<th>信托产品代码</th>
								<th>信托项目名称</th>
								<th>客户姓名</th>
								<th>
									<a data-paramname="assetRecycleDate" class="sort none">
										计划还款日期<i class="icon"></i>
									</a>
								</th>
								<th>当前期数</th>
								<th>总期数</th>
								<th>
									<a data-paramname="amount" class="sort none">
										应还款金额<i class="icon"></i>
									</a>
								</th>
								<th>
									<!-- <a data-paramname="actualAmount" class="sort none">
										实际还款金额<i class="icon"></i>
									</a> -->
									实际还款金额
								</th>
								<th>
									<a data-paramname="actualRecycleDate" class="sort none">
										实际还款日期<i class="icon"></i>
									</a>
								</th>
								<th>
									<a data-paramname="refundAmount" class="sort none">
										退款金额<i class="icon"></i>
									</a>
								</th>
								<th>
									<!-- <a data-paramname="auditOverdueDays" class="sort none">
										逾期天数<i class="icon"></i>
									</a> -->
									逾期天数
								</th>
								<th>备注</th>
								<th>还款状态</th>
								<th>担保状态</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
							    {% _.each(list, function(assetSetModel, index){ %}
							        <tr>
							            <td><a href="${ctx}/assets/{%= assetSetModel.assetSetId %}/detail#/assets/{%= assetSetModel.assetSetId %}/detail">{%= assetSetModel.singleLoanContractNo %}</a></td>
							            <td>
							                <p style="width:80px; cursor: pointer;" class="text-overflow" data-title="{%= assetSetModel.contractNo %}">{%= assetSetModel.contractNo %}</p>
							            </td>
							            <td>{%= assetSetModel.financialContractNo %}</td>
										<td>{%= assetSetModel.financialProjectName %}</td>
										<td>{%= assetSetModel.customerName %}</td>
							            <td>{%= new Date(assetSetModel.assetRecycleDate).format('yyyy-MM-dd') %}</td>
										<td>{%= assetSetModel.currentPeriod %}</td>                                         
										<td>{%= assetSetModel.allPeriods %}</td>
							            <td data-asset-set-id = "{%= assetSetModel.assetSetId %}">
							            	<span 	class="showPopover"
													data-container="body"
													data-placement="top"
													data-html="true"
													data-trigger="focus" 
													data-toggle="popover">
							            		{%= (+assetSetModel.amount ).formatMoney(2,'')%}
							            	</span>
							            </td>
										<td>
											{%= (+assetSetModel.actualAmount).formatMoney(2,'') %}
										</td>
                                        <td>{%= new Date(assetSetModel.actualRecycleDate).format('yyyy-MM-dd') %}</td>
							            <td>{%= (+assetSetModel.refundAmount).formatMoney(2, '') %}</td>
							            <td><span class="{%= +assetSetModel.auditOverdueDays > 0 ? 'color-danger':'' %}">{%= assetSetModel.auditOverdueDays %}</span></td>
							            <td>{%= assetSetModel.comment %}</td>
							            <td>
							            	{% if(assetSetModel.paymentStatus =='还款异常'){ %}
												<span class="color-danger">还款异常</span>
											{%	} else { %}
							            		{%= assetSetModel.paymentStatus %}
											{%  } %}
							            </td>
							            <td>
							            	{% if(assetSetModel.guaranteeStatus =='待补足'){ %}
												<span class="color-warning">待补足</span>
											{%	}else if(assetSetModel.guaranteeStatus == '担保作废'){ %}
												<span class="color-danger">担保作废</span>
											{%  }else{  %}
												{%= assetSetModel.guaranteeStatus%}
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
