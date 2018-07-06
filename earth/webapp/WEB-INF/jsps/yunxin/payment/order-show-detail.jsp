<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>

<fmt:setBundle basename="ApplicationMessage" />
<fmt:setLocale value="zh-CN" />

<%@ include file="/WEB-INF/include/meta.jsp"%>
<%@ include file="/WEB-INF/include/css-newlayout.jsp"%>
<title>结算单详情 - 五维金融金融管理平台</title>

<script type="template/script" id="modifyPenaltyTmpl">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close close-dialog" aria-label="关闭" data-dismiss="modal"><span aria-hidden="true">×</span></button>
        <h4 class="modal-title" id="dialoglabel">修改差异罚息金额</h4>
      </div>
      <div class="modal-body form-wrapper">
        <form class="form adapt">
            <div class="field-row" style="padding: 0;">
                <label for="" class="field-title">差异天数</label>
                <div class="field-value form-static">
                    {%= obj.numbersOfOverdueDays %}
                </div>
            </div>
            <div class="field-row" style="padding: 0;">
                <label for="" class="field-title">贷款客户姓名</label>
                <div class="field-value form-static">
                    {%= obj.customerName %}
                </div>
            </div>
            <div class="field-row" style="padding: 0;">
                <label for="" class="field-title">计划还款日期</label>
                <div class="field-value form-static">
                    {%= obj.assetRecycleDate %}
                </div>
            </div>
            <div class="field-row" style="padding: 0;">
                <label for="" class="field-title">原差异罚息金额</label>
                <div class="field-value form-static">
                    {%= obj.penalty %} <span class="color-dim">元</span>
                </div>
            </div>
          <div class="field-row">
            <label for="" class="field-title require">修改为</label>
            <div class="field-value small">
                <input name="penalty-amount" class="form-control real-value" value="{%= obj.penalty %}" /> <span class="color-dim">元</span>
            </div>
          </div>
          <div class="field-row">
            <label for="" class="field-title require">备注</label>
            <div class="field-value">
                <textarea name="comment" class="multiline-input form-control real-value" placeHolder="{%= obj.comment %}" cols="30" rows="10">{%= obj.comment %}</textarea>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default cancel-dialog" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-success submit">确定</button>
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
            <input type="hidden" name="overdueStatus" value="${assetSetModel.assetSet.overdueStatus.ordinal}">
            <div class="scroller">
                <div class="position-map">
                    <div class="pull-left">
                        <button type="button" onclick="window.history.go(-1);" class="btn btn-default">&lt;&lt; 返回</button>
                        当前位置:
                        <span class="item current">结算单详情</span>
                    </div>
                    <div class="pull-right">
                        <c:if test="${orderViewDetail.order.executingSettlingStatus.ordinal() == 0 ||orderViewDetail.order.executingSettlingStatus.ordinal() == 1}">
                            <button type="button" class="btn btn-default closeOrder">关闭</button>
                        </c:if>
                    </div>
                </div>
                
                <div class="col-layout-detail">
                    <div class="top">
                        <div class="block" style="width: 23%">
                            <h5 class="hd">贷款信息</h5>
                            <div class="bd">
                                <div class="col">
                                    <p>信托项目名称：${ orderViewDetail.order.financialContract.contractName }</p>
                                    <p>合同编号 ：<a href="${ctx}/contracts#/data/contracts/detail?id=${orderViewDetail.order.assetSet.contract.id}">${ orderViewDetail.order.assetSet.contract.contractNo}</a></p>
                                    <p>客户名称 ：<span class="customer-name">${ orderViewDetail.customer.name}</span></p>
                                    <p>客户账户 ：${ orderViewDetail.contractAccount.payAcNo } </p>
                                </div>
                            </div>
                        </div>
                        <div class="block">
                            <h5 class="hd">还款信息</h5>
                            <div class="bd">
                                <div class="col">
                                    <p>还款编号 ：
                                        <a href="${ctx}/assets/${ orderViewDetail.order.assetSet.id }/detail#/assets/${ orderViewDetail.order.assetSet.id }/detail">${ orderViewDetail.order.assetSet.singleLoanContractNo }</a>
                                    </p>
                                    <p>计划还款日期 ：
                                        <span class="asset-recycle-date"><fmt:formatDate value="${ orderViewDetail.order.assetRecycleDate}" pattern="yyyy-MM-dd" /></span>
                                    </p>
                                    <p>计划还款金额（元） ：
                                          <fmt:formatNumber type="number" pattern="#,##0.00#" value="${orderViewDetail.order.assetInitialValue}"/>
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div class="block">
                            <h5 class="hd">结算信息</h5>
                            <div class="bd">
                                <div class="col">
                                    <p>结算编号 ：${ orderViewDetail.order.orderNo }</p>
                                    <p>结算金额（元） ： 
                                        <span   class="showPopover"
                                                data-container="body"
                                                data-placement="right"
                                                data-html="true"
                                                data-trigger="focus" 
                                                data-toggle="popover"
                                                style="color:#23527c">
                                            <fmt:formatNumber type="number" pattern="#,##0.00#" value="${ orderViewDetail.order.totalRent}"></fmt:formatNumber>
                                        </span>
                                    </p>
                                    <p>创建时间 ：${ orderViewDetail.order.createTime }</p>
                                    <p>状态变更时间 ：<fmt:formatDate value="${ orderViewDetail.order.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
                                    <p>结算状态 ：<fmt:message key="${ orderViewDetail.order.executingSettlingStatus.key}" /></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="table-layout-detail">
                     <div class="block">
                        <h5 class="hd">还款订单信息</h5>
                        <div class="bd">
                            <table>
                                <thead>
                                    <tr>
                                        <th>订单编号</th>
                                        <th>订单类型</th>
                                        <th>扣款金额</th>
                                        <th>状态变更时间</th>
                                        <th>状态</th>
                                        <th>备注</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${ not empty deductApplications }">
                                            <c:forEach var="item" items="${deductApplications}" varStatus="status">
                                                <tr>
                                                    <td><a href='${ctx}/deduct/application/detail/${item.deductId }'>${item.deductId }</a></td>
                                                    <td><fmt:message key="${item.repaymentType.key}" />
                                                    <td>
                                                        <fmt:formatNumber type="number" pattern="#,##0.00#" value=" ${item.plannedDeductTotalAmount}"/>
                                                    </td>
                                                    <td>${item.completeTime}</td>
                                                    <td><fmt:message key="${item.executionStatus.key}" /></td>
                                                    <td>${item.executionRemark}</td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="6" align="center">
                                                    没有更多数据
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="block">
                        <h5 class="hd">支付记录</h5>
                        <div class="bd">
                            <table>
                                <thead>
                                    <tr>
                                        <th>支付编号</th>
                                        <th>支付通道</th>
                                        <th>银行名称</th>
                                        <th>账户姓名</th>
                                        <th>银行账户号</th>
                                        <th>交易金额</th>
                                        <th>状态变更时间</th>
                                        <th>状态</th>
                                        <th>备注</th>
                                    </tr>
                                </thead>
                                <tbody>
                                     <c:choose>
                                        <c:when test="${ not empty PaymentRecordModels }">
                                            <c:forEach var="item" items="${PaymentRecordModels}" varStatus="status">
                                                <tr>
                                                    <td>
                                                        <c:if test="${ item.recordType == 1}">
                                                            <a href='${ctx}/payment-manage/payment/${item.id}/detail'>${ item.paymentRecordNo }</a>
                                                        </c:if>
                                                         <c:if test="${ item.recordType == 2}">
                                                            <a href='${ctx}/interfacePayment/list/${item.id}/detail'>${ item.paymentRecordNo }</a>
                                                        </c:if>
                                                         <c:if test="${ item.recordType == 3}">
                                                            <a href='${ctx}/offline-payment-manage/payment/${item.id}/detail'>${ item.paymentRecordNo }</a>
                                                        </c:if>
                                                         <c:if test="${ item.recordType == 4}">
                                                            ${ item.paymentRecordNo }
                                                        </c:if>
                                                    </td>
                                                    <td>${ item.paymentGateway }</td>
                                                    <td>${ item.bankName }</td>
                                                    <td>${ item.accountCustomerName }</td>
                                                    <td>${ item.bankAccountNo }</td>
                                                    <td>
                                                        <fmt:formatNumber type="number" pattern="#,##0.00#" value="${ item.transactionAmount }"/>
                                                    </td>
                                                    <td>${ item.stateChangeTime }</td>
                                                    <td><span class="color-warning">${ item.status }</span></td>
                                                    <td>${ item.remark }</td>
                                                </tr>
                                            </c:forEach>
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
                   
                    <div class="block">
                        <jsp:include page="/WEB-INF/include/system-operate-log.jsp">
                            <jsp:param value="${orderViewDetail.order.repaymentBillId}" name="objectUuid"/>
                        </jsp:include>
                    </div>
                </div>

            </div>
        </div>
    </div>
    <%@ include file="/WEB-INF/include/script-newlayout.jsp"%>

</body>
</html>
