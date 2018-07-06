<template>
    <Modal v-model="show">
        <ModalHeader :title="openType == 'add' ? '新增银行卡' : '编辑银行卡'">
        </ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                :model="currentModel"
                label-width="120px"
                :rules="rules"
                class="sdf-form sdf-modal-form">
                <el-form-item label="银行账户名" prop="accountName" required>
                    <el-input class="middle" v-model="currentModel.accountName"></el-input>
                </el-form-item>
                <el-form-item
                    label="银行账户号"
                    prop="accountNo"
                    required>
                    <el-input class="middle" v-model="currentModel.accountNo"></el-input>
                </el-form-item>
                <el-form-item
                    label="开户行"
                    prop="bankCode"
                    required>
                    <el-select 
                        v-model="currentModel.bankCode"
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
                            <el-form-item prop="provinceCode">
                                <select-province 
                                    v-model="currentModel.provinceCode"
                                    @input="currentModel.province = arguments[1]">
                                </select-province>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item prop="cityCode">
                                <select-city 
                                    :province-code="currentModel.provinceCode"
                                    v-model="currentModel.cityCode"
                                    @input="currentModel.city = arguments[1]">
                                </select-city>
                            </el-form-item>
                        </el-col>
                    </select-address>
                </el-form-item>
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button @click="submit" type="success">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { mapState } from 'vuex';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import modalMixin from './modal-mixin';
    import { idCard } from 'src/validators';

    export default {
        mixins: [modalMixin],
        props: {
            value: Boolean,
            model: {
                type: Object,
                default: () => ({}),
            },
            openType: String
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
        data: function() {
            var validateAccountNo = (rule, value, callback) => { 
                /^\d+$/g.test(value) ? callback() : callback(new Error('请输入数字且只能输入数字'));
            };
            return {
                show: this.value,
                currentModel: Object.assign({}, this.model),
                bankNames: [],
                rules: {
                    accountName: {required: true, message: ' ', trigger: 'blur'},
                    bankCode: {required: true, message: ' ', trigger: 'change'},
                    accountNo: {required: true, validator: validateAccountNo, trigger: 'blur'},
                    provinceCode: {required: true, message: ' ', trigger: 'change'},
                    cityCode: {required: true, message: ' ', trigger: 'change'}
                },
            }
        },
        watch: {
            model: function(cur) {
                this.currentModel = Object.assign({
                    bankCode: '',
                    accountNo: '',
                    provinceCode: '',
                    province: '',
                    cityCode: '',
                    city: '',
                    accountName: '',
                }, cur);
            },
            value: function(cur) {
                this.show = cur;
            },
            show: function(cur) {
                if(!cur) {
                    this.$refs.form.resetFields();
                }
                this.$emit('input', cur);
            }
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
                        var d = Object.assign({}, this.currentModel);
                        d.bankName = this.getBankName(d.bankCode);
                        this.$emit('submit', d)
                    }
                });
            }
        }
    }
</script>