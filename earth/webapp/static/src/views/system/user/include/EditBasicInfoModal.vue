<style lang="sass">

</style>

<template>
    <Modal v-model="visible">
        <ModalHeader title="编辑基础信息"></ModalHeader>
        <ModalBody align="left">
            <el-form
            :model="model"
            :rules="rules"
            ref="form"
            label-width="100px"
            class="sdf-form sdf-modal-form">
                <el-form-item label="真实名字" prop="realname" required>
                    <el-input class="middle" v-model="model.realname"></el-input>
                </el-form-item>
                <el-form-item label="联系邮箱" prop="email">
                    <el-input class="middle" v-model="model.email"></el-input>
                </el-form-item>
                <el-form-item label="联系电话" prop="phone">
                    <el-input class="middle" v-model="model.phone"></el-input>
                </el-form-item>
                <el-form-item label="所属公司" prop="companyId" required>
                    <CompanyDropdown v-model="model.companyId"></CompanyDropdown>
                </el-form-item>
                <el-form-item label="所属部门" prop="groupId" required>
                    <GroupDropdown v-model="model.groupId"></GroupDropdown>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input class="long" type="textarea" v-model="model.remark"></el-input>
                </el-form-item>
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="visible = false">取消</el-button>
            <el-button @click="submit" type="success">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import { REGEXPS } from 'src/validators';

    export default {
        components: {
            CompanyDropdown: require('./CompanyDropdown'),
            GroupDropdown: require('./GroupDropdown')
        },
        props: {
            value: Boolean,
            initialModel: Object,
            principalId: [String, Number]
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
                visible: this.value,
                model: {
                    username: '',
                    realname: '',
                    email: '',
                    phone: '',
                    companyId: '',
                    groupId: '',
                    remark: '',
                },
                rules: {
                    username: { validator: validateUserName, trigger: 'blur'},
                    realname: { required: true, message: ' '},
                    companyId: { required: true, message: ' '},
                    groupId: { required: true, message: ' '},
                    email: { type: 'email', message: '请输入合法的邮箱' },
                    phone: { pattern: REGEXPS.MOBILE, message: '请输入合法的手机号' },
                }
            }
        },
        watch: {
            value: function(current) {
                this.visible = current;
            },
            visible: function(current) {
                this.$emit('input', current);
            },
            initialModel: function(current) {
                this.model = Object.assign({}, this.model, current);
            }
        },
        methods: {
            submit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        ajaxPromise({
                            url: `/edit-user-role`,
                            type: 'post',
                            data: {
                                ...this.model,
                                principalId: this.principalId
                            }
                        }).then(data => {
                            this.visible = false;
                            setTimeout(() => this.$emit('submit'), 400);
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                });
            }
        }
    }
</script>
