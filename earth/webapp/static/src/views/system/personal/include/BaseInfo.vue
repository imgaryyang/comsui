<template>
	<div>
		<el-form 
            :model="currentModel" 
            :rules="rules" 
            ref="form"
            label-width="120px" 
            style="margin-top: 30px"
            class="sdf-form sdf-modal-form">
            <el-form-item label="真实姓名" prop="name" required>
                <el-input class="long" v-model.trim="currentModel.name"></el-input>
            </el-form-item>
            <el-form-item label="所属公司" prop="companyName">
                <span>{{currentModel.companyName}}</span>
            </el-form-item>
            <el-form-item label="所属部门" prop="deptName">
                <span>{{currentModel.deptName}}</span>
            </el-form-item>
            <el-form-item label="身份证号" prop="idNumber">
                <el-input class="long" v-model.trim="currentModel.idNumber"></el-input>
            </el-form-item>
            <el-form-item label="工号" prop="jobNumber">
                <el-input class="long" v-model.trim="currentModel.jobNumber"></el-input>
            </el-form-item>
            <el-form-item label="联系电话" prop="phone">
                <el-input class="long" v-model.trim="currentModel.phone"></el-input>
            </el-form-item>
            <el-form-item label="联系邮箱" prop="email">
                <el-input class="long" v-model.trim="currentModel.email"></el-input>
            </el-form-item>
            <el-form-item style="margin-top: 20px;">
                <el-button @click="submit" type="primary">提交</el-button>
            </el-form-item>
        </el-form>
	</div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import { REGEXPS, idCard } from 'src/validators';

	export default {
		props: {
			model: Object
		},
		data: function() {
            var validateIDCard = (rule, value, callback) => {
                if (!value) {
                    callback();
                } else {
                    idCard(value) ? callback() : callback(new Error('请输入合法的身份证号'));
                }
            };

			return {
				currentModel: Object.assign({
                    name: '',
                    phone: '',
                    email: '',
                    companyName: '',
                    deptName: '',
                    jobNumber: '',
                    idNumber: ''
                }, this.model),
                
				rules: {
                    name: { required: true, message: ' ' },
                    email: { type: 'email', message: '请输入合法的邮箱' },
                    phone: { pattern: REGEXPS.MOBILE, message: '请输入合法的手机号' },
                    idNumber: { validator: validateIDCard, trigger: 'blur'}
				}
			}
		}, 
		watch: {
			model: function(current) {
				this.currentModel = Object.assign({
                    name: '',
                    phone: '',
                    email: '',
                    companyName: '',
                    deptName: '',
                    jobNumber: '',
                    idNumber: ''
                }, current);
			}
		},
        deactivated: function() {
            this.$refs.form.resetFields();
        },
		methods: {
			submit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        ajaxPromise({
                            url: '/update-principal',
                            type: 'post',
                            data: this.currentModel
                        }).then(data => {
                            MessageBox.open("提交成功");
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                });
				
			}
		}
	}
</script>