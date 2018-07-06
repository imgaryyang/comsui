define(function(require, exports, module) {
    var popupTip = require('component/popupTip');
    var TableContentView = require('baseView/tableContent');
    var RepurchaseModel = require('./repurchase-model').RepurchaseModel;
    var $ = jQuery;
    var root = global_config.root;

    var TableFieldView = TableContentView.extend({
        actions: {
            detail: root + '/paymentchannel/config/detail'
        },
        initialize: function() {
            TableFieldView.__super__.initialize.apply(this, arguments);
            this.model = new RepurchaseModel();
            this.$showPopover = $('.statistical-amount');

            this.listenTo(this, 'show-popover:amountStatistics', this.showAmountStatisticsPopover);
        },
        showAmountStatisticsPopover: function(queryAmount) {
            if (!queryAmount) return;
            this.queryAmountStatistics();
        },
        queryAmountStatistics: function() {
            var data = this.collectParams();
            var opt = {
                dataType: 'json',
                url: global_config.root + '/repurchasedoc/amountStatistics',
                data: data
            };
            opt.success = function(resp) {
                var popover = this.$showPopover.data('bs.popover');
                if (resp.code == 0) {

                    var repurchaseAmount = 0;
                    if (!_.isEmpty(resp.data)) {
                        repurchaseAmount = resp.data.repurchaseAmount.formatMoney(2,'');
                    }
                    
                    var content = '<div><span class="text-muted">回购金额:</span>' + repurchaseAmount +'</div>';
                    popover.options.content = content;

                } else {
                    popover.options.content = resp.message;
                }
                this.$showPopover.popover('show');
            }.bind(this);

            $.ajax(opt);
        }
    });
    module.exports = TableFieldView;
});