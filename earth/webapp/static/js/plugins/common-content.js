// 主内容区域的初始化UI

var global_helperObj = {
    // utils function
    appendFormParams: function(params, el) {
        var input = $('<input type="hidden">').get(0);
        var fragment = document.createDocumentFragment();
        for (var prop in params) {
            var node = input.cloneNode();
            node.name = prop;
            node.value = params[prop];
            fragment.appendChild(node);
        }
        el.appendChild(fragment);
    },
    /**
     * 好像没有用到，那就删掉
     * @deprecated
     */
    collectSortParams: function(root) {
        root || (root = $(document));
        var sortParams = {};
        root.find('.select-sort-type').each(function(el) {
            var item = {};
            var key = $(this).data('key');
            item[key] = $(this).hasClass('desc') ? 'desc' : 'asc';
            $.extend(sortParams, item);
        });
        return sortParams;
    }
};

// 全选操作
(function(exports, $) {
    // 应该要有一个filter
    function CheckAll(el, options) {
        this.el = el;
        this.options = options || {};
        this._init();
    }
    CheckAll.prototype._init = function() {
        var el = this.el;
        var dataListEl = this.dataListEl = el.find('.data-list'),
            checkalls = this.checkallBtns = el.find('.check-all-btn');

        el.on('click', '.check-all-btn', function(e) {
            var source = e.target.checked;
            dataListEl.find('input[type="checkbox"]').each(function() {
                !this.disabled && (this.checked = source);
            });
            checkalls.each(function() {
                this.checked = source;
            });
        });

        this._initClickToCheckEvent(this.options);
    }
    CheckAll.prototype._initClickToCheckEvent = function(options) {
        var el = this.el,
            checkalls = this.checkallBtns;

        if (options.clickTrToChecked) {
            var mousedownTime,
                mouseupTime;
            el.on('click.howCheck', 'tr', function(e) {
                if ((mouseupTime - mousedownTime) > 200) return; // 理解为长按
                var tar = $(e.target),
                    filters = ['input[type=text]', 'a[href]', 'button'];
                for (var i = 0, len = filters.length; i < len; i++) {
                    if (tar.is(filters[i])) return;
                }
                if (tar.is('input[type=checkbox]')) {
                    cancelCheckAll(tar.get(0).checked);
                    return;
                }
                var checkbox = $(this).find('input[type="checkbox"]').get(0);
                if (!checkbox || checkbox.disabled) return;
                source = !!(checkbox.checked);
                checkbox.checked = !source;
                cancelCheckAll(!source);
            }).on('mousedown.howCheck', 'tr', function() {
                mousedownTime = Date.now();
            }).on('mouseup.howCheck', 'tr', function() {
                mouseupTime = Date.now();
            });
        } else {
            el.on('click.howCheck', 'input[type=checkbox]', function(e) {
                var checked = this.checked;
                cancelCheckAll(checked);
            });
        }

        function cancelCheckAll(flag) {
            if (flag === false) {
                checkalls.each(function() {
                    this.checked = false;
                });
            }
        }
    }
    CheckAll.prototype.refresh = function(oldOpt, newOpt) {
        var el = this.el;
        if (oldOpt.clickTrToChecked !== newOpt.clickTrToChecked) {
            el.off('click.howCheck', 'tr')
                .off('mousedown.howCheck')
                .off('mouseup.howCheck');
            this._initClickToCheckEvent(newOpt);
        }
    }
    CheckAll.prototype.setOptions = function(options) {
        var oldOpt = $.extend({}, this.options);
        $.extend(this.options, options || {});
        this.refresh(oldOpt, this.options);
    }

    var obj = null;

    exports.checkAllHelper = function(method, options) {
        if (typeof method === 'string') {
            obj[method](options);
        } else {
            var root = method;
            obj = new CheckAll(root, options);
        }
    };
})(global_helperObj, jQuery);


// 时间选择器
(function(exports, $) {
    var DEFAULT_OPTIONS = {
        language: 'zh-CN'
    };

    var processOption = function($el, options) {
        var htmlOptions = $el.data();
        var resultOptions = $.extend({}, DEFAULT_OPTIONS,  htmlOptions, options);

        if (resultOptions.pickTime) {
            resultOptions.format = 'yyyy-MM-dd hh:mm:ss';
            resultOptions.autoclose = false;
        } else {
            resultOptions.format = 'yyyy-MM-dd';
            resultOptions.autoclose = true;
        }

        if (resultOptions.startDate) {
            resultOptions.startDate = new Date(resultOptions.startDate);
        }

        if (resultOptions.endDate) {
            resultOptions.endDate = new Date(resultOptions.endDate);
        }

        return resultOptions;
    };

    var isValidDate = function(date) {
        if ( Object.prototype.toString.call(date) === '[object Date]' ) {
            return isNaN(date.getTime()) ? false : true;
        } else {
            return false;
        }
    };

    function ItemDatepicker($el, options) {
        var self;
        var onClickClear = function() {
            self.setLocalDate('');
        };

        options = processOption($el, options);

        $el.datetimepicker(options)
            .on('changeDate', function(e) {
                $el.data('date') ? $el.addClass('full') : $el.removeClass('full');
                options.changeDate && options.changeDate(e);
            })
            .on('click', '.input-group-addon', onClickClear);

        self = $el.data('datetimepicker');

        var _destroy = self.destory;

        self.destory = function() {
            _destroy.call(self);
            $el.off('click', onClickClear);
        };

        return self;
    }


    function BeginEndDatepicker($el, options) {
        this.$el = $el;
        this.$el.data('datetimepicker', this);
        this.options = options || {};
        this.startDateOptions = this.options.startDatePicker;
        this.endDateOptions = this.options.endDatePicker;
        this._init();
    }
    BeginEndDatepicker.prototype._init = function() {
        var self = this;
        var $startPicker = this.$el.find('.starttime-datepicker');
        var defaultStartDate = $startPicker.find('input').val();
        var $endPicker = this.$el.find('.endtime-datepicker');
        var defaultEndDate = $endPicker.find('input').val();

        var sopt = $.extend({}, this.startDateOptions, {
            changeDate: function(e) {
                var stime =  self.startPicker.getDate();
                self.endPicker.setStartDate(stime);
            }
        });
        if (defaultEndDate) {
            sopt.endDate = defaultEndDate;
        }

        var eopt = $.extend({}, this.endDateOptions, {
            changeDate: function(e) {
                var etime =  self.endPicker.getDate();
                self.startPicker.setEndDate(etime);
            }
        });
        if (defaultStartDate) {
            eopt.startDate = defaultStartDate;
        }

        this.startPicker = new ItemDatepicker($startPicker, sopt);
        this.endPicker = new ItemDatepicker($endPicker, eopt);
    };
    BeginEndDatepicker.prototype.destory = function() {
        this.startPicker.destory();
        this.endPicker.destory();
        delete this.$el.data().datetimepicker;
    };

    exports.datepicker = function(el, options) {
        return new ItemDatepicker(el, options);
    };
    exports.beginEndDatepicker = function(el, options) {
        return new BeginEndDatepicker(el, options);
    };

    $(document).on('click', '.imitate-datetimepicker-input', function(e) {
        var datetimepicker;
        var $par;
        var $el = $(this);
        // 防止BeginEndDatepicker模式中一个disabled一个enabled的情况下的意外情况
        if ($el.attr('disabled') || !$el.is('.emitbyclick')) return;
        if ($el.data('datetimepicker')) return;

        $par = $el.parents('.beginend-datepicker');

        if ($par.length) {
            datetimepicker = new BeginEndDatepicker($par);
            $el.data('datetimepicker').show();
        } else {
            datetimepicker = new ItemDatepicker($el);
            datetimepicker.show();
        }
    });

})(global_helperObj, jQuery);


// 帮助提示
(function(exports, $) {
    var $body = $(document.body);
    var $win = $(window);

    function getCenterSize(el) {
        var elSize = el.offset();
        var width = el.width();
        var height = el.height();
        var centerSize = {
            left: elSize.left + width / 2,
            top: elSize.top, // 不变
            width: width,
            height: height
        };
        return centerSize;
    }

    function HelpTip(options) {
        this.wrapper = $('<div class="help-tip"></div>');
        this.wrapper.css('position', 'fixed');
        this.setOptions(options);
    }
    HelpTip.prototype.show = function() {
        this.resolve();
        $body.append(this.wrapper);
    }
    HelpTip.prototype.hide = function() {
        this.wrapper.remove();
    }
    HelpTip.prototype.getWrapperSize = function() {
        var wrapper = this.wrapper.clone();
        wrapper.css({
            visibility: 'hidden',
            float: 'left'
        });
        $body.append(wrapper);
        var res = {
            width: wrapper[0].offsetWidth,
            height: wrapper[0].offsetHeight
        };
        wrapper.remove();
        return res;
    }
    HelpTip.prototype.caluteXY = function() {
        var winSize = {
            height: $win.height(),
            width: $win.width()
        };
        var elSize = getCenterSize(this.el);
        var wrapperSize = this.getWrapperSize();
        var restWidth = (winSize.width - elSize.left) * 2;
        if (restWidth < wrapperSize.width) {
            wrapperSize.width = restWidth;
            this.wrapper.css('width', restWidth);
        }
        var res = {
            left: elSize.left - wrapperSize.width / 2,
            top: elSize.top - wrapperSize.height - 15
        };

        return res;
    }
    HelpTip.prototype.resolve = function() {
        this.wrapper.css(this.caluteXY());
    }
    HelpTip.prototype.setOptions = function(options) {
        options || (options = {});
        this.el = options.el;
        this.txt = options.txt;
        this.wrapper.html(this.txt);
    }

    var helpTip = window.helpTip = new HelpTip();

    $(document).on('mouseover', '[data-title]', function() {
        var tar = $(this);
        var title = tar.data('title');
        if (!title) return;

        helpTip.setOptions({
            el: tar,
            txt: title
        });
        helpTip.show();
    }).on('mouseleave', '[data-title]', function() {
        helpTip.hide();
    });
})(global_helperObj, jQuery);

// 使用checkbox模拟radio行为
(function(exports, $) {
    var RadioCheckboxControl = function($container, options) {
        if (options && options.groupSelector) {
            this.$container = $container.on('click.radiocheckbox', options.itemSelector || '[type=checkbox]', function(e) {
                var $target = $(e.currentTarget);

                if ($target.is(':checked')) {
                    var $group = $target.closest(options.groupSelector);
                    var $colleague = $group.find('[type=checkbox]').not($target.get(0));
                    $colleague.each(function() {
                        this.checked = false;
                    });
                }
            });
        }
    };

    RadioCheckboxControl.prototype.remove = function() {
        this.$container.off('click.radiocheckbox');
        this.$container = null;
    };

    $.fn.simulateRadioAction = function(options) {
        this.each(function() {
            var $el = $(this);
            var radioCheckboxControl = $el.data('radioCheckboxControl');
            if (!radioCheckboxControl) {
                $el.data('radioCheckboxControl', new RadioCheckboxControl($el, options));
            }
        });
    };

    exports.RadioCheckboxControl = RadioCheckboxControl;
})(global_helperObj, jQuery);


;(function(initUIFunc) {
    var mainEl = $('[data-commoncontent]');
    if (mainEl.length > 0) {
        initUIFunc(mainEl);
    }
})(function(rootEl) {
    // global_helperObj.checkAllHelper(rootEl);
    // global_helperObj.searchDatepicker(rootEl);
    rootEl.trigger('initui.commoncontent');
});