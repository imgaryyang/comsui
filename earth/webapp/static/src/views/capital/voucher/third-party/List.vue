<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <list-cascader
                            clearable
                            size="small"
                            :collection="financialContractQueryModels"
                            v-model="queryConds.financialContractUuids"
                            :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
                    </el-form-item>
                    <el-form-item>
                        <el-select 
                            v-model="queryConds.repaymentType"
                            multiple
                            size="small" 
                            placeholder="代扣类型">
                            <el-select-all-option 
                                :options="repaymentType">
                            </el-select-all-option>
                            <el-option
                                v-for="(value, label) in repaymentType"
                                :label="label"
                                :value="value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.deductVoucherSource"
                            multiple
                            size="small" 
                            placeholder="凭证来源">
                            <el-select-all-option 
                                :options="voucherSource">
                            </el-select-all-option>
                            <el-option
                                v-for="(value, label) in voucherSource"
                                :label="label"
                                :value="value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select 
                            v-model="queryConds.paymentChannel"
                            multiple
                            size="small"  
                            placeholder="第三方通道">
                            <el-select-all-option 
                                :options="repayChannel">
                            </el-select-all-option>
                            <el-option
                                v-for="(value, label) in repayChannel"
                                :label="label"
                                :value="value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select 
                            v-model="queryConds.voucherStatus"
                            multiple
                            size="small" 
                            placeholder="凭证状态">
                            <el-select-all-option 
                                :options="voucherStatus">
                            </el-select-all-option>
                            <el-option
                                v-for="(value, label) in voucherStatus"
                                :label="label"
                                :value="value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-input
                            :value="queryConds.specialAccountNo"
                            @change.native="queryConds.specialAccountNo = $event.target.value.trim()"
                            size="small" 
                            placeholder="专户账号">
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input
                            :value="queryConds.payerName"
                            @change.native="queryConds.payerName = $event.target.value.trim()"
                            size="small" 
                            placeholder="账户姓名">
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input
                            :value="queryConds.payerBankAccountNo"
                            @change.native="queryConds.payerBankAccountNo = $event.target.value.trim()"
                            size="small" 
                            placeholder="机构账户号">
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table 
                    class="no-table-bottom-border"
                    stripe
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column 
                        inline-template
                        :context="_self"
                        label="凭证编号">
                        <a :href="`${ctx}#/capital/voucher/third-party/${row.journalVoucherUuid}/detail`">{{ row.voucherCode }}</a>
                    </el-table-column>
                    <el-table-column prop="specialAccountNo" label="专户账号"></el-table-column>
                    <el-table-column prop="payerBankName" label="往来机构名称"></el-table-column>
                    <el-table-column prop="payerName" label="账户姓名"></el-table-column>
                    <el-table-column prop="payerBankAccountNo" label="机构账户号"></el-table-column>
                    <el-table-column inline-template label="凭证金额">
                        <div>{{ row.voucherAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column prop="settlementNo" label="清算单号"></el-table-column>
                    <el-table-column prop="deductType" label="代扣类型"></el-table-column>
                    <el-table-column prop="voucherSource" label="凭证来源"></el-table-column>
                    <el-table-column prop="thirdPartChannel" label="第三方通道"></el-table-column>
                    <el-table-column prop="voucherStatus" label="凭证状态"></el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <div class="pull-right">
                <PageControl 
                    v-model="pageConds.pageIndex"
                    :size="dataSource.size"
                    :per-page-record-number="pageConds.perPageRecordNumber">
                </PageControl>
            </div>
        </div>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise } from 'assets/javascripts/util';

    export default {
        mixins: [Pagination, ListPage],
        data: function() {
            return {
                action: '/voucher/thirdParty/query',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    repaymentType: [],
                    deductVoucherSource: [],
                    paymentChannel: [],
                    voucherStatus: [],
                    specialAccountNo: '',
                    payerName: '',
                    payerBankAccountNo: '',
                },

                financialContractQueryModels: [],
                repayChannel: [],
                repaymentType: [],
                voucherSource: [],
                voucherStatus: [],
            };
        },
        methods: {
            initialize: function() {
                return this.getOptions();
            },
            getOptions: function() {
                return ajaxPromise({
                    url: '/voucher/thirdParty/optionData'
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.repayChannel = data.repayChannel || [];
                    this.repaymentType = data.repaymentType || [];
                    this.voucherSource = data.voucherSource || [];
                    this.voucherStatus = data.voucherStatus || [];

                    var queryConds = this.queryConds;

                    queryConds.paymentChannel = Object.keys(this.repayChannel).map(key => this.repayChannel[key]);
                    queryConds.repaymentType = Object.keys(this.repaymentType).map(key => this.repaymentType[key]);
                    queryConds.deductVoucherSource = Object.keys(this.voucherSource).map(key => this.voucherSource[key]);
                    queryConds.voucherStatus = Object.keys(this.voucherStatus).map(key => this.voucherStatus[key]);
                    
                }).catch(msg => {
                    console.log(msg);
                })
            }
        }
    }
</script>