<template>
    <div class="content">
        <div class="scroller">
            <Breadcrumb :routes="[
                { title: '用户管理' },
                { title: $route.params.id ? '编辑用户' : '新建用户' }
            ]"></Breadcrumb>

            <el-form
                :model="model"
                :rules="rules"
                ref="form"
                label-width="120px"
                style="margin-top: 30px"
                class="sdf-form">
                <div>
                    <el-form-item label="用户信息" class="form-item-legend"></el-form-item>
                    <el-form-item label="用户名" prop="username" required>
                        <el-input class="middle" v-model="model.username"></el-input>
                    </el-form-item>
                    <div style="font-size: 12px; color: #66512c; margin-left: 67px;margin-top:5px">
                        <i class="glyphicon glyphicon-info-sign"></i>
                        <span class="alert-warning">长度为6-16位、英文字母开头、可选择英文字母数字以及下划线组合</span>
                    </div>
                    <br>
                </div>

                <div>
                    <el-form-item label="基础信息" class="form-item-legend"></el-form-item>
                    <el-form-item label="真实名字" prop="realname" required>
                        <el-input class="middle" v-model="model.realname"></el-input>
                    </el-form-item>
                    <el-form-item label="所属公司" prop="companyId" required>
                        <CompanyDropdown
                            ref="companyDropdown"
                            v-model="model.companyId">
                        </CompanyDropdown>
                    </el-form-item>
                    <el-form-item label="所属分组" prop="groupId" required>
                        <GroupDropdown
                            ref="groupDropdown"
                            v-model="model.groupId">
                        </GroupDropdown>
                    </el-form-item>
                    <el-form-item label="身份证号" prop="idNumber">
                        <el-input class="middle" v-model="model.idNumber"></el-input>
                    </el-form-item>
                    <el-form-item label="工号" prop="jobNumber">
                        <el-input class="middle" v-model="model.jobNumber"></el-input>
                    </el-form-item>
                    <el-form-item label="联系电话" prop="phone">
                        <el-input class="middle" v-model="model.phone"></el-input>
                    </el-form-item>
                    <el-form-item label="联系邮箱" prop="email">
                        <el-input class="middle" v-model="model.email"></el-input>
                    </el-form-item>
                    <el-form-item label="备注" prop="remark">
                        <el-input class="long" type="textarea" v-model="model.remark"></el-input>
                    </el-form-item>
                </div>

                <el-form-item style="margin-top: 20px;">
                    <el-button @click="submit" type="primary">提交</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import { REGEXPS } from 'src/validators';

    export default {
        components: {
            CompanyDropdown: require('./include/CompanyDropdown'),
            GroupDropdown: require('./include/GroupDropdown')
        },
        data: function() {
            var validateUserName = (rule, value, callback) => {
                if(value === ''){
                    callback(new Error(' '));
                }else {
                    ajaxPromise({
                        url: `create-user-role/username`,
                        data: {
                            username: value
                        }
                    }).then(data => {
                        callback();
                    }).catch(message => {
                        callback(new Error(message));
                    });
                }
            };
            return {
                model: {
                    username: '',
                    realname: '',
                    email: '',
                    phone: '',
                    companyId: '',
                    groupId: '',
                    remark: '',
                    idNumber: '',
                    jobNumber: '',
                },
                rules: {
                    username: {  required: true, validator: validateUserName, trigger: 'blur'},
                    realname: { required: true, message: ' '},
                    companyId: { required: true, message: ' '},
                    groupId: { required: true, message: ' '},
                    email: { type: 'email', message: '请输入合法的邮箱' },
                    phone: { pattern: REGEXPS.MOBILE, message: '请输入合法的手机号' },
                }
            }
        },
        deactivated: function() {
            this.$refs.form.resetFields();
        },
        methods: {
            submit: function() {
                var { $refs } = this;
                $refs.form.validate(valid => {
                    $refs.companyDropdown._data.valid = valid;
                    $refs.groupDropdown._data.valid = valid;
                    if (valid) {
                        ajaxPromise({
                            url: `create-user-role`,
                            type: 'post',
                            data: this.model
                        }).then(data => {
                            MessageBox.open('用户创建成功，初始密码为：' + data.pwd, '提示', [{
                                text: '确定',
                                type: 'success',
                                handler: () => {
                                    location.assign(`${this.ctx}#/system/user/${data.newprincipal.id}`);
                                    MessageBox.close();
                                }
                            }]);
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    } else {
                        $('.el-form-item__error').first().parent()[0].scrollIntoView();
                    }
                });
            }
        }
    }
</script>
