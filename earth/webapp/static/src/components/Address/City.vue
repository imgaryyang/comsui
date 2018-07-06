<style lang="sass">
</style>

<template>
    <el-select 
        :value="value"
        @option-select="handleOptionSelect"
        @input="handleInput"
        class="short">
        <el-option 
            v-for="item in options"
            :label="item.name"
            :value="item.code">
        </el-option>
    </el-select>
</template>

<script>
    import { getCity } from 'api/address';
    import Emitter from 'element-ui/lib/mixins/emitter';

    export default {
        name: 'select-city',
        mixins: [Emitter],
        props: {
            value: String,
            provinceCode: String
        },
        data: function() {
            return {
                internalValue: this.value,
                options: []
            }
        },
        watch: {
            internalValue: function(cur) {
                var index = this.options.findIndex(item => cur === item.code);
                this.$emit('input', cur, index !== -1 ? this.options[index].name : void 0);
            },
            value: function(cur) {
                this.internalValue = cur;
            },
            provinceCode: function(cur) {
                this.fetch();
            }
        },
        beforeMount: function() {
            this.fetch();
        },
        mounted: function() {
            this.dispatch('select-address', 'mounted.city', this);
        },
        methods: {
            fetch: function() {
                const { provinceCode } = this;
                if (!provinceCode) return;
                getCity(provinceCode).then(list => this.options = list);
            },
            handleInput: function(value) {
                this.internalValue = value;
            },
            handleOptionSelect: function(cur, old) {
                if (cur.value !== old.value) {
                    this.dispatch('select-address', 'select.city');
                }
            }
        }
    }
</script>