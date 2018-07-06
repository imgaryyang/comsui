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
									v-model="queryConds.transactionStratTime"
									:end-date="queryConds.transactionEndTime"
									placeholder="入账起始日"
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
									v-model="queryConds.transactionEndTime"
									:start-date="queryConds.transactionStratTime"
									placeholder="入账终止日"
									size="small">
								</DateTimePicker>
							</el-form-item>
						</el-col>
					</el-form-item>
					<el-form-item>
						<el-col :span="11">
							<el-form-item>
								<DateTimePicker
									v-model="queryConds.createStartTime"
									:end-date="queryConds.createEndTime"
									placeholder="导入起始日"
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
									v-model="queryConds.createEndTime"
									:start-date="queryConds.createStartTime"
									placeholder="导入终止日"
									size="small">
								</DateTimePicker>
							</el-form-item>
						</el-col>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option value="merchantOrderNo" label="商户订单号"></el-option>
							<el-option value="channelSequenceNo" label="支付流水号"></el-option>
							<el-option value="counterAccountName" label="客户姓名"></el-option>
							<el-option value="counterAccountNo" label="银行账号"></el-option>
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
					<el-table-column prop="merchantOrderNo" label="商户订单号">
					</el-table-column>
					<el-table-column 
						prop="channelSequenceNo"
						label="支付流水号">
					</el-table-column>
					<el-table-column label="交易类型" prop="accountSide"></el-table-column>
					<el-table-column 
						inline-template
						label="交易金额">
						<div>{{ row.transactionAmount | formatMoney}}</div>
						</el-table-column>
					<el-table-column 
						label="币种" 
						prop="currency">
					</el-table-column>
					<el-table-column 
						label="客户姓名" 
						prop="counterAccountName">
					</el-table-column>
					<el-table-column 
						label="银行账号" 
						prop="counterAccountNo">
					</el-table-column>
					<el-table-column label="开户行" prop="banks"></el-table-column>
					<el-table-column 
						inline-template
						label="入账时间" 
						prop="transactionTime">
							<div>{{ row.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
						</el-table-column>
					<el-table-column 
						inline-template
						label="导入时间">
						<div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>	
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
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		mixins: [Pagination, ListPage],
		components: { 
			ComboQueryBox: require('views/include/ComboQueryBox'),
		},
		data: function() {
			return {
				action: '/bill/third-party/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractIds: [],
					transactionStratTime: '',
					transactionEndTime: '',
					createStartTime: '',
					createEndTime: '',
				},
				comboConds: {
					merchantOrderNo: '',
					channelSequenceNo: '',
					counterAccountName: '',
					counterAccountNo: '',
				},

				financialContractQueryModels: [],
				debitStatusList: [],
				creditStatusList: [],
				businessTypeList: [],

				showDetail: false,
				detail: {}
			};
		},
		methods: {
			initialize: function() {
			    return this.getOptions();
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/bill/third-party/optionData`
				}).then(data => {
			    	this.financialContractQueryModels = data.queryAppModels || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			}
		}
	}
</script>