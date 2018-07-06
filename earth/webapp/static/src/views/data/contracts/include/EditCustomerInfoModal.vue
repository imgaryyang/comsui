<template>
	<Modal v-model="show">
        <ModalHeader title="修改还款账户信息">
        </ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                :model="currentModel"
                label-width="120px"
                :rules="rules"
                class="sdf-form sdf-modal-form">
                <el-form-item
                    label="还款方账户名"
                    prop="payerName"
                    required>
                    <el-input class="middle" v-model="currentModel.payerName">
                    </el-input>
                </el-form-item>
                <el-form-item
                    v-if="!isYunXin"
                    label="手机号"
                    prop="mobile"
                    required>
                    <el-input class="middle" v-model="currentModel.mobile">
                    </el-input>
                </el-form-item>
                <el-form-item
                    label="身份证号"
                    prop="idCardNum"
                    required>
                    <el-input class="middle" v-model="currentModel.idCardNum">
                    </el-input>
                </el-form-item>
                <el-form-item
                    label="银行账户号"
                    prop="bankAccount"
                    required>
                   	<el-input class="middle" v-model="currentModel.bankAccount">
                   	</el-input>
                </el-form-item>
                <el-form-item
                    label="开户行"
                    prop="bankCode"
                    required>
                   	<el-select class="middle" v-model="currentModel.bankCode">
                        <el-option
                          v-for="item in bankNames"
                          :label="item.value"
                          :value="item.key">
                        </el-option>
                   	</el-select>
                </el-form-item>
                <el-form-item label="开户行所在地">
                    <select-address>
                        <el-col :span="7">
                            <el-form-item>
                                <select-province
                                    v-model="currentModel.provinceCode"
                                    @input="currentModel.province = arguments[1]">
                                </select-province>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item>
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
	import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import modalMixin from './modal-mixin';
    import { idCard, contact } from 'src/validators';

    export default {
    	mixins: [modalMixin],
    	props: {
            model: {
                type: Object,
                default: () => ({}),
            },
            contractId: '',
            coreBanks: {
                type: Object,
                default: () => ({}),
            },
            isYunXin: Boolean
    	},
    	data: function() {
            var validateAccountNo = (rule, value, callback) => { 
                /^\d+$/g.test(value) ? callback() : callback(new Error('请输入数字且只能输入数字'));
            };
            var validateIDCard = (rule, value, callback) => {
                if (!value) {
                    callback(new Error('请输入身份证号'));
                } else {
                    idCard(value) ? callback() : callback(new Error('请输入合法的身份证号'));
                }
            };
            var validateMobile = (rule, value, callback) => {
                if (!value) {
                    callback(new Error('请输入手机号'));
                } else {
                    contact(value) ? callback() : callback(new Error('请输入正确的手机号'))
                }
            };
    		return {
    			show: this.value,
                currentModel: Object.assign({}, this.model),
                bankNames: [],
                rules: {
                    payerName: {required: true, message: ' ', trigger: 'blur'},
                    mobile: {required: true, validator: validateMobile, trigger: 'blur'},
                    idCardNum: {validator: validateIDCard, trigger: 'blur' },
                    bankAccount: {required: true, validator: validateAccountNo, trigger: 'blur'},
                    bankCode: {required: true, message: ' ', trigger: 'blur'},
                },
    		}
    	},
    	watch: {
    		model: function(cur) {
    			this.currentModel = Object.assign({
                    payerName: '',
                    bankCode: '',
                    bankAccount: '',
                    provinceCode: '',
                    cityCode: '',
                    idCardNum: '',
                    mobile: '',
                }, cur);
    		},
    		show: function(cur) {
    			if(!cur) {
                    this.$refs.form.resetFields();
    			}
    		},
            coreBanks: function(cur) {
                var arr = [];
                Object.keys(cur).forEach(bankCode => {
                arr.push({
                        key: bankCode,
                        value: cur[bankCode]
                    });
                });
                this.bankNames = arr;
            }
    	},
    	methods: {
            submit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        this.currentModel.contractId = this.contractId;
                        this.modifyReapymentInfo(this.currentModel);
                    }
                });
            },
            modifyReapymentInfo: function(data) {
                ajaxPromise({
                    url: `/modifyContractAccount/repaymentInfo/modify`,
                    data: data,
                    type: 'post',
                }).then(data => {
                    MessageBox.once('close', () => {
                        this.show = false;
                        setTimeout(() => {
                            this.$emit('submit');
                        },300);
                        this.fetchSysLog();
                    });
                    MessageBox.open('操作成功');
                }).catch(message => {
                    MessageBox.open(message);
                })
            }
    	}
    }
</script>