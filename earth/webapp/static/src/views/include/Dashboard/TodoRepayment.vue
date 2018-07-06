<template>
    <div class="tab-content-item repayment">
        <div class="list">
            <div class="item">
                <div class="lt">
                    <a @click="redirect(`${ctx}#/finance/assets`, {
                        paymentStatusOrdinals: [2,3],
                        auditOverDueStatusOrdinals: [0]
                    })">
                        <p><strong data-total="${repaymentData.notOverDueAssetsNum}">{{repaymentData.notOverDueAssetsNum}}</strong></p>
                        <div>正常还款</div>
                    </a>
                </div>
                <div class="rt">
                    <p>
                        <a @click="redirect(`${ctx}#/finance/assets`, {
                            paymentStatusOrdinals: [1],
                            auditOverDueStatusOrdinals: [0]
                        })">
                            处理中：{{repaymentData.processing_payment_status_and_not_overdue_assets_nums}}
                        </a>
                    </p>
                    <div>
                        <a @click="redirect(`${ctx}#/finance/assets`, {
                            paymentStatusOrdinals: [2],
                            auditOverDueStatusOrdinals: [0]
                        })">
                            扣款中：{{repaymentData.deducting_payment_status_and_not_overdue_assets_nums}}
                        </a>
                    </div>
                </div>
                <i class="more"></i>
            </div>
            <div class="item">
                <div class="lt">
                    <a @click="redirect(`${ctx}#/finance/assets`, {
                        paymentStatusOrdinals: [1,2],
                        auditOverDueStatusOrdinals: [1,2]
                    })">
                        <p><strong data-total="${repaymentData.overDueAssetsNum}">{{repaymentData.overDueAssetsNum}}</strong></p>
                        <div>逾期还款</div>
                    </a>
                </div>
                <div class="rt">
                    <p>
                        <a @click="redirect(`${ctx}#/finance/assets`, {
                            auditOverDueStatusOrdinals: [1]
                        })">
                            待确认：{{repaymentData.unconfirmed_overdue_assets_nums}}
                        </a>
                    </p>
                    <div>
                        <a @click="redirect(`${ctx}#/finance/assets`, {
                            paymentStatusOrdinals: [1,2],
                            auditOverDueStatusOrdinals: [2]
                        })">
                            逾期未还：{{repaymentData.overdue_and_processing_unusual_payment_status_assets_nums}}
                        </a>
                    </div>
                </div>
                <i class="more"></i>
            </div>
            <div class="item">
                <div class="lt">
                    <a @click="redirect(`${ctx}#/finance/guarantee/complement`, {
                        guaranteeStatus: 1,
                        settlementStatus: 1
                    })">
                        <p><strong data-total="${repaymentData.guranteeNum}">{{repaymentData.guranteeNum}}</strong></p>
                        <div>担保</div>
                    </a>
                </div>
                <div class="rt">
                    <p>
                        <a @click="redirect(`${ctx}#/finance/guarantee/complement`, {
                            guaranteeStatus: 1
                        })">
                            待补足：{{repaymentData.waiting_guarantee_orders_nums}}
                        </a>
                    </p>
                    <div>
                        <a @click="redirect(`${ctx}#/finance/guarantee/settlement`, {
                            settlementStatus: 1
                        })">
                            待清算：{{repaymentData.settlement_status_create_settlement_orders_nums}}
                        </a>
                    </div>
                </div>
                <i class="more"></i>
            </div>
            <div class="item">
                <div class="lt">
                    <a @click="redirect(`${ctx}#/finance/repurchasedoc`, {
                        repurchaseStatusOrdinals: [0,2]
                    })">
                        <p><strong data-total="${repaymentData.repurchaseNum}">{{repaymentData.repurchaseNum}}</strong></p>
                        <div>回购</div>
                    </a>
                </div>
                <div class="rt">
                    <p>
                        <a @click="redirect(`${ctx}#/finance/repurchasedoc`, {
                            repurchaseStatusOrdinals: [0]
                        })">
                            回购中：{{repaymentData.repurchasing_repurchase_doc_nums}}
                        </a>
                    </p>
                    <div>
                        <a @click="redirect(`${ctx}#/finance/repurchasedoc`, {
                            repurchaseStatusOrdinals: [2]
                        })">
                            违约：{{repaymentData.default_repurchase_doc_nums}}
                        </a>
                    </div>
                </div>
                <i class="more"></i>
            </div>
        </div>
    </div>
</template>

<script>
    import { purify, searchify } from 'assets/javascripts/util';

    export default {
        props: {
            repaymentData: Object,
            selectedFinancialContract: {
                default: () => {[]}
            }
        },
        data: function() {
            return {}
        },
        computed: {
           
        },
        methods: {
            // 旧文件链接
            redirect: function(path, options) {
                var { selectedFinancialContract } = this;
                var attr = {
                    ...options,
                    t: Date.now(),
                    financialContractUuids: selectedFinancialContract,
                    financialContractIds: selectedFinancialContract
                };
                var search =  searchify(purify(attr));
                location.assign(encodeURI(`${path}?${search}`));
            }
        }
    }
</script>