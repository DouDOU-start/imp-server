#!/bin/bash

rm -rf rootfs/
rm -rf package/

docker rm -f pacs
docker rm -f algorithm-scheduling

# docker rm $(docker ps -a -q)

docker rmi $(docker images -f "dangling=true" -q)

