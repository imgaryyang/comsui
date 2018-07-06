<style lang="sass">
    @import '~assets/stylesheets/base';

	#retention_voucher_remittance_modal{

		@include min-screen(768px) {
            .modal-dialog {
                width: 65%;
                margin: 30px auto;

				.error {
					input{
						border-color: #e8415f;
					}
				}
            }
        }
		.danger-color{
			input{
				border-color: #ff4949;
			}
		}
	}

</style>
<template>
	<Modal v-model="visible" id="retention_voucher_remittance_modal">
		<ModalHeader title="还款弹窗"></ModalHeader>
		<ModalBody align="left">
			<div class="row-layout-detail" style="padding: 0;">
				<div class="query-area" style="background:none;padding:0 0 20px 0;border:none">
					<el-form
						ref="form"
					    class="sdf-form sdf-query-form" 
					    :inline="true">
				        <el-form-item>
				        	<ComboQueryBox v-model="comboConds">
				        		<el-option value="repaymentPlanNo" label="还款编号"></el-option>
				        		<el-option value="customerName" label="客户姓名"></el-option>
				        		<el-option value="contractNo" label="贷款合同编号"></el-option>
				        		<el-option value="contractUniqueId" label="贷款唯一识别编号"></el-option>
				        	</ComboQueryBox>
				        </el-form-item>
				        <el-form-item>
				        	<el-button type="primary" size="small" @click="query" :disabled="disabledQuery">查询</el-button>
				        </el-form-item>
					</el-form>
				</div>
				<div class="block">
					<div class="bd">
						<el-table
							class="td-15-padding th-8-15-padding no-th-border"
							:data="remittanceModel"
							style="width:100%"
							max-height="400"
							stripe
							borde
							@selection-change="SelectionChange">
							<el-table-column 
								type="selection"
	      						width="55">
							</el-table-column>
							<el-table-column prop="repaymentPlanNo" label="还款编号"></el-table-column>
							<el-table-column prop="assetRecycleDate" label="计划还款日期" inline-template>
								<div>{{ row.assetRecycleDate | formatDate}}</div>
							</el-table-column>
							<el-table-column prop="contractNo" label="贷款合同编号"></el-table-column>
							<el-table-column prop="customerName" label="客户姓名"></el-table-column>
							<el-table-column prop="overdueDays" label="逾期天数"></el-table-column>
							<el-table-column prop="planRecycleAmount" label="应还款金额"></el-table-column>
							<el-table-column prop="actualRecycleAmount" label="实际还款金额"></el-table-column>
							<el-table-column prop="assetStatus" label="还款状态"></el-table-column>
							<el-table-column prop="overdueStatus" label="逾期状态"></el-table-column>
						</el-table>
					</div>
				</div>
				<div class="block">
				    <h5 class="hd">
				        还款金额
				    </h5>
				    <div class="bd">
				        <el-table 
				            :data="remittanceEditModel"
				            class="td-15-padding th-8-15-padding no-th-border"
				            stripe
				            border>
				            <el-table-column prop="repaymentBusinessNo" label="还款编号">
				            </el-table-column>
				            <el-table-column prop="assetRecycleDate" label="计划还款日期" inline-template>
				            	<div>{{row.assetRecycleDate | formatDate}}</div>
				            </el-table-column>
				            <el-table-column prop="loanAssetPrincipal " label="还款本金" inline-template>
				            	<el-input :class="[+row.loanAssetPrincipal  <= row.detailAmounts.loanAssetPrincipal  ? '' : 'danger-color']" size="small" v-model="row.loanAssetPrincipal "></el-input>
				            </el-table-column>
				            <el-table-column prop="loanAssetInterest " label="还款利息" inline-template>
				            	<el-input :class="[+row.loanAssetInterest  <= row.detailAmounts.loanAssetInterest  ? '' : 'danger-color']" size="small" v-model="row.loanAssetInterest "></el-input>
				            </el-table-column>
				            <el-table-column prop="loanServiceFee " label="贷款服务费" inline-template>
				            	<el-input :class="[+row.loanServiceFee  <= row.detailAmounts.loanServiceFee  ? '' : 'danger-color']" size="small" v-model="row.loanServiceFee "></el-input>
				            </el-table-column>
				            <el-table-column prop="loanTechFee " label="技术维护费" inline-template>
				            	<el-input :class="[+row.loanTechFee  <= row.detailAmounts.loanTechFee  ? '' : 'danger-color']" size="small" v-model="row.loanTechFee "></el-input>
				            </el-table-column>
				            <el-table-column prop="loanOtherFee " label="其他费用" inline-template>
				            	<el-input :class="[+row.loanOtherFee  <= row.detailAmounts.loanOtherFee  ? '' : 'danger-color']" size="small" v-model="row.loanOtherFee "></el-input>
				            </el-table-column>
				            <el-table-column prop="overdueFeePenalty " label="逾期罚息" inline-template>
				            	<el-input :class="[+row.overdueFeePenalty  <= row.detailAmounts.overdueFeePenalty  ? '' : 'danger-color']" size="small" v-model="row.overdueFeePenalty "></el-input>
				            </el-table-column>
				            <el-table-column prop="overdueFeeObligation " label="逾期违约金" inline-template>
				            	<el-input :class="[+row.overdueFeeObligation  <= row.detailAmounts.overdueFeeObligation  ? '' : 'danger-color']" size="small" v-model="row.overdueFeeObligation "></el-input>
				            </el-table-column>
				            <el-table-column prop="overdueFeeService " label="逾期服务费" inline-template>
				            	<el-input :class="[+row.overdueFeeService  <= row.detailAmounts.overdueFeeService  ? '' : 'danger-color']" size="small" v-model="row.overdueFeeService "></el-input>
				            </el-table-column>
				            <el-table-column prop="overdueFeeOther " label="逾期其他费用" inline-template>
				            	<el-input :class="[+row.overdueFeeOther  <= row.detailAmounts.overdueFeeOther  ? '' : 'danger-color']" size="small" v-model="row.overdueFeeOther "></el-input>
				            </el-table-column>
				        </el-table>
				    </div>
				</div>
				<div class="block">
					<p style="text-align:center;">业务金额：{{transactionAmount}}     滞留金额：{{residualAmount}}</p>
				</div>
			</div>
		</ModalBody>
		<ModalFooter>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="success" @click="submit" :disabled="disable">确定</el-button>
		</ModalFooter>
	</Modal>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import { intersectionBy } from 'lodash';
	import Decimal from 'decimal.js';

	export default {
		components: {
			ComboQueryBox: require('views/include/ComboQueryBox'),
		},
		props: {
			value: Boolean,
			residualAmount: Number
		},
		data: function() {
			return {
				action:'temporary-deposit-doc/query-repayment-plan',
				visible: this.value,
				comboConds:{
					repaymentPlanNo: '',
					customerName: '',
					contractNo: '',
					contractUniqueId: ''
				},
				remittanceModel: [],
				remittanceEditModel: [],
				disable: false,
				disabledQuery: true,
				transactionAmount: '',
				dataSource:[]
			};
		},
		computed:{
			disabledQuery: function(){
				return Object.values(this.comboConds).every((item,i,arr)=>{
					return item === "" || item === undefined
				})
			},
			remittanceModel: function(){
				return this.dataSource.map((item, i , arr)=>{
					try {
						item.detailAmounts = JSON.parse(item.detailAmounts)
					} catch(error) {
						item.detailAmounts = {loanAssetPrincipal : 0,loanAssetInterest : 0,loanServiceFee : 0,loanTechFee : 0,loanOtherFee : 0,overdueFeePenalty : 0,overdueFeeObligation : 0,overdueFeeService : 0,overdueFeeOther : 0,}
					}
					return Object.assign({}, item, item.detailAmounts)
				})
			},
			transactionAmount: function(){
				var sum = Decimal(0)
				try{
					this.remittanceEditModel.forEach((item, i , arr)=>{
						sum = sum.plus(item.loanAssetPrincipal ).plus(item.loanAssetInterest ).plus(item.loanServiceFee ).plus(item.loanTechFee ).plus(item.loanOtherFee ).plus(item.overdueFeePenalty ).plus(item.overdueFeeObligation ).plus(item.overdueFeeService ).plus(item.overdueFeeOther )
					})
					return sum.toNumber()
				}catch(e){
					return ""
				}
			},
			disable: function(){
				var valid = this.remittanceEditModel.every((item,i,arr)=>{
					var origin = item.detailAmounts;
					return item.loanAssetPrincipal  <= origin.loanAssetPrincipal 
						&& item.loanAssetInterest  <= origin.loanAssetInterest 
						&& item.loanServiceFee  <= origin.loanServiceFee 
						&& item.loanTechFee  <= origin.loanTechFee
						&& item.loanOtherFee  <= origin.loanOtherFee
						&& item.overdueFeePenalty  <= origin.overdueFeePenalty
						&& item.overdueFeeObligation  <= origin.overdueFeeObligation
						&& item.overdueFeeService  <= origin.overdueFeeService
						&& item.overdueFeeOther  <= origin.overdueFeeOther
				})
				return !valid || this.transactionAmount > this.residualAmount || this.transactionAmount === ""
			}
		},
		watch: {
			value: function(current) {
				this.visible = current;
			},

            visible: function(current) {
            	this.$emit('input', current);
            	if (!current) {
            		this.dataSource=[];
            	} 
            },
		},
		methods: {
			submit: function(){
				var res = this.remittanceEditModel.map((item, i ,arr) =>{
					delete item.detailAmounts
					return item
				})
				this.$emit('submit', res)
			},
			SelectionChange: function(current){
				//让editModel只修改current变化的部分
				var exsitedInEditModel =  intersectionBy(this.remittanceEditModel, current, 'repaymentPlanNo');
				this.remittanceEditModel = current.map((item,i,arr)=>{
					var v
					item.repaymentBusinessNo = item.repaymentPlanNo
					if(v = exsitedInEditModel.find((ele,i,arr) => {
						return ele.repaymentPlanNo == item.repaymentPlanNo
					})){
						return v
					}else{
						return item
					}
				})
			},
			query: function(){
				ajaxPromise({
					url: this.action,
					data: this.comboConds,
					type: 'post'
				}).then(data =>{
					this.dataSource = data.queryRepaymentPlanShowModels;
				}).catch(message =>{
					MessageBox.open(message)
				})
			}
		}
	}
</script>