<style lang="sass">
    .modal-body {
        line-height: 1.42857143;
        min-height: 130px;

        &.align-left {
            text-align: left;
        }

        &.align-center {
            text-align: center;
        }

        .form {
            min-width: auto;
            width: auto;
            padding: 10px;
        }
    }
</style>

<template>
    <div class="modal-body" :class="'align-'+align">
        <slot></slot>
    </div>
</template>

<script>
    export default {
        props: {
            align: {
                default: 'center' // left, center
            }
        }
    }
</script>