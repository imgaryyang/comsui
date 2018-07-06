<template>
	<div class="content">
		<div class="scroller">
			<div class="query-area">
				<el-form class="sdf-form sdf-query-form" :inline="true">
					<el-form-item>
						<el-select 
						    v-model="queryConds.gateway"
						    size="small" 
						    clearable
						    placeholder="选择网关">
						    <el-option
						        v-for="item in gatewayList"
						        :label="item.value"
						        :value="item.key">
						    </el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select 
						    v-model="queryConds.debitStatus"
						    size="small" 
						    clearable
						    placeholder="收款状态">
						    <el-option
						        v-for="(value, label) in debitStatusList"
								:label="label"
								:value="value">
						    </el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select 
						    v-model="queryConds.creditStatus"
						    size="small" 
						    clearable
						    placeholder="付款状态">
						    <el-option
						        v-for="(value, label) in creditStatusList"
								:label="label"
								:value="value">
						    </el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select 
						    v-model="queryConds.businessType"
						    size="small" 
						    clearable
						    placeholder="业务类型">
						    <el-option
						        v-for="item in businessTypeList"
								:label="item.value"
								:value="item.key">
						    </el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<span class="item vertical-line"></span>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option value="outlierChannelName" label="商户号"></el-option>
							<el-option value="contractName" :label="$utils.locale('financialContract')"></el-option>
							<el-option value="paymentChannelName" label="通道名称"></el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button ref="lookup" size="small" type="primary">查询</el-button>
					</el-form-item>
					<el-form-item>
						<router-link :to="{name: 'channelConfigCreate'}">
							<el-button size="small" type="success">新增</el-button>
						</router-link>
					</el-form-item>
				</el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border"
					stripe
					@sort-change="onSortChange"
					v-loading="dataSource.fetching"
					:data="dataSource.list">
					<el-table-column
						inline-template
						label="通道名称">
						<div>
						{{ row.paymentChannelName }}
						<a v-if="row.configureProgress=='WAITING'" :href="`${ctx}#/financial/channel/configure/${ row.paymentChannelUuid }/edit`">
							<span class="badge badge-dander">待配置</span>
						</a>
						</div>
					</el-table-column>
					<el-table-column 
						inline-template
						prop="createTime"
						label="创建日期"
						sortable="custom">
						<div>{{ row.createTime | formatDate }}</div>
					</el-table-column>
					<el-table-column
						prop="paymentInstitutionName"
						label="网关名称">
					</el-table-column>
					<el-table-column 
						inline-template
						label="业务类型">
						<div>
							<span v-if="row.businessType === 'SELF'">自有</span>				
							<span v-else-if="row.businessType === 'ENTRUST'">委托</span>
						</div>
					</el-table-column>
					
					<el-table-column 
						inline-template 
						label="收款状态">
						<div>
							<span v-if="row.debitChannelWorkingStatus === 'ON'" class="color-success">已启用</span>
							<span v-else-if="row.debitChannelWorkingStatus === 'OFF'" class="color-danger">已关闭</span>
							<span v-else-if="row.debitChannelWorkingStatus === 'NOTLINK'" class="color-danger">未对接</span>
						</div>
					</el-table-column>
					<el-table-column 
						inline-template 
						label="付款状态">
						<div>
							<span v-if="row.creditChannelWorkingStatus === 'ON'" class="color-success">已启用</span>
							<span v-else-if="row.creditChannelWorkingStatus === 'OFF'" class="color-danger">已关闭</span>
							<span v-else-if="row.creditChannelWorkingStatus === 'NOTLINK'" class="color-danger">未对接</span>
						</div>
					</el-table-column>
					<el-table-column prop="outlierChannelName" label="商户号"></el-table-column>
					<el-table-column prop="relatedFinancialContractName" :label="$utils.locale('financialContract')"></el-table-column>
					<el-table-column 
						inline-template 
						label="操作">
						<div>
							<a v-if="row.configureProgress =='WAITING'" :href="`${ctx}#/financial/channel/configure/${ row.paymentChannelUuid }/edit`" class="success">配置</a>
							<a v-else href="javascript:void(0)" @click="onClickShowDetail(row.paymentChannelUuid)">详情</a>
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
		<Modal v-model="showDetail" classes="yunxin-modal">
		    <ModalHeader :title="detail.paymentChannelName"></ModalHeader>
		    <ModalBody>	
		        <div class="sketch">
		        	<span class="item">
		        		对接网关：<span>{{ detail.paymentInstitutionNameMsg }}</span>
		        	</span>
		        	<span class="item">
		        		商户号：<span>{{ detail.outlierChannelName }}</span>
		        	</span>
		        	<span class="item">
		        		{{ $utils.locale('financialContract')}}：<span>{{ detail.relatedFinancialContractName }}</span>
		        	</span>
		        </div>
		       	<div class="detail">
		       		<div class="block">
		       			<h5 class="hd">收款交易</h5>
		       			<div class="bd">
		       				<div class="col">
		       					<div class="item">
		       						<span class="key">状态：</span>
		       						<span v-if="debitChannelDetails" class="val">
		       							<span v-if="debitChannelDetails.channelStatus == 'ON'" class="color-success">已启用</span>
		       							<span v-else-if="debitChannelDetails.channelStatus == 'OFF'" class="color-danger">已关闭</span>
		       							<span v-else-if="debitChannelDetails.channelStatus == 'NOTLINK'" class="color-danger">未对接</span>
		       						</span>
		       					</div>
		       					<div class="item">
		       						<span class="key">通道扣率：</span>
		       						<span v-if="debitChannelDetails" class="val">
		       							<span v-if="debitChannelDetails.chargeRateMode == 'SINGLEFIXED'">
		       								{{ debitChannelDetails.chargePerTranscation ? debitChannelDetails.chargePerTranscation + '元\/笔' : '' }}
		       							</span>
		       							<span v-else-if="debitChannelDetails.chargeRateMode == 'SINGLERATA'">
		       								{{ '['+(debitChannelDetails.lowerestChargeLimitPerTransaction || ' ') + ', ' + (debitChannelDetails.upperChargeLimitPerTransaction || ' ') + ', ' + (debitChannelDetails.chargeRatePerTranscation || ' ') + '%]' }}
		       							</span>
		       						</span>
		       					</div>
		       					<div class="item">
		       						<span class="key">清算周期：</span>
		       						<span v-if="debitChannelDetails && debitChannelDetails.clearingInterval" class="val">
		       							T{{ debitChannelDetails.clearingInterval }}日
		       						</span>
		       					</div>
		       				</div>
		       				<div class="col">
		       					<div class="item">
		       						<span class="key">交易类型：</span>
		       						<span v-if="debitChannelDetails" class="val">
		       							<span v-if="debitChannelDetails.chargeType == 'BATCH'">批量</span>
		       							<span v-else-if="debitChannelDetails.chargeType == 'SINGLE'">单笔</span>
		       							<span v-else-if="debitChannelDetails.chargeType == 'BOTH'">批量、单笔</span>
		       						</span>
		       					</div>
		       					<div class="item">
		       						<span class="key">费用模式：</span>
		       						<span v-if="debitChannelDetails" class="val">
		       							<span v-if="debitChannelDetails.chargeExcutionMode == 'FORWARD'">向前收费</span>
		       							<span v-if="debitChannelDetails.chargeExcutionMode == 'BACKWARD'">向后收费</span>
		       						</span>
		       					</div>
		       					<div class="item">
		       						<span class="key">银行限额表：</span>
		       						<span v-if="debitChannelDetails && typeof(debitChannelDetails.bankLimitationFileName) !='undefined'" class="val">
		       								<a :href="`${api}/paymentchannel/file/download?fileKey=${debitChannelDetails.bankLimitationFileKey}`" style="    width: 100px;display: inline-flex;">{{ debitChannelDetails.bankLimitationFileName }}</a>
		       						</span>
		       					</div>
		       				</div>
		       			</div>
		       		</div>
		       		<div class="block">
		       			<h5 class="hd">付款交易</h5>
		       			<div class="bd">
		       				<div class="col">
		       					<div class="item">
		       						<span class="key">状态：</span>
		       						<span v-if="creditChannelDetails" class="val">
		       							<span v-if="creditChannelDetails.channelStatus == 'ON'" class="color-success">已启用</span>
		       							<span v-else-if="creditChannelDetails.channelStatus == 'OFF'" class="color-danger">已关闭</span>
		       							<span v-else-if="creditChannelDetails.channelStatus == 'NOTLINK'" class="color-danger">未对接</span>
		       						</span>
		       					</div>
		       					<div class="item">
		       						<span class="key">通道扣率：</span>
		       						<span v-if="creditChannelDetails" class="val">
		       							<span v-if="creditChannelDetails.chargeRateMode == 'SINGLEFIXED'">
		       								{{ creditChannelDetails.chargePerTranscation ? creditChannelDetails.chargePerTranscation + '元\/笔' : '' }}
		       							</span>
		       							<span v-else-if="creditChannelDetails.chargeRateMode == 'SINGLERATA'">
		       								{{ '['+(creditChannelDetails.lowerestChargeLimitPerTransaction || ' ') + ', ' + (creditChannelDetails.upperChargeLimitPerTransaction || ' ') + ', ' + (creditChannelDetails.chargeRatePerTranscation || ' ') + '%]' }}
		       							</span>
		       						</span>
		       					</div>
		       					<div class="item">
		       						<span class="key">清算周期：</span>
		       						<span v-if="creditChannelDetails && creditChannelDetails.clearingInterval" class="val">
		       							T{{ creditChannelDetails.clearingInterval }}日
		       						</span>
		       					</div>
		       				</div>
		       				<div class="col">
		       					<div class="item">
		       						<span class="key">交易类型：</span>
		       						<span v-if="creditChannelDetails" class="val">
		       							<span v-if="creditChannelDetails.chargeType == 'BATCH'">批量</span>
		       							<span v-else-if="creditChannelDetails.chargeType == 'SINGLE'">单笔</span>
		       							<span v-else-if="creditChannelDetails.chargeType == 'BOTH'">批量、单笔</span>
		       						</span>
		       					</div>
		       					<div class="item">
		       						<span class="key">费用模式：</span>
		       						<span v-if="creditChannelDetails" class="val">
		       							<span v-if="creditChannelDetails && creditChannelDetails.chargeExcutionMode == 'FORWARD'">向前收费</span>
		       							<span v-else-if="creditChannelDetails && creditChannelDetails.chargeExcutionMode == 'BACKWARD'">向后收费</span>
		       						</span>
		       					</div>
		       					<div class="item">
		       						<span class="key">银行限额表：</span>
		       						<span v-if="creditChannelDetails && typeof(creditChannelDetails.bankLimitationFileName) !='undefined'" class="val">
	       								<a :href="`${api}/paymentchannel/file/download?fileKey=${creditChannelDetails.bankLimitationFileKey}`" style="    width: 100px;display: inline-flex;">{{ creditChannelDetails.bankLimitationFileName }}</a>
		       						</span>
		       					</div>
		       				</div>
		       			</div>
		       		</div>
		       	</div>
		    </ModalBody>
		    <ModalFooter>
		    	<el-button type="primary" @click="location.assign(`${ctx}#/financial/channel/configure/${ detail.paymentChannelUuid}/detail`)">
			    	明细
		    	</el-button>
		    	<el-button type="success" @click="location.assign(`${ctx}#/financial/channel/configure/${ detail.paymentChannelUuid}/edit`)" v-if="ifElementGranted('reset-channel')">
			    	重新配置
		    	</el-button>
		    </ModalFooter>
		</Modal>
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
		},
		data: function() {
			return {
				action: '/paymentchannel/config/search',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					gateway: '',
					debitStatus: '',
					creditStatus: '',
					businessType: '',
				},
				comboConds: {
					outlierChannelName: '',
					contractName: '',
					paymentChannelName: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},

				gatewayList: [],
				debitStatusList: [],
				creditStatusList: [],
				businessTypeList: [],

				showDetail: false,
				detail: {}
			};
		},
		methods: {
			initialize: function() {
			    return this.getOptions();
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/paymentchannel/optionData`
				}).then(data => {
					this.gatewayList = data.gatewayList || [];
					this.debitStatusList = data.debitStatusList || [];
					this.creditStatusList = data.creditStatusList || [];
					this.businessTypeList = data.businessTypeList || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			onClickShowDetail: function(uuid) {
				ajaxPromise({
					url: `/paymentchannel/config/detail`,
					data: {
						paymentChannelUuid: uuid
					},
					type: 'GET'
				}).then(data => {
					this.detail = data;
					this.debitChannelDetails = data.debitChannelDetails;
					this.creditChannelDetails = data.creditChannelDetails;
					this.showDetail = true;
				}).catch(message => {
					MessageBox.open(message);
				});
			}
		}
	}
</script>