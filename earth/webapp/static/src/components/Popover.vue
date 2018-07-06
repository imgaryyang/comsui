<style lang="sass">
    .el-popover {
        background: #fff;
        position: fixed;
        border: 1px solid #ccc;
        border-radius: 2px;
        line-height: 1.414;

        .title {
            line-height: 32px;
            padding: 0 15px;
        }

        .content {
            padding: 13px;
            background: transparent;
        }

        &.effect-leave-active,
        &.effect-enter-active {
          transition: all .2s ease;
        }

        &.effect-leave-active,
        &.effect-enter {
            opacity: 0;
        }
    }

    .arrow-indicator, 
    .arrow-indicator:after {
        position: absolute;
        display: block;
        width: 0;
        height: 0;
        border-color: transparent;
        border-style: solid;
    }

    .arrow-indicator {
        border-width: 8px;

        &:after {
            border-width: 7px;
            content: "";
        }
    }

    .arrow-indicator.bottom {
        border-top-width: 0;
        border-bottom-color: #ccc;
        bottom: 100%;
        left: 50%;
        margin-left: -8px;

        &:after {
            border-top-width: 0;
            border-bottom-color: #fff;
            top: 1px;
            margin-left: -7px;
        }
    }

    .arrow-indicator.top {
        border-bottom-width: 0;
        border-top-color: #ccc;
        top: 100%;
        left: 50%;
        margin-left: -8px;

        &:after {
            border-bottom-width: 0;
            border-top-color: #fff;
            bottom: 1px;
            margin-left: -7px;
        }
    }

</style>

<template>
    <span>
        <span ref="trigger">
            <slot></slot>
        </span>

        <transition name="effect">
            <div class="el-popover" ref="popover" v-if="visible">
                <div v-if="title" class="title" v-html="title"></div>
                <div class="content" v-html="content"></div>
                <span class="arrow-indicator" :class="placement"></span>
            </div>
        </transition>
    </span>
</template>

<script>
    import PopoverMixin from 'mixins/Popover';

    export default {
        mixins: [PopoverMixin]
    }
</script>