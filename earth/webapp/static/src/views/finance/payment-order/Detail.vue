<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '结算单详情'}]">
                <el-button
                    v-if="ifElementGranted('close-payment-order') && (orderViewDetail.executingSettlingStatus_od == 0 ||orderViewDetail.executingSettlingStatus_od == 1)"
                    type="primary"
                    size="small"
                    @click="handleClose">关闭</el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
            	<div class="top">
            		<div class="block">
            			<h5 class="hd">贷款信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>{{$utils.locale('financialContract')}}名称：{{ orderViewDetail.financialContractName }}</p>
                                <p>合同编号 ：<a :href="`${ctx}#/data/contracts/detail?id=${orderViewDetail.contractId}`">{{ orderViewDetail.contractNo }}</a></p>
                                <p>客户名称 ：<span class="customer-name">{{ orderViewDetail.customerName }}</span></p>
                                <p>客户账户 ：{{ orderViewDetail.customerAccount }}</p>
            				</div>
            			</div>
            		</div>
            		<div class="block">
            			<h5 class="hd">还款信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>还款编号 ：
                                    <a :href="`${ctx}#/finance/assets/${orderViewDetail.assetSetUuid}/detail`">
                                        {{ orderViewDetail.singleLoanContractNo }}
                                    </a>
                                </p>
                                <p>商户还款计划编号 ：{{ orderViewDetail.outerRepaymentPlanNo }}</p>
                                <p>计划还款日期 ：
                                    <span class="asset-recycle-date">{{ orderViewDetail.assetRecycleDate | formatDate }}</span>
                                </p>
                                <p>计划还款金额（元） ：{{ orderViewDetail.assetRecycleAmount | formatMoney }}</p>
                            </div>
                        </div>
            		</div>
            		<div class="block">
            			<h5 class="hd">结算信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>结算编号 ：{{ orderViewDetail.orderNo }}</p>
                                <p>结算金额（元） ：
                                    <el-popover
                                        @show="fetchAssetSetStatistics(orderViewDetail.orderId)"
                                        trigger="hover"
                                        placement="right">
                                        <div>
                                            <div v-if="statistics.error">{{ statistics.error }}</div>
                                            <template v-else>
                                                <div>还款本金：
                                                    <span v-if="statistics.loanAssetPrincipal == null">--</span>
                                                    <span v-else>{{ statistics.loanAssetPrincipal | formatMoney}}</span>
                                                </div>
                                                <div>还款利息：
                                                    <span v-if="statistics.loanAssetInterest == null">--</span>
                                                    <span v-else>{{ statistics.loanAssetInterest | formatMoney }}</span>
                                                </div>
                                                <div>贷款服务费：
                                                    <span v-if="statistics.loanServiceFee == null">--</span>
                                                    <span v-else>{{ statistics.loanServiceFee | formatMoney }}</span>
                                                </div>
                                                <div>技术维护费：
                                                    <span v-if="statistics.loanTechFee == null">--</span>
                                                    <span v-else>{{ statistics.loanTechFee | formatMoney }}</span>
                                                </div>
                                                <div>其他费用：
                                                    <span v-if="statistics.loanOtherFee == null">--</span>
                                                    <span v-else>{{ statistics.loanOtherFee | formatMoney }}</span>
                                                </div>
                                                <div>逾期罚息：
                                                    <span v-if="statistics.overdueFeePenalty == null">--</span>
                                                    <span v-else>{{ statistics.overdueFeePenalty | formatMoney }}</span>
                                                </div>
                                                <div>逾期违约金：
                                                    <span v-if="statistics.overdueFeeObligation == null">--</span>
                                                    <span v-else>{{ statistics.overdueFeeObligation | formatMoney }}</span>
                                                </div>
                                                <div>逾期服务费：
                                                    <span v-if="statistics.overdueFeeService == null">--</span>
                                                    <span v-else>{{ statistics.overdueFeeService | formatMoney }}</span>
                                                </div>
                                                <div>逾期其他费用：
                                                    <span v-if="statistics.overdueFeeOther == null">--</span>
                                                    <span v-else>{{ statistics.overdueFeeOther | formatMoney }}</span>
                                                </div>
                                                <div>逾期费用合计：
                                                    <span v-if="statistics.totalOverdueFee == null">--</span>
                                                    <span v-else>{{ statistics.totalOverdueFee | formatMoney }}</span>
                                                </div>
                                            </template>
                                        </div>
                                        <a slot="reference">{{ orderViewDetail.orderAmount | formatMoney }}</a>
                                    </el-popover>
                                </p>
                                <p>创建时间 ：{{ orderViewDetail.createTime }}</p>
                                <p>状态变更时间 ：{{ orderViewDetail.modifyTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</p>
                                <p>结算状态 ：{{ orderViewDetail.executingSettlingStatus_cn }}</p>
            				</div>
            			</div>
            		</div>
            	</div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">扣款订单明细</h5>
                    <PagingTable
                        :data="deductApplications"
                        :pagination="true">
                            <el-table-column label="订单编号" prop="deductId" inline-template>
                                <a :href="`${ctx}#/finance/application/${row.deductId }/detail`">{{ row.deductId }}</a>
                            </el-table-column>
                            <el-table-column label="订单类型" prop="repaymentType_CN"></el-table-column>
                            <el-table-column label="扣款金额" prop="plannedDeductTotalAmount" inline-template>
                                <span>{{ row.plannedDeductTotalAmount | formatMoney }}</span>
                            </el-table-column>
                            <el-table-column label="状态变更时间" prop="completeTime"></el-table-column>
                            <el-table-column label="状态" prop="executionStatus_CN"></el-table-column>
                            <el-table-column label="备注" prop="executionRemark"></el-table-column>
                    </PagingTable>
                </div>

                <div class="block">
                    <h5 class="hd">支付记录</h5>
                    <PagingTable
                        :data="paymentRecordModels"
                        :pagination="true">
                            <el-table-column label="支付编号" inline-template>
                                <div>
                                    <a v-if="row.recordType == 1" :href="`javascript: void 0;`">{{ row.paymentRecordNo }}</a>
                                    <a v-else-if="row.recordType == 2" :href="`${ctx}#/capital/payment-cash-flow/online-payment/${row.id}/detail`">{{ row.paymentRecordNo }}</a>
                                    <a v-else-if="row.recordType == 3" :href="`${ctx}#/capital/payment-cash-flow/offline-payment/${row.id}/detail`">{{ row.paymentRecordNo }}</a>
                                    <span v-else-if="row.recordType == 4">{{ row.paymentRecordNo }}</span>
                                </div>
                            </el-table-column>
                            <el-table-column label="支付通道" prop="paymentGateway"></el-table-column>
                            <el-table-column label="银行名称" prop="bankName"></el-table-column>
                            <el-table-column label="账户姓名" prop="accountCustomerName"></el-table-column>
                            <el-table-column label="银行账户号" prop="bankAccountNo"></el-table-column>
                            <el-table-column label="交易金额" prop="transactionAmount" inline-template>
                                <div>{{ row.transactionAmount | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column label="状态变更时间" prop="stateChangeTime"></el-table-column>
                            <el-table-column label="状态" prop="status"></el-table-column>
                            <el-table-column label="备注" prop="remark"></el-table-column>
                    </PagingTable>
                </div>

                <div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="orderViewDetail.repaymentBillId"></SystemOperateLog>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default{
        components:{
            SystemOperateLog: require('views/include/SystemOperateLog'),
            PagingTable: require('views/include/PagingTable'),
        },
        data: function() {
            return {
                fetching: false,
                statistics: {},
                orderViewDetail: {
                    aasetSetId: '',
                    assetRecycleAmount: '',
                    assetRecycleDate: '',
                    contractId: '',
                    contractNo: '',
                    createTime: '',
                    customerAccount: '',
                    customerName: '',
                    executingSettlingStatus_cn: '',
                    executingSettlingStatus_od: '',
                    financialContractName: '',
                    modifyTime: '',
                    orderAmount: '',
                    orderId: '',
                    orderNo: '',
                    paymentRecordModels: '',
                    repaymentBillId: '',
                    singleLoanContractNo: '',
                },
                deductApplications: [],
                paymentRecordModels: []
            }
        },
        activated: function() {
            this.fetch(this.$route.params.orderId);
            console.log('ddd')
        },
        methods: {
            fetch: function(orderId) {
                this.fetching = true;

                ajaxPromise({
                    url: `/payment-manage/order/${orderId}/detail`,
                }).then(data => {
                    const { paymentRecordModels, deductApplications, ...others } = data.detail;
                    this.orderViewDetail = others;
                    this.paymentRecordModels = paymentRecordModels || [];
                    this.deductApplications = deductApplications || []
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                });
            },
            fetchAssetSetStatistics: function(orderId) {
                ajaxPromise({
                    url: `/payment-manage/order/${orderId}/chargesDetail`
                }).catch(error => {
                    this.statistics = { error };
                }).then(data => {
                    this.statistics = data.data || {};
                });
            },
            handleClose: function() {
                const orderId = this.$route.params.orderId;

                MessageBox.open('确认关闭该结算单？', null, [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: `/payment-manage/order/close`,
                            type: 'post',
                            data: {
                                orderId,
                                type: 'normal'
                            }
                        }).then(data => {
                            MessageBox.close();
                            this.fetch(orderId);
                        }).catch(message => MessageBox.open(message))
                    }
                }]);
            }
        }
    }
</script>
