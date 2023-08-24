#!/bin/bash

echo "==================================================================="
echo "   Building pacs packages...   "
echo "==================================================================="

set -e
set -x

# -DskipTests 不执行测试用例，但编译测试用例类生成相应的class文件至target/test-classes下
# -Dmaven.test.skip=true 不执行测试用例，也不编译测试用例类。
mvn clean package -DskipTests -Dmaven.test.skip=true
mvn dependency:copy-dependencies -DincludeScope=runtime

rm -rf /etc/imp/pacs
rm -rf /etc/systemd/system/imp-pacs.service
mkdir -p /etc/imp/pacs/sql

# jar依赖
cp -r pacs-service/target/dependency /etc/imp/pacs/
cp pacs-service/target/pacs-service-*.jar /etc/imp/pacs/pacs-service.jar

# 配置文件
cp resources/pacs.yml /etc/imp/pacs/

# SQL
cp -r doc/sql/*.sql /etc/imp/pacs/sql/

# 服务
cp resources/imp-pacs.service /etc/imp/pacs/
chmod u+x /etc/imp/pacs/imp-pacs.service
ln -s /etc/imp/pacs/imp-pacs.service /etc/systemd/system/imp-pacs.service

systemctl daemon-reload
systemctl restart imp-pacs
# systemctl enable imp-pacs
# systemctl status imp-pacs
# systemctl stop imp-pacs
