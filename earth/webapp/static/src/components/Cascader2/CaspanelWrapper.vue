<style lang="sass">
    .caspanels {
        white-space: nowrap;
        background: #fff;
        position: absolute;
        margin: 5px 0;
        z-index: 2;
        border: 1px solid #d1dbe5;
        border-radius: 2px;
        box-shadow: 0 2px 4px rgba(0,0,0,.12), 0 0 6px rgba(0,0,0,.04);
    }
</style>

<script>
    import Caspanel from './Caspanel.vue';

    export default {
        name: 'CaspanelWrapper',
        components: { Caspanel },
        data: function() {
            return {
                visible: false,
                collection: [], // 组件内不能修改
                selected: [], // 组件内不能修改
                actives: []
            }
        },
        methods: {
            handleActivateSubpanel: function(data, index) {
                this.actives.splice(index, this.actives.length, data)
            },
            handleDeactivateSubpanel: function(data, index) {

            },
            handleSelect: function(data, index) {
                var i = this.selected.findIndex(item => item._id === data._id)
                if (i == -1) {
                    this.selected.push(data)
                }
            },
            handleDeselect: function(data, index) {
                var i = this.selected.findIndex(item => item._id === data._id)
                if (i != -1) {
                    this.selected.splice(i, 1)
                }
            }
        },
        render: function(h) {
            var { collection, selected, actives, visible, multiple, customItem } = this
            var panels = this._l([{_id: null}].concat(actives), (panel, index) => {
                var events = {
                    on: {
                        ['activate-subpanel']: data => this.handleActivateSubpanel(data, index),
                        ['deactivate-subpanel']: data => this.handleActivateSubpanel(data, index),
                        ['select']: data => this.$emit('select', data),
                        ['deselect']: data => this.$emit('deselect', data),
                        ['all-item-select']: (data, isAll) => this.$emit('all-item-select', data, isAll),
                        ['all-all-select']: (data, isAll) => this.$emit('all-all-select', data, isAll),
                    }
                }
                return (
                    <Caspanel
                        {...events}
                        multiple={multiple}
                        parentId={panel._id}
                        collection={collection}
                        selected={selected}
                        customItem={customItem}>
                    </Caspanel>
                )
            })

            return (
                <transition>
                    <div
                        v-show={visible}
                        class="caspanels">
                        {panels}
                    </div>
                </transition>
            )
        }
    };
</script>