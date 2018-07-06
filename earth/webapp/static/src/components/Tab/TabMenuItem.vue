<style>
    .tab-menu-item > a {
        display: block;
        text-decoration: none;
        cursor: pointer;
    }
</style>

<template>
    <li class="tab-menu-item"
        @click="$parent.$emit('input', id)"
        :class="{active: $parent.value === id}">
        <a>
            <slot></slot>
        </a>
    </li>
</template>

<script>
    export default {
        props: ['id']
    }
</script>