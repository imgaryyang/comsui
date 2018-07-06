<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <fmt:setBundle basename="ApplicationMessage" />
  <fmt:setLocale value="zh_CN" />
  <%@ include file="/WEB-INF/include/meta.jsp"%>
  <%@ include file="/WEB-INF/include/css-newlayout.jsp"%>
	<title>银企直联流水 - 五维金融金融管理平台</title>

  <style>
    #balanceDialog .form-wrapper .field-row {
      padding: 8px 0;
    }
    #balanceDialog .form-wrapper .field-row .field-title {
      margin: 8px 5px 0 12%;
    }
    .rich-dropdown-menu li > a {
        min-height: 56px;;
    }
  </style>

</head>
<body>
  <%@ include file="/WEB-INF/include/header.jsp"%>
  
  <div class="web-g-main">

    <%@ include file="/WEB-INF/include/aside.jsp"%>

    <div class="content content-cashflow">
        <div class="scroller">
            <form method="GET" action="${ctx}/capital/directbank-cash-flow/search"> 
                <div class="lookup-params">
                    <div class="inner clearfix">
                        <span class="item">
                            <select class="form-control real-value selectpicker rich-dropdown-menu" data-show-content="false" data-size="5" name="accountId" title="信托项目">
                              <c:forEach var="item" items="${financialContracts}">
                                 <option 
                                  data-content='<div class="selectpicker-content"><div class="identification"><img src="${ctx.resource}/images/bank-logo/bank_${fn:toLowerCase(item.capitalAccount.bankCode)}.png"></div><div class="content"><div class="title">${item.contractName }</div><div class="title">${item.capitalAccount.markedAccountNo }</div><div class="subtitle">(${item.capitalAccount.bankName})<span href="#" data-finance-contract-id="${item.id}" class="display-balance link pull-right">显示余额 >></span></div></div></div>' 
                                  value="${item.capitalAccount.id}" <c:if test="${directbankCashFlowQueryModel.accountId == item.capitalAccount.id }">selected="selected"</c:if> >${item.contractName}(${item.contractNo })</option>
                              </c:forEach>
                            </select>
                        </span> 
                	      <span class="item beginend-datepicker">
          	              <jsp:include page="/WEB-INF/include/plugins/datetimepicker.jsp">
          	                <jsp:param value="group" name="type"/>
          	                <jsp:param value="startDateString" name="paramName1"/>
          	                <jsp:param value="endDateString" name="paramName2"/>
          	                <jsp:param value="请输入起始日期" name="placeHolder1"/>
          	                <jsp:param value="请输入终止日期" name="placeHolder2"/>
          	                <jsp:param value="${directbankCashFlowQueryModel.startDateString }" name="paramValue1"/>
          	                <jsp:param value="${directbankCashFlowQueryModel.endDateString }" name="paramValue2"/>
          	              </jsp:include>
          	            </span>
                        <span class="item">
                            <select name="accountSide" class="form-control real-value small">
                             	<option value="">借贷</option>
                               <option value="2" <c:if test="${directbankCashFlowQueryModel.accountSide == 2 }">selected="selected"</c:if>>借</option>
                               <option value="1" <c:if test="${directbankCashFlowQueryModel.accountSide == 1 }">selected="selected"</c:if>>贷</option>
                            </select>
                        </span>     
                      <%-- <span class="item">
                         <select name="accountId" class="form-control apps">
                           <c:forEach var="item" items="${accountList}" varStatus="status">
                             <option value="${item.id }" <c:if test="${directbankCashFlowQueryModel.accountId == item.id }">selected="selected"</c:if> >${item.markedAccountNo }</option>
                           </c:forEach>
                         </select>
                      </span> --%>
                        <span class="item vertical-line"></span>
                        <span class="item">
                            <select class="form-control real-value select-key-word" name="selectKeyWord" autoquery="false">
                                <option value="recipAccNo" <c:if test="${directbankCashFlowQueryModel.selectKeyWord == 'recipAccNo' }">selected="selected"</c:if>>对方账号</option>
                                <option value="recipName" <c:if test="${directbankCashFlowQueryModel.selectKeyWord == 'recipName' }">selected="selected"</c:if>>对方户名</option>
                                <option value="summary" <c:if test="${directbankCashFlowQueryModel.selectKeyWord == 'summary' }">selected="selected"</c:if>>摘要</option>
                            </select>
                            <input type="text" name="keyWords" class="form-control real-value input-key-words" placeholder="请输入关键字..." value="${directbankCashFlowQueryModel.keyWords }">
                        </span>
                  	     <span class="item">
                            <button id="lookup" class="btn btn-primary">查询</button>
                        </span>
                  	</div>
                </div>
            </form>
            <c:if test="${not empty infoMessage}">
              <div class="alert alert-success alert-dismissable alert-fade top-margin-10 text-align-center">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <i class="glyphicon glyphicon-info-sign"></i>&nbsp;&nbsp;
                <fmt:message key="${infoMessage}" />
              </div>
            </c:if>
            <div class="table-area">
              <table class="data-list" style="overflow:auto">
                    <thead>
                      <tr>
                        <th>流水号</th>
                        <th>借贷标志</th>
                        <th>
                           <!--  <a data-paramname="creditAmount" class="sort none">
                                借方发生额<i class="icon"></i>
                            </a> -->
                            借方发生额
                        </th>
                        <th>
                            贷方发生额
                            <!-- <a data-paramname="debitAmount" class="sort none">
                                贷方发生额<i class="icon"></i>
                            </a> -->
                        </th>
                        <th>
                          余额
                           <!--  <a data-paramname="balance" class="sort none">
                                余额<i class="icon"></i>
                            </a> -->
                        </th>
                        <!-- <th>凭证号</th> -->
                        <th>对方账号</th>
                        <th>对方户名</th>
                        <th>对方开户号</th>
                        <th>
                            入账时间
                            <!-- <a data-paramname="time" class="sort none">
                                入账时间<i class="icon"></i>
                            </a> -->
                        </th>
                        <th>摘要</th>
                        <th>附言</th>
                      </tr>
                    </thead>
                    <tbody>
                    	<c:choose>
                    		<c:when test="${flow_result.code == '1' }">
                    			<tr>
                    				<td colspan = "9">${flow_result.message }</td>
                    			</tr>
                    		</c:when>
                    		<c:when test="${not empty flow_result.data.flowList}">
                        			<c:forEach var="flow" items="${flow_result.data.flowList}">
          		                  <tr>
                    							<td>${flow.serialNo }</td>		                    
          		                    <td><c:choose>
          		                        <c:when test="${not empty flow.drcrf and flow.drcrf eq 1}">贷</c:when>
          		                        <c:otherwise>借</c:otherwise>
          		                      </c:choose></td>
          		                    <td><fmt:formatNumber value="${flow.creditAmount}" type="number" pattern="#,##0.00#" /></td>
          		                    <td><fmt:formatNumber value="${flow.debitAmount}" type="number" pattern="#,##0.00#" /></td>
          		                    <td><fmt:formatNumber value="${flow.balance}" type="number" pattern="#,##0.00#" /></td>		                    
          		                    <%-- <td>${flow.vouhNo}</td> --%>
          		                    <td>${flow.recipAccNo}</td>
          		                    <td>${flow.recipName}</td>
          		                    <td>${flow.recipBkName }</td>
          		                    <td><fmt:formatDate value="${flow.time}" pattern="yyyy/MM/dd HH:mm:ss" /></td>
          		                    <td>${flow.summary}</td>
          		                    <td>${flow.postScript}</td>
          		                  </tr>
          		                </c:forEach>
          		                <tr>
          		                	<td></td>
          		                	<td>合计</td>
    		                	<td>借:<fmt:formatNumber value="${flow_result.data.debitSum}" type="number" pattern="#,##0.00#" /></td>
    		                	<td>贷:<fmt:formatNumber value="${flow_result.data.creditSum}" typez="number" pattern="#,##0.00#" /></td>                       
          		                	<td></td>
          		                	<td></td>
          		                	<td></td>
          		                	<td></td>
          		                	<td></td>
          		                </tr>
          		            </c:when>
                    	</c:choose>                	
                    </tbody>
              </table>
            </div>
        </div>
	</div>

  <div id="balanceDialog" class="modal fade form-modal small-form-modal">
      <div class="modal-dialog">
          <div class="modal-content">
              <div class="modal-header">
                  <button type="button" class="close close-dialog" aria-label="关闭" data-dismiss="modal"><span aria-hidden="true">×</span></button>
                  <h4 class="modal-title" id="dialoglabel">查看余额</h4>
              </div>
              <div class="modal-body form-wrapper">
                  <form class="form adapt">
                      <div class="field-row">
                          <label class="field-title">信托专户号：</label>
                          <div class="field-value">
                            <span class="field-span">{%= financeAccountNo %}</span>
                          </div>
                      </div>
                      <div class="field-row">
                          <label class="field-title">信托专户名：</label>
                          <div class="field-value">
                            <span class="field-span">{%= financeAccountName %}</span>
                          </div>
                      </div>
                      <div class="field-row">
                          <label class="field-title">专户开户行：</label>
                          <div class="field-value">
                            <span class="field-span">{%= financeAccountBankName %}</span>
                          </div>
                      </div>
                      <div class="field-row">
                          <label class="field-title">信托合同名称：</label>
                          <div class="field-value">
                            <span class="field-span">{%= financeContractName %}</span>
                          </div>
                      </div>
                      <div class="field-row">
                          <label class="field-title">信托合同代码：</label>
                          <div class="field-value">
                            <span class="field-span">{%= financeContractNo %}</span>
                          </div>
                      </div>
                      <div class="field-row">
                          <label class="field-title">查询时间：</label>
                          <div class="field-value">
                            <span class="field-span">{%= (new Date(queryTime)).format('yyyy-MM-dd HH:mm:ss') %}</span>
                          </div>
                      </div>
                      <div class="field-row">
                          <label class="field-title">账户余额：</label>
                          <div class="field-value">
                            <span class="field-span color-danger">{%= (+accountBalance).formatMoney(2, '') %}</span>
                          </div>
                      </div>
                  </form>
              </div>
              <div class="modal-footer">
                  <button type="button" class="btn btn-success submit" data-dismiss="modal">确定</button>
              </div>
          </div>
      </div>
  </div>
	</div>

	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
	<script src="${ctx.resource}/js/bootstrap-modal-trigger.js"></script>
	<script src="${ctx.resource}/js/bootstrap.validate.js"></script>

</body>
</html>
