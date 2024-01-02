#!/bin/bash

echo "==================================================================="
echo "   Building pacs packages...   "
echo "==================================================================="

SCRIPT_DIR=$(dirname "$0")
ROOT_DIR=$SCRIPT_DIR/../..
ZIP_DIR=$SCRIPT_DIR/rootfs

set -e
set -x

# 清理垃圾文件
rm -rf $ROOT_DIR/docker/package/algorithm_package.zip

pushd $ROOT_DIR > /dev/null
    # -DskipTests 不执行测试用例，但编译测试用例类生成相应的class文件至target/test-classes下
    # -Dmaven.test.skip=true 不执行测试用例，也不编译测试用例类。
    mvn clean package -DskipTests -Dmaven.test.skip=true
    mvn dependency:copy-dependencies -DincludeScope=runtime
    
    # jar依赖
    # -r 递归复制，-f 强制复制
    cp -r -f algorithm-scheduling-server/target/dependency resources/algorithm_scheduling/rootfs/etc/imp/algorithm-scheduling/
    cp -f algorithm-scheduling-server/target/algorithm-scheduling-server-*.jar resources/algorithm_scheduling/rootfs/etc/imp/algorithm-scheduling/algorithm-scheduling-server.jar
popd

pushd $ZIP_DIR > /dev/null
    # 创建package包
    if [ ! -d "../../../docker/package" ]; then
        mkdir -p ../../../docker/package
    fi
    # 压缩
    zip -r ../../../docker/package/./algorithm_scheduling_package.zip ./
popd

# 清理垃圾文件
rm -rf $ZIP_DIR/etc/imp/algorithm-scheduling/algorithm-scheduling-server.jar
rm -rf $ZIP_DIR/etc/imp/algorithm-scheduling/dependency
