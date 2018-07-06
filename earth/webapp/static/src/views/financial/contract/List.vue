<template>
	<div class="content">
		<div class="scroller">
			<div class="query-area">
				<el-form class="sdf-form sdf-query-form" :inline="true">
					<el-form-item>
						<el-select 
						    v-model="queryConds.appId"
						    size="small" 
						    clearable
						    placeholder="资产方">
						    <el-option
						        v-for="item in appList"
						        :label="item.name"
						        :value="item.id">
						    </el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select 
						    v-model="queryConds.financialContractType"
						    size="small" 
						    clearable
						    :placeholder="$utils.locale('financialContract.type')">
						    <el-option
						        v-for="(value, label) in financialContractTypeList"
								:label="label"
								:value="value">
						    </el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<span class="item vertical-line"></span>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option value="financialContractNo" :label="$utils.locale('financialContract.no')"></el-option>
							<el-option value="financialContractName" :label="$utils.locale('financialContract.name')"></el-option>
							<el-option value="financialAccountNo" :label="$utils.locale('financialContract.account.no')"></el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
					<el-form-item v-if="ifElementGranted('create-financialcontract')">
						<router-link :to="`/financial/contract/create`">
							<el-button size="small" type="success">
									<i class="glyphicon glyphicon-plus"></i>新增合同
							</el-button>
						</router-link>
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
					<el-table-column inline-template :label="$utils.locale('financialContract.no')">
						<a :href="`${ctx}#/financial/contract/${row.financialContractUuid}/detail`">{{ row.contractNo }}</a>
					</el-table-column>
					<el-table-column prop="contractName" :label="$utils.locale('financialContract.name')">
					</el-table-column>
					<el-table-column prop="app.name" label="资产方"></el-table-column>
					<el-table-column prop="financialContractTypeMsg" :label="$utils.locale('financialContract.type')" >
					</el-table-column>
					<el-table-column 
						inline-template 
						label="合同起始日"
						prop="advaStartDate"
						sortable="custom">
						<div>{{ row.advaStartDate | formatDate }}</div>
					</el-table-column>
					<el-table-column 
						inline-template 
						label="合同截止日" 
						sortable="custom"
						prop="thruDate">
						<div>{{ row.thruDate | formatDate }}</div>
					</el-table-column>
					<el-table-column prop="capitalAccount.accountNo" :label="$utils.locale('financialContract.account.no')"></el-table-column>
					<el-table-column prop="receiveTime" label="放款通道名称">
					</el-table-column>
					<el-table-column 
						inline-template 
						label="回款通道名称">
						<div>{{ row.paymentChannel ? row.paymentChannel.channelName : ''}}</div>
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
				action: '/financialContract/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					appId: '',
					financialContractType: '',
				},
				comboConds: {
					financialContractNo: '',
					financialContractName: '',
					financialAccountNo: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

				appList: [],
				financialContractTypeList: []
			};
		},
		methods: {
			initialize: function() {
			    return this.getOptions();
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/financialContract/optionData`
				}).then(data => {
					this.financialContractTypeList = data.financialContractTypeList || [];
					this.appList = data.appList || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			}
		}
	}
</script>