import { ajaxPromise } from 'assets/javascripts/util';

export function login(username, password, verifyCode) {
    // to do
}

export function register() {
    // to do
}

export function getPrincipalInfo(username) {
    return new Promise((resolve, reject) => {
        ajaxPromise({
            url: `/get-auth-menus`,
            data: {
                username
            },
            parse: data => {
                data.authority =  data.authority ? data.authority.split(/\s*,\s*/) : null;
                return data;
            }
        }).then(data => {
            resolve(data)
        }).catch(reject);
    });
}

export function getElementAuthority(username, mkey, onprogress) {
    return new Promise((resolve, reject) => {
        ajaxPromise({
            url: `/get-auth-buttons`,
            data: {
                username,
                mkey
            },
            progress: typeof onprogress === 'function' ? onprogress : undefined
        }).then(data => {
            resolve(data)
        }).catch(reject);
    });
}


