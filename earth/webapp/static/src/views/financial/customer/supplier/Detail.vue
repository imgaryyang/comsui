<style lang="sass">
	
</style>

<template>
	<div class="content">
		<div class="scroller" v-loading="fetching">
			<Breadcrumb :routes="[{ title: '供应商管理'},{ title: '供应商信息详情'}]">
                <el-button @click="handlerEdit" type="primary" size="small" v-if="supplier">编辑</el-button>
			</Breadcrumb>
            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">供应商信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>供应商全称: {{ supplier.name }}</p>
                                <p>公司法人: {{ supplier.legalPerson }}</p>
                                <p>公司营业执照: {{ supplier.businessLicence }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
			<div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">
                        银行信息
                        <el-button type="text" style="padding: 0 10px; font-weight: normal;" @click="createBank">新增银行卡</el-button>
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="bankCards"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="accountNo" label="账户号">
                            </el-table-column>
                            <el-table-column prop="accountName" label="账户名">
                            </el-table-column>
                            <el-table-column prop="bankName" label="开户行">
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="开户行所在地">
                                <div>{{ row.province }} {{ row.city }}</div>
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="操作">
                                <template>
                                    <a href="#" @click.prevent="editBank(row)">编辑</a>
                                    <a href="#" @click.prevent="deleteBank(row)">删除</a>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>

				<div class="block">
					<h5 class="hd">业务信息</h5>
                    <div class="bd">
                    	<el-table
                            :data="sisms"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column :label="$utils.locale('financialContract.name')" prop="financialContractName"></el-table-column>
                            <el-table-column 
                                inline-template
                                :label="$utils.locale('financialContract.no')">
                                <a :href="`${ctx}#/financial/contract/${row.financialContractUuid}/detail`">{{ row.financialContractNo }}</a>
                            </el-table-column>
                            <el-table-column label="贷款合同" prop="contractNo" inline-template>
                                <a :href="`${ctx}#/data/contracts/detail?id=${row.contractId}`">{{ row.contractNo }}</a>
                            </el-table-column>
                        </el-table>
                    </div>
                    <div class="ft text-align-center" v-if="sisms.length">
                        <a href="javascript:void(0)" class="drawer" @click="isShowPageControl = !isShowPageControl">
                            <span class="msg">{{ drawerMsg }}</span>
                            <i class="icon icon-up-down"  v-bind:class="{active: isShowPageControl}"></i>
                        </a>
                        <PageControl 
                            v-model="pageConds.pageIndex"
                            v-if="isShowPageControl"
                            :size="sisms.length"
                            :per-page-record-number="pageConds.perPageRecordNumber">
                        </PageControl>
                    </div>
				</div>

				<div class="block">
    				<SystemOperateLog 
                        ref="sysLog"
                        :for-object-uuid="uuid"></SystemOperateLog>
    			</div>
			</div>
		</div>

        <EditInfoModal
            v-model="editInfoModal.show"
            @submit="refreshDetail"
            :model="editInfoModal.model">
        </EditInfoModal>

        <EditBankModal 
            :outerIdentifier="uuid"
            @submit="refreshDetail"
            v-model="bankModal.show" 
            :model="bankModal.model">
        </EditBankModal>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

	export default {
        components: {
            SystemOperateLog: require('views/include/SystemOperateLog'),
            EditBankModal: require('./include/EditBankModal'),
            EditInfoModal: require('./include/EditInfoModal')
        },
		data: function() {
			return {
				fetching: false,

                supplier: {},
                bankCards: [],
                sisms: [],

                editInfoModal: {
                    model: {},
                    show: false
                },

                bankModal: {
                    show: false,
                    isUpdate: false,
                    model: {}
                },

                isShowPageControl: false,
                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                },

			}
		},
		computed: {
            uuid: function() {
                return this.$route.params.uuid;
            },
            drawerMsg: function() {
                return this.isShowPageControl ? '收起' : '展开';
            },
            showDataList: function() {
                return this.isShowPageControl ? this.paginationList : this.paginationList.slice(0,3);
            },
            paginationList: function() {
                var { pageIndex, perPageRecordNumber } = this.pageConds;
                var start = (pageIndex - 1) * perPageRecordNumber;
                var end = start + perPageRecordNumber;
                return this.sisms ? this.sisms.slice(start, end) : this.sisms;
            }
		},
        activated: function() {
            if (this.$route.params.uuid) {
                this.fetchDetail();
            }  
        },
		methods: {
            fetchDetail: function() {
                if (this.fetching) return;
                this.fetching = true;
                ajaxPromise({
                    url: `/supplier/detail`,
                    data: {
                        uuid: this.uuid
                    }
                }).then(data => {
                   this.supplier = data.supplier || {};
                   this.bankCards = data.bankCards || [];
                   this.sisms = data.sisms || [];
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            },
			handlerEdit: function() {
                var { editInfoModal } = this;
                editInfoModal.model = {
                    supplierUuid: this.supplier.uuid,
                    supplierName: this.supplier.name,
                    legalPerson: this.supplier.legalPerson,
                    businessLicence: this.supplier.businessLicence,
                };
                editInfoModal.show = true;
            },
            refreshDetail: function() {
                this.fetchDetail();
                this.$refs.sysLog.fetch();
            },
            createBank: function() {
                var { bankModal } = this;
                bankModal.model = {};
                bankModal.isUpdate = false;
                bankModal.show = true;
            },
            deleteBank: function(bank) {
                if (this.bankCards.length == 1) {
                    MessageBox.open('银行卡只剩一张，不可删除');
                    return
                }

                MessageBox.open('确认删除该银行信息', '提示', [{
                    text: '确认',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: `/bankCard/delete`,
                            data: {
                                uuid: bank.uuid
                            }
                        }).then(data => {
                            MessageBox.once('closed', () => this.refreshDetail());
                            MessageBox.open('操作成功');
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                }, {
                    text: '关闭',
                    handler: () => MessageBox.close()
                }])
            },
            editBank: function(bank) {
                var { bankModal } = this;
                bankModal.model = Object.assign({}, bank);
                bankModal.isUpdate = true;
                bankModal.show = true;
            }
		}
	}
</script>