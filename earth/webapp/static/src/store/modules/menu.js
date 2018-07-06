import * as types from '../mutationTypes';

const state = {
    menus: [],
    filter: function(conds) {
        var { menus } = this;
        var condKeys = Object.keys(conds);
        var results = menus.filter(menu => {
            return condKeys.every(condKey => conds[condKey] === menu[condKey]);
        });
        return results.sort((a, b) => a.seqNo - b.seqNo);
    },
    getByKey: function(mkey) {
        var menu = this.filter({ mkey: mkey });
        return menu.length ? menu[0] : null;
    },
    getSubmenus: function(parentMkey, systemMenuLevel) {
        var conds = { parentMkey };
        if (systemMenuLevel != null) {
            conds.systemMenuLevel = systemMenuLevel;
        }

        return this.filter(conds);
    },
    getMenuPath: function(mkey) {
        var results = [];

        var find = (mkey) => {
            var menu = this.getByKey(mkey);
            if (!menu) return;
            results.unshift(menu);
            find(menu.parentMkey);
        };

        find(mkey);

        return results;
    }
};

const mutations = {
    [types.GET_PRINCIPAL_MENUS_SUCCESS]: function(state, data) {
        state.menus = data;
    },
    [types.GET_AUTH_ELEMENTS]: function(state, { mkey, elements }) {
        const menu = state.getByKey(mkey);
        Object.assign(menu, { authElements: elements });
    }
};

export default {
    state,
    mutations
};