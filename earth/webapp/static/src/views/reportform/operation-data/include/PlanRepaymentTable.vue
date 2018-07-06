<template>
    <el-table
        stripe
        class="no-table-bottom-border"
        v-loading="dataSource.fetching"
        :data="dataSource.list">
        <el-table-column label="时间" inline-template>
            <div>{{ row.createDate | formatDate }}</div>
        </el-table-column>
        <el-table-column :label="`${tableHeaderTitle}计划还款笔数`" prop="count">
        </el-table-column>
        <el-table-column :label="`${tableHeaderTitle}计划还款总额`" inline-template>
            <div>{{ row.amount | formatMoney }}</div>
        </el-table-column>
        <el-table-column :label="`${tableHeaderTitle}计划还款本金`" inline-template>
            <div>{{ row.assetPrincipalValue | formatMoney }}</div>
        </el-table-column>
        <el-table-column :label="`${tableHeaderTitle}计划还款利息`" inline-template>
            <div>{{ row.assetInterestValue | formatMoney }}</div>
        </el-table-column>
        <el-table-column :label="`${tableHeaderTitle}计划还款贷款服务费`" inline-template>
            <div>{{ row.loanServiceFee | formatMoney }}</div>
        </el-table-column>
        <el-table-column :label="`${tableHeaderTitle}计划还款技术服务费`" inline-template>
            <div>{{ row.loanTechFee | formatMoney }}</div>
        </el-table-column>
        <el-table-column :label="`${tableHeaderTitle}计划还款其他费用`" inline-template>
            <div>{{ row.loanOtherFee | formatMoney }}</div>
        </el-table-column>
    </el-table>
</template>
<script>
    export default {
        props: {
            dataSource: {
                type: Object,
                default: () => {}
            },
            tableHeaderTitle: String,
        },
        data: function() {
            return {}
        }
    }
</script>