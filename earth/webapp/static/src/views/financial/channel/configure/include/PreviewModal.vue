<template>
    <Modal v-model="show">
        <ModalHeader title="银行限额预览"></ModalHeader>
        <ModalBody>
            <el-table
                :data="dataSource.list"
                class="td-15-padding th-8-15-padding"
                :loading="dataSource.fetching"
                stripe
                border>
                <el-table-column
                    prop="bankName"
                    label="银行名称">
                </el-table-column>
                <el-table-column
                    inline-template
                    label="单笔限额">
                    <div>{{ row.transactionLimitPerTranscation | formatAmount}}</div>
                </el-table-column>
                <el-table-column
                    inline-template
                    label="单日限额">
                    <div>{{ row.transcationLimitPerDay | formatAmount }}</div>
                </el-table-column>
                <el-table-column
                    inline-template
                    label="单月限额">
                    <div>{{ row.transactionLimitPerMonth | formatAmount }}</div>
                </el-table-column>
            </el-table>
        </ModalBody>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import Pagination  from 'mixins/Pagination';
//    import ListPage from 'mixins/ListPage';
    import modalMixin from './modal-mixin';
    import format from 'filters/format';

    export default {
        mixins: [Pagination, modalMixin],
        props: {
            model: {
                default: () => ({})
            },
        },
        data: function() {
            return {
                show: this.value,
                queryConds: Object.assign({}, this.model),
                action: '/paymentchannel/config/edit/bankLimitPreview',
                autoload: false,

                pageConds: {
                    perPageRecordNumber: 9999,
                    pageIndex: 1
                },
            }
        },
        watch: {
            model: function(current) {
                this.queryConds = Object.assign({
                    paymentInstitutionOrdinal: '',
                    outlierChannelName: '',
                    accountSide: ''
                }, current);
            },
            show: function(cur) {
                if (!cur) {
                    this.dataSource.list = [];
                }
                this.$emit('input', cur);
            },
        },
        computed: {
            conditions: function() {
                return Object.assign({}, this.queryConds, this.pageConds);
            }
        },
        filters: {
            formatAmount: function(value) {
                if(!value) {
                    return '--';
                }else {
                    return format.formatMoney(value);
                }
            }
        },
    }
</script>