<style lang="sass">
    @import '~assets/stylesheets/base';
    #principalModal {
        @include min-screen(768px) {
            .modal-dialog {
                width: 400px;
                margin: 80px auto;
            }
        }
    }
    .principal-table {
        .el-table__header-wrapper {
            .el-checkbox {
                display: none;
            }
        }
    }
</style>
<template>
    <Modal v-model="show" id="principalModal">
        <!-- <ModalHeader title="关联系统账号">
        </ModalHeader> -->
        <div class="query-area" style="padding: 0 5px 0 15px">

            <el-form class="sdf-form sdf-query-form":inline="true">
                <el-form-item>
                    <el-input
                            placeholder="请输入用户名或持有者"
                            size="small"
                            class="combo-query-box"
                            v-model="searchInp">
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button  type="primary" size="small" @click="seachList">查询</el-button>
                </el-form-item>
            </el-form>
        </div>
        <ModalBody align="left">
            <el-table
                :data="filterPrincipalShowList"
                class="td-15-padding th-8-15-padding principal-table"
                @select="handleSelect"
                ref="selectionTable"
                style="width: 100%"
                max-height="480"
                stripe
                border>
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column
                    prop="name"
                    label="用户名">
                </el-table-column>
                <el-table-column
                    prop="realName"
                    label="持有者">
                </el-table-column>
            </el-table>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button @click="save" type="primary">保存</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        props: {
            value: Boolean,
            model: {
                type: Object,
                default: () => ({})
            },
            principalShowList: {
                type: Array,
                default: () => []
            }
        },
        watch: {
            value: function(cur) {
                this.show = cur;
                if(cur) {
                    this.principalShowList.forEach(item => {
                        if(item.principalId === this.model.principalId) {
                            this.$nextTick(() => {
                                this.$refs.selectionTable.toggleRowSelection(item, true);
                            })
                        }

                    })
                    this.filterPrincipalShowList = this.principalShowList.slice();
                }
                this.searchInp = "";
            },
            show: function(cur) {
                this.$emit('input', cur);
                this.$refs.selectionTable.clearSelection();
            }
        },
        data: function() {
            return {
                show: this.value,

                searchInp: '',
                filterPrincipalShowList: this.principalShowList.slice(),

                selectPrincipal: {},

            }
        },
        methods: {
            handleSelect: function(selection, row) {
                this.$refs.selectionTable.clearSelection();
                if(selection.length) {
                    this.$refs.selectionTable.toggleRowSelection(row);
                    this.selectPrincipal = row;
                } else {
                    this.selectPrincipal = {
                        principalId: '',
                        name: '',
                        realName: '',
                    };
                }
            },
            save: function() {
                this.$emit('submit', this.selectPrincipal, this.model);
                this.show = false;
            },
            seachList: function () {
                var arrList = [];
                this.principalShowList.forEach( item =>{
                    if(item.name.indexOf(this.searchInp) === -1){
                        if(typeof item.realName!=='undefined'){
                            if(item.realName.indexOf(this.searchInp) !== -1){
                                arrList.push(item);
                            }
                        }
                    }
                    else {
                        arrList.push(item);
                    }

                })
                this.filterPrincipalShowList = arrList;
            }
        }
    }
</script>