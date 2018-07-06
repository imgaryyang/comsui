<template>
	<Modal v-model="show">
	    <ModalHeader title="作废"></ModalHeader>
	    <ModalBody align="left">
	        <el-form
	            ref="form"
	            :model="model"
	            :rules="rules"
	            class="sdf-form"
	            :style="{'margin-left': '50px'}"
	            label-width="120px">
	            <div style="margin: 15px 0;">
	                <div style="margin-left: 160px;">
	                    是否作废公告？
	                </div>
	            </div>
	            <el-form-item
	            	label="备注"
	            	prop="remarks"
	            	class="middle">
	                <el-input v-model="model.remarks" placeholder="请输入作废原因">
	                </el-input>
	            </el-form-item>
	            <br>
	        </el-form>
	    </ModalBody>
	    <ModalFooter>
	        <el-button @click="show = false">取消</el-button>
	        <el-button type="success" :loading="revoking" @click="revoke">确定</el-button>
	    </ModalFooter>
	</Modal>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		props: {
			value: Boolean,
			noticeUuid: String,
		},
		data: function() {
			return {
				fetching: false,
				revoking: false,
				show: this.value,
				model: {
					remarks: '',
				},
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
		        } 
		    },
		},
		methods: {
			revoke: function(e) {
				const noticeUuid = this.noticeUuid;
				this.$refs.form.validate( valid => {
					if(valid) {
		                this.revoking = true;
		                ajaxPromise({
		                    url: `/notice/notice-invalid`,
		                    data:{
		                    	remarks: this.model.remarks,
		                    	noticeUuid: this.noticeUuid
		                    },
		                    type: 'post'
		                }).then(data => {
		                	this.show = false;
                        	this.$emit('submit');
		                }).catch(message => {
		                	MessageBox.open(message);
		                }).then(() => {
		                    this.revoking = false;
		                });
					}
					
				})
            },
		}
	}
</script>