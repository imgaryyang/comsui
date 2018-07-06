define(function(require, exports, module) {
    var B = Backbone;

    var CashBillModel = B.Model.extend({
        submit: function() {
            var opt = {
                url: './deposit',
                type: 'post',
                dataType: 'json'
            };

            opt.data = this.toJSON();
            opt.data.cashFlowUuid = this.collection.cashFlowAuditModel.get('cashFlowUuid');

            opt.success = function(resp) {
                if (resp.code == 0) {
                    this.clear({silent: true});
                    this.set(resp.data.depositResult);
                }

                this.trigger('submited', resp, this);
            }.bind(this);

            $.ajax(opt);
        },
        disable: function() {
            var opt = {
                url: './deposit-cancel',
                type: 'post',
                dataType: 'json'
            };

            opt.data = this.toJSON();
            opt.data.cashFlowUuid = this.collection.cashFlowAuditModel.get('cashFlowUuid');

            opt.success = function(resp) {
                this.trigger('disabled', resp, this);

                if (resp.code == 0) {
                    this.destroy(); // destroy事件
                }
            }.bind(this);

            $.ajax(opt);
        },
        create: function(attr) {
            var opt = {
                url: './build-deposit-result',
                type: 'post',
                dataType: 'json',
                data: {
                    customerUuid: attr.customerUuid,
                    financialContractUuid: attr.financialContractUuid,
                    cashFlowUuid: this.collection.cashFlowAuditModel.get('cashFlowUuid')
                }
            };

            opt.success = function(resp) {
                if (resp.code == 0) {
                    var origAttr = this.toJSON();
                    var respAttr = resp.data.depositResult;
                    var finaAttr = $.extend(true, {}, origAttr, respAttr, {isCreated: true});
                    this.set(finaAttr);
                }

                this.trigger('created', resp, this);
            }.bind(this);

            $.ajax(opt);
        }
    });

    var CashBillCollection = B.Collection.extend({
        model: CashBillModel,
        initialize: function(opt) {
            this.cashFlowAuditModel = opt.cashFlowAuditModel;
        },
        fetch: function() {
            var opt = {
                url: './show-deposit-result',
                type: 'post',
                dataType: 'json',
                data: {
                    cashFlowUuid: this.cashFlowAuditModel.get('cashFlowUuid')
                }
            };

            opt.success = function(resp) {
                if (resp.code == 0) {
                    this.reset(resp.data.depositResults); // reset事件
                }
            }.bind(this);
 
            this.trigger('request');

            $.ajax(opt);
        }
    });

    var CashFlowAuditModel = B.Model.extend({
        initialize: function() {
            this.expanded = false;
            this.bills = new CashBillCollection({cashFlowAuditModel: this});

            this.listenTo(this.bills, 'submited disabled', function(resp) {
                if (resp.code == 0) {
                    this.set(resp.data.cashFlow);
                }
            });
        },
        fetchBill: function() {
            this.bills.fetch();
        },
        createBill: function(attr) {
            this.bills.create(attr);
        },
        toggle: function() {
            if (this.expanded) {
                this.collapse();
            } else {
                this.expand();
            }
        },
        expand: function() {
            this.expanded = true;
            this.trigger('expand');
            this.fetchBill();
        },
        collapse: function() {
            this.expanded = false;
            this.trigger('collapse');
        },
        getRemainDepositAmount: function() {
            var transactionAmount = (+this.get('transactionAmount'));
            // var issuedAmount = (+this.get('issuedAmount')); // submit之后会变
            var sumDepositAmount = this.bills.pluck('depositAmount').reduce(function(prev, next) {
                return (+prev) + (+next);
            }, 0);
            return transactionAmount - sumDepositAmount;
        }
    });

    var CashFlowAuditCollection = B.Collection.extend({
        model: CashFlowAuditModel
    });

    exports.CashBillModel = CashBillModel;
    exports.CashBillCollection = CashBillCollection;
    exports.CashFlowAuditModel = CashFlowAuditModel;
    exports.CashFlowAuditCollection = CashFlowAuditCollection;

});