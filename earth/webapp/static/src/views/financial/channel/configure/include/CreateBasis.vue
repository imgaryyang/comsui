<style type="sass">
	.form_border {
		width: 65%;
		min-width: 500px;
		border: 1px dashed red;
		padding-top: 20px;
		margin-top: 20px;
		margin-bottom: 10px;
	}
</style>
<template>
	<div>
		<el-form 
		    ref="form"
		    :model="internalModel"
		    :rules="rules"
		    label-width="120px" 
		    class="sdf-form">
		    <el-form-item style="padding: 5px 0;" label="基础信息" class="form-item-legend"></el-form-item>
		    <el-form-item style="padding: 5px 0;"  label="通道名称" prop="paymentChannelName" required>
		        <el-input class="long" v-model="internalModel.paymentChannelName" placeholder="信托项目代码+网关名称，例：G31700广银联收付" v-if="initStatus"></el-input>
		        <span v-else>{{ internalModel.paymentChannelName }}</span>
		    </el-form-item>
		    <div :class="{'form_border': !isSaveBasis}">
		    <el-form-item style="padding: 5px 0;" label="唯一编号" prop="uniqueNo" required v-if="initStatus">
		        <el-input class="long" v-model="internalModel.uniqueNo" placeholder="唯一编号"></el-input>
		        <template>
			        <el-form-item style="padding: 5px 0;margin-left: -40px;" label="网关名称">
			       		<span>{{ internalModel.paymentInstitutionName }}</span>
				    </el-form-item>
				    <el-form-item style="padding: 5px 0;margin-left: -54px;" label="商户号">
				    	<span>{{ internalModel.outlierChannelName }}</span>
				    </el-form-item>
		        </template>
		    </el-form-item>
		    <template v-if="isSaveBasis">
			    <el-form-item style="padding: 5px 0;" label="网关名称" >
			        <span>{{ internalModel.paymentInstitutionName }}</span>
			    </el-form-item>
			    <el-form-item style="padding: 5px 0;" label="商户号" >
			    	<span>{{ internalModel.outlierChannelName }}</span>
			    </el-form-item>
		    </template>
		    <el-form-item style="padding: 5px 0;" prop="appId" :label="$utils.locale('financialContract.appAccount.name')" required>
                <el-select class="middle"
                    placeholder="请选择"
                    v-model="internalModel.appId">
                    <el-option
                        v-for="item in financialContractList" 
                        :label="item.label"
                        :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
		    <el-form-item style="padding: 5px 0;" :label="$utils.locale('financialContract')" prop="relatedFinancialContractUuid" required>
	        	<el-select 
	        	    v-model="internalModel.relatedFinancialContractUuid"
	        	    class="middle"
	        	    v-if="initStatus"
	        	    :placeholder="$utils.locale('financialContract')">
	        	    <el-option
	        	        v-for="item in currentFinancialContracts"
	        	        :label="item.label"
	        	        :value="item.value">
	        	    </el-option>
	        	</el-select>
	        	<span v-else>{{ internalModel.relatedFinancialContractName }}</span>
			</el-form-item>
		    <el-form-item style="padding: 5px 0;" label="业务类型" required prop="businessTypeOrdinal">
		    	<el-select 
	        	    v-model="internalModel.businessTypeOrdinal"
	        	    class="middle"
	        	    v-if="initStatus"
	        	    :placeholder="业务类型">
	        	    <el-option
	        	        v-for="item in businessTypeList"
	        	        :label="item.value"
	        	        :value="item.key + ''">
	        	    </el-option>
	        	</el-select>
		        <span v-else>{{ bussinessTypeName }}</span>
		    </el-form-item>
		    <el-form-item style="padding: 5px 0;" label="清算号" prop="clearingNo">
		    	<el-input class="long" v-model="internalModel.clearingNo" placeholder="清算号" v-if="initStatus"></el-input>
		        <span v-else>{{ internalModel.clearingNo }}</span>
		    </el-form-item>
		    <el-form-item style="margin-top: 20px;">
                <el-button type="primary" @click="saveBasis" v-if="initStatus" :loading="isSaveing"> 保存</el-button>
            </el-form-item>
            </div>
            <div style="padding: 5px 0;"  v-if="!isSaveBasis">
		    	<span class="color-danger el-icon-warning">通道信息决定资金流向，填写错误可能引起法律纠纷，请谨慎填写并保存，保存之后不可更改信息！</span>
		    </div>
		</el-form>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		props: {
			businessTypeList: Array,
			financialContractList: Array,
		},
		data: function() {
			const isUnique = (rule, value, callback) => {
				if (!value) {
					callback();
					return;
				}

				ajaxPromise({
					url: '/paymentchannel/config/edit/check',
					data: {
						paymentChannelName: value,
						paymentChannelUuid: ''
					}
				}).then(data => {
					callback();
				}).catch((message, xhr) => {
					callback(new Error('通道名称已存在'));
				});
			};

			const isLegal = (rule, value, callback) => {
				if (!value) {
					this.internalModel.paymentInstitutionName = '';
					this.internalModel.outlierChannelName = '';
					callback();
					return
				}

				ajaxPromise({
					url: `/paymentchannel/config/analyze`,
					data: {
						uniqueNo: this.internalModel.uniqueNo
					}
				}).then(data => {
					this.internalModel = Object.assign(this.internalModel, data);
					this.basisData = Object.assign(this.basisData, data);
					this.$emit('updateBasisData', this.basisData);
					callback();
				}).catch(message => {
					this.internalModel.paymentInstitutionName = '';
					this.internalModel.outlierChannelName = '';
					MessageBox.open(message);
					callback(new Error(' '));
				});
			};

			return {
				internalModel: {
					appId: '',
					paymentChannelName: '',
					uniqueNo: '',
					relatedFinancialContractUuid: '',
					relatedFinancialContractName: '',
					outlierChannelName: '',
					clearingNo: '',
					businessTypeOrdinal: '',
					paymentInstitutionName : '',

				},
				basisData: {},
				isSaveBasis: false,
				isSaveing: false,
				rules: {
					paymentChannelName: [
						{ required: true, message: ' ' },
						{ max: 50, type: 'string', message: '不能超过50个字符' },
						{ validator: isUnique, trigger: 'blur' }
					],
					uniqueNo: [
						{required: true, message: ' ', trigger: 'blur'},
						{ validator: isLegal, trigger: 'blur' }
					],
					appId: {required: true, message: ' ', trigger: 'change'},
					relatedFinancialContractUuid: {required: true, message: ' ', trigger: 'change'},
					businessTypeOrdinal: {required: true, message: ' ', trigger: 'change'},
				},
			}
		},
		watch: {
			// 'internalModel.uniqueNo': function(current) {
			// 	if (!current) return;
			// 	ajaxPromise({
			// 		url: `/paymentchannel/config/analyze/${current}`,
			// 	}).then(data => {
			// 		this.internalModel = Object.assign(this.internalModel, data);
			// 		this.basisData = Object.assign(this.basisData, data);
			// 		this.$emit('updateBasisData', this.basisData);
			// 	}).catch(message => {
			// 		MessageBox.open(message);
			// 	});
			// },
			'internalModel.relatedFinancialContractUuid': function(current) {
				this.basisData = Object.assign(this.basisData, {relatedFinancialContractUuid: current});
				this.$emit('updateBasisData', this.basisData);

				var contractName = '';
				this.currentFinancialContracts.forEach(item => {
					if(item.value == current) {
						contractName = item.contractName;
					} 
				});

				this.internalModel.relatedFinancialContractName = contractName;
			},
			isSaveBasis: function(current) {
				if (current) this.$emit('isSaveBasis', current);
			},
			'internalModel.appId': function(current) {
                this.internalModel.relatedFinancialContractUuid = '';
            }
		},
		computed: {
			initStatus: function() {
				return !this.isSaveBasis;
			},
			bussinessTypeName: function() {
				var name = '';
				this.businessTypeList.forEach(item => {
					if (item.key == this.internalModel.businessTypeOrdinal) {
						name = item.value;
					}
				});
				return name;
			},
			currentFinancialContracts: function() {
                var result = [];
                this.financialContractList.forEach(item => {
                    if (item.value == this.internalModel.appId) {
                        result = item.children;
                    }
                });
                return result;
            }
		},
		methods: {
			save: function() {
				return this.internalModel;
			},
			validate: function() {
				return new Promise((resolve, reject) => {
					this.$refs.form.validate(valid => {
						resolve(valid)
					});
				});
			},
			saveBasis: function() {
				this.$refs.form.validate(valid => {
					if (valid) this.isSaveBasis = true;
				});
			},
			resetFields: function() {
				this.isSaveBasis = false;
				this.$refs.form.resetFields();
				this.internalModel.appId = '';
				this.internalModel.uniqueNo = '';
				this.internalModel.clearingNo = '';
				this.internalModel.paymentInstitutionName = '';
				this.internalModel.outlierChannelName = '';
			},
			getModel: function() {
				return this.internalModel;
			},
		},
		beforeRouteEnter: function(){
			console.log(111);
		}
	}
</script>