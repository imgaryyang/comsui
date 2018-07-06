<style lang="sass">
	
</style>

<template>
	<div class="content">
		<div class="scroller" v-loading="fetching">
			<Breadcrumb :routes="[{ title: '信托商户管理'},{ title: '信托商户信息详情'}]">
                <el-button @click.prevent="handlerEdit" type="primary" size="small">编辑</el-button>
			</Breadcrumb>
            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">商户公司信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>商户公司全称: {{ app.company.fullName }}</p>
                                <p>商户简称: {{ app.company.shortName }}</p>
                                <p>商户代码: {{ app.appId }}</p>
                            </div>
                            <div class="col">
                                <p>地址: {{ app.company.address }}</p>
                                <p>公司法人: {{ app.company.legalPerson }}</p>
                                <p>公司营业执照: {{ app.company.businessLicence }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
			<div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">
                        银行卡
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="appAccounts"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="accountNo" label="银行账户号">
                            </el-table-column>
                            <el-table-column prop="accountName" label="银行账户名">
                            </el-table-column>
                            <el-table-column prop="bankName" label="开户行">
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="开户行所在地">
                                <div>{{ row.province }} {{ row.city }}</div>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>

				<div class="block">
					<h5 class="hd">业务信息</h5>
                    <div class="bd">
                    	<el-table
                            :data="showDataList"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column :label="$utils.locale('financialContract.name')" prop="financialContractName"></el-table-column>
                            <el-table-column 
                                inline-template
                                :label="$utils.locale('financialContract.no')">
                                <a :href="`${ctx}#/financial/contract/${row.financialContractuuid}/detail`">{{ row.financialContractNo }}</a>
                            </el-table-column>
                            <el-table-column label="角色" prop="role"></el-table-column>
                            <el-table-column 
                                inline-template
                                label="账户编号">
                                <a :href="`${ctx}#/capital/account/virtual-acctount/${row.virtualAccountUuid}/detail`">{{ row.virtualAccountNo }}</a>
                            </el-table-column>
                        </el-table>
                    </div>
                    <div class="ft text-align-center" v-if="appInfoShows.length">
                        <a href="javascript:void(0)" class="drawer" @click="isShowPageControl = !isShowPageControl">
                            <span class="msg">{{ drawerMsg }}</span>
                            <i class="icon icon-up-down"  v-bind:class="{active: isShowPageControl}"></i>
                        </a>
                        <PageControl 
                            v-model="pageConds.pageIndex"
                            v-if="isShowPageControl"
                            :size="appInfoShows.length"
                            :per-page-record-number="pageConds.perPageRecordNumber">
                        </PageControl>
                    </div>
				</div>

				<div class="block">
    				<SystemOperateLog 
                        ref="sysLog"
                        :for-object-uuid="appId"></SystemOperateLog>
    			</div>
			</div>
		</div>

        <EditInfoModal
            v-model="editInfoModal.show"
            @submit="refreshDetail"
            :model="editInfoModal.model">
        </EditInfoModal>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

	export default {
        components: {
            SystemOperateLog: require('views/include/SystemOperateLog'),
            EditInfoModal: require('./include/EditInfoModal')
        },
		data: function() {
			return {
				fetching: false,

				isShowPageControl: false,

                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                },

                editInfoModal: {
                    model: {},
                    show: false
                },
                app: {
                    company: {}
                },
                appAccounts: [],
                appInfoShows: []
			}
		},
		computed: {
			drawerMsg: function() {
				return this.isShowPageControl ? '收起' : '展开';
			},
			showDataList: function() {
				return this.isShowPageControl ? this.paginationList : this.paginationList.slice(0,3);
			},
            appId: function() {
                return this.$route.params.appId
            },
            paginationList: function() {
                var { pageIndex, perPageRecordNumber } = this.pageConds;
                var start = (pageIndex - 1) * perPageRecordNumber;
                var end = start + perPageRecordNumber;
                return this.appInfoShows ? this.appInfoShows.slice(start, end) : this.appInfoShows;
            }
		},
        activated:  function() {
            this.isShowPageControl = false;
            this.pageConds = {
                pageIndex: 1,
                perPageRecordNumber: 12
            };

            if (this.$route.params.appId) {
                this.fetchDetail();
            }
        },
		methods: {
            fetchDetail: function() {
                if (this.fetching) return;
                this.fetching = true;
                ajaxPromise({
                    url: `/app/detail`,
                    data: {
                        appId: this.appId
                    }
                }).then(data => {
                   this.app = data.app || {};
                   this.appAccounts = data.appAccounts || [];
                   this.appInfoShows = data.appInfoShows || [];
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            },
			handlerEdit: function() {
                var { editInfoModal, app } = this;
                editInfoModal.model = {
                    id: app.id,
                    companyFullName: app.company.fullName,
                    appId: app.appId,
                    appName: app.name,
                    address: app.company.address,
                    legalPerson: app.company.legalPerson,
                    businessLicence: app.company.businessLicence
                };
                editInfoModal.show = true;
            },
            refreshDetail: function() {
                this.fetchDetail();
                this.$refs.sysLog.fetch();
            }
		}
	}
</script>