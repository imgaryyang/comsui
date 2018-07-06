<template>
    <Modal v-model="show">
        <ModalHeader title="转账"></ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                :model="fields"
                :rules="rules"
                class="sdf-form"
                style="margin-left: 20px;"
                label-width="160px">
                <div>当前{{model.secondAccountName}}-{{model.thirdAccountName}}：{{ model.balance | formatMoney }}</div>
                <el-form-item label="账户名称" prop="counterAccountUuid" required>
                    <el-select
                        v-model="fields.counterAccountUuid"
                        placeholder="请选择"
                        class="middle">
                        <el-option
                          v-for="item in accountList"
                          :label="item.accountName"
                          :value="item.uuid">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="转账金额" prop="amount" required>
                     <el-input class="middle" v-model="fields.amount"></el-input>
                </el-form-item>
                <el-form-item label="转账类型" prop="accountTransType" required>
                    <el-select
                        v-model="fields.accountTransType"
                        placeholder="请选择"
                        class="middle">
                        <el-option
                          v-for="item in accountTransTypeList"
                          :label="item.value"
                          :value="item.key">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="备注" prop="remark" required>
                    <el-input class="middle" type="textarea" v-model="fields.remark"></el-input>
                </el-form-item>
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button type="success" :loading="transfering" @click="transferAccrualAccount">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        props: {
            value: Boolean,
            model: {
                type: Object,
                default: () => {}
            },
        },
        data: function() {
            var validateAmount = (rule, value, callback) => {
                if ($.isNumeric(value)) {
                    if (value > this.model.balance) {
                        callback(new Error('转账金额不能大于账户剩余金额'));
                        return;
                    }
                    callback()
                } else {
                    callback(new Error('金额格式有误'))
                }
            };
            return {
                show: this.value,
                fields: {
                    counterAccountUuid: '',
                    amount: '',
                    accountTransType: '',
                    remark: '',
                },
                rules: {
                    counterAccountUuid: { required: true, message: ' ', trigger: 'change'},
                    amount: [
                        { required: true, message: ' ', trigger: 'blur'},
                        { validator: validateAmount }
                    ],
                    accountTransType: { required: true, type: 'number', message: ' ', trigger: 'change'},
                    remark: { required: true, message: ' ', trigger: 'blur'},
                },
                accountList: [],
                accountTransTypeList: [],
                transfering: false,
            }
        },
        watch: {
            value: function(current) {
                this.show = current;
                if(current) {
                    this.getOptions();
                }
            },
            show: function(current) {
                this.$emit('input', current);
                if (!current) {
                    this.fields = {
                        counterAccountUuid: '',
                        amount: '',
                        accountTransType: '',
                        remark: '',
                    }
                }
            },
        },
        methods: {
            getOptions: function() {
                ajaxPromise({
                    url: `/capital/pre-transfer-account`,
                    data: {
                        hostAccountUuid: this.model.hostAccountUuid
                    }
                }).then(data => {
                    this.accountList = data.accountList || [];
                    this.accountTransTypeList = data.accountTransTypeList || [];
                }).catch(message => {
                    MessageBox.open(message);
                })
            },
            transferAccrualAccount: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        this.transfering = true;
                        ajaxPromise({
                            url: `/capital/transfer-accrual-account`,
                            type: 'post',
                            data: Object.assign({hostAccountUuid: this.model.hostAccountUuid}, this.fields)
                        }).then(data => {
                            MessageBox.once('close', () => {
                                this.$emit('input', false);
                            });
                            MessageBox.open('任务已提交');
                        }).catch(message => {
                            MessageBox.open(message);
                        }).then(() => {
                            this.transfering = false;
                        })
                    }
                })
            }
        }
    }
</script>