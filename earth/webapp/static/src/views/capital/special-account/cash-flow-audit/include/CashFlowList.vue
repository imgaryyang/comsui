<style lang='sass'>
    @import '~assets/stylesheets/base.scss';

    .cash-flow-list {
        background: #fff;
        padding: 20px;

        .hd {
            margin: 0;
            font-size: 14px;
            margin-bottom: 10px;
        }

        .ft {
            line-height: 40px;
            background-color: #f5f5f5;
            margin-top: -1px;
            height: 42px;
            border: 1px solid #dedede;

            .add-bill {
                text-align: center;
                display: inline;
            }
        }
        .el-table .el-table__header {
            min-width: 100%;
        }
        .el-table .el-table__body {
            min-width: 100%;
        }
        .el-table .el-table__body td {
            border-bottom: 1px solid #dedede!important;

            & > .cell {
                overflow: initial;
            }

            .error {
                position: absolute;
                right: 100%;
                margin-right: 10px;
                width: 150px;
                padding: 4px 5px;
                background: #fccac1;
                border: 1px solid #f79686;
                color: #d34b4b;
                font-weight: 400;

                &:before {
                    content: '';
                    display: block;
                    background: url(~assets/images/icons.png) -25px -262px no-repeat;
                    height: 15px;
                    width: 15px;
                    float: left;
                    margin-right: 4px;
                }

                &:after {
                    content: '';
                    display: block;
                    height: 10px;
                    width: 10px;
                    background: #fccac1;
                    border: 1px solid #f79686;
                    border-left: none;
                    border-bottom: none;
                    position: absolute;
                    right: 3px;
                    top: 50%;
                    @include transform(rotate(45deg) translateY(-50%));
                    @include transform-origin(bottom, right);
                }
            }

        }
    }
</style>

<template>
    <tr>
        <td colspan="10">
            <div class="cash-flow-list" v-loading="fetching">
                <h3 class="hd">
                    <span style="color:#666;">存疑金额：</span>
                    <span class="color-danger doubt-amount">{{ doubtAmount | formatMoney }}</span>
                </h3>
                <div class="bd">
                    <PagingTable :data="bills">
                        <el-table-column label="充值单号" prop="showData.depositNo"></el-table-column>
                        <el-table-column label="账户编号" prop="showData.virtualAccountNo"></el-table-column>
                        <el-table-column label="客户姓名" prop="showData.customerName"></el-table-column>
                        <el-table-column label="客户类型" prop="showData.customerTypeMsg"></el-table-column>
                        <el-table-column label="贷款合同编号" prop="showData.contractNo"></el-table-column>
                        <el-table-column :label="$utils.locale('financialContract.name')" prop="showData.financialContractName"></el-table-column>
                        <el-table-column label="账户余额" prop="balance" inline-template>
                            <div>{{ row.balance | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column label="充值金额" :context="_self" inline-template :min-width="90">
                            <div style="position: relative;">
                                <template v-if="isRecharge && row.isCreated">
                                    <el-input ref="depositAmount" size="small" v-model="bills[$index].depositAmount"></el-input>
                                </template>
                                <span class="color-success" v-else>{{ row.depositAmount | formatMoney }}</span>
                            </div>
                        </el-table-column>
                        <el-table-column label="备注" :context="_self" inline-template :min-width="90">
                            <div style="position: relative;">
                                <template v-if="isRecharge && row.isCreated">
                                    <el-input ref="remark" size="small" v-model="bills[$index].remark"></el-input>
                                </template>
                                <span v-else>{{ row.remark }}</span>
                            </div>
                        </el-table-column>
                        <el-table-column label="状态" prop="sourceDocumentStatusEnum"></el-table-column>
                        <el-table-column v-if="isRecharge" :context="_self" inline-template label="操作">
                            <div>
                                <button v-if="row.isCreated" @click="handleSubmit(row)" class="btn btn-basic btn-success">提交</button>
                                <button v-else @click="handleDisabled(row, $index)" class="btn btn-basic btn-danger">作废</button>
                            </div>
                        </el-table-column>
                    </PagingTable>
                </div>
                <div 
                    class="ft clearfix text-align-center"
                    v-if=" ifElementGranted('create-deposit-receipt')
                        && isRecharge 
                        && doubtAmount > 0 
                        && (appArrivalItem && appArrivalItem.canBeDeposited)">
                    <a 
                        class="add-bill" 
                        href="#"
                        @click.prevent="handleCreate">
                        +  新增充值账单
                    </a>
                </div>
            </div>
        </td>
    </tr>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import RequestError from 'assets/javascripts/RequestError';
    import MessageBox from 'components/MessageBox';

    export default {
        components: {
            PagingTable: require('views/include/PagingTable'),
        },
        props: {
            appArrivalItem: Object,
            triggerByDepositButton: Boolean,
        },
        data: function() {
            return {
                active: false,
                fetching: false,

                error: new RequestError({message: 'test RequestError'}),

                bills: []
            }
        },
        computed: {
            isRecharge: function() {
                // return this.triggerByDepositButton || this.bills.length < 1;
                return this.triggerByDepositButton;
            },
            doubtAmount: function() {
                if (!this.appArrivalItem) return '';
                var transactionAmount = (+this.appArrivalItem.transactionAmount);
                // var issuedAmount = (+this.get('issuedAmount')); // submit之后会变
                var sumDepositAmount = this.bills.map(item => $.isNumeric(item.depositAmount) ? item.depositAmount : 0).reduce((prev, next) => (+prev) + (+next), 0);
                return transactionAmount - sumDepositAmount;
            }
        },
        watch: {
            active: function(current) {
                if (current) {
                    this.fetch();
                }
            }
        },
        methods: {
            appendBill: function(bill) {
                var d = Object.assign({ isCreated: true }, bill);
                this.bills.push(d);
            },
            fetch: function() {
                if (!this.appArrivalItem) return;

                this.fetching = true;

                ajaxPromise({
                    url: `/capital/account-manager/cash-flow-audit/show-deposit-result`,
                    data: {
                        cashFlowUuid: this.appArrivalItem.cashFlowUuid
                    },
                    type: 'post'
                }).then(data => {
                    this.bills = data.depositResults;
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            },
            toastErrorTip: function(ref, message) {
                var el = $(ref.$el);
                var errorEl = el.prev('.error');
                var width = message.length * 12 + 30; // 12: 字体大小， 30: icon
                if (errorEl.length < 1) {
                    errorEl = $('<span class="error">' + message + '</span>');
                    el.before(errorEl);
                } else {
                    errorEl.html(message);
                }
                errorEl.width(width).addClass('anim-fadeIn');
                var timer = el.data('timer');
                if (timer) {
                    clearTimeout(timer);
                }
                timer = setTimeout(function() {
                    errorEl.fadeOut(500, function() {
                        errorEl.remove();
                    });
                }, 4000);
                el.data('timer', timer);
            },
            validate: function(row) {
                var $depositAmount = this.$refs.depositAmount;
                var $remark = this.$refs.remark;
                var depositAmount = row.depositAmount;
                var remark = row.remark;
                var flag = true;

                if (!$.isNumeric(depositAmount)) {
                    flag = false;
                    this.toastErrorTip($depositAmount, '请输入数字');
                }

                if (!remark) {
                    flag = false;
                    this.toastErrorTip($remark, '请输入备注');
                }

                return flag;
            },
            handleDisabled: function(row, $index) {
                MessageBox.open('即将作废该充值账单，是否继续？', null, [{
                    text: '否',
                    handler: () => MessageBox.close()
                }, {
                    text: '是',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: `/capital/account-manager/cash-flow-audit/deposit-cancel`,
                            type: 'post',
                            data: {
                                ...row,
                                cashFlowUuid: this.appArrivalItem.cashFlowUuid
                            }
                        }).then(data => {
                            this.$emit('disabled-bill', data.cashFlow);
                            this.bills.splice($index, 1);
                            MessageBox.close();
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                }])
            },
            handleSubmit: function(row) {
                if (!this.validate(row)) return;

                ajaxPromise({
                    url: '/capital/account-manager/cash-flow-audit/deposit',
                    type: 'post',
                    data: {
                        remark: row.remark,
                        depositAmount: row.depositAmount,
                        customerUuid: row.customerUuid,
                        financialContractUuid: row.financialContractUuid,
                        cashFlowUuid: this.appArrivalItem.cashFlowUuid
                    }
                }).then(data => {
                    this.$emit('created-bill', data.cashFlow);
                    MessageBox.once('close', () => {
                        this.fetch();
                    });
                    MessageBox.open('充值单提交成功');
                }).catch(requestError => {
                    if (requestError.code == -6005) {
                        var htm = [
                            '<div style="margin: 30px;">',
                            '流水银行卡',
                            '<span class="color-danger"> ',
                            this.appArrivalItem.counterAccountName,
                            ' ',
                            this.appArrivalItem.counterBankName,
                            ' ',
                            this.appArrivalItem.counterAccountNo,
                            ' </span>',
                            '与客户账户中银行卡信息不符，请修正。',
                            '</div>'
                        ].join('');

                        MessageBox.open(htm, null, [{
                            text: '确定',
                            type: 'success',
                            handler: () => {
                                MessageBox.close();
                                location.assign(`${this.ctx}#/capital/account/virtual-acctount/${requestError.response.data.virtualAccountUuid}/detail`);
                            }
                        }]);
                    } else {
                        MessageBox.open(requestError);
                    }
                });
            },
            handleCreate: function() {
                this.$emit('create-bill', this);
            }
        }
    }
</script>