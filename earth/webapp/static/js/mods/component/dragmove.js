define(function(require, exports, module) {
    var $ = jQuery;
    var $document = $(document);

    /*
     * To Do：
     * 集成事件Backbone.Event
     * 暴露dragstart, dragmove, dragend事件
     * 容器边界限制
     * 能否move的条件判断
     * 只能移动X或Y轴方向
     */

    function Dragmove($element, option) {
        this.$element = $element;
        var namespace = this.namespace = _.uniqueId('dragmove');
        this.START_EVENT = 'mousedown.' + namespace + ' touchstart.' + namespace;
        this.MOVE_EVENT = 'mousemove.' + namespace + ' touchmove.' + namespace;
        this.END_EVENT = 'mouseup.' + namespace + ' touchend.' + namespace;

        var active;
        var startX;
        var startY;
        var click;
        var touch;

        $element.find(option.handler).css('cursor', 'move');

        $element.on(this.START_EVENT, option.handler, function(e) {
            active = true;
            startX = e.originalEvent.pageX - $element.offset().left;
            startY = e.originalEvent.pageY - $element.offset().top;

            if ('mousedown' == e.type) {
                click = $element;
            }

            if ('touchstart' == e.type) {
                touch = $element;
            }

            if (window.mozInnerScreenX == null) {
                return false;
            }
        });

        $document
            .on(this.MOVE_EVENT, function(e) {
                if ('mousemove' == e.type && active) {
                    click.offset({
                        left: e.originalEvent.pageX - startX,
                        top: e.originalEvent.pageY - startY
                    });
                }

                if ('touchmove' == e.type && active) {
                    touch.offset({
                        left: e.originalEvent.pageX - startX,
                        top: e.originalEvent.pageY - startY
                    });
                }
            })
            .on(this.END_EVENT, function() {
                active = false;
            });
    }

    Dragmove.prototype.remove = function() {
        this.$element.off(this.namespace);
        $document.off(this.namespace);
        $document.off(this.namespace);
    };

    $.fn.dragmove = function() {
        var option;
        var method;
        var arg1 = arguments[0];

        if (typeof arg1 === 'string') {
            method = arg1;
        } else if (typeof arg1 === 'object') {
            option = arg1;
        } else {
            option = {};
        }

        return this.each(function() {
            var $this = $(this);
            var handler = $this.data('dragmove');
            var dragable = $this.data('dragable');

            if (!handler && option && dragable !== false) {
                handler = new Dragmove($this, option);
                $this.data('dragmove', handler);
            }

            if (handler && method) {
                handler[method].call(handler);
            }
        });
    };

    $(document).on('click', '[data-toggle=dragmove]', function(e) {
        var $target = $(e.currentTarget);
        var dragmove = $target.data('dragmove');
        if (dragmove) return;
        var option = $target.data();
        $target.dragmove(option);
    });

});

