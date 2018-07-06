<style lang="sass">
    .casitem {
        font-size: 14px;
        padding: 8px 30px 8px 10px;
        position: relative;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        color: #475669;
        line-height: 1.5;
        box-sizing: border-box;
        cursor: pointer;

        &:hover {
            background-color: #e4e8f1;
        }

        &.selected {
            color: #436ba7;

            &:after {
                position: absolute;
                right: 10px;
                font-family: element-icons;
                content: "\E608";
                font-size: 11px;
                -webkit-font-smoothing: antialiased;
                -moz-osx-font-smoothing: grayscale;
            }
        }

        &.custom-selected {
            color: white;
            background: #1d8ce0;

            &:after {
                position: absolute;
                right: 10px;
                font-family: element-icons;
                content: " ";
                font-size: 11px;
                -webkit-font-smoothing: antialiased;
                -moz-osx-font-smoothing: grayscale;
            }
        }

        &.has-children {
            &:before {
                position: absolute;
                height: 100%;
                width: 4px;
                content: '';
                left: 0;
                top: 0;
                opacity: 0;
                background: #436ba7;
            }
            &.full:before {
                opacity: 1;
            }
            &.partion:before {
                opacity: 0.3;
            }

            &:after {
                font-family: element-icons;
                content: "\E606";
                font-size: 12px;
                transform: scale(.8);
                color: #bfcbd9;
                position: absolute;
                right: 10px;
                margin-top: 1px;
            }

            &.active-item {
                background-color: #e4e8f1;
                transition: all 0.32s ease;
            }
        }

        &.has-after {
            &:after {
                font-family: element-icons;
                content: "\E606";
                font-size: 12px;
                transform: scale(.8);
                color: #bfcbd9;
                position: absolute;
                right: 10px;
                margin-top: 1px;
            }
            &.full:after {
                color: #436ba7;
            }
            &.partion:after {
                color: #436ba7;
            }
            &.active-item {
                background-color: #e4e8f1;
                transition: all 0.32s ease;
            }

        }

        &.all-select {
            &:before {
                position: absolute;
                height: 100%;
                width: 4px;
                content: '';
                left: 0;
                top: 0;
                opacity: 0;
                background: #436ba7;
            }
            &.full:before {
                opacity: 1;
            }
            &.partion:before {
                opacity: 0.3;
            }
        }
    }
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
                top: 10px;
            }

            .identification img {
                height: 36px;
                width: 36px;
            }

            .link {
                color: #436ba7;
                cursor: pointer;
            }
            .pull-right{
                float: right;
            }
        }
</style>

<script>
    export default {
        name: 'Caspanel',
        props: {
            data: Object,
            collection: Array,
            selected: Array,
            ismenu: Boolean,
            multiple: [Number, String],
            customItem: {
                type: Function,
                default: null
            }
        },
        data: function() {
            var index = this.selected.findIndex(item => item._id === this.data._id)
            return {
                checked: index != -1, // 为什么不能直接用isInSelected？？？
                activeItem: false
            }
        },
        watch: {
            selected: function(current){
                this.checked = this.isInSelected
            }
        },
        computed: {
            isInSelected: function() {
                var index = this.selected.findIndex(item => item._id === this.data._id)
                return index != -1
            },
            children: function() {
                return this.collection.filter(item => item.parentId === this.data._id)
            },
            hasChildren: function() {
                return this.children.length > 0
            },
            childrenSelectedState: function() {
                var count = 0
                for (var i = 0, len = this.children.length; i < len; i++) {
                    var child = this.children[i]
                    var index = this.selected.findIndex(item => item._id === child._id)
                    if (index != -1) {
                        count++
                    }
                }
                if (count === 0) {
                    return 'zero'
                } else if (count === this.children.length) {
                    return 'full'
                } else {
                    return 'partion'
                }
            },
            hasAfter: function() {
                return this.childrenSelectedState
            }
        },
        methods: {
            handlerMouseEnter: function() {
                if (this.hasChildren) {
                    this.$parent.$emit('activate-subpanel', this.data)
                }

                var children = this.$parent.$children;
                children.forEach(item => item.activeItem = false);
                this.activeItem = true;
            },
            handlerMouseLeave: function() {
                if (this.hasChildren) {
                    this.$parent.$emit('deactivate-subpanel', this.data)
                }
            },
            handlerClick: function() {
                if(!this.ismenu){
                    if(this.multiple == 0){
                        this.checked = true
                        this.$parent.$emit('select', Object.assign({}, this.data))
                    } else{
                        if (this.checked ) {
                            this.checked = false
                            this.$parent.$emit('deselect', Object.assign({}, this.data))
                        } else {
                            this.checked = true
                            this.$parent.$emit('select', Object.assign({}, this.data))
                        }
                    }
                }
            }
        },
        render: function (h) {
            var {hasChildren, multiple, data, isInSelected, childrenSelectedState, activeItem} = this
            const events = {
                on: {
                    click: this.handlerClick,
                    mouseenter: this.handlerMouseEnter,
                    mouseleave: this.handlerMouseLeave
                }
            }
            if(multiple == 0 && this.customItem && !hasChildren){
                return (
                    <li
                        {...events}
                        class={{
                            'casitem': true,
                            'has-after': hasChildren && multiple == 0,
                            'has-children': hasChildren && multiple != 0,
                            'custom-selected': !hasChildren && isInSelected,
                            'active-item': activeItem,
                            'zero': hasChildren && childrenSelectedState == 'zero',
                            'partion': hasChildren && childrenSelectedState == 'partion',
                            'full': hasChildren && childrenSelectedState == 'full'
                        }}>
                        {this.customItem(h, data)}
                    </li>
                )
            } else{
                return (
                    <li
                        {...events}
                        class={{
                            'casitem': true,
                            'has-after': hasChildren && multiple == 0,
                            'has-children': hasChildren && multiple != 0,
                            'selected': !hasChildren && isInSelected,
                            'active-item': activeItem,
                            'zero': hasChildren && childrenSelectedState == 'zero',
                            'partion': hasChildren && childrenSelectedState == 'partion',
                            'full': hasChildren && childrenSelectedState == 'full'
                        }}>
                        {data.label}
                    </li>
                )
            }
        }
    };
</script>