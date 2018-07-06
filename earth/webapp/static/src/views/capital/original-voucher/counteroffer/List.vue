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
						<el-select 
						    v-model="queryConds.tradeBatchState"
						    size="small" 
						    clearable
						    placeholder="回盘状态">
						    <el-option
						        v-for="(value, label) in tradeBatchState"
								:label="value"
								:value="label">
						    </el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select 
						    v-model="queryConds.arrivalState"
						    size="small" 
						    clearable
						    placeholder="到账状态">
						    <el-option
						        v-for="(value, label) in arrivalState"
								:label="value"
								:value="label">
						    </el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-col :span="11">
							<el-form-item>
								<DateTimePicker
									v-model="queryConds.plannedStartDate"
									:end-date="queryConds.plannedEndDate"
									placeholder="导入起始日"
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
									placeholder="导入终止日"
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
							<el-option value="externalBatchNo" label="批次号"></el-option>
							<el-option value="financialContractName" label="回盘编号"></el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
					<el-form-item>
						<el-button size="small" type="success" @click="importExcel.show = true" v-if="ifElementGranted('import-counter-offer-excel')">导入</el-button>
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
					<el-table-column 
						inline-template 
						label="回盘编号">
						<a :href="`${ctx}#/capital/original-voucher/counteroffer/${row.externalTradeBatchUuid}/detail`">{{ row.externalTradeBatchUuid }}</a>
					</el-table-column>
					<el-table-column label="批次号" prop="externalBatchNo"></el-table-column>
					<el-table-column :label="$utils.locale('financialContract')" prop="financialContractName"></el-table-column>
					<el-table-column 
						inline-template
						label="总金额" 
						prop="totalAmount"
						sortable="custom">
						<div>{{ row.totalAmount | formatMoney }}</div>
					</el-table-column>
					<el-table-column
						inline-template
						width="150px"
						label="三方代扣金额" 
						prop="deductAmount"
						sortable="custom">
						<div>{{ row.deductAmount | formatMoney }}</div>
					</el-table-column>
					<el-table-column
						inline-template
						label="转账总额" 
						prop="onlineTransferAmount"
						sortable="custom">
						<div>{{ row.onlineTransferAmount | formatMoney }}</div>
					</el-table-column>
					<el-table-column 
						inline-template
						label="现金总额" 
						prop="offlineCashAmount"
						sortable="custom">
						<div>{{ row.offlineCashAmount | formatMoney }}</div>
					</el-table-column>
					<el-table-column label="总数量" prop="totalDetailNumber"></el-table-column>
					<el-table-column label="验真成功数量" prop="positiveAuditNumber"></el-table-column>
					<el-table-column label="验真失败数量" prop="negativeAuditNumber"></el-table-column>
					<el-table-column
						inline-template 
						label="导入时间"
						sortable="custom"
						prop="createTime">
						<div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
					</el-table-column>
					<el-table-column label="回盘状态" prop="tradeBatchState"></el-table-column>
					<el-table-column label="清算状态" prop="arrivalState"></el-table-column>
					<el-table-column 
						inline-template 
						label="操作">
						<a 
							v-if="arrivalStateJudge(row)"
							href="javascript:void(0)" 
							@click="handleAddCashFlow(row)">
							添加流水
						</a>	
					</el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
			<div class="pull-right">
				<PageControl 
	                v-model="pageConds.pageIndex"
	                :size="dataSource.size"
	                :per-page-record-number="pageConds.perPageRecordNumber">
	            </PageControl>
			</div>
		</div>
		<ImportExcelModal 
			v-model="importExcel.show"
			:financialContractQueryModels="financialContractQueryModels"
			@submit="onSubmitImportExcel">
		</ImportExcelModal>
		<AddCashFlow 
			v-model="addCashFlow.show"
			:externalTradeBatchUuid="addCashFlow.externalTradeBatchUuid"
			@submit="onSubmitAddCashFlow">
		</AddCashFlow>
	</div>
</template>

<script>
	import Pagination from 'mixins/Pagination';
	import ListPage from 'mixins/ListPage';
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		mixins: [Pagination,ListPage],
		components: { 
			ComboQueryBox: require('views/include/ComboQueryBox'),
			ImportExcelModal: require('./include/ImportExcelModal'),
			AddCashFlow: require('./include/AddCashFlow')
		},
		data: function() {
			return {
				action: '/trade/external-batch/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractIds: [],
					tradeBatchState: '',
					arrivalState: '',
					plannedStartDate: '',
					plannedEndDate: '',
				},
				comboConds: {
					externalBatchNo: '',
					financialContractName: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

				arrivalState: [],
				financialContractQueryModels: [],
				tradeBatchState: [],

				importExcel:{
					show: false,
				},
				addCashFlow: {
					show: false,
					externalTradeBatchUuid: ''
				},

				detail: {}
			};
		},
		methods: {
			initialize: function() {
			    return this.getOptions();
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/trade/external-batch/optionData`
				}).then(data => {
					this.arrivalState = data.arrivalState || [];
					this.tradeBatchState = data.tradeBatchState || [];
			    	this.financialContractQueryModels = data.queryAppModels || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			arrivalStateJudge: function(batchInfo) {
				if (batchInfo.externalTradeActiveStatus == 1) {
				    return false;
				}
				if (batchInfo.externalTradeVerifyResult == 2) {
				    return false;
				}
				return ['未结清'].includes(batchInfo.arrivalState) && [0, 2].includes(batchInfo.externalTradeAuditProgress);
			},
			onSubmitImportExcel: function() {
				this.importExcel.show = false;
				this.fetch();
			},
			onSubmitAddCashFlow: function() {
				this.fetch();
				this.addCashFlow.show = false;
			},
			handleAddCashFlow: function(data) {
				this.addCashFlow.externalTradeBatchUuid = data.externalTradeBatchUuid;
				this.addCashFlow.show = true;
			}
		}
	}
</script>