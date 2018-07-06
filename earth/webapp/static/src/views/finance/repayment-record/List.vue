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
						    v-model="queryConds.financialContractUuids"
						    :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
					</el-form-item>
				    <el-form-item>
                        <DatePicker
                            v-model="HappenDate"
                            size="small"
                            type="datetimerange"
                            placeholder="发生时间">
                        </DatePicker>
                    </el-form-item>
				    <el-form-item>
                        <DatePicker
                            v-model="ActualRecycleDate"
                            size="small"
                            type="datetimerange"
                            placeholder="实际还款时间">
                        </DatePicker>
                    </el-form-item>
				    <el-form-item>
				        <ComboQueryBox v-model="comboConds">
				            <el-option label="业务编号" value="repaymentBusinessNo">
				            </el-option>
				            <el-option label="贷款合同编号" value="contractNo">
				            </el-option>
				        </ComboQueryBox>
				    </el-form-item>
				    <el-form-item>
				        <el-button ref="lookup" size="small" type="primary">查询</el-button>
				    </el-form-item>
				    <el-form-item>
                        <el-button size="small" type="primary" :disabled="dataSource.size == 0 || dataSource.size == undefined" @click="handleExport">导出</el-button>
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
					<el-table-column inline-template label="业务编号">
						<a :href="row.capitalType != '回购' ? `${ctx}#/finance/assets/${row.assetSetUuid}/detail` : `${ctx}#/finance/repurchasedoc/${row.uuid}/detail`">{{ row.repaymentPlanNo }}</a>
					</el-table-column>
					<el-table-column prop="outerRepaymentPlanNo" label="商户还款计划编号">
					</el-table-column>
					<el-table-column inline-template :min-width="'130px'" label="贷款合同编号">
						<a :href="`${ctx}#/data/contracts/detail?uuid=${row.contractUuid}`">{{ row.contractNo }}</a>
					</el-table-column>
					<el-table-column prop="capitalType" label="还款方式">
					</el-table-column>
					<el-table-column prop="happenDate" label="发生时间" sortable="custom" inline-template>
						<div>{{ row.happenDate | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
					</el-table-column>
					<el-table-column prop="actualRecycleDate" label="实际还款时间" sortable="custom" inline-template>
						<div>{{ row.actualRecycleDate | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
					</el-table-column>
					<el-table-column 
						inline-template 
						prop="accountedDate"
						label="资金入账时间">
						<div>{{ row.accountedDate | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
					</el-table-column>
					<el-table-column 
						inline-template
						label="实收金额" 
						prop="amount"
						sortable="custom">
						<el-popover
							trigger="hover"
							placement="top">
							<div>
								<template v-if="row.capitalType != '回购'">
									<div>实收本金：{{ row.loanAssetPrincipal | formatMoney }}</div>
									<div>实收利息：{{ row.loanAssetInterest | formatMoney }}</div>
									<div>实收贷款服务费：{{ row.loanServiceFee | formatMoney }}</div>
									<div>实收技术维护费：{{ row.loanTechFee | formatMoney }}</div>
									<div>实收其他费用：{{ row.loanOtherFee | formatMoney }}</div>
									<div>实收逾期罚息：{{ row.overdueFeePenalty | formatMoney }}</div>
									<div>实收逾期违约金：{{ row.overdueFeeObligation | formatMoney }}</div>
									<div>实收逾期服务费：{{ row.overdueFeeService | formatMoney }}</div>
									<div>实收逾期其他费用：{{ row.overdueFeeOther | formatMoney }}</div>
								</template>
								<template v-else>
									<div>实收回购本金：{{ row.repurchasePrincipal | formatMoney }}</div>
									<div>实收回购利息：{{ row.repurchaseInterest | formatMoney }}</div>
									<div>实收回购罚息：{{ row.repurchasePenalty | formatMoney }}</div>
									<div>实收其他费用：{{ row.repurchaseOtherCharges | formatMoney }}</div>
								</template>
							</div>
							<span slot="reference">{{ row.totalFee | formatMoney }}</span>
						</el-popover>
					</el-table-column>
					<el-table-column prop="delayDays" label="延迟入账天数">
					</el-table-column>
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

	</div>

</template>

<script>
	import Pagination from 'mixins/Pagination';
	import ListPage from 'mixins/ListPage';
	import { ajaxPromise, downloadFile, purify } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import format from 'filters/format';
	import DatePicker from 'components/DatePicker'

	export default {
		mixins: [Pagination, ListPage],
		components: { 
			ComboQueryBox: require('views/include/ComboQueryBox'),
			DatePicker
		},
		data: function() {
			return {
				action: '/repayment-record/repaymentRecord/query',
				autoload: false,
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				HappenDate:[],
				ActualRecycleDate:[],
				queryConds: {
					financialContractUuids: [],
					startHappenDate : '',
					endHappenDate : '',
					startActualRecycleDate : '',
					endActualRecycleDate : '',
				},
				comboConds: {
					repaymentBusinessNo: '',
					contractNo: ''
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

				financialContractQueryModels: [],
			};
		},
		watch:{
            HappenDate: function(current){
                this.queryConds.startHappenDate = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.endHappenDate = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            },
			ActualRecycleDate: function(current){
                this.queryConds.startActualRecycleDate = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.endActualRecycleDate = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            },
		},
		methods: {
			initialize: function() {
			    return ajaxPromise({
			    	url: `/repayment-record/repayment/optionsData`
			    }).then(data => {
			    	this.financialContractQueryModels = data.queryAppModels || [];
			    }).catch(message => {
			    	MessageBox.open(message);
			    });
			},
			handleExport: function() {
			    if (this.queryConds.financialContractUuids.length > 1) {
			        MessageBox.open('信托合同只能选一个');
			        return
			    }
			    const source = Object.assign({}, this.conditions);
			    source.isAsc = source.isAsc === 'ascending'
                        ? true
                        : source.isAsc === 'descending'
                            ? false
                            : null
			    ajaxPromise({
			        url: '/repayment-record/repaymentRecord/countNums',
			        data: purify(source)
			    }).then(data => {
			       MessageBox.open('导出明细总数大约为:' + data.size + '条', '提示', [{
			            text: '导出',
			            type: 'success',
			            handler: () => {
			                downloadFile(`/report/export?reportId=21`,source);
			                setTimeout(MessageBox.close(), 1000)
			            }
			       }, {
			            text: '取消',
			            handler: () => MessageBox.close()
			       }])
			    }).catch(message => {
			        MessageBox.open(message);
			    });
			},
			onSortChange: function({column, prop, order}) {
				var sortField;
				if (prop === 'happenDate') {
					sortField = 'issuedTime';
				} else if(prop === 'actualRecycleDate') {
					sortField = 'tradeTime';
				} else if(prop === 'amount') {
					sortField = 'bookingAmount';
				} else {
					sortField = prop;
				}
	            this.sortConds.sortField = sortField;
	            this.sortConds.isAsc = order;
	        },
		}
	}
</script>