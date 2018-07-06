var oridinal = 1;

function uniqueId(prefix) {
    return prefix + oridinal++;
}

export default {
    bind: function(el, binding) {
        var $el = $(el);
        var handler = binding.value || null;
        var namespace = uniqueId('dragmove');
        var START_EVENT = 'mousedown.' + namespace + ' touchstart.' + namespace;
        var MOVE_EVENT = 'mousemove.' + namespace + ' touchmove.' + namespace;
        var END_EVENT = 'mouseup.' + namespace + ' touchend.' + namespace;

        var active;
        var startX;
        var startY;
        var click;
        var touch;

        $el.on(START_EVENT, handler, (e) => {
            active = true;
            startX = e.originalEvent.pageX - $el.offset().left;
            startY = e.originalEvent.pageY - $el.offset().top;

            if ('mousedown' == e.type) {
                click = $el;
            }

            if ('touchstart' == e.type) {
                touch = $el;
            }

            if (window.mozInnerScreenX == null) {
                return false;
            }
        });

        $(document)
            .on(MOVE_EVENT, (e) => {
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
            .on(END_EVENT, (e) => {
                active = false;
            });

        $el.data('namespace', namespace);
        (handler ? $el.find(handler) : $el).css('cursor', 'move');
    },
    unbind: function(el) {
        var $el = $(el);
        var namespace = $el.data('namespace');
        $el.off(namespace);
        $(document).off(namespace);
    }
};