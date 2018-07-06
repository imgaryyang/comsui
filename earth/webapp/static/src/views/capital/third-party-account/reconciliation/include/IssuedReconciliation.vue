<style lang="sass">
    .channel-cashflow-data {
        & > td {
            background: #f5f5f5;
        }

        .el-table {
            width: 92%; 
            margin: 15px auto;

            td,
            th {
                padding: 0;
            }
        }
        
        .el-table__header,
        .el-table__body {
            margin-top: -1px;
            margin-left: -1px;
        }
    }

    .issued-reconciliation-data {

    }
</style>

<template>
    <div class="table">
        <div class="bd">
            <table v-loading="dataSource.fetching">
                <thead>
                    <tr>
                        <th width="12%">来源单号</th>
                        <th>单据类型</th>
                        <th>对方账户</th>
                        <th>交易金额</th>
                        <th>借贷标记</th>
                        <th width="12%">通道请求号</th>
                        <th>状态变更时间</th>
                        <th>执行状态</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="dataSource.size == 0">
                        <td style="text-align: center;" colspan="9">暂无数据</td>
                    </tr>
                    <template v-else v-for="item in dataSource.list">
                        <tr class="issued-reconciliation-data">
                            <td>
                            	<div>
                                    <a 
                                        :href="`${ctx}#/capital/remittance-cash-flow/plan-execlog/${item.systemBill.systemBillNo}/detail`"
                                        v-if="item.systemBill.systemBillType == 'T_REMITTANCE_PLAN_EXEC_LOG'">
                                        {{ item.systemBill.systemBillNo }}
                                    </a>
				                	<span v-else>{{ item.systemBill.systemBillNo }}</span>
			                	</div>
                            </td>
                            <td>{{ item.systemBill.systemBillTypeName }}</td>
                            <td>
                                <el-popover 
                                    v-if="item.systemBill.systemBillCpAccountInfo"
                                    trigger="hover" 
                                    placement="top">
                                        <div>角色类型: --</div>
                                        <div>账户名: {{ item.systemBill.systemBillCpAccountInfo.accountName }}</div>
                                        <div>账户号: {{ item.systemBill.systemBillCpAccountInfo.accountNo }}</div>
                                        <div>开户行: {{ item.systemBill.systemBillCpAccountInfo.bankName }}</div>
                                        <div>所在省: {{ item.systemBill.systemBillCpAccountInfo.province }}</div>
                                        <div>所在市: {{ item.systemBill.systemBillCpAccountInfo.city }}</div>
                                        <span slot="reference">{{ item.systemBill.systemBillCpAccountInfo.accountName }}</span>
                                </el-popover>
                            </td>
                            <td>{{ item.systemBill.systemBillAmount | formatMoney }}</td>
                            <td>{{ item.systemBill.systemBillAccountSideName }}</td>
                            <td>{{ item.systemBill.systemBillTradeUuid }}</td>
                            <td>{{ item.systemBill.systemBillLastModifedTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</td>
                            <td>{{ item.systemBill.executionStatusName }}</td>
                            <td width="110">
                                资金流水
                                <a 
                                    href="#" 
                                    @click.prevent="item.expand = !item.expand">
                                    <i class="icon" 
                                        :class="{
                                            'icon-unexpand': item.expand,
                                            'icon-expand': !item.expand,
                                        }">
                                    </i>
                                </a>
                            </td>
                        </tr>
                        <tr v-if="item.expand" class="channel-cashflow-data">
                            <td colspan="9">
                                <el-table
                                    :data="item.cashFlowAdapter && item.cashFlowAdapter.cashFlowTradeUuid ? [item.cashFlowAdapter] : []"
                                    class="td-15-padding th-8-15-padding no-th-border">
                                    <el-table-column prop="cashFlowSerialNo" label="通道流水号"></el-table-column>
                                    <el-table-column inline-template label="对方账户">
                                        <el-popover 
                                            v-if="row.cashFlowCpAccountInfo"
                                            trigger="hover" 
                                            placement="top">
                                                <div>角色类型：--</div>
                                                <div>账户名：{{ row.cashFlowCpAccountInfo.accountName }}</div>
                                                <div>账户号：{{ row.cashFlowCpAccountInfo.accountNo }}</div>
                                                <div>开户行：{{ row.cashFlowCpAccountInfo.bankName }}</div>
                                                <div>所在省：{{ row.cashFlowCpAccountInfo.province }}</div>
                                                <div>所在市：{{ row.cashFlowCpAccountInfo.city }}</div>
                                                <span slot="reference">{{ row.cashFlowCpAccountInfo.accountName }}</span>
                                        </el-popover>
                                    </el-table-column>
                                    <el-table-column prop="cashFlowAmount" label="流水金额"></el-table-column>
                                    <el-table-column prop="accountSideName" label="借贷标记"></el-table-column>
                                    <el-table-column prop="cashFlowTradeUuid" label="通道请求号"></el-table-column>
                                    <el-table-column inline-template label="入账时间">
                                        <div>{{ row.cashFlowTransationTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                                    </el-table-column>
                                    <el-table-column prop="cashFlowRemark" label="备注"></el-table-column>
                                </el-table>
                            </td>
                        </tr>
                    </template>
                </tbody>
            </table>
        </div>
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

    export default {
        mixins: [Pagination],
        props: {
            selected: Boolean,
            action: {
                type: String,
                required: true
            },
            autoload: {
                type: Boolean,
                default: false
            },
            auditJobUuid:String,
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
            }
        },
        methods: {
            parse: function(data) {
                data.list = data.list.map(item => {
                    item.expand = false;
                    return item;
                });
                return data;
            }
        }
    }
</script>