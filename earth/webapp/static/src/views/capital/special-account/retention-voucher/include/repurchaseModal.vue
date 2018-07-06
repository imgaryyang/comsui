<style lang="sass">
	@import '~assets/stylesheets/base';
	#retention_voucher_repurchase_modal{
		@include min-screen(768px) {
			.modal-dialog{
				width: 65%;
				
				.error {
					input{
						border-color: #e8415f;
					}
				}
			}
		}
		.query-area {
			background: none;
		}
		.danger-color{
			input{
				border-color: #ff4949;
			}
		}
	}
</style>
<template>
	<Modal v-model="visible" id="retention_voucher_repurchase_modal">
		<ModalHeader title="回购弹框"></ModalHeader>
		<ModalBody align="left">
			<div class="row-layout-detail" style="padding: 0;">
				<div class="query-area" style="background:none;padding:0 0 20px 0;border:none">
					<el-form
						ref="form"
					    class="sdf-form sdf-query-form" 
					    :inline="true">
				        <el-form-item>
				        	<ComboQueryBox v-model="comboConds">
				        		<el-option value="repurchaseNo" label="回购编号"></el-option>
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
							:data="showDataSource"
							style="width:100%"
							max-height="400"
							stripe
							borde
							@selection-change="SelectionChange">
							<el-table-column 
								type="selection"
	      						width="55">
							</el-table-column>
							<el-table-column prop="repurchaseDocUuid" label="回购编号"></el-table-column>
							<el-table-column prop="appName" label="商户名称"></el-table-column>
							<el-table-column prop="repurchasePrincipal" label="回购本金" inline-template>
								<div>{{ row.detailAmount.repurchasePrincipal | formatMoney}}</div>
							</el-table-column>
							<el-table-column prop="repurchaseInterest" label="回购利息" inline-template>
								<div>{{ row.detailAmount.repurchaseInterest | formatMoney}}</div>
							</el-table-column>
							<el-table-column prop="repurchasePenalty" label="回购罚息" inline-template>
								<div>{{ row.detailAmount.repurchasePenalty | formatMoney}}</div>
							</el-table-column>
							<el-table-column prop="repurchaseOtherFee" label="回购其他费用" inline-template>
								<div>{{ row.detailAmount.repurchaseOtherFee | formatMoney}}</div>
							</el-table-column>
							<el-table-column prop="repurchaseDays" label="回购天数"></el-table-column>
							<el-table-column prop="lastModifedTime" label="状态变更时间" inline-template>
								<div>{{ row.lastModifedTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
							</el-table-column>
							<el-table-column prop="repurchaseStatus" label="回购状态"></el-table-column>
						</el-table>
					</div>
				</div>
				<div class="block">
				    <h5 class="hd">
				        回购金额
				    </h5>
				    <div class="bd">
				        <el-table 
				            :data="repurchaseEditModel"
				            class="td-15-padding th-8-15-padding no-th-border"
				            stripe
				            border>
				            <el-table-column prop="repurchaseDocUuid" label="回购编号">
				            </el-table-column>
				            <el-table-column prop="appName" label="商户名称">
				            </el-table-column>
				            <el-table-column prop="repurchasePrincipal " label="回购本金" inline-template>
				            	<el-input :class="[+row.repurchasePrincipal  <= row.detailAmount.repurchasePrincipal  ? '' : 'danger-color']" size="small" v-model="row.repurchasePrincipal "></el-input>
				            </el-table-column>
				            <el-table-column prop="repurchaseInterest " label="回购利息" inline-template>
				            	<el-input :class="[+row.repurchaseInterest  <= row.detailAmount.repurchaseInterest  ? '' : 'danger-color']" size="small" v-model="row.repurchaseInterest "></el-input>
				            </el-table-column>
				            <el-table-column prop="repurchasePenalty " label="回购罚息" inline-template>
				            	<el-input :class="[+row.repurchasePenalty  <= row.detailAmount.repurchasePenalty  ? '' : 'danger-color']" size="small" v-model="row.repurchasePenalty "></el-input>
				            </el-table-column>
				            <el-table-column prop="repurchaseOtherFee " label="回购其他费用" inline-template>
				            	<el-input :class="[+row.repurchaseOtherFee  <= row.detailAmount.repurchaseOtherFee  ? '' : 'danger-color']" size="small" v-model="row.repurchaseOtherFee "></el-input>
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
	import MessageBox from 'components/MessageBox';
	import { intersectionBy } from 'lodash';
	import Decimal from 'decimal.js';
    import { ajaxPromise } from 'assets/javascripts/util';

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
				action:'temporary-deposit-doc/query-repurchase-doc',
				visible: this.value,
				comboConds:{
					repurchaseNo: '',
					customerName: '',
					contractNo: '',
					contractUniqueId: ''
				},
				disable: false,
				transactionAmount: '',
				dataSource:[],
				repurchaseEditModel: []
			};
		},
		computed: {
			transactionAmount: function(){
				var sum = Decimal(0)
				try{
					this.repurchaseEditModel.forEach((item, i , arr)=>{
						sum = sum.plus(item.repurchasePrincipal ).plus(item.repurchaseInterest ).plus(item.repurchasePenalty ).plus(item.repurchaseOtherFee )
					})
					return sum.toNumber()
				}catch(e){
					return ""
				}
			},
			disable: function(){
				var valid = this.repurchaseEditModel.every((item,i,arr)=>{
					var origin = item.detailAmount;
					return item.repurchasePrincipal  <= origin.repurchasePrincipal 
						&& item.repurchaseInterest  <= origin.repurchaseInterest 
						&& item.repurchasePenalty  <= origin.repurchasePenalty 
						&& item.repurchaseOtherFee  <= origin.repurchaseOtherFee
				})
				return !valid || this.transactionAmount > this.residualAmount || this.transactionAmount === ""
			},
			disabledQuery: function(){
				return Object.values(this.comboConds).every((item,i,arr)=>{
					return item === "" || item === undefined
				})
			},
			showDataSource: function() {
				return this.dataSource.map((item, i , arr)=>{
					try {
						item.detailAmount = JSON.parse(item.detailAmount)
					} catch(error) {
						item.detailAmount = {}
					}
					return Object.assign({}, item, item.detailAmount)
				})
			}
		},
		watch: {
			value: function(current) {
				this.visible = current;
			},

            visible: function(current) {
            	this.$emit('input', current);
            	if (!current) {
            		this.dataSource = [];
            	} 
            },
 		},
		methods: {
			SelectionChange: function(current){
				//让editModel只修改current变化的部分
				var exsitedInEditModel =  intersectionBy(this.repurchaseEditModel, current, 'repurchaseDocUuid');
				this.repurchaseEditModel = current.map((item,i,arr)=>{
					var v
					if(v = exsitedInEditModel.find((ele,i,arr) => {
						return ele.repurchaseDocUuid == item.repurchaseDocUuid
					})){
						return v
					}else{
						return item
					}
				})			
			},
			submit: function() {
				var res = [];
				this.repurchaseEditModel.forEach((item, i ,arr)=>{
					var {contractUniqueId, repurchaseDocUuid,repurchasePrincipal ,repurchaseInterest ,repurchasePenalty ,repurchaseOtherFee } = item;
					var repaymentBusinessNo = repurchaseDocUuid;
					res.push({contractUniqueId, repaymentBusinessNo,repurchasePrincipal,repurchaseInterest,repurchasePenalty,repurchaseOtherFee});
				})
				this.$emit('submit', res)
			},
			query: function(){
				ajaxPromise({
					url: this.action,
					data: this.comboConds,
					type: 'post'
				}).then(data =>{
					this.dataSource = data.queryRepurchaseShowModels
				}).catch(message =>{
					MessageBox.open(message)
				})
			}
		}
	}
</script>