<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '线下支付单详情'}]">
                <el-button
                    v-if="ifElementGranted('associate-offline-payment')"
                    size="small"
                    :disabled="offlineBill.offlineBillConnectionState == 'ALL'"
                    @click="offlinePaymentManageMatchModal.visible = true">关联</el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">线下支付单信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>支付单号 ：{{offlineBill.offlineBillNo}}</p>
                                <p>支付接口编号 ：</p>
                                <p>付款方名称 ：{{offlineBill.payerAccountName}}</p>
                                <p>发生时间 ：{{offlineBill.statusModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</p>
                            </div>
                            <div class="col">
                                <p>付款方开户行 ：{{offlineBill.bankShowName}}</p>
                                <p>发生金额 ：{{ offlineBill.amount | formatMoney }}</p>
                                <p>支付状态 ：{{ offlineBill.offlineBillStatusMsg }}</p>
                                <p>绑定号 ：</p>
                            </div>
                            <div class="col">
                                <p>支付机构流水号 ：{{offlineBill.serialNo }}</p>
                                <p>支付机构 ：</p>
                                <p>支付方式 ：线下打款</p>
                                <p>备注 ：{{offlineBill.comment}}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">线下支付单关联记录</h5>
                    <div class="bd">
                        <PagingTable :data="orderMatchShowModelList">
                            <el-table-column label="来源单号" inline-template>
                                <div>
                                    <a v-if="row.orderType == 'NORMAL'" :href="`${ctx}#/finance/payment-order/${row.orderId}/detail`">{{ row.orderNo }}</a>
                                    <a v-else-if="row.orderType == 'GUARANTEE'" :href="`${ctx}#/finance/guarantee/complement/${row.orderId}/detail`">{{ row.orderNo }}</a>
                                </div>
                            </el-table-column>
                            <el-table-column label="类型" prop="orderTypeMsg"></el-table-column>
                            <el-table-column label="还款编号" inline-template>
                                <a :href="`${ctx}#/finance/assets/${row.assetSetUuid }/detail`">{{ row.assetSetSingleLoanContractNo }}</a>
                            </el-table-column>
                            <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo"></el-table-column>
                            <el-table-column label="计划还款日期" prop="assetSetAssetRecycleDate" inline-template>
                                <div>{{ row.assetSetAssetRecycleDate | formatDate }}</div>
                            </el-table-column>
                            <el-table-column label="结算日期" prop="orderDueDate" inline-template>
                                <div>{{ row.orderDueDate | formatDate }}</div>
                            </el-table-column>
                            <el-table-column label="客户姓名" prop="orderCustomerName"></el-table-column>
                            <el-table-column label="结算金额" inline-template>
                                <div>{{ row.orderTotalRent | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column label="已付金额" inline-template>
                                <div>{{ row.paidAmount | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column label="关联金额" inline-template>
                                <div>{{ row.issuedAmount | formatMoney }}</div>
                            </el-table-column>
                        </PagingTable>
                    </div>
                </div>

                <div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="offlineBill.offlineBillUuid">
                    </SystemOperateLog>
                </div>
            </div>
        </div>

        <OfflinePaymentManageMatchModal
            v-model="offlinePaymentManageMatchModal.visible"
            @submit="fetch"
            :orderTypes="orderTypes"
            :offlineBillUuid="offlineBill.offlineBillUuid">
        </OfflinePaymentManageMatchModal>
    </div>

</template>

<script>
    import { ctx } from 'src/config';
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default{
        components:{
            SystemOperateLog: require('views/include/SystemOperateLog'),
            PagingTable: require('views/include/PagingTable'),
            OfflinePaymentManageMatchModal: require('./OfflinePaymentManageMatchModal')
        },
        activated: function() {
            this.fetch();
        },
        data: function() {
            return {
                fetching: false,
                orderMatchShowModelList: [],
                offlineBill: {},
                offlinePaymentManageMatchModal: {
                    visible: false
                },
                orderTypes: []
            }
        },
        methods: {
            fetch: function() {
                this.fetching = true;

                ajaxPromise({
                    url: `/offline-payment-manage/payment/${this.$route.params.id}/detail`
                }).then(data => {
                    this.orderMatchShowModelList = data.orderMatchShowModelList || [];
                    this.orderTypes = data.orderTypes ||[];
                    this.offlineBill = Object.assign({}, data.offlineBill);
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            }
        }
    }
</script>
