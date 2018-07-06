<style lang="sass">
    .tab-menu {
        list-style: none;
        padding: 0;
        overflow: hidden;

        .tab-menu-item {
            float: left;
        }
    }

    .tab-menu.segment-menu {
        border: 1px solid #436ba7;
        border-radius: 2px;
        display: inline-block;

        .tab-menu-item {
            &.active a {
                color: #fff;
                background-color: #436ba7;
            }

            a {
                color: #436ba7;
                padding: 6px 20px;
                line-height: 1;
                text-decoration: none;
            }
        }
    }

    .tab-menu.nav-menu {
        border-bottom: 2px solid #436ba7;

        .tab-menu-item {
            &.active a {
                color: #fff;
                background-color: #436ba7;
            }

            a {
                padding: 10px 15px;
                color: #333;
                line-height: 1;
                border-radius: 7px 7px 0 0;
                text-align: center;
                min-width: 80px;
            }
        }
    }
</style>

<template>
    <ul class="tab-menu" :class="{
        'nav-menu': type == 'nav',
        'segment-menu': type == 'segment'
    }">
        <slot></slot>
    </ul>
</template>

<script>
    export default {
        props: {
            value: String,
            type: {
                default: 'nav' // nav, segment
            }
        }
    }
</script>