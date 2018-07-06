<template>
    <el-table
        stripe
        class="no-table-bottom-border"
        v-loading="dataSource.fetching"
        :data="dataSource.list">
        <el-table-column label="时间" inline-template>
            <div>{{ row.createDate | formatDate }}</div>
        </el-table-column>
        <el-table-column label="回购笔数" prop="count">
        </el-table-column>
        <el-table-column label="回购总额" inline-template>
            <div>{{ row.amount | formatMoney }}</div>
        </el-table-column>
        <el-table-column label="回购本金" inline-template>
            <div>{{ row.repurchasePrincipal | formatMoney }}</div>
        </el-table-column>
        <el-table-column label="回购利息" inline-template>
            <div>{{ row.repurchaseInterest | formatMoney }}</div>
        </el-table-column>
        <el-table-column label="回购罚息" inline-template>
            <div>{{ row.repurchasePenalty | formatMoney }}</div>
        </el-table-column>
        <el-table-column label="回购其他费用" inline-template>
            <div>{{ row.repurchaseOtherCharges | formatMoney }}</div>
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