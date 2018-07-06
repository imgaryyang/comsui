<style lang="sass">
    @import './multiple-dropdown.scss';
</style>

<template>
    <div>
        <el-dropdown
            class="penalty-interest-dropdown"
            trigger="click"
            menu-align="start"
            :hide-on-click="true">
            <div class="el-dropdown-link">
                <span class="el-dropdown-link-text">{{ value ? value :'请选择' }}</span>
                <i class="el-icon-caret-bottom el-icon--right"></i>
            </div>
            <el-dropdown-menu
                class="penalty-interest-dropdown-menu"
                slot="dropdown">
                <el-dropdown-item 
                    v-for="item in penaltyInterestList">
                    <span class="text" @click="handleSelect(item)">{{item.content}}</span>
                    <span class="edit"><i @click="handleEdit(item)" class="el-icon-edit"></i></span>
                    <span class="delete"><i @click="handleDelete(item)" class="el-icon-delete"></i></span>
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
            penaltyInterestList: {
                type: Array,
                default: () => []
            }
        },
        watch: {
            value: function(current) {
                this.cacheSelectedValue = current;
                this.selectedValue = current;
            },
            selectedValue: function(current) {
                this.$emit('input', current);
            },
        },
        data: function() {
            return {
                selectedValue: this.value,
                cacheSelectedValue: this.value,
            }
        },
        methods: {
            handleSelect: function(item) {
                if (!this.cacheSelectedValue || item.content === this.cacheSelectedValue) {
                    this.selectedValue = item.content;
                    return;
                }

                MessageBox.open(`
                    <div id="confirmModal">
                        <div>
                            <span class="title">原罚息算法：</span><span class="content">${ this.cacheSelectedValue }</span>
                        </div>
                        <div>
                            <span class="title">新罚息算法：</span><span class="content">${ item.content }</span>
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
            handleEdit: function(item) {
                this.$emit('editDropItem', {
                    content: item.content,
                    uuid: item.uuid
                });
            },
            handleDelete: function(item) {
                this.$emit('deleteDropItem', {
                    uuid: item.uuid
                });
            },
            handleCreate: function() {
                this.$emit('createDropItem', {
                    content: '',
                    uuid: ''
                });
            },
        }
    }
</script>
