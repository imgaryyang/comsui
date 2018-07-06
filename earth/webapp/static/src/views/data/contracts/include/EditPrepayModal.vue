<style lang="sass">
    @import '~assets/stylesheets/base';

    .edit-prepay-modal {
        @include min-screen(768px) {
            .modal-dialog {
                width: 60%;
                margin: 30px auto;
            }
        }
    }
</style>
<template>
	 <Modal v-model="show" class="edit-prepay-modal">
        <div v-loading="fetching">
            <ModalHeader title="提前还款">
            </ModalHeader>
            <ModalBody align="left">
                <div v-if="error" style="text-align: center; margin-top: 35px;">
                    {{ error }}
                </div>
                <div v-else>
                    <div class="block">
                        <h5 class="hd">提前还款日期</h5>
                        <div class="bd">
                            <DateTimePicker
                                v-model="currentModel.assetRecycleDate"
                                :startDate="minRecycleDate"
                                :endDate="maxRecycleDate"
                                style="width:140px"
                                size="middle">
                            </DateTimePicker>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">可提前还款计划</h5>
                        <div class="bd">
                            <PagingTable
                                :loading="assetInfoFetching"
                                :data="assetInfoModelList">
                                <el-table-column label="还款编号" inline-template>
                                    <a :href="`${ctx}#/finance/assets/${row.assetSetUuid}/detail`">{{ row.repaymentPlanCode }}</a>
                                </el-table-column>
                                <el-table-column label="计划还款日期" inline-template>
                                    <div>{{ row.assetRecycleDate | formatDate }}</div>
                                </el-table-column>
                                <el-table-column label="计划还款本金" inline-template>
                                    <div>{{ row.assetPrincipal | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column label="计划还款利息" inline-template>
                                    <div>{{ row.assetInterest | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column label="贷款服务费" inline-template>
                                    <div>{{ row.serviceCharge | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column label="技术维护费" inline-template>
                                    <div>{{ row.maintenanceCharge | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column label="其他费用" inline-template>
                                    <div>{{ row.otherCharge | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column label="计划还款金额" inline-template>
                                    <div>{{ row.assetAmount | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column label="计划状态" prop="planStatus">
                                </el-table-column>
                                <el-table-column label="还款状态" prop="paymentStatus">
                                </el-table-column>
                            </PagingTable>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">提前还款金额({{ currentModel.checkRule | CHECK_RULE_MSG_ZH_CN }})</h5>
                        <div class="bd">
                            <el-table
                                :data="[currentModel]"
                                class="td-15-padding th-8-15-padding no-th-border"
                                stripe
                                border>
                                <el-table-column label="未偿本金" inline-template>
                                    <div>{{ currentModel.assetPrincipal | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column label="还款利息" inline-template>
                                    <div>
                                        <el-input class="small" v-model="currentModel.assetInterest"></el-input>
                                    </div>
                                </el-table-column>
                                <el-table-column label="贷款服务费" inline-template>
                                    <div>
                                        <el-input class="small" v-model="currentModel.serviceCharge"></el-input>
                                    </div>
                                </el-table-column>
                                <el-table-column label="技术维护费" inline-template>
                                    <div>
                                        <el-input class="small" v-model="currentModel.maintenanceCharge"></el-input>
                                    </div>
                                </el-table-column>
                                <el-table-column label="其他费用" inline-template>
                                    <div>
                                        <el-input class="small" v-model="currentModel.otherCharge"></el-input>
                                    </div>
                                </el-table-column>
                                <el-table-column label="提前还款总额" inline-template>
                                    <div :class="{'color-danger': errorAmount}">
                                        {{ totalAmount | formatMoney }}<br/>
                                        ({{ multiple }}*未偿本金)
                                    </div>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                    <div style="display: inline-block;line-height: 34px;margin-top: 15px;">
                        <span style="float: left;margin-right: 10px;">备注:</span>
                        <el-input class="middle" v-model="currentModel.comment" style="width: 200px;float: left"></el-input>
                    </div>
                </div>
            </ModalBody>
            <ModalFooter>
                <el-button @click="show = false">取消</el-button>
                <el-button v-if="!error" @click="submit" type="success" :loading="submitting" :disabled="errorAmount">确定</el-button>
            </ModalFooter>
        </div>
    </Modal>
</template>
<script>
	import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import modalMixin from './modal-mixin';
    import { REGEXPS } from 'src/validators';
    import format from 'filters/format';
    import Enum from 'assets/javascripts/enum';

    //校验规则
    const CHECK_RULE_MSG = new Enum([
            {0 : '未尝本金<=提前还款总额<=1.24*未尝本金'}
        ]);

    export default {
    	mixins: [modalMixin],
        components: {
            PagingTable: require('views/include/PagingTable'),
        },
    	props: {
            contractId: {
                required: true
            }
    	},
    	data: function() {
    		return {
    			show: this.value,
                submitting: false,
                fetching: false,
                assetInfoFetching: false,

                error: '',
                assetInfoModelList: [],
                minRecycleDate: '',
                maxRecycleDate: '',

                currentModel: {
                    assetRecycleDate: '',
                    assetInitialValue: '',
                    assetPrincipal: '',
                    assetInterest: '',
                    serviceCharge: '',
                    maintenanceCharge: '',
                    otherCharge: '',
                    comment: '',
                    checkRule: 0,
                }
    		}
    	},
        watch: {
            show: function(current) {
                if (current) {
                    this.fetch();
                }else {
                    this.minRecycleDate = '',
                    this.maxRecycleDate = '',
                    this.assetInfoModelList = [];
                    this.currentModel = {
                        assetRecycleDate: '',
                        assetInitialValue: '',
                        assetPrincipal: '',
                        assetInterest: '',
                        serviceCharge: '',
                        maintenanceCharge: '',
                        otherCharge: '',
                        comment: '',
                        checkRule: 0,
                    }
                }
            },
            'currentModel.assetRecycleDate': function(current) {
                if (!current || this.error) return;
                this.searchAssetInfo();
            }
        },
        computed: {
            errorAmount: function() {
                var { parseMoney, currentModel, totalAmount } = this;
                if (currentModel.checkRule == 0) {
                    return !(parseMoney(currentModel.assetPrincipal) <= totalAmount && totalAmount <= 1.24 * parseMoney(currentModel.assetPrincipal)) ;
                }
                return false;
            },
            totalAmount: function() {
                var sum = 0;
                var { currentModel, parseMoney } = this;
                sum = parseMoney(currentModel.assetPrincipal) + parseMoney(currentModel.assetInterest) + parseMoney(currentModel.serviceCharge) + parseMoney(currentModel.maintenanceCharge) + parseMoney(currentModel.otherCharge);
                return sum;
            },
            multiple: function() {
                var value = this.totalAmount / parseFloat(this.currentModel.assetPrincipal);
                return value == NaN ? 0 : value.toFixed(2);
            },
        },
        filters: {
            CHECK_RULE_MSG_ZH_CN: Enum.filter(CHECK_RULE_MSG),
        },
    	methods: {
            parseMoney: function(value) {
                return isNaN(parseFloat(value)) ? 0 : parseFloat(value);
            },
            searchAssetInfo: function() {
                var { currentModel, contractId} = this;
                this.assetInfoFetching = true;

                const getAssetAmount = function(item) {
                    return item.assetPrincipal + item.assetInterest + item.serviceCharge + item.maintenanceCharge + item.otherCharge ;
                };
                ajaxPromise({
                    url: `/contracts/prepayment/search-asset`,
                    data: {
                        contractId: contractId,
                        assetRecycleDate: currentModel.assetRecycleDate
                    },
                }).then(data => {
                    var map = data.assetInfoAndPrincipalMap;

                    this.currentModel.assetPrincipal = map.assetPrincipal;
                    // this.currentModel.assetInfoModelListJson = map.assetInfoModelListJson;
                    this.assetInfoModelList = JSON.parse(map.assetInfoModelListJson);
                    this.assetInfoModelList.forEach(item => {
                        item.assetAmount = getAssetAmount(item);
                    });
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.assetInfoFetching = false;
                });
            },
            fetch: function() {
                this.fetching = true;
                ajaxPromise({
                    url: `/contracts/prepayment/check`,
                    data: {
                        contractId: this.contractId
                    },
                    type: 'post'
                }).then(data => {
                    this.minRecycleDate = format.formatDate(data.dateMap.minRecycleDate);
                    this.maxRecycleDate = format.formatDate(data.dateMap.maxRecycleDate);
                    this.currentModel.assetRecycleDate = this.minRecycleDate;

                    this.error = '';
                }).catch(message => {
                    this.error = message.toString();
                }).then(() => {
                    this.fetching = false;
                });
            },
            submit: function() {
                this.submitting = true;
                this.currentModel.assetInitialValue = this.totalAmount.toFixed(2);

                ajaxPromise({
                    url: `/contracts/prepayment/save`,
                    data: {
                    	contractId: this.contractId,
                        ...this.currentModel
                    },
                    type: 'post'
                }).then(data => {
                    MessageBox.open('提交成功！');
                    MessageBox.once('close', () => {
                        this.show = false;
                        setTimeout(() => {
                            this.$emit('submit');
                        }, 500);
                    });
                }).catch(message => {
                    MessageBox.open(message)
                }).then(() => {
                    this.submitting = false;
                });
            }
    	}
    }
</script>
