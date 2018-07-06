import store from './store';
import router from './routes';
import * as api from 'api/principal';
import * as types from './store/mutationTypes';

const getUsername = function() {
    if (process.env.NODE_ENV === 'development') {
        return localStorage.getItem('username');
    } else {
        return $('[name=username]').val();
    }
};

const setUsername = function() {
    // to do
};

const getUserId = function() {
    // 从本地缓存中读取
    var userId = $('[name=userId]').val();
    return userId;
};

const setUserId = function(userId) {
    // to do: 缓存到本地
};

const getPrincipalInfo = function(username) {
    return new Promise((resolve, reject) => {
        const { state, commit } = store;

        if (state.principal.id) {
            resolve();
            return;
        }

        api.getPrincipalInfo(username).then(data => {
            var { menus, ...principal } = data;
            commit(types.GET_PRINCIPAL_MENUS_SUCCESS, menus);
            commit(types.GET_PRINCIPAL_INFO_SUCCESS, principal);
            resolve();
        });
    });
};

const getElementAuthority = function(mkey) {
    return new Promise((resolve, reject) => {
        const { state, commit } = store;
        const menu = state.menu.getByKey(mkey);
        const username = getUsername();

        if (menu == null || menu.authElements != null) {
            resolve();
            return;
        }

        api.getElementAuthority(username, mkey)
            .then(data => {
                commit(types.GET_AUTH_ELEMENTS, { mkey: mkey, elements: data.elements ? data.elements : [] })
                resolve();
            })
            .catch(reject);
    });
}

const match = function(possess, requirement) {
    if (requirement.length == 0) {
        return true;
    }

    if (possess.length == 0 && requirement.length != 0) {
        return false;
    }

    return possess.some(one => {
        return requirement.includes(one)
    });
};

export const ifRoleGranted = function(role) {
    var roles = [];
    var authority = store.state.principal.authority || [];

    if (typeof role === 'string') {
        roles = [role];
    } else if (Array.isArray(role)) {
        roles = role;
    }

    // 只要有一属于就有权限
    return match(authority, roles);
}

export const ifElementGranted = function(elemId) {
    var mkey = router.currentRoute.meta.mkey;
    var menu = store.state.menu.getByKey(mkey);

    if (!menu || !menu.authElements) return true;

    return menu.authElements.includes(elemId);
}

export const ifUrlGranted = function(authType, mkey) {
    // 前台明确定义不需要验证 && 不是修改密码页面
    if (authType === false && mkey !== 'submenu-update-password') {
        return Promise.resolve();
    }

    const username = getUsername();
    const logined = !!username;

    // 是否登录
    if (!logined) {
        return Promise.reject('NOT_LOGGED_IN');
    }

    return getPrincipalInfo(username)
        .then(() => {
            // 前台没有显示定义校验类型，则从对应的menu中继承
            if (authType == null && mkey != null) {
                const menu = store.state.menu.getByKey(mkey);

                if (!menu) {
                    throw 'NO_PERMISSION';
                }
                
                authType = menu.auth
            }

            if (authType != null && !ifRoleGranted(authType)) {
                throw 'NO_PERMISSION';
            }
        })
        .then(() => {
            if (mkey) {
                return getElementAuthority(mkey).catch(() => { throw 'ERROR' });
            }
        });
}

export const login = function(username, password, verifyCode) {
    return api.login(username, password, verifyCode)
        .then(userId => {
            setUserId(userId);
            setUsername(username);
        });
}


