<template>
	<Modal v-model="show">
        <ModalHeader title="编辑商户公司信息">
        </ModalHeader>
        <ModalBody align="left">
			<el-form 
	            ref="form"
	            :rules="rules" 
	            label-width="120px"
	            :model="currentModel" 
	            class="sdf-form">
	            <el-form-item label="商户公司全称" prop="companyFullName" required>
	                <el-input class="long" v-model.trim="currentModel.companyFullName"></el-input>
	            </el-form-item>
	            <el-form-item label="商户简称" prop="appName" required>
	                <el-input class="long" v-model.trim="currentModel.appName"></el-input>
	            </el-form-item>
	            <el-form-item label="地址" prop="address" required>
	                <el-input class="long" v-model.trim="currentModel.address"></el-input>
	            </el-form-item>
	            <el-form-item label="公司法人" prop="legalPerson">
	                <el-input class="long" v-model.trim="currentModel.legalPerson"></el-input>
	            </el-form-item>
	            <el-form-item label="公司营业执照" prop="businessLicence">
	                <el-input class="long" v-model.trim="currentModel.businessLicence"></el-input>
	            </el-form-item>
	        </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button @click="submit" type="success">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import modalMixin from './modal-mixin';

	export default {
    	mixins: [modalMixin],
		props: {
			model: {
				type: Object,
				default: () => ({})
			}
		},
		watch: {
			model: function(current) {
				this.currentModel = Object.assign({
					id: '',
					companyFullName: '',
                    appId: '',
					appName: '',
					address: '',
					legalPerson: '',
					businessLicence: ''
				}, current);
			}
		},
		data: function() {
			return {
				show: this.value,
				currentModel: Object.assign({}, this.model),
				rules: {
					companyFullName: {required: true, message: ' ',  trigger: 'blur'},
					appName: {required: true, message: ' ',  trigger: 'blur'},
					address: {required: true, message: ' ',  trigger: 'blur'},
				}
			}
		},
		methods: {
			submit: function() {
				this.$refs.form.validate(valid => {
                    if (valid) {
                       	ajaxPromise({
							url: `/app/modify`,
							type: 'post',
							data: this.currentModel
						}).then(data => {
							MessageBox.once('closed', () => {
								this.show = false;
								this.$emit('submit');
							});
							MessageBox.open('操作成功');
						}).catch(message => {
							MessageBox.open(message);
						});
                    }
                });
			}
		}
	}
</script>