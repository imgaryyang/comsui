<style lang="sass">
</style>

<template>
    <div id="editBasicInfo">
        <el-form 
            :model="currentModel" 
            :rules="rules" 
            ref="form"
            label-width="120px" 
            class="sdf-form">
            <el-form-item label="合同基础信息" class="form-item-legend"></el-form-item>
            <el-form-item :label="$utils.locale('financialContract.company')" prop="companyUuid" required>
                <el-select class="long" v-model="currentModel.companyUuid">
                    <el-option
                        v-for="item in companyList"
                        :label="item.value"
                        :value="item.key">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item :label="$utils.locale('financialContract.no')" prop="financialContractNo" required>
                <el-input class="long" v-model.trim="currentModel.financialContractNo"></el-input>
            </el-form-item>
            <el-form-item label="资产方" prop="appId" required>
                <el-select class="long" v-model="currentModel.appId">
                    <el-option 
                        v-for="item in appList" 
                        :label="item.value"
                        :value="item.key">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="资金方" prop="capitalParty">
                <MerchantSelect
                    :data="appList"
                    :selected="currentModel.capitalParty"
                    @add="onMerchantAdd(arguments[0], currentModel.capitalParty)"
                    @delete="onMerchantDelete(arguments[0], currentModel.capitalParty)">
                </MerchantSelect>
            </el-form-item>
            <el-form-item label="其他合作商户" prop="otherParty">
                <MerchantSelect
                    :data="appList"
                    :selected="currentModel.otherParty"
                    @add="onMerchantAdd(arguments[0], currentModel.otherParty)"
                    @delete="onMerchantDelete(arguments[0], currentModel.otherParty)">
                </MerchantSelect>
            </el-form-item>
            <el-form-item :label="$utils.locale('financialContract.name')" prop="financialContractName" required>
                <el-input class="long" v-model.trim="currentModel.financialContractName"></el-input>
            </el-form-item>
            <el-form-item label="信托合同简称" prop="financialContractShortName">
                <el-input class="long" v-model.trim="currentModel.financialContractShortName"></el-input>
            </el-form-item>
            <el-form-item label="信托类型" prop="financialType" required>
                <el-select class="long" v-model="currentModel.financialType">
                    <el-option 
                        v-for="item in financialTypes" 
                        :label="item.value"
                        :value="item.key">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item :label="$utils.locale('financialContract.deadline')" required>
                    <el-col :span="6">
                        <el-form-item prop="advaStartDate">
                            <DateTimePicker 
                                v-model="currentModel.advaStartDate"
                                :end-date="currentModel.thruDate"></DateTimePicker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="1"><div class="text-align-center color-dim">至</div></el-col>
                    <el-col :span="6">
                        <el-form-item prop="thruDate">
                            <DateTimePicker 
                                v-model="currentModel.thruDate"
                                :start-date="currentModel.advaStartDate"></DateTimePicker>
                        </el-form-item>
                    </el-col>
            </el-form-item>
            <el-form-item label="合同类型" prop="financialContractType" required>
                <el-select class="long" v-model="currentModel.financialContractType">
                    <el-option 
                        v-for="item in financialContractType" 
                        :label="item.value"
                        :value="item.key">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="收付子专户" prop="subAccounts">
                <SketchItem v-for="(item,index) in currentModel.subAccounts">
                    <img v-if="item.bankCode" :src="item.bankCode" class="stamp">
                    <div class="text">
                      <p>{{item.accountNo}}</p>
                      <p>{{item.accountName}} - {{ item.bankName }}</p>
                    </div>
                    <span class="operate">
                      <a class="edit" @click="onEditTrustAccount(item, 'subAccounts')"></a>
                      <a class="delete" @click="onDeleteSubTrustAccount(index)"></a>
                    </span>
                </SketchItem>
                <div>
                    <el-button class="button-multimedia" @click="onAddTrustAccount('subAccounts')">添加账户</el-button>
                </div>
            </el-form-item>
            <el-form-item label="账本数量" prop="isOpenBookBackups" required>
                <el-radio-group v-model="currentModel.isOpenBookBackups">
                    <el-radio :label="0">单账本</el-radio>
                    <el-radio :label="1">双账本（内部使用）</el-radio>
                    <el-radio :label="2">关程序</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item style="margin-top: 20px;">
                <el-button 
                    @click="next"
                    type="primary">
                    {{ isUpdate ? '提交' : '下一步' }}
                </el-button>
            </el-form-item>
        </el-form>

        <EditAccountModal 
            :is-update="modal.isUpdate"
            :model="modal.model"
            :identification="modal.identification"
            v-model="modal.show"
            @submit="onSubmitTrustAccount">
        </EditAccountModal>
    </div>
</template>

<script>
    import SketchItem from 'components/SketchItem';
    import { mapState } from 'vuex';
    import EditAccountModal from './EditAccountModal';
    import MerchantSelect from './MerchantSelect';

    export default {
        components: {
            SketchItem, EditAccountModal, MerchantSelect
        },
        props: {
            isUpdate: {
                default: false
            },
            model: {
                default: () => ({})
            },
            basicInfo: {
                default: () => ({})
            }
        },
        computed: {
            ...mapState({
                companyList: state => state.financialContract.companyList,
                appList: state => state.financialContract.appList,
                financialContractType: state => state.financialContract.financialContractType,
                financialTypes: state => state.financialContract.financialType
            })
        },
        data: function() {
            var validateFinancialType = (rule, value, callback) => {
                var error;

                error = value === '' || value == -1? new Error(' ') : void 0;

                callback(error);
            };
            return {
                modal: {
                    show: false,
                    isUpdate: false,
                    identification: '',
                    model: {},
                    _model: {}
                },
                rules: {
                    companyUuid: { required: true, message: ' ' },
                    financialContractNo: { required: true, message: ' ', trigger: 'blur', transform: value => value.trim()},
                    appId: { type: 'number', required: true, message: ' '},
                    financialContractName: { required: true, message: ' ', trigger: 'blur', transform: value => value.trim()},
                    advaStartDate: { required: true, message: ' ' },
                    thruDate: { required: true, message: ' ' },
                    financialContractType: { required: true, message: ' ' },
                    accountNo: { required: true, message: ' '},
                    accountName: { required: true, message: ' '},
                    financialType: {required: true, message: ' ', trigger: 'change', validator: validateFinancialType}
                },

                currentModel: {
                    financialContractNo: '',
                    financialContractName: '',
                    financialContractShortName: '',
                    companyUuid: '',
                    appId: '',
                    financialContractType: '',
                    accountName: '',
                    bankName: '',
                    accountNo: '',
                    advaStartDate: '',
                    thruDate: '',
                    financialType: '',
                    isOpenBookBackups: 0,

                    capitalParty: [],
                    otherParty: [],
                }
            };
        },
        watch: {
            model: {
                immediate: true,
                handler: function(cur) {
                    setTimeout(() => {
                        this.currentModel = Object.assign({}, this.currentModel, cur);
                    }, 0);
                }
            },
            basicInfo: function(cur){
                this.currentModel = Object.assign(this.currentModel,cur)
            }
        },

        beforeMount: function() {
            const { $store } = this;
            $store.dispatch('getCompanyList');
            $store.dispatch('getAppList');
            $store.dispatch('getFinancialContractType');
            $store.dispatch('getFinancialType');
        },

        methods: {
            next: function(e) {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        var data = JSON.parse(JSON.stringify(this.currentModel));
                        this.$emit('next', data);
                    } else {
                        $('.el-form-item__error').first().parent()[0].scrollIntoView();
                    }
                });
            },
            onMerchantAdd: function(key, list) {
                list.push(key)
            },
            onMerchantDelete: function(key, list) {
                var i = list.indexOf(key)
                if (i != -1) {
                    list.splice(i, 1)
                }
            },
            onSubmitTrustAccount: function(data) {
                var { currentModel, modal } = this;
                if(data.identification == 'subAccounts'){
                    if (modal.isUpdate) {
                        Object.assign(modal._model, data);
                    } else {
                        currentModel.subAccounts.push(data)
                    }
                }else{
                    currentModel.accountNo = data.accountNo;
                    currentModel.bankName = data.bankName;
                    currentModel.accountName = data.accountName;
                }
                modal.show = false;
            },
            onDeleteSubTrustAccount: function(index){
                this.currentModel.subAccounts.splice(index,1)
            },
            onEditTrustAccount: function(account, identification='') {
                var { modal } = this;
                modal.show = true;
                modal.identification = identification;
                modal.isUpdate = true;
                modal.model = Object.assign({}, account);
                modal._model = account;
            },
            onAddTrustAccount: function(identification='') {
                var { modal } = this;
                modal.show = true;
                modal.identification = identification;
                modal.isUpdate = false;
                modal.model = {};
            },
        }
    }
</script>