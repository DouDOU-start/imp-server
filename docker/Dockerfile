FROM debian:bullseye-slim

LABEL author="Allen" email="allen.huang@hanglok-tech.cn" descripton="Image Manager Platform"

RUN sed -i "s@http://deb.debian.org@http://mirrors.aliyun.com@g" /etc/apt/sources.list

RUN apt-get update && \
	apt-get install -y net-tools vim openssh-server \
    nginx openjdk-17-jdk && \
    apt-get clean && \
    rm -Rf /var/lib/apt/lists/*

# 设置时区
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

ADD rootfs /

RUN chmod +x /etc/imp/pacs/imp-pacs && \
ln -s /etc/imp/pacs/imp-pacs /etc/init.d/imp-pacs