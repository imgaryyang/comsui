<template>
  <div>
    <el-table
        class="td-15-padding th-8-15-padding no-th-border"
        border
        stripe
        v-loading="dataSource.fetching"
        :data="dataSource.list">
        <el-table-column inline-template label="来源单号">
            <a :href="`${ctx}#/capital/payment-cash-flow/online-payment/${row.deductPlanDetailShowModel.deductPlanId}/detail`">{{ row.deductPlanDetailShowModel.deductPlanNo }}</a>
        </el-table-column>
        <el-table-column inline-template label="单据类型">
            <div>{{ row.beneficiaryAuditResult.systemBillTypeName }}</div>
        </el-table-column>
        <el-table-column inline-template label="对方账户">
            <el-popover
                trigger="hover"
                placement="top">
                <div>角色类型：--</div>
                <div>账户名：{{ row.deductPlanDetailShowModel.payerName }}</div>
                <div>账户号：{{ row.deductPlanDetailShowModel.bankAccountNo }}</div>
                <div>开户行：{{ row.deductPlanDetailShowModel.bankName }}</div>
                <div>所在省：{{ row.deductPlanDetailShowModel.bankProvince }}</div>
                <div>所在市：{{ row.deductPlanDetailShowModel.bankCity }}</div>
                <span slot="reference">{{ row.beneficiaryAuditResult.counterPartyAccountName }}</span>
            </el-popover>
        </el-table-column>
        <el-table-column
            inline-template
            label="交易金额">
            <div>{{ row.beneficiaryAuditResult.systemBillPlanAmount | formatMoney }}</div>
        </el-table-column>
        <el-table-column label="借贷标记" inline-template>
            <div>{{ row.deductPlanDetailShowModel.orderType }}</div>
        </el-table-column>
        <el-table-column inline-template label="通道请求号">
            <div>{{ row.beneficiaryAuditResult.tradeUuid }}</div>
        </el-table-column>
        <el-table-column
            inline-template
            label="状态变更时间">
            <div>{{ row.beneficiaryAuditResult.systemBillOccurDate | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
        </el-table-column>
        <el-table-column inline-template label="执行状态">
            <div>{{ row.beneficiaryAuditResult.transExcutionStatusName }}</div>
        </el-table-column>
        <el-table-column inline-template label="清算状态">
            <div>{{ row.deductPlanDetailShowModel.clearingStatus }}</div>
        </el-table-column>
    </el-table>
    <div class="ft">
        <PageControl
            v-model="pageConds.pageIndex"
            :size="dataSource.size"
            :per-page-record-number="pageConds.perPageRecordNumber">
        </PageControl>
    </div>
  </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import Pagination from 'mixins/Pagination';
    import { Loading } from 'element-ui';

    export default {
        mixins: [Pagination],
        props: {
            selected: Boolean,
            action: {
                type: String,
                required: true
            },
            autoload:{
                type: Boolean,
                default: false
            },
            auditJobUuid: String,
            value: {
                type: Boolean,
                default: false
            }
        },
        data: function() {
            return {
                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                },
            };
        },
        computed: {
            conditions: function() {
                return Object.assign({}, this.pageConds);
            }
        },
        watch: {
            auditJobUuid: function(current) {
                if(!current) {
                    this.dataSource.list=[];
                }
            },
            selected: function(current) {
                if (current && !this.dataSource.list.length) {
                    this.fetch();
                }
            },
            value: function(current) {
                if (this.selected && current) {
                    this.fetch();
                }
            },
        },
        methods: {
            onSuccess: function(data) {
                var d = this.parse(data);
                this.dataSource.list = d.list || [];
                this.dataSource.size = d.size || 0;
                this.dataSource.error = '';
            },
            onComplete: function() {
                if (this.value) {
                    this.$emit('input', false);
                }
                this.dataSource.fetching = false;
            },
        }
    }
</script>