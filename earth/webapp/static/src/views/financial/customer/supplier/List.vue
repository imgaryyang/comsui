<style lang="sass">
	
</style>

<template>
	<div class="content">
		<div class="scroller">
			<div class="query-area">
				<el-form class="sdf-form sdf-query-form" :inline="true">
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option value="supplierName" label="供应商名称"></el-option>
							<el-option value="supplierUuid" label="供应商编号"></el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
					<el-form-item>
						<a  class="el-button el-button--primary el-button--small" 
                            :href="`${ctx}#/financial/customer/supplier/create`">新增</a>
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
					<el-table-column label="供应商编号" prop="uuid" inline-template>
						<a :href="`${ctx}#/financial/customer/supplier/${row.uuid}/detail`">{{ row.uuid }}</a>
					</el-table-column>
					<el-table-column label="供应商名称" prop="name">
					</el-table-column>
					<el-table-column label="银行账号" prop="accountNo">
					</el-table-column>
					<el-table-column label="账户开户行" prop="bankName">
					</el-table-column>
					<el-table-column label="开户行所在地" inline-template>
						<div>{{ row.province }} {{ row.city }}</div>
					</el-table-column>
					<el-table-column label="创建日期" inline-template prop="createTimeString" sortable="custom">
						<div>{{ row.createTime | formatDate }}</div>
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
			ComboQueryBox: require('views/include/ComboQueryBox'),
		},
		data: function() {
			return {
				action: '/supplier/search',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				
				comboConds: {
					supplierUuid: '',
					supplierName: ''
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},
			}
		},
	}
</script>