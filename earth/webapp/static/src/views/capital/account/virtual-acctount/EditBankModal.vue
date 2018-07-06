<style lang="sass">
    
</style>

<template>
    <Modal v-model="visible">
        <ModalHeader 
            :title="fields.uuid ? '编辑银行卡' : '新增银行卡'"></ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                :model="fields" 
                :rules="rules" 
                class="sdf-form sdf-modal-form"
                label-width="120px">
                <el-form-item label="开户行" prop="bankCode" required>
                    <el-select 
                        filterable 
                        v-model="fields.bankCode"
                        clearable
                        class="middle">
                        <el-option
                          v-for="item in bankNames"
                          :label="item.value"
                          :value="item.key">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="银行账户号" prop="accountNo" required>
                    <el-input class="middle" v-model="fields.accountNo"></el-input>
                </el-form-item>
                <el-form-item label="开户行所在地">
                    <select-address>
                        <el-col :span="7">
                            <el-form-item prop="provinceCode">
                                <select-province 
                                    v-model="fields.provinceCode"
                                    @input="fields.province = arguments[1]">
                                </select-province>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item prop="cityCode">
                                <select-city 
                                    :province-code="fields.provinceCode"
                                    v-model="fields.cityCode"
                                    @input="fields.city = arguments[1]">
                                </select-city>
                            </el-form-item>
                        </el-col>
                    </select-address>
                </el-form-item>
                <el-form-item label="账户姓名" prop="accountName">
                    <el-input class="middle" v-model="fields.accountName"></el-input>
                </el-form-item>
                <el-form-item label="身份证号码" prop="idCardNo">
                    <el-input class="middle" v-model="fields.idCardNo"></el-input>
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
    import { mapState } from 'vuex';
    import { idCard } from 'src/validators';

    export default {
        props: {
            value: Boolean,
            model: {
                type: Object,
                default: {}
            },
            bankAccountAdapterList: {
                type: Array,
                default: () => []
            },
            virtualAccountUuid: String,
            contractId: String,
            financialContractUuid: String,
        },
        data: function() {
            var notExist = (accountNo, bankCode, uuid) => {

                var index = this.bankAccountAdapterList.findIndex(item => {
                    return item.accountNo === accountNo && item.uuid !== uuid;
                });

                return index === -1;
            };

            var validateIDCard = (rule, value, callback) => {
                if (!value) {
                    callback();
                } else {
                    idCard(value) ? callback() : callback(new Error('请输入合法的身份证号'));
                }
            };

            var validateAccountNo = (rule, value, callback) => {
                if (!/^\d+$/g.test(value)) {
                    callback(new Error('银行账户号格式不正确'));
                } else {
                    notExist(value, this.fields.bankCode, this.fields.uuid) ? callback() : callback(new Error('银行帐户已存在'));
                }
            };

            return {
                visible: this.value,
                submitting: false,
                fields: Object.assign({}, this.model),
                rules: {
                    bankCode: { required: true, message: ' ', trigger: 'change' },
                    accountNo: { required: true, validator: validateAccountNo, trigger: 'blur' },
                    idCardNo: { validator: validateIDCard, trigger: 'blur' }
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
            model: function(current) {
                this.fields = Object.assign({
                    bankCode: '',
                    accountNo: '',
                    provinceCode: '',
                    province: '',
                    cityCode: '',
                    city: '',
                    accountName: '',
                    idCardNo: '',
                }, current);
            }
        },
        computed: {
            ...mapState({
                bankNames: state => {
                    var arr = [];
                    var bankNames = state.financialContract.bankNames;
                    Object.keys(bankNames).forEach(bankCode => {
                        arr.push({
                            key: bankCode,
                            value: bankNames[bankCode]
                        });
                    });
                    return arr;
                }
            })
        },
        beforeMount: function() {
            this.$store.dispatch('getBankNames');
        },
        methods: {
            getBankName: function(bankCode) {
                var index = this.bankNames.findIndex(item => {
                    return item.key === bankCode
                });
                return this.bankNames[index].value;
            },
            submit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        var d = Object.assign({}, this.fields);
                        var uuid = d.uuid;

                        d.bankName = this.getBankName(d.bankCode);

                        this.submitting = true;

                        ajaxPromise({
                            url: uuid ? '/capital/customer-account-manage/virtual-account-list/bank_card_update_after' : '/capital/customer-account-manage/virtual-account-list/insert_bank_card',
                            data: {
                                uuid,
                                virtualAccountUuid: this.virtualAccountUuid,
                                contractId: this.contractId,
                                financialContractUuid: this.financialContractUuid,
                                bankAccountAdapterJson: JSON.stringify(d),
                            }
                        }).then((data) => {
                            this.visible = false;
                            this.$emit('submit', data.data);
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