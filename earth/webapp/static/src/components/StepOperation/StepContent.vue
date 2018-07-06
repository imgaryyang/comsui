<style>
    .step-content {
        padding: 10px 0;
    }
</style>

<template>
    <div class="step-content">
        <slot></slot>
    </div>
</template>

<script>
    export default {
        props: {
            value: Number
        }
    }
</script>