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
                            v-model="queryConds.financialContractIds"
                            :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.executingSettlingStatusInt"
                            placeholder="结算状态"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in executingSettlingStatusList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    :pickTime="true"
                                    :formatToMinimum="true"
                                    v-model="queryConds.createTimeStartDateString"
                                    :end-date="queryConds.createTimeEndDateString"
                                    placeholder="创建起始日期"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2">
                            <div class="text-align-center color-dim">至</div>
                        </el-col>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    :pickTime="true"
                                    :formatToMaximum="true"
                                    v-model="queryConds.createTimeEndDateString"
                                    :start-date="queryConds.createTimeStartDateString"
                                    placeholder="创建终止日期"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="结算单号" value="orderNo"></el-option>
                            <el-option label="还款编号" value="singleLoanContractNo"></el-option>
                            <el-option label="商户还款计划编号" value="outerRepaymentPlanNo"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                </el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border"
					stripe
                    @sort-change="onSortChange"
					v-loading="dataSource.fetching"
					:data="dataSource.list">
					<el-table-column label="结算单号" prop="order.orderNo" inline-template>
                        <a :href="`${ctx}#/finance/payment-order/${row.order.id}/detail`">{{row.order.orderNo }}</a>
                    </el-table-column>
                    <el-table-column label="还款编号" prop="singleLoanContractNo" inline-template>
                        <a :href="`${ctx}#/finance/assets/${row.assetSet.assetUuid}/detail`">{{row.assetSet.singleLoanContractNo }}</a>
                    </el-table-column>
                    <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo" inline-template>
                        <div>{{ row.assetSet.outerRepaymentPlanNo }}</div>
                    </el-table-column>
                    <el-table-column label="计划还款日期" prop="assetRecycleDate" sortable="custom" inline-template>
                        <div>{{ row.assetSet.assetRecycleDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="结算金额（元）" prop="totalRent" sortable="custom" inline-template>
                        <el-popover
                            @show="fetchAssetSetStatistics(row.order.id)"
                            trigger="hover"
                            placement="top">
                            <div>
                                <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                                <template v-else>
                                    <div>还款本金：
                                        <span v-if="row.statistics.loanAssetPrincipal == null">--</span>
                                        <span v-else>{{ row.statistics.loanAssetPrincipal | formatMoney}}</span>
                                    </div>
                                    <div>还款利息：
                                        <span v-if="row.statistics.loanAssetInterest == null">--</span>
                                        <span v-else>{{ row.statistics.loanAssetInterest | formatMoney }}</span>
                                    </div>
                                    <div>贷款服务费：
                                        <span v-if="row.statistics.loanServiceFee == null">--</span>
                                        <span v-else>{{ row.statistics.loanServiceFee | formatMoney }}</span>
                                    </div>
                                    <div>技术维护费：
                                        <span v-if="row.statistics.loanTechFee == null">--</span>
                                        <span v-else>{{ row.statistics.loanTechFee | formatMoney }}</span>
                                    </div>
                                    <div>其他费用：
                                        <span v-if="row.statistics.loanOtherFee == null">--</span>
                                        <span v-else>{{ row.statistics.loanOtherFee | formatMoney }}</span>
                                    </div>
                                    <div>逾期罚息：
                                        <span v-if="row.statistics.overdueFeePenalty == null">--</span>
                                        <span v-else>{{ row.statistics.overdueFeePenalty | formatMoney }}</span>
                                    </div>
                                    <div>逾期违约金：
                                        <span v-if="row.statistics.overdueFeeObligation == null">--</span>
                                        <span v-else>{{ row.statistics.overdueFeeObligation | formatMoney }}</span>
                                    </div>
                                    <div>逾期服务费：
                                        <span v-if="row.statistics.overdueFeeService == null">--</span>
                                        <span v-else>{{ row.statistics.overdueFeeService | formatMoney }}</span>
                                    </div>
                                    <div>逾期其他费用：
                                        <span v-if="row.statistics.overdueFeeOther == null">--</span>
                                        <span v-else>{{ row.statistics.overdueFeeOther | formatMoney }}</span>
                                    </div>
                                    <div>逾期费用合计：
                                        <span v-if="row.statistics.totalOverdueFee == null">--</span>
                                        <span v-else>{{ row.statistics.totalOverdueFee | formatMoney }}</span>
                                    </div>
                                </template>
                            </div>
                            <span slot="reference">{{ row.order.totalRent | formatMoney }}</span>
                        </el-popover>
                    </el-table-column>
                    <el-table-column label="创建时间" prop="createTime" sortable="custom" inline-template>
                        <div>{{ row.order.createTime }}</div>
                    </el-table-column>
                    <el-table-column label="状态变更时间" prop="modifyTime" sortable="custom" inline-template>
                        <div>{{ row.order.modifyTime }}</div>
                    </el-table-column>
                    <el-table-column label="结算状态" prop="order.executingSettlingStatus" inline-template>
                        <div>
                            <span v-if="row.order.executingSettlingStatus == 'FAIL'" class="color-danger">{{ row.order.executingSettlingStatus | executingSettlingStatus2zh }}</span>
                            <span v-else>{{ row.order.executingSettlingStatus | executingSettlingStatus2zh }}</span>
                        </div>
                    </el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
			<div class="pull-right">
                <ListStatistics
                    action="/guarantee/order/amountStatistics"
                    identifier="financialContractIds"
                    :parameters="conditions">
                    <template scope="statistics">
                        <div><span class="text-muted">担保金额:</span>{{ statistics.rentAmount | formatMoney }}</div>
                    </template>
                </ListStatistics>
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
            ComboQueryBox: require('views/include/ComboQueryBox'),
            ListStatistics: require('views/include/ListStatistics')
        },
		data: function() {
			return {
				action: '/payment-manage/order/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
                    financialContractIds: [],
                    executingSettlingStatusInt: '',
                    createTimeStartDateString: '',
                    createTimeEndDateString: '',
                },
				comboConds: {
					orderNo: '',
                    singleLoanContractNo: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

				financialContractQueryModels: [],
				executingSettlingStatusList: []
			};
		},
        filters: {
            executingSettlingStatus2zh: function(value) {
                var maps = { CREATE:'已创建', DOING:'处理中', SUCCESS:'成功', FAIL:'失败', CLOSED:'已关闭' };
                return maps[value];
            }
        },
		methods: {
			initialize: function() {
			    return ajaxPromise({
			    	url: `/payment-manage/order/options`
			    }).then(data => {
			    	this.financialContractQueryModels = data.queryAppModels || [];
    	            this.executingSettlingStatusList = data.executingSettlingStatusList || [];
			    }).catch(message => {
			    	MessageBox.open(message);
			    });
			},
            onSuccess: function(data) {
                this.dataSource.list = data.list.map(item => {
                    item.statistics = {};
                    return item;
                });
                this.dataSource.size = data.size;
            },
            fetchAssetSetStatistics: function(orderId) {
                var { list } = this.dataSource;
                var index = list.findIndex(item => item.id === orderId);

                if (index === -1) return;

                ajaxPromise({
                    url: `/payment-manage/order/${orderId}/chargesDetail`
                }).catch(error => {
                    this.$set(list[index], 'statistics', { error });
                }).then(data => {
                    this.$set(list[index], 'statistics', data.data || {});
                });
            }
		}
	}
</script>
