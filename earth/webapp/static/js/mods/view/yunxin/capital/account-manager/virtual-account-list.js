define(function(require, exports, module) {
    var TableContentView = require('baseView/tableContent');
    var WithdrawDialogView = require('./include/withdraw-dialog');

    var $ = jQuery;
    var $body = $('body');

    var VirtualAccountListView = TableContentView.extend({
        events: {
            'click .withdraw': 'onClickWithdraw',
            'mouseover .show-popover': 'showPopover',
            'mouseout .show-popover': 'showPopover'
        },
        showPopover: function(e) {
            var el = $(e.target);
            var popoverContent = el.siblings('.account').html();
            el.popover({
                content: popoverContent
            });
            el.popover('toggle');
        },
        onClickWithdraw: function() {
            var withdrawDialog = new WithdrawDialogView();
            withdrawDialog.show();
        }
    });

    module.exports = VirtualAccountListView;
});