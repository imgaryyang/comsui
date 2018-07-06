<style lang="sass" scoped>
    .operation-data-list {
        background: #fff;
        padding: 20px;
        overflow: auto;
        .hd {
            margin: 0;
            font-size: 14px;
            margin-bottom: 15px;
        }
        .put-data{
            width: 600px;
        }
    }
</style>

<template>
    <tr>
        <td colspan="12">
            <div class="operation-data-list">
                <div class="put-data">
                    <h3 class="hd">
                        <span style="color:#666;font-weight:bold">放款数据</span>
                    </h3>
                    <PagingTable :data="appArrivaList">
                        <el-table-column label="应还本金" prop="paymentPrincipal" inline-template>
                            <div>{{ row.paymentPrincipal | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column label="应还利息" prop="paymentInterest" inline-template>
                            <div>{{row.paymentInterest | formatMoney}}</div>
                        </el-table-column>
                        <el-table-column label="应还贷款服务费" prop="paymentLoanServiceFee" inline-template>
                            <div>{{row.paymentLoanServiceFee | formatMoney}}</div>
                        </el-table-column>
                        <el-table-column label="应还总额" prop="paymentAmount" inline-template>
                            <div>{{row.paymentAmount | formatMoney}}</div>
                        </el-table-column>
                    </PagingTable>
                </div><br>
                <div v-for="item in items">
                    <h3 class="hd">
                        <span style="color:#666;font-weight:bold">{{item[0].paymentGatewayName}}实际收款数据</span>
                    </h3>
                    <PagingTable :data="item">
                        <el-table-column label="实际还款笔数" prop="actualRepaymentNumber"></el-table-column>
                        <el-table-column label="实收金额" prop="actualTotalFee" inline-template>
                            <div>{{row.actualTotalFee | formatMoney}}</div>
                        </el-table-column>
                        <el-table-column label="实收本金" prop="actualPrincipal" inline-template>
                            <div>{{row.actualPrincipal | formatMoney}}</div>
                        </el-table-column>
                        <el-table-column label="实收利息" prop="actualInterest" inline-template>
                            <div>{{row.actualInterest | formatMoney}}</div>
                        </el-table-column>
                        <el-table-column label="实收贷款服务费" prop="actualLoanServiceFee" inline-template>
                            <div>{{row.actualLoanServiceFee | formatMoney}}</div>
                        </el-table-column>
                        <el-table-column label="实收技术维护费" prop="actualTechFee" inline-template>
                            <div>{{row.actualTechFee | formatMoney}}</div>
                        </el-table-column>
                        <el-table-column label="实收其他费用" prop="actualOtherFee" inline-template>
                            <div>{{row.actualOtherFee | formatMoney}}</div>
                        </el-table-column>
                        <el-table-column label="实收逾期罚息" prop="actualOverduePenalty" inline-template>
                            <div>{{row.actualOverduePenalty | formatMoney}}</div>
                        </el-table-column>
                        <el-table-column label="实收逾期违约金" prop="actualOverdueDefaultFee" inline-template>
                            <div>{{row.actualOverdueDefaultFee | formatMoney}}</div>
                        </el-table-column>
                        <el-table-column label="实收逾期服务费" prop="actualOverdueServiceFee" inline-template>
                            <div>{{row.actualOverdueServiceFee | formatMoney}}</div>
                        </el-table-column>
                        <el-table-column label="实收逾期其他费用" prop="actualOverdueOtherFee" inline-template>
                            <div>{{row.actualOverdueOtherFee | formatMoney}}</div>
                        </el-table-column>
                    </PagingTable>
                    <br>
                </div>
            </div>
        </td>
    </tr>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        components: {
            PagingTable: require('views/include/PagingTable'),
        },
        props: {
            appArrivaList: Array,
        },
        data: function() {
            return {

            }
        },
        computed: {
            items: function(){
                var result = [];
                if(this.appArrivaList && this.appArrivaList.length &&this.appArrivaList[0].paymentGatewayAmountDetailsJson){
                    var temp = JSON.parse(this.appArrivaList[0].paymentGatewayAmountDetailsJson);
                    temp.map(item => result.push([item]));
                    temp = null;
                }
                return result;
            }
        },
    }
</script>