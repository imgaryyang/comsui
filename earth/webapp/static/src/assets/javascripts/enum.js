const isArray = function(a) {
    return Object.prototype.toString.call(a) === '[object Array]';
};

const isPlainObject = function(o) {
    return o.constructor === Object;
};

const each = function(elements, callback) {
    if (isArray(elements)) {
        elements.forEach(callback);
    } else if (isPlainObject(elements)) {
        for (var prop in elements) {
            callback(elements[prop], +prop);
        }
    }
};


function EnumItem(ordinal, kw) {
    this.ordinal = ordinal;
    this.key = Object.keys(kw)[0];
    this.message = Object.values(kw)[0];
}

EnumItem.prototype.getOrdinal = function() {
    return this.ordinal;
};

EnumItem.prototype.getMessage = function() {
    return this.message;
};

EnumItem.prototype.getKey = function() {
    return this.key;
};

EnumItem.prototype.toString = function() {
    return this.message;
};

EnumItem.prototype.valueOf = function() {
    return this.ordinal;
};


// 枚举类型构造函数
function Enum(elements) {
    each(elements, (element, ordinal) => {
        const enumItem = new EnumItem(ordinal, element);
        this[enumItem.getOrdinal()] = this[enumItem.getKey()] = enumItem;
    });
}

// 用户构造vue的filter，返回中文message
Enum.filter = function(enums) {
    return function(index) {
        // index可以是ordinal或者可以key
        return enums[index] ? enums[index].getMessage() : '';
    }
};

export default Enum;

// usage
// const OFFLINE_BILL_STATUS = new Enum([
//     { UNPAID: '失败' },
//     { PAID: '成功' }
// ]);
// // 或指定ordinal
// const OFFLINE_BILL_STATUS = new Enum({
//     0: { UNPAID: '失败' },
//     1: { PAID: '成功' },
// });
// '' + OFFLINE_BILL_STATUS.OFFLINE_BILL_STATUS
// + OFFLINE_BILL_STATUS.OFFLINE_BILL_STATUS
// OFFLINE_BILL_STATUS.OFFLINE_BILL_STATUS.getKey();
// OFFLINE_BILL_STATUS.OFFLINE_BILL_STATUS.getOrdinal();
// OFFLINE_BILL_STATUS.OFFLINE_BILL_STATUS.getMessage();


