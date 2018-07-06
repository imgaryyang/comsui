<style lang="sass">

</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '扣款单详情'}]">
            </Breadcrumb>

            <div class="col-layout-detail">
            	<div class="top">
                    <div class="block">
                        <h5 class="hd">贷款信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>贷款合同编号 ：<a class="contractNo" 
                                        :href="`${ctx}#/data/contracts/detail?id=${loanInformation.loanContractId}`">{{loanInformation.loanContractNo}}</a></p>
                                <p>贷款客户姓名：{{ loanInformation.loanCustoemrNo }}</p>
                                <p>贷款客户编号：{{ loanInformation.loanCustoemrSource }}</p>
                                <p>资产编号：{{ loanInformation.assetSetNo}}</p>
                                <p>还款编号：{{ loanInformation.repaymentPlanNos }}</p>
                                <p>商户还款计划编号：{{ loanInformation.outerRepaymentPlanNos }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">扣款信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>扣款单号：{{deductInformation.deductApplicationCode}}</p>
                                <p>创建时间：{{ deductInformation.deductCreateDate | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>扣款受理时间：{{ deductInformation.deductReceiveTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>状态变更时间：{{ deductInformation.deductHappenTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>扣款类型：{{ deductInformation.repaymentType }}</p>
                                <p>扣款状态：{{ deductInformation.deductStatus }}</p>
                            </div>
                            <div class="col">
                                <p>应扣款金额：{{deductInformation.planDeductAmount | formatMoney }}</p>
                                <p>实际扣款金额：{{ deductInformation.actualDeductAmount | formatMoney }}</p>
                                <p>扣款本金：{{ deductInformation.repaymentPrincipal | formatMoney }}</p>
                                <p>扣款利息：{{ deductInformation.repaymentInterest | formatMoney }}</p>
                                <p>贷款服务费：{{ deductInformation.loanFee | formatMoney }}</p>
                                <p>技术维护费：{{ deductInformation.techFee | formatMoney }}</p>
                                <p>其他费用：{{ deductInformation.otherFee | formatMoney }}</p>
                            </div>
                            <div class="col">
                                <p>逾期罚息：{{ deductInformation.penaltyFee | formatMoney }}</p>
                                <p>逾期违约金：{{ deductInformation.latePenalty | formatMoney }}</p>
                                <p>逾期服务费：{{ deductInformation.lateFee | formatMoney }}</p>
                                <p>逾期其他费用：{{ deductInformation.lateOtherCost | formatMoney }}</p>
                                <p>备注：{{ deductInformation.remark }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">还款账户信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>还款账户名：{{ paymentAccountInformation.cpBankAccountHolder }}</p>
                                <p>还款方账户号：{{ paymentAccountInformation.cpBankCardNo }}</p>
                                <p>账户开户行：{{ paymentAccountInformation.cpBankName }}</p>
                                <p>开户行所在地：{{ paymentAccountInformation.cpBankProvince }}&nbsp; {{ paymentAccountInformation.cpBankCity }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">收款账户信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>收款账户名：{{ capitalAccountInformation.accountName }}</p>
                                <p>收款方账户号：{{ capitalAccountInformation.accountNo }}</p>
                                <p>账户开户行：{{ capitalAccountInformation.bankName }}</p>
                                <p>开户行所在地：{{ }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">线上支付记录</h5>
                    <PagingTable
                        :data="paymentOrderInformations"
                        :pagination="true">
                            <el-table-column inline-template prop="paymentOrderNo" label="支付编号">
                                <a :href="`${ctx}#/capital/payment-cash-flow/online-payment/${ row.deductPlanId }/detail`">{{ row.paymentOrderNo }}</a>
                            </el-table-column>
                            <el-table-column 
                                prop="deductOrderNo"
                                label="扣款单号">
                            </el-table-column>
                            <el-table-column prop="bankName" label="银行名称">
                            </el-table-column>
                            <el-table-column 
                                prop="bankAccount"
                                label="银行账户号">
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="代扣金额">
                                <div>{{ row.deductAmount | formatMoney}}</div>
                            </el-table-column>
                            <el-table-column
                                prop="paymentChannelCn"
                                label="通道名称">
                            </el-table-column>
                            <el-table-column inline-template
                                label="发生时间">
                                <div>{{ row.occurTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
                            </el-table-column>
                            <el-table-column inline-template label="状态">
                                <span class="color-danger">{{ row.repaymentPlanStatus }}</span>
                            </el-table-column>
                            <el-table-column prop="remark" label="备注">
                            </el-table-column>
                    </PagingTable>
                </div>
            </div>
            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">凭证明细</h5>
                    <PagingTable
                        :data="thirdPartVoucherDeductShowModels"
                        :pagination="true">
                            <el-table-column inline-template label="凭证编号">
                                <a :href="`${ctx}#/capital/voucher/third-party/${row.voucherUuid}/detail`">{{row.voucherCode}}</a>
                            </el-table-column>
                            <el-table-column 
                                prop="specialAccountNo"
                                label="专户账号">
                            </el-table-column>
                            <el-table-column prop="bankAccountName" label="往来机构名称">
                            </el-table-column>
                            <el-table-column 
                                prop="accountPayerName"
                                label="账户姓名">
                            </el-table-column>
                            <el-table-column 
                                prop="bankAccountNo"
                                label="机构账户号">
                            </el-table-column>
                            <el-table-column 
                                prop="voucherAmount"
                                label="凭证金额">
                            </el-table-column>
                            <el-table-column
                                prop="settleClearNo"
                                label="清算单号">
                            </el-table-column>
                            <el-table-column prop="deductType" label="代扣类型">
                            </el-table-column>
                            <el-table-column prop="voucherSource" label="凭证来源">
                            </el-table-column>
                            <el-table-column prop="thirdPartPaymentChannel" label="第三方通道">
                            </el-table-column>
                            <el-table-column prop="voucherStatus" label="凭证状态">
                            </el-table-column>
                    </PagingTable>
                </div>
                <div class="block">
                    <SystemOperateLog 
                        ref="sysLog"
                        :for-object-uuid="$route.params.id"></SystemOperateLog>
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

                reCallbackModal: {
                    show:false,
                    fields:{
                        comment: ''
                    },
                    rules: {
                        comment: { required: true, message: ' '}
                    }
                },

                accountInformation:{},
                deductInformation:{},
                loanInformation:{},
                paymentAccountInformation:{},
                capitalAccountInformation:{},
                showModel: {},

                paymentOrderInformations: [],
                repaymentPlanDetailShowModels:[],
                thirdPartVoucherDeductShowModels: [],
            }
        },
        watch: {
            show: function(current) {
                this.$emit('input', current);
                if (current) {
                    this.$refs.form.resetFields();
                }
            },
        },
        activated: function() {
            this.fetch(this.$route.params.deudctNo);
        },
        methods: {
            fetch: function(deudctNo) {
                this.fetching = true;

                ajaxPromise({
                    url: `/deduct/application/detail/${deudctNo}`
                }).then(data => {
                    this.showModel = data.showModel;
                    this.accountInformation = data.showModel.accountInformation;
                    this.deductInformation = data.showModel.deductInformation;
                    this.loanInformation = data.showModel.loanInformation;
                    this.paymentAccountInformation = data.showModel.paymentAccountInformation;
                    this.capitalAccountInformation = data.showModel.capitalAccountInformation;
                    this.paymentOrderInformations = data.showModel.paymentOrderInformations || [];
                    this.repaymentPlanDetailShowModels = data.showModel.repaymentPlanDetailShowModels || [];
                    this.thirdPartVoucherDeductShowModels = data.showModel.thirdPartVoucherDeductShowModels || [];
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            },
            submit: function() {
                this.$refs.form.validate(valid => {
                    if (!valid) return;
                    ajaxPromise({
                        url: `/remittance/application/details/${this.$route.params.id}`,
                        data: this.reCallbackModal.fields,
                        type: 'POST'
                    }).then(data => {
                        this.reCallbackModal.show = false;
                        setTimeout(()=> {
                            MessageBox.once('close', () => {
                                this.fetch(this.$route.params.id);
                                this.$refs.sysLog.fetch();
                            });
                            MessageBox.open('计划回调次数已增加，稍后系统会自动回调结果');
                        }, 500);
                    })
                    .catch(message => {
                        MessageBox.open(message);
                    })
                    .then(() => {
                        this.fetching = false;
                    });
                });

            }
        }
    }
</script>
