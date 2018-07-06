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
							v-model="queryConds.paymentInstitutionOrdinals"
							placeholder="放款通道"
							size="small"
							multiple>
                            <el-select-all-option
                                :options="paymentInstitutionNames">
                            </el-select-all-option>
							<el-option
								v-for="item in paymentInstitutionNames"
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
									:end-date="queryConds.endDate"
									size="small"
									placeholder="创建日期起始">
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
									size="small"
									placeholder="创建日期终止">
								</DateTimePicker>
							</el-form-item>
						</el-col>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option label="通道流水号" value="channelCashFlowNo"></el-option>
							<el-option label="退回账户" value="returnedAccountNo"></el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button size="small" type="primary" ref="lookup">查询</el-button>
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
                    <el-table-column label="撤销单号" prop="remittanceRefundBillUuid"></el-table-column>
                    <el-table-column label="通道流水号" prop="channelCashFlowNo"></el-table-column>
                    <el-table-column label="通道名称" prop="paymentChannelName"></el-table-column>
                    <el-table-column label="发生时间" prop="createTime" inline-template sortable="custom">
                        <div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="退回账户" prop="hostAccountNo"></el-table-column>
                    <el-table-column label="冲账类型" prop="reverseType"></el-table-column>
                    <el-table-column label="金额" prop="amount" inline-template sortable="custom">
                        <div>{{ row.amount | formatMoney }}</div>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <div class="pull-right">
                <ListStatistics 
                    action="/capital/refundbill/amountStatistics"
                    :parameters="conditions">
                    <template scope="statistics">
                        <div>计划放款金额<span class="pull-right">{{ statistics.data.refundAmount | formatMoney }}</span></div>
                    </template>
                </ListStatistics>
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
            ListStatistics: require('views/include/ListStatistics'),
        	ComboQueryBox: require('views/include/ComboQueryBox'),
        },
        data: function() {
        	return {
        		action: '/capital/refundbill/query',
        		pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
        		queryConds: {
                    financialContractUuids: [],
                    paymentInstitutionOrdinals: [],
                    startDate: '',
                    endDate: '',
                },
        		comboConds: {
                    channelCashFlowNo: '',
                    returnedAccountNo: '',
                },
        		sortConds: {
                    sortField: '',
                    isAsc: ''
                },

        		financialContractQueryModels: [],
        		paymentInstitutionNames: [],
        	};
        },
        methods: {
            initialize: function() {
                return ajaxPromise({
                    url: `/capital/refundbill/options`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.paymentInstitutionNames = data.paymentInstitutionNames || [];
                    this.queryConds.paymentInstitutionOrdinals = this.paymentInstitutionNames.map(item => item.key);
                }).catch(message => {
                    MessageBox.open(message);
                });
            }
        }
    }
</script>