function ResquestError(response, xhr) {
    var { code, message } = response;
    this.code = code == null ? -1 : code;
    this.message = message;
    this.response = response;
    this.xhr = xhr;
}

ResquestError.CODE_DESCRIPTION = {
    [-1]: '系统错误'
};

ResquestError.prototype = new Error();

ResquestError.prototype.constructor = ResquestError;

ResquestError.prototype.toString = function() {
    var { message, code } = this;
    return message ? message : ResquestError.CODE_DESCRIPTION[code];
};

export default ResquestError;