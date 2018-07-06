<style lang="sass">
	
</style>

<template>
	<div class="content">
		<div class="scroller">
			<Breadcrumb :routes="[{ title: '供应商管理'},{ title: '供应商新增'}]">
			</Breadcrumb>
			<el-form 
	            ref="form"
	            :rules="rules" 
	            label-width="120px"
	            :model="currentModel" 
	            class="sdf-form">
	            <el-form-item label="供应商信息" class="form-item-legend"></el-form-item>
	            <el-form-item label="供应商全称" prop="supplierName" required>
	                <el-input class="long" v-model.trim="currentModel.supplierName"></el-input>
	            </el-form-item>
	            <el-form-item label="公司法人" prop="legalPerson">
	                <el-input class="long" v-model.trim="currentModel.legalPerson"></el-input>
	            </el-form-item>
	            <el-form-item label="公司营业执照" prop="businessLicence">
	                <el-input class="long" v-model.trim="currentModel.businessLicence"></el-input>
	            </el-form-item>
	            <el-form-item label="银行信息" prop="bankInfo" required>
	                <template>
	                	<el-button type="text" style="padding: 0 10px; font-weight: normal;" @click="createBank">新增银行卡</el-button>
	                	<el-table 
                            :data="bankCards"
                            class="td-15-padding th-8-15-padding no-th-border"
                            style="width:75%"
                            stripe
                            border>
                            <el-table-column prop="accountNo" label="账户号">
                            </el-table-column>
                            <el-table-column prop="accountName" label="账户名">
                            </el-table-column>
                            <el-table-column prop="bankName" label="开户行">
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="开户行所在地">
                                <div>{{ row.province }} {{ row.city }}</div>
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="操作">
                                <template>
                                    <a href="#" @click.prevent="editBank(row, $index)">编辑</a>
                                    <a href="#" @click.prevent="deleteBank(row, $index)">删除</a>
                                </template>
                            </el-table-column>
                        </el-table>
	                </template>
	            </el-form-item>
	            <el-form-item style="margin-top: 20px;">
	            	<el-button @click="$router.go(-1)">取消</el-button>
	                <el-button 
	                    @click="submit"
	                    type="primary">提交</el-button>
	            </el-form-item>
	        </el-form>
		</div>

		<EditBankModal 
            @submitBank="submitBank"
            :isFromCreate="true"
            v-model="bankModal.show" 
            :model="bankModal.model">
        </EditBankModal>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

	export default {
		components: {
            EditBankModal: require('./include/EditBankModal')
		},
		data: function() {
			return {
				currentModel: {
					supplierName: '',
					legalPerson: '',
					businessLicence: '',
					bankInfo: ''
				},
				rules: {
					supplierName: {required: true, message: ' ', trigger: 'blur'}
				},
				bankModal: {
                    show: false,
                    isUpdate: false,
                    curIndex: -1,
                    model: {}
                },
			}
		},
		activated: function() {
			this.bankCards = [];
			this.currentModel = {
				supplierName: '',
				legalPerson: '',
				businessLicence: '',
				bankInfo: ''
			}
		},
		methods:  {
			createBank: function() {
				var { bankModal } = this;
                bankModal.model = {};
                bankModal.isUpdate = false;
                bankModal.show = true;
			},
			submit: function() {
				this.$refs.form.validate(valid => {
                    if (valid) {
                    	if (this.bankCards.length == 0) {
                    		MessageBox.open('银行信息还未填写！');
                    		return
                    	}
                    	this.currentModel.bankInfo = JSON.stringify(this.bankCards);
                    	
                        ajaxPromise({
                            url: `/supplier/create`,
                            type: 'post',
                            data: this.currentModel
                        }).then(data => {
                        	MessageBox.once('closed', () => location.assign(`${this.ctx}#/financial/customer/supplier?t=` + new Date().getTime()));
                            MessageBox.open('操作成功');
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                });
			},
			submitBank: function(bank) {
            	var { bankModal } = this;
				if (bankModal.isUpdate) {
					this.bankCards[bankModal.curIndex] = Object.assign(this.bankCards[bankModal.curIndex] , bank);
				} else {
	                this.bankCards.push(bank);
				}
            },
            editBank: function(bank, index) {
            	var { bankModal } = this;
                bankModal.model = Object.assign({}, bank);
                bankModal.isUpdate = true;
                bankModal.curIndex = index;
                bankModal.show = true;
            },
            deleteBank: function(bank, index) {
            	this.bankCards.splice(index, 1);
            }
		}
	}
</script>