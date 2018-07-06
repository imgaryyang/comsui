<style lang="sass">
    
</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{title: '放款单详情'} ]">
                <el-button v-if="false" size="small" type="primary">修改备注</el-button>
                <el-button v-if="false" size="small" type="primary">通道打款</el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
            	<div class="top">
            		<div class="block">
            			<h5 class="hd">贷款信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>贷款合同编号：
                                    <a :href="`${ctx}#/data/contracts/detail?uid=${remittancePlan.contractUniqueId}`">
                                        {{remittancePlan.contractNo}}
                                    </a>
                                </p>
                                <p>计划单号：
                                    <a :href="`${ctx}#/data/remittance/application/${remittancePlan.remittanceApplicationUuid}/detail`">
                                        {{remittancePlan.remittanceApplicationUuid}}
                                    </a>
                                </p>
                                <p>审核人：{{ remittanceApplication.auditorName }}</p>
                                <p>审核时间：{{ remittanceApplication.auditTime | formatDate }}</p>
                                <p>商户明细号：{{ remittancePlan.businessRecordNo }}</p>
            				</div>
            			</div>
            		</div>
            		<div class="block">
            			<h5 class="hd">放款单信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>放款编号：{{ remittancePlan.remittancePlanUuid }}</p>
                                <p>计划执行金额：{{ remittancePlan.plannedTotalAmount | formatMoney }}</p>
                                <p>计划执行日期：{{ remittancePlan.plannedPaymentDate | formatDate }}</p>
                                <p>创建时间：{{ remittancePlan.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>状态变更时间：{{ remittancePlan.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>执行状态：{{ remittancePlan.executionStatusMsg }}</p>
                                <p>备注：{{ remittancePlan.executionRemark }}</p>
                                <p>放款类型：{{ remittancePlan.transactionType == 'CREDIT' ? '代付' : '代收' }}</p>
                            </div>
                        </div>
            		</div>
            		<div class="block">
            			<h5 class="hd">付款方账户信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>付款方账户名：{{ remittancePlan.pgAccountInfo.accountName }}</p>
                                <p>付款方账户号：{{ remittancePlan.pgAccountInfo.accountNo }}</p>
                                <p>账户开户行：{{ remittancePlan.pgAccountInfo.bankName }}</p>
                                <p>开户行所在地：{{ remittancePlan.pgAccountInfo.province }} &nbsp;{{ remittancePlan.pgAccountInfo.city }}</p>
                                <p>银行编号：{{ remittancePlan.pgAccountInfo.bankCode }}</p>
            				</div>
            			</div>
            		</div>
                    <div class="block">
                        <h5 class="hd">收款方账户信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>收款方账户名：{{ remittancePlan.cpAccountInfo.accountName }}</p>
                                <p>收款方账户号：{{ remittancePlan.cpAccountInfo.accountNo }}</p>
                                <p>账户开户行：{{ remittancePlan.cpAccountInfo.bankName }}</p>
                                <p>开户行所在地：{{ remittancePlan.cpAccountInfo.province }} &nbsp;{{ remittancePlan.cpAccountInfo.city }}</p>
                                <p>银行编号：{{ remittancePlan.cpAccountInfo.bankCode }}</p>
                            </div>
                        </div>
                    </div>
            	</div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">
                        线上代付单列表
                        <template v-if="ifElementGranted('reexecute-remittance-plan') && reRemittanceFlag">
                            <div class="pull-right">
                                <el-button size="small" type="primary" class="btn btn-primary" @click="reExecModal.visible = true">重新执行</el-button>
                            </div>
                        </template>
                    </h5>
                    <PagingTable
                        :data="remittancePlanExecLogs"
                        :pagination="true">
                        <el-table-column 
                            inline-template 
                            prop="execReqNo" 
                            label="代付单号">
                            <a :href="`${ctx}#/capital/remittance-cash-flow/plan-execlog/${row.execReqNo}/detail`">{{ row.execReqNo }}</a>
                        </el-table-column>
                        <el-table-column 
                            inline-template 
                            label="付款方账户名">
                            <el-popover
                                v-if="row.pgAccountInfo.accountName"
                                trigger="hover"
                                placement="top">
                                <div>
                                    <div>账户名：{{ row.pgAccountInfo.accountName }}</div>
                                    <div>账户号：{{ row.pgAccountInfo.accountNo }}</div>
                                    <div>开户行：{{ row.pgAccountInfo.bankName }}</div>
                                    <div>所在地：{{ row.pgAccountInfo.province }}&nbsp;{{ row.pgAccountInfo.city }}</div>
                                    <div>证件号：{{ row.pgAccountInfo.desensitizationIdNumber }}</div>
                                </div>
                                <span slot="reference">{{ row.pgAccountInfo.accountName }}</span>
                            </el-popover>
                        </el-table-column>
                        <el-table-column 
                            inline-template 
                            label="收款方账户名">
                            <el-popover
                                v-if="row.cpAccountInfo.accountName"
                                trigger="hover"
                                placement="top">
                                <div>
                                    <div>账户名：{{ row.cpAccountInfo.accountName }}</div>
                                    <div>账户号：{{ row.cpAccountInfo.accountNo }}</div>
                                    <div>开户行：{{ row.cpAccountInfo.bankName }}</div>
                                    <div>所在地：{{ row.cpAccountInfo.province }}&nbsp;{{ row.cpAccountInfo.city }}</div>
                                    <div>证件号：{{ row.cpAccountInfo.desensitizationIdNumber }}</div>
                                </div>
                                <span slot="reference">{{ row.cpAccountInfo.accountName }}</span>
                            </el-popover>
                        </el-table-column>
                        <el-table-column inline-template label="发生金额">
                            <div>{{ row.plannedAmount | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column prop="paymentGatewayMessage" label="执行通道"></el-table-column>
                        <el-table-column inline-template label="状态变更时间">
                            <div>{{ row.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                        </el-table-column>
                        <el-table-column inline-template label="执行状态">
                            <span :class="{
                                    'color-danger': row.executionStatus == 'FAIL',
                                    'color-warning': row.executionStatus == 'ABNORMAL'
                                }">
                                {{ row.executionStatusMessage }}
                            </span>
                        </el-table-column>
                        <el-table-column prop="reverseStatusMessage" label="冲账状态"></el-table-column>
                        <el-table-column prop="executionRemark" label="备注"></el-table-column>
                    </PagingTable>
                </div>

                <div class="block">
                    <SystemOperateLog 
                        ref="sysLog"
                        :for-object-uuid="$route.params.id"></SystemOperateLog>
                </div>
            </div>
        </div>

        <ReExecModal 
            v-model="reExecModal.visible"
            :planned-payment-date="remittancePlan.plannedPaymentDate"
            :remittance-plan-uuid="$route.params.id"></ReExecModal>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import formats from 'filters/format';
    import PagingTable from 'views/include/PagingTable'

    export default{
        components: {
            SystemOperateLog: require('views/include/SystemOperateLog'),
            ReExecModal: require('./ReExecModal'),
            PagingTable,
        },  
        data: function() {
            return {
                fetching: false,
                reExecModal: {
                    visible: false
                },
                remittancePlan: {
                    pgAccountInfo: {},
                    cpAccountInfo: {}
                },
                remittanceApplication: {},
                remittancePlanExecLogs: []
            }
        },
        computed: {
            reRemittanceFlag: function(){
                if(this.remittancePlanExecLogs.length == 0){
                    return false
                }
                var planStatus = this.remittancePlan.executionStatus,//放款单
                    applicationStatus = this.remittanceApplication.executionStatus,//计划订单
                    planExecLogExec = this.remittancePlanExecLogs[0].executionStatus,//代付状态
                    planExecLogRever = this.remittancePlanExecLogs[0].reverseStatus;//代付冲账状态

                return planStatus == 'FAIL' && ((applicationStatus == 'ABNORMAL' && (planExecLogExec == 'FAIL' || planExecLogExec == 'SUCCESS' && planExecLogRever == 'REFUND')) || (applicationStatus == 'FAIL' && planExecLogExec == 'SUCCESS' && planExecLogRever == 'REFUND'))
            }
        },
        activated: function() {
            this.fetch(this.$route.params.id);
        },
        methods: {
            fetch: function(remittancePlanUuid) {
                this.fetching = true;

                ajaxPromise({
                    url: `/remittance/plan/details/${remittancePlanUuid}`
                })
                .then(data => {
                    this.remittancePlan = data.remittancePlan;
                    this.remittanceApplication = data.remittanceApplication;
                    this.remittancePlanExecLogs = data.remittancePlanExecLogs;
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
