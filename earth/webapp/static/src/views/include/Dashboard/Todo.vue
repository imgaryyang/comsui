<style lang="sass">
	
</style>

<template>
	<div 
        v-if="active" 
        class="dashboard item-todo">
        <div class="inner">
            <div class="hd">
                <p class="name">
                    <strong>[{{principal.username}}],欢迎您</strong>
                </p>
                <p>您今日所需关注的工作内容如下</p>
            </div>
            <div class="bd">
                <div class="projects">
                    <p style="color:#999;">请选择{{ $utils.locale('financialContract') }}</p>
                    <list-cascader
                        clearable
                        :width="300"
                        size="small"
                        :defaultSelected="true"
                        :collection="financialContractQueryModels"
                        v-model="selectedFinancialContractList"
                        :placeholder="$utils.locale('financialContract')">
                    </list-cascader>
                </div>
                <div class="todos">
                    <TabMenu 
                        class="nav nav-tabs" 
                        style="overflow: initial;"
                        type="none" 
                        v-model="selected">
                        <TabMenuItem id="remittance">
                            放款 <span class="total"></span>
                        </TabMenuItem>
                        <TabMenuItem id="repayment">
                            还款 <span class="total"></span>
                        </TabMenuItem>
                    </TabMenu>
                    <TabContent v-model="selected">
                        <TabContentItem id="remittance" v-loading="remittance.fetching">
                            <div class="text-align-center" v-if="remittance.error">{{ remittance.error }}</div>
                            <TodoRemittance 
                                v-else 
                                :selected-financial-contract="selectedFinancialContractList" 
                                :remittance-data="remittance.data">
                            </TodoRemittance>
                        </TabContentItem>
                        <TabContentItem id="repayment" v-loading="repayment.fetching">
                            <div class="text-align-center" v-if="repayment.error">{{ repayment.error }}</div>
                            <TodoRepayment 
                                v-else 
                                :selected-financial-contract="selectedFinancialContractList" 
                                :repayment-data="repayment.data">
                            </TodoRepayment>
                        </TabContentItem>
                    </TabContent>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import store from 'store';
    import { ajaxPromise, searchify, purify } from 'assets/javascripts/util';
    import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';

	export default {
        components: {
            TabMenu, TabMenuItem, TabContent, TabContentItem,
            TodoRemittance: require('./TodoRemittance'),
            TodoRepayment: require('./TodoRepayment'),
         },
		props: {
            id: String,
            data: Object,
            visible: Boolean
		},
        computed: {
            principal: function() {
                return store.state.principal;
            },
            financialContractQueryModels: function() {
                return store.state.financialContract.financialContractQueryModels;
            },
            currentFinancialContractIds: function() {
                return  this.selectedFinancialContractList ? this.selectedFinancialContractList.map(item => item.financialContractId) : [];
            },
            active: function() {
                return this.$parent.selected == this.id;
            }
        },
        watch: {
            // active: function(current) {
            //     if (current && !this.financialContractQueryModels.length) {
            //         store.dispatch('getFinancialContractQueryModels');
            //     }
            // },
            // selectedFinancialContractList: function(current) {
            //     this.fetch();
            // },
            // selected: function(current) {
            //     this.fetch();
            // }
        },
        data: function() {
            return {
                selected: 'remittance',
                selectedFinancialContractList: [],
                remittance: {
                    fetching: false,
                    error: '',
                    data: {},
                },
                repayment: {
                    fetching: false,
                    error: '',
                    data: {},
                },
            };
        },
        methods: {
            fetch: function() {
                if (this.selected == 'remittance' && !this.remittance.fetching) {
                    this.fetchRemittance();
                } else if (this.selected == 'repayment' && !this.repayment.fetching) {
                    this.fetchRepayment();
                }
            },
            fetchRemittance: function() {
                if (!this.currentFinancialContractIds) return;
                this.remittance.fetching = true;
                ajaxPromise({
                    url: '/welcome/statistics?type=remittance',
                    data: purify({ financialContractIds: this.currentFinancialContractIds })
                }).then(data => {
                    this.remittance.data = data.remittanceData;
                }).catch(message => {
                    this.remittance.error = message;
                }).then(() => {
                    this.remittance.fetching = false;
                });
            },
            fetchRepayment: function() {
                if (!this.currentFinancialContractIds) return;
                this.repayment.fetching = true;
                ajaxPromise({
                    url: '/welcome/statistics?type=repayment',
                    data: purify({ financialContractIds: this.currentFinancialContractIds})
                }).then(data => {
                    this.repayment.data = data.repaymentData;
                }).catch(message => {
                    this.repayment.error = message;
                }).then(() => {
                    this.repayment.fetching = false;
                });
            }
        }
	}
</script>