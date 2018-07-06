<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <el-input
                            size="small" 
                            :value="queryConds.id"
                            @change.native="queryConds.id = $event.target.value.trim()"
                            placeholder="角色ID">
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input
                            size="small" 
                            :value="queryConds.roleName"
                            @change.native="queryConds.roleName = $event.target.value.trim()"
                            placeholder="角色名称">
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" ref="lookup" type="primary">查询</el-button>
                    </el-form-item>
                    <el-form-item v-if="ifElementGranted('create-system-role')">
                        <a  class="el-button el-button--success el-button--small" 
                            :href="`${ctx}#/system/role/create`">新增角色</a>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table 
                    class="no-table-bottom-border"
                    stripe
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column label="角色ID" prop="id"></el-table-column>
                    <el-table-column label="角色名称" prop="roleName"></el-table-column>
                    <el-table-column label="当前用户" prop="userCount"></el-table-column>
                    <el-table-column label="建立时间" inline-template>
                        <div>{{row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="备注" prop="roleRemark"></el-table-column>
                    <el-table-column label="状态" prop="roleState" inline-template>
                        <span :class="{
                            'color-danger': row.roleState == '停用'
                        }">{{ row.roleState }}</span>
                    </el-table-column>
                    <el-table-column label="操作" inline-template>
                        <div>
                            <a :href="`${ctx}#/system/role/${row.id}/edit`" v-if="ifElementGranted('modify-system-role')">配置</a>
                            <a href="javascript: void 0;" @click="deleteRole(row.id)" v-if="ifElementGranted('delete-system-role')">删除</a>
                        </div>
                    </el-table-column>
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
        data: function() {
            return {
                action: `/show-systemrole/query`,
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    roleName: '',
                    id: ''
                }
            };
        },
        methods: {
            deleteRole: function(id) {
                MessageBox.open(`删除操作后，当前选定该角色的用户权限将会受到影响，是否确认删除？`, null, [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '确认',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: `/del-systemrole`,
                            data: { id }
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