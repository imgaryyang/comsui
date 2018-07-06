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
									v-model="queryConds.planStartDate"
									:end-date="queryConds.planEndDate"
									placeholder="计划还款起始日"
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
									v-model="queryConds.planEndDate"
									:start-date="queryConds.planStartDate"
									placeholder="计划还款终止日"
									size="small">
								</DateTimePicker>
							</el-form-item>
						</el-col>
					</el-form-item>
					<el-form-item>
						<el-col :span="11">
							<el-form-item>
								<DateTimePicker
									v-model="queryConds.actualStartDate"
									:end-date="queryConds.actualEndDate"
									size="small"
									placeholder="实际还款起始日">
								</DateTimePicker>
							</el-form-item>
						</el-col>
						<el-col :span="2">
							<div class="text-align-center color-dim">至</div>
						</el-col>
						<el-col :span="11">
							<el-form-item>
								<DateTimePicker
									v-model="queryConds.actualEndDate"
									:start-date="queryConds.actualStartDate"
									size="small"
									placeholder="实际还款终止日">
								</DateTimePicker>
							</el-form-item>
						</el-col>
					</el-form-item>
					<el-form-item>
						<el-select
							v-model="queryConds.planStatus"
							placeholder="计划状态"
							clearable
							size="small">
							<el-option
								v-for="(value, label) in planStatus"
								:label="label"
								:value="value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select
							v-model="queryConds.repaymentStatus"
							size="small"
							clearable
							placeholder="还款状态">
							<el-option
								v-for="(value, label ) in executingStatus"
								:label="label"
								:value="value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select
							v-model="queryConds.repaymentType"
							placeholder="还款类型"
							clearable
							size="small">
							<el-option
								v-for="(value, label) in repaymentType"
								:label="label"
								:value="value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option label="还款编号" value="repaymentNo"></el-option>
							<el-option label="商户还款计划编号" value="outerRepaymentPlanNo"></el-option>
							<el-option label="贷款合同编号" value="contractNo"></el-option>
							<el-option label="客户姓名" value="customerName"></el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
					<el-form-item v-if="ifElementGranted('export-detail-repayment-plan')">
						<ExportDropdown @command="exportRepaymentPlan">
							<el-dropdown-item >还款计划</el-dropdown-item>
						</ExportDropdown>
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
					<el-table-column inline-template label="还款编号">
						<a :href="`${ctx}#/finance/assets/${row.assetSetUuid}/detail`">{{ row.singleLoanContractNo }}</a>
					</el-table-column>
					<el-table-column prop="outerRepaymentPlanNo" label="商户还款计划编号">
					</el-table-column>
					<el-table-column inline-template :min-width="'130px'" label="贷款合同编号">
						<a :href="`${ctx}#/data/contracts/detail?id=${row.contractId}`">{{ row.contractNo }}</a>
					</el-table-column>
					<!-- <el-table-column prop="financialContractNo" :label="$utils.locale('financialContract.no')">
					</el-table-column> -->
					<el-table-column prop="financialProjectName" :label="$utils.locale('financialContract.name')">
					</el-table-column>
					<el-table-column prop="customerName" label="客户姓名">
					</el-table-column>
					<el-table-column prop="currentPeriod" label="当前期数">
					</el-table-column>
					<el-table-column
						inline-template
						prop="assetRecycleDate"
						width="115px"
						label="计划还款日期"
						sortable="custom">
						<div>{{ row.assetRecycleDate | formatDate }}</div>
					</el-table-column>
					<el-table-column
						inline-template
						width="115px"
						label="应还款金额"
						prop="amount"
						sortable="custom">
						<el-popover
							@show="fetchAssetSetStatistics(row.assetSetUuid)"
							trigger="hover"
							placement="top">
							<div>
								<div v-if="row.statistics.error">{{ row.statistics.error }}</div>
								<template v-else>
									<div>计划还款本金：{{ row.statistics.loanAssetPrincipal | formatMoney }}</div>
									<div>计划还款利息：{{ row.statistics.loanAssetInterest | formatMoney }}</div>
									<div>贷款服务费：{{ row.statistics.loanServiceFee | formatMoney }}</div>
									<div>技术维护费：{{ row.statistics.loanTechFee | formatMoney }}</div>
									<div>其他费用：{{ row.statistics.loanOtherFee | formatMoney }}</div>
									<div>逾期费用合计：{{ row.totalOverDueFee | formatMoney }}</div>
								</template>
							</div>
							<span slot="reference">{{ row.amount | formatMoney }}</span>
						</el-popover>
					</el-table-column>
					<el-table-column
						inline-template
						prop="actualAmount"
						width="115px"
						label="实际还款金额"
						>
						<div>{{ row.actualAmount | formatMoney }} </div>
					</el-table-column>
					<el-table-column
						inline-template
						prop="actualRecycleDate"
						width="115px"
						label="实际还款日期"
						sortable="custom">
						<div>{{ row.actualRecycleDate | formatDate }}</div>
					</el-table-column>
					<el-table-column prop="planStatus" label="计划状态">
					</el-table-column>
					<el-table-column prop="paymentStatus" label="还款状态">
					</el-table-column>
					<el-table-column prop="repaymentType" label="还款类型">
					</el-table-column>
					<el-table-column prop="comment" label="备注">
					</el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
			<div class="pull-right">
				<ListStatistics
					action="/repaymentPlan/amountStatistics"
					:parameters="conditions">
					<template scope="statistics">
						<div>应还款金额<span class="pull-right">{{ statistics.data.plannedAmount | formatMoney }}</span></div>
						<div>足额还款金额<span class="pull-right">{{ statistics.data.fullAmount | formatMoney }}</span></div>
						<div>部分还款金额<span class="pull-right">{{ statistics.data.partAmount | formatMoney }}</span></div>
						<div>未偿总额<span class="pull-right">{{ statistics.data.differenceAmount | formatMoney }}</span></div>
					</template>
				</ListStatistics>
				<PageControl
	                v-model="pageConds.pageIndex"
	                :size="dataSource.size"comboConds
	                :per-page-record-number="pageConds.perPageRecordNumber">
	            </PageControl>
			</div>
		</div>

		<ExportPreviewModal
            :parameters="conditions"
            :query-action="`/repaymentPlan/preview-export-repayment-management`"
            :download-action="`/report/export?reportId=3`"
			v-model="exportModal.show">
            <el-table-column prop="uniqueId" label="uniqueid"></el-table-column>
            <el-table-column prop="financialContractNo" :label="$utils.locale('financialContract.no')"></el-table-column>
            <el-table-column prop="appName" :label="$utils.locale('financialContract.appAccount.name')"></el-table-column>
            <el-table-column prop="financialAccountNo" :label="$utils.locale('financialContract.account.no')"></el-table-column>
            <el-table-column width="140px" prop="loanContractNo" label="贷款合同编号"></el-table-column>
            <el-table-column prop="customerName" label="贷款客户姓名"></el-table-column>
            <el-table-column width="140px" prop="repaymentNo" label="还款编号"></el-table-column>
            <el-table-column width="140px" prop="outerRepaymentPlanNo" label="商户还款计划编号"></el-table-column>
            <el-table-column prop="loanDate" label="放款日期"></el-table-column>
            <el-table-column prop="effectiveDate" label="生效日期"></el-table-column>
            <el-table-column prop="assetRecycleDate" label="计划还款日期"></el-table-column>
            <el-table-column prop="actualRecycleDate" label="实际还款日期"></el-table-column>
            <el-table-column prop="assetPrincipalValue" label="计划还款本金"></el-table-column>
            <el-table-column prop="assetInterestValue" label="计划还款利息"></el-table-column>
            <el-table-column prop="loanServiceFee" label="贷款服务费"></el-table-column>
            <el-table-column prop="technicalServiceFee" label="技术维护费"></el-table-column>
            <el-table-column prop="otherExpenses" label="其他费用"></el-table-column>
            <el-table-column prop="planStatus" label="计划状态"></el-table-column>
            <el-table-column prop="paymentStatus" label="还款状态"></el-table-column>
            <el-table-column prop="idCardNo" label="贷款客户身份证号码"></el-table-column>
            <el-table-column prop="age" label="客户年龄"></el-table-column>
            <el-table-column prop="bankName" label="还款账户开户行名称"></el-table-column>
            <el-table-column prop="province" label="开户行所在省"></el-table-column>
            <el-table-column prop="city" label="开户行所在市"></el-table-column>
            <el-table-column prop="payAcNo" label="还款账户号"></el-table-column>
		</ExportPreviewModal>
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
				action: '/repaymentPlan/query',
				autoload: false,
				listenConditionChange: false,
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					planStartDate: '',
					planEndDate: '',
					actualStartDate: '',
					actualEndDate: '',
					planStatus: '',
					repaymentStatus: '',
					repaymentType: '',
				},
				comboConds: {
					repaymentNo: '',
					contractNo: '',
					customerName: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

				financialContractQueryModels: [],
				planStatus:[],
				executingStatus:[],
				repaymentType:[],

				exportModal: {
					show: false
				},
			};
		},
		methods: {
			initialize: function() {
			    return this.getOptions();
			},
			fetchAssetSetStatistics: function(assetSetUuid) {
				var { list } = this.dataSource;
				var index = list.findIndex(item => item.assetSetUuid === assetSetUuid);

				if (index === -1) return;

				ajaxPromise({
					url: `/repaymentPlan/repaymentInfo?uuid=${assetSetUuid}`,
				}).catch(error => {
					return { error };
				}).then(data => {
					this.$set(list[index], 'statistics', data);
				});
			},
			onSuccess: function(data) {
			    this.dataSource.list = data.list.map(item => {
			    	item.statistics = {};
			    	return item;
			    });
			    this.dataSource.size = data.size;
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/repaymentPlan/optionData`
				}).then(data => {
					this.financialContractQueryModels = data.queryAppModels || [];
					this.planStatus = data.planStatus || [];
					this.repaymentType = data.repaymentType || [];
					this.executingStatus = data.executingStatus || [];

					var queryConds = this.queryConds;

				}).catch(message => {
					MessageBox.open(message);
				});
			},
			exportRepaymentPlan: function() {
				if (this.dataSource.size > 30000) {
					MessageBox.open('当前占用资源过多，请缩小搜索范围！');
					return;
				}
				this.exportModal.show = true;
			}
		}
	}
</script>
