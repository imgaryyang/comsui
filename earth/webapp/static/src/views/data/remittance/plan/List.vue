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
						    v-model="queryConds.financialContractUuids"
						    :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
					</el-form-item>
					<el-form-item>
						<el-col :span="11">
							<el-form-item>
								<DateTimePicker
									v-model="queryConds.plannedStartDate"
									:end-date="queryConds.plannedEndDate"
									placeholder="起始计划放款日"
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
									v-model="queryConds.plannedEndDate"
									:start-date="queryConds.plannedStartDate"
									placeholder="终止计划放款日"
									size="small">
								</DateTimePicker>
							</el-form-item>
						</el-col>
					</el-form-item>
					<el-form-item>
						<el-select 
							v-model="queryConds.executionStatus" 
							placeholder="执行状态" 
							clearable 
							size="small">
							<el-option
								v-for="item in executionStatus"
								:label="item.value"
								:value="item.key">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select 
							v-model="queryConds.remittanceType" 
							clearable 
							placeholder="交易类型" 
							size="small">
							<el-option value="0" label="代付"></el-option>
							<el-option value="1" label="代收"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<span class="item vertical-line"></span>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option value="orderNo" label="放款编号"></el-option>
							<el-option value="loanContractNo" label="贷款合同编号"></el-option>
							<el-option value="payerAccountHolder" label="付款方账户名"></el-option>
							<el-option value="cpBankAccountHolder" label="收款方账户名"></el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
					<el-form-item v-if="false">
						<el-button size="small" type="primary">批量放款</el-button>
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
					<el-table-column inline-template label="放款编号">
						<a :href="`${ctx}#/data/remittance/plan/${row.remittancePlanUuid}/detail`">{{ row.remittancePlanUuid }}</a>
					</el-table-column>
					<el-table-column min-width="100px" inline-template label="贷款合同编号">
						<a :href="`${ctx}#/data/contracts/detail?uid=${row.contractUniqueId}`">{{ row.contractNo }}</a>
					</el-table-column>
					<el-table-column inline-template prop="plannedPaymentDate" label="计划放款日期" sortable="custom">
						<div>{{ row.plannedPaymentDate | formatDate }}</div>
					</el-table-column>
					<el-table-column inline-template prop="plannedTotalAmount" label="执行金额" sortable="custom">
						<div>{{ row.plannedTotalAmount | formatMoney }}</div>
					</el-table-column>
					<el-table-column inline-template label="付款方账户名">
						<div>
							<span>{{ row.pgAccountInfo.accountName }}</span>
							<el-popover
								v-if="row.pgAccountInfo.accountName"
								trigger="click"
								placement="top">
								<div>
									<div>账户名：{{ row.pgAccountInfo.accountName }}</div>
									<div>账户号：{{ row.pgAccountInfo.accountNo }}</div>
									<div>开户行：{{ row.pgAccountInfo.bankName }}</div>
									<div>所在地：{{ row.pgAccountInfo.province }}&nbsp;{{ row.pgAccountInfo.city }}</div>
									<div>证件号：{{ row.pgAccountInfo.desensitizationIdNumber }}</div>
								</div>
								<i slot="reference" class="icon icon-bankcard"></i>
							</el-popover>
						</div>
					</el-table-column>
					<el-table-column inline-template label="收款方账户名">
						<div>
							<span>{{ row.cpAccountInfo.accountName }}</span>
							<el-popover
								v-if="row.cpAccountInfo.accountName"
								trigger="click"
								placement="top">
								<div>
									<div>账户名：{{ row.cpAccountInfo.accountName }}</div>
									<div>账户号：{{ row.cpAccountInfo.accountNo }}</div>
									<div>开户行：{{ row.cpAccountInfo.bankName }}</div>
									<div>所在地：{{ row.cpAccountInfo.province }}&nbsp;{{ row.cpAccountInfo.city }}</div>
									<div>证件号：{{ row.cpAccountInfo.desensitizationIdNumber }}</div>
								</div>
								<i slot="reference" class="icon icon-bankcard"></i>
							</el-popover>
						</div>
					</el-table-column>
					<el-table-column width="70px" prop="transactionType" label="交易类型"></el-table-column>
					<el-table-column width="70px" inline-template label="执行状态">
						<span :class="{
								'color-warning': row.executionStatus =='异常',
								'color-danger': row.executionStatus =='失败',
							}">
							{{ row.executionStatus }}
						</span>
					</el-table-column>
					<el-table-column prop="transactionRemark" label="备注"></el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
			<div class="pull-right">
				<ListStatistics 
					:action="`/remittance/plan/amountStatistics`"
					:parameters="conditions">
					<template scope="statistics">
						<div>执行金额：{{ statistics.data.plannedAmount | formatMoney }}</div>
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
				action: '/remittance/plan/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					plannedStartDate: '',
					plannedEndDate: '',
					executionStatus: '',
					remittanceType: ''
				},
				comboConds: {
					orderNo: '',
					loanContractNo: '',
					payerAccountHolder: '',
					cpBankAccountHolder: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

				financialContractQueryModels: [],
				executionStatus: []
			};
		},
		methods: {
			initialize: function() {
			    return this.getOptions();
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/remittance/plan/options`
				}).then(data => {
					this.financialContractQueryModels = data.queryAppModels || [];
					this.executionStatus = data.executionStatus || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			}
		}
	}
</script>