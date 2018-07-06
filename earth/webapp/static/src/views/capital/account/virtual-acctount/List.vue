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
						<el-select 
							v-model="queryConds.customerType" 
							name="customerType"
							placeholder="客户类型" 
							clearable 
							size="small">
							<el-option
								v-for="item in customerTypeLists"
								:label="item.value"
								:value="item.key">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select
							v-model="queryConds.virtualAccountStatus"
							name="virtualAccountStatus"
							size="small"
							clearable 
							placeholder="账户状态">
							<el-option
								v-for="item in virtualAccountStatusLists"
								:label="item.value"
								:value="item.key">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<span class="item vertical-line"></span>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option label="账户名称" value="virtualAccountAlias"></el-option>
							<el-option label="账户编号" value="virtualAccountNo"></el-option>
							<el-option label="账户余额" value="amount"></el-option>
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
					<el-table-column inline-template label="账户编号">
						<a :href="`${ctx}#/capital/account/virtual-acctount/${ row.virtualAccountUuid }/detail`">{{ row.virtualAccountNo }}</a>
					</el-table-column>
					<el-table-column prop="virtualAccountAlias" label="账户名称">
					</el-table-column>
					<el-table-column prop="customerTypeName" label="客户类型">
					</el-table-column>
					<el-table-column prop="contractName" :label="$utils.locale('financialContract.name')">
					</el-table-column>
					<el-table-column 
						inline-template 
						label="贷款合同编号">
						<a :href="`${ctx}#/data/contracts/detail?id=${ row.contractId }`">{{ row.contactNo }}</a>
					</el-table-column>
					<el-table-column 
						inline-template 
						width="115px" 
						label="账户余额" 
						prop="totalBalance"
						sortable="custom">
						<div>{{ row.totalBalance | formatMoney}}</div>
					</el-table-column>
					<el-table-column 
						inline-template 
						width="115px" 
						label="银行账户名">
						<el-popover trigger="hover">
							<div>
								<div>账户名: {{ row.accountName }}</div>
								<div>账户号: {{ row.accountNo }}</div>
								<div>开户行: {{ row.bankName }}</div>
								<div>所在地: {{ row.location }}</div>
								<div>证件号: {{ row.desensitizationIdCardNo }}</div>
							</div>
							<span slot="reference">{{ row.accountName }}</span>
						</el-popover>
					</el-table-column>
					<el-table-column 
						inline-template 
						label="创建时间" 
						prop="createTime"
						sortable="custom">
						<div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
					</el-table-column>
					<el-table-column prop="virtualAccountStatusName" label="账户状态">
					</el-table-column>
					<el-table-column label="操作">
<!-- 						<a href="javascript: void 0" style="cursor: pointer; color: #436ba7;" @click="withdrawDeposit(row)">提现</a> -->
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
		<EditWithdrawModal 
			v-model="editWithdrawModalVisible" 
			:model="editWithdrawModel"></EditWithdrawModal>
	</div>

</template>

<script>
	import Pagination from 'mixins/Pagination';
	import ListPage from 'mixins/ListPage';
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import EditWithdrawModal from './EditWithdrawModal'

	export default {
		mixins: [Pagination, ListPage],
		components: { 
			ComboQueryBox: require('views/include/ComboQueryBox'),
			EditWithdrawModal
		},
		data: function() {
			return {
				action: '/capital/customer-account-manage/virtual-account-list/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					virtualAccountStatus: '',
					customerType: '',
				},
				comboConds: {
					virtualAccountAlias: '',
					virtualAccountNo: '',
					amount: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},
				financialContractQueryModels: [],
				customerTypeLists:[],
				virtualAccountStatusLists:[],

				editWithdrawModalVisible: false,
				editWithdrawModel: {},
			};
		},
		methods: {
			initialize: function() {
				return ajaxPromise({
					url: `/capital/customer-account-manage/virtual-account-list/show/options`
				}).then(data => {	
					this.financialContractQueryModels = data.queryAppModels || [];
					this.customerTypeLists = data.customerTypeList || [];
					this.virtualAccountStatusLists = data.virtualAccountStatusList || [];

					var queryConds = this.queryConds;

				}).catch(message => {
					MessageBox.open(message);
				});
			},
			withdrawDeposit: function(row){
				this.editWithdrawModalVisible = true;
				console.log(row)
			}
		}
	}
</script>