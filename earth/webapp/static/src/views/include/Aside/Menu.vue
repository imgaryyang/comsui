<style lang="sass">
    @import '~assets/stylesheets/base';

    .aside-menu {
        height: 100%;
        overflow-y: auto;
        position: relative;
        background-color: #fff;
        width: 185px;
        float: left;
        color: #666;
        border-right: 1px solid #dedede;
        background-color: #f4f4f4;

        @include max-screen(1080px) {
            width: 170px;
        }

        .extend {
            position: absolute;
            right: 0;
            width: 20px;
            bottom: 0;
            top: 0;

            &:hover {
                @include linear-gradient(90deg, rgba(255, 255, 255, 0.19), rgba(0, 0, 0, 0.04));
            }
            .icon {
                width: 20px;
                height: 20px;
                background: url(~assets/images/icons.png) no-repeat;
                background-position: -243px -225px;
                position: absolute;
                right: 0;
                top: 50%;
                @include opacity(40);
            }
        }

        &.closed {
            width: 18px;

            .extend {
                .icon {
                    background-position: -225px -225px;
                }
            }

            .menus {
                display: none;
            }
        }

        .menus {
            padding: 40px 0 10px 0;
            position: relative;
        }
    }
</style>

<template>
    <div 
        class="aside-menu" 
        :class="{closed: closed}">
        <ul class="menus">
            <SMenuItem 
                v-for="submenu in submenus"
                :active-menu-path="activeMenuPath"
                :menu="submenu"
                :key="submenu.id"></SMenuItem>
        </ul>
        <div class="extend" @click="onToggleAside">
            <i class="icon"></i>
        </div>
    </div>
</template>

<script>
    import { mapState } from 'vuex';

    export default {
        components: {
            SMenuItem: require('./MenuItem')
        },
        props: {
            mkey: {
                type: String,
            }
        },
        computed: {
            submenus: function() {
                var { menu } = this.$store.state;
                var parents = menu.getMenuPath(this.mkey);
                return parents.length ? menu.getSubmenus(parents[0].mkey, 1) : [];
            },
            activeMenuPath: function() {
                return this.$store.state.menu.getMenuPath(this.mkey);
            }
        },
        data: function() {
            return {
                closed: false
            };
        },
        methods: {
            onToggleAside: function() {
                this.closed = !this.closed
                $(window).trigger('toggle.aside', this.closed)
            }
        }
    }
</script>