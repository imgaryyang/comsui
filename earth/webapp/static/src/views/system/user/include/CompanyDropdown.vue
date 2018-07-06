<style lang="sass">
    @import './multiple-dropdown.scss';
</style>

<template>
    <el-dropdown
        ref="companyDropdown"
        class="company-dropdown"
        trigger="click"
        menu-align="start"
        @command="handleCommand"
        :hide-on-click="true">
        <div class="el-dropdown-link">
            <span class="el-dropdown-link-text">{{ selectedLabel ? selectedLabel : '请选择' }}</span>
            <i class="el-icon-caret-bottom el-icon--right"></i>
        </div>
        <el-dropdown-menu
            class="company-dropdown-menu"
            slot="dropdown">
            <div class="scroll">
                <el-dropdown-item 
                    v-for="item in companyList"
                    :class="{selected: item.id === selectedValue}">
                    <span class="text" @click="handleSelect(item)">{{item.fullName}}</span>
                    <span class="delete" @click="handleDelete(item)"><i class="el-icon-delete"></i></span>
                </el-dropdown-item>
            </div>

            <el-dropdown-item 
                command="add"
                style="background: rgb(239, 239, 239); padding-top: 5px; padding-bottom: 5px; margin-bottom: -7px;" >
                <div class="create-box">
                    <input type="text" v-model.trim="createCompanyName">
                    <el-button size="small" type="primary" @click="handleCreateCompany">新增</el-button>
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
                createCompanyName: '',
                companyList: []
            }
        },
        computed: {
            selected: function() {
                var index = this.companyList.findIndex(item => item.id == this.selectedValue);
                return index == -1 ? null : this.companyList[index];
            },
            selectedLabel: function() {
                return this.selected ? this.selected.fullName : '';
            }
        },
        beforeMount: function(){
            this.fetchCompanyList();
        },
        methods: {
            handleDelete: function(item) {
                ajaxPromise({
                    url: `/del-company`,
                    data: {
                        fullName: item.fullName
                    }
                }).then(data => {
                    this.fetchCompanyList();
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleSelect: function(item) {
                this.selectedValue = item.id;
            },
            handleCreateCompany: function() {
                if(this.createCompanyName == '') return;

                ajaxPromise({
                    url: `/create-company`,
                    data: {
                        fullName: this.createCompanyName
                    }
                }).then(data => {
                    this.createCompanyName = '';
                    this.fetchCompanyList();
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            fetchCompanyList: function() {
                ajaxPromise({
                    url: `/getCompanyList`
                }).then(data => {
                    this.companyList = data.companyList;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleCommand: function(command) {
               if (command === 'add') {
                    this.$refs.companyDropdown.visible = true;
               }
            }
        }
    }
</script>
