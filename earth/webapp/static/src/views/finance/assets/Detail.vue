<style lang="sass" scoped>
.no-border-top {
    border-top: 0px;
}
.row-layout-detail .block .ft .drawer {
    line-height: 28px;
}
.el-table .green {
    background: #e2f0e4;
}
</style>

<template>
	<div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '还款列表'}, {title: '还款单详情'} ]">
                <el-button
                    v-if="data.canBeInvalid"
                    type="primary"
                    size="small"
                    @click="invalidateModal.show = true">作废</el-button>
                <el-button
                    v-if="ifElementGranted('modify-repayment-management-comment')"
                    type="primary"
                    size="small"
                    @click="modifyRemark">修改备注</el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
            	<div class="top">
            		<div class="block">
            			<h5 class="hd">贷款信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>贷款合同编号 ：
                                    <a :href="`${ctx}#/data/contracts/detail?id=${data.contractId}`" > {{ data.contractNo}} </a>
                                </p>
  								<p>贷款客户编号 ：{{ data.customerNo}}</p>
  								<p>资产编号 ：{{ data.assetNo}}</p>
                                <p>还款编号 ：{{ data.repaymentPlanNo}}</p>
  								<p>商户还款计划编号 ：{{ data.outerRepaymentPlanNo}}</p>
            				</div>
            			</div>
            		</div>
            		<div class="block">
            			<h5 class="hd">还款信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>计划还款日期 :{{ data.assetRecycleDate}}</p>
            					<p>计划还款本金 : {{ planChargesDetail.loanAssetPrincipal | formatMoney }}</p>
            					<p>计划还款利息 : {{ planChargesDetail.loanAssetInterest | formatMoney }}</p>
            					<p>贷款服务费 : {{ planChargesDetail.loanServiceFee | formatMoney }}</p>
            					<p>技术维护费 : {{ planChargesDetail.loanTechFee | formatMoney }}</p>
            					<p>其他费用 : {{ planChargesDetail.loanOtherFee | formatMoney }}</p>
                            </div>
                            <div class="col">
            					<p>差异天数 : {{ data.overDueDays}}</p>
            					<p>逾期天数 : {{ data.auditOverdueDays}}</p>
            					<p>逾期费用合计 : {{ planChargesDetail.totalOverdueFee | formatMoney }}</p>
            					<p>应还金额总计 : {{ planChargesDetail.totalFee | formatMoney }}</p>
            					<p>实际还款时间 : {{ data.actualRecycleDate }}</p>
            					<p>实际还款金额 : {{ data.actualPaidAmount | formatMoney }}</p>
                            </div>
                            <div class="col">
            					<p>担保状态 : {{ data.guaranteeStatus}}</p>
            					<p>逾期状态 :
                                    <span class="color-danger">{{ data.overdueStatus }}</span>
                                    <a
                                        v-if="ifElementGranted('modify-overdue-status')
                                            && data.overdueStatus != '已逾期'"
                                        href="javascript: void 0"
                                        @click="modifyOverdueStatus">
                                        修改
                                    </a>
                                </p>
            					<p>还款状态 : {{ data.paymentStatus}}</p>
                                <p>计划状态 : {{ data.planStatus }}</p>
                                <p>还款类型 : {{ data.repaymentType }}</p>
            					<p>备注 : {{ data.comment}}</p>
                            </div>
                        </div>
            		</div>
            		<div class="block">
            			<h5 class="hd">账户信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>客户姓名 : {{ data.customerName }}</p>
            					<p>账户开户行 : {{ data.accountBank }}</p>
            					<p>开户行所在地 : {{ data.accountProvince }}&nbsp;{{ data.accountCity }}</p>
            					<p>账号 : {{ data.accountNo }}</p>
                                <p>系统账户余额 : {{ data.accountBalance | formatMoney }}</p>
            				</div>
            			</div>
            		</div>
            	</div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <TabMenu v-model="tabSelected">
                        <TabMenuItem id="actualTabMenuItem">实收明细</TabMenuItem>
                        <TabMenuItem id="overdueTabMenuItem">逾期费用明细</TabMenuItem>
                        <div @click="fetchMutableFee()">
                            <TabMenuItem id="mutableTabMenuItem">浮动费用明细</TabMenuItem>
                        </div>
                    </TabMenu>
                    <TabContent v-model="tabSelected">
                        <TabContentItem id="actualTabMenuItem">
                            <div class="table">
                                <div class="block">
                                    <h5 class="hd">实收明细 &nbsp;
                                        <el-button size="small" type="primary" @click="handleRefund" v-if="false">退款</el-button>
                                    </h5>
                                    <div class="bd">
                                        <table>
                                            <thead>
                                                <tr>
                                                    <th></th>
                                                    <th>资金类型</th>
                                                    <th>本金</th>
                                                    <th>利息</th>
                                                    <th>贷款服务费</th>
                                                    <th>技术维护费</th>
                                                    <th>其他费用</th>
                                                    <th>逾期罚息</th>
                                                    <th>逾期违约金</th>
                                                    <th>逾期服务费</th>
                                                    <th>逾期其他费用</th>
                                                    <th>金额合计</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td>计划应还金额</td>
                                                    <td>应还金额</td>
                                                    <td>{{ data.planChargesDetail.loanAssetPrincipal | formatMoney }}</td>
                                                    <td>{{ data.planChargesDetail.loanAssetInterest | formatMoney }}</td>
                                                    <td>{{ data.planChargesDetail.loanServiceFee | formatMoney }}</td>
                                                    <td>{{ data.planChargesDetail.loanTechFee | formatMoney }}</td>
                                                    <td>{{ data.planChargesDetail.loanOtherFee | formatMoney }}</td>
                                                    <td>{{ data.planChargesDetail.overdueFeePenalty | formatMoney }}</td>
                                                    <td>{{ data.planChargesDetail.overdueFeeObligation | formatMoney }}</td>
                                                    <td>{{ data.planChargesDetail.overdueFeeService | formatMoney }}</td>
                                                    <td>{{ data.planChargesDetail.overdueFeeOther | formatMoney }}</td>
                                                    <td>{{ data.planChargesDetail.totalFee | formatMoney }}</td>
                                                </tr>
                                                <tr>
                                                    <td rowspan="3">实际还款金额</td>
                                                    <td>入账资金</td>
                                                    <td>{{ data.actualChargesDetail.loanAssetPrincipal | formatMoney }}</td>
                                                    <td>{{ data.actualChargesDetail.loanAssetInterest | formatMoney }}</td>
                                                    <td>{{ data.actualChargesDetail.loanServiceFee | formatMoney }}</td>
                                                    <td>{{ data.actualChargesDetail.loanTechFee | formatMoney }}</td>
                                                    <td>{{ data.actualChargesDetail.loanOtherFee | formatMoney }}</td>
                                                    <td>{{ data.actualChargesDetail.overdueFeePenalty | formatMoney }}</td>
                                                    <td>{{ data.actualChargesDetail.overdueFeeObligation | formatMoney }}</td>
                                                    <td>{{ data.actualChargesDetail.overdueFeeService | formatMoney }}</td>
                                                    <td>{{ data.actualChargesDetail.overdueFeeOther | formatMoney }}</td>
                                                    <td>{{ data.actualChargesDetail.totalFee | formatMoney }}</td>
                                                </tr>
                                                <tr>
                                                    <td>在途资金</td>
                                                    <td>{{ data.intransitChargesDetail.loanAssetPrincipal | formatMoney }}</td>
                                                    <td>{{ data.intransitChargesDetail.loanAssetInterest | formatMoney }}</td>
                                                    <td>{{ data.intransitChargesDetail.loanServiceFee | formatMoney }}</td>
                                                    <td>{{ data.intransitChargesDetail.loanTechFee | formatMoney }}</td>
                                                    <td>{{ data.intransitChargesDetail.loanOtherFee | formatMoney }}</td>
                                                    <td>{{ data.intransitChargesDetail.overdueFeePenalty | formatMoney }}</td>
                                                    <td>{{ data.intransitChargesDetail.overdueFeeObligation | formatMoney }}</td>
                                                    <td>{{ data.intransitChargesDetail.overdueFeeService | formatMoney }}</td>
                                                    <td>{{ data.intransitChargesDetail.overdueFeeOther | formatMoney }}</td>
                                                    <td>{{ data.intransitChargesDetail.totalFee | formatMoney }}</td>
                                                </tr>
                                                <tr>
                                                    <td>实还总计</td>
                                                    <td>{{ data.paidUpChargesDetail.loanAssetPrincipal | formatMoney }}</td>
                                                    <td>{{ data.paidUpChargesDetail.loanAssetInterest | formatMoney }}</td>
                                                    <td>{{ data.paidUpChargesDetail.loanServiceFee | formatMoney }}</td>
                                                    <td>{{ data.paidUpChargesDetail.loanTechFee | formatMoney }}</td>
                                                    <td>{{ data.paidUpChargesDetail.loanOtherFee | formatMoney }}</td>
                                                    <td>{{ data.paidUpChargesDetail.overdueFeePenalty | formatMoney }}</td>
                                                    <td>{{ data.paidUpChargesDetail.overdueFeeObligation | formatMoney }}</td>
                                                    <td>{{ data.paidUpChargesDetail.overdueFeeService | formatMoney }}</td>
                                                    <td>{{ data.paidUpChargesDetail.overdueFeeOther | formatMoney }}</td>
                                                    <td>{{ data.paidUpChargesDetail.totalFee | formatMoney }}</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </TabContentItem>
                        <TabContentItem id="overdueTabMenuItem">
                            <div>
                                <div class="block">
                                    <h5 class="hd">逾期费用明细</h5>
                                    <div class="bd">
                                        <el-table
                                            :data="data.overdueChargesDetail"
                                            class="td-15-padding th-8-15-padding no-th-border"
                                            :row-class-name="setRowClass"
                                            stripe
                                            border>
                                            <el-table-column prop="title" label="">
                                            </el-table-column>
                                            <el-table-column
                                                inline-template
                                                label="逾期罚息">
                                                <div>
                                                    <div v-if="row.editting"><el-input size="small" v-model="data.overdueChargesDetail[$index].overdueFeePenalty"></el-input></div>
                                                    <div v-else>{{ row.overdueFeePenalty | formatMoney }}</div>
                                                </div>
                                            </el-table-column>
                                            <el-table-column
                                                inline-template
                                                label="逾期违约金">
                                                <div>
                                                    <div v-if="row.editting"><el-input size="small" v-model="data.overdueChargesDetail[$index].overdueFeeObligation"></el-input></div>
                                                    <div v-else>{{ row.overdueFeeObligation | formatMoney }}</div>
                                                </div>
                                            </el-table-column>
                                            <el-table-column
                                                inline-template
                                                label="逾期服务费">
                                                <div>
                                                    <div v-if="row.editting"><el-input size="small" v-model="data.overdueChargesDetail[$index].overdueFeeService"></el-input></div>
                                                    <div v-else>{{ row.overdueFeeService | formatMoney }}</div>
                                                </div>
                                            </el-table-column>
                                            <el-table-column
                                                inline-template
                                                label="逾期其他费用">
                                                <div>
                                                    <div v-if="row.editting"><el-input size="small" v-model="data.overdueChargesDetail[$index].overdueFeeOther"></el-input></div>
                                                    <div v-else>{{ row.overdueFeeOther | formatMoney }}</div>
                                                </div>
                                            </el-table-column>
                                            <el-table-column
                                                inline-template
                                                label="逾期费用合计">
                                                <div>{{ row.totalOverdueFee | formatMoney }}</div>
                                            </el-table-column>
                                            <el-table-column prop="lastModifyTime" label="变更时间">
                                            </el-table-column>
                                            <el-table-column prop="status" label="状态">
                                            </el-table-column>
                                            <el-table-column
                                                inline-template
                                                label="操作">
                                                <div>
                                                    <div v-if="row.editting">
                                                        <a href="#" @click.prevent="onSubmitUpdatedCharges($index)">保存</a>
                                                        <a href="#" @click.prevent="onCancelCharges($index)">取消</a>
                                                    </div>
                                                    <div v-else>
                                                        <a
                                                            v-if="ifElementGranted('modify-overdue-fee') &&  !['未到期', '成功', '作废', '回购中', '已回购', '违约'].includes(data.paymentStatus) && row.allowModify"
                                                            href="#"
                                                            @click.prevent="onEditCharges($index)">编辑</a>
                                                    </div>
                                                </div>
                                            </el-table-column>
                                        </el-table>

                                        <el-table
                                            :data="dataSource.list"
                                            class="td-15-padding th-8-15-padding no-th-border"
                                            :show-header="false"
                                            v-loading="dataSource.fetching"
                                            style="z-index: 2; margin-top: -1px;"
                                            v-if="isShowOverdueFee"
                                            stripe
                                            border>
                                            <el-table-column prop="title" label="">
                                            </el-table-column>
                                            <el-table-column
                                                inline-template
                                                label="逾期罚息">
                                                <div>{{ row.overdueFeePenalty | formatMoney }}</div>
                                            </el-table-column>
                                            <el-table-column
                                                inline-template
                                                label="逾期违约金">
                                                <div>{{ row.overdueFeeObligation | formatMoney }}</div>
                                            </el-table-column>
                                            <el-table-column
                                                inline-template
                                                label="逾期服务费">
                                                <div>{{ row.overdueFeeService | formatMoney }}</div>
                                            </el-table-column>
                                            <el-table-column
                                                inline-template
                                                label="逾期其他费用">
                                                    <div>{{ row.overdueFeeOther | formatMoney }}</div>
                                            </el-table-column>
                                            <el-table-column
                                                inline-template
                                                label="逾期费用合计">
                                                <div>{{ row.totalOverdueFee | formatMoney }}</div>
                                            </el-table-column>
                                            <el-table-column  inline-template label="变更时间">
                                                <div>{{ row.lastModifyTime }}</div>
                                            </el-table-column>
                                            <el-table-column prop="status" label="状态">
                                            </el-table-column>
                                            <el-table-column prop="" label="操作">
                                        </el-table>
                                    </div>
                                    <div class="ft text-align-center">
                                        <a href="javascript:void(0)" class="drawer" @click="isShowOverdueFee = !isShowOverdueFee">
                                            <span class="msg">{{ drawerMsg }}</span>
                                            <i class="icon icon-up-down"  v-bind:class="{active: isShowOverdueFee}"></i>
                                        </a>
                                        <PageControl
                                            v-model="pageConds.pageIndex"
                                            v-if="isShowOverdueFee"
                                            :size="dataSource.size"
                                            :per-page-record-number="pageConds.perPageRecordNumber">
                                        </PageControl>
                                    </div>
                                </div>
                            </div>
                        </TabContentItem>
                        <TabContentItem id="mutableTabMenuItem">
                            <div>
                                <div class="block">
                                    <h5 class="hd">浮动费用明细({{mutableFeePages}}) &nbsp;
                                        <el-button type="primary" @click="createMutablerFee" :loading="fetchingMutableFee" v-if="data.canCreate">新增</el-button>
                                    </h5>
                                    <div class="bd">
                                        <el-table
                                            :data="mutableFeeFirst"
                                            class="td-15-padding th-8-15-padding no-th-border"
                                            stripe
                                            border
                                            v-loading="mutableLoading">
                                            <el-table-column prop="mutableFeeNo" label="浮动费用编号" v-if="!createMutablerFeeTag">
                                            </el-table-column>
                                            <el-table-column prop="effectiveTime" label="生效时间" inline-template>
                                                <div>{{row.effectiveTime | formatDate}}</div>
                                            </el-table-column>
                                            <el-table-column prop="reasonCode" label="变更原因" inline-template>
                                                <div>
                                                    <template v-if="row.editting && createMutablerFeeTag">
                                                        <el-select
                                                            v-model="row.reasonCode"
                                                            size="small">
                                                            <el-option
                                                                v-for="item in mutableFeeReasonCode"
                                                                :label="item.value"
                                                                :value="item.key">
                                                            </el-option>
                                                        </el-select>
                                                    </template>
                                                    <span v-else>{{ row.reasonCode }}</span>
                                                </div>
                                            </el-table-column>
                                            <el-table-column prop="assetInterestValue" label="计划还款利息" :render-header="renderHeader0" inline-template>
                                                <div>
                                                    <template v-if="row.editting && createMutablerFeeTag">
                                                       <el-input size="small" v-model="row.assetInterestValue"></el-input>
                                                    </template>
                                                    <span v-else>{{ row.assetInterestValue | formatMoney }}</span>
                                                </div>
                                            </el-table-column>
                                            <el-table-column prop="serviceCharge" label="贷款服务费" :render-header="renderHeader1" inline-template>
                                                <div>
                                                    <template v-if="row.editting && createMutablerFeeTag">
                                                       <el-input size="small" v-model="row.serviceCharge"></el-input>
                                                    </template>
                                                    <span v-else>{{ row.serviceCharge | formatMoney }}</span>
                                                </div>
                                            </el-table-column>
                                            <el-table-column prop="maintenanceCharge" label="技术维护费" :render-header="renderHeader2" inline-template>
                                                <div>
                                                    <template v-if="row.editting && createMutablerFeeTag">
                                                       <el-input size="small" v-model="row.maintenanceCharge"></el-input>
                                                    </template>
                                                    <span v-else>{{ row.maintenanceCharge | formatMoney }}</span>
                                                </div>
                                            </el-table-column>
                                            <el-table-column prop="otherCharge" label="其他费用" :render-header="renderHeader3" inline-template>
                                                <div>
                                                    <template v-if="row.editting && createMutablerFeeTag">
                                                       <el-input size="small" v-model="row.otherCharge"></el-input>
                                                    </template>
                                                    <span v-else>{{ row.otherCharge | formatMoney }}</span>
                                                </div>
                                            </el-table-column>
                                            <el-table-column prop="approver" label="审核人"></el-table-column>
                                            <el-table-column label="审核时间" inline-template>
                                                <div>{{ row.approvedTime | formatDate }}</div>
                                            </el-table-column>
                                            <el-table-column prop="comment" label="备注" inline-template>
                                                <div>
                                                    <template v-if="row.editting && createMutablerFeeTag">
                                                       <el-input size="small" v-model="row.comment"></el-input>
                                                    </template>
                                                    <span v-else>{{ row.comment }}</span>
                                                </div>
                                            </el-table-column>
                                            <el-table-column label="操作" v-if="createMutablerFeeTag" inline-template>
                                                <div>
                                                    <template v-if="row.editting">
                                                        <a href="#" @click.prevent="onSaveMutablerFee(row)">保存</a>
                                                        <a href="#" @click.prevent="onCancelMutablerFee()">取消</a>
                                                    </template>
                                                </div>
                                            </el-table-column>
                                        </el-table>
                                    </div>
                                    <div class="ft text-align-center">
                                        <a href="javascript:void(0)" class="drawer" @click="handleMutableFee">
                                            <span class="msg">{{ MutableFeeDrawerMsg }}</span>
                                            <i class="icon icon-up-down"  v-bind:class="{active: isShowMutableFee}"></i>
                                        </a>
                                        <PageControl
                                            v-model="pageCondsFloat.pageIndex"
                                            v-if="isShowMutableFee"
                                            :size="mutableFeePages"
                                            :per-page-record-number="pageCondsFloat.perPageRecordNumber">
                                        </PageControl>
                                    </div>
                                </div>
                            </div>
                        </TabContentItem>
                    </TabContent>
                </div>

                <div class="block">
                    <TabMenu v-model="tabSelected1">
                        <TabMenuItem id="normalOrders">结算单列表</TabMenuItem>
                        <div @click="refreshRepaymentOrders = true">
                            <TabMenuItem id="repaymentOrders">还款订单列表</TabMenuItem>
                        </div>
                    </TabMenu>
                    <TabContent v-model="tabSelected1">
                        <TabContentItem id="normalOrders">
                            <div>
                                <div class="block">
                                    <h5 class="hd">
                                        结算单列表({{ data.normalOrders.length }})
                                        <el-button
                                            v-if="ifElementGranted('split-payment-order')
                                                && data.allowSplitSettlement"
                                            class="pull-right"
                                            type="primary"
                                            size="small"
                                            @click="splitOrderModal.show = true">拆分</el-button>
                                    </h5>
                                    <PagingTable
                                        :data="data.normalOrders"
                                        :pagination="true">
                                        <el-table-column inline-template label="结算单号">
                                            <a :href="`${ctx}#/finance/payment-order/${row.normalOrderId}/detail`">{{ row.normalOrderNo }}</a>
                                        </el-table-column>

                                        <el-table-column inline-template label="结算金额">
                                            <div>{{ row.normalOrderAmount | formatMoney  }}</div>
                                        </el-table-column>

                                        <el-table-column prop="createTime" label="创建时间">
                                        </el-table-column>

                                        <el-table-column prop="lastModifyTime" label="状态变更时间">
                                        </el-table-column>

                                        <el-table-column prop="normalOrderStatus" label="结算状态">
                                        </el-table-column>
                                    </PagingTable>
                                </div>
                            </div>
                        </TabContentItem>
                        <TabContentItem id="repaymentOrders">
                           <StretchTableWithQuery
                                title="还款订单"
                                :action="`/assets/${$route.params.assetSetId}/queryRepaymentOrder`"
                                v-model="refreshRepaymentOrders"
                                :titleShowSize="true"
                                :defaultShowNumber="1"
                                >
                                <el-table-column inline-template label="还款订单编号">
                                    <a :href="`${ctx}#/finance/repayment-order/${row.orderUuid}/detail`">{{ row.orderUuid }}</a>
                                </el-table-column>
                                <el-table-column inline-template label="订单关联金额">
                                    <div>{{ row.placeOrderAmount | formatMoney  }}</div>
                                </el-table-column>
                                <el-table-column prop="orderCreateTime" label="创建时间" inline-template>
                                    <div>{{ row.orderCreateTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                                </el-table-column>
                                <el-table-column prop="orderLastModifiedTime" label="状态变更时间" inline-template>
                                    <div>{{ row.orderLastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                                </el-table-column>
                                <el-table-column prop="orderStatusName" label="订单状态">
                                </el-table-column>
                           </StretchTableWithQuery>
                        </TabContentItem>
                    </TabContent>
                </div>

                <div class="block">
                    <h5 class="hd">扣款订单</h5>
                    <PagingTable
                        :data="data.deductApplications"
                        :pagination="true">
                        <el-table-column label="订单编号" inline-template>
                            <a :href="`${ctx}#/finance/application/${row.deudctNo}/detail`">{{ row.deudctNo }}</a>
                        </el-table-column>

                        <el-table-column prop="loanContractNo" label="合同编号">
                        </el-table-column>

                        <el-table-column label="创建时间" inline-template>
                            <div>
                                {{ row.createTime | formatDate("yyyy-MM-dd HH:mm:ss") }}
                            </div>
                        </el-table-column>

                        <el-table-column prop="repaymentType" label="订单类型">
                        </el-table-column>

                        <el-table-column label="扣款金额" inline-template>
                            <div>{{ row.planDeductAmount | formatMoney }}</div>
                        </el-table-column>

                        <el-table-column label="状态变更时间" inline-template>
                            <div> {{ row.statusModifyTime | formatDate("yyyy-MM-dd HH:mm:ss")}}</div>
                        </el-table-column>

                        <el-table-column prop="deductStatus" label="状态">
                        </el-table-column>

                        <el-table-column prop="remark" label="备注">
                        </el-table-column>
                    </PagingTable>
                </div>

                <div class="block">
                    <h5 class="hd">支付记录</h5>
                    <PagingTable
                        :data="data.paymentRecords"
                        :pagination="true">
                        <el-table-column label="支付编号" inline-template>
                            <a
                                :href="row.recordType == '1'
                                    ? `javascript: void 0;`
                                    : row.recordType == '2'
                                        ? `${ctx}#/capital/payment-cash-flow/online-payment/${row.id}/detail`
                                        : `${ctx}#/capital/account/payment-order/${row.uuid}/detail`">
                                {{ row.paymentRecordNo }}
                            </a>
                        </el-table-column>

                        <el-table-column label="扣款订单" inline-template>
                            <a :href="`${ctx}#/finance/application/${row.repaymentNo}/detail`">{{ row.repaymentNo }}</a>
                        </el-table-column>

                        <el-table-column label="交易金额" inline-template>
                            <div>
                                {{ row.transactionAmount | formatMoney }}
                            </div>
                        </el-table-column>

                        <el-table-column label="状态变更时间" inline-template>
                            <div>
                                {{ row.stateChangeTime | formatDate("yyyy-MM-dd HH:mm:ss") }}
                            </div>
                        </el-table-column>

                        <el-table-column prop="paymentGateway" label="支付通道">
                        </el-table-column>

                        <el-table-column prop="status" label="状态">
                        </el-table-column>

                        <el-table-column prop="remark" label="备注">
                        </el-table-column>

                    </PagingTable>
                </div>

				<div class="block">
                    <h5 class="hd">还款记录</h5>
                    <div class="bd">
                        <PagingTable :data="repaymentHistorys">
                            <el-table-column label="记录编号" prop="" inline-template>
                                <div>{{ $index + 1 }}</div>
                            </el-table-column>
                            <el-table-column label="业务编号" prop="repaymentPlanNo" inline-template>
                                <a :href="row.capitalType != '回购' ? `${ctx}#/finance/assets/${row.assetSetUuid}/detail` : `${ctx}#/finance/repurchasedoc/${row.uuid}/detail`">{{ row.repaymentPlanNo }}</a>
                            </el-table-column>
                            <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo" inline-template>
                                <div>{{ row.outerRepaymentPlanNo }}</div>
                            </el-table-column>
                            <el-table-column label="计划还款日期" prop="planDate" inline-template>
                                <div>
                                    {{ row.planDate | formatDate('yyyy-MM-dd') }}
                                </div>
                            </el-table-column>
                            <el-table-column label="实际还款时间" prop="actualRecycleDate" inline-template>
                                <div>
                                    {{ row.actualRecycleDate | formatDate('yyyy-MM-dd HH:mm:ss') }}
                                </div>
                            </el-table-column>
                            <el-table-column label="发生时间" prop="happenDate" inline-template>
                                <div>
                                    {{ row.happenDate | formatDate('yyyy-MM-dd HH:mm:ss') }}
                                </div>
                            </el-table-column>
                            <el-table-column label="资金入账时间" prop="accountedDate" inline-template>
                                <div>
                                    {{ row.accountedDate | formatDate('yyyy-MM-dd HH:mm:ss') }}
                                </div>
                            </el-table-column>
                            <el-table-column label="本次实收金额" prop="totalFee" inline-template>
                                <el-popover
                                    placement="top"
                                    trigger="hover">
                                    <template v-if="row.capitalType != '回购'">
                                        <div>实收本金：{{ row.loanAssetPrincipal | formatMoney }}</div>
                                        <div>实收利息：{{ row.loanAssetInterest | formatMoney }}</div>
                                        <div>实收贷款服务费：{{ row.loanServiceFee | formatMoney }}</div>
                                        <div>实收技术维护费：{{ row.loanTechFee | formatMoney }}</div>
                                        <div>实收其他费用：{{ row.loanOtherFee | formatMoney }}</div>
                                        <div>实收逾期罚息：{{ row.overdueFeePenalty | formatMoney }}</div>
                                        <div>实收逾期违约金：{{ row.overdueFeeObligation | formatMoney }}</div>
                                        <div>实收逾期服务费：{{ row.overdueFeeService | formatMoney }}</div>
                                        <div>实收逾期其他费用：{{ row.overdueFeeOther | formatMoney }}</div>
                                        <div>实收逾期费用合计：{{ (row.overdueFeePenalty + row.overdueFeeObligation + row.overdueFeeService + row.overdueFeeOther) | formatMoney }}</div>
                                    </template>
                                    <template v-else>
                                        <div>实收回购本金：{{ row.repurchasePrincipal | formatMoney }}</div>
                                        <div>实收回购利息：{{ row.repurchaseInterest | formatMoney }}</div>
                                        <div>实收回购罚息：{{ row.repurchasePenalty | formatMoney }}</div>
                                        <div>实收回购其他费用：{{ row.repurchaseOtherCharges | formatMoney }}</div>
                                    </template>
                                    <span slot="reference">{{ row.totalFee | formatMoney }}</span>
                                </el-popover>
                            </el-table-column>
                            <el-table-column label="资金类型" prop="capitalType">
                            </el-table-column>
                            <el-table-column label="支付通道" prop="paymentGateway">
                            </el-table-column>
                            <el-table-column label="相关凭证" prop="voucherNo">
                            </el-table-column>
                        </PagingTable>
                    </div>
                </div>

                <div class="block">
                    <h5 class="hd">退款记录</h5>
                    <div class="bd">
                        <RefundList :assetSetUuid="$route.params.assetSetId" ref="refundList"></RefundList>
                    </div>
                </div>

                <div class="block" v-if="data.guaranteeOrders.length != 0">
                    <h5 class="hd">担保补足单</h5>
                    <div class="bd">
                        <el-table
                            :data="data.guaranteeOrders"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column inline-template label="担保补足单号">
                                <a :href="`${ctx}#/finance/guarantee/complement/${row.guaranteeOrderId}/detail`">{{ row.guaranteeOrderNo }}</a>
                            </el-table-column>

                            <el-table-column prop="guaranteeDueDate" label="担保截止日">
                            </el-table-column>

                            <el-table-column prop="lastModifyTime" label="状态变更时间">
                            </el-table-column>

                            <el-table-column inline-template label="担保金额">
                                <div>{{ row.guaranteeOrderAmount | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="guaranteeOrderStatus" label="担保状态">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>

                <div class="block" v-if="data.settelementOrders.length != 0">
                    <h5 class="hd">担保清算单</h5>
                    <div class="bd">
                        <el-table
                            :data="data.settelementOrders"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="settlementOrderNo" label="清算单号">
                            </el-table-column>

                            <el-table-column prop="settlementDueDate" label="清算截止日">
                            </el-table-column>

                            <el-table-column prop="settlementOverdueDays" label="逾期天数">
                            </el-table-column>

                            <el-table-column inline-template label="逾期费用合计">
                                <div>{{ row.settlementOverduePenalty | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="lastModifyTime" label="状态变更时间">
                            </el-table-column>

                            <el-table-column inline-template label="清算金额">
                                <div>{{ row.settlementOrderAmount | formatMoney }}</div>
                            </el-table-column>

                            <el-table-column prop="settlementOrderStatus" label="状态">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>

                <div class="block">
                    <h5 class="hd">相关凭证</h5>
                    <PagingTable
                        :data="data.vouchers"
                        :pagination="true">
                        <el-table-column label="凭证编号" inline-template>
                            <a :href="row.realLink == '1' ? `${ctx}#/capital/voucher/business/${row.id}/detail` : row.realLink == '2' ? `${ctx}#/capital/voucher/active/${row.voucherNo}/detail` : `${ctx}#/capital/voucher/third-party/${row.uuid}/detail`">
                                {{ row.voucherNo }}
                            </a>
                        </el-table-column>

                        <el-table-column prop="receivableAccountNo" label="专户账户">
                        </el-table-column>

                        <el-table-column prop="paymentBank" label="往来机构名称">
                        </el-table-column>

                        <el-table-column prop="paymentName" label="账户名">
                        </el-table-column>

                        <el-table-column prop="paymentAccountNo" label="机构账户号">
                        </el-table-column>

                        <el-table-column label="凭证金额" inline-template>
                            <div> {{ row.amount | formatMoney }}</div>
                        </el-table-column>

                        <el-table-column prop="voucherTypeCn" label="凭证类型">
                        </el-table-column>

                        <el-table-column prop="content" label="凭证内容">
                        </el-table-column>

                        <el-table-column prop="voucherSource" label="凭证来源">
                        </el-table-column>

                        <el-table-column prop="status" label="凭证状态">
                        </el-table-column>
                    </PagingTable>
                </div>

    			<div class="block">
    				<SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="data.repaymentPlanUuid"></SystemOperateLog>
    			</div>
            </div>
        </div>

        <InvalidateModal
            :assetSetId="$route.params.assetSetId"
            v-model="invalidateModal.show"
            @submit="onSubmitInvalidte">
        </InvalidateModal>

        <ModifyRemarkModal
            :model="remarkModal.remarkModel"
            :assetSetId="$route.params.assetSetId"
            v-model="remarkModal.show"
            @submit="onSubmitModifyRemark">
        </ModifyRemarkModal>

        <ModifyOverdueStatusModal
            :model="overdueStatusModal.overdueStatusModel"
            :auditOverdueStatusList="auditOverdueStatusList"
            :assetSetId="$route.params.assetSetId"
            :actualRecycleDate="data.actualRecycleDate"
            v-model="overdueStatusModal.show"
            @submit="onSubmitModifyOverdueStatus">
        </ModifyOverdueStatusModal>

        <SplitOrderModal
            @submit="fetchDetail($route.params.assetSetId);"
            :repayment-plan-uuid="data.repaymentPlanUuid"
            v-model="splitOrderModal.show">
        </SplitOrderModal>

        <RefundModal
            v-model="refundModal.visible"
            @submit="fetchDetail($route.params.assetSetId);"
            :model="refundModal.model"
            :actualChargesDetail="data.actualChargesDetail">
        </RefundModal>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { mapState } from 'vuex';
    import InvalidateModal from './include/InvalidateModal';
    import ModifyRemarkModal from './include/ModifyRemarkModal';
    import ModifyOverdueStatusModal from './include/ModifyOverdueStatusModal';
    import SplitOrderModal from './include/SplitOrderModal';
    import RefundModal from './include/RefundModal';
    import Pagination  from 'mixins/Pagination';
    import PagingTable from 'views/include/PagingTable';
    import StretchTableWithQuery from 'views/include/StretchTableWithQuery';
    import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';
    import format from 'filters/format';
    import { REGEXPS } from 'src/validators';
    import RefundList from './include/RefundList';

    export default {
        mixins: [Pagination],
        components: {
            SystemOperateLog,TabMenu,TabMenuItem, TabContent, TabContentItem,
            InvalidateModal,
            ModifyRemarkModal,
            ModifyOverdueStatusModal,
            SplitOrderModal,
            RefundModal,
            StretchTableWithQuery,
            PagingTable,
            RefundList
        },
        data: function() {
            return {
                fetching: false,
                submiting: false,
                submitingFloat: false,
                refreshRepaymentOrders: false,
                splitOrderModal: {
                    show: false,
                    model: {

                    }
                },
                refundModal: {
                    visible: false,
                    model: {
                        assetUuid: '',
                        repaymentPlanNo: '',
                        contractId: ''
                    }
                },
                invalidateModal: {
                    show: false
                },
                remarkModal: {
                    show: false,
                    remarkModel: {
                        comment: ''
                    }
                },
                overdueStatusModal: {
                    show: false,
                    overdueStatusModel: {
                        overdueStatus: '',
                        overdueDate: '',
                        selectReason: '',
                        reason: ''
                    }
                },
                auditOverdueStatusList: {},
                data: {
                    actualChargesDetail: {},
                    intransitChargesDetail: {},
                    paidUpChargesDetail: {},

                    overdueChargesDetail: [],
                    planChargesDetail: {},
                    settelementOrders: [],
                    guaranteeOrders: [],
                    normalOrders: [],
                    deductApplications: [],
                    paymentRecords: [],
                    vouchers: [],

                    refundRecord: [],
                },
                isShowOverdueFee: false,
                autoload: false,
                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 3
                },

                pageCondsFloat: {
                    pageIndex: 1,
                    perPageRecordNumber: 5
                },
                mutableFeeFirst: [],
                isShowMutableFee: false,

                tabSelected: 'actualTabMenuItem',
                tabSelected1: 'repaymentOrders',
                repaymentPlanNo: '',
                mutableFeeList: [],
                mutableFeePages: 0,
                originalMutableCharge: {
                    originalAssetInterestValue: '',
                    originalServiceCharge: '',
                    originalMaintenanceCharge: '',
                    originalOtherCharge: ''
                },
                mutableLoading: false,
                fetchingMutableFee:true,

                createMutablerFeeTag: false,
                mutableFeeReasonCode: [],
                repaymentHistorys: [],
            };
        },
        computed: {
            action: function() {
                return `/assets/overdueFee/query/`+this.$route.params.assetSetId
            },
            invertAuditOverdueStatusList: function() {
                var res = {};
                var auditOverdueStatusList = this.auditOverdueStatusList;
                for (var key in auditOverdueStatusList) {
                    var value = auditOverdueStatusList[key];
                    res[value] = key;
                }
                return res;
            },
            planChargesDetail: function() {
                return this.data.planChargesDetail;
            },
            drawerMsg: function() {
                return this.isShowOverdueFee ? '收起': '展开';
            },
            MutableFeeDrawerMsg: function() {
                return this.isShowMutableFee ? '收起': '展开';
            },
            conditions: function() {
                return Object.assign({}, this.pageConds);
            },
            mutableConditions: function() {
                return Object.assign({},this.pageCondsFloat);
            }
        },
        watch: {
            'pageCondsFloat.pageIndex': function() {
                this.mutableLoading = true;
                this.fetchMutableFee();
            },
            remarkModel: {
                immediate: true,
                handler: function(cur) {
                    setTimeout(() => {
                        this.data = Object.assign({}, this.data, cur);
                    }, 0);
                }
            },
            overdueStatusModal: {
                immediate: true,
                handler: function(cur) {
                    setTimeout(() => {
                        this.data = Object.assign({}, this.data, cur);
                    }, 0);
                }
            },
            isShowOverdueFee: {
                immediate: true,
                handler: function(cur) {
                    if (!cur) {
                       this.pageConds.pageIndex = 1;
                    }
                }
            },

            isShowMutableFee: {
                immediate: true,
                handler: function(cur) {
                    if (!cur) {
                        this.pageCondsFloat.pageIndex = 1;
                    }
                }
            },
            mutableFeeList: function(cur) {
                if(!this.isShowMutableFee){
                    this.mutableFeeFirst = cur.slice(0,1);
                }else{
                    this.mutableFeeFirst = cur;
                }
            },
        },
        activated: function() {
            this.tabSelected = 'actualTabMenuItem';
            this.tabSelected1 = 'repaymentOrders';
            this.isShowOverdueFee = false;
            this.isShowMutableFee = false;
            this.createMutablerFeeTag = false;
            this.refreshRepaymentOrders = true;
            //重置mutableFeeList
            this.mutableFeeList = [];
            this.fetchDetail(this.$route.params.assetSetId);
            this.queryOverdueFee(this.$route.params.assetSetId);
        },
        methods: {
            renderHeader0: function(h, { column, $index }) {
                return h('el-popover', {
                    props: {
                        trigger: "hover",
                        placement: "top"
                    }
                }, [
                    h('div', { slot: 'reference' }, column.label),
                    h('div', {}, '最初计划还款利息: '+this.originalMutableCharge.originalAssetInterestValue)
                ]);
            },
            renderHeader1: function(h, { column, $index }) {
                return h('el-popover', {
                    props: {
                        trigger: "hover",
                        placement: "top"
                    }
                }, [
                    h('div', { slot: 'reference' }, column.label),
                    h('div', {}, '最初贷款服务费: '+this.originalMutableCharge.originalServiceCharge)
                ]);
            },
            renderHeader2: function(h, { column, $index }) {
                return h('el-popover', {
                    props: {
                        trigger: "hover",
                        placement: "top"
                    }
                }, [
                    h('div', { slot: 'reference' }, column.label),
                    h('div', {}, '最初技术维护费: '+this.originalMutableCharge.originalMaintenanceCharge)
                ]);
            },
            renderHeader3: function(h, { column, $index }) {
                return h('el-popover', {
                    props: {
                        trigger: "hover",
                        placement: "top"
                    }
                }, [
                    h('div', { slot: 'reference' }, column.label),
                    h('div', {}, '最初其他费用: '+this.originalMutableCharge.originalOtherCharge)
                ]);
            },
            handleMutableFee: function(){
                if(!this.isShowMutableFee){
                    this.isShowMutableFee = !this.isShowMutableFee;
                    this.mutableFeeFirst = this.mutableFeeList;
                }else{
                    this.isShowMutableFee = !this.isShowMutableFee;
                    this.mutableFeeFirst = this.mutableFeeList.slice(0,1);
                }
            },
            handleRefund: function() {
                this.refundModal.visible = true;
                this.refundModal.model.assetUuid = this.$route.params.assetSetId;
                this.refundModal.model.repaymentPlanNo = this.data.repaymentPlanNo;
                this.refundModal.model.contractId = this.data.contractId;
            },
            setRowClass: function(row, index) {
                if (index === 0 && row.title =='实收') {
                    return 'green';
                }
            },
            fetchDetail: function(assetSetUuid) {
                this.fetching = true;
                if (this.$refs.refundList) {
                    this.$refs.refundList.fetch()
                }
                return ajaxPromise({
                    url: `/assets/${assetSetUuid}/detail?format=json`
                }).then(resp => {
                    resp.data.overdueChargesDetail.forEach(item => {
                        item.editting = false; // 必须先注册这个属性？
                    });
                    Object.assign(this.data, resp.data);
                    this.mutableFeeReasonCode = resp.mutableFeeReasonCode;
                    this.repaymentPlanNo = resp.data.repaymentPlanNo;
                    this.auditOverdueStatusList = resp.auditOverdueStatusList;
                    this.repaymentHistorys = resp.data.repaymentRecordDetails || [];
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                })

            },
            queryOverdueFee: function(assetSetUuid) {
                this.getData({
                    url: `/assets/overdueFee/query/${assetSetUuid}`,
                    data: this.conditions
                })
            },
            modifyRemark: function() {
                var {
                    remarkModal,
                    data
                } = this;
                remarkModal.show = true;
                remarkModal.remarkModel = {
                    comment: data.comment
                };
            },
            modifyOverdueStatus: function() {
                var {
                    overdueStatusModal,
                    invertAuditOverdueStatusList,
                    data
                } = this;
                overdueStatusModal.show = true;
                overdueStatusModal.overdueStatusModel = {
                    overdueStatus: invertAuditOverdueStatusList[data.overdueStatus]
                }
            },
            onSubmitInvalidte: function() {
                this.fetchDetail(this.$route.params.assetSetId);
                this.queryOverdueFee(this.$route.params.assetSetId);

            },
            onSubmitModifyRemark: function(cur) {
                var {
                    remarkModal,
                    data
                } = this;
                data.comment = cur.comment;
                remarkModal.show = false;
            },
            onSubmitModifyOverdueStatus: function(cur) {
                var { overdueStatusModal} = this;
                overdueStatusModal.show = false;
                this.fetchDetail(this.$route.params.assetSetId);
            },
            onEditCharges: function(index) {
                var row = this.data.overdueChargesDetail[index];

                row._overdueFeeObligation = row.overdueFeeObligation;
                row._overdueFeeOther = row.overdueFeeOther;
                row._overdueFeePenalty = row.overdueFeePenalty;
                row._overdueFeeService = row.overdueFeeService;

                row.editting = true;
            },
            onCancelCharges: function(index) {
                var row = this.data.overdueChargesDetail[index];

                row.overdueFeeObligation = row._overdueFeeObligation;
                row.overdueFeeOther = row._overdueFeeOther;
                row.overdueFeePenalty = row._overdueFeePenalty;
                row.overdueFeeService = row._overdueFeeService;

                row.editting = false;
            },
            onSubmitUpdatedCharges: function(index) {
                var row = this.data.overdueChargesDetail[index];
                var { overdueFeeObligation, overdueFeeOther, overdueFeePenalty, overdueFeeService } = row;

                if (this.submiting) return;
                this.submiting = true;
                ajaxPromise({
                    url: `/assets/charges`,
                    type: 'post',
                    data: {
                        type: 'overdue',
                        repaymentPlanUuid: this.data.repaymentPlanUuid,
                        overdueCharges: JSON.stringify({
                            overdueFeeObligation, overdueFeeOther, overdueFeePenalty, overdueFeeService
                        })
                    }
                }).then(data => {
                    row.totalOverdueFee = (+overdueFeeObligation) + (+overdueFeeOther) + (+overdueFeePenalty) + (+overdueFeeService);
                    row.editting = false;
                    this.fetchDetail(this.$route.params.assetSetId);
                    this.queryOverdueFee(this.$route.params.assetSetId);
                    this.$refs.sysLog.fetch();
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.submiting = false;
                });
            },
            fetchMutableFee: function(){
                this.fetchingMutableFee = true;
                this.mutableLoading = true;
                ajaxPromise({
                    url: `/assets/mutableFee/query/${this.repaymentPlanNo}`,
                    data: Object.assign({},this.mutableConditions)
                }).then(data => {
                    this.mutableLoading = false;
                    this.mutableFeeList = data.list;
                    this.mutableFeePages = data.size;
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.mutableLoading = false
                    this.fetchingMutableFee = false
                });

                ajaxPromise({
                    url: `/assets/mutableFee/queryOriginal/${this.repaymentPlanNo}`,
                }).then(data => {
                    try {
                        this.originalMutableCharge.originalAssetInterestValue = format.formatMoney(data.model.originalAssetInterestValue);
                        this.originalMutableCharge.originalServiceCharge = format.formatMoney(data.model.originalServiceCharge);
                        this.originalMutableCharge.originalMaintenanceCharge = format.formatMoney(data.model.originalMaintenanceCharge);
                        this.originalMutableCharge.originalOtherCharge = format.formatMoney(data.model.originalOtherCharge);
                    } catch (err) {
                        console.log(err);
                    }
                })
            },
            freshDetail: function() {
                this.refreshRepaymentOrders = true;
                this.fetchDetail(this.$route.params.assetSetId);
                this.fetchMutableFee();
                this.$refs.sysLog.fetch();
            },
            createMutablerFee: function() {
                this.createMutablerFeeTag = true;
                var createItem = {
                    editting: true,
                    repaymentPlanNo: this.repaymentPlanNo,
                    reasonCode: 0,
                    assetInterestValue: '',
                    serviceCharge: '',
                    maintenanceCharge: '',
                    otherCharge: '',
                    comment: '',
                };
                this.mutableFeeFirst.unshift(createItem);
            },
            onSaveMutablerFee: function(row) {
                var money
                var rightMoney = true;

                if (row.assetInterestValue && !REGEXPS.MONEY.test(row.assetInterestValue)) {
                    rightMoney = false;
                } else if (row.serviceCharge &&  !REGEXPS.MONEY.test(row.serviceCharge)) {
                    rightMoney = false;
                } else if (row.maintenanceCharge && !REGEXPS.MONEY.test(row.maintenanceCharge)) {
                    rightMoney = false;
                } else if (row.otherCharge  && !REGEXPS.MONEY.test(row.otherCharge)) {
                    rightMoney = false;
                }
                if(!rightMoney) {
                    MessageBox.open('金额格式有误');
                }

                if (!rightMoney || this.submitingFloat) return;
                this.submitingFloat = true;

                ajaxPromise({
                    url: '/assets/mutableFee/modify',
                    type: 'post',
                    data: row
                }).then(data => {
                    this.createMutablerFeeTag = false;
                    var self = this;
                    MessageBox.once('closed', function() {
                        self.freshDetail();
                    });
                    MessageBox.open('浮动费用新增成功');
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.submitingFloat = false;
                })
            },
            onCancelMutablerFee: function() {
                if (this.submitingFloat) return;
                this.createMutablerFeeTag = false;
                this.mutableFeeFirst.shift();
            },
        },
    }
</script>
