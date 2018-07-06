export default [
    {
        path: '/reportform/project-information',
        meta: {
            mkey: 'submenu-project-information'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/reportform/project/List'))
            }, 'reportform');
        }
    },
    {
        path: '/reportform/loans',
        meta: {
            mkey: 'submenu-report-form-loans'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/reportform/loans/List'))
            }, 'reportform');
        }
    },
    {
        path: '/reportform/interest',
        meta: {
            mkey: 'submenu-report-form-interest'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/reportform/interest/List'))
            }, 'reportform');
        }
    },
    {
        path: '/reportform/tasks',
        meta: {
            mkey: 'submenu-report-job'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/reportform/tasks/List'))
            }, 'reportform');
        }
    },
    {
        path: '/reportform/operation-data',
        meta: {
            mkey: 'submenu-report-form-operation-data'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/reportform/operation-data/List'))
            }, 'reportform');
        }
    },
    {
        path: '/reportform/overdue-graph',
        meta: {
            mkey: 'submenu-report-form-overdue-graph'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/reportform/json2graph/List'))
            }, 'reportform');
        }
    },
    {
        path: '/reportform/first-overdue-rate',
        meta: {
            mkey: 'submenu-first-overdue-rate'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/reportform/first-overdue-rate/List'))
            }, 'reportform');
        }
    }
];
