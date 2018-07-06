<template>
    <input type="file"
        @change="change"
        :name="$parent.name"
        :disabled="$parent.disabled"
        :multiple="$parent.multiple">
</template>

<script>
    export default {
        methods: {
            change(e) {
                // this.$destroy();
                // this.$parent._addInputFileElement(e.target);
                console.log('changed');
            }
        }
    }
</script>
