<style lang="sass">
    .query-area .combo-query-box .el-input__inner {
        width: 210px;
    }
</style>

<template>
    <el-input 
        :value="activeValue"
        @change="handleChange"
        size="small"
        :icon="iconClose"
        @click="activeValue = ''"
        class="combo-query-box" 
        placeholder="请输入内容">
        <el-select 
            v-model="activeKey"
            class="short" 
            slot="prepend" 
            placeholder="请选择">
            <slot></slot>
        </el-select>
    </el-input>
</template>

<script>
    const extract = function(source) {
        var activeKey;
        var activeValue;
        var iconClose = '';

        var existed = Object.keys(source).some(_key => {
            var _value = source[_key];
            if (_value != null && _value != '') {
                activeKey = _key;
                activeValue = _value;
                return true;
            }
        });

        return { activeKey, activeValue, iconClose};
    };

    export default {
        props: {
            value: {
                required: true,
                type: Object // 按顺序只取第一个有值的字段
            }
        },
        data: function() {
            return extract(this.value);
        },
        watch: {
            activeKey: function(current) {
                if (!this.activeValue) return;
                this.$emit('input', {[this.activeKey]: this.activeValue});
            },
            activeValue: function(current) {
                if (!this.activeKey) return;
                this.$emit('input', {[this.activeKey]: this.activeValue});
            },
            value: function(current) {
                var { activeKey, activeValue } = extract(current);
                activeKey && (this.activeKey = activeKey);
                this.activeValue = activeValue;
            }
        },
        methods: {
            handleChange: function(val){
                this.iconClose = val ? 'close' : '';
                this.activeValue = val
            }
        }
    }
</script>