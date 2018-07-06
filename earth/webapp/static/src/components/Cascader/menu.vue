<style lang="sass">
  .el-cascader-menu__item {
    display: block;
    .selectpicker-content {
      min-height: 50px;

      .content {
          overflow: hidden;
          background: transparent;
          padding-left: 50px;
          min-width: 300px;
      }

      .content .title {
          font-size: 14px;
          white-space: nowrap;
      }

      .content .subtitle {
          color: #999;
          font-size: 12px;
          white-space: nowrap;
      }

      .identification {
          position: absolute;
      }

      .identification img {
          height: 36px; 
          width: 36px;
      }

      .link {
          color: #436ba7;
          cursor: pointer;
      }
  }
}
</style>
<script>
  export default {
    name: 'ElCascaderMenu',

    data() {
      return {
        inputWidth: 0,
        options: [],
        props: {},
        visible: false,
        activeValue: [],
        value: [],
        expandTrigger: 'click',
        changeOnSelect: false,
        popperClass: '',
        multiple: false,
        allSelectFlag: false,
        filterable: false,
        customItem: false,
        renderItem: null,
      };
    },

    watch: {
      visible(value) {
        if (value) {
          this.activeValue = this.value;
        }
      },
      value: {
        immediate: true,
        handler(value) {
          this.activeValue = value;
        }
      },
      activeValue(val) {
          this.allSelectFlag = Array.isArray(val[val.length - 1]) && Array.isArray(this.activeOptions[this.activeOptions.length - 1 ]) && val[val.length - 1].length === this.activeOptions[this.activeOptions.length - 1 ].filter(item => !item.disabled).length;
      }
    },

    computed: {
      activeOptions: {
        cache: false,
        get() {
          const activeValue = this.activeValue;
          const configurableProps = ['label', 'value', 'children', 'disabled'];

          const formatOptions = options => {
            options.forEach(option => {
              if (option.__IS__FLAT__OPTIONS) return;
              configurableProps.forEach(prop => {
                const value = option[this.props[prop] || prop];
                if (value) option[prop] = value;
              });
              if (Array.isArray(option.children)) {
                formatOptions(option.children);
              }
            });
          };

          const loadActiveOptions = (options, activeOptions = []) => {
            const level = activeOptions.length;
            activeOptions[level] = options;
            let active = activeValue[level];
            if (active) {
              options = options.filter(option => option.value === active)[0];
              if (options && options.children) {
                loadActiveOptions(options.children, activeOptions);
              }
            }
            return activeOptions;
          };

          formatOptions(this.options);
          return loadActiveOptions(this.options);
        }
      }
    },

    methods: {
      select(item, menuIndex) {
        var flag = menuIndex === this.activeOptions.length - 1;
        if (item.__IS__FLAT__OPTIONS) {
          this.activeValue = item.value;
        } else if (menuIndex) {
            if(this.multiple && flag){
              if(!Array.isArray(this.activeValue[menuIndex]))
                this.activeValue.splice(menuIndex,0,[]);
              var exist = this.activeValue[menuIndex].findIndex(function(value){
                return value === item.value;
              });
              if(exist != -1){
                this.activeValue[menuIndex].splice(exist,1);
              }else{
                this.activeValue[menuIndex].push(item.value);
              };
              if(this.activeValue.slice(-1)[0].length === 0)
                this.activeValue.pop();
              this.$emit('pick', this.activeValue,false);
              return;
            }else{
              this.activeValue.splice(menuIndex, this.activeValue.length - 1, item.value);
            }
        } else {
          this.activeValue = [item.value];
        }
        this.$emit('customPick', item,false)
        this.$emit('pick', this.activeValue);
      },
      handleMenuLeave() {
        this.$emit('menuLeave');
      },
      activeItem(item, menuIndex) {
        const len = this.activeOptions.length;

        this.activeValue.splice(menuIndex, len, item.value);
        this.activeOptions.splice(menuIndex + 1, len, item.children);
        if (this.changeOnSelect) {
          this.$emit('pick', this.activeValue, false);
        } else {
          this.$emit('activeItemChange', this.activeValue);
        }
      },
      allSelect(menu, menuIndex) {
        if(!this.multiple || this.filterable)
          return;
        if(!this.allSelectFlag){
          const len = this.activeOptions.length;
          var all = menu.filter(item => !item.disabled).map(item => item.value)
          this.activeValue.splice(menuIndex, len, all);
          this.$emit('pick', this.activeValue,false);
        }else{
          this.activeValue.pop();
          this.$emit('pick', [],false);
        }
        this.allSelectFlag = !this.allSelectFlag;
      },
    },

    render(h) {
      const {
        activeValue,
        activeOptions,
        visible,
        expandTrigger,
        popperClass,
        options,
        allSelectFlag,
        customItem,//+++++
        renderItem
      } = this;
      const menus = this._l(activeOptions, (menu, menuIndex) => {
        let isFlat = false;
        const items = this._l(menu, item => {
          const resource = this.resource;
          let defaultItemFlag = true;
          const events = {
            on: {}
          };
          if (item.__IS__FLAT__OPTIONS) isFlat = true;

          if (!item.disabled) {
            if (item.children) {
              let triggerEvent = {
                click: 'click',
                hover: 'mouseenter'
              }[expandTrigger];
              events.on[triggerEvent] = () => { this.activeItem(item, menuIndex); };
            } else {
              defaultItemFlag = !customItem;
              events.on.click = () => { this.select(item, menuIndex); };
            }
          }

          if (defaultItemFlag) {
            return (
              <li
                class={{
                  'el-cascader-menu__item': true,
                  'el-cascader-menu__item--extensible': item.children,
                  'is-active': item.value === activeValue[menuIndex] || (Array.isArray(activeValue[menuIndex]) && activeValue[menuIndex].includes(item.value)),
                  // 'is-multiple': !item.children && activeValue[menuIndex] && activeValue[menuIndex].includes(item.value),
                  'is-disabled': item.disabled,
                  'remove-after': item.children,
                }}
                {...events}
              >
                <span>{item.label}</span>
              </li>
            );
          }else {
            return (
              <li
                class={{
                  'el-cascader-menu__item': true,
                  '.el-cascader-menu__custom_item--extensible': item.children,
                  'is-active': item.value === activeValue[menuIndex] || (Array.isArray(activeValue[menuIndex]) && activeValue[menuIndex].includes(item.value)),
                  'is-custom': true,
                  'is-disabled': item.disabled,
                  'remove-after': item.children,
                }}
                {...events}
              >
                { renderItem(h,item) }
              </li>
            );
          }
        });
        let menuStyle = {};
        if (isFlat) {
          menuStyle.minWidth = this.inputWidth + 'px';
        };

        const allSelectEvent ={
          on : {}
        };
        allSelectEvent.on.click = () => { this.allSelect(menu,menuIndex); };

        const hasChild = activeOptions[menuIndex].some(item => {
          return item.children;
        });
        return (
          <ul
            class={{
              'el-cascader-menu': true,
              'el-cascader-menu--flexible': isFlat
            }}
            style={menuStyle}>
            <li
              v-show={ items.length > 0 && !this.filterable && this.multiple && menuIndex === activeOptions.length -1 && !hasChild}
              //如果仅在最后一级显示全选，需比较menuIndex与items在options的层级
              class={{
                'el-cascader-menu__item': true,
                // 'is-multiple': allSelectFlag
                'is-active': allSelectFlag
              }}
              {...allSelectEvent}
            >
              <span>全选</span>
            </li>
            <li v-show={items.length == 0 }
                class={{
                'el-cascader-menu__item': true,
                'empty_item': true
                }}>
              <span>暂无数据</span>
            </li>
            {items}
          </ul>
        );
      });
      return (
        <transition name="" on-after-leave={this.handleMenuLeave}>
          <div
            v-show={visible}
            class={[
              'el-cascader-menus',
              popperClass
            ]}
          >
            {menus}
          </div>
        </transition>
      );
    }
  };
</script>