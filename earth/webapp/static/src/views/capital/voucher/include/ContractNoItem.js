import './style.scss';
import Vue from 'vue';

export default Vue.component('contract-no-item', {
    functional: true,
    render: function (h, ctx) {
        var item = ctx.props.item;
        return h('li', ctx.data, [
            h('div', { attrs: { class: 'title' } }, [item.contract.contractNo]),
            h('div', { attrs: { class: 'subtitle' } }, [
                [item.financialContractName],
                h('span', { attrs: { class: 'supplement' } }, [`（${item.financialContractNo}）`])
            ])
        ]);
    },
    props: {
        item: { type: Object, required: true }
    }
});