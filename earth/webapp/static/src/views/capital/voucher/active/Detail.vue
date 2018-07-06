<style lang="sass">

</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{title: '主动付款凭证详情'}]">
                <el-button
                    size="small"
                    class="primary"
                    v-if="detail.voucherStatus == '未核销'"
                    @click="reWrite">
                    重新核销
                </el-button>
                <el-button
                    size="small"
                    class="primary"
                    v-if="ifElementGranted('modify-active-voucher-comment') && detail.voucherStatus != '作废'"
                    @click="modifyComment">
                    修改备注
                </el-button>
            	<el-button
                    size="small"
                    class="primary"
                    v-if="ifElementGranted('modify-active-voucher') && detail.voucherStatus === '未提交'"
                    @click="editVoucher($route.params.id)">
                    编辑
                </el-button>
            	<el-button
                    size="small"
                    type="danger"
                    v-if="detail.voucherStatus == '未核销'"
                    @click="invalidVoucher(voucherId)">
                    作废
                </el-button>
            </Breadcrumb>
            <div class="col-layout-detail">
            	<div class="top">
					<div class="block">
						<h5 class="hd">凭证信息</h5>
						<div class="bd">
							<div class="col">
								<p>凭证编号 ：{{ detail.voucherNo}}</p>
								<p>凭证来源 ：银行流水 </p>
								<p>凭证类型 ：{{ detail.voucherTypeMessage }}</p>
								<p>原始凭证 ：<a :href="`${api}/voucher/active/detail/resource/${voucherId}`" v-if="ifElementGranted('download-original-voucher') && detail.haveFile">下载</a></p>
							</div>
							<div class="col">
								<p>{{ $utils.locale('financialContract') }} ：{{ detail.financialContractName }}</p>
								<p>专户账号 ：{{ detail.receivableAccountNo }}</p>
								<p>贷款合同编号 ：{{ detail.contractNo }}</p>
								<p>客户名称 ：{{ detail.customerName }}</p>
								<p>流水入账时间 ：{{ detail.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
							</div>
							<div class="col">
								<p>创建时间 ：{{ detail.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
								<p>凭证状态 ：{{ detail.voucherStatus}}</p>
								<p>状态变更时间 ：{{ detail.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
								<p>备注 ：{{ detail.comment}}</p>
							</div>
						</div>
					</div>
            	</div>
            </div>
            <div class="row-layout-detail">
            	<div class="block">
            		<h5 class="hd">
                        凭证资金流水(流水总金额: {{ voucherAmount | formatMoney }})
                        <el-button
                            size="small"
                            class="default pull-right"
                            @click="cashFlowSelect(voucherId)"
                            v-if="ifElementGranted('select-active-voucher-cashflow') && !detail.cashFlow">
                            流水选择
                        </el-button>
                    </h5>
            		<div class="bd">
            			<el-table
            				:data="cashFlowList"
            				class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="bankSequenceNo" label="流水号"></el-table-column>
                                <el-table-column inline-template label="交易金额">
                                    <div> {{ row.transactionAmount | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column prop="counterAccountName" label="对方户名"></el-table-column>
                                <el-table-column prop="counterAccountNo" label="对方账号"></el-table-column>
                                <el-table-column prop="counterBankName" label="对方开户行"></el-table-column>
                                <el-table-column inline-template label="入账时间">
                                    <div>
                                        {{ row.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss') }}
                                    </div>
                                </el-table-column>
                                <el-table-column prop="remark" label="摘要"></el-table-column>
            			</el-table>
            		</div>
            	</div>

            	<div class="block">
            		<h5 class="hd">凭证业务明细(业务总金额: {{ repaymentPlanAmount | formatMoney }})</h5>
            		<div class="bd">
            			<el-table
            				:data="detail.assetInfoModels"
            				class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column inline-template label="还款单号">
                                <a :href="`${ctx}#/finance/assets/${row.assetSetUuid}/detail`">{{ row.singleLoanContractNo }}</a>
                            </el-table-column>
                            <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo">
                            </el-table-column>
                            <el-table-column prop="assetRecycleDate" label="计划还款日期" inline-template>
                                <div>{{ row.assetRecycleDate | formatDate}}</div>
                            </el-table-column>
                            <el-table-column prop="principalValue" label="还款本金" inline-template>
                                <div>
                                    {{ row.principalValue | formatMoney }}
                                </div>
                            </el-table-column>
                            <el-table-column prop="interestValue" label="还款利息" inline-template>
                                <div>
                                    {{ row.interestValue | formatMoney }}
                                </div>
                            </el-table-column>
                            <el-table-column prop="serviceCharge" label="贷款服务费" inline-template>
                                <div>
                                    {{ row.serviceCharge | formatMoney }}
                                </div>
                            </el-table-column>
                            <el-table-column prop="maintenanceCharge" label="技术维护费" inline-template>
                                <div>
                                    {{ row.maintenanceCharge | formatMoney }}
                                </div>
                            </el-table-column>
                            <el-table-column prop="otherCharge" label="其他费用" inline-template>
                                <div>
                                    {{ row.otherCharge | formatMoney }}
                                </div>
                            </el-table-column>
                            <el-table-column prop="overduePenaltyFee" label="逾期罚息" inline-template>
                                <div>
                                    {{ row.overduePenaltyFee | formatMoney }}
                                </div>
                            </el-table-column>
                            <el-table-column prop="overdueObligationFee" label="逾期违约金" inline-template>
                                <div>
                                    {{ row.overdueObligationFee | formatMoney }}
                                </div>
                            </el-table-column>
                            <el-table-column prop="overdueServiceCharge" label="逾期服务费" inline-template>
                                <div>
                                    {{ row.overdueServiceCharge | formatMoney }}
                                </div>
                            </el-table-column>
                            <el-table-column prop="overdueOtherCharge" label="逾期其他费用" inline-template>
                                <div>
                                    {{ row.overdueOtherCharge | formatMoney }}
                                </div>
                            </el-table-column>
            			</el-table>
            		</div>
            	</div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="detail.voucherUuid">
                    </SystemOperateLog>
                </div>
            </div>
        </div>

        <ModifyCommentModal
            v-model="commentModal.show"
            :model="commentModal.model"
            @submit="onSubmitModifyComment">
        </ModifyCommentModal>

        <CashFlowSelectModal
            :voucherId="voucherId"
            :cashFlows="cashFlowSelectModal.cashFlows"
            v-model="cashFlowSelectModal.show">
        </CashFlowSelectModal>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import ModifyCommentModal from './include/ModifyCommentModal';

    export default{
    	components: {
    		SystemOperateLog,
            ModifyCommentModal,
            CashFlowSelectModal: require('../business/include/CashFlowSelectModal'),
    	},
        data: function() {
        	return {
        		fetching: false,
                voucherId: '',
                detail: {
                    cashFlow: {},
                    assetInfoModels: [],
                },
                commentModal: {
                    show: false,
                    title: '',
                    model: {
                        comment: ''
                    },
                },
                cashFlowSelectModal: {
                    cashFlows: [],
                    show: false,
                }
        	};
        },
        activated: function() {
            this.fetch(this.$route.params.id);
            this.$refs.sysLog.fetch();
        },
        computed: {
            voucherAmount: function() {
                return this.cashFlowList.length !=0 ? this.cashFlowList[0].transactionAmount : 0;
            },
            repaymentPlanAmount: function() {
                var { detail } = this;
                var totalAmount = 0;
                detail.assetInfoModels.forEach(item => {
                    var overdueAmount = isNaN(parseFloat(item.overdueAmount))? 0 : parseFloat(item.overdueAmount);
                    var principalValue = isNaN(parseFloat(item.principalValue)) ? 0 : parseFloat(item.principalValue);
                    var interestValue = isNaN(parseFloat(item.interestValue)) ? 0 : parseFloat(item.interestValue);
                    var serviceCharge = isNaN(parseFloat(item.serviceCharge)) ? 0 : parseFloat(item.serviceCharge);
                    var maintenanceCharge = isNaN(parseFloat(item.maintenanceCharge)) ? 0 : parseFloat(item.maintenanceCharge);
                    var otherCharge = isNaN(parseFloat(item.otherCharge)) ? 0 : parseFloat(item.otherCharge);
                    var overduePenaltyFee = isNaN(parseFloat(item.overduePenaltyFee)) ? 0 : parseFloat(item.overduePenaltyFee);
                    var overdueObligationFee = isNaN(parseFloat(item.overdueObligationFee)) ? 0 : parseFloat(item.overdueObligationFee);
                    var overdueServiceCharge = isNaN(parseFloat(item.overdueServiceCharge)) ? 0 : parseFloat(item.overdueServiceCharge);
                    var overdueOtherCharge = isNaN(parseFloat(item.overdueOtherCharge)) ? 0 : parseFloat(item.overdueOtherCharge);

                    var totalLine = overdueAmount + principalValue + interestValue + serviceCharge + maintenanceCharge + otherCharge + overduePenaltyFee + overdueObligationFee + overdueServiceCharge + overdueOtherCharge;
                    totalAmount += totalLine;
                });
                return totalAmount;
            },
            cashFlowList: function() {
               return this.detail.cashFlow == undefined ? [] : [this.detail.cashFlow];
            }
        },
        methods: {
            fetch: function(voucherNo) {
                this.fetching = true;

                return ajaxPromise({
                    url: `/voucher/active/detail/${voucherNo}/data`
                }).then(data => {
                    this.detail = data.detail;
                    this.voucherId = this.detail.voucherId;
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                })
            },
            modifyComment: function() {
                var { commentModal, detail } = this;

                commentModal.show = true;
                commentModal.title = "修改备注"
                commentModal.model = {
                    comment: detail.comment
                }
            },
            reWrite: function(){
                var { commentModal, detail } = this;
                commentModal.show = true;
                commentModal.title = "重新核销"
                commentModal.model = {
                  comment: ''
                }
            },
            onSubmitModifyComment: function(currentModel) {
                if(this.commentModal.title === '修改备注'){
                    ajaxPromise({
                        url: `/voucher/active/detail/update-comment/${this.voucherId}`,
                        data: currentModel.comment
                      }).then(data => {
                        MessageBox.open('修改成功');
                        // this.fetchSysLog();
                      }).catch(message => {
                        MessageBox.open(message);
                      });
                    var { detail, commentModal } = this;

                    detail.comment = currentModel.comment;
                    commentModal.show = false;
                }else if(this.commentModal.title === '重新核销'){
                    ajaxPromise({
                        url: `/voucher/active/${this.detail.voucherNo}/re_write_off`,
                        type: 'post',
                        data: {
                            remark: currentModel.comment
                        }
                      }).then(data => {
                        MessageBox.open('重新核销提交成功！');
                      }).catch(message => {
                        MessageBox.open(message);
                      });
                    this.commentModal.show = false;
                }
            },
            invalidVoucher: function(voucherId) {
                var that = this;
                MessageBox.open('确认作废？','提示',[{
                    type: 'success',
                    text: '确认',
                    handler: function() {
                        ajaxPromise({
                            url: `/voucher/active/invalid/${voucherId}`
                        }).then(resp => {
                            // location.reload();
                            MessageBox.open('作废成功');
                            that.fetch(that.$route.params.id);
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                },{
                    type: 'default',
                    text: '取消',
                    handler: function() {
                        MessageBox.close();
                    }
                }]);

            },
            editVoucher: function(voucherId) {
                location.assign(this.ctx + `#/capital/voucher/active/${voucherId}/edit`);
            },
            cashFlowSelect: function(voucherId) {
                ajaxPromise({
                    url: `/voucher/business/detail/match-cash-flow/${voucherId}`
                }).then(data => {
                    var { cashFlowSelectModal } = this;
                    cashFlowSelectModal.cashFlows = data.cashFlows;
                    cashFlowSelectModal.show = true;
                }).catch(message => {
                    MessageBox.open(message);
                });
            }
        }

    }
</script>
