<style type="sass">
</style>

<template>
	<div>
		<div class="bd">
			<el-table
				:data="showList"
				v-loading="loading"
	            class="td-15-padding th-8-15-padding no-th-border"
	            stripe
	            border>
	            <slot></slot>
			</el-table>
		</div>
		<div class="ft text-align-center" style="margin-bottom:15px" v-if="pagination && data && data.length > perPageRecordNumber">
	        <PageControl 
	            v-model="pageIndex"
	            :size="data.length"
	            :per-page-record-number="perPageRecordNumber">
	        </PageControl>
	    </div>
	</div>
</template>

<script>
	// 前端分页，接受父节点传递进来的所有数据
	export default {
		props: {
			data: {
				type: Array,
				default: () => {[]}
			},
			perPageRecordNumber: {
				default: 5,
				type: Number
			},
			pagination: {
				default: false
			},
			loading: {
				default: false
			}
		},
		watch: {
			data: function() {
				this.pageIndex = 1;
			}
		},
		computed: {
			showList: function() {
				var { pageIndex, perPageRecordNumber, pagination } = this;
				var start = (pageIndex - 1) * perPageRecordNumber;
				var end = start + perPageRecordNumber;
				return pagination && this.data ? this.data.slice(start, end) : this.data;
			}
		},
		data: function() {
			return {
				pageIndex: 1
			};
		},
	}
</script>