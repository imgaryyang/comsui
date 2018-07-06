<style lang="sass">
    .personInfo {
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
    <div class="personInfo">
        <div v-if="!isEdit" class="bd">
            <div class="col">
                <p>客户姓名：<span>{{ customer.name }}</span></p>
                <p>性别：<span>{{ customerPerson.sexStr }}</span></p>
                <p>出生日期：<span>{{ customerPerson.birthday | formatDate }}</span></p>
                <p>证件类型：<span>{{ customer.idTypeStr }}</span></p>
                <p>证件号码：<span>{{ customer.desensitizationAccount }}</span></p>
                <p>手机号码：<span>{{ customerPerson.mobile }}</span></p>
                <p>婚姻状况：<span>{{ customerPerson.maritalStatusStr }}</span></p>
            </div>
            <div class="col">
                <p>最高学历：<span>{{ customerPerson.highestEducationStr }}</span></p>
                <p>最高学位：<span>{{ customerPerson.highestDegreeStr }}</span></p>
                <p>居住状况：<span>{{ customerPerson.residentialStatusStr }}</span></p>
                <p>居住地址：<span>{{ customerPerson.residentialAddress }}</span></p>
                <p>通讯地址：<span>{{ customerPerson.postalAddress }}</span></p>
                <p>居住地邮编：<span>{{ customerPerson.residentialCode }}</span></p>
                <p>通讯地邮编：<span>{{ customerPerson.postalCode }}</span></p>
            </div>
            <div class="col">
                <p>职业：<span>{{ customerPerson.occupationStr }}</span></p>
                <p>职务：<span>{{ customerPerson.dutyStr }}</span></p>
                <p>职称：<span>{{ customerPerson.titleStr }}</span></p>
                <p>单位所属行业：<span>{{ customerPerson.industryStr }}</span></p>
                <p>单位名称：<span>{{ customerPerson.companyName }}</span></p>
            </div>
        </div>
        <div v-else class="bd">
            <el-form
                ref="form"
                :model="model"
                :rules="rules"
                label-width="120px">
                <div class="col">
                    <el-form-item label="客户姓名" prop="name" required>
                        <el-input v-model="model.name"></el-input>
                    </el-form-item>
                    <el-form-item label="性别">
                        <el-select v-model="model.sexOrdinal" placeholder="请选择性别">
                            <el-option
                                v-for="item in options.sex"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="出生日期">
                        <DateTimePicker
                            v-model="model.birthdayString"
                            placeholder="请选择出生日期"
                            size="large">
                        </DateTimePicker>
                    </el-form-item>
                    <el-form-item label="证件类型" prop="idTypeOrdinal" required>
                        <el-select v-model="model.idTypeOrdinal" placeholder="请选择证件类型">
                            <el-option
                                v-for="item in options.IDType_person"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="证件号码" prop="idNumber" required>
                        <el-input v-model="model.idNumber"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号码" prop="mobile">
                        <el-input v-model="model.mobile"></el-input>
                    </el-form-item>
                    <el-form-item label="婚姻状况">
                        <el-select v-model="model.maritalStatusOrdinal" placeholder="请选择婚姻状况">
                            <el-option
                                v-for="item in options.maritalStatus"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </div>

                <div class="col">
                    <el-form-item label="最高学历">
                        <el-select v-model="model.highestEducationOrdinal" placeholder="请选择学历">
                            <el-option
                                v-for="item in options.education"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="最高学位">
                        <el-select v-model="model.highestDegreeOrdinal" placeholder="请选择学位">
                            <el-option
                                v-for="item in options.degree"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="居住状况">
                        <el-select v-model="model.residentialStatusOrdinal" placeholder="请选择居住状况">
                            <el-option
                                v-for="item in options.residentialStatus"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="居住地址">
                        <el-input v-model="model.residentialAddress"></el-input>
                    </el-form-item>
                    <el-form-item label="通讯地址">
                        <el-input v-model="model.postalAddress"></el-input>
                    </el-form-item>
                    <el-form-item label="居住地邮编">
                        <el-input v-model="model.residentialCode"></el-input>
                    </el-form-item>
                    <el-form-item label="通讯地邮编">
                        <el-input v-model="model.postalCode"></el-input>
                    </el-form-item>
                </div>

                <div class="col">
                    <el-form-item label="职业">
                        <el-select v-model="model.occupationOrdinal" placeholder="请选择职业">
                            <el-option
                                v-for="item in options.occupation"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="职务">
                        <el-select v-model="model.dutyOrdinal" placeholder="请选择职务">
                            <el-option
                                v-for="item in options.duty"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="职称">
                        <el-select v-model="model.titleOrdinal" placeholder="请选择职称">
                            <el-option
                                v-for="item in options.title"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="单位所属行业">
                        <el-select v-model="model.industryOrdinal" placeholder="请选择单位所属行业">
                            <el-option
                                v-for="item in options.industry"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="单位名称">
                        <el-input v-model="model.companyName"></el-input>
                    </el-form-item>
                </div>
            </el-form>
        </div>
    </div>
</template>
<script>
    import format from 'filters/format';
    import { REGEXPS , idCard} from 'src/validators';

    export default {
        props: {
            value: {
                type: Object,
                default: () => {}
            },
            options: {
                type: Object,
                default: () => ({})
            },
            customer: {
                type: Object,
                default: () => ({})
            },
            customerPerson: {
                type: Object,
                default: () => ({})
            },
            isEdit: Boolean
        },
        data: function(){
            const idNumberValidate = (rule, value, callback) => { 
                value === '' ? callback(new Error(' ')) :  this.model.idTypeOrdinal === 0 && !idCard(value) ?  callback(new Error('请输入合法的身份证号')) : callback();
            };
            const mobileValidate = (rule, value, callback) => {
                value != '' && !REGEXPS.MOBILE.test(value) ?  callback(new Error('手机格式有误')) : callback();
            };
            return {
                model: {
                    customerUuid: '',
                    name: '',
                    sexOrdinal: '',
                    birthdayString: '',
                    idTypeOrdinal: '',
                    idNumber: '',
                    mobile: '',
                    maritalStatusOrdinal: '',
                    highestEducationOrdinal: '',
                    highestDegreeOrdinal: '',
                    residentialStatusOrdinal: '',
                    residentialAddress: '',
                    postalAddress: '',
                    residentialCode: '',
                    postalCode: '',
                    occupationOrdinal: '',
                    dutyOrdinal: '',
                    titleOrdinal: '',
                    industryOrdinal: '',
                    companyName: ''
                },
                rules: {
                    name: {required: true, message: ' ', trigger: 'blur'},
                    mobile: {validator: mobileValidate, trigger: 'blur'},
                    idTypeOrdinal: {type: 'number', required: true, message: ' ', trigger: 'blur'},
                    idNumber: {required: true, validator: idNumberValidate, trigger: 'blur'}
                },
            }
        },
        watch: {
            customerPerson: function(current) {
               this.model = {
                    customerUuid: '',
                    name: '',
                    sexOrdinal: '',
                    birthdayString: '',
                    idTypeOrdinal: '',
                    idNumber: '',
                    mobile: '',
                    maritalStatusOrdinal: '',
                    highestEducationOrdinal: '',
                    highestDegreeOrdinal: '',
                    residentialStatusOrdinal: '',
                    residentialAddress: '',
                    postalAddress: '',
                    residentialCode: '',
                    postalCode: '',
                    occupationOrdinal: '',
                    dutyOrdinal: '',
                    titleOrdinal: '',
                    industryOrdinal: '',
                    companyName: ''
                }
                this.model = Object.assign(this.model, current);
                this.model.name = this.customer.name || '';
                this.model.idTypeOrdinal = this.customer.idTypeOrdinal;
                this.model.idNumber = this.customer.account || '';
                this.model.customerUuid = this.customer.customerUuid || '';
                this.model.birthdayString = format.formatDate(this.customerPerson.birthday) || '';
            }
        },
        methods: {
            validate: function() {
                this.$refs.form.validate(valid => {
                    if(valid) {
                        this.$emit('submit', Object.assign({},this.model));
                    }
                });
            },
            resetFields: function() {
                this.$refs.form.resetFields();
                this.model = {
                    customerUuid: '',
                    name: '',
                    sexOrdinal: '',
                    birthdayString: '',
                    idTypeOrdinal: '',
                    idNumber: '',
                    mobile: '',
                    maritalStatusOrdinal: '',
                    highestEducationOrdinal: '',
                    highestDegreeOrdinal: '',
                    residentialStatusOrdinal: '',
                    residentialAddress: '',
                    postalAddress: '',
                    residentialCode: '',
                    postalCode: '',
                    occupationOrdinal: '',
                    dutyOrdinal: '',
                    titleOrdinal: '',
                    industryOrdinal: '',
                    companyName: ''
                }
            }
        }
    }
</script>