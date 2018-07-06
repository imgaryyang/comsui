function Identity() {
    this._index = 0
}

Identity.prototype.getNextId = function(prev) {
    this._index++
    if (prev == null) {
        return '' + this._index
    } else {
        return prev + '_' + this._index
    }
}

function Flat(tree) {
    if (!(this instanceof Flat)){
        return new Flat(tree)
    }
    this.tree = tree
    this.array = this.flatten(tree)
}

Flat.prototype.flatten = function(tree=[]) {
    var result = []
    var identity = new Identity()
    var rescusive = (items, parentId = null) => {
        items.forEach(item => {
            var {children,capitalAccount,subAccounts, ...rest} = item

            if(Array.isArray(children)){
                rest._id = identity.getNextId(parentId)
                rest.parentId = parentId
                result.push(rest)
                rescusive(children, rest._id)
            }else{
                if(capitalAccount || subAccounts){
                    subAccounts = subAccounts || []
                    rest._id = identity.getNextId(parentId)
                    rest.parentId = parentId
                    result.push(rest)
                    var list = [capitalAccount, ...subAccounts]
                    list.forEach(item => {
                        item.financialContractId = rest.financialContractId
                        item.contractNo = rest.contractNo
                        //把二级需要过滤的label和三级需要过滤的银行卡号拼接
                        //直接过滤最后一级的label实现二级和三级菜单的过滤
                        item.label = rest.label + '(' + item.accountNo + ')'
                    })
                    rescusive(list, rest._id)
                }else{
                    rest._id = identity.getNextId(parentId)
                    rest.parentId = parentId
                    rest._isLastLevel = true
                    // rest.label = rest.contractNo+ '(' + rest.accountNo+ ')'
                    result.push(rest)
                }
            }
        })
    }
    rescusive(tree)
    return result
}

Flat.prototype.backtracking = function(id) {
    var tmp = id
    var reg = /_(\d+)$/
    var ids = []
    while (true) {
        ids.unshift(tmp)
        if (reg.test(tmp)) {
            tmp = tmp.replace(reg, '')
        } else {
            break
        }
    }
    return ids.map(id => {
        var index = this.array.findIndex(item => item.id === id)
        return this.array[index]
    })
}

export default Flat