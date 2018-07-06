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
                                v-for="item in paymentInstitutionNames"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.sourceDocumentStatus"
                            size="small"
                            clearable
                            placeholder="凭证状态">
                            <el-option
                                v-for="item in auditResults"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.voucherType"
                            placeholder="凭证类型"
                            size="small"
                            clearable>
                            <el-option
                                v-for="item in voucherTypes"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <span class="item vertical-line"></span>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
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
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column
                        inline-template
                        :context="_self"
                        label="凭证编号">
                        <a :href="`${ctx}#/capital/voucher/remittance/${row.sourceDocumentUuid}/detail`">{{ row.sourceDocumentNo }}</a>
                    </el-table-column>
                    <el-table-column prop="bankName" label="来往机构"></el-table-column>
                    <el-table-column prop="outlierCounterPartyName" label="机构账户名"></el-table-column>
                    <el-table-column prop="outlierCounterPartyAccount" label="机构账户号"></el-table-column>
                    <el-table-column prop="voucherType" label="凭证类型"></el-table-column>
                    <!-- <el-table-column prop="sourceAccountSide" label="借贷标记"></el-table-column> -->
                    <el-table-column inline-template label="凭证金额">
                        <div>{{ row.bookingAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column prop="transcationGateway" label="交易网关"></el-table-column>
                    <el-table-column prop="createTime" label="创建时间"></el-table-column>
                    <el-table-column prop="voucherStatusModifyTime" label="状态变更时间"></el-table-column>
                    <el-table-column prop="sourceDocumentStatus" label="凭证状态"></el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <div class="pull-left" v-if="false">
                <el-button
                    style="background: #fff"
                    size="small"
                    @click="redirect(`${ctx}#/capital/voucher/third-pay/voucher-batch`)">凭证批次</el-button>
                <el-button
                    style="background: #fff"
                    size="small"
                    @click="redirect(`${ctx}#/capital/voucher/third-pay/channel`)">通道交易记录</el-button>
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
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox'),
        },
        data: function() {
            return {
                action: '/audit/remittance/list/remittanceVoucher',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    transcationGateway: '',
                    sourceDocumentStatus: '',
                    voucherType: '',
                },

                comboConds: {
                    paymentName: '',
                    paymentAccountNo: ''
                },

                financialContractQueryModels: [],
                paymentInstitutionNames: [],
                auditResults: [],
                voucherTypes: [],
            };
        },
        methods: {
            initialize: function() {
                return this.getOptions();
            },
            getOptions: function() {
                return ajaxPromise({
                    url: `/audit/remittance/options/remittanceVoucher`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.paymentInstitutionNames = data.paymentInstitutionNames || [];
                    this.auditResults = data.auditResults || [];
                    this.voucherTypes = data.voucherTypes || [];
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