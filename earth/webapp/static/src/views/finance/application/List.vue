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
                            v-model="queryConds.repaymentType"
                            placeholder="订单类型"
                            size="small"
                            multiple>
                            <el-select-all-option
                                :options="repaymentType">
                            </el-select-all-option>
                            <el-option
                                v-for="item in repaymentType"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.executionStatus"
                            placeholder="订单状态"
                            size="small"
                            multiple>
                            <el-select-all-option
                                :options="orderStatus">
                            </el-select-all-option>
                            <el-option
                                v-for="item in orderStatus"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.createStartDate"
                                    name="startDate"
                                    pickTime="true"
                                    formatToMinimum="true"
                                    :end-date="queryConds.createEndDate"
                                    placeholder="创建起始时间"
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
                                    v-model="queryConds.createEndDate"
                                    name="endDate"
                                    pickTime="true"
                                    formatToMaximum="true"
                                    :start-date="queryConds.createStartDate"
                                    placeholder="创建终止时间"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="客户姓名" value="customerName"></el-option>
                            <el-option label="贷款合同编号" value="loanContractNo"></el-option>
                            <el-option label="订单编号" value="deudctNo"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                    <el-form-item v-if="!ifElementGranted('exprot-application')">
                        <ExportDropdown @command="deductApplicationModal.show = true">
                            <el-dropdown-item command="deductApplication">扣款订单表</el-dropdown-item>
                        </ExportDropdown>
                    </el-form-item>
                </el-form>
			</div>
			<div class="table-area">
                <el-table
                    class="no-table-bottom-border"
                    stripe
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column label="订单编号" inline-template>
                        <a :href="`${ctx}#/finance/application/${ row.deudctNo }/detail`">{{ row.deudctNo }}</a>               
                    </el-table-column>
                    <el-table-column label="贷款合同编号" prop="loanContractNo"></el-table-column>
                    <el-table-column label="客户姓名" prop="customerName"></el-table-column>
                    <el-table-column label="创建时间" inline-template>
                        <div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="订单类型" prop="repaymentType">
                    </el-table-column>
                    <el-table-column label="扣款金额" inline-template>
                        <div>{{ row.planDeductAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="状态变更时间" prop="statusModifyTime"></el-table-column>
                    <el-table-column label="状态" inline-template>
                        <div>
                            <span v-if="row.deductStatus =='异常'" class="color-warning">异常</span>
                            <span v-else-if="deductStatus == '失败'" class="color-danger">失败</span>
                            <span v-else>{{ row.deductStatus }}</span>
                        </div>
                    </el-table-column>
                    <el-table-column label="备注" prop="remark"></el-table-column>
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
            :query-action="`/deduct/application/preview-exprot-application`"
            :download-action="`/report/export?reportId=18`"
            v-model="deductApplicationModal.show">
            <el-table-column label="订单编号" prop="deudctNo"></el-table-column>
            <el-table-column label="贷款合同编号" prop="loanContractNo"></el-table-column>
            <el-table-column label="客户姓名" prop="customerName"></el-table-column>
            <el-table-column label="创建时间" prop="createTime"></el-table-column>
            <el-table-column label="订单类型" prop="repaymentType"></el-table-column>
            <el-table-column label="扣款金额" prop="planDeductAmount"></el-table-column>
            <el-table-column label="状态变更时间" prop="statusModifyTime"></el-table-column>
            <el-table-column label="状态" prop="deductStatus"></el-table-column>
            <el-table-column label="备注" prop="remark"></el-table-column>
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
            ExportPreviewModal: require('views/include/ExportPreviewModal'),
            ExportDropdown: require('views/include/ExportDropdown')
        },
		data: function() {
			return {
				action: '/deduct/application/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
                    financialContractIds: [],
                    repaymentType: [],
                    executionStatus: [],
                    createEndDate: '',
                    createStartDate: '',

                },
				comboConds: {
					singleLoanContractNo: '',
					contractNo: '',
					customerName: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

				financialContractQueryModels: [],
				repaymentType: [],
                orderStatus: [],

                deductApplicationModal: {
                    show: false
                }
			};
		},
        computed: {
            conditions: function() {
                var currentQueryConds = this.filterQueryConds(this.queryConds);
                return Object.assign({}, currentQueryConds, this.comboConds, this.sortConds, this.pageConds);
            },
        },
		methods: {
			initialize: function() {
			    return ajaxPromise({
			    	url: `/deduct/application/options`
			    }).then(data => {
			    	this.financialContractQueryModels = data.queryAppModels || [];
    	            this.repaymentType = data.repaymentType || [];
    	            this.orderStatus = data.orderStatus || [];

                    this.queryConds.repaymentType = this.repaymentType.map(item => item.key);
                    this.queryConds.executionStatus = this.orderStatus.map(item => item.key);
			    }).catch(message => {
			    	MessageBox.open(message);
			    });
			},
            filterQueryConds: function(queryConds) {
                var list;
                var currentQueryConds = Object.assign({}, queryConds);
                if (currentQueryConds.financialContractIds && currentQueryConds.financialContractIds.length) {
                    list = currentQueryConds.financialContractIds || [];
                    currentQueryConds.financialContractIds = list.map(item => item.value);
                }
                return currentQueryConds;
            }
		}
	}
</script>