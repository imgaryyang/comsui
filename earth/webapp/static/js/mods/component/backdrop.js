define(function(require, exports, module) {
    var $ = jQuery;
    var $body = $('body');
    var bootstrapTransition = $.support.transition;

    var Backdrop = Backbone.View.extend({
        tagName: 'div',
        className: 'modal-backdrop fade',
        events: {
            'click': 'onClickEl'
        },
        supportTransition: !!bootstrapTransition,
        initialize: function(options) {
            $.extend(this, { statics: true }, options);
            if (this.zIndex) {
                this.$el.css('z-index', this.zIndex);
            }

            this.on('opened:backdrop', function() {
                this.isOpened = true;
                this.view = '';
            });
            this.on('closed:backdrop', function() {
                this.isOpened = false;
            });

        },
        onClickEl: function() {
            this.statics && this.hide();
        },
        remove: function() {
            this.$el.detach();
            this.stopListening();
            return this;
        },
        hide: function() {
            var self = this;
            self.trigger('close:backdrop');
            if (self.supportTransition) {
                self.$el.one(bootstrapTransition.end, function() {
                    self.remove();
                    self.trigger('closed:backdrop');
                });
                self.$el.removeClass('in');
            } else {
                self.remove();
                self.trigger('closed:backdrop');
            }
            return self;
        },
        open: function(view) {
            this.trigger('open:backdrop',view);
            $body.append(this.$el.addClass('in'));
            this.trigger('opened:backdrop');
            return this;
        },
        toggle: function(view) {
            var self = this;
            if (this.isOpened) {
                if(self.view != view ){
                    self.trigger('showView',view);
                } else{
                    this.hide();
                }
            } else {
                this.open(view);
            }
            self.view = view;
        }
    });

    module.exports = Backdrop;
});