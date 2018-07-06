<template>
    <li
        v-if="isShowAll"
        @click="allSelect"
        :class="[
            'casitem',
            !index ? 'all-select' : '',
            !index ? childrenSelectedState : '',
            index && allSelectFlag ? 'selected' : ''

    ]">
        <slot>
            <span>{{ label }}</span>
        </slot>
    </li>
</template>

<script>

    export default {
        name: 'el-select-all-item',

        props: {
            label: {
                type: String,
                default: '全选'
            },
            options: {
                type: [Array, Object],
                required: true
            },
            index: {
                type: [Number, String]
            },
            collection: Array,
            selected: Array,
            multiple: [Number, String]
        },

        data: function() {
            var isAllSelect = () => {
                var flag = true
                if(this.index){
                     this.options.forEach(data => {
                        if(!this.isInSelected(data)){
                            flag = false;
                        }
                    })
                }else{
                    this.collection.forEach(data => {
                        if(!this.isInSelected(data)){
                            flag = false;
                        }
                    })
                }
                return flag
            };
            return {
                allSelectFlag: isAllSelect(),
            }
        },
        computed: {
            //菜单的全选是否显示: 2级菜单都可全选 只有子菜单可以全选 单选不显示全选
            isShowAll: function(){
                return this.multiple == 2 || (this.multiple == 1 && this.index)
            },
            //是否全选
            isAllSelect: function(){
                var flag = true
                if(this.index){
                     this.options.forEach(data => {
                        if(!this.isInSelected(data)){
                            flag = false;
                        }
                    })
                }else{
                    this.currentCollections.forEach(data => {
                        if(!this.isInSelected(data)){
                            flag = false;
                        }
                    })
                }
                return flag
            },
            //总的全选标志
            notallSelected: function() {
                var flag = true;
                this.currentCollections.forEach(data => {
                    if(this.isInSelected(data)){
                        flag = false;
                    }
                })
                return flag;
            },
            isallSelected: function() {
                var flag = true;
                this.currentCollections.forEach(data => {
                    if(!this.isInSelected(data)){
                        flag = false;
                    }
                })
                return flag;
            },
            //当前的子菜单列表
            currentCollections: function(){
                return this.collection.filter(item => item.parentId)
            },
            childrenSelectedState: function() {
                if (this.notallSelected) {
                    return 'zero'
                } else if (this.isallSelected) {
                    return 'full'
                } else {
                    return 'partion'
                }
            }
        },
        watch: {
            isAllSelect: function(current){
                this.allSelectFlag = current
            }
        },
        methods: {
            allSelect: function(){
                this.allSelectFlag = !this.allSelectFlag
                if(this.index){
                    this.$parent.$emit('all-item-select', this.options, this.allSelectFlag)
                }else{
                    this.$parent.$emit('all-all-select', this.currentCollections ,this.allSelectFlag)
                }
            },
            isInSelected: function(data) {
                var index = this.selected.findIndex(item => item._id === data._id)
                return index != -1
            }
        }
    }
</script>