<style lang="sass">
    .modal-header {
        background-color: #282d33;
        padding: 10px 15px;

        .modal-title {
            font-size: 14px;
            color: #fff;

            .sub-title {
                color: #43c3ff;
            }
        }

        .close {
            font-weight: normal;
            text-shadow: none;
            opacity: 1;
            color: #6D6D6D;
            margin-top: 0;
        }
    }
</style>

<template>
    <div class="modal-header">
        <slot>
            <button type="button" class="close pull-right" @click="$parent.close()"><span>×</span></button>
            <h4 class="modal-title">{{title}}</h4>
        </slot>
    </div>
</template>

<script>
    export default {
        props: {
            title: {
                type: String,
                default: '提示'
            }
        }
    }
</script>