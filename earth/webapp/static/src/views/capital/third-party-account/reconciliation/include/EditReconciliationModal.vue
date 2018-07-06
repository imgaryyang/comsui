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
                :style="{'margin-left': '20px'}"
                label-width="145px">
                <el-form-item prop="appId" :label="$utils.locale('financialContract.appAccount.name')" required>
                    <el-select class="middle"
                        placeholder="请选择"
                        v-model="fields.appId">
                        <el-option
                            v-for="item in financialContractQueryModels" 
                            :label="item.label"
                            :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="financialContractUuid" :label="$utils.locale('financialContract')" required>
                    <el-select 
                        class="middle" 
                        :placeholder="$utils.locale('financialContract')"
                        v-model="fields.financialContractUuid">
                       <el-option 
                            v-for="item in currentFinancialContracts" 
                            :label="item.label"
                            :value="item.value">
                            <div>{{ item.label }}</div>
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="capitalAccountNo" label="账户" required>
                    <el-select 
                        v-model="fields.capitalAccountNo"
                        class="middle"
                        placeholder="请选择账户"
                        :dropdown-class="'richtext-dropdown'">
                        <el-option 
                            v-for="item in capitalAcounts" 
                            :label="item.accountName"
                            :value="item.accountNo">
                            <div>{{ item.accountName }}</div>
                            <div>{{ item.accountNo}}（{{item.bankName}}）</div>
                        </el-option>
                    </el-select>
                </el-form-item>
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
                <el-form-item prop="paymentChannelUuid" label="交易通道及清算号" required>
                    <el-select 
                        class="middle"
                        placeholder="请选择交易通道及清算号"
                        v-model="fields.paymentChannelUuid"
                        dropdown-class="richtext-dropdown">
                        <el-option 
                            v-for="item in showPaymentChannels" 
                            :label="item.paymentInstitutionNameMsg"
                            :value="item.paymentChannelUuid">
                            <div>{{ item.paymentInstitutionNameMsg }}</div>
                            <div>{{ item.clearingNo }}（{{ item.creditChannelWorkingStatus == 'ON' ? '代付' : '' }} {{ item.debitChannelWorkingStatus == 'ON' ? '代收' : ''}}）</div>
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
                default: false
            },
            financialContractQueryModels: {
                required: true
            }
        },
        computed: {
            capitalAcounts: function() {
                var selected = this.currentFinancialContracts.filter(item => {
                    return item.financialContractUuid === this.fields.financialContractUuid;
                });
                var item = selected[0];
                if (item && item.capitalAccount) {
                    this.fields.capitalAccountNo = item.capitalAccount.accountNo;
                    return [item.capitalAccount];
                } else {
                    this.fields.capitalAccountNo = '';
                    return [];
                }
            },
            showPaymentChannels: function() {
                var type;

                if (this.fields.accountSide == 0) {
                    type = 'creditChannelWorkingStatus';
                } else if (this.fields.accountSide == 1) {
                    type = 'debitChannelWorkingStatus';
                } else {
                    return [];
                }

                var res = this.paymentChannels.filter(item => {
                    return item[type] == 'ON';
                });

                return res;
            },
            currentFinancialContracts: function() {
                var result = [];
                this.fields.financialContractUuid = '';
                this.financialContractQueryModels.forEach(item => {
                    if (item.value == this.fields.appId) {
                        result = item.children;
                    }
                });
                return result;
            }
        },
        data: function() {
            return {
                visible: this.value,

                paymentChannels: [],
                accountSides: [{
                    key: 0,
                    value: '代付'
                }],

                fields: {
                    appId: '',
                    capitalAccountNo: '',
                    financialContractUuid: '',
                    paymentChannelUuid: '',
                    pgClearingAccount: '',
                    startTime: '',
                    endTime: '',
                    accountSide: ''
                },

                rules: {
                    appId: { required: true, message: ' '},
                    capitalAccountNo: { required: true, message: ' '},
                    financialContractUuid: { required: true, message: ' '},
                    paymentChannelUuid: { required: true, message: ' '},
                    pgClearingAccount: { required: true, message: ' '},
                    startTime: { required: true, message: ' '},
                    endTime: { required: true, message: ' '},
                    accountSide: { required: true, message: ' '}
                }
            };
        },
        watch: {
            'fields.paymentChannelUuid': function(current) {
                var index = this.showPaymentChannels.findIndex(item => item.paymentChannelUuid === current);
                if (index !== -1) {
                    this.fields.pgClearingAccount = this.showPaymentChannels[index].clearingNo;
                }
            },
            'fields.financialContractUuid': function(current) {
                ajaxPromise({
                    url: `/audit/remittance/query-payment-channel`,
                    data: {
                        finnalContractUuid: current
                    }
                }).then(data => {
                    this.paymentChannels = data.paymentChannelInformationList || [];
                });
            },
            visible: function(current) {
                this.$emit('input', current);
                if (!current) {
                    this.$refs.form.resetFields();
                }
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
                            url: '/audit/remittance/create-audit-job',
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