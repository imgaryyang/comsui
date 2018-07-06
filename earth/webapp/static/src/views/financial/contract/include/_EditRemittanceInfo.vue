<style lang="sass">
    #editRemittanceInfo {
        .subform {
            padding: 20px;
            margin-right: 30%;

            .hd {
                line-height: 1;
                margin-bottom: 15px;
                font-size: 12px;
                color: #999;

                &:before {
                    content: '*';
                    color: #e8415f;
                    margin-right: 4px;
                }
            }
        }
    }
</style>

<template>
    <div id="editRemittanceInfo">
        <el-form
            :model="currentModel" 
            :rules="rules" 
            ref="form"
            label-width="120px" 
            class="sdf-form">
            <el-form-item label="放款信息" class="form-item-legend"></el-form-item>
            <el-form-item label="单笔放款上限" prop="transactionLimitPerTranscation">
                <el-input class="long" v-model="currentModel.transactionLimitPerTranscation"></el-input>
                <span class="muted">万元</span>
                <HelpPopover content="此处为空代表单笔放款无限额"/>
            </el-form-item>
           <!--  <el-form-item label="单日放款上限" prop="transactionLimitPerDay">
                <el-input class="long" v-model="currentModel.transactionLimitPerDay"></el-input>
                <span class="muted">万元</span>
                <HelpPopover content="此处为空代表单日放款无限额"/>
            </el-form-item> -->
            <el-form-item label="放款总额">
                <span v-if="currentModel.remittancetotalAmount == undefined">--</span>
                <span v-else>{{ currentModel.remittancetotalAmount | formatMoney}}</span>
                <template v-if="isEditAmount">
                    <el-input class="middle" placeholder="追加金额" style="margin-left:10px" v-model="increaseAmount"></el-input>
                    <el-button icon="check" size="small" style="margin-left:10px" @click="handleAddAmount"></el-button>
                </template>
                <el-button icon="plus" size="small" style="margin-left:10px" @click="isEditAmount = true" v-else></el-button>
                <HelpPopover content="该信托专户下,历史允许放款的金额总和" style="margin-left: 20px" />
            </el-form-item>
            <el-form-item label="头寸余额">
                <span v-if="currentModel.bankSavingLoan == undefined">--</span>
                <span v-else>{{ currentModel.bankSavingLoan | formatMoney}}</span>
            </el-form-item>
            <el-form-item label="放款模式" required>
                <div>
                    <el-radio-group v-model="currentModel.remittanceStrategyMode">
                        <el-radio :label="0">单向放款</el-radio>
                        <el-radio :label="1">双向放款</el-radio>
                        <el-radio :label="2">放扣联动</el-radio>
                        <el-radio :label="3">线下放款</el-radio>
                        <el-radio :label="4">无</el-radio>
                    </el-radio-group>
                    &nbsp;&nbsp;
                    <HelpPopover>
                        1、单向放款适用于一次只向一个账户放款；<br/>
                        2、双向放款适用于砍头息客户，需提供砍头息入账账户信息；<br/>
                        3、放扣联动适用于受托代扣类型客户，需提供受托代扣方入账账户信息；<br/>
                        4、线下放款系统无需进行放款操作；<br/>
                        5、无，适用于资产证券化信托产品，系统无需放款。
                    </HelpPopover>
                </div>
                <div class="subform" 
                    v-if="currentModel.remittanceStrategyMode == 2 
                        || currentModel.remittanceStrategyMode == 1">
                    <div class="hd">{{currentModel.remittanceStrategyMode == 2 ? '受托代扣入款账户' : '砍头息入款账户' }}</div>
                    <el-form-item prop="appAccounts">
                        <SketchItem v-for="item in currentModel.appAccounts">
                            <img v-if="item.bankCode" :src="item.bankCode" class="stamp">
                            <div class="text">
                              <p>{{item.accountNo}}</p>
                              <p>{{item.accountName}} - {{ item.bankName }}</p>
                            </div>
                            <span class="operate">
                              <a class="edit" @click="onEditTrustAccount(item)"></a>
                              <a class="delete" @click="onDeleteTrustAccount(item)"></a>
                            </span>
                        </SketchItem>
                        <div>
                            <el-button class="button-multimedia" @click="onAddTrustAccount">添加账户</el-button>
                            <span class="muted font-size-12 color-dim">请添加至少 1 个账户</span> 
                        </div>
                    </el-form-item>
                </div>
            </el-form-item>
            <el-form-item label="二次放款" prop="allowModifyRemittanceApplication" required>
                <el-radio-group v-model="currentModel.allowModifyRemittanceApplication">
                    <el-radio :label="1">开启</el-radio>
                    <el-radio :label="0">关闭</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="放款对象" prop="remittanceObject" required>
                <el-radio-group v-model="currentModel.remittanceObject">
                    <el-radio :label="0">贷款人</el-radio>
                    <el-radio :label="1">供应商</el-radio>
                    <el-radio :label="2">其他</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item style="margin-top: 20px;">
                <template v-if="isUpdate">
                    <el-button 
                        type="primary" 
                        :disabled="isEditAmount"
                        @click="next">
                        提交
                    </el-button>
                </template>
                <template v-else>
                    <el-button 
                        type="primary" 
                        :disabled="isEditAmount"
                        @click="next">
                        下一步
                    </el-button>
                    <a @click="prev" class="prev">
                        上一步
                    </a>
                </template>
            </el-form-item>
        </el-form>

        <EditAccountModal 
            :is-update="modal.isUpdate"
            :model="modal.model"
            v-model="modal.show"
            @submit="onSubmitTrustAccount">
        </EditAccountModal>
    </div>
</template>

<script>
    import SketchItem from 'components/SketchItem';
    import { mapState } from 'vuex';
    import EditAccountModal from './EditAccountModal';
    import { REGEXPS } from 'src/validators';
    import HelpPopover from 'views/include/HelpPopover';
    import Decimal from 'decimal.js';
    import MessageBox from 'components/MessageBox';
    import format from 'filters/format';

    export default {
        components: {
            SketchItem, EditAccountModal, HelpPopover
        },
        props: {
            isUpdate: {
                default: false
            },
            model: {
                default: () => ({})
            },
            remittanceInfo: {
                default: () => ({})
            },
        },
        data: function() {
            var validateAppAccounts = (rule, value, callback) => {
                const { remittanceStrategyMode } = this.currentModel;
                var error;

                if (remittanceStrategyMode == 1 || remittanceStrategyMode == 2) {
                    error = value.length == 0 ? new Error(' ') : void 0;
                }

                callback(error);
            };

            var validateTransaction = (rule, value, callback) => {
                var currentModel = this.currentModel;
                var isEmpty = function(value) {
                    return value == '' || value == null;
                };

                // if (isEmpty(currentModel.transactionLimitPerDay)) {
                //     callback();
                //     return;
                // }

                // if (!isEmpty(currentModel.transactionLimitPerDay) && isEmpty(currentModel.transactionLimitPerTranscation)) {
                //     callback(new Error('单日放款上限存在时必须输入单笔放款上限'));
                //     return;
                // }

                var transactionLimitPerDay = + currentModel.transactionLimitPerDay;
                var transactionLimitPerTranscation = + currentModel.transactionLimitPerTranscation;

                if (transactionLimitPerTranscation > transactionLimitPerDay) {
                    callback(new Error('单日放款上限必须大于单笔放款上限'));
                } else {
                    callback();
                }
            };

            var validateRemittanceObject = (rule, value, callback) => {
                var error;

                error = value === '' || value == -1? new Error(' ') : void 0;

                callback(error);
            };

            return { 
                modal: {
                    show: false,
                    isUpdate: false,
                    model: {}
                },

                currentModel: {
                    transactionLimitPerTranscation:  '',
                    transactionLimitPerDay: '',
                    remittanceStrategyMode: 0,
                    appAccounts:  [],
                    allowModifyRemittanceApplication: 0,
                    remittanceObject: -1
                },

                rules: {
                    transactionLimitPerTranscation:  [
                        { pattern: REGEXPS.MONEY, message: '请输入合法的金额' },
                    ],
                    allowModifyRemittanceApplication: { type: 'number', required: true, message: ' ', trigger: 'change' },
                    remittanceObject: {type: 'number', required: true, message: '放款对象不能为空', trigger: 'change', validator: validateRemittanceObject },
                    appAccounts: { type: 'array', validator: validateAppAccounts, trigger: 'change' }
                },

                isEditAmount: false,
                increaseAmount: '',
                totalIncreaseAmount: 0
            };
        },
        // computed: {
        //     remittancetotalAmountFormat: function() {
        //         console.log(this.currentModel.remittancetotalAmount);
        //         return this.currentModel.remittancetotalAmount == undefined ? '--' : format.formatMoney(this.currentModel.remittancetotalAmount);
        //     },
        //     bankSavingLoanFormat: function() {
        //         return this.currentModel.bankSavingLoan == undefined ? '--' : format.formatMoney(this.currentModel.bankSavingLoan);
        //     }
        // },
        watch: {
            model: function(cur) {
                if (typeof cur.appAccounts === 'string') {
                    cur.appAccounts = JSON.parse(cur.appAccounts || '[]');
                }
                setTimeout(() => {
                    this.currentModel = Object.assign({
                        transactionLimitPerTranscation:  '',
                        transactionLimitPerDay: '',
                        remittanceStrategyMode: 0,
                        appAccounts:  [],
                        allowModifyRemittanceApplication: 0,
                        remittanceObject: -1
                    }, cur);
                }, 0);
                this.totalIncreaseAmount = 0;
                this.fetchAmountError = cur.remittancetotalAmount == undefined || cur.bankSavingLoan == undefined;

            },
            remittanceInfo : function(cur) {
               this.currentModel = Object.assign(this.currentModel, cur)
            }
        },
        methods: {
            next: function(e) {
                var self = this;
                this.$refs.form.validate(valid => {
                    if (valid) {
                        debugger
                        var data = JSON.parse(JSON.stringify(this.currentModel));
                        if (data.remittanceStrategyMode == 3 || data.remittanceStrategyMode == 0) {
                            data.appAccounts = '';
                        } else {
                            data.appAccounts = JSON.stringify(data.appAccounts);
                        }

                        data.bankSavingLoanAddAmount = self.totalIncreaseAmount;
                        this.$emit('next', data);
                    }
                });
            },
            prev: function() {
                this.$emit('prev');
            },
            findAccountIndex: function(accountNo) {
                var appAccounts = this.currentModel.appAccounts;
                return appAccounts.findIndex(function(item, index) {
                    return item.accountNo === accountNo;
                });
            },
            onAddTrustAccount: function() {
                var { modal } = this;
                modal.show = true;
                modal.isUpdate = false;
                modal.model = {bankName: ''};
            },
            onEditTrustAccount: function(account) {
                var { modal } = this;
                modal.show = true;
                modal.isUpdate = true;
                modal.model = Object.assign({}, account);
                modal._model = account;
            },
            onDeleteTrustAccount: function(account) {
                var { currentModel } = this;
                var appAccounts = currentModel.appAccounts;
                var index = this.findAccountIndex(account.accountNo);
                appAccounts.splice(index, 1);
            },
            onSubmitTrustAccount: function(data) {
                var { currentModel, modal } = this;
                var appAccounts = currentModel.appAccounts;

                if (modal.isUpdate) {
                    Object.assign(modal._model, data);
                } else {
                    appAccounts.push(data);
                }

                modal.show = false;
            },
            handleAddAmount: function() {
                var {currentModel, increaseAmount} = this;
                if (increaseAmount == '') {
                    this.isEditAmount = false;
                    return;
                }
                if (!/^[0-9]+[\.]?[0-9]{0,2}$/.test(increaseAmount)) {
                    MessageBox.open('请输入合法的金额');
                    return;
                }
                var amount = Decimal(0).add(increaseAmount).toNumber();
                this.currentModel.remittancetotalAmount = currentModel.remittancetotalAmount == undefined ? amount : Decimal(0).add(currentModel.remittancetotalAmount).add(amount).toNumber();
                this.currentModel.bankSavingLoan = currentModel.bankSavingLoan == undefined ? amount : Decimal(0).add(currentModel.bankSavingLoan).add(amount).toNumber();

                this.totalIncreaseAmount += amount;
                this.isEditAmount = false;
                this.increaseAmount = '';
            }
        }
    }
</script>