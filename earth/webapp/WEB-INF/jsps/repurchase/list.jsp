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
<title>商户回购列表 - 五维金融金融管理平台</title>

<script type="script/template" id="repurchaseExcel">
    <thead>
        <tr>
            <th>uniqueid</th>
            <th>回购单号</th>
            <th>贷款合同编号</th>
            <th>回购起始日</th>
            <th>回购截止日</th>
            <th>商户名称</th>
            <th>客户名称</th>
            <th>回购金额</th>
            <th>回购天数</th>
            <th>发生时间</th>
            <th>回购状态</th>
        </tr>
    </thead>
    <tbody>
        {% _.each(list, function(item, index) { %}
            <tr>
                <td>{%= item.uniqueId %}</td>
                <td>{%= item.repurchaseDocUuid %}</td>
                <td>{%= item.contractNo %}</td>
                <td>{%= item.repoStartDate %}</td>
                <td>{%= item.repoEndDate %}</td>
                <td>{%= item.appName %}</td>
                <td>{%= item.customerName %}</td>
                <td>{%= item.amount %}</td>
                <td>{%= item.repoDays %}</td>
                <td>{%= item.creatTime %}</td>
                <td>{%= item.repoStatus %}</td>
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
						<span class="item">
							<select  name="financialContractUuids" class="form-control real-value selectpicker" multiple data-title="信托合同项目" data-actions-box="true">
								<c:forEach var="item" items="${financialContracts }">
									<option value="${item.financialContractUuid }">${item.contractName }(${item.contractNo })</option>
								</c:forEach>
							</select>
						</span>
						<span class="item">
							<select name="appId" class="form-control real-value">
								<option value="-1">商户名称</option>
								<c:forEach var="item" items="${appList}">
									<option value="${item.id }">${item.name }</option>
								</c:forEach>
							</select>
						</span>
						<span class="item">
							<jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
								<jsp:param value="group" name="type" />
								<jsp:param value="repoStartDate" name="paramName1" />
								<jsp:param value="repoEndDate" name="paramName2" />
								<jsp:param value="回购起始日" name="placeHolder1" />
								<jsp:param value="回购截止日" name="placeHolder2" />
							</jsp:include>
						</span>
						<span class="item">
							<select name="repurchaseStatusOrdinals" class="form-control real-value selectpicker" multiple data-title="回购状态" data-actions-box="true">
								<c:forEach var="item" items="${repoStatus}">
									<option value="${item.ordinal }" selected><fmt:message key="${item.key }"/></option>
								</c:forEach>
							</select>
						</span>
						<span class="item vertical-line"></span>
						<span class="item">
							<select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
								<option value="contractNo">贷款合同编号</option>
								<option value="customerName">客户姓名</option>
							</select>
							<input type="text" name="keyWords" class="form-control real-value input-key-words" placeholder="请输入关键字..." >
						</span>
						<span class="item">
							<button id="lookup" class="btn btn-primary">查询</button>
						</span>
						<span class="item">
				            <button id="lookup" class="btn btn-primary hide">新增</button>
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
					                		data-action="${ctx}/repurchasedoc/export" 
					                		data-preview-action="${ctx}/repurchasedoc/preview-export" 
					                		data-preview-template="#repurchaseExcel"
					                		data-toggle="export">
					                		回购汇总表
					                	</a>
					                </li>
					            </ul>
		             		</div>
			            </span>
					</div>
				</div>	
				<div class="table-area">
					<table class="data-list" data-action="${ctx}/repurchasedoc/query" data-autoload="true">
						<thead>
							<tr>
								<th class="hide"><input class="check-box check-box-all" type="checkbox" name="" id="">全选</th>
								<th width="12%">回购单号</th>
								<th width="12%">贷款合同编号</th>
								<th>回购起始日</th>
								<th>回购截止日</th>
								<th>商户名称</th>
								<th>客户姓名</th>
								<th>回购金额</th>
								<th>回购天数</th>
								<th>发生时间</th>
								<th>回购状态</th>
								<th width="90">操作</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
								{% _.each(list,function(item,index) { %}
									<tr>
										<td class="hide"><input class="check-box" type="checkbox" name="" id=""></td>
										<td>
											<a href="${ctx}/repurchasedoc/detail?rduid={%= item.repurchaseDocUuid %}">{%= item.repurchaseDocUuid %}</a>
										</td>
										<td><a href="${ctx}/contracts#/data/contracts/detail?id={%= item.contractId %}">{%= item.contractNo %}</a></td>
										<td>{%= new Date(item.repoStartDate).format('yyyy年MM月dd日') %}</td>
										<td>{%= new Date(item.repoEndDate).format('yyyy年MM月dd日') %}</td>
										<td>{%= item.appName %}</td>
										<td>{%= item.customerName %}</td>
										<td>{%= (+item.amount).formatMoney(2,'') %}</td>
										<td>{%= item.repoDays %}</td>
										<td>{%= new Date(item.creatTime).format('yyyy-MM-dd HH:mm:ss') %}</td>
										<td>{%= item.repoStatus %}</td>
										<td data-repurchase-doc-uuid="{%= item.repurchaseDocUuid %}">
											{% if(item.isDefault && item.repoStatusOrdinal == 0  ){ %}
												<a href="javascript:void(0)" class="default">违约</a>
											{% } else { %}
												--
											{% } %}
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
</html>>
