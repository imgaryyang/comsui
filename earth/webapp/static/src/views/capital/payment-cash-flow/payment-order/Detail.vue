<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '支付订单详情'}]">
                <el-button
                    type="primary"
                    size="small"
                    v-if="isUpdate"
                    @click="handlerCashFlow($route.params.uuid)">
                    流水匹配
                </el-button>
                <el-button
                    type="primary"
                    size="small"
                    v-if="false"
                    @click="reCallback">
                    重新回调
                </el-button>
                <el-button
                    type="primary"
                    size="small"
                    :disabled="!canCancel || detailModel.paymentOrderStatus == 'CANCEL'"
                    @click="obsolete($route.params.uuid)">
                    作废
                </el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">订单信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>商户支付编号：{{ detailModel.paymentNo }}</p>
                                <p>五维支付编号: {{ detailModel.uuid }}</p>
                                <p>商户订单号：{{ detailModel.orderUniqueId }} </p>
                                <p>五维订单号：<a :href="`${ctx}#/finance/repayment-order/${detailModel.orderUuid}/detail`">{{ detailModel.orderUuid }}</a></p>
                                <p>产品代码：{{ detailModel.financialContractNo }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">通道信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>支付方式：{{ detailModel.payWayChinese }}</p>
                                <p>支付通道：{{ detailModel.paymentGateWay }}</p>
                                <p>通道交易号：{{ detailModel.tradeUuid }}</p>
                                <p>交易金额：{{ detailModel.amount | formatMoney }}</p>
                                <p>交易时间：{{ detailModel.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">付款方信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>付款方账号：{{ detailModel.counterAccountNo }}</p>
                                <p>付款方户名：{{ detailModel.counterAccountName }}</p>
                                <p>付款方开户行：{{ detailModel.counterAccountBankName }}</p>
                                <p>开户行所在地： </p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">收款方信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>收款方账号：{{ detailModel.hostAccountNo }}</p>
                                <p>收款方户名：{{ detailModel.hostAccountName }}</p>
                                <p>收款方开户行：{{ detailModel.hostAccountBankName }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">执行信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>支付状态：{{ detailModel.paymentOrderStatusChinese }} <a href="#" @click.prevent="modifyPaymentOrderStatus($route.params.uuid)" v-if="isUpdate">[修改]</a></p>
                                <p>创建时间：{{ detailModel.createTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</p>
                                <p>状态变更时间：{{ detailModel.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</p>
                                <p>计划回调次数：-- </p>
                                <p>实际回调次数：-- </p>
                                <p>备注：{{ detailModel.remark }} </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block" v-if="detailModel.payWay === 'OFFLINE_TRANSFER'">
                    <h5 class="hd">
                        银行流水
                        <el-button
                            size="small"
                            class="default pull-right"
                            @click="cashFlowSelect"
                            v-if="isCanSelect">
                            流水选择
                        </el-button>
                    </h5>
                    <div class="bd">
                        <el-table
                            stripe
                            border
                            key="cashFlow"
                            :data="cashFlowList"
                            class="td-15-padding th-8-15-padding no-th-border">
                            <el-table-column label="流水号" prop="bankSequenceNo" inline-template>
                                <a :href="`${ctx}#/capital/special-account/cash-flow-audit/${row.cashFlowUuid}/detail`">{{ row.bankSequenceNo }}</a>
                            </el-table-column>
                            <el-table-column label="流水金额" prop="cashFlowAmount" inline-template>
                                <div>{{ row.cashFlowAmount | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column label="入账时间" prop="transactionTime" inline-template>
                                <div>{{ row.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                            </el-table-column>
                            <el-table-column label="状态" prop="auditStatus"></el-table-column>
                            <el-table-column label="摘要" prop="remark"></el-table-column>
                        </el-table>
                    </div>
                </div>

            	<div class="block" v-else>
					<h5 class="hd">支付记录</h5>
					<div class="bd">
						<el-table
			                :data="dataSource.list"
			                class="td-15-padding th-8-15-padding no-th-border"
			                v-loading="dataSource.fetching"
                            key="deductPlan"
			                stripe
			                border>
			                <el-table-column label="支付编号" prop="deductPlanUuid" inline-template>
			                	<a :href="`${ctx}#/capital/payment-cash-flow/online-payment/${row.id}/detail`">{{ row.deductPlanUuid }}</a>
			                </el-table-column>
	                        <el-table-column label="交易金额" prop="amount" inline-template>
	                        	<div>{{ row.amount | formatMoney }}</div>
	                        </el-table-column>
	                        <el-table-column label="状态变更时间" prop="lastModifiedTime" inline-template>
	                        	<div>{{ row.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
	                        </el-table-column>
	                        <el-table-column label="支付通道" prop="paymentGateWay"></el-table-column>
	                        <el-table-column label="状态" prop="status"></el-table-column>
	                        <el-table-column label="备注" prop="remark">
	                        </el-table-column>
			            </el-table>
					</div>
					<div class="ft text-align-center">
			            <PageControl 
			                v-model="pageConds.pageIndex"
			                :size="dataSource.size"
			                :per-page-record-number="pageConds.perPageRecordNumber">
			            </PageControl>
			        </div>
				</div>
				
                <div class="block">
                    <SystemOperateLog
                        ref="sysLog" 
                        :for-object-uuid="$route.params.uuid">
                    </SystemOperateLog>
                </div>
            </div>
        </div>

        <ReCallBackModal
            v-model="reCallbackModal.show"
            :id="$route.params.uuid"
            @submit="refreshDetail($route.params.uuid)">
        </ReCallBackModal>

        <CashFlowSelectModal
            v-model="cashFlowSelectModal.show"
            :uuid="$route.params.uuid">
        </CashFlowSelectModal>

        <ModifyPaymentOrderStatusModal
            v-model="paymentOrderStatusModal.show"
            :model="paymentOrderStatusModal.model"
            @submit="refreshDetail($route.params.uuid)">
        </ModifyPaymentOrderStatusModal>

    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import Pagination from 'mixins/Pagination';

    export default {
        mixins: [Pagination],
        components:{
            SystemOperateLog: require('views/include/SystemOperateLog'),
            ReCallBackModal: require('./include/ReCallBackModal'),
            ModifyPaymentOrderStatusModal: require('./include/ModifyPaymentOrderStatusModal'),
            CashFlowSelectModal: require('./include/CashFlowSelectModal')
        },
        activated: function() {
            if (this.$route.params.uuid) {
                this.fetchDetail(this.$route.params.uuid);
            }
        },
        data: function() {
            return {
                fetching: false,
                action: '/repayment-order/payment/detail-data-record',
                autoload: false,
                
                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                },
                detailModel: {},
                canCancel: true,
                showModel: {},
                isUpdate: false,
                isCanSelect: false,

                reCallbackModal: {
                    show: false
                },
                cashFlowSelectModal: {
                    show: false
                },
                paymentOrderStatusModal: {
                    show: false,
                    model: {}
                }
            }
        },
        computed: {
            cashFlowList: function() {
                return $.isEmptyObject(this.showModel) ? [] : [this.showModel];
            }
        },
        methods: {
            fetch: function() {
                if (!this.$route.params.uuid) return;
                
                if (this.fetchTimer) {
                    clearTimeout(this.fetchTimer);
                }

                this.fetchTimer = setTimeout(() => {
                    if (this.dataSource.fetching
                        && this.equalTo(purify(this.conditions), purify(this.previousConditions))) {
                        return
                    }

                    this.getData({
                        url: this.action,
                        data: Object.assign({}, {paymentOrderUuid: this.$route.params.uuid})
                    });
                }, 10);
            },
            fetchDetail: function(uuid) {
                if (this.fetching) return;
                this.fetching = true;

                ajaxPromise({
                    url: `/repayment-order/payment/detail-data`,
                    data: {
                        paymentOrderUuid: uuid
                    }
                }).then(data => {
                    this.detailModel = data.model;
                    this.canCancel = data.canCancelPaymentOrder;
                    this.showModel = data.showModel;
                    this.isUpdate = data.isUpdate;
                    this.isCanSelect = data.isCanSelect;

                    this.detailModel.payWay != 'OFFLINE_TRANSFER' && this.fetch();
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            },
            obsolete: function(paymentOrderUuid) {
                MessageBox.open('确认作废','',[{
                    text: '关闭',
                    handler: () => MessageBox.close()
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: `/repayment-order/cancelPaymentOrder`,
                            data: {
                                paymentOrderUuid: paymentOrderUuid
                            }
                        }).then(data => {
                            MessageBox.once('closed', () => {
                                this.refreshDetail();
                            });
                            MessageBox.open(data.message);
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                }])
            },
            reCallback: function() {
                var modal = this.reCallbackModal;
                modal.show = true;
            },
            handlerCashFlow: function(paymentOrderUuid) {
                ajaxPromise({
                    url: `/repayment-order/matchCashFlow`,
                    data: {
                        paymentOrderUuid: paymentOrderUuid
                    }
                }).then(data => {
                    MessageBox.once('closed', () => {
                        this.refreshDetail();
                    });
                    MessageBox.open('操作成功');
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            modifyPaymentOrderStatus: function(paymentOrderUuid) {
                this.paymentOrderStatusModal.show = true;
                this.paymentOrderStatusModal.model =  {
                    paymentOrderUuid: paymentOrderUuid
                }
            },
            cashFlowSelect: function() {
                this.cashFlowSelectModal.show = true;
            },
            refreshDetail: function() {
                this.$refs.sysLog.fetch();
                this.fetchDetail(this.$route.params.uuid);
            }
        }
    }
</script>