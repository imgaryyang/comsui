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
                    <h5 class="hd" style="margin-bottom: 0;">查找流水
                        <span style="padding-left: 20px;font-weight: normal">业务金额：{{model.plannedAmount | formatMoney}}</span>
                    </h5>
                    <div class="bd">
                        <div class="query-area" style="padding-left: 0">
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

                <div class="block">
                    <h5 class="hd" style="margin-bottom: 10px;">绑定流水
                        <span style="padding-left: 20px;font-weight: normal">流水金额：{{sequenceAmount | formatMoney}}</span>
                    </h5>
                    <div class="bd">
                        <el-table
                            :data="multipleSelection"
                            border
                            style="width: 100%">
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
	import {uniqBy} from 'lodash'

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
                    this.disabledFlag = true;
                    this.sequenceAmount = 0;
                    this.multipleSelection = [];
                    this.fetching = false;
				}
			}
		},
		data: function() {
			return {
				visible: this.value,
				comboConds: {
					bankSequenceNo: '',
					hostAccountNo:'',
					hostAccountName:'',
					remark:''
				},
				cashFlowList: [],
				multipleSelection: [],
                sequenceAmount: 0,
				disabledFlag: true,
				fetching: false
			}
		},
		computed: {
            sequenceAmount: function() {
                var amount = 0;
                this.multipleSelection.forEach(item => {
                    amount += item.transactionAmount;
                })
                return amount;
            },
            cashFlowUuids: function() {
                return this.multipleSelection.map(item => item.cashFlowUuid);
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
				    url: `/capital/plan/execlog/searchCashFlow`,
				    data: this.comboConds
				}).then(data => {
					this.fetching = false;
					this.cashFlowList = data.list;
				}).catch(message => {
                    MessageBox.open(message);
                    this.fetching = false;
                });
			},
			// handleCurrentChange(currentRow,oldCurrentRow) {
			// 	if(!this.isEmptyObject(currentRow)){
			// 		this.disabledFlag = true;
			// 		this.currentRadio = currentRow.id || '';
			// 	    this.currentSelection = currentRow;
			// 	    if(currentRow.transactionAmount == this.model.plannedAmount){
			// 	    	this.disabledFlag = false;
			// 	    }
			// 	}
			// },
			handleSelectionChange(val) {
                this.multipleSelection = uniqBy(this.multipleSelection.concat(val), 'bankSequenceNo');
                if(this.sequenceAmount == this.model.plannedAmount) {
                    this.disabledFlag = false;
                }
            },
			saveCashFlow(){
				var cashFlowUuids = this.cashFlowUuids,
					execReqNo = this.model.execReqNo,
					reverseStatusModal = this.model.modifyReverseStatusModel,
					executionRemark = reverseStatusModal.executionRemark;

                ajaxPromise({
					url:`/capital/plan/execlog/addCashFlow`,
					data:{
						execReqNo: execReqNo,
						cashFlowUuids: JSON.stringify(cashFlowUuids),
						executionRemark: executionRemark
					},
					type: 'post'
				}).then(data => {
                    MessageBox.open('修改状态和添加流水成功');
                    MessageBox.once('close', () => {
                      this.$emit('submit',reverseStatusModal);
                    });
                    setTimeout(() => {
                    	MessageBox.close()
                    },1000)
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