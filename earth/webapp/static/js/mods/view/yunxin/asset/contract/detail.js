define(function(require, exports, module) {
    var FormDialogView = require('baseView/baseFormView').FormDialogView;
    var popupTip = require('component/popupTip');
    var DialogView = require('component/dialogView');
    var AreaSelectView = require('component/areaSelect');
    var ReleaseTabContentView = require('./tab-switch').ReleaseTabContentView;
    var RepayTabContentView = require('./tab-switch').RepayTabContentView;

    var root = global_config.root;
    var loading = global_const.loadingImg;
    var $ = jQuery;
    var RepurchaseDialogView = FormDialogView.extend({
        template: _.template($('#repurchaseTmpl').html(), {
            variable: 'data'
        }),
        initialize: function() {
            RepurchaseDialogView.__super__.initialize.apply(this, arguments);
            this.defineValidator();
        },
        defineValidator: function() {
            $.validator.addMethod('rightformatAmount', function(value, element) {
                return this.optional(element) || (/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/.test(value));
            }, '请输入正确的金额格式');

            this.validator = this.$('.form').validate({
                rules: {
                    repurchaseAmount: {
                        required: true,
                        rightformatAmount: true
                    }
                }
            });
        },
        validate: function() {
            return this.validator.form();
        },
        submit: function() {
            if (this.loading) return;
            this.loading = true;
            var attr = this.extractDomData();
            attr.id = this.model.id;
            var self = this;
            var opt = {
                url: root + '/contracts/detail/repurchase',
                data: attr,
                type: 'post',
                dataType: 'json'
            };

            opt.success = function(resp) {
                if (resp.code == 0) {
                    popupTip.show('操作成功');

                    setTimeout(function() {
                        location.reload();
                    }, 1000);
                } else {
                    popupTip.show(resp.message);
                }
            };

            opt.complete = function() {
                self.loading = false;
            };

            $.ajax(opt);
        },
        submitHandler: function() {
            var self = this;
            if (!this.validate()) return;
            var confirmDialog = new DialogView({
                title: '提示',
                bodyInnerTxt: '该金额可能与系统计算的金额不相等，是否确认提交?'
            });
            confirmDialog.on('goahead', function() {
                self.submit();
                confirmDialog.hide();

            });
            confirmDialog.show();
        }

    });

    var PrepayDialogView = FormDialogView.extend({
        template: _.template($('#prepaymentTmpl').html(), {
            variable: 'obj'
        }),
        initialize: function() {
            PrepayDialogView.__super__.initialize.apply(this, arguments);
            this.defineValidator();
        },
        defineValidator: function() {
            this.validator = this.$('.form').validate({
                rules: {
                    type: 'required',
                    assetInitialValue: {
                        required: true,
                        number: true
                    }
                }
            });
        },
        validate: function() {
            var flag = true;
            var datetimepicker = this.$('.datetimepicker-form-control');
            if (!datetimepicker.val()) {
                var flag = false;
                datetimepicker.parent().addClass('error');
            }
            return this.validator.form() && flag;
        },
        save: function() {
            var data = this.extractDomData();
            data.contractId = this.model.get('contractId');

            var opt = {
                url: root + '/contracts/prepayment/save',
                data: data,
                type: 'post',
                dataType: 'json'
            };

            opt.success = function(resp) {
                if (resp.code == 0) {
                    this.hide();
                    popupTip.show('提交成功!');
                } else {
                    popupTip.show(resp.message);
                }
            }.bind(this);

            setTimeout(function() {
                $.ajax(opt);
            }, 450);
        },
        submitHandler: function() {
            if (!this.validate()) return;

            var self = this;
            var $input = this.$('input[name=assetInitialValue]');
            var input = $input.val();
            var amount = this.model.get('amount');

            if (input <= amount) {
                this.save();
                return;
            }

            popupTip.show('该金额可能与系统计算的金额不相等，是否确认提交?', null, [{
                text: '关闭',
                style: 'default',
                handler: function() {
                    $input.val(amount);
                    popupTip.hide();
                }
            }, {
                text: '确定',
                style: 'success',
                handler: function() {
                    self.save();
                    popupTip.hide();
                }
            }]);
        }
    });
    var EditCustomerInfoVierw = FormDialogView.extend({
        template: _.template($('#editCustomerInfoTmpl').html(), {
            variable: 'obj'
        }),
        actions: {
            submit: root + '/modifyContractAccount/repaymentInfo/modify'
        },
        initialize: function() {
            EditCustomerInfoVierw.__super__.initialize.call(this, arguments);
            this.defineValidator();
            this.areaSelect = new AreaSelectView({
                el: this.$('.area-select')
            });
            this.$('.selectpicker').selectpicker('render');
        },
        defineValidator: function() {
            this.validator = this.$('.form').validate({
                rules: {
                    bankAccount: 'required',
                    cityCode: 'required',
                    provinceCode: 'required'
                }
            });
        },
        validate: function() {
            var flag = true;
            if (this.$('.filter-option').html() === '请选择') {
                this.$('.dropdown-toggle').addClass('error');
                flag = false;
            } else {
                flag = true;
                this.$('.dropdown-toggle').removeClass('error');
            }
            return this.validator.form() && flag;
        },
        save: function() {
            var self = this;

            var contractId = $('[name=contractId]').val().trim();
            if (!contractId) return;

            var attr = this.extractDomData();
            attr.contractId = contractId;
            attr.bankCode = $('[name=bankCode]').val();

            var opt = {
                url: this.actions.submit,
                type: 'post',
                dataType: 'json',
                data: attr
            };

            opt.success = function(resp) {
                self.model.trigger('model:edit-customer-info', resp, attr);
            };

            $.ajax(opt);
        },
        submitHandler: function() {
            if (!this.validate()) return;
            this.save();
            this.hide();
        }
    });

    var TerminateContractView = FormDialogView.extend({
        template: _.template($('#terminateContractTmpl').html(), {
            variable: 'obj'
        }),
        actions: {
            submit: root + '/contracts/invalidate'
        },
        initialize: function() {
            TerminateContractView.__super__.initialize.call(this, arguments);
            this.defineValidator();
        },
        defineValidator: function() {
            this.validator = this.$('.form').validate({
                rules: {
                    comment: {
                        required: true,
                        maxlength: 50
                    }
                }
            });
        },
        validate: function() {
            return this.validator.form();
        },
        save: function() {
            var self = this;
            var opt = {
                url: this.actions.submit,
                type: 'post',
                dataType: 'json'
            };

            opt.success = function(resp) {
                self.model.trigger('model:terminate-contract', resp);
            };

            this.$('.form').ajaxSubmit(opt);
        },
        submitHandler: function() {
            if (!this.validate()) return;
            this.save();
            this.hide();
        }
    });

    var ContractDetailView = Backbone.View.extend({
        el: '.content',
        events: {
            'click #terminateContract': 'onClickTerminateContract',
            'click .edit-customer-info': 'onClickEditCustomerInfo',
            'click .tab-menu-item': 'onClickTabMenuItem',
            'mouseover .showPopover': 'showPopover',
            'click .icon-bankcard': 'onClickBankcard',
            'mouseout .showPopover': 'showPopover',
            'click .repurchase': 'onClickRepurchase',
            'click .prepay': 'onCLickPrepay',
            'click .download-file': 'onClickDownloadFile'
        },
        initialize: function(attr) {
            this.initModel();
            this.initTabContentView();
            $(window).on('click', function() {
                if ($('body').find('.popover').last().hasClass('clickShow')) {
                    $('body').find('.popover').popover('hide');
                }
            });
        },
        initModel: function() {
            this.model = new Backbone.Model();
            var data = {};
            data.contractId = this.$('[name=contractId]').val().trim();
            data.contractNo = this.$('.contractNo').text().trim();
            data.amount = this.$('.amount').text().trim();
            data.customer = this.$('.customer').text().trim();
            data.idCardNum = this.$('.idcard-num').text().trim();
            data.customerSource = this.$('.customer-source').text().trim();
            data.bank = this.$('.bank').text().trim();
            data.bankCode = this.$('.account-bankCode').text().trim();
            data.provinceCode = this.$('.bank-provinceCode').text().trim();
            data.cityCode = this.$('.bank-cityCode').text().trim();
            this.model.set(data);
        },
        showPopover: function(e) {
            e.preventDefault();
            var el = $(e.target);
            var popoverContent = el.siblings('.account').html();
            el.popover({
                content: popoverContent
            });
            el.popover('toggle');
        },
        onClickBankcard: function(e) {
            e.preventDefault();
            this.showPopover(e);
            var el = $(e.target);
            el.on('shown.bs.popover', function() {
                $('body').find('.popover').last().addClass('clickShow');
            });
            el.on('hidden.bs.popover', function() {
                el.removeClass('active');
            });
            el.toggleClass('active');
            return false;
        },
        initTabContentView: function() {
            var obj = this.tabContentViews = {};

            obj.release = new ReleaseTabContentView({
                el: this.$('.tab-content-item-release').get(0),
                $tabMenuItem: this.$('.tab-menu-item-release'),
                model: this.model
            });

            obj.repay = new RepayTabContentView({
                el: this.$('.tab-content-item-repay').get(0),
                $tabMenuItem: this.$('.tab-menu-item-repay'),
                model: this.model
            });

            obj.release.hide();
            obj.repay.hide();

            this.activeTabContentView = obj.repay;
            this.activeTabContentView.show();

            this.$('.tabs').show();
        },
        handleAfterPopupHide: function(resp) {
            if (resp.code == 0) {
                popupTip.show('操作成功');

                setTimeout(function() {
                    location.reload();
                }, 1000);
            } else {
                popupTip.show(resp.message || '操作失败，请重试');
            }
        },
        onClickEditCustomerInfo: function(e) {
            e.preventDefault();
            var view = new EditCustomerInfoVierw({
                model: this.model
            });

            this.model.once('model:edit-customer-info', this.handleAfterPopupHide);

            view.show();
        },
        onClickTerminateContract: function() {
            var view = new TerminateContractView({
                model: this.model
            });

            this.model.once('model:terminate-contract', this.handleAfterPopupHide);

            view.show();
        },
        onClickTabMenuItem: function(e) {
            e.preventDefault();

            var $target = $(e.currentTarget);
            var tabContentKey = $target.data('target');
            var nextTabContentView = this.tabContentViews[tabContentKey];

            this.activeTabContentView.hide();
            nextTabContentView.show();

            this.activeTabContentView = nextTabContentView;
        },
        onClickRepurchase: function(e) {
            var contractId = $(e.target).data('contract-id');

            if (this.loading) return;
            this.loading = true;

            var self = this;
            var opt = {
                url: root + '/contracts/detail/repurchaseAmount',
                data: {
                    id: contractId
                },
                type: 'post',
                dataType: 'json'
            };

            opt.success = function(resp) {
                if (resp.code == 0) {
                    var model = new Backbone.Model(resp.data);
                    model.id = contractId;
                    var view = new RepurchaseDialogView({
                        model: model
                    });
                    view.show();
                } else {
                    popupTip.show(resp.message);
                }
            };

            opt.complete = function() {
                self.loading = false;
            };

            $.ajax(opt);
        },
        onCLickPrepay: function(e) {
            e.preventDefault();
            var contractId = this.model.get('contractId');
            var opt = {
                url: root + '/contracts/prepayment/check',
                type: 'post',
                data: {
                    contractUuid: contractId
                },
                dataType: 'json'
            };
            opt.success = function(resp) {
                if (resp.code === '0') {
                    var data = {};
                    data.amount = resp.data.defaultAmount['0'];
                    data.contractId = contractId;
                    var model = new Backbone.Model(data);
                    var prepayDialogView = new PrepayDialogView({
                        model: model
                    });
                    prepayDialogView.show();
                } else {
                    popupTip.show(resp.message);
                }
            };
            $.ajax(opt);
        },
        onClickDownloadFile: function(e) {
            e.preventDefault();
            var contractId = this.model.get('contractId');
            var opt = {
                url: root + '/contracts/download',
                type: 'get',
                data: {
                    contractId: contractId
                },
                dataType: 'json'
            };
            opt.success = function(resp) {
                if (resp.code === '0') {
                    window.location.href = root + '/contracts/download?contractId=' + contractId;
                } else {
                    $('.download-file').addClass('hide');
                    $('.no-file').removeClass('hide');
                }
            };
            $.ajax(opt);
        }
    });

    module.exports = ContractDetailView;

});