<style>
    
</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '提现详情'}]">
                <el-button size="small" type="primary" @click="modifyComment">修改备注</el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">预付单信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>滞留单号 ：{{detailModel.temDepositDocNo}}</p>
                                <p>滞留金额 ：{{detailModel.temDepositTotalAmount | formatMoney}}</p>
                                <p>已用金额 ：{{detailModel.temDepositUsedAmount | formatMoney }}</p>
                                <p>创建时间 ：{{detailModel.temDepositCreateTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>使用状态 ：{{detailModel.tmpDepositUseStatus}}</p>
                                <p>最近使用时间 ：{{detailModel.tmpDepositLastModifyTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>备注 ：{{detailModel.remark}}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">客户信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>信托项目名称 ：{{detailModel.finacialContractName}}</p>
                                <p>贷款合同编号 ：<a :href="`${ctx}#/data/contracts/detail?id=${detailModel.contractId}`">{{detailModel.contractNo}}</a></p>
                                <p>账户编号 ：<a :href="`${ctx}#/capital/account/virtual-acctount/${detailModel.virtualAccountUuid}/detail`">{{detailModel.customerNo}}</a></p>
                                <p>账户名称 ：{{detailModel.customerName}}</p>
                                <p>客户类型 ：{{detailModel.customerType}}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">付款方信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>账户姓名 ：{{detailModel.payCustomerName}}</p>
                                <p>银行账户号 ：{{detailModel.bankNo}}</p>
                                <p>开户行 ：{{detailModel.bankName}}</p>
                                <p>开户行所在地 ：{{detailModel.bankLocal}}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">
                        资金来源
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="[detailModel]"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="voucherNo" label="凭证编号" inline-template>
                                <a href="#" @click.prevent="getVoucherDetail()">{{row.voucherNo}}</a>
                            </el-table-column>
                            <el-table-column prop="receivableAccountNo" label="专户账号">
                            </el-table-column>
                            <el-table-column prop="voucherCustomerName" label="账户姓名">
                            </el-table-column>
                            <el-table-column prop="paymentAccountNo" label="机构账户号">
                            </el-table-column>
                            <el-table-column prop="voucherAmount" label="凭证金额" inline-template>
                                <div>{{ row.voucherAmount | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="secondType" label="凭证类型">
                            </el-table-column>
                            <el-table-column prop="firstType" label="凭证来源">
                            </el-table-column>
                            <el-table-column prop="voucherLastModifyTime" label="状态变更时间" inline-template>
                                <div>{{ row.voucherLastModifyTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                            </el-table-column>
                            <el-table-column prop="cashFlowTime" label="流水入账时间" inline-template>
                                <div>{{ row.cashFlowTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                            </el-table-column>
                            <el-table-column prop="voucherStatus" label="凭证状态">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="block">
                    <h5 class="hd">
                        还款计划
                        <span style="font-weight: normal;">还款总金额: {{remittanceTotalAmount}}</span>
                        <el-button type="text" class="pull-right" style="padding: 0 10px; font-weight: normal;line-height:28px;" @click="remittanceModalShow=true">还款</el-button>
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="repayShowModel"
                            v-loading="repayModelFetching"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="repaymentPlanNo" label="还款编号">
                            </el-table-column>
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
                            <el-table-column prop="maintenanceCharge" label="技术维护费" inline-template>
                                <div>{{ row.maintenanceCharge | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="otherCharge" label="其他费用" inline-template>
                                <div>{{ row.otherCharge | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="penaltyFee" label="逾期罚息" inline-template>
                                <div>{{ row.penaltyFee | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="latePenalty" label="逾期违约金" inline-template>
                                <div>{{ row.latePenalty | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="lateFee" label="逾期服务费" inline-template>
                                <div>{{ row.lateFee | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="lateOtherCost" label="逾期其他费用" inline-template>
                                <div>{{ row.lateOtherCost | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="checkState" label="校验状态"></el-table-column>
                            <el-table-column prop="status" label="核销状态"></el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="block">
                    <h5 class="hd">
                        回购单
                        <span style="font-weight: normal;">回购总金额: {{repurchaseTotalAmount | formatMoney }} </span>
                        <el-button type="text" class="pull-right" style="padding: 0 10px; font-weight: normal;line-height:28px;" @click="repurchaseModalShow=true">回购</el-button>
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="repuchaseShowModel"
                            v-loading="repurcharseModelFetching"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="repurchaseDocUuid" label="回购单号">
                            </el-table-column>
                            <el-table-column prop="repoStartDate" label="回购起始日" inline-template>
                                <div>{{ row.repoStartDate | formatDate }}</div>
                            </el-table-column>
                            <el-table-column prop="repoEndDate" label="回购截止日" inline-template>
                                <div>{{ row.repoEndDate | formatDate }}</div>
                            </el-table-column>
                            <el-table-column prop="appName" label="商户名称">
                            </el-table-column>
                            <el-table-column prop="repurchasePrincipal" label="回购本金" inline-template>
                                <div>{{ row.repurchasePrincipal | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="repurchaseInterest" label="回购利息" inline-template>
                                <div>{{ row.repurchaseInterest | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="repurchasePenalty" label="回购罚息" inline-template>
                                <div>{{ row.repurchasePenalty | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="repurchaseOtherFee" label="回购其他费用" inline-template>
                                <div>{{ row.repurchaseOtherFee | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="repoDays" label="回购天数">
                            </el-table-column>
                            <el-table-column prop="creatTime" label="状态变更时间" inline-template>
                                <div>{{ row.creatTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                            </el-table-column>
                            <el-table-column prop="repoStatus" label="回购状态">
                            </el-table-column>
                            <el-table-column prop="sourceDocumentDetailCheckState" label="校验状态"></el-table-column>
                            <el-table-column prop="sourceDocumentDetailStatus" label="核销状态"></el-table-column>
                        </el-table>
                    </div>
                </div>
<!--                 <div class="block">
                    <h5 class="hd">
                        提现单
                        <span>提现总金额： </span>
                        <el-button type="text" class="pull-right" style="padding: 0 10px; font-weight: normal;">申请提现</el-button>
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="[]"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="" label="提现序号">
                            </el-table-column>
                            <el-table-column prop="" label="提现金额">
                            </el-table-column>
                            <el-table-column prop="" label="创建时间">
                            </el-table-column>
                            <el-table-column prop="" label="备注">
                            </el-table-column>
                        </el-table>
                    </div>
                </div> -->

                <div class="block">
                    <SystemOperateLog :for-object-uuid="$route.params.no"></SystemOperateLog>
                </div>
            </div>
        </div>
        <ModifyCommentModal
            v-model="commentModal.show"
            :model="commentModal.model"
            @submit="onSubmitModifyComment">
        </ModifyCommentModal>

        <RemittanceModal
            :residualAmount="residualAmount"
            v-model="remittanceModalShow"
            @submit="handleSubmitRemittance"></RemittanceModal>
        <RepurchaseModal
            :residualAmount="residualAmount"
            v-model="repurchaseModalShow"
            @submit="handleSubmitRepurchase"></RepurchaseModal>
        <!-- <WithdrawModal></WithdrawModal> -->
        
    </div>
</template>

<script>
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import MessageBox from 'components/MessageBox';
    import ModifyCommentModal from '../../voucher/active/include/ModifyCommentModal';
    import RemittanceModal from './include/remittanceModal';
    import RepurchaseModal from './include/repurchaseModal';
    import WithdrawModal from './include/withdrawModal';
    import Decimal from 'decimal.js';
    import { ajaxPromise } from 'assets/javascripts/util';

    export default {
        components: {
            SystemOperateLog,
            ModifyCommentModal,
            RemittanceModal,
            RepurchaseModal,
            WithdrawModal
        },
        data: function() {
            return {
                fetching: false,
                repurcharseModelFetching: false,
                repayModelFetching: false,
                detailModel: {},
                repuchaseShowModel: [],
                repayShowModel: [],
                financialContractQueryModels: [],
                commentModal: {
                    show: false,
                    model: {
                        comment: ''
                    },
                },
                residualAmount: 0,
                remittanceModalShow: false,
                repurchaseModalShow: false,
            }
        },
        computed: {
            repurchaseTotalAmount: function(){
                var sum = Decimal(0)
                this.repuchaseShowModel.filter(item => item.sourceDocumentDetailStatus == '已核销').forEach((item, i , arr) =>{
                    sum = sum.add(item.repurchasePrincipal).add(item.repurchaseInterest).add(item.repurchasePenalty).add(item.repurchaseOtherFee)
                })
                return sum.toNumber()
            },
            remittanceTotalAmount: function(){
                var sum = Decimal(0)
                this.repayShowModel.filter(item => item.status == '已核销').forEach((item, i , arr) =>{
                    sum = sum.add(item.principal).add(item.interest).add(item.serviceCharge).add(item.maintenanceCharge).add(item.otherCharge).add(item.penaltyFee).add(item.latePenalty).add(item.lateFee).add(item.lateOtherCost)
                })
                return sum.toNumber()
            },
        },
        activated: function() {
            this.fetchDetailModel(this.$route.params.no)
            this.fetchRepurcharseModel(this.$route.params.no)
            this.fetchRepayModel(this.$route.params.no)
        },
        methods: {
            fetchDetailModel: function(no){
                this.fetching = true;
                ajaxPromise({
                    url: `/temporary-deposit-doc/query-tem-deporary-doc-information`,
                    data: {
                        temDepositDocUuid: no
                    },
                    type: 'POST'
                }).then(data =>{
                    this.detailModel = Object.assign({}, data.temDepositDocShowModel)
                    this.residualAmount = Decimal.sub(data.temDepositDocShowModel.temDepositTotalAmount, data.temDepositDocShowModel.temDepositUsedAmount).toNumber()
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                })
            },
            fetchRepurcharseModel: function(no){
                this.repurcharseModelFetching = true;
                ajaxPromise({
                    url: `/temporary-deposit-doc/query-repurchase-doc-information`,
                    data: {
                        temDepositDocUuid: no
                    },
                    type: 'POST'
                }).then(data =>{
                    this.repuchaseShowModel = data.repuchaseShowModel.map(item =>{
                        return Object.assign({}, item,item.sourceDocumentChargeDetail)
                    })
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.repurcharseModelFetching = false;
                })
            },
            fetchRepayModel: function(no){
                this.repayModelFetching = true;
                ajaxPromise({
                    url: `/temporary-deposit-doc/query-repayment-plan-information`,
                    data: {
                        temDepositDocUuid: no
                    },
                    type: 'POST'
                }).then(data =>{
                    this.repayShowModel = data.repayRecordModelList
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.repayModelFetching = false;
                })
            },
            modifyComment: function(){
                var { commentModal, detailModel } = this;

                commentModal.show = true;
                commentModal.model = {
                    comment: detailModel.comment
                }
            },
            onSubmitModifyComment: function(currentModel) {
                ajaxPromise({
                    url: `temporary-deposit-doc/update-temporary-deposit-doc-remark`,
                    data: {
                        remark: currentModel.comment,
                        temporaryDepositDocUuid: this.$route.params.no
                    },
                    type: 'post',
                }).then(data => {
                    MessageBox.open('修改成功');
                    var { detailModel, commentModal } = this;

                    detailModel.remark = currentModel.comment;
                    commentModal.show = false;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleSubmitRemittance: function(model){
                ajaxPromise({
                    url: `temporary-deposit-doc/post-repayment_order`,
                    data: {
                        repaymentBusinessType: 0,
                        repaymentModelListJson: JSON.stringify(model),
                        temDepositDocUuid: this.$route.params.no
                        },
                    type: 'post'
                }).then(data => {
                    MessageBox.open('修改成功');
                    this.remittanceModalShow = false
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleSubmitRepurchase: function(model){
                ajaxPromise({
                    url: `temporary-deposit-doc/post-repayment_order`,
                    data: {
                        repaymentBusinessType: 1,
                        repaymentModelListJson: JSON.stringify(model),
                        temDepositDocUuid: this.$route.params.no
                   		 },
                    type: 'post'
                }).then(data => {
                    MessageBox.open('修改成功');
                    this.repurchaseModalShow = false
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            getVoucherDetail: function(){
                if(this.detailModel.firstType == "主动付款凭证"){
                    this.$router.push(`/capital/voucher/active/${this.detailModel.voucherNo}/detail`)
                }else if(this.detailModel.firstType == "商户付款凭证"){
                    this.$router.push(`/capital/voucher/business/${this.detailModel.voucherId}/detail`)
                }else{
                    MessageBox.open('未知的凭证来源');
                }
            }
        }
    }
</script>