define(function(require, exports, module) {
    require('component/autocomplete');
    var TableContentView = require('baseView/tableContent');
    var popupTip = require('component/popupTip');
    var root = global_config.root;

    var PModel = Backbone.Model.extend({
        refund: function(data) {
            data.journalVoucherUuid = this.get('uuid');

            var self = this;

            var opt = {
                url: root + '/capital/customer-account-manage/payment-order-list/refund',
                data: data,
                type: 'post',
                dataType: 'JSON'
            };

            opt.success = function(resp) {
                self.trigger('refund:success', resp);
            };

            opt.complete = function() {
                self.trigger('refund:complete');
            };

            this.trigger('refund:start');

            $.ajax(opt);
        }
    });

    var PaymentOrderListView = TableContentView.extend({
        events: {
            'click .refund': 'onClickRefund',
            'click .voucher': 'onClickVoucherLink'
        },
        packParams: function($target) {
            var data = {};
            data.uuid = $target.data('uuid');
            data.sourceDocumentNo = $target.data('sourceDocumentNo');
            data.counterPartyAccount = $target.find('.counter-party-account').text();
            data.bookingAmount = $target.find('.booking-amount').text();
            data.counterPartyName = $target.find('.counter-party-name').text();
            data.journalVoucherNo = $target.find('.journal-voucher-no').text();
            return data;
        },
        onClickVoucherLink: function(e) {
            e.preventDefault();

            var opt = {
                url: root + '/capital/customer-account-manage/payment-order-list/query-voucher-detail',
                data: {
                    sourceDocumentUuid: $(e.target).closest('tr').data('source-document-uuid')
                },
                dataType: 'json'
            };

            opt.success = function(resp) {
                if (resp.code == 0) {
                    var voucher = resp.data.voucher;
                    var href = '';

                    if (!voucher) return;

                    if (voucher.voucherSource === '商户付款凭证') {
                        href = root + '/voucher/business/detail/' + voucher.id;
                    } else if (voucher.voucherSource === '主动付款凭证') {
                        href = root + '/voucher/active#/capital/voucher/active/' +  voucher.voucherNo + '/detail';
                    } else if (voucher.voucherSource === '第三方扣款凭证') {
                        href = root + '/voucher/thirdParty#/capital/voucher/third-party/' + voucher.uuid + '/detail';
                    }

                    href && location.assign(href);
                } else {
                    popupTip.show(resp.message);
                }
            };

            $.ajax(opt);
        },
        onClickRefund: function(e) {
            e.preventDefault();

            var $target = $(e.target);
            if ($target.is('.disabled')) return;

            var self = this;
            var $parent = $target.parents('tr');
            var attr = this.packParams($parent);
            var model = new PModel(attr);

            model.on('refund:start', function() {
                $target.addClass('disabled');
            });

            model.on('refund:success', function(resp) {
                if (resp.code == 0) {
                    var successTmpl = _.template($('#RefundSuccessTmpl').html());
                    popupTip.show(successTmpl(model.toJSON()));
                    self.query();
                } else {
                    popupTip.show(resp.message);
                }
            });

            model.on('refund:complete', function() {
                $target.removeClass('disabled');
            });

            var template = _.template($('#RefundTmpl').html());

            popupTip.show(template(model.toJSON()), '退款', [{
                text: '关闭',
                style: 'default',
                handler: function() {
                    this.hide();
                }
            }, {
                text: '确定',
                style: 'success',
                handler: function() {
                    var appendix = this.$('#appendix').val();
                    model.refund({
                        appendix: appendix
                    });
                }
            }]);
        }

    });

    exports.PaymentOrderListView = PaymentOrderListView;

});