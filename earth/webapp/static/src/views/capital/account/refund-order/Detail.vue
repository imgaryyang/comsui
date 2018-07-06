<style>

</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb
                :routes="[
                    { title: '退款详情'}
                ]">
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block" style="width: 25%">
                        <h5 class="hd">退款信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>退款单号 ：{{model.journalVoucherRefundNo}}</p>
                                <p>退款金额 ：{{model.bookingAmount | formatMoney }}</p>
                                <p>创建时间 ：{{model.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>状态 ：{{model.journalVoucherStatusName }}</p>
                                <p>发生时间 ：{{model.lastModifyTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</p>
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
                    <h5 class="hd">支付单</h5>
                    <div class="bd">
                        <el-table
                            :data="refundOrderList"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column
                                inline-template
                                label="支付单号">
                                <a :href="`${ctx}#/capital/account/payment-order/${row.journalVoucherUuid}/detail`">{{ row.journalVoucherNo }}</a>
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
                        </el-table>
                    </div>
                </div>
                <div class="block">
                    <SystemOperateLog :for-object-uuid="$route.params.id"></SystemOperateLog>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        components: {
            SystemOperateLog
        },
        data: function() {
            return {
                fetching: false,
                model: {}
            }
        },
        computed: {
            refundOrderList: function() {
                var res = [];
                var {
                    virtualAccountUuid,
                    journalVoucherUuid,
                    journalVoucherNo,
                    virtualAccountNo,
                    virtualAccountName,
                    bookingAmount
                } = this.model;

                if (journalVoucherNo) {
                    res.push({
                        virtualAccountUuid,
                        journalVoucherUuid,
                        journalVoucherNo,
                        virtualAccountNo,
                        virtualAccountName,
                        bookingAmount
                    });
                }

                return res;
            }
        },
        activated: function() {
            this.fetch(this.$route.params.id);
        },
        methods: {
            fetch: function(journalVoucherUuid) {
                this.fetching = true;

                ajaxPromise({
                    url: `/capital/customer-account-manage/refund-order-list/detail-data`,
                    data: {
                        journalVoucherUuid
                    }
                }).then(data => {
                    this.model = Object.assign({}, this.model, data.refundOrderDetailModel);
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                });
            },
        }
    }
</script>
