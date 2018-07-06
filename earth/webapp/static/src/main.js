import Vue from 'vue';
import VueRouter from 'vue-router';
import store from './store';
import router from './routes';
import directives from 'directives/';
import filters from 'filters/';
import components from 'components/globals';
import { ifRoleGranted, ifElementGranted, ifUrlGranted } from './authorize';
import { ctx, ctx_deprecated, resource, api, production } from './config';
import locale from './locale';
import lang from './locale/lang';


locale.use(lang);

Vue.prototype.production = production;
Vue.prototype.ctx = ctx;
Vue.prototype.ctx_deprecated = ctx_deprecated;
Vue.prototype.resource = resource;
Vue.prototype.api = api;
Vue.prototype.location = location;
Vue.prototype.$utils = { locale };
Vue.prototype.ifAnyGranted = ifRoleGranted; // removing...
Vue.prototype.ifRoleGranted = ifRoleGranted;
Vue.prototype.ifElementGranted = ifElementGranted;

Object.keys(components).forEach(function(name) {
    Vue.component(name, components[name]);
});

Object.keys(directives).forEach(function(name) {
    Vue.directive(name, directives[name]);
});

Object.keys(filters).forEach(function(name) {
    Vue.filter(name, filters[name]);
});


const App = Vue.extend(require('views/App'));

router.beforeEach((to, from, next) => {
    var { auth, mkey } = to.meta;

    ifUrlGranted(auth, mkey)
        .then(next)
        .catch(type => {
            switch (type) {
                case 'NOT_LOGGED_IN':
                    location.assign('/login');
                    // next('login');
                    break;
                case 'NO_PERMISSION':
                    next('/403');
                    break;
                case 'ERROR':
                    next('/error');
                    break;
            }
        });
});

const app = new App({
    store,
    router
}).$mount('#app');
