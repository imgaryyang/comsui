<template>
	<Modal v-model="show">
	    <ModalHeader title="编辑公告"></ModalHeader>

	    <ModalBody align="left">
			<TabMenu type="segment" v-model="selected">
			    <TabMenuItem id="EDIT">编辑公告</TabMenuItem>
			    <TabMenuItem id="OPERATIONS">操作日志</TabMenuItem>
			</TabMenu> 
			<TabContent v-model="selected">
				<TabContentItem id="EDIT">
			        <el-form
			            ref="form"
			            :model="fields"
			            :rules="rules"
			            class="sdf-form"
			            :style="{'margin-left': '0px'}"
			            label-width="120px">
			            <el-form-item
			            	label="标题"
			            	prop="title"
			            	class="long"
			            	name="title"
			            	required>
			                <el-input v-model="fields.title" placeholder="">
			                </el-input>
			            </el-form-item>
			            <el-form-item
			            	label="内容"
			            	prop="content"
			            	name="content"
			            	class="long"
			            	required>
			                <el-input type="textarea" :rows="10" :cols="40" v-model="fields.content" placeholder="">
			                </el-input>
			            </el-form-item>
			            <br>
			        </el-form>
				</TabContentItem>
				<TabContentItem id="OPERATIONS">
					<div class="bolck">
						<div class="system-operate-log">
							<div class="bd">
								<el-table 
									class="td-15-padding th-8-15-padding no-th-border"
									:data="dataSource.list"
									v-loading="dataSource.fetching"
									stripe
									border>
								    <el-table-column width="60px" type="index" label="序号">
								    </el-table-column>
								    <el-table-column width="150px" prop="occurTime" label="操作发生时间">
								    </el-table-column>
								    <el-table-column prop="operateName" label="操作员登录名">
								    </el-table-column>

								    <el-table-column inline-template label="操作内容">
								    	<el-popover
								    		trigger="hover" 
		                                    placement="top">
								    		<div style="max-width: 500px;">{{ row.recordContent }}</div>
								    		<span class="notice-content" slot="reference">{{ row.recordContent }}</span>
								    	</el-popover>
								    </el-table-column>
								</el-table>
							</div>
							<div class="sys-log">
								<div class="ft clearfix">
							        <PageControl 
							            v-model="pageConds.pageIndex"
							            :size="dataSource.size"
							            :per-page-record-number="pageConds.perPageRecordNumber">
							        </PageControl>
								</div>
							</div>
						</div>
					</div>
				</TabContentItem>
			</TabContent>  	
	    </ModalBody>
	    <ModalFooter>
	        <el-button @click="show = false">取消</el-button>
	        <template v-if="fields.noticeStatus == 'UNRELEASED' ">
		        <el-button type="success" :loading="fetching" @click="saveNotice">保存</el-button>
	        	<el-button type="success" :loading="fetching" @click="releaseNotice">发布</el-button>
	        </template>
	    </ModalFooter>
	</Modal>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
    import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';
	import Pagination from 'mixins/Pagination';

	export default {
		mixins: [Pagination],
		components: {
			SystemOperateLog: require('views/include/SystemOperateLog'),
			TabMenu, TabMenuItem, TabContent, TabContentItem 
		},
		props: {
			value: Boolean,
			model: Object
		},
		data: function() {
			return {
				action: `/system-operate-log/query`,
				pageConds: {
					pageIndex: 1,
					perPageRecordNumber: 5
				},

				fetching: false,
				show: this.value,
				selected: 'EDIT',
				rules: {
					title: { required: true, message: ' '},
					content: { required: true, MessageBox: ' '}
				},
				fields: Object.assign({}, this.model),
			}
		},
		watch: {
		    value: function(current) {
		        this.show = current;
		    },
		    show: function(current) {
		        this.$emit('input', current);
		        if (!current) {
		        	this.$refs.form.resetFields();
		        	this.selected = 'EDIT';
		        } 
		        this.fields = Object.assign({
		            title: '',
		            content: '',
		            noticeUuid: '',
		            noticeStatus: '',
		        }, this.model);

		        this.fields.title = unescape(this.fields.title);
		        this.fields.content = unescape(this.fields.content);
		    },
		    selected: function(current) {
		    	if( current == 'OPERATIONS') {
		    		this.fetch();
		    	}
		    },
		},
		computed: {
			queryConds: function() {
				return {
					objectUuid: this.model.uuid,
				};
			},
		    conditions: function() {
		        return Object.assign({}, this.queryConds, this.pageConds);
		    }
		},
		methods: {
			saveNotice: function(e) {
				this.$refs.form.validate( valid => {
					if(valid) {
		                ajaxPromise({
		                    url: `/notice/notice-add`,
		                    data:{
		                    	title: escape(this.fields.title),
		                    	content: escape(this.fields.content),
		                    	noticeUuid: this.fields.noticeUuid
		                    },
		                    type: 'post'
		                }).then(data => {
		                	this.show= false;
                        	this.$emit('submit');
		                }).catch(message => {
		                	MessageBox.open(message);
		                }).then(() => {
		                    this.fetching = false;
		                });
					}
					
				})
            },
			releaseNotice: function(e) {
				this.$refs.form.validate( valid => {
					if(valid) {
		                ajaxPromise({
		                    url: `/notice/notice-release`,
		                    data:{
		                    	title: escape(this.fields.title),
		                    	content: escape(this.fields.content),
		                    	noticeUuid: this.fields.noticeUuid
		                    },
		                    type: 'post'
		                }).then(data => {
		                	this.show = false;
                        	this.$emit('submit');
		                }).catch(message => {
		                	MessageBox.open(message);
		                }).then(() => {
		                    this.fetching = false;
		                });
					}
					
				})
            },
            fetch: function() {
				if (!this.conditions.objectUuid) return;
			    this.getData({
			        url: this.action,
			        data: this.conditions
			    });
			},
		}
	}
</script>