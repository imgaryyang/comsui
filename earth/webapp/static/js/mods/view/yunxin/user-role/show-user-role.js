define(function(require, exports, module) {
    var baseView = require('baseView/baseFormView');
    var Pagination = require('component/pagination');
    var BaseFormView = baseView.BaseFormView;

    var MinePagination = Pagination.extend({
        refreshTableDataList: function(data, opepration, response, query) {
            if (!this.template) return;

            var htm;
            if (data.length < 1) {
                htm = '<tr class="nomore"><td style="text-align: center;" colspan="' + this.rowNum + '">没有更多数据</td></tr>';
                $('.has-bind').hide();
            } else {
                htm = this.template({
                    list: this.polish(data)
                });
            }
            this.tableListEl.html(htm);
            this.trigger('refresh', data, opepration, response, query);
        }
    });

    var ShowUserRoleView = BaseFormView.extend({
        el: '.content',
        initialize: function(userId) {
            ShowUserRoleView.__super__.initialize.apply(this, arguments);

            this.pagination = new MinePagination({
                el: '.has-bind'
            });
        }
    });

    module.exports = ShowUserRoleView;

});