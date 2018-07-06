<style lang="sass">
    .aside-content {
        border: 1px solid #e7e8e9;
        background-color: #eee;
        height: 100%;
        padding: 20px 15px 15px 20px;
    }
    .aside-scroll {
        height: 100%;
        overflow-y: auto;
    }
    .account-box {
        border: 1px solid #e7e8e9;
        width: 100%;
        background-color: #fff;
        padding: 10px 25px 20px;
        margin-bottom: 10px;
        hr {
            margin: 0 0 18px;
        }
        &:before, &:after {
            content: '';
            display: table;
            clear: both;
        }
        .title {
            position: relative;
            vertical-align: middle;
            padding: 10px 0;
            color: #333;
            .el-button--small {
                position: absolute;
                right: 0;
                top: 50%;
                margin-top: -12px;
                border-radius: 1px;
                padding: 5px 10px;
            }
        }
        .no-data {
            color: #666;
        }
        .amount {
            float: left;
            width: 50%;
            display: inline-block;
            box-sizing: border-box;
            line-height: 30px;
            color: #666;
            &.no-border {
                border-right: none!important;
            }
            &:nth-child(odd) {
                border-right: 1px solid #e7e8e9;
            }
            &:nth-child(even) {
                padding-left: 20px;
            }
        }
        .money {
            cursor: pointer;
            word-break: break-all;
            font-size: 20px;
            font-weight: bold;
            font-family: Arial;
            color: #436ba7;
        }
    }

</style>
<template>
    <div class="aside-content">
        <div class="aside-scroll">
            <div class="account-box">
                <div class="title">
                    {{data.specialAccountForPending.accountName}}
                </div>
                <hr>
                <div class="no-data" v-if="list1.length == 0">暂无数据</div>
                <div
                    v-for="item in list1"
                    :class="['amount', list1.length > 1 ? '' : 'no-border']">
                    {{item.accountName}}：
                    <span class="money" @click="handlerAmountClick(item)">
                        {{ item.balance | formatMoney }}
                    </span>
                </div>
            </div>
            <div class="account-box">
                <div class="title">
                    {{data.specialAccountForRemittance.accountName}}
                    <el-button v-if="false" size="small" type="primary">配置</el-button>
                </div>
                <hr>
                <div class="no-data" v-if="list2.length == 0">暂无数据</div>
                <div
                    v-for="item in list2"
                    :class="['amount', list2.length > 1 ? '' : 'no-border']">
                    {{item.accountName}}：
                    <span class="money" @click="handlerAmountClick(item)">
                        {{ item.balance | formatMoney }}
                    </span>
                </div>
            </div>
            <div class="account-box">
                <div class="title">
                    {{data.specialAccountForAccount.accountName}}
                </div>
                <hr>
                <div class="no-data" v-if="list3.length == 0">暂无数据</div>
                <div
                    v-for="item in list3"
                    :class="['amount', list3.length > 1 ? '' : 'no-border']">
                    {{item.accountName}}：
                    <span class="money">
                        {{ item.balance | formatMoney }}
                    </span>
                </div>
            </div>
            <div class="account-box">
                <div class="title">
                    {{data.specialAccountForRepayment.accountName}}
                    <el-button v-if="false" size="small" type="primary">配置</el-button>
                </div>
                <hr>
                <div class="no-data" v-if="list4.length == 0">暂无数据</div>
                <div
                    v-for="item in list4"
                    :class="['amount', list4.length > 1 ? '' : 'no-border']">
                    {{item.accountName}}：
                    <span class="money" @click="handlerAmountClick(item)">
                        {{ item.balance | formatMoney }}
                    </span>
                </div>
            </div>
            <div class="account-box" style="margin-bottom: 0">
                <div class="title">
                    {{data.specialAccountForAccrual.accountName}}
                </div>
                <hr>
                <div class="no-data" v-if="list5.length == 0">暂无数据</div>
                <div
                    v-for="item in list5"
                    :class="['amount', list5.length > 1 ? '' : 'no-border']">
                    {{item.accountName}}：
                    <span class="money" @click="handlerAmountClick(item)">
                        {{ item.balance | formatMoney }}
                    </span>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    export default {
        props: {
            data: {
                type: Object,
                default: () => {}
            }
        },
        data: function() {
            return {

            }
        },
        computed: {
            list1() {
                return this.data.specialAccountForPendingList || [];
            },
            list2() {
                return this.data.specialAccountForRemittanceList || [];
            },
            list3() {
                return this.data.specialAccountForAccountList || [];
            },
            list4() {
                return this.data.specialAccountForRepaymentList || [];
            },
            list5() {
                return this.data.specialAccountForAccrualList || [];
            }
        },
        methods: {
            handlerAmountClick: function(item) {
                if(item.uuid) {
                    this.$emit('query-amount', item.uuid)
                }
            }
        }
    }
</script>