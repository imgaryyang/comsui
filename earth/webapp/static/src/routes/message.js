export default [
    {
        path: '/message',
        meta: {
            mkey: 'submenu-message-activate'
        },
        component: resolve => {
            require.ensure([], () => {
                resolve(require('views/message/List'))
            }, 'message')
        }
    }
];
