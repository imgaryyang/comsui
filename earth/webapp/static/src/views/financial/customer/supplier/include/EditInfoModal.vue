<template>
	<Modal v-model="show">
        <ModalHeader title="编辑供应商信息">
        </ModalHeader>
        <ModalBody align="left">
			<el-form 
	            ref="form"
	            :rules="rules" 
	            label-width="120px"
	            :model="currentModel" 
	            class="sdf-form">
	            <el-form-item label="供应商全称" prop="supplierName" required>
	                <el-input v-model.trim="currentModel.supplierName"></el-input>
	            </el-form-item>
	            <el-form-item label="公司法人" prop="legalPerson">
	                <el-input v-model.trim="currentModel.legalPerson"></el-input>
	            </el-form-item>
	            <el-form-item label="公司营业执照" prop="businessLicence">
	                <el-input v-model.trim="currentModel.businessLicence"></el-input>
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
				default: {}
			}
		},
		watch: {
			model: function(current) {
				this.currentModel = Object.assign({
					supplierUuid: '',
                    supplierName: '',
                    legalPerson: '',
                    businessLicence: ''
				}, current);
			},
		},
		data: function() {
			return {
				show: this.value,
				currentModel: Object.assign({}, this.model),
				rules: {
					supplierName: {required: true, message: ' ',  trigger: 'blur'}
				}
			}
		},
		methods: {
			submit: function() {
				this.$refs.form.validate(valid => {
                    if (valid) {
                       	ajaxPromise({
							url: `/supplier/modify`,
							type: 'post',
							data: this.currentModel
						}).then(data => {
							MessageBox.once('closed', () => {
								this.show = false;
								this.$emit('submit');
							});
							MessageBox.open('编辑成功');
						}).catch(message => {
							MessageBox.open(message);
						});
                    }
                });
			}
		}
	}
</script>