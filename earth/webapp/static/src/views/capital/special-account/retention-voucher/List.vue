<style lang="sass">
	
</style>

<template>
	<div class="content" id="repayment-order">
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
                            v-model="queryConds.customerTypeList"
                            placeholder="客户类型"
                            clearable
                            size="small"
                            multiple>
                            <el-select-all-option
                                :options="customerParameters">
                            </el-select-all-option>
                            <el-option
                                v-for="item in customerParameters"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.tmpDepositUseStatus"
                            placeholder="使用状态"
                            clearable
                            multiple
                            size="small">
                            <el-select-all-option
                                :options="tmpDepositUseStatusParameters">
                            </el-select-all-option>
                            <el-option
                                v-for="item in tmpDepositUseStatusParameters"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    :pickTime="true"
                                    :formatToMinimum="true"
                                    v-model="queryConds.startDateTimeString"
                                    :end-date="queryConds.endDateTimeString"
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
                                    :pickTime="true"
                                    :formatToMaximum="true"
                                    v-model="queryConds.endDateTimeString"
                                    :start-date="queryConds.startDateTimeString"
                                    placeholder="创建终止时间"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="客户姓名" value="customerName"></el-option>
                            <el-option label="滞留单号" value="tmpDepositNo"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                </el-form>
			</div>
			<div class="table-area">
				<el-table
					stripe
                    class="no-table-bottom-border"
					v-loading="dataSource.fetching"
					:data="dataSource.list">
					<el-table-column label="滞留单号" prop="temporaryDepositDocNo" inline-template>
                        <a :href="`${ctx}#/capital/special-account/retention-voucher/${row.temporaryDepositDocUuid}/detail`">{{row.temporaryDepositDocNo }}</a>
                    </el-table-column>
                    <el-table-column label="客户姓名" prop="customerName">
                    </el-table-column>
                    <el-table-column label="客户类型" prop="customerType">
                    </el-table-column>
                    <el-table-column label="滞留金额(排序)" prop="totalAmount">
                    </el-table-column>
                    <el-table-column label="剩余金额(排序)" prop="remainAmount" inline-template>
                        <div>{{ row.remainAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="创建时间" prop="createdTime" inline-template>
                        <div>{{ row.createdTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="来源凭证" prop="voucherNo" inline-template>
                        <a :href="getVoucherDetail(row)">{{row.voucherNo }}</a>
                    </el-table-column>
                    <el-table-column label="来源类型" prop="voucherSource">
                    </el-table-column>
                    <el-table-column label="使用状态" prop="tmpDepositStatus">
                    </el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
<!--             <div class="pull-left">
                <el-button @click="$router.push({path: '/capital/special-account/retention-voucher/merge', query: {t: new Date().getTime()}})">申请提现</el-button>
            </div> -->
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
    import { mapState } from 'vuex';

	export default {
		mixins: [Pagination, ListPage],
		components: {
			ComboQueryBox: require('views/include/ComboQueryBox')
		},
        computed: {
            ...mapState({
                financialContractQueryModels: state => {
                    return state.financialContract.financialContractQueryModels;
                }
            })
        },
        beforeMount: function() {
            this.$store.dispatch('getFinancialContractQueryModels');
        },
		data: function() {
			return {
				action: '/temporary-deposit-doc/query-temporary-deposit-doc',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					customerTypeList: [],
                    tmpDepositUseStatus: [],
					startDateTimeString: '',
					endDateTimeString: ''
				},
				comboConds: {
                    customerName: '',
                    tmpDepositNo: '',
				},

				financialContractQueryModels: [],
                customerParameters: [],
                tmpDepositUseStatusParameters: [],
			}
		},
		methods: {
			initialize: function() {
				return ajaxPromise({
					url: '/temporary-deposit-doc/gain-drop-down-parameters',
				}).then(data => {
                    this.customerParameters = data.customerParameters || [];
                    this.tmpDepositUseStatusParameters = data.tmpDepositUseStatusParameters || [];
                    this.feildParameters = data.feildParameters || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			},
            getVoucherDetail: function(row){
                if(row.voucherSource == "主动付款凭证"){
                    return `${this.ctx}#/capital/voucher/active/${row.voucherNo}/detail`
                }else if(row.voucherSource == "商户付款凭证"){
                    return `${this.ctx}#/capital/voucher/business/${row.voucherId}/detail`
                }else{
                    return '#'
                }
            }
		}
	}
</script>