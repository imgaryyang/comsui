### 安装运行和开发环境
* 后台：JAVA，Mysql，maven，eclipse
* 前端：Node.js, sublime(推荐)

### 需要掌握的前端框架，库
* [vue v2.0](https://vuefe.cn/v2/guide/)
* [element-ui v1.0.9](http://element.eleme.io/1.0/#/zh-CN/component/installation) 

有时间也可以了解下下面这些   

* [vue-rotuer v2.0](https://router.vuejs.org/zh-cn/)
* [vuex v2.0](https://vuex.vuejs.org/zh-cn/)
* [ES6 JavaScript](http://es6.ruanyifeng.com/)
    箭头函数；
    const, let；
    Promise；
    变量解构；
    (以上是用的较多的)
* [webpack v1.x](http://webpack.github.io/)


### 编译
将vue代码打包为原生js文件，以便在浏览器中执行
* 开发环境调试：npm run dev
* 生产环境上线：npm run dist (此命令一般情况不用执行，除了做测试的情况。上线是运维会调用此命令)


### 项目代码
路径：earth/webapp/static

主要目录及文件说明：
```
webpack.config.dev.js: 开发环境下的webpack配置
webpack.config.dist.js: 开发环境下的webpack配置
package.json: 前端依赖库，插件的版本管理
index.tpl.jsp: 入口html的模版文件
/src: vue代码在下
    api: 跟后台异步通信部分
    assets: 用到的一些非组件资源
    components: 公共组件
    directives: 全局指令
    filters: 全局过滤器
    mixins: 也是些公共代码，但不是组件
    routes: 路由配置，根据导航一级菜单拆分为各个文件，异步加载。URL的定义格式为：一级菜单/二级菜单/三级菜单/.../具体的某张页面名称
    store: 全局状态，所有组件都能访问
    views: 具体的某张页面的代码
        include: 也是公共组件，但是是跟业务关联程度更高的，而components中的组件更通用点
        其他按照一级菜单拆分为各个目录
    main.js: 入口js文件
    config.js: 配置信息
    authorize.js: 权限校验
    element-ui.js: 定义需要经常用到的element-ui库中的组件，以全局方式加载
    validators.js: 定义一些常用的表单校验器
```

### 规范

##### 命名
1. url字母之间用```-```连接
2. vue组件首字母大写，单词之间用驼峰。其他文件用```-```连接



### 碰到的一些问题
* https://github.com/webpack/webpack/tree/master/examples/chunkhash
* https://github.com/vuejs/vue-loader/issues/287
* https://segmentfault.com/a/1190000006435886
* https://segmentfault.com/a/1190000005128101
* http://webpack.github.io/docs/optimization.html
* https://github.com/yiminghe/async-validator#transform
* https://github.com/lmk123/blog/issues/28
* https://github.com/xiaoyann/webpack-react-redux-es6-boilerplate/blob/master/build/webpack.config.js#L184-L203