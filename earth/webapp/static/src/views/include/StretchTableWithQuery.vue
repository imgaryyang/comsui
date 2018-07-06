<style lang="sass">
	
</style>

<template>
	<div>
		<div class="block">
			<h5 class="hd" v-if="title">{{title}}<template v-if="titleShowSize">({{dataSource.size}})</template></h5>
			<slot name="title" v-else></slot>
            <div class="bd">
            	<el-table
                    :data="showDataList"
                    key="stretchTableWithQuery"
                    class="td-15-padding th-8-15-padding no-th-border"
                    v-loading="dataSource.fetching"
                    stripe
                    border>
                    <slot></slot>
                </el-table>
            </div>
            <div class="ft text-align-center" v-if="dataSource.size">
                <a href="javascript:void(0)" class="drawer" @click="isShowPageControl = !isShowPageControl">
                    <span class="msg">{{ drawerMsg }}</span>
                    <i class="icon icon-up-down"  :class="{active: isShowPageControl}"></i>
                </a>
                <PageControl 
                    v-model="pageConds.pageIndex"
                    v-if="isShowPageControl"
                    :size="dataSource.size"
                    :per-page-record-number="pageConds.perPageRecordNumber">
                </PageControl>
            </div>
		</div>
	</div>
</template>

<script>
	//带有展开，收起功能的列表,带有异步请求
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import Pagination from 'mixins/Pagination';
	export default {
		mixins: [Pagination],
		props: {
			type: {
				default: 'get'
			},
			action: {
				type: String,
				default: '',
				required: true
			},
			autoload: false,
			title: {
				type: String,
				default: ''
			},
			titleShowSize: {
				default: false
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
			defaultShowNumber: {
				type: Number,
				default: 3
			},
		},
		data: function() {
			return {
				isShowPageControl: false,
				pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                },
			}
		},
		watch: {
			value: function(current) {
				if (current) {
					this.isShowPageControl = false;
					this.pageConds = {
	                    pageIndex: 1,
	                    perPageRecordNumber: 12
	                };
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
		computed: {
			drawerMsg: function() {
				return this.isShowPageControl ? '收起' : '展开';
			},
			showDataList: function() {
				return this.isShowPageControl ? this.dataSource.list : this.dataSource.list.slice(0,this.defaultShowNumber);
			},
            conditions: function() {
				return Object.assign({}, this.queryConditions, this.pageConds)
			}
		},
		methods: {
			onComplete: function() {
                if (this.value) {
                    this.$emit('input', false);
                }
                this.dataSource.fetching = false;
            }
		}
	}
</script>