<style lang="sass" scoped>
	.el-form.sdf-form{
		margin-right: 12%;
	}
</style>

<template>
<div>
	<el-form class="sdf-form" label-width="120px" ref="form" :rules="rules" :model="currentModel" >
        <el-form-item prop="accountName" label="开户名称" required>
            <el-input class="middle" v-model.trim="currentModel.accountName"></el-input>
        </el-form-item>
        <el-form-item prop="accountNo" label="开户号" required>
            <el-input class="middle" v-model.trim="currentModel.accountNo"></el-input>
        </el-form-item>
        <el-form-item prop="bankName" label="开户行名" required>
            <el-select class="middle" v-model="currentModel.bankName">
                <el-option 
                    v-for="item in bankNames" 
                    :label="item.value"
                    :value="item.value">
                </el-option>
            </el-select>
        </el-form-item>
	</el-form>
	<specialAccountContainer
		ref="saContainer"
		v-model="specialAccountType.childAccountTypeString" 
		:isChecked="specialAccountSwitchOn" 
		@toggleUseSpecialAccountCfg="toggle"></specialAccountContainer>
	<el-form class="sdf-form" style="margin-top: 40px;" label-width="120px">
		<el-form-item >
			<el-button @click="next" type="primary">下一步</el-button>
	        <a @click="prev" class="prev">上一步</a>
		</el-form-item>
	</el-form>
</div>
</template>

<script>
	import { mapState } from 'vuex';
	import specialAccountContainer from './_specialAccountConfigContainer';
	export default{
		components: {
			specialAccountContainer
		},
		props: {
			specialAccountInfo: {
				default: () =>{}
			},
			specialAccountType: Object,
			specialAccountSwitchOn: Boolean
		},
		watch: {
			specialAccountInfo: function(v){
				var {accountName, accountNo, bankName} = v
				this.currentModel = {accountName, accountNo, bankName}
			},
		},
		data(){
			return {
				currentModel: Object.assign({}, this.specialAccountInfo),
				rules: {
				    accountNo: { required: true, message: ' ', trigger: 'blur'},
				    accountName: { required: true, message: ' ', trigger: 'blur'},
				    bankName: { required: true, message: ' ', trigger: 'change' }
				}
			}
		},
        computed: {
            ...mapState({
                bankNames: state => {
                    var arr = [];
                    var bankNames = state.financialContract.bankNames;
                    Object.keys(bankNames).forEach(bankCode => {
                        arr.push({
                            key: bankNames[bankCode],
                            value: bankNames[bankCode]
                        });
                    });
                    return arr;
                }
            })
        },
		methods: {
			toggle: function(checked){
				this.$emit('specialAccountSwitch', checked)
			},
			next: function(){
				new Promise((resolve, reject) =>{
	                this.$refs.form.validate(valid => {
	                    if (valid) {
	                    	resolve()
	                    }else{
	                    	reject()
	                    }
	                });
				}).then(()=>{
					// debugger
					if (this.specialAccountSwitchOn) {
						this.$refs.saContainer.$refs.form.validate(valid=>{
							if(valid){
								this.$emit('next', this.currentModel);
							}else{
								console.error(valid)
							}
						})
					} else {
						this.$emit('next', this.currentModel);
					}
				}).catch(e=>{
				})
			},
			prev: function() {
                this.$emit('prev');
            }
		}
	}
</script>