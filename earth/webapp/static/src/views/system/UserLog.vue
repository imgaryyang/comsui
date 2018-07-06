<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <el-select
                            v-model="queryConds.company"
                            placeholder="公司"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in companies"
                                :label="item.fullName"
                                :value="item.fullName">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.logOperateType"
                            placeholder="用户事件"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in logOperateTypes"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <DatePicker
                            v-model="HappenDate"
                            size="small"
                            type="datetimerange"
                            placeholder="发生时间">
                        </DatePicker>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="用户名" value="userName">
                            </el-option>
                            <el-option label="登录IP" value="ip">
                            </el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table
                    class="no-table-bottom-border"
                    v-loading="dataSource.fetching"
                    :data="dataSource.list"
                    stripe>
                    <el-table-column label="发生时间" prop="occurTime" inline-template>
                        <div>{{ row.occurTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
                    </el-table-column>
                    <el-table-column label="所属公司" prop="company"></el-table-column>
                    <el-table-column label="角色" prop="roleName"></el-table-column>
                    <el-table-column label="用户id" prop="userId"></el-table-column>
                    <el-table-column label="用户名" prop="userName"></el-table-column>
                    <el-table-column label="登陆ip" prop="ip"></el-table-column>
                    <el-table-column label="用户事件" prop="logOperateTypeMsg"></el-table-column>
                    <el-table-column label="事件内容" prop="recordContent"></el-table-column>
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
    import DatePicker from 'components/DatePicker';
    import format from 'filters/format';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox'),
            DatePicker,
        },
        data: function() {
            return {
                action: `/logs/user-login-log/query`,
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    company: '',
                    logOperateType: '',
                    startOccurTime: '',
                    endOccurTime: '',
                },
                comboConds: {
                    userName: '',
                    ip: '',
                },

                HappenDate: [],
                logOperateTypes: [],
                companies: [],
            };
        },
        watch: {
            HappenDate: function(current){
                this.queryConds.startOccurTime = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.endOccurTime = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            },
        },
        methods: {
            initialize: function() {
                return ajaxPromise({
                    url: `/logs/user-login-log/options`
                }).then(data => {
                    this.companies = data.companies || [];
                    this.logOperateTypes = data.logOperateTypes || [];
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
        }
    }
</script> 