<template>
    <Modal v-model="show">
        <ModalHeader title="退款"></ModalHeader>
        <ModalBody>
            <el-form
                ref="form"
                class="sdf-form"
                style="margin-left: 0;"
                label-width="0">
                <div style="margin: 15px 0;">
                    退款
                    <span class='color-danger'>{{ model.bookingAmount | formatMoney }}</span>
                    至客户账户
                    <span class='color-danger'>{{ model.virtualAccountNo }}</span>
                    ?
                </div>
                <el-form-item>
                    <el-input class="middle" v-model="appendix" placeholder="原因备注（选填）"></el-input>
                </el-form-item>
                <br>
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button type="success" :loading="refunding" @click="refund">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        props: {
            model: {
                required: true,
                default: () => ({})
            },
            journalVoucherUuid: {
                required: true
            },
            value: Boolean
        },
        data: function() {
            return {
                refunding: false,
                show: !!this.value,
                appendix: ''
            }
        },
        watch: {
            show: function(current) {
                this.$emit('input', current);
                if (!current) {
                    this.appendix = '';
                }
            },
            value: function(current) {
                this.show = current;
            }
        },
        methods: {
            refund: function() {
                this.refunding = true;

                ajaxPromise({
                    url: '/capital/customer-account-manage/payment-order-list/refund',
                    type: 'post',
                    data: {
                        journalVoucherUuid: this.journalVoucherUuid,
                        appendix: this.appendix
                    }
                }).then(data => {
                    var { journalVoucherNo, sourcedoumentNo, sourceDocumentNo } = this.model;
                    var htm = `
                        <div style="margin-top: 25px;">余额支付单<span class="color-danger">${journalVoucherNo}</span>退款成功！</div>
                        <div>请及时对凭证<span class="color-danger">${sourcedoumentNo || sourcedoumentNo == '' ? sourcedoumentNo : sourceDocumentNo}</span>进行销账处理！</div>
                    `;

                    MessageBox.once('close', () => {
                        this.$emit('submit');
                    });
                    MessageBox.open(htm);
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.refunding = false;
                    this.show = false;
                });
            }
        }
    }
</script>