define(function(require, exports, module) {
    var $ = jQuery;
    var U = _;

    var tmplStr = [
        '<div class="modal-dialog">',
        '    <div class="modal-content">',
        '        <div class="modal-header">',
        '            <button type="button" class="close close-dialog" data-dismiss="modal">',
        '                <span>&times;</span>',
        '            </button>',
        '            <h4 class="modal-title" id="dialoglabel">{%= title || \'提示\' %}</h4>',
        '        </div>',
        '        <div class="modal-body">',
        '            <div class="inner" style="{%= !isHtml ? \'margin: 50px 0;\' : \'\'%}">',
        '                {%= message %}',
        '            </div>',
        '        </div>',
        '        <div class="modal-footer">',
        '            {% _.each(buttons, function(btn, index) { %}',
        '                <button type="button" class="btn {%= btn.id %} btn-{%= btn.style || \'default\' %}">',
        '                    {%= btn.text || \'关闭\' %}',
        '                </button>',
        '            {% }) %}',
        '        </div>',
        '    </div>',
        '</div>'
    ].join('');

    var Dialog = Backbone.View.extend({
        template: U.template(tmplStr),
        tagName: 'div',
        className: 'modal fade',
        // 这两个属性对keyup的触发有影响
        attributes: {
            'role': 'dialog',
            'tabindex': '-1'
        },
        events: {
            'shown.bs.modal': 'onShown',
            'hidden.bs.modal': 'onHidden'
        },
        initialize: function(options) {
            this.primitiveEvents = this.events;
            this.$el.modal($.extend({show: false}, options));
        },
        isHtml: function(str) {
            var $div = $('<div>');
            $div.html(str);
            return $div.children().length > 0;
        },
        onShown: function() {
            this.trigger('shown');
        },
        onHidden: function() {
            this.trigger('hidden');
            this.$el.detach();
        },
        show: function() {
            var Constructor = this.constructor;
            var message;
            var title;
            var buttons;

            if (2 in arguments) {
                buttons = arguments[2];
                title = arguments[1];
                message = arguments[0];
            } else if (1 in arguments) {
                buttons = Constructor.BUTTONS;
                title = arguments[1];
                message = arguments[0];
            } else if (0 in arguments) {
                buttons = Constructor.BUTTONS;
                title = Constructor.TITLE;
                message = arguments[0];
            } else {
                buttons = Constructor.BUTTONS;
                title = Constructor.TITLE;
                message = Constructor.MESSAGE;
            }

            if (buttons && buttons.length > 0) {
                var events = {};
                var btn;
                var i;
                for (i = 0; i < buttons.length; i++) {
                    btn = buttons[i];
                    btn.id = U.uniqueId('btn-');
                    events['click .' + btn.id] = btn.handler;
                }
                this.delegateEvents($.extend(this.primitiveEvents, events));
            }

            var htm = this.template({
                message: message,
                title: title,
                buttons: buttons,
                isHtml: this.isHtml(message)
            });

            this.$el
                .html(htm)
                .modal('show');
        },
        hide: function() {
            this.$el.modal('hide');
        }
    });

    Dialog.TITLE = '提示';

    Dialog.MESSAGE = '系统繁忙请稍候重试';

    Dialog.BUTTONS = [{
        text: '关闭',
        style: 'default',
        handler: function() {
            this.hide();
        }
    }];

    module.exports = new Dialog();

});