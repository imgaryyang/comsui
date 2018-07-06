<style lang="sass">
    @import '~assets/stylesheets/base';

    .export-modal {

    	.el-table th > .cell,
    	.el-table td > .cell {
    		white-space: nowrap;
    	}

        @include min-screen(768px) {
            .modal-dialog {
                width: 85%;
                margin: 30px auto;
            }
        }

    }
</style>

<template>
	<Modal v-model="visible" class="export-modal">
		<ModalHeader :title="title">
		</ModalHeader>
		<ModalBody v-loading="dataSource.fetching">
			<div class="row-layout-detail">
				<div class="block">
					<div class="bd">
						<PagingTable
							v-if="pagination"
							:pagination="pagination"
							:perPageRecordNumber="12"
							:data="dataSource.list">
							<slot></slot>
						</PagingTable>
						<el-table
							v-else
							stripe
							border
							style="width: 100%"
							:data="dataSource.list"
							class="td-15-padding th-8-15-padding">
							<slot></slot>
						</el-table>
					</div>
				</div>
			</div>
			<div style="height:20px">
				<el-checkbox v-model="exportTags" v-if="canExportTags" class="pull-left" style="margin-left:20px">导出标签</el-checkbox>
			</div>
		</ModalBody>
		<ModalFooter>
			<el-button @click="visible = false" type="default">取消</el-button>
			<el-button @click="download" :disabled="dataSource.list.length === 0" type="success">下载</el-button>
		</ModalFooter>
	</Modal>
</template>

<script>
	import { purify, searchify,downloadFile } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import Pagination from 'mixins/Pagination';
	import PagingTable from './PagingTable';

	export default {
		mixins: [Pagination],
		components: {
			PagingTable
		},
		props: {
			value: Boolean,
			queryAction: {
				required: true,
				type: String
			},
			downloadAction: {
				required: true,
				type: String	
			},
			parameters: {
				default: () => ({})
			},
			autoload: {
				default: false
			},
			pagination: {
				default: false
			},
			handlerExport: {
				default: false
			},
			canExportTags: {
				default: false
			},
			title: {
				default: '导出预览'
			}
		},
		watch: {
			visible: function(current) {
				this.$emit('input', current);

				if (current) {
					this.exportTags = false;
					this.fetch();
				}
			},
			value: function(current) {
				this.visible = current;
			},
			queryAction: function(current) {
				this.action = current;
			}
		},
		data: function() {
			return {
				fetching: false,
				visible: this.value,
				exportTags: false
			}
		},
		methods: {
			fetch: function() {
				if (!this.visible) return;
				this.getData({
				    url: this.queryAction,
				    data: this.parameters
				});
			},
			download: function() {
				var parameters;
				if (this.canExportTags) {
					parameters = Object.assign(this.parameters, {exportTags: this.exportTags});
				} else {
					parameters = this.parameters;
				}
				this.handlerExport ? this.$emit('handlerExport') : downloadFile(this.downloadAction, parameters);
			}
		}
	}
</script>