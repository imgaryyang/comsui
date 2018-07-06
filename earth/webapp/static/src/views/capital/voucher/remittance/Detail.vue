<style lang="sass">
    
</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '专户放款凭证'}, {title: '专户放款凭证详情'} ]">
                <!-- <el-button  v-if="false" @click="clickRevoke" type="primary" size="small">作废</el-button> -->
            </Breadcrumb>
            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">贷款信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>项目名称 : 
                                    <a :href="`${ctx}#/financial/contract/${showModel.financialContractUuid}/detail`">{{ showModel.financialContractNo }}</a>
                                </p>
                                <p>贷款合同编号 : 
                                    <a :href="`${ctx}#/data/contracts/detail?id=${showModel.contractId}`">{{ showModel.contractUniqueId }}</a>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">凭证信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>凭证编号 : {{ showModel.sourceDocumentNo }}</p>
                                <p>来源编号 : {{ showModel.sourceNo }}</p>
                                <p>凭证金额 : {{ showModel.bookingAmount | formatMoney }}</p>
                                <p>凭证来源 : {{ showModel.voucherSource }}</p>
                                <p>备注 : {{ showModel.remark }}</p>
                            </div>
                            <div class="col">
                                <p>交易通道 : {{ showModel.transcationGateway }}</p>
                                <p>交易请求号 : {{ showModel.tradeUuid }}</p>
                                <p>创建时间 : {{ showModel.createTime }}</p>
                                <p>状态变更时间 : {{ showModel.voucherStatusModifyTime }}</p>
                                <p>凭证状态 : {{ showModel.sourceDocumentStatus }}</p>
                            </div>
                            <div class="col">
                                <p>借贷标记 : {{showModel.sourceAccountSide}}</p>
                                <p>专户账号 : {{ showModel.outlierAccount }}</p>
                                <p>来往机构 : {{ showModel.bankName }}</p>
                                <p>机构账户名 : {{ showModel.outlierCounterPartyName }}</p>
                                <p>机构账户号 : {{ showModel.outlierCounterPartyAccount }}</p>
                            </div>
                        </div>
                    </div>
                    <!-- <div class="block">
                        <h5 class="hd">账户信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>客户姓名 : {{ showModel.customerName }}</p>
                                <p>客户账号 : {{ showModel.counterPartyAccountNo }}</p>
                                <p>账号开户行 : {{ showModel.counterPartyPaymentBank }}</p>
                                <p>开户行所在地 : {{ showModel.counterPartyPaymentBank }}</p>
                            </div>
                        </div>
                    </div> -->
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block" key="system">
                    <h5 class="hd">凭证资金明细</h5>
                    <div class="bd">
                        <el-table 
                            :data="[showModel]" 
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column  label="代付单号" prop="execReqNo" inline-template>
                            <div>
                                <a v-if="row.systemBillType==0" :href="`${ctx}#/capital/remittance-cash-flow/plan-execlog/${row.execReqNo}/detail`">{{ row.execReqNo }}</a>
                                <span v-else>{{ row.execReqNo }}</span>
                            </div>

                            </el-table-column>

                            <el-table-column label="放款单号" prop="remittancePlanUuid" inline-template>
                                <a :href="`${ctx}#/data/remittance/plan/${row.remittancePlanUuid}/detail`">{{ row.remittancePlanUuid }}</a>
                            </el-table-column>

                            <el-table-column inline-template label="交易金额">
                                <div>{{ row.plannedAmount | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="capitalRequestNo" label="通道请求号">
                            </el-table-column>

                            <el-table-column inline-template label="状态变更时间">
                                <div>{{ row.remittanceStatusModifyTime | formatDate  }}</div>
                            </el-table-column>

                            <el-table-column prop="executionStatus" label="交易状态">
                            </el-table-column>

                            <el-table-column prop="executionRemark" label="备注">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="block" key="interface">
                    <h5 class="hd">交易流水明细</h5>
                    <div class="bd">
                        <el-table 
                            :data="[showModel]"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column label="通道流水号" prop="bankSequenceNo">
                            </el-table-column>

                            <el-table-column label="流水金额" inline-template>
                                <div>{{ row.transactionAmount | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="accountSide" label="借贷标记">
                            </el-table-column>

                            <el-table-column prop="cashFlowRequestNo" label="通道请求号">
                            </el-table-column>

                            <el-table-column inline-template label="流水入账时间">
                                <div>{{ row.transactionTime | formatDate }}</div>
                            </el-table-column>

                            <el-table-column prop="cashFlowRemark" label="备注">
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
    import SystemOperateLog from 'views/include/SystemOperateLog';

    export default{
        components:{
            SystemOperateLog
        },
        data: function() {
            return {
                fetching: false,
                showModel: {},
            }
        },
        activated: function() {
            this.fetch(this.$route.params.id);
        },
        methods: {
            fetch: function(voucherNo) {
                this.fetching = true;
                ajaxPromise({
                    // url: `/remittance/detail/{sourceDocumentUuid}/dataVoucherDetailModel`
                    url: `/audit/remittance/${voucherNo}/detail/remittanceVoucher`,
                })
                .then(data => {
                    this.showModel = data.showModel;
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                });
            },
            clickRevoke: function() {
                MessageBox.open('是否确定作废此凭证？','提示',[
                    {
                        text:'确定',
                        type: 'success',
                        handler: ()=>{}
                    },
                    {
                        text:'取消',
                        handler: ()=>{
                            MessageBox.close();
                        }
                    }
                ]);
            },
        }
    }
</script>
