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
    this.tree = tree
    this.array = this.flatten(tree)
}

Flat.prototype.flatten = function(tree) {
    var result = []
    var identity = new Identity()
    var rescusive = (items, parentId = null) => {
        items.forEach(item => {
            var {children, ...rest} = item
            rest._id = identity.getNextId(parentId)
            rest.parentId = parentId
            if (Array.isArray(children)) {
                result.push(rest)
                rescusive(children, rest._id)
            }else{
                rest._isLastLevel = true
                result.push(rest)
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
        var index = this.array.findIndex(item => item._id === id)
        return this.array[index]
    })
}

export default Flat


