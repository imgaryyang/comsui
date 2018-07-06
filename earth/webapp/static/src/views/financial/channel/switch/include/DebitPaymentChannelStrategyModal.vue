<style lang="sass">
    .channelStrategyModal{
        .step-content-item {
            .panels {
                .panel {
                    margin: 15px;
                    float: left;
                    width: 140px;
                    padding: 10px;
                    border: 1px solid #ddd;

                    .paymentChannelName {
                        height: 50px;
                        color: #38e;
                        overflow: hidden;
                        margin-bottom: 3px;
                    }

                    .transactionLimitPer {
                        height: 42px;
                    }

                    &.z-active {
                        background: #ddd;
                    }

                    &.color-trap.z-active {
                        background: #fff;
                    }

                    &:hover {
                        border: 1px solid #38e;
                    }

                }
            }
        }
        .el-table__body-wrapper {
            overflow-x: hidden;
        }
        .show-bank-list .el-radio__label {
            display: none;
        }
    }
</style>
<template>
    <Modal v-model="show" class="channelStrategyModal">
        <ModalHeader title="设置策略"></ModalHeader>
        <ModalBody>
            <div>
                <StepTip v-model="stepIndex" style="display: flex">
                    <StepTipItem :index="1">选择通道策略</StepTipItem>
                    <StepTipItem
                        v-if="['0','1','2','3'].includes(paymentStrategyMode)"
                        :index="2">
                        配置通道
                    </StepTipItem>
                    <StepTipItem
                        v-if="['1'].includes(paymentStrategyMode)"
                        :index="3">
                        配置发卡行首选通道
                    </StepTipItem>
                    <StepTipItem
                        v-if="['1'].includes(paymentStrategyMode)"
                        :index="4">
                        优先级预览
                    </StepTipItem>
                </StepTip>
                <StepContent v-model="stepIndex">
                    <StepContentItem :index="1">
                        <div style="text-align:left">
                            <div style="padding-bottom: 15px;">请选择通道策略</div>
                            <el-select
                                size="middle"
                                style="max-width: 200px"
                                v-model="paymentStrategyMode"
                                placeholder="通道策略">
                                <el-option value="-1" label="无通道模式">
                                </el-option>
                                <el-option value="0" label="单一通道模式">
                                </el-option>
                                <el-option value="1" label="发卡行优先模式" >
                                </el-option>
                                <el-option value="2" label="限额优先-签约模式" >
                                </el-option>
                                <el-option value="3" label="限额优先-非签约式模式" >
                                </el-option>
                            </el-select>
                        </div>
                    </StepContentItem>
                    <StepContentItem :index="2">
                        <div v-if="paymentChannelList.length != 0 " style="text-align:left;float:left">
                            <i>请配置默认通道：</i>
                            <span class="color-dim">
                                拖动方块，以从左到右、从上到下为序排优先级，左上优先级最高
                            </span>
                            <draggable :list="paymentChannelList" class="panels">
                                <div
                                    class="panel"
                                    v-for="(item, index) in paymentChannelList"
                                    :class="{'z-active': item.isActive, 'color-trap': isColorTrap}"
                                    @click="clickPanel(index, paymentChannelList)"
                                    :key="item.paymentChannelUuid">
                                    <div class="paymentChannelName">{{ item.paymentChannelName }}</div>
                                    <div class="transactionLimitPer">
                                        <div>单笔限额 ： {{item.transactionLimitPer}}万/笔</div>
                                    </div>
                                    <div>
                                        <span class="color-dim">{{ '('+item.fee+')' }}</span>
                                        <span class="pull-right">{{ item.channelStatusMsg }}</span>
                                    </div>
                                </div>
                            </draggable>
                        </div>
                        <div v-else>
                            <div style="width:100%; height: 100px; text-align: center; padding-top: 40px;">没有通道</div>
                        </div>
                    </StepContentItem>
                    <StepContentItem :index="3">
                        <div style="text-align:left">
                            <div style="padding: 0 15px;">请配置发卡行首选通道</div>
                            <el-table
                                class="show-bank-list"
                                :data="showBankList"
                                stripe
                                border>
                                <el-table-column prop="bankName" label="">
                                </el-table-column>
                                <template v-for="(item, index) in paymentChannelList">
                                    <el-table-column 
                                        :label="item.paymentChannelName" 
                                        :prop="item.paymentChannelUuid"
                                        inline-template 
                                        :context="_self">
                                        <el-radio
                                            :label="column.property"
                                            :value="currentModel.paymentChannelOrderForBanks[row.bankCode]"
                                            @input="$set(currentModel.paymentChannelOrderForBanks, row.bankCode, column.property)">
                                        </el-radio>
                                    </el-table-column>
                                </template>
                            </el-table>
                            <div class="pull-right">
                                <PageControl
                                    v-model="step3Pager.pageIndex"
                                    :size="bankList.length"
                                    :per-page-record-number="step3Pager.pagerNumber">
                                </PageControl>
                            </div>
                        </div>
                    </StepContentItem>
                    <StepContentItem :index="4">
                        <div style="text-align:left">
                            <div style="padding: 0 15px;">发卡行通道优先级预览</div>
                            <PagingTable :data="previewList" :pagination="true" :perPageRecordNumber="8">
                                <el-table-column prop="bankName" label="银行名称">
                                </el-table-column>
                                <template v-for="n in paymentChannelList.length">
                                    <el-table-column :label="`优先级第${n}位`" inline-template :context="_self">
                                        <el-popover
                                            @show="bankChannelLimitPreview(row.paymentchannelOrder[n-1].paymentChannelUuid,row.bankCode)"
                                            trigger="hover"
                                            placement="top">
                                            <div>
                                                <div v-if="row.error">
                                                    {{ row.error }}
                                                </div>
                                                <template v-if="row.bankChannelLimitData">
                                                    <div>{{ row.bankChannelLimitData.paymentChannelName }}</div><br/>
                                                    <div>{{ row.bankChannelLimitData.bankName }}</div><br/>
                                                    <div><span class="text-muted">单笔限额:</span> {{ row.bankChannelLimitData.transactionLimitPerTranscation}}</div><br/>
                                                </template>
                                            </div>
                                            <span slot="reference">{{ row.paymentchannelOrder[n-1].paymentChannelName }}</span>
                                        </el-popover>
                                    </el-table-column>
                                </template>
                            </PagingTable>
                        </div>
                    </StepContentItem>
                </StepContent>
                <StepFooter
                    v-model="stepIndex"
                    :step-number="stepNumber"
                    :disabled="disabledNextBtn"
                    @input="next"
                    @complete="complete"
                    @cancel="show = false">
                </StepFooter>
            </div>
        </ModalBody>
    </Modal>
</template>

<script>
    import modalMixin from './modal-mixin';
    import { StepContent, StepContentItem, StepTip, StepTipItem, StepFooter } from 'components/StepOperation';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import PagingTable from 'views/include/PagingTable';
    import draggable from 'vuedraggable';

    export default {
        mixins: [modalMixin],
        components: {
            StepContent, StepContentItem, StepTip, StepTipItem, StepFooter, draggable, PagingTable
        },
        props: {
            model: {
                default: null
            }
        },
        watch: {
            show: function() {
                this.paymentStrategyMode = this.LIMIT_SIGN_STRATEGY;
            },
            model: function(cur) {
                this.currentModel = Object.assign({
                    financialContractUuid: '',
                    accountSide: '',
                    businessType: '',
                    paymentStrategyMode: this.paymentStrategyMode,
                    paymentChannelUuids: [],
                    paymentChannelOrderForBanks: {}
                }, cur);

                this.stepIndex = 1;
                this.paymentChannelList = [];
                this.bankList = [];
                this.previewList = [];
            },
            paymentStrategyMode: function(mode) {
                this.isColorTrap = false;

                switch(mode) {
                    case this.SINGLE_STRATEGY:
                        this.stepNumber = 2;
                        break;
                    case this.ISSUER_STRATEGY:
                        this.stepNumber = 4;
                        this.isColorTrap = true;
                        break;
                    case this.LIMIT_SIGN_STRATEGY:
                        this.stepNumber = 2;
                        break;
                    case this.LIMIT_NOT_SIGN_STRATEGY:
                        this.stepNumber = 2;
                        this.isColorTrap = true;
                        break;
                    default:
                        this.stepNumber = 1;
                        break;
                }
                this.currentModel.paymentStrategyMode = parseInt(mode);
            },
        },
        data: function() {
            return {
                NO_CHANNEL_STRATEGY: '-1',
                SINGLE_STRATEGY: '0',
                ISSUER_STRATEGY: '1',
                LIMIT_SIGN_STRATEGY: '2',
                LIMIT_NOT_SIGN_STRATEGY: '3',

                stepIndex: 1,
                stepNumber: 2,
                show: this.value,
                paymentStrategyMode: '',
                currentModel: Object.assign({paymentStrategyMode:''}, this.model),
                paymentChannelList: [],
                bankList: [],
                previewList: [],

                isColorTrap: false,

                step3Pager: {
                    pageIndex: 1,
                    pagerNumber: 8
                }
            }
        },
        computed: {
            showBankList: function() {
                var start = (this.step3Pager.pageIndex - 1) * this.step3Pager.pagerNumber
                var end = start + this.step3Pager.pagerNumber
                return this.bankList.slice(start, end)
            },
            disabledNextBtn: function() {
                return (this.stepIndex == 2 && this.paymentChannelList.length == 0) || (this.stepIndex == 3 && this.bankList.length == 0);
            }
        },
        methods: {
            complete: function() {
                var mode = this.currentModel.paymentStrategyMode;
                if(mode == this.NO_CHANNEL_STRATEGY) {
                    this.currentModel.paymentChannelUuids = [];
                    this.currentModel.paymentChannelOrderForBanks = {};
                }else if(mode == this.SINGLE_STRATEGY) {
                    this.currentModel.paymentChannelOrderForBanks = {};
                    this.savePaymentChannelUuid();
                }else {
                    this.savePaymentChannelUuids();
                }
                this.save();
            },
            next: function(step, prevStep) {
                if(step == 2){
                    this.getPaymentChannelList()
                }else if(step == 3) {
                    this.leaveStep2();
                    this.getAllBank().then(_ => {
                        if (prevStep == 2) {
                            if (this.paymentChannelList.length > 0) {
                                var firstChannel = this.paymentChannelList[0]
                                this.bankList.forEach(bank => {
                                    this.$set(this.currentModel.paymentChannelOrderForBanks, bank.bankCode, firstChannel.paymentChannelUuid)
                                })
                            }
                        }
                    })
                }else {
                    this.leaveStep3();
                    this.getPreviewList();
                }
            },
            getPaymentChannelList: function() {
                ajaxPromise({
                    url: `/paymentchannel/switch/strategy/step/2`,
                    data: this.model
                }).then(data => {
                    this.paymentChannelList = data.list;
                    this.paymentChannelList.forEach((item, index) => {
                        this.$set(item, 'isActive', index == 0);
                    });
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            getAllBank: function() {
                if (this.bankList.length != 0) {
                    return Promise.resolve()
                }

                return ajaxPromise({
                    url: `/paymentchannel/switch/strategy/step/3`,
                    data: this.model
                }).then(data =>{
                    this.bankList = data.list;
                    
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            getPreviewList: function() {
                var { paymentChannelList, bankList } = this;
                var paymentChannelOrderForBanks = this.currentModel.paymentChannelOrderForBanks;

                this.previewList = bankList.map(bank =>{
                    var a1 = [];
                    var a2 = [];

                    paymentChannelList.forEach(paymentChannel => {
                        if (paymentChannel.paymentChannelUuid === paymentChannelOrderForBanks[bank.bankCode]) {
                            a1.push(paymentChannel);
                        } else {
                            a2.push(paymentChannel);
                        }
                    });

                    bank.paymentchannelOrder = a1.concat(a2);
                    return bank;
                });
            },
            leaveStep2: function() {
                this.savePaymentChannelUuids();
                this.updatePaymentChannelOrder();
            },
            leaveStep3: function() {
                // var res = {};
                // var checkedRedios = $('input:checked');
                // this.bankList.forEach((item, index) => {
                //     var bankCode = item.bankCode;
                //     var checkedRedio = checkedRedios[index];
                //     res[bankCode] = this.paymentChannelList[checkedRedio.getAttribute('data-index')].paymentChannelUuid;
                // });
                // this.currentModel.paymentChannelOrderForBanks = res;
            },
            updatePaymentChannelOrder: function() {
                var uuids = this.currentModel.paymentChannelUuids;
                var source = this.paymentChannelList;
                var dest = [];

                uuids.forEach(uuid => {
                    for (var i = 0; i < source.length; i++) {
                        var item = source[i];
                        if (item.paymentChannelUuid == uuid) {
                            source.splice(i, 1);
                            dest.push(item);
                            break;
                        }
                    }
                });
                this.paymentChannelList = dest.concat(source);
            },
            clickPanel: function(index, array) {
                if (this.currentModel.paymentStrategyMode == this.SINGLE_STRATEGY){
                    array.forEach((item, n) => {
                        if (n == index) {
                            item.isActive = !item.isActive;
                        }else {
                            item.isActive = false;
                        }
                    })
                    return
                }
            },
            bankChannelLimitPreview: function(paymentChannelUuid,bankCode) {
                var list = this.previewList;
                var index = list.findIndex(item => item.bankCode === bankCode);
                if (index === -1) return;

                ajaxPromise({
                    url: `/paymentchannel/switch/strategy/bankChannelLimitPreview`,
                    data: {
                        paymentChannelUuid: paymentChannelUuid,
                        bankCode: bankCode,
                        accountSide: this.currentModel.accountSide
                    }
                }).then(data => {
                    this.$set(list[index], 'bankChannelLimitData', data);
                }).catch(message => {
                    this.$set(list[index], 'error', message);
                });
            },
            save: function() {
                ajaxPromise({
                    url: `/paymentchannel/switch/strategy/saveResult`,
                    data: JSON.stringify(this.currentModel),
                    type: 'post',
                    contentType: 'application/json',
                }).then(data => {
                    this.show = false;
                    MessageBox.open('提交成功');
                    MessageBox.once('closed', () => {
                        this.$emit('confirm');
                    });
                }).catch(message => {
                    MessageBox.open()
                });
            },
            savePaymentChannelUuid: function() {
                this.paymentChannelList.forEach(item => {
                    if (item.isActive) {
                        this.currentModel.paymentChannelUuids = [item.paymentChannelUuid];
                    }
                });
            },
            savePaymentChannelUuids: function() {
                var res = [];
                this.paymentChannelList.forEach(item => {
                    res.push(item.paymentChannelUuid);
                });
                this.currentModel.paymentChannelUuids = res;
            },
        }
    }
</script>