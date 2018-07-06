c<style lang="sass" scoped>
	.margin{
		margin:0 0 68px 150px;
		width: 900px;
	}
	.text-content{
		width: 350px;
		overflow-wrap: break-word;
		word-break: break-word;
	}
</style>
<template>
	<div class="content" id="tagManageDetail">
		<div class="scroller" v-loading="fetching">
			<Breadcrumb :routes="[{ title: '标签详情'}]"></Breadcrumb>
			<el-form ref="form"
			:model="fields"
			label-width="100px"
			class="sdf-form"
			@submit.native.prevent
			style="margin:40px 0 40px 118px">
				<el-form-item style="padding: 5px 0;" label="标签信息" class="form-item-legend"></el-form-item>
				<el-form-item label="标签名称" prop="name" required>
					<div class="text-content">{{fields.name}}</div>
				</el-form-item>
				<el-form-item label="标签描述" prop="description">
					<div class="text-content">{{fields.description}}</div>
				</el-form-item>
			</el-form>

			<div class="block margin">
				<h5 class="hd">数据标签</h5>
				<div class="bd">
					<el-table
		                :data="filterDataSource"
		                v-loading="dataSource.fetching"
		                class="td-15-padding th-8-15-padding no-th-border"
		                stripe
		                border>
		                <el-table-column label="序号" type="index" width="100"></el-table-column>
		                <el-table-column label="单据编号" prop="outerIdentifier"></el-table-column>
		                <el-table-column label="单据类型" prop="typeCn"></el-table-column>
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

			<div class="block margin">
				<SystemOperateLog
					ref="sysLog"
					:for-object-uuid="fields.uuid">
				</SystemOperateLog>
			</div>

		</div>
	</div>
</template>

<script>
	import MessageBox from 'components/MessageBox';
	import { ajaxPromise } from 'assets/javascripts/util';
	import SystemOperateLog from 'views/include/SystemOperateLog';

	export default {
		components: {
			SystemOperateLog
		},

		data(){
			return {
				fetching: false,
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1,
					page: 1,
					pageNumber: 12
				},
				fields: {
					name: '',
					description: ''
				},
				dataSource: {
					fetching: false,
					list: [],
					size: 0
				}
			}
		},

		computed:{
			action: function(){
				return `/tag/detail/query`
			},
		    filterDataSource: function () {
				return this.dataSource.list.map(item => {
					return Object.assign({deleted: false},item);
				})
      		}
		},

		watch: {
			'pageConds.pageIndex': function(){
				this.pageConds.page = this.pageConds.pageIndex
				this.fetch()
			}
		},

		activated(){
			this.fetchDetail(this.$route.params.id);
			this.fetch()
			this.$refs.sysLog.fetch()
		},

		methods: {
			fetchDetail: function(uuid){
				this.fetching = true;
				ajaxPromise({
					url: `/tag/detail?uuid=${uuid}`
				}).then(data=>{
					this.fields = data.tag;
				}).catch(e=>{
					MessageBox.open(message)
				}).then(() => {
					this.fetching = false;
				})
			},
			fetch: function(){
				this.dataSource.fetching = true;
				ajaxPromise({
					url: this.action,
					data: Object.assign({}, {uuid: this.$route.params.id}, this.pageConds)
				}).then(data=>{
					this.dataSource.list = data.list;
					this.dataSource.size = data.size;
				}).catch(message => {
					MessageBox.open(message)
				}).then(() => {
					this.dataSource.fetching = false
				})
			},
		}
	}
</script>
