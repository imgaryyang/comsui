export default [
    {
        path: '/system/update-password',
        meta: {
            mkey: 'submenu-user-center'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/system/UpdatePassword'))
            }, 'system');
        }
    },
    {
        path: '/system/personal',
        meta: {
            mkey: 'submenu-user-center'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/system/personal/Personal'))
            }, 'system');
        }
    },
    {
        path: '/system/personal/secret-key-info',
        meta: {
            mkey: 'submenu-user-center'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/system/personal/include/SecretKeyInfo'))
            }, 'system');
        }
    },
    {
        path: '/system/user',
        meta: {
            mkey: 'submenu-role-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/system/user/List'))
            }, 'system');
        }
    },
    {
        path: '/system/user/create',
        meta: {
            mkey: 'submenu-role-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/system/user/Edit'))
            }, 'system');
        }
    },
    {
        path: '/system/user/:id',
        meta: {
            mkey: 'submenu-role-list'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/system/user/Detail'))
            }, 'system');
        }
    },
    {
        path: '/system/role',
        meta: {
            mkey: 'submenu-show-systemrole'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/system/role/List'))
            }, 'system');
        }
    },
    {
        path: '/system/role/create',
        meta: {
            mkey: 'submenu-show-systemrole'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/system/role/Edit'))
            }, 'system');
        }
    },
    {
        path: '/system/role/:id/edit',
        meta: {
            mkey: 'submenu-show-systemrole'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/system/role/Edit'))
            }, 'system');
        }
    },
    {
        path: '/system/notice/show',
        meta: {
            mkey: 'submenu-notice-manage'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/system/notice/List'))
            }, 'system');
        }
    },
    {
        path: '/system/user-log',
        meta: {
            mkey: 'submenu-user-login-log'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/system/UserLog'))
            }, 'system');
        }
    },
    {
        path: '/system/interface',
        meta: {
            mkey: 'submenu-custom-interface'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/system/interface/CustomInterface'))
            }, 'editor');
        }
    },
    {
        path: '/system/tagmanage/list',
        meta: {
            mkey:'submenu-tag'
        },
        component: resolve => {
            require.ensure([], ()=> {
                resolve(require('views/system/tagmanage/List'), 'system')
            })
        }
    },
    {
        path: '/system/tagmanage/:id/detail',
        meta: {
            mkey:'submenu-tag'
        },
        component: resolve => {
            require.ensure([], ()=> {
                resolve(require('views/system/tagmanage/Detail'),'system')
            })
        }
    },
    {
        path: '/system/tagmanage/:id/edit',
        meta: {
            mkey:'submenu-tag'
        },
        component: resolve => {
            require.ensure([], ()=> {
                resolve(require('views/system/tagmanage/Edit'),'system')
            })
        }
    },
    {
        path: '/system/message',
        meta: {
            mkey:'submenu-system-message'
        },
        component: resolve => {
            require.ensure([], ()=> {
                resolve(require('views/system/message/List'), 'system')
            })
        }
    },
];