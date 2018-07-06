<style>
</style>

<template>
    <Modal v-model="show">
        <ModalHeader title="修改订单状态">
        </ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                :model="currentModel"
                label-width="120px"
                :rules="rules"
                class="sdf-form sdf-modal-form">
                <el-form-item prop="paymentOrderStatus" label="修改订单状态" required>
                    <el-select class="middle" v-model="currentModel.paymentOrderStatus">
                        <el-option
                            v-for="item  in paymentOrderStatusList"
                            :label="item.label"
                            :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="备注"
                    prop="remark"
                    required>
                    <el-input
                        class="long"
                        type="textarea"
                        v-model="currentModel.remark"
                        placeholder="请输入原因(30个字以内)">
                    </el-input>
                </el-form-item>
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="submit" type="success">确定</el-button>
            <el-button @click="show = false">取消</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import modalMixin from './modal-mixin';
    import formats from 'filters/format';

    export default {
        mixins: [modalMixin],
        props: {
            model: {
              default: () =>({})
            }
        },
        watch: {
            model: function(cur) {
                this.currentModel = Object.assign({
                    paymentOrderUuid: '',
                    paymentOrderStatus: '2',
                    remark: '',
                },cur)
            },
            show: function(cur){
                if (!cur) {
                    this.$refs.form.resetFields();
                }
            }
        },
        data: function() {
            return {
                show: this.value,
                currentModel: Object.assign({},this.model),
                rules: {
                    remark: {
                        required: true,
                        trigger: 'blur',
                        message: ' '
                    },
                    paymentOrderStatus: {
                        required: true,
                        trigger: 'change',
                        message: ' '
                    }
                }
            }
        },
        computed: {
            paymentOrderStatusList: function() {
                return [{
                        label: '支付失败',
                        value: '2'
                    }]
            }
        },
        methods: {
            submit:function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        ajaxPromise({
                            url:`/repayment-order/updatePaymentOrderFail`,
                            data: this.currentModel
                        }).then(data => {
                            MessageBox.once('closed', () => {
                                this.show = false;
                                this.$emit('submit');
                            });
                            MessageBox.open('操作成功');
                        }).catch(message => {
                          MessageBox.open(message);
                        })
                    }
                })
            }
        }
    }
</script>
