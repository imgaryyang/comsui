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
					        placeholder="报表类型"	
					        size="small"
					        clearable
					        v-model="queryConds.type">
					        <el-option
					            v-for="(key,value) in requestIds"
					            :label="key"
					            :value="value">
					        </el-option>
					    </el-select>
					</el-form-item>
					<el-form-item>
					    <el-select
					        :placeholder="production === 'avictc' ? '任务状态' : '导出状态'"
					        v-model="queryConds.status"
					        clearable
					        size="small">
					        <el-option
					            v-for="item in reportJobStatusLists"
					            :label="item.value"
					            :value="item.key">
					        </el-option>
					    </el-select>
					</el-form-item>
					<el-form-item>
					    <el-col :span="11">
					        <el-form-item>
					            <DateTimePicker
					                v-model="queryConds.queryStartDate"
					                :end-date="queryConds.queryEndDate"
					                :pickTime="true"
					                name="loanEffectStartDate"
					                placeholder="创建起始日期"
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
					                v-model="queryConds.queryEndDate"
					                :start-date="queryConds.queryStartDate"
					                :pickTime="true"
					                name="loanEffectEndDate"
					                placeholder="创建截止日期"
					                size="small">
					            </DateTimePicker>
					        </el-form-item>
					    </el-col>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
					<!-- v-if="ifElementGranted('create-reportform-query-job')" -->
					<el-form-item>
						<el-button @click="production === 'avictc' ? createZHQueryModal.visible = true : createQueryModal.visible = true" size="small" type="primary">新建查询任务</el-button>
					</el-form-item>
				</el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border"
					stripe
					v-loading="dataSource.fetching"
					:data="dataSource.list">
					<el-table-column prop="id" label="任务标识"></el-table-column>
					<el-table-column 
						inline-template
						label="创建时间">
						<div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
					</el-table-column>
					<el-table-column prop="typeStr" label="报表类型">
					</el-table-column>
					<el-table-column 
						v-if="production === 'avictc'"
						inline-template
						:show-overflow-tooltip="true"
						:label="$utils.locale('financialContract.no') ">
				          <span>{{ row.financialProductCodes }}</span>
					</el-table-column>
					<el-table-column 
						v-else
						inline-template
						:show-overflow-tooltip="true"
						:label="$utils.locale('financialContract') ">
				          <span>{{ row.financialContractShowNames }}</span>
					</el-table-column>
					<el-table-column prop="" label="查询时间区间" inline-template>
						<div>
							{{ row.reportParams | reportTime }}
						</div>	
					</el-table-column>
					<el-table-column prop="status" label="导出状态"></el-table-column>
					<el-table-column label="操作" :width="180" inline-template>
						<div>
							<template v-if="production === 'avictc'">
								<el-upload
									accept=".xls"
									style="width: auto; display: inline-block;"
									:data="{'type': row.type, 'uuid': row.reportJobUuid}"
									:on-success="handleSuccess"
									:on-error="handleError"
									:show-upload-list="false"
									action="/zh/report/jobs/files">
									<el-button type="text">{{ !row.filePath ? '上传' : '重新上传'}}</el-button>
								</el-upload>
								<el-button type="text" :disabled="!row.filePath" @click="lookUpFile(row)">查看</el-button>
								<el-button type="text" @click="downloadTemplateFile(row.type)">下载模板</el-button>
							</template>
							<template v-else>
								<el-button type="primary" size="small" @click="download(row)" :disabled="row.status !='已完成'">开始下载</el-button>
							</template>
							<!-- <el-button type="primary" size="small" :disabled="row.success" @click="SendEmail(row)">发邮件</el-button> -->
						</div>
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

		<CreateQueryModal
			v-model="createQueryModal.visible"
			:financialContractQueryModels="financialContractQueryModels"
			:requestIds="requestIds"
			@submit="createQuery">
		</CreateQueryModal>

		<CreateZHQueryModal
			v-model="createZHQueryModal.visible"
			:financialContractQueryModels="financialContractQueryModels"
			:requestIds="requestIds"
			@submit="createQuery">
		</CreateZHQueryModal>

		<ConfigEmailModal v-model="configEmailModal.visible">
		</ConfigEmailModal>

		<ExportPreviewModal
			key="loanRepaymentExportModal"
			title="放款还款数据"
			:pagination="true"
			:parameters="loanRepaymentExportModal.parameters"
			:query-action="`/zh/report/jobs/files`"
			:download-action="`/zh/report/jobs/files/download`"
			v-model="loanRepaymentExportModal.show">
			<el-table-column prop="date" label="日期">
			</el-table-column>
			<el-table-column prop="loanAmount" label="放款金额">
			</el-table-column>
			<el-table-column prop="repaymentAmount" label="还款金额">
			</el-table-column>
			<el-table-column prop="repaymentPrincipal" label="还款本金">
			</el-table-column>
			<el-table-column prop="repaymentInterest" label="还款利息">
			</el-table-column>
			<el-table-column prop="repaymentCompoundInterest" label="还款复利">
			</el-table-column>
			<el-table-column prop="repaymentOverduePenalty" label="还款罚息">
			</el-table-column>
			<el-table-column prop="activePrincipal" label="主动还款-本金">
			</el-table-column>
			<el-table-column prop="activeInterest" label="主动还款-利息">
			</el-table-column>
			<el-table-column prop="unactivePrincipal" label="代扣-本金">
			</el-table-column>
			<el-table-column prop="unactiveInterest" label="代扣-利息">
			</el-table-column>
			<el-table-column prop="distributableInterest" label="可分配利息（累计金额）">
			</el-table-column>
			<el-table-column prop="dispositionOfIncome" label="收益分配">
			</el-table-column>
			<el-table-column prop="remark" label="备注">
			</el-table-column>
		</ExportPreviewModal>

		<ExportPreviewModal
			key="paymentInfoExportModal"
			title="专户流水数据"
			:pagination="true"
			:parameters="paymentInfoExportModal.parameters"
			:query-action="`/zh/report/jobs/files`"
			:download-action="`/zh/report/jobs/files/download`"
			v-model="paymentInfoExportModal.show">
			<el-table-column prop="accountNo" label="账号">
			</el-table-column>
			<el-table-column prop="accountName" label="账户名称">
			</el-table-column>
			<el-table-column prop="transactionTime" label="交易时间">
			</el-table-column>
			<el-table-column prop="debitAmount" label="借方发生额（支取）">
			</el-table-column>
			<el-table-column prop="creditAmount" label="贷方发生额（收入）">
			</el-table-column>
			<el-table-column prop="principal" label="本金">
			</el-table-column>
			<el-table-column prop="interest" label="利息">
			</el-table-column>
			<el-table-column prop="remark" label="备注">
			</el-table-column>
			<el-table-column prop="balance" label="余额">
			</el-table-column>
			<el-table-column prop="counterAccountName" label="对方户名">
			</el-table-column>
			<el-table-column prop="counterAccountNo" label="对方账号">
			</el-table-column>
			<el-table-column prop="counterBank" label="对方开户机构">
			</el-table-column>
			<el-table-column prop="chargeDate" label="记账日期">
			</el-table-column>
			<el-table-column prop="summary" label="摘要">
			</el-table-column>
			<el-table-column prop="otherRemark" label="备注">
			</el-table-column>
			<el-table-column prop="details" label="账户明细编号-交易流水号">
			</el-table-column>
			<el-table-column prop="transactionVoucherNo" label="企业流水号">
			</el-table-column>
			<el-table-column prop="voucherType" label="凭证种类">
			</el-table-column>
			<el-table-column prop="voucherNo" label="凭证号">
			</el-table-column>
		</ExportPreviewModal>
	</div>
</template>

<script>
	import Pagination from 'mixins/Pagination';
	import ListPage from 'mixins/ListPage';
	import { ajaxPromise, downloadFile} from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		mixins: [Pagination, ListPage],
		components: { 
			CreateQueryModal: require('./CreateQueryModal'),
			CreateZHQueryModal: require('./CreateZHQueryModal'),
			ConfigEmailModal: require('./ConfigEmailModal'),
			ComboQueryBox: require('views/include/ComboQueryBox'),
			ExportPreviewModal: require('views/include/ExportPreviewModal')
		},
		data: function() {
			return {
				action: this.production === 'avictc' ? '/zh/report/jobs/' : '/report/job/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					type:'',
					status:'',
					queryEndDate:'',
					queryStartDate: ''
				},
				createQueryModal: {
					visible: false,
				},
				createZHQueryModal: {
					visible: false,
				},
				configEmailModal: {
					visible: false
				},

				financialContractQueryModels: [],
				reportJobStatusLists: [],
				requestIds: {},
				financialContractUuidMap: {},

				loanRepaymentExportModal: {
					show: false,
					parameters: {}
				},
				paymentInfoExportModal: {
					show: false,
					parameters: {}
				}
			};
		},
		filters: {
			reportTime: function(value) {
				var data = JSON.parse(value);
				return '<'+ data.queryStartDate + '--' + data.queryEndDate + '>';
			}
		},
		methods: {
			initialize: function() {
			    return ajaxPromise({
			    	url: this.production === 'avictc' ? `/zh/report/jobs/options` :  `/report/job/options`
			    }).then(data => {
			    	this.financialContractQueryModels = data.queryAppModels || [];
			    	this.reportJobStatusLists = data.reportJobStatusList || [];
			    	this.requestIds = data.requestId || {};
			    	if (this.production != 'avictc') {
			    		this.financialContractUuidMap = data.financialContractUuidMap || []
			    	}
			    }).catch(message => {
			    	MessageBox.open(message);
			    });
			},
			onSuccess: function(data) {
				var d = this.parse(data);
				var self = this;
				this.dataSource.list = d.list.map(item => {
					item.typeStr = this.requestIds[item.type];
					if (self.production != 'avictc') {
						item.financialContractShowNames = !item.financialContractUuidList ? [] : item.financialContractUuidList.map(uuid => {
							return this.financialContractUuidMap[uuid];
						});
						item.financialContractShowNames = item.financialContractShowNames.join(', ');
					} 
					return item;	
				});
				this.dataSource.size = d.size;
				this.dataSource.error = '';
			},
			SendEmail: function(data) {
				this.configEmailModal.visible = true;
			},
			createQuery: function(data) {
				this.fetch();
			},
			download: function(data) {
				var reportJobUuid = data.reportJobUuid;
				window.open('/report/job/download?reportJobUuid='+ reportJobUuid);
			},
			downloadTemplateFile: function(type) {
				downloadFile('/zh/report/jobs/templates', {'type': type})
			},
			lookUpFile: function(row) {
				const modal = row.type == 0 ? this.loanRepaymentExportModal : this.paymentInfoExportModal;

				modal.show= true;
				modal.parameters = {
					uuid: row.reportJobUuid
				}
			},
			handleSuccess: function(response, file, fileList) {
				if (response.code == 0) {
					MessageBox.open("报表上传成功");
					this.fetch();
				} else {
					MessageBox.open(response.message);
				}
			},
			handleError: function(err, file, fileList) {
				MessageBox.open(err);
			}
		}
	}
</script>