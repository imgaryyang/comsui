export const REGEXPS = {
    MOBILE: /^1[3|4|5|7|8][0-9]\d{4,8}$/, // 手机号
    PHONE_EXT: /^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})([*#][0-9]{1,4})?$/, // 合法固话或分机号

    NUMBER: /^[0-9]+$/, 
    POSITIVE_INTEGER: /^[1-9][0-9]*$/, // 正整数
    MONEY: /^[0-9]+[\.]?[0-9]{0,3}$/,
    NON_NEGATIVE_INTEGER: /^[0-9]+$/, // 非负整数

    CHINESE_CHARACTOR: /[\u4e00-\u9fa5]{1,}/, // 汉字字符
    ENGLISH_NAME: /^[A-Za-z,.]+$/,
    NUMBER_AND_ALPHABET: /^[A-Za-z0-9]+$/, // 英文字母和数字
    INCLUDE_CHINESE_CHARACTOR: /[\u4e00-\u9fa5]{1,}/,
    INCLUDE_NUMBER: /[0-9]/,
    ID_CARD: /(^\d{15}$)|(^\d{17}([0-9]|X)$)/,
};


// 联系方式
export const contact = function(value) {
    return REGEXPS.PHONE_EXT.test(value) || REGEXPS.MOBILE.test(value);
};

// 正数
export const positiveNumber = function(value) {
    return value != '' && (+value) > 0;
};

// 非负数
export const nonNegativeNumber = function(value) {
    return value != '' && (+value) >= 0;
};

// 不包含中文字符
export const excludeChineseCharactor = function(value) {
    return !REGEXPS.CHINESE_CHARACTOR.test(value);
};

export const idCard = function(value) {
    var isValid = REGEXPS.ID_CARD.test(value);

    if (!isValid) {
        return false;
    }

    if (value.length == 18) {
        // 计算校验位
        var totalCheck = (new Number(value.charAt(0)) + new Number(value.charAt(10))) * 7 + (new Number(value.charAt(1)) + new Number(value.charAt(11))) * 9 + (new Number(value.charAt(2)) + new Number(value.charAt(12))) * 10 + (new Number(value.charAt(3)) + new Number(value.charAt(13))) * 5 + (new Number(value.charAt(4)) + new Number(value.charAt(14))) * 8 + (new Number(value.charAt(5)) + new Number(value.charAt(15))) * 4 + (new Number(value.charAt(6)) + new Number(value.charAt(16))) * 2 + new Number(value.charAt(7)) * 1 + new Number(value.charAt(8)) * 6 + new Number(value.charAt(9)) * 3
        var indexCheck = totalCheck % 11;
        var strCheckCodes = '10X98765432';
        var charCheck = strCheckCodes.charAt(indexCheck);
        if (charCheck === value.charAt(17)) {
            isValid = true;
        } else if (value.charAt(17) === 'A') {
            isValid = true;
        } else {
            isValid = false;
        }
    }
    
    return isValid;
}

