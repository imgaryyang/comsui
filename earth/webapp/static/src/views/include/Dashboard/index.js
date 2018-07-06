import Vue from 'vue';
import MessageBox from 'components/MessageBox';

const Constructor = Vue.extend(require('./Dashboard'));
const instance = new Constructor({el: document.createElement('div')});
const Dashboard = {
    open: function(name, options) {
        document.body.appendChild(instance.$el);
        instance.open(name, options);
    },
    close: function() {
        instance.close(name);
    }
};

export default Dashboard;