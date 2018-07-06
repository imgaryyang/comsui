<style lang="sass">
    #editUserRole {
        .el-checkbox {
            margin-left: 0;
            margin-right: 10px;
        }
    }
</style>

<template>
    <Modal v-model="visible" id="editUserRole">
        <ModalHeader title="编辑基础信息"></ModalHeader>
        <ModalBody align="left">
            <el-form
            :model="model"
            ref="form"
            label-width="100px"
            class="sdf-form sdf-modal-form">
                <el-form-item label="用户角色" required>
                    <el-checkbox 
                        v-for="(item, key) in model.auth"
                        v-model="item.checked"
                        :label="key"></el-checkbox>
                    <!-- <el-checkbox-group v-model="model.roleIds">
                    </el-checkbox-group> -->
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
            principalId: [String, Number],
            roleList: [Array, Object]
        },
        data: function() {
            return {
                visible: this.value,
                model: {
                    auth: {}
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
                var auth = this.model.auth;
                var roleIds = Object.keys(auth)
                    .filter(key => auth[key].checked)
                    .map(key => auth[key].id);
                
                ajaxPromise({
                    url: `/link-user-role`,
                    data: {
                        roleIds: roleIds.join(','),
                        principalId: this.principalId
                    }
                }).then(data => {
                    this.visible = false;
                    setTimeout(() => this.$emit('submit'), 400);
                }).catch(message => {
                    MessageBox.open(message);
                });
            }
        }
    }
</script>
