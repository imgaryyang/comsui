<template>
  <Modal v-model="visible" class="modal-create-bill">
    <ModalHeader title="提现弹窗"></ModalHeader>
    <ModalBody align="left">
        <el-form
            ref="form"
            :model="fields"
            :rules="rules"
            class="sdf-form sdf-modal-form"
            label-width="100px">
            <el-form-item label="充值金额" prop="depositAmount">
                <el-input v-model="fields.depositAmount" class="middle"></el-input>
                <div>！提现金额将转入当前虚户的提现户</div>
            </el-form-item>
        </el-form>
		<p style="text-align:center;">业务金额：9,000.00     滞留金额：1,000.00</p>
    </ModalBody>
    <ModalFooter>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="success" @click="handleSubmit" :loding="creating">确定</el-button>
    </ModalFooter>
  </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import Vue from 'vue';
    import MessageBox from 'components/MessageBox';


    export default {
        props: {
            value: Boolean,
        },
        data: function() {
            var validateAmount = (rule, value, callback) => {
                if ($.isNumeric(value)) {
                    if (value > this.doubtAmount) {
                        callback(new Error('充值金额不能大于存疑金额'));
                        return;
                    }
                    callback()
                } else {
                    callback(new Error('金额格式有误'))
                }
            };
            return {
                visible: this.value,
                fields: {
                    depositAmount: '',
                },
                rules: {
                    depositAmount: [
                        { required: true, message: ' ' },
                        { validator: validateAmount }
                    ]
                },
            }
        },
        watch: {
            visible: function(current) {
                this.$emit('input', current);
            },
            value: function(current) {
                this.visible = current;
                if (!current) {
                    this.$refs.form.resetFields();
                }
            }
        },
        methods: {
            handleSubmit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        this.creating = true;
                        ajaxPromise({
                            url: ``,
                            type: 'post',
                            data: {
                                depositAmount: this.fields.depositAmount,
                            }
                        }).then(data => {
                            MessageBox.once('close', () => {
                                this.$emit('submitSuccess');
                            });
                            MessageBox.open('提现提交成功');
                        }).catch(requestError => {
                           this.$emit('submitFailed', requestError);
                        })
                    }
                });
            }
        }
    }
</script>
