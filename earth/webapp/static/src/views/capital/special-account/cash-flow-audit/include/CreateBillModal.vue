<template>
  <Modal v-model="visible" class="modal-create-bill">
    <ModalHeader title="新增充值账单"></ModalHeader>
    <ModalBody align="left">
        <el-form
            ref="form"
            :model="fields"
            :rules="rules"
            class="sdf-form sdf-modal-form"
            label-width="110px">
           <el-form-item prop="appId" :label="$utils.locale('financialContract.appAccount.name')" required>
                <el-select class="middle"
                    placeholder="请选择"
                    v-model="fields.appId">
                    <el-option
                        v-for="item in financialContractModels" 
                        :label="item.label"
                        :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item :label="$utils.locale('financialContract')" prop="financialContractUuid">
                <el-select
                    v-model="fields.financialContractUuid"
                    class="middle"
                    :placeholder="$utils.locale('financialContract')">
                    <el-option
                        v-for="item in currentFinancialContracts"
                        :label="item.contractName"
                        :value="item.financialContractUuid">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="客户类型" prop="customerType">
                <el-select
                  v-model="fields.customerType"
                  class="middle"
                  placeholder="客户类型">
                    <el-option
                        v-for="item in customerTypeList"
                        :label="item.value"
                        :value="item.key">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="虚户搜索" prop="customerUuid">
                <el-autocomplete
                    v-model="fields.keyWord"
                    class="long"
                    :fetch-suggestions="fetch"
                    @select="handleSelectCustomer"
                    custom-item="search-result-item">
                </el-autocomplete>
            </el-form-item>
            <el-form-item label="充值金额" prop="depositAmount">
                <el-input v-model="fields.depositAmount" class="middle"></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
                <el-input v-model="fields.remark" class="middle"></el-input>
            </el-form-item>
            <el-form-item>
                <el-checkbox v-model="fields.binding">绑定至虚户</el-checkbox>
            </el-form-item>
        </el-form>
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

    Vue.component('search-result-item', {
        functional: true,
        render: function(h, ctx) {
            var item = ctx.props.item;
            return h('li', ctx.data, [
                h('div', {}, [item.name + '(' + item.number + ')']),
            ]);
        },
        props: {
            item: {
                type: Object,
                required: true
            }
        }
    });

    export default {
        props: {
            value: Boolean,
            financialContractModels: Array,
            customerTypeList: Array,
            cashFlowUuid: String,
            doubtAmount: Number
        },
        data: function() {
            var validateCustomerUuidMatchKeyWrod = (rule, value, callback) => {
                if (this.fields.keyWord === this.fields.customerUuidMatchKeyWord) {
                    callback()
                } else {
                    callback(new Error(' '));
                }
            };
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
                creating: false,
                visible: this.value,
                fields: {
                    appId: '',
                    financialContractUuid: '',
                    customerType: '',
                    customerUuid: '',
                    customerUuidMatchKeyWord: '',
                    keyWord: '',
                    depositAmount: '',
                    remark: '',
                    binding: false
                },
                rules: {
                    appId: [
                        { required: true, message: ' ' }
                    ],
                    financialContractUuid: [
                        { required: true, message: ' ' }
                    ],
                    customerType: [
                        { required: true, message: ' ' }
                    ],
                    customerUuid: [
                        { required: true, message: ' ' },
                        { validator: validateCustomerUuidMatchKeyWrod }
                    ],
                    depositAmount: [
                        { required: true, message: ' ' },
                        { validator: validateAmount }
                    ],
                    remark: [
                        { required: true, message: ' '}
                    ]
                },
                modelList: []
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
                    this.fields.keyWord = '';
                }
            },
            'fields.appId': function(current) {
                this.fields.financialContractUuid = '';
            }
        },
        computed: {
            currentFinancialContracts: function() {
                var result = [];
                this.financialContractModels.forEach(item => {
                    if (item.value == this.fields.appId) {
                        result = item.children;
                    }
                });
                return result;
            }
        },
        methods: {
            fetch: function(search, callback) {
                if (!search || this.fields.customerType === '' || this.fields.financialContractUuid === '') {
                    callback([]);
                    return;
                }
                ajaxPromise({
                    url: `/capital/account-manager/cash-flow-audit/query-customer`,
                    data: {
                        name: this.fields.keyWord,
                        customerType: this.fields.customerType,
                        financialContractUuid: this.fields.financialContractUuid
                    }
                }).then(data => {
                    data.modelList.forEach(item => {
                        item.value = item.name + '(' + item.number + ')';
                    });
                    this.modelList = data.modelList || [];
                    callback(this.modelList);
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleSelectCustomer: function(selected) {
                this.fields.customerUuidMatchKeyWord = selected.value;
                this.fields.customerUuid = selected.customerUuid;
            },
            handleSubmit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        this.creating = true;
                        ajaxPromise({
                            url: `/capital/account-manager/cash-flow-audit/deposit`,
                            type: 'post',
                            data: {
                                customerUuid: this.fields.customerUuid,
                                financialContractUuid: this.fields.financialContractUuid,
                                cashFlowUuid: this.cashFlowUuid,
                                depositAmount: this.fields.depositAmount,
                                remark: this.fields.remark,
                                binding: this.fields.binding
                            }
                        }).then(data => {
                            MessageBox.once('close', () => {
                                this.$emit('submitSuccess');
                            });
                            MessageBox.open('充值单提交成功');
                        }).catch(requestError => {
                           this.$emit('submitFailed', requestError);
                        }).then(() => {
                            this.creating = false;
                        });
                    }
                });
            }
        }
    }
</script>
