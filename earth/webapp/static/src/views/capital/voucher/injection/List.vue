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
                            v-model="queryConds.customerTypeList"
                            placeholder="状态"
                            size="small"
                            multiple>
                            <el-select-all-option
                                :options="customerParameters">
                            </el-select-all-option>
                            <el-option
                                v-for="item in customerParameters"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <DateTimePicker
                            v-model="queryConds.createdTime"
                            placeholder="创建时间"
                            size="small">
                        </DateTimePicker>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="对方户名" value="deductApplicationNo"></el-option>
                            <el-option label="对方账号" value="customerName"></el-option>
                            <el-option label="对方开户行" value="bankName"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table
                    stripe
                    class="no-table-bottom-border"
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column label="凭证编号" prop="voucherNo" inline-template>
                        <a :href="`${ctx}#/capital/voucher/withdrawals/${row.voucherUuid}/detail`">{{row.voucherNo }}</a>
                    </el-table-column>
                    <el-table-column :label="$utils.locale('financialContract')" prop="contractName">
                    </el-table-column>
                    <el-table-column label="对方户名" prop="customer">
                    </el-table-column>
                    <el-table-column label="对方账号" prop="customer">
                    </el-table-column>
                    <el-table-column label="对方开户行" prop="customer">
                    </el-table-column>
                    <el-table-column label="凭证金额" prop="remainAmount" inline-template>
                        <div>{{ row.remainAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="创建时间" prop="createdTime" inline-template>
                        <div>{{ row.createdTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="状态变更时间" prop="createdTime" inline-template>
                        <div>{{ row.createdTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="凭证状态" prop="tmpDepositStatus">
                    </el-table-column>
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
    import MessageBox from 'components/MessageBox';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox')
        },
        data: function() {
            return {
                action: '',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    customerTypeList: [],
                    tmpDepositUseStatus: [],
                    createdTime: ''
                },
                comboConds: {

                },
                financialContractQueryModels: [],
                customerParameters: [],
                tmpDepositUseStatusParameters: [],
            }
        },
        methods: {

        }
    }
</script>