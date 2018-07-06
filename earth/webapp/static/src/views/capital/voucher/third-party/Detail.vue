<style lang="sass">

</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '第三方凭证账户'}, {title: '第三方扣款凭证详情'} ]">
            </Breadcrumb>

            <div class="col-layout-detail">
            	<div class="top">
            		<div class="block">
            			<h5 class="hd">贷款信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>贷款合同编号 :
                                    <a :href="`${ctx}#/data/contracts/detail?id=${thirdPartVoucherLoanInformation.contractId}`">{{ thirdPartVoucherLoanInformation.contractNo }}</a>
                                </p>
                                <p>贷款客户编号 : {{ thirdPartVoucherLoanInformation.loanCustomerNo }}</p>
            					<p>资产编号 : {{ thirdPartVoucherLoanInformation.assetSetNo }}</p>
            					<p>还款编号 : {{ thirdPartVoucherLoanInformation.repayPlanNo }}</p>
                                <p>商户还款计划编号 : {{ thirdPartVoucherLoanInformation.outerRepaymentPlanNo }}</p>
            				</div>
            			</div>
            		</div>
            		<div class="block">
            			<h5 class="hd">凭证信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>凭证编号 : {{ thirdPartVoucherVoucherInformation.voucherNo }}</p>
            					<p>凭证来源 : {{ thirdPartVoucherVoucherInformation.voucherSource }}</p>
            					<p>代扣类型 : {{ thirdPartVoucherVoucherInformation.deductType }}</p>
            					<p>凭证金额 : {{ thirdPartVoucherVoucherInformation.voucherAmount | formatMoney }}</p>
            					<p>专户账号 : {{ thirdPartVoucherVoucherInformation.specialAccountNo }}</p>
                            </div>
                            <div class="col">
            					<p>来往机构名称 : {{ thirdPartVoucherVoucherInformation.payBankName }}</p>
            					<p>机构账户号 : {{ thirdPartVoucherVoucherInformation.payBankAccount }}</p>
            					<p>发生时间 : {{ thirdPartVoucherVoucherInformation.occurTime }}</p>
            					<p>第三方通道 : {{ thirdPartVoucherVoucherInformation.thirdPartPayChannel }}</p>
            					<p>凭证状态 : {{ thirdPartVoucherVoucherInformation.voucherStatus }}</p>
                            </div>
                            <div class="col">
            					<p>清算单号 : {{ thirdPartVoucherVoucherInformation.settleNo }}</p>
            					<p>接口请求编号 : {{ thirdPartVoucherVoucherInformation.interfaceRequestNo }}</p>
            					<p>备注 : {{ thirdPartVoucherVoucherInformation.remark }}</p>
                            </div>
                        </div>
            		</div>
            		<div class="block">
            			<h5 class="hd">账户信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>客户姓名 : {{ thirdPartVoucherCustomerInformation.cutomerName }}</p>
            					<p>账户开户行 : {{ thirdPartVoucherCustomerInformation.bankName }}</p>
            					<p>开户行所在地 : {{ thirdPartVoucherCustomerInformation.bankRegion }}</p>
            					<p>绑定账号 : {{ thirdPartVoucherCustomerInformation.bankAccountNo }}</p>
            				</div>
            			</div>
            		</div>
            	</div>
            </div>

            <div class="row-layout-detail">
            	<div class="block" key="system" v-if="thirdPartVoucherFinanceInformation.billType == 0">
            		<h5 class="hd">凭证资金单据(系统线上支付单)</h5>
            		<div class="bd">
            			<el-table
                            :data="[thirdPartVoucherFinanceInformation]"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
            				<el-table-column label="支付单号" prop="onlinePaymentNo">
                                <!-- <a :href="`${ctx}/`">{{ row.onlinePaymentNo }}</a> -->
            				</el-table-column>

            				<el-table-column label="结算单号" prop="businessNo">
                                <!-- <a :href="`${ctx}/`">{{ row.businessNo }}</a> -->
            				</el-table-column>

            				<el-table-column prop="bankName" label="银行名称">
            				</el-table-column>

            				<el-table-column prop="payerName" label="账户姓名">
            				</el-table-column>

            				<el-table-column prop="bankAccountNo" label="银行账户名">
            				</el-table-column>

            				<el-table-column prop="deductAmount" inline-template label="代扣金额">
                                <div>{{ row.deductAmount | formatMoney }}</div>
            				</el-table-column>

            				<el-table-column prop="occurTime" label="发生时间">
            				</el-table-column>

            				<el-table-column prop="paymentWay" label="支付方式">
            				</el-table-column>

            				<el-table-column prop="channelRequestNo" label="通道请求号">
            				</el-table-column>

            				<el-table-column prop="thirdPartFlowNo" label="第三方流水号">
            				</el-table-column>

            				<el-table-column prop="status" label="状态">
            				</el-table-column>

            				<el-table-column prop="remark" label="备注">
            				</el-table-column>
            			</el-table>
            		</div>
            	</div>

                <div class="block" key="interface" v-else>
                    <h5 class="hd">凭证资金单据(接口线上支付单)</h5>
                    <div class="bd">
                        <el-table
                            :data="[thirdPartVoucherFinanceInformation]"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column label="支付编号" inline-template>
                               <a :href="`${ctx}#/capital/payment-cash-flow/online-payment/${row.deductPlanId}/detail`">{{ row.onlinePaymentNo }}</a>
                            </el-table-column>

                            <el-table-column label="扣款单号" inline-template>
                               <a :href="`${ctx}#/finance/application/${row.deductId}/detail`">{{ row.businessNo }}</a>
                            </el-table-column>

                            <el-table-column prop="bankName" label="银行名称">
                            </el-table-column>

                            <el-table-column prop="bankAccountNo" label="银行账户号">
                            </el-table-column>

                            <el-table-column prop="deductAmount" inline-template label="代扣金额">
                                <div>{{ row.deductAmount | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="channelRequestNo" label="通道请求号">
                            </el-table-column>

                            <el-table-column prop="thirdPartFlowNo" label="第三方流水号">
                            </el-table-column>

                            <el-table-column prop="occurTime" label="发生时间">
                            </el-table-column>

                            <el-table-column prop="status" label="状态">
                            </el-table-column>

                            <el-table-column prop="remark" label="备注">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>

                <div class="block">
                    <h5 class="hd">凭证业务单据</h5>
                    <div class="bd">
                        <el-table
                            :data="[thirdPartVoucherBusinessInformation]"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column label="还款编号" inline-template>
                                <a :href="`${ctx}#/finance/assets/${row.assetSetUuid}/detail`">{{ row.repaymentPlanNo }}</a>
                            </el-table-column>

                            <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo">
                            </el-table-column>

                            <el-table-column label="贷款合同编号" inline-template>
                                <a :href="`${ctx}#/data/contracts/detail?id=${row.contractId}`">{{ row.contractNo }}</a>
                            </el-table-column>

                            <el-table-column prop="financialProjectName" :label="$utils.locale('financialContract.name')">
                            </el-table-column>

                            <el-table-column prop="planRecycleDate" label="计划还款日期">
                            </el-table-column>

                            <el-table-column prop="actualRecycleDate" label="实际还款日期">
                            </el-table-column>

                            <el-table-column prop="customerName" label="客户姓名">
                            </el-table-column>

                            <el-table-column prop="planRepayAmount" inline-template label="应还款金额">
                                <div>{{ row.planRepayAmount | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="payedAmount" inline-template label="已还金额">
                                <div>{{ row.payedAmount | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="voucherPayedAmount" inline-template label="凭证关联金额">
                                <div>{{ row.voucherPayedAmount | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="repayStatus" label="还款状态">
                            </el-table-column>

                            <el-table-column prop="settleStatus" label="核销状态">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>

                <div class="block">
                    <h5 class="hd">清算流水单据</h5>
                    <div class="bd">
                        <el-table
                            :data="[]"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="" label="商户号">
                            </el-table-column>

                            <el-table-column prop="" label="终端号">
                            </el-table-column>

                            <el-table-column prop="" label="交易类型">
                            </el-table-column>

                            <el-table-column prop="" label="交易子类型">
                            </el-table-column>

                            <el-table-column prop="" label="宝付订单号">
                            </el-table-column>

                            <el-table-column prop="" label="商户订单号">
                            </el-table-column>

                            <el-table-column prop="" label="清算日期">
                            </el-table-column>

                            <el-table-column prop="" label="订单状态">
                            </el-table-column>

                            <el-table-column prop="" label="交易金额">
                            </el-table-column>

                            <el-table-column prop="" label="手续费">
                            </el-table-column>

                            <el-table-column prop="" label="宝付交易号">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default{
        data: function() {
            return {
                fetching: false,
                thirdPartVoucherLoanInformation: {},
                thirdPartVoucherVoucherInformation: {},
                thirdPartVoucherCustomerInformation: {},
                thirdPartVoucherFinanceInformation: {},
                thirdPartVoucherBusinessInformation: {}
            }
        },
        activated: function() {
            this.fetch(this.$route.params.id);
        },
        methods: {
            fetch: function(journalVoucherUuid) {
                this.fetching = true;

                ajaxPromise({
                    url: `/voucher/thirdParty/detail/${journalVoucherUuid}`
                }).then(data => {
                    this.thirdPartVoucherLoanInformation = data.detailShowModel.thirdPartVoucherLoanInformation;
                    this.thirdPartVoucherVoucherInformation = data.detailShowModel.thirdPartVoucherVoucherInformation;
                    this.thirdPartVoucherCustomerInformation = data.detailShowModel.thirdPartVoucherCustomerInformation;
                    this.thirdPartVoucherFinanceInformation = data.detailShowModel.thirdPartVoucherFinanceInformation;
                    this.thirdPartVoucherBusinessInformation = data.detailShowModel.thirdPartVoucherBusinessInformation;
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                });
            }
        }
    }
</script>
