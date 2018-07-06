const isValidDate = function(date) {
    if (Object.prototype.toString.call(date) === '[object Date]') {
        return isNaN(date.getTime()) ? false : true;
    } else {
        return false;
    }
};

const formatMoney = function(number, places, symbol, thousand, decimal) {
    places = !isNaN(places = Math.abs(places)) ? places : 2;
    symbol = symbol !== undefined ? symbol : "";
    thousand = thousand || ",";
    decimal = decimal || ".";

    // number = +number; // 不转换也是没事的，比较时会转为number进行比较
    var negative = number < 0 ? "-" : "",
        i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
        j = (j = i.length) > 3 ? j % 3 : 0;

    return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
};

const formatDate = function(_time, partten = "yyyy-MM-dd") {
    var time;

    if (typeof _time === 'number' || typeof _time === 'string') {
        time = new Date(_time);

        if (!isValidDate(time)) {
            _time = _time.replace(/-/g, '/')
            time = new Date(_time);
        }
    } else {
        time = _time;
    }

    var lengthNum = function (num) {
        return (+num).toString().length === 1 ? '0' + num : num;  
    };

    if (!isValidDate(time)) return '';

    var year = time.getFullYear(),
        month = time.getMonth()+1,
        day = time.getDate(),
        hour = time.getHours(),
        minute = time.getMinutes(),
        seconds = time.getSeconds();

    var res = partten.replace('yyyy', year)
        .replace('MM', lengthNum(month))
        .replace('dd', lengthNum(day))
        .replace('HH', lengthNum(hour))
        .replace('mm', lengthNum(minute))
        .replace('ss', lengthNum(seconds));

    return /NaN/i.test(res) ? '' : res;
};

const formatPercent = function(number) {
    if (number == undefined) return;
    var n = number.toString();
    var movePointerRight = function(){
        var s, s1, s2, ch, ps,
        ch = '.';

        s = n ? n : '';
        ps = s.split('.');
        s1 = ps[0] ? ps[0] : "";
        s2 = ps[1] ? ps[1] : "";
        if (s2.length <= 2)
        {
            ch = '';
            s2 = padRight(s2, 2);
        }
        return Number(s1 + s2.slice(0, 2) + ch + s2.slice(2, s2.length)) + '%';
    }
    var padRight = function(s2, nSize) {
        var len = 0,
        ch = '0';// 默认补0
        len = s2.length;
        while (len < nSize)
        {
            s2 = s2 + ch;
            len++;
        }
        return s2;
    }
    return movePointerRight();
    // if(number.toString().split('.')[1]){
    //     var l = number.toString().split('.')[1].length;
    //     var m = Math.pow(10, l);
    //     var n = Math.pow(10, l-2)
    //     return Math.round(number*m)/n + '%';
    // }else{
    //     return number*100 + '%';
    // }
};

//针对[2,6,4,8] 排序 
const sortArrayList = function(list) {
    var result = [].concat(list ? list : []) ;

    result.sort(function(value1, value2) {
        return value1 - value2;
    })

    return result;
}

export default { formatDate, formatMoney, formatPercent, sortArrayList};