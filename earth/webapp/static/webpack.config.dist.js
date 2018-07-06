var path = require('path');
var webpack = require('webpack');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var WebpackMd5Hash = require('webpack-md5-hash');
var AssetsPlugin = require('assets-webpack-plugin');

var OUTPUT_PATH = path.resolve(__dirname, './dist/vue');
var DEPLOY_PRODUCTION = process.env.DEPLOY_PRODUCTION;
const prefix = DEPLOY_PRODUCTION == 'weifang' ? '/weifang' : '';
var OUTPUT_PUBLIC_PATH = `${prefix}/static/dist/vue/`;

module.exports = {
    entry: {
        polyfill: 'babel-polyfill',
        lib: ['vue', 'vue-router', 'vuex'],
        'element-ui': './src/element-ui',
        main: './src/main.js'
    },
    watchOptions: {
      poll:true
    },
    output: {
        filename: '[name].[chunkhash:8].js',
        chunkFilename: '[name].[chunkhash:8].js',
        path: OUTPUT_PATH,
        publicPath: OUTPUT_PUBLIC_PATH
    },
    resolve: {
        extensions: ['', '.js', '.vue'],
        alias: {
            vue: 'vue/dist/vue.common.js',
            src: path.join(__dirname, '/src'),
            api: path.join(__dirname, '/src/api'),
            store: path.join(__dirname, '/src/store'),
            assets: path.join(__dirname, '/src/assets'),
            views: path.join(__dirname, '/src/views'),
            components: path.join(__dirname, '/src/components'),
            mixins: path.join(__dirname, '/src/mixins'),
            directives: path.join(__dirname, '/src/directives'),
            filters: path.join(__dirname, '/src/filters')
        }
    },
    module: {
        loaders: [{
            test: /\.vue$/,
            loader: 'vue'
        }, {
            test: /\.js$/,
            exclude: /node_modules/,
            // exclude: /node_modules\/(?!(element-ui)\/).*/,
            loader: 'babel'
        }, {
            test: /\.css/,
            loader: ExtractTextPlugin.extract('css')
        }, {
            test: /\.(sass|scss)$/,
            loader: ExtractTextPlugin.extract('css!sass')
        }, {
            test: /\.(png|jpg|gif)$/,
            loader: 'url?limit=10000&name=img/[name].[ext]'
        }, {
            test: /\.(eot|svg|ttf|woff|woff2)(\?\S*)?$/,
            loader: 'file-loader'
        }]
    },
    // externals对象的key是给require时用的，比如require('react')，对象的value表示的是如何在global（即window）中访问到该对象，这里是window.React。
    externals: {
        jQuery: true,
        jquery: 'jQuery',
        highcharts: 'Highcharts'
    },
    vue: {
        loaders: {
            css: ExtractTextPlugin.extract('css'),
            sass: ExtractTextPlugin.extract('css!sass')
        }
    },
    plugins: [
        new webpack.NormalModuleReplacementPlugin(/\.[\/\\]locale[\/\\]lang/, './locale/lang/' + DEPLOY_PRODUCTION),
        // 注意：https://github.com/webpack/webpack/tree/master/examples/chunkhash
        new webpack.optimize.CommonsChunkPlugin({
            names: ['manifest'],
            filename: '[name].[hash:8].js'
        }),
        new webpack.optimize.CommonsChunkPlugin({
            name: 'lib',
            chunks: ['element-ui']
        }),
        new webpack.optimize.CommonsChunkPlugin({
            names: ['lib', 'element-ui'],
            chunks: ['main']
        }),
        new ExtractTextPlugin('[name].[contenthash:8].css', {
            allChunks: true // 默认只对入口分支处理，设置true，对异步生成的所有chunk也处理
        }),
        new webpack.ProvidePlugin({
            $: 'jQuery'
        }),
        new webpack.DefinePlugin({
            'process.env': {
                DEPLOY_PRODUCTION: '"' + DEPLOY_PRODUCTION + '"',
                NODE_ENV: '"production"'
            }
        }),
        new webpack.optimize.UglifyJsPlugin({
            sourceMap: true,
            compress: {
                warnings: false
            }
        }),
        new webpack.optimize.OccurenceOrderPlugin(),
        new WebpackMd5Hash(),
        new AssetsPlugin({
            includeManifest: 'manifest',
            prettyPrint: true,
            filename: 'stat.json',
            path: OUTPUT_PATH
        })
    ]
};