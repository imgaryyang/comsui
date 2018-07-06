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
<title>商户付款凭证列表 - 五维金融金融管理平台</title>
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
						<select name="financialContractUuids" class="form-control real-value selectpicker" multiple data-title="信托合同项目"  data-actions-box="true">
								<c:forEach var="item" items="${financialContracts}">
									<option value="${item.financialContractUuid}">${item.contractName}(${item.contractNo })</option>
								</c:forEach>
							</select>
						</span> 
						<span class="item">
							<select name="voucherType" class="form-control real-value">
								<option value="-1">凭证类型</option>
								<c:forEach var="voucherType" items= "${voucherTypeList }">
									<option value="${voucherType.ordinal() }"><fmt:message key="${voucherType.key}" ></fmt:message></option>
								</c:forEach>
							</select>
						</span>
						<span class="item">
							<select name="voucherStatus" class="form-control real-value">
								<option value="-1">凭证状态</option>
								<c:forEach var="voucherStatus" items= "${voucherStatusList }">
									<option value="${voucherStatus.ordinal() }"><fmt:message key="${voucherStatus.key}" ></fmt:message></option>
								</c:forEach>
							</select>
						</span>
						<span class="item beginend-datepicker">
							<jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
								<jsp:param value="group" name="type" />
								<jsp:param value="true" name="pickTime"/>
								<jsp:param value="startDateString" name="paramName1" />
								<jsp:param value="endDateString" name="paramName2" />
								<jsp:param value="发生起始日期" name="placeHolder1" />
								<jsp:param value="发生终止日期" name="placeHolder2" />
								<jsp:param value="true" name="formatToMinimum" />
								<jsp:param value="true" name="formatToMaximum" />
							</jsp:include>
						</span>
						<span class="item vertical-line"></span>
						<span class="item">
	                        <select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
	                            <option value="hostAccount">专户账号</option>
	                            <option value="counterName">账户姓名</option>
	                            <option value="counterNo">机构账户号</option>
	                        </select>
	                        <input type="text" name="keyWords" class="form-control real-value input-key-words" placeholder="请输入关键字..." >
                    	</span>
						<span class="item">
							<button id="lookup" class="btn btn-primary">
								查询
							</button>
						</span>
						<span class="item">
							<button id="create" class="btn btn-primary hide">
								新增
							</button>
						</span>
					</div>
				</div>	
				<div class="table-area">
					<table class="data-list" data-action="${ctx}/voucher/business/query" data-autoload="true">
						<thead>
							<tr>
								<th class="hide">
									<label><input class="check-box check-box-all" type="checkbox" >全选</label>
								</th>
								<th>凭证编号</th>
								<th>专户账号</th>
								<th>账户姓名</th>
								<th>机构账户号</th>
								<th>
									<!-- <a data-paramname="amount" class="sort none">
                            			凭证金额<i class="icon"></i>
                        			</a> -->
                        			凭证金额
								</th>
								<th>凭证类型</th>
								<th>凭证内容</th>
								<th>凭证来源</th>
								<th>发生时间</th>
								<th>流水入账时间</th>
								<th>凭证状态</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
								{% _.each(list, function(item, index){ %}
									<tr data-detail-id="{%= item.id %}">
										<td class="hide">
											<label><input class="check-box" type="checkbox" >全选</label>
										</td>
										<td>
											<a href="${ctx}/voucher/business/detail/{%= item.id %}">
											{%= item.voucherNo %}
											</a>
										</td>
										<td>{%= item.receivableAccountNo %}</td>
										<td>{%= item.paymentName %}</td>
										<td>{%= item.paymentAccountNo %}</td>
										<td>{%= (+item.amount).formatMoney(2, '') %}</td>
										<td>{%= item.voucherType %}</td>
										<td></td>
										<td>{%= item.voucherSource %}</td>
										<td>{%= new Date(item.createTime).format('yyyy-MM-dd HH:mm:ss') %}</td>
										<td>{%= new Date(item.sourceDocumentTradeTime).format('yyyy-MM-dd HH:mm:ss') %}</td>
										<td>{%= item.voucherStatus %}</td>
									</tr>
								{% }) %}
							</script>
						</tbody>
					</table>
				</div>				
			</div>

			<div class="operations">
				<span class="item hide">
	                <button class="btn export" data-action="">导出</button>
	            </span>
	            <span class="item hide">
	                <button class="btn invalidate">作废</button>
	            </span>
				<jsp:include page="/WEB-INF/include/plugins/assembly.jsp">
		            <jsp:param value="page-control" name="type"/>
		            <jsp:param value="true" name="advanced"/>
	            </jsp:include>
			</div>	
		</div>
	</div>
	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
</body>
</html>
