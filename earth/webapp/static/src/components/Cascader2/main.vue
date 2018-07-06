<style lang="sass">
    .cascader{
        position: relative;
        cursor: pointer;
        width: 130px;

        .cascader_close__icon {
            opacity: 0.8;
            cursor: pointer;
            z-index: 1;
        }
        &:hover{
            .el-input__inner{
                border-color: #20a0ff;
            }
        }
        .el-input__inner {
            border-radius: 2px;
            outline: none;
        }
        .el-input__icon{
            transition-duration: 0.3s;
        }
        .el-input__icon.is-reverse {
            transform: rotate(180deg);
        }
    }
    .cascader_label{
        position: absolute;
        top: 0;
        left: 0;
        box-sizing: border-box;
        width: 100%;
        padding-right: 30px;
        height: 100%;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        font-size: 12px;
        line-height: 30px;
        padding-left: 10px;
        text-align: left;
    }
    .filter_input {
        position: absolute;
        left: 0;
        top: 1px;
        z-index: 999;
    }
    .label_hide {
        display: none;
    }
</style>

<template>
    <div
        class="cascader"
        ref="reference"
        :style="{width: width + 'px'}"
        @click="handleClick"
        @mouseenter="inputHover = true"
        @mouseleave="inputHover = false"
        v-clickoutside="handleClickoutside">
        <input
            type="text"
            class="el-select__input filter_input"
            :class="`is-${ size }`"
            @focus="infilter = true"
            v-model="query"
            v-if="filterable"
            :style="{ width: inputLength + 'px', 'max-width': inputWidth - 42 + 'px' }"
            ref="input">
        <el-input
            :placeholder="selected.length || infilter ? undefined : placeholder"
            :size="size"
            readonly>
            <template slot="icon">
                <i
                  key="1"
                  v-if="clearable && inputHover && currentLabels.length"
                  class="el-input__icon el-icon-circle-close cascader_close__icon"
                  @click="clearValue"
                ></i>
                <i
                  key="2"
                  v-if="!(clearable && inputHover && currentLabels.length) && !infilter"
                  class="el-input__icon el-icon-caret-bottom"
                  :class="{ 'is-reverse': menuVisible }"
                ></i>
            </template>
        </el-input>
        <span :class="['cascader_label', infilter ? 'label_hide' : '']">
            <template  v-for="(item, index) in currentLabels">{{ item.label }}<span v-if="index < currentLabels.length - 1">、</span></template>
        </span>
    </div>
</template>

<script>
    import Clickoutside from 'element-ui/lib/utils/clickoutside';
    import Popper from 'element-ui/lib/utils/vue-popper'
    import CaspanelWrapper from './CaspanelWrapper.vue'
    import Flat from './flatten.js'
    import Vue from 'vue'
    import { debounce, uniq } from 'lodash'

    const popperMixin = {
        props: {
            placement: {
                type: String,
                default: 'bottom-start'
            },
            appendToBody: Popper.props.appendToBody,
            offset: Popper.props.offset,
            boundariesPadding: Popper.props.boundariesPadding,
            options: Popper.props.options
        },
        methods: Popper.methods,
        data: Popper.data,
        beforeDestroy: Popper.beforeDestroy
    }

    export default {
        name: 'list-cascader',
        directives: { Clickoutside },
        mixins: [popperMixin],
        props: {
            collection: {
                type: Array,
                required: true
            },
            value: {
                type: Array,
                default: () => []
            },
            placeholder: {
                type: String,
                default: '请选择'
            },
            size: String,
            clearable: {
                type: Boolean,
                default: false
            },
            multiple: {
                type: [Number, String],
                default: 2
            },
            customItem: Function,
            defaultSelected: { //默认全选
                type: Boolean,
                default: false
            },
            width: {
                type: Number,
                default: 130
            },
            flattenFn: {
                type: Function,
                default: null
            },
            filter: {
                type: Boolean,
                default: false
            }
        },
        watch: {
            value: function(current) {
                this.selected = current
            },
            selected: function(current) {
                if(this.filterable) {
                    this.$refs.input.focus();
                }
                if (this.menu) {
                    this.menu.selected = current
                    this.$emit('input', current)
                }
            },
            collection: {
                deep: true,
                handler: function(current) {
                    this.flatCollection = this.flattenFn ? this.flattenFn(current).array : new Flat(current).array
                    this.filterCollection = this.flattenFn ? this.flattenFn(current).array : new Flat(current).array
                    this.$emit('getFlatCollection', this.flatCollection.filter(item => item._isLastLevel).slice())
                    if (this.defaultSelected) {
                        if(this.multiple == 0){
                            var items = this.flatCollection.filter(item => item._isLastLevel).slice()
                            this.selected = items.length > 0 ? [items[0]] : []
                        } else {
                            this.selected = this.flatCollection.filter(item => item._isLastLevel).slice()
                        }
                    }
                    if (!this.menu) {
                        this.initMenu()
                    }
                }
            },
            menuVisible: function(current) {
                current ? this.showMenu() : this.hideMenu()
            },
            infilter: function(current) {
                this.query = '';
            },
            query: function(current) {
                this.filterFlatCollection();
            }
        },
        data: function() {
            return {
                flatCollection: new Flat(this.collection).array,
                menuVisible: false,
                selected: [],
                inputHover: false,
                inputLength: 90,
                inputWidth: 0,
                query: '',
                infilter: false,
                filterCollection: new Flat(this.collection).array
            }
        },
        computed: {
            filterable: function(){
                return this.multiple == 2 || this.filter
            },
            currentLabels: function(){
                return this.selected.filter(item => item._isLastLevel)
            },
        },
        methods: {
            filterFlatCollection: debounce(
                function() {
                    var itemCollection = this.query != '' ? this.flatCollection.filter(item => item._isLastLevel && item.label.indexOf(this.query) != -1) : this.flatCollection.slice();
                    var menu = itemCollection.map(item => item.parentId);
                    var menuCollection = this.flatCollection.filter(item => !item._isLastLevel && menu.includes(item._id));

                    var firstmenu = menuCollection.map(item => item.parentId);
                    var firstMenuCollection = this.flatCollection.filter(item => !item.parentId && firstmenu.includes(item._id))

                    this.filterCollection = uniq(menuCollection.concat(itemCollection, firstMenuCollection));
                    console.log(this.filterCollection.filter(item => item._isLastLevel))
                    this.menu.collection = this.filterCollection.slice();
            }, 500),
            initMenu: function() {
                this.menu = new Vue(CaspanelWrapper).$mount()
                this.menu.collection = this.filterCollection
                this.menu.selected = this.selected
                this.menu.multiple = this.multiple
                this.menu.customItem = this.customItem
                this.menu.$on('select', this.handleSelect)
                this.menu.$on('deselect', this.handleDeselect)
                this.menu.$on('all-item-select', this.handleSelectAllItemClick)
                this.menu.$on('all-all-select', this.handleSelectAllAllClick)
                this.popperElm = this.menu.$el
            },
            showMenu: function() {
                if (!this.menu) {
                    this.initMenu()
                }
                this.menu.visible = true
                this.$nextTick(() => {
                    this.updatePopper()
                })
            },
            hideMenu: function() {
                this.menu.visible = false
            },
            clearValue: function(ev) {
                if(!this.infilter) {
                    ev.stopPropagation()
                    this.selected = [];
                    this.menuVisible = false;
                } else {
                    this.query = '';
                }
            },
            handleSelect: function(data) {
                var mult1 = this.selected.length && this.selected[0].parentId === data.parentId
                var i = this.selected.findIndex(item => item._id === data._id)
                if(this.multiple == 0){
                    this.selected = []
                    this.selected.push(data)
                    this.menuVisible = false
                    return
                } else if(this.multiple == 1 ){
                    if(!mult1) this.selected = []
                }
                if (i == -1) {
                    this.selected.push(data)
                }
            },
            handleDeselect: function(data) {
                var i = this.selected.findIndex(item => item._id === data._id)
                if (i != -1) {
                    this.selected.splice(i, 1)
                }
            },
            handleSelectAllItemClick: function(data, isAll) {
                if(isAll){
                    data.forEach(item => this.handleSelect(item))
                }else{
                    data.forEach(item => this.handleDeselect(item))
                }
            },
            handleSelectAllAllClick: function(data, isAll){
                if(isAll){
                    data.forEach(item => this.handleSelect(item))
                }else{
                    data.forEach(item => this.handleDeselect(item))
                }
            },
            handleClickoutside() {
                this.menuVisible = false;
                this.infilter = false;
            },
            handleClick: function(){
                if(this.filterable) {
                    this.menuVisible = true;
                    this.$refs.input.focus();
                } else {
                    this.menuVisible = !this.menuVisible;
                }
            }
        }
    };
</script>