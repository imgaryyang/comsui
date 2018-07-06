<template>
	<Modal v-model="show">
        <ModalHeader title="访问记录近一个月">
        </ModalHeader>
        <ModalBody>
        	<div class="row-layout-detail">
	        	<div class="block">
					<div class="bd">
			        	<el-table
			                :data="dataSource.list"
			                class="td-15-padding th-8-15-padding no-th-border"
			                v-loading="dataSource.fetching"
			                stripe
			                border>
							<el-table-column label="时间" prop="requestTime" inline-template>
			                    <div>{{ row.requestTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
			                </el-table-column>
			                <el-table-column label="来源" prop="sourceStr"></el-table-column>
			                <el-table-column label="IP" prop="ip"></el-table-column>
			            </el-table>
					</div>
				</div>
	            <div class="ft text-align-center">
		            <PageControl 
		                v-model="pageConds.pageIndex"
		                :size="dataSource.size"
		                :per-page-record-number="pageConds.perPageRecordNumber">
		            </PageControl>
		        </div>
        	</div>
        </ModalBody>
	</Modal>
</template>

<script>
    import modalMixin from './modal-mixin';
	import Pagination from 'mixins/Pagination';

	export default {
		mixins: [modalMixin,Pagination],
		components: {
			QueryTable: require('views/include/QueryTable')
		},
		props: {
			uuid: {
				required: true,
				type: String
			}
		},
		data: function() {
			return {
				action: `/keystore/${this.uuid}/log`,
				autoload: false,
				show: this.value,

				pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                },
			}
		},
		computed: {
			conditions: function() {
				return Object.assign({}, this.queryConditions, this.pageConds)
			}
		},
		watch: {
			show: function(current) {
				if (current) {
					this.fetch();
				}
			},
			uuid: function(current) {
				this.action = `/keystore/${current}/log`;
			}
		}
	}
</script>