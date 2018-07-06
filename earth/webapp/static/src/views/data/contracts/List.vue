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
					 	<el-select
					 		v-model="queryConds.contractStateOrdinals"
					 		placeholder="合同状态"
					 		size="small"
					 		multiple>
					 		<el-select-all-option
					 			:options="contractStates">
					 		</el-select-all-option>
					 		<el-option
					 			v-for="item in contractStates"
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
							<el-option label="贷款合同编号" value="contractNo">
							</el-option>
							<el-option label="贷款人" value="customerName">
							</el-option>
							<el-option label="贷款合同唯一识别码" value="uniqueId">
							</el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
					<el-form-item v-if="ifElementGranted('exprot-loan-contract')">
						<ExportDropdown @command="handleCommand" title="批量">
							<el-dropdown-item command="exportFile">贷款合同</el-dropdown-item>
							<el-dropdown-item command="batchRepurchase">批量回购</el-dropdown-item>
							<el-dropdown-item command="batchAddTags">添加标签</el-dropdown-item>
                            <el-dropdown-item command="batchDeleteTags">删除标签</el-dropdown-item>
						</ExportDropdown>
					</el-form-item>
				</el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border tag-table"
					v-loading="dataSource.fetching"
					@sort-change="onSortChange"
					:data="dataSource.list"
					stripe>
					<el-table-column width="180" label="贷款合同编号" inline-template>
						<div>
							<el-popover
                                style="display: inline-block;"
                                popper-class="tag-popover"
                                placement="right"
                                trigger="hover">
                               	<h5 class="tag-title">标签</h5>
                                <ul class="ul-reset" :class="{'init-ul': row.tags.length == 0}">
                                    <li v-for="tag in row.tags" class="li-tag">
                                        {{tag.tagName}}<span class="el-icon-close" @click="handleClickDeleteCurrentLabel(tag, row.tags)"></span>
                                    </li>
                                    <li class="el-icon-plus li-tag create-tag" @click="handleClickAddLabel(row,$index)"></li>
                                </ul>
                                <template v-if="row.transitivityTags.length">
                                    <h5 class="tag-title">联动标签</h5>
                                    <ul class="ul-reset">
                                        <li v-for="tag in row.transitivityTags" class="li-tag">
                                            {{tag.tagName}}<span class="el-icon-close" @click="handleClickDeleteCurrentLabel(tag, row.transitivityTags)"></span>
                                        </li>
	                                    <li class="el-icon-plus li-tag create-tag" @click="handleClickAddLabel(row,$index)"></li>
                                    </ul>
                                </template>
                                <span :class="['tag','tag-center', !row.tags.length && !row.transitivityTags.length ? 'tagEmpty' :'tagFull']" slot="reference"></span>
                            </el-popover>
							<a class="atag" :href="`${ctx}#/data/contracts/detail?id=${row.id}`">{{ row.contractNo }}</a>
						</div>
					</el-table-column>
					<el-table-column label="贷款合同唯一识别码" inline-template>
						<div>{{ row.uniqueId }}</div>
					</el-table-column>
					<el-table-column 
						label="贷款利率"
						prop="interestRate"
						sortable="custom" 
						inline-template>
						<div>{{ row.interestRate | formatPercent }}</div>
					</el-table-column>
					<el-table-column 
						label="生效日期" 
						prop="beginDate"
						sortable="custom" 
						inline-template>
						<div>{{ row.beginDate | formatDate }}</div>
					</el-table-column>
					<el-table-column 
						label="截止日期" 
						prop="endDate"
						sortable="custom" 
						inline-template>
						<div>{{ row.endDate | formatDate }}</div>
					</el-table-column>
					<el-table-column label="贷款方式" prop="repaymentWayMsg">
					</el-table-column>
					<el-table-column label="期数" prop="periods">
					</el-table-column>
					<el-table-column label="还款周期" inline-template>
						<div>{{ row.paymentFrequency }}月付</div>
					</el-table-column>
					<el-table-column label="放款账户名" inline-template>
						<div>{{ row.cpBankAccountHolder }}</div>
					</el-table-column>
					<el-table-column label="贷款人" inline-template>
						<div>{{ row.customerName }}</div>
					</el-table-column>
					<el-table-column 
						label="贷款总额" 
						prop="totalAmount"
						sortable="custom" 
						inline-template>
						<div>{{ row.totalAmount | formatMoney}}</div>
					</el-table-column>
					<el-table-column label="合同状态" inline-template>
						<div>
							<span v-bind:class="[row.state === 'DEFAULT' || row.state === 'INVALIDATE' ? 'color-danger' : row.state === 'REPURCHASING' ? 'color-warning' : '']">{{ row.stateMsg }}</span>
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

		<ExportPreviewModal
			:parameters="conditions"
			:query-action="`/contracts/preview-exprot-loan-contract`"
			:download-action="`/report/export?reportId=1`"
            :canExportTags="true"
			v-model="exportModal.show">
			<el-table-column prop="uniqueId" label="贷款合同唯一识别码">
			</el-table-column>
			<el-table-column prop="financialContractNo" :label="$utils.locale('financialContract.no')">
			</el-table-column>
			<el-table-column prop="appName" :label="$utils.locale('financialContract.appAccount.name')">
			</el-table-column>
			<el-table-column prop="accountNo" :label="$utils.locale('financialContract.account.no')">
			</el-table-column>
			<el-table-column prop="contractNo" label="贷款合同编号">
			</el-table-column>
			<el-table-column prop="cpBankAccountHolder" label="放款账户名">
			</el-table-column>
			<el-table-column prop="customerName" label="贷款人">
			</el-table-column>
			<el-table-column prop="loanDate" label="放款日期">
			</el-table-column>
			<el-table-column prop="dueDate" label="到期日期">
			</el-table-column>
			<el-table-column prop="totalAmount" label="贷款本金总额">
			</el-table-column>
			<el-table-column prop="totalInterest" label="贷款利息总额">
			</el-table-column>
			<el-table-column prop="periods" label="贷款期数">
			</el-table-column>
			<el-table-column prop="restSumPrincipal" label="剩余本金余额">
			</el-table-column>
			<el-table-column prop="restSumInterest" label="剩余利息余额">
			</el-table-column>
			<el-table-column prop="repaymentWay" label="还款方法">
			</el-table-column>
			<el-table-column prop="interestRate" label="贷款利率">
			</el-table-column>
			<el-table-column prop="penaltyInterest" label="罚息利率">
			</el-table-column>
			<el-table-column prop="loanOverdueEndDay" label="回购期">
			</el-table-column>
			<el-table-column prop="repaymentGraceTerm" label="还款宽限期">
			</el-table-column>
			<el-table-column prop="idCardNum" label="贷款客户身份证号码">
			</el-table-column>
			<el-table-column prop="age" label="客户年龄">
			</el-table-column>
			<el-table-column prop="bank" label="还款账户开户行名称">
			</el-table-column>
			<el-table-column prop="province" label="开户行所在省">
			</el-table-column>
			<el-table-column prop="city" label="开户行所在市">
			</el-table-column>
			<el-table-column prop="payAcNo" label="还款账户号">
			</el-table-column>
			<el-table-column prop="beginDate" label="设定生效日期">
			</el-table-column>
		</ExportPreviewModal>

		<BatchRepurchaseModal
			:financialContractQueryModels="financialContractQueryModels"
			v-model="batchRepurchaseModal.show"
			:model="batchRepurchaseModal.model">
		</BatchRepurchaseModal>

		<CreateTagModal
            :isAddWith="true"
            v-model="createTagModel.show"
            :options="createTagModel.options"
            :tags="createTagModel.currentTags"
            :batch="createTagModel.batch"
            :size="createTagModel.size"
            :maxSize="1000"
            :type="createTagModel.type"
            :action="createTagModel.action"
            @submit="handleSubmitCreateTagModal"
            @submit-add-with="handleSubmitAddWithCreateTagModal"
            @batch-submit="handleBatchSubmitCreateTagModal">
        </CreateTagModal>
	</div>
</template>

<script>
	import Pagination from 'mixins/Pagination';
	import ListPage from 'mixins/ListPage';
	import { ajaxPromise, purify } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
    import CreateTagModal from 'components/CreateTagModal';

	export default {
		mixins: [Pagination, ListPage],
		components: {
			ComboQueryBox: require('views/include/ComboQueryBox'),
			ExportDropdown: require('views/include/ExportDropdown'),
			BatchRepurchaseModal: require('./include/BatchRepurchaseModal'),
			ExportPreviewModal: require('views/include/ExportPreviewModal'),
			CreateTagModal
		},
		data: function() {
			return {
				action: '/contracts/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					contractStateOrdinals: [],
					startDateString: '',
					endDateString: '',
				},
				comboConds: {
					contractNo: '',
	                underlyingAsset: '',
	                customerName: '',
	                uniqueId: ''
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

				financialContractQueryModels: [],
				contractStates: [],
				flatCollection: [],

				exportModal: {
					show: false
				},
				batchRepurchaseModal: {
					show: false,
					model: {}
				},
				createTagModel: {
                    show: false,
                    options: [],
                    uniqueId: '',
                    currentTags: [],
                    dataSourceIndex: '',
                    batch: false,
                    size: '',
                    type: 1,//贷款合同
                    action: 'add',//默认为新增
                },
			};
		},
		activated: function() {
			this.setRedirectWatch();
			this.refreshTagNames();
		},
		methods: {
			initialize: function() {
			    return this.getOptions();
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/contracts/options`
				}).then(data => {
					this.financialContractQueryModels = data.queryAppModels || [];
					this.contractStates = data.contractStates || [];

					var queryConds = this.queryConds;

					if (!queryConds.contractStateOrdinals.length) {
						queryConds.contractStateOrdinals = this.contractStates.map(item => item.key);
					}
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			refreshTagNames: function() {
                ajaxPromise({
                    url:`/tag/names`
                }).then(data=>{
                    this.createTagModel.options = data.data
                }).catch(message =>{
                    MessageBox.open(message)
                })
            },
			getFlatCollection: function(data) {
				this.flatCollection = data;
				this.setRedirectWatch();
			},
			setRedirectWatch: function() {
				var self = this;
				if (this.$route.query.isRedirect) {
					if (!this.$route.query.financialContractUuids) return;
					var financialContractUuid = JSON.parse(this.$route.query.financialContractUuids)[0];
					this.flatCollection.forEach(item => {
						if (item.value == financialContractUuid) {
							setTimeout(() => {
								self.queryConds.financialContractUuids = [item];
							}, 0);
						}
					})
				}
			},
			handleCommand: function(command) {
				switch(true) {
					case command === 'exportFile':
						this.exportModal.show = true
						break;
					case command === 'batchRepurchase':
						this.batchRepurchaseModal.show = true;
						break;
					case command === 'batchAddTags':
						this.batchHandlerTags('add');
						break;
					case command === 'batchDeleteTags':
						this.batchHandlerTags('delete');
						break;
					default:
						break;
				}
			},
            handleClickDeleteCurrentLabel: function(tag, tags){
                ajaxPromise({
                    url: `tag/delcfg`,
                    data:{
                      uuid:tag.tagConfigUuid,
                    },
                    type: 'post'
                }).then(data => {
                    var index = tags.findIndex(item => {
                        return item === tag
                    })
                    if(index != -1){
                        tags.splice(index,1)
                    }
                }).catch(message =>{
                    MessageBox.open(message)
                })
            },
            handleClickAddLabel: function(row, index){
                this.createTagModel.show = true;
                this.createTagModel.currentTags =[].concat(row.tags).concat(row.transitivityTags);
                this.createTagModel.uniqueId = row.uniqueId;
                this.createTagModel.dataSourceIndex = index;
                this.createTagModel.action = 'add';
                this.createTagModel.batch = false;
                this.createTagModel.size = 1;
            },
            handleSubmitAddWithCreateTagModal: function(transitivityTagList, defaultTagList) {
            	var data = {
            		no: this.createTagModel.uniqueId,
                    type: 1,
                    transitivityTagList: JSON.stringify(transitivityTagList),
                    defaultTagList: JSON.stringify(defaultTagList)
            	};
                this.singleSubmit(data);
            },
            handleSubmitCreateTagModal: function(defaultTagList){
            	var data = {
            		no: this.createTagModel.uniqueId,
                    type: 1,
                    defaultTagList: JSON.stringify(defaultTagList)
            	};
                this.singleSubmit(data)
            },
            singleSubmit: function(postData) {
            	ajaxPromise({
                    url: `/tag/add/single`,
                    data: postData,
                    type: 'post'
                }).then(data => {
                    MessageBox.open('添加标签成功');
                    this.fetch();
                    this.refreshTagNames();
                }).catch(message => {
                    MessageBox.open(message)
                }).then(()=>{
                    this.createTagModel.show = false;
                })
            },
            batchHandlerTags: function(action) {
                this.createTagModel.show = true;
                this.createTagModel.currentTags = [];
                this.createTagModel.batch = true;
                this.createTagModel.action = action;
                this.createTagModel.size = this.dataSource.size;
            },
            handleBatchSubmitCreateTagModal: function(postData) {
                let actionType = postData.actionType;
                let conditions = purify(Object.assign({},this.conditions));
                let url = postData.fileKey ? '/tag/submit/1' : '/tag/submit/2';
                let data = postData.fileKey ? postData : Object.assign(conditions, postData);
                ajaxPromise({
                    url: url,
                    type: 'post',
                    data: data
                }).then(data => {
                    MessageBox.open(actionType === 'add' ? '添加标签成功' : '删除标签成功');
                    this.fetch();
                    this.refreshTagNames();
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.createTagModel.show = false;
                })
            },
		}
	}
</script>
