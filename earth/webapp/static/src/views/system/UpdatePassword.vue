<template>
    <div class="content">
        <div class="scroller">
            <Breadcrumb :routes="[
                { title: '修改密码' }
            ]"></Breadcrumb>
            <el-form
                :model="model"
                :rules="rules"
                ref="form"
                label-width="120px"
                style="margin-top: 30px"
                class="sdf-form">
                <el-form-item label="修改密码" class="form-item-legend"></el-form-item>
                <el-form-item label="账户">
                    {{ userName }}
                </el-form-item>
                <el-form-item label="当前登陆密码" prop="oldPassword" required>
                    <el-input type="password" class="middle" v-model="model.oldPassword"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword" required>
                    <el-input type="password" class="middle" v-model="model.newPassword"></el-input>
                </el-form-item>
                <el-form-item label="确认新密码" prop="newPasswordAgain" required>
                    <el-input type="password" class="middle" v-model="model.newPasswordAgain"></el-input>
                </el-form-item>
                <el-form-item style="margin-top: 20px;">
                    <el-button @click="submit" type="primary">确认修改</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>
<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import md5 from 'assets/javascripts/md5';
    import MessageBox from 'components/MessageBox';
    import { REGEXPS } from 'src/validators';
    import { mapState } from 'vuex';
    export default {
        data: function() {
            var validateNewPasswordSame = (rule, value, callback) => {
                const { newPassword, newPasswordAgain } = this.model;
                if (newPassword !== newPasswordAgain) {
                    callback(new Error('两次密码不一致'));
                } else {
                    callback();
                }
            };
            return {
                model: {
                    newPassword: '',
                    newPasswordAgain: '',
                    oldPassword: ''
                },
                rules: {
                    oldPassword: [
                        { required: true, message: ' '},
                    ],
                    newPassword: [
                        { required: true, message: ' '},
                        // { validator: validateNewPasswordSame }
                    ],
                    newPasswordAgain: [
                        { required: true, message: ' '},
                        { validator: validateNewPasswordSame }
                    ],
                }
            }
        },
        computed: {
            ...mapState({
                userName: state => state.principal.username,
                modifyPasswordTimes: state => state.principal.modifyPasswordTimes
            })
        },
        deactivated: function() {
            this.$refs.form.resetFields();
        },
        activated: function() {
            if (this.modifyPasswordTimes == 0) {
                MessageBox.open('首次登陆，请修改密码！');
            }
        },
        methods: {
            submit: function() {
                var { $refs } = this;
                $refs.form.validate(valid => {
                    if (valid) {
                        ajaxPromise({
                            url: `/update-password`,
                            type: 'post',
                            data: {
                                userName: this.userName,
                                newPassword: md5(this.model.newPassword),
                                oldPassword: md5(this.model.oldPassword),
                            }
                        }).then(data => {
                            MessageBox.open('修改成功，正在重新登录');
                            setTimeout(() => {
                                location.assign('${this.ctx_deprecated}/j_spring_security_logout');
                            }, 1000);
                        })
                        .catch(message => {
                            MessageBox.open(message);
                        });
                    }
                });
            }
        }
    }
</script>