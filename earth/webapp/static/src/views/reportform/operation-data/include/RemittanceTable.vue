<template>
    <el-table
        stripe
        class="no-table-bottom-border"
        v-loading="dataSource.fetching"
        :data="dataSource.list">
        <el-table-column label="时间" inline-template>
            <div>{{ row.createDate | formatDate }}</div>
        </el-table-column>
        <el-table-column label="计划订单笔数" prop="applicationCount">
        </el-table-column>
        <el-table-column label="计划订单总额" inline-template>
            <div>{{ row.applicationAmount | formatMoney }}</div>
        </el-table-column>
        <el-table-column label="放款单笔数" prop="planCount">
        </el-table-column>
        <el-table-column label="放款单总额" inline-template>
            <div>{{ row.planAmount | formatMoney }}</div>
        </el-table-column>
        <el-table-column label="实际放款笔数" prop="actualCount">
        </el-table-column>
        <el-table-column label="实际放款总额" inline-template>
            <div>{{ row.actualAmount | formatMoney }}</div>
        </el-table-column>
        <el-table-column label="导入资产包总额" inline-template>
            <div>{{ row.assetAmount | formatMoney }}</div>
        </el-table-column>
        <el-table-column label="导入资产包本金总额" inline-template>
            <div>{{ row.assetPrincipal | formatMoney }}</div>
        </el-table-column>
        <el-table-column label="导入资产包利息总额" inline-template>
            <div>{{ row.assetInterest | formatMoney }}</div>
        </el-table-column>
        <el-table-column label="导入资产包贷款服务费总额" inline-template>
            <div>{{ row.assetLoanServiceFee | formatMoney }}</div>
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
        },
        data: function() {
            return {}
        }
    }
</script>