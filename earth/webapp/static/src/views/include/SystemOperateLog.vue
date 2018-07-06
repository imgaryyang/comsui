<template>
	<div class="system-operate-log">
		<h5 class="hd">{{ title }}</h5>
		<div class="bd">
			<el-table 
				class="td-15-padding th-8-15-padding no-th-border"
				:data="dataSource.list"
				v-loading="dataSource.fetching"
				stripe
				border>
			    <el-table-column width="60px" type="index" label="序号">
			    </el-table-column>

			    <el-table-column width="150px" prop="occurTime" label="操作发生时间">
			    </el-table-column>

			    <el-table-column prop="operateName" label="操作员登录名">
			    </el-table-column>

			    <el-table-column prop="recordContent" label="操作内容">
			    </el-table-column>
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

	export default {
		mixins: [Pagination],
		props: {
			forObjectUuid: [String, Number],
			title: {
				default: '操作日志'
			}
		},
		data: function() {
			return {
				action: `/system-operate-log/query`,
				pageConds: {
					pageIndex: 1,
					perPageRecordNumber: 5
				}
			}
		},
		computed: {
			queryConds: function() {
				return {
					objectUuid: this.forObjectUuid
				};
			},
		    conditions: function() {
		        return Object.assign({}, this.queryConds, this.pageConds);
		    }
		},
		methods: {
			fetch: function() {
				if (!this.conditions.objectUuid) return;
			    this.getData({
			        url: this.action,
			        data: this.conditions
			    });
			},
		}
	}
</script>