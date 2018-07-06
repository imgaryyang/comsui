export default [
    // 凭证
    {
        path: '/capital/voucher/business',
        meta: {
            mkey: 'submenu-voucher-business'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/business/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/business/create',
        meta: {
            mkey: 'submenu-voucher-business'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/business/Edit'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/business/:id/detail',
        meta: {
            mkey: 'submenu-voucher-business'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/business/Detail'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/active',
        meta: {
            mkey: 'submenu-voucher-active'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/active/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/active/:id/detail',
        meta: {
            mkey: 'submenu-voucher-active'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/active/Detail'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/active/:voucherNo/edit',
        meta: {
            mkey: 'submenu-voucher-active'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/active/Edit'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/active/create',
        meta: {
            mkey: 'submenu-voucher-active'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/active/Edit'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/remittance',
        meta: {
            mkey: 'submenu-voucher-remittance'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/remittance/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/remittance/:id/detail',
        meta: {
            mkey: 'submenu-voucher-remittance'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/remittance/Detail'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/third-party',
        meta: {
            mkey: 'submenu-voucher-thirdpart'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/third-party/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/third-party/:id/detail',
        name: 'submenu-voucher-thirdpartirdPartyDetail',
        meta: {
            mkey: 'submenu-voucher-thirdpart'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/third-party/Detail'))
            }, 'capital');
        }
    },
    // //专户注资凭证
    // {
    //     path: '/capital/voucher/injection',
    //     meta: {
    //         mkey: 'submenu-voucher-injection',
    //         auth: false
    //     },
    //     component: resolve => {
    //         require.ensure([], () => {
    //             resolve(require('views/capital/voucher/injection/List'));
    //         }, 'capital')
    //     }
    // },
    // {
    //     path: '/capital/voucher/injection/:id/detail',
    //     meta: {
    //         mkey: 'submenu-voucher-injection',
    //         auth: false
    //     },
    //     component: resolve => {
    //         require.ensure([], () => {
    //             resolve(require('views/capital/voucher/injection/Detail'));
    //         }, 'capital')
    //     }
    // },
    // //专户提现凭证
    // {
    //     path: '/capital/voucher/withdrawals',
    //     meta: {
    //         mkey: 'submenu-voucher-withdrawals',
    //         auth: false
    //     },
    //     component: resolve => {
    //         require.ensure([], () => {
    //             resolve(require('views/capital/voucher/withdrawals/List'));
    //         }, 'capital')
    //     }
    // },
    // {
    //     path: '/capital/voucher/withdrawals/:id/detail',
    //     meta: {
    //         mkey: 'submenu-voucher-withdrawals',
    //         auth: false
    //     },
    //     component: resolve => {
    //         require.ensure([], () => {
    //             resolve(require('views/capital/voucher/withdrawals/Detail'));
    //         }, 'capital')
    //     }
    // },
    //银企直联流水
    {
        path: '/capital/directbank',
        meta: {
            mkey: 'submenu-flow-monitor'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/directbank/List'))
            }, 'capital');
        }
    },
    // 银行对账
    {
        path: '/capital/special-account/cash-flow-audit',
        meta: {
            mkey: 'submenu-bank-cash-flow-audit'
        },

        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/special-account/cash-flow-audit/List'));
            }, 'capital');
        }
    },
    {
        path: '/capital/special-account/cash-flow-audit/:uuid/detail',
        meta: {
            mkey: 'submenu-bank-cash-flow-audit'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/special-account/cash-flow-audit/Detail'));
            }, 'capital');
        }
    },
    //账户管理
    {
        path: '/capital/special-account/account-manager',
        meta: {
            mkey: 'submenu-account-management-list',
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/special-account/account-manager/List'));
            }, 'capital');
        }
    },
    {
        path: '/capital/special-account/account-manager/:id/edit',
        meta: {
            mkey: 'submenu-account-management-list',
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/special-account/account-manager/Edit'));
            }, 'capital');
        }
    },
    //滞留单
    {
        path: '/capital/special-account/retention-voucher',
        meta: {
            mkey: 'submenu-special-account-retention-voucher'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/special-account/retention-voucher/List'));
            }, 'capital')
        }
    },
    {
        path: '/capital/special-account/retention-voucher/:no/detail',
        meta: {
            mkey: 'submenu-special-account-retention-voucher'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/special-account/retention-voucher/Detail'));
            }, 'capital')
        }
    },
    //转账单
    {
        path: '/capital/special-account/transfer',
        meta: {
            mkey: 'submenu-transfer-bill'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/special-account/transfer/List'));
            }, 'capital')
        }
    },
    {
        path: '/capital/special-account/transfer/:orderUuid/detail',
        meta: {
            mkey: 'submenu-transfer-bill'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/special-account/transfer/Detail'));
            }, 'capital')
        }
    },
    // //注资管理
    // {
    //     path: '/capital/special-account/injection',
    //     meta: {
    //         mkey: 'submenu-special-account-injection',
    //         auth: false
    //     },
    //     component: resolve => {
    //         require.ensure([], () => {
    //             resolve(require('views/capital/special-account/injection/List'));
    //         }, 'capital')
    //     }
    // },
    // {
    //     path: '/capital/special-account/injection/:id/detail',
    //     meta: {
    //         mkey: 'submenu-special-account-injection',
    //         auth: false
    //     },
    //     component: resolve => {
    //         require.ensure([], () => {
    //             resolve(require('views/capital/special-account/injection/Detail'));
    //         }, 'capital')
    //     }
    // },
    // //提现管理
    // {
    //     path: '/capital/special-account/withdrawals',
    //     meta: {
    //         mkey: 'submenu-special-account-withdrawals',
    //         auth: false
    //     },
    //     component: resolve => {
    //         require.ensure([], () => {
    //             resolve(require('views/capital/special-account/withdrawals/List'));
    //         }, 'capital')
    //     }
    // },
    // {
    //     path: '/capital/special-account/withdrawals/:id/detail',
    //     meta: {
    //         mkey: 'submenu-special-account-withdrawals'
    //         ,
    //         auth: false
    //     },
    //     component: resolve => {
    //         require.ensure([], () => {
    //             resolve(require('views/capital/special-account/withdrawals/Detail'));
    //         }, 'capital')
    //     }
    // },
    // 客户账户管理
    {
        path: '/capital/account/virtual-acctount',
        meta: {
            mkey: 'submenu-virtual-account-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/virtual-acctount/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/account/account-flow',
        meta: {
            mkey: 'submenu-account-flow-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/account-flow/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/account/deposit-receipt',
        meta: {
            mkey: 'submenu-deposit-receipt-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/deposit-receipt/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/account/recharge-revoke',
        meta: {
            mkey: 'submenu-recharge-revoke-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/recharge-revoke/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/account/deposit-receipt/:id/detail',
        meta: {
            mkey: 'submenu-deposit-receipt-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/deposit-receipt/Detail'))
            }, 'capital');
        }
    },
    {
        path: '/capital/account/payment-order',
        meta: {
            mkey: 'submenu-payment-order-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/payment-order/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/account/payment-order/:id/detail',
        meta: {
            mkey: 'submenu-payment-order-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/payment-order/Detail'))
            }, 'capital');
        }
    },
    {
        path: '/capital/account/recharge-revoke/:id/detail',
        meta: {
            mkey: 'submenu-recharge-revoke-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/recharge-revoke/Detail'))
            }, 'capital');
        }
    },
    {
        path: '/capital/account/refund-order',
        meta: {
            mkey: 'submenu-refund-order-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/refund-order/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/account/refund-order/:id/detail',
        meta: {
            mkey: 'submenu-refund-order-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/refund-order/Detail'))
            }, 'capital');
        }
    },
    {
        path: '/capital/account/virtual-acctount/:id/detail',
        meta: {
            mkey: 'submenu-virtual-account-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/virtual-acctount/Detail'))
            }, 'capital');
        }
    },
    {
        path: '/capital/account/withdraw/:id/detail',
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/withdraw/Detail'))
            }, 'capital');
        }
    },
     // 放款资金流
    {
        path: '/capital/remittance-cash-flow/plan-execlog',
        meta: {
            mkey: 'submenu-remittance-plan-execlog',
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/remittance-cash-flow/plan-execlog/List'));
            }, 'capital');
        }
    },
    {
        path: '/capital/remittance-cash-flow/plan-execlog/merge',
        meta: {
            mkey: 'submenu-remittance-plan-execlog',
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/remittance-cash-flow/plan-execlog/MergeList'));
            }, 'capital');
        }
    },
    {
        path: '/capital/remittance-cash-flow/plan-execlog/:id/detail',
        meta: {
            mkey: 'submenu-remittance-plan-execlog'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/remittance-cash-flow/plan-execlog/Detail'));
            }, 'capital');
        }
    },
    {
        path: '/capital/remittance-cash-flow/refund-bill',
        meta: {
            mkey: 'submenu-remittance-refund-bill'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/remittance-cash-flow/refund-bill/List'));
            }, 'capital');
        }
    },
    // 还款资金流
    {
        path: '/capital/payment-cash-flow/offline-payment',
        meta: {
            mkey: 'submenu-offline-payment-payment'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/payment-cash-flow/offline-payment/List'));
            }, 'capital');
        }
    },
    {
        path: '/capital/payment-cash-flow/offline-payment/:id/detail',
        meta: {
            mkey: 'submenu-offline-payment-payment'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/payment-cash-flow/offline-payment/Detail'));
            }, 'capital');
        }
    },
    {
        path: '/capital/payment-cash-flow/online-payment',
        meta: {
            mkey: 'submenu-interface-payment'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/payment-cash-flow/online-payment/List'));
            }, 'capital');
        }
    },
    {
        path: '/capital/payment-cash-flow/online-payment/:id/detail',
        meta: {
            mkey: 'submenu-interface-payment'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/payment-cash-flow/online-payment/Detail'));
            }, 'capital');
        }
    },
    {
        path: '/capital/payment-cash-flow/payment-order',
        meta: {
            mkey: 'submenu-payment-order-pay-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/payment-cash-flow/payment-order/List'));
            }, 'capital');
        }
    },
    {
        path: '/capital/payment-cash-flow/payment-order/:uuid/detail',
        meta: {
            mkey: 'submenu-payment-order-pay-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/payment-cash-flow/payment-order/Detail'));
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/third-pay',
        name: 'voucherThirdParfftyList',
        meta: {
            mkey: 'submenu-voucher-thirdpay'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/third-pay/List'))
            }, 'capital');
        }
    }, 
    {
        path: '/capital/voucher/third-pay/:id/detail',
        name: 'voucherThirdParddtyDetail',
        meta: {
            mkey: 'submenu-voucher-thirdpay'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/third-pay/Detail'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/third-pay/voucher-batch',
        name: 'voucherThirdPartyList',
        meta: {
            mkey: 'submenu-voucher-thirdpay'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/third-pay/voucher-batch/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/third-pay/voucher-batch/:id/detail',
        name: 'voucherThirdPartyVoucherBatchDettail',
        meta: {
            mkey: 'submenu-voucher-thirdpay'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/third-pay/voucher-batch/Detail'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/third-pay/channel',
        name: 'voucherThirdPartyDetail',
        meta: {
            mkey: 'submenu-voucher-thirdpay'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/third-pay/channel/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/third-pay/history-voucher',
        name: 'voucherThirdParfftyDetail',
        meta: {
            mkey: 'submenu-voucher-thirdpay'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/third-pay/history-voucher/List'))
            }, 'capital');
        }
    }, 
    {
        path: '/capital/voucher/third-pay/history-voucher/:id/detail',
        meta: {
            mkey: 'submenu-voucher-thirdpay'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/third-pay/history-voucher/Detail'))
            }, 'capital');
        }
    }, 
    //转账凭证
    {
        path: '/capital/voucher/transfer',
        meta: {
            mkey: 'submenu-voucher-transfer'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/transfer/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/transfer/:id/detail',
        meta: {
            mkey: 'submenu-voucher-transfer'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/transfer/Detail'))
            }, 'capital');
        }
    },
    //原始凭证
    {
        path: '/capital/original-voucher/reconciliation',
        meta: {
            mkey: 'submenu-third-party-audit-bill'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/original-voucher/reconciliation/List'))
            }, 'finance');
        }
    },

    {
        path: '/capital/original-voucher/counteroffer',
        meta: {
            mkey: 'submenu-external-trade-batch'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/original-voucher/counteroffer/List'))
            }, 'finance');
        }
    },
    {
        path: '/capital/original-voucher/counteroffer/:uuid/detail',
        meta: {
            mkey: 'submenu-external-trade-batch'
        },
        name: 'counterofferDetail',
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/original-voucher/counteroffer/Detail'))
            }, 'finance');
        }
    },

    //清算凭证
    {
        path: '/capital/voucher/clearing',
        meta: {
            mkey: 'submenu-clearing-voucher'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/clearing/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/voucher/clearing/:voucherUuid/detail',
        meta: {
            mkey: 'submenu-clearing-voucher'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/voucher/clearing/Detail'))
            }, 'capital');
        }
    },

    //第三方账户管理 代付 代收 ----------
    {
        path: '/capital/third-party-account/reconciliation',
        meta: {
            mkey: 'submenu-remittance-audit'
        },
        name: 'reconciliationList',
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/third-party-account/reconciliation/List'));
            }, 'capital');
        }
    },
    {
        path: '/capital/third-party-account/reconciliation/:id/detail',
        meta: {
            mkey: 'submenu-remittance-audit'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/third-party-account/reconciliation/Detail'));
            }, 'capital');
        }
    },
    {
        path: '/capital/third-party-account/beneficiary',
        meta: {
            mkey: 'submenu-beneficiary-audit'
        },
        name: 'beneficiaryList',
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/third-party-account/beneficiary/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/third-party-account/beneficiary/merge',
        meta: {
            mkey: 'submenu-beneficiary-audit'
        },
        name: 'beneficiaryMergeList',
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/third-party-account/beneficiary/MergeList'))
            }, 'capital');
        }
    },
    {
        path: '/capital/third-party-account/beneficiary/:uuid/detail',
        meta: {
            mkey: 'submenu-beneficiary-audit'
        },
        name: 'beneficiaryDetail',
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/third-party-account/beneficiary/Detail'))
            }, 'capital');
        }
    },
    //余额退款单
    {
        path: '/capital/account/refund-bill',
        meta: {
            mkey: 'submenu-refund-bill'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/refund-bill/List'))
            }, 'capital');
        }
    },
    {
        path: '/capital/account/refund-bill/:refundOrderUuid/detail',
        meta: {
            mkey: 'submenu-refund-bill'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/capital/account/refund-bill/Detail'))
            }, 'capital');
        }
    },
];