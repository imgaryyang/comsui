<style lang="sass">
</style>

<template>
	<div class="block table-with-query">
		<div class="query-area" style="background: transparent;padding-left: 0;">
			<el-form class="sdf-form sdf-query-form" :inline="true">
				<el-form-item>
					<ComboQueryBox v-model="comboConds">
						<el-option label="日期" value="statisticsDate"></el-option>
					</ComboQueryBox>
				</el-form-item>
				<el-form-item>
					<el-button size="small" type="primary" @click="onClick">查询</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="bd">
			<el-table
				class="td-15-padding th-8-15-padding no-th-border"
				style="border-top: none;"
				v-loading="waitingResult"
				:data="showDataList"
				stripe>
				<el-table-column label="日期" prop="statisticsDate"></el-table-column>
				<el-table-column :label="quotientDesc" prop="quotient"></el-table-column>
				<el-table-column :label="denominatorDesc" prop="denominator"></el-table-column>
				<el-table-column :label="numeratorDesc" prop="numerator"></el-table-column>
				<el-table-column label="笔数" prop="statisticsNumber" width="70"></el-table-column>
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
	import { debounce } from 'lodash';

	export default {
		components: {
			ComboQueryBox: require('views/include/ComboQueryBox'),
		},
		props: {
			dataSource: {
				type: Object,
				default: function(){
					return {
						list: [],
						size: 0,
					}
				}
			},
            waitingResult: {
                type: Boolean,
                default: false
            },
		},
		data: function() {
			return {
				pageConds: {
					perPageRecordNumber: 5,
					pageIndex: 1
				},
				comboConds: {
					statisticsDate: ''
				},
				dataSourceList: [],
				showDataList: []
			}
		},
		computed: {
			showDataList: function(){
				var pi = this.pageConds.pageIndex
				var start = (pi-1) * this.pageConds.perPageRecordNumber
				var end = pi * this.pageConds.perPageRecordNumber
				return  this.dataSourceList.slice(start,end)
			},
			quotientDesc: function() {
				return this.showDataList[0] ? this.showDataList[0].quotientDesc : ''
			},
			numeratorDesc: function() {
				return this.showDataList[0] ? this.showDataList[0].numeratorDesc : ''
			},
			denominatorDesc: function() {
				return this.showDataList[0] ? this.showDataList[0].denominatorDesc : ''
			}
		},
		watch: {
			'dataSource.list': function(l){
				this.dataSourceList = l
			},
			// 'comboConds.statisticsDate': function(date){
			// 	// this.pageConds.pageIndex = 1
			// 	// this.filterTableData(date)
			// }
		},
		methods: {
			onClick: function(e) {
				this.pageConds.pageIndex = 1
				this.filterTableData(this.comboConds.statisticsDate)
			},
			filterTableData: function(date){
				if (date && date.trim() != '') {
					this.dataSourceList = this.dataSource.list.filter(item => item.statisticsDate.includes(date))
				} else {
					this.dataSourceList = this.dataSource.list
				}
			}
		}
	}
</script>