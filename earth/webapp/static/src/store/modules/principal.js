import * as api from 'api/principal';
import * as types from '../mutationTypes';

const state = {
    id: '',
    username: '',
    companyId: '',
    authority: '',
    modifyPasswordTimes: ''
};

const actions = {
    // 在全局勾子内会先获取principal的信息，所以没有必要定义action了
    // getPrincipalInfo: function({commit, state}, userId) {
    //     api.getPrincipalInfo(userId)
    //         .then(data => {
    //             commit(types.GET_PRINCIPAL_INFO_SUCCESS, data);
    //         });
    // }
};

const mutations = {
    [types.GET_PRINCIPAL_INFO_SUCCESS]: function(state, data) {
        Object.assign(state, data);
    },
    [types.INCREASE_MODIFY_PASSWORD_TIMES]: function(state, times) {
        const previous = + state.modifyPasswordTimes;
        state.modifyPasswordTimes = previous + 1;
    }
};

export default {
    state,
    actions,
    mutations
};