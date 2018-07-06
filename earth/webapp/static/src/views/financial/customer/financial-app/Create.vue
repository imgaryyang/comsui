<style lang="sass">
	
</style>

<template>
	<div class="content">
		<div class="scroller">
			<Breadcrumb :routes="[{ title: '信托商户管理'},{ title: '信托商户新增'}]">
			</Breadcrumb>
			<el-form 
	            ref="form"
	            :rules="rules" 
	            label-width="120px"
	            :model="currentModel" 
	            class="sdf-form">
	            <el-form-item label="商户公司信息" class="form-item-legend"></el-form-item>
	            <el-form-item label="商户公司全称" prop="companyFullName" required>
	                <el-input class="long" v-model.trim="currentModel.companyFullName"></el-input>
	            </el-form-item>
	            <el-form-item label="商户简称" prop="appName" required>
	                <el-input class="long" v-model.trim="currentModel.appName"></el-input>
	            </el-form-item>
	            <el-form-item label="商户代码" prop="appId" required>
	                <el-input class="long" v-model.trim="currentModel.appId"></el-input>
	            </el-form-item>
	            <el-form-item label="地址" prop="address" required>
	                <el-input class="long" v-model.trim="currentModel.address"></el-input>
	            </el-form-item>
	            <el-form-item label="公司法人" prop="legalPerson">
	                <el-input class="long" v-model.trim="currentModel.legalPerson"></el-input>
	            </el-form-item>
	            <el-form-item label="公司营业执照" prop="businessLicence">
	                <el-input class="long" v-model.trim="currentModel.businessLicence"></el-input>
	            </el-form-item>
	            <el-form-item style="margin-top: 20px;">
	            	<el-button @click="$router.go(-1)">取消</el-button>
	                <el-button 
	                    @click="submit"
	                    type="primary">提交</el-button>
	            </el-form-item>
	        </el-form>
		</div>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

	export default {
		activated: function() {
			this.currentModel = {
				companyFullName: '',
				appName: '',
				appId: '',
				address: '',
				legalPerson: '',
				businessLicence: '',
			}
		},
		data: function() {
			return {
				currentModel: {
					companyFullName: '',
					appName: '',
					appId: '',
					address: '',
					legalPerson: '',
					businessLicence: '',
				},
				rules: {
					companyFullName: {required: true, message: ' ',  trigger: 'blur'},
					appName: {required: true, message: ' ',  trigger: 'blur'},
					appId: {required: true, message: ' ',  trigger: 'blur'},
					address: {required: true, message: ' ',  trigger: 'blur'},
				}
			}
		},
		methods:  {
			submit: function() {
				this.$refs.form.validate(valid => {
                    if (valid) {
                        ajaxPromise({
							url: `/app/create`,
							type: 'post',
							data: this.currentModel
						}).then(data => {
							MessageBox.once('closed', () => location.assign(`${this.ctx}#/financial/customer/financial-app?t=` + new Date().getTime()));
							MessageBox.open('操作成功');
						}).catch(message => {
							MessageBox.open(message);
						});
                    }
                });
			}
		}
	}
</script>