<style lang="sass">
    .icon-except-zero {
        width: 15px;
        height: 15px;
        background-position: -3px -280px;

        &.active {
            background-position: -24px -280px;
        }
    }
    .tag-table {
        .tag{
            display: inline-block;
            width: 14px;
            height: 14px;
            margin-right: 3px;
            background-image: url('./include/tag.png');
            background-size: 14px 28px;
            background-repeat: no-repeat;
        }
        .tagEmpty{
            background-position: 0 0;
        }
        .tagFull{
            background-position: 0 -14px;
        }
        .tag-center {
            display: inline-block;
            position: absolute;
            top: 50%;
            margin-top: -7px;
        }
        .atag {
            display: inline-block;
            padding-left: 20px;
        }
    }
    .tag-popover {
        .tag-title {
            color: #666;
            font-size: 14px;
        }
        .ul-reset{
            list-style: none;
            padding: 0;
            margin: 0;
            max-height: 100px;
            overflow: auto;
            overflow-x: none;
            width: 360px;
            .li-tag{
                display: inline-block;
                line-height: 20px;
                border: 1px solid #4669a9;
                text-align: center;
                padding: 4px 8px;
                margin-right: 10px;
                margin-bottom: 5px;
                cursor: default;
                color: #3562a6;

                .el-icon-close{
                    cursor: pointer;
                    transform: scale(0.6);
                    visibility: hidden;
                    opacity: 0;
                    margin-left: 5px;
                }
                &:hover{
                    background-color: #4669a9;
                    color: white;
                    .el-icon-close{
                        visibility: visible;
                        opacity: 1;
                    }
                }
            }
            .create-tag {
                border: 0px;

                &:hover{
                    background-color: white;
                }
            }
            .el-icon-plus{
                color: #75B734;
                cursor: pointer;

                &:hover{
                    color: #75B734;
                }
            }
        }
        .init-ul {
            width: 50px;
        }
    }

</style>

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
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.startDate"
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
                                    v-model="queryConds.actualRecycleStartDate"
                                    :end-date="queryConds.actualRecycleEndDate"
                                    size="small"
                                    placeholder="实际还款起始日">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2">
                            <div class="text-align-center color-dim">至</div>
                        </el-col>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.actualRecycleEndDate"
                                    :start-date="queryConds.actualRecycleStartDate"
                                    size="small"
                                    placeholder="实际还款终止日">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.paymentStatusOrdinals"
                            size="small"
                            clearable
                            placeholder="还款状态"
                            multiple>
                            <el-select-all-option
                                :options="paymentStatusList">
                            </el-select-all-option>
                            <el-option
                                v-for="item in paymentStatusList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.auditOverDueStatusOrdinals"
                            placeholder="逾期状态"
                            clearable
                            size="small"
                            multiple>
                            <el-select-all-option
                                :options="auditOverdueStatusList">
                            </el-select-all-option>
                            <el-option
                                v-for="item in auditOverdueStatusList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="还款编号" value="singleLoanContractNo"></el-option>
                            <el-option label="商户还款计划编号" value="outerRepaymentPlanNo"></el-option>
                            <el-option label="贷款合同编号" value="contractNo"></el-option>
                            <el-option label="客户姓名" value="customerName"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary" @click="queryConds.isRepayment = false">查询</el-button>
                    </el-form-item>
                   <!--  <el-form-item v-if="ifElementGranted('export-repayment-management')">
                        <ExportDropdown @command="handleExport"> -->
<!--                             <el-dropdown-item command="overDueRepayment">逾期还款明细表</el-dropdown-item> -->
                            <!-- <el-dropdown-item command="repaymentPlanManagement">还款管理表</el-dropdown-item> -->
                            <!-- <el-dropdown-item command="repaymentPlanDetail">还款计划明细汇总表</el-dropdown-item> -->
                        <!-- </ExportDropdown> -->
                    <!-- </el-form-item> -->
                    <el-form-item>
                        <ExportDropdown @command="handleOperate" title="批量">
                            <el-dropdown-item command="repaymentPlanManagement" v-if="ifElementGranted('export-repayment-management')">导出</el-dropdown-item>
                            <el-dropdown-item command="batchAddTags">添加标签</el-dropdown-item>
                            <el-dropdown-item command="batchDeleteTags">删除标签</el-dropdown-item>
                        </ExportDropdown>
                    </el-form-item>
                </el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border tag-table"
					stripe
					@sort-change="onSortChange"
					v-loading="dataSource.fetching"
					:data="dataSource.list">
					<el-table-column label="还款编号" width="100" prop="" inline-template>
                        <div>
                            <el-popover
                                style="display: inline-block;"
                                popper-class="tag-popover"
                                placement="right"
                                trigger="hover">
                                <h5 class="tag-title">标签</h5>
                                <ul class="ul-reset" :class="{'init-ul': row.tags.length == 0}">
                                    <li v-for="tag in row.tags" class="li-tag">
                                        {{tag.tagName}}<span class="el-icon-close" @click="handleClickDeleteCurrentLabel(tag, row.tags)"></span>
                                    </li>
                                    <li class="el-icon-plus li-tag create-tag" @click="handleClickAddLabel(row,$index)"></li>
                                </ul>
                                <template v-if="row.transitivityTags.length">
                                    <h5 class="tag-title">联动标签</h5>
                                    <ul class="ul-reset">
                                        <li v-for="tag in row.transitivityTags" class="li-tag">
                                            {{tag.tagName}}
                                        </li>
                                    </ul>
                                </template>
                                <span :class="['tag','tag-center', !row.tags.length && !row.transitivityTags.length ? 'tagEmpty' :'tagFull']" slot="reference"></span>
                            </el-popover>
                            <a class="atag" :href="`${ctx}#/finance/assets/${row.assetSetUuid}/detail`">{{ row.singleLoanContractNo }}</a>
                        </div>
					</el-table-column>
                    <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo"></el-table-column>
                    <el-table-column
                        inline-template
                        label="贷款合同编号" prop="contractNo">
                        <a :href="`${ctx}#/data/contracts/detail?id=${ row.contractId }`">{{ row.contractNo }}</a>
                    </el-table-column>
                    <!-- <el-table-column :label="$utils.locale('financialContract.no')" prop="financialContractNo"></el-table-column> -->
                    <el-table-column label="客户姓名" prop="customerName"></el-table-column>
                    <el-table-column label="当前期数" prop="currentPeriod" inline-template>
                        <div>{{ row.currentPeriod }}/{{ row.allPeriods }}</div>
                    </el-table-column>
                    <el-table-column label="计划还款日期" prop="assetRecycleDate" inline-template sortable="custom">
                        <div>{{ row.assetRecycleDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="实际还款日期" prop="actualRecycleDate" inline-template sortable="custom">
                        <div>{{ row.actualRecycleDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="逾期天数" prop="auditOverdueDays"></el-table-column>
                    <el-table-column label="应还款金额" prop="amount" inline-template sortable="custom">
                        <el-popover
                            @show="fetchAssetSetStatistics(row.assetSetUuid)"
                            trigger="hover"
                            placement="top">
                            <div>
                                <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                                <template v-else>
                                    <div>计划还款本金：{{ row.statistics.planChargesDetail.loanAssetPrincipal | formatMoney }}</div>
                                    <div>计划还款利息：{{ row.statistics.planChargesDetail.loanAssetInterest | formatMoney }}</div>
                                    <div>贷款服务费：{{ row.statistics.planChargesDetail.loanServiceFee | formatMoney }}</div>
                                    <div>技术维护费：{{ row.statistics.planChargesDetail.loanTechFee | formatMoney }}</div>
                                    <div>其他费用：{{ row.statistics.planChargesDetail.loanOtherFee | formatMoney }}</div>
                                    <div>逾期罚息：{{ row.statistics.planChargesDetail.overdueFeePenalty | formatMoney }}</div>
                                    <div>逾期违约金：{{ row.statistics.planChargesDetail.overdueFeeObligation | formatMoney }}</div>
                                    <div>逾期服务费：{{ row.statistics.planChargesDetail.overdueFeeService | formatMoney }}</div>
                                    <div>逾期其他费用：{{ row.statistics.planChargesDetail.overdueFeeOther | formatMoney }}</div>
                                </template>
                            </div>
                            <span slot="reference">{{ row.amount | formatMoney }}</span>
                        </el-popover>
                    </el-table-column>
                    <el-table-column label="实际还款金额" prop="actualAmount" :render-header="renderActualAmountHeader" inline-template>
                        <el-popover
                            @show="fetchAssetSetStatistics(row.assetSetUuid)"
                            trigger="hover"
                            placement="top">
                            <div>
                                <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                                <template v-else>
                                    <div>实收本金：{{  row.statistics.paidUpChargesDetail.loanAssetPrincipal | formatMoney }}</div>
                                    <div>实收利息：{{  row.statistics.paidUpChargesDetail.loanAssetInterest | formatMoney }}</div>
                                    <div>实收贷款服务费：{{  row.statistics.paidUpChargesDetail.loanServiceFee | formatMoney }}</div>
                                    <div>实收技术维护费：{{  row.statistics.paidUpChargesDetail.loanTechFee | formatMoney }}</div>
                                    <div>实收其他费用：{{  row.statistics.paidUpChargesDetail.loanOtherFee | formatMoney }}</div>
                                    <div>实收逾期罚息：{{  row.statistics.paidUpChargesDetail.overdueFeePenalty | formatMoney }}</div>
                                    <div>实收逾期违约金：{{  row.statistics.paidUpChargesDetail.overdueFeeObligation | formatMoney }}</div>
                                    <div>实收逾期服务费：{{  row.statistics.paidUpChargesDetail.overdueFeeService | formatMoney }}</div>
                                    <div>实收逾期其他费用：{{  row.statistics.paidUpChargesDetail.overdueFeeOther | formatMoney }}</div>
                                </template>
                            </div>
                            <span slot="reference">{{ row.actualAmount | formatMoney }}</span>
                        </el-popover>
                    </el-table-column>
                    <el-table-column label="未偿金额" inline-template>
                        <el-popover
                            @show="fetchAssetSetStatistics(row.assetSetUuid)"
                            trigger="hover"
                            placement="top">
                            <div>
                                <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                                <template v-else>
                                    <div>未偿本金：{{ row.statistics.unPaidDetail.unPaidPrincipalValue | formatMoney }}</div>
                                    <div>未偿利息：{{ row.statistics.unPaidDetail.unPaidInterestValue | formatMoney }}</div>
                                    <div>未偿其他：{{ row.statistics.unPaidDetail.unPaidTotalOtherFee | formatMoney }}</div>
                                </template>
                            </div>
                            <span slot="reference">{{ (row.amount - row.actualAmount) | formatMoney }}</span>
                        </el-popover>
                    </el-table-column>
                    <el-table-column label="还款状态" prop="paymentStatus" inline-template>
                        <span :class="{
                            'color-danger': row.paymentStatus == '还款异常'
                            }">{{ row.paymentStatus }}</span>
                    </el-table-column>
                    <el-table-column label="逾期状态" prop="overdueStatus" inline-template>
                        <span :class="{
                            'color-danger': row.overdueStatus == '已逾期'
                            }">{{ row.overdueStatus }}</span>
                    </el-table-column>
                    <el-table-column label="担保状态" prop="guaranteeStatus" inline-template>
                        <span :class="{
                            'color-danger': row.guaranteeStatus == '担保作废',
                            'color-warning': row.guaranteeStatus == '待补足'
                            }">{{ row.guaranteeStatus }}</span>
                    </el-table-column>
                    <el-table-column label="备注" prop="comment"></el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
			<div class="pull-right">
				<ListStatistics
					action="/assets/amountStatistics"
                    identifier="financialContractIds"
					:parameters="conditions">
					<template scope="statistics">
						<div>应还款总额<span class="pull-right">{{ statistics.data.plannedAmount | formatMoney }}</span></div>
						<div>足额还款总额<span class="pull-right">{{ statistics.data.fullAmount | formatMoney }}</span></div>
						<div>部分还款总额<span class="pull-right">{{ statistics.data.partAmount | formatMoney }}</span></div>
						<div>未偿总额<span class="pull-right">{{ statistics.data.differenceAmount | formatMoney }}</span></div>
					</template>
				</ListStatistics>
				<PageControl
	                v-model="pageConds.pageIndex"
	                :size="dataSource.size"
	                :per-page-record-number="pageConds.perPageRecordNumber">
	            </PageControl>
			</div>
		</div>

<!-- 		<ExportPreviewModal
            :parameters="conditions"
            :query-action="`/assets/preview-exprot-overDue-repayment-plan-detail`"
            :download-action="`/assets/exprot-overDue-repayment-plan-detail`"
			v-model="overDueRepaymentExportModal.show">
            <el-table-column label="uniqueid" prop="uniqueId"></el-table-column>
            <el-table-column :label="$utils.locale('financialContract.appAccount.name')" prop="appName"></el-table-column>
            <el-table-column label="贷款合同编号" prop="loanContractNo"></el-table-column>
            <el-table-column label="还款编号" prop="repaymentNo"></el-table-column>
            <el-table-column label="放款日期" prop="loanDate"></el-table-column>
            <el-table-column label="贷款客户姓名" prop="customerName"></el-table-column>
            <el-table-column label="计划还款日期" prop="assetRecycleDate"></el-table-column>
            <el-table-column label="实际还款日期" prop="actualRecycleDate"></el-table-column>
            <el-table-column label="当前期数" prop="currentPeriod"></el-table-column>
            <el-table-column label="差异天数" prop="daysDifference"></el-table-column>
            <el-table-column label="逾期天数" prop="auditOverdueDays"></el-table-column>
            <el-table-column label="应还款金额" prop="assetFairValue"></el-table-column>
            <el-table-column label="计划还款本金" prop="assetPrincipalValue"></el-table-column>
            <el-table-column label="计划还款利息" prop="assetInterestValue"></el-table-column>
            <el-table-column label="贷款服务费" prop="loanServiceFee"></el-table-column>
            <el-table-column label="技术维护费" prop="loanTechFee"></el-table-column>
            <el-table-column label="其他费用" prop="loanOtherFee"></el-table-column>
            <el-table-column label="逾期罚息" prop="overdueFeePenalty"></el-table-column>
            <el-table-column label="逾期违约金" prop="overdueFeeObligation"></el-table-column>
            <el-table-column label="逾期服务费" prop="overdueFeeService"></el-table-column>
            <el-table-column label="逾期其他费用" prop="overdueFeeOther"></el-table-column>
            <el-table-column label="实收金额" prop="paidUpAssetFairValue"></el-table-column>
            <el-table-column label="实收本金" prop="paidUpPrincipalValue"></el-table-column>
            <el-table-column label="实收利息" prop="paidUpInterestValue"></el-table-column>
            <el-table-column label="实收贷款服务费" prop="paidUpLoanServiceFee"></el-table-column>
            <el-table-column label="实收技术维护费" prop="paidUpLoanTechFee"></el-table-column>
            <el-table-column label="实收其他费用" prop="paidUpLoanOtherFee"></el-table-column>
            <el-table-column label="实收逾期罚息" prop="paidUpOverdueFeePenalty"></el-table-column>
            <el-table-column label="实收逾期违约金" prop="paidUpOverdueFeeObligation"></el-table-column>
            <el-table-column label="实收逾期服务费" prop="paidUpOverdueFeeService"></el-table-column>
            <el-table-column label="实收逾期其他费用" prop="paidUpOverdueFeeOther"></el-table-column>
            <el-table-column label="未偿本金" prop="unPaidPrincipalValue"></el-table-column>
            <el-table-column label="未偿利息" prop="unPaidInterestValue"></el-table-column>
            <el-table-column label="未偿其他" prop="unPaidTotalOtherFee"></el-table-column>
            <el-table-column label="退款金额" prop="refundAmount"></el-table-column>
            <el-table-column label="还款状态" prop="repaymentStatus"></el-table-column>
            <el-table-column label="逾期状态" prop="overDueStatus"></el-table-column>
            <el-table-column label="担保状态" prop="guaranteeStatus"></el-table-column>
            <el-table-column label="备注" prop="comment"></el-table-column>
		</ExportPreviewModal> -->

		<ExportPreviewModal
            :parameters="conditions"
            :query-action="`/assets/preview-exprot-repayment-management`"
            :download-action="`/report/export?reportId=5`"
            :canExportTags="true"
			v-model="repaymentPlanManagementExportModal.show">
            <el-table-column label="uniqueid" prop="uniqueId"></el-table-column>
            <el-table-column :label="$utils.locale('financialContract.no')" prop="financialContractNo"></el-table-column>
            <el-table-column :label="$utils.locale('financialContract.name')" prop="financialProjectName"></el-table-column>
            <el-table-column label="资产编号" prop="assetNo"></el-table-column>
            <el-table-column label="贷款合同编号" prop="contractNo"></el-table-column>
            <el-table-column label="贷款客户姓名" prop="customerName"></el-table-column>
            <el-table-column label="还款编号" prop="singleLoanContractNo"></el-table-column>
            <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo"></el-table-column>
            <el-table-column label="计划还款日期" prop="assetRecycleDate"></el-table-column>
            <el-table-column label="实际还款日期" prop="actualRecycleDate"></el-table-column>
            <el-table-column label="当前期数" prop="currentPeriod"></el-table-column>
            <el-table-column label="总期数" prop="allPeriods"></el-table-column>
            <el-table-column label="差异天数" prop="overDueDays"></el-table-column>
            <el-table-column label="逾期天数" prop="auditOverdueDays"></el-table-column>
            <el-table-column label="应还款金额" prop="amount"></el-table-column>
            <el-table-column label="计划还款金额" prop="assetInitialValue"></el-table-column>
            <el-table-column label="计划还款本金" prop="assetPrincipalValue"></el-table-column>
            <el-table-column label="计划还款利息" prop="assetInterestValue"></el-table-column>
            <el-table-column label="贷款服务费" prop="loanServiceFee"></el-table-column>
            <el-table-column label="技术维护费" prop="techMaintenanceFee"></el-table-column>
            <el-table-column label="其他费用" prop="otherFee"></el-table-column>
            <el-table-column label="逾期费用合计" prop="totalOverdueFee"></el-table-column>
            <el-table-column label="逾期罚息" prop="overduePenalty"></el-table-column>
            <el-table-column label="逾期违约金" prop="overdueDefaultFee"></el-table-column>
            <el-table-column label="逾期服务费" prop="overdueServiceFee"></el-table-column>
            <el-table-column label="逾期其他费用" prop="overdueOtherFee"></el-table-column>
            <el-table-column label="实收金额" prop="actualAmount"></el-table-column>
            <el-table-column label="退款金额" prop="refundAmount"></el-table-column>
            <el-table-column label="还款状态" prop="paymentStatus"></el-table-column>
            <el-table-column label="担保状态" prop="guaranteeStatus"></el-table-column>
            <el-table-column label="备注" prop="comment"></el-table-column>
            <el-table-column label="客户年龄" prop="age"></el-table-column>
            <el-table-column label="开户行所在省" prop="province"></el-table-column>
		</ExportPreviewModal>

		<!-- <ExportPreviewModal
            :parameters="queryConds"
            :query-action="`/assets/preview-exprot-repayment-plan-detail`"
            :download-action="`/assets/exprot-repayment-plan-detail`"
			v-model="repaymentPlanDetailExportModal.show">
            <el-table-column label="uniqueid" prop="uniqueId"></el-table-column>
            <el-table-column :label="$utils.locale('financialContract.no')" prop="financialContractNo"></el-table-column>
            <el-table-column :label="$utils.locale('financialContract.appAccount.name')" prop="appName"></el-table-column>
            <el-table-column label="贷款合同编号" prop="loanContractNo"></el-table-column>
            <el-table-column label="还款编号" prop="repaymentNo"></el-table-column>
            <el-table-column label="放款日期" prop="loanDate"></el-table-column>
            <el-table-column label="计划还款日期" prop="assetRecycleDate"></el-table-column>
            <el-table-column label="实际还款日期" prop="actualRecycleDate"></el-table-column>
            <el-table-column label="计划还款利息" prop="assetInterestValue"></el-table-column>
            <el-table-column label="计划还款本金" prop="assetPrincipalValue"></el-table-column>
            <el-table-column :label="$utils.locale('financialContract.account.no')" prop="financialAccountNo"></el-table-column>
            <el-table-column label="贷款客户姓名" prop="customerName"></el-table-column>
            <el-table-column label="贷款客户身份证号码" prop="idCardNo"></el-table-column>
            <el-table-column label="还款账户开户行名称" prop="bankName"></el-table-column>
            <el-table-column label="开户行所在省" prop="province"></el-table-column>
            <el-table-column label="开户行所在市" prop="city"></el-table-column>
            <el-table-column label="还款账户号" prop="payAcNo"></el-table-column>
            <el-table-column label="生效日期" prop="effectiveDate"></el-table-column>
		</ExportPreviewModal> -->

        <CreateTagModal
            v-model="createTagModel.show"
            :options="createTagModel.options"
            :tags="createTagModel.currentTags"
            :batch="createTagModel.batch"
            :size="createTagModel.size"
            :maxSize="2000"
            :type="createTagModel.type"
            :action="createTagModel.action"
            @submit="handleSubmitCreateTagModal"
            @submit-add-with="handleSubmitAddWithCreateTagModal"
            @batch-submit="handleBatchSubmitCreateTagModal"></CreateTagModal>
	</div>

</template>

<script>
	import Pagination from 'mixins/Pagination';
	import ListPage from 'mixins/ListPage';
	import { ajaxPromise, purify } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
    import CreateTagModal from 'components/CreateTagModal'

	export default {
		mixins: [Pagination, ListPage],
		components: {
			ExportPreviewModal: require('views/include/ExportPreviewModal'),
			ExportDropdown: require('views/include/ExportDropdown'),
			ComboQueryBox: require('views/include/ComboQueryBox'),
			ListStatistics: require('views/include/ListStatistics'),
            CreateTagModal
		},
		data: function() {
			return {
				action: '/assets/query',
                autoload: false,
                listenConditionChange: false,
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
                    financialContractIds: [],
                    paymentStatusOrdinals: [],
                    auditOverDueStatusOrdinals: [],
                    startDate: '',
                    endDate: '',
                    actualRecycleStartDate: '',
                    actualRecycleEndDate: '',
                    overDueStatus: '',
                    isRepayment: false //实际还款金额不为0
                },
				comboConds: {
					repaymentNo: '',
					contractNo: '',
					customerName: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

				financialContractQueryModels: [],
				paymentStatusList: [],
				auditOverdueStatusList: [],
				overDueStatusList: [],

				overDueRepaymentExportModal: {
					show: false
				},
				repaymentPlanManagementExportModal: {
					show: false
				},
                createTagModel: {
                    show: false,
                    options: [],
                    singleLoanContractNo: '',
                    currentTags: [],
                    dataSourceIndex: '',
                    batch: false,
                    size: '',
                    type: 0,//还款计划
                    action: 'add'//默认为新增
                },
			};
		},
		methods: {
			initialize: function() {
			    return ajaxPromise({
			    	url: `/assets/options`
			    }).then(data => {
			    	this.financialContractQueryModels = data.queryAppModels || [];
    	            this.paymentStatusList = data.paymentStatusList || [];
    	            this.auditOverdueStatusList = data.auditOverdueStatusList || [];
    	            this.overDueStatusList = data.overDueStatusList || [];

                    this.queryConds.paymentStatusOrdinals = this.paymentStatusList.map(item => item.key);
                    this.queryConds.auditOverDueStatusOrdinals = this.auditOverdueStatusList.map(item => item.key);
			    }).catch(message => {
			    	MessageBox.open(message);
			    });
			},
            onSuccess: function(data) {
                this.dataSource.list = data.list.map(item => {
                    item.statistics = {
                        planChargesDetail: {},
                        paidUpChargesDetail: {},
                        unPaidDetail: {}
                    };
                    return item;
                });
                this.dataSource.size = data.size;
            },
			handleOperate: function(command) {
                if (command === 'repaymentPlanManagement') {
    				this[command + 'ExportModal'].show = true;
                } else if(command === 'batchAddTags') {
                    this.batchHandlerTags('add');
                } else if(command === 'batchDeleteTags') {
                    this.batchHandlerTags('delete');
                }
			},
            handleCellClick: function(row, column, cell, e) {
                if (window.getSelection && column.property === 'contractNo') {
                    var range = document.createRange();
                    range.selectNode($(e.target)[0]);
                    window.getSelection().addRange(range); // 选择对象
                    document.execCommand('Copy'); // 执行浏览器复制命令
                }
            },
			fetchAssetSetStatistics: function(assetSetUuid) {
				var { list } = this.dataSource;
				var index = list.findIndex(item => item.assetSetUuid === assetSetUuid);

				if (index === -1) return;

                if (list[index].statistics.success) return;

				ajaxPromise({
					url: `/assets/${assetSetUuid}/feeDetail`,
				}).catch(error => {
                    this.$set(list[index], 'statistics', { error });
				}).then(data => {
					this.$set(list[index], 'statistics', Object.assign({
                        success: true,
                        planChargesDetail: {},
                        paidUpChargesDetail: {},
                        unPaidDetail: {}
                    }, data));
				});
			},
            handleClickDeleteCurrentLabel: function(tag, tags){
                ajaxPromise({
                    url: `tag/delcfg`,
                    data:{
                      uuid:tag.tagConfigUuid
                    },
                    type: 'post'
                }).then(data => {
                    var index = tags.findIndex(item => {
                        return item === tag
                    })
                    if(index != -1){
                        tags.splice(index,1)
                    }
                }).catch(message =>{
                    MessageBox.open(message)
                })
            },
            handleClickAddLabel: function(row, index){
                this.createTagModel.show = true;
                this.createTagModel.currentTags = row.tags;
                this.createTagModel.singleLoanContractNo = row.singleLoanContractNo;
                this.createTagModel.dataSourceIndex = index;
                this.createTagModel.action = 'add';
                this.createTagModel.batch = false;
                this.createTagModel.size = 1;
            },
            handleSubmitAddWithCreateTagModal: function(transitivityTagList, defaultTagList) {
                var data = {
                    no: this.createTagModel.singleLoanContractNo,
                    type: 0,
                    transitivityTagList: JSON.stringify(transitivityTagList),
                    defaultTagList: JSON.stringify(defaultTagList)
                };
                this.singleSubmit(data);
            },
            handleSubmitCreateTagModal: function(defaultTagList){
                var data = {
                    no: this.createTagModel.singleLoanContractNo,
                    type: 0,
                    defaultTagList: JSON.stringify(defaultTagList)
                }
                this.singleSubmit(data);
            },
            singleSubmit: function(postData) {
                ajaxPromise({
                    url: `/tag/add/single`,
                    data: postData,
                    type: 'post'
                }).then(data => {
                    MessageBox.open('添加标签成功');
                    this.dataSource.list[this.createTagModel.dataSourceIndex]['tags'] = data.data;
                    this.refreshTagNames();
                }).catch(message => {
                    MessageBox.open(message)
                }).then(()=>{
                    this.createTagModel.show = false;
                })
            },
            batchHandlerTags: function(action) {
                this.createTagModel.show = true;
                this.createTagModel.currentTags = [];
                this.createTagModel.batch = true;
                this.createTagModel.action = action;
                this.createTagModel.size = this.dataSource.size;
            },
            handleBatchSubmitCreateTagModal: function(postData) {
                let actionType = postData.actionType;
                let conditions = purify(Object.assign({},this.conditions));
                let url = postData.fileKey ? '/tag/submit/1' : '/tag/submit/2';
                let data = postData.fileKey ? postData : Object.assign(conditions, postData);
                ajaxPromise({
                    url: url,
                    type: 'post',
                    data: data
                }).then(data => {
                    MessageBox.open(actionType === 'add' ? '添加标签成功' : '删除标签成功');
                    this.fetch();
                    this.refreshTagNames();
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.createTagModel.show = false;
                });
            },
            refreshTagNames: function() {
                ajaxPromise({
                    url:`/tag/names`
                }).then(data=>{
                    this.createTagModel.options = data.data
                }).catch(message =>{
                    MessageBox.open(message)
                })
            },
            renderActualAmountHeader: function(h, { column, $index }) {
                return h('div', {}, [
                    h('span', {
                        style: {
                            color: '#999'
                        }
                    }, column.label),
                    h('i', {
                        'class': {
                            'icon': true,
                            'icon-except-zero': true,
                            'active': this.queryConds.isRepayment
                        },
                        style: {
                            'margin-left': '5px'
                        },
                        on: {
                            click: this.clickExceptZero
                        }
                    }, ''),
                ]);
            },
            clickExceptZero: function() {
                if (this.queryConds.isRepayment) return;
                this.queryConds.isRepayment = true;
                this.fetch();
            }
		},
        activated(){
           this.refreshTagNames();
        }
	}
</script>
