define(function(require, exports, module) {
    var FormDialogView = require('baseView/baseFormView').FormDialogView;
    var popupTip = require('component/popupTip');
    var WithdrawDialogView = require('./include/withdraw-dialog');
    var AreaSelectView = require('component/areaSelect');
    var $ = jQuery;

    var EditBankCardDialog = FormDialogView.extend({
        template: _.template($('#EditBankCardDialogTmpl').html()),
        initialize: function() {
            EditBankCardDialog.__super__.initialize.apply(this, arguments);
            this.$('.selectpicker').selectpicker();
            this.areaSelect = new AreaSelectView({
                el: this.$('.area-select')
            });
        },
        submitHandler: function(e) {
            e.preventDefault();
        }
    });
    var VirtualAccountDetailView = Backbone.View.extend({
        el: '.content',
        events: {
            'click .bind': 'onClickBindBankCard',
            'click .edit': 'onClickEditBankCard',
            'click .add': 'onClickAddBankCard',
            'click #withdraw': 'onClickWithdraw'
        },
        onClickWithdraw: function(e) {
            e.preventDefault();
            var withDrawDialog = new WithdrawDialogView();
            withDrawDialog.show();
        },
        onClickAddBankCard: function(e) {
            e.preventDefault();
            var addBankCardDialog = new EditBankCardDialog();
            addBankCardDialog.show();
        },
        onClickBindBankCard: function(e) {
            e.preventDefault();
        },
        onClickEditBankCard: function(e) {
            e.preventDefault();
            var editBankCardDialog = new EditBankCardDialog();
            editBankCardDialog.show();

        },
        initialize: function() {
            VirtualAccountDetailView.__super__.initialize.apply(this, arguments);
        }
    });
    module.exports = VirtualAccountDetailView;

});