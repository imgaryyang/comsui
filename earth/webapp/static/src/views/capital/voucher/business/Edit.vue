<style lang="sass">
	
</style>

<template>
	<div class="content">
		<div class="scroller" v-loading="fetching">
			<Breadcrumb :routes="[{title: '凭证管理'}, {title: '新增商户凭证'}]">
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
                        <el-form-item label="信托项目名称" prop="financialContractUuid" required>
                            <el-select
                                class="long"
                                v-model="model.financialContractUuid"
                                :remote-method="fetchFinacialContract"
                                :loading="financialContractList.loading"
                                remote
                                filterable>
                                <el-option
                                    v-for="item in financialContractList.list"
                                    :key="item.financialContractUuid"
                                    :label="item.financialContractNo"
                                    :value="item.financialContractUuid">
                                    <span>{{ item.financialContractName + '(' + item.financialContractNo + ')' }}</span>
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="凭证来源" prop="">
                            银行流水
                        </el-form-item>
                        <el-form-item label="凭证类型" prop="voucherType" required>
                            <el-select 
                                ref="voucherType"
                                class="long"
                                :value="model.voucherType"
                                @input="handleVoucherchange"
                                placeholder="凭证类型">
                                <el-option
                                    v-for="item in voucherTypes" 
                                    :label="item.value" 
                                    :value="item.key">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item 
                            label="机构账户号" 
                            prop="paymentAccountNo" 
                            required>
                            <el-autocomplete 
                                ref="paymentAccount"
                                v-model="model.paymentAccountNo"
                                class="long repayment-account-list"
                                custom-item="repayment-account-item" 
                                @select="handleSelectRepaymentAccount"
                                :fetch-suggestions="filterRepaymentAccount">
                            </el-autocomplete>
                            <el-button type="primary" @click="fetchRepaymentAccount">查询</el-button>
                        </el-form-item>
                        <el-form-item label="账户名称" prop="paymentName" required>
                            <el-input 
                                class="long" 
                                v-model="model.paymentName">
                            </el-input>
                        </el-form-item>
                        <el-form-item 
                            label="支付机构开户行" 
                            prop="paymentBank" 
                            required>
                            <el-input 
                                class="long" 
                                v-model="model.paymentBank">
                            </el-input>
                        </el-form-item>
                        <el-form-item 
                            label="付款流水号" 
                            prop="creditSerialNumber" 
                            required>
                            <el-input 
                                class="long"
                                v-model="model.creditSerialNumber">
                            </el-input>
                        </el-form-item>
                        <!-- <el-form-item label="原始凭证">
                            <el-upload
                                accept=".jpg,.png"
                                :default-file-list="[]"
                                :on-success="handleUpload"
                                action="${api}/">
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
                        </el-form-item> -->
                        <el-form-item 
                            label="备注" 
                            prop="remark">
                            <el-input 
                                class="long"
                                v-model="model.remark">
                            </el-input>
                        </el-form-item>
                    </div>

                    <div class="fieldset">
                        <el-form-item label="流水信息" class="form-item-legend">
                            <div class="supplement">（流水金额：{{ cashFlowAmount | formatMoney }}）</div>
                        </el-form-item>
                        <el-form-item 
                            label="选择流水" 
                            prop="cashFlowUuid"
                            required>
                            <el-select 
                                @click.native="handleFetchCashFlow" 
                                v-model="cashFlow.selectedCashFlow"
                                :loading="cashFlow.loading"
                                loading-text="正在加载数据"
                                class="long" 
                                placeholder="请选择流水" 
                                dropdown-class="richtext-dropdown">
                                <el-option
                                    v-for="item in cashFlow.list"
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
                            <el-button type="primary" @click="handleAddCashFlow">确定</el-button>
                        </el-form-item>
                        <div style="margin: 10px 15% 10px 53px;">
                            <PagingTable :data="cashFlow.finalSelectedCashFlow ? [cashFlow.finalSelectedCashFlow] : []">
                                <el-table-column prop="bankSequenceNo" label="流水号"></el-table-column>
                                <el-table-column inline-template label="交易金额">
                                    <div> {{ row.transactionAmount | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column prop="counterAccountName" label="账户名称"></el-table-column>
                                <el-table-column prop="counterAccountNo" label="机构账户号"></el-table-column>
                                <el-table-column prop="counterBankName" label="支付机构开户行"></el-table-column>
                                <el-table-column inline-template label="入账时间">
                                    <div>
                                        {{ row.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss') }}
                                    </div>
                                </el-table-column>
                                <el-table-column prop="remark" label="摘要"></el-table-column>
                                <el-table-column inline-template label="操作" :context="_self">
                                    <a href="#" @click.prevent="handleCancelSelectedCashFlow">取消</a>
                                </el-table-column>
                            </PagingTable>
                        </div>
                    </div>

                    <div class="fieldset">
                        <el-checkbox v-model="transTemDepDoc" style="margin: 10px 15% 10px 53px;">多余资金存入滞留款</el-checkbox>
                    </div>

                    <div class="fieldset">
                        <el-form-item label="业务信息" class="form-item-legend">
                            <div class="supplement">（凭证金额：{{ voucherAmount | formatMoney }}）</div>
                        </el-form-item>
                        <el-form-item label="业务明细表">
                            <el-upload
                                style="width: auto; display: inline-block;"
                                :before-upload="handleBeforeUpload"
                                action="/">
                                <el-button size="small" type="primary">导入</el-button>
                            </el-upload>
                            <el-button type="primary" size="small" @click="handleDownload">下载模版</el-button>
                        </el-form-item>
                        <div style="margin: 10px 15% 10px 53px;">
                            <PagingTable :data="businessInfo.fileList">
                                <el-table-column inline-template label="表格名称">
                                    <span>{{ row.name }}</span>
                                </el-table-column>
                                <el-table-column inline-template :context="_self" label="操作">
                                    <a href="#" @click.prevent="handleDeleteFile($index)">删除</a>
                                </el-table-column>
                            </PagingTable>
                        </div>
                        <div v-if="businessInfo.dataList.length > 0" style="margin: 10px 15% 10px 53px;">
                            <PagingTable :data="businessInfo.dataList">
                                <el-table-column 
                                    v-for="(label, key) in businessInfo.headers" 
                                    :prop="key"
                                    :label="label">
                                </el-table-column>
                            </PagingTable>
                        </div>
                        
                    </div>

                    <div class="fieldset" style="margin-left: 53px;">
                        <el-button style="min-width: 80px;" type="primary" @click="$router.go(-1)">取消</el-button>
                        <el-button style="min-width: 80px;" type="primary" @click="handleSubmit">提交</el-button>
                    </div>
                </el-form>
            </div>
		</div>
	</div>
</template>

<script>
	import { ajaxPromise, downloadFile } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import { mapState } from 'vuex';
    import '../include/RepaymentAccountItem';

    export default {
        components:{
            PagingTable: require('views/include/PagingTable'),
        },
        data: function() {
            var validateUnique = (rule, value, callback) => {
                ajaxPromise({
                    url: '/voucher/business/check',
                    data: {
                        creditSerialNo: value
                    }
                }).then(() => {
                    callback();
                }).catch(message => {
                    callback(new Error(message))
                });
            };

            return {
                model: {
                    financialContractNo: '',
                    financialContractUuid: '',
                    voucherType: '',
                    paymentAccountNo: '',
                    paymentName: '',
                    paymentBank: '',
                    appAccountUuid: '',
                    creditSerialNumber: '',
                    remark: '',
                    cashFlowUuid: '',
                },
                rules: {
                    financialContractUuid: { required: true, message: ' '},
                    voucherType: { required: true, message: ' '},
                    paymentAccountNo: { required: true, message: ' '},
                    paymentName: { required: true, message: ' '},
                    paymentBank: { required: true, message: ' '},
                    cashFlowUuid: { required: true, message: ' ', trigger: 'blur' },
                    creditSerialNumber: [
                        { required: true, message: ' '},
                        { validator: validateUnique, trigger: 'blur' }
                    ]
                },

                voucherTypes: [],
                accountList: [],
                financialContractList: {
                    list: [],
                    loading: false,
                },

                cashFlow: {
                    list: [],
                    finalSelectedCashFlow: null,
                    selectedCashFlow: null,
                    loading: false
                },

                businessInfo: {
                    headers: {},
                    dataList: [],
                    fileList: [],
                    voucherAmount: ''
                },

                transTemDepDoc: false,
            }
        },
        computed: {
            cashFlowAmount: function() {
                return this.cashFlow.finalSelectedCashFlow ? this.cashFlow.finalSelectedCashFlow.transactionAmount : '';
            },
            voucherAmount: function() {
                // return this.businessInfo.voucherAmount;
                return this.cashFlowAmount;
            },
        },
        beforeMount: function() {
            ajaxPromise({
                url: `/voucher/business/voucher-type`,
            }).then(data => {
                this.voucherTypes = data.voucherTypeList || [];
            });
        },
        deactivated: function() {
            this.$refs.form.resetFields();

            this.businessInfo = {
                dataList: [],
                fileList: [],
                headers: {},
                voucherAmount: ''
            };

            this.cashFlow = {
                list: [],
                finalSelectedCashFlow: null,
                selectedCashFlow: null,
                loading: false
            };
        },
        methods: {
            fetchFinacialContract: function(keyword) {
                if (!keyword) {
                    this.financialContractList.list = [];
                    return;
                }

                this.financialContractList.loading = true;

                ajaxPromise({
                    url: `/voucher/business/search-financial`,
                    data: {
                        financialContractNo: keyword
                    }
                }).then(data => {
                    this.financialContractList.list = data.fcList || [];
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.financialContractList.loading = false;
                })
            },
            filterRepaymentAccount: function(keyword, callback) {
                if (this.accountList.length == 0 || !keyword) {
                    callback([]);
                    return;
                }

                var result = this.accountList.filter(account => {
                    return account.paymentAccountNo.includes(keyword);
                });

                callback(result);
            },
            fetchRepaymentAccount: function() {
                ajaxPromise({
                    url: `/voucher/business/search-account`,
                    data: {
                        accountNo: this.model.paymentAccountNo,
                        financialContractUuid: this.model.financialContractUuid
                    }
                }).then(data => {
                    data.data.forEach(item => {
                        item.value = item.paymentAccountNo;
                    });

                    this.accountList = data.data || [];

                    if (this.accountList) {
                        this.$refs.paymentAccount.showSuggestions(this.model.paymentAccountNo);
                    }
                }).catch(message => {
                    MessageBox.open(message)
                });
            },
            handleFetchCashFlow: function(value) {
                if (this.cashFlow.loading) return;

                this.cashFlow.loading = true;

                ajaxPromise({
                    url: `/voucher/business/search-cashflow`,
                    data: {
                        financialContractUuid: this.model.financialContractUuid,
                        accountNo: this.model.paymentAccountNo,
                    }
                }).then(data => {
                    this.cashFlow.list = data.cashFlowList || [];
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.cashFlow.loading = false;
                });
            },
            handleSelectFinacialContract: function(selected) {
                this.model.financialContractNo = selected.financialContractNo;
                this.model.financialContractUuid = selected.financialContractUuid;
            },
            handleSelectRepaymentAccount: function(selected) {
                this.model.paymentAccountNo = selected.paymentAccountNo;
                this.model.paymentBank = selected.paymentBank;
                this.model.paymentName = selected.paymentName;
            },
            handleSubmit: function() {
                this.$refs.form.validate(valid => {
                    if (!valid) return;
                    if (this.cashFlowAmount !== this.voucherAmount) {
                        MessageBox.open('流水金额应与凭证金额相等');
                        return;
                    }
                    ajaxPromise({
                        url: `voucher/business/add`,
                        type: 'post',
                        data: {
                            ...this.model,
                            cashFlowAmount: this.cashFlowAmount,
                            voucherAmount: this.voucherAmount,
                            amountDetail: JSON.stringify(this.businessInfo.dataList),
                            transTemDepDoc: +this.transTemDepDoc,
                        }
                    }).then(() => {
                        MessageBox.once('close', () => location.assign(`${this.ctx}#/capital/voucher/business`));
                        MessageBox.open('创建成功');
                    }).catch((message) => {
                        MessageBox.open(message);
                    });

                });
            },
            handleAddCashFlow: function() {
                if (!this.cashFlow.selectedCashFlow) return;
                this.$set(this.cashFlow, 'finalSelectedCashFlow', Object.assign({}, this.cashFlow.selectedCashFlow));
                this.model.cashFlowUuid = this.cashFlow.finalSelectedCashFlow.cashFlowUuid;
            },
            handleCancelSelectedCashFlow: function() {
                this.cashFlow.finalSelectedCashFlow = null;
                this.model.cashFlowUuid = '';
            },
            handleDownload: function() {
                const voucherType = this.model.voucherType;

                if (voucherType == null || voucherType === '') {
                    MessageBox.open('请先选择凭证类型');
                    return;
                }

                downloadFile('voucher/business/export', { voucherType })
            },
            import: function(files, callback) {
                const voucherType = this.model.voucherType;

                if (voucherType == null || voucherType === '') {
                    MessageBox.open('请先选择凭证类型');
                    return false;
                }

                var formData = new FormData();

                formData.append('voucherType', voucherType);

                files.forEach((file, index) => {
                    formData.append('file' + index, file);
                });

                return ajaxPromise({
                    url: `voucher/business/import`,
                    type: 'POST',
                    processData: false,
                    contentType: false,
                    data: formData
                }).then(data => {
                    this.businessInfo.dataList = data.dataList;
                    this.businessInfo.amountDetail = data.amountDetail;
                    this.businessInfo.voucherAmount = data.voucherAmount;
                    this.businessInfo.headers = data.headers || {};
                    callback();
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleDeleteFile: function(index) {
                var fileList = this.businessInfo.fileList;

                var p = fileList.slice(0, index);
                var n = fileList.slice(index + 1);

                this.import(p.concat(n), () => fileList.splice(index, 1));
            },
            handleBeforeUpload: function(file) {
                var fileList = this.businessInfo.fileList;
                
                this.import(fileList.concat(file), () => fileList.push(file));

                return false;
            },
            handleVoucherchange: function(value) {
                var group = {
                    0: 0,
                    3: 0,
                    7: 0,
                    1: 1,
                    2: 2
                };

                var prevVoucherType = this.model.voucherType;
                var currVoucherType = value;

                if (this.businessInfo.dataList.length == 0 || currVoucherType === '') {
                    this.model.voucherType = currVoucherType;
                    return;
                }

                if (group[prevVoucherType] !== group[currVoucherType]) {
                    var pressSuccess = false;

                    var cancel = () => {
                        pressSuccess = false;
                        MessageBox.close();
                    };

                    var success = () => {
                        this.model.voucherType = currVoucherType;
                        this.businessInfo = {
                            headers: {},
                            dataList: [],
                            fileList: [],
                            voucherAmount: '',
                        };

                        pressSuccess = true;

                        MessageBox.close();
                    };

                    MessageBox.once('close', () => {
                        if (pressSuccess === false) {
                            this.model.voucherType = '';
                            setTimeout(() => {
                                this.model.voucherType = prevVoucherType;
                            }, 10);
                        }
                    });

                    MessageBox.open('凭证类型模版不一致，将会删除导入数据，确定？', null, [{
                        text: '取消',
                        handler: cancel
                    }, {
                        text: '确定',
                        type: 'success',
                        handler: success
                    }]);
                    return;
                }

                this.model.voucherType = currVoucherType;
            }
        }
    }
</script>
