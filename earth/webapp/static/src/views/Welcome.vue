<style lang="sass">
    @import '~assets/stylesheets/base';

    .el-notification {
        padding: 10px;

        .el-notification__group p {
            margin: 0 0;
            color: #999;
        }
    }
    .welcome {
        .web-g-main{
            z-index: 8; 
        }

        .background {
            height: 100%;
            position: relative;
            padding-right: $width-dashboard;

            .inner {
                height: 100%;
                background: url(~assets/images/bg-welcome.jpg) no-repeat left center;
                background-size: cover;
                position: relative;
                color: #fff;
                text-align: center;
            }

            .slogan {
                height: 100%;
                overflow: hidden;

                .bd {
                    margin-top: -180px;
                }

                .bd,
                &:before {
                    display: inline-block;
                    vertical-align: middle;
                }

                &:before {
                    height: 100%;
                    content: '';
                }

                .title {
                    font-size: 72px;
                    font-style: italic;
                    font-weight: normal;
                }

                .subtitle {
                    font-weight: normal;  
                    margin-top: 0px; 
                }
            }

            .copyright {
                position: absolute;
                bottom: 40px;
                left: 0;
                right: 0;
            }
        }

    }
    .welcome-notice {
        position: absolute;
        width: 100%;
        z-index: 1;

        .notice-content {
            background: rgba(0,0,0,0.4);
            padding: 5px 0 5px 70px;
            line-height: 20px;
            margin-right: 370px;
        }
        .icon-notice {
            position: absolute;
        }
        .notice-block {
            display: inline-block;
            height: 20px;
            padding-left: 20px;
           
            ul {
                padding: 0;
                margin: 0;
                position: absolute;
                top: 0;
                list-style: none;
                @include transition(top 0.4s);

                li {
                    height: 20px;

                    a{
                        color: #fff;
                        font-size: 12px;
                        cursor:pointer;
                    }
                }
            }
        }
    }
</style>
<template>
    <div class="welcome">
        <SHeader></SHeader>
        <div class="web-g-main">
            <div class="background">
                <div v-if="noticeList.length > 0" class="welcome-notice">
                    <div class="notice-content">
                        <i class="icon icon-notice"></i>
                        <div class="notice-block">
                            <ul class="notice-list" 
                                ref="notice"
                                :style="{
                                    top: `${-20 * index}px`
                                }">
                                <li v-for="item in noticeList">
                                    <a 
                                        @mouseenter="handleMouseEnter" 
                                        @mouseleave="handleMouseLeave"
                                        @click.prevent="handleClickNotice(item)" 
                                        herf="#">
                                        {{ item.title | unescape }}
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="inner">
                    <div class="slogan">
                        <div class="bd">
                            <h1 class="title">Welcome</h1>
                            <h3 class="subtitle">五维金融管理平台</h3>
                        </div>
                    </div>
                    <div class="copyright">
                        © 2015 杭州随地付网络技术有限公司 版权所有
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import Dashboard from './include/Dashboard';
    import { Notification } from 'element-ui';
    import MessageBox from 'components/MessageBox';

    export default {
        components: {
            SHeader: require('views/include/Header'),
        },
        data: function() {
            return {
                noticeList: [],
                index: 0,
            };
        },
        filters: {
            unescape: function(value) {
                return unescape(value);
            }
        },
        mounted: function() {
            Dashboard.open('item-todo');
        },
        activated: function() {
            Dashboard.open();
            this.fetchNotice();
            this.run();
            this.queryZHReportJobs(this.production === 'avictc');
        },
        deactivated: function() {
            this.cancel();
            Dashboard.close();
        },
        methods: {
            run: function() {
                var self = this;
                this.timer = setInterval(() => {
                    self.index = (self.index + 1) % self.noticeList.length;
                }, 2500);
            },
            cancel: function() {
                clearTimeout(this.timer);
                this.timer = null
            },
            fetchNotice: function() {
                ajaxPromise({
                    url: `/notice/notice-released-list`
                }).then(data => {
                    this.noticeList = data.list.slice(0, 3);
                }).catch(message => {
                    MessageBox.open(message );
                });
            },
            handleClickNotice: function(options) {
                Dashboard.open('item-notice-detail', options);
            },
            handleMouseEnter: function() {
                this.cancel();
            },
            handleMouseLeave: function() {
                this.run();
            },
            queryZHReportJobs: function(query) {
                if (!query) return;

                ajaxPromise({
                    url: '/zh/report/jobs/views/create'
                }).then(data => {
                    if (data.totalOfCreate == undefined || data.totalOfCreate == 0) return;

                    Notification({
                        message:  `${data.totalOfCreate}条报表任务已创建`,
                        offset: window.screen.availHeight - 200,
                        duration: 0
                    });

                }).catch(message => {
                    console.log(message);
                });
            }
        }
    }
</script>