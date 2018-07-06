<style lang="sass">
</style>
<template>
	<div class="content" id="guarantee-complement">
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
                            v-model="queryConds.guaranteeStatus"
                            placeholder="担保状态"
                            clearable
                            size="small">
                            <el-option
                                v-for="(label,value) in GuaranteeStatus"
                                :label="value"
                                :value="label">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.startDate"
                                    name="startDate"
                                    :end-date="queryConds.endDate"
                                    placeholder="计划还款起始日期"
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
                                    v-model="queryConds.endDate"
                                    name="endDate"
                                    :start-date="queryConds.startDate"
                                    placeholder="计划还款终止日期"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.dueStartDate"
                                    name="dueStartDate"
                                    :end-date="queryConds.dueEndDate"
                                    placeholder="担保截止起始日期"
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
                                    v-model="queryConds.dueEndDate"
                                    name="dueEndDate"
                                    :start-date="queryConds.dueStartDate"
                                    placeholder="担保截止终止日期"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <span class="item vertical-line"></span>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="担保补足号" value="orderNo"></el-option>
                            <el-option label="还款编号" value="singleLoanContractNo"></el-option>
                            <el-option label="商户还款计划编号" value="outerRepaymentPlanNo"></el-option>
                            <el-option label="商户编号" value="appId"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                    <el-form-item v-if="ifElementGranted('export-guarantee-complement')">
                        <ExportDropdown @command="exportModal.show = true">
                            <el-dropdown-item>担保汇总表</el-dropdown-item>
                        </ExportDropdown>
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
					<el-table-column label="担保补足号" prop="order.orderNo" inline-template>
                        <a :href="`${ctx}#/finance/guarantee/complement/${ row.order.id }/detail`">{{ row.order.orderNo }}</a>
                    </el-table-column>
                    <el-table-column label="还款编号" prop="singleLoanContractNo" inline-template>
                        <a :href="`${ctx}#/finance/assets/${ row.assetSet.assetUuid }/detail`">{{ row.assetSet.singleLoanContractNo }}</a>
                    </el-table-column>
                    <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo" inline-template>
                        <span>{{ row.assetSet.outerRepaymentPlanNo }}</span>
                    </el-table-column>
                    <el-table-column label="计划还款日期" prop="assetRecycleDate" sortable="custom" inline-template>
                        <div>{{ row.assetSet.assetRecycleDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="担保截止日" prop="dueDate" sortable="custom" inline-template>
                        <div>{{ row.order.dueDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="商户编号" prop="assetSet.contract.app.appId">
                    </el-table-column>
                    <el-table-column label="计划还款金额" prop="assetInitialValue" sortable="custom" inline-template>
                        <div>{{ row.assetSet.assetInitialValue | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="发生时间" prop="order.modifyTime" sortable="custom">
                    </el-table-column>
                    <el-table-column label="担保金额" inline-template>
                        <div>{{ row.order.totalRent | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="担保状态" inline-template>
                        <div>
                            <span
                                :style="{
                                    'color-warning': row.assetSet.guaranteeStatusMsg =='待补足',
                                    'color-danger': row.assetSet.guaranteeStatusMsg =='担保作废',
                                    }">
                                {{ row.assetSet.guaranteeStatusMsg }}
                            </span>
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

        <ExportPreviewModal
            :parameters="conditions"
            :query-action="`/guarantee/order/preview-export-excel`"
            :download-action="`/report/export?reportId=6`"
            v-model="exportModal.show">
            <el-table-column label="序号" type="index" width="50"></el-table-column>
            <el-table-column label="uniqueid" prop="uniqueId"></el-table-column>
            <el-table-column label="补差单号" prop="orderNo"></el-table-column>
            <el-table-column label="还款期号" prop="singleLoanContractNo"></el-table-column>
            <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo"></el-table-column>
            <el-table-column label="应还日期" prop="assetRecycleDate"></el-table-column>
            <el-table-column label="补差截止日" prop="dueDate"></el-table-column>
            <el-table-column label="商户编号" prop="appId"></el-table-column>
            <el-table-column label="本息金额" prop="monthFee"></el-table-column>
            <el-table-column label="差异天数" prop="overDueDays"></el-table-column>
            <el-table-column label="发生时间" prop="modifyTime"></el-table-column>
            <el-table-column label="补差金额" prop="totalRent"></el-table-column>
            <el-table-column label="补差状态" prop="guaranteeStatus"></el-table-column>
        </ExportPreviewModal>

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
            ListStatistics: require('views/include/ListStatistics'),
            ExportDropdown: require('views/include/ExportDropdown'),
            ExportPreviewModal: require('views/include/ExportPreviewModal'),
        },
		data: function() {
			return {
				action: '/guarantee/order/search',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
                    financialContractIds: [],
                    guaranteeStatus: '',

                    startDate: '',
                    endDate: '',
                    dueStartDate: '',
                    dueEndDate: '',

                },
				comboConds: {
					orderNo: '',
                    singleLoanContractNo: '',
                    appId: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

                exportModal:{
                    show: false
                },

				financialContractQueryModels: [],
				GuaranteeStatus: {}
			};
		},
		methods: {
			initialize: function() {
			    return ajaxPromise({
			    	url: `/guarantee/order/options`
			    }).then(data => {
			    	this.financialContractQueryModels = data.queryAppModels || [];
    	            this.GuaranteeStatus = data.GuaranteeStatus || {};
			    }).catch(message => {
			    	MessageBox.open(message);
			    });
			}
		}
	}
</script>
