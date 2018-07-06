<template>
	 <Modal v-model="show">
        <ModalHeader title="关闭合同">
        </ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                :model="currentModel"
                label-width="120px"
                :rules="rules"
                class="sdf-form sdf-modal-form">
                <el-form-item style="padding: 0;" label="贷款合同编号">
                    {{ contract.contractNo}}
                </el-form-item>
                <el-form-item style="padding: 0;" label="客户姓名">
                    {{ customer.name}}
                </el-form-item>
                <el-form-item style="padding: 0;" label="合同本金">
                    {{ contract.totalAmount}}
                </el-form-item>
                <el-form-item style="padding: 0;" label="客户账号">
                    {{ contractAccount.payAcNo}}
                </el-form-item>
                <el-form-item style="padding: 0;" label="身份证">
                    {{ contractAccount.idCardNum}}
                </el-form-item>
                <el-form-item
                    label="备注"
                    prop="comment"
                    required>
                    <el-input placeholder="请输入终止原因" class="long" type="textarea" v-model="currentModel.comment"></el-input>
                </el-form-item>
                <el-form-item
                    style="padding-top: 0;"
                    label="上传附件"
                    prop="file">
                    <el-upload
                        :before-upload="handleBeforeUpload"
                        action="/">
                        <el-button class="button-multimedia">
                            <div :style="{
                                'max-width': '75px',
                                'overflow': 'hidden',
                                'text-overflow': 'ellipsis',
                                }">
                                {{ currentModel.file ? currentModel.file.name : '点击上传' }}
                            </div>
                        </el-button>
                    </el-upload>
                </el-form-item>
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button @click="submit" type="success" :loading="submitting">确定</el-button>
        </ModalFooter> 
    </Modal>
</template>
<script>
	import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import modalMixin from './modal-mixin';
    import { REGEXPS } from 'src/validators';

    export default {
    	mixins: [modalMixin],
    	props: {
    	    customer: [Object],
            contractId: {
                required: true
            },
            contract: {
                required: true
            },
            contractAccount: {
                required: true
            }
    	},
    	data: function() {
    		return {
    			show: this.value,
                submitting: false,
                rules: {
                    comment: [
                        { required: true, message: ' '},
                        { max: 50, message: '不能超过50个字符'}
                    ]
                },
                currentModel: {
                    file: null,
                    comment: ''
                }
    		}
    	},
    	methods: {
            handleBeforeUpload: function(file) {
                this.currentModel.file = file;
                return false
            },
            submit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        this.submitting = true;

                        var formData = new FormData();
                        formData.append('contractId', this.contractId);
                        formData.append('file', this.currentModel.file);
                        formData.append('comment', this.currentModel.comment);

                        ajaxPromise({
                            url: `/contracts/invalidate`,
                            processData: false,
                            contentType: false,
                            data: formData,
                            type: 'post'
                        }).then(data => {
                            MessageBox.open('提交成功！');
                            MessageBox.once('close', () => {
                                this.show = false;
                                setTimeout(() => {
                                    this.$emit('submit');
                                }, 500);
                            });
                        }).catch(message => {
                            MessageBox.open(message)
                        }).then(() => {
                            this.submitting = false;
                        });
                    }
                });
            }
    	}
    }
</script>