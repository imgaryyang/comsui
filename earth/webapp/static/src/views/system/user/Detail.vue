<style lang="sass">
    #userDetail {
        .scroller .el-form-item:not(.form-item-legend) {
            padding-top: 5px;
            padding-bottom: 5px;
        }

        .role-item {
            margin-right: 10px;
        }
    }
</style>

<template>
    <div class="content" id="userDetail">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[
                { title: '用户管理' },
                { title: '用户详情' }
            ]"></Breadcrumb>

            <el-form
                label-width="120px"
                style="margin: 30px 14% 30px 7%;"
                class="sdf-form">
                <div>
                    <el-form-item label="用户信息" class="form-item-legend">
                        <el-button
                            class="pull-right"
                            style="margin-top: 4px;"
                            size="small"
                            type="primary"
                            @click="secretKeyModal.show = true">添加密钥</el-button>
                    </el-form-item>
                    <el-form-item label="用户名"><span class="pull-left">{{ principal.name }}</span><span class="icon icon-key pull-left" style="margin: 5px 5px 0px 10px"></span>{{count}}</el-form-item>
                </div>

                <div>
                    <el-form-item label="基础信息" class="form-item-legend">
                        <div>
                            <el-button 
                                class="pull-right" 
                                style="margin-top: 4px;" 
                                size="small" 
                                type="primary"
                                @click="handleEditBasicInfo">编辑</el-button>
                        </div>
                    </el-form-item>
                    <el-form-item label="真实名字">{{ principal.realName }}</el-form-item>
                    <el-form-item label="所属公司">{{ principal.companyName }}</el-form-item>
                    <el-form-item label="所属部门">{{ principal.deptName }}</el-form-item>
                    <el-form-item label="身份证号">{{ principal.idNumber }}</el-form-item>
                    <el-form-item label="工号">{{ principal.jobNumber }}</el-form-item>
                    <el-form-item label="联系电话">{{ principal.phone }}</el-form-item>
                    <el-form-item label="联系邮箱">{{ principal.email }}</el-form-item>
                    <el-form-item label="备注" prop="remark">{{ principal.remark }}</el-form-item>
                </div>

                <div>
                    <el-form-item label="权限信息" class="form-item-legend"></el-form-item>
                    <el-form-item label="用户角色">
                        <el-row>
                            <el-col :span="20" style="min-height: 1px;line-height: 1.414; word-break: break-all; margin-top: 9px;">
                                <!-- <template v-for="(item, key) in auth">
                                    <span 
                                        class="role-item" 
                                        v-if="item.checked"
                                        v-text="key"></span>
                                </template> -->
                                <span>{{ principal.authority }}</span>
                            </el-col>
                            <el-col :span="4">
                                <el-button 
                                    class="pull-right" 
                                    style="margin-top: 4px;" 
                                    size="small" 
                                    type="primary"
                                    @click="handleEditUserRole">变更</el-button>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <el-form-item label="业务权限">
                        <div style="margin-bottom: 10px; overflow: hidden;">
                            <div class="pull-left">
                                <el-checkbox v-model="bindAll" style="margin-right: 20px;">全选</el-checkbox>
                                <HelpPopover content="勾选全选，拥有所有信托合同的业务权限，当有新项目时，自动纳入当前账号的管理权限中。"/>
                            </div>
                            <div class="pull-right" style="margin-top: 4px;">
                                <el-button size="small" type="primary" @click="editBusinessModal.visible = true">新增绑定</el-button>
                            </div>
                        </div>
                        <div class="row-layout-detail" style="padding: 0;">
                            <Business :principalId="$route.params.id" ref="business"></Business>
                        </div>
                    </el-form-item>
                </div>
            </el-form>
        </div>

        <EditUserRoleModal
            @submit="fetch"
            :principalId="$route.params.id"
            v-model="editUserRoleModal.visible"
            :roleList="auth"
            :initial-model="editUserRoleModal.model"></EditUserRoleModal>

        <EditBasicInfoModal
            @submit="fetch"
            :principalId="$route.params.id"
            v-model="editBasicInfoModal.visible"
            :initial-model="editBasicInfoModal.model"></EditBasicInfoModal>

        <EditBusinessModal
            :principalId="$route.params.id"
            :appList="appList"
            :financialContractTypeList="financialContractTypeList"
            @closed="$refs.business.fetch()"
            v-model="editBusinessModal.visible"></EditBusinessModal>

        <SetSecretKeyModal
            :principalId="$route.params.id"
            @showHistoryModal="showHistoryModal(arguments[0])"
            v-model="secretKeyModal.show">
        </SetSecretKeyModal>

        <HistoryModal v-model="historyModal.show" :uuid="historyModal.uuid">
        </HistoryModal>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        components: {
            EditBasicInfoModal: require('./include/EditBasicInfoModal'),
            EditUserRoleModal: require('./include/EditUserRoleModal'),
            EditBusinessModal: require('./include/EditBusinessModal'),
            SetSecretKeyModal: require('./include/SetSecretKeyModal'),
            Business: require('./include/Business'),
            HelpPopover: require('views/include/HelpPopover'),
            HistoryModal: require('views/system/personal/include/HistoryModal')
        },
        data: function() {
            return {
                fetching: false,
                editUserRoleModal: {
                    visible: false,
                    model: {}
                },
                editBasicInfoModal: {
                    visible: false,
                    model: {}
                },
                editBusinessModal: {
                    visible: false
                },
                principal: {
                    authority: '',
                    name: '',
                    realName: '',
                    email: '',
                    phone: '',
                    remark: '',
                    groupName: '',
                    companyName: '',
                    companyId: '',
                    groupId: '',
                    bindAll: ''
                },
                auth: {},
                groupList: [],
                companyList: [],
                appList: [],
                financialContractTypeList: [],

                bindAll: false,

                secretKeyModal: {
                    show: false,
                },

                count: 0,//密钥数

                historyModal: {
                    show: false,
                    uuid: ''
                },
            }
        },
        activated: function() {
            this.fetch(); 
        },
        watch: {
            'principal.bindAll': function(current) {
                this.bindAll = current;
            },
            bindAll: function(current) {
                if(current) {
                    ajaxPromise({
                        url: `/bind-financial-contract/bindAll`,
                        data: {
                            principalId: this.$route.params.id
                        },
                        type: 'post'
                    }).then(data => {
                        this.$refs.business.fetch();
                    }).catch(message => {
                        MessageBox.open(message);
                    })
                } else {
                    ajaxPromise({
                        url: `bind-financial-contract/unbindAll`,
                        data: {
                            principalId: this.$route.params.id
                        },
                        type: 'post'
                    }).then(data => {
                        this.$refs.business.fetch();
                    }).catch(message => {
                        MessageBox.open(message)
                    })
                }
            },
            'secretKeyModal.show': function(current) {
                this.fetch();
            }
        },
        methods: {
            fetch: function() {
                this.fetching = true;

                ajaxPromise({
                    url: `/show-user-role/${this.$route.params.id}`
                }).then(data => {
                    this.principal = Object.assign({}, this.principal, data.principal);
                    this.groupList = data.groupList || [];
                    this.companyList = data.companyList || [];
                    this.auth = data.auth;
                    this.appList = data.appList || [];
                    this.financialContractTypeList = data.financialContractTypeList || [];
                    this.count = data.count || 0;
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                })
            },
            handleEditBasicInfo: function() {
                var { name, realName, groupName, companyName, ...obj } = this.principal;
                obj.username = name;
                obj.realname = realName;
                this.editBasicInfoModal.model = obj;
                this.editBasicInfoModal.visible = true;
            },
            handleEditUserRole: function() {
                this.editUserRoleModal.model = { auth: JSON.parse(JSON.stringify(this.auth)) };
                this.editUserRoleModal.visible = true;
            },
            handleUnBind: function(id) {
                this.unBind([id])
                    .then(() => {

                    })
                    .catch(message => {
                        MessageBox.open(message);
                    });
            },
            handleUnBindBatch: function() {
                this.unBind([])
                    .then(() => {

                    })
                    .catch(message => {
                        MessageBox.open(message);
                    });
            },
            unBind: function(ids) {
                return ajaxPromise({
                    url: `/bind-financial-contract/unbind`,
                    type: 'post',
                    data: {
                        principalId: this.$route.params.id,
                        financialContractIds: JSON.stringify(ids)
                    }
                });
            },
            showHistoryModal: function(uuid) {
                var { historyModal } = this;
                historyModal.show = true;
                historyModal.uuid = uuid;
            }
        }

    }
</script>