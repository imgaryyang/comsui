<template>
	<div>
		<el-form 
		    ref="form"
		    :model="internalModel"
		    :rules="rules"
		    label-width="120px" 
		    class="sdf-form">
		    <el-form-item :label="title" class="form-item-legend"></el-form-item>
		    <el-form-item label="通道状态" required prop="channelStatus">
		    	<el-select v-if="workingStatus == 0" class="short" v-model="internalModel.channelStatus" disabled >
		    		<el-option value="NOTLINK" label="未对接" selected></el-option>
		    	</el-select>
		    	<el-select v-else class="short" v-model="internalModel.channelStatus">
		    		<el-option 
		    			v-for="item in channelWorkingStatus"
		    			:label="item.value"
		    			:value="item.key">
	    			</el-option>
		    	</el-select>
		    </el-form-item>
		    <template  v-if="currentWorkingStatus == 1">
			    <el-form-item label="通道扣率" required prop="chargeRateMode">
		    		<el-form-item style="display: inline-block;">
		    			<el-select class="short" v-model="internalModel.chargeRateMode" :disabled="disabled">
		    				<el-option 
		    					v-for="item in chargeRateMode"
		    					:label="item.value"
		    					:value="item.key"></el-option>
		    			</el-select>
		    		</el-form-item>
			    	<template v-if="internalModel.chargeRateMode == 'SINGLEFIXED'">
			    		<el-form-item style="display: inline-block;" prop="chargePerTranscation" required>
			    			<el-input class="short" v-model="internalModel.chargePerTranscation" :disabled="disabled"></el-input><span class="color-dim">元/笔</span>
			    		</el-form-item>
			    	</template>
			    	<template v-else>
			    		<el-form-item style="display: inline-block;" prop="chargeRatePerTranscation">
			    			<el-input class="short" v-model="internalModel.chargeRatePerTranscation" :disabled="disabled"></el-input>
			    			<span class="color-dim">%</span>
			    		</el-form-item>
			    		<el-form-item style="display: inline-block;" prop="lowerestChargeLimitPerTransaction">
			    			<el-input class="short" placeholder="最低收取" v-model="internalModel.lowerestChargeLimitPerTransaction" :disabled="disabled"></el-input>
			    			<span class="color-dim">至</span>
			    		</el-form-item>
			    		<el-form-item style="display: inline-block;" prop="upperChargeLimitPerTransaction">
			    			<el-input class="short" placeholder="最高收取" v-model="internalModel.upperChargeLimitPerTransaction" :disabled="disabled"></el-input>
			    			<span class="color-dim" style="margin-right: 10px">元/笔</span>
			    			<HelpPopover content="最低最高收取为空时不作保底封顶计算。"/>
			    			<span class="color-danger" v-if="chargeRateModeError" style="margin-left: 10px">{{ chargeRateModeError }}</span>
			    		</el-form-item>
			    	</template>
			    </el-form-item>
			    <el-form-item label="通道单笔限额">
			    	<el-form-item v-if="title == '收款交易'" prop="trasncationLimitPerTransaction">
			    		<el-input class="short" v-model="internalModel.trasncationLimitPerTransaction" :disabled="disabled"></el-input> <span class="color-dim">万元/笔</span>
			    	</el-form-item>
			    	<template v-if="title == '付款交易'">
			    		<el-form-item style="display: inline-block;" prop="trasncationLimitPerTransactionMin">
				    		<el-input class="short" v-model="internalModel.trasncationLimitPerTransactionMin" :disabled="disabled"></el-input>
				    		<span class="color-dim">~</span>
			    		</el-form-item>
				    	<el-form-item style="display: inline-block;" prop="trasncationLimitPerTransactionMax">
				    		<el-input class="short" v-model="internalModel.trasncationLimitPerTransactionMax" :disabled="disabled"></el-input>
				    		<span class="color-dim">万元/笔</span>
				    		<span class="color-danger" v-if="trasncationLimitError" style="margin-left: 10px">{{ trasncationLimitError }}</span>
				    	</el-form-item>
			    	</template>
			    </el-form-item>
			    <el-form-item label="费用模式" prop="chargeExcutionMode" required>
			    	<el-select class="short" v-model="internalModel.chargeExcutionMode" :disabled="disabled">
			    		<el-option 
			    			v-for="item in chargeExcutionMode"
			    			:label="item.value"
			    			:value="item.key">
		    			</el-option>
			    	</el-select>
			    </el-form-item>
			    <el-form-item label="清算周期" required>
			    	<el-form-item prop="clearingInterval">
		    			<span>T+</span>
			    		<el-input class="short" v-model="internalModel.clearingInterval" :disabled="disabled"></el-input>
		    			<span>日</span>
			    	</el-form-item>
			    </el-form-item>
			    <el-form-item label="银行限额列表">
	    		    <el-upload
	    		    	accept=".xlsx,.xls"
	    		    	style="width: auto; display: inline-block;"
	    		    	:on-remove="handleRemove"
	    		    	:on-success="handleSuccess"
	    		    	:data="uploadData"
	    		    	v-if="!disabled"
	    		    	:default-file-list="defaultFileList"
	    	        	:action="`${api}/paymentchannel/file/upload`">
	    	        	<el-button style="width: 115px;" class="button-multimedia">点击上传</el-button>
	    	        </el-upload>
	    	        <el-button style="width: 115px;" :plain="true" class="button-multimedia" :disabled="disabled" v-else>点击上传</el-button>
	    	        <a style="margin:0px 10px" :href="`${api}/paymentchannel/file/download?fileKey=1`">下载限额表模版</a>
	    	        <a style="margin:0px 10px" :href="`${api}/paymentchannel/file/download?fileKey=2`">下载银行编号列表</a>
	    	        <HelpPopover content="可选配置项，限额列表为空代表不限额。"/>
	    	        <div v-if="showPreview"><a href="#" @click.prevent="$emit('preview')">限额预览</a></div>
			    </el-form-item>
		    </template>
		</el-form>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
    import { REGEXPS } from 'src/validators';


	export default {
		components: {
			HelpPopover: require('views/include/HelpPopover')
		},
		props: {
			model: Object,
			title: String,
			channelWorkingStatus: {
				default: () => [],
				type: Array
			},
			chargeRateMode: {
				default: () => [],
				type: Array
			},
			chargeExcutionMode: {
				default: () => [],
				type: Array	
			},
			workingStatus: {
				type: Number, String,
			},
			accountSide: [Number, String],
			paymentInstitutionOrdinal: [Number, String],
			outlierChannelName: [Number, String],
			showPreview: {
				default: false,
				type: Boolean
			}
		},
		data: function() {
			const compare = (lowerStr, upperStr, type) => {
				var lower = parseFloat(lowerStr);
				var upper = parseFloat(upperStr);
				if (lower > upper) {

					type ? this.trasncationLimitError = '最高收取应大于最低收取' : this.chargeRateModeError = '最高收取应大于最低收取';
					return false;
				} else {
					return true;
				}
			};
			const rightFormat = (value, type) => {
				if (!REGEXPS.MONEY.test(value)) {
					type ? this.trasncationLimitError = '请输入正数' : this.chargeRateModeError = '请输入正数';
					return false;
				}
				return true;
			};
			const chargeRatePerTranscationValidator = (rule, value, callback) => {
				this.chargeRateModeError = ''
				if (this.internalModel.chargeRateMode == 'SINGLERATA') {
					value ? rightFormat(value) ? callback() : callback(new Error(' ')) : callback(new Error(' '));
					return;
				}
				callback();
			};
			const lowerestChargeValidator = (rule, value, callback) => {
				this.chargeRateModeError = ''
				if (this.internalModel.chargeRateMode == 'SINGLERATA') {
					if (value) {
						if (!rightFormat(value)) {
							callback(new Error(' '));
							return;
						}
						if (this.internalModel.upperChargeLimitPerTransaction) {
							compare(value, this.internalModel.upperChargeLimitPerTransaction) ? callback() : callback(new Error(' '));
							return;
						} 
					} 
				} 
				callback();
			};
			const upperChargeValidator = (rule, value, callback) => {
				this.chargeRateModeError = ''
				if (this.internalModel.chargeRateMode == 'SINGLERATA') {
					if (value) { 
						if (!rightFormat(value)) {
							callback(new Error(' '));
							return;
						}
						if (this.internalModel.lowerestChargeLimitPerTransaction) {
							compare(this.internalModel.lowerestChargeLimitPerTransaction, value) ? callback() : callback(new Error(' '));
							return;
						}
					}
				}
				callback();
			};
			const trasncationLimitMin = (rule, value, callback) => {
				this.trasncationLimitError = ''
				if (this.title == '付款交易') {
					if (value) {
						if (!rightFormat(value, 'trasncationLimit')) {
							callback(new Error(' '));
							return;
						}
						if (this.internalModel.trasncationLimitPerTransactionMax) {
							compare(value, this.internalModel.trasncationLimitPerTransactionMax, 'trasncationLimit') ? callback() : callback(new Error(' '));
							return;
						}
					} 
				} 
				callback();
			};
			const trasncationLimitMax = (rule, value, callback) => {
				this.trasncationLimitError = ''
				if (this.title == '付款交易') {
					if (value) { 
						if (!rightFormat(value, 'trasncationLimit')) {
							callback(new Error(' '));
							return;
						}
						if (this.internalModel.trasncationLimitPerTransactionMin) {
							compare(this.internalModel.trasncationLimitPerTransactionMin, value, 'trasncationLimit') ? callback() : callback(new Error(' '));
							return;
						}
					} 
				}
				callback();
			};
			return {
				internalModel: Object.assign({}, this.model),
				defaultFileList: [],
				chargeRateModeError: '',
				trasncationLimitError: '',
				currentWorkingStatus: this.workingStatus,//是否显示详细配置
				rules: {
					channelStatus: { required: true, message: ' ', trigger: 'change'},
					chargeRateMode: { required: true, message: ' ', trigger: 'change'},
					chargeExcutionMode: { required: true, message: ' ', trigger: 'change'},
					clearingInterval: { required: true, message: ' ', trigger: 'blur'},
					chargeRatePerTranscation: {required: true, trigger: 'blur', validator: chargeRatePerTranscationValidator},
					lowerestChargeLimitPerTransaction: { validator: lowerestChargeValidator, trigger: 'blur'},
					upperChargeLimitPerTransaction: { validator: upperChargeValidator, trigger: 'blur'},
					trasncationLimitPerTransactionMin: { validator: trasncationLimitMin, trigger: 'blur'},
					trasncationLimitPerTransactionMax: { validator: trasncationLimitMax, trigger: 'blur'}
				},
			}
		},
		computed: {
			uploadData: function() {
				const search = {
					paymentInstitutionName: this.paymentInstitutionOrdinal,
					outlierChannelName: this.outlierChannelName,
					accountSide: this.accountSide
				};
				return search;
			},
			disabled: function() {
				return this.internalModel.channelStatus == 'OFF';
			}
		},
		watch: {
			model: function(current) {
				this.internalModel = Object.assign({}, current);

				this.internalModel.clearingInterval += '';
				this.defaultFileList = [];

				if (current.bankLimitationFileKey && current.bankLimitationFileName) {
					this.defaultFileList = [{name: current.bankLimitationFileName, key: current.bankLimitationFileKey}];
				}

				this.$refs.form.resetFields();
			},
			'internalModel.chargeRateMode': function(current) {
				var { internalModel } = this;
				if (current == 'SINGLEFIXED') {
					internalModel.chargeRatePerTranscation = '';
					internalModel.lowerestChargeLimitPerTransaction = '';
					internalModel.upperChargeLimitPerTransaction = '';
				} else {
					internalModel.chargePerTranscation = '';
				}
			},
			workingStatus: function(current) {
				this.currentWorkingStatus = current;
			},
			'internalModel.channelStatus': function(current) {
				if (this.workingStatus == -1 && current == 'NOTLINK') {
					this.currentWorkingStatus = 0;//新增时
				} else {
					if (this.workingStatus != 0) {
						this.currentWorkingStatus = 1;
					}
				}

				if (current != "ON") {
					var { channelStatus, ...rest } = this.model
					this.internalModel = Object.assign({}, this.internalModel, rest);
					this.chargeRateModeError = '';
				}

			}
		},
		methods: {
			save: function() {
				if (this.defaultFileList.length) {
					var file = this.defaultFileList[0];
					this.internalModel.bankLimitationFileName = file.name;
					this.internalModel.bankLimitationFileKey = file.key;
				}

				return this.currentWorkingStatus == 0 ? {channelStatus: 'NOTLINK'} : this.internalModel;
			},
			validate: function() {
				return new Promise((resolve, reject) => {
					if (this.internalModel.channelStatus != 'ON') {
						resolve(true);
						return;
					}
					this.$refs.form.validate(valid => {
						resolve(valid);
					});
				});
			},
			handleRemove: function(file, fileList) {
				ajaxPromise({
					url: `/paymentchannel/file/delete`,
					data: this.uploadData,
					type: 'post'
				}).then(data => {
					this.defaultFileList = [];
					this.internalModel.bankLimitationFileName = '';
					this.internalModel.bankLimitationFileKey = '';
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			handleSuccess: function(response, file, fileList) {
				if (response.code != 0) {
					fileList.pop();
					MessageBox.open(response.message);
					return;
				}
				this.defaultFileList = [{name: response.data.fileName, key: response.data.fileKey}];
			},
			resetFields: function() {
				this.internalModel = {};
				this.$refs.form.resetFields();
			}
		}
	}
</script>