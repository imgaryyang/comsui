<style lang="sass">
    @import '~assets/stylesheets/base';

    #pollingModal {
    	
        @include min-screen(768px) {
            .modal-dialog {
                width: 65%;
                margin: 30px auto;
            }
        }

        .el-table td {
	        .disabled {
	        	color: #999
	        }
        }
        
		.modal-body.align-center {
			text-align: left;
		}
    }
</style>

<template>
	<Modal v-model="show" id="pollingModal">
		<ModalHeader title="通道轮循设置">
		</ModalHeader>
		<ModalBody>
			<el-form
                ref="form"
                :model="currentModel"
                label-width="120px"
                :rules="rules"
                class="sdf-form sdf-modal-form">
                <el-form-item
                    label="收款通道轮循"
                    prop="open"
                    required>
                    <el-radio-group v-model="currentModel.open">
                    	<el-radio :label="0">无轮循</el-radio>
                    	<el-radio :label="1">有轮循</el-radio>
                    </el-radio-group>
                </el-form-item>
                <template v-if="canSelectNumber">
	                <el-form-item
		                required
	                    label="循环次数"
	                    prop="times">
	                    <el-select class="middle" v-model="currentModel.times">
	                    	<el-option 
	                    		v-for="n in 5"
	                    		:label="n"
	                    		:value="n">
	                    	</el-option>
	                    </el-select>
	                </el-form-item>
	                <div class="table">
						<table>
							<thead>
								<tr>
									<th v-for="(item, index) in pollingList">{{ index | POLLING_TABLE_TITLE_FILTER }}</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td v-for="(row, index) in pollingList">
			                			<span v-if="row.disabled" class="disabled">--</span>
				                		<el-time-picker 
					                		v-else
					                		:editable="false"
					                		:clearable="false"
					                		type="datetime"
					                		:value="row.time"
					                		@input="handleTime(arguments[0], index)"
				                			:picker-options="{selectableRange: '1:00:00 - 23:00:00'}">
				                		</el-time-picker>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
                </template>
            </el-form>
		</ModalBody>
		<ModalFooter>
			<el-button @click="show = false">取消</el-button>
			<el-button type="primary" @click="handleSave">保存</el-button>
		</ModalFooter>
	</Modal>
</template>

<script>
	import modalMixin from './modal-mixin';
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
    import Enum from 'assets/javascripts/enum';
    import format from 'filters/format';
    
	const POLLING_TABLE_TITLE = new Enum([
	{
		0: '第一次轮循时间'
	},
	{
		1: '第二次轮循时间'
	},
	{
		2: '第三次轮循时间'
	},
	{
		3: '第四次轮循时间'
	},
	{
		4: '第五次轮循时间'
	}]);

	export default {
		mixins: [modalMixin],
		components: {
			PagingTable: require('views/include/PagingTable')
		},
		props: {
			model: {
				default : null
			},
		},
		data: function() {
			var validateTimes = (rule, value, callback) => {
				if (this.currentModel.open) {
					return value == '' ? callback(new Error(' ')) : callback();
				}
				return callback()
			};

			return {
				show: this.value,
				currentModel: Object.assign({
					open: 0,
					times: '',
				}, this.model),

				rules: {
					open: {required: true, message: ' '},
					times: {required: true, message: ' ', validator: validateTimes}
				},

				pollingList: [{
					time:new Date(2017, 12, 12, 8, 0),
					disabled: true
				},{
					time:new Date(2017, 12, 12, 10, 0),
					disabled: true
				},{
					time:new Date(2017, 12, 12, 12, 0),
					disabled: true
				},{
					time:new Date(2017, 12, 12, 14, 0),
					disabled: true
				},{
					time:new Date(2017, 12, 12, 16, 0),
					disabled: true
				}]
			}
		},
		computed: {
			canSelectNumber: function() {
				return this.currentModel.open == 1;
			},
		},
		watch: {
			model: function(current) {
				this.currentModel = Object.assign({
					open: 0,
					times: '',
				}, current);
			},
			'model.pollings': function(current) {
				var length = current.length;
				this.currentModel.times = length;
				for (var i = 0; i < length; i++) {
					this.pollingList[i].time = new Date('2017-12-12 ' + current[i]);
				}
			},
			'currentModel.times': function(current) {
				var { pollingList } = this;
				for (var i = 0; i < pollingList.length; i++) {
					if (i < current) {
						pollingList[i].disabled = false;
					} else {
						pollingList[i].disabled = true;
					}
				}
			},
			show: function(current) {
				if (!current) {
					this.currentModel =  {
						open: 0,
						times: '',
					}
					this.pollingList = [].concat(
						[{
							time:new Date(2017, 12, 12, 8, 0),
							disabled: true
						},{
							time:new Date(2017, 12, 12, 10, 0),
							disabled: true
						},{
							time:new Date(2017, 12, 12, 12, 0),
							disabled: true
						},{
							time:new Date(2017, 12, 12, 14, 0),
							disabled: true
						},{
							time:new Date(2017, 12, 12, 16, 0),
							disabled: true
						}]
					);
				}
			}
		},
		filters: {
			POLLING_TABLE_TITLE_FILTER: Enum.filter(POLLING_TABLE_TITLE)
		},
		methods: {
			handleSave: function() {
				this.$refs.form.validate(valid => {
					if (valid) {
						var list = this.currentModel.open ? this.pollingList.slice(0, this.currentModel.times) : [];
						var polling =  list.map(item => format.formatDate(item.time, 'HH:mm:ss')).sort();

						var pass = true;
						polling.forEach((time, index) => {
							if (polling.slice(index + 1).includes(time)) {
								pass = false;
								MessageBox.open('时间设置重复，请重新设置');
								return;
							}
						});

						if (!pass) return;
						
						var postData = Object.assign({polling: JSON.stringify(polling)}, _.omit(this.currentModel,'pollings', 'times'));
						ajaxPromise({
							url: '/paymentchannel/switch/strategy/polling',
							type: 'post',
							data: postData
						}).then(data => {
							this.show = false;
		                    MessageBox.open('提交成功');
		                    MessageBox.once('closed', () => {
		                        this.$emit('confirm');
		                    });
						}).catch(message => {
							MessageBox.open(message);
						});
					}
				});

			},
			handleTime: function(time, index) {
				this.pollingList[index].time = time == undefined ? this.pollingList[index].time : time;
			}
		}
	}
</script>