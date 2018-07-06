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
						    @getFlatCollection="getFlatCollection(arguments[0])"
						    :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
					</el-form-item>
					<el-form-item>
						<el-select size="small" v-model="queryConds.available" placeholder="状态" clearable>
							<el-option label="已激活" value="true"></el-option>
							<el-option label="待激活" value="false"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-col :span="11">
							<el-form-item>
								<DateTimePicker
									v-model="queryConds.startDate"
									:end-date="queryConds.endDate"
									placeholder="状态变更起始时间"
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
									v-model="queryConds.endDate"
									:start-date="queryConds.startDate"
									placeholder="状态变更终止时间"
									size="small">
								</DateTimePicker>
							</el-form-item>
						</el-col>
					</el-form-item>
					<el-form-item>
						<span class="item vertical-line"></span>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option value="loanBatchCode" label="导入批次号"></el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
					<el-form-item v-if="ifElementGranted('import-asserts-package')">
						<el-button size="small" type="success" @click="importAssetMoal.visible = true">
							<i class="glyphicon glyphicon-plus"></i> 导入资产包
						</el-button>
					</el-form-item>
				</el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border"
					stripe
					v-loading="dataSource.fetching"
					:data="dataSource.list">
					<el-table-column label="导入批次号" inline-template>
						<a href="#" @click.prevent="showLogModal(row)">{{ row.loanBatchCode }}</a>
					</el-table-column>
					<el-table-column label="请求批次编号" prop="requestBatchNo"></el-table-column>
					<el-table-column :label="$utils.locale('financialContract.no')" prop="contractNo"></el-table-column>
					<el-table-column min-width="120px" label="贷款合同号段" inline-template>
						<div>{{ row.loanBatchFromRange }}~{{ row.loanBatchThruRange }}</div>
					</el-table-column>
					<el-table-column label="总条数" prop="loanBatchTotalContractNum"></el-table-column>
					<el-table-column label="本金总额" inline-template>
						<div>{{ row.loanBatchTotalAmount | formatMoney }}</div>
					</el-table-column>
					<el-table-column label="状态变更时间" inline-template>
						<div>{{ row.dateTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
					</el-table-column>
					<el-table-column label="状态" inline-template>
						<div>
							<span v-if="row.available">已激活</span>
							<span v-else class="color-danger">待激活</span>
						</div>
					</el-table-column>
					<el-table-column width="150px" label="操作" inline-template>
						<div>
							<a href="#" @click.prevent="exportRepaymentPlan(row)" v-if="ifElementGranted('export-batch-repaymentplan')">导出还款计划</a>
							<template v-if="!row.available">
								<a href="#" @click.prevent="activiteRepaymentPlan(row)" v-if="ifElementGranted('activate-asset-package')">激活</a>
								<a href="#" @click.prevent="deleteRepaymentPlan(row)" v-if="ifElementGranted('delete-asset-package')">删除</a>
							</template>
						</div>
					</el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
			<div class="pull-left">
				<a :href="`${ctx}#/data/loan-batch/appendix` + '?t=' + Date.now()" class="el-button el-button--primary el-button--small" >附件导入</a>
			</div>
			<div class="pull-right">
				<PageControl 
	                v-model="pageConds.pageIndex"
	                :size="dataSource.size"
	                :per-page-record-number="pageConds.perPageRecordNumber">
	            </PageControl>
			</div>
		</div>

		<ExportPreviewModal
            :parameters="exportModal.parameters"
            :download-action="exportModal.downloadAction"
            :query-action="exportModal.queryAction"
			v-model="exportModal.visible">
			<el-table-column label="uniqueid" prop="uniqueId" min-width="150px"></el-table-column>
			<el-table-column label="还款日期" prop="repaymentDate"></el-table-column>
			<el-table-column label="返还收益" prop="repatriatedEarnings"></el-table-column>
			<el-table-column label="计划本金/计算本金" prop="principal" width="130px"></el-table-column>
			<el-table-column label="返还本金" prop="repatriatedPrincipal"></el-table-column>
			<el-table-column label="贷款服务费" prop="loanServiceFee"></el-table-column>
			<el-table-column label="技术维护费" prop="techMaintenanceFee"></el-table-column>
			<el-table-column label="其他费用" prop="otherFee"></el-table-column>
			<el-table-column label="逾期罚息" prop="overduePenalty"></el-table-column>
			<el-table-column label="逾期违约金" prop="overdueDefaultFee"></el-table-column>
			<el-table-column label="逾期服务费" prop="overdueServiceFee"></el-table-column>
			<el-table-column label="逾期其他费用" prop="overdueOtherFee"></el-table-column>
			<el-table-column label="返还种类" prop="repatriatedType"></el-table-column>
			<el-table-column label="收益类别" prop="earningsType"></el-table-column>
			<el-table-column label="还款方式" prop="repaymentWay"></el-table-column>
			<el-table-column label="开始日期" prop="startDate"></el-table-column>
			<el-table-column label="结束日期" prop="endDate"></el-table-column>
			<el-table-column label="利率" prop="interestRate"></el-table-column>
			<el-table-column label="计息天数" prop="interestDays"></el-table-column>
			<el-table-column label="公式算法" prop="formula"></el-table-column>
			<el-table-column label="备注信息" prop="comment"></el-table-column>
		</ExportPreviewModal>

		<ImportAssetMoal 
			v-model="importAssetMoal.visible"
			:financialContractQueryModels="financialContractQueryModels">
		</ImportAssetMoal>

		<RemittanceBatchInfoModal
			v-model="remittanceBatchInfoModal.visible"
			:loan-batch-uuid="remittanceBatchInfoModal.loanBatchUuid">
		</RemittanceBatchInfoModal>

		<!-- <Dashboard :value="true"></Dashboard> -->
	</div>

</template>

<script>
	import Pagination from 'mixins/Pagination';
	import ListPage from 'mixins/ListPage';
	import { ajaxPromise, filterQueryConds } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		mixins: [Pagination, ListPage],
		components: { 
			Dashboard: require('views/include/Dashboard/Dashboard'),
			ExportPreviewModal: require('views/include/ExportPreviewModal'),
			RemittanceBatchInfoModal: require('./RemittanceBatchInfoModal'),
			ImportAssetMoal: require('./ImportAssetMoal'),
			ComboQueryBox: require('views/include/ComboQueryBox'),
		},
		data: function() {
			return {
				action: '/loan-batch/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					available: '',
					startDate: '',
					endDate: ''
				},
				comboConds: {
					loanBatchCode: ''
				},

				remittanceBatchInfoModal: {
					visible: false,
					loanBatchUuid: ''
				},
				importAssetMoal: {
					visible: false
				},
				exportModal: {
					parameters: {},
					downloadAction: '',
					queryAction: '',
					visible: false
				},

				financialContractQueryModels: [],
				flatCollection: []
			};
		},
		activated: function() {
			this.setRedirectWatch();
		},
		methods: {
			initialize: function() {
                return this.getOptions();
            },
			showLogModal: function(row) {
				this.remittanceBatchInfoModal.loanBatchUuid = row.loanBatchUuid;
				this.remittanceBatchInfoModal.visible = true;
			},
			exportRepaymentPlan: function(row) {
				this.exportModal.parameters = filterQueryConds(this.queryConds);
				this.exportModal.downloadAction = `/report/export?reportId=2&loanBatchId=${ row.loanBatchId }`;
				this.exportModal.queryAction =  `/loan-batch/preview-export-repayment-plan/${ row.loanBatchId }`;
				this.exportModal.visible = true;
			},
			deleteRepaymentPlan: function(row) {
				MessageBox.open(`确认删除？${row.loanBatchId}`, null, [{
					text: '取消',
					handler: () => MessageBox.close()
				}, {
					text: '确定',
					type: 'success',
					handler: () => {
						ajaxPromise({
							url: `/loan-batch/delete-loan-batch`,
							type: 'post',
							data: {
								loanBatchId: row.loanBatchId,
								code: row.code
							},
							parse: (data, resp) => resp.message
						}).then(message => {
							MessageBox.open(message);
							this.fetch();
							setTimeout(() => MessageBox.close(), 1000);
						}).catch(message => {
							MessageBox.open(message);
						});
					}
				}]);
			},
			activiteRepaymentPlan: function(row) {
				MessageBox.open('确认激活？', null, [{
					text: '取消',
					handler: () => MessageBox.close()
				}, {
					text: '确定',
					type: 'success',
					handler: () => {
						ajaxPromise({
							url: `/loan-batch/activate`,
							type: 'post',
							data: {
								loanBatchId: row.loanBatchId,
								code: row.code
							},
							parse: (data, resp) => resp.message
						}).then(message => {
							MessageBox.open(message);
							this.fetch();
							setTimeout(() => MessageBox.close(), 1000);
						}).catch(message => {
							MessageBox.open(message);
						});
					}
				}]);
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/loan-batch/options`
				}).then(data => {
					this.financialContractQueryModels = data.queryAppModels || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			getFlatCollection: function(data) {
				this.flatCollection = data;
				this.setRedirectWatch();
			},
			setRedirectWatch: function() {
				var self = this;
				if (this.$route.query.isRedirect) {
					if (!this.$route.query.financialContractUuids) return;
					var financialContractUuid = JSON.parse(this.$route.query.financialContractUuids)[0];
					this.flatCollection.forEach(item => {
						if (item.value == financialContractUuid) {
							setTimeout(() => {
								self.queryConds.financialContractUuids = [item];
							}, 0);
						}
					})
				}
			}
		}
	}
</script>
