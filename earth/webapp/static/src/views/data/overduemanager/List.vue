<style>

</style>

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
                            :show-all-levels="false"
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
                                    placeholder="计划还款起始日期"
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
                                    placeholder="计划还款终止日期"
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
                            v-model="queryConds.paymentStatusOrdinals"
                            size="small"
                            clearable
                            placeholder="还款状态"
                            multiple>
                            <el-select-all-option
                                :options="paymentStatusList">
                            </el-select-all-option>
                            <el-option
                                v-for="item in paymentStatusList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.auditOverDueStatusOrdinals"
                            placeholder="逾期状态"
                            clearable
                            size="small"
                            multiple>
                            <el-select-all-option
                                :options="auditOverdueStatusList">
                            </el-select-all-option>
                            <el-option
                                v-for="item in auditOverdueStatusList"
                                :label="item.value"
                                :value="item.key">
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
                    <el-form-item v-if="ifElementGranted('export-overdue-repayment-plan')">
                        <ExportDropdown @command="handleExport">
                            <el-dropdown-item command="overDueRepayment">逾期还款管理表</el-dropdown-item>
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
					<el-table-column label="还款编号" prop="" inline-template>
						<a :href="`${ctx}#/finance/assets/${row.assetSetUuid}/detail`">{{ row.singleLoanContractNo }}</a>
					</el-table-column>
                    <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo"></el-table-column>
					<el-table-column
                        inline-template
                        label="贷款合同编号" prop="contractNo">
                        <a :href="`${ctx}#/data/contracts/detail?id=${ row.contractId }`">{{ row.contractNo }}</a>
                    </el-table-column>
					<!-- <el-table-column :label="$utils.locale('financialContract.no')" prop="financialContractNo"></el-table-column> -->
					<el-table-column label="客户姓名" prop="customerName"></el-table-column>
                    <el-table-column label="当前期数" prop="currentPeriod" inline-template>
                        <div>{{ row.currentPeriod }}/{{ row.allPeriods }}</div>
                    </el-table-column>
					<el-table-column label="计划还款日期" prop="assetRecycleDate" inline-template sortable="custom">
						<div>{{ row.assetRecycleDate | formatDate }}</div>
					</el-table-column>
                    <el-table-column label="实际还款日期" prop="actualRecycleDate" inline-template sortable="custom">
                        <div>{{ row.actualRecycleDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="差异天数" prop="diffDay" sortable="custom"></el-table-column>
                    <el-table-column label="逾期天数" prop="overdueDay" sortable="custom"></el-table-column>
					<el-table-column label="应还款金额" prop="amount" inline-template sortable="custom">
						<el-popover
							@show="fetchAssetSetStatistics(row.assetSetUuid)"
							trigger="hover"
							placement="top">
							<div>
								<div v-if="row.statistics.error">{{ row.statistics.error }}</div>
								<template v-else>
									<div>计划还款本金：{{ row.statistics.planChargesDetail.loanAssetPrincipal | formatMoney }}</div>
                                    <div>计划还款利息：{{ row.statistics.planChargesDetail.loanAssetInterest | formatMoney }}</div>
                                    <div>贷款服务费：{{ row.statistics.planChargesDetail.loanServiceFee | formatMoney }}</div>
                                    <div>技术维护费：{{ row.statistics.planChargesDetail.loanTechFee | formatMoney }}</div>
                                    <div>其他费用：{{ row.statistics.planChargesDetail.loanOtherFee | formatMoney }}</div>
                                    <div>逾期罚息：{{ row.statistics.planChargesDetail.overdueFeePenalty | formatMoney }}</div>
                                    <div>逾期违约金：{{ row.statistics.planChargesDetail.overdueFeeObligation | formatMoney }}</div>
                                    <div>逾期服务费：{{ row.statistics.planChargesDetail.overdueFeeService | formatMoney }}</div>
                                    <div>逾期其他费用：{{ row.statistics.planChargesDetail.overdueFeeOther | formatMoney }}</div>
								</template>
							</div>
							<span slot="reference">{{ row.amount | formatMoney }}</span>
						</el-popover>
					</el-table-column>
					<el-table-column label="实际还款金额" prop="actualAmount" inline-template sortable="custom">
                        <el-popover
                            @show="fetchAssetSetStatistics(row.assetSetUuid)"
                            trigger="hover"
                            placement="top">
                            <div>
                                <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                                <template v-else>
                                    <div>实收本金：{{  row.statistics.paidUpChargesDetail.loanAssetPrincipal | formatMoney }}</div>
                                    <div>实收利息：{{  row.statistics.paidUpChargesDetail.loanAssetInterest | formatMoney }}</div>
                                    <div>实收贷款服务费：{{  row.statistics.paidUpChargesDetail.loanServiceFee | formatMoney }}</div>
                                    <div>实收技术维护费：{{  row.statistics.paidUpChargesDetail.loanTechFee | formatMoney }}</div>
                                    <div>实收其他费用：{{  row.statistics.paidUpChargesDetail.loanOtherFee | formatMoney }}</div>
                                    <div>实收逾期罚息：{{  row.statistics.paidUpChargesDetail.overdueFeePenalty | formatMoney }}</div>
                                    <div>实收逾期违约金：{{  row.statistics.paidUpChargesDetail.overdueFeeObligation | formatMoney }}</div>
                                    <div>实收逾期服务费：{{  row.statistics.paidUpChargesDetail.overdueFeeService | formatMoney }}</div>
                                    <div>实收逾期其他费用：{{  row.statistics.paidUpChargesDetail.overdueFeeOther | formatMoney }}</div>
                                </template>
                            </div>
                            <span slot="reference">{{ row.actualAmount | formatMoney }}</span>
                        </el-popover>
					</el-table-column>
					<el-table-column label="未偿金额" inline-template sortable="custom">
                        <el-popover
                            @show="fetchAssetSetStatistics(row.assetSetUuid)"
                            trigger="hover"
                            placement="top">
                            <div>
                                <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                                <template v-else>
                                    <div>未偿本金：{{ row.statistics.unPaidDetail.unPaidPrincipalValue | formatMoney }}</div>
                                    <div>未偿利息：{{ row.statistics.unPaidDetail.unPaidInterestValue | formatMoney }}</div>
                                    <div>未偿其他：{{ row.statistics.unPaidDetail.unPaidTotalOtherFee | formatMoney }}</div>
                                </template>
                            </div>
                            <span slot="reference">{{ (row.amount - row.actualAmount) | formatMoney }}</span>
                        </el-popover>
					</el-table-column>
					<el-table-column label="还款状态" prop="paymentStatus" inline-template>
						<span :class="{
							'color-danger': row.paymentStatus == '还款异常'
							}">{{ row.paymentStatus }}</span>
					</el-table-column>
                    <el-table-column label="逾期状态" prop="overdueStatus" inline-template>
                        <span :class="{
                            'color-danger': row.overdueStatus == '已逾期'
                            }">{{ row.overdueStatus }}</span>
                    </el-table-column>
					<el-table-column label="担保状态" prop="guaranteeStatus" inline-template>
						<span :class="{
							'color-danger': row.guaranteeStatus == '担保作废',
							'color-warning': row.guaranteeStatus == '待补足'
							}">{{ row.guaranteeStatus }}</span>
					</el-table-column>
                    <el-table-column label="备注" prop="comment"></el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
			<div class="pull-right">
				<ListStatistics
					action="/overdueAsset/amountStatistics"
                    identifier="financialContractUuids"
					:parameters="conditions">
					<template scope="statistics">
						<div>应还款总额<span class="pull-right">{{ statistics.data.plannedAmount | formatMoney }}</span></div>
						<div>实际还款总额<span class="pull-right">{{ statistics.data.actualAmount | formatMoney }}</span></div>
						<div>未偿总额<span class="pull-right">{{ statistics.data.differenceAmount | formatMoney }}</span></div>
					</template>
				</ListStatistics>
				<PageControl
	                v-model="pageConds.pageIndex"
	                :size="dataSource.size"
	                :per-page-record-number="pageConds.perPageRecordNumber">
	            </PageControl>
			</div>
		</div>
        <ExportPreviewModal
            :parameters="conditions"
            :query-action="`/overdueAsset/preview-export-overdue-management`"
            :download-action="`/report/export?reportId=4`"
            v-model="overDueRepaymentExportModal.show">
            <el-table-column label="uniqueid" prop="uniqueId"></el-table-column>
            <el-table-column :label="$utils.locale('financialContract.appAccount.name')" prop="appName"></el-table-column>
            <el-table-column label="贷款合同编号" prop="loanContractNo"></el-table-column>
            <el-table-column label="贷款客户姓名" prop="customerName"></el-table-column>
            <el-table-column label="贷款合同未偿本金" prop="outstandingPrincipalValue"></el-table-column>
            <el-table-column label="贷款合同未偿利息" prop="outstandingInterestValue"></el-table-column>
            <el-table-column label="还款编号" prop="repaymentNo"></el-table-column>
            <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo"></el-table-column>
            <el-table-column label="放款日期" prop="loanDate"></el-table-column>
            <el-table-column label="计划还款日期" prop="assetRecycleDate"></el-table-column>
            <el-table-column label="实际还款日期" prop="actualRecycleDate"></el-table-column>
            <el-table-column label="当前期数" prop="currentPeriod"></el-table-column>
            <el-table-column label="差异天数" prop="daysDifference"></el-table-column>
            <el-table-column label="逾期天数" prop="auditOverdueDays"></el-table-column>
            <el-table-column label="应还款金额" prop="assetFairValue"></el-table-column>
            <el-table-column label="计划还款本金" prop="assetPrincipalValue"></el-table-column>
            <el-table-column label="计划还款利息" prop="assetInterestValue"></el-table-column>
            <el-table-column label="贷款服务费" prop="loanServiceFee"></el-table-column>
            <el-table-column label="技术维护费" prop="loanTechFee"></el-table-column>
            <el-table-column label="其他费用" prop="loanOtherFee"></el-table-column>
            <el-table-column label="逾期罚息" prop="overdueFeePenalty"></el-table-column>
            <el-table-column label="逾期违约金" prop="overdueFeeObligation"></el-table-column>
            <el-table-column label="逾期服务费" prop="overdueFeeService"></el-table-column>
            <el-table-column label="逾期其他费用" prop="overdueFeeOther"></el-table-column>
            <el-table-column label="实收金额" prop="paidUpAssetFairValue"></el-table-column>
            <el-table-column label="实收本金" prop="paidUpPrincipalValue"></el-table-column>
            <el-table-column label="实收利息" prop="paidUpInterestValue"></el-table-column>
            <el-table-column label="实收贷款服务费" prop="paidUpLoanServiceFee"></el-table-column>
            <el-table-column label="实收技术维护费" prop="paidUpLoanTechFee"></el-table-column>
            <el-table-column label="实收其他费用" prop="paidUpLoanOtherFee"></el-table-column>
            <el-table-column label="实收逾期罚息" prop="paidUpOverdueFeePenalty"></el-table-column>
            <el-table-column label="实收逾期违约金" prop="paidUpOverdueFeeObligation"></el-table-column>
            <el-table-column label="实收逾期服务费" prop="paidUpOverdueFeeService"></el-table-column>
            <el-table-column label="实收逾期其他费用" prop="paidUpOverdueFeeOther"></el-table-column>
            <el-table-column label="未偿本金" prop="unPaidPrincipalValue"></el-table-column>
            <el-table-column label="未偿利息" prop="unPaidInterestValue"></el-table-column>
            <el-table-column label="未偿其他" prop="unPaidTotalOtherFee"></el-table-column>
            <el-table-column label="退款金额" prop="refundAmount"></el-table-column>
            <el-table-column label="还款状态" prop="repaymentStatus"></el-table-column>
            <el-table-column label="逾期状态" prop="overDueStatus"></el-table-column>
            <el-table-column label="担保状态" prop="guaranteeStatus"></el-table-column>
            <el-table-column label="备注" prop="comment"></el-table-column>
            <el-table-column label="客户年龄" prop="age"></el-table-column>
            <el-table-column label="开户行所在省" prop="province"></el-table-column>
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
				action: '/overdueAsset/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
                    financialContractUuids: [],
                    paymentStatusOrdinals: [],
                    auditOverDueStatusOrdinals: [],
                    planStartDate: '',
                    planEndDate: '',
                    actualStartDate: '',
                    actualEndDate: '',
                    auditOverdueStatus: '',
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
				paymentStatusList: [],
				auditOverdueStatusList: [],

				overDueRepaymentExportModal: {
					show: false
				},
				repaymentPlanManagementExportModal: {
					show: false
				}
			};
		},
		methods: {
			initialize: function() {
			    return ajaxPromise({
			    	url: `/overdueAsset/options`
			    }).then(data => {
			    	this.financialContractQueryModels = data.queryAppModels || [];
    	            this.paymentStatusList = data.paymentStatusList || [];
    	            this.auditOverdueStatusList = data.auditOverdueStatusList || [];

                    this.queryConds.paymentStatusOrdinals = this.paymentStatusList.map(item => item.key);
                    this.queryConds.auditOverDueStatusOrdinals = this.auditOverdueStatusList.map(item => item.key);
			    }).catch(message => {
			    	MessageBox.open(message);
			    });
			},
            onSuccess: function(data) {
                this.dataSource.list = data.list.map(item => {
                    item.statistics = {
                        planChargesDetail: {},
                        paidUpChargesDetail: {},
                        unPaidDetail: {}
                    };
                    return item;
                });
                this.dataSource.size = data.size;
            },
			handleExport: function(command) {
				this[command + 'ExportModal'].show = true;
			},
            handleCellClick: function(row, column, cell, e) {
                if (window.getSelection && column.property === 'contractNo') {
                    var range = document.createRange();
                    range.selectNode($(e.target)[0]);
                    window.getSelection().addRange(range); // 选择对象
                    document.execCommand('Copy'); // 执行浏览器复制命令
                }
            },
			fetchAssetSetStatistics: function(assetSetUuid) {
				var { list } = this.dataSource;
				var index = list.findIndex(item => item.assetSetUuid === assetSetUuid);

				if (index === -1) return;

                if (list[index].statistics.success) return;

				ajaxPromise({
					url: `/assets/${assetSetUuid}/feeDetail`,
				}).catch(error => {
                    this.$set(list[index], 'statistics', { error });
				}).then(data => {
					this.$set(list[index], 'statistics', Object.assign({
                        success: true,
                        planChargesDetail: {},
                        paidUpChargesDetail: {},
                        unPaidDetail: {}
                    }, data));
				});
			},
		}
	}
</script>
