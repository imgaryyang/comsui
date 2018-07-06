<template>
  <li
    @mouseenter="hoverItem"
    @click.stop="selectOptionClick"
    class="el-select-dropdown__item"
    v-show="visible"
    :class="{ 
      'selected': itemSelected, 
      'hover': parent.hoverIndex === index
    }">
    <slot>
      <span>{{ label }}</span>
    </slot>
  </li>
</template>

<script type="text/babel">
  import Emitter from 'element-ui/lib/mixins/emitter';

  export default {
    mixins: [Emitter],

    name: 'el-select-all-option',

    componentName: 'option',

    props: {
      label: {
        type: String,
        default: '全选'
      },
      options: {
        type: [Array, Object],
        required: true
      }
    },

    data() {
      return {
        index: -999,
        visible: true,
        selected: false,
        hitState: false
      };
    },

    computed: {
      parent() {
        let result = this.$parent;
        while (!result.isSelect) {
          result = result.$parent;
        }
        return result;
      },

      size() {
        if (Array.isArray(this.options)) {
          return this.options.length;
        } else {
          return Object.keys(this.options).length;
        }
      },

      itemSelected() {
        if (Array.isArray(this.parent.selected)) {
          return this.parent.value.length === this.size;
        }
      }
    },

    methods: {
      hoverItem() {
        this.parent.hoverIndex = this.index;
      },

      selectOptionClick() {
        this.dispatch('select', 'handleSelectAllOptionClick', !this.itemSelected);
      },
    },

    created() {
      // this.$on('queryChange', this.queryChange);
    },

    beforeDestroy() {
      this.dispatch('select', 'onOptionDestroy', this);
    }
  };
</script>
