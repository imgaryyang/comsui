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
                <el-form-item label="银行账户名" prop="accountName" required>
                    <el-input class="middle" v-model="fields.accountName"></el-input>
                </el-form-item>
                <el-form-item label="银行账户号" prop="accountNo" required>
                    <el-input class="middle" v-model="fields.accountNo"></el-input>
                </el-form-item>
                <el-form-item label="开户行" prop="bankCode" required>
                    <el-select 
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
                <el-form-item label="开户行所在地" required>
                    <select-address>
                        <el-col :span="7">
                            <el-form-item prop="provinceCode" required>
                                <select-province 
                                    v-model="fields.provinceCode"
                                    @input="fields.province = arguments[1]">
                                </select-province>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item prop="cityCode" required>
                                <select-city 
                                    :province-code="fields.provinceCode"
                                    v-model="fields.cityCode"
                                    @input="fields.city = arguments[1]">
                                </select-city>
                            </el-form-item>
                        </el-col>
                    </select-address>
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
            outerIdentifier: {
                type: String,
                default: ''
            },
            identityOrdinal: {
                type: Number,
                default: 0
            },
            isFromCreate: {//新增页面
                type: Boolean,
                default: false
            }
        },
        data: function() {
            var validateAccountNo = (rule, value, callback) => {
                !/^\d+$/g.test(value) ?  callback(new Error('银行账户号格式不正确')) : callback();
            };

            return {
                visible: this.value,
                submitting: false,
                fields: Object.assign({}, this.model),
                rules: {
                    bankCode: { required: true, message: ' ', trigger: 'change' },
                    accountNo: { required: true, validator: validateAccountNo, trigger: 'blur' },
                    accountName: { required: true, message: ' ', trigger: 'blur' },
                    provinceCode: { required: true, message: ' ', trigger: 'blur' },
                    cityCode: { required: true, message: ' ', trigger: 'blur' },
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
                        d.bankName = this.getBankName(d.bankCode);

                        if (this.isFromCreate) {
                            this.visible = false;
                            this.$emit('submitBank', d);
                            return
                        }

                        var uuid = d.uuid;
                        this.submitting = true;

                        ajaxPromise({
                            url: uuid ? '/bankCard/modify' : '/bankCard/add',
                            data: {
                                outerIdentifier: this.outerIdentifier,
                                identityOrdinal: this.identityOrdinal,
                                ...d
                            }
                        }).then((data) => {
                            this.visible = false;
                            this.$emit('submit');
                        }).catch(message => {
                            MessageBox.open(message);
                        }).then(() => {
                            this.submitting = false;
                        });
                    }
                });
            },
        }
    }
</script>