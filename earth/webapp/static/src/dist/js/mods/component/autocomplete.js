define(function(require,exports,module){var e=jQuery,n={action:"",container:null,search:{},parse:function(e){return e},parcelItem:function(e){},onSync:function(){},onClose:function(e,n){},onSubmit:function(e,n){e.val(n.text())}},t=function(e,n){var t=null;return function(){t&&(clearTimeout(t),t=null);var i=Array.prototype.slice.call(arguments);return t=setTimeout(function(){e.apply(null,i)},n||500)}};e.fn.autocomplete=function(i){return i=e.extend(n,i),this.each(function(){function n(n){var t=i.search;"function"==typeof t&&(t=t(n)),t.keyword=n;var c=function(e){a.empty(),o.show();var t=i.parse(e);if(0===t.length)o.addClass("nomatch");else{for(var c=0,r=t.length;c<r;c++){var l=i.parcelItem(t[c]);a.append(l)}o.html(a)}i.onSync(t,n,o)};if(i.action)e.getJSON(i.action,t,c);else if(i.responseData){var r=i.filter||function(e,n){return e},l=r(i.responseData,n);c(l)}}var c=e(this),r="",o=i.container,a=e('<ul class="list">'),l=function(e){o.is(":hidden")||(a.empty(),o.hide(),i.onClose(e,c))},s=function(n,t){i.onSubmit(c,e(n),t),l({triggerType:"select"})},u=t(function(e){n(e)},500);c.on("focus",function(){c.val()&&n(c.val())}),o.on("click",".item",function(e){e.stopPropagation(),s(e.currentTarget,"click")}),c.on("keydown",function(e){var n,t,i;switch(e.which){case 38:n=o.find(".item"),t=n.filter(".selected").removeClass("selected"),t.length>0&&(i=t.prev()),i&&0!==i.length||(i=n.last()),i.addClass("selected");break;case 40:n=o.find(".item"),t=n.filter(".selected").removeClass("selected"),t.length>0&&(i=t.next()),i&&0!==i.length||(i=n.first()),i.addClass("selected");break;case 13:n=o.find(".item"),t=n.filter(".selected"),t.length>0&&s(t,"enter")}}).on("keyup",function(e){var n=c.val();if(r!==n)return r=n,""===n?void l({triggerType:"clear"}):void(0===~[13,37,38,39,40].indexOf(e.which)&&u(n))}),e(document).on("click",function(n){n.target!==c.get(0)&&(e.contains(o.get(0),n.target)||l({triggerType:"blur"}))})}),this}});