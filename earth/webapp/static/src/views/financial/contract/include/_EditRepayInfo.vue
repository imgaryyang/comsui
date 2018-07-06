<style lang="sass">
    #editRepayInfo {
        .subform {
            padding: 10px 20px;
        }

        .selectBox {
            margin: 20px;
            background: #f5f5f5;
            border: 1px #e0e0e0 solid;
            border-radius: 4px;
            width: 400px;
            padding: 15px;

            .selectItem {
                display: flex;
                margin: 10px;
                color: gray;

                .spanText {
                    width: 70px;
                    text-align: end;
                }

            }

            .repurchase-dropdown {

                .el-dropdown-link {
                    background: white;
                   
                }
            }
        }

        .sdf-form {
            .div-input {
                width: 180px;
                height: 34px;
                line-height: 34px;
                padding: 0 10px;
                border: 1px #e0e0e0 solid;
                border-radius: 4px;
                cursor: pointer;
                margin-right: 10px;
            }
        }

        .el-form-item {
            margin-bottom: 5px;
            padding-top: 5px;
        }
    }
</style>

<template>
    <div id="editRepayInfo">
        <el-form
            :model="currentModel"
            :rules="rules"
            ref="form"
            label-width="140px"
            class="sdf-form editForm">
            <el-form-item label="还款信息" class="form-item-legend"></el-form-item>
            <el-form-item label="还款类型" required prop="assetPackageFormat">
                <el-select class="middle" v-model="currentModel.assetPackageFormat">
                    <el-option
                        v-for="item in assetPackageFormat"
                        :label="item.value"
                        :value="item.key">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="还款模式" required>
                <el-form-item prop="allowRepayment">
                    <el-checkbox-group v-model="currentModel.allowRepayment">
                        <el-checkbox label="allowOnlineRepayment">线上还款</el-checkbox>
                        <el-checkbox label="allowOfflineRepayment">线下还款</el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <div
                    class="subform"
                    v-if="currentModel.allowRepayment.indexOf('allowOnlineRepayment') !== -1">
                    <el-form-item label="正常扣款" required>
                        <el-radio-group
                            :value="currentModel.sysNormalDeductFlag ? 'true' : 'false'"
                            @input="currentModel.sysNormalDeductFlag = arguments[0] === 'true'">
                            <el-radio label="true">系统定时发起</el-radio>
                            <el-radio label="false">接口主动调起</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="逾期扣款" required>
                        <el-radio-group
                            :value="currentModel.sysOverdueDeductFlag ? 'true' : 'false'"
                            @input="currentModel.sysOverdueDeductFlag = arguments[0] === 'true'">
                            <el-radio label="true">系统定时发起</el-radio>
                            <el-radio label="false">接口主动调起</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="允许接口提前划扣" required>
                        <el-radio-group
                            :value="currentModel.allowAdvanceDeductFlag ? 'true' : 'false'"
                            @input="currentModel.allowAdvanceDeductFlag = arguments[0] === 'true'">
                            <el-radio label="true">是</el-radio>
                            <el-radio label="false">否</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </div>
            </el-form-item>
            <el-form-item label="设定还款时间生效" prop="effectiveTime">
                <el-checkbox-group v-model="currentModel.effectiveTime" style="display: inline-block">
                    <el-checkbox label="effectiveOnline">线上还款</el-checkbox>
                    <el-checkbox label="effectiveOffline">线下还款</el-checkbox>
                </el-checkbox-group>
                <HelpPopover content="生效后实际还款时间取设定还款时间！"/>
            </el-form-item>
             <el-form-item label="设定还款时间生效规则" prop="repaymentCheckDays" v-if="currentModel.effectiveTime.length > 0" required>
                不早于订单创建前
                <el-input class="middle" v-model="currentModel.repaymentCheckDays"></el-input>
                <span class="muted">天</span>
                <HelpPopover content="若设定还款时间为M，订单创建时间为T，生效规则为不早于订单创建前N日，则T-M<=N，N为正整数。"/>
            </el-form-item>
            <el-form-item label="还款宽限日" prop="advaRepaymentTerm" required>
                <el-input class="middle" v-model="currentModel.advaRepaymentTerm"></el-input>
                <span class="muted">日</span>
                <HelpPopover content="免罚息期（自然日），若计划还款日为T日，还款宽限日为N日，则T+1~T+N日为宽限期，T+N+1日凌晨逾期状态置为待确认。"/>
            </el-form-item>
            <el-form-item label="还款计划变更时间">
                <el-radio-group v-model="currentModel.modifyFlag">
                    <el-radio :label="0">未到期</el-radio>
                    <el-radio :label="1">应还日及以前</el-radio>
                    <el-radio :label="2">宽限期及以前</el-radio>
                </el-radio-group>
                <HelpPopover content="实收为0，且没有在扣款中、支付中的还款计划允许变更的阶段 。"/>
            </el-form-item>

            <el-form-item label="逾期转坏账" prop="advaRepoTerm" required>
                <el-input class="middle" v-model="currentModel.advaRepoTerm"></el-input>
                <span class="muted">日</span>
                <HelpPopover content="逾期未还自动标记为坏账的天数。若计划还款日为T日，逾期转坏账为N日，则T+N日凌晨标记为坏账。"/>
            </el-form-item>
            <el-form-item label="商户打款宽限日" prop="advaMatuterm">
                <el-input class="middle" v-model="currentModel.advaMatuterm"></el-input>
                <span class="muted">工作日</span>
                <HelpPopover content="商户需在宽限日内打款。若坏账日为T日，商户打款宽限日为N日，则T+N日为打款截止日。"/>
            </el-form-item>
            <el-form-item label="逾期费用生成方式" required>
                <el-radio-group
                    :value="currentModel.sysCreatePenaltyFlag ? 'true' : 'false'"
                    @input="clearOverdueFeeTypes(arguments[0] === 'true')">
                    <el-radio label="true">系统生成</el-radio>
                    <el-radio label="false">对手方传递</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="逾期费用类型">
                <SketchItem v-for="item in overdueFeeTypes">
                    <div class="text">
                      <p>{{ currentModel.sysCreatePenaltyFlag ? item.details : '对手方传递' }}</p>
                      <p>{{ feeTypeKeyToZh(item.feeTypeKey) }}</p>
                    </div>
                    <span class="operate">
                      <a class="edit" @click="onEditFee(item)"></a>
                      <a class="delete" @click="onDeleteFee(item)"></a>
                    </span>
                </SketchItem>
                <div v-if="overdueFeeTypes.length < 4">
                    <el-button @click="onAddFee">添加类型</el-button>
                    <span class="muted font-size-12 color-dim">可添加多个类型</span>
                </div>
            </el-form-item>
            <el-form-item label="逾期状态自动确认" prop="allowNotOverdueAutoConfirm" required>
                <div>
                    未逾期自动确认
                    <el-radio-group v-model="currentModel.allowNotOverdueAutoConfirm">
                        <el-radio :label="1">开启</el-radio>
                        <el-radio :label="0">关闭</el-radio>
                    </el-radio-group>
                    <HelpPopover content="自动将逾期费用合计为0，且逾期状态为待确认的已结清还款计划的逾期状态标记为正常。"/>
                </div>
                <div>
                    已逾期自动确认
                    <el-radio-group v-model="currentModel.allowOverdueAutoConfirmIsOpen">
                        <el-radio :label="1">开启</el-radio>
                        <el-radio :label="0">关闭</el-radio>
                    </el-radio-group>
                    <HelpPopover content="自动将逾期费用合计不为0，且逾期状态为待确认的已结清还款计划的逾期状态标记为已逾期。"/>
                    <span v-if="currentModel.allowOverdueAutoConfirmIsOpen == 1" style="margin-left: 18px;">
                        逾期起始日：<el-button @click="onSetOverdueDate">计划还款日期+还款宽限日+{{currentModel.allowOverdueAutoConfirm}}天</el-button>
                    </span>
                </div>
            </el-form-item>
            <el-form-item label="回购受理方式" required>
                <el-form-item prop="repurchaseApproach">
                    <el-radio-group v-model="currentModel.repurchaseApproach">
                        <el-radio label="1">系统生成</el-radio>
                        <el-radio label="2">对手方传递</el-radio>
                        <el-radio label="0">无</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form-item>
            <el-form-item label="回购规则" v-if="currentModel.repurchaseApproach == '1'">
                <div style="display: flex; margin-top: -10px">
                    <el-radio class="radio" v-model="currentModel.repurchaseRule" label="0">坏账回购</el-radio>
                    <div style="margin: 10px 20px; display: flex" v-if="currentModel.repurchaseRule == '0'">
                        {{ currentModel.advaRepoTerm }}
                        <span class="muted" v-if="currentModel.advaRepoTerm !='' ">日</span>
                    </div>
                </div>
                <div style="display: flex">
                    <el-radio class="radio" v-model="currentModel.repurchaseRule" label="1">不定期</el-radio>
                    <div v-if="currentModel.repurchaseRule == 1" style="margin: 10px 20px; display: flex">
                        <div class="div-input" @click="showRepurchaseDateModal">
                            <span style="white-space: nowrap;max-width: 130px;display: inline-block;overflow: hidden;text-overflow: ellipsis;">
                            {{ repurchaseDateContent }}</span>
                            <i class="icon icon-date pull-right" style="margin-top: 8px"></i>
                        </div>
                        <HelpPopover content="不定期对贷款合同进行回购"/>
                    </div>
                </div>
                <RepurchaseTaskView
                    ref="taskView"
                    v-if="currentModel.repurchaseRule == 1"
                    :temporaryRepurchases="currentModel.temporaryRepurchases"
                    @submitTask="onSubmitTask"
                    @deleteTask="onDeleteTask">
                </RepurchaseTaskView>
            </el-form-item>
            <el-form-item label="回购算法" v-if="currentModel.repurchaseApproach != '0'">
                <div class="selectBox">
                    <div class="selectItem">
                        <span class="spanText">本金</span>
                        <RepurchaseDropdown
                            v-model="currentModel.repurchasePrincipalAlgorithm"
                            @deleteDropItem="deleteDropItem"
                            @editDropItem="editDropItem"
                            @createDropItem="createDropItem"
                            :keyword="'本金'"
                            :repurchaseList="repurchaseList">
                        </RepurchaseDropdown>
                    </div>
                    <div class="selectItem">
                        <span class="spanText">利息</span>
                        <RepurchaseDropdown
                            v-model="currentModel.repurchaseInterestAlgorithm"
                            @deleteDropItem="deleteDropItem"
                            @editDropItem="editDropItem"
                            @createDropItem="createDropItem"
                            :keyword="'利息'"
                            :repurchaseList="repurchaseList">
                        </RepurchaseDropdown>
                    </div>
                    <div class="selectItem">
                        <span class="spanText">罚息</span>
                        <RepurchaseDropdown
                            v-model="currentModel.repurchasePenaltyAlgorithm"
                            @deleteDropItem="deleteDropItem"
                            @editDropItem="editDropItem"
                            @createDropItem="createDropItem"
                            :keyword="'罚息'"
                            :repurchaseList="repurchaseList">
                        </RepurchaseDropdown>
                    </div>
                    <div class="selectItem">
                        <span class="spanText">其他费用</span>
                        <RepurchaseDropdown
                            v-model="currentModel.repurchaseOtherChargesAlgorithm"
                            @deleteDropItem="deleteDropItem"
                            @editDropItem="editDropItem"
                            @createDropItem="createDropItem"
                            :keyword="'其他费用'"
                            :repurchaseList="repurchaseList">
                        </RepurchaseDropdown>
                    </div>
                </div>
            </el-form-item>
            <el-form-item style="margin-top: 20px;">
                <template v-if="isUpdate">
                    <el-button type="primary" @click="next">提交</el-button>
                </template>
                <template v-else>
                    <el-button @click="next" type="primary">提交</el-button>
                    <a @click="prev" class="prev">上一步</a>
                </template>
            </el-form-item>
        </el-form>

        <EditOverdueModal
            :is-update="modal.isUpdate"
            :model="modal.model"
            v-model="modal.show"
            :optionalFeeType="optionalFeeType"
            :sysCreatePenaltyFlag="currentModel.sysCreatePenaltyFlag"
            @submit="onSubmitFee">
        </EditOverdueModal>

        <RepurchaseAlgorithmModal
            v-model="algorithmModal.show"
            @confirm="fetchRepurchaseList"
            :ifFromContract="false"
            :keyword="algorithmModal.keyword"
            :model="algorithmModal.model">
        </RepurchaseAlgorithmModal>

        <RepurchaseDateModal
            v-model="repurchaseDateModal.show"
            @submit="onSubmitDate"
            :repurchaseCycles="repurchaseCycles"
            :model="repurchaseDateModal.model">
        </RepurchaseDateModal>

        <SetOverdueDateModal
            v-model="setOverdueDateModal.show"
            @submit="onSubmitSetOverdueDate"
            :model="setOverdueDateModal.model">
        </SetOverdueDateModal>

    </div>
</template>

<script>
    import { mapState } from 'vuex';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import EditOverdueModal from './EditOverdueModal';
    import { REGEXPS } from 'src/validators';
    import HelpPopover from 'views/include/HelpPopover';
    import RepurchaseDropdown from './RepurchaseDropdown';
    import RepurchaseAlgorithmModal from './RepurchaseAlgorithmModal';
    import RepurchaseDateModal from './RepurchaseDateModal';
    import RepurchaseTaskView from './RepurchaseTaskView';
    import SetOverdueDateModal from './SetOverdueDateModal';
    import formats from 'filters/format';
    import SketchItem from 'components/SketchItem';


    var feeMap = {
        penalty: '0',
        overdueDefaultFee: '1',
        overdueServiceFee: '2',
        overdueOtherFee: '3',
    };
    var invertFeeMap = {
        0: 'penalty',
        1: 'overdueDefaultFee',
        2: 'overdueServiceFee',
        3: 'overdueOtherFee'
    };

    export default {
        components: {
            SetOverdueDateModal,
            EditOverdueModal, HelpPopover, RepurchaseDropdown, RepurchaseAlgorithmModal, RepurchaseDateModal, RepurchaseTaskView, SketchItem
        },
        props: {
            isUpdate: {
                default: false
            },
            model: {
                default: () => ({})
            },
            repayInfo: {
                default: () => ({})
            }
        },
        data: function() {
            var validateAdvaRepoTerm = (rule, value, callback) => {
                var advaRepaymentTerm = + this.currentModel.advaRepaymentTerm;
                var advaRepoTerm = + value;
                if (advaRepoTerm - advaRepaymentTerm > 1) {
                    callback()
                } else {
                    callback(new Error('必须大于还款宽限日至少2天'));
                }
            };

            var validateRepurchaseApproach = (rule, value, callback) => {
                if (!value || value === '-1') {
                    callback(new Error('请至少选择一个'))
                } else {
                    callback()
                }
            };

            var validateRepaymentCheckDays = (rule, value, callback) => {
                if (this.currentModel.effectiveTime.length > 0) {
                    if (value.length == 0) {
                        callback(new Error('请输入非负整数'));
                    } else {
                       if (REGEXPS.NON_NEGATIVE_INTEGER.test(value)) {
                            callback()
                        } else {
                            callback(new Error('请输入非负整数'));
                        }
                    }
                }
            };

            return{
                repurchaseList: [],
                showContent: '',

                modal: {
                    show: false,
                    isUpdate: false,
                    model: {},
                    _model: {}
                },

                algorithmModal: {
                    show: false,
                    keyword: '',
                    model: {},
                },

                rules: {
                    allowRepayment: { type: 'array', required: true, message: '请至少选择一个', trigger: 'change' },
                    assetPackageFormat: { required: true, message: ' '},
                    repurchaseApproach: { required: true, trigger: 'change', validator: validateRepurchaseApproach},
                    advaRepaymentTerm: [
                        { required: true, message: ' ', transform: value => value + '' },
                        { pattern: REGEXPS.NON_NEGATIVE_INTEGER, message: '请输入非负整数' }
                    ],
                    advaRepoTerm: [
                        { required: true, message: ' ', transform: value => value + '' },
                        { pattern: REGEXPS.NON_NEGATIVE_INTEGER, message: '请输入非负整数' },
                        { validator: validateAdvaRepoTerm }
                    ],
                    advaMatuterm: { pattern: REGEXPS.NON_NEGATIVE_INTEGER, message: '请输入非负整数' },
                    allowNotOverdueAutoConfirm: { type: 'number', required: true, message: ' ', trigger: 'change' },
                    allowOverdueAutoConfirm: { type: 'number', required: true, message: ' ', trigger: 'change' },
                    repaymentCheckDays: { validator: validateRepaymentCheckDays}
                },

                repurchaseDateModal: {
                    show: false,
                    model: {},
                },

                setOverdueDateModal: {
                    show: false,
                    model: {},
                },

                currentModel: {
                    allowRepayment: [], // 存allowOnlineRepayment，allowOfflineRepayment的值
                    effectiveTime: [], //设定还款时间生效

                    assetPackageFormat: '',
                    allowOnlineRepayment: false,
                    allowOfflineRepayment: false,
                    sysNormalDeductFlag: true,
                    sysOverdueDeductFlag: true,
                    allowAdvanceDeductFlag: true,
                    advaRepoTerm: '',
                    advaRepaymentTerm: '',
                    advaMatuterm: '',
                    sysCreatePenaltyFlag: true,
                    penalty: '',
                    overdueDefaultFee: '',
                    overdueServiceFee: '',
                    overdueOtherFee: '',
                    repurchaseCycle: '',
                    daysOfMonth: [],
                    temporaryRepurchases: [],
                    repurchaseRule: '',
                    repurchaseApproach: '-1',

                    repurchasePrincipalAlgorithm: '',
                    repurchaseInterestAlgorithm: '',
                    repurchasePenaltyAlgorithm: '',
                    repurchaseOtherChargesAlgorithm: '',

                    modifyFlag: '',
                    allowNotOverdueAutoConfirm: 0,
                    allowOverdueAutoConfirm: 0,
                    allowOverdueAutoConfirmIsOpen: 0,

                    planRepaymentTimeOnline: 0,
                    planRepaymentTimeOffline: 0,
                    repaymentCheckDays: '' //设定还款时间生效规则
                }
            }
        },
        watch: {
            model: function(cur) {
                this.currentModel.effectiveTime = [];
                this.currentModel.repaymentCheckDays = '';
                cur.allowOnlineRepayment && this.currentModel.allowRepayment.push('allowOnlineRepayment');
                cur.allowOfflineRepayment && this.currentModel.allowRepayment.push('allowOfflineRepayment');
                cur.planRepaymentTimeOnline && this.currentModel.effectiveTime.push('effectiveOnline');
                cur.planRepaymentTimeOffline && this.currentModel.effectiveTime.push('effectiveOffline');

                setTimeout(() => {
                    this.currentModel = Object.assign({}, this.currentModel, cur);

                    this.currentModel.allowOverdueAutoConfirmIsOpen = this.currentModel.allowOverdueAutoConfirm == '' ? 0 : 1

                    this.currentModel.temporaryRepurchases.forEach((item, index) => {
                        item.effectEndDate = formats.formatDate(item.effectEndDate);
                        item.effectStartDate = formats.formatDate(item.effectStartDate);
                        item.repurchaseDate = formats.formatDate(item.repurchaseDate);
                    });
                    if (this.currentModel.repaymentCheckDays == -1) {
                        this.currentModel.repaymentCheckDays = '';
                    }
                }, 0);
            },
            repayInfo: function(cur) {
                this.currentModel.effectiveTime = [];
                this.currentModel.repaymentCheckDays = '';
                this.currentModel = Object.assign(this.currentModel, cur)
            },
            'currentModel.allowOverdueAutoConfirmIsOpen': function (current) {
                if (this.currentModel.allowOverdueAutoConfirm == '') {
                    this.currentModel.allowOverdueAutoConfirm = 1
                }
            },
            'currentModel.effectiveTime': function(current) {
                var {currentModel} = this;
                currentModel.planRepaymentTimeOnline = 0;
                currentModel.planRepaymentTimeOffline = 0;
                current.forEach(item => {
                    if (item == 'effectiveOnline') {
                        currentModel.planRepaymentTimeOnline = 1;
                    } else if (item == 'effectiveOffline') {
                        currentModel.planRepaymentTimeOffline = 1;
                    }
                })
            }
        },
        computed: {
            overdueFeeTypes: function() {
                var arr = [];
                var currentModel = this.currentModel;

                if (currentModel.penalty) {
                    arr.push({feeTypeKey: feeMap.penalty, details: currentModel.penalty});
                }
                if (currentModel.overdueDefaultFee) {
                    arr.push({feeTypeKey: feeMap.overdueDefaultFee, details: currentModel.overdueDefaultFee});
                }
                if (currentModel.overdueServiceFee) {
                    arr.push({feeTypeKey: feeMap.overdueServiceFee, details: currentModel.overdueServiceFee});
                }
                if (currentModel.overdueOtherFee) {
                    arr.push({feeTypeKey: feeMap.overdueOtherFee, details: currentModel.overdueOtherFee});
                }

                return arr;
            },
            optionalFeeType: function() {
                var { feeType, overdueFeeTypes, modal } = this;

                var notIn = feeTypeKey => overdueFeeTypes.findIndex(item => item.feeTypeKey == feeTypeKey) === -1;
                var res = feeType.filter(fee => {
                    if (notIn(fee.key)) {
                        return true;
                    } else {
                        return fee.key == modal._model.feeTypeKey;
                    }
                });

                return res;
            },
            ...mapState({
                assetPackageFormat: state => state.financialContract.assetPackageFormat,
                feeType: state => state.financialContract.feeType,
                repurchaseCycles: state => state.financialContract.repurchaseCycles,
            }),
            repurchaseDateContent: function() {
                var result = '';
                var { daysOfCycle, repurchaseCycle } = this.currentModel;

                this.repurchaseCycles && this.repurchaseCycles.forEach(item => {
                    if (item.key == repurchaseCycle) {
                        result = item.value;
                    }
                });

                if (daysOfCycle && daysOfCycle.length) {
                    var daysOfCycleList = formats.sortArrayList(daysOfCycle);
                    result += daysOfCycleList.join();
                }

                return result;
            }
        },
        beforeMount: function() {
            var { $store } = this;
            $store.dispatch('getAssetPackageFormat');
            $store.dispatch('getFeeType');
            $store.dispatch('getRepurchaseCycles');
            this.fetchRepurchaseList();
        },
        methods: {
            next: function(e) {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        var data = JSON.parse(JSON.stringify(this.currentModel));
                        data.allowOnlineRepayment = data.allowRepayment.indexOf('allowOnlineRepayment') !== -1;
                        data.allowOfflineRepayment = data.allowRepayment.indexOf('allowOfflineRepayment') !== -1;
                        delete data.allowRepayment;
                        if (data.allowOverdueAutoConfirmIsOpen == 0) {
                            delete data.allowOverdueAutoConfirmIsOpen
                            data.allowOverdueAutoConfirm = ''
                        }
                        this.$emit('next', data);
                    } else {
                        $('.el-form-item__error').first().parent()[0].scrollIntoView();
                    }
                });
            },
            prev: function() {
                this.$emit('prev');
            },
            onAddFee: function() {
                var { modal, optionalFeeType} = this;
                if (optionalFeeType.length) {
                    modal.show = true;
                    modal.isUpdate = false;
                    modal._model={};
                    modal.model = {
                        feeTypeKey: optionalFeeType[0].key
                    };
                }
            },
            onEditFee: function(fee) {
                var { modal } = this;
                modal.show = true;
                modal.isUpdate = true;
                modal._model = fee;
                modal.model = Object.assign({}, fee);
            },
            onDeleteFee: function(fee) {
                var field = invertFeeMap[fee.feeTypeKey];
                this.currentModel[field] = null;
            },
            onSubmitFee: function(fee) {
                var { modal, currentModel } = this;

                if (modal.isUpdate) {
                    var _field = invertFeeMap[modal._model.feeTypeKey];
                    currentModel[_field] = null;
                }

                var field = invertFeeMap[fee.feeTypeKey];

                if (currentModel.sysCreatePenaltyFlag) {
                    currentModel[field] = fee.details;
                } else {
                    currentModel[field] = -1;
                }

                modal.show = false;
            },
            feeTypeKeyToZh: function(feeTypeKey) {
                var index = this.feeType.findIndex(function(item) {
                    return item.key == feeTypeKey;
                });
                return index !== -1 ? this.feeType[index].value : feeTypeKey;
            },
            clearOverdueFeeTypes: function (value) {
                var { currentModel, overdueFeeTypes } = this;
                var goahead = function() {
                    setTimeout(() => {
                        currentModel.sysCreatePenaltyFlag = value;
                    }, 0);
                };

                if (overdueFeeTypes.length < 1) {
                    goahead();
                    return;
                }

                MessageBox.open('逾期费用生成方式变更将影响逾期费用类型配置，是否继续？', '变更逾期费用生成方式', [{
                    text: '关闭',
                    handler: () => {
                        MessageBox.close();
                    }
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        currentModel.penalty = null;
                        currentModel.overdueDefaultFee = null;
                        currentModel.overdueServiceFee = null;
                        currentModel.overdueOtherFee = null;

                        goahead();
                        MessageBox.close();
                    }
                }]);
            },
            fetchRepurchaseList: function() {
                ajaxPromise({
                    url: `/inputHistory`,
                    data: {
                        whatFor: '1',
                    },
                }).then(data => {
                    this.repurchaseList = data.list;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            deleteDropItem: function(model) {
                ajaxPromise({
                    url: `/inputHistory/del`,
                    data: {
                        uuid: model.uuid
                    },
                    type: 'post',
                }).then(data => {
                    this.fetchRepurchaseList();
                });
            },
            editDropItem: function(model, keyword) {
                this.algorithmModal.show = true;
                this.algorithmModal.keyword = keyword;
                this.algorithmModal.model = model;
            },
            createDropItem: function(model, keyword) {
                this.algorithmModal.show = true;
                this.algorithmModal.keyword = keyword;
                this.algorithmModal.model = model;
            },
            showRepurchaseDateModal: function() {
                var modal = this.repurchaseDateModal;
                modal.show = true;
                modal.model = {
                    repurchaseCycle: this.currentModel.repurchaseCycle,
                    daysOfCycle: this.currentModel.daysOfCycle
                }
            },
            onSubmitDate: function(model) {
                this.currentModel.repurchaseCycle = model.repurchaseCycle;
                this.currentModel.daysOfCycle = model.daysOfCycle;
                this.repurchaseDateModal.show = false;
            },
            onDeleteTask: function(index) {
                this.currentModel.temporaryRepurchases.splice(index, 1);
                this.$refs.taskView.closeModal();
            },
            onSubmitTask: function(task, isUpdate, index) {
                var { currentModel } = this;

                if (isUpdate && index != -1) {
                    currentModel.temporaryRepurchases[index] = task;
                } else {
                    currentModel.temporaryRepurchases.push(task);
                }
                this.$refs.taskView.closeModal();
            },
            onSetOverdueDate: function () {
                this.setOverdueDateModal.show = true
                this.setOverdueDateModal.model = {
                    allowOverdueAutoConfirm: this.currentModel.allowOverdueAutoConfirm
                }
            },
            onSubmitSetOverdueDate: function (newModel) {
                this.currentModel.allowOverdueAutoConfirm = newModel.allowOverdueAutoConfirm
                this.setOverdueDateModal.show = false
            }
        }
    }
</script>
