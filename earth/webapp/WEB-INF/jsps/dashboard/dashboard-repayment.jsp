<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="tab-content-item repayment" data-dashboard-item="true">
    <div class="list">
        <div class="item">
            <div class="lt">
                <a data-vue-path="${ctx}/v#/finance/assets" data-hash="paymentStatusOrdinals=[2,3]&auditOverDueStatusOrdinals=[0]">
                    <p><strong data-total="${repaymentData.notOverDueAssetsNum}">${repaymentData.notOverDueAssetsNum}</strong></p>
                    <div>正常还款</div>
                </a>
            </div>
            <div class="rt">
                <p><a data-vue-path="${ctx}/v#/finance/assets" data-hash="paymentStatusOrdinals=[2]&auditOverDueStatusOrdinals=[0]">处理中：${repaymentData.processing_payment_status_and_not_overdue_assets_nums }</a></p>
                <div><a data-vue-path="${ctx}/v#/finance/assets" data-hash="paymentStatusOrdinals=[3]&auditOverDueStatusOrdinals=[0]">扣款中：${repaymentData.deducting_payment_status_and_not_overdue_assets_nums}</a></div>
            </div>
            <i class="more"></i>
        </div>
       <!--  <div class="item">
            <div class="lt">
                <a href="">
                    <p><strong data-total="20">20</strong></p>
                    <div>提前还款</div>
                </a>
            </div>
            <div class="rt">
                <p>未关联：<a href="">10</a></p>
                <div>部分关联：<a href="">10</a></div>
            </div>
            <i class="more"></i>
        </div> -->
        <div class="item">
            <div class="lt">
                <a data-vue-path="${ctx}/v#/finance/assets"  data-hash="paymentStatusOrdinals=[1,2]&auditOverDueStatusOrdinals=[1,2]">
                    <p><strong data-total="${repaymentData.overDueAssetsNum}">${repaymentData.overDueAssetsNum}</strong></p>
                    <div>逾期还款</div>
                </a>
            </div>
            <div class="rt">
                <p><a data-vue-path="${ctx}/v#/finance/assets" data-hash="auditOverDueStatusOrdinals=[1]">待确认：${repaymentData.unconfirmed_overdue_assets_nums}</a></p>
                <div><a data-vue-path="${ctx}/v#/finance/assets" data-hash="paymentStatusOrdinals=[1,2]&auditOverDueStatusOrdinals=[2]">逾期未还：${repaymentData.overdue_and_processing_unusual_payment_status_assets_nums}</a></div>
            </div>
            <i class="more"></i>
        </div>
        <!-- <div class="item">
            <div class="lt">
                <a href="">
                    <p><strong data-total="20">20</strong></p>
                    <div>待关联</div>
                </a>
            </div>
            <div class="rt">
                <p>未关联：<a href="">10</a></p>
                <div>部分关联：<a href="">10</a></div>
            </div>
            <i class="more"></i>
        </div> -->
        <div class="item">
            <div class="lt">
                <a data-vue-path="${ctx}/v#/finance/guarantee/complement"  data-hash="guaranteeStatus=1&settlementStatus=1">
                    <p><strong data-total="${repaymentData.guranteeNum}">${repaymentData.guranteeNum}</strong></p>
                    <div>担保</div>
                </a>
            </div>
            <div class="rt">
                <p><a data-vue-path="${ctx}/v#/finance/guarantee/complement" data-hash="guaranteeStatus=1">待补足：${repaymentData.waiting_guarantee_orders_nums}</a></p>
                <div><a data-vue-path="${ctx}/settlement-order#/finance/guarantee/settlement" data-hash="settlementStatus=1">待清算：${repaymentData.settlement_status_create_settlement_orders_nums}</a></div>
            </div>
            <i class="more"></i>
        </div>
        <div class="item">
            <div class="lt">
                <a data-vue-path="${ctx}/v#/finance/repurchasedoc"  data-hash="repurchaseStatusOrdinals=[0,2]">
                    <p><strong data-total="${repaymentData.repurchaseNum}">${repaymentData.repurchaseNum}</strong></p>
                    <div>回购</div>
                </a>
            </div>
            <div class="rt">
                <p><a data-vue-path="${ctx}/v#/finance/repurchasedoc" data-hash="repurchaseStatusOrdinals=[0]">回购中：${repaymentData.repurchasing_repurchase_doc_nums}</a></p>
                <div><a data-vue-path="${ctx}/v#/finance/repurchasedoc" data-hash="repurchaseStatusOrdinals=[2]">违约：${repaymentData.default_repurchase_doc_nums}</a></div>
            </div>
            <i class="more"></i>
        </div>
    </div>
</div>
