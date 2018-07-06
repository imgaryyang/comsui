<style lang="sass">
    .merchant-select {
        .item {
            margin-right: 5px;
            border: 1px solid #e0e0e0;
            padding: 6px 8px;
            border-radius: 4px;
            margin-bottom: 5px;
            float: left;
            line-height: normal;

            a {
                display: none;
            }

            &:hover a {
                display: inline;
            }
        }
    }
</style>

<template>
    <div class="merchant-select">
        <div class="selecteds clearfix" style="margin-bottom: -1px;">
            <span class="item" v-for="key in selected">
                {{getByKey(key).value}}
                <a href="#" @click.prevent="$emit('delete', key)">&times;</a>
            </span>
            <el-select 
                ref="select"
                clearable
                v-show="optional.length > 0"
                class="short" 
                @input="onInput">
                <el-option 
                    v-for="item in optional" 
                    :label="item.value"
                    :value="item.key">
                </el-option>
            </el-select>
        </div>
    </div>
</template>

<script>
    import SketchItem from 'components/SketchItem';
    import { mapState } from 'vuex';
    import EditAccountModal from './EditAccountModal';

    export default {
        props: {
            data: {
                type: Array,
                default: () => []
            },
            selected: {
                type: Array,
                default: () => []
            }
        },
        computed: {
            optional: function() {
                return this.data.filter(item => !this.selected.includes(item.key))
            }
        },
        methods: {
            onInput: function(key) {
                if (key !== '' && key !== undefined) {
                    this.$emit('add', key)
                    this.$nextTick(() => {
                        this.$refs.select.deleteSelected({stopPropagation: () => {}})
                    })
                }
            },
            getByKey: function(appKey) {
                var index = this.data.findIndex(item => item.key === appKey)
                if (index != -1) {
                    return this.data[index]
                } else {
                    return {}
                }
            }
        }
    }
</script>