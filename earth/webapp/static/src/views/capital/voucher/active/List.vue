<style type="sass">
    .color-red {
        color: red;
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
                            v-model="queryConds.financialContractUuids"
                            :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
					</el-form-item>
					<el-form-item>
						<el-select
							v-model="queryConds.voucherType"
							placeholder="凭证类型"
							size="small"
							clearable>
							<el-option
								v-for="item in voucherTypeList"
								:label="item.value"
								:value="item.key">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select
							v-model="queryConds.voucherStatus"
							placeholder="凭证状态"
							size="small"
							clearable>
							<el-option
								v-for="item in voucherStatusList"
								:label="item.value"
								:value="item.key">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-col :span="11">
							<el-form-item>
								<DateTimePicker
									v-model="queryConds.startDateString"
									:end-date="queryConds.endDateString"
									size="small"
                                    pickTime="true"
                                    formatToMinimum="true"
									placeholder="状态变更起始日">
								</DateTimePicker>
							</el-form-item>
						</el-col>
						<el-col :span="2">
							<div class="text-align-center color-dim">至</div>
						</el-col>
						<el-col :span="11">
							<el-form-item>
								<DateTimePicker
									v-model="queryConds.endDateString"
									:start-date="queryConds.startDateString"
									size="small"
                                    pickTime="true"
                                    formatToMaximum="true"
									placeholder="状态变更终止日">
								</DateTimePicker>
							</el-form-item>
						</el-col>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option label="专户账号" value="hostAccount"></el-option>
							<el-option label="还款方户名" value="counterName"></el-option>
							<el-option label="还款方账号" value="counterNo"></el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>

					<el-form-item>
						<el-button	
							size="small" 
							type="success"
							@click="createVoucherActive"
							v-if="ifElementGranted('create-active-voucher')">
							新增
						</el-button>
					</el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table 
                    class="no-table-bottom-border"
                    v-loading="dataSource.fetching"
                    @sort-change="onSortChange"
                    :data="dataSource.list"
                    stripe>
                    <el-table-column inline-template label="凭证编号">
                    	<a :href="`${ctx}#/capital/voucher/active/${row.voucherNo}/detail`">{{ row.voucherNo }}</a>
                    </el-table-column>
                    <el-table-column prop="receivableAccountNo" label="专户账号">
                    </el-table-column>
                    <el-table-column prop="paymentName" label="还款方户名">
                    </el-table-column>
                    <el-table-column prop="paymentAccountNo" label="还款方账号">
                    </el-table-column>
                  	<el-table-column 
                  		prop="amount"
                  		inline-template 
                  		label="凭证金额"
                  		sortable="custom">
                  		<div>{{ row.amount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column prop="voucherType" label="凭证类型">
                    </el-table-column>
                    <el-table-column prop="voucherSource" label="凭证来源">
                    </el-table-column>
                    <el-table-column prop="voucherStatusMessage" label="凭证状态" inline-template>
                        <div :class="row.voucherStatusMessage == '校验失败'? 'color-red' : ''"> {{ row.voucherStatusMessage }}</div>
                    </el-table-column>
                    <el-table-column 
                    	prop="lastModifiedTime" 
                    	inline-template 
                    	label="状态变更时间"
                    	sortable="custom">
                    	<div>{{ row.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
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
        		action: '/voucher/active/query',
        		pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
        		queryConds: {
                    financialContractUuids: [],
                    voucherType: '',
                    voucherStatus: '',
                    startDateString: '',
                    endDateString: ''
                },
        		comboConds: {
                    hostAccount: '',
                    counterName: '',
                    counterNo: ''
                },
        		sortConds: {
                    sortField: '',
                    isAsc: ''
                },

        		financialContractQueryModels: [],
        		voucherTypeList: [],
        		voucherStatusList: []
        	};
        },
        methods: {
            initialize: function() {
                return this.getOptions();
            },
        	getOptions: function() {
        		return ajaxPromise({
        			url: `/voucher/active/optionData`
        		}).then(data => {
        			this.financialContractQueryModels = data.queryAppModels || [];
        			this.voucherTypeList = data.voucherTypeList || [];
        			this.voucherStatusList = data.voucherStatusList || [];

        		}).catch(message => {
        			MessageBox.open(message);
        		})
        	},
        	createVoucherActive: function() {
        		location.assign(this.ctx + '#/capital/voucher/active/create');
        	}
        }
    }
</script>