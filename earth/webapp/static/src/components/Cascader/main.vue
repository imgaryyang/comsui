<style lang="sass">
  @import '~assets/themes/default/cascader.css';
  
/*  如果需要自定义样式可以去掉menu的注释，自定义
  cascader.css可能会发生变化*/

/*  .el-cascader-menu .el-cascader-menu__item.is-multiple{
    background-color: #ddd; 
  }*/
  .el-cascader-menus .el-cascader-menu__item {

    &.el-cascader-menu__item--extensible {

        &:after {
         content: '';
        }
    }
    &.is-active {
      color: #436ba7;
      background-color: #fff;

      &.is-custom {
        &:after {
          content: ""
        }
      }
      &:hover {
        background-color: #e4e8f1;
      }

      &:after {
        position: absolute;
        right: 10px;
        font-family: element-icons;
        content: "\E608";
        font-size: 11px;
        -webkit-font-smoothing: antialiased;
      }

      &.remove-after {
        background-color: #e4e8f1;

        &:after {
          font-family: element-icons;
          content: "\E602";
          font-size: 12px;
          -ms-transform: scale(.8);
          transform: scale(.8);
          position: absolute;
          right: 10px;
          margin-top: 1px;
          color: #436ba7;
        }
      }
    }

    &.empty_item {
      padding: 10px 0;
      margin: 0;
      text-align: center;
      color: #999;
      font-size: 14px;
      cursor: default;
    }
  }
</style>

<template>
  <span
    class="el-cascader"
    :class="[
      {
        'is-opened': menuVisible,
        'is-disabled': disabled
      },
      size ? 'el-cascader--' + size : ''
    ]"
    @click="handleClick"
    @mouseenter="inputHover = true"
    @mouseleave="inputHover = false"
    ref="reference"
    v-clickoutside="handleClickoutside"
  >
    <el-input
      ref="input"
      :readonly="!filterable"
      :placeholder="currentLabels.length ? undefined : placeholder"
      v-model="inputValue"
      @change="debouncedInputChange"
      :validate-event="false"
      :size="size"
      :disabled="disabled"
    >
      <template slot="icon">
        <i
          key="1"
          v-if="clearable && inputHover && currentLabels.length"
          class="el-input__icon el-icon-circle-close el-cascader__clearIcon"
          @click="clearValue"
        ></i>
        <i
          key="2"
          v-else
          class="el-input__icon el-icon-caret-bottom"
          :class="{ 'is-reverse': menuVisible }"
        ></i>
      </template>
    </el-input>
    <span class="el-cascader__label" v-show="inputValue === ''">
      <template v-if="showAllLevels">
        <template v-for="(label, index) in currentLabels">
          {{ label }}
          <span v-if="index < currentLabels.length - 1"> / </span>
        </template>
      </template>
      <template v-else>
        {{ currentLabels[currentLabels.length - 1] }}
      </template>
    </span>
  </span>
</template>

<script>
import Vue from 'vue';
import ElCascaderMenu from './menu';
import Popper from 'element-ui/lib/utils/vue-popper';
import Clickoutside from 'element-ui/lib/utils/clickoutside';
import emitter from 'element-ui/lib/mixins/emitter';
import Locale from 'element-ui/lib/mixins/locale';
import { t } from 'element-ui/lib/locale';
import debounce from 'throttle-debounce/debounce';

const popperMixin = {
  props: {
    placement: {
      type: String,
      default: 'bottom-start'
    },
    appendToBody: Popper.props.appendToBody,
    offset: Popper.props.offset,
    boundariesPadding: Popper.props.boundariesPadding,
    popperOptions: Popper.props.popperOptions
  },
  methods: Popper.methods,
  data: Popper.data,
  beforeDestroy: Popper.beforeDestroy
};

export default {
  name: 'el-cascader',

  directives: { Clickoutside },

  mixins: [popperMixin, emitter, Locale],

  props: {
    options: {
      type: Array,
      required: true
    },
    props: {
      type: Object,
      default() {
        return {
          children: 'children',
          label: 'label',
          value: 'value',
          disabled: 'disabled'
        };
      }
    },
    value: {
      type: Array,
      default() {
        return [];
      }
    },
    placeholder: {
      type: String,
      default() {
        return t('el.cascader.placeholder');
      }
    },
    disabled: Boolean,
    clearable: {
      type: Boolean,
      default: false
    },
    changeOnSelect: Boolean,
    popperClass: String,
    expandTrigger: {
      type: String,
      default: 'click'
    },
    filterable: {
      type: Boolean,
      default: false
    },
    size: String,
    showAllLevels: {
      type: Boolean,
      default: true
    },
    debounce: {
      type: Number,
      default: 300
    },
    multiple: {
      type: Boolean,
      default: false
    },
    customItem: {
      type: Boolean,
      default: false
    },
    renderItem: {
      type: Function,
      default: null
    }
//设置multiple时，不允许同时设置filterable，否则全选变多选
  },

  data() {
    return {
      currentValue: this.value,
      menu: null,
      debouncedInputChange() {},
      menuVisible: false,
      inputHover: false,
      inputValue: '',
      flatOptions: null
    };
  },

  computed: {
    labelKey() {
      return this.props.label || 'label';
    },
    valueKey() {
      return this.props.value || 'value';
    },
    childrenKey() {
      return this.props.children || 'children';
    },
    currentLabels() {
      let options = this.options;
      let labels = [];
      this.currentValue.forEach(value => {
        const targetOption = options && options.filter(option => option[this.valueKey] === value)[0];
        if (targetOption) {
          labels.push(targetOption[this.labelKey]);
          options = targetOption[this.childrenKey];
        };
        if(Array.isArray(value)){
          var arr = [];
          value.forEach(v => {
            var targetOption = options && options.filter(option => option[this.valueKey] === v)[0];
            if (targetOption) {
              arr.push(targetOption[this.labelKey]);
            };
          });
          arr = arr.join(',');
          labels.push(arr);
          if(labels.slice(-1)[0].length == 0)
            labels.pop();
        }
      });
      return labels;
    }
  },

  watch: {
    menuVisible(value) {
      value ? this.showMenu() : this.hideMenu();
    },
    value(value) {
      this.currentValue = value;
    },
    currentValue(value) {
      this.dispatch('ElFormItem', 'el.form.change', [value]);
    },
    options: {
      deep: true,
      handler(value) {
        if (!this.menu) {
          this.initMenu();
        }
        this.flatOptions = this.flattenOptions(this.options);
        this.menu.options = value;
      }
    }
  },

  methods: {
    initMenu() {
      this.menu = new Vue(ElCascaderMenu).$mount();
      this.menu.customItem = this.customItem;
      this.menu.renderItem = this.renderItem;
      this.menu.multiple = this.multiple;
      this.menu.options = this.options;
      this.menu.props = this.props;
      this.menu.filterable = this.filterable;
      this.menu.expandTrigger = this.expandTrigger;
      this.menu.changeOnSelect = this.changeOnSelect;
      this.menu.popperClass = this.popperClass;
      this.popperElm = this.menu.$el;
      this.menu.$on('pick', this.handlePick);
      this.menu.$on('customPick', this.handleCustomPick)
      this.menu.$on('activeItemChange', this.handleActiveItemChange);
      this.menu.$on('menuLeave', this.doDestroy);
    },
    showMenu() {
      if (!this.menu) {
        this.initMenu();
      }

      this.menu.value = this.currentValue.slice(0);
      this.menu.visible = true;
      this.menu.options = this.options;
      this.$nextTick(_ => {
        this.updatePopper();
        this.menu.inputWidth = this.$refs.input.$el.offsetWidth - 2;
      });
    },
    hideMenu() {
      this.inputValue = '';
      this.menu.visible = false;
    },
    handleActiveItemChange(value) {
      this.$nextTick(_ => {
        this.updatePopper();
      });
      this.$emit('active-item-change', value);
    },
    handlePick(value, close = true) {
      this.currentValue = value;
      this.$emit('input', value);
      this.$emit('change', value);

      if (close) {
        this.menuVisible = false;
      }
    },
    handleCustomPick(item, close= true){
      this.$emit('customPick', item)
      if(close){
        this.menuVisible= false
      }
    },
    handleInputChange(value) {
      if (!this.menuVisible) return;
      const flatOptions = this.flatOptions;

      if (!value) {
        this.menu.options = this.options;
        return;
      }

      let filteredFlatOptions = flatOptions.filter(optionsStack => {
        return optionsStack.some(option => new RegExp(value, 'i').test(option[this.labelKey]));
      });

      if (filteredFlatOptions.length > 0) {
        filteredFlatOptions = filteredFlatOptions.map(optionStack => {
          return {
            __IS__FLAT__OPTIONS: true,
            value: optionStack.map(item => item[this.valueKey]),
            label: this.renderFilteredOptionLabel(value, optionStack)
          };
        });
      } else {
        filteredFlatOptions = [{
          __IS__FLAT__OPTIONS: true,
          label: this.t('el.cascader.noMatch'),
          value: '',
          disabled: true
        }];
      }
      this.menu.options = filteredFlatOptions;
    },
    renderFilteredOptionLabel(inputValue, optionsStack) {
      return optionsStack.map((option, index) => {
        const label = option[this.labelKey];
        const keywordIndex = label.toLowerCase().indexOf(inputValue.toLowerCase());
        const labelPart = label.slice(keywordIndex, inputValue.length + keywordIndex);
        const node = keywordIndex > -1 ? this.highlightKeyword(label, labelPart) : label;
        return index === 0 ? node : [' / ', node];
      });
    },
    highlightKeyword(label, keyword) {
      const h = this._c;
      return label.split(keyword)
        .map((node, index) => index === 0 ? node : [
          h('span', { class: { 'el-cascader-menu__item__keyword': true }}, [this._v(keyword)]),
          node
        ]);
    },
    flattenOptions(options, ancestor = []) {
      let flatOptions = [];
      options.forEach((option) => {
        const optionsStack = ancestor.concat(option);
        if (!option[this.childrenKey]) {
          flatOptions.push(optionsStack);
        } else {
          if (this.changeOnSelect) {
            flatOptions.push(optionsStack);
          }
          flatOptions = flatOptions.concat(this.flattenOptions(option[this.childrenKey], optionsStack));
        }
      });
      return flatOptions;
    },
    clearValue(ev) {
      ev.stopPropagation();
      this.handlePick([], true);
    },
    handleClickoutside() {
      this.menuVisible = false;
    },
    handleClick() {
      if (this.disabled) return;
      if (this.filterable) {
        this.menuVisible = true;
        this.$refs.input.$refs.input.focus();
        return;
      }
      this.menuVisible = !this.menuVisible;
    }
  },

  created() {
    this.options.gpuAcceleration = false;
    //因为vue-popper.js里，props中options命名冲突，导致默认gpuAcceleration设置失效，继而在popper的applyStyle默认设置translate3d,使vue默认过渡css冲突，故手动设置
    this.debouncedInputChange = debounce(this.debounce, value => {
      this.handleInputChange(value);
    });
  },

  mounted() {
    this.flatOptions = this.flattenOptions(this.options);
  }
};
</script>
