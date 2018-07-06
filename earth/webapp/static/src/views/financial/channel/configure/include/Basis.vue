<template>
	<div>
		<el-form 
		    ref="form"
		    :model="internalModel"
		    :rules="rules"
		    label-width="120px" 
		    class="sdf-form">
		    <el-form-item style="padding: 5px 0;" label="基础信息" class="form-item-legend"></el-form-item>
		    <el-form-item label="通道名称" prop="paymentChannelName" required>
		        <el-input class="long" v-model="internalModel.paymentChannelName" placeholder="信托项目代码+网关名称，例：G31700广银联收付"></el-input>
		    </el-form-item>
		    <el-form-item style="padding: 5px 0;" label="网关名称">
		        <span>{{ internalModel.paymentInstitutionName }}</span>
		    </el-form-item>
		    <el-form-item style="padding: 5px 0;" label="商户号">
		    	<span>{{ internalModel.outlierChannelName }}</span>
		    </el-form-item>
		    <el-form-item style="padding: 5px 0;" :label="'关联' + $utils.locale('financialContract')">
		    	<span>{{ internalModel.relatedFinancialContractName }}</span>
		    </el-form-item>
		    <!-- <el-form-item style="padding: 5px 0;" label="专户行、号">
		       	<span>{{ internalModel.captitalAccountNameAndNo }}</span>
		    </el-form-item> -->
		    <el-form-item style="padding: 5px 0;" label="业务类型">
		        <span>{{ internalModel.businessTypeName }}</span>
		    </el-form-item>
		    <el-form-item style="padding: 5px 0;" label="清算号">
		        <span>{{ internalModel.clearingNo }}</span>
		    </el-form-item>
		</el-form>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		props: {
			model: Object,
			paymentChannelUuid: String
		},
		data: function() {
			const isUnique = (rule, value, callback) => {
				if (!value) {
					callback();
					return;
				}

				ajaxPromise({
					url: '/paymentchannel/config/edit/check',
					// async: false,
					data: {
						paymentChannelName: value,
						paymentChannelUuid: this.paymentChannelUuid
					}
				}).then(data => {
					callback();
				}).catch((message, xhr) => {
					callback(new Error('通道名称已存在'));
				});
			};

			return {
				internalModel: Object.assign({}, this.model),
				rules: {
					paymentChannelName: [
						{ required: true, message: ' ' },
						{ max: 50, type: 'string', message: '不能超过50个字符' },
						{ validator: isUnique, trigger: 'blur' }
					]
				},
			}
		},
		watch: {
			model: function(current) {
				this.internalModel = Object.assign({
					paymentChannelName: '',
					relatedFinancialContractName: '',
					// captitalAccountNameAndNo: '',
					outlierChannelName: '',
					clearingNo: '',
					businessTypeName: '',
					paymentInstitutionName : ''
				}, current);
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
			}
		}
	}
</script>