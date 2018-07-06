<style lang="sass">
	.table-with-query {
		margin-top: 10px;

		.query-area {
			background: #FFFFFF;
			padding: 6px 0px;
			border-top-color: #8FB9F8;
		    border-top-style: dashed;
		    border-top-width: 1px; 
	        border-bottom: 0px;
		}

		.el-button--primary {
			background: #84b4fd;
			border-color: #84b4fd;
			color: white;

			&:hover {
				background: #8FB9F8;
				border-color: #8FB9F8;
				color: white;
			}
		}
	}
</style>

<template>
	<div class="block table-with-query">
		<div class="query-area">
			<el-form class="sdf-form sdf-query-form" :inline="true">
				<el-form-item>
					<ComboQueryBox v-model="comboConds">
						<el-option label="贷款合同编号" value="contractNo"></el-option>
					</ComboQueryBox>
				</el-form-item>
				<el-form-item>
					<el-button size="small" type="primary">查询</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="bd">
			<el-table
				class="td-15-padding th-8-15-padding no-th-border"
				v-loading="dataSource.fetching"
				:data="dataSource.list"
				stripe>
				<el-table-column label="事件" prop=""></el-table-column>
				<el-table-column label="时间" prop=""></el-table-column>
				<el-table-column label="二级科目" prop=""></el-table-column>
				<el-table-column label="方向" prop=""></el-table-column>
				<el-table-column label="机构" prop=""></el-table-column>
			</el-table>
		</div>
		<div class="ft clearfix">
			<PageControl
				v-model="pageConds.pageIndex"
				:size="dataSource.size"
				:per-page-record-number="pageConds.perPageRecordNumber">
			</PageControl>
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
			ComboQueryBox: require('views/include/ComboQueryBox')
		},
		props: {
			action: {
				type: String
			},
			autoload: {
				default: false
			}
		},
		data: function() {
			return {
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				comboConds: {
					contractNo: '',
				},
			}
		},
		methods: {

		}
	}
</script>