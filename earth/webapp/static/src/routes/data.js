export default [
    //贷款合同
    {
        path: '/data/contracts',
        meta: {
            mkey: 'submenu-assets-contract'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/data/contracts/List'))
            }, 'data');
        }
    },
    {
        path: '/data/contracts/detail',
        meta: {
            mkey: 'submenu-assets-contract'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/data/contracts/Detail'))
            }, 'data');
        }
    },
    // 导入资产包
    {
        path: '/data/loan-batch',
        meta: {
            mkey: 'submenu-assets-package-loan_batch'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/data/loan-batch/List'))
            }, 'data');
        }
    },//附件导入
    {
        path: '/data/loan-batch/appendix',
        meta: {
            mkey: 'submenu-assets-package-loan_batch'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/data/loan-batch/appendix/List'))
            }, 'data');
        }
    },
    // 放款计划
    {
        path: '/data/remittance/application',
        meta: {
            mkey: 'submenu-remittance-application'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/data/remittance/application/List'))
            }, 'data');
        }
    },
    {
        path: '/data/remittance/application/:id/detail',
        meta: {
            mkey: 'submenu-remittance-application'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/data/remittance/application/Detail'))
            }, 'data');
        }
    },
    {
        path: '/data/remittance/plan',
        meta: {
            mkey: 'submenu-remittance-plan'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/data/remittance/plan/List'))
            }, 'data');
        }
    },
    {
        path: '/data/remittance/plan/:id/detail',
        meta: {
            mkey: 'submenu-remittance-plan'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/data/remittance/plan/Detail'))
            }, 'data');
        }
    },
    // 还款计划
    {
        path: '/data/repayment-plan',
        name: 'repaymentPlanList',
        meta: {
            mkey: 'submenu-repayment-plan'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/data/repayment-plan/List'))
            }, 'data');
        }
    },
    //逾期管理
    {
        path: '/data/overduemanager',
        meta: {
            mkey:"submenu-overdue-asset"
        },
        component: resolve => {
            require.ensure([],() => {
                resolve(require('views/data/overduemanager/List'))
            }, 'data');
        }
    }
];
