<style lang="sass">
    @import '~assets/stylesheets/base';

    #positionDetailModal {
        @include min-screen(768px) {
            .modal-dialog {
                width: 800px;
                margin: 30px auto;
            }
        }
        .modal-header {
            background-color: #fff;
            border-bottom: none;
            padding: 15px 30px 0;
            @include clearfix
        }
        .modal-footer {
            border-top: none;
        }
        .modal-body {
            padding: 20px 30px;
        }
        .create-row {
            background: #fff;
            line-height: 28px;
            border: 1px solid #dedede;
            border-top: none;
            text-align: center;
        }
        .edit-button {
            float: right;
            margin-right: 20px;
            border-radius: 2px!important;
            min-width: 60px;
            padding: 7px 9px;
            font-size: 12px;
            border-radius: 4px;
        }
        .close {
            position: absolute;
            top: 5px;
            right: 10px;
            z-index: 2;
        }
        .error-input {
            .el-input__inner {
                border: 1px solid #ff4949
            }
        }
        .conflict-input {
            .el-input__inner {
                border: 1px solid #ff4949
            }
        }
    }

</style>
<template>
    <div>
        <Modal v-model="show" id="positionDetailModal">
            <button type="button" class="close" @click="show = false"><span>×</span></button>
            <ModalHeader>
                <b style="line-height: 28px;">{{model.financialContractNo}}&nbsp;&nbsp;&nbsp;&nbsp;{{model.financialContractName}}</b>
                <el-button class="edit-button" type="primary" v-if="!isEdit" @click="isEdit = true">编辑</el-button>
            </ModalHeader>
            <ModalBody align="left">
                <div>
                    <div class="bd">
                        <el-table
                            :data="showDataList"
                            class="td-15-padding th-8-15-padding"
                            max-height="680"
                            stripe
                            border>
                            <el-table-column label="项目职务" inline-template>
                                <div>
                                    <div v-if="isEdit && !isEditPostion(row.position)">
                                        <el-input
                                            :class="{
                                                'error-input': !row.position && (row.name || row.phone) && isCheck,
                                                'conflict-input': isConfict(row.position) && isCheck
                                            }"
                                            size="small"
                                            v-model="row.position">
                                        </el-input>
                                        <span
                                            v-if="isConfict(row.position) && isCheck"
                                            style="font-size:10px;color:red;position:absolute;">项目职务不能重复</span>
                                    </div>
                                    <div v-else>{{ row.position }}</div>
                                </div>
                            </el-table-column>
                            <el-table-column label="姓名" inline-template>
                                <div>
                                    <div v-if="isEdit">
                                        <el-input size="small" v-model="row.name"></el-input>
                                    </div>
                                    <div v-else>{{ row.name }}</div>
                                </div>
                            </el-table-column>
                            <el-table-column label="联系方式" inline-template>
                                <div>
                                    <div v-if="isEdit">
                                        <el-input size="small" v-model="row.phone"></el-input>
                                    </div>
                                    <div v-else>{{ row.phone }}</div>
                                </div>
                            </el-table-column>
                            <el-table-column label="系统账号" inline-template class="middle">
                                <div>
                                    <a href="#" @click.prevent="openPrincipalModal(row)" v-if="!row.principalName && isEdit">关联+</a>
                                    <a href="#" @click.prevent="openPrincipalModal(row)" v-if="row.principalName && isEdit">{{ row.principalName }}</a>
                                    <a :href="`${ctx}#/system/user/${row.principalId}`" v-if="row.principalName && !isEdit">{{ row.principalName }}</a>
                                </div>
                            </el-table-column>
                        </el-table>
                    </div>
                    <div class="create-row" v-if="isEdit">
                        <a href="#" @click.prevent="handleCreate">新增+</a>
                    </div>
                </div>
            </ModalBody>
            <ModalFooter v-if="isEdit">
                <el-button size="small" @click="handleCancel">取消</el-button>
                <el-button size="small" type="primary" @click="saveChange">保存</el-button>
            </ModalFooter>
        </Modal>
        <PrincipalModal
            v-model="principalModal.visible"
            :model="principalModal.model"
            :principalShowList="principalShowList"
            @submit="handlePrincipalSubmit">
        </PrincipalModal>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import { uniqBy, cloneDeep } from 'lodash';

    export default {
        components: {
            PrincipalModal: require('./PrincipalModal'),
        },
        props: {
            value: Boolean,
            model: {
                type: Object,
                default: () => {}
            },
            principalShowList: {
                type: Array,
                default: () => []
            }
        },
        data: function() {
            return {
                show: this.value,
                showDataList: cloneDeep(this.model.list),
                isEdit: false,
                isCheck: false,

                principalModal: {
                    visible: false,
                    model: {}
                }
            }
        },
        watch: {
            value: function(current) {
                this.show = current;
            },
            show: function(current) {
                this.isEdit = false;
                this.$emit('input', current);
            },
            'model.list': {
                deep: true,
                handler: function(current) {
                    if(current.length){
                        this.showDataList = cloneDeep(current);
                    } else {
                        this.showDataList = ['五维负责人','云信投运对接人' ,'贷前运营' ,'贷前技术' ,'云信信托经理' ,'云信财务' ].map(item => {
                            return { position: item, name: '', phone: '', principalName: '', principalId: '' };
                        })
                    }
                }
            },
        },
        methods: {
            isConfict: function(name) {
                var list = this.showDataList.map(item => item.position);
                var l = list.filter(item => item === name && name !== '').length;
                return l > 1;
            },
            isEditPostion: function(position) {
                return ['五维负责人', '云信投运对接人', '贷前运营', '贷前技术', '云信信托经理', '云信财务'].includes(position);
            },
            handleCreate: function() {
                this.isCheck = false;
                this.showDataList.push({
                    position: '',
                    name: '',
                    phone: '',
                    principalName: '',
                    principalId: '',
                });
            },
            openPrincipalModal: function(row) {
                this.principalModal.visible = true;
                this.principalModal.model = row;
            },
            handlePrincipalSubmit: function(data, model) {
                this.showDataList.filter(item => {
                    if(item.position === model.position) {
                        this.$set(item, 'principalId', data.principalId);
                        this.$set(item, 'principalName', data.name)
                        // this.$nextTick(() => {
                        //     item.principalId = data.principalId;
                        //     item.principalName = data.name;
                        // })
                    }
                })
            },
            saveChange: function() {
                setTimeout(() => {
                    this.isCheck = true;
                }, 0);

                const isEmptyOrConflict = (item) => {
                    let result = false;
                    if(!item.position && (item.name || item.phone) || this.isConfict(item.position)){
                        result = true;
                    }
                    return result;
                }

                var error = this.showDataList.some(item => isEmptyOrConflict(item));
                if(error) return;

                this.showDataList = this.showDataList.filter(item => item.position !== '')
                ajaxPromise({
                    url: `/position/edit-position`,
                    type: 'post',
                    data: {
                        financialContractUuid: this.model.financialContractUuid,
                        positionDetailList: JSON.stringify(this.showDataList),
                    }
                }).then(data => {
                    MessageBox.once('close', () => {
                        this.$emit('submit', this.model.financialContractUuid);
                    });
                    MessageBox.open('保存成功');
                    this.isEdit = false;
                }).catch(message => {
                    MessageBox.open(message);
                })
            },
            handleCancel: function() {
                if(this.model.list.length){
                    this.showDataList = cloneDeep(this.model.list);
                } else {
                    this.showDataList = ['五维负责人','云信投运对接人' ,'贷前运营' ,'贷前技术' ,'云信信托经理' ,'云信财务' ].map(item => {
                        return { position: item, name: '', phone: '', principalName: '', principalId: '' };
                    })
                }
                this.isEdit = false;
            }
        }
    }
</script>