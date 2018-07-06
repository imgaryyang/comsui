<style type="sass">
	
</style>

<template>
	<div>
        <h5 class="hd">放款记录</h5>
        <div class="table">
            <div class="bd">
                <table>
                    <thead>
                        <tr>
                            <th width="12%">订单编号</th>
                            <th>计划放款金额</th>
                            <th>放款策略类型</th>
                            <th>受理时间</th>
                            <th>审核人</th>
                            <th>审核时间</th>
                            <th>订单状态</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-if="models.length == 0">
                            <td colspan="8" align="center">暂无数据</td>
                        </tr>
                        <template v-else v-for="(item, index) in models">
                            <tr>
                                <td> 
                                    <a :href="`${ctx}#/data/remittance/application/${item.remittanceApplicationUuid}/detail`">
                                        {{ item.remittanceApplicationUuid }}
                                    </a>
                                </td>
                                <td>
                                    {{ item.plannedTotalAmount | formatMoney }}
                                </td>
                                <td>
                                    {{ item.remittanceStrategyMsg }}
                                </td>
                                <td>
                                    {{ item.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}
                                </td>
                                <td>
                                    {{ item.auditorName }}
                                </td>
                                <td>
                                    {{ item.auditTime | formatDate('yyyy-MM-dd HH:mm:ss') }}
                                </td>
                                <td>
                                    {{ item.executionStatusMsg }}
                                </td>
                                <td>
                                    订单明细
                                    <a href="#" @click.prevent="showOrderDetailList(index)">
                                        <i class="icon" :class="{'icon-unexpand': item.expand,
                                            'icon-expand': !item.expand}">
                                        </i>
                                    </a>
                                </td>
                            </tr>
                            <tr v-if="item.expand" class="channel-cashflow-data">
                                <td colspan="8">
                                    <div v-loading="orderDetailList[index] && orderDetailList[index].fetching">
                                        <h5 class="hd">订单明细</h5>
                                        <div class="bd">
                                            <el-table
                                                :data="orderDetailList[index] && orderDetailList[index].list"
                                                class="td-15-padding th-8-15-padding no-th-border"
                                               >
                                                <el-table-column inline-template label="放款编号">
                                                    <a :href="`${ctx}#/data/remittance/plan/${ row.remittancePlanUuid}/detail`">
                                                        {{ row.remittancePlanUuid }}
                                                    </a>
                                                </el-table-column>
                                                <el-table-column prop="priorityLevel" label="放款优先级">
                                                </el-table-column>
                                                <el-table-column inline-template label="计划执行日期">
                                                    <div>
                                                        {{ row.plannedPaymentDate | formatDate }}
                                                    </div>
                                                </el-table-column>
                                                <el-table-column inline-template label="执行金额">
                                                    <div>
                                                        {{ row.plannedTotalAmount | formatMoney }}
                                                    </div>
                                                </el-table-column>
                                                <el-table-column inline-template label="付款方账户名">
                                                    <div v-if="row.pgAccountInfo.accountName">
                                                        <el-popover
                                                            trigger="hover"
                                                            placement="top">
                                                            <div>账户名：{{ row.pgAccountInfo.accountName }}</div>
                                                            <div>账户号：{{ row.pgAccountInfo.accountNo }}</div>
                                                            <div>开户行：{{ row.pgAccountInfo.bankName }}</div>
                                                            <div>所在地：{{ row.pgAccountInfo.province }}&nbsp;{{ row.pgAccountInfo.city }}</div>
                                                            <div>证件号:{{ row.pgAccountInfo.desensitizationIdNumber }}</div>
                                                            <span slot="reference">
                                                                {{ row.pgAccountInfo.accountName }}
                                                            </span>
                                                        </el-popover>
                                                        <el-popover
                                                            trigger="click"
                                                            placement="top"
                                                            @show="row.isActive = !row.isActive"
                                                            @hide="row.isActive = !row.isActive">
                                                            <div>账户名：{{ row.pgAccountInfo.accountName }}</div>
                                                            <div>账户号：{{ row.pgAccountInfo.accountNo }}</div>
                                                            <div>开户行：{{ row.pgAccountInfo.bankName }}</div>
                                                            <div>所在地：{{ row.pgAccountInfo.province }}&nbsp;{{ row.pgAccountInfo.city }}</div>
                                                            <div>证件号:{{ row.pgAccountInfo.desensitizationIdNumber }}</div>
                                                            <i  class="icon icon-bankcard" 
                                                                :class="{'active': row.isActive }" 
                                                                slot="reference">
                                                            </i>
                                                        </el-popover>
                                                    </div>
                                                </el-table-column>
                                                <el-table-column inline-template label="收款方账号名">
                                                    <div v-if="row.cpAccountInfo.accountName">
                                                        <el-popover
                                                            trigger="hover"
                                                            placement="top">
                                                            <div>账户名：{{ row.cpAccountInfo.accountName }}</div>
                                                            <div>账户号：{{ row.cpAccountInfo.accountNo }}</div>
                                                            <div>开户行：{{ row.cpAccountInfo.bankName }}</div>
                                                            <div>所在地：{{ row.cpAccountInfo.province }}&nbsp;{{ row.cpAccountInfo.city }}</div>
                                                            <div>证件号:{{ row.cpAccountInfo.desensitizationIdNumber }}</div>
                                                            <span slot="reference">
                                                                {{ row.cpAccountInfo.accountName }}
                                                            </span>
                                                        </el-popover>
                                                        <el-popover
                                                            trigger="click"
                                                            placement="top"
                                                            @show="row.isActive_ = !row.isActive_"
                                                            @hide="row.isActive_ = !row.isActive_">
                                                            <div>账户名：{{ row.cpAccountInfo.accountName }}</div>
                                                            <div>账户号：{{ row.cpAccountInfo.accountNo }}</div>
                                                            <div>开户行：{{ row.cpAccountInfo.bankName }}</div>
                                                            <div>所在地：{{ row.cpAccountInfo.province }}&nbsp;{{ row.cpAccountInfo.city }}</div>
                                                            <div>证件号:{{ row.cpAccountInfo.desensitizationIdNumber }}</div>
                                                            <i  class="icon icon-bankcard" 
                                                                :class="{'active': row.isActive_ }" 
                                                                slot="reference">
                                                            </i>
                                                        </el-popover>
                                                    </div>
                                                </el-table-column>
                                                <el-table-column prop="executionStatusMsg" label="执行状态">
                                                </el-table-column>
                                                <el-table-column prop="executionRemark" label="备注">
                                                </el-table-column>
                                            </el-table>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </template>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    
    export default {
    	props: {
            remittanceApplications: {
                type: Array,
                required: true,
                default: () => ([]),
            }
    	},
    	data: function() {
    		return {
                fetching: false,
                models: [],
                orderDetailList: {},
    		}
    	},
    	watch: {
            remittanceApplications: function(cur) {
                this.models = cur;
                this.models.forEach(item => {
                    this.$set(item,'expand', false);
                });
            },
            models: function(cur) {
                this.models = cur;
            },
    	},
    	methods: {
            showOrderDetailList: function(index) {
                this.models[index].expand =  !this.models[index].expand;

                if (!this.models[index].expand) return;
                this.expandOrderDetailList(this.models[index].remittanceApplicationUuid, index);
            },
            expandOrderDetailList: function(remittanceApplicationUuid, index) {
                this.orderDetailList = Object.assign({}, this.orderDetailList, {
                    [index]: {
                        fetching: true,
                        list: []
                    }
                });

                ajaxPromise({
                    url: `/contracts/detail/planlist`,
                    data: { remittanceApplicationUuid }
                }).then(data => {
                    this.orderDetailList[index].list = data.remittancePlans.map(item => {
                        item.isActive = false;
                        item.isActive_ = false;
                        return item;
                    });
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.orderDetailList[index].fetching = false;
                });
            }
    	}
    }
</script>