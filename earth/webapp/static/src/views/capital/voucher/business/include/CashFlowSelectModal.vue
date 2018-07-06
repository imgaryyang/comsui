<style type="sass">

</style>

<template>
    <Modal v-model="show">
        <ModalHeader title="流水匹配">
        </ModalHeader>
        <ModalBody>
            <PagingTable
                :data="cashFlows">
                <el-table-column :width="50" label="" inline-template :context="_self">
                    <el-checkbox @input="handleInput(row.cashFlowUuid, arguments[0])" :value="row.cashFlowUuid == selectCashFlowUuid"></el-checkbox >
                </el-table-column>
                <el-table-column label="支付编号" prop="id">
                </el-table-column>
                <el-table-column label="付款方开户行" :min-width="120" prop="counterBankName">
                </el-table-column>
                <el-table-column label="支付机构流水号" :min-width="120" prop="bankSequenceNo">
                </el-table-column>
                <el-table-column label="支付接口编号" :min-width="120" prop="">
                </el-table-column>
                <el-table-column label="支付机构" :min-width="100" prop="counterAccountName">
                </el-table-column>
                <el-table-column label="支付金额" inline-template >
                    <div> {{ row.transactionAmount | formatMoney }}</div>
                </el-table-column>
                <el-table-column label="发生时间" :min-width="100" inline-template>
                    <div> {{ row.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
                </el-table-column>
                <el-table-column label="支付方式" prop="">
                </el-table-column>
                <el-table-column label="状态" prop="">
                </el-table-column>
            </PagingTable>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button @click="submit" :disabled="selectCashFlowUuid === ''" type="success">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import modalMixin from './modal-mixin';

  	export default {
        mixins: [modalMixin],
        components: {
            PagingTable: require('views/include/PagingTable'),
        },
        props: {
            voucherId: {
              default: null
            },
            cashFlows: {
                type: Array,
                default: () => ([])
            }
        },
        data: function() {
            return {
                selectCashFlowUuid: '',
                show: this.value,
            }
        },
        methods: {
            submit: function() {
                var { voucherId, selectCashFlowUuid } = this;

                ajaxPromise({
                    url: `/voucher/business/detail/connection-cash-flow/${voucherId}`,
                    dataType: 'json',
                    data: {
                        cashFlowUuid: selectCashFlowUuid
                    },
                }).then(resp => {
                    MessageBox.open('流水匹配成功!正在跳转...');
                    setTimeout(function() {
                        location.reload();
                    }, 1000);
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleInput: function(cashFlowUuid, checked) {
                if (checked) {
                    this.selectCashFlowUuid = cashFlowUuid
                } else {
                    this.selectCashFlowUuid = ''
                }
            }
        }
    }

</script>
