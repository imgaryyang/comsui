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
                            v-model="queryConds.settlementStatus"
                            placeholder="清算状态"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in settlementStatusList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="清算单号" value="settlementOrderNo"></el-option>
                            <el-option label="还款编号" value="repaymentNo"></el-option>
                            <el-option label="商户还款计划编号" value="outerRepaymentPlanNo"></el-option>
                            <el-option label="商户编号" value="appNo"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                    <el-form-item v-if="ifElementGranted('export-guarantee-settlement')">
                        <ExportDropdown @command="handleExport">
                            <el-dropdown-item>清算汇总表</el-dropdown-item>
                        </ExportDropdown>
                    </el-form-item>
                    <el-form-item v-if="ifElementGranted('batch-submit-settle-guarantee-settlement')">
                        <ExportDropdown @command="handleOperate" title="批量">
                            <el-dropdown-item command="batchSubmit">提交</el-dropdown-item>
                            <el-dropdown-item command="batchSettlement">清算</el-dropdown-item>
                        </ExportDropdown>
                    </el-form-item>
                </el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border"
                    @sort-change="onSortChange"
                    @selection-change="onSelectionChange"
					stripe
					v-loading="dataSource.fetching"
					:data="dataSource.list">
					<el-table-column label="" type="selection" :selectable="handleSelectCheck"></el-table-column>
                    <el-table-column label="清算单号" inline-template>
                        <a @click="handleShowDetail(row.id)" href="javascript: void 0;">
                            {{ row.settleOrderNo }}
                        </a>
                    </el-table-column>
                    <el-table-column inline-template
                        label="还款编号">
                          <a :href="`${ctx}#/finance/assets/${ row.assetSet.assetUuid }/detail`">{{ row.assetSet.singleLoanContractNo }}</a>  
                    </el-table-column>
                    <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo" inline-template>
                        <span>{{ row.assetSet.outerRepaymentPlanNo }}</span>
                    </el-table-column>
                    <el-table-column label="计划还款日期" prop="assetRecycleDate" inline-template sortable="custom">
                        <div>{{ row.assetSet.assetRecycleDate }}</div>
                    </el-table-column>
                    <el-table-column label="清算截止日" inline-template prop="dueDate" sortable="custom">
                        <div>{{ row.settlementOrder.dueDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="商户编号" prop="assetSet.contract.app.appId"></el-table-column>
                    <el-table-column label="计划还款金额" prop="assetInitialValue" inline-template sortable="custom">
                        <div>{{ row.assetSet.assetInitialValue | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="逾期天数" inline-template prop="overdueDays" sortable="custom">
												<div>{{ row.settlementOrder.overdueDays }}</div>
										</el-table-column>
                    <el-table-column label="逾期费用合计" inline-template sortable="custom">
                        <div>{{ row.settlementOrder.overduePenalty | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="发生时间" inline-template prop="lastModifyTime" sortable="custom">
												<div>{{ row.settlementOrder.lastModifyTime | formatDate }}</div>
										</el-table-column>
                    <el-table-column label="清算金额" inline-template prop="settlementAmount" sortable="custom">
                        <div>{{ row.settlementOrder.settlementAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="清算状态" inline-template>
                        <div :class="{
                            'color-warning': row.assetSet.settlementStatusMsg == '待结清'
                            }">
                            {{ row.assetSet.settlementStatusMsg }}
                        </div>
                    </el-table-column>
                    <el-table-column label="备注" prop="settlementOrder.comment"></el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
			<div class="pull-right">
				<PageControl
	                v-model="pageConds.pageIndex"
	                :size="dataSource.size"
	                :per-page-record-number="pageConds.perPageRecordNumber">
	            </PageControl>
			</div>
		</div>
        <ExportPreviewModal
            :parameters="conditions"
            :query-action="`/settlement-order/settle/preview_export_settlement_excel`"
            :download-action="`/report/export?reportId=7`"
            v-model="exportModal.show">
            <el-table-column label="uniqueid" prop="uniqueId"></el-table-column>
            <el-table-column label="结清单号" prop="settleOrderNo"></el-table-column>
            <el-table-column label="回款单号" prop="repaymentNo"></el-table-column>
            <el-table-column label="应还日期" prop="recycleDate"></el-table-column>
            <el-table-column label="清算截止日" prop="dueDate"></el-table-column>
            <el-table-column label="商户编号" prop="appId"></el-table-column>
            <el-table-column label="本息金额" prop="principalAndInterestAmount"></el-table-column>
            <el-table-column label="差异天数" prop="overdueDays"></el-table-column>
            <el-table-column label="差异罚息" prop="overduePenalty"></el-table-column>
            <el-table-column label="发生时间" prop="modifyTime"></el-table-column>
            <el-table-column label="结清金额" prop="settlementAmount"></el-table-column>
            <el-table-column label="状态" prop="settlementStatus"></el-table-column>
            <el-table-column label="备注" prop="comment"></el-table-column>
        </ExportPreviewModal>
        <SettlementDetailModal
            v-model="settlementDetailModal.show"
            :settlementId="settlementDetailModal.settlementId">
        </SettlementDetailModal>
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
            ExportDropdown: require('views/include/ExportDropdown'),
            ExportPreviewModal: require('views/include/ExportPreviewModal'),
            SettlementDetailModal: require('./SettlementDetailModal'),
        },
		data: function() {
			return {
				action: '/settlement-order/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
                    financialContractIds: [],
                    settlementStatus: ''
                },
				comboConds: {
					settlementOrderNo: '',
                    repaymentNo: '',
                    appNo: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

				financialContractQueryModels: [],
				settlementStatusList: [],

                exportModal: {
                    show: false
                },
                settlementDetailModal: {
                    show: false,
                    settlementId: ''
                },
                selections: []
			};
		},
		methods: {
			initialize: function() {
			    return ajaxPromise({
			    	url: `/settlement-order/options`
			    }).then(data => {
			    	this.financialContractQueryModels = data.queryAppModels || [];
    	            this.settlementStatusList = data.settlementStatusList || [];
			    }).catch(message => {
			    	MessageBox.open(message);
			    });
			},
            onSelectionChange: function(selections) {
                this.selections = selections;
            },
            handleExport: function(command) {
                this.exportModal.show = true;
            },
            handleOperate: function(command) {
                const checkeds = this.selections.map(item => item.settlementOrderUuid);

                if (command == 'batchSubmit') {
                    ajaxPromise({
                        url: `/settlement-order/batch-submit`,
                        type: 'post',
                        data: {
                            settlementOrderUuids : JSON.stringify(checkeds)
                        }
                    }).then(data => {
                        this.fetch();
                    }).catch(message => {
                        MessageBox.open(message);
                    });
                } else if (command == 'batchSettlement') {
                    ajaxPromise({
                        url: `/settlement-order/batch-settlement`,
                        type: 'post',
                        data: {
                            settlementOrderUuids : JSON.stringify(checkeds)
                        }
                    }).then(data => {
                        this.fetch();
                    }).catch(message => {
                        MessageBox.open(message);
                    });
                }
            },
            handleShowDetail: function(id) {
                this.settlementDetailModal.show = true;
                this.settlementDetailModal.settlementId = id;
            },
            handleSelectCheck: function(row) {
                return row.assetSet.settlementStatusMsg == '已清算' ? false : true;
            }
		}
	}
</script>
