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
					                v-model="queryConds.loanEffectStartDate"
					                :end-date="queryConds.loanEffectEndDate"
					                name="loanEffectStartDate"
					                placeholder="生效起始日期"
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
					                v-model="queryConds.loanEffectEndDate"
					                :start-date="queryConds.loanEffectStartDate"
					                name="loanEffectEndDate"
					                placeholder="生效终止日期"
					                size="small">
					            </DateTimePicker>
					        </el-form-item>
					    </el-col>
					</el-form-item>
					<el-form-item>
					    <el-col :span="11">
					        <el-form-item>
					            <DateTimePicker
					                v-model="queryConds.loanExpectTerminateStartDate"
					                :end-date="queryConds.loanExpectTerminateEndDate"
					                name="loanExpectTerminateStartDate"
					                placeholder="预计终止起始日期"
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
					                v-model="queryConds.loanExpectTerminateEndDate"
					                :start-date="queryConds.loanExpectTerminateStartDate"
					                name="loanExpectTerminateEndDate"
					                placeholder="预计终止终止日期"
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
							<el-option value="contractNo" label="贷款合同编号"></el-option>
							<el-option value="customerName" label="客户姓名"></el-option>
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
					<el-table-column
						inline-template
						label="贷款合同编号">
						<a :href="`${ctx}#/data/contracts/detail?id=${ row.contractId}`">{{ row.contractNo }}
						</a>
					</el-table-column>
					<el-table-column
						inline-template
						label="贷款利率"
						sortable="custom">
						<div>{{ row.loanRate | formatPercent }}</div>
					</el-table-column>
					<el-table-column 
						inline-template
						label="生效日期">
						<div>
							{{ row.effectDate | formatDate}}
						</div>
					</el-table-column>
					<el-table-column 
						inline-template 
						label="预计终止日期">
						<div>{{ row.expectTerminalDate | formatDate }}</div>
					</el-table-column>
					<el-table-column 
						inline-template 
						label="实际终止日期">
						<div>{{ row.actualTermainalDate }}</div>
					</el-table-column>
					<el-table-column prop="repaymentSchedule" label="还款进度"></el-table-column>
					<el-table-column prop="loanType" label="贷款方式"></el-table-column>
					<el-table-column 
						inline-template 
						label="还款周期">
						<div>{{ row.repaymentCycle }}月付</div>
					</el-table-column>
					<el-table-column 
						inline-template 
						label="还款日期">
						<div>{{ row.repaymentDate }}</div>
					</el-table-column>
					<el-table-column 
						prop="customerName"
						label="客户姓名">
					</el-table-column>
					<el-table-column 
						inline-template 
						label="贷款总额">
						<div>{{ row.loanAmount | formatMoney }}</div>
					</el-table-column>
					<el-table-column 
						inline-template 
						label="本期还款金额">
						<div>{{ row.currentPeriodRepaymentAmount | formatMoney }}</div>
					</el-table-column>
					<el-table-column 
						inline-template 
						label="本期还款利息">
						<div>{{ row.currentPeriodRepaymentInterest | formatMoney}}</div>
					</el-table-column>
					<el-table-column 
						prop="repaymentSituation"
						label="还款情况">
					</el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
			<div class="pull-left">
				<el-button 
					style="background: #fff"
					size="small"
					v-if="ifElementGranted('export-project-info')"
				    @click="download">
				    导出项目信息
				</el-button>
			</div>
			<div class="pull-right">
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
	import { ajaxPromise, purify, searchify, filterQueryConds } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		mixins: [Pagination, ListPage],
		components: { 
			ComboQueryBox: require('views/include/ComboQueryBox'),
		},
		data: function() {
			return {
				action: '/project-information/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					loanEffectStartDate: '',
					loanEffectEndDate: '',
					loanExpectTerminateEndDate:'',
					loanExpectTerminateStartDate: '',
				},

				comboConds: {
					contractNo: '',
					underlyingAsset: '',
					customerName: ''
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

				financialContractQueryModels: [],
			};
		},
		methods: {
			initialize: function() {
			    return this.getOptions();
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/project-information/options`
				}).then(data => {
					this.financialContractQueryModels = data.queryAppModels || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			download: function() {
				var search = searchify(purify(filterQueryConds(this.queryConds)));
				var action = this.api + '/project-information/export-excel' + '?' + search;
				console.log(action);
				window.open(action, '_download');
			}
		}
	}
</script>