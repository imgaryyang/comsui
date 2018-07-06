<style lang="sass">
</style>

<template>
    <div>
        <slot></slot>
    </div>
</template>

<script>
    export default {
        name: 'select-address',
        componentName: 'select-address',
        props: {},
        data: function() {
            return {}
        },
        beforeMount: function() {
            this.$on(`select.province`, () => {
                this.city && (this.city.internalValue = '');
                this.district && (this.district.internalValue = '');
            });

            this.$on(`select.city`, () => {
                this.district && (this.district.internalValue = '');
            });

            this.$on(`mounted.province`, (province) => {
                this.province = province;
            });

            this.$on(`mounted.city`, (city) => {
                this.city = city;
            });

            this.$on(`mounted.district`, (district) => {
                this.district = district;
            });
        }
    }
</script>