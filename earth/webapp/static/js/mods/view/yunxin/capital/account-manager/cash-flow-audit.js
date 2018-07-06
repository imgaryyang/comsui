define(function(require, exports, module) {
    require('component/autocomplete');
    var TableContentView = require('baseView/tableContent');
    var popupTip = require('component/popupTip');
    var DialogView = require('component/dialogView');
    var FormDialogView = require('view/baseFormView').FormDialogView;
    var entity = require('./cash-flow-audit-entity');
    var path = require('scaffold/path');

    var CashFlowAuditModel = entity.CashFlowAuditModel;
    var CashFlowAuditCollection = entity.CashFlowAuditCollection;
    var CashBillModel = entity.CashBillModel;

    var root = global_config.root;
    var loadingImg = global_const.loadingImg;

    var CashFlowAuditListView = TableContentView.extend({
        initialize: function(accountSide) {
            CashFlowAuditListView.__super__.initialize.apply(this, arguments);
            this.initCollection();
        },
        onClickExport: function(e) {

            e.preventDefault();

            var params = this.collectParams();
            var tradeStartTimeStr = params.tradeStartTime;
            var tradeEndTimeStr = params.tradeEndTime;

            if (tradeStartTimeStr && tradeEndTimeStr) {
                var tradeStartTime = Date.parse(tradeStartTimeStr.replace(/\-/g, '/'));
                var tradeEndTime = Date.parse(tradeEndTimeStr.replace(/\-/g, '/'));
                var diffSecond = Math.floor((tradeEndTime - tradeStartTime) / 1000);
                var maxDiddSecond = 3 * 24 * 60 * 60;
                if (diffSecond > maxDiddSecond) {
                    var dialogView = new DialogView();
                    dialogView.show('时间跨度不允许超过3天！');
                    dialogView.on('goahead', function() {
                        this.hide();
                    });
                    return;
                }
            } else {
                var noTimeDialogView = new DialogView();
                noTimeDialogView.show('请选择入账起止时间!');
                noTimeDialogView.on('goahead', function() {
                    this.hide();
                });
                return;
            }

            var action = $(e.target).data('action');
            if (!action) return;

            var els = this.tableListEl.find('tbody').children();
            var nomore = els.length == 0 || els.first().is('.nomore');
            if (nomore) return;

            var url = path.format({
                path: action,
                query: params
            });

            url += '&reportId=12';
            // 没下完不能关闭
            window.open(url, '_download');
        },
        initCollection: function() {
            this.collection = new CashFlowAuditCollection();

            this.listenTo(this.collection, 'reset', function(collection, options) {
                var htm;

                if (collection.length < 1) {
                    htm = '<tr><td style="text-align: center;" colspan="100">没有更多数据</td></tr>';
                } else {
                    htm = [];
                    for (var i = 0, len = collection.length; i < len; i++) {
                        var view = new CashFlowItemView({
                            model: collection.at(i)
                        });
                        view.render();
                        htm.push(view.el);
                    }
                }

                this.tableListEl.find(' > tbody').html(htm);
            });

        },
        refreshTableDataList: function(data, opepration, response, query) {
            if (data.length < 1) {
                this.collection.reset();
            } else {
                var perfact = this.polish(data);
                this.collection.reset(perfact);
            }

            this.trigger('refreshdata.tablelist');
        }
    });

    var CashFlowItemView = Backbone.View.extend({
        tagName: 'tr',
        className: 'bill-item',
        template: _.template($('#tableFieldTmpl').html(), {
            variable: 'obj'
        }),
        events: {
            'click .btn-recharge': function(e) {
                e.preventDefault();
                this.model.set('isRecharge', true);
                this.model.expand();
            },
            'click .expand-bill': function(e) {
                e.preventDefault();
                this.model.set('isRecharge', false);
                this.model.toggle();
            }
        },
        initialize: function() {
            this.billView = new CashBillView({
                model: this.model
            });

            this.listenTo(this.model, 'expand', function() {
                this.billView.$el.insertAfter(this.$el);
                this.$el.addClass('z-active');
            });

            this.listenTo(this.model, 'collapse', function() {
                this.billView.$el.detach();
                this.$el.removeClass('z-active');
            });

            this.listenTo(this.model, 'change:auditStatusMsg', this.render);

            this.listenTo(this.model.bills, 'submited', function(resp, model) {
                if (resp.code == 0) {
                    this.model.set('isRecharge', true);
                    this.model.expand();
                    popupTip.show('充值单提交成功');
                } else if (resp.code == -6005) {
                    var htm = [
                        '<div style="margin-top: 40px;">',
                        '流水银行卡',
                        '<span class="color-danger"> ',
                        this.model.get('counterAccountName'),
                        ' ',
                        this.model.get('counterBankName'),
                        ' ',
                        this.model.get('counterAccountNo'),
                        ' </span>',
                        '与客户账户中银行卡信息不符，请修正。',
                        '</div>'
                    ].join('');

                    popupTip.show(htm, null, [{
                        text: '确定',
                        style: 'success',
                        handler: function() {
                            location.assign(root + '/v#/capital/account/virtual-acctount/' + resp.data.virtualAccountUuid + '/detail');
                        }
                    }]);
                } else {
                    popupTip.show(resp.message);
                }
            });
        },
        render: function() {
            var data = this.model.toJSON();
            var htm = this.template(data);
            this.$el.html(htm);
        }
    });

    var CashBillView = Backbone.View.extend({
        tagName: 'tr',
        className: 'bill-card-box',
        template: _.template($('#CashBillTmpl').html(), {
            variable: 'obj'
        }),
        events: {
            'click .add-bill': 'onClickAddBill'
        },
        initialize: function() {
            this.listenTo(this.model.bills, 'request', function() {
                var htm = $('<td colspan="1000" style="text-align: center;">');
                htm.append(loadingImg.clone());
                this.$el.html(htm);
            });

            this.listenTo(this.model.bills, 'destroy', function() {
                var doubtAmount = this.model.getRemainDepositAmount();
                this.$('.doubt-amount').text(doubtAmount);
            });

            var bills = this.model.bills;
            this.listenTo(bills, 'reset', this.addAllBill);
            this.listenTo(bills, 'add', this.addOneBill);
        },
        addOneBill: function(model) {
            var view = new CashBillItemView({
                model: model
            });
            view.render();
            this.$('.record-list').append(view.$el);
        },
        addAllBill: function() {
            var bills = this.model.bills;
            if (bills.length < 1) {
                this.model.set('isRecharge', true);
            }

            var data = this.model.toJSON();
            data.doubtAmount = this.model.getRemainDepositAmount();
            var $content = $(this.template(data));
            this.$el.html($content);

            for (var i = 0; i < bills.length; i++) {
                this.addOneBill(bills.at(i));
            }
        },
        onClickAddBill: function(e) {
            e.preventDefault();
            var model = new CashBillModel({}, {
                collection: this.model.bills
            });
            var dialogView = new AddBillDialog({
                model: model
            });

            this.listenToOnce(model, 'created', function(resp) {
                if (resp.code == 0) {
                    this.model.bills.add(model);
                } else {
                    popupTip.show(resp.message);
                }
            });

            dialogView.show();
        }
    });

    var CashBillItemView = Backbone.View.extend({
        tagName: 'tr',
        template: _.template($('#CashBillItemTmpl').html(), {
            variable: 'obj'
        }),
        events: {
            'click .btn-success': 'onClickSbumit',
            'click .btn-danger': 'onClickDisable'
        },
        initialize: function() {
            this.confirmDisable = new DialogView({
                cancelBtnTxt: '否',
                goaheadBtnTxt: '是'
            });

            this.listenTo(this.confirmDisable, 'goahead', function() {
                this.confirmDisable.hide();
                this.model.disable();
            });

            this.listenTo(this.model, 'disabled', function(resp) {
                resp.code != 0 && popupTip.show(resp.message);
            });

            this.listenTo(this.model, 'destroy', this.remove);

            this.listenTo(this.model, 'change', this.render);
        },
        render: function() {
            var cashFlowAuditModel = this.model.collection.cashFlowAuditModel;
            var data = this.model.toJSON();
            data.isRecharge = cashFlowAuditModel.get('isRecharge');
            var htm = this.template(data);
            this.$el.html(htm);
        },
        toastErrorTip: function(el, message) {
            var errorEl = el.prev('.error');
            var width = message.length * 12 + 30; // 12: 字体大小， 30: icon
            if (errorEl.length < 1) {
                errorEl = $('<span class="error">' + message + '</span>');
                el.before(errorEl);
            } else {
                errorEl.html(message);
            }
            errorEl.width(width).addClass('anim-fadeIn');
            var timer = el.data('timer');
            if (timer) {
                clearTimeout(timer);
            }
            timer = setTimeout(function() {
                errorEl.fadeOut(500, function() {
                    errorEl.remove();
                });
            }, 4000);
            el.data('timer', timer);
        },
        validate: function() {
            var flag = true;
            var $depositAmountEl = this.$('[name=depositAmount]');
            var $remark = this.$('[name=remark]');
            var remark = $remark.val().trim();
            var depositAmount = $depositAmountEl.val().trim();

            if (!$.isNumeric(depositAmount)) {
                flag = false;
                this.toastErrorTip($depositAmountEl, '请输入非负数');
            }

            if (!remark) {
                flag = false;
                this.toastErrorTip($remark, '请输入备注');
            }

            return flag;
        },
        onClickSbumit: function(e) {
            e.preventDefault();

            if (!this.validate()) return;

            var attr = {
                remark: this.$('[name=remark]').val().trim(),
                depositAmount: this.$('[name=depositAmount]').val().trim()
            };

            this.model.set(attr, {
                silent: true
            });
            this.model.submit();
        },
        onClickDisable: function() {
            this.confirmDisable.show('即将作废该充值账单，是否继续？');
        }
    });

    var AddBillDialog = FormDialogView.extend({
        id: 'createBillDialog',
        template: _.template($('#AddBillDialogTmpl').html()),
        events: {
            'keyup [name=keyWord]': 'onKeyupKeyWord'
        },
        initialize: function() {
            AddBillDialog.__super__.initialize.apply(this, arguments);

            var self = this;
            this.$customerTypeMsg = this.$('[name=customerTypeMsg]');
            this.$financialContractName = this.$('[name=financialContractName]');

            this.$('[name=keyWord]').autocomplete({
                action: './query-customer',
                container: this.$('.auto-complete-list'),
                parse: function(resp) {
                    return resp.code == 0 ? resp.data.modelList : [];
                },
                search: function(inputValue) {
                    return {
                        name: inputValue,
                        customerType: self.$customerTypeMsg.val(),
                        financialContractUuid: self.$financialContractName.val()
                    };
                },
                parcelItem: function(item) {
                    var str = '<li class="item" data-customeruuid="' + item.customerUuid + '">' +
                        '<span class="title">' + item.name + '(' + item.number + ')' + '</span>' +
                        '</li>';
                    return str;
                },
                onSubmit: function(input, itemEl) {
                    input.val(itemEl.text())
                        .data('customeruuid', itemEl.data('customeruuid'));
                }
            });

            this.defineValidate();
        },
        defineValidate: function() {
            $.validator.addMethod('existCustomerUuid', function(value, element) {
                return $(element).data('customeruuid') && $(element).val();
            }, '');

            this.validator = this.$('.form').validate({
                onfocusout: false,
                rules: {
                    customerTypeMsg: 'required',
                    financialContractName: 'required',
                    keyWord: 'existCustomerUuid'
                }
            });
        },
        onKeyupKeyWord: function(e) {
            $(e.target).data('customeruuid', null);
        },
        validate: function() {
            return this.validator.form();
        },
        extractDataFromDom: function() {
            var attr = {
                showData: {}
            };

            var $financialContractName = this.$financialContractName;
            attr.showData.financialContractName = $financialContractName.find('option:selected').text();
            attr.financialContractUuid = $financialContractName.val();

            var $customerTypeMsg = this.$customerTypeMsg;
            attr.showData.customerTypeMsg = $customerTypeMsg.find('option:selected').text();
            attr.customerType = $customerTypeMsg.val();

            attr.customerUuid = this.$('[name=keyWord]').data('customeruuid');

            return attr;
        },
        save: function(attr) {
            var attr = this.extractDataFromDom();
            this.model.set(attr);
            this.model.create(attr);
        },
        submitHandler: function() {
            if (!this.validate()) return;
            this.save();
            this.hide();
        }
    });

    module.exports = CashFlowAuditListView;

});
