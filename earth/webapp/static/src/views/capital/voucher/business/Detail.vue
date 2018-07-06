<style lang="sass">

</style>

<template>
	<div class="content">
		<div class="scroller" v-loading="fetching">
			<Breadcrumb :routes="[{title: '商户凭证详情'}]">
				<el-button
					size="small"
					type="primary"
					@click="invalidVoucher(voucherId)"
					v-if=" ifElementGranted('diable-bussiness-voucher') && detail.hasFails && detail.voucherStatus !== '作废'">
					作废
				</el-button>
                <el-button
                    size="small"
                    type="primary"
                    v-if="ifElementGranted('writeoff-bussiness-voucher') && detail.voucherStatus === '未核销'"
                    @click="deleteJob">
                    重新核销
                </el-button>
			</Breadcrumb>
			<div class="col-layout-detail">
				<div class="top">
					<div class="block">
						<h5 class="hd">凭证信息</h5>
						<div class="bd">
							<div class="col">
								<p>凭证编号：{{ detail.voucherNo }}</p>
								<p>凭证来源：{{ detail.voucherSource }}</p>
								<p>凭证类型：{{ detail.voucherType }}</p>
								<p>凭证金额：
									{{ detail.voucherAmount | formatMoney }}
								</p>
								<p>凭证内容：</p>
							</div>
							<div class="col">
								<p>专户账号：{{ detail.receivableAccountNo }}</p>
								<p>往来机构名称：{{ detail.paymentBank }}</p>
								<p>机构账户号：{{ detail.paymentAccountNo }}</p>
								<p>发生时间：{{ detail.time | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
							</div>
							<div class="col">
								<p>凭证状态：{{ detail.voucherStatus }}</p>
								<p>接口请求编号：{{ detail.requestNo }}</p>
								<p>备注：{{ detail.comment || '无'}}</p>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row-layout-detail">
				<div class="block">
					<h5 class="hd">
						凭证资金单据（银行流水）
						<el-button
							size="small"
							class="default pull-right"
							@click="cashFlowSelect(voucherId)"
							v-if="ifElementGranted('select--bussiness-voucher-cashflow') && !detail.cashFlow">
							流水选择
						</el-button>
					</h5>
					<PagingTable
						:data="detail.cashFlow ? [detail.cashFlow] : []">
						<el-table-column label="支付编号" prop="id">
						</el-table-column>
						<el-table-column label="付款方开户行" prop="counterBankName">
						</el-table-column>
						<el-table-column label="支付机构流水号" prop="bankSequenceNo">
						</el-table-column>
						<el-table-column label="支付接口编号" prop="">
						</el-table-column>
						<el-table-column label="支付机构" prop="counterAccountName">
						</el-table-column>
						<el-table-column label="支付金额" inline-template >
							<div> {{ row.transactionAmount | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column label="发生时间" inline-template>
                            <div> {{ row.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
                        </el-table-column>
                        <el-table-column label="支付方式" prop="">
                        </el-table-column>
                        <el-table-column label="状态" prop="auditStatusMsg">
                        </el-table-column>
                    </PagingTable>
                </div>

                <QueryTable
                    v-if="detail.voucherType != '回购' && detail.voucherType != '商户担保'"
                    key="daichan"
                    :title="'凭证业务单据'"
                    :showExport="true"
                    :action="`/voucher/business/detail/query`"
                    :active="detail.voucherType && detail.voucherType != '回购' && detail.voucherType != '商户担保'"
                    :voucherId="voucherId"
                    :voucherUuid="detail.voucherUuid"
                    :secondType="detail.secondType">
                    <el-table-column label="还款编号" inline-template>
                        <div>
                            <a :href="`${ctx}#/finance/assets/${row.assetSetUuid}/detail`" v-if="row.assetSetUuid"> {{ row.singleLoanContractNo }}</a>
                            <span v-else>{{ row.singleLoanContractNo }}</span>
                        </div>
                    </el-table-column>
                    <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo">
                    </el-table-column>
                    <el-table-column label="贷款合同编号" inline-template>
                        <div>
                            <a :href="`${ctx}#/data/contracts/detail?id=${row.contractId}`" v-if="row.contractId">
                            {{ row.contractNo }}
                            </a>
                            <span v-else>
                                {{ row.contractNo }}
                            </span>
                        </div>
                    </el-table-column>
                    <el-table-column :label="$utils.locale('financialContract.name')" inline-template>
                        <div>
                            <a :href="`${ctx}#/financial/contract/${row.financialContractUuid}/detail`" v-if="row.financialContractUuid">
                            {{ row.financialContractName }}
                            </a>
                            <span v-else>
                                {{ row.financialContractName }}
                            </span>
                        </div>
                    </el-table-column>
                    <el-table-column label="计划还款日期" inline-template>
                        <div>
                            {{ row.assetRecycleDate | formatDate }}
                        </div>
                    </el-table-column>
                    <el-table-column label="实际还款日期" inline-template>
                        <div>
                            {{ row.actualRecycleDate | formatDate }}
                        </div>
                    </el-table-column>
                    <el-table-column label="客户姓名" prop="customerName">
                    </el-table-column>
                    <el-table-column label="应还款金额" inline-template>
                        <el-popover
                            placement="top"
                            trigger="hover"
                            v-if="row.assetFairValue"
                            @show="fetchFeeDetail(row.assetSetUuid)">
                            <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                            <template v-else>
                                <div>还款本金：{{ row.statistics.loanAssetPrincipal | formatMoney }}</div>
                                <div>还款利息：{{ row.statistics.loanAssetInterest | formatMoney }}</div>
                                <div>贷款服务费：{{ row.statistics.loanServiceFee | formatMoney }}</div>
                                <div>技术维护费：{{ row.statistics.loanTechFee | formatMoney }}</div>
                                <div>其他费用：{{ row.statistics.loanOtherFee | formatMoney }}</div>
                                <div>逾期罚息：{{ row.statistics.overdueFeePenalty | formatMoney }}</div>
                                <div>逾期违约金：{{ row.statistics.overdueFeeObligation | formatMoney }}</div>
                                <div>逾期服务费：{{ row.statistics.overdueFeeService | formatMoney }}</div>
                                <div>逾期其他费用：{{ row.statistics.overdueFeeOther | formatMoney }}</div>
                                <div>逾期费用合计：{{ row.statistics.totalOverdueFee | formatMoney }}</div>
                            </template>
                            <span slot="reference" class="color-danger">{{ row.assetFairValue  | formatMoney }}</span>
                        </el-popover>
                    </el-table-column>
                    <el-table-column label="已还金额" inline-template>
                        <div> {{ row.payedAmount | formatMoney  }}</div>
                    </el-table-column>
                    <el-table-column label="凭证关联金额" inline-template>
                        <el-popover
                            v-if="row.detailAmount"
                            placement="top"
                            trigger="hover">
                            <div>还款本金：{{ row.principal | formatMoney }}</div>
                            <div>还款利息：{{ row.interest | formatMoney }}</div>
                            <div>贷款服务费：{{ row.serviceCharge | formatMoney }}</div>
                            <div>技术维护费：{{ row.maintenanceCharge | formatMoney }}</div>
                            <div>其他费用：{{ row.otherCharge | formatMoney }}</div>
                            <div>逾期罚息：{{ row.penaltyFee | formatMoney }}</div>
                            <div>逾期违约金：{{ row.latePenalty | formatMoney }}</div>
                            <div>逾期服务费：{{ row.lateFee | formatMoney }}</div>
                            <div>逾期其他费用：{{ row.lateOtherCost | formatMoney }}</div>
                            <div>逾期费用合计：{{ (row.penaltyFee + row.latePenalty + row.lateFee + row.lateOtherCost) | formatMoney }}</div>
                            <span slot="reference" class="color-danger">{{ row.detailAmount  | formatMoney }}</span>
                        </el-popover>
                    </el-table-column>
                    <el-table-column label="还款状态" prop="paymentStatus">
                    </el-table-column>
                    <el-table-column label="校验状态" prop="verifyStatus">
                    </el-table-column>
                    <el-table-column label="核销状态" prop="detailStatus">
                    </el-table-column>
                    <el-table-column label="明细备注" inline-template :context="_self">
                        <div> {{ row.comment | parseComment }} </div>
                    </el-table-column>
                </QueryTable>

                <QueryTable
                    v-if="detail.voucherType == '回购'"
                    key="huigou"
                    :title="'凭证业务单据'"
                    :action="`/voucher/business/repurchase/detail/query`"
                    :active="detail.voucherType && detail.voucherType == '回购'"
                    :voucherId="voucherId"
                    :voucherUuid="detail.voucherUuid"
                    :secondType="detail.secondType">
                    <el-table-column label="回购单号" inline-template>
                        <a :href="`${ctx}#/finance/repurchasedoc/${row.repurchaseDocUuid}/detail`"> {{ row.repurchaseDocUuid }}</a>
                    </el-table-column>
                    <el-table-column label="贷款合同编号" inline-template>
                        <a :href="`${ctx}#/data/contracts/detail?id=${row.contractId}`"> {{ row.contractNo }}</a>
                    </el-table-column>
                    <el-table-column label="回购起始日" prop="repoStartDate">
                    </el-table-column>
                    <el-table-column label="回购截止日" prop="repoEndDate">
                    </el-table-column>
                    <el-table-column label="商户名称" prop="appName">
                    </el-table-column>
                    <el-table-column label="客户名称" prop="customerName">
                    </el-table-column>
                    <el-table-column label="回购金额" inline-template>
                        <el-popover
                            v-if="row.amount"
                            placement="top"
                            trigger="hover">
                            <div>回购本金金额：{{ row.repurchasePrincipal | formatMoney }}</div>
                            <div>回购利息金额：{{ row.repurchaseInterest | formatMoney }}</div>
                            <div>回购罚息金额：{{ row.repurchasePenalty | formatMoney }}</div>
                            <div>回购其他费用金额：{{ row.repurchaseOtherCharges | formatMoney }}</div>
                            <span slot="reference" class="color-danger">{{ row.amount  | formatMoney }}</span>
                        </el-popover>
                    </el-table-column>
                    <el-table-column label="已还金额" inline-template>
                        <div> {{ row.paidAmount | formatMoney  }}</div>
                    </el-table-column>
                    <el-table-column label="凭证关联金额" inline-template>
                        <el-popover
                            v-if="row.relatedAmount"
                            placement="top"
                            trigger="hover">
                            <div>回购本金金额：{{ row.relatedRepurchasePrincipal | formatMoney }}</div>
                            <div>回购利息金额：{{ row.relatedRepurchaseInterest | formatMoney }}</div>
                            <div>回购罚息金额：{{ row.relatedRepurchasePenalty | formatMoney }}</div>
                            <div>回购其他费用金额：{{ row.relatedRepurchaseOtherCharges | formatMoney }}</div>
                            <span slot="reference" class="color-danger">{{ row.relatedAmount  | formatMoney }}</span>
                        </el-popover>
                    </el-table-column>
                    <el-table-column label="回购天数" prop="repoDays">
                    </el-table-column>
                    <el-table-column label="创建时间" inline-template>
                        <div> {{ row.creatTime | formatDate('yyyy-MM-dd') }}</div>
                    </el-table-column>
                    <el-table-column label="回购状态" prop="repoStatus">
                    </el-table-column>
                    <el-table-column label="校验状态" prop="sourceDocumentDetailCheckState">
                    </el-table-column>
                    <el-table-column label="核销状态" prop="sourceDocumentDetailStatus">
                    </el-table-column>
                    <el-table-column label="明细备注" inline-template :context="_self">
                        <div> {{ row.comment | parseComment }} </div>
                    </el-table-column>
                </QueryTable>

                <QueryTable
                    v-if="detail.voucherType == '商户担保'"
                    key="danbao"
                    :title="'凭证业务单据'"
                    :action="`/voucher/business/guarantee/detail/query`"
                    :active="detail.voucherType && detail.voucherType == '商户担保'"
                    :voucherId="voucherId"
                    :voucherUuid="detail.voucherUuid"
                    :secondType="detail.secondType">
                    <el-table-column prop="guaranteeOrderNo" label="担保补足号" inline-template>
                      <a :href="`${ctx}#/finance/guarantee/complement/${row.orderId}/detail`"> {{row.guaranteeOrderNo}} </a>
                    </el-table-column>
                    <el-table-column prop="repaymentNo" label="还款编号" inline-template>
                      <a :href="`${ctx}#/finance/assets/${row.assetSetUuid}/detail`"> {{row.repaymentNo}} </a>
                    </el-table-column>
                    <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo">
                    </el-table-column>
                    <el-table-column prop="planRecycleDate" label="计划还款日期" inline-template>
                      <div>{{ row.planRecycleDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column prop="guaranteeEndDate" label="担保截止日" inline-template>
                      <div>{{ row.guaranteeEndDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column prop="merchantName" label="商户名称"></el-table-column>
                    <el-table-column prop="planRecycleAmount" label="计划还款金额" inline-template>
                      <div>{{ row.planRecycleAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column prop="guaranteeAmount" label="担保金额" inline-template>
                      <div>{{ row.guaranteeAmount | formatMoney }}</div>
                    </el-table-column>
                </QueryTable>

                <div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="this.voucherUuid">
                    </SystemOperateLog>
                </div>
			</div>
		</div>

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

    export default{
    	components: {
    		PagingTable: require('views/include/PagingTable'),
    		CashFlowSelectModal: require('./include/CashFlowSelectModal'),
    		QueryTable: require('./include/QueryTable'),
            SystemOperateLog
    	},
    	data: function() {
    		return {
    			fetching: false,

    			detail: {
    				cashFlow: {},
    			},
                cashFlowSelectModal: {
                	cashFlows: [],
                	show: false,
                }
    		}
    	},
    	activated: function() {
    		this.fetchDetail(this.voucherId);
    	},
        deactivated: function() {
            this.detail = {
                cashFlow: {},
            };
        },
    	computed: {
    		voucherId: function() {
    			return this.$route.params.id;
    		},
            voucherUuid: function() {
                return this.detail.voucherUuid;
            }
    	},
        filters: {
            parseComment: function(comment) {
                var parse_comment = '';
                if (comment == undefined ) return parse_comment;

                try{
                    var result = JSON.parse(comment);
                    var type = typeof result;

                    const isArray = function(o) {
                        return Object.prototype.toString.call(o) === '[object Array]';
                    };

                    if (type == 'string') {
                        parse_comment = result;
                    } else if(type == 'object') {
                        if (isArray(result)) {
                            result.forEach(item => {
                                parse_comment += item.message;
                            });
                        } else {
                            parse_comment = result.message;
                        }
                    } else {
                        parse_comment = comment;
                    }
                    return parse_comment;

                } catch(error) {
                    return comment;
                }
            }
        },
    	methods: {
    		fetchDetail: function(voucherId) {
    			this.fetching = true;

                return ajaxPromise({
                    url: `/voucher/business/detail/${voucherId}/data`
                }).then(data => {
                    this.detail = data.detail;
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                })
    		},
            deleteJob: function(voucherUuid) {
                if (!this.voucherUuid) return;

                ajaxPromise({
                    url: '/voucher/business/detail/delete-job',
                    data: {
                        voucherUuid: this.voucherUuid
                    }
                }).then(() => {
                    this.$refs.sysLog.fetch();
                    MessageBox.open('重新核销成功');
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
    		invalidVoucher: function(voucherId) {
                var that = this;
    			MessageBox.open('确认作废？','提示',[{
                    type: 'success',
                    text: '确认',
                    handler: function() {
                        ajaxPromise({
                            url: `/voucher/business/invalid/${voucherId}`
                        }).then(resp => {
                            // location.reload();
                            MessageBox.open('作废成功');
                            that.fetchDetail(voucherId);
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
    		},
    	}
    }
</script>
