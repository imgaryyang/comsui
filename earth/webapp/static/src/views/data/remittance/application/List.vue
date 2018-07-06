<template>
	<div class="content">
		<div class="scroller">
			<div class="query-area">
				<el-form class="sdf-form sdf-query-form" :inline="true">
					<el-form-item>
						<list-cascader
						    clearable
						    size="small"
                        	:collection="financialContractQueryModels"
						    v-model="queryConds.financialContractIds"
						    :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
					</el-form-item>
					<el-form-item>
						<el-col :span="11">
							<el-form-item>
								<DateTimePicker
									v-model="queryConds.receiveStartDate"
									:end-date="queryConds.receiveEndDate"
									placeholder="受理日期起始"
									:pick-time="true"
									:format-to-minimum="true"
									size="small">
								</DateTimePicker>
							</el-form-item>
						</el-col>
						<el-col :span="2">
							<div class="text-align-center color-dim">至</div>
						</el-col>
						<el-col :span="11">
							<el-form-item>
								<DateTimePicker
									v-model="queryConds.receiveEndDate"
									:start-date="queryConds.receiveStartDate"
									placeholder="受理日期终止"
									:pick-time="true"
									:format-to-maximum="true"
									size="small">
								</DateTimePicker>
							</el-form-item>
						</el-col>
					</el-form-item>
					<el-form-item>
						<el-select 
							v-model="queryConds.orderStatus" 
							placeholder="订单状态"
							multiple 
							size="small">
							<el-select-all-option 
							    :options="orderStatus">
							</el-select-all-option>
							<el-option
								v-for="item in orderStatus"
								:label="item.value"
								:value="item.key">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<span class="item vertical-line"></span>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option value="orderNo" label="订单编号"></el-option>
							<el-option value="loanContractNo" label="贷款合同编号"></el-option>
							<el-option value="contractUniqueId" label="贷款合同唯一识别码"></el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
				</el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border"
					stripe
					@sort-change="onSortChange"
					v-loading="dataSource.fetching"
					:data="dataSource.list">
					<el-table-column inline-template label="订单编号">
						<a :href="`${ctx}#/data/remittance/application/${row.orderNo}/detail`">{{ row.orderNo }}</a>
					</el-table-column>
					<el-table-column inline-template label="贷款合同唯一识别码">
						<a :href="`${ctx}#/data/contracts/detail?uid=${row.contractUniqueId}`">{{ row.contractUniqueId }}</a>
					</el-table-column>
					<el-table-column inline-template label="贷款合同编号">
						<a :href="`${ctx}#/data/contracts/detail?uid=${row.contractUniqueId}`">{{ row.loanContractNo }}</a>
					</el-table-column>
					<el-table-column prop="financialProjectName" :label="$utils.locale('financialContract.no')"></el-table-column>
					<el-table-column inline-template prop="planLoanAmount" label="计划放款金额" sortable="custom">
						<div>{{ row.planLoanAmount | formatMoney }}</div>
					</el-table-column>
					<el-table-column inline-template prop="actualLoanAmount" label="实际放款金额" sortable="custom">
						<div>{{ row.actualLoanAmount | formatMoney }}</div>
					</el-table-column>
					<el-table-column prop="remittanceStrategy" label="放款策略类型"></el-table-column>
					<el-table-column inline-template prop="receiveTime" label="受理时间" sortable="custom">
						<div>{{ row.receiveTime }}</div>
					</el-table-column>
					<el-table-column inline-template label="订单状态">
						<span :class="{
								'color-warning': row.orderStatus =='异常',
								'color-danger': row.orderStatus =='失败',
							}">
							{{ row.orderStatus }}
						</span>
					</el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
			<div class="pull-right">
				<ListStatistics 
					identifier="financialContractIds"
					:action="`/remittance/application/amountStatistics`"
					:parameters="conditions">
					<template scope="statistics">
						<div>计划放款金额：{{ statistics.data.plannedAmount | formatMoney }}</div>
						<div>实际放款金额：{{ statistics.data.actualAmount | formatMoney }}</div>
						<div>差值：{{ statistics.data.differenceAmount | formatMoney }}</div>
					</template>
				</ListStatistics>
				<PageControl 
	                v-model="pageConds.pageIndex"
	                :size="dataSource.size"
	                :per-page-record-number="pageConds.perPageRecordNumber">
	            </PageControl>
			</div>
		</div>
	</div>
</template>

<script>
	import Pagination from 'mixins/Pagination';
	import ListPage from 'mixins/ListPage';
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		mixins: [Pagination, ListPage],
		components: { 
			ExportPreviewModal: require('views/include/ExportPreviewModal'),
			ExportDropdown: require('views/include/ExportDropdown'),
			ComboQueryBox: require('views/include/ComboQueryBox'),
			ListStatistics: require('views/include/ListStatistics')
		},
		data: function() {
			return {
				action: '/remittance/application/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractIds: [],
					receiveStartDate: '',
					receiveEndDate: '',
					orderStatus: [],
					selectKeyWord: ''
				},
				comboConds: {
					orderNo: '',
					loanContractNo: '',
					contractUniqueId: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

				financialContractQueryModels: [],
				orderStatus: []
			};
		},
		methods: {
			initialize: function() {
                return this.getOptions();
            },
			getOptions: function() {
				return ajaxPromise({
					url: `/remittance/application/options`
				}).then(data => {
					this.financialContractQueryModels = data.queryAppModels || [];
					this.orderStatus = data.orderStatus || [];
					if (!this.queryConds.orderStatus.length) {
						this.queryConds.orderStatus = this.orderStatus.map(item => item.key);
					}
				}).catch(message => {
					MessageBox.open(message);
				});
			}
		}
	}
</script>