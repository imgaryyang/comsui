<style lang="sass">
	
</style>

<template>
	<div class="content">
		<div class="scroller">
			<div class="query-area">
				<el-form class="sdf-form sdf-query-form" :inline="true">
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option value="appId" label="信托商户代码"></el-option>
							<el-option value="companyFullName" label="商户公司全称"></el-option>
							<el-option value="appName" label="商户简称"></el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
					<el-form-item>
						<a  class="el-button el-button--primary el-button--small" 
                            :href="`${ctx}#/financial/customer/financial-app/create`">新增</a>
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
					<el-table-column label="信托商户代码" inline-template>
						<a :href="`${ctx}#/financial/customer/financial-app/${row.appId}/detail`">{{ row.appId }}</a>
					</el-table-column>
					<el-table-column label="商户公司全称" inline-template>
						<div>{{ row.company.fullName }}</div>
					</el-table-column>
					<el-table-column label="商户简称" inline-template>
						<div>{{ row.name }}</div>
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
				action: '/app/search',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				
				comboConds: {
					appId: '',
					companyFullName: '',
					appName: ''
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},
			}
		},
	}
</script>