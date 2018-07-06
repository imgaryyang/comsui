import * as types from '../mutationTypes';

const state = {
    financialContractIds: [],
    financialContractUuids: []
};

const mutations = {
    [types.SET_CACHE_QUERY]: function(state, payload) {
        const possess = Object.keys(state);

        Object
            .keys(payload)
            .filter(key => possess.includes(key))
            .forEach(key => state[key] = payload[key]);
    }
};

export default {
    state,
    mutations
};