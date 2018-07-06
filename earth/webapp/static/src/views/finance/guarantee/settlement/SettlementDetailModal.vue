<style lang="sass">
    #settlementOrderModal {
        .el-row {
            margin: 10px 0;

            .label {
                color: #999;
                font-weight: normal;
                font-size: 14px;
            }
        }
    }
</style>

<template>
    <Modal id="settlementOrderModal" v-model="show">
        <ModalHeader title="清算单详情"></ModalHeader>
        <ModalBody align="left">
            <div v-if="!error">
                <el-row>
                    <el-col :span="12">
                        <span class="label">清算单号</span>
                        <span class="value">{{ settlementOrder.settleOrderNo }}</span>
                    </el-col>
                    <el-col :span="12">
                        <span class="label">还款编号</span>
                        <span class="value">{{assetSet.singleLoanContractNo}}</span>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <span class="label">贷款合同编号</span>
                        <span class="value">{{assetSet.contract.contractNo}}</span>
                    </el-col>
                    <el-col :span="12">
                        <span class="label">商户还款计划编号</span>
                        <span class="value">{{assetSet.outerRepaymentPlanNo}}</span>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <span class="label">商户编号</span>
                        <span class="value">{{assetSet.contract.app.appId}}</span>
                    </el-col>
                    <el-col :span="12">
                        <span class="label">应还日期</span>
                        <span class="value">{{assetSet.assetRecycleDate | formatDate }}</span>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <span class="label">商户名称</span>
                        <span class="value">{{assetSet.contract.app.name}}</span>
                    </el-col>
                    <el-col :span="12">
                        <span class="label">发生时间</span>
                        <span class="value">{{settlementOrder.lastModifyTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</span>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <span class="label">打款账号</span>
                        <span class="value"></span>
                    </el-col>
                    <el-col :span="12">
                        <span class="label">打款开户行</span>
                        <span class="value"></span>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <span class="label">本息金额</span>
                        <span class="value">{{ assetSet.assetInitialValue | formatMoney }}</span>
                    </el-col>
                    <el-col :span="12">
                        <span class="label">打款银行账户名称</span>
                        <span class="value"></span>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <span class="label">补差状态</span>
                        <span class="value">{{assetSet.guaranteeStatusMsg}}</span>
                    </el-col>
                    <el-col :span="12">
                        <span class="label">清算金额</span>
                        <span class="value">{{ settlementOrder.settlementAmount }}</span>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <span class="label">清算状态</span>
                        <span class="value">{{assetSet.settlementStatusMsg}}</span>
                    </el-col>
                   <!--  <el-col :span="12">
                        <span class="label">清算状态</span>
                        <span class="value">{{settlementOrder.assetSet.settlementStatusMsg}}</span>
                    </el-col> -->
                </el-row>
            </div>
            <div
                v-else 
                style="margin-top: 30px;">
                {{ error }}
            </div>
        </ModalBody>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';

    export default {
        props: {
            settlementId: [String, Number],
            value: Boolean
        },
        watch: {
            value: function(current) {
                this.show = current;
            },
            show: function(current) {
                this.$emit('input', current);
                if (current) {
                    this.fetch();
                }
            }
        },
        data: function() {
            return {
                fetching: false,
                show: this.value,
                error: '',
                settlementOrder: {
                },
                assetSet: {
                  contract: {
                    app: {}
                  }
                },
            }
        },
        methods: {
            fetch: function() {
                if (!this.settlementId) return;

                this.fetching = true;
                ajaxPromise({
                    url: `/settlement-order/settle/${ this.settlementId }/detail`
                }).then(data => {
                    this.settlementOrder = data.settlementOrder;
                    this.assetSet = data.assetSet;
                    this.error = ''
                }).catch(message => {
                    this.error = message;
                }).then(() => {
                    this.fetching = false;
                });
            }
        }
    }
</script>