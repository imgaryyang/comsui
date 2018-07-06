<script>
	import specialAccountItem from './_specialAccountConfigItem';
	import { Modal, ModalBody, ModalHeader, ModalFooter } from 'components/Modal';
	export default{
		components: {
			specialAccountItem,
		},
		props: {
			value: Array,
			isChecked: Boolean
		},
		computed:{
			tree: {
				get(){
					this.WithdrawTemplate = null
					//通过路由切换回来时，手动置为空
					this.currentModel = {
						withdraw: []
					}
					return this.value
				},
				set(v){
					this.$emit('input', v)
				}
			}
		},
		data(){
			var required = (rule, value, callback) =>{
				if(Array.isArray(value) && value.length >0){
					callback()
				}else{
					callback(new Error('请添加计提户'))
				}
			}
			return {
				showCreateWithdrawModal: false,
				optionalWithdrawItem: {},
				withdrawOptions: [],
				WithdrawTemplate: null,
				rules: {
					withdraw: { validator: required, trigger: 'blur' }
				},
				currentModel:{
					withdraw: []
				}
			}
		},
		render(h){
			var WithdrawModalSubmit = function(){}
			var self = this
			function createSpecialAccountItem(h, item){
				var el_form_item_cfg = {
					props: {

					}
				}
				var events = {
				    on: {
				      // input: function (event) {
				      //   self.value = event.target.value
				      //   self.$emit('input', event.target.value)
				      // }
				      //不需要手动写?自动会注射input?写了也没用！
				    }
				};
				var config = {
					props: {
				      value: item.childAccountTypeString || [],
				      property: 'accountTypeName'
				    },
				};
				var selectLabelProperty = config.props.property
				if(item[selectLabelProperty] === '计提户'){
					config.props.config = self.isChecked;
					el_form_item_cfg.props.required = true;
					el_form_item_cfg.props.prop = 'withdraw';
					if(!self.WithdrawTemplate){
						self.WithdrawTemplate = item.childAccountTypeString.pop()
						self.currentModel.withdraw = item.childAccountTypeString
					}
					events.on.create = function(){
						self.showCreateWithdrawModal = true;
						self.optionalWithdrawItem = Object.assign({}, self.WithdrawTemplate, {
							[selectLabelProperty]: ''
						})
					}
					WithdrawModalSubmit = function(){
		            	if(self.optionalWithdrawItem[selectLabelProperty].trim() != ''){
							item.childAccountTypeString.push(self.optionalWithdrawItem)
		            	}
		            	self.showCreateWithdrawModal= false
					}
				}
				return (
					<el-form-item label={item.accountTypeName} {...el_form_item_cfg}>
						<specialAccountItem {...events} {...config}></specialAccountItem>
					</el-form-item>
				)
			}
			var el_form_items  = [];
			if(this.tree && this.tree.length> 0){
				el_form_items = this.tree.map(item => {
					return createSpecialAccountItem(h, item)
				})
			}else{
				el_form_items = <el-form-item label="无数据"></el-form-item>
			}

			function CreateModal(h){
				var modalCfg = {
					props: {
				      value: self.showCreateWithdrawModal
					},
					on: {
				      input: function (v) {
						self.showCreateWithdrawModal = v
				      }
					}
				};
				var modalBodySelectCfg = {
					props: {
				      value: self.optionalWithdrawItem.accountTypeName,
				      filterable: true,
				      clearable: true,
				      allowCreate: true
					},
					on: {
				      input: function (v) {
						self.optionalWithdrawItem.accountTypeName = v
				      }
					}
				}
				var modalFooterCancelCfg = {
					on: {
						click: function(){
							self.showCreateWithdrawModal = false
						}
					}
				}
				var modalFooterConfirmCfg = {
					on: {
						click: WithdrawModalSubmit
					},
					props: {
						type : 'success'
					}
				}

				// var el_options = self.withdrawOptions.map(item =>{
				//	return (
				//		<el-option
				// 			key="item.value"
				// 			label="item.label"
				// 			value="item.value">
				// 		</el-option>
				// 	)
				// })
				// <el-select {...modalBodySelectCfg}>
				// 	{el_options}
				// </el-select>
				return (
					<Modal {...modalCfg}>
						<ModalHeader title="计提户添加"></ModalHeader>
						<ModalBody>
							<el-form label-width="100px" label-position="left" style="width: 80%;margin: 20px auto;">
								<el-form-item label="计提户选择">
									<el-input {...modalBodySelectCfg}/>
									<input style="display:none"/>
								</el-form-item>
							</el-form>
						</ModalBody>
				        <ModalFooter>
				            <el-button {...modalFooterCancelCfg}>取消</el-button>
				            <el-button {...modalFooterConfirmCfg}>确定</el-button>
				        </ModalFooter>
					</Modal>
				)
			}
			const isUseSpecialAccount = {
				props: {
					value:self.isChecked
				},
				on: {
					change: function(){
						self.$emit('toggleUseSpecialAccountCfg', !self.isChecked)
					}
				}
			}

			const formCfg = {
				props: {
					rules: self.rules,
					model: self.currentModel
				}
			}
			return (
			<div>
				<el-form class="sdf-form" label-width="120px" ref="form" {...formCfg}>
					<el-form-item label="专户配置信息" class="form-item-legend">
						<el-checkbox {...isUseSpecialAccount}>是否开启专户</el-checkbox>
					</el-form-item>
					{ this.isChecked ? el_form_items : null}
				</el-form>
				{CreateModal(h)}
			</div>
			)
		}
	}
</script>