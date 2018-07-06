define(function(require, exports, module) {
    var popupTip = require('component/popupTip');
    var Pagination = require('component/pagination');
    var DialogView = require('component/dialogView');

    var root = global_config.root;
    var loading = global_const.loadingImg;
    var $ = jQuery;

    var mixins = {
        initialize: function(options) {
            this.$tabMenuItem = options.$tabMenuItem;
            this.mInitialize && this.mInitialize(options);
        },
        hide: function() {
            this.$el.hide();
            this.$tabMenuItem.removeClass('active');
        },
        show: function() {
            this.$el.show();
            this.$tabMenuItem.addClass('active');
        }
    };

    // 放款Tab
    var ReleaseItemView = Backbone.View.extend({
        events: {
            'click .expand': 'onClickExpand'
        },
        orderDetailTemplate: _.template($('#orderDetailTmpl').html() || ''),
        initialize: function() {
            this.$loading = $('<tr><td colspan="8" align="center"></td></tr>').find('td').append(loading.clone());
            this.expanded = false;

            this.on('request:orderdetail', function() {
                this.$el.after(this.$loading);
            });

            this.on('response:orderdetail', function(resp) {
                this.$loading.remove();
                if (resp.code == 0) {
                    var htm = this.orderDetailTemplate(resp.data);
                    this.$el.after(htm);
                } else {
                    popupTip.show(resp.message);
                }
            });

        },
        onClickExpand: function(e) {
            e.preventDefault();
            this.toggleOrderDetail();
        },
        toggleOrderDetail: function() {
            if (this.expanded) {
                this.collapseOrderDetail();
            } else {
                this.expandOrderDetail();
            }
        },
        collapseOrderDetail: function() {
            this.expanded = false;
            this.$el.removeClass('active');

            var $next = this.$el.next('.bill-card-box');
            $next.remove();
        },

        expandOrderDetail: function() {
            this.expanded = true;
            this.$el.addClass('active');
            var data = {
                remittanceApplicationUuid: this.$el.data('remittance-application-uuid')
            };

            var obj = {
                url: './detail/planlist',
                type: 'get',
                dataType: 'json',
                data: data
            };

            obj.success = function(resp) {
                this.trigger('response:orderdetail', resp);
            }.bind(this);

            this.trigger('request:orderdetail');

            $.ajax(obj);

            // setTimeout(function() {
            //     obj.success({ code: 0 });
            // }, 1000);
        }
    });

    var ReleaseTabContentView = Backbone.View.extend($.extend({}, mixins, {
        mInitialize: function(options) {
            this.$('.item-release').toArray().forEach(function(el, index) {
                var view = new ReleaseItemView({
                    el: el
                });
                if (index == 0) {
                    view.expandOrderDetail();
                }
            });
        }
    }));

    // 还款Tab
    var RepayPlanCollection = Backbone.Collection.extend({
        actions: {
            fetch: function() {
                return root + '/contracts/detail/' + this.contractId;
            },
            submit: function() {
                return root + '/contracts/detail/updateRepaymentPlan';
            }
        },
        initialize: function(models, options) {
            RepayPlanCollection.__super__.initialize.apply(this, arguments);
            this.contractId = options.contractId;
        },
        fetch: function(versionNo) {
            var opt = {
                url: this.actions.fetch.call(this),
                data: {
                    versionNo: versionNo
                },
                dataType: 'json'
            };

            opt.success = function(resp) {
                if (resp.code == 0) {
                    this.reset(resp.data.list);
                }
                this.trigger('sync');
            }.bind(this);

            this.trigger('request');

            $.ajax(opt);
        },
        submit: function(modifyCode, creates) {
            var modelList = creates ? creates : [];
            var self = this;
            this.rightModel = true;
            for (var i = 0; i < modelList.length; i++) {
                if (!modelList[i].rightAttr) {
                    self.rightModel = false;
                }
            }
            this.each(function(model) {
                if (!model.get('canBeModifed')) return;
                if (!model.get('rightAttr')) {
                    self.rightModel = false;
                }
                var endModel = _.pick(model.toJSON(), 'assetRecycleDate', 'assetPrincipal', 'assetInterest', 'serviceCharge', 'maintenanceCharge', 'otherCharge');
                modelList.push(endModel);
            });
            if (!this.rightModel) return;
            var opt = {
                type: 'post',
                dataType: 'json',
                url: this.actions.submit(),
                data: {
                    data: JSON.stringify(modelList),
                    contractId: this.contractId,
                    modifyCode: modifyCode
                }
            };

            opt.success = function(resp) {
                if (resp.code == 0) {
                    this.add(creates, {
                        silent: true
                    });
                }
                this.trigger('submit', resp);
            }.bind(this);

            $.ajax(opt);
        }
    });

    var RepayPlanItemView = Backbone.View.extend({
        tagName: 'tr',
        template: {
            template: _.template($('.block-replay-plan .repay-plan-item').html(), {
                variable: 'obj'
            })
        },
        render: function() {
            var data = this.model.toJSON();
            var htm = this.template.template(data);
            this.$el.html(htm);
            return this;
        }
    });

    var EditRepayPlanItemView = Backbone.View.extend({
        tagName: 'tr',
        template: _.template($('.block-replay-plan .edit-repay-plan-item').html(), {
            variable: 'obj'
        }),
        events: {
            'click .delete': function(e) {
                e.preventDefault();
                var self = this;
                popupTip.show('即将删除该还款计划是否继续？', '', [{
                    text: '继续',
                    style: 'success',
                    handler: function() {
                        this.hide();
                        if (self.model.get('isCreate')) {
                            self.model.destroy();
                            return;
                        }

                        var $tr = $(e.target).closest('tr');

                        if ($tr.siblings().length == 0) {
                            popupTip.show('还款计划不能删光哦！');
                        } else {
                            self.model.clear('idAttribute');
                            self.model.destroy();
                        }
                    }
                }, {
                    text: '取消',
                    style: 'default',
                    handler: function() {
                        this.hide();
                    }
                }]);
            }
        },
        initialize: function() {
            this.listenTo(this.model, 'extract', this.save);
            this.listenTo(this.model, 'destroy', this.remove);
        },
        render: function() {
            var data = this.model.toJSON();
            var htm = this.template(data);
            this.$el.html(htm);
            return this;
        },
        save: function() {
            var assetRecycleDate = this.$el.find('[name=assetRecycleDate]').val().trim();
            var assetPrincipal = this.$el.find('[name=assetPrincipal]').val().trim();
            var assetInterest = this.$el.find('[name=assetInterest]').val().trim();
            var serviceCharge = this.$el.find('[name=serviceCharge]').val().trim();
            var maintenanceCharge = this.$el.find('[name=maintenanceCharge]').val().trim();
            var otherCharge = this.$el.find('[name=otherCharge]').val().trim();

            var attr = {
                assetRecycleDate: assetRecycleDate,
                assetPrincipal: assetPrincipal == '' ? '0' : assetPrincipal,
                assetInterest: assetInterest == '' ? '0' : assetInterest,
                serviceCharge: serviceCharge == '' ? '0' : serviceCharge,
                maintenanceCharge: maintenanceCharge == '' ? '0' : maintenanceCharge,
                otherCharge: otherCharge == '' ? '0' : otherCharge,
            };

            var rightAttr = this.validateAttr(attr);
            this.model.set(attr);
            this.model.set('rightAttr', rightAttr);
        },
        validateAttr: function(attr) {
            var parnt1 = /^([0-9]\d{0,9}|0)([.]?|(\.\d{1,2})?)$/;
            var rightAttr = true;
            if (attr.assetRecycleDate == '') {
                this.$el.find('[name=assetRecycleDate]').addClass('error');
                this.$el.find('.input-group-addon').css('border-color', 'red');
                rightAttr = false;
            } else {
                this.$el.find('[name=assetRecycleDate]').removeClass('error');
                this.$el.find('.input-group-addon').css('border-color', '#ccc');
                rightAttr = true;
            }
            if (!attr.assetPrincipal.match(parnt1)) {
                this.$el.find('[name=assetPrincipal]').addClass('error');
                rightAttr = false;
            } else {
                this.$el.find('[name=assetPrincipal]').removeClass('error');
                if (rightAttr) rightAttr = true;
            }
            if (!attr.assetInterest.match(parnt1)) {
                this.$el.find('[name=assetInterest]').addClass('error');
                rightAttr = false;
            } else {
                this.$el.find('[name=assetInterest]').removeClass('error');
                if (rightAttr) rightAttr = true;
            }
            if (!attr.serviceCharge.match(parnt1)) {
                this.$el.find('[name=serviceCharge]').addClass('error');
                rightAttr = false;
            } else {
                this.$el.find('[name=serviceCharge]').removeClass('error');
                if (rightAttr) rightAttr = true;
            }
            if (!attr.maintenanceCharge.match(parnt1)) {
                this.$el.find('[name=maintenanceCharge]').addClass('error');
                rightAttr = false;
            } else {
                this.$el.find('[name=maintenanceCharge]').removeClass('error');
                if (rightAttr) rightAttr = true;
            }
            if (!attr.otherCharge.match(parnt1)) {
                this.$el.find('[name=otherCharge]').addClass('error');
                rightAttr = false;
            } else {
                this.$el.find('[name=otherCharge]').removeClass('error');
                if (rightAttr) rightAttr = true;
            }
            return rightAttr;
        }
    });

    var RepayPlanView = Backbone.View.extend({
        events: {
            'change .select-version': 'onChangeVersion',
            'click .cancel': 'toCancel',
            'click .submit': 'submit',
            'click .create': 'create',
            'click .edit-repay-plan': 'toEdit',
            'change [name=assetPrincipal]': 'changeAssetPrincipal'
        },
        template: {
            normal: _.template($('.block-replay-plan .normal').html()),
            edit: _.template($('.block-replay-plan .edit').html())
        },
        initialize: function() {
            this.createCacheRepayPlanList = new RepayPlanCollection([], {
                contractId: this.model.get('contractId')
            });

            this.listenTo(this.createCacheRepayPlanList, 'add', this.addOneRepayPlan);
            this.listenTo(this.createCacheRepayPlanList, 'remove', this.changePrincipal);

            this.repayPlanList = new RepayPlanCollection([], {
                contractId: this.model.get('contractId')
            });

            this.listenTo(this.repayPlanList, 'reset', this.addAllRepayPlan);
            this.listenTo(this.repayPlanList, 'add', this.addOneRepayPlan);
            this.listenTo(this.repayPlanList, 'remove', this.changePrincipal);
            this.listenTo(this.repayPlanList, 'submit', function(resp) {
                if (resp.code == 0) {
                    popupTip.show('操作成功');
                    setTimeout(function() {
                        location.reload();
                    });
                } else {
                    popupTip.show(resp.message || '操作失败，请重试');
                }
            });

            this.currentVersion = this.$('[name=activeVersionNo]').val();
            this.repayPlanList.fetch(this.currentVersion);
        },
        changeAssetPrincipal: function(e) {
            var value = $(e.target).val().trim();
            if (value == '') {
                $(e.target).removeClass('error');
                this.changePrincipal();
            } else {
                if (/^([0-9]\d{0,9}|0)([.]?|(\.\d{1,2})?)$/.test(value)) {
                    $(e.target).removeClass('error');
                    this.changePrincipal();
                } else {
                    $(e.target).addClass('error');
                }
            }
        },
        changePrincipal: function() {
            var principalInputs = this.$('.assetPrincipal');
            var nowTotal = 0;
            for (var i = 0; i < principalInputs.length; i++) {
                var el = principalInputs.eq(i);
                var value;
                if (el.is('input')) {
                    value = parseFloat(el.val());
                } else {
                    value = parseFloat(el.html().replace(/,/g, ''));
                }
                if (_.isNaN(value)) continue;
                nowTotal += value;
            }
            this.residualPrincipal = this.principal - nowTotal;
            this.$('.residual-principal').text(this.residualPrincipal.formatMoney(2, ''));
            if (this.residualPrincipal < 0) {
                this.$('.residual-principal').css('color', 'red');
            } else {
                this.$('.residual-principal').css('color', '#333');
            }
        },
        addAllRepayPlan: function() {
            this.$('.bd tbody').empty();
            var list = this.repayPlanList;
            var principal = 0.00;
            var profit = 0.00;
            var loanServiceFee = 0.00;
            var techMaintenanceFee = 0.00;
            var otherFee = 0.00;
            var totalAmount = 0.00;

            for (var i = 0, len = list.models.length; i < len; i++) {
                var model = list.at(i);
                this.addOneRepayPlan(model);
                principal += model.get('repayPrincipal') == undefined ? 0 : model.get('repayPrincipal');
                profit += model.get('repayProfit') == undefined ? 0 : model.get('repayProfit');
                loanServiceFee += model.get('loanServiceFee') == undefined ? 0 : model.get('loanServiceFee');
                techMaintenanceFee += model.get('techMaintenanceFee') == undefined ? 0 : model.get('techMaintenanceFee');
                otherFee += model.get('otherFee') == undefined ? 0 : model.get('otherFee');
            }
            totalAmount = principal + profit + otherFee + loanServiceFee + techMaintenanceFee;
            this.$('.principal').text(principal.formatMoney(2, ''));
            this.$('.profit').text(profit.formatMoney(2, ''));
            this.$('.loanServiceFee').text(loanServiceFee.formatMoney(2, ''));
            this.$('.techMaintenanceFee').text(techMaintenanceFee.formatMoney(2, ''));
            this.$('.otherFee').text(otherFee.formatMoney(2, ''));
            this.$('.totalAmount').text(totalAmount.formatMoney(2, ''));

            if (this.isEdit) {
                this.principal = principal;
                this.residualPrincipal = 0;
                this.$('.residual-principal').text(this.residualPrincipal.formatMoney(2, ''));
            }
        },
        addOneRepayPlan: function(model) {
            model.set('isEdit', this.isEdit);
            if (this.isEdit) {
                var view = new EditRepayPlanItemView({
                    model: model
                });
            } else {
                var view = new RepayPlanItemView({
                    model: model
                });
            }

            this.$('.bd tbody').append(view.render().$el);
        },
        toCancel: function() {
            this.createCacheRepayPlanList.reset();
            this.repayPlanList.fetch(this.currentVersion);
            this.toNormal();
        },
        toNormal: function() {
            this.isEdit = false;
            var htm = this.template.normal({
                version: this.currentVersion
            });
            this.$el.html(htm);
            this.addAllRepayPlan();
        },
        toEdit: function() {
            this.isEdit = true;
            var htm = this.template.edit({
                version: this.currentVersion
            });
            this.$el.html(htm);
            this.addAllRepayPlan();
        },
        create: function() {
            var data = {
                assetRecycleDate: '',
                assetPrincipal: '',
                assetInterest: '',
                serviceCharge: '',
                maintenanceCharge: '',
                otherCharge: '',
                isCreate: true,
                canBeModifed: true
            };
            this.createCacheRepayPlanList.add(data);
        },
        submit: function() {
            if (this.residualPrincipal < 0) {
                var dialog = new DialogView({
                    bodyInnerTxt: '计划还款本金与原版本不相等！',
                    excludeGoahed: true
                });
                dialog.show();
                return;
            }
            var modifyCode = $('[name = modifyCode]').val();

            this.repayPlanList.each(function(model) {
                if (!model.get('canBeModifed')) return;
                model.trigger('extract');
            });

            this.createCacheRepayPlanList.each(function(model) {
                model.trigger('extract');
            });

            var creates = this.createCacheRepayPlanList.toJSON();
            this.repayPlanList.submit(modifyCode, creates);
        },
        onChangeVersion: function() {
            if (this.$('.select-version').get(0).selectedIndex == 0) {
                this.$('.edit-repay-plan').show();
            } else {
                this.$('.edit-repay-plan').hide();
            }
            this.currentVersion = this.$('.select-version').val();
            this.repayPlanList.fetch(this.currentVersion);
        }
    });


    var RepayTabContentView = Backbone.View.extend($.extend({}, mixins, {
        mInitialize: function() {
            this.repayPlanView = new RepayPlanView({
                el: this.$('.block-replay-plan').get(0),
                model: this.model
            });
            this.repayPlanView.toNormal();

            if (!this.model.get('contractId')) return;

            this.repaymentHistorysView = new Pagination({
                el: this.$('.repayment-historys').get(0),
                pageRecordNum: 99999,
                queryAction: root + '/contracts/detail/repaymentHistorys/' + this.model.get('contractId')
            });
        }
    }));

    exports.ReleaseTabContentView = ReleaseTabContentView;
    exports.RepayTabContentView = RepayTabContentView;

});