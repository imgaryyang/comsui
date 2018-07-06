import './style.scss';
import Vue from 'vue';

export default Vue.component('repayment-account-item', {
    functional: true,
    render: function(h,ctx) {
        var item = ctx.props.item;
        return h('li', ctx.data, [
            h('div', { attrs: { class: 'title' } }, [item.paymentAccountNo]),
            h('div', { attrs: { class: 'subtitle' } }, [
                [item.paymentName],
                h('span', { attrs: { class: 'supplement' } }, [`(${item.paymentBank ? item.paymentBank : ''})`])
            ])
        ]);
    },
    props: {
        item: { type: Object, required: true}
    }
});