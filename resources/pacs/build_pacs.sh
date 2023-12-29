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
rm -rf $ROOT_DIR/docker/package/pacs_package.zip

pushd $ROOT_DIR > /dev/null
    # -DskipTests 不执行测试用例，但编译测试用例类生成相应的class文件至target/test-classes下
    # -Dmaven.test.skip=true 不执行测试用例，也不编译测试用例类。
    mvn clean package -DskipTests -Dmaven.test.skip=true
    mvn dependency:copy-dependencies -DincludeScope=runtime
    
    # jar依赖
    # -r 递归复制，-f 强制复制
    cp -r -f pacs-service/target/dependency resources/pacs/rootfs/etc/imp/pacs/
    cp -f pacs-service/target/pacs-service-*.jar resources/pacs/rootfs/etc/imp/pacs/pacs-service.jar
    
    # SQL
    rsync -av --include='/*.sql' --exclude '*' doc/sql/ resources/pacs/rootfs/etc/imp/pacs/sql/
popd

pushd $ZIP_DIR > /dev/null
    # 创建package包
    if [ ! -d "../../../docker/package" ]; then
        mkdir -p ../../../docker/package
    fi
    # 压缩
    zip -r ../../../docker/package/pacs_package.zip ./
popd

# 清理垃圾文件
rm -rf $ZIP_DIR/etc/imp/pacs/pacs-service.jar
rm -rf $ZIP_DIR/etc/imp/pacs/dependency
rm -rf $ZIP_DIR/etc/imp/pacs/sql

# 服务
# chmod u+x /etc/imp/pacs/imp-pacs.service
# ln -s /etc/imp/pacs/imp-pacs.service /etc/systemd/system/imp-pacs.service

# systemctl daemon-reload
# systemctl restart imp-pacs
# systemctl enable imp-pacs
# systemctl status imp-pacs
# systemctl stop imp-pacs
