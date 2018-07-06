<template>
    <div class="tab-content-item repayment">
        <div class="list">
            <div class="item">
                <i class="icon icon-tag pull-right"></i>
                <div class="lt">
                    <a @click="redirect([1, 3, 4])">
                        <p><strong data-total="${remittanceData.totalNums}">{{ remittanceData.totalNums }}</strong></p>
                        <div>放款计划</div>
                    </a>
                </div>
                <div class="rt">
                    <div>
                        <a @click="redirect([1])">
                            处理中：{{ remittanceData.processingRemittanceApplicationNums }}
                        </a>
                    </div>
                    <div>
                        <a @click="redirect([4])">
                            异常：{{ remittanceData.abnormalRemittanceApplicationNums }}
                        </a>
                    </div>
                    <div>
                        <a @click="redirect([3])">
                            失败：{{ remittanceData.failedRemittanceApplicationNums }}
                        </a>
                    </div>
                </div>
                <i class="more"></i>
            </div>
        </div>
    </div>
</template>

<script>
    import formats from 'src/filters';
    import { purify, searchify } from 'assets/javascripts/util';

    export default {
        props: {
            remittanceData: Object,
            selectedFinancialContract: {
                default: () => {[]}
            }
        },
        data: function() {
            return {};
        },
        methods: {
            redirect: function(orderStatus) {
                var { ctx, selectedFinancialContract } = this;
                var attr = {
                    orderStatus,
                    t: Date.now(),
                    financialContractUuids: selectedFinancialContract,
                    receiveStartDate: formats.formatDate(Date.now(), 'yyyy-MM-dd 00:00:00'),
                    financialContractIds: selectedFinancialContract
                };
                var search =  searchify(purify(attr));
                location.assign(encodeURI(`${ctx}#/data/remittance/application?${search}`));
            }
        }
    }
</script>