<style lang="sass">
    @import './multiple-dropdown.scss';
</style>

<template>
    <el-dropdown
        ref="groupDropdown"
        class="group-dropdown"
        trigger="click"
        menu-align="start"
        @command="handleCommand"
        :hide-on-click="true">
        <div class="el-dropdown-link">
            <span class="el-dropdown-link-text">{{ selectedLabel ? selectedLabel : '请选择' }}</span>
            <i class="el-icon-caret-bottom el-icon--right"></i>
        </div>
        <el-dropdown-menu
            class="group-dropdown-menu"
            slot="dropdown">
            <div class="scroll">
                <el-dropdown-item 
                    v-for="item in groupList"
                    :class="{selected: item.id === selectedValue}">
                    <span class="text" @click="handleSelect(item)">{{item.groupName}}</span>
                    <span class="delete" @click="handleDelete(item)"><i class="el-icon-delete"></i></span>
                </el-dropdown-item>
            </div>

            <el-dropdown-item 
                command="add"
                style="background: rgb(239, 239, 239); padding-top: 5px; padding-bottom: 5px; margin-bottom: -7px;">
                <div class="create-box">
                    <input type="text" v-model="createGroupName">
                    <el-button size="small" type="primary" @click="handleCreateGroup">新增</el-button>
                </div>
            </el-dropdown-item>
        </el-dropdown-menu>
    </el-dropdown>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    export default {
        props: {
            value: [String, Number],
        },
        watch: {
            value: function(cur) {
                this.selectedValue = cur;
            },
            selectedValue: function(cur) {
                this.$emit('input', cur);
            }
        },
        data: function() {
            return {
                selectedValue: this.value,
                createGroupName: '',
                groupList: []
            }
        },
        computed: {
            selected: function() {
                var index = this.groupList.findIndex(item => item.id == this.selectedValue);
                return index == -1 ? null : this.groupList[index];
            },
            selectedLabel: function() {
                return this.selected ? this.selected.groupName : '';
            }
        },
        beforeMount: function(){
            this.fetchGroupList();
        },
        methods: {
            handleDelete: function(item) {
                ajaxPromise({
                    url: `/del-group`,
                    data: {
                        groupName: item.groupName
                    }
                }).then(data => {
                    this.fetchGroupList();
                }).catch(message => {
                    MessageBox.open(message);
                })
            },
            handleSelect: function(item) {
                this.selectedValue = item.id;
            },
            handleCreateGroup: function() {
                if(this.createGroupName == '') return;

                ajaxPromise({
                    url: `/create-group`,
                    data: {
                        groupName: this.createGroupName
                    }
                }).then(data => {
                    this.createGroupName = '';
                    this.fetchGroupList();
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            fetchGroupList: function() {
                ajaxPromise({
                    url: `/getGroupList`
                }).then(data => {
                    this.groupList = data.groupList;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleCommand: function(command) {
               if (command === 'add') {
                    this.$refs.groupDropdown.visible = true;
               }
            }
        }
    }
</script>
