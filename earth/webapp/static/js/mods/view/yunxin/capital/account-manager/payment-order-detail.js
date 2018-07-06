define(function(require, exports, module) {
    var FormDialogView = require('baseView/baseFormView').FormDialogView;
    var popupTip = require('component/popupTip');
    var $ = jQuery;

    var PaymentOrderDetailView = Backbone.View.extend({
        el: '.content',
        events: {
            'click #refund': 'onClickRefund'
        },
        initialize: function() {
            PaymentOrderDetailView.__super__.initialize.apply(this, arguments);
            this.initModel();
            this.listenTo(this.model, 'refund-success', this.onRefundSuccess);
        },
        onRefundSuccess: function() {

        },
        initModel: function() {
            var journalVoucherUuid = this.$('#journalVoucherUuid').val();
            var bookingAmount = this.$('#bookingAmount').val();
            var sourceDocumentNo = this.$('#sourceDocumentNo').val();
            var counterPartyAccount = this.$('#counterPartyAccount').val();
            var journalVoucherNo = this.$('#journalVoucherNo').val();

            var model = new Backbone.Model({
                journalVoucherUuid: journalVoucherUuid,
                bookingAmount: bookingAmount,
                sourceDocumentNo: sourceDocumentNo,
                counterPartyAccount: counterPartyAccount,
                journalVoucherNo: journalVoucherNo
            });

            this.model = model;
        },
        onClickRefund: function(e) {
            e.preventDefault();
            var refundDialogView = new RefundDialogView({
                model: this.model
            });
            refundDialogView.show();
        }
    });

    var RefundDialogView = FormDialogView.extend({
        template: _.template($('#RefundTmpl').html()),
        initialize: function() {
            RefundDialogView.__super__.initialize.apply(this, arguments);
        },
        save: function() {
            var self = this;
            var data = this.extractDomData();
            data.journalVoucherUuid = this.model.get('journalVoucherUuid');
            var opt = {
                url: '/capital/customer-account-manage/payment-order-list/refund',
                data: data,
                type: 'post',
                dataType: 'JSON'
            };

            opt.success = function(resp) {
                if (resp.code == 0) {
                    var dialog = new RefundDialogSuccessView({
                        model: self.model
                    });
                    dialog.show();
                    self.hide();
                } else {
                    popupTip.show(resp.message);
                }
            };

            $.ajax(opt);
        },
        submitHandler: function(e) {
            e.preventDefault();
            this.save();
        }
    });

    var RefundDialogSuccessView = FormDialogView.extend({
        template: _.template($('#RefundSuccessTmpl').html()),
        initialize: function() {
            RefundDialogSuccessView.__super__.initialize.apply(this, arguments);
        },
        submitHandler: function(e) {
            e.preventDefault();
            this.model.trigger('refund-success');
            this.hide();
        }
    });

    module.exports = PaymentOrderDetailView;

});