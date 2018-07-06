<style lang="sass">
    .richtext-dropdown {
        .el-select-dropdown__item {
            height: auto;
        }
    }
</style>

<template>
    <Modal v-model="visible">
        <ModalHeader title="新增对账任务"></ModalHeader>
        <ModalBody align="left">
            <el-form
                :model="fields"
                :rules="rules"
                ref="form"
                class="sdf-form"
                style="margin-left: 20px;"
                label-width="145px">
                <el-form-item prop="accountSide" label="收付类型" required>
                    <el-select
                        class="middle"
                        placeholder="请选择收付类型"
                        v-model="fields.accountSide">
                        <el-option
                            v-for="item in accountSides"
                            :label="item.value"
                            :value="item.key">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="paymentInstitutionName" label="网关" required>
                    <el-select
                        class="middle"
                        placeholder="请选择网关"
                        v-model="fields.paymentInstitutionName">
                        <el-option
                            v-for="item in paymentInstitutionNames"
                            :label="item.value"
                            :value="item.key">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="pgAccount" label="商户号" required>
                    <el-select
                        class="middle"
                        placeholder="请选择商户号"
                        v-model="fields.pgAccount">
                        <el-option
                            v-for="item in pgAccountList"
                            :label="item"
                            :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="pgClearingAccount" label="清算号" required>
                    <el-select
                        class="middle"
                        placeholder="请选择清算号"
                        v-model="fields.pgClearingAccount">
                        <el-option
                            v-for="item in clearingNoList"
                            :label="item"
                            :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="入账时间" required>
                    <el-col :span="10">
                        <el-form-item prop="startTime">
                            <DateTimePicker
                                :pick-time="true"
                                :formatToMinimum="true"
                                v-model="fields.startTime"
                                :end-date="fields.endTime"
                                placeholder="入账起始时间">
                            </DateTimePicker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="2">
                        <div class="text-align-center color-dim">至</div>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item prop="endTime">
                            <DateTimePicker
                                :pick-time="true"
                                :formatToMaximum="true"
                                v-model="fields.endTime"
                                :start-date="fields.startTime"
                                placeholder="入账终止时间">
                            </DateTimePicker>
                        </el-form-item>
                    </el-col>
                </el-form-item>
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="visible = false">取消</el-button>
            <el-button @click="submit" type="success">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        props: {
            value: {
                type: Boolean,
                default: false
            },
            paymentInstitutionNames: {
                type: Array,
                default: () => []
            },
        },
        data: function() {
            return {
                visible: this.value,

                accountSides: [{
                    key: 1,
                    value: '代收'
                }],

                fields: {
                    accountSide: '',
                    paymentInstitutionName: '',
                    pgAccount: '',
                    pgClearingAccount: '',
                    startTime: '',
                    endTime: '',
                },

                rules: {
                    accountSide: { required: true, message: ' '},
                    paymentInstitutionName: { required: true, message: ' '},
                    pgAccount: { required: true, message: ' '},
                    pgClearingAccount: { required: true, message: ' '},
                    startTime: { required: true, message: ' '},
                    endTime: { required: true, message: ' '},
                },

                pgAccountList: [],
                clearingNoList: []
            };
        },
        watch: {
            'fields.paymentInstitutionName': function(current) {
                this.fields.pgAccount = '';
                this.fields.pgClearingAccount = '';
                this.pgAccountList = [];
                this.clearingNoList = [];
                if(current !== '') {
                    ajaxPromise({
                        url: `/audit/beneficiary/query-payment-channel-pgAccount`,
                        data: {
                            paymentInstitutionOrdinal: current
                        }
                    }).then(data => {
                        this.pgAccountList = data.pgAccountList || [];
                    }).catch(message => {
                        MessageBox.open(message);
                    })
                }
            },
            'fields.pgAccount': function(current) {
                this.fields.pgClearingAccount = '';
                this.clearingNoList = [];
                if(current !== '') {
                    ajaxPromise({
                        url: `/audit/beneficiary/query-payment-channel-clearingNo`,
                        data: {
                            paymentInstitutionOrdinal: this.fields.paymentInstitutionName,
                            outlierChannelName: current
                        }
                    }).then(data => {
                        this.clearingNoList = data.clearingNoList || [];
                    }).catch(message => {
                        MessageBox.open(message);
                    })
                }
            },
            visible: function(current) {
                this.$emit('input', current);
                if (!current) {
                    this.$refs.form.resetFields();
                }

                this.fields = {
                    accountSide: '',
                    paymentInstitutionName: '',
                    pgAccount: '',
                    pgClearingAccount: '',
                    startTime: '',
                    endTime: '',
                };
                this.pgAccountList = [];
                this.clearingNoList = [];
            },
            value: function(current) {
                this.visible = current;
            }
        },
        methods: {
            submit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        ajaxPromise({
                            url: '/audit/beneficiary/create-audit-job',
                            data: this.fields
                        }).then(data => {
                            MessageBox.once('close', () => {
                                this.visible = false;
                                this.$emit('submit');
                            });
                            MessageBox.open('新建成功');
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                });
            }
        }
    }
</script>