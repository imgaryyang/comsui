<style lang="sass">
	
</style>

<template>
	<div class="block">
		<h5 class="hd"> {{ title }}</h5>
		<div class="bd">
			<el-table
                :data="dataSource.list"
                class="td-15-padding th-8-15-padding no-th-border"
                v-loading="dataSource.fetching"
                stripe
                border>
                <slot></slot>
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
</template>

<script>
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
			autoload: false,
			title: {
				type: String,
				default: ''
			},
			orderUuid: [Number, String],
			value: {
				type: Boolean,
				default: false
			}
		},
		watch: {
			value: function(current) {
				if (current) {
					this.fetch();
				}
			}
		},
		data: function() {
			return {
				pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                }
			}
		},
		computed: {
			conditions: function() {
				return Object.assign({orderUuid: this.orderUuid}, this.pageConds)
			}
		},
		methods: {
			fetch: function() {
				if (this.orderUuid) {
					this.getData({
					    url: this.action,
					    data: this.conditions
					});
				}
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
		}
	}
</script>