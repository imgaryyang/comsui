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
						<el-col :span="11">
							<el-form-item>
								<DateTimePicker
									v-model="queryConds.startDate"
									:end-date="queryConds.endDate"
									placeholder="导入起始时间"
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
									v-model="queryConds.endDate"
									:start-date="queryConds.startDate"
									placeholder="导入终止时间"
									size="small">
								</DateTimePicker>
							</el-form-item>
						</el-col>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option value="batchNo" label="导入批次编号"></el-option>
							<el-option value="requestNo" label="请求批次编号"></el-option>
							<el-option value="contractUniqueId" label="贷款合同唯一识别码"></el-option>
							<el-option value="contractNo" label="贷款合同编号"></el-option>
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
					<el-table-column label="导入批次编号" prop="batchNo"></el-table-column>
					<el-table-column label="请求批次编号" prop="requestNo"></el-table-column>
					<el-table-column label="信托合同代码" prop="financialContractNo"></el-table-column>
					<el-table-column label="贷款合同唯一识别码" prop="contractUniqueId"></el-table-column>
					<el-table-column label="贷款合同编号" prop="contractNo" inline-template>
						<a :href="`${ctx}#/data/contracts/detail?uid=${row.contractUniqueId}`">{{ row.contractNo }}</a>
					</el-table-column>
					<el-table-column label="附件数量" prop="appendixCount"></el-table-column>
					<el-table-column label="上传时间" prop="uploadTime" inline-template>
						<div>{{ row.uploadTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
					</el-table-column>
					<el-table-column label="管理" inline-template>
						<el-popover
                        popper-class="attachment"
                        trigger="click"
                        placement="bottom">
	                        <ul v-if="row.showAppendices.length">
	                            <li v-for="item in row.showAppendices">
	                                <span><img src="./include/attachment-pdf.png"></span>
	                                <span style="margin-left: 2px;">{{ item.fileName }}</span>
	                                <span class="pull-right" @click="deleteAttachedFile(item, row)" style="margin-left: 10px;cursor: pointer;">
	                                    <img src="./include/attachment-delete.png">
	                                </span>
	                                <span class="pull-right" @click="downloadAttachedFile(item)" style="cursor: pointer;">
	                                    <img src="./include/attachment-download.png">
	                                </span>
	                            </li>
	                        </ul>
	                        <div v-else style="line-height: 50px; text-align: center">暂无附件</div>
	                        <el-button size="small" type="primary" slot="reference">管理</el-button>
	                    </el-popover>
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
	import { ajaxPromise, downloadFile } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		mixins: [Pagination, ListPage],
		components: {
			ComboQueryBox: require('views/include/ComboQueryBox'),
		},
		data: function() {
			return {
				action: '/appendix/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					startDate: '',
					endDate: ''
				},
				comboConds: {
					batchNo: '',
					requestNo: '',
					contractUniqueId: '',
					contractNo: ''
				},
				financialContractQueryModels: [],
			}
		},
		methods: {
			initialize: function() {
                return this.getOptions();
            },
            getOptions: function() {
				return ajaxPromise({
					url: `/appendix/options`
				}).then(data => {
					this.financialContractQueryModels = data.queryAppModels || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			deleteAttachedFile: function(item, row) {
				var htm = `<div style="margin-top: 25px;">确定删除附件<span class="color-danger">${item.fileName}</span>吗？</div>`;
                MessageBox.open(htm, '提示', [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        MessageBox.close();

                        ajaxPromise({
                            url: `/contracts/appendices/${item.fileKey}`,
                            type: 'delete'
                        }).then(data => {
                        	row.showAppendices = row.showAppendices.filter(cur => cur.fileKey != item.fileKey);
                            MessageBox.open("删除成功");
                        }).catch(message => {
                            MessageBox.open(message);
                        })
                    }
                }]);
			},
			downloadAttachedFile: function(item) {
                downloadFile(`/contracts/appendices/${item.fileKey}`);
			}
		}
	}
</script>