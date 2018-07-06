<style lang="sass">
	@import '~assets/stylesheets/base';
	#addCashFlow {
		.modal-dialog{
			width: 65%;
			
			.error {
				input{
					border-color: #e8415f;
				}
			}
		}
		.query-area {
			background: none;
		}
	}
</style>
<template>
	<Modal v-model="visible"  id="addCashFlow">
		<ModalHeader title="添加流水"></ModalHeader>
		<ModalBody align="left">
			<div class="row-layout-detail" style="padding: 0;">
				<div class="query-area" style="background:none;padding:0 0 20px 0;border:none">
					<el-form
						ref="form"
					    class="sdf-form sdf-query-form" 
					    :inline="true">
				        <el-form-item>
				        	<ComboQueryBox v-model="comboConds">
				        		<el-option value="bankSequenceNo" label="流水号"></el-option>
				        		<el-option value="tradeRemark" label="交易摘要"></el-option>
				        		<el-option value="counterAccountNo" label="银行账号"></el-option>
				        	</ComboQueryBox>
				        </el-form-item>
				        <el-form-item>
				        	<el-button type="primary" size="small">查询</el-button>
				        </el-form-item>
					</el-form>
				</div>
				<div class="block">
					<div class="bd">
						<el-table
							class="td-15-padding th-8-15-padding no-th-border"
							:data="dataSource.list"
							style="width:100%"
							max-height="400"
							stripe
							borde
							@selection-change="SelectionChange">
							<el-table-column 
								type="selection"
	      						width="55">
							</el-table-column>
							<el-table-column prop="bankSequenceNo" label="流水号">
							</el-table-column>
							<el-table-column 
								prop="accountSide"
								label="借贷标志">
							</el-table-column>
							<el-table-column 
								inline-template
								label="交易金额">
								<div>{{ row.tradeAmount | formatMoney}}</div>
							</el-table-column>
							<el-table-column label="银行账号" prop="counterAccountNo"></el-table-column>
							<el-table-column 
								label="银行账户名" 
								prop="counterAccountName">
							</el-table-column>
							<el-table-column 
								label="开户行" 
								prop="bankName">
							</el-table-column>
							<el-table-column 
								inline-template
								label="入账时间">
								<div>{{ row.tradeTime | formatDate}}</div>
							</el-table-column>
							<el-table-column 
								label="摘要" 
								prop="tradeRemark">
							</el-table-column>
							<el-table-column
								inline-template
								label="对应类型"
								width="150px">
								<div>
									<el-select
										clearable  
										v-model="row.tradeType"
										:class="row.error ? 'error':''"
									    size="small" 
									    placeholder="请选择"
									    :change="changetradeType(row)">
									    <el-option
									    	v-for="(value, label) in tradeTypes"
									    	:label="value"
									    	:value="label">
									    </el-option>
									</el-select>
								</div>
							</el-table-column>
						</el-table>
					</div>
				</div>
			</div>
		</ModalBody>
		<ModalFooter>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="success" @click="submit">确定</el-button>
		</ModalFooter>
	</Modal>
</template>

<script>
	import Pagination, { extract } from 'mixins/Pagination';
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		mixins: [Pagination],
		components: {
			ComboQueryBox: require('views/include/ComboQueryBox'),
		},
		props: {
			value: Boolean,
			externalTradeBatchUuid:String
		},
		data: function() {
			return {
				action:'/trade/external-batch/cash/query',
				validate: true,
				visible: this.value,
				comboConds:{},
				tradeTypes:{},
				selectData:[]
			};
		},
		computed: {
			conditions: function() {
                return Object.assign({externalTradeBatchUuid:this.externalTradeBatchUuid},this.comboConds);
            }
		},
		watch: {
			value: function(current) {
				this.visible = current;
			},
			externalTradeBatchUuid: function(current){
				this.externalTradeBatchUuid = current;
			},
            visible: function(current) {
            	this.$emit('input', current);
            	if (!current) {
            	    this.$refs.form.resetFields();
            		this.comboConds = {};
            		this.dataSource.list=[];
            	} 
            },
		},
		methods: {
			beforeUpload: function(file) {
				this.fields.file = file;
				return false
			},
			fetch: function() {
			    if(this.visible){
				    this.getData({
				        url: this.action,
				        data: this.conditions
				    });
			    }
			},
			onSuccess: function(data) {
				data.list.forEach((row)=>{
					row.error = false;
					row.tradeType = '';
				})
			    this.dataSource.list = data.list;
			    this.dataSource.size = data.size;
				this.tradeTypes = data.tradeTypes;
			},
			getList: function() {
				ajaxPromise({
					url: `/trade/external-batch/optionData`,
				}).then(data => {
					this.arrivalState = data.arrivalState || [];

					this.financialContracts = data.financialContracts || [];
					this.tradeBatchState = data.tradeBatchState || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			select: function() {
				
			},
			SelectionChange: function(value) {
				this.selectData = value;
			},
			valid: function() {
				this.validate = true;
				this.selectData.forEach((row) =>{
					if(row.tradeType === ''){
						row.error = true;
						this.validate = false;
					}
				})
			},
			submit: function() {

				if(this.selectData.length == 0 ){
					this.visible = false;
				} else {
					this.valid();

					if(this.validate){
						var data = {};
						data.externalTradeBatchUuid = this.externalTradeBatchUuid;
						data.cashFlowInfos =[];
						this.selectData.forEach((row)=>{
							var item = {};
							item.cashFlowUuid = row.cashFlowUuid;
							item.relatedTradeType = row.tradeType;
							data.cashFlowInfos.push(item);
						});
						data.cashFlowInfos = JSON.stringify(data.cashFlowInfos);
						ajaxPromise({
							url: '/trade/external-batch/cash/bind',
							type: 'POST',
							data: data,
						}).then(data =>{
							this.visible = false;
							setTimeout(()=> {
								MessageBox.open('添加成功');
								this.$emit('submit');
							},500);
						}).catch(message => {
							MessageBox.open(message);
						});
					}
				}
			},
			changetradeType: function(value) {
				if( typeof value.tradeType == 'undefined'){
					value.tradeType = '';
					return;
				}
				if(value.tradeType!= ''){
					value.error = false;
				}


			}
		}
	}
</script>