export default [
    //信托合同
    {
        path: '/financial/contract/create',
        meta: {
             mkey: 'submenu-financial-contract'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/contract/Create'))
            }, 'financial');
        }
    },
    {
        path: '/financial/contract',
        meta: {
            mkey: 'submenu-financial-contract'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/contract/List'))
            }, 'financial');
        }
    },
    {
        path: '/financial/contract/:financialContractUuid/detail',
        name: 'financialContractdetail',
        meta: {
            mkey: 'submenu-financial-contract',
            auth: false
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/contract/Detail'))
            }, 'financial');
        }
    },
    {
        path: '/financial/contract/:financialContractUuid/edit/basic',
        name: 'editFinancialBasic',
        meta: {
            mkey: 'submenu-financial-contract'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/contract/EditBasicInfo'))
            }, 'financial');
        }
    },
    {
        path: '/financial/contract/:financialContractUuid/edit/remittance',
        name: 'editFinancialRemittance',
        meta: {
            mkey: 'submenu-financial-contract'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/contract/EditRemittanceInfo'))
            }, 'financial');
        }
    },
    {
        path: '/financial/contract/:financialContractUuid/edit/repay',
        name: 'editFinancialRepay',
        meta: {
            mkey: 'submenu-financial-contract',
            auth:false
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/contract/EditRepayInfo'))
            }, 'financial');
        }
    },
    //通道管理
    {
        path: '/financial/channel/configure/list',
        name: 'channelConfigList',
        meta: {
            mkey: 'submenu-channel-config'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/channel/configure/List'))
            }, 'financial');
        }
    },
    {
        path: '/financial/channel/configure/:paymentChannelUuid/detail',
        meta: {
            mkey: 'submenu-channel-config'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/channel/configure/Detail'))
            }, 'financial');
        }
    },
    {
        path: '/financial/channel/configure/:paymentChannelUuid/edit',
        meta: {
            mkey: 'submenu-channel-config'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/channel/configure/Edit'))
            }, 'financial');
        }
    },
    {
        path: '/financial/channel/configure/create',
        name: 'channelConfigCreate',
        meta: {
            mkey: 'submenu-channel-config'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/channel/configure/Create'))
            }, 'financial');
        }
    },
    {
        path: '/financial/channel/switch/list',
        name: 'switchList',
        meta: {
            mkey: 'submenu-channel-switch'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/channel/switch/List'))
            }, 'financial');
        }
    },
    {
        path: '/financial/channel/switch/detail/:financialContractUuid',
        name: 'switchDetail',
        meta: {
            mkey: 'submenu-channel-switch'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/channel/switch/Detail'))
            }, 'financial');
        }
    },
    {
        path: '/financial/channel/efficentanalysis',
        meta: {
            mkey: 'submenu-channel-efficentanalysis'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/channel/EfficentAnalysis'))
            });
        }
    },
    {
        path: '/financial/channel/limit-sheet',
        meta: {
            mkey: 'submenu-channel-limit'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/channel/LimitSheet'))
            });
        }
    },
    //客户管理
    {
        path: '/financial/customer/financial-app',
        meta: {
            mkey: 'submenu-app'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/customer/financial-app/List'))
            });
        }
    },
    {
        path: '/financial/customer/financial-app/:appId/detail',
        meta: {
            mkey: 'submenu-app'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/customer/financial-app/Detail'))
            });
        }
    },
    {
        path: '/financial/customer/financial-app/create',
        meta: {
            mkey: 'submenu-app'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/customer/financial-app/Create'))
            });
        }
    },
    {
        path: '/financial/customer/supplier',
        meta: {
            mkey: 'submenu-supplier'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/customer/supplier/List'))
            });
        }
    },
    {
        path: '/financial/customer/supplier/:uuid/detail',
        meta: {
            mkey: 'submenu-supplier'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/customer/supplier/Detail'))
            });
        }
    },
    {
        path: '/financial/customer/supplier/create',
        meta: {
            mkey: 'submenu-supplier'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/customer/supplier/Create'))
            });
        }
    },
    {
        path: 'financial/customer/loan-customer',
        meta: {
            mkey: 'submenu-customer'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/customer/loan-customer/List'))
            });
        }
    },
    {
        path: 'financial/customer/loan-customer/:customerUuid/detail',
        meta: {
            mkey: 'submenu-customer'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/customer/loan-customer/Detail'))
            });
        }
    },
    //项目人员
    {
        path: 'financial/position',
        meta: {
            mkey: 'submenu-position'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/financial/position/List'))
            });
        }
    }
];
