<style lang="sass" scoped>
	.query-area .el-input__inner{
		width: inherit!important;
	}
	.tag{
		display: inline-block;
		width: 14px;
		height: 14px;
		margin-right: 3px;
		background-image: url('./include/tag.png');
		background-size: 14px 28px;
		background-repeat: no-repeat;
	}
	.tagEmpty{
		background-position: 0 0;
	}
	.tagFull{
		background-position: 0 -14px;
	}
	.ul-reset{
		list-style: none;
		padding: 0;
		margin: 0;
		li{
			display: inline-block;
			line-height: 14px;
			border-radius: 20px;
			border: 1px solid #4669a9;
			text-align: center;
			padding: 4px;
			margin-right: 10px;
			.el-icon-close{
				cursor: pointer;
				transform: scale(0.6);
				visibility: hidden;
				opacity: 0;
			}
			&:hover{
				.el-icon-close{
					visibility: visible;
					opacity: 1;
				}
			}
		}
		.el-icon-plus{
			color: #3663a6;
			cursor: pointer;
		}
	}

</style>

<template>
	<div class="content">
		<div class="scroller">
			<div class="query-area">
				<el-form class="sdf-form sdf-query-form" :inline="true" @submit.native.prevent>
					<el-form-item>
						<el-input v-model="queryConds.name" placeholder="请输入标签名称" size="small"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button @click="fetch" size="small" type="primary">查询</el-button>
					</el-form-item>
					<el-form-item>
						<el-button size="small" type="success" style="color:#ddd" @click="showAddTagModal">新增标签</el-button>
					</el-form-item>
				</el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border"
					:data="dataSource.list"
					stripe>
					<el-table-column label="标签名称" prop="name" inline-template><a :href="`${ctx}#/system/tagmanage/${row.uuid}/detail`" style="cursor: pointer">{{row.name}}</a></el-table-column>
					<el-table-column label="标签描述" prop="description"></el-table-column>
					<el-table-column label="创建时间" prop="createTime" inline-template><div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div></el-table-column>
					<el-table-column label="修改时间" prop="lastModifiedTime" inline-template><div>{{ row.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div></el-table-column>
					<el-table-column label="操作" prop="" inline-template>
						<div>
							<a :href="`${ctx}#/system/tagmanage/${row.uuid}/edit`" style="cursor: pointer">编辑</a>
							<a @click.prevent="handleClickDeleteTag(row)" style="cursor: pointer;margin-left:15px;">删除</a>
						</div>
					</el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
			<div class="pull-right">
				<PageControl
					v-model="pageConds.pageIndex"
					:size="dataSource.size"
					:per-page-record-number="pageConds.perPageRecordNumber">
				</PageControl>
			</div>
		</div>
		<AddTagModal
			v-model="addTagModel.show"
			@reloadDataSource="reloadDataSource"></AddTagModal>
	</div>
</template>

<script>
	import {ajaxPromise} from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {

		components: {
			AddTagModal: require('./include/AddTagModal')
		},

		data(){
			return {
				action: 'tag/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1,
					page: 1,
					pageNumber: 12
				},
				queryConds: {
					name: ''
				},
				addTagModel: {
					show: false
				},
				dataSource: {
					list: [],
					size: 0
				}
			}
		},
		activated(){
			this.fetch()
		},

		watch: {
			'pageConds.pageIndex': function(){
				this.pageConds.page = this.pageConds.pageIndex
				this.fetch()
			}
		},

		methods: {
			fetch: function(){
				ajaxPromise({
					url: this.action,
					data: Object.assign({}, this.queryConds, this.pageConds)
				}).then(data=>{
					this.dataSource.list = data.list;
					this.dataSource.size = data.size;
				}).catch(message => {
					MessageBox.open(message)
				})
			},
			showAddTagModal: function(){
				this.addTagModel.show = true;
			},
			handleClickDeleteTag: function(row){
				MessageBox.open('确认删除该条标签？', '提示', [{
				    text: '取消',
				    handler: () => {
				        MessageBox.close();
				    }
				},{
				    text: '确定',
				    type: 'success',
				    handler: () => {
				    	ajaxPromise({
				    		url: `tag/delete`,
				    		data:{
				    			uuid:row.uuid
				    		},
				    		type:'POST'
				    	}).then(data=>{
				    		var index = this.dataSource.list.findIndex(i => {
				    			return i.uuid === row.uuid
				    		})
				    		if(index != -1){
				    			this.dataSource.list.splice(index,1)
				    		}
				    	}).then(()=>{
								MessageBox.close()
							}).catch(message=>{
				    		MessageBox.open(message)
				    	})
				    }
				}]);
			},
			reloadDataSource: function(){
				this.fetch()
			}
		}
	}
</script>
