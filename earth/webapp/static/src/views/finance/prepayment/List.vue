<template>
	<div class="content">
		<div class="scroller">
			<div class="query-area">
				<el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                       <!--  <el-select
                            v-model="queryConds.financialContractUuids"
                            :placeholder="$utils.locale('financialContract')"
                            size="small"
                            multiple>
                            <el-select-all-option
                                :options="financialContracts">
                            </el-select-all-option>
                            <el-option
                                v-for="item in financialContracts"
                                :label="item.contractName + '('+item.contractNo+')'"
                                :value="item.uuid">
                            </el-option>
                        </el-select> -->
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
                            v-model="queryConds.appIds"
                            placeholder="合作商户"
                            size="small"
                            multiple>
                            <el-select-all-option
                                :options="apps">
                            </el-select-all-option>
                            <el-option
                                v-for="item in apps"
                                :label="item.name"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <DateTimePicker
                            v-model="queryConds.startTime"
                            placeholder="提前还款日期"
                            size="small">
                        </DateTimePicker>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.paymentStatusOrdinals"
                            placeholder="还款状态"
                            size="small"
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
                            v-model="queryConds.prepaymentTypes"
                            placeholder="提前还款类型"
                            size="small"
                            multiple>
                            <el-select-all-option
                                :options="prepaymentTypes">
                            </el-select-all-option>
                            <el-option
                                v-for="item in prepaymentTypes"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="还款编号" value="singleLoanContractNo"></el-option>
                            <el-option label="贷款合同编号" value="contractNo"></el-option>
                            <el-option label="客户姓名" value="customerName"></el-option>
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
					v-loading="dataSource.fetching"
					:data="dataSource.list">
					<el-table-column label="还款编号" prop="contractNo" inline-template>
                        <a :href="`${ctx}#/finance/assets/${row.assetSetUuid}/detail`">{{ row.singleLoanContractNo }}</a>
                    </el-table-column>
                    <el-table-column label="贷款合同编号" prop="contractNo"></el-table-column>
                    <el-table-column label="客户姓名" prop="customerName"></el-table-column>
                    <el-table-column label="提前还款日期" prop="assetRecycleDate" inline-template>
                        <div>{{ row.assetRecycleDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="提前还款金额" prop="assetPrincipalValue" inline-template>
                        <div>{{ row.assetPrincipalValue | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="实际还款金额" prop="actualAmount" inline-template>
                        <div>{{ row.actualAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="还款状态" prop="paymentStatus"></el-table-column>
                    <el-table-column label="提前还款类型" prop="prepaymentType"></el-table-column>
                    <el-table-column label="还款单状态" prop="activeStatus"></el-table-column>
                    <el-table-column label="备注" prop=""></el-table-column>
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
				action: '/assets/prepayment/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
                    financialContractUuids: [],
                    appIds: [],
                    paymentStatusOrdinals: [],
                    prepaymentTypes: [],
                    startTime: '',

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

				financialContracts: [],
				apps: [],
				paymentStatusList: [],
				prepaymentTypes: [],
			};
		},
		methods: {
			initialize: function() {
			    return ajaxPromise({
			    	url: `/assets/prepayment/options`
			    }).then(data => {
			    	this.financialContracts = data.financialContracts;
    	            this.apps = data.apps;
    	            this.paymentStatusList = data.paymentStatusList;
    	            this.prepaymentTypes = data.prepaymentTypes;

                    this.queryConds.appIds = this.apps.map(item => item.id);
                    this.queryConds.paymentStatusOrdinals = this.paymentStatusList.map(item => item.key);
                    this.queryConds.prepaymentTypes = this.prepaymentTypes.map(item => item.key);
			    }).catch(message => {
			    	MessageBox.open(message);
			    });
			}
		}
	}
</script>
