<style lang="sass" scoped>
    #editActiveVoucher {
        .financial-no-list,
        .repayment-account-list,
        .contract-no-list {
            li {
                line-height: 22px;
                padding: 5px;

                .subtitle {
                    font-size: 12px;

                    .supplement {
                        color: #999;
                    }
                }
            }
        }
        .richtext-dropdown {
            .el-select-dropdown__item {
                height: auto;
                .title {
                    line-height: 22px;
                    .subtitle {
                        font-size: 12px;

                        .supplement {
                            color: #999;
                        }
                    }
                    .color-red {
                        color: red;
                    }
                    .color-gray {
                        color: gray;
                    }
                }
            }
        }
    }
    .el-select-dropdown.is-multiple .el-select-dropdown__item.selected::after{
        top: 25px;
    }
</style>

<template>
    <div class="content" id="editActiveVoucher">
        <div class="scroller">
            <Breadcrumb :routes="[
                { title: '凭证管理' },
                { title: '主动付款凭证新增' }
            ]">
            </Breadcrumb>

            <div>
                <el-form
                    ref="form"
                    :model="model"
                    :rules="rules"
                    label-width="120px"
                    class="sdf-form">
                    <div class="fieldset">
                        <el-form-item label="凭证信息" class="form-item-legend"></el-form-item>
                        <el-form-item :label="$utils.locale('financialContract')" prop="financialContractNo" required>
                            <el-autocomplete
                                v-model="model.financialContractNo"
                                class="long financial-no-list"
                                custom-item="financial-no-item"
                                :fetch-suggestions="fetchfinancialContract"
                                @select="handleSelect"
                                >
                            </el-autocomplete>
                        </el-form-item>
                        <el-form-item
                            label="客户姓名"
                            prop="customerName"
                            required>
                            <el-input
                                class="long"
                                v-model="model.customerName" @blur="fetchContractNo">
                            </el-input>
                        </el-form-item>
                        <el-form-item label="贷款合同编号" prop="contractNo" required>
                            <el-select
                                v-model="model.contractNo"
                                class="long contract-no-list"
                                placeholder="请选择贷款合同"
                                dropdown-class="richtext-dropdown"
                                @change="getRepaymentPlan"
                                multiple
                                >
                                <el-option
                                    v-for="item in contractList"
                                    :label="item.contractNo"
                                    :value="item">
                                    <div class="title">
                                        <span>{{ item.contractNo }}</span>
                                        <div class="subtitle">
                                            {{ item.financialContractName }}
                                            <span class="supplement">({{ item.financialContractNo }})</span>
                                        </div>
                                    </div>
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="凭证来源" prop="" required>
                            银行流水
                        </el-form-item>
                        <el-form-item label="凭证类型" prop="voucherType" required>
                            <el-select
                                class="long"
                                v-model="model.voucherType"
                                placeholder="凭证类型"
                                clearable>
                                <el-option
                                    v-for="item in voucherTypes"
                                    :label="item.value"
                                    :value="item.key">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="还款方账号" prop="paymentAccountNo" required>
                            <el-autocomplete
                                v-model="model.paymentAccountNo"
                                class="long repayment-account-list"
                                custom-item="repayment-account-item"
                                @select="selectAccountNo"
                                :fetch-suggestions="fetchRepaymentAccount">
                            </el-autocomplete>
                        </el-form-item>
                        <el-form-item
                            label="还款方户名"
                            prop="paymentName"
                            required>
                            <el-input
                                class="long"
                                v-model="model.paymentName">
                            </el-input>
                        </el-form-item>
                        <el-form-item
                            label="账户开户行"
                            prop="paymentBank"
                            required>
                            <el-input
                                class="long"
                                v-model="model.paymentBank">
                            </el-input>
                        </el-form-item>
                        <el-form-item
                            label="付款流水号"
                            prop="bankTransactionNo"
                            required>
                            <el-input
                                class="long"
                                v-model="model.bankTransactionNo"
                                >
                            </el-input>
                            &nbsp;
                            <el-button type="primary" @click="fetchCashFlow" :disabled="!disabledSelectCashFlow">查询</el-button>
                        </el-form-item>
                    </div>

                    <div class="fieldset">
                        <el-form-item label="流水信息" class="form-item-legend">
                            <div class="supplement">（凭证金额：{{ voucherTotalAmount | formatMoney }}）</div>
                        </el-form-item>
                        <el-form-item
                            label="选择流水"
                            prop="selectCashFlow"
                            required>
                            <el-select class="long" placeholder="请选择流水" v-model="selectCashFlow" dropdown-class="richtext-dropdown">
                                <el-option
                                    v-for="item in optionCashFlow"
                                    :label="item.bankSequenceNo"
                                    :value="item"
                                    >
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
                            <el-button type="primary" @click="addCashFlow" :disabled="disableAddCashFlow">确定</el-button>
                        </el-form-item>
                        <div style="margin: 10px 15% 10px 53px;">
                            <el-table
                                :data="cashFlowTableList"
                                class="td-15-padding th-8-15-padding no-th-border"
                                border>
                                <el-table-column prop="bankSequenceNo" label="流水号"></el-table-column>
                                <el-table-column inline-template label="交易金额">
                                    <div> {{ row.transactionAmount | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column prop="counterAccountName" label="对方户名"></el-table-column>
                                <el-table-column prop="counterAccountNo" label="对方账号"></el-table-column>
                                <el-table-column prop="counterBankName" label="对方开户行"></el-table-column>
                                <el-table-column inline-template label="入账时间">
                                    <div>
                                        {{ row.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss') }}
                                    </div>
                                </el-table-column>
                                <el-table-column prop="remark" label="摘要"></el-table-column>
                                <el-table-column inline-template label="操作">
                                    <a href="#" @click.prevent="cancelCashFlow">取消</a>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                    <div class="fieldset">
                        <el-checkbox v-model="transTemDepDoc" style="margin: 10px 15% 10px 53px;">多余资金存入滞留款</el-checkbox>
                    </div>
<!-- start -->
                    <div class="fieldset">
                        <el-form-item label="业务信息" class="form-item-legend">
                            <div class="supplement">（明细金额：{{ detailTotalAmount | formatMoney }} ）</div>
                        </el-form-item>
                        <el-form-item label="还款计划" prop="selectAssetInfo" required>
                            <el-select
                                class="long"
                                placeholder="请选择还款计划"
                                v-model="selectAssetInfo"
                                dropdown-class="richtext-dropdown">
                                <el-option
                                    v-for="item in optionAssetInfoModel"
                                    :label="item.singleLoanContractNo"
                                    :value="item"
                                    :disabled="item.repaymentExecutionState == '扣款中'">
                                    <div class="title">
                                       {{ item.singleLoanContractNo}}
                                        <span class="pull-right" :class = "item.repaymentExecutionState =='扣款中' ? 'color-gray' : 'color-red'">
                                        {{ item.repaymentExecutionState}}
                                        </span>
                                        <div class="subtitle">
                                            计划金额:{{ item.repaymentPlanAmount}};逾期费用: {{ item.overdueAmount}}
                                            <span class="supplement">({{ item.assetRecycleDate | formatDate}})</span>
                                        </div>
                                    </div>
                                </el-option>
                            </el-select>
                            &nbsp;
                            <el-button type="primary" @click="addAssetInfoModel" :disabled="disableAddAssetInfo">添加</el-button>
                        </el-form-item>
                        <div style="margin: 10px 15% 10px 53px;">
                            <el-table
                                :data="assetInfoModelList"
                                class="td-15-padding th-8-15-padding no-th-border"
                                border>
                                <el-table-column inline-template label="还款单号">
                                    <a href="`${ctx}/assets#/finance/assets/${row.assetSetUuid}/detail`">{{ row.singleLoanContractNo }}</a>
                                </el-table-column>
                                <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo">
                                </el-table-column>
                                <el-table-column prop="assetRecycleDate" label="计划还款日期" inline-template>
                                    <div>{{ row.assetRecycleDate | formatDate}}</div>
                                </el-table-column>
                                <el-table-column prop="principalValue" label="还款本金" inline-template>
                                    <div>
                                        <div v-if="row.editting"><el-input size="small" v-model="assetInfoModelList[$index].principalValue"></el-input></div>
                                        <div v-else>{{ row.principalValue | formatMoney }}</div>
                                    </div>
                                </el-table-column>
                                <el-table-column prop="interestValue" label="还款利息" inline-template>
                                    <div>
                                        <div v-if="row.editting"><el-input size="small" v-model="assetInfoModelList[$index].interestValue"></el-input></div>
                                        <div v-else>{{ row.interestValue | formatMoney }}</div>
                                    </div>
                                </el-table-column>
                                <el-table-column prop="serviceCharge" label="贷款服务费" inline-template>
                                    <div>
                                        <div v-if="row.editting"><el-input size="small" v-model="assetInfoModelList[$index].serviceCharge"></el-input></div>
                                        <div v-else>{{ row.serviceCharge | formatMoney }}</div>
                                    </div>
                                </el-table-column>
                                <el-table-column prop="maintenanceCharge" label="技术维护费" inline-template>
                                    <div>
                                        <div v-if="row.editting"><el-input size="small" v-model="assetInfoModelList[$index].maintenanceCharge"></el-input></div>
                                        <div v-else>{{ row.maintenanceCharge | formatMoney }}</div>
                                    </div>
                                </el-table-column>
                                <el-table-column prop="otherCharge" label="其他费用" inline-template>
                                    <div>
                                        <div v-if="row.editting"><el-input size="small" v-model="assetInfoModelList[$index].otherCharge"></el-input></div>
                                        <div v-else>{{ row.otherCharge | formatMoney }}</div>
                                    </div>
                                </el-table-column>
                                <el-table-column prop="overduePenaltyFee" label="逾期罚息" inline-template>
                                    <div>
                                        <div v-if="row.editting"><el-input size="small" v-model="assetInfoModelList[$index].overduePenaltyFee"></el-input></div>
                                        <div v-else>{{ row.overduePenaltyFee | formatMoney }}</div>
                                    </div>
                                </el-table-column>
                                <el-table-column prop="overdueObligationFee" label="逾期违约金" inline-template>
                                    <div>
                                        <div v-if="row.editting"><el-input size="small" v-model="assetInfoModelList[$index].overdueObligationFee"></el-input></div>
                                        <div v-else>{{ row.overdueObligationFee | formatMoney }}</div>
                                    </div>
                                </el-table-column>
                                <el-table-column prop="overdueServiceCharge" label="逾期服务费" inline-template>
                                    <div>
                                        <div v-if="row.editting"><el-input size="small" v-model="assetInfoModelList[$index].overdueServiceCharge"></el-input></div>
                                        <div v-else>{{ row.overdueServiceCharge | formatMoney }}</div>
                                    </div>
                                </el-table-column>
                                <el-table-column prop="overdueOtherCharge" label="逾期其他费用" inline-template>
                                    <div>
                                        <div v-if="row.editting"><el-input size="small" v-model="assetInfoModelList[$index].overdueOtherCharge"></el-input></div>
                                        <div v-else>{{ row.overdueOtherCharge | formatMoney }}</div>
                                    </div>
                                </el-table-column>
                                <el-table-column prop="temporaryDepositDocCharge" label="滞留金额" inline-template>
                                    <div>
                                        <div v-if="row.editting"><el-input size="small" v-model="assetInfoModelList[$index].temporaryDepositDocCharge"></el-input></div>
                                        <div v-else>{{ row.temporaryDepositDocCharge | formatMoney }}</div>
                                    </div>
                                </el-table-column>
                                <el-table-column inline-template label="操作">
                                    <div>
                                        <template v-if="row.editting">
                                            <a href="#" @click.prevent="saveChanges($index)">保存</a>
                                            <a href="#" @click.prevent="cancelChanges($index)">取消</a>
                                        </template>
                                        <a v-else href="#" @click.prevent="deleteChanges($index)">删除</a>
                                    </div>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
<!-- end -->
                    <div class="fieldset">
                        <el-form-item label="原始凭证">
                            <el-upload
                                accept=".jpg,.png"
                                :default-file-list="defaultFiles"
                                :on-success="onUploadFileSuccess"
                                :action="`${api}/voucher/active/create/upload-file`">
                                <el-button class="button-multimedia">
                                    <div :style="{
                                        'max-width': '75px',
                                        'overflow': 'hidden',
                                        'text-overflow': 'ellipsis',
                                        }">
                                       点击上传
                                    </div>
                                </el-button>
                                <span style="margin-left: 10px; font-size: 12px; color: #999;" slot="tip">只能上传jpg,png文件</span>
                            </el-upload>
                        </el-form-item>
                        <el-form-item label="备注" prop="comment">
                            <el-input
                                class="long"
                                type="textarea"
                                placeholder="请输入备注"
                                v-model="model.comment">
                            </el-input>
                        </el-form-item>
                    </div>
                    <div class="fieldset" style="margin-left: 53px;">
                        <el-button style="min-width: 80px;" type="primary" @click="$router.go(-1)">取消</el-button>
                        <!-- <el-button style="min-width: 80px;" type="primary" @click="save" :disabled="disabledSaveButton">保存</el-button> -->
                        <el-button style="min-width: 80px;" type="primary" @click="submit" :disabled="diabledSubmitButton">提交</el-button>
                    </div>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
    import Pagination, { extract } from 'mixins/Pagination';
    import { ajaxPromise } from 'assets/javascripts/util';
    import Vue from 'vue';
    import { mapState } from 'vuex';
    import MessageBox from 'components/MessageBox';
    import { Upload } from 'element-ui';
    import Decimal from 'decimal.js';

    Vue.component('financial-no-item',{
        functional: true,
        render: function (h, ctx) {
            var item = ctx.props.item;
            return h('li', ctx.data, [
                h('div', { attrs: { class: 'title' } }, [item.financialContractName ,'（',item.financialContractNo,'）'])
            ]);
        },
        props: {
            item: { type: Object, required: true }
        }
    });
    Vue.component('repayment-account-item', {
        functional: true,
        render: function(h,ctx) {
            var item = ctx.props.item;
            return h('li', Object.assign({},ctx.data,{style:{padding:'5px'}}), [
                h('div', { style: { 'line-height': '22px' } }, [item.paymentAccountNo]),
                h('div', { style: { 'font-size': '12px','line-height': '22px'} }, [
                    [item.paymentName],
                    h('span', { style: { color: '#999' } }, [`(${item.paymentBank})`])
                ])
            ]);
        },
        props: {
            item: { type: Object, required: true}
        }
    });

    export default {
        components: {
            [Upload.name]: Upload
        },
        data: function() {
            return {
                model: {
                    financialContractNo: '',
                    customerName: '',
                    financialContractUuid: '',
                    contractNo: [],
                    voucherType: '',
                    paymentAccountNo: '',
                    paymentName: '',
                    paymentBank: '',
                    bankTransactionNo: '',
                    cashFlowUuid: '',
                    assetInfoModels: '',
                    resourceUuids: [],
                    comment: '',
                    voucherAmount: '',
                    repaymentPlanAmount: '',
                },

                rules: {
                    //contractNo: { required: true, message: ' '},
                    voucherType: { required: true, message: ' '},
                    paymentAccountNo: { required: true, message: ' '},
                    paymentName: { required: true, message: ' '},
                    paymentBank: { required: true, message: ' '},
                    bankTransactionNo: { required: true, message: ' '},
                    cashFlowUuid: { required: true, message: ' '},
                },

                financialContractList: [],
                contractList: [],
                accountList: [],
                cashFlowList: [],
                cashFlowTableList: [],

                selectAssetInfo: {},
                selectCashFlow: {},
                optionAssetInfoModelList: [],
                assetInfoModelList: [],

                defaultFiles: [],
                currentItem:{},

                transTemDepDoc: false,
            }
        },
        beforeMount: function() {
            const { $store } = this;
            $store.dispatch('getVoucherTypes');
        },
        activated: function() {
            if (this.$route.params.voucherNo) {
                this.fetch(this.$route.params.voucherNo);
            }
            //贷款合同详情入口
            var query = this.$route.query;
            if (query.contractNo != undefined) {
               this.model.contractNo = query.contractNo;
            }
            this.currentItem = {};
        },
        computed: {
            ...mapState({
                voucherTypes: state => state.financialContract.voucherTypes,
            }),
            disableAddAssetInfo: function() {
                var { model, selectAssetInfo, isEdittingState } = this;
                return isEdittingState ? true : typeof(selectAssetInfo) == "object" && selectAssetInfo.singleLoanContractNo != undefined ? false : true;
            },
            isEdittingState: function() {
                var lastItem = this.assetInfoModelList.slice(this.assetInfoModelList.length-1);

                return lastItem[0] != undefined  && lastItem[0].editting;
            },
            hasSelectAssetInfo: function() {
                return this.assetInfoModelList.length != 0;
            },
            disabledSelectCashFlow: function(){
                return this.model.paymentAccountNo && this.model.paymentName && this.model.bankTransactionNo;
            },
            disableAddCashFlow: function() {
                return this.cashFlowList.length == 0 || this.hasSelectCashFlow;
            },
            hasSelectCashFlow: function() {
                return this.model.cashFlowUuid != '';
            },
            optionCashFlow: function() {
                var res = this.cashFlowList.filter(item => {
                    return this.model.cashFlowUuid != item.cashFlowUuid;
                })
                return res;
            },
//start
            // assetInfoModelList: function() {
            //     return this.selectedContract ? this.selectedContract.assetInfoModelList : [];
            // },
            optionAssetInfoModel: function() {
                var res = this.optionAssetInfoModelList.filter(item => {
                    var index = this.assetInfoModelList.findIndex(item2 => item2.singleLoanContractNo == item.singleLoanContractNo);
                    return index == -1;
                });
                return res;
            },
            // selectedContract: function() {
            //     var index = this.contractList.findIndex(item => item.contract.contractNo == this.model.contractNo);
            //     return this.contractList[index];
            // },
//end

            selectedAccountNo: function() {
                var index = this.accountList.findIndex(item => item.paymentAccountNo == this.model.paymentAccountNo);
                return this.accountList[index];
            },

            detailTotalAmount: function() {
                var totalAmount = new Decimal(0);
                this.assetInfoModelList.forEach(item => {
                    var overdueAmount = isNaN(parseFloat(item.overdueAmount))? 0 : parseFloat(item.overdueAmount);
                    var principalValue = isNaN(parseFloat(item.principalValue)) ? 0 : parseFloat(item.principalValue);
                    var interestValue = isNaN(parseFloat(item.interestValue)) ? 0 : parseFloat(item.interestValue);
                    var serviceCharge = isNaN(parseFloat(item.serviceCharge)) ? 0 : parseFloat(item.serviceCharge);
                    var maintenanceCharge = isNaN(parseFloat(item.maintenanceCharge)) ? 0 : parseFloat(item.maintenanceCharge);
                    var otherCharge = isNaN(parseFloat(item.otherCharge)) ? 0 : parseFloat(item.otherCharge);
                    var overduePenaltyFee = isNaN(parseFloat(item.overduePenaltyFee)) ? 0 : parseFloat(item.overduePenaltyFee);
                    var overdueObligationFee = isNaN(parseFloat(item.overdueObligationFee)) ? 0 : parseFloat(item.overdueObligationFee);
                    var overdueServiceCharge = isNaN(parseFloat(item.overdueServiceCharge)) ? 0 : parseFloat(item.overdueServiceCharge);
                    var overdueOtherCharge = isNaN(parseFloat(item.overdueOtherCharge)) ? 0 : parseFloat(item.overdueOtherCharge);
                    
                    totalAmount = totalAmount.add(principalValue).add(interestValue).add(serviceCharge).add(maintenanceCharge).add(otherCharge).add( overduePenaltyFee).add(overdueObligationFee).add(overdueServiceCharge ).add(overdueOtherCharge);
                });

                return totalAmount.toNumber();
            },
            voucherTotalAmount: function() {
                var index = this.cashFlowList.findIndex(item => item.cashFlowUuid == this.model.cashFlowUuid);
                return index == -1 ? 0 : this.cashFlowList[index].transactionAmount;
            },
            diabledSubmitButton: function() {
                return this.isEdittingState || !this.hasSelectCashFlow || !this.hasSelectAssetInfo;
            },
            disabledSaveButton: function() {
                var { model, isEdittingState } = this;
                var isdisabled = model.contractNo == ''|| model.voucherType == ''|| model.paymentAccountNo == ''|| model.paymentName == ''|| model.paymentBank == ''|| model.bankTransactionNo == '' || isEdittingState;
                return isdisabled;
            }
        },
        watch: {
            'model.contractNo': function(current, previous) {
                var { model, selectedContract } = this;
                if (model.voucherType == 5
                    && selectedContract
                    && selectedContract.contractAccount != null) {
                    model.paymentAccountNo = selectedContract.contractAccount.payAcNo;
                    model.paymentName = selectedContract.contractAccount.payerName;
                    model.paymentBank = selectedContract.contractAccount.bank;
                }
            },
            'model.voucherType': function(current, previous) {
                var { model, selectedContract } = this;
                if (model.voucherType == 5
                    && selectedContract
                    && selectedContract.contractAccount != null) {
                    model.paymentAccountNo = selectedContract.contractAccount.payAcNo;
                    model.paymentName = selectedContract.contractAccount.payerName;
                    model.paymentBank = selectedContract.contractAccount.bank;
                }
            },
            'model.paymentAccountNo': {
                immediate: true,
                handler: function(cur) {
                    setTimeout(() => {
                        var { model, selectedAccountNo } = this;
                        if (selectedAccountNo == null) return;
                        model.paymentAccountNo = selectedAccountNo.paymentAccountNo;
                        model.paymentName = selectedAccountNo.paymentName;
                        model.paymentBank = selectedAccountNo.paymentBank;
                    }, 0);
                }
            }
        },
        methods: {
            selectAccountNo: function(cur) {
                var selectedAccountNo = cur;

                var { model} = this;
                if (selectedAccountNo == null) return;
                model.paymentAccountNo = selectedAccountNo.paymentAccountNo;
                model.paymentName = selectedAccountNo.paymentName;
                model.paymentBank = selectedAccountNo.paymentBank;
            },
            fetch: function(voucherNo) {
                ajaxPromise({
                    url: `/voucher/active/detail/${voucherNo}/data`
                }).then(data => {
                    console.log(data);
                    this.model = data.detail;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            fetchfinancialContract: function(search, callback) {
                if (!search) {
                    callback([]);
                    return;
                }
                ajaxPromise({
                    url: `/voucher/active/search-financial`,
                    data: {
                        financialContractNo: this.model.financialContractNo
                    }
                }).then(data => {
                    data.fcList.forEach(item => {
                        item.value = item.financialContractName + '(' + item.financialContractNo + ')';
                    })
                    this.financialContractList = data.fcList || [];

                    callback(this.financialContractList);
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleSelect: function(item){
                this.currentItem = item ;
                this.model.financialContractUuid = item.financialContractUuid;
                this.fetchContractNo();
            },
            fetchContractNo: function() {
                ajaxPromise({
                    url: `/voucher/active/search-contract`,
                    data: {
                        customerName: this.model.customerName,
                        financialContractUuid: this.currentItem.financialContractUuid,
                        financialContractName: this.currentItem.financialContractName,
                        financialContractNo: this.currentItem.financialContractNo
                    }
                }).then(data => {
                    this.contractList = data.contractlList || [];

                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            fetchRepaymentAccount: function(search, callback) {
                if (!search) {
                    callback([]);
                    return;
                }
                ajaxPromise({
                    url: `/voucher/active/search-account`,
                    data: {
                        paymentAccountNo: this.model.paymentAccountNo,
                        contractNo: this.model.contractNo
                    }
                }).then(data => {
                    data.models.forEach(item => {
                        item.value = item.paymentAccountNo;
                    });
                    this.accountList = data.models || [];

                    callback(this.accountList);
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            addAssetInfoModel: function() {
                var { selectAssetInfo, model } = this;

                if (typeof(selectAssetInfo) == "undefined" || typeof(selectAssetInfo) == "object" && selectAssetInfo.singleLoanContractNo == undefined) return;

                var item = Object.assign({temporaryDepositDocCharge: 0}, selectAssetInfo);

                item._principalValue = item.principalValue;
                item._interestValue = item.interestValue;
                item._serviceCharge = item.serviceCharge;
                item._maintenanceCharge = item.maintenanceCharge;
                item._otherCharge = item.otherCharge;
                item._overduePenaltyFee = item.overduePenaltyFee;
                item._overdueObligationFee = item.overdueObligationFee;
                item._overdueServiceCharge = item.overdueServiceCharge;
                item._overdueOtherCharge = item.overdueOtherCharge;
                item._temporaryDepositDocCharge = item.temporaryDepositDocCharge;
                item.editting = true;

                this.assetInfoModelList.push(item);
                this.selectAssetInfo = {};
            },
            saveChanges: function(index) {
                var row = this.assetInfoModelList[index];
                row.editting = false;
            },
            cancelChanges: function(index) {
                var row = this.assetInfoModelList[index];

                row.principalValue = row._principalValue;
                row.interestValue = row._interestValue;
                row.serviceCharge = row._serviceCharge;
                row.maintenanceCharge = row._maintenanceCharge;
                row.otherCharge = row._otherCharge;
                row.overduePenaltyFee = row._overduePenaltyFee;
                row.overdueObligationFee = row._overdueObligationFee;
                row.overdueServiceCharge = row._overdueServiceCharge;
                row.overdueOtherCharge = row._overdueOtherCharge;
                row.temporaryDepositDocCharge = row._temporaryDepositDocCharge;

                row.editting = false;
            },
            deleteChanges: function(index) {
                this.assetInfoModelList.splice(index, 1);
            },
            fetchCashFlow: function(value) {
                ajaxPromise({
                    url: `/voucher/active/search-cashflow`,
                    data: {
                        paymentAccountNo: this.model.paymentAccountNo,
                        paymentName: this.model.paymentName,
                        bankTransactionNo: this.model.bankTransactionNo
                    }
                }).then(data => {
                    this.cashFlowList = data.cashFlowList || [];
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            addCashFlow: function() {
                var { cashFlowList, model, selectCashFlow, cashFlowTableList, hasSelectCashFlow } = this;

                if ( typeof(selectCashFlow) == "undefined" || typeof(selectCashFlow) == "object" && selectCashFlow.cashFlowUuid == undefined ||  hasSelectCashFlow) return;

                model.cashFlowUuid = selectCashFlow.cashFlowUuid;
                cashFlowTableList.push(selectCashFlow);
                this.selectCashFlow = {};
            },
            cancelCashFlow: function() {
                this.model.cashFlowUuid = '';
                this.cashFlowTableList = [];
            },
            getRepaymentPlan: function(){
                ajaxPromise({
                    url: `/voucher/active/search-asset`,
                    data: {
                        financialContractUuid: this.currentItem.financialContractUuid,
                        contractInfoModels: JSON.stringify(this.model.contractNo)
                    }
                }).then(data =>{
                    this.optionAssetInfoModelList = data.assetList;
                }).catch(message =>{
                    MessageBox.open(message);
                })
            },
            onUploadFileSuccess: function(response, file, fileList) {
                if (response.code != 0) return;

                var fileUUid = '';
                fileUUid = response.data.uuid;

                this.model.resourceUuids.push(fileUUid);
                this.defaultFiles = fileList;

                console.log(this.defaultFiles);
            },
            formatModel: function() {
                var { voucherTotalAmount, detailTotalAmount, model } = this;

                model.voucherAmount = voucherTotalAmount;
                model.repaymentPlanAmount = detailTotalAmount;
                model.resourceUuids = JSON.stringify(model.resourceUuids);
                model.assetInfoModels = JSON.stringify(this.assetInfoModelList);
                model.transTemDepDoc = +this.transTemDepDoc;

                return model;
            },
            submit: function() {
                var { voucherTotalAmount, detailTotalAmount, model } = this;

                // if (voucherTotalAmount != detailTotalAmount ) {
                //     MessageBox.open('明细金额必须等于凭证金额');
                //     return;
                // }

                this.$refs.form.validate(valid => {
                    if (valid) {
                        ajaxPromise({
                            url: `/voucher/active/submit`,
                            type: 'post',
                            data: this.formatModel(),
                        }).then(data => {
                            MessageBox.once('close',() => {
                                location.assign(this.ctx + `#/capital/voucher/active`);
                            });
                            MessageBox.open('提交成功');
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    } else {
                       $('.el-form-item__error').first().parent()[0].scrollIntoView();
                    }
                })
            },
            save: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        if (this.isEdittingState) return;
                        ajaxPromise({
                            url: `/voucher/active/save`,
                            type: 'post',
                            data: this.formatModel(),
                        }).then(data => {
                            MessageBox.once('close',() => {
                                location.assign(this.ctx + `#/capital/voucher/active`);
                            });
                            MessageBox.open('保存成功');
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    } else {
                        $('.el-form-item__error').first().parent()[0].scrollIntoView();
                    }
                })
            }
        },
        deactivated: function() {
            this.$refs.form.resetFields();
            this.cashFlowTableList = [];
            this.assetInfoModelList = [];
            this.model.cashFlowUuid = '';
            this.defaultFiles = [];
        }
    }
</script>
