<style lang="sass">
    @import '~assets/stylesheets/base';

    .first-overdue-rate-history-modal {

        @include min-screen(768px) {
            .modal-dialog {
                width: 85%;
                margin: 30px auto;
            }
        }
    }
</style>

<template>
    <Modal v-model="show" class="first-overdue-rate-history-modal">
        <ModalHeader title="首逾率更新历史">
        </ModalHeader>
        <ModalBody>
            <div class="row-layout-detail">
                <div class="block">
                    <div class="bd">
                        <el-table
                            :data="dataSource.list"
                            class="td-15-padding th-8-15-padding no-th-border"
                            v-loading="dataSource.fetching"
                            stripe
                            border>
                           <el-table-column
                                prop="contractNo"
                                :label="$utils.locale('financialContract.no')">
                            </el-table-column>
                            <el-table-column 
                                prop="contractName"
                                :label="$utils.locale('financialContract.name')">
                            </el-table-column>
                            <el-table-column
                                inline-template
                                label="日期">
                                <span>{{ row.date | formatDate }}</span>
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="首逾率1(本金包含宽限日)">
                                <el-popover trigger="hover" :disabled="rate1 == undefined">
                                    <div>
                                        ({{ row.remainingPrincipalValue1 | formatMoney }}(未还总额)-{{ row.offlinePrincipalValue1 | formatMoney }}(线下还款)/{{ row.assetPrincipalValue1 | formatMoney }}(应还总额))
                                    </div>
                                    <span slot="reference">{{ row.rate1 | formatPercent }}</span>
                                </el-popover>
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="首逾率2(本息包含宽限日)">
                                <el-popover trigger="hover" :disabled="rate2 == undefined">
                                    <div>
                                        ({{ row.remainingPrincipalValue1 + row.remainingInterestValue1 | formatMoney }}(未还总额)-{{ row.offlinePrincipalValue1 + row.offlineInterestValue1 | formatMoney }}(线下还款)/{{ row.assetPrincipalValue1 + row.assetInterestValue1 | formatMoney }}(应还总额))
                                    </div>
                                    <span slot="reference">{{ row.rate2 | formatPercent }}</span>
                                </el-popover>
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="首逾率3(本金不包含宽限日)">
                                <el-popover trigger="hover" :disabled="rate3 == undefined">
                                    <div>
                                        ({{ row.remainingPrincipalValue | formatMoney }}(未还总额)-{{ row.offlinePrincipalValue | formatMoney }}(线下还款)/{{ row.assetPrincipalValue | formatMoney }}(应还总额))
                                    </div>
                                    <span slot="reference">{{ row.rate3 | formatPercent }}</span>
                                </el-popover>
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="首逾率4(本息不包含宽限日)">
                                <el-popover trigger="hover" :disabled="rate4 == undefined">
                                    <div>
                                        ({{ row.remainingPrincipalValue + row.remainingInterestValue | formatMoney }}(未还总额)-{{ row.offlinePrincipalValue + row.offlineInterestValue | formatMoney }}(线下还款)/{{ row.assetPrincipalValue + row.assetInterestValue | formatMoney }}(应还总额))
                                    </div>
                                    <span slot="reference">{{ row.rate4 | formatPercent}}</span>
                                </el-popover>
                            </el-table-column>
                            <el-table-column label="更新人" prop="userName"></el-table-column>
                            <el-table-column label="操作时间" prop="lastModifyTime" inline-template>
                                <span>{{ row.lastModifyTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</span>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
            </div>
        </ModalBody>
    </Modal>
</template>

<script>
    import modalMixin from './modal-mixin';
    import Pagination from 'mixins/Pagination';

    export default {
        mixins: [modalMixin,Pagination],
        components: {
            QueryTable: require('views/include/QueryTable')
        },
        props: {
            model: {
                required: true,
                type: Object
            }
        },
        data: function() {
            return {
                action: `/firstOverdueRate/showHistory`,
                autoload: false,
                show: this.value,

                queryConds: Object.assign({
                    financialContractUuids: '',
                    date: '',
                }, this.model),
                
            }
        },
        computed: {
            conditions: function() {
                return Object.assign({}, this.queryConds)
            }
        },
        watch: {
            show: function(current) {
                console.log(current);
                if (current) {
                    this.fetch();
                }
            },
            model: function(current) {
                this.queryConds = Object.assign({
                    financialContractUuids: '',
                    date: ''
                }, current);
            }
        }
    }
</script>