<style lang="sass">
    #TIMER_STRATEGY_CLASS{
        .align-left{
            text-align: left;
            margin-left: 35px;
            .selectStrategy{
                display: block
            }
            .add{
                color: #3388ee;
                cursor: pointer;
            }
            .selectStrategyContent{
                & > ul{
                    list-style: none;
                    padding: 0;
                    margin: 0;
                    & > li {
                        padding-left: 18px;
                        margin: 10px 0;
                    }
                }
                .selectequalinput{
                    height: 28px;
                    margin-left: 11px;
                }
            }
            .strategy-item{
                width: 100%;
                border: 1px solid #eee;
                border-radius: 5px;
                padding: 14px 8px;
                overflow: hidden;
                margin-bottom: 10px;
                .pointer{
                    cursor: pointer;
                }
                .paymentChannelNameTitle{
                    border: 1px solid #e0e0e0;
                    display: inline-block;
                    margin-right: 10px;
                    padding: 6px;
                    border-radius: 6px;
                }
                .paymentChannelNameTitle.z-active{
                    background-color: #ddd;
                }
            }
            .strategy-item.full{
                border: 1px solid #38e;
            }
            .strategy-item.timepicker{
                overflow: initial;
            }
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
                        v-if="['0','2'].includes(paymentStrategyMode)"
                        :index="2">
                        配置通道
                    </StepTipItem>
                    <StepTipItem
                        v-if="['3','1'].includes(paymentStrategyMode)"
                        :index="2">
                        选择通道策略
                    </StepTipItem>
                    <StepTipItem
                        v-if="['3','1'].includes(paymentStrategyMode)"
                        :index="3">
                        {{paymentStrategyMode == '3' ? '配置通道' : '配置发卡行首选通道'}}
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
                                <el-option value="2" label="限额优先模式">
                                </el-option>
                                <el-option value="3" label="定时切换模式">
                                </el-option>
                            </el-select>
                        </div>
                    </StepContentItem>
                    <div v-if="paymentStrategyMode == 3" id="TIMER_STRATEGY_CLASS">
                        <StepContentItem :index="2" class="align-left">
                            <div v-loading="fetchingChannel">
                                <p>请选择通道策略</p>
                                <div class="strategy-item timepicker">
                                    <label class="selectStrategy">
                                        <input type="radio" name="selectStrategy" value="everyday" v-model="timerStrategy">
                                        每天
                                        <span class="add" v-show="timerStrategy == 'everyday'" @click="timerStrategyModel.everydayItems.push({timerBeginValue:{HH: '00',mm:'00',ss:'00'},timerEndValue:{HH: '23', mm: '59', ss:'59'},timerStrategy:2,channelList: [],isfull: false})">添加</span>
                                    </label>
                                    <div v-show="timerStrategy == 'everyday'"  class="selectStrategyContent">
                                        <ul>
                                            <li v-for="(item, index) in timerStrategyModel.everydayItems">
                                                <VueTimepicker format="HH:mm:ss" v-model="item.timerBeginValue"></VueTimepicker>
                                                    ——
                                                <VueTimepicker format="HH:mm:ss" v-model="item.timerEndValue"></VueTimepicker>
                                                <select v-model="item.timerStrategy" placeholder="选择通道策略" class="selectequalinput">
                                                    <option value="0">单一通道模式</option>
                                                    <option value="2">限额优先模式</option>
                                                </select>
                                                <span class="el-icon-circle-close" style="color: #999; cursor:pointer;" @click="deleteCurrentItem(index, 'everydayItems')" v-show="1 in timerStrategyModel.everydayItems"></span>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="strategy-item timepicker">
                                    <label class="selectStrategy">
                                        <input type="radio" name="selectStrategy" value="workdayAndholiday" v-model="timerStrategy">
                                        工作日节假日
                                    </label>
                                    <div v-show="timerStrategy == 'workdayAndholiday'"  class="selectStrategyContent">
                                        <h5 style="padding-left: 18px;">工作日 
                                            <span class="add" @click="timerStrategyModel.workdayItems.push({timerBeginValue:{HH: '00',mm:'00',ss:'00'},timerEndValue:{HH: '23', mm: '59', ss:'59'},timerStrategy:2, channelList: [],isfull: false})">添加</span>
                                        </h5>
                                        <ul>
                                            <li v-for="(item, index) in timerStrategyModel.workdayItems">
                                                <VueTimepicker format="HH:mm:ss" v-model="item.timerBeginValue"></VueTimepicker>
                                                    ——
                                                <VueTimepicker format="HH:mm:ss" v-model="item.timerEndValue"></VueTimepicker>
                                                <select v-model="item.timerStrategy" placeholder="选择通道策略" class="selectequalinput">
                                                    <option value="0">单一通道模式</option>
                                                    <option value="2">限额优先模式</option>
                                                </select>
                                                <span class="el-icon-circle-close" style="color: #999;cursor:pointer;" @click="deleteCurrentItem(index, 'workdayItems')" v-show="1 in timerStrategyModel.workdayItems"></span>
                                            </li>
                                        </ul>
                                        <hr style="margin: 15px 18px;border-color: #e0e0e0;">
                                        <h5 style="padding-left: 18px;">节假日
                                            <span class="add" @click="timerStrategyModel.holidayItems.push({timerBeginValue:{HH: '00',mm:'00',ss:'00'},timerEndValue:{HH: '23', mm: '59', ss:'59'},timerStrategy:2, channelList: [],isfull: false})">添加</span>
                                        </h5>
                                        <ul>
                                            <li v-for="(item, index) in timerStrategyModel.holidayItems">
                                                <VueTimepicker format="HH:mm:ss" v-model="item.timerBeginValue"></VueTimepicker>
                                                    ——
                                                <VueTimepicker format="HH:mm:ss" v-model="item.timerEndValue"></VueTimepicker>
                                                <select v-model="item.timerStrategy" placeholder="选择通道策略" class="selectequalinput">
                                                    <option value="0">单一通道模式</option>
                                                    <option value="2">限额优先模式</option>
                                                </select>
                                                <span class="el-icon-circle-close" style="color: #999; cursor:pointer;" @click="deleteCurrentItem(index, 'holidayItems')" v-show="1 in timerStrategyModel.holidayItems"></span>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </StepContentItem>
                        <StepContentItem :index="3" class="align-left">
                            <div v-if="">
                                <p><i>请配置默认通道：</i><span class="color-dim">拖动方块，以从左到右、从上到下为序排优先级，左上优先级最高</span></p>
                                <div v-for="(items, key , index) in timerStrategyModel">
                                    <div v-for="item in items">
                                        <div :class="['strategy-item', item.isfull ? 'full' : '']">
                                            <p><b>{{timerCN[key]}}</b><span class="color-dim"> ({{item.timerBeginValue | time2str}} - {{item.timerEndValue | time2str}}  {{timerStr[+item.timerStrategy+1]}} )</span><i :class="['pull-right','pointer', item.isfull ? 'el-icon-arrow-up' : 'el-icon-arrow-down']" @click="item.isfull = !item.isfull"></i></p>
                                            <div v-show="!item.isfull">
                                                <span v-for="(channel, index) in item.channelList" :class="['paymentChannelNameTitle', 'color-dim', channel.isActive && item.timerStrategy == SINGLE_STRATEGY? 'z-active' : '']">{{ channel.paymentChannelName }}</span>
                                            </div>
                                            <draggable :list="item.channelList" class="panels" v-show="item.isfull">
                                                <div 
                                                    class="panel" 
                                                    v-for="(channel, index) in item.channelList" 
                                                    :class="{'z-active': channel.isActive, 'color-trap': item.timerStrategy != SINGLE_STRATEGY}" 
                                                    @click="clickPanel(index, item)"
                                                    :key="channel.paymentChannelUuid">
                                                    <div class="paymentChannelName">{{ channel.paymentChannelName }}</div>
                                                    <div class="transactionLimitPer">
                                                        <div>单笔限额 ： {{channel.transactionLimitPerMin}}~{{channel.transactionLimitPerMax}}万/笔</div>
                                                    </div>
                                                    <div>
                                                        <span class="color-dim">{{ '('+channel.fee+')' }}</span>
                                                        <span class="pull-right">{{ channel.channelStatusMsg }}</span>
                                                    </div>
                                                </div>
                                            </draggable>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </StepContentItem>
                    </div>
                    <div v-else>
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
                                            <div>单笔限额 ： {{item.transactionLimitPerMin}}~{{item.transactionLimitPerMax}}万/笔</div>
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
                    </div>
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
    import draggable from 'vuedraggable';
    import validateTimerStrategyMixin from './validateTimerStrategy-mixin';
    import _ from 'lodash';
    import VueTimepicker from 'components/Vue2TimePicker';
    import PagingTable from 'views/include/PagingTable';

    export default {
        mixins: [modalMixin, validateTimerStrategyMixin],
        components: {
            StepContent, StepContentItem, StepTip, StepTipItem, StepFooter, draggable, VueTimepicker, PagingTable,
        },
        props: {
            model: {
                default: null
            }
        },
        watch: {
            show: function(){
                this.timerStrategyModel = {
                    everydayItems:[],
                    workdayItems: [],
                    holidayItems: []
                }
                this.paymentStrategyMode = this.LIMIT_STRATEGY;
            },
            model: function(cur) {
                this.currentModel = Object.assign({
                    financialContractUuid: '',
                    accountSide: '',
                    businessType: '',
                    paymentStrategyMode: this.paymentStrategyMode,
                    paymentChannelUuids: [],
                    paymentChannelOrderForBanks: {}
                },cur);

                this.stepIndex = 1;
                this.paymentChannelList = [];
                this.bankList = [];
                this.previewList = [];
            },
            paymentStrategyMode: function(mode) {
                this.isColorTrap = false;

                switch (mode){
                    case this.SINGLE_STRATEGY:
                        this.stepNumber = 2;
                        break;
                    case this.ISSUER_STRATEGY:
                        this.stepNumber = 4;
                        this.isColorTrap = true;
                        break;
                    case this.LIMIT_STRATEGY:
                        this.stepNumber = 2;
                        this.isColorTrap = true;
                        break;
                    case this.TIMER_STRATEGY:
                        this.stepNumber = 3;
                        this.isColorTrap = true;
                        break;
                    default:
                        this.stepNumber = 1;
                        break;
                }
                this.currentModel.paymentStrategyMode = parseInt(mode);
            },
            'currentModel.paymentChannelOrderForBanks': function (current) {
                console.log(current)
            }
        },
        data: function() {
            return {
                NO_CHANNEL_STRATEGY: '-1',
                SINGLE_STRATEGY: '0',
                ISSUER_STRATEGY: '1',
                LIMIT_STRATEGY: '2',
                TIMER_STRATEGY: '3',

                stepIndex: 1,
                stepNumber: 2,
                show: this.value,
                paymentStrategyMode: '',

                currentModel: Object.assign({paymentStrategyMode:''}, this.model),

                paymentChannelList: [],
                bankList: [],
                previewList: [],
// 用isColorTrap模拟isActive的颜色，形成被选中的效果，使视觉上一致
                isColorTrap: false,
//定时切换策略相关
                timerValue: '',
                timerStrategy: 'everyday',
                timerStrategyModel: {
                    everydayItems:[],
                    workdayItems: [],
                    holidayItems: []
                },
                timerCN : {
                    everyday: '每天',
                    everydayItems: '每天',
                    workday: '工作日',
                    workdayItems: '工作日',
                    holiday: '节假日',
                    holidayItems: '节假日',
                },
                timerStr: ['无通道模式', '单一通道模式', '', '限额优先模式'],
                fetchingChannel: false,

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
                if (this.paymentStrategyMode == this.TIMER_STRATEGY && this.stepIndex == 2) {
                    if(this.timerStrategy == 'everyday'){
                        return this.timerStrategyModel.everydayItems.length == 0 || !this.timerStrategyModel.everydayItems.every(i => {
                            return i.timerBeginValue && i.timerEndValue
                        })
                    }else{
                        return this.timerStrategyModel.workdayItems.length == 0 || !this.timerStrategyModel.workdayItems.every(i => {
                            return i.timerBeginValue && i.timerEndValue
                        }) || this.timerStrategyModel.holidayItems.length == 0 || !this.timerStrategyModel.holidayItems.every(i => {
                            return i.timerBeginValue && i.timerEndValue
                        })
                    }
                }else if (this.paymentStrategyMode == this.TIMER_STRATEGY && this.stepIndex == 3){
                    return false
                }else{
                    return (this.stepIndex == 2 && this.paymentChannelList.length == 0) || (this.stepIndex == 3 && this.bankList.length == 0);
                }
            }
        },
        methods: {
            deleteCurrentItem: function(index, arr){
                this.timerStrategyModel[arr].splice(index, 1)
            },
            complete: function() {
                var mode = this.currentModel.paymentStrategyMode;
                if (mode == this.NO_CHANNEL_STRATEGY ) {
                    this.currentModel.paymentChannelUuids = [];
                    this.currentModel.paymentChannelOrderForBanks = {};
                }else if(mode == this.SINGLE_STRATEGY) {
                    this.currentModel.paymentChannelOrderForBanks = {};
                    this.savePaymentChannelUuid();
                }else if(mode == this.TIMER_STRATEGY){
                    this.savePaymentChannelForTimerStrategy()
                    return
                }else {
                    this.savePaymentChannelUuids();
                }
                this.save();
            },
            next: function(step, prevStep) {
                if(this.paymentStrategyMode != this.TIMER_STRATEGY){
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
                }else if(this.paymentStrategyMode == this.TIMER_STRATEGY && this.stepIndex == 3){
                    --this.stepIndex;
                    var valid = {}
                    if(this.timerStrategy == 'everyday'){
                        valid = this.validateTimerStrategyItems(this.timerStrategyModel.everydayItems, 'everyday')
                    }else{
                        valid = this.validateTimerStrategyItems(this.timerStrategyModel.workdayItems, 'workday')
                        if(!valid.overlap && valid.alltime){
                            valid = this.validateTimerStrategyItems(this.timerStrategyModel.holidayItems, 'holiday')
                        }
                    }
                    if(valid.overlap){
                        MessageBox.open(`"${this.timerCN[valid.timer]}"您选择的时间有重叠，请修改！`)
                        return 
                    }
                    if(!valid.alltime){
                        MessageBox.open(`"${this.timerCN[valid.timer]}"您选择的时间不足24小时，是否继续下一步！`, '提示', [{
                            text: '取消',
                            handler: () => {
                                MessageBox.close()
                            }
                        }, {
                            text: '继续',
                            type: 'success',
                            handler: () => {
                                MessageBox.close();
                                this.getPaymentChannelList()
                            }
                        }]);
                        return 
                    }
                    this.getPaymentChannelList()
                }
            },
            getPaymentChannelList: function() {
                if (this.paymentChannelList.length != 0){
                    if(this.paymentStrategyMode == this.TIMER_STRATEGY){
                        this.fetchingChannel = false
                        for(var k in this.timerStrategyModel){
                            this.timerStrategyModel[k].forEach((item, index)=>{
                                if(item.timerStrategy != this.NO_CHANNEL_STRATEGY)
                                    item.channelList = _.cloneDeep(this.paymentChannelList)
                            })
                        }
                        if(this.timerStrategy == 'everyday'){
                            this.timerStrategyModel.workdayItems = []
                            this.timerStrategyModel.holidayItems = []
                        }else if (this.timerStrategy == 'workdayAndholiday'){
                            this.timerStrategyModel.everydayItems = []
                        }
                        ++this.stepIndex
                    }
                     return;
                }
                if(this.paymentStrategyMode == this.TIMER_STRATEGY){
                    this.fetchingChannel = true
                }
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
                }).then(()=>{
                    if(this.paymentStrategyMode == this.TIMER_STRATEGY){
                        this.fetchingChannel = false
                        for(var k in this.timerStrategyModel){
                            this.timerStrategyModel[k].forEach((item, index)=>{
                                if(item.timerStrategy != this.NO_CHANNEL_STRATEGY)
                                    item.channelList = _.cloneDeep(this.paymentChannelList)
                            })
                        }
                        if(this.timerStrategy == 'everyday'){
                            this.timerStrategyModel.workdayItems = []
                            this.timerStrategyModel.holidayItems = []
                        }else if (this.timerStrategy == 'workdayAndholiday'){
                            this.timerStrategyModel.everydayItems = []
                        }
                        ++this.stepIndex
                    }
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
                if (this.paymentStrategyMode == this.TIMER_STRATEGY && array.timerStrategy == this.SINGLE_STRATEGY){
                    array.channelList.forEach((item, n) => {
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
            savePaymentChannelForTimerStrategy: function(){
                var result = {};
                result.financialContractUuid = this.model.financialContractUuid;
                result.businessType = this.model.businessType;
                result.accountSide = this.model.accountSide;
                result.timingStrategies = [];
                var code = {
                  everydayItems: 0,
                  workdayItems: 1,
                  holidayItems:2
                }
                var k;
                for(k in this.timerStrategyModel){
                  if(this.timerStrategyModel[k].length > 0){
                    var o = {};
                    o.timeCycle = code[k]
                    o.timeSlices = []
                    this.timerStrategyModel[k].forEach(item=>{
                        var uuids;
                        if (item.timerStrategy == this.SINGLE_STRATEGY){
                            uuids = item.channelList.filter(i =>{
                                return i.isActive
                            }).map(i =>{
                                return i.paymentChannelUuid
                            })
                        }else{
                            uuids = item.channelList.map(i =>{
                                return i.paymentChannelUuid
                            })
                        }
                        o.timeSlices.push({
                          begin: item.timerBeginValue.HH + ':' + item.timerBeginValue.mm + ':' + item.timerBeginValue.ss,
                          end: item.timerEndValue.HH + ':' + item.timerEndValue.mm + ':' + item.timerEndValue.ss,
                          paymentStrategyMode: item.timerStrategy,
                          paymentChannelInformationUuids: uuids
                        })
                    })
                    result.timingStrategies.push(o)
                  }
                }
                ajaxPromise({
                    url: `/paymentchannel/switch/strategy/timing/saveResult`,
                    data: JSON.stringify(result),
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
        },
        filters: {
            time2str: function(time){
                if (!time) return ''
                return time.HH +':'+time.mm+':'+time.ss
            }
        }
    }
</script>