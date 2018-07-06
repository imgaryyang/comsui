<template>
	<Modal v-model="show">
	    <ModalHeader title="新建公告"></ModalHeader>
	    <ModalBody align="left">
	        <el-form
	            ref="form"
	            :model="model"
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
	                <el-input v-model="model.title" placeholder="">
	                </el-input>
	            </el-form-item>
	            <el-form-item
	            	label="内容"
	            	prop="content"
	            	name="content"
	            	class="long"
	            	required>
	                <el-input type="textarea" v-model="model.content" placeholder="">
	                </el-input>
	            </el-form-item>
	            <br>
	        </el-form>
	    </ModalBody>
	    <ModalFooter>
	        <el-button @click="show = false">取消</el-button>
	        <el-button type="success" :loading="revoking" @click="addNotice">确定</el-button>
	    </ModalFooter>
	</Modal>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		props: {
			value: Boolean,
		},
		data: function() {
			return {
				fetching: false,
				show: this.value,
				model: {
					title: '',
					content: ''
				},
				rules: {
					title: { required: true, message: ' '},
					content: { required: true, message: ' '}
				}
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
			addNotice: function(e) {
				this.$refs.form.validate( valid => {
					if(valid) {
		                ajaxPromise({
		                    url: `/notice/notice-add`,
		                    data:{
		                    	title: escape(this.model.title),
		                    	content: escape(this.model.content)
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
		}
	}
</script>