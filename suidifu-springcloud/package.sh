#! /bin/bash
# 打包脚本
# 将指定目标的源码打成固定的docker镜像

project=(refund-asset-consumer modify-overdue-fee-consumer suidifu-discovery-eureka suidifu-config-server recon-repayment-order-active-consumer recon-repayment-order-deduct-consumer clear-deduct-plan-consumer tmp-deposit-reconciliation-consumer source-document-reconciliation-consumer all)

declare -A PROJECT_FOLDER_NAME

function packageProject(){

	dist_category=$3
	projectName=$1
	version=$2
	#BASE_PATH=$(cd `dirname $0`;pwd)

	cd $BASE_PATH;

	echo 'BASE_PATH:'$BASE_PATH

	mvn -Dsuidifu.version=$version  -Dmaven.test.skip=true clean install -pl ./$projectName -am 

	if [[ $? -ne 0 ]]; then
		echo -e '\033[31m打包项目['$projectName']失败，请找出原因重试！\033[0m'
		exit 110
	else
		echo -e '\033[33m成功打包项目['$projectName']\033[0m'
	fi

	PROJECT_PATH=${PROJECT_FOLDER_NAME["$projectName"]}

	if [[ -z $PROJECT_PATH ]]; then
		PROJECT_PATH=${projectName}
	fi

	cd $BASE_PATH'/'${PROJECT_PATH}/target/
	echo '开始进行镜像编译'

	mkdir images;

	\cp $projectName*.jar images/ROOT.jar;

	\cp $BASE_PATH/Dockerfile images/;

	cd images;

	docker build -t 120.26.102.180:5000/suidifu/$projectName:$version .

	echo '结束镜像编译'

	echo '开始上传镜像'
	
        docker push 120.26.102.180:5000/suidifu/$projectName:$version

        echo -e '\033[33m结束镜像上传,镜像的版本号是:'$version'\033[0m'
}

function printOneLineInfo(){
	info=''
	index=0
	for item in $@ ; do
		if [[ $index -eq 0 ]] ; then
			info=${item}'['
		else
			info+=${item}'|'
		fi
		let index++
	done
	info+=']'

	echo $info
}

PROJECT_NUM=${#project[*]}
resource_category=(yunxin shrbank avictc weifang bohai)
declare -A resource_category_alias
resource_category_alias[yunxin]="信托(类似云信)"
resource_category_alias[shrbank]="信贷(类似华瑞)"
resource_category_alias[avictc]="信托(类似中航)"
resource_category_alias[weifang]="信贷(类似潍坊)"
resource_category_alias[bohai]="信托(类似渤海)"

if [[ $# -eq 0 ]]; then

	echo '目前支持打包的项目个数有['$PROJECT_NUM']个，分别如下：'

	for(( i=0;i<${PROJECT_NUM};i++ ));
	{
		echo $i'.'${project[$i]}
	}
	read -p  '请输入项目的编号数字:' num

	while [[ "$num" -lt 0 ]] || [[ "$num" -ge ${PROJECT_NUM} ]]; do
		read -p '输入项目编号有误，不存在该编号，请重新输入:' num
	done

	projectName=${project[$num]}

	read -p '请输入项目['$projectName']版本号[注：可选项，如果不填入将使用当前时间戳作为版本号]:' version

	if [[ -z $version ]]; then
	    current_timestamp=`date +%Y%m%d%H%M%S`
		version=$current_timestamp
	fi

else

	if [[ $# != 3 ]]; then
		echo '传入的参数个数不正确，正确如下：'

		dist_category_line_info=`printOneLineInfo ${resource_category[*]}`
		dist_category_alias_line_info=`printOneLineInfo ${resource_category_alias[*]}`

		for(( i=0;i<${PROJECT_NUM};i++ ));
		{
			projectName=${project[$i]}
			echo $i'./package.sh '${projectName}' 1.0.0-SNAPSHOT '$dist_category_line_info' ===> 将打包'${dist_category_alias_line_info}'资源的'$projectName'项目包,版本为1.0.0-SNAPSHOT'
		}

		exit 110
	else

		projectName=$1
		version=$2
		dist_category='dist_'$3
	fi
fi

echo '开始打包项目['$projectName']:'

BASE_PATH=$(cd `dirname $0`;pwd)

if [[ $projectName == "all" ]]; then

	for projectNameItem in ${project[*]}; do

		echo 'projectNameItem :'$projectNameItem

		if [[ $projectNameItem != "all" ]] ; then

				packageProject $projectNameItem $version $dist_category ${BASE_PATH}
		fi
	done

else
	packageProject $projectName $version $dist_category ${BASE_PATH}
fi
