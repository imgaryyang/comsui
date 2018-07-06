<style lang="sass">
    .notice-content {
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        max-height: 35px;
    }
</style>
<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <el-button size="small" type="primary" @click="addNotice.show = true" v-if="ifElementGranted('create-system-notice')">新建公告</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table 
                    class="no-table-bottom-border"
                    stripe
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column inline-template label="标题" prop="title">
                        <div>{{ row.title | unescape }}</div>
                    </el-table-column>
                    <el-table-column label="内容" inline-template>
                        <span class="notice-content">{{ row.content | unescape }}</span>
                    </el-table-column>
                    <el-table-column label="状态变更时间" inline-template>
                        <div>{{ row.statusChangeTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="状态" inline-template>
                        <div>
                            <span v-if="row.noticeStatus == 'INVALID'" class="color-danger">已作废</span>
                            <span v-else-if="row.noticeStatus == 'UNRELEASED'" class="color-warning">未发布</span>
                            <span v-else-if="row.noticeStatus == 'RELEASED'">已发布</span>
                        </div>
                    </el-table-column>
                    <el-table-column label="操作" inline-template>
                        <div>
                            <a v-if="row.noticeStatus == 'INVALID'" @click="EditNotice(row)" href="javascript:void(0)">查看</a>
                            <template v-else-if="row.noticeStatus == 'UNRELEASED'">
                                <a href="javascript:void(0)" @click.prevent="EditNotice(row)" >编辑</a>
                                <a href="javascript:void(0)" @click.prevent="InvalidNotice(row)" v-if="ifElementGranted('disable-system-notice')">作废</a>
                            </template>
                            <template v-else-if="row.noticeStatus == 'RELEASED'">
                                <a href="javascript:void(0)" @click.prevent="EditNotice(row)">查看</a>
                                <a href="javascript:void(0)" @click.prevent="InvalidNotice(row)" v-if="ifElementGranted('disable-system-notice')">作废</a>
                            </template>
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
        <AddNotice
            v-model="addNotice.show"
            @submit="fetchNotice">
        </AddNotice>
        <EditNotice
            v-model="editNotice.show"
            :model="editNotice.model"
            @submit="fetchNotice">
        </EditNotice>
        <RevokeNotice
            v-model="revokeNotice.show"
            :noticeUuid="revokeNotice.noticeUuid"
            @submit="fetchNotice"></RevokeNotice>
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
            AddNotice: require('./include/AddNotice'),
            EditNotice: require('./include/EditNotice'),
            RevokeNotice: require('./include/RevokeNotice'),
        },
        data: function() {
            return {
                action: `/notice/notice-list`,
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                addNotice: {
                    show: false
                },
                editNotice: {
                    show: false,
                    model: {}
                },
                revokeNotice: {
                    show: false,
                    noticeUuid:''
                }
            };
        },
        filters: {
            unescape: function(value) {
                return unescape(value);
            }
        },
        methods: {
            fetchNotice: function() {
                this.fetch();
            },
            EditNotice: function(data) {
                this.editNotice.show = true;
                this.editNotice.model = data;
            },
            InvalidNotice: function(data) {
                this.revokeNotice.show = true;
                this.revokeNotice.noticeUuid = data.noticeUuid;
            }
        }
    }
</script>