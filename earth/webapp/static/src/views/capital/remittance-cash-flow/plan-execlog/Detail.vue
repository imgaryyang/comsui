<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '线上代付单详情'}]">
                <el-button size="small" type="primary" @click="revouching" v-if="ifElementGranted('plan-execlog-check-execution-status')">重新核单</el-button>
                <el-button size="small" type="primary" @click="reRemittance.visible = true" v-if="ifElementGranted('plan-execlog-re-remittance') && reRemittanceFlag">重新放款</el-button>
<!--                 <el-button size="small" type="primary" v-if="ifElementGranted('plan-execlog-add-cash-flow') && remittancePlanExecLog.reverseStatus =='REFUND'"
                @click="showCashFlowModal">添加流水</el-button> -->
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd" v-if="execlogTypeText == '放款'">凭证信息</h5>
                        <h5 class="hd" v-if="execlogTypeText == '转账'">贷款信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p v-if="execlogTypeText == '转账'">转账单号：{{remittanceApplication.requestNo}}</p>
                                <p v-if="execlogTypeText == '放款'">放款编号：<a :href="`${ctx}#/data/remittance/plan/${remittancePlanExecLog.remittancePlanUuid}/detail`">{{remittancePlanExecLog.remittancePlanUuid}}</a></p>
                                <p>代付单号：{{remittancePlanExecLog.execReqNo }}</p>
                                <p>发生金额：{{ remittancePlanExecLog.plannedAmount | formatMoney }}</p>
                                <p>业务类别: {{execlogTypeText}}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">付款方账户信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>付款方账户名：{{ remittancePlanExecLog.pgAccountInfo.accountName }}</p>
                                <p>付款方账户号：{{ remittancePlanExecLog.pgAccountInfo.accountNo }}</p>
                                <p>账户开户行：{{ remittancePlanExecLog.pgAccountInfo.bankName }}</p>
                                <p>开户行所在地：{{ remittancePlanExecLog.pgAccountInfo.province }}&nbsp;{{ remittancePlanExecLog.pgAccountInfo.city }}</p>
                                <p>银行编号：{{ remittancePlanExecLog.pgAccountInfo.bankCode }}</p>
                                <p>证件号：{{ remittancePlanExecLog.pgAccountInfo.desensitizationIdNumber }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">收款方账户信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>收款方账户名：{{remittancePlanExecLog.cpAccountInfo.accountName}}</p>
                                <p>收款方账户号：{{remittancePlanExecLog.cpAccountInfo.accountNo}}</p>
                                <p>账户开户行：{{remittancePlanExecLog.cpAccountInfo.bankName}}</p>
                                <p>开户行所在地：{{remittancePlanExecLog.cpAccountInfo.province}}&nbsp;{{remittancePlanExecLog.cpAccountInfo.city}}</p>
                                <p>银行编号：{{remittancePlanExecLog.cpAccountInfo.bankCode}}</p>
                                <p>证件号：{{remittancePlanExecLog.cpAccountInfo.desensitizationIdNumber}}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">执行信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>创建时间：{{ remittancePlanExecLog.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>状态变更时间：{{ remittancePlanExecLog.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>执行时差：{{remittancePlanExecLog.executedTime}}</p>
                                <p>代付状态：{{remittancePlanExecLog.executionStatusMessage}}</p>
                                <p>
                                    冲账状态：<span class="color-danger">{{ remittancePlanExecLog.reverseStatusMessage }}</span>
                                    <a href="javascript: void 0" @click.prevent="modifyReverseStatus" v-if="ifElementGranted('plan-execlog-modify-reverse-status') && showButtonReverseStatus.includes(remittancePlanExecLog.reverseStatus)">修改</a>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">通道信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>放款通道：{{remittancePlanExecLog.paymentChannelName}}</p>
                                <p>通道编号：{{remittancePlanExecLog.paymentChannelUuid}}</p>
                                <p>通道请求号：{{remittancePlanExecLog.execRspNo}}</p>
                                <p>通道流水号：{{remittancePlanExecLog.transactionSerialNo}}</p>
                                <p>备注：{{remittancePlanExecLog.executionRemark}}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">代付撤销单</h5>
                    <div class="bd">
                        <PagingTable :data="remittanceRefundBills">
                            <el-table-column label="通道流水号" prop="channelCashFlowNo"></el-table-column>
                            <el-table-column label="通道名称" prop="paymentGatewayMsg"></el-table-column>
                            <el-table-column label="发生时间" prop="createTime" inline-template>
                                <div>{{ row.createTime | formatDate }}</div>
                            </el-table-column>
                            <el-table-column label="退回账户" prop="hostAccountNo"></el-table-column>
                            <el-table-column label="交易类型" inline-template>
                                <div>{{ row.reverseType == "REFUND" ? "退票" : "冲账"}}</div>
                            </el-table-column>
                            <el-table-column label="金额" prop="amount" inline-template>
                                <div>{{ row.amount | formatMoney }}</div>
                            </el-table-column>
                        </PagingTable>
                    </div>
                </div>
                <div class="block">
                    <SystemOperateLog
                        ref="sysLog" 
                        :for-object-uuid="remittancePlanExecLog.execReqNo">
                    </SystemOperateLog>
                </div>
            </div>
        </div>

        <ModifyReverseStatusModal
            v-model="reverseStatusModal.show"
            :model="reverseStatusModal.model"
            @submit="freshPage"
            @showAddFlow="showCashFlowModal">
        </ModifyReverseStatusModal>

        <AddCashFlowModal
            v-model="addCashFlowModal.show"
            :model="addCashFlowModal.model"
            @submit="freshPage">
        </AddCashFlowModal>

        <ReRemittanceModal 
            v-model="reRemittance.visible"
            :remittance-plan-uuid="reRemittance.id">
        </ReRemittanceModal>
    </div>
</template>

<script>
    import { ctx } from 'src/config';
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import ModifyReverseStatusModal from './include/ModifyReverseStatusModal';
    import AddCashFlowModal from './include/AddCashFlowModal';
    import ReRemittanceModal from './include/ReRemittanceModal';

    export default{
        components:{
            SystemOperateLog: require('views/include/SystemOperateLog'),
            PagingTable: require('views/include/PagingTable'),
            ReRemittanceModal,ModifyReverseStatusModal,AddCashFlowModal
        },
        activated: function() {
            this.fetch();
        },
        data: function() {
            return {
                fetching: false,
                remittancePlanExecLog: {
                    pgAccountInfo: {},
                    cpAccountInfo: {}
                },
                reRemittance: {
                    visible: false,
                    id:''
                },
                remittanceRefundBills: [],
                reverseStatusModal: {
                    show: false,
                    model: {
                        execReqNo: '',
                        reverseStatus: '',
                        executionRemark : ''
                    }
                },
                showButtonReverseStatus:['UNOCCUR','NOTREVERSE'],
                addCashFlowModal: {
                    show: false,
                    model: {
                        plannedAmount: ''
                    }
                },
                remittancePlan: {},
                remittanceApplication: {},

                execlogTypeText: ''
            }
        },
        computed: {
            reRemittanceFlag: function(){
                var planStatus = this.remittancePlan.executionStatus,//放款单
                    applicationStatus = this.remittanceApplication.executionStatus,//计划订单
                    planExecLogExec = this.remittancePlanExecLog.executionStatus,//代付状态
                    planExecLogRever = this.remittancePlanExecLog.reverseStatus;//代付冲账状态

                return planStatus == 'FAIL' && ((applicationStatus == 'ABNORMAL' && (planExecLogExec == 'FAIL' || planExecLogExec == 'SUCCESS' && planExecLogRever == 'REFUND')) || (applicationStatus == 'FAIL' && planExecLogExec == 'SUCCESS' && planExecLogRever == 'REFUND'))
            }
        },
        methods: {
            fetch: function() {
                this.fetching = true;
                ajaxPromise({
                    url: `/capital/plan/execlog/details/${this.$route.params.id}`
                }).then(data => {
                    this.remittancePlanExecLog = Object.assign({
                        pgAccountInfo: {},
                        cpAccountInfo: {}
                    }, data.remittancePlanExecLog);
                    this.execlogTypeText = this.remittancePlanExecLog.transferTransactionType ? '转账' : '放款'
                    this.reRemittance.id = data.remittancePlanExecLog.remittancePlanUuid;
                    this.remittanceRefundBills = data.remittanceRefundBills || [];
                    this.remittancePlan = Object.assign({}, data.remittancePlan);
                    this.remittanceApplication = Object.assign({}, data.remittanceApplication);
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            },
            modifyReverseStatus: function() {
                var {execReqNo,reverseStatus} = this.remittancePlanExecLog;
                this.reverseStatusModal = {
                    show: true,
                    model: {
                        execReqNo: execReqNo,
                        reverseStatus: reverseStatus,
                        executionRemark: '',
                    }
                }
            },
            showCashFlowModal: function(model){
                this.addCashFlowModal.model.plannedAmount = this.remittancePlanExecLog.plannedAmount;
                this.addCashFlowModal.model.execReqNo = this.remittancePlanExecLog.execReqNo;
                this.addCashFlowModal.model.modifyReverseStatusModel = model;
                this.addCashFlowModal.show = true;
            },
            revouching: function() {
                MessageBox.open('确认重新核单？', '提示', [{
                    text: '取消',
                    handler: () => {
                        MessageBox.close();
                    }
                },{
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: `/capital/plan/execlog/checkExecutionStatus/${this.remittancePlanExecLog.execReqNo}`,
                            type: 'post'
                        }).then(data => {
                            MessageBox.open('核单成功');
                            setTimeout(()=>{
                                MessageBox.close();
                            },500)
                        }).catch(message => {
                          MessageBox.open(message);
                        });
                    }
                }]);
            },
            freshPage: function(model){
                var {execReqNo, executionRemark, reverseStatus} = model;
                if(execReqNo === this.remittancePlanExecLog.execReqNo){
                    this.remittancePlanExecLog.executionRemark = executionRemark;
                    this.remittancePlanExecLog.reverseStatus = '';
                    if(reverseStatus === 3){
                        this.remittancePlanExecLog.reverseStatusMessage = '已退票';
                        this.remittancePlanExecLog.reverseStatus = 'REFUND';
                    }else if(reverseStatus === 2){
                        this.remittancePlanExecLog.reverseStatusMessage = '已冲账';
                        this.remittancePlanExecLog.reverseStatus = 'REVERSED';
                    }
                }else{
                    this.fetch()
                }
            }
        }
    }
</script>