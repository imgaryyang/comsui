#! /bin/bash

####
####  前端js、css压缩工具脚本 V2
###   从项目中移除node_modules，通过软链接使用npm依赖，减少项目的大小
###
###

function printTip(){

	if [[ $1 -ne 0 ]]; then
		echo -e '\033[31m '$2' \033[0m'
		exit
	else
		echo $3
	fi
}

echo '开始进行js\css压缩'
echo '单独使用该脚本时，示例如下：'
echo './compress.sh dist_yunxin  ==> 打包云信的包'
echo './compress.sh dist_shrbank ==> 打包华瑞的包'
echo './compress.sh dist_avictc ==> 打包中航的包'

echo '开始准备设置npm环境源'

npm config set registry="https://registry.npm.taobao.org/";
npm config set sass_binary_sit="https://npm.taobao.org/mirrors/node-sass/";

printTip $? '设置npm环境源失败！' '设置npm环境源成功！'

BASE_PATH=$(cd `dirname $0`;cd ..;pwd)

NPM_DEPENDIES_DIR=${BASE_PATH}'/npm_dependies'

NODE_MODULES_DIR=${NPM_DEPENDIES_DIR}'/node_modules';

WEB_STATIC_DIR=${BASE_PATH}'/earth/webapp/static';

echo '开始检查npm依赖文件'

if [[ ! -d $NPM_DEPENDIES_DIR ]]; then
	mkdir $NPM_DEPENDIES_DIR
fi
echo '使用代码中最新的package.json文件'

cp -f ${WEB_STATIC_DIR}'/package.json' ${NPM_DEPENDIES_DIR}

printTip $? '拷贝代码中最新的package.json文件失败！' '拷贝代码中最新的package.json文件成功！'

echo '开始安装最新依赖'

cd ${NPM_DEPENDIES_DIR}

npm install

printTip $? '安装最新依赖失败！' '安装最新依赖成功！'

echo '开始检查npm软链接'

cd ${BASE_PATH}/earth

cd ${WEB_STATIC_DIR}

rm -rf node_modules

ln -s  ${BASE_PATH}/npm_dependies/node_modules/ node_modules

printTip $? 'npm软链接建立失败！' 'npm软链接建立成功！'

dist_target=$1

echo '开始对earth项目进行js和css压缩,将使用['$dist_target']资源类型'

rm -rf dist && npm run $dist_target

printTip $? '对earth项目进行js和css压缩失败！' '对earth项目进行js和css压缩成功！'
