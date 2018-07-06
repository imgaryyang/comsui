var gulp = require('gulp');
var browserSync = require('browser-sync').create();
var fs = require('fs');
var path = require('path');

var sass = require('gulp-sass');
var minifycss = require('gulp-minify-css');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var rename = require('gulp-rename');
var clean = require('gulp-clean');
var eslint = require('gulp-eslint');
var replace = require('gulp-replace');

var DIST_DIR = './dist/';
var CSS_DIST_DIR = './dist/css/';
var IMAGES_DIST_DIR = './dist/images/';
var JS_DIST_DIR = './dist/js/';
var FONT_DIST_DIR = './dist/fonts/';

gulp.task('lint', () => {
    return gulp.src(['./js/plugins/common-content.js', './js/mods/**/*.js', '!node_modules/**|^~/**'])
        // eslint() attaches the lint output to the 'eslint' property
        // of the file object so it can be used by other modules.
        .pipe(eslint())
        // eslint.format() outputs the lint results to the console.
        // Alternatively use eslint.formatEach() (see Docs).
        .pipe(eslint.format())
        // To have the process exit with an error code (1) on
        // lint error, return the stream and pipe to failAfterError last.
        .pipe(eslint.failAfterError());
});

// 代理
gulp.task('unittest', function() {
    browserSync.init({
        files: 'tests/**/*.*',
        proxy: {
            target: 'http://127.0.0.1:9090/'
        },
        startPath: '/earth/static/tests/runner.html'
            // browser: ['google chrome', 'firefox']
    });
});

// 静态服务器
gulp.task('staticserver', function() {
    browserSync.init({
        server: {
            baseDir: './'
        },
        route: {}
    });
});

// css
gulp.task('sass:develop', function() {
    var srcfiles;
    srcfiles = ['./css/src/local.config.scss', './css/src/app.scss'];

    return gulp.src(srcfiles)
        .pipe(concat('app.scss'))
        .pipe(sass())
        .pipe(rename('app-last.css'))
        .pipe(gulp.dest('./css/build/'));
});

gulp.task('sass:deploy', function() {
    var srcfiles;
    srcfiles = ['./css/src/online.config.scss', './css/src/app.scss'];

    return gulp.src(srcfiles)
        .pipe(concat('app.scss'))
        .pipe(sass())
        .pipe(rename('app-last.css'))
        .pipe(gulp.dest('./css/build/'));
});

gulp.task('sass:watch', ['sass:develop'], function() {
    gulp.watch('./css/src/**/*.scss', ['sass:develop']);
});

gulp.task('stylesheets', ['sass:deploy'], function() {
    gulp.src(['./css/*.css'])
        .pipe(minifycss())
        .pipe(gulp.dest(CSS_DIST_DIR));

    gulp.src(['./css/build/**/*.css'])
        .pipe(minifycss())
        .pipe(gulp.dest(CSS_DIST_DIR + 'build/'));

    gulp.src(['./css/plugins/**/*.css'])
        .pipe(minifycss())
        .pipe(gulp.dest(CSS_DIST_DIR + 'plugins/'));

    return gulp.src(['./css/utils/**/*.css'])
        .pipe(minifycss())
        .pipe(gulp.dest(CSS_DIST_DIR + 'utils/'));
});

// js
gulp.task('javascripts', function() {
    gulp.src('./js/mods/init.js')
        .pipe(uglify({
            mangle: {
                except: ['require', 'exports', 'module']
            }
        }))
        .pipe(gulp.dest(JS_DIST_DIR + 'mods/'));

    gulp.src('./js/mods/**/*.js')
        .pipe(uglify({
            mangle: {
                except: ['require', 'exports', 'module']
            }
        }))
        .pipe(gulp.dest(JS_DIST_DIR + 'mods/'));

    gulp.src(['./js/mods/**/*.css'])
        .pipe(minifycss())
        .pipe(gulp.dest(JS_DIST_DIR + 'mods/'));

    gulp.src(['./js/mods/**/*.!(js|css)'])
        .pipe(gulp.dest(JS_DIST_DIR + 'mods/'));
});

gulp.task('buildlib', function() {
    gulp.src('./js/*.js')
        .pipe(gulp.dest(JS_DIST_DIR));
    gulp.src('./js/vendor/*.js')
        .pipe(gulp.dest(JS_DIST_DIR + 'vendor/'));
    gulp.src('./js/virtual-account/*.js')
        .pipe(gulp.dest(JS_DIST_DIR + 'virtual-account/'));
    gulp.src('./js/utils/infovis/*.js')
        .pipe(gulp.dest(JS_DIST_DIR + 'utils/infovis/'));
    gulp.src('./js/utils/*.js')
        .pipe(uglify())
        .pipe(gulp.dest(JS_DIST_DIR + 'utils/'));
    gulp.src('./js/plugins/*.js')
        .pipe(uglify())
        .pipe(gulp.dest(JS_DIST_DIR + 'plugins/'));
});

gulp.task('resource', function() {
    gulp.src('./images/**/*')
        .pipe(gulp.dest(IMAGES_DIST_DIR));

    gulp.src('./fonts/**/*')
        .pipe(gulp.dest(FONT_DIST_DIR));
});

gulp.task('clean', function() {
    var stream = gulp
        .src([DIST_DIR], {
            read: false
        })
        .pipe(clean({
            force: true
        }));

    return stream;
});

gulp.task('develop', function() {
    gulp.run('sass:watch'); // 需要实时编译的
});

gulp.task('_deploy', ['resource', 'buildlib', 'stylesheets', 'javascripts'], function() {
    gulp.run('sass:develop'); // 部署之后修复本地的app-last文件
});

gulp.task('deploy', ['clean'], function() {
    gulp.run('_deploy');
});

gulp.task('default', ['develop']);

// ==================================================
// ==================================================
gulp.task('dist:replace', function() {
    var stat = JSON.parse(fs.readFileSync('./dist/vue/stat.json'));
    var extract = function(filepath) {
        return path.basename(filepath);
    };

    // 给页面中的入口文件打上version
    gulp.src('./index.tpl.jsp')
        .pipe(replace('manifest.js', extract(stat['manifest'].js)))
        .pipe(replace('polyfill.js', extract(stat['polyfill'].js)))
        .pipe(replace('lib.js', extract(stat['lib'].js)))
        .pipe(replace('element-ui.css', extract(stat['element-ui'].css)))
        .pipe(replace('element-ui.js', extract(stat['element-ui'].js)))
        .pipe(replace('main.css', extract(stat['main'].css)))
        .pipe(replace('main.js', extract(stat['main'].js)))
        .pipe(rename('index.jsp'))
        .pipe(gulp.dest('../WEB-INF/jsps/'));
});

gulp.task('dev:replace', function() {
    // 给页面中的入口文件打上version
    gulp.src('./index.tpl.jsp')
        .pipe(rename('index.jsp'))
        .pipe(gulp.dest('../WEB-INF/jsps/'));
});