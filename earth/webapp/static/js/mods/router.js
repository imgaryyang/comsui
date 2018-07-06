// router.js
define(function(require, exports, module) {
    require('component/dashboard');
    require('initialize/routine');
    var path = require('scaffold/path');

    var AppRouters = window.AppRouters = [];

    // 只能在当前host进行跳转
    AppRouters.navigate = function(href, options) {
        options || (options = {});

        if (options.external) {
            location.assign(href);
            return;
        }

        var origin;

        if (typeof href === 'string') {
            origin = {
                path: href
            };
        } else if (typeof href === 'object') {
            origin = href;
        } else {
            throw new Error('参数不正确');
        }

        var url = path.format(origin);

        if (options.absolute) {
            var root = Backbone.history.root;
            url = path.join(root, path.format(origin));
        }

        location.assign(url);
    };


    // 云信资产管理
    var AssertRouter = Backbone.Router.extend({
        routes: {
            'loan-batch': 'loadAssetCreat',
            'assets/:assetId/detail': 'loadPaymentManageDetail',
            'assets': 'loadAssetList',
            'contracts': 'loadContractList',
            'contracts/detail': 'loadContractDetail',
            'project-information': 'loadProjectInformationList',

            'financialContract': 'loadFinancialContractList',
            'offline-payment-manage/payment/list': 'loadOfflinePaymentList',

            'payment-manage/order/list': 'loadOrderList',
            'payment-manage/payment/list': 'loadPaymentList',
            'interfacePayment/list': 'loadInterfacePaymentlist',

            'payment-manage/order/:assetId/detail': 'loadOrderDetail',

            'guarantee/order': 'loadGuaranteeOrderList',
            'guarantee/order/:orderId/guarantee-detail': 'loadGuaranteeDetail',

            'settlement-order': 'loadSettlementOrder',

            'reportform/loans': 'loadLoansReportFormList',
            'reportform/interest': 'loadInterestReportFormList',

            'remittance/application': 'loadPlanOrderList',
            'remittance/application/details/:orderNo': 'loadPlanOrderDetail',
            'remittance/plan/details/:remittancePlanUuid': 'loadLoanDetail',
            'remittance/plan': 'loadLoanList',


            'voucher/business': 'loadVoucherList',
            'voucher/business/detail/:detailId': 'loadVoucherDetail',

            'deduct/application': 'loadDeductApplicationList',

            'voucher/active/detail/:detailId': 'loadActivePaymentVoucherDetail',
            'voucher/active': 'loadActivePaymentList',
            'voucher/active/create': 'loadActivePaymentCreate',
            'repurchasedoc/list': 'loadRepurchaseList',
            'repurchasedoc/detail?rduid=:rduid': 'loadRepurchaseDetail',

            'assets/prepayment/list': 'loadPrepaymentList',
            'assets/prepayment/:assetUuid/detail': 'loadPrepaymentDetail'

        },
        loadAssetCreat: function() {
            seajs.use('view/yunxin/asset/asset-create', function(ExportView) {
                var view = new ExportView();
                console.log('page: 资产包导入');
            });
        },
        loadPaymentManageDetail: function(assetId) {
            seajs.use('view/yunxin/payment-manage/payment-manage-detail', function(ExportView) {
                var view = new ExportView({
                    assetId: assetId
                });
                console.log('page: 还款单详情');
            });
        },
        loadAssetList: function() {
            seajs.use('view/yunxin/payment-manage/asset-list', function(ExportView) {
                var view = new ExportView();
                console.log('page: 还款列表');
            });
        },
        loadContractList: function() {
            seajs.use('view/yunxin/asset/contract/list', function(ExportView) {
                var view = new ExportView();
                console.log('page: 贷款合同列表');
            });
        },
        loadContractDetail: function(search) {
            var query = path.parseQueryString(search);
            var attr = {};
            query.id && (attr.contractId = query.id);
            query.uid && (attr.uniqueId = query.uid);
            seajs.use('view/yunxin/asset/contract/detail', function(ExportView) {
                var view = new ExportView(attr);
                console.log('page: 贷款合同详情');
            });
        },
        loadProjectInformationList: function() {
            seajs.use('view/yunxin/asset/project-information', function(ExportView) {
                var view = new ExportView();
                console.log('page: 项目信息列表');
            });
        },

        loadOrderList: function() {
            seajs.use('view/yunxin/payment-manage/order-list', function(ExportView) {
                var view = new ExportView();
                console.log('page: 结算单列表');
            });
        },
        loadPaymentList: function() {
            seajs.use('view/yunxin/payment-manage/online-payment-list', function(ExportView) {
                var view = new ExportView();
                console.log('page: 线上支付单列表');
            });
        },

        loadInterfacePaymentlist: function() {
            seajs.use('view/yunxin/payment-manage/interface-online-payment-list', function(ExportView) {
                var view = new ExportView();
                console.log('page:  接口线上支付单');
            });
        },

        loadFinancialContractList: function() {
            seajs.use('view/tableContent', function(ExportView) {
                var view = new ExportView();
                console.log('page: 信托管理列表');
            });
        },
        loadOrderDetail: function(orderId) {
            seajs.use('view/yunxin/payment-manage/order-detail', function(ExportView) {
                var view = new ExportView({
                    orderId: orderId
                });
                console.log('page: 结算单详情');
            });
        },

        loadOfflinePaymentList: function() {
            seajs.use('view/yunxin/payment-manage/offline-payment-list', function(ExportView) {
                var view = new ExportView();
                console.log('page:线下支付单列表');
            });
        },

        loadGuaranteeOrderList: function() {
            seajs.use('view/yunxin/payment-manage/guarantee-list', function(ExportView) {
                var view = new ExportView();
                console.log('page: 担保补足列表');
            });
        },
        loadGuaranteeDetail: function(orderId) {
            seajs.use('view/yunxin/payment-manage/guarantee-detail', function(ExportView) {
                var view = new ExportView(orderId);
                console.log('page: 担保补足详情');
            });
        },

        loadSettlementOrder: function() {
            seajs.use('view/tableContent', function(ExportView) {
                var view = new ExportView();
                console.log('page: 担保清算列表');
            });
        },

        loadLoansReportFormList: function() {
            seajs.use('view/yunxin/reportform/loans-reportform', function(ExportView) {
                var view = new ExportView();
                console.log('page: 贷款规模管理');
            });
        },
        loadInterestReportFormList: function() {
            seajs.use('view/yunxin/reportform/interest-reportform', function(ExportView) {
                var view = new ExportView();
                console.log('page: 应收利息管理');
            });
        },

        loadPlanOrderList: function() {
            seajs.use('view/yunxin/loan-manage/plan-order-list', function(ExportView) {
                var view = new ExportView();
                console.log('page: 计划订单列表');
            });
        },
        loadPlanOrderDetail: function() {
            seajs.use('view/yunxin/loan-manage/plan-order-detail', function(ExportView) {
                var view = new ExportView();
                console.log('page:计划订单详情');
            });
        },

        loadLoanDetail: function() {
            seajs.use('view/yunxin/loan-manage/loan-detail', function(ExportView) {
                var view = new ExportView();
                console.log('page: 放款单详情');
            });
        },
        loadLoanList: function() {
            seajs.use('view/yunxin/loan-manage/loan-list', function(ExportView) {
                var view = new ExportView();
                console.log('page: 放款单列表');
            });
        },

        loadVoucherList: function() {
            seajs.use('view/yunxin/voucher/voucher-list', function(ExportView) {
                var view = new ExportView();
            });
        },
        loadVoucherDetail: function(detailId) {
            seajs.use('view/yunxin/voucher/voucher-detail', function(ExportView) {
                var view = new ExportView(detailId);
            });
        },

        loadDeductApplicationList: function() {
            seajs.use('view/yunxin/payment-manage/deduct-list', function(ExportView) {
                var view = new ExportView();
                console.log('page: 扣款订单列表');
            });
        },
        loadActivePaymentList: function() {
            seajs.use('view/tableContent', function(ExportView) {
                var view = new ExportView();
                console.log('page: 主动还款凭证列表');
            });
        },
        loadActivePaymentVoucherDetail: function(detailId) {
            seajs.use('view/yunxin/voucher/active-payment-voucher-detail', function(ExportView) {
                var view = new ExportView(detailId);
                console.log('page:主动还款凭证详情');
            });
        },
        loadActivePaymentCreate: function() {
            seajs.use('view/yunxin/voucher/active-payment-create', function(ExportView) {
                var view = new ExportView();
                console.log('page:新增主动还款凭证');
            });
        },
        loadRepurchaseList: function() {
            seajs.use('view/yunxin/repurchase/list', function(ExportView) {
                var view = new ExportView();
                console.log('page: 商户回购列表');
            });
        },
        loadRepurchaseDetail: function(rduid) {
            seajs.use('view/yunxin/repurchase/detail', function(ExportView) {
                var view = new ExportView(rduid);
                console.log('page: 商户回购详情');
            });
        },
        loadPrepaymentList: function() {
            seajs.use('view/tableContent', function(ExportView) {
                var view = new ExportView();
                console.log('page: 提前还款');
            });
        },
        loadPrepaymentDetail: function() {
            window.Pagination.find('.online-order-list');
        }
    });
    AppRouters.push(new AssertRouter());

    var PaymentChannelRouter = Backbone.Router.extend({
        routes: {
            'paymentchannel': 'loadChannelConfigList',
            'paymentchannel/config/:paymentChannelUuid': 'loadChannelConfigEdit',
            'paymentchannel/config/transactiondetail/:paymentChannelUuid': 'loadChannelConfigDetail',
            'paymentchannel/efficentanalysis': 'loadChannelEfficentAnalysis',
            'paymentchannel/switch/list': 'loadSwitchList',
            'paymentchannel/switch/strategy/:financialContractUuid': 'loadSwitchStrategy',
            'paymentchannel/limitSheet/list': 'loadQuotaList',
            'paymentchannel/switch/detail/:financialContractUuid': 'loadChannelDetail'
        },

        loadChannelConfigList: function() {
            seajs.use('view/yunxin/channel/config-list', function(ExportView) {
                var view = new ExportView();
            });
        },
        loadChannelConfigEdit: function(paymentChannelUuid) {
            seajs.use('view/yunxin/channel/config-edit', function(ExportView) {
                var view = new ExportView(paymentChannelUuid);
            });
        },
        loadChannelConfigDetail: function(paymentChannelUuid) {
            seajs.use('view/yunxin/channel/config-detail', function(ExportView) {
                var view = new ExportView(paymentChannelUuid);
            });
        },
        loadChannelEfficentAnalysis: function(paymentChannelUuid) {
            seajs.use('view/yunxin/channel/efficent-analysis', function(ExportView) {
                var view = new ExportView(paymentChannelUuid);
            });
        },
        loadSwitchList: function() {
            seajs.use('view/yunxin/channel/switch-list', function(ExportView) {
                var view = new ExportView();
                console.log('page: 通道切换列表');
            });
        },
        loadSwitchStrategy: function(financialContractUuid) {
            seajs.use('view/yunxin/channel/switch-strategy', function(ExportView) {
                var view = new ExportView(financialContractUuid);
                console.log('page: 通道策略');
            });
        },
        loadQuotaList: function() {
            seajs.use('view/yunxin/channel/quota-list', function(ExportView) {
                var view = new ExportView();
                console.log('view/yunxin/channel/quota-list');
            });
        },
        loadChannelDetail: function(financialContractUuid) {
            seajs.use('view/yunxin/channel/channel-detail', function(ExportView) {
                var view = new ExportView(financialContractUuid);
            });
        }
    });
    AppRouters.push(new PaymentChannelRouter());

    var SystemRouter = Backbone.Router.extend({
        routes: {
            'show-user-list': 'loadUserList',
            'edit-user-role/:userId': 'loadEditUserRole',
            'show-user-role/:userId': 'loadShowUserRole',
            'create-user-role': 'loadCreateUserRole',
            'post-update-password': 'loadUpdatePassword'
        },
        loadUserList: function() {
            seajs.use('view/yunxin/user-role/user-role-list', function(ExportView) {
                var view = new ExportView();
                console.log('page: 用户管理列表');
            });
        },
        loadEditUserRole: function(userId) {
            seajs.use('view/yunxin/user-role/edit-user-role', function(ExportView) {
                var view = new ExportView(userId);
                console.log('page: 编辑用户');
            });
        },
        loadShowUserRole: function(userId) {
            seajs.use('view/yunxin/user-role/show-user-role', function(ExportView) {
                var view = new ExportView(userId);
                console.log('page: 用户详情');
            });
        },
        loadCreateUserRole: function() {
            seajs.use('view/yunxin/user-role/create-user-role', function(ExportView) {
                var view = new ExportView();
                console.log('page: 新增用户');
            });
        },
        loadUpdatePassword: function() {
            seajs.use('view/yunxin/principal-update-password/update-password', function(ExportView) {
                var view = new ExportView.UpdatePasswordView();
                console.log('page: 修改密码');
            });
        }
    });
    AppRouters.push(new SystemRouter());

    var CapitalRouter = Backbone.Router.extend({
        routes: {
            'capital/directbank-cash-flow': 'loadDirectbankCashFlow',
            'capital/directbank-cash-flow/search': 'loadDirectbankCashFlow',
            'capital/refundbill': 'loadAdvanceCancelList',
            'capital/plan/execlog': 'loadOnlineAdvanceList',

            'capital/account-manager/cash-flow-audit/show': 'loadAccountManager',
            'capital/customer-account-manage/deposit-receipt-list/show': 'loadDepositReceiptList',
            'capital/customer-account-manage/virtual-account-list/show': 'loadVirtualAccountList',
            'capital/customer-account-manage/account-flow-list/show': 'loadAccountFlowList',
            'capital/customer-account-manage/payment-order-list/show': 'loadPaymentOrderList',
            'capital/customer-account-manage/refund-order-list/show': 'loadRefundOrderList',
            'capital/customer-account-manage/recharge-revoke-list/show': 'loadRechargeRevokeList',
            'payment-order-datail.jsp': 'loadPaymentOrderDetail',
            'virtual-account-detail.jsp': 'loadVirtualAccountDetail'
        },

        loadDirectbankCashFlow: function() {
            seajs.use('view/yunxin/capital/directbank-cash-flow', function(ExportView) {
                // var view = new ExportView();
            });
        },

        loadVirtualAccountDetail: function() {
            seajs.use('view/yunxin/capital/account-manager/virtual-account-detail', function(ExportView) {
                var view = new ExportView();
            });
        },
        loadPaymentOrderDetail: function() {
            seajs.use('view/yunxin/capital/account-manager/payment-order-detail', function(ExportView) {
                var view = new ExportView();
            });
        },
        loadAdvanceCancelList: function() {
            seajs.use('view/yunxin/loan-manage/advance-cancel-list', function(ExportView) {
                var view = new ExportView();
                console.log('page: 代付撤销单');
            });
        },
        loadOnlineAdvanceList: function() {
            seajs.use('view/yunxin/loan-manage/online-advance-list', function(ExportView) {
                var view = new ExportView();
            });
        },

        loadAccountManager: function() {
            seajs.use('view/yunxin/capital/account-manager/cash-flow-audit', function(ExportView) {
                var view = new ExportView();
            });
        },
        loadDepositReceiptList: function() {
            seajs.use('view/yunxin/capital/account-manager/deposit-receipt-list', function(ExportView) {
                var view = new ExportView();
            });
        },
        loadRechargeRevokeList: function() {
            seajs.use('view/tableContent', function(ExportView) {
                var view = new ExportView();
            });
        },
        loadVirtualAccountList: function() {
            seajs.use('view/yunxin/capital/account-manager/virtual-account-list', function(ExportView) {
                var view = new ExportView();
            });
        },
        loadAccountFlowList: function() {
            seajs.use('view/tableContent', function(ExportView) {
                var view = new ExportView();
            });
        },
        loadPaymentOrderList: function() {
            seajs.use('view/yunxin/capital/account-manager/payment-order-list', function(ExportView) {
                var view = new ExportView.PaymentOrderListView();
            });
        },
        loadRefundOrderList: function() {
                seajs.use('view/tableContent', function(ExportView) {
                    var view = new ExportView();
                });
            }
            // ,
            // loadUserLoginLog: function() {
            //     seajs.use('view/tableContent', function(ExportView) {
            //         console.log('用户日志');
            //         var view = new ExportView();
            //     });
            // }
    });
    AppRouters.push(new CapitalRouter());


    // 其他页面
    var RoutineRouter = Backbone.Router.extend({
        routes: {
            'welcome': 'loadWelcome',
            'notice/notice-show': 'loadNoticeList'
        },
        loadWelcome: function() {
            seajs.use('view/yunxin/welcome/welcome', function(ExportView) {
                var view = new ExportView.WelcomeView();
                console.log('welcome');
            });
        },
        loadNoticeList: function() {
            seajs.use('view/yunxin/notice/notice-list', function(ExportView) {
                var view = new ExportView.NoticeListView();
                console.log('notice-list');
            });
        }
    });
    AppRouters.push(new RoutineRouter());
});