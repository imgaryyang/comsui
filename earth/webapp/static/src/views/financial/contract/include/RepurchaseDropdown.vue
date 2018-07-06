<style lang="sass">
    @import './multiple-dropdown.scss';
</style>

<template>
    <div style="margin:auto">
        <el-dropdown
            class="repurchase-dropdown"
            trigger="click"
            menu-align="start"
            :hide-on-click="false">
            <div class="el-dropdown-link">
                <span class="el-dropdown-link-text">{{ value ? value : '空' }}</span>
                <i class="el-icon-caret-bottom el-icon--right"></i>
            </div>
            <el-dropdown-menu
                class="repurchase-dropdown-menu"
                slot="dropdown">
                <el-dropdown-item>
                    <span class="text" @click="handleSelect({content: ''})">空</span>
                </el-dropdown-item>
                <el-dropdown-item 
                    v-for="item in repurchaseList">
                    <span class="text" @click="handleSelect(item)">{{item.content}}</span>
                    <span class="edit" @click="handleEdit(item)"><i class="el-icon-edit"></i></span>
                    <span class="delete" @click="handleDelete(item)"><i class="el-icon-delete"></i></span>
                </el-dropdown-item>

                <el-dropdown-item 
                    style="background: rgb(239, 239, 239); padding-top: 2px; padding-bottom: 2px; margin-bottom: -7px; text-align: center;">
                    <el-button size="small" type="text" @click="handleCreate">+新增</el-button>
                </el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    export default {
        props: {
            value: [String, Number],
            repurchaseList: {
                type: Array,
                default: () => []
            },
            keyword: {
                default: '回购'
            }
        },
        watch: {
            value: function(current) {
                this.cacheSelectedValue = current;
                this.selectedValue = current;
            },
            selectedValue: function(cur) {
                this.$emit('input', cur);
            }
        },
        data: function() {
            return {
                selectedValue: this.value,
                cacheSelectedValue: this.value,
            }
        },
        methods: {
            handleEdit: function(item) {
                this.$emit('editDropItem', {
                    content: item.content,
                    uuid: item.uuid
                }, this.keyword);
            },
            handleDelete: function(item) {
                this.$emit('deleteDropItem', {
                    uuid: item.uuid
                });
            },
            handleSelect: function(item) {
                if (!this.cacheSelectedValue || this.cacheSelectedValue === item.content) {
                    this.selectedValue = item.content;
                    return;
                }
                MessageBox.open(`
                    <div id="confirmModal">
                        <div>
                            <span class="title">原${ this.keyword }算法：</span><span class="content">${ this.cacheSelectedValue } </span>
                        </div>
                        <div>
                            <span class="title">新${ this.keyword }算法：</span><span class="content">${ item.content }</span>
                        </div>
                    </div>
                `, null, [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        this.selectedValue = item.content;
                        MessageBox.close();
                    }
                }]);

            },
            handleCreate: function() {
                this.$emit('createDropItem', {
                    content: '',
                    uuid: ''
                }, this.keyword);
            },
        }
    }
</script>
