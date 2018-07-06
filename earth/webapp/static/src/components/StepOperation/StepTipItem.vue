<style lang="sass">
    .step-tip-item {
        display: inline-block;
        text-align: center;
        position: relative;

        .ordinal {
            color: #fff;
            height: 36px;
            width: 36px;
            line-height: 36px;
            margin: auto;
            border-radius: 50%;
            background: #999;
        }

        .text {
            margin-top: 6px;
            color: #999;
        }

        &.active {
            .text {
                color: #3388ee;
            }

            .ordinal {
                background: #3388ee;
            }
        }
    }

    .step-tip-item:not(:first-child) {
        &:before {
            content: '';
            height: 1px;
            display: block;
            position: absolute;
            background: #ddd;
            top: 18px;
        }
    }
</style>

<template>
    <div class="step-tip-item"
        :class="{active: $parent.value === index}">
        <div class="ordinal">{{ index }}</div>
        <div class="text"><slot></slot></div>
    </div>
</template>

<script>
    export default {
        props: {
            index: {
                type: Number,
                required: true
            }
        }
    }
</script>