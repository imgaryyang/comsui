<style lang="sass" scoped>
    @import '~assets/stylesheets/base';

    .query-area{
        padding: 0px !important;
        background: #fff;
        border: none;
    }
    .export-modal {
        .el-table th > .cell,
        .el-table td > .cell {
            white-space: nowrap;
        }
        @include min-screen(768px) {
            .modal-dialog {
                width: 85%;
                margin: 30px auto;
            }
        }
    }
</style>

<template>
    <Modal v-model="visible" class="export-modal">
        <ModalHeader title="绑定流水">
        </ModalHeader>
        <ModalBody align="left">
            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd" style="margin-bottom: 0;">选择流水
                        <span style="padding-left: 20px;font-weight: normal">业务金额：{{model.totalAmount | formatMoney}}</span>
                    </h5>
                    <div class="bd">
                        <div class="query-area" style="padding-left: 0">
                            <el-form ref="form" :model="formModel" :inline="true" :rules="rules" class="sdf-form sdf-query-form">
                                <el-form-item>
                                    <ComboQueryBox v-model="comboConds">
                                        <el-option label="流水号" value="cashFlowNo"></el-option>
                                        <el-option label="银行账户号" value="accountNo"></el-option>
                                        <el-option label="账户姓名" value="accountName"></el-option>
                                        <el-option label="交易摘要" value="transactionRemark"></el-option>
                                    </ComboQueryBox>
                                </el-form-item>
                                <el-form-item>
                                    <el-button ref="lookup" size="small" type="primary" @click="queryCashFlow">查询</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                        <el-table
                            :data="cashFlowList"
                            v-loading="fetching"
                            border
                            @selection-change="handleSelectionChange"
                            style="width: 100%">
                            <el-table-column
                                type="selection"
                                width="55">
                            </el-table-column>
                            <el-table-column
                                prop="serialNo"
                                label="流水号"
                                width="120">
                            </el-table-column>
                            <el-table-column
                                prop="drcrf"
                                label="借贷标志"
                                width="120"
                                inline-template>
                                <div>{{row.drcrf | formatAccountSide}}</div>
                            </el-table-column>
                            <el-table-column
                                prop="relatedAmount"
                                label="交易金额"
                                inline-template>
                                <div>{{row.relatedAmount | formatMoney}}</div>
                            </el-table-column>
                            <el-table-column
                                prop="bankNo"
                                label="银行账号"
                                show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column
                                prop="bankNoName"
                                label="银行账号名"
                                show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column
                                prop="openBank"
                                label="开户行"
                                show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column
                                prop="time"
                                label="入账时间"
                                inline-template>
                                <div>{{row.time | formatDate}}</div>
                            </el-table-column>
                            <el-table-column
                                prop="summary"
                                label="摘要"
                                show-overflow-tooltip>
                            </el-table-column>
                          </el-table>
                    </div>
                </div>

                <div class="block">
                    <h5 class="hd" style="margin-bottom: 10px;">已选流水
                        <span style="padding-left: 20px;font-weight: normal">流水金额：{{sequenceAmount | formatMoney}}</span>
                    </h5>
                    <div class="bd">
                        <el-table
                            :data="multipleSelection"
                            border
                            style="width: 100%">
                            <el-table-column
                                prop="serialNo"
                                label="流水号"
                                width="120">
                            </el-table-column>
                            <el-table-column
                                prop="drcrf"
                                label="借贷标志"
                                width="120"
                                inline-template>
                                <div>{{row.drcrf | formatAccountSide}}</div>
                            </el-table-column>
                            <el-table-column
                                prop="relatedAmount"
                                label="交易金额"
                                inline-template>
                                <div>{{row.relatedAmount | formatMoney}}</div>
                            </el-table-column>
                            <el-table-column
                                prop="bankNo"
                                label="银行账号"
                                show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column
                                prop="bankNoName"
                                label="银行账号名"
                                show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column
                                prop="openBank"
                                label="开户行"
                                show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column
                                prop="time"
                                label="入账时间"
                                inline-template>
                                <div>{{row.time | formatDate}}</div>
                            </el-table-column>
                            <el-table-column
                                prop="summary"
                                label="摘要"
                                show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column
                                label="操作"
                                inline-template>
                                <div>
                                    <a @click.prevent="delCashFlow(row, $index)" href="javascript: void 0;">删除</a>
                                </div>
                            </el-table-column>
                          </el-table>
                    </div>
                </div>
            </div>
        </ModalBody>
        <ModalFooter>
            <el-button @click="visible = false" type="default">取消</el-button>
            <el-button @click="saveCashFlow" type="success" :disabled="disabledFlag">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import {ajaxPromise, purify, searchify} from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import {uniqBy} from 'lodash'

    export default {
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox'),
            PagingTable: require('views/include/PagingTable')
        },
        props: {
            value: Boolean,
            model: {
                default: () => ({})
            }
        },
        watch: {
            visible: function(current) {
                this.$emit('input', current);
            },
            value: function(current) {
                this.visible = current;
                if(!current){
                    this.cashFlowList = [];
                    this.comboConds = {};
                    this.disabledFlag = true;
                    this.multipleSelection = [];
                    this.fetching = false;
                }
            }
        },
        data: function() {
            return {
                visible: this.value,
                comboConds: {
                    cashFlowNo: '',
                    accountNo: '',
                    accountName:'',
                    transactionRemark:''
                },
                cashFlowList: [],
                multipleSelection: [],
                disabledFlag: true,
                fetching: false
            }
        },
        computed: {
            sequenceAmount: function() {
                var amount = 0;
                this.multipleSelection.forEach(item => {
                    if(item.drcrfName == '贷'){
                        amount -= item.relatedAmount;
                    }else{
                        amount += item.relatedAmount;
                    }
                })
                return amount;
            },
            cashFlows: function() {
                return this.multipleSelection.map(item => item);
            }
        },
        methods: {
            isEmptyObject:function(e){
                var t;
                for (t in e)
                    return !1;
                return !0
            },
            queryCashFlow: function() {
                if (!this.visible) return;

                this.fetching = true;
                ajaxPromise({
                    url: `/audit/beneficiary/clearingCashFlowQueryV2`,
                    data: Object.assign({}, this.queryConds, this.comboConds)
                }).then(data => {
                    this.fetching = false;
                    this.cashFlowList = data.list;
                }).catch(message => {
                    MessageBox.open(message);
                    this.fetching = false;
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = uniqBy(this.multipleSelection.concat(val), 'cashFlowUuid');
                if(this.sequenceAmount == this.model.totalAmount) {
                    this.disabledFlag = false;
                }
            },
            delCashFlow: function(row, $index) {
                this.multipleSelection.splice($index, 1);
            },
            saveCashFlow(){
                var clearingCashFlowModeList = this.cashFlows,
                    auditJobUuidList = this.model.auditJobUuidList,
                    remark = this.remark;

                ajaxPromise({
                    url:`/audit/beneficiary/reTouchingV2`,
                    data:{
                        clearingCashFlowModeList: JSON.stringify(clearingCashFlowModeList),
                        auditJobUuidList: JSON.stringify(auditJobUuidList),
                        remark: remark
                    },
                    type: 'post'
                }).then(data => {
                    MessageBox.open('修改状态和添加流水成功');
                    this.$emit('submit');
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.visible = false;
                });
            }
        },
        filters: {
            formatAccountSide: function(val){
                switch(val){
                    case 0:
                        return '贷';
                    case 1:
                        return '借';
                    default:
                        return '';
                }
            }
        }
    }
</script>