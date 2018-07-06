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
                            v-model="queryConds.appId"
                            placeholder="商户名称"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in appList"
                                :label="item.name"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.repoStartDate"
                                    :end-date="queryConds.repoEndDate"
                                    placeholder="回购起始日"
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
                                    v-model="queryConds.repoEndDate"
                                    :start-date="queryConds.repoStartDate"
                                    placeholder="回购截止日"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.repurchaseStatusOrdinals"
                            placeholder="回购状态"
                            size="small"
                            multiple>
                            <el-select-all-option
                                :options="repoStatus">
                            </el-select-all-option>
                            <el-option
                                v-for="item in repoStatus"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="贷款合同编号" value="contractNo"></el-option>
                            <el-option label="客户姓名" value="customerName"></el-option>
                            <el-option label="批次号" value="batchNo"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                    <el-form-item v-if="ifElementGranted('export-repurchasedoc')">
                        <ExportDropdown @command="exportModalShow = true">
                            <el-dropdown-item>回购汇总表</el-dropdown-item>
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
					<el-table-column label="回购单号" prop="repurchaseDocUuid" inline-template>
                        <a :href="`${ctx}#/finance/repurchasedoc/${ row.repurchaseDocUuid }/detail`">{{ row.repurchaseDocUuid }}</a>
                    </el-table-column>
                    <el-table-column label="贷款合同编号" prop="contractId" inline-template>
                        <a :href="`${ctx}#/data/contracts/detail?id=${ row.contractId }`">{{ row.contractNo }}</a>
                    </el-table-column>
                    <el-table-column label="批次号" prop="batchNo"></el-table-column>
                    <el-table-column label="回购起始日" prop="repoStartDate" inline-template>
                        <div>{{ row.repoStartDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="回购截止日" prop="repoEndDate" inline-template>
                        <div>{{ row.repoEndDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="商户名称" prop="appName"></el-table-column>
                    <el-table-column label="客户姓名" prop="customerName"></el-table-column>
                    <el-table-column label="回购金额" prop="amount" inline-template>
                        <div>{{ row.amount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="回购天数" prop="repoDays"></el-table-column>
                    <el-table-column label="发生时间" prop="creatTime" inline-template>
                        <div>{{ row.creatTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="回购状态" prop="repoStatus"></el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
			<div class="pull-right">
                <ListStatistics 
                    action="/repurchasedoc/amountStatistics"
                    identifier="financialContractUuids"
                    :parameters="conditions">
                    <template scope="statistics">
                        <div>回购金额<span class="pull-right">{{ statistics.data.repurchaseAmount | formatMoney }}</span></div>
                    </template>
                </ListStatistics>
				<PageControl 
	                v-model="pageConds.pageIndex"
	                :size="dataSource.size"
	                :per-page-record-number="pageConds.perPageRecordNumber">
	            </PageControl>
			</div>
		</div>

        <ExportPreviewModal
            :parameters="conditions"
            :query-action="`/repurchasedoc/preview-export`"
            :download-action="`/report/export?reportId=8`"
            v-model="exportModalShow">
            <el-table-column label="uniqueid" prop="uniqueId"></el-table-column>
            <el-table-column label="回购单号" prop="repurchaseDocUuid"></el-table-column>
            <el-table-column label="贷款合同编号" prop="contractNo"></el-table-column>
            <el-table-column label="批次号" prop="batchNo"></el-table-column>
            <el-table-column label="回购起始日" prop="repoStartDate"></el-table-column>
            <el-table-column label="回购截止日" prop="repoEndDate"></el-table-column>
            <el-table-column label="商户名称" prop="appName"></el-table-column>
            <el-table-column label="客户名称" prop="customerName"></el-table-column>
            <el-table-column label="回购金额" prop="amount"></el-table-column>
            <el-table-column label="回购本金" prop="repurchasePrincipal"></el-table-column>
            <el-table-column label="回购利息" prop="repurchaseInterest"></el-table-column>
            <el-table-column label="回购罚息" prop="repurchasePenalty"></el-table-column>
            <el-table-column label="回购其他费用" prop="repurchaseOtherCharges"></el-table-column>
            <el-table-column label="回购天数" prop="repoDays"></el-table-column>
            <el-table-column label="发生时间" prop="creatTime"></el-table-column>
            <el-table-column label="回购状态" prop="repoStatus"></el-table-column>
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
            ExportPreviewModal: require('views/include/ExportPreviewModal'),
            ExportDropdown: require('views/include/ExportDropdown'),
            ComboQueryBox: require('views/include/ComboQueryBox'),
            ListStatistics: require('views/include/ListStatistics'),
        },
		data: function() {
			return {
				action: '/repurchasedoc/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
                    financialContractUuids: [],
                    appId: '',
                    repoStartDate: '',
                    repoEndDate: '',
                    repurchaseStatusOrdinals: []
                },
				comboConds: {
					contractNo: '',
                    customerName: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},
				financialContractQueryModels: [],
				appList: [],
				repoStatus: [],

                exportModalShow: false
			};
		},
		methods: {
			initialize: function() {
			    return ajaxPromise({
			    	url: `/repurchasedoc/options`
			    }).then(data => {
			    	this.financialContractQueryModels = data.queryAppModels || [];
                    this.appList = data.appList || [];
                    this.repoStatus = data.repoStatus || [];

                    this.queryConds.repurchaseStatusOrdinals = this.repoStatus.map(item => item.key);
			    }).catch(message => {
			    	MessageBox.open(message);
			    });
			},
		}
	}
</script>