<style lang="sass">
    @import '~assets/stylesheets/base';
    #export-operation-data{
        @include min-screen(768px) {
            .modal-dialog{
                width: 62%;
            }
        }
        .modal-body-inner{
            text-align: left;
            display: flex;
            padding: 30px 55px;
            height: 440px;
                .left-content{
                    flex-basis:760px;
                    flex-grow:1;
                    .left-header-title{
                        overflow: hidden;
                        font-size: 14px;
                        line-height: 35px;
                        height: 40px;
                        border: 1px solid #dedede;
                        span{
                            font-weight: 600;
                            float: left;
                            margin-left:20px
                        }
                        a{
                            float: right;
                            margin-right: 20px;
                        }
                    }
                    .left-content-menu{
                        border: 1px solid #dedede;
                        border-top: 0;
                        height: 340px;
                        overflow-y:auto;
                        .menu-item{
                            position: relative;
                            height: 40px;
                            border-bottom: 1px solid #dedede;
                            padding-left: 20px;
                            line-height: 40px;
                            .arrow {
                                cursor: pointer;
                                position: absolute;
                                right: 20px;
                                top: 12px;
                            }
                        }
                        .submenu-item{
                            padding:10px 20px 0 20px;
                            background: #ebebeb;
                            line-height: 34px;
                            .el-checkbox{
                                background: #ffffff;
                                box-shadow: 0 2px 0 #d7d7d7;
                                padding: 3px 10px;
                                margin-bottom: 10px;
                                margin-right: 10px;
                            }
                            .el-checkbox + .el-checkbox{
                                margin-left:0;
                            }
                        }
                    }
                }
                .right-content{
                    flex-basis: 240px;
                    margin-left: 15px;
                    border: 1px solid #dedede;
                    flex-grow: 1;
                    .right-content-title{
                        height: 40px;
                        border-bottom: 1px solid #dedede;
                        overflow: hidden;
                        font-size: 14px;
                        line-height:35px;
                        span{
                            float: left;
                            margin-left:20px;
                            font-weight: 600;
                        }
                        a{
                            float: right;
                            margin-right: 20px;
                        }
                    }
                    .right-selected{
                        height: 340px;
                        overflow: auto;
                        padding: 20px;
                        margin-bottom:0;
                        .selected-item{
                            border: 1px solid #dedede;
                            box-shadow:  0 1px 0 #fefafa;
                            padding: 8px;
                            list-style:none;
                            margin-bottom:10px;
                            position: relative;
                            .icon-remove{
                                top: 12px;
                                font-size: 10px;
                                cursor: pointer;
                                position: absolute;
                                right: 20px;
                            }
                        }

                    }
                }
        }
    }
</style>

<template>
    <Modal v-model="show" id="export-operation-data">
        <ModalHeader title="导出运营数据"></ModalHeader>
        <ModalBody>
            <div class="modal-body-inner">
                <div class="left-content">
                    <div class="left-header-title">
                        <span>待选字段</span>
                        <a href="javascript:void 0" @click="clearHasChecked">清空</a>
                    </div>
                    <div class="left-content-menu">
                        <div class="menu-item">
                            <el-checkbox v-model="hasCheckedArrOther" border label="remittance">放款数据</el-checkbox>
                        </div>
                        <div class="menu-item">
                            <el-checkbox v-model="planChecked" name="plan-repay"  border @change="handlePlanCheckAll($event)">计划还款</el-checkbox>
                            <span class="arrow" :class="{'el-icon-arrow-down':!expandPlan,'el-icon-arrow-up':expandPlan}" @click="expandPlan = !expandPlan"></span>
                        </div>
                        <el-checkbox-group v-model="hasCheckedArrPlan" @change="handleCheckPlan" class="submenu-item" v-show="expandPlan">
                            <el-checkbox v-for="(subM, index) in planData" :label="subM" border>{{chineseToEnglish[subM]}}</el-checkbox>
                        </el-checkbox-group>
                        <div class="menu-item">
                            <el-checkbox v-model="actChecked" border @change="handleActCheckAll($event)">实际还款</el-checkbox>
                            <span class="arrow" :class="{'el-icon-arrow-down':!expandAct,'el-icon-arrow-up':expandAct}" @click="expandAct = !expandAct"></span>
                        </div>
                        <el-checkbox-group v-model="hasCheckedArrAct" @change="handleCheckAct" class="submenu-item" v-show="expandAct">
                            <el-checkbox v-for="(subM, index) in actData" :label="subM" border>{{chineseToEnglish[subM]}}</el-checkbox>
                        </el-checkbox-group>
                        <div class="menu-item">
                            <el-checkbox v-model="hasCheckedArrOther" border label="guarantee">担保数据</el-checkbox>
                        </div>
                        <div class="menu-item">
                            <el-checkbox v-model="hasCheckedArrOther" border label="repurchase">回购数据</el-checkbox>
                        </div>
                    </div>
                </div>
                <div class="right-content">
                    <div class="right-content-title">
                        <span>已选字段</span>
                        <a href="javascript:void 0" @click="clearHasChecked">清空</a>
                    </div>
                    <ul class="right-selected">
                        <li class="selected-item" v-if="hasCheckedArrOther.includes('remittance')">
                            <span>放款数据</span>
                            <span class="icon-remove el-icon-close" @click="hasCheckedArrOther.splice(hasCheckedArrOther.indexOf('remittance'),1)"></span>
                        </li>
                        <li class="selected-item" v-for="item in hasCheckedArrPlan">
                            <span>{{chineseToEnglish[item]}}</span>
                            <span class="icon-remove el-icon-close" @click="hasCheckedArrPlan.splice(hasCheckedArrPlan.indexOf(item),1)"></span>
                        </li>
                        <li class="selected-item" v-for="item in hasCheckedArrAct">
                            <span>{{chineseToEnglish[item]}}</span>
                            <span class="icon-remove el-icon-close" @click="hasCheckedArrAct.splice(hasCheckedArrAct.indexOf(item),1)"></span>
                        </li>
                        <li class="selected-item" v-if="hasCheckedArrOther.includes('guarantee')">
                            <span>担保数据</span>
                            <span class="icon-remove el-icon-close" @click="hasCheckedArrOther.splice(hasCheckedArrOther.indexOf('guarantee'),1)"></span>
                        <li class="selected-item" v-if="hasCheckedArrOther.includes('repurchase')">
                            <span>回购数据</span>
                            <span class="icon-remove el-icon-close" @click="hasCheckedArrOther.splice(hasCheckedArrOther.indexOf('repurchase'),1)"></span>
                    </ul>
                </div>
            </div>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button type="success" @click="exportOperationData">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise, downloadFile } from 'assets/javascripts/util';
    export default {
        props: {
            value: Boolean,
            model: {
                type:Object,
                default:()=>{}
            }
        },
        data: function () {
            return {
                show: this.value,
                hasCheckedArrPlan: [],
                hasCheckedArrAct: [],
                hasCheckedArrOther: [],
                expandPlan:false,
                expandAct:false,
                planData: ['planRepayment', 'notOverduePlanRepayment', 'unconfirmedPlanRepayment','overduePlanRepayment'],
                actData: ['totalActualRepayment','onlineRepayment', 'offlineRepayment','offlinePayment','PreRepayment','PartRepayment'],
                planChecked: false,
                actChecked: false,
                chineseToEnglish:{
                    remittance:'放款数据',
                    planRepayment:'应还日计划还款数据',
                    notOverduePlanRepayment:'宽限期计划还款数据',
                    unconfirmedPlanRepayment:'已逾期计划还款数据',
                    overduePlanRepayment:'待确认计划还款数据',
                    totalActualRepayment:'实际还款总额',
                    onlineRepayment:'线上还款',
                    offlineRepayment:'线下还款',
                    offlinePayment: '线下支付单',
                    PreRepayment: '提前还款',
                    PartRepayment:'部分还款',
                    guarantee:'担保数据',
                    repurchase:'回购数据'
                }
            }
        },
        watch: {
            show: function (current) {
                this.$emit('input', current);
            },
            value: function (current) {
                this.show = current;
            }
        },
        methods:{
            handlePlanCheckAll:function (ev) {
                this.hasCheckedArrPlan = ev.target.checked? ['planRepayment','notOverduePlanRepayment','unconfirmedPlanRepayment','overduePlanRepayment']:[]
            },
            handleActCheckAll:function (ev) {
                this.hasCheckedArrAct = ev.target.checked? ['totalActualRepayment','onlineRepayment','offlineRepayment','offlinePayment','PreRepayment','PartRepayment']:[]
            },
            handleCheckPlan:function (value) {
                this.planChecked = value.length === 4;
            },
            handleCheckAct:function (value) {
                this.actChecked = value.length === 6;
            },
            clearHasChecked:function () {
                this.hasCheckedArrPlan = [];
                this.hasCheckedArrAct = [];
                this.hasCheckedArrOther = [];
            },
            exportOperationData:function () {
                let checkedData = {};
                this.hasCheckedArrPlan.forEach(function (value) {
                    checkedData[value] = true;
                });
                this.hasCheckedArrAct.forEach(function (value) {
                    checkedData[value] = true;
                });
                this.hasCheckedArrOther.forEach(function (value) {
                    checkedData[value] = true;
                });
                const queryModel = {financialContractUuid:this.model.financialContractUuid,queryStartDate: this.model.queryStartDate,queryEndDate:this.model.queryEndDate};
                checkedData.queryModel = JSON.stringify(queryModel)

                downloadFile(`${this.api}/operation-data/export`, checkedData)
            }
        }
    }
</script>