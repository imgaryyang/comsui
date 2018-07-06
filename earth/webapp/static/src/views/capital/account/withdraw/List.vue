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
						<el-select size="small" v-model="queryConds.a" placeholder="提现方式" clearable>
							<el-option label="现金" value="0"></el-option>
							<el-option label="线上提现" value="1"></el-option>
							<el-option label="线下转账" value="2"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select size="small" v-model="queryConds.b" placeholder="提现状态" clearable>
							<el-option label="已创建" value="0"></el-option>
							<el-option label="处理中" value="1"></el-option>
							<el-option label="成功" value="2"></el-option>
							<el-option label="失败" value="3"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select size="small" v-model="queryConds.c" placeholder="冲账状态" clearable>
							<el-option label="未发生" value="0"></el-option>
							<el-option label="待冲账" value="1"></el-option>
							<el-option label="已冲账" value="2"></el-option>
							<el-option label="已退票" value="3"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-col :span="11">
							<el-form-item>
								<DateTimePicker
									v-model="queryConds.startDateString"
									:end-data="queryConds.endDateString"
									placeholder="生效起始日期"
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
									v-model="queryConds.endDateString"
									:start-date="queryConds.startDateString"
									placeholder="生效终止日期"
									size="small">
								</DateTimePicker>
							</el-form-item>
						</el-col>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option label="提现金额" value="a">
							</el-option>
							<el-option label="提现卡号" value="b">
							</el-option>
							<el-option label="开户行" value="c">
							</el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
					<el-form-item>
						<el-button :disabled="dataSource.list == 0" size="small" type="primary" @click="exportModal.show = true">导出</el-button>
					</el-form-item>
				</el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border"
					v-loading="dataSource.fetching"
					:data="dataSource.list"
					stripe>
					<el-table-column width="180" label="提现单号" inline-template>
						<a href="#">{{ row.a }}</a>
					</el-table-column>
					<el-table-column label="账户编号" inline-template>
						<a href="#">{{ row.b }}</a>
					</el-table-column>
					<el-table-column 
						label="账户名称"
						prop="c">
					</el-table-column>
					<el-table-column 
						label="银行账户号" 
						prop="d">
					</el-table-column>
					<el-table-column 
						label="账户姓名" 
						prop="e">
					</el-table-column>
					<el-table-column label="开户行" prop="f">
					</el-table-column>
					<el-table-column label="提现方式" prop="g">
					</el-table-column>
					<el-table-column label="创建时间" inline-template>
						<div>{{ row.h | formatDate("yyyy-MM-dd HH:mm:ss")}}月付</div>
					</el-table-column>
					<el-table-column label="提现金额" prop="i">
					</el-table-column>
					<el-table-column label="提现状态" prop="j">
					</el-table-column>
					<el-table-column 
						label="冲账状态" 
						prop="k">
					</el-table-column>
					<el-table-column
						prop="l"
						label="备注">
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

		<ExportPreviewModal
			:parameters="conditions"
			:query-action=""
			:download-action=""
			v-model="exportModal.show">
			<el-table-column prop="k" label="摘要">
			</el-table-column>
		</ExportPreviewModal>
	</div>
</template>

<script>
	import Pagination from 'mixins/Pagination';
	import ListPage from 'mixins/ListPage';
	import { ajaxPromise, purify } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		mixins: [Pagination, ListPage],
		components: {
			ComboQueryBox: require('views/include/ComboQueryBox'),
			ExportPreviewModal: require('views/include/ExportPreviewModal')
		},
		data: function() {
			return {
				action: '',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					contractStateOrdinals: [],
					startDateString: '',
					endDateString: ''
				},
				comboConds: {
					contractNo: '',
	                underlyingAsset: '',
	                customerName: '',
	                uniqueId: ''
				},

				financialContractQueryModels: [],
				contractStates: [],
				flatCollection: [],

				exportModal: {
					show: false
				},
			};
		},
		activated: function() {
		},
		methods: {
			initialize: function() {
			    return this.getOptions();
			},
			getOptions: function() {
				return ajaxPromise({
					url: ``
				}).then(data => {
				}).catch(message => {
					MessageBox.open(message);
				});
			}
		}
	}
</script>
