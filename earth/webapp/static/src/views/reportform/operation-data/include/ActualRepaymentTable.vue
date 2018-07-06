<template>
    <div>
        <el-table
            stripe
            class="no-table-bottom-border"
            v-if="['实际还款', '提前还款', '部分还款'].includes(tableHeaderTitle)"
            v-loading="dataSource.fetching"
            key="stb"
            :data="showData.list">
            <el-table-column label="时间" inline-template>
                <div>{{ row.createDate | formatDate }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}笔数`" prop="count">
            </el-table-column>
            <el-table-column :label="`${showTitle}总额`" inline-template>
                <div>{{ row.amount | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}本金`" inline-template>
                <div>{{ row.loanAssetPrincipal | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}利息`" inline-template>
                <div>{{ row.loanAssetInterest | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}贷款服务费`" inline-template>
                <div>{{ row.loanServiceFee | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}技术维护费`" inline-template>
                <div>{{ row.loanTechFee | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}其他费用`" inline-template>
                <div>{{ row.loanOtherFee | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期罚息`" inline-template>
                <div>{{ row.overdueFeePenalty | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期违约金`" inline-template>
                <div>{{ row.overdueFeeObligation | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期服务费`" inline-template>
                <div>{{ row.overdueFeeService | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期其他费用`" inline-template>
                <div>{{ row.overdueFeeOther | formatMoney }}</div>
            </el-table-column>
        </el-table>
        <el-table
            stripe
            class="no-table-bottom-border"
            v-if="tableHeaderTitle == '线下支付单'"
            key="xxzfd"
            v-loading="dataSource.fetching"
            :data="showData.list">
            <el-table-column label="时间" inline-template>
                <div>{{ row.createDate | formatDate }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}笔数`" prop="count">
            </el-table-column>
            <el-table-column :label="`${showTitle}还款总额`" inline-template>
                <div>{{ row.amount | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}还款本金`" inline-template>
                <div>{{ row.loanAssetPrincipal | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}还款利息`" inline-template>
                <div>{{ row.loanAssetInterest | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}还款贷款服务费`" inline-template>
                <div>{{ row.loanServiceFee | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}还款技术维护费`" inline-template>
                <div>{{ row.loanTechFee | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}其他费用`" inline-template>
                <div>{{ row.loanOtherFee | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期罚息`" inline-template>
                <div>{{ row.overdueFeePenalty | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期违约金`" inline-template>
                <div>{{ row.overdueFeeObligation | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期服务费`" inline-template>
                <div>{{ row.overdueFeeService | formatMoney }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期其他费用`" inline-template>
                <div>{{ row.overdueFeeOther | formatMoney }}</div>
            </el-table-column>
        </el-table>
        <el-table
            stripe
            class="no-table-bottom-border"
            v-if="tableHeaderTitle == '线上实收'"
            v-loading="dataSource.fetching"
            key="xsss"
            :data="showData.list">
            <el-table-column label="时间" inline-template>
                <div>{{ row.createDate | formatDate }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}笔数`" prop="count" inline-template>
                <el-popover
                    @show="fetchOnlineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ cashFlowChannelTypeList[item.cashFlowChannelType].value }}实收笔数：{{ item.count }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.count }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}金额`" inline-template>
                <el-popover
                    @show="fetchOnlineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ cashFlowChannelTypeList[item.cashFlowChannelType].value }}实收金额：{{ item.amount | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.amount | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}本金`" inline-template>
                <el-popover
                    @show="fetchOnlineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ cashFlowChannelTypeList[item.cashFlowChannelType].value }}实收本金：{{ item.loanAssetPrincipal | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.loanAssetPrincipal | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}利息`" inline-template>
                <el-popover
                    @show="fetchOnlineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ cashFlowChannelTypeList[item.cashFlowChannelType].value }}实收利息：{{ item.loanAssetInterest | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.loanAssetInterest | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}贷款服务费`" inline-template>
                <el-popover
                    @show="fetchOnlineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list" >
                                {{ cashFlowChannelTypeList[item.cashFlowChannelType].value }}实收贷款服务费：{{ item.loanServiceFee | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.loanServiceFee | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}技术维护费`" inline-template>
                <el-popover
                    @show="fetchOnlineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ cashFlowChannelTypeList[item.cashFlowChannelType].value }}实收技术维护费：{{ item.loanTechFee | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.loanTechFee | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}其他费用`" inline-template>
                <el-popover
                    @show="fetchOnlineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ cashFlowChannelTypeList[item.cashFlowChannelType].value }}实收其他费用：{{ item.loanOtherFee | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.loanOtherFee | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期罚息`" inline-template>
                <el-popover
                    @show="fetchOnlineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ cashFlowChannelTypeList[item.cashFlowChannelType].value }}实收逾期罚息：{{ item.overdueFeePenalty | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.overdueFeePenalty | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期违约金`" inline-template>
                <el-popover
                    @show="fetchOnlineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ cashFlowChannelTypeList[item.cashFlowChannelType].value }}实收逾期违约金：{{ item.overdueFeeObligation | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.overdueFeeObligation | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期服务费`" inline-template>
                <el-popover
                    @show="fetchOnlineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ cashFlowChannelTypeList[item.cashFlowChannelType].value }}实收逾期服务费：{{ item.overdueFeeService | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.overdueFeeService | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期其他费用`" inline-template>
                <el-popover
                    @show="fetchOnlineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ cashFlowChannelTypeList[item.cashFlowChannelType].value }}实收逾期其他费用：{{ item.overdueFeeOther | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.overdueFeeOther | formatMoney }}</span>
                </el-popover>
            </el-table-column>
        </el-table>
        <el-table
            stripe
            class="no-table-bottom-border"
            v-if="tableHeaderTitle == '线下实收'"
            key="xxss"
            v-loading="dataSource.fetching"
            :data="showData.list">
            <el-table-column label="时间" inline-template>
                <div>{{ row.createDate | formatDate }}</div>
            </el-table-column>
            <el-table-column :label="`${showTitle}笔数`" inline-template>
                <el-popover
                    @show="fetchOfflineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ journalVoucherTypeList[item.journalVoucherType].value }}笔数：{{ item.count }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.count }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}金额`" inline-template>
                <el-popover
                    @show="fetchOfflineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ journalVoucherTypeList[item.journalVoucherType].value }}金额：{{ item.amount | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.amount | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}本金`" inline-template>
                <el-popover
                    @show="fetchOfflineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ journalVoucherTypeList[item.journalVoucherType].value }}本金：{{ item.loanAssetPrincipal | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.loanAssetPrincipal | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}利息`" inline-template>
                <el-popover
                    @show="fetchOfflineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ journalVoucherTypeList[item.journalVoucherType].value }}利息：{{ item.loanAssetInterest | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.loanAssetInterest | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}贷款服务费`" inline-template>
                <el-popover
                    @show="fetchOfflineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ journalVoucherTypeList[item.journalVoucherType].value }}贷款服务费：{{ item.loanServiceFee | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.loanServiceFee | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}技术维护费`" inline-template>
                <el-popover
                    @show="fetchOfflineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ journalVoucherTypeList[item.journalVoucherType].value }}技术维护费：{{ item.loanTechFee | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.loanTechFee | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}其他费用`" inline-template>
                <el-popover
                    @show="fetchOfflineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ journalVoucherTypeList[item.journalVoucherType].value }}其他费用：{{ item.loanOtherFee | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.loanOtherFee | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期罚息`" inline-template>
                <el-popover
                    @show="fetchOfflineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ journalVoucherTypeList[item.journalVoucherType].value }}逾期罚息：{{ item.overdueFeePenalty | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.overdueFeePenalty | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期违约金`" inline-template>
                <el-popover
                    @show="fetchOfflineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ journalVoucherTypeList[item.journalVoucherType].value }}逾期违约金：{{ item.overdueFeeObligation | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.overdueFeeObligation | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期服务费`" inline-template>
                <el-popover
                    @show="fetchOfflineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ journalVoucherTypeList[item.journalVoucherType].value }}逾期服务费：{{ item.overdueFeeService | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.overdueFeeService | formatMoney }}</span>
                </el-popover>
            </el-table-column>
            <el-table-column :label="`${showTitle}逾期其他费用`" inline-template>
                <el-popover
                    @show="fetchOfflineSetStatistics(row.createDate)"
                    trigger="hover"
                    placement="top">
                    <div>
                        <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                        <template v-else>
                            <div v-for="item in row.statistics.list">
                                {{ journalVoucherTypeList[item.journalVoucherType].value }}逾期其他费用：{{ item.overdueFeeOther | formatMoney }}
                            </div>
                        </template>
                    </div>
                    <span slot="reference">{{ row.overdueFeeOther | formatMoney }}</span>
                </el-popover>
            </el-table-column>
        </el-table>
    </div>
</template>
<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import format from 'filters/format'

    export default {
        props: {
            dataSource: {
                type: Object,
                default: () => {}
            },
            tableHeaderTitle: String,
            //实际还款 线上实收 线下实收 线下支付单 提前还款 部分还款
            financialContractUuid: String,
            cashFlowChannelTypeList: {
                type: Array,
                default: () => []
            },
            journalVoucherTypeList: {
                type: Array,
                default: () => []
            }
        },
        data: function() {
            return {
                showData: Object.assign({}, this.dataSource),
            }
        },
        watch: {
            dataSource: {
                deep: true,
                handler: function(current) {
                    this.showData = Object.assign({}, current);
                }
            },
        },
        computed: {
            showTitle: function() {
                return this.tableHeaderTitle;
            }
        },
        methods: {
            fetchOnlineSetStatistics: function(queryDate) {
                var { list } = this.showData;
                var index = list.findIndex(item => item.createDate === queryDate);

                if (index === -1) return;

                if (list[index].statistics.success) return;

                ajaxPromise({
                    url: '/operation-data/float/online',
                    data: {
                        financialContractUuid: this.financialContractUuid,
                        queryDate: format.formatDate(queryDate)
                    }
                }).catch(error => {
                    this.$set(list[index], 'statistics', { error });
                }).then(data => {
                    this.$set(list[index], 'statistics', Object.assign({
                        success: true,
                        list: []
                    }, data));
                });
            },
            fetchOfflineSetStatistics: function(queryDate) {
                var { list } = this.showData;
                var index = list.findIndex(item => item.createDate === queryDate);

                if (index === -1) return;

                if (list[index].statistics.success) return;

                ajaxPromise({
                    url: '/operation-data/float/offline',
                    data: {
                        financialContractUuid: this.financialContractUuid,
                        queryDate: format.formatDate(queryDate)
                    }
                }).catch(error => {
                    this.$set(list[index], 'statistics', { error });
                }).then(data => {
                    this.$set(list[index], 'statistics', Object.assign({
                        success: true,
                        list: []
                    }, data));
                });
            }
        }
    }
</script>