<style>
    
</style>

<template>
    <div class="step-content-item"
        v-show="$parent.value === index">
        <slot></slot>
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