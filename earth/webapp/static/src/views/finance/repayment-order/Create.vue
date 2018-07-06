<template>
	<div class="content">
		<div class="scroller">
			<Breadcrumb :routes="[{title: '还款订单列表'}, {title: '新增还款订单'}]">
			</Breadcrumb>
			<el-form
				ref="form"
				:model="currentModel"
				:rules="rules"
				label-width="120px" 
                class="sdf-form">
				<div class="fieldset">
					<el-form-item label="信托合同信息" class="form-item-legend"></el-form-item>
					<el-form-item prop="appId" :label="$utils.locale('financialContract.appAccount.name')" required>
	                    <el-select class="middle"
	                        placeholder="请选择"
	                        v-model="currentModel.appId">
	                        <el-option
	                            v-for="item in financialContractQueryModels" 
	                            :label="item.label"
	                            :value="item.value">
	                        </el-option>
	                    </el-select>
	                </el-form-item>
				    <el-form-item :label="$utils.locale('financialContract')" prop="financialContractNo" required>
			        	<el-select 
			        	    v-model="currentModel.financialContractNo"
			        	    class="middle"
			        	    :placeholder="$utils.locale('financialContract')">
			        	    <el-option
			        	        v-for="item in currentFinancialContracts"
			        	        :label="item.label"
			        	        :value="item.contractNo">
			        	    </el-option>
			        	</el-select>
				    </el-form-item>
				</div>

				<div class="fieldset">
					<el-form-item label="明细信息" class="form-item-legend">
                        <div class="supplement">（订单总金额：{{ totalAmount | formatMoney }} ）</div>
					</el-form-item>
					<el-form-item label="订单明细">
						<el-dropdown menu-align="start">
						  	<el-button type="primary" size="small">
						    	导入<i class="el-icon-caret-bottom el-icon--right"></i>
						  	</el-button>
						  	<el-dropdown-menu slot="dropdown">
							    <el-dropdown-item>
									<el-upload 
										key="repaymentPlanfile"
										style="width: auto; display: inline-block;"
					    		    	:on-success="handleRepaymentPlanSuccess"
					    		    	:on-error="handleRepaymentPlanError"
					    		    	:show-upload-list="false"
			                            action="/repayment-order/importRepaymentPlanRepaymentOrderFile">
										<el-button size="small" type="text">还款明细</el-button>
									</el-upload>
								</el-dropdown-item>
							    <el-dropdown-item>
							    	<el-upload 
										key="repurchasefile"
										style="width: auto; display: inline-block;"
					    		    	:on-success="handleRepurchaseSuccess"
					    		    	:on-error="handleRepurchaseError"
					    		    	:show-upload-list="false"
			                            action="/repayment-order/importRepurchaseRepaymentOrderFile">
										<el-button size="small" type="text">回购明细</el-button>
									</el-upload>
							    </el-dropdown-item>
						  	</el-dropdown-menu>
						</el-dropdown>
						<el-button type="primary" size="small" @click="downloadFile">下载模板</el-button>
					</el-form-item>
					<div style="margin: 10px 15% 10px 53px;">
                        <PagingTable :data="resultList" :pagination="true" :perPageRecordNumber="12">
                            <el-table-column 
                                prop="repaymentBusinessNo"
                                label="业务编号">
                            </el-table-column>
                            <el-table-column 
                                prop="contractUniqueId"
                                label="贷款合同唯一识别码">
                            </el-table-column>
                            <el-table-column 
                                prop="contractNo"
                                label="贷款合同编号">
                            </el-table-column>
                            <el-table-column 
                                prop="repaymentWay"
                                label="还款方式">
                            </el-table-column>
                            <el-table-column 
                            	inline-template
                                prop="repaymentAmount"
                                label="还款金额">
                                <div>{{ row.repaymentAmount | formatMoney }}</div>
                            </el-table-column>
                        </PagingTable>
	                </div>
				</div>

				<div class="fieldset">
					<el-form-item label="备注信息" class="form-item-legend">
					</el-form-item>
					<el-form-item label="备注" prop="remark">
						<el-input	
							class="long"
                            type="textarea"
                            v-model="currentModel.remark"
                            placeholder="请输入备注">
                        </el-input>
					</el-form-item>
				</div>

				<div class="fieldset" style="margin-left: 53px;">
                    <el-button style="min-width: 80px;" type="primary" @click="$router.go(-1)">取消</el-button>
                    <el-button style="min-width: 80px;" type="primary" @click="handleSubmit">提交</el-button>
                </div>
			</el-form>
		</div>
	</div>	
</template>
<script>
    import store from 'store';
	import { ajaxPromise, downloadFile } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

	export default {
		components: {
			PagingTable: require('views/include/PagingTable')
		},
		data: function() {
			return {
				currentModel: {
					appId: '',
					financialContractNo: '',
					remark: '',
				},

				rules: {
					appId: {required: true, message: ' '},
					financialContractNo: {required: true, message: ' '},
				},
				repaymentPlanInfo: {
					list: [],
					amount: 0
				},
				repurchaseInfo: {
					list: [],
					amount: 0
				},
				resultList: []
			}
		},
		computed: {
			currentFinancialContracts: function() {
				var result = [];
                this.financialContractQueryModels.forEach(item => {
                    if (item.value == this.currentModel.appId) {
                        result = item.children;
                    }
                });
                return result;
			},
			financialContractQueryModels: function() {
				return store.state.financialContract.financialContractQueryModels;
			},
			totalAmount: function() {
				return this.repaymentPlanInfo.amount + this.repurchaseInfo.amount;
			}
		},
		beforeMount: function() {
			const { $store } = this;
			$store.dispatch('getFinancialContractQueryModels');
		},
		deactivated: function() {
			this.$refs.form.resetFields();
		},
		activated: function() {
			this.currentModel = {
				appId: '',
				financialContractNo: '',
				remark: '',
			};
			this.repaymentPlanInfo = {
				list: [],
				amount: 0
			};
			this.repurchaseInfo = {
				list: [],
				amount: 0
			};
			this.resultList = [];
		},
		methods: {
			handleRepaymentPlanSuccess: function(response, file, fileList) {
				if (response.code == 0) {
					const { list, amount } = response.data;
					var {repaymentPlanInfo} = this;
					repaymentPlanInfo.list = repaymentPlanInfo.list.concat(list);
					repaymentPlanInfo.amount = repaymentPlanInfo.amount + amount;
					this.resultList = this.resultList.concat(list);
				} else {
					MessageBox.open(response.message);
				}
			},
			handleRepaymentPlanError: function(error, response, file) {
				MessageBox.open(response.message);
			},
			handleRepurchaseSuccess: function(response, file, fileList) {
				if (response.code == 0) {
					const { list, amount } = response.data;
					var {repurchaseInfo} = this;
					repurchaseInfo.list = repurchaseInfo.list.concat(list);
					repurchaseInfo.amount = repurchaseInfo.amount + amount;
					this.resultList = this.resultList.concat(list);
				} else {
					MessageBox.open(response.message);
				}
			},
			handleRepurchaseError: function(error, response, file) {
				MessageBox.open(response.message);
			},
			downloadFile: function() {
				downloadFile('/repayment-order/repaymentOrderDownload')
			},
			handleSubmit: function() {
				this.$refs.form.validate(vaild => {
					if (vaild) {
						MessageBox.open('确认新增还款订单？', '提示', [{
							text: '取消',
							handler:() => MessageBox.close()
						}, {
							text: '确定',
							type: 'success',
							handler: () => {
								const postData = Object.assign(this.currentModel,{data: this.repaymentPlanInfo.list.length ? JSON.stringify(this.repaymentPlanInfo.list) : '',dataRepurchase: this.repurchaseInfo.list.length ? JSON.stringify(this.repurchaseInfo.list) : '', amount: this.totalAmount});
								ajaxPromise({
									url: `/repayment-order/importRepaymentOrderDetail`,
									type: 'post',
									data: postData
								}).then(data => {
									MessageBox.close();
									MessageBox.once('closed',() => this.$router.go(-1))
									MessageBox.open('还款订单提交成功!');
								}).catch(message => {
									MessageBox.open(message);
								})
							}
						}])
					}
				})
			}
		}
	}
</script>