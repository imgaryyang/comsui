<style lang="sass">
    #roleEdit {
        .permissions {
            th {
                line-height: 1.414;
            }

            td {
                border: none;
            }

            tbody tr {
                border-top: 1px solid #d9d9d9;
            }

            .el-tree__empty-block {
                min-height: auto;
                color: #666;
            }

            .el-tree-node__content {
                display: inline-block;
                line-height: 30px;
                height: 30px;

                &:hover {
                    background: transparent;
                }
            }

            .el-tree-node__expand-icon {
                display: none;
            }

            .el-tree-node__label {
                font-size: 12px;
            }
        }
    }
</style>

<template>
    <div class="content" id="roleEdit">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[
                { title: '角色管理' }, 
                { title: $route.params.id ? '编辑角色' : '添加角色' }
            ]"></Breadcrumb>

            <div style="width: 80%; margin: 30px auto;">
                <el-form 
                    :model="model" 
                    :rules="rules" 
                    ref="form"
                    label-width="90px" 
                    style="margin-left: 0;"
                    class="sdf-form">
                    <el-form-item label="角色名称" prop="roleName" required>
                        <el-input 
                            v-model="model.roleName" 
                            class="middle"></el-input>
                    </el-form-item>
                    <el-form-item label="角色备注">
                        <el-input 
                            v-model="model.roleRemark"
                            class="long" 
                            type="textarea"
                            placeholder="最多输入30字"></el-input>
                    </el-form-item>
                    <el-form-item label="权限配置">
                        <div class="table without-alternative-bg">
                            <div class="block">
                                <table class="permissions" style="table-layout: fixed;">
                                    <thead>
                                        <tr>
                                            <th width="120">模块</th>
                                            <th width="310">权限</th>
                                            <th>权限描述</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr v-for="(menu, index) in menus">
                                            <td>
                                                <el-checkbox
                                                    :value="menu.checked"
                                                    @input="handleLevel1MenuCheckChange(index, menu, [ menu, arguments[0] ])"></el-checkbox>
                                                &nbsp;
                                                <span 
                                                    class="text"
                                                    style="cursor: pointer; font-weight: bold; " 
                                                    @click="handleExpanded(menu)">
                                                    {{ menu.name }}
                                                </span>
                                            </td>
                                            <td colspan="2">
                                                <div v-show="menu.expanded">
                                                    <el-tree
                                                        ref="menus"
                                                        :expand-on-click-node="false"
                                                        :check-strictly="true"
                                                        show-checkbox
                                                        node-key="id"
                                                        style="border: none;" 
                                                        @check-change="handleCheckChange(index, menu, arguments)"
                                                        :render-content="renderContent"
                                                        :data="menu.children" 
                                                        :default-checked-keys="menu.defaultCheckedKeys"
                                                        :default-expand-all="true"
                                                        :props="{
                                                            label: 'name',
                                                            children: 'children'
                                                        }">
                                                    </el-tree>
                                                </div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </el-form-item>
                </el-form>

                <el-button 
                    style="margin-left: 90px;"
                    @click="submit" 
                    type="primary">
                    提交
                </el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import { Tree } from 'element-ui';
    import { excludeChineseCharactor } from 'src/validators';

    const filter = function(target, condition) {
        var condKeys = Object.keys(condition);
        var results = target.filter(menu => {
            return condKeys.every(condKey => condition[condKey] === menu[condKey]);
        });
        return results;
    };

    export default {
        components: {
            [Tree.name]: Tree
        },
        data: function() {

            return {
                fetching: false,
                rules: {
                    roleName: [
                        { required: true, message: ' ', trigger: 'blur' },
                    ],
                },
                model: {
                    roleName: '',
                    roleRemark: ''
                },
                menus: []
            }
        },
        deactivated: function() {
            this.$refs.form.resetFields();
        },
        activated: function() {
            if (this.$route.params.id) {
                this.updateFetch();
            } else {
                this.createFetch();
            }
        },
        methods: {
            handleExpanded: function(level1Menu) {
                var current = !level1Menu.expanded;

                if (current === true) {
                    this.menus.forEach(item => {
                        if (item.id !== level1Menu.id) {
                            item.expanded = false;
                        }
                    });
                }

                level1Menu.expanded = current;
            },
            handleLevel1MenuCheckChange: function(index, level1Menu, [data, checked]) {
                var $level1Menu = this.$refs.menus[index];
                var checkedKeys = $level1Menu.getCheckedKeys();

                if (checked) {
                    var allNotInclude = data.children.every(item => {
                        return !checkedKeys.includes(item.id)
                    });
                    if (allNotInclude) {
                        for (var i = 0; i < data.children.length; i++) {
                            $level1Menu.setChecked(data.children[i].id, true, true);
                        }
                    }
                } else {
                    for (var i = 0; i < data.children.length; i++) {
                        $level1Menu.setChecked(data.children[i].id, false, true);
                    }
                }

                this.$set(level1Menu, 'checked', checked);
            },
            handleCheckChange: function(index, level1Menu, [data, checked]) {
                var $level1Menu = this.$refs.menus[index];
                var checkedKeys = $level1Menu.getCheckedKeys();

                // 往下回溯
                if (checked) {
                    var allNotInclude = data.children.every(item => {
                        return !checkedKeys.includes(item.id)
                    });
                    if (allNotInclude) {
                        for (var i = 0; i < data.children.length; i++) {
                            $level1Menu.setChecked(data.children[i].id, true, true);
                        }
                    }
                } else {
                    for (var i = 0; i < data.children.length; i++) {
                        $level1Menu.setChecked(data.children[i].id, false, true);
                    }
                }

                // 往上回溯
                if (checked && data.parentId) {
                    if (data.level === 1) {
                        this.$set(level1Menu, 'checked', checked);
                    } else {
                        $level1Menu.setChecked(data.parentId, checked);
                    }
                }
            },
            renderContent: function(h, { data, node }) {
                var caculate = function(level) {
                    return 286 - 16 * (level - 1); // 46px
                };

                return h('span', [
                    h('span', { attrs: { style: data.type === 'element' ? `display: inline-block; width: ${caculate(data.level)}px` : `font-weight: bold; display: inline-block; width: ${caculate(data.level)}px` } }, data.name),
                    h('span', { attrs: { title: data.type === 'element' ? data.description : ''} }, data.type === 'element' ? data.description : '')
                ]);
            },
            submit: function() {
                const { form, menus } = this.$refs;

                form.validate(valid => {
                    if (valid) {
                        var isUpdate = this.$route.params.id != null;
                        var ids = [];

                        menus.forEach(menu => {
                            ids = ids.concat(menu.getCheckedKeys());
                        });

                        var buttonIds = [];
                        var menuIds = this.menus.filter(menu => menu.checked).map(menu => menu.id.replace('m_', ''));

                        ids.forEach(item => item.startsWith('b_') ? buttonIds.push(item.replace('b_', '')) : menuIds.push(item.replace('m_', '')))

                        ajaxPromise({
                            url: isUpdate ? `/edit-systemrole` : `/create-systemrole`,
                            data: {
                                ...this.model,
                                buttonIds: buttonIds.join(','),
                                menuIds: menuIds.join(',')
                            }
                        }).then(data => {
                            MessageBox.once('closed', () => this.$router.push('/system/role'));
                            MessageBox.open(isUpdate ? '修改成功' : '创建成功');
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                });
            },
            createFetch: function() {
                this.fetching = true;
                ajaxPromise({
                    url: `/before-create-systemrole`
                }).then(data => {
                    this.model = {};
                    var menus_elements = this.purifyData(data.menus, data.buttons);
                    this._menus = menus_elements;
                    this.menus = this.parseData(menus_elements);
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            },
            updateFetch: function() {
                this.fetching = true;
                ajaxPromise({
                    url: `/before-edit-systemrole`,
                    data: {
                        id: this.$route.params.id
                    }
                }).then(data => {
                    this.model = data.data;
                    var menus_elements = this.purifyData(data.menus, data.buttons);
                   
                    this._menus = menus_elements;
                    this.menus = this.parseData(menus_elements);
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            },
            purifyData: function(menus, elements) {
                var formatMenus = menus.map(item => {
                    return {
                        type: 'menu',
                        checked: item.checked,
                        description: item.url,
                        name: item.name,
                        id: 'm_' + item.id,
                        parentId: item.parentId ? 'm_' + item.parentId : undefined,
                        level: item.systemMenuLevel,
                    };
                });

                var formatElements = elements.map(item => {
                    var menu = formatMenus.filter(menu => {
                        return 'm_' + item.menuId == menu.id;
                    })[0];

                    if (!menu) return null;

                    return {
                        type: 'element',
                        checked: item.checked,
                        description: item.description,
                        name: item.name,
                        id: 'b_' + item.id,
                        parentId: menu.id,
                        level: menu.level + 1,
                    };
                });

                formatElements = formatElements.filter(item => !!item);

                return [].concat(formatMenus, formatElements);
            },
            parseData: function(list) {
                var level1Menus = filter(list, { level: 0 });

                var results = level1Menus.map((menu, index) => {
                    var r = this.composite2Tree(menu);

                    var ids = r.flattenChildren.filter(item => item.checked).map(item => item.id);

                    r.expanded = false;
                    r.defaultCheckedKeys = ids;

                    return r;
                });
               
                return results;
            },
            composite2Tree: function(parent) {
                var menus = this._menus;
                var flattenChildren = [];
                var parse = function(subParent, level) {
                    var c = { parentId: subParent.id, level: level };
                    var children = filter(menus, c) || [];

                    for (var i = 0; i < children.length; i++) {
                        parse(children[i], level + 1);
                    }

                    subParent.children = children;
                    flattenChildren = flattenChildren.concat(children);
                };

                parse(parent, 1);

                parent.flattenChildren = flattenChildren;
                
                return parent;
            },
        }
    }
</script>