<style lang="sass">
	.payment-order-popper {
        .el-select-dropdown__list {
            .el-select-dropdown__item {
                &.selected {
                    &:after{
                        position: absolute;
                        right: 10px;
                        top: 23px;
                        font-family: 'element-icons';
                        content: "\E608";
                        font-size: 11px;
                        -webkit-font-smoothing: antialiased;
                    }
                }
            }
        }
    }
</style>

<template>
	<div class="content">
		<div class="scroller" v-loading="fetching">
			<Breadcrumb :routes="[{title: '还款订单列表'},{title: '订单支付'}]">
			</Breadcrumb>
			<div>
				<el-form
					ref="form"
                    :model="currentModel"
                    :rules="rules"
                    label-width="120px" 
                    class="sdf-form">
                    <div class="fieldset">
                    	<el-form-item label="支付信息" class="form-item-legend">
                    		<div class="supplement"> &nbsp;(需支付金额：{{ currentModel.unPaidAmount  | formatMoney }})</div>
                    	</el-form-item>
                    	<el-form-item label="五维订单号" prop="orderUuid">
                            <div>{{ currentModel.orderUuid }}</div>
                    	</el-form-item>
                    	<el-form-item :label="$utils.locale('financialContract.name')" prop="financialContractNo">
                            <div>{{ currentModel.financialContractNo }}</div>
                    	</el-form-item>
                    	<el-form-item label="支付方式" prop="payWay" required>
                    		<el-select v-model="currentModel.payWay" class="long">
                    			<el-option 
                    				v-for="item in payWayList"
                    				:label="item.value"
                    				:value="item.key">
                    			</el-option>
                    		</el-select>
                    	</el-form-item>
                    	<el-form-item label="付款方户名" prop="counterAccountName">
                    		<el-autocomplete 
                                v-model="currentModel.counterAccountName"
                                class="long repayment-account-list"
                                custom-item="repayment-account-item"
                                @select="selectAccountNo"
                                :fetch-suggestions="fetchRepaymentAccount">
                            </el-autocomplete>
                    	</el-form-item>
                    	<el-form-item label="付款方账号" prop="counterAccountNo" required>
                    		<el-input class="long" v-model="currentModel.counterAccountNo"></el-input>
                    	</el-form-item>
                    	<el-form-item label="付款方开户行" prop="counterBankName">
                    		<el-input class="long" v-model="currentModel.counterBankName"></el-input>
                    	</el-form-item>
                    </div>
                    <div class="fieldset">
                    	<el-form-item label="流水信息" class="form-item-legend">
                    		<div class="supplement"> &nbsp;(流水金额：{{ selectedTransactionAmount | formatMoney }})</div>
                    	</el-form-item>
                    	<el-form-item label="选择流水" prop="selectedCashFlows" required>
	                    	<el-select class="long" v-model="selectedCashFlows" multiple placeholder="请选择流水" dropdown-class="richtext-dropdown payment-order-popper">
	                    		<el-option 
	                    			v-for="item in cashFlowModels"
	                    			:label="item.bankSequenceNo"
	                    			:value="item">
                                    <div class="title">
                                        <span>{{ item.bankSequenceNo}}</span>
                                        <div class="subtitle">
                                            {{ item.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss')}}
                                            <span class="supplement">({{ item.transactionAmount | formatMoney }})</span>
                                        </div>
                                    </div>
	                    		</el-option>
	                    	</el-select>
	                    	&nbsp;
	                    	<el-button style="min-width: 80px;" type="primary" @click="selectCashFlow" :disabled="cashFlowModels.length == 0">确定</el-button>
                    	</el-form-item>
                    	<div style="margin: 10px 15% 10px 53px;">
                            <el-table 
                                :data="selectedCashFlowList"
                                class="td-15-padding th-8-15-padding no-th-border"
                                border>
                                <el-table-column prop="bankSequenceNo" label="流水号"></el-table-column>
                                <el-table-column inline-template label="交易金额">
                                    <div> {{ row.transactionAmount | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column prop="counterAccountName" label="账户户名"></el-table-column>
                                <el-table-column prop="counterAccountNo" label="银行账户号"></el-table-column>
                                <el-table-column prop="counterBankName" label="开户行"></el-table-column>
                                <el-table-column inline-template label="入账时间">
                                    <div>
                                        {{ row.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss') }}
                                    </div>
                                </el-table-column>
                                <el-table-column prop="auditStatusMessage" label="对账状态"></el-table-column>
                                <el-table-column prop="remark" label="摘要"></el-table-column>
                                <el-table-column inline-template label="操作">
                                    <a href="#" @click.prevent="cancelCashFlow(row)">取消</a>
                                </el-table-column>
                            </el-table>
                        </div>    
                    </div>
                    <div class="fieldset">
                    	<el-form-item label="支付金额" class="form-item-legend">
                    	</el-form-item>
                    	<el-form-item label="实际支付金额" prop="">
                            <div>{{ selectedTransactionAmount | formatMoney }}</div>
                    	</el-form-item>
                    </div>
                    <div class="fieldset">
                        <el-form-item label="备注信息" class="form-item-legend">
                        </el-form-item>
                        <el-form-item label="备注" prop="remark" required>
                            <el-input class="long" v-model="currentModel.remark"></el-input>
                        </el-form-item>
                    </div>
                    <div class="fieldset">
                    	<el-button style="min-width: 80px;" type="primary" @click="$router.go(-1)">取消</el-button>
                        <el-button style="min-width: 80px;" type="primary" @click="submit">提交</el-button>
                    </div>
				</el-form>
			</div>
		</div>
	</div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import Vue from 'vue';
    import Decimal from 'decimal.js';

	Vue.component('repayment-account-item', {
		functional: true,
        render: function(h,ctx) {
            var item = ctx.props.item;
            return h('li', Object.assign({},ctx.data,{style:{padding:'5px'}}), [
                h('div', { style: { 'line-height': '22px' } }, [item.counterAccountNo]),
                h('div', { style: { 'font-size': '12px','line-height': '22px'} }, [
                    [item.counterAccountName],
                    h('span', { style: { color: '#999' } }, [`(${item.counterBankName == undefined ? '' : item.counterBankName})`])
                ])
            ]);
        },
        props: {
            item: { type: Object, required: true}
        }
	});

	export default {
		data: function() {
			return {
                fetching: false,
				currentModel: Object.assign({}, {
                    unPaidAmount: '',
                    financialContractUuid: '',
                    financialContractNo: '',
                    orderUuid: '',
                    counterAccountName: '',
                    counterAccountNo: '',
                    counterBankName: '',
                    payWay: '0',
                    remark: '',
                }),
				rules: {
                    counterAccountNo: { required: true, message: ' ', trigger: 'blur' },
                    remark: { required: true, message: ' ', trigger: 'blur' },
				},
                payWayList: [],
				cashFlowList: [],
                selectedCashFlows: [],
                selectedCashFlowList: [],
                accountList: [],

                getCounterAccountName: false, //counterAccountName 从选择流水中提取

			}
		},
        computed: {
            orderUuid: function() {
                return this.$route.query.orderUuid;
            },
            selectedTransactionAmount: function() {
                var resultAmount = new Decimal(0);
                this.selectedCashFlowList.forEach(item => {
                    var transactionAmount = isNaN(parseFloat(item.transactionAmount))? 0 : parseFloat(item.transactionAmount);
                    resultAmount = resultAmount.add(transactionAmount);
                })
                return resultAmount.toNumber();
            },
            cashFlowModels: function() {
                var res = this.cashFlowList.filter(item => {
                    var index = this.selectedCashFlowList.findIndex(selected => selected.cashFlowUuid == item.cashFlowUuid);
                    return index == -1;
                });
                return res;
            },
            selectedCashFlowUuids: function() {
                var cashFlowUuids = [];
                this.selectedCashFlowList.forEach(item => {
                    cashFlowUuids.push(item.cashFlowUuid);
                });
                return cashFlowUuids;
            }
        },
        watch: {
            'currentModel.counterAccountName': function(current) {
                if (this.getCounterAccountName) return;
                this.cancelAllCashFlow();
            },
            'currentModel.counterAccountNo': function(current) {
                this.cancelAllCashFlow();
                this.fetchCashFlowList();
            },
            selectedCashFlows: function(current) {
                if (!current.length) return;
                if (this.currentModel.counterAccountName == undefined || this.currentModel.counterAccountName == '') {
                    this.getCounterAccountName =  true;
                    this.currentModel.counterAccountName = current[0].counterAccountName;
                }
            }
        },
        activated: function() {
            if (this.orderUuid) {
                this.getCounterAccountName = false;
                this.fetchDetailInfo(this.orderUuid);
                this.fetchPayWayList();
            }
        },
		methods: {
            fetchDetailInfo: function(orderUuid) {
                ajaxPromise({
                    url: '/repayment-order/repayment/detail-info',
                    data: {
                        orderUuid: orderUuid
                    }
                }).then(data => {
                    this.currentModel = Object.assign({}, {
                        unPaidAmount: '',
                        financialContractUuid: '',
                        financialContractNo: '',
                        orderUuid: '',
                        counterAccountName: '',
                        counterAccountNo: '',
                        counterBankName: '',
                        payWay: '0',
                        remark: '',
                    });
                    this.currentModel.unPaidAmount = data.unPaidAmount;
                    this.currentModel.financialContractNo = data.financialContractNo;
                    this.currentModel.financialContractUuid = data.financialContractUuid;
                    this.currentModel.orderUuid = orderUuid;
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            },
            fetchPayWayList: function() {
                ajaxPromise({
                    url: '/repayment-order/repayment/payWay',
                }).then(data => {
                    this.payWayList = data.payWay;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
			selectAccountNo: function(current) {
				var selectedAccountNo = current;
                if (selectedAccountNo == null) return;

                var { currentModel } = this;
                currentModel.counterAccountName = selectedAccountNo.counterAccountName;
                currentModel.counterAccountNo = selectedAccountNo.counterAccountNo;
                currentModel.counterBankName = selectedAccountNo.counterBankName;

                this.getCounterAccountName = false;
			},
			fetchRepaymentAccount: function(search, callback) {
                if (!search) {
                    callback([]);
                    return;
                }
				ajaxPromise({
                    url: '/repayment-order/search-account',
                    data: {
                        financialContractUuid: this.currentModel.financialContractUuid,
                        counterAccountName: search
                    }
                }).then(data => {
                    this.accountList = data.models || [];
                    callback(this.accountList);
                }).catch(message => {
                    MessageBox.open(message);
                });
			},
            fetchCashFlowList: function() {
                ajaxPromise({
                    url: '/repayment-order/search-cashflow',
                    data: {
                        financialContractUuid: this.currentModel.financialContractUuid,
                        counterAccountNo: this.currentModel.counterAccountNo,
                        orderUuid: this.orderUuid
                    }
                }).then(data => {
                    this.cashFlowList = data.models || [];
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            selectCashFlow: function() {
                var transactionAmount = 0;
                this.selectedCashFlows.forEach(item => {
                    transactionAmount += item.transactionAmount;
                });
                transactionAmount += this.selectedTransactionAmount;
                if (transactionAmount > this.currentModel.unPaidAmount) {
                    MessageBox.open('该流水金额大于需支付金额，暂不支持支付！');
                    return;
                }
                this.selectedCashFlowList = this.selectedCashFlowList.concat(this.selectedCashFlows);
                this.selectedCashFlows = [];
            },
			cancelCashFlow: function(row) {
                var index = this.selectedCashFlowList.findIndex(item => item.cashFlowUuid == row.cashFlowUuid );
                this.selectedCashFlowList.splice(index, 1);
				this.selectedCashFlows = [];
			},
            cancelAllCashFlow: function() {
                this.selectedCashFlowList = [];
            },
			submit: function() {
                if (!this.selectedCashFlowUuids.length) {
                    MessageBox.open('请选择流水');
                    return;
                }
                
                this.$refs.form.validate(valid => {
                    if (valid) {
                        MessageBox.open(`确定支付还款订单:${this.currentModel.orderUuid},支付金额:${this.selectedTransactionAmount}?`, '确认支付', [{
                            text: '取消',
                            handler: () => {
                                MessageBox.close();
                            }
                        }, {
                            text: '确定',
                            type: 'success',
                            handler: () => {
                				ajaxPromise({
                                    url: '/repayment-order/submit',
                                    data: {
                                        cashFlowUuidList: JSON.stringify(this.selectedCashFlowUuids),
                                        ...this.currentModel
                                    },
                                    type: 'post'
                                }).then(data => {
                                    MessageBox.close();
                                    MessageBox.once('closed', () => {
                                        this.$router.go(-1);
                                    });
                                    MessageBox.open(data.message);
                                }).catch(message => {
                                    MessageBox.open(message);
                                });
                            }
                        }])
                    }
                });
			}
		}
	}
</script>