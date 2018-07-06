<style lang="sass">
    @import '~assets/stylesheets/base';
    .step-tip {
        padding: 10px 0;
        @include clearfix;
    }

    .step-tip.align-left {
        .step-tip-item {
            padding: 0 15px;
        }

        .step-tip-item:not(:first-child) {
            $dividtion-wdith: 55px;
            margin-left: $dividtion-wdith;

            &:before {
                right: 100%;
                width: $dividtion-wdith;
            }
        }
    }

    .step-tip.align-center {
        .step-tip-item {
            width: 32.5%;
        }

        .step-tip-item:not(:first-child) {
            &:before {
                left: -25%;
                width: 50%;
            }
        }
    }
</style>

<template>
    <div class="step-tip" :class="['align-'+align]">
        <slot></slot>
    </div>
</template>

<script>
    export default {
        props: {
            value: Number,
            align: {
                default: 'left' // center, left
            },
            size: {
                default: 'long'
            }
        }
    }
</script>