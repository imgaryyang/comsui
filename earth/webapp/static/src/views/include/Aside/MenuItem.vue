<style lang="sass">
    @import '~assets/stylesheets/base';

    .aside-menu-item {
        line-height: 30px;
        margin: 5px 0;

        .menu-link {
            padding-left: 45px;
            position: relative;
            text-decoration: none;
            display: block;
            color: inherit;
        }

        .submenus {
            font-size: 12px;
            padding-left: 20px;
            color: #436ba7;
            margin: 0;
            padding: 0;
            overflow: hidden;
        }

        &.z-active {
            font-weight: bold;
            background-color: #fff;
            border-right: 3px solid #436ba7;
            color: #436ba7;
        }
    }

    .aside-menu-item.has-submenu {
        & > .menu-link:before {
            content: "";
            background: url(~assets/images/icons.png) no-repeat;
            background-position: -2px -157px;
            position: absolute;
            left: 20px;
            top: 9px;
            display: inline-block;
            height: 12px;
            width: 12px;
        }

        .aside-menu-item {
            margin: 0;
            padding-left: 10px;
        }

        &.expanded {
            & > .menu-link:before {
                background-position: -2px -179px;
            }
        }
    }
</style>

<template>
    <li class="aside-menu-item"
        :class="{
            'has-submenu': hasSubmenu,
            'z-active': active,
            'expanded': hasSubmenu ? expanded : false
        }">
        <a class="menu-link" 
            @click="onClickMenuLink"
            :href="hasSubmenu ? 'javascript:void(0)' : `${ctx_deprecated}/${menu.url}`">
            {{menu.name}}
        </a>
        <ul v-if="hasSubmenu"
            class="submenus"
            v-show="expanded">
            <MenuItem v-for="secondSubmenu in submenus"
                :active-menu-path="activeMenuPath"
                :menu="secondSubmenu"
                :key="secondSubmenu.id"></MenuItem>
        </ul>
    </li>
</template>

<script>
    import { mapState } from 'vuex';

    export default {
        name: 'MenuItem',
        props: {
            menu: {
                required: true
            },
            activeMenuPath: {
                type: Array
            }
        },
        computed: {
            submenus: function() {
                return this.$store.state.menu.getSubmenus(this.menu.mkey) || [];
            },
            hasSubmenu: function() {
                return !!this.submenus.length;
            },
            active: function() {
                var len = this.activeMenuPath.length;
                var last = this.activeMenuPath[len - 1];
                return last ? last.mkey === this.menu.mkey : false
            }
        },
        data: function() {
            return {
                expanded: this.existInActiveMenuPath(this.menu.mkey)
            };
        },
        watch: {
            activeMenuPath: function() {
                var exist = this.existInActiveMenuPath(this.menu.mkey);
                if (exist) {
                    this.expanded = exist;
                }
            }
        },
        methods: {
            existInActiveMenuPath: function(mkey) {
                var index = this.activeMenuPath.findIndex(item => item.mkey === mkey);
                return index !== -1;
            },
            onClickMenuLink: function(e) {
                e.preventDefault();
                if (this.hasSubmenu) {
                    this.expanded = !this.expanded;
                } else {
                    var href = e.target.href + '?t=' + Date.now(); // 对旧页面不会影响。没有考虑复杂的URL情况
                    location.assign(href);
                }
            }
        }
    }
</script>