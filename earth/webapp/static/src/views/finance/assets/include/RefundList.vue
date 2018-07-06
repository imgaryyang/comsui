<template>
    <div>

        <el-table :data="dataSource.list">
            <el-table-column label="退款单号" :context="_self" inline-template>
                <a @click.prevent="redirectRefund(row.uuid)" href="#">{{ row.refundOrderNo }}</a>
            </el-table-column>
            <el-table-column label="退款类型" prop="refundTypeName">
            </el-table-column>
            <el-table-column label="退款金额" inline-template>
                <el-popover
                        placement="top"
                        trigger="hover">
                    <template>
                        <div>本金：{{ row.principal | formatMoney }}</div>
                        <div>利息：{{ row.interest | formatMoney }}</div>
                        <div>贷款服务费：{{ row.serviceCharge | formatMoney }}</div>
                        <div>技术维护费：{{ row.maintenanceCharge | formatMoney }}</div>
                        <div>其他费用：{{ row.otherCharge | formatMoney }}</div>
                        <div>逾期罚息：{{ row.penaltyFee | formatMoney }}</div>
                        <div>逾期违约金：{{ row.latePenalty | formatMoney }}</div>
                        <div>逾期服务费：{{ row.lateFee | formatMoney }}</div>
                        <div>逾期其他费用：{{ row.lateOtherCost | formatMoney }}</div>
                    </template>
                    <span slot="reference">{{ row.amount | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column label="创建时间" inline-template>
                <div>
                    {{ row.createTime | formatDate('yyyy-MM-dd') }}
                </div>
            </el-table-column>
            <el-table-column label="状态变更时间" inline-template>
                <div>
                    {{ row.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}
                </div>
            </el-table-column>
            <el-table-column label="退款状态" prop="refundStatusName">
            </el-table-column>
            <el-table-column label="备注" prop="remark">
            </el-table-column>
        </el-table>
        <PageControl
                v-model="pageConds.pageIndex"
                :size="dataSource.size"
                :per-page-record-number="pageConds.perPageRecordNumber">
        </PageControl>
    </div>
</template>

<script>
    import PageControl from 'components/PageControl'
    import Pagination from 'mixins/Pagination'
    export default {
        mixins: [Pagination],
        compontents: {
            PageControl
        },
        props:{
            assetSetUuid: String
        },
        data: function () {
            return {
                action: `/refund/${this.assetSetUuid}/queryRecord`,
                pageConds:{
                    pageIndex:1,
                    perPageRecordNumber:12
                },
                autoload: false
            }
        },
        methods: {
            redirectRefund: function(uuid) {
                var url = `${this.ctx}#/capital/account/refund-bill/${uuid}/detail`
                location.assign(url)
            },
        },
        computed: {
            conditions: function () {
                return Object.assign({}, this.pageConds)
            },
        },
        watch:{
            assetSetUuid: function (cur) {
                if(!cur){
                    return
                }
                this.action = `/refund/${cur}/queryRecord`
                this.pageConds.pageIndex = 1
                this.fetch()
            }
        }
    }
</script>

<style scoped>

</style>