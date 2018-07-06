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
                            @getFlatCollection="getFlatCollection(arguments[0])"
                            :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
					</el-form-item>
					<el-form-item>
						<el-select 
							v-model="queryConds.accountSide" 
							name="accountSide"
							placeholder="收支类型" 
							clearable 
							size="small">
							<el-option
								v-for="item in accountSideList"
								:label="item.value"
								:value="item.key">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select
							v-model="queryConds.transactionType"
							name="transactionType"
							size="small"
							clearable 
							placeholder="交易类型">
							<el-option
								v-for="item in virtualAccountTransactionTypeList"
								:label="item.value"
								:value="item.key">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-col :span="11">
							<el-form-item>
								<DateTimePicker
									v-model="queryConds.startDate"
									name="startDate"
									pickTime="true"
									formatToMinimum="true"
									:end-date="queryConds.endDate"
									placeholder="发生起始日期"
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
									pickTime="true"
									formatToMaximum="true"
									:start-date="queryConds.startDate"
									placeholder="发生终止日期"
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
							<el-option label="账户编号" value="virtualAccountNo"></el-option>
							<el-option label="交易号" value="businessDocumentNo"></el-option>
							<el-option label="账户名称" value="virtualAccountAlias"></el-option>
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
					<el-table-column prop="virtualAccountFlowNo" label="账户流水号">
					</el-table-column>
					<el-table-column 
						inline-template 
						label="交易号">
						<div>
							<a  v-if="row.transactionType == 'DEPOSIT'" :href="`${ctx}#/capital/account/deposit-receipt/${ row.businessDocumentUuid }/detail`">{{ row.businessDocumentNo }}</a>
						    <a v-else-if="row.transactionType == 'INTER_ACCOUNT_REMITTANCE'" :href="`${ctx}#/capital/account/payment-order/${ row.businessDocumentUuid }/detail`">{{ row.businessDocumentNo }}</a>
						    <a v-else-if="row.transactionType == 'INTER_ACCOUNT_BENEFICIARY'" :href="`${ctx}#/capital/account/refund-order/${ row.businessDocumentUuid }/detail`">{{ row.businessDocumentNo }}</a>
						    <a v-else-if="row.transactionType == 'DEPOSIT_CANCEL'" :href="`${ctx}#/capital/account/recharge-revoke/${ row.businessDocumentUuid }/detail`">{{ row.businessDocumentNo }}</a>
						</div>
					</el-table-column>
					<el-table-column prop="accountSideName" label="收支类型">
					</el-table-column>
					<el-table-column 
						inline-template
					 	label="账户编号">
						<a :href="`${ctx}#/capital/account/virtual-acctount/${ row.virtualAccountUuid }/detail`">{{ row.virtualAccountNo }}</a>
					</el-table-column>
					<el-table-column prop="virtualAccountAlias" label="账户名称">
					</el-table-column>
					<el-table-column 
						inline-template 
						label="发生金额" 
						prop="transactionAmount"
						sortable="custom">
						<div>{{ row.transactionAmount | formatMoney}}</div>
					</el-table-column>
					<el-table-column 
						inline-template 
						label="瞬时余额"
						prop="balance"
						sortable="custom">
						<div>
							<span v-if="row.balance">{{ row.balance | formatMoney }}</span>
							<span v-else></span>
						</div>
					</el-table-column>
					<el-table-column prop="transactionTypeName" label="交易类型">
					</el-table-column>
					<el-table-column 
						inline-template
						label="发生时间"
						prop="createTime"
						sortable="custom">
						<div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
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
				action: '/capital/customer-account-manage/account-flow-list/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					accountSide: -1,
					transactionType: -1,
					endDate: '',
					startDate: ''
				},
				comboConds: {
					virtualAccountNo: '',
					businessDocumentNo: '',
					virtualAccountAlias: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},
				financialContractQueryModels: [],
				accountSideList:[],
				virtualAccountTransactionTypeList: [],
				flatCollection: []

			};
		},
		activated: function() {
			this.setRedirectWatch();
		},
		methods: {
			initialize: function() {
				return ajaxPromise({
					url: `/capital/customer-account-manage/account-flow-list/show/options`
				}).then(data => {	
					this.accountSideList = data.accountSideList || [];
					this.virtualAccountTransactionTypeList = data.virtualAccountTransactionTypeList || [];
					this.financialContractQueryModels = data.queryAppModels || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			getFlatCollection: function(data) {
				this.flatCollection = data;
				this.setRedirectWatch();
			},
			setRedirectWatch: function() {
				var self = this;
				if (this.$route.query.isRedirect) {
					if (!this.$route.query.financialContractUuids) return;
					var financialContractUuid = JSON.parse(this.$route.query.financialContractUuids)[0];
					this.flatCollection.forEach(item => {
						if (item.value == financialContractUuid) {
							setTimeout(() => {
								self.queryConds.financialContractUuids = [item];
							}, 0);
						}
					})
				}
			}
		}
	}
</script>