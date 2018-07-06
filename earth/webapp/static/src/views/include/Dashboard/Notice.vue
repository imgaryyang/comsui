<style lang="sass">
    // 不依赖旧的dashboard.scss
	.databoard-notice {
        .fot {
            position: absolute;
            bottom: 15px;
            left: 0;
            right: 0;
            margin: auto;
            text-align: center;
        }

        .notice {
            margin-top: 10px;

            .item {
                padding: 10px 15px;
                border: 1px solid #d8d8d8;
                border-top: none;
                font-size: 12px;

                &:nth-child(2n) {
                    background: #f9f9f9;
                }

                &:nth-child(2n + 1) {
                    background: #fff;
                }

                &:hover {
                    background-color: #fffcc7;
                }

                &:first-child {
                    border-top: 1px solid #d8d8d8;
                }
            }
        }
    }
</style>

<template>
	<div v-if="active" 
        class="dashboard databoard-notice item-notice">
        <div class="inner">
            <div class="hd">
                <p class="name"><strong>公告栏</strong></p>
            </div>
            <div class="bd">
                <div class="notice" v-loading="dataSource.fetching">
                    <div class="text-align-center" v-if="dataSource.error">
                        {{ dataSource.error }}
                    </div>
                    <template v-else>
                        <div
                            v-for="item in dataSource.list" class="item">
                            <a href="#" class='notice-item' @click.prevent="showNoticeDetail(item)">
                                <p>{{ item.title | unescape }}</p>
                                <div class='color-dim'>{{ item.releaseTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                            </a>
                        </div>
                    </template>
                </div>                
            </div>
            <div class="fot">
                <PageControl 
                    float="normal"
                    :enable-redirect="false"
                    :enable-head-tail="false"
                    v-model="pageConds.pageIndex"
                    :size="dataSource.size"
                    :per-page-record-number="pageConds.perPageRecordNumber">
                </PageControl>
            </div>
        </div>  
    </div>
</template>

<script>
    import { ajaxPromise, searchify, purify } from 'assets/javascripts/util';
    import Pagination from 'mixins/Pagination';

	export default {
        mixins: [Pagination],
		props: {
            id: String,
            data: Object,
            visible: Boolean
		},
        computed: {
            active: function() {
                return this.$parent.selected == this.id;
            },
            conditions: function() {
                return Object.assign({}, this.pageConds);
            }
        },
        watch: {
            active: function(current) {
                if (current) {
                    this.fetch();
                }
            }
        },
        data: function() {
            return {
                action: `/notice/notice-released-page-list`,
                autoload: false,
                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 7
                }
            }
        },
        filters: {
            unescape: function(value) {
                return unescape(value);
            }
        },
        methods: {
            showNoticeDetail: function(item) {
                this.$parent.open('item-notice-detail', item);  
            },
            onError: function(message) {
                this.dataSource.error = message.toString();
            }
        }
	}
</script>