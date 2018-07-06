c<style lang="sass">
	.margin{
		margin:0 0 68px 150px;
		width: 900px;
	}
	#tagManageDetail {
		.el-table {
			.deleted {
				background-color: #f5f5f5;
			}
		}
	}
</style>
<template>
	<div class="content" id="tagManageDetail">
		<div class="scroller" v-loading="fetching">
			<Breadcrumb :routes="[{ title: '编辑标签'}]"></Breadcrumb>
			<el-form ref="form"
			:rules="rules"
			:model="fields"
			label-width="100px"
			class="sdf-form"
			@submit.native.prevent
			style="margin:40px 0 40px 118px">
				<el-form-item style="padding: 5px 0;" label="标签信息" class="form-item-legend"></el-form-item>
				<el-form-item label="标签名称" prop="name" required>
					<el-input size="middle" v-model="fields.name" class="long"></el-input>
					<div style="font-size: 11px;color: #666666;line-height:24px;">1~20个字符，区分大小写</div>
				</el-form-item>
				<el-form-item label="标签描述" prop="description">
					<el-input
					  class="long"
					  type="textarea"
					  :autosize="{ minRows: 2, maxRows: 4}"
					  placeholder="200字符以内"
					  v-model="fields.description">
					</el-input>
				</el-form-item>
			</el-form>

			<div class="block margin">
				<h5 class="hd">数据标签</h5>
				<div class="bd">
					<el-table
		                :data="filterDataSource"
		                v-loading="dataSource.fetching"
		                class="td-15-padding th-8-15-padding no-th-border"
		                :row-class-name="tableRowClassName"
		                border>
		                <el-table-column label="序号" type="index" width="100"></el-table-column>
		                <el-table-column label="单据编号" prop="outerIdentifier"></el-table-column>
		                <el-table-column label="单据类型"  prop="typeCn"></el-table-column>
		                <el-table-column label="操作" inline-template><div @click="row.deleted = !row.deleted" style="cursor:pointer;">{{ row.deleted ? '添加' : ' 删除 '}}</div></el-table-column>
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
				<el-button type="primary" @click="handleClickCancel">取消</el-button>
				<el-button type="primary" :loding="uploading" @click="handleClickSubmit">提交</el-button>
			</div>
		</div>
	</div>
</template>

<script>
	import MessageBox from 'components/MessageBox';
	import { ajaxPromise } from 'assets/javascripts/util';

	export default {

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
					uuid:'',
					name: '',
					description: ''
				},
				rules: {
					name:[{required: true, message: '请输入标签名称', trigger: 'blur'},
					{ min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}],
					description:{max: 200, trigger: 'blur', message: '长度不要超过200个字符'}
				},
				uploading: false,
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
		},

		methods: {
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
					this.dataSource.fetching = false;
				})
			},
			fetchDetail: function(uuid){
				this.fetching = true;
				ajaxPromise({
					url: `/tag/detail?uuid=${uuid}`
				}).then(data=>{
					this.fields = data.tag;
				}).catch(message=>{
					MessageBox.open(message)
				}).then(() => {
					this.fetching = false;
				})
			},
			handleClickCancel:function(){
				new Promise((resolve, reject) => {
					window.history.back()
					resolve()
				}).then(() => {
					MessageBox.close()
				})
			},
			handleClickSubmit:function(){
				this.$refs.form.validate(valid => {
					if(valid){
						this.uploading = true;
						var deleteUuids = this.filterDataSource.filter(item => item.deleted).map(item=>{
							return item.uuid
						})
						ajaxPromise({
							url: `tag/edit-tag`,
							data: {
								uuid: this.fields.uuid,
								name: this.fields.name,
								description: this.fields.description,
								deleteUuids: JSON.stringify(deleteUuids)
							},
							type: 'post'
						}).then(data => {
							MessageBox.open('编辑标签成功！')
							setTimeout(()=>{
								window.history.back();
							},1000)
						}).catch(message=>{
							MessageBox.open(message)
						}).then(()=>{
							this.uploading = false;
						})
					}
				})
			},
			tableRowClassName: function(row, index) {
				return row.deleted ? 'deleted' : '';
			}
		}
	}
</script>
