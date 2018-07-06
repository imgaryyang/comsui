<style lang="sass">
    @import '~assets/stylesheets/base.scss';

    .editor-tab {
        .button {
            background: #8fb9f8;
            border-color: #8fb9f8;
            border-radius: 2px;
            display: inline-block;
            color: #fff;
            min-width: 48px;
            text-align: center;
            margin-bottom: 1px;
        }

        .menu {
            float: left;

            .el-dropdown-link {
                cursor: pointer;
                float: left;
                padding: 0 10px;
                color: #fff;
                margin-right: 1px;
                border-radius: 4px 0 0 0;
            }
        }

        .tabs {
            margin: 0;
            padding: 0;
            list-style: none;
            float: left;

            li {
                position: relative;
                color: #fff;
                float: left;
                margin-left: 1px;

                &:first-child {
                    margin-left: 0;
                }

                &:last-child {
                    span {
                        padding-right: 10px;
                    }
                }

                &.active {
                    span {
                        border-bottom: 1px solid #282d33;
                        background-color: #282d33;
                    }
                }

                span {
                    overflow: hidden;
                    text-overflow: ellipsis;
                    user-select: none;
                    cursor: default;
                    border-radius: 2px 2px 0 0;
                    display: block;
                    background-color: #51575d;
                    padding: 0 10px;
                    padding-right: 30px;
                    white-space: nowrap;
                }

                .el-icon-close {
                    position: absolute;
                    padding: 5px;
                    right: 3px;
                    top: 5px;
                    font-size: 12px;
                    z-index: 2;
                    @include scale(0.7);
                }
            }
        }
    }
</style>

<template>
    <div class="editor-tab">
        <el-dropdown class="menu" trigger="click" @command="handleMenuCommand">
            <span class="el-dropdown-link button">菜单</span>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="library">添加类库</el-dropdown-item>
                <!-- <el-dropdown-item command="usage">使用文档</el-dropdown-item> -->
            </el-dropdown-menu>
        </el-dropdown>
        <ul class="tabs" coordinate>
            <li v-for="(view, index) in views" :class="{ item: true, active: view.selected }">
                <span @click="handleSelect(view)" :title="view.title">{{view.title || 'untitled'}}</span>
                <i class="el-icon-close" @click="handleClose(view)"></i>
            </li>
            <li>
                <span @click="handleCreate">
                    <i class="el-icon-plus"></i>
                </span>
            </li>
        </ul>
        <div class="btns pull-right">
            <a href="javascript:void 0" class="button" @click="handleRunScript">{{ canSave?'保存' :'运行'}}</a>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            views: Array,
            model:Object
        },
        data(){
            return{
                canSave: false
            }
        },
        computed:{
            canSave: function(){
                return this.model && this.model.successCompiled
            }
        },
        watch: {
            views: function() {
                this.$nextTick(() => {
                    this.coordinate();
                });
            },
        },
        methods: {
            handleClose: function(view) {
                this.$emit('close', view);
            },
            handleSelect: function(view) {
                this.$emit('select', view);
                this.$nextTick(() => {
                    this.coordinate();
                });
            },
            handleCreate: function() {
                this.$emit('create');
            },
            handleMenuCommand: function(command) {
                this.$emit('command', command);
            },
            handleRunScript: function(){
                this.canSave ? this.$emit('save') : this.$emit('run')
                // this.$emit('run')
            },
            coordinate: function() {
                var $el = $(this.$el);
                var $items = $el.find('.item');
                var itemLength = $items.length;
                var remainWidth = $el.parent().width() - 49 - 49 - 35;
                var activeItemWidth = 90 + 1;
                var itemMaxWidth = 90 + 1;
                var inactiveItemWidth = Math.min((remainWidth - activeItemWidth) / (itemLength - 1), itemMaxWidth);
                $items.each((index, item) => {
                    var $item = $(item);
                    $item.hasClass('active') ? $item.css('width', activeItemWidth - 1) : $item.css('width', inactiveItemWidth - 1);
                });
            }
        },
    }
</script>
