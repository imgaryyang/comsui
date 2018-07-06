<style lang="sass">
	#customerLoanInfo {
		padding: 35px 104px 39px 50px;
		border: 1px solid #e0e0e0;
		p{
			line-height: 38px;
		}
		p span{
			font-weight: bold;
			margin-left: 20px;
		}
		p span.cursor-pointer{
			cursor: pointer;
			user-select: none;
		}
		p span:first-child{
			margin-left: 0;
		}
		p span .rect{
			margin-right: 7px;
			display: inline-block;
			height: 12px;
			width: 18px;
			vertical-align: middle;
		}
		.bg{
			display: inline-block;
			margin-right: 3px;
			line-height: 35px;
			vertical-align: middle;
			text-indent: 10px;
		}
		.bg:last-child{
			margin-right: 0;
		}
		.bg-purple{
			background-color: #959be7;
		}
		.bg-blue{
			background-color: #7cb5ec;
		}
		.bg-gray{
			background-color: #999999;
		}
		.title{
			float: left;
			width: 45px;
			height: 35px;
			line-height: 35px;
		}
		.bd-wrapper .chart-wrapper{
			width: 100%;
			margin-top: 30px;
		}
		.chart{
			width:100%;
			height:35px;
			padding-left:45px;
		}
		.chart .el-row{
			margin-left: 0!important;
		}
		.chart .el-tooltip{
			display: block!important;
		}
		.el-tooltip__rel{
			display: block!important;
		}
		.chart .grid-content{
			height: 35px;
			font-size: 11px;
			line-height: 35px;
			text-indent: 10px;
			overflow: hidden;
		}
		.bg-invisible{
			background-color: #e0e0e0;
		}
	}
</style>

<template>
	<div class="bd" id="customerLoanInfo">
		<div class="bd-wrapper">
		    <p>客户贷款金额 :{{loandetail.totalPrincipal + loandetail.totalInterest | formatMoney}}</p>
		    <p>
		    	客户贷款本金：{{loandetail.totalPrincipal | formatMoney}}&nbsp;&nbsp;&nbsp;
		    	客户贷款利息：{{loandetail.totalInterest | formatMoney}}&nbsp;&nbsp;&nbsp;
		    	客户未偿本金： {{ unPaidMoney | formatMoney}}</p>
		    <p>
		        <span class="cursor-pointer" @click="handleCapital('paid')"><i class="rect bg-purple" :class="{ 'bg-invisible': paidIsinvisible}"></i>已还
		        </span><span class="cursor-pointer" @click="handleCapital('normalUnPaid')"><i class="rect bg-gray" :class="{ 'bg-invisible': normalUnPaidIsinvisible}"></i>未偿
		        <!-- </span><span class="cursor-pointer" @click="handleCapital('overdueUnPaid')"><i class="rect bg-gray" :class="{ 'bg-invisible': overdueUnPaidIsinvisible}"></i>未偿</span> -->
		    </p>
		    <div class="chart-wrapper">
				<div class="title">
					本金：
				</div>
				<div class="chart">
					<el-row :gutter="3" v-show="capitalSum">
					  <el-col v-show="capitalPaiedRatio != 0" :span ="capitalPaiedRatio">
					  	<div class="grid-content bg-purple">
						  	<el-tooltip class="item" effect="dark" :content="createToolTip(interCapitalModel.paid)" placement="top">
						  		<div>{{ interCapitalModel.paid | formatMoney }}</div>
						  	</el-tooltip>
					  	</div>
					  </el-col>
					  <el-col v-show="capitalPendingRatio != 0" :span="capitalPendingRatio" >
					  	<div class="grid-content bg-gray">
					  		<el-tooltip class="item" effect="dark" :content="createToolTip(interCapitalModel.normalUnPaid)" placement="top">
					  			<div>{{ interCapitalModel.normalUnPaid | formatMoney}}</div>
					  		</el-tooltip>
					  	</div>
					  </el-col>
					 <!--  <el-col v-show="capitalOverdueRatio != 0" :span="capitalOverdueRatio">
					  	<div class="grid-content bg-gray">
						  	<el-tooltip class="item" effect="dark" :content="createToolTip(interCapitalModel.overdueUnPaid)" placement="top">
						  		<div>{{ interCapitalModel.overdueUnPaid | formatMoney}}</div>
						  	</el-tooltip>
					  	</div>
					  </el-col> -->
					</el-row>
				</div>
			</div>
		    <div class="chart-wrapper">
				<div class="title">
					利息：
				</div>
				<div class="chart">
					<el-row :gutter="3" v-show="loanSum">
					  <el-col v-show="loanPaiedRatio" :span="loanPaiedRatio" >
					  	<div class="grid-content bg-purple">
						  	<el-tooltip class="item" effect="dark" :content="createToolTip(interLoanModel.paid)" placement="top">
						  		<div>{{ interLoanModel.paid | formatMoney}}</div>
						  	</el-tooltip>
					  	</div>
					  </el-col>
					  <el-col v-show="loanPendingRatio" :span="loanPendingRatio" >
					  	<div class="grid-content bg-gray">
					  		<el-tooltip class="item" effect="dark" :content="createToolTip(interLoanModel.normalUnPaid)" placement="top">
					  			<div>{{ interLoanModel.normalUnPaid | formatMoney}}</div>
					  		</el-tooltip>
					  	</div>
					  </el-col>
					 <!--  <el-col v-show="loanOverdueRatio" :span="loanOverdueRatio" >
					  	<div class="grid-content bg-gray">
						  	<el-tooltip class="item" effect="dark" :content="createToolTip(interLoanModel.overdueUnPaid)" placement="top">
						  		<div>{{ interLoanModel.overdueUnPaid | formatMoney }}</div>
						  	</el-tooltip>
					  	</div>
					  </el-col> -->
					</el-row>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import formats from 'filters/format';

	export default{
		props: {
			loandetail:{
				type: Object,
				required: true,
				default: () => ({})
			}
		},
		watch: {
			capitalModel: function(current) {
				this.interCapitalModel = Object.assign({}, this.interCapitalModel, current);
			},
			loanModel: function(current) {
				this.interLoanModel = Object.assign({}, this.interLoanModel, current);
			},
		},
		data: function(){
			return {
				interLoanModel: Object.assign({
					paid: '',
					normalUnPaid: ''
				}, this.loanModel),
				interCapitalModel: Object.assign({
					paid: '',
					normalUnPaid: ''
				}, this.capitalModel),
				paidIsinvisible: false,
				normalUnPaidIsinvisible: false,
				overdueUnPaidIsinvisible: false
			}
		},
		computed:{
			loanModel: function() {
				return {
					paid: this.loandetail.paidInterest,
					normalUnPaid: this.loandetail.totalInterest - this.loandetail.paidInterest
				}
			},
			capitalModel: function() {
				return {
					paid: this.loandetail.paidPrincipal,
					normalUnPaid: this.unPaidMoney
				}
			},
			capitalSum: function() {
				return Object.values(this.interCapitalModel).reduce((prev, next) => {
					return (+prev) + (+next);
				}, 0);
			},
			loanSum: function() {
				return Object.values(this.interLoanModel).reduce((prev, next) => {
					return (+prev) + (+next);
				}, 0);
			},
			capitalPaiedRatio: function(){
				return this.capitalSum === 0 ? 0 : Math.round(24*this.interCapitalModel.paid/(this.capitalSum));
			},
			capitalPendingRatio: function(){
				return this.capitalSum === 0 ? 0 : Math.round(24*this.interCapitalModel.normalUnPaid/(this.capitalSum));
			},
			capitalOverdueRatio: function(){
				return this.capitalSum === 0 ? 0 : 24 - this.capitalPaiedRatio - this.capitalPendingRatio; 
			},
			loanPaiedRatio: function(){
				return this.loanSum === 0 ? 0 : Math.round(24*this.interLoanModel.paid/(this.loanSum));
			},
			loanPendingRatio: function(){
				return this.loanSum === 0 ? 0 : Math.round(24*this.interLoanModel.normalUnPaid/(this.loanSum));
			},
			loanOverdueRatio: function(){
				return this.loanSum === 0 ? 0 : 24 - this.loanPendingRatio - this.loanPaiedRatio;
			},
			unPaidMoney: function(){
				return this.loandetail.totalPrincipal - this.loandetail.paidPrincipal;
			}
		},
		methods:{
			handleCapital: function(type){
				var current = this[type + 'Isinvisible'] = !this[type + 'Isinvisible'];
				this.interLoanModel[type] = current ? 0 : this.loanModel[type];
				this.interCapitalModel[type] = current ? 0 : this.capitalModel[type];
			},
			createToolTip: function(key){
				return formats.formatMoney(key);
			}
		}
	}
</script>