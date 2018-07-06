import Vue from 'vue';

var $body = $(document.body);
var id = 0;

var uid = function() {
    return ++id;
};

var create = function() {
    var $bar = $('<div/>');
    $bar.attr('progressuid', uid());
    $bar.css({
        height: '3px',
        background: '#15a151',
        position: 'fixed',
        transition: 'width 0.3s',
        zIndex: 9999,
        left: 0,
        top: 0,
        width: 0,
    });
    return $bar;
}

export default {
    create: function() {
        var $bar = create();
        $body.append($bar);

        return function(percent) {
            $bar.css('width', percent);
            if (percent === 100) {
                Vue.nextTick(() => $bar.remove())
            }
        }
    }
}
