define(function(require, exports, module) {
    var root = global_config.root;

    var RepurchaseModel = Backbone.Model.extend({
        actions: {
            default: root + '/repurchasedoc/default',
            activate: root + '/repurchasedoc/activate',
            nullify: root + '/repurchasedoc/nullify'
        },
        nullify: function() {
            var self = this;
            var opt = {
                url: this.actions.nullify,
                dataType: 'json',
                data: this.toJSON()
            };

            opt.success = function(resp) {
                self.trigger('nullify:success', resp);
            };

            $.ajax(opt);
        },
        activate: function() {
            var self = this;
            var opt = {
                url: this.actions.activate,
                dataType: 'json',
                data: this.toJSON()
            };

            opt.success = function(resp) {
                self.trigger('activate:success', resp);
            };

            $.ajax(opt);
        },
        default: function() {
            if (this.loading) return;
            this.loading = true;

            var self = this;
            var opt = {
                url: self.actions.default,
                data: {
                    rduid: this.rduid
                },
                type: 'post',
                dataType: 'json'
            };

            opt.success = function(resp) {
                self.trigger('model:default', resp, self);
            };

            opt.complete = function() {
                self.loading = false;
            };

            $.ajax(opt);
        }
    });

    exports.RepurchaseModel = RepurchaseModel;

});