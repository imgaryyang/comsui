import Cascader from './main';

/* istanbul ignore next */
// Cascader.install = function(Vue) {
//   Vue.component(Cascader.name, Cascader);
// };

export default Cascader;

//使用之前确保以下
// 1、安装npm依赖
// babel-plugin-syntax-jsx\
// babel-plugin-transform-vue-jsx\
// babel-helper-vue-jsx-merge-props\
// 2、修改 .babelrc
// {  "presets": ["es2015"],  "plugins": ["transform-vue-jsx"]
// }