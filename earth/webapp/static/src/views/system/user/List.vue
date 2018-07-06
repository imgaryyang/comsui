<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <el-select 
                            v-model="queryConds.companyId" 
                            placeholder="所属公司" 
                            clearable
                            size="small">
                            <el-option
                                v-for="item in companies"
                                :label="item.fullName"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select 
                            v-model="queryConds.groupId" 
                            placeholder="用户分组" 
                            clearable
                            size="small">
                            <el-option
                                v-for="item in groupList"
                                :label="item.groupName"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select 
                            v-model="queryConds.roleId" 
                            placeholder="用户角色" 
                            clearable
                            size="small">
                            <el-option
                                v-for="item in roleList"
                                :label="item.roleName"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="用户id" value="id"></el-option>
                            <el-option label="账户名" value="accountName"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" ref="lookup" type="primary">查询</el-button>
                    </el-form-item>
                    <el-form-item>
                        <a  
                            class="el-button el-button--success el-button--small" 
                            v-if="ifElementGranted('create-system-user')"
                            :href="`${ctx}#/system/user/create`">新增用户</a>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table 
                    class="no-table-bottom-border"
                    stripe
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column label="用户ID" prop="id"></el-table-column>
                    <el-table-column label="用户角色" prop="role"></el-table-column>
                    <el-table-column label="账户名" prop="accountName"></el-table-column>
                    <el-table-column label="持有者" prop="userName"></el-table-column>
                    <el-table-column label="工号" prop="jobNumber"></el-table-column>
                    <el-table-column label="身份证号" prop="idNumber"></el-table-column>
                    <el-table-column label="联系邮箱" prop="email"></el-table-column>
                    <el-table-column label="联系号码" prop="number"></el-table-column>
                    <el-table-column label="所属公司" prop="company"></el-table-column>
                    <el-table-column label="所属部门" prop="deptName"></el-table-column>
                    <el-table-column label="状态" inline-template>
                        <span :class="{
                            'color-danger': row.status == '冻结'
                            }">{{ row.status }}</span>
                    </el-table-column>
                    <el-table-column label="操作" inline-template>
                        <div>
                            <a :href="`${ctx}#/system/user/${ row.id }`" v-if="ifElementGranted('modify-system-user')">详情</a>
                            <a 
                                v-if="ifElementGranted('close-system-user') && row.status != '冻结'" 
                                href="javascript: void 0;"
                                @click="closeUser(row.id)">
                                关闭
                            </a>
                        </div>
                    </el-table-column>
                    <el-table-column label="密钥" prop="count"></el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <div class="pull-right">
                <PageControl 
                    v-model="pageConds.pageIndex"
                    :size="dataSource.size"
                    :per-page-record-number="pageConds.perPageRecordNumber">
                </PageControl>
            </div>
        </div>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import MessageBox from 'components/MessageBox';
    import { ajaxPromise } from 'assets/javascripts/util';

    export default {
        mixins: [Pagination, ListPage],
        components: { 
            ComboQueryBox: require('views/include/ComboQueryBox'),
        },
        data: function() {
            return {
                action: `/show-user-list/query`,
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                comboConds: {
                    id: '',
                    accountName: ''
                },
                queryConds: {
                    companyId: '',
                    groupId: '',
                    roleId: ''
                },
                companies: [],
                groupList: [],
                roleList: []
            };
        },
        methods: {
            initialize: function() {
                return ajaxPromise({
                    url: `/show-user-list/options`,
                }).then(data => {
                    this.companies = data.companies;
                    this.groupList = data.groupList;
                    this.roleList = data.roleList;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            closeUser: function(id) {
                MessageBox.open(`确认关闭此用户？`, null, [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '确认',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: `/delete-user-role/${id}`
                        }).then(data => {
                            MessageBox.close();
                            this.fetch();
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                }]);
            }
        }
    }
</script>