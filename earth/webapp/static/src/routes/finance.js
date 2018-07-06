export default [
    {
        path: '/finance/assets',
        name: 'assetsList',
        meta: {
            mkey: 'submenu-payment-asset'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/assets/List'))
            }, 'finance');
        }
    },
    {
        path: '/finance/assets/:assetSetId/detail',
        name: 'assetsDetail',
        meta: {
            mkey: 'submenu-payment-asset'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/assets/Detail'))
            }, 'finance');
        }
    },

    {
        path: '/finance/prepayment',
        meta: {
            mkey: 'submenu-prepayment-order'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/prepayment/List'))
            }, 'finance');
        }
    },

    {
        path: '/finance/application',
        meta: {
            mkey: 'submenu-deduct'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/application/List'))
            }, 'finance');
        }
    },
    {
        path: '/finance/application/:deudctNo/detail',
        meta: {
            mkey: 'submenu-deduct'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/application/Detail'))
            }, 'finance');
        }
    },
    {
        path: '/finance/payment-order',
        meta: {
            mkey: 'submenu-payment-order'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/payment-order/List'))
            }, 'finance');
        }
    },
    {
        path: '/finance/payment-order/:orderId/detail',
        meta: {
            mkey: 'submenu-payment-order'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/payment-order/Detail'))
            }, 'finance');
        }
    },
    {
        path: '/finance/repurchasedoc',
        meta: {
            mkey: 'submenu-repurchase-doc'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/repurchasedoc/List'))
            }, 'finance');
        }
    },
    {
        path: '/finance/repurchasedoc/:rduid/detail',
        meta: {
            mkey: 'submenu-repurchase-doc'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/repurchasedoc/Detail'))
            }, 'finance');
        }
    },
    {
        path: '/finance/guarantee/complement',
        meta: {
            mkey: 'submenu-guarantee-order'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/guarantee/complement/List'))
            }, 'finance');
        }
    },
    {
        path: '/finance/guarantee/complement/:id/detail',
        meta: {
            mkey: 'submenu-guarantee-order'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/guarantee/complement/Detail'))
            }, 'finance');
        }
    },
    {
        path: '/finance/guarantee/settlement',
        meta: {
            mkey: 'submenu-settlement-order'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/guarantee/settlement/List'))
            }, 'finance');
        }
    },
    {
        path: '/finance/repayment-order',
        meta: {
            mkey: 'submenu-repayment-order-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/repayment-order/List'))
            }, 'finance');
        }
    },
    {
        path: '/finance/repayment-order/merge',
        meta: {
            mkey: 'submenu-repayment-order-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/repayment-order/MergeList'))
            }, 'finance');
        }
    },
    {
        path: '/finance/repayment-order/:uuid/detail',
        meta: {
            mkey: 'submenu-repayment-order-list'
        },
        name: 'repaymentOrder',
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/repayment-order/Detail'))
            }, 'finance');
        }
    },
    {
        path: '/finance/repayment-order/create-payment-order',
        meta: {
            mkey: 'submenu-repayment-order-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/repayment-order/PaymentOrder'))
            }, 'finance');
        }
    },
    {
        path: '/finance/repayment-order/create',
        meta: {
            mkey: 'submenu-repayment-order-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/finance/repayment-order/Create'))
            }, 'finance');
        }
    },
    {
    	 path: 'finance/repayment-record',
         meta: {
        	 mkey: 'submenu-repayment-record-list'
         },
         component: resolve => {
             require.ensure([], ()=>{
                 resolve(require('views/finance/repayment-record/List'))
             }, 'finance')
         }
    }
];