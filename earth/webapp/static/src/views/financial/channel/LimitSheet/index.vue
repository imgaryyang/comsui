<template>
	<div class="content">
		<div class="scroller">
			<div class="query-area">
				<el-form class="sdf-form sdf-query-form" :inline="true">
					<el-form-item>
                        <el-select
                            v-model="queryConds.gateway"
                            placeholder="选择网关"
                            name="gateway"
                            size="small"
                            clearable>
                            <el-option
                                v-for="item in gatewayList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
					</el-form-item>
					<el-form-item>
                        <el-select
                            v-model="queryConds.outlierChannelName"
                            placeholder="选择商户号"
                            name="outlierChannelName"
                            size="small"
                            clearable>
                            <el-option
                                v-for="item in outlierChannelNames"
                                :label="item"
                                :value="item">
                            </el-option>
                        </el-select>
					</el-form-item>
					<el-form-item>
                        <el-select
                            v-model="queryConds.accountSide"
                            placeholder="选择收付类型"
                            name="accountSide"
                            size="small"
                            clearable>
                            <el-option
                                v-for="item in accountSide"
                                :label="item.key == 0 ? '代付' : '代收'"
                                :value="item.key">
                            </el-option>
                        </el-select>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option label="银行名称" value="keyWord">
							</el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
					<el-form-item>
						<el-button size="small" type="primary" @click="createLimitSheetModal.visible = true" v-if="ifElementGranted('upload-3rd-limit-sheet')">上传限额表</el-button>
					</el-form-item>
				</el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border"
					v-loading="dataSource.fetching"
					:data="dataSource.list"
					stripe>
					<el-table-column label="网关" prop="paymentInstitutionName"></el-table-column>
					<el-table-column label="商户号" prop="outlierChannelName"></el-table-column>
					<el-table-column label="收付类型" prop="accountSide"></el-table-column>
					<el-table-column label="银行名称" prop="bankName"></el-table-column>
					<el-table-column label="单笔限额" inline-template>
					    <div>
					        {{ row.transactionLimitPerTranscation != null ? (row.transactionLimitPerTranscation | formatMoney) : '--' }}
					    </div>
					</el-table-column>
					<el-table-column label="单日限额" inline-template>
					    <div>
					        {{ row.transcationLimitPerDay != null ? (row.transcationLimitPerDay | formatMoney) : '--' }}
					    </div>
					</el-table-column>
					<el-table-column label="单月限额" inline-template>
					    <div>
					        {{ row.transactionLimitPerMonth != null ? (row.transactionLimitPerMonth | formatMoney) : '--' }}
					    </div>
					</el-table-column>
					<el-table-column label="操作" inline-template>
					    <a href="javascript: void 0;" @click="handleModifyLimitSheet(row)" v-if="ifElementGranted('modify-3rd-limit-sheet')">编辑</a>
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

		<CreateLimitSheetModal
			@submit="fetch"
			v-model="createLimitSheetModal.visible"
			:gatewayList="gatewayList"
			:outlierChannelNames="outlierChannelNames"
			:accountSides="accountSide"></CreateLimitSheetModal>

        <ModifyLimitSheetModal
            @submit="fetch"
            :model="modifyLimitSheetModal.model"
            v-model="modifyLimitSheetModal.visible"></ModifyLimitSheetModal>
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
			ComboQueryBox: require('views/include/ComboQueryBox'),
			CreateLimitSheetModal: require('./CreateLimitSheetModal'),
            ModifyLimitSheetModal: require('./ModifyLimitSheetModal'),
		},
		data: function() {
			return {
				action: '/paymentchannel/limitSheet/search',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					gateway: '',
					outlierChannelName: '',
					accountSide: ''
				},
				comboConds: {
					keyWord: '',
				},
				gatewayList: [],
				accountSide: [],
				outlierChannelNames: [],

				createLimitSheetModal: {
					visible: false
				},
                modifyLimitSheetModal: {
                    visible: false,
                    model: {}
                }
			}
		},
		methods: {
			initialize: function() {
			    return this.getOptions();
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/paymentchannel/limitSheet/list/options`
				}).then(data => {
					this.gatewayList = data.gatewayList;
					this.accountSide = data.accountSide;
					this.outlierChannelNames = data.outlierChannelNames;
				}).catch(message => {
					MessageBox.open(message);
				});
			},
            handleModifyLimitSheet: function(row) {
                this.modifyLimitSheetModal.visible = true;
                this.modifyLimitSheetModal.model = Object.assign({}, row);
            }
		}
	}
</script>