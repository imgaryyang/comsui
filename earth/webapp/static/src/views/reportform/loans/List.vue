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
					                v-model="queryConds.startDateString"
					                :end-date="queryConds.endDateString"
					                name="startDateString"
					                placeholder="起始日期"
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
					                v-model="queryConds.endDateString"
					                :start-date="queryConds.startDateString"
					                name="endDateString"
					                placeholder="终止日期"
					                size="small">
					            </DateTimePicker>
					        </el-form-item>
					    </el-col>
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
					v-loading="dataSource.fetching"
					:data="dataSource.list"
					:empty-text="dataSource.error">
					<el-table-column
						prop="contractNo"
						:label="$utils.locale('financialContract.no')">
					</el-table-column>
					<el-table-column 
						prop="contractName"
						:label="$utils.locale('financialContract.name')">
					</el-table-column>
					<el-table-column
						inline-template
						label="期初">
						<span>{{ row.beginningLoans | formatMoney }}</span>
					</el-table-column>
					<el-table-column 
						inline-template
						label="本期新增">
						<span>{{ row.newLoans | formatMoney }}</span>
					</el-table-column>
					<el-table-column
						inline-template
						label="本期减少">
						<span>{{ row.reduceLoans | formatMoney }}</span>
					</el-table-column>
					<el-table-column 
						inline-template
						label="期末">
						<span>{{ row.endingLoans | formatMoney }}</span>
					</el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
			<div class="pull-left">
				<el-button
					style="background: #fff" 
					size="small"
					v-if="ifElementGranted('export-loan-scale')"
				    @click="download">
				    导出报表
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
				action: '/reportform/loans/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					endDateString: '',
					startDateString: '',
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
					url: `/reportform/loans/options`
				}).then(data => {
					this.financialContractQueryModels = data.queryAppModels || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			onError: function(message) {
			    this.dataSource.error = message.toString();
			    this.dataSource.list = [];
			},
			download: function() {
				var search = searchify(purify(filterQueryConds(this.queryConds)));
				var action = this.api + '/reportform/loans/exprot' + '?' + search;
				window.open(action, '_download');
			}
		}
	}
</script>