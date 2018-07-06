<style lang="sass">
    @import '~assets/stylesheets/base';

    .split-order-legend {
        padding: 0 20px;

        .supplement,
        .annotation {
            display: table-cell;
            font-size: 12px;
            vertical-align: bottom;
        }

        .supplement {
            text-align: right;
            width: 250px;
        }

        .annotation {
            .item {
                float: left;
                margin-right: 5px;
                cursor: pointer;

                &:before {
                    content: '';
                    display: inline-block;
                    height: 13px;
                    width: 13px;
                    vertical-align: middle;
                    margin-right: 5px;
                }
            }
        }

        .graph {
            background: #e7e7e7;
            margin: 10px 0;
            font-size: 12px;

            .item {
                float: left;
                height: 40px;
                line-height: 40px;
                text-indent: 10px;
                overflow-x: hidden;

                .el-tooltip,
                .el-tooltip__rel {
                    display: block;
                }
            }
        }

        .graph .item-loanAssetPrincipal,
        .annotation .item-loanAssetPrincipal:before {
            background: #084081;
            color: #fff;
        }

        .graph .item-loanAssetInterest,
        .annotation .item-loanAssetInterest:before {
            background: #0868ac;
            color: #fff;
        }

        .graph .item-loanServiceFee,
        .annotation .item-loanServiceFee:before {
            background: #2b8cbe;
            color: #fff;
        }

        .graph .item-loanTechFee,
        .annotation .item-loanTechFee:before {
            background: #4eb3d3;
        }

        .graph .item-loanOtherFee,
        .annotation .item-loanOtherFee:before {
            background: #7bccc4;
        }

        .graph .item-overdueFeePenalty,
        .annotation .item-overdueFeePenalty:before {
            background: #a8ddb5;
        }

        .graph .item-overdueFeeObligation,
        .annotation .item-overdueFeeObligation:before {
            background: #ccebc5;
        }

        .graph .item-overdueFeeService,
        .annotation .item-overdueFeeService:before {
            background: #e0f3db;
        }

        .graph .item-overdueFeeOther,
        .annotation .item-overdueFeeOther:before {
            background: #f7fcf0;
        }
    }
</style>

<template>
    <div class="legend split-order-legend">
        <div>
            <div class="annotation">
                <span v-for="(label, type) in types"
                    @click="toggle(type)"
                    class="item"
                    :class="[`item-${type}`, active === type ? 'color-success' : '']">
                    {{ label }}（{{ canSplitChargesDetail[type] | formatMoney }}）
                </span>
            </div>
            <div class="supplement">
                <div class="color-success">可拆分金额：{{ canSplitChargesDetail.totalFee | formatMoney }}</div>
                <div>应收未收金额：{{ receivableChargesDetail.totalFee | formatMoney }}</div>
            </div>
        </div>
        <div class="graph clearfix">
            <div v-for="(label, type) in types"
                class="item" 
                :class="`item-${type}`"
                :style="{width: calcute(canSplitChargesDetail[type], type)}"
                v-if="canSplitChargesDetail[type]">
                <el-tooltip 
                    placement="top"
                    :content="createTooltip(type, label)">
                        {{ canSplitChargesDetail[type] | formatMoney }}
                </el-tooltip>
            </div>
        </div>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import formats from 'filters/format';

    export default {
        props: {
            show: Boolean,
            canSplitChargesDetail: Object,
            receivableChargesDetail: Object
        },
        data: function() {
            return {
                active: null,
                types: {
                    loanAssetPrincipal: '可拆分还款本金',
                    loanAssetInterest: '可拆分还款利息',
                    loanServiceFee: '可拆分贷款服务费',
                    loanTechFee: '可拆分技术维护费',
                    loanOtherFee: '可拆分其他费用',
                    overdueFeePenalty: '可拆分逾期罚息',
                    overdueFeeObligation: '可拆分逾期违约金',
                    overdueFeeService: '可拆分逾期服务费用',
                    overdueFeeOther: '可拆分逾期其他费用',
                }
            };
        },
        watch: {
            show: function(cur) {
                if (cur) {
                    this.active = null;
                }
            }
        },
        computed: {
            canSplitChargesPercent: function() {
                var { canSplitChargesDetail, receivableChargesDetail } = this;
                return receivableChargesDetail.totalFee == 0 ? 0 : canSplitChargesDetail.totalFee / receivableChargesDetail.totalFee;
            }
        },
        methods: {
            createTooltip: function(type, label) {
                var money = formats.formatMoney(this.canSplitChargesDetail[type]);
                return `${label}：${money}`;
            },
            toggle: function(type) {
                if (this.active === type) {
                    this.active = null;
                } else {
                    this.active = type
                }
            },
            calcute: function(value, type) {
                if (this.active === type) {
                    return 100 + '%';
                } else if (this.active === null) {
                    var { canSplitChargesDetail, canSplitChargesPercent } = this;
                    var percent = canSplitChargesDetail.totalFee == 0 ? 0 : value / canSplitChargesDetail.totalFee;
                    return percent * canSplitChargesPercent * 100 + '%';
                } else {
                    return 0 +'%';
                }
            }
        }
    }
</script>
