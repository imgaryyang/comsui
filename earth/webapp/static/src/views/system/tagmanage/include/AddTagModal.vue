<template>
	<Modal v-model="show">
        <ModalHeader title="新增标签">
        </ModalHeader>
        <ModalBody align="left">
        	<el-form ref="form"
				:rules="rules"
				:model="fields"
				label-width="100px"
				@submit.native.prevent
				class="sdf-form">
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
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button @click="submit" type="success" :loading="uploading">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	export default {
		props: {
		    value: {
		        default: false
		    }
		},

		data(){
			return {
				fields: {
					name: '',
					description: ''
				},
				rules: {
					name:[{required: true, message: '请输入标签名称', trigger: 'blur'},
					{ min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}],
					description:{max: 200,trigger: 'blur', message: '长度不要超过200个字符' }
				},
				show: false,
				uploading: false
			}
		},
		watch: {
		    value: function(current) {
		        this.show = current;
		    },
		    show: function(current) {
		        this.$emit('input', current);
		        if (!current && this.$refs.form) {
		            this.$refs.form.resetFields();
		        }
		    }
		},
		methods: {
			submit: function(){
				this.$refs.form.validate(valid => {
					if(valid){
						this.uploading = true;
						ajaxPromise({
							url: `tag/create-tag`,
							data: {
								name: this.fields.name,
								description: this.fields.description
							},
							type: 'post'
						}).then(data => {
							this.show = false;
							MessageBox.open('新增标签成功！')
							this.$emit('reloadDataSource')
						}).catch(message=>{
							MessageBox.open(message)
						}).then(()=>{
							this.uploading = false;
						})
					}
				})
			}
		}
	}
</script>
