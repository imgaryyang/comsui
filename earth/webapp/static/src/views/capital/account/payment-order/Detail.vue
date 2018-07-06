<style>

</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb
                :routes="[
                    { title: '支付详情'}
                ]">
                <el-button v-if="model.canRefund" size="small" type="primary" @click="show = true">退款</el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">支付信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>支付单号 ：{{ model.journalVoucherNo }}</p>
                                <p>支付金额 ：{{ model.bookingAmount | formatMoney }}</p>
                                <p>创建时间 ：{{ model.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>状态 ：{{ model.journalVoucherStatusName }}</p>
                                <p>发生时间 ：{{ model.lastModifyTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">贷款信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>{{ $utils.locale('financialContract.name') }} ：{{ model.financialContractName }}</p>
                                <p>{{ $utils.locale('financialContract.no') }} ：<a :href="`${ctx}#/financial/contract/${model.financialContractUuid}/detail`">{{ model.financialContractNo}}</a></p>
                                <p>贷款合同编号 ：<a :href="`${ctx}#/data/contracts/detail?id=${model.contractId}`">{{ model.contractNo}}</a></p>
                                <p>还款编号 ：<a :href="`${ctx}#/finance/assets/${ model.assetSetUuid }/detail`">{{ model.relatedBillContractNoLv3 }}</a></p>
                                <p>商户还款计划编号 ：{{ model.outerRepaymentPlanNo }}</p>
                                <p>订单编号 ：
                                    <a v-if="model.orderType == '结算单'" :href="`${ctx}#/finance/payment-order/${model.orderId}/detail`">{{ model.relatedBillContractNoLv4 }}</a>
                                    <a v-else :href="`${ctx}#/finance/guarantee/complement/${model.orderId}/detail`">{{ model.relatedBillContractNoLv4 }}</a>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">客户信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>账户编号 ：<a :href="`${ctx}#/capital/account/virtual-acctount/${model.virtualAccountUuid}/detail`">{{ model.virtualAccountNo }}</a></p>
                                <p>账户名称 ：{{ model.virtualAccountName }}</p>
                                <p>客户类型 ：{{ model.customerTypeName }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">凭证列表</h5>
                    <div class="bd">
                        <el-table
                            :data="voucherList"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column inline-template label="银行流水">
                                <a href="#" @click.prevent="redirectCashflow">{{ row.bankSequenceNo }}</a>
                            </el-table-column>
                            <el-table-column
                                inline-template
                                label="流水总金额">
                                <div>{{ row.transactionAmount | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column
                                inline-template
                                label="凭证编码">
                                <a href="#" @click.prevent="redirectVoucher">{{ row.sourcedoumentNo }}</a>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="block">
                    <h5 class="hd">支付退款单</h5>
                    <div class="bd">
                        <el-table
                            :data="refundList"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column
                                inline-template
                                label="退款单号">
                                <a :href="`${ctx}#/capital/account/refund-order/${row.journalVoucherRefundUuid}/detail`">{{ row.journalVoucherRefundNo }}</a>
                            </el-table-column>
                            <el-table-column
                                inline-template
                                label="账户编号">
                                <a :href="`${ctx}#/capital/account/virtual-acctount/${row.virtualAccountUuid}/detail`">{{ row.virtualAccountNo }}</a>
                            </el-table-column>
                            <el-table-column prop="virtualAccountName" label="账户名称">
                            </el-table-column>
                            <el-table-column
                                inline-template
                                label="退款金额">
                                <div>{{ row.bookingAmount | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="summaryRefund" label="备注">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="$route.params.id"></SystemOperateLog>
                </div>
            </div>

        </div>

        <RefundModal
            v-model="show"
            :journalVoucherUuid="$route.params.id"
            @submit="handleRefundSubmit"
            :model="model">
        </RefundModal>
    </div>
</template>

<script>
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import RefundModal from './RefundModal';

    export default {
        components: {
            SystemOperateLog, RefundModal
        },
        data: function() {
            return {
                fetching: false,
                show: false,
                model: {},
            }
        },
        computed: {
            voucherList: function() {
                var res = [];
                var {
                    virtualAccountUuid,
                    bankSequenceNo,
                    transactionAmount,
                    sourcedoumentNo,
                    bookingAmount
                } = this.model;


                if (sourcedoumentNo) {
                    res.push({
                        journalVoucherUuid: this.$route.params.id,
                        virtualAccountUuid,
                        bankSequenceNo,
                        transactionAmount,
                        sourcedoumentNo,
                        bookingAmount
                    });
                }

                return res;
            },
            refundList: function() {
                var res = [];
                var {
                    virtualAccountUuid,
                    journalVoucherRefundUuid,
                    journalVoucherRefundNo,
                    virtualAccountNo,
                    virtualAccountName,
                    bookingAmount,
                    summaryRefund
                } = this.model;

                if (journalVoucherRefundNo) {
                    res.push({
                        journalVoucherRefundUuid,
                        virtualAccountUuid,
                        journalVoucherRefundNo,
                        virtualAccountNo,
                        virtualAccountName,
                        bookingAmount,
                        summaryRefund
                    });
                }

                return res;
            }
        },
        activated: function() {
            this.fetch(this.$route.params.id);
        },
        methods: {
            redirectVoucher: function() {
                var { sourcedoumentNo, sourceDocumentUuid } = this.model;
                if (!sourcedoumentNo) return;

                ajaxPromise({
                    url: `/capital/customer-account-manage/payment-order-list/query-voucher-detail`,
                    data: {
                        sourceDocumentUuid
                    },
                    parse: data => data.voucher
                }).then(voucher => {
                    var href = '';

                    if (!voucher) return;

                    if (voucher.voucherSource === '商户付款凭证') {
                        href = `${this.ctx}#/capital/voucher/business/${voucher.id}/detail`;
                    } else if (voucher.voucherSource === '主动付款凭证') {
                        href = `${this.ctx}#/capital/voucher/active/${voucher.voucherNo}/detail`;
                    } else if (voucher.voucherSource === '第三方扣款凭证') {
                        href = `${this.ctx}#/capital/voucher/third-party/${voucher.uuid}/detail`;
                    }

                    href && location.assign(href);
                });
            },
            redirectCashflow: function() {
                var { accountNo, bankSequenceNo } = this.model;
                var search = encodeURI(`hostAccountNo=${accountNo}&cashFlowNo=${bankSequenceNo}`);
                var href = `${this.ctx}#/capital/special-account/cash-flow-audit?${search}`;
                // var href = `${this.ctx_deprecated}/capital/account-manager/cash-flow-audit/show#${search}`;
                location.assign(href);
            },
            fetch: function(journalVoucherUuid) {
                this.fetching = true;

                ajaxPromise({
                    url: `/capital/customer-account-manage/payment-order-list/detail-data`,
                    data: {
                        journalVoucherUuid
                    }
                }).then(data => {
                    this.model = Object.assign({}, this.model, data.paymentOrderDetailModel);
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                });
            },
            handleRefundSubmit: function() {
                this.fetch(this.$route.params.id);
                this.$refs.sysLog.fetch();
            }
        }
    }
</script>
