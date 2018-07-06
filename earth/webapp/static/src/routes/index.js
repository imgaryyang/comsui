import Vue from 'vue';
import VueRouter from 'vue-router';
import CapitalRouter from './capital';
import DataRouter from './data';
import FinanceRouter from './finance';
import FinancialRouter from './financial';
import SystemRouter from './system';
import ReportformRouter from './reportform';
import MessageRouter from './message';

Vue.use(VueRouter);

// 为后续菜单的导航做准备
var TypicalLayoutRouter = [
    {
        path: '/',
        component: require('views/Layout'),
        children: [
            {
                path: '/403',
                meta: {
                    auth: false,
                },
                component: require('views/403')
            },
            {
                path: '/error',
                meta: {
                    auth: false,
                },
                component: require('views/Error')
            },
            ...CapitalRouter,
            ...DataRouter,
            ...FinanceRouter,
            ...FinancialRouter,
            ...SystemRouter,
            ...ReportformRouter,
            ...MessageRouter
        ]
    }
];

var NoLayoutRouter = [
    {
        path: '/login',
        component: require('views/Login')
    },
    {
        path: '/welcome',
        component: require('views/Welcome')
    },
    {
        path: '/faq',
        component: require('views/include/Dashboard/FAQ/List'),
        children: [
            {
                path: '',
                component: require('views/include/Dashboard/FAQ/include/MainLeft')
            },
            {
                path: 'base',
                component: require('views/include/Dashboard/FAQ/include/BaseQuestions')
            },
            {
                path: 'process',
                component: require('views/include/Dashboard/FAQ/include/ProcessQuestions')
            },
            {
                path: 'operation',
                component: require('views/include/Dashboard/FAQ/include/OperationQuestions')
            },
            {
                path: 'error',
                component: require('views/include/Dashboard/FAQ/include/ErrorQuestions')
            },
            {
                path: 'search/:id',
                name: 'searchRouter',
                component: require('views/include/Dashboard/FAQ/include/SearchResult')
            }
        ]
    },
];


const router = new VueRouter({
    routes: [
        ...TypicalLayoutRouter,
        ...NoLayoutRouter
    ]
});

export default router;
