<style lang="sass">
    
</style>

<template>
    <Modal v-model="visible">
        <ModalHeader title="重新执行"></ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                :model="fields" 
                :rules="rules" 
                class="sdf-form"
                :style="{
                    'margin-right': '30px',
                    'margin-left': '30px'
                }"
                label-width="120px">
                <el-form-item style="padding-bottom: 0;">
                    <div>是否重新执行放款操作？</div>
                </el-form-item>
                <el-form-item label="备注" prop="comment">
                    <el-input class="long" type="textarea" placeholder="请输入原因备注" v-model="fields.comment"></el-input>
                </el-form-item>
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="visible = false">取消</el-button>
            <el-button type="success" @click="submit" :loading="submitting">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import MessageBox from 'components/MessageBox';
    import { ajaxPromise } from 'assets/javascripts/util';

    export default {
        props: {
            value: Boolean,
            remittancePlanUuid: String,
            plannedPaymentDate: [String, Number]
        },
        data: function() {
            return {
                visible: this.value,
                submitting: false,
                fields: {
                    comment: ''
                },
                rules: {
                    comment: { required: true, message: ' '}
                }
            }
        },
        watch: {
            visible: function(current) {
                this.$emit('input', current);
                if (!current) {
                    this.$refs.form.resetFields();
                }
            },
            value: function(current) {
                this.visible = current;
            },
        },
        methods: {
            submit: function() {
                if (this.plannedPaymentDate && new Date(this.plannedPaymentDate) <= new Date('2016-10-10')) {
                    MessageBox.open('该贷款合同已生效，禁止再放款。请重新核对信息')
                    return;
                }

                this.$refs.form.validate(valid => {
                    if (valid) {
                        this.submitting = true;

                        ajaxPromise({
                            url: `/remittance/plan/resend?remittancePlanUuid=${this.remittancePlanUuid}`,
                            data: this.fields
                        }).then((data) => {
                            MessageBox.once('close', () => {
                                this.visible = false;
                                this.$emit('submit');
                            });
                            MessageBox.open('放款单，重新执行成功');
                        }).catch(msg => {
                            MessageBox.open(msg);
                        }).then(() => {
                            this.submitting = false;
                        });
                    }
                });
            },
        }
    }
</script>