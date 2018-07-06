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
                            v-model="queryConds.transcationGateway"
                            size="small"
                            clearable  
                            placeholder="交易网关">
                            <el-option
                                v-for="item in paymentInstitutionNameList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select 
                            v-model="queryConds.voucherLogStatus"
                            size="small" 
                            clearable 
                            placeholder="校验状态">
                            <el-option
                                v-for="item in verificationStatusList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select 
                            v-model="queryConds.voucherLogIssueStatus"
                            size="small" 
                            clearable 
                            placeholder="核销状态">
                            <el-option
                                v-for="item in voucherLogIssueStatusList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.startTime"
                                    :end-date="queryConds.endTime"
                                    :pickTime="true"
                                    :formatToMinimum="true"
                                    size="small"
                                    placeholder="起始时间">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2">
                            <div class="text-align-center color-dim">至</div>
                        </el-col>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.endTime"
                                    :start-date="queryConds.startTime"
                                    :pickTime="true"
                                    :formatToMaximum="true"
                                    size="small"
                                    placeholder="终止时间">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="凭证编号" value="voucherUuid"></el-option>
                            <el-option label="交易请求号" value="tradeUuid"></el-option>
                            <el-option label="清算账号" value="receivableAccountNo"></el-option>
                            <el-option label="机构账户名" value="paymentName"></el-option>
                            <el-option label="机构账户号" value="paymentAccountNo"></el-option>
                        </ComboQueryBox>
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
                    @sort-change="onSortChange"
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column 
                        inline-template
                        :context="_self"
                        label="凭证编号">
                        <a :href="`${ctx}#/capital/voucher/third-pay/${row.voucherUuid}/detail`">{{ row.voucherUuid }}</a>
                    </el-table-column>
                    <el-table-column prop="receivableAccountNo" label="清算账号"></el-table-column>
                    <el-table-column prop="paymentBank" label="来往机构"></el-table-column>
                    <el-table-column prop="paymentName" label="机构账户名"></el-table-column>
                    <el-table-column prop="paymentAccountNo" label="机构账户号"></el-table-column>
                    <el-table-column inline-template label="凭证金额">
                        <div>{{ row.transcationAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column prop="voucherSource" label="凭证来源"></el-table-column>
                    <el-table-column prop="transcationGateway" label="交易网关"></el-table-column>
                    <el-table-column prop="tradeUuid" label="交易请求号"></el-table-column>
                    <el-table-column prop="voucherLogStatus" label="校验状态" inline-template>
                        <span :class="{
                            'color-danger': row.voucherLogStatus === '校验成成功'
                        }">{{ row.voucherLogStatus }}</span>
                    </el-table-column>
                    <el-table-column prop="voucherLogIssueStatus" label="核销状态"></el-table-column>
                    <el-table-column prop="createTime" label="创建时间" sortable="custom"></el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <div class="pull-left">
                <el-button
                    style="background: #fff"
                    size="small"
                    @click="redirect(`${ctx}#/capital/voucher/third-pay/voucher-batch`)">凭证批次</el-button>
                <el-button
                    style="background: #fff"
                    size="small"
                    @click="redirect(`${ctx}#/capital/voucher/third-pay/channel`)">通道交易记录</el-button>
                <el-button
                    style="background: #fff"
                    size="small"
                    @click="redirect(`${ctx}#/capital/voucher/third-pay/history-voucher`)">历史凭证记录</el-button>
            </div>
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
    import { ajaxPromise, downloadFile, filterQueryConds } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox'),
        },
        data: function() {
            return {
                action: '/voucher/thirdPartyPayApi/paymentVoucherList',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    transcationGateway: '',
                    voucherLogStatus: '',
                    voucherLogIssueStatus: '',
                    startTime: '',
                    endTime: '',
                },

                comboConds: {
                    receivableAccountNo:'',
                    paymentName: '',
                    paymentAccountNo: ''
                },

                sortConds: {
                    sortField: '',
                    isAsc: '',
                },

                financialContractQueryModels: [],
                paymentInstitutionNameList: [],
                verificationStatusList: [],
                voucherLogIssueStatusList: [],
            };
        },
        methods: {
            initialize: function() {
                return this.getOptions();
            },
            getOptions: function() {
                return ajaxPromise({
                    url: '/voucher/thirdPartyPayApi/options'
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.paymentInstitutionNameList = data.paymentInstitutionNameList || [];
                    this.verificationStatusList = data.verificationStatusList || [];
                    this.voucherLogIssueStatusList = data.voucherLogIssueStatusList || [];
                }).catch(msg => {
                    MessageBox.open(msg);
                });
            },
            redirect: function(href) {
                location.assign(`${href}?t=${Date.now()}`);
            }
        }
    }
</script>