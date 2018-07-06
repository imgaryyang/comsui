<script>
	import specialAccountItem from './include/EditConfigItem';
	import Breadcrumb from 'components/Breadcrumb';
	import { Modal, ModalBody, ModalHeader, ModalFooter } from 'components/Modal';
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	export default{
		components: {
			specialAccountItem,
		},
		data(){
			return {
				tree: {},
				parentAccountUuid: '',
				showCreateWithdrawModal: false,
				optionalWithdrawItem: {},
				withdrawOptions: [],
				WithdrawTemplate: undefined
			}
		},
		activated(){
			ajaxPromise({
				url: `/capital/${this.$route.params.id}/query-financial-accrual-account`,
				type: 'post'
			}).then(data => {
				function transfer(jsonStr){
					var o = {};
					if (typeof jsonStr == 'string'){
						try{
							o = JSON.parse(jsonStr)
							function recurs(o){
								if(typeof o.childAccountTypeString == 'string'){
									o.childAccountTypeString = JSON.parse(o.childAccountTypeString)
									o.childAccountTypeString.forEach(item => {
										recurs(item)
									})
								}
							}
							recurs(o)
						}catch(e){
							console.error(e)
						}
					}
					return o
				}
				this.tree = transfer(data.model.childAccountTypeString);
				this.parentAccountUuid = data.model.parentAccountUuid;
			}).catch(message =>{
				MessageBox.open(message)
			})
		},
		methods: {
			submit: function(){
				var res = this.tree.childAccountTypeString.filter(item => item._config)
				res.forEach(item =>{
					item.parentAccountUuid = this.parentAccountUuid,
					item.financialContractUuid = this.$route.params.id
				})
				ajaxPromise({
					url: '/capital/modify-accrual-account',
					type: 'post',
					data: {
						accrual_account_list_json: JSON.stringify(res)
					}
				}).then(data => {
					this.tree.childAccountTypeString.forEach(item =>{
						if(item._config){
							this.$delete(item,'_config')
						}
					})
					MessageBox.open('提交成功')
				}).catch(message =>{
					MessageBox.open(message)
				})
			}
		},
		render(h){
			var WithdrawModalSubmit = function(){}
			var self = this
			function createSpecialAccountItem(h, item){
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
					config.props.config = true
					if(!self.WithdrawTemplate){
						self.WithdrawTemplate = Object.assign({_config: true},item.childAccountTypeString[0])
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
					<specialAccountItem {...events} {...config}></specialAccountItem>
				)
			}
			var el_form_items  = [];
			if(this.tree){
				el_form_items = (
						<el-form-item label={this.tree.accountTypeName} class="form-item-legend">
							{
								createSpecialAccountItem(h, this.tree)
							}
						</el-form-item>
					)
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
					},
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
				// 	return (
				// 		<el-option
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
			return (
			<div class="content">
				<div class="scroller">
					<Breadcrumb {...{
						props: {
							routes: [{title: this.$route.query.title}]
						}
					}}></Breadcrumb>
					<el-form class="sdf-form" label-width="120px" style="margin-top:36px;" >
						<el-form-item label="专户配置信息" class="form-item-legend"></el-form-item>
							{el_form_items}
						<el-form-item >
							<el-button onClick={this.submit} type="primary">确认</el-button>
						</el-form-item>
					</el-form>
					{CreateModal(h)}
				</div>
			</div>
			)
		}
	}
</script>