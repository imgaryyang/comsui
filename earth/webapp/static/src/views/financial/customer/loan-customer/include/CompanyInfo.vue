<style lang="sass">
    .enterpriseInfo {
        .bd {
            &:before,&:after{
                content: '';
                display: table;
                clear: both;
            }
        }
        .col {
            width: 33%;
            display: inline-block;
            float: left;
            p {
                color: #666;
                line-height: 32px;
            }
        }
    }
</style>
<template>
    <div class="enterpriseInfo">
        <div v-if="!isEdit" class="bd">
            <div class="col">
                <p>公司名称：<span>{{ customer.name }}</span></p>
                <p>成立日期：<span>{{ customerEnterprise.birthday | formatDate }}</span></p>
                <p>证件类型：<span>{{ customer.idTypeStr }}</span></p>
                <p>证件号码：<span>{{ customer.desensitizationAccount }}</span></p>
            </div>
            <div class="col">
                <p>公司法人：<span>{{ customerEnterprise.legalPerson }}</span></p>
                <p>法人身份证号：<span>{{ customerEnterprise.desensitizationIdCardNum }}</span></p>
                <p>公司所属行业：<span>{{ customerEnterprise.industryStr }}</span></p>
                <p>公司地址：<span>{{ customerEnterprise.address }}</span></p>
            </div>
            <div class="col">
                <p>公司注册金：<span>{{ customerEnterprise.registeredCapital }}</span></p>
                <p>公司类型：<span>{{ customerEnterprise.companyTypeStr }}</span></p>
            </div>
        </div>
        <div v-else class="bd">
            <el-form
                ref="form"
                :model="model"
                :rules="rules"
                label-width="120px">
                <div class="col">
                    <el-form-item label="公司名称" prop="name" required>
                        <el-input v-model="model.name"></el-input>
                    </el-form-item>
                    <el-form-item label="成立日期">
                        <DateTimePicker
                            v-model="model.birthdayString"
                            placeholder="请选择日期"
                            size="large">
                        </DateTimePicker>
                    </el-form-item>
                    <el-form-item label="证件类型" prop="idTypeOrdinal" required>
                        <el-select v-model="model.idTypeOrdinal" placeholder="请选择证件类型">
                            <el-option
                                v-for="item in options.IDType_enterprise"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="证件号码" prop="idNumber" required>
                        <el-input v-model="model.idNumber"></el-input>
                    </el-form-item>
                </div>

                <div class="col">
                    <el-form-item label="公司法人">
                        <el-input v-model="model.legalPerson"></el-input>
                    </el-form-item>
                    <el-form-item label="法人身份证号" prop="idCardNum">
                        <el-input v-model="model.idCardNum"></el-input>
                    </el-form-item>
                    <el-form-item label="公司所属行业">
                        <el-select v-model="model.industryOrdinal" placeholder="请选择公司所属行业">
                            <el-option
                                v-for="item in options.industry"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="公司地址">
                        <el-input v-model="model.address"></el-input>
                    </el-form-item>
                </div>

                <div class="col">
                    <el-form-item label="公司注册资金" prop="registeredCapital">
                        <el-input v-model="model.registeredCapital"></el-input>
                    </el-form-item>
                    <el-form-item label="公司类型">
                        <el-select v-model="model.companyTypeOrdinal" placeholder="请选择公司类型">
                            <el-option
                                v-for="item in options.companyType"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </div>
            </el-form>
        </div>
    </div>
</template>
<script>
    import { REGEXPS , idCard} from 'src/validators';
    import format from 'filters/format';
    
    export default {
        props: {
            value: {
                type: Object,
                default: () => {}
            },
            options: {
                type: Object,
                default: () => {}
            },
            customer: {
                type: Object,
                default: () => ({})
            },
            customerEnterprise: {
                type: Object,
                default: () => ({})
            },
            isEdit: Boolean
        },
        data: function(){
            const registeredCapital = (rule, value, callback) => { 
                value === '' ? callback() :  REGEXPS.MONEY.test(value) ?  callback() : callback(new Error('金额格式不正确'));
            };
            const idCardValidate = (rule, value, callback) => { 
                value == '' ? callback() :  idCard(value) ?  callback() : callback(new Error('请输入合法的身份证号'));
            };
            return {
                model: {
                    customerUuid: '',
                    name: '',
                    birthdayString: '',
                    idTypeOrdinal: '',
                    idNumber: '',
                    legalPerson: '',
                    idCardNum: '',
                    industryOrdinal: '',
                    address: '',
                    registeredCapital: '',
                    companyTypeOrdinal: ''
                },
                rules: {
                    name: {required: true, message: ' ', trigger: 'blur'},
                    idTypeOrdinal: {type: 'number', required: true, message: ' ', trigger: 'change'},
                    idNumber: {required: true, message: ' ', trigger: 'blur'},
                    idCardNum: {validator: idCardValidate, trigger: 'blur'},
                    registeredCapital: {validator: registeredCapital, trigger: 'blur'}
                },
            }
        },
        watch: {
            customerEnterprise: function(current) {
               this.model = {
                    customerUuid: '',
                    name: '',
                    birthdayString: '',
                    idTypeOrdinal: '',
                    idNumber: '',
                    legalPerson: '',
                    idCardNum: '',
                    industryOrdinal: '',
                    address: '',
                    registeredCapital: '',
                    companyTypeOrdinal: ''
                }
                this.model = Object.assign(this.model, current);
                this.model.name = this.customer.name || '';
                this.model.idTypeOrdinal = this.customer.idTypeOrdinal;
                this.model.idNumber = this.customer.account || '';
                this.model.customerUuid = this.customer.customerUuid || '';
                this.model.birthdayString = format.formatDate(this.customerEnterprise.birthday) || '';
            }
        },
        methods: {
            validate: function() {
                this.$refs.form.validate(valid => {
                    if(valid) {
                        this.$emit('submit', Object.assign({},this.model));
                    }
                })
            },
            resetFields: function() {
                this.$refs.form.resetFields();
                this.model = {
                    customerUuid: '',
                    name: '',
                    birthdayString: '',
                    idTypeOrdinal: '',
                    idNumber: '',
                    legalPerson: '',
                    idCardNum: '',
                    industryOrdinal: '',
                    address: '',
                    registeredCapital: '',
                    companyTypeOrdinal: ''
                }
            }
        }
    }
</script>