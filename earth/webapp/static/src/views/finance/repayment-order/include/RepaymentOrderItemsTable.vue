<style lang="sass">

</style>

<template>
	<div>
		<div class="row-layout-detail">
			<QueryTable
				:title="title"
				key="query-table"
				ref="queryTable"
				v-model="value"
				:autoload="autoload"
				:queryConditions="queryConditions"
				:action="'repayment-order/repayment/query-detail'">
				<template slot="query-form">
                    <el-form-item>
                        <el-select
                            v-model="queryConds.checkStatus"
                            placeholder="校验状态"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in checkStatusNumList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.detailPayStatusNumList"
                            placeholder="核销状态"
                            multiple
                            size="small">
                            <el-select-all-option :options="detailPayStatusNumList"></el-select-all-option>
                            <el-option
                                v-for="item in detailPayStatusNumList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="贷款合同编号" value="contractNo"></el-option>
                            <el-option label="商户还款计划编号" value="repayScheduleNo"></el-option>
                            <el-option label="业务编号" value="repaymentBusinessNo"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                </template>
                <template slot="table">
             	  	<el-table-column label="业务编号" prop="repaymentBusinessNo" inline-template>
						<div>
							<a :href="row.repaymentWay == 'REPURCHASE' ? `${ctx}#/finance/repurchasedoc/${row.repaymentBusinessNo}/detail` : `${ctx}#/finance/assets/${row.repaymentBusinessUuid}/detail`" v-if="row.repaymentBusinessNo">{{ row.repaymentBusinessNo }}</a>
							<span v-else>{{ row.repaymentBusinessNo }}</span>
						</div>
					</el-table-column>
					<el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo">
					</el-table-column>
					<el-table-column label="贷款合同编号" prop="contractNo" inline-template>
						<div>
							<a :href="`${ctx}#/data/contracts/detail?uid=${row.contractUniqueId}`" v-if="row.contractUniqueId">{{ row.contractNo }}</a>
							<span v-else>{{ row.contractNo }}</span>
						</div>
					</el-table-column>
					<el-table-column label="设定还款时间" prop="repaymentPlanTime" inline-template>
						<div>{{ row.repaymentPlanTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
					</el-table-column>
					<el-table-column label="还款方式" prop="repaymentWayCh" >
					</el-table-column>
					<el-table-column label="还款金额" prop="amount" inline-template>
						<el-popover trigger="hover">
							<div v-if="!row.repurchaseWay">
								<div>还款本金: {{ row.repaymentChargesDetail.loanAssetPrincipal | formatMoney  }}</div>
								<div>还款利息: {{ row.repaymentChargesDetail.loanAssetInterest | formatMoney  }}</div>
								<div>贷款服务费: {{ row.repaymentChargesDetail.loanServiceFee | formatMoney  }}</div>
								<div>技术服务费: {{ row.repaymentChargesDetail.loanTechFee | formatMoney  }}</div>
								<div>其他费用: {{ row.repaymentChargesDetail.loanOtherFee | formatMoney  }}</div>
								<div>逾期罚息: {{ row.repaymentChargesDetail.overdueFeePenalty | formatMoney  }}</div>
								<div>逾期违约金: {{ row.repaymentChargesDetail.overdueFeeObligation | formatMoney  }}</div>
								<div>逾期服务费: {{ row.repaymentChargesDetail.overdueFeeService | formatMoney  }}</div>
								<div>逾期其他费用: {{ row.repaymentChargesDetail.overdueFeeOther | formatMoney  }}</div>
							</div>
							<div v-else>
								<div>回购本金: {{ row.repaymentChargesDetail.repurchasePrincipal | formatMoney  }}</div>
								<div>回购利息: {{ row.repaymentChargesDetail.repurchaseInterest | formatMoney  }}</div>
								<div>回购罚息: {{ row.repaymentChargesDetail.repurchasePenalty | formatMoney  }}</div>
								<div>回购其他费用: {{ row.repaymentChargesDetail.repurchaseOtherFee | formatMoney   }}</div>
							</div>
							<span slot="reference">{{ row.amount | formatMoney }}</span>
						</el-popover>
					</el-table-column>
					<el-table-column label="校验状态" prop="checkStatus">
					</el-table-column>
					<el-table-column label="核销状态" prop="detailPayStatusCh" >
					</el-table-column>
					<el-table-column label="备注" prop="remark">
					</el-table-column>
					<el-table-column label="操作" inline-template>
						<div>
							<a href="javascripts: void 0" @click.prevent="cancelHandler(row)" v-if="row.canBeEdit">作废</a>
						</div>
					</el-table-column>
                 </template>
			</QueryTable>
		</div>
        <EditOrderModal
        	v-model="editOrderModal.show"
        	:model="editOrderModal.model">
        </EditOrderModal>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	export default {
		components: {
			EditOrderModal: require('./EditOrderModal'),
			ComboQueryBox: require('views/include/ComboQueryBox'),
			QueryTable: require('views/include/QueryTable')
		},
		props: {
			action: {
				type: String,
				default: '',
				required: true
			},
			autoload: false,
			title: {
				type: String,
				default: ''
			},
			orderUuid: [Number, String],
			value: {
				type: Boolean,
				default: false
			},
			repaymentOrderStatusOrdinal: [Number, String]
		},
		data: function() {
			return {
                editOrderModal: {
                	show: false,
                	model: {

                	}
                },
                checkStatusNumList: [{value: '校验成功', key: 0},{value: '校验失败', key: 1},{value: '作废', key: 2}],
                detailPayStatusNumList: [{value: '已核销', key: 1},{value: '未核销', key: 0}],

                queryConds: {
                	checkStatus: '',
                	detailPayStatusNumList: [],
                },
                comboConds: {
                	contractNo: '',
                	repayScheduleNo: '',
                	repaymentBusinessNo: ''
                }
			}
		},
		computed: {
			queryConditions: function() {
                return Object.assign({orderUuid: this.orderUuid},this.queryConds, this.comboConds);
            }
		},
		watch: {
			orderUuid: function(current) {
				this.queryConds = Object.assign({}, {checkStatus: '', detailPayStatusNumList: []});
				this.comboConds = Object.assign({}, {contractNo: '', repayScheduleNo: '', repaymentBusinessNo: ''});
			}
		},
		methods: {
            cancelHandler: function(item) {
            	ajaxPromise({
            		url: `/repayment-order/query_unclear_asset`,
            		type: 'post',
            		data: {
            			lapsedItemUuid: item.orderDetailUuid
            		}
            	}).then(data => {
	            	this.editOrderModal.show = true;
	            	this.editOrderModal.model = {
	            		orderUuid: this.orderUuid,
	            		cancelItem: item,
	            		itemModel: JSON.parse(data.itemModel)
	            	}
            	}).catch(message => {
            		MessageBox.open(message);
            	});
            }
		}
	}
</script>
