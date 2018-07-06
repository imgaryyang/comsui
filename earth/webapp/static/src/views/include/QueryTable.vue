<style lang="sass">
	#query-table-detail {
		.query-area {
			padding: 5px 20px;
		    border: 1px solid #d1d1d1;
		    border-bottom: 0;
		}
	}
</style>

<template>
	<div id="query-table-detail">
		<div class="block">
			<h5 class="hd"> {{ title }}</h5>
			<div class="bd">
				<div class="query-area">
					<el-form class="sdf-form sdf-query-form" :inline="true">
	                    <slot name="query-form"></slot>
	                     <el-form-item>
                            <el-button v-if="querybtn" size="small" type="primary" @click="fetch">查询</el-button>
                        </el-form-item>
	                </el-form>
				</div>
				<table v-if="isEleTable" class="el-table el-table__body">
					<slot name="custom-table" :data="dataSource.list"></slot>
				</table>
				<el-table
	                :data="dataSource.list"
	                class="td-15-padding th-8-15-padding no-th-border"
	                v-loading="dataSource.fetching"
	                :context="$parent"
	                stripe
	                border
				    v-else>
	                <slot name="table"></slot>
	            </el-table>
			</div>
			<div class="ft text-align-center">
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
	//带有查询功能的table
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import Pagination from 'mixins/Pagination';

	export default {
		mixins: [Pagination],
		props: {
			action: {
				type: String,
				default: '',
				required: true
			},
			autoload: {
				type: Boolean,
				default: false
			},
			title: {
				type: String,
				default: ''
			},
			value: {
				type: Boolean,
				default: false
			},
			queryConditions: {
				type: Object,
				default: () => ({})
			},
			pageIndex: {
				default: 1
			},
			perPageRecordNumber: {
				default: 12
			},
			querybtn: {
				type: Boolean,
				default: true
			},
            isEleTable: {
			    type: Boolean,
				default: false
			},
		},
		watch: {
			value: function(current) {
				if (current) {
					this.fetch();
				}
			},
			pageIndex: function(current) {
				this.pageConds.pageIndex = current;
			},
			perPageRecordNumber: function(current) {
				this.pageConds.perPageRecordNumber = current;
			}
		},
		data: function() {
			return {
				pageConds: {
                    pageIndex: this.pageIndex,
                    perPageRecordNumber: this.perPageRecordNumber
                },
                hasExpandList: []
			}
		},
		computed: {
			conditions: function() {
				return Object.assign({}, this.queryConditions, this.pageConds)
			}
		},
		methods: {
			fetch: function() {
				this.getData({
				    url: this.action,
				    data: this.conditions
				});
			},
			onSuccess: function(data) {
	            this.dataSource.list = data.list;
	            this.dataSource.size = data.size;
			},
			onComplete: function() {
                if (this.value) {
                    this.$emit('input', false);
                }
                this.dataSource.fetching = false;
            },
            clearList: function() {
            	this.dataSource.list = [];
            }
		}
	}
</script>