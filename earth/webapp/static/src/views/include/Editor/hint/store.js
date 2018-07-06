import { ajaxPromise } from 'assets/javascripts/util';

var isArray = function(obj) {
    return Object.prototype.toString.call(obj) === '[object Array]';
};

var packFunction = function(likeName) {
    return function(packageFullName) {
        return packageFullName.endsWith(likeName);
    }
};

var libraryStore = {
    ['com.zufangbao.sun.utils.EnumUtil']: {
        getKVList: {
            text: 'getKVList',
            doc: 'getKVList(Sting name, Object obj):List'
        },
        getKVMap: {
            text: 'getKVMap'
        }
    },
    ['java.lang.Math']: {
        BigNumber: {
            __type__: 'object',
            sum: {
                text: 'sum'
            },
            divison: {
                text: 'divison'
            }
        },
        abs: {
            text: 'abs'
        },
        ceil: {
            text: 'ceil'
        },
        floor: {
            text: 'floor'
        },
        round: {
            text: 'round'
        }
    }
};

export const library = {
    exist: function(packageFullName) {
        return libraryStore[packageFullName];
    },

    fetch: function() {
        var packageFullNames;

        if (!isArray(arguments[0])) {
            packageFullNames = [arguments[0]];
        } else {
            packageFullNames = arguments[0];
        }

        var notExistPackageFullNames = packageFullNames.filter(packageFullName => !this.exist(packageFullName));
        var existPackageFullNames = packageFullNames.filter(packageFullName => this.exist(packageFullName));

        if (notExistPackageFullNames.length === 0) {
            return Promise.resolve(existPackageFullNames);
        }

        return ajaxPromise({
            url: ``,
            data: {
                packageFullNames: JSON.stringify(notExistPackageFullNames)
            }
        }).then(packages => {
            return existPackageFullNames.concat(notExistPackageFullNames);
        }).catch((message) => {
            console.error(message);
        }).then(() => {
            return existPackageFullNames;
        });
    },

    search: function() {
        var result = {};
        var check;

        if (typeof arguments[0] === 'string') {
            check = packFunction(arguments[0]);
        } else if (typeof arguments[0] === 'function') {
            check = arguments[0];
        } else {
            throw '无效的参数类型';
        }

        for (var packageFullName in libraryStore) {
            if (check(packageFullName)) {
                result[packageFullName] = libraryStore[packageFullName];
            }
        }

        return result;
    },

    get: function(packageFullName) {
        return libraryStore[packageFullName];
    }
};

var snippetStore = {
    sysout: {
        text: 'System.out.println(${})'
    },
    cls: {
        text: 'public class ${} {\n}'
    }
};

export const snippet = {
    exist: function(snippetName) {
        return snippetStore[snippetName];
    },

    search: function() {
        var result = {};
        var check;

        if (typeof arguments[0] === 'string') {
            check = packFunction(arguments[0]);
        } else if (typeof arguments[0] === 'function') {
            check = arguments[0];
        } else {
            throw '无效的参数类型';
        }

        for (var snippetName in snippetStore) {
            if (check(snippetName)) {
                result[snippetName] = snippetStore[snippetName];
            }
        }

        return result;
    },

    get: function(snippetName) {
        return snippetStore[snippetName];
    }
};



