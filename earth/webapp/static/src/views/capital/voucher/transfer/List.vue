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
						<el-select size="small" v-model="queryConds.paymentInstitutionNames" placeholder="交易网关" clearable>
							<el-option
                                v-for="item in paymentInstitutionNames"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select size="small" v-model="queryConds.sourceDocumentStatus" placeholder="凭证状态" clearable>
							<el-option
                                v-for="item in sourceDocumentStatus"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
                        <DatePicker
                            v-model="createdTimeRange"
                            size="small"
                            type="datetimerange"
                            placeholder="创建时间">
                        </DatePicker>
                    </el-form-item>
                    <el-form-item>
                        <DatePicker
                            v-model="statusModifyTimeRange"
                            size="small"
                            type="datetimerange"
                            placeholder="状态变更时间">
                        </DatePicker>
                    </el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option label="机构账户名" value="paymentName"></el-option>
							<el-option label="机构账户号" value="paymentAccountNo"></el-option>
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
					<el-table-column width="180" label="凭证编号" inline-template>
						<a :href="`${ctx}#/capital/voucher/transfer/${row.voucherNo}/detail`">{{ row.voucherNo }}</a>
					</el-table-column>
					<el-table-column label="来往机构" prop="bankName"></el-table-column>
					<el-table-column label="机构账户名" prop="outlierCounterPartyName"></el-table-column>
					<el-table-column label="机构账户号" prop="outlierCounterPartyAccount"></el-table-column>
					<el-table-column label="借贷标记" prop="accountSide"></el-table-column>
					<el-table-column label="凭证金额" prop="voucherAmount"></el-table-column>
					<el-table-column label="交易网关" prop="transcationGatewayName"></el-table-column>
					<el-table-column label="创建时间" inline-template>
						<div>{{ row.createTime | formatDate("yyyy-MM-dd HH:mm:ss")}}</div>
					</el-table-column>
					<el-table-column label="状态变更时间" inline-template>
						<div>{{ row.lastModifiedTime | formatDate("yyyy-MM-dd HH:mm:ss")}}</div>
					</el-table-column>
					<el-table-column label="凭证状态" prop="voucherStatus"></el-table-column>
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
	import { ajaxPromise, purify } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import DatePicker from 'components/DatePicker';
	import format from 'filters/format';

	export default {
		mixins: [Pagination, ListPage],
		components: {
			ComboQueryBox: require('views/include/ComboQueryBox'),
			ExportPreviewModal: require('views/include/ExportPreviewModal'),
			DatePicker,
		},
		data: function() {
			return {
				action: '/audit/remittance/list/transferVoucher',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					paymentInstitutionNames: '',
					sourceDocumentStatus: '',
					startCreateTime: '',
                    endCreateTime: '',
                    startModifedTime: '',
                    endModifedTime: '',

				},
				comboConds: {
					paymentName: '',
					paymentAccountNo: ''
				},

				financialContractQueryModels: [],
				paymentInstitutionNames: [],
				sourceDocumentStatus: [],
				createdTimeRange: [],
                statusModifyTimeRange: [],
			};
		},
        watch: {
            createdTimeRange: function(current){
                this.queryConds.startCreateTime = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.endCreateTime = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            },
            statusModifyTimeRange: function(current){
                this.queryConds.startModifedTime = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.endModifedTime = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            }
        },
		activated: function() {
		},
		methods: {
			initialize: function() {
			    return this.getOptions();
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/audit/remittance/options/transferVoucher`
				}).then(data => {
					this.financialContractQueryModels = data.queryAppModels
					// 信托
					this.paymentInstitutionNames = data.paymentInstitutionNames
					// 通道
					this.sourceDocumentStatus = data.sourceDocumentStatus
					// 凭证状态
				}).catch(message => {
					MessageBox.open(message);
				});
			}
		}
	}
</script>
