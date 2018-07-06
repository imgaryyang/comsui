<template>
	<div class="content">
		<div class="scroller">
			<div class="query-area">
				<el-form class="sdf-form sdf-query-form" :inline="true">
					<el-form-item>
                        <el-select
                            v-model="queryConds.debitStrategyMode"
                            placeholder="收款策略模式"
                            size="small"
                            clearable
                            >
                            <el-option
                                v-for="item in debitStrategyMode"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
					</el-form-item>
					<el-form-item>
					 	<el-select
					 		v-model="queryConds.creditStrategyMode"
					 		placeholder="付款策略模式"
					 		size="small"
					 		clearable
					 		>
					 		<el-option
					 			v-for="item in creditStrategyMode"
					 			:label="item.value"
					 			:value="item.key">
					 		</el-option>
					 	</el-select>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option :label="$utils.locale('financialContract.name')" value="contractName">
							</el-option>
							<el-option :label="$utils.locale('financialContract.no')" value="contractNo">
							</el-option>
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
					v-loading="dataSource.fetching"
					:data="dataSource.list"
					stripe>
					<el-table-column :label="$utils.locale('financialContract.name')" prop="contractName">
					</el-table-column>
					<el-table-column :label="$utils.locale('financialContract.no')" prop="contractNo">
					</el-table-column>
					<el-table-column 
						label="清算绑定行号"
						prop="bankNameUionAccountNo">
					</el-table-column>
					<el-table-column 
						label="收款通道策略" 
						prop="debitPaymentChannelMode">
					</el-table-column>
					<el-table-column 
						label="付款通道策略" 
						prop="creditPaymentChannelMode">
					</el-table-column>
					<el-table-column label="操作" inline-template>
						<a v-if="ifElementGranted('switch-channel-strategy')" :href="`${ctx}#/financial/channel/switch/detail/${row.financialContractUuid}`">详情</a>
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
			ComboQueryBox: require('views/include/ComboQueryBox')
		},
		data: function() {
			return {
				action: '/paymentchannel/switch/search',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					debitStrategyMode: '',
					creditStrategyMode: '',
				},
				comboConds: {
					contractName: '',
					contractNo: ''
				},
				debitStrategyMode: [],
				creditStrategyMode: [],
			}
		},
		methods: {
			initialize: function() {
			    return this.getOptions();
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/paymentchannel/switch/list/option`
				}).then(data => {
					this.creditStrategyMode = data.creditStrategyMode;
					this.debitStrategyMode = data.debitStrategyMode;

				}).catch(message => {
					MessageBox.open(message);
				})
			}
		}
	}
</script>