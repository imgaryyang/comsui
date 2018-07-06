import Vue from 'vue';
import Vuex from 'vuex';
import principal from './modules/principal';
import menu from './modules/menu';
import financialContract from './modules/financialContract';
import query from './modules/query';

Vue.use(Vuex);

const debug = process.env.NODE_ENV !== 'production';

export default new Vuex.Store({
    // plugins: debug ? [createLogger()] : [],
    strict: debug,
    modules: {
        menu,
        principal,
        query,
        financialContract
    }
})