define(function(require, exports, module) {
    var $ = jQuery;
    var $balanceDialog = $('#balanceDialog');
    var template = _.template($('#balanceDialog').html());
    var root = global_config.root;
    var loading = global_const.loadingImg.clone().css('margin-top', 35);

    function getBalance(data) {
        $balanceDialog.modal();
        $balanceDialog.find('.modal-body').html('<span style="margin-top: 35px; display: inline-block;">余额查询中...</span>');

        var opt = {
            url: root + '/capital/balance',
            dataType: 'json',
            data: data
        };

        opt.success = function(resp) {
            if (resp.code == 0) {
                var htm = template(resp.data.balanceQueryResult);
                $balanceDialog.html(htm);
            } else {
                var notice = '<span style="margin-top: 35px; display: inline-block;">' + resp.message + '<span>';
                $balanceDialog.find('.modal-body').html(notice);
            }
        };

        $.ajax(opt);
    }

    $('select.rich-dropdown-menu').on('loaded.bs.select', function() {
        $(document).on('click', '.link', function(e) {
            var $target = $(e.currentTarget);
            var data = $target.data();
            getBalance(data);
        });
    });
});
