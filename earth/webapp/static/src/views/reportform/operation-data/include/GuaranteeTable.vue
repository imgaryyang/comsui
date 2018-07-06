<template>
    <el-table
        stripe
        class="no-table-bottom-border"
        v-loading="dataSource.fetching"
        :data="dataSource.list">
        <el-table-column label="时间" inline-template>
            <div>{{ row.createDate | formatDate }}</div>
        </el-table-column>
        <el-table-column label="担保笔数" prop="count">
        </el-table-column>
        <el-table-column label="担保总额" inline-template>
            <div>{{ row.amount | formatMoney }}</div>
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