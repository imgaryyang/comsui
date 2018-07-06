import base from './lang/base';

let lang = base;

let locale = function(keypath, options) {
    var value = lang[keypath];

    if (value == null) {
        return keypath;
    }

    if (typeof options !== 'object') {
        return value;
    }

    return value.replace(/{\s*([\s\S]+?)\s*}/g, (total, placeholder) => {
        var realValue = options[placeholder];
        return realValue ? realValue : placeholder;
    });
};

locale.use = function(localeLang) {
    lang = Object.assign({}, base, localeLang);
};

export default locale;