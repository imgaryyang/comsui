<style lang="sass">
    .amount-card-content {
        padding: 25px 15px;
        .box-card {
            position: relative;
            float: left;
            margin: 0 10px;
            border: 1px solid #dedede;
            width: 320px;
            height: 150px;
            box-sizing: border-box;
            border-radius: 5px;
            overflow: hidden;
            .box-card-scroll {
                position: relative;
                left: 0;
                .left > div {
                    height: 100%;
                    overflow: auto;
                }
                &.right {
                    left: -100%;
                }
                width: 200%;
                height: 100%;
                .left, .right {
                    width: 50%;
                    height: 100%;
                    float: left;
                    padding: 20px;
                }
                .left {
                    padding: 20px 20px 10px 20px;
                }
            }
            ._hidden {
                display: block;
                width: 0;
                height: 0;
                overflow: hidden;
            }
            &.active {
                background-color: #f5f5f5;
            }
            .hd {
                font-size: 18px;
                color: #666;
                font-weight: normal;
                margin-bottom: 10px;
                span {
                    font-size: 24px;
                    font-weight: bold;
                    color: #436ba7;
                }
            }
            p {
                margin-bottom: 0;
                line-height: 20px;
                font-size: 12px;
                color: #666;
                word-break: break-all;
                span {
                    font-size: 14px;
                    font-weight: bold;
                    color: #436ba7;
                }
            }
            .left-skip, .right-skip {
                position: absolute;
                top: 50%;
                margin-top: -20px;
                display: block;
                background-color: #fff;
                color: #999;
                font-size: 10px;
                width: 10px;
                height: 40px;
                line-height: 40px;
                cursor: pointer;
                border: 1px solid #e7e8e9;
                z-index: 9;
            }
            .left-skip {
                left: 0px;
                border-left: none;
                border-bottom-right-radius: 20px;
                border-top-right-radius: 20px;
                &:before {
                    margin-left: -3px;
                }
            }
            .right-skip {
                right: 0px;
                border-right: none;
                border-bottom-left-radius: 20px;
                border-top-left-radius: 20px;
            }
            .box-card-item {
                cursor: pointer;
                float: left;
                width: 45%;
                box-sizing: border-box;
                border: 1px solid #dedede;
                margin-bottom: 8px;
                text-align: center;
                border-radius: 5px;
                color: #999;
                line-height: 30px;
                &:nth-child(odd) {
                    margin-right: 10%;
                }
                &.active {
                    border-color: #436ba7;
                    color: #666;
                    font-weight: bold;
                }
            }
        }
    }
</style>
<template>
    <div
        @click="isLeft && $emit('click')"
        :class="{
            'box-card': true,
            'active': isLeft && active
        }">
        <span class="el-icon-caret-right left-skip" v-if="!isLeft && hasRight" @click.stop="isLeft = true"></span>
        <div class="box-card-scroll" :class="{'right': !isLeft}">
            <div class="left">
                <slot name="left"></slot>
            </div>
            <div class="right">
                <slot name="right" v-if="hasRight"></slot>
            </div>
        </div>
        <span class="el-icon-caret-left right-skip" v-if="isLeft && hasRight" @click.stop="isLeft = false"></span>
    </div>
</template>
<script>
    export default {
        props: {
            value: Boolean,
            hasRight: {
                type: Boolean,
                default: false
            },
            url: {
                type: Array,
                default: () => []
            },
            action: String,
        },
        data: function() {
            return {
                isLeft: true
            }
        },
        watch: {
            value: function(cur) {
                if(cur) {
                    this.isLeft = cur;
                    this.$emit('input', false);
                }
            }
        },
        computed: {
            active: function() {
                return this.url.length && this.url.includes(this.action);
            }
        }
    }
</script>
