define(function(require, exports, moudle) {
    var root = global_config.root;

    var UserRoleModel = Backbone.Model.extend({
        initialize: function(attr) {
            this.userId = attr.userId;
        },
        actions: {
            submit: root + '/edit-user-role',
            bind: root + '/bind-financial-contract/bind',
            unbind: root + '/bind-financial-contract/unbind'
        },
        bind: function(idList) {
            this.idList = idList;
            this.bindAction(this.actions.bind, 'model:bind');
        },
        unbind: function(idList) {
            this.idList = idList;
            this.bindAction(this.actions.unbind, 'model:unbind');
        },
        bindAction: function(url, event) {
            var self = this;

            var opt = {
                type: 'post',
                dataType: 'json',
                url: url,
                data: {
                    principalId: self.userId,
                    financialContractIds: JSON.stringify(self.idList)
                }
            };

            opt.success = function(resp) {
                self.trigger(event, resp);
            };
            $.ajax(opt);
        },
        submit: function(data) {
            var opt = {
                type: 'post',
                dataType: 'json',
                url: this.actions.submit,
                data: data
            };

            opt.success = function(resp) {
                this.trigger('model:submit', resp);
            }.bind(this);
            $.ajax(opt);
        }
    });

    exports.UserRoleModel = UserRoleModel;
});