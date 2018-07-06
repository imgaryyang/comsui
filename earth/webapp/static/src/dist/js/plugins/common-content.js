var global_helperObj={appendFormParams:function(t,e){var i=$('<input type="hidden">').get(0),n=document.createDocumentFragment();for(var o in t){var c=i.cloneNode();c.name=o,c.value=t[o],n.appendChild(c)}e.appendChild(n)},collectSortParams:function(t){t||(t=$(document));var e={};return t.find(".select-sort-type").each(function(t){var i={},n=$(this).data("key");i[n]=$(this).hasClass("desc")?"desc":"asc",$.extend(e,i)}),e}};!function(t,e){function i(t,e){this.el=t,this.options=e||{},this._init()}i.prototype._init=function(){var t=this.el,e=this.dataListEl=t.find(".data-list"),i=this.checkallBtns=t.find(".check-all-btn");t.on("click",".check-all-btn",function(t){var n=t.target.checked;e.find('input[type="checkbox"]').each(function(){!this.disabled&&(this.checked=n)}),i.each(function(){this.checked=n})}),this._initClickToCheckEvent(this.options)},i.prototype._initClickToCheckEvent=function(t){function i(t){t===!1&&o.each(function(){this.checked=!1})}var n=this.el,o=this.checkallBtns;if(t.clickTrToChecked){var c,a;n.on("click.howCheck","tr",function(t){if(!(a-c>200)){for(var n=e(t.target),o=["input[type=text]","a[href]","button"],r=0,h=o.length;r<h;r++)if(n.is(o[r]))return;if(n.is("input[type=checkbox]"))return void i(n.get(0).checked);var s=e(this).find('input[type="checkbox"]').get(0);s&&!s.disabled&&(source=!!s.checked,s.checked=!source,i(!source))}}).on("mousedown.howCheck","tr",function(){c=Date.now()}).on("mouseup.howCheck","tr",function(){a=Date.now()})}else n.on("click.howCheck","input[type=checkbox]",function(t){var e=this.checked;i(e)})},i.prototype.refresh=function(t,e){var i=this.el;t.clickTrToChecked!==e.clickTrToChecked&&(i.off("click.howCheck","tr").off("mousedown.howCheck").off("mouseup.howCheck"),this._initClickToCheckEvent(e))},i.prototype.setOptions=function(t){var i=e.extend({},this.options);e.extend(this.options,t||{}),this.refresh(i,this.options)};var n=null;t.checkAllHelper=function(t,e){if("string"==typeof t)n[t](e);else{var o=t;n=new i(o,e)}}}(global_helperObj,jQuery),function(t,e){function i(t,e){var i,n=function(){i.setLocalDate("")};e=c(t,e),t.datetimepicker(e).on("changeDate",function(i){t.data("date")?t.addClass("full"):t.removeClass("full"),e.changeDate&&e.changeDate(i)}).on("click",".input-group-addon",n),i=t.data("datetimepicker");var o=i.destory;return i.destory=function(){o.call(i),t.off("click",n)},i}function n(t,e){this.$el=t,this.$el.data("datetimepicker",this),this.options=e||{},this.startDateOptions=this.options.startDatePicker,this.endDateOptions=this.options.endDatePicker,this._init()}var o={language:"zh-CN"},c=function(t,i){var n=t.data(),c=e.extend({},o,n,i);return c.pickTime?(c.format="yyyy-MM-dd hh:mm:ss",c.autoclose=!1):(c.format="yyyy-MM-dd",c.autoclose=!0),c.startDate&&(c.startDate=new Date(c.startDate)),c.endDate&&(c.endDate=new Date(c.endDate)),c};n.prototype._init=function(){var t=this,n=this.$el.find(".starttime-datepicker"),o=n.find("input").val(),c=this.$el.find(".endtime-datepicker"),a=c.find("input").val(),r=e.extend({},this.startDateOptions,{changeDate:function(e){var i=t.startPicker.getDate();t.endPicker.setStartDate(i)}});a&&(r.endDate=a);var h=e.extend({},this.endDateOptions,{changeDate:function(e){var i=t.endPicker.getDate();t.startPicker.setEndDate(i)}});o&&(h.startDate=o),this.startPicker=new i(n,r),this.endPicker=new i(c,h)},n.prototype.destory=function(){this.startPicker.destory(),this.endPicker.destory(),delete this.$el.data().datetimepicker},t.datepicker=function(t,e){return new i(t,e)},t.beginEndDatepicker=function(t,e){return new n(t,e)},e(document).on("click",".imitate-datetimepicker-input",function(t){var o,c,a=e(this);!a.attr("disabled")&&a.is(".emitbyclick")&&(a.data("datetimepicker")||(c=a.parents(".beginend-datepicker"),c.length?(o=new n(c),a.data("datetimepicker").show()):(o=new i(a),o.show())))})}(global_helperObj,jQuery),function(t,e){function i(t){var e=t.offset(),i=t.width(),n=t.height(),o={left:e.left+i/2,top:e.top,width:i,height:n};return o}function n(t){this.wrapper=e('<div class="help-tip"></div>'),this.wrapper.css("position","fixed"),this.setOptions(t)}var o=e(document.body),c=e(window);n.prototype.show=function(){this.resolve(),o.append(this.wrapper)},n.prototype.hide=function(){this.wrapper.remove()},n.prototype.getWrapperSize=function(){var t=this.wrapper.clone();t.css({visibility:"hidden",float:"left"}),o.append(t);var e={width:t[0].offsetWidth,height:t[0].offsetHeight};return t.remove(),e},n.prototype.caluteXY=function(){var t={height:c.height(),width:c.width()},e=i(this.el),n=this.getWrapperSize(),o=2*(t.width-e.left);o<n.width&&(n.width=o,this.wrapper.css("width",o));var a={left:e.left-n.width/2,top:e.top-n.height-15};return a},n.prototype.resolve=function(){this.wrapper.css(this.caluteXY())},n.prototype.setOptions=function(t){t||(t={}),this.el=t.el,this.txt=t.txt,this.wrapper.html(this.txt)};var a=window.helpTip=new n;e(document).on("mouseover","[data-title]",function(){var t=e(this),i=t.data("title");i&&(a.setOptions({el:t,txt:i}),a.show())}).on("mouseleave","[data-title]",function(){a.hide()})}(global_helperObj,jQuery),function(t,e){var i=function(t,i){i&&i.groupSelector&&(this.$container=t.on("click.radiocheckbox",i.itemSelector||"[type=checkbox]",function(t){var n=e(t.currentTarget);if(n.is(":checked")){var o=n.closest(i.groupSelector),c=o.find("[type=checkbox]").not(n.get(0));c.each(function(){this.checked=!1})}}))};i.prototype.remove=function(){this.$container.off("click.radiocheckbox"),this.$container=null},e.fn.simulateRadioAction=function(t){this.each(function(){var n=e(this),o=n.data("radioCheckboxControl");o||n.data("radioCheckboxControl",new i(n,t))})},t.RadioCheckboxControl=i}(global_helperObj,jQuery),function(t){var e=$("[data-commoncontent]");e.length>0&&t(e)}(function(t){t.trigger("initui.commoncontent")});