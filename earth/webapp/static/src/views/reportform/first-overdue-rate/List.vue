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
					    <DateTimePicker
                            type="date"
                            size="small"
                            v-model="queryConds.date"
                            placeholder="请选择日期">
                        </DateTimePicker>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
					<el-form-item>
						<el-button 
							size="small"
							type="primary"
							:disabled="dataSource.size<=0"
						    @click="download">
						    导出报表
						</el-button>
					</el-form-item>
				</el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border"
					stripe
					v-loading="dataSource.fetching"
					:data="dataSource.list"
					:empty-text="dataSource.error">
					<el-table-column
						prop="contractNo"
						:label="$utils.locale('financialContract.no')">
					</el-table-column>
					<el-table-column 
						prop="contractName"
						:label="$utils.locale('financialContract.name')">
					</el-table-column>
					<el-table-column
						inline-template
						label="日期">
						<span>{{ row.date | formatDate }}</span>
					</el-table-column>
					<el-table-column 
						inline-template
						label="首逾率(本金包含宽限日)">
						<el-popover trigger="hover" :disabled="rate1 == undefined">
							<div>
								({{ row.remainingPrincipalValue1 | formatMoney }}(未还总额)-{{ row.offlinePrincipalValue1 | formatMoney }}(线下还款)/{{ row.assetPrincipalValue1 | formatMoney }}(应还总额))
							</div>
							<span slot="reference">{{ row.rate1 | formatPercent }}</span>
						</el-popover>
					</el-table-column>
					<el-table-column 
						inline-template
						label="首逾率(本息包含宽限日)">
						<el-popover trigger="hover" :disabled="rate2 == undefined">
							<div>
								({{ row.remainingPrincipalValue1 + row.remainingInterestValue1 | formatMoney }}(未还总额)-{{ row.offlinePrincipalValue1 + row.offlineInterestValue1 | formatMoney }}(线下还款)/{{ row.assetPrincipalValue1 + row.assetInterestValue1 | formatMoney }}(应还总额))
							</div>
							<span slot="reference">{{ row.rate2 | formatPercent }}</span>
						</el-popover>
					</el-table-column>
					<el-table-column 
						inline-template
						label="首逾率(本金不包含宽限日)">
						<el-popover trigger="hover" :disabled="rate3 == undefined">
							<div>
								({{ row.remainingPrincipalValue | formatMoney }}(未还总额)-{{ row.offlinePrincipalValue | formatMoney }}(线下还款)/{{ row.assetPrincipalValue | formatMoney }}(应还总额))
							</div>
							<span slot="reference">{{ row.rate3 | formatPercent }}</span>
						</el-popover>
					</el-table-column>
					<el-table-column 
						inline-template
						label="首逾率(本息不包含宽限日)">
						<el-popover trigger="hover" :disabled="rate4 == undefined">
							<div>
								({{ row.remainingPrincipalValue + row.remainingInterestValue | formatMoney }}(未还总额)-{{ row.offlinePrincipalValue + row.offlineInterestValue | formatMoney }}(线下还款)/{{ row.assetPrincipalValue + row.assetInterestValue | formatMoney }}(应还总额))
							</div>
							<span slot="reference">{{ row.rate4 | formatPercent}}</span>
						</el-popover>
					</el-table-column>
					<el-table-column
						inline-template
						label="操作">
						<span>
							<el-button size="small" @click="handleUpdate(row, $index)">更新</el-button>
							<el-button size="small" @click="showHistory(row)">查看历史</el-button>
						</span>
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

		<ShowHistoryModal
			v-model="showHistoryModal.show"
			:model="showHistoryModal.model">
		</ShowHistoryModal>
	</div>
</template>

<script>
	import Pagination from 'mixins/Pagination';
	import ListPage from 'mixins/ListPage';
	import { ajaxPromise, purify, searchify, filterQueryConds } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import format from 'filters/format';

	export default {
		mixins: [Pagination, ListPage],
		components: { 
			ComboQueryBox: require('views/include/ComboQueryBox'),
			ShowHistoryModal: require('./include/ShowHistoryModal')
		},
		data: function() {
			return {
				action: '/firstOverdueRate/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					date: '',
				},

				financialContractQueryModels: [],

				showHistoryModal: {
					show: false,
					model: {
						financialContractUuid: '',
						date: '',
					}
				}
			};
		},
		methods: {
			initialize: function() {
				var date = new Date();
				date.setDate(date.getDate() - 1);
				this.queryConds.date = format.formatDate(date)
			    return this.getOptions();
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/firstOverdueRate/options`
				}).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			download: function(){
				var search = searchify(purify(filterQueryConds(this.queryConds)));
				var action = this.api + '/report/export?reportId=22&&' + search;
				window.open(action, '_download');
			},
			handleUpdate: function(row, index) {
				var self = this;
				MessageBox.open('交易繁忙时期,更新报表工作会占用较多性能是否继续执行?','更新首逾率', [{
					text: '取消',
					handler: () => MessageBox.close()
				},{
					text: '确定',
					handler: () => {
						ajaxPromise({
							url: '/firstOverdueRate/update',
							data: {
								date: format.formatDate(row.date),
								financialContractUuid: row.financialContractUuid
							}
						}).then(data => {
							MessageBox.close();
							if (data.row !=null) {
								self.dataSource.list[index] = Object.assign(self.dataSource.list[index], data.row);
							}
						}).catch(message => {
							MessageBox.open(message);
						});
					}
				}])
			},
			showHistory: function(row) {
				this.showHistoryModal.show = true;
				this.showHistoryModal.model = {
					financialContractUuid: row.financialContractUuid,
					date: format.formatDate(row.date)
				}
			}
		}
	}
</script>