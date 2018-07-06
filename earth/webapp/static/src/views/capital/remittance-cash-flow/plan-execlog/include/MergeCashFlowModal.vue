<style lang="sass" scoped>
    @import '~assets/stylesheets/base';

    .query-area{
        
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
        <ModalHeader title="添加流水">
        </ModalHeader>
        <ModalBody align="left">
            <div class="query-area">
                <el-form ref="form" :model="formModel" :inline="true" :rules="rules" class="sdf-form sdf-query-form">
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="流水号" value="bankSequenceNo"></el-option>
                            <el-option label="银行账户号" value="hostAccountNo"></el-option>
                            <el-option label="账户姓名" value="hostAccountName"></el-option>
                            <el-option label="交易摘要" value="remark"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary" @click="queryCashFlow">查询</el-button>
                    </el-form-item>
                    <el-form-item>
                        <div>合并金额： {{model.plannedAmount | formatMoney}}</div>
                    </el-form-item>
                    <el-form-item
                        style="margin-left: 73px;"
                        label="备注"
                        prop="executionRemark"
                        required>
                        <el-input
                            class="long"
                            type="textarea"
                            v-model="formModel.executionRemark"
                            placeholder="请输入备注">
                        </el-input>
                    </el-form-item>
                </el-form>
            </div>
            <div class="row-layout-detail">
                <div class="block">
                    <div class="bd">
                        <el-table
                            :data="cashFlowList"
                            border
                            highlight-current-row
                            @current-change="handleCurrentChange"
                            style="width: 100%">
                            <el-table-column
                              label=""
                              width="55"
                              inline-template>
                              <el-radio v-model="currentRadio" :label="row.id">&nbsp;</el-radio>
                            </el-table-column>
                            <el-table-column
                              prop="bankSequenceNo"
                              label="流水号"
                              width="120">
                            </el-table-column>
                            <el-table-column
                              prop="accountSide"
                              label="借贷标志"
                              width="120"
                              inline-template>
                              <div>{{row.accountSide | formatAccountSide}}</div>
                            </el-table-column>
                            <el-table-column
                              prop="transactionAmount"
                              label="交易金额"
                              inline-template>
                              <div>{{row.transactionAmount | formatMoney}}</div>
                            </el-table-column>
                            <el-table-column
                              prop="hostAccountNo"
                              label="银行账号"
                              show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column
                              prop="hostAccountName"
                              label="银行账号名"
                              show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column
                              prop="aa"
                              label="开户行"
                              show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column
                              prop="transactionTime"
                              label="入账时间"
                              inline-template>
                              <div>{{row.transactionTime | formatDate}}</div>
                            </el-table-column>
                            <el-table-column
                              prop="remark"
                              label="摘要"
                              show-overflow-tooltip>
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

    export default {
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox'),
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
                    this.currentSelection = {};
                    this.currentRadio = '';
                    this.formModel.executionRemark = '';
                }
            }
        },
        data: function() {
            return {
                rules: {
                    executionRemark: {
                        required: true,
                        message: ' ',
                        trigger: 'blur'
                    }
                },

                visible: this.value,
                currentSelection: {},
                comboConds: {
                    bankSequenceNo: '',
                    hostAccountNo:'',
                    hostAccountName:'',
                    remark:''
                },
                cashFlowList: [],
                currentRadio:'',
                disabledFlag: false,

                formModel: {
                    executionRemark: ''
                }
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
                ajaxPromise({
                    url: `/capital/plan/execlog/searchCashFlow`,
                    data: this.comboConds
                }).then(data => {
                    this.cashFlowList = data.list;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleCurrentChange(currentRow,oldCurrentRow) {
                if(!this.isEmptyObject(currentRow)){
                    this.disabledFlag = true;
                    this.currentRadio = currentRow.id || '';
                    this.currentSelection = currentRow;
                    if(currentRow.transactionAmount == this.model.plannedAmount){
                        this.disabledFlag = false;
                    }
                }
            },
            saveCashFlow(){
                var cashFlowUuid = this.currentSelection.cashFlowUuid,
                    execReqNos = this.model.execReqNos,
                    executionRemark = this.formModel.executionRemark;
                this.$refs.form.validate(valid => {
                    if(valid){
                        ajaxPromise({
                            url:`/capital/plan/execlog/refundCombine`,
                            data:{
                                execReqNos: execReqNos,
                                cashFlowUuid: cashFlowUuid,
                                executionRemark: executionRemark
                            },
                            type: 'post'
                        }).then(data => {
                            MessageBox.open('修改状态和添加流水成功');
                            MessageBox.once('close', () => {
                                this.$router.push('/capital/remittance-cash-flow/plan-execlog');
                            });
                        }).catch(message => {
                            MessageBox.open(message);
                        }).then(() => {
                            this.currentRadio = '';
                            this.visible = false;
                        });
                    }
                })
            }
        },
        filters: {
            formatAccountSide: function(val){
                switch(val){
                    case 'DEBIT':
                        return '借';
                    case 'CREDIT':
                        return '贷';
                    default:
                        return '';
                }
            }
        }
    }
</script>