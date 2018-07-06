<template>
	<div>
		<el-form 
            :model="currentModel" 
            :rules="rules" 
            ref="form"
            style="margin-top: 30px"
            label-width="120px" 
            class="sdf-form sdf-modal-form">
            <el-form-item label="标题" prop="title" required>
                <el-input class="long" v-model.trim="currentModel.title"></el-input>
            </el-form-item>
            <el-form-item label="密钥" prop="publicKey" required>
                <el-input class="long" type="textarea" :rows="5" v-model.trim="currentModel.publicKey"></el-input>
                <span style="display: block;margin-top: -10px;color:#666">在增加密钥之前需先<a :href="`${ctx}#/system/personal/secret-key-info`">生成密钥</a></span>
            </el-form-item>
            <el-form-item label="  ">
                <el-button @click="postSecretKey" type="success">添加密钥</el-button>
            </el-form-item>
            <el-form-item label="  ">
           		<div style="width:100%;height:1px;background-color:#e0e0e0;margin-top: 15px;"></div>
            </el-form-item>
            <el-form-item label="  ">
                <span style="color:#666">您已有 <span style="color:#436BA7">{{secretKeys.length}}</span> 个密钥, 还可以增加 <span style="color:#436BA7">{{5-secretKeys.length}}</span> 个密钥</span>
                <div>
	                <SketchItem v-for="item in secretKeys" style="float:left;margin-right:10px">
                		<span class="icon icon-key"></span>
	                	<div class="text">
	                		<el-popover
								placement="top"
								trigger="hover"
								>
								{{ item.publicKey }}
		                       	<span slot="reference" style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;max-width: 300px;float: left;">{{item.title}}</span>
							</el-popover>
	                       	<p>创建时间 {{ item.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
		                    <p>最后一次访问时间 {{ item.requestTime | formatDate('yyyy-MM-dd HH:mm:ss') }} <span style="color:#436BA7;float: right;cursor: pointer;" @click="handleMore(item.publicKeyUuid)" v-if="item.requestTime != ''">更多</span></p>
	                    </div>
	                    <span class="operate">
	                      	<span class="el-icon-close delete-key" @click="onDeleteSecretKey(item.publicKeyUuid)"></span>
	                    </span>
	                </SketchItem>
                </div>
            </el-form-item>
        </el-form>
	</div>
</template>

<script>
    import MessageBox from 'components/MessageBox';
    import { ajaxPromise } from 'assets/javascripts/util';

	export default {
		components: {
			SketchItem: require('components/SketchItem'),
			HelpPopover: require('views/include/HelpPopover')
		},
		props: {
			value: {
				type: Boolean,
				default: false
			},
			principalId: null
		},
		data: function() {
			return {
				currentModel: {
					title: '',
					publicKey: ''
				},
				rules: {
					title: {required: true, message: ' '},
					publicKey: {required: true, message: ' '}
				},
				
				secretKeys: [],
			}
		},
		watch: {
			value: function(current) {
				if (current) {
					this.$refs.form.resetFields();
					this.fetchSecretKeys();
				}
			}
		},
		methods: {
			fetchSecretKeys: function() {
				ajaxPromise({
					url: '/keystore',
					data: {
						principalId: this.principalId
					}
				}).then(data => {
					this.secretKeys = [].concat(data.list || []);
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			postSecretKey: function() {
				this.$refs.form.validate(valid => {
					if (valid) {
						ajaxPromise({
							url: '/keystore',
							type: 'post',
							data: Object.assign({}, this.currentModel, {principalId: this.principalId})
						}).then(data => {
							MessageBox.once('closed', () => {
								this.$refs.form.resetFields();
								this.fetchSecretKeys();
							});
							MessageBox.open("密钥添加成功");
						}).catch(message => {
							MessageBox.open(message);
						});
					}
				});
			},
			onDeleteSecretKey: function(publicKeyUuid) {
				MessageBox.open('删除后,绑定该密钥的设备将不能再访问五维贷后系统是否确定删除','删除密钥', [{
					text: '取消',
					handler: () => MessageBox.close()
				},{
					text: '确定',
					type: 'success',
					handler: () => this.handleDelete(publicKeyUuid)
				}])
			},
			handleDelete: function(publicKeyUuid) {
				ajaxPromise({
					url: `/keystore/${publicKeyUuid}/delete`,
					type: 'delete'
				}).then(data => {
					MessageBox.close();
					MessageBox.once('closed', () => this.fetchSecretKeys());
					MessageBox.open("密钥删除成功");
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			handleMore: function(uuid) {
				this.$emit('showHistoryModal', uuid);
			}
		}
	}
</script>