import { api } from '../../config';
import RequestError from './RequestError';

export function ajaxPromise(params) {
    return new Promise((resolve, reject) => {
        var opt = Object.assign({
            type: 'get',
            dataType: 'json',
        }, params);

        opt.url = ([api, opt.url].join('/')).replace(/(\/+)/g, '/');

        if (typeof opt.progress === 'function') {
            opt.xhr = function() {
                var xhr = $.ajaxSettings.xhr();
                xhr.onprogress = evt => opt.progress(evt.loaded/evt.total*100, xhr);
                return xhr;
            };
        }

        opt.success = function(resp, textStatus, xhr) {
            if (resp.code == 0) {
                resolve(params.parse ? params.parse(resp.data, resp) : resp.data);
            } else {
                reject(new RequestError(resp, xhr));
            }
        };

        opt.error = function(xhr, message) {
            var contentType = xhr.getResponseHeader('Content-Type');
            if (xhr.status === 200 && contentType.toLowerCase().indexOf('text/html') >= 0) {
                // 登陆过期
                location.reload();
            } else {
                reject(new RequestError({ message: message }, xhr));
            }
        };

        $.ajax(opt);
    });
};

// cancel后不会往下走
export const patchCancelPromise = function(promise) {
    var canceled = false;

    var p = new Promise((resolve, reject) => {
        promise.then(value => {
            canceled ? void 0 : resolve(value);
        });
        promise.catch(error => {
            canceled ? void 0 : reject(error);
        });
    });

    return {
        promise: p,
        cancel: () => {
            canceled = true;
        }
    };
};

export const searchify = function(query) {
    var temp = [];
    for (var prop in query) {
        temp.push(prop + '=' + query[prop]);
    }
    return temp.join('&');
};

export const purify = function() {
    var conds = {};
    var res = {};
    var args = Array.from(arguments);

    args.forEach(arg => Object.assign(conds, arg));

    Object.keys(conds).forEach(key => {
        var value = conds[key];
        if (value == null || value === '') return;
        if (value instanceof Array) {
            res[key] = JSON.stringify(value);
        } else {
            res[key] = value;
        }
    });

    return res;
};

export const downloadFile = function (url,options,fileName) {
    var search = searchify(purify(options));
    var aLink = document.createElement('a');
    var action;

    if (url.includes('?')) {
        action = api + url + '&' + search;
    } else {
        if(search == ''){
            action = api + url;
        } else {
            action = api + url + '?' + search;
        }
    }

    if (false) {
        fileName && (aLink.download = fileName);
        aLink.href = action;
        aLink.click();
    } else {
        window.open(action, 'download');
    }
};

/**
    列表页中信托合同下拉框优化后，其数据结构为[{},{}]
**/

export const filterQueryConds = function(query) {
    var list = [];
    var currentQueryConds = Object.assign({}, query);
    
    if (currentQueryConds.financialContractUuids && currentQueryConds.financialContractUuids.length) {
        list = currentQueryConds.financialContractUuids || [];
        currentQueryConds.financialContractUuids = list.map(item => item.value);
    }else if (currentQueryConds.financialContractIds && currentQueryConds.financialContractIds.length) {
        list = currentQueryConds.financialContractIds || [];
        currentQueryConds.financialContractIds = list.map(item => item.financialContractId);
    }
    return currentQueryConds;
}

export const filterExportQuery = function(query) {
    var value = '';
    var currentQuery = Object.assign({}, query);
    if (currentQuery.financialContractUuids && currentQuery.financialContractUuids.length) {
        value = currentQuery.financialContractUuids[0];
        currentQuery.financialContractUuid = value || '';
        delete  currentQuery.financialContractUuids;
    }else if (currentQuery.financialContractIds && currentQuery.financialContractIds.length) {
        value = currentQuery.financialContractIds[0];
        currentQuery.financialContractId = value || '';
        delete  currentQuery.financialContractIds;
    }
    return currentQuery;
}