<style lang="sass">
    
</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '第三方凭证账户'}, {title: '第三方支付凭证详情'} ]">
                <el-button @click="clickRecharge" type="primary" size="small" v-if="showModel.voucherLogIssueStatus !=='已核销'">重新核销</el-button>
                <el-button @click="clickRevoke" type="primary" size="small" v-if="showModel.voucherLogStatus !== '检校成功'">作废</el-button>
                <el-button @click="clickHistory" type="primary" size="small">历史版本</el-button>
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
            					<p>凭证编号 : {{ showModel.voucherUuid }}</p>
            					<p>来源编号 : {{ showModel.voucherNo }}</p>
            					<p>凭证金额 : {{ showModel.transcationAmount | formatMoney }}</p>
            					<p>凭证来源 : {{ showModel.voucherSource }}</p>
            					<p>备注 : {{ showModel.errorMessge }}</p>
                            </div>
                            <div class="col">
            					<p>交易网关 : {{ showModel.transcationGateway }}</p>
            					<p>交易请求号 : {{ showModel.tradeUuid }}</p>
            					<p>创建时间 : {{ showModel.createTime }}</p>
            					<p>状态变更时间 : {{ showModel.statusModifyTime }}</p>
            					<p>校验状态 : {{ showModel.voucherLogStatus }}</p>
                                <p>核销状态 : {{ showModel.voucherLogIssueStatus }}</p>
                            </div>
                            <div class="col">
            					<p>清算状态 : </p>
            					<p>清算账号 : {{ showModel.receivableAccountNo }}</p>
            					<p>来往机构 : {{ showModel.paymentBank }}</p>
                                <p>机构账户名 : {{ showModel.paymentName }}</p>
                                <p>机构账户号 : {{ showModel.paymentAccountNo }}</p>
                            </div>
                        </div>
            		</div>
            		<div class="block">
            			<h5 class="hd">账户信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>客户姓名 : {{ showModel.customerName }}</p>
            					<!-- <p>客户账号 : {{ showModel.counterPartyAccountNo }}</p>
            					<p>账号开户行 : {{ showModel.counterPartyPaymentBank }}</p>
            					<p>开户行所在地 : {{ showModel. }}</p> -->
            				</div>
            			</div>
            		</div>
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
                            <el-table-column  label="支付单号" prop="deductPlanUuid" inline-template>
                                <a :href="`${ctx}#/capital/payment-cash-flow/online-payment/${row.deductPlanId}/detail`">{{ row.deductPlanUuid }}</a>
            				</el-table-column>

            				<el-table-column label="扣款单号" prop="deductId" inline-template>
                                <a :href="`${ctx}#/finance/application/${row.deductId}/detail`">{{ row.deductId }}</a>
            				</el-table-column>

            				<el-table-column inline-template label="交易金额">
            				    <div>{{ row.plannedTotalAmount | formatMoney }}</div>
                            </el-table-column>

            				<el-table-column prop="channelRequestNumber" label="通道请求号">
            				</el-table-column>

            				<el-table-column inline-template label="状态变更时间">
            				    <div>{{ row.statusModifyTime  }}</div>
                            </el-table-column>

            				<el-table-column prop="executionStatus" label="交易状态">
            				</el-table-column>

            				<el-table-column prop="executionRemark" label="备注">
            				</el-table-column>
            			</el-table>
            		</div>
            	</div>
                <div class="block" key="system">
                    <h5 class="hd">凭证业务明细</h5>
                    <div class="bd">
                        <el-table 
                            :data="showModel.repayDetails" 
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="repaymentPlanNo" label="还款计划编号" inline-template>
                                <a :href="`${ctx}#/finance/assets/${row.assetSetUuid}/detail`">{{ row.repaymentPlanNo  }}</a>
                            </el-table-column>

                            <el-table-column prop="outerRepaymentPlanNo" label="商户还款计划编号"></el-table-column>

                            <el-table-column prop="assetRecycleDate" label="计划还款日期" inline-template>
                                <div>{{ row.assetRecycleDate | formatDate }}</div>
                            </el-table-column>

                            <el-table-column prop="principal" label="还款本金" inline-template>
                                <div>{{ row.principal | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="interest" label="还款利息" inline-template>
                                <div>{{ row.interest | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="serviceCharge" label="贷款服务费" inline-template>
                                <div>{{ row.serviceCharge | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="penaltyFee" label="逾期罚息" inline-template>
                                <div>{{ row.penaltyFee | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="lateFee" label="逾期服务费" inline-template>
                                <div>{{ row.lateFee | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="otherCharge" label="杂项" inline-template>
                                <el-popover
                                    trigger="hover"
                                    placement="top">
                                    <div>
                                        <template>
                                            <div>技术维护费：{{ row.maintenanceCharge | formatMoney }}</div>
                                            <div>其他费用：{{ row.otherCharge | formatMoney }}</div>
                                            <div>逾期违约金：{{ row.latePenalty | formatMoney }}</div>
                                            <div>逾期其他费用：{{ row.lateOtherCost | formatMoney }}</div>
                                        </template>
                                    </div>
                                    <span slot="reference">{{ row.sundryAmount | formatMoney }}</span>
                                </el-popover>
                            </el-table-column>

                            <el-table-column prop="amount" label="本次还款总额"inline-template>
                                <div>{{ row.amount | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="" label="校验状态">
                            </el-table-column>
                            <el-table-column prop="" label="核销状态">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>

                <div class="block" key="interface">
                    <h5 class="hd">交易流水明细</h5>
                    <div class="bd">
                        <el-table 
                            :data="[]"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column label="通道流水号" prop="bankTransactionNo">
                            </el-table-column>

                            <el-table-column label="流水金额" inline-template>
                                <div>{{ row.amount | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="accountSide" label="借贷标记">
                            </el-table-column>

                            <el-table-column prop="batchUuid" label="通道请求号">
                            </el-table-column>

                            <el-table-column inline-template label="流水入账时间">
                                <div>{{ row.completeTime }}</div>
                            </el-table-column>

                            <el-table-column prop="comment" label="备注">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>

               <!--  <div class="block">
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

                <div class="block">
                    <SystemOperateLog 
                        ref="sysLog"
                        :for-object-uuid="ss"></SystemOperateLog>
                </div> -->
            </div>

            <HistoryVersionModal 
                v-model="historyVersionModal.show"
                :historyData="historyVersionModal.data"
                :tradeUuid="tradeUuid"></HistoryVersionModal>
        </div>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import HistoryVersionModal from '../include/HistoryVersionModal'

    export default{
        components:{
            SystemOperateLog,
            HistoryVersionModal
        },
        data: function() {
            return {
                fetching: false,
                showModel: {},
                historyVersionModal: {
                    show: false,
                    data: {
                        historyDetail: {},
                        versionNameList: [],
                        voucher: {}
                    }
                },
                tradeUuid: '',
            }
        },
        activated: function() {
            this.fetch(this.$route.params.id);
        },
        methods: {
            fetch: function(voucherUuid) {
                this.fetching = true;
                ajaxPromise({
                    url: `/voucher/thirdPartyPayApi/detail/${voucherUuid}`
                })
                .then(data => {
                    this.showModel = data.showModel;
                    this.tradeUuid = data.showModel.tradeUuid;
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
                        handler: ()=>{
                            MessageBox.close();

                            ajaxPromise({
                                url: '/voucher/thirdPartyPayApi/del',
                                data: {
                                    tradeUuid : this.tradeUuid
                                }
                            }).then(data => {
                                MessageBox.open('作废成功');
                                this.$router.push('/capital/voucher/third-pay');
                            }).catch(message => {
                                MessageBox.open(message);
                            });
                        }
                    },
                    {
                        text:'取消',
                        handler: ()=>{
                            MessageBox.close();
                        }
                    }
                ]);
            },
            clickRecharge: function(){
                MessageBox.open('是否确定重新核销此凭证？', '提示',[
                {
                    text: '确定',
                    type: 'success',
                    handler: ()=>{
                        MessageBox.close();

                        ajaxPromise({
                            url: '/voucher/thirdPartyPayApi/reUpload',
                            data: {
                                tradeUuid: this.tradeUuid
                            }
                        }).then(data => {
                            MessageBox.open('操作成功，当前核销处理中，请稍后。');
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                },{
                    text: '取消',
                    handler: ()=>{
                        MessageBox.close();
                    }
                }])
            },
            clickHistory: function(){
                ajaxPromise({
                    url: `/voucher/thirdPartyPayApi/openRepaymentDetailHistory`,
                    data: {
                        tradeUuid: this.tradeUuid
                    }
                }).then(data => {
                    if($.isEmptyObject(data))
                        throw ['没有历史版本信息'];
                    this.historyVersionModal.data = Object.assign({}, data);
                    this.historyVersionModal.show = true;
                })
                .catch(message => {
                    MessageBox.open(message);
                });
            }
        }
    }
</script>
