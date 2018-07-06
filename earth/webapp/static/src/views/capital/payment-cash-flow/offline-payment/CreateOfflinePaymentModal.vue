<style lang="sass">
    #createOfflinePaymentModal {
        .table td {
            font-size: 12px;
            vertical-align: middle;
        }
    }
</style>

<template>
    <Modal v-model="visible" id="createOfflinePaymentModal">
        <ModalHeader title="新增线下支付单"></ModalHeader>
        <ModalBody align="left" v-loading="fetching">
            <table class="table">
                <col name="col_1" width="110"></col>
                <col name="col_2" width="auto"></col>
                <col name="col_3" width="110"></col>
                <col name="col_4" width="auto"></col>
                <tbody>
                    <tr>
                        <td>{{$utils.locale('financialContract.appAccount.name')}}</td>
                        <td colspan="3">
                            <el-select
                            v-model="model.appId"
                            :placeholder="$utils.locale('financialContract.appAccount.name')"
                            clearable
                            size="small">
                                <el-option
                                    v-for="item in financialContractQueryModels" 
                                    :label="item.label"
                                    :value="item.value">
                                </el-option>
                        </el-select>
                        </td>
                    </tr>
                    <tr>
                        <td>{{$utils.locale('financialContract')}}</td>
                        <td colspan="3">
                            <el-select
                            v-model="model.financialContractUuid"
                            :placeholder="$utils.locale('financialContract')"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in currentFinancialContracts"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                        </td>
                    </tr>
                    <tr>
                        <td>还款方名称</td>
                        <td>
                            <el-input size="small" v-model="model.payerAccountName"></el-input>
                        </td>
                        <td>支付机构</td>
                        <td><el-input size="small"></el-input></td>
                    </tr>
                    <tr>
                        <td>还款方开户行</td>
                        <td>
                            <el-input size="small" v-model="model.bankShowName"></el-input>
                        </td>
                        <td>支付机构流水号</td>
                        <td>
                            <el-input size="small" v-model="model.serialNo"></el-input>
                        </td>
                    </tr>
                    <tr>
                        <td>还款方账户</td>
                        <td>
                            <el-input size="small" v-model="model.payerAccountNo"></el-input>
                        </td>
                        <td>发生时间</td>
                        <td>
                            <span v-if="date">{{ date | formatDate }}</span>
                            <DateTimePicker
                                v-else
                                v-model="model.tradeTimeString"
                                :pickTime="true"
                                style="line-height: 28px;"
                                size="small"
                                placeholder="发生时间">
                            </DateTimePicker>
                        </td>
                    </tr>
                    <tr>
                        <td>发生金额</td>
                        <td>
                            <div v-if="totalAmount">{{ totalAmount }}元</div>
                            <el-input size="small" v-else v-model="model.amount"></el-input>
                        </td>
                        <td>备注</td>
                        <td>
                            <el-input size="small" v-model="model.comment"></el-input>
                        </td>
                    </tr>
                </tbody>
            </table>
        </ModalBody>
        <ModalFooter>
            <el-button @click="visible = false">关闭</el-button>
            <el-button type="success" :disabled="submitDisabled" @click="handleCreate" :loading="creating">提交</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        props: {
            value: Boolean,
            preCreateAction: String,
            createAction: String,
            financialContractQueryModels: Array
        },
        data: function() {
        	return {
                visible: false,
                fetching: false,
                creating: false,
                date: '',
                totalAmount: '',
                guaranteeRepaymentUuids: '',
                source: '',
                model: {
                    appId: '',
                    financialContractUuid: '',
                    payerAccountName: '',
                    bankShowName: '',
                    serialNo: '',
                    payerAccountNo: '',
                    tradeTimeString: '',
                    amount: '',
                    comment: ''
                },

                submitDisabled: false
        	};
        },
        watch: {
            value: function(current) {
                this.visible = current;
                if (current) {
                    this.fetchBillPreCreate();
                    this.submitDisabled = false;
                }
            },
            visible: function(current, previous) {
                this.$emit('input', current);
                if (!current) {
                    this.model = {
                        appId: '',
                        financialContractUuid: '',
                        payerAccountName: '',
                        bankShowName: '',
                        serialNo: '',
                        payerAccountNo: '',
                        tradeTimeString: '',
                        amount: '',
                        comment: ''
                    }
                }
            },
            'model.appId': function(current) {
                this.model.financialContractUuid = '';
            }
        },
        computed: {
            currentFinancialContracts: function() {
                var result = [];
                this.financialContractQueryModels.forEach(item => {
                    if (item.value == this.model.appId) {
                        result = item.children;
                    }
                });
                return result;
            }
        },
        methods: {
            fetchBillPreCreate: function() {
                if (this.preCreateAction) {
                    ajaxPromise({
                        url: this.preCreateAction
                    }).then(data => {
                        this.date = data.date;
                        this.totalAmount = data.totalAmount;
                        this.guaranteeRepaymentUuids = data.guaranteeRepaymentUuids;
                        this.source = data.source;
                        this.fetching = false;
                    });
                }
            },
            handleCreate: function() {
                if (this.createAction) {
                    this.submitDisabled = true;
                    this.creating = true;
                    ajaxPromise({
                        url: '/offline-payment-manage/payment/create-offline-bill',
                        data: {
                            ...this.model,
                            guaranteeRepaymentUuids: this.guaranteeRepaymentUuids
                        },
                        type: 'post'
                    }).then(data => {
                        this.submitDisabled = false;
                        MessageBox.once('close', () => {
                            this.visible = false;
                        });
                        MessageBox.once('closed', () => {
                            this.$emit('submit');
                        });
                        MessageBox.open('生成支付单号：' + data.offlineBillNo);
                    }).catch(message => {
                        MessageBox.open(message);

                    }).then(() => {
                        this.creating = false;
                        this.submitDisabled = false;
                    });
                }
            }
        }
    }
</script>