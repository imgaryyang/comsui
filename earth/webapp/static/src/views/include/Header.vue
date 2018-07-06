<style lang="sass">
    .web-g-hd .profile .dropdown-menu li.item-faq a:before{
        background-image: url("~assets/images/faq-image/help.png");
        background-position: center; 
        background-position: -6px -5px;
    }
    .web-g-hd .profile .dropdown-menu li.item-faq a:hover:before{
        background-image: url("~assets/images/faq-image/help2.png"); 
    }
</style>

<template>
    <div class="web-g-hd">
        <div class="bd">
            <div class="logo pull-left">
                <i class="icon"></i>
            </div>

            <button type="button" class="hd-collapsed">
                <span class="glyphicon glyphicon-menu-hamburger"></span>
            </button>
            <div class="nav pull-left">
                <ul class="menu">
                    <li 
                        v-for="menu in level1Menus"
                        class='menu-item'
                        :class="{
                            [menu.mkey]: true,
                            'z-active': menu.mkey === level1ActiveMenu.mkey
                        }">
                        <a @click="onClickMenuLink" :href="`${ctx_deprecated}/${menu.url}`">{{ menu.name }}</a>
                    </li>
                </ul>
            </div>

            <div class="profile pull-right">
                <div class="dropdown">
                    <a class="dropdown-toggle" href="javascript: void 0;" data-toggle="dropdown">
                        <span class="company-logo">
                        <img :src="`${resource}/images/company-logo/${production}.png`">
                        </span>
                        <span class="company-name">{{principal.username}}</span>
                        <span class="glyphicon"></span>
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="dropdownMenu2">
                        <li class="item-todo"><a @click.prevent="onToggleDashboard('item-todo')" href="javascript:void 0;" class="btn-toggle-dashboard">任务单</a></li>
                        <li class="item-notice"><a @click.prevent="onToggleDashboard('item-notice')" href="javascript:void 0;" class="btn-toggle-dashboard" >公告</a></li>
                        <li class="item-faq">
                            <router-link :to="`/faq`">帮助</router-link>
                        </li>
                        <li class="item-logout"><a href="${api}/j_spring_security_logout" >退出</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { mapState } from 'vuex';
    import Dashboard from './Dashboard';
    import MessageBox from 'components/MessageBox';

    export default {
        props: {
            mkey: {
                type: String,
            },
        },
        computed: {
            ...mapState({
                level1Menus: state => state.menu.filter({systemMenuLevel: 0}),
                principal: state => state.principal
            }),
            level1ActiveMenu: function() {
                var path = this.$store.state.menu.getMenuPath(this.mkey);
                return path.length ? path[0] : {};
            }
        },
        // watch: {
        //     mkey: function(value) {
        //         if(value){
        //             Dashboard.close();
        //         }else {
        //             Dashboard.open('item-todo');
        //         }
        //         console.log(value);
        //     }

        // },
        mounted: function() {
            var $el = $(this.$el);
            var $logo = $el.find('.logo');
            var $profile = $el.find('.profile');
            var $nav = $el.find('.nav');
            var $triggerCollapseBtn = $el.find('.hd-collapsed');
            var nav_width = $nav.width();
            
            var responsive = function(){
                var width = $(window).width();
                var pullleft_width = $logo.width();
                var pullright_width = $profile.width();
                var mid_width = width - pullleft_width - pullright_width;

                if(nav_width >= mid_width - 20){
                    $el.addClass('x-collapse');
                    $nav.hide();
                } else {
                    $el.removeClass('x-collapse');
                    $nav.show();
                }
            };
            var timer = null;
            
            $(window).resize(function(){
                if (timer) {
                    clearTimeout(timer);
                    timer = null;
                }

                var timer = setTimeout(responsive, 100);
            });

            $triggerCollapseBtn.click(function(e) {
                e.preventDefault();
                $nav.toggle();
            });

            responsive();
        },
        methods: {
            onToggleDashboard: function(name,options) {
                if (this.principal.modifyPasswordTimes == 0) {
                    MessageBox.open('首次登陆，请修改密码！');
                    return;
                }

                if ($('body').is('.welcome')) {
                    return;
                }

                Dashboard.open(name,options);
            },
            onClickMenuLink: function(e) {
                e.preventDefault();
                var href = e.target.href + '?t=' + Date.now();
                location.assign(href);
            }
        }
    }
</script>