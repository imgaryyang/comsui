<style>
</style>

<template>
    <div class="tab-content-item"
        v-show="$parent.value === id">
        <slot></slot>
    </div>
</template>

<script>
    export default {
        props: ['id']
    }
</script>