<style lang="sass">
    .directbank-financialcontract-select {
        .el-select-dropdown__list{
            .el-select-dropdown__item{
                height: 76px;
                .selectpicker-content {
                    min-height: 50px;

                    .content {
                        overflow: hidden;
                        background: transparent;
                        padding-left: 50px;
                        min-width: 300px;
                    }

                    .content .title {
                        font-size: 14px;
                        white-space: nowrap;
                    }

                    .content .subtitle {
                        color: #999;
                        font-size: 12px;
                        white-space: nowrap;
                    }

                    .identification {
                        position: absolute;
                        top: 10px;
                    }

                    .identification img {
                        height: 36px; 
                        width: 36px;
                    }

                    .link {
                        color: #436ba7;
                        cursor: pointer;
                    }
                }

                &.selected {
                    .selectpicker-content {
                        .link {
                            color: #fff;
                        }   
                    }
                }
            }
        }
    }
    #directbankList{
        .scroller{
        	overflow: hidden;
            .errorMsg {
                font-size: 12px;
                text-align: center;
                line-height: 40px;
                color: #666;
            }
        	.table-area {
        		overflow-x: auto;
        		margin-bottom: 0;
                font-size: 12px;

                .vue-recyclist {
                	height: 100%;
                }

                .vue-recyclist-item > div,
                .table-header {
                	display: table;
                	table-layout: fixed;
                	width: 100%;

                	.row {
                		margin: 0;
                		display: table-row;

                        &:before,
                        &:after {
                            display: none;
                        }
                	}
                }

                .table-header {
                    color: #666;
                    background-color: #f5f5f5;
                    text-align: left;
                }

                .table-header {
                	.row {
                		.cell {
                			color: #999;
                		}

                		&:first-child {
                			.cell {
	                			padding-top: 8px;
	                			padding-bottom: 8px;
                			}
                		}

                		&:last-child {
                			.cell {
                				padding-top: 0px;
                				padding-bottom: 8px;
                			}
                		}
                		
                	}
                }

        		.cell{
        		    display: table-cell;
        		    vertical-align: middle;
        		    padding: 10px 0 10px 20px;
        		    overflow: hidden;
        		    text-overflow: ellipsis;
        		    word-break: break-all;

        		    &.cell-long {
        		    	width: 200px;
        		    }

        		    &.cell-middle {
        		    	width: 150px;
        		    }

        		    &.cell-short {
        		    	width: 100px;
        		    }
        		}

        	}
            
            &::after{
                height: 0;
            }
        }
    }
</style>
<template>
    <div class="content" id="directbankList">
        <div class="scroller" ref="scroller">
            <div class="query-area" ref="queryArea">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <list-cascader
                            clearable
                            size="small"
                            multiple="0"
                            :filter="true"
                            :defaultSelected="true"
                            :collection="financialContractQueryModels"
                            v-model="financialContractUuids"
                            :customItem="renderItem"
                            :flattenFn="flattenFn"
                            :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.startDateString"
                                    :end-date="queryConds.endDateString"
                                    placeholder="生效起始日期"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2">
                            <div class="text-align-center color-dim">至</div>
                        </el-col>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.endDateString"
                                    :start-date="queryConds.startDateString"
                                    placeholder="生效终止日期"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.accountSide"
                            placeholder="借贷"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in accountSides"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="对方账号" value="recipAccNo">
                            </el-option>
                            <el-option label="对方户名" value="recipName">
                            </el-option>
                            <el-option label="摘要" value="summary">
                            </el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                        <el-button :disabled="dataSource.list == 0" size="small" type="primary" @click="handleExport">导出</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
            	<div ref="tableHeader" class="table-header">
            		<div class="row">
            		    <div class="cell cell-long">流水号</div>
            		    <div class="cell cell-short">借贷标志</div>
            		    <div class="cell cell-middle">借方发生额</div>
            		    <div class="cell cell-middle">贷方发生额</div>
            		    <div class="cell cell-short">余额</div>
            		    <div class="cell cell-long">对方账号</div>
            		    <div class="cell cell-long">对方户名</div>
            		    <div class="cell cell-short">对方开户行</div>
            		    <div class="cell cell-middle">入账时间</div>
            		    <div class="cell cell-middle">摘要</div>
            		    <div class="cell cell-short">附言</div>
            		</div>
            		<div class="row">
            		    <div class="cell cell-long"></div>
            		    <div class="cell cell-short">合计</div>
            		    <div class="cell cell-middle">借：{{debitSum | formatMoney}}</div>
            		    <div class="cell cell-middle">贷：{{creditSum | formatMoney}}</div>
            		    <div class="cell cell-short"></div>
            		    <div class="cell cell-long"></div>
            		    <div class="cell cell-long"></div>
            		    <div class="cell cell-short"></div>
            		    <div class="cell cell-middle"></div>
            		    <div class="cell cell-middle"></div>
            		    <div class="cell cell-short"></div>
            		</div>
            	</div>
                <div v-if="dataSource.error" class="errorMsg">{{dataSource.error}}</div>
                <div v-if="!dataSource.size" class="errorMsg">暂无数据</div>
            	<div ref="tableBody" class="table-body" v-loading="dataSource.fetching">
            		<VueRecyclist
            		  :spinner="false"
            		  :list="dataSource.list"
            		  :size="size"
            		  :offset="offset">
            		  <template slot="item" scope="props">
            		    <div class="row">
            		        <div class="cell cell-long" >{{props.data.serialNo}}</div>
            		        <div class="cell cell-short">{{props.data.drcrf == '1' ? '贷':'借'}}</div>
            		        <div class="cell cell-middle">{{props.data.creditAmount}}</div>
            		        <div class="cell cell-middle">{{props.data.debitAmount}}</div>
            		        <div class="cell cell-short">{{props.data.balance}}</div>
            		        <div class="cell cell-long">{{props.data.recipAccNo}}</div>
            		        <div class="cell cell-long">{{props.data.recipName}}</div>
            		        <div class="cell cell-short">{{props.data.recipBkName}}</div>
            		        <div class="cell cell-middle" >{{props.data.time}}</div>
            		        <div class="cell cell-middle">{{props.data.summary}}</div>
            		        <div class="cell cell-short">{{props.data.postScript}}</div>
            		    </div>
            		  </template>
            		</VueRecyclist>
            	</div>
            </div>
        </div>

        <BalanceModal
            v-model="balanceModal.show" 
            :financeContractId="balanceModal.financeContractId"
            :accountId="balanceModal.accountId">
        </BalanceModal>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise,purify } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import VueRecyclist from './include/VueRecyclist';
    import flattenFn from './include/flatten';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox'),
            BalanceModal: require('./include/BalanceModal'),
            VueRecyclist
        },
        data: function() {
            return {
                action: '/capital/directbank-cash-flow/search',

                queryConds: {
                    startDateString: '',
                    endDateString: '',
                    accountSide: '',
                },
                comboConds: {
                    recipAccNo: '',
                    recipName: '',
                    summary: ''
                },

                accountSides: [{
                    label: '借',
                    value: '1',
                },{
                    label: '贷',
                    value: '0',
                }],
                financialContractUuids: [],
                financialContractQueryModels: [],

                balanceModal: {
                    show: false,
                    financeContractId: 0,
                    accountId: 0
                },

                creditSum: '',
                debitSum: '',

                size: 30,
                offset: 120
            };
        },
        computed: {
            showList: function() {
                return this.dataSource.list.slice(0)
            },
            exportCSVName: function() {
                return [
                    '银企直联流水',
                    this.financialContractUuids[0].contractName,
                    this.conditions.startDateString,
                    this.conditions.endDateString
                ].join('_') + '.csv'
            }
        },
        mounted: function() {
        	var self = this
        	$(window).on('resize', () => {
        		if (self.resizeTimer) {
        			clearTimeout(self.resizeTimer)
        		}
        		self.resizeTimer = setTimeout(function() {
        			self.setTableBodySize()
        		}, 500)
        	})

        	self.setTableBodySize()
        },
        watch: {
            financialContractUuids: function(current) {
                if(current.length == 0){
                    this.queryConds.accountId = '';
                } else {
                    this.queryConds.accountId = current[0].id;
                }
            }
        },
        methods: {
            setTableBodySize: function() {
            	var $tableHeader = $(this.$refs.tableHeader)
            	var $tableBody = $(this.$refs.tableBody)
            	var $scroller = $(this.$refs.scroller)
            	var $queryArea = $(this.$refs.queryArea)

            	var height = $tableHeader.get(0).clientWidth
            	var width = 0

            	if (navigator.userAgent.indexOf("Windows", 0) != -1) {
            		width = $scroller.height() - $queryArea.get(0).clientHeight - $tableHeader.get(0).clientHeight - 14
            	} else {
            		width = $scroller.height() - $queryArea.get(0).clientHeight - $tableHeader.get(0).clientHeight
            	}

            	$tableBody.css({
            		width: $tableHeader.get(0).clientWidth,
            		height: width
            	})
            },
            initialize: function() {
                return this.getOptions();
            },
            getOptions: function(){
                return ajaxPromise({
                    url: `/capital/directbank-cash-flow/options`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.queryConds.startDateString = data.directbankCashFlowQueryModel.startDateString;
                    this.queryConds.endDateString = data.directbankCashFlowQueryModel.endDateString;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            fetch: function() {
                if (this.dataSource.fetching
                    && this.equalTo(purify(this.conditions), purify(this.previousConditions))) {
                    return
                }
                this.getData({
                    url: this.action,
                    data: this.conditions
                })

            },
            onRequest: function() {
                this.dataSource.fetching = true;
                this.dataSource.list = [];
                this.dataSource.error = '';
            },
            onSuccess: function(data) {
                var d = this.parse(data);
                this.dataSource.list = d.flow_result;
                this.dataSource.size = d.size;
                this.creditSum = d.creditSum;
                this.debitSum = d.debitSum;
            },
            onError: function(message) {
                this.dataSource.error = message.toString();
                this.dataSource.list = [];
            },
            handleShowBalance: function(data) {
                this.balanceModal.show = true;
                this.balanceModal.financeContractId = data.financialContractId;
                this.balanceModal.accountId = data.id;
            },
            renderItem: function(h,item){
                var handleShowBalance = this.handleShowBalance.bind(this,item)
                var clicks = {
                    on: {
                        click : handleShowBalance
                    }
                }
                return (<div class={['selectpicker-content']}>
                  <div class={['identification']}>
                    <img src={`${this.resource}/images/bank-logo/bank_${ item && item.bankCode ? item.bankCode.toLowerCase() : '' }.png`}></img>
                  </div>
                  <div class={['content']}>
                      <div class={['title']}>{ item.contractName }</div>
                      <div class={['title']}>{ item && item.markedAccountNo }</div>
                      <div class={['subtitle']}>
                          <span style="float: left">({ item && item.bankName })</span>
                          <span herf="#" class={['display-balance', 'link', 'pull-right']} {...clicks}>显示余额 >>
                          </span>
                      </div>
                  </div>
                </div>)
            },
            handleExport: function() {
                var stringify = function(list) {
                    var comma = ','
                    var data = list.map(item => {
                        return [
                            item.serialNo,
                            item.drcrf == '1' ? '贷':'借',
                            item.creditAmount,
                            item.debitAmount,
                            item.balance,
                            item.recipAccNo,
                            item.recipName,
                            item.recipBkName,
                            item.time,
                            item.summary,
                            item.postScript,
                        ].join(comma)
                    })
                    data.unshift([
                        "流水号",
                        "借贷标志",
                        "借方发生额",
                        "贷方发生额",
                        "余额",
                        "对方账号",
                        "对方户名",
                        "对方开户行",
                        "入账时间",
                        "摘要",
                        "附言"
                    ].join(comma))
                    return data.join('\n')
                }
                var downloadCSV = function(data, fileName) {
                    var csvData = '\ufeff' + data;
                    var blob = new Blob([ csvData ], {
                        type : "application/csv;charset=utf-8;"
                    });

                    if (window.navigator.msSaveBlob) {
                        // FOR IE BROWSER
                        navigator.msSaveBlob(blob, fileName);
                    } else {
                        // FOR OTHER BROWSERS
                        var link = document.createElement("a");
                        var csvUrl = URL.createObjectURL(blob);
                        link.href = csvUrl;
                        link.style = "visibility:hidden";
                        link.download = fileName;
                        document.body.appendChild(link);
                        link.click();
                        document.body.removeChild(link);
                    }
                }
                var csvContent = stringify(this.dataSource.list)
                downloadCSV(csvContent, this.exportCSVName)
            },
            flattenFn: flattenFn
        }
    }
</script>