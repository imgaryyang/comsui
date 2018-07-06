<template>
	<Modal v-model="visible">
		<ModalHeader title="导入回盘文件"></ModalHeader>
		<ModalBody align="left">
			<el-form
			    ref="form"
			    :model="fields" 
			    :rules="rules"
			    class="sdf-form sdf-modal-form"
			    label-width="120px">
			    <el-form-item prop="appId" :label="$utils.locale('financialContract.appAccount.name')" required>
                    <el-select class="middle"
                        placeholder="请选择"
                        v-model="fields.appId">
                        <el-option
                            v-for="item in financialContractQueryModels" 
                            :label="item.label"
                            :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
			    <el-form-item :label="$utils.locale('financialContract')" prop="financialContractNo" required>
			        <el-form-item>
			        	<el-select 
			        	    v-model="fields.financialContractNo"
			        	    class="middle"
			        	    placeholder="请选择">
			        	    <el-option
			        	        v-for="item in currentFinancialContracts"
			        	        :label="item.contractName"
						        :value="item.contractNo">
			        	    </el-option>
			        	</el-select>
			        </el-form-item>
			    </el-form-item>
			    <el-form-item label="文件" prop="file">
				    <el-upload
				    	accept=".data"
				    	:before-upload="beforeUpload"
			        	action="/">
			        	<el-button class="button-multimedia">
			        		<div :style="{
			        			'max-width': '75px',
			        			'overflow': 'hidden',
			        			'text-overflow': 'ellipsis',
			        			}">
			        			{{ fields.file ? fields.file.name : '选择文件' }}
			        		</div>
			        	</el-button>
			        	<span style="margin-left: 10px; font-size: 12px; color: #999;" slot="tip">只能上传data文件</span>
			        </el-upload>
			    </el-form-item>
			</el-form>
		</ModalBody>
		<ModalFooter>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="success" @click="submit" :loading="uploading">确定</el-button>
		</ModalFooter>
	</Modal>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import { Upload } from 'element-ui';

	export default {
		components: {
			[Upload.name]: Upload
		},
		props: {
			value: Boolean,
			financialContractQueryModels: Array,
			params: Object
		},
		data: function() {
			return {
				uploading: false,
				visible: this.value,

				fields: {
					appId: '',
				    financialContractNo: '',
				    file: null
				},
				rules: {
					appId: { required: true, message: ' ', trigger: 'change'},
				    financialContractNo: { required: true, message: ' ', trigger: 'change'},
				    file: { type: 'object', required: true, message: ' ', trigger: 'change'}
				}
			};
		},
		watch: {
			value: function(current) {
				this.visible = current;
			},
            visible: function(current) {
            	this.$emit('input', current);
            	if (!current) {
                    this.$refs.form.resetFields();
                }
            }
		},
		computed: {
			currentFinancialContracts: function() {
                var result = [];
                this.fields.financialContractNo = '';
                this.financialContractQueryModels.forEach(item => {
                    if (item.value == this.fields.appId) {
                        result = item.children;
                    }
                });
                return result;
            }
		},
		methods: {
			beforeUpload: function(file) {
				this.fields.file = file;
				return false
			},
			submit: function() {
				if (typeof FormData == 'undefined') {
					MessageBox.open('不支持FormData');
					return;
				}

				this.$refs.form.validate(valid => {
				    if (valid) {
				    	var formData = new FormData();
				    	formData.append('file', this.fields.file);
				    	formData.append('financialContractNo', this.fields.financialContractNo);

				    	this.uploading = true;

				    	ajaxPromise({
				    	    url: `/trade/external-batch/importData`,
				    	    type: 'post',
				    	    processData: false,
				    	    contentType: false,
				    	    data: formData
				    	}).then((data) => {
				    	    this.visible = false;
				    	    this.$emit('submit');
				    	}).catch(msg => {
				    	    MessageBox.open(msg);
				    	}).then(() => {
				    	    this.uploading = false;
				    	});
				    }
				});
			}
		}
	}
</script>