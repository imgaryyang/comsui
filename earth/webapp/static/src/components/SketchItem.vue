<style lang="sass">
    @import '~assets/stylesheets/base';

    .icon-key {
        background-position: -24px -212px;
        display: inline-block;
        height: 24px;
        width: 24px;
        background-repeat: no-repeat;
        vertical-align: middle;
        float: left;
        margin-right: 10px;
        margin-top: 5px;
    }

    .sketch-item {
        padding: 10px;
        border: 1px solid #e0e0e0;
        border-radius: 4px;
        position: relative;
        margin-bottom: 12px;
        overflow: hidden;
        width: 415px;
        line-height: 1.414;


        &:last-child {
            margin-bottom: 20px;
        }

        &:hover {
            .operate {
                display: block;
            }
        }

        .operate {
            position: absolute;
            right: 12px;
            top: 7px;
            display: none;

            & > a {
                height: 16px;
                width: 16px;
                cursor: pointer;

                &.disabled {
                    opacity: 0.5!important;
                }
            }

            & > a + a {
                margin-left: 5px;
            }

            .edit,
            .delete {
                background-image: url(~assets/images/icons.png);
                display: inline-block;
                background-repeat: no-repeat;
                vertical-align: middle;
            }

            .edit {
                background-position: -116px -25px;
            }

            .delete {

                background-position: -117px -4px;
            }

            .el-icon-close {

                &:before {
                    width: 10px;
                    height: 10px;
                }
            }
            .delete-key {
                color: #999;
                width: 10px;
                height: 10px;
                cursor: pointer;
            }
        }

        .stamp {
            height: 60px;
            width: 60px;
            float: left;
            margin-right: 10px;
            border: none;
        }

        .text {
            overflow: hidden;
            font-size: 14;

            & > p {
                @include ellipsis();
                margin: 0;

                &:first-child {
                    color: #333;
                    font-size: 18px;
                    margin: 3px 0 5px;
                }

                &:not(:first-child) {
                    color: #999;
                    padding-top: 1px;
                }
            }
        }
    }

    .sketch-item.error {
        border-color: $brand-danger; 
    }
</style>

<template>
    <div class="sketch-item">
        <slot></slot>
    </div>
</template>

