#!/bin/bash

VERSION="v1.0.0"
IMAGE_NAME="pacs"

SCRIPT_DIR=$(dirname "$0")
PACS_SCRIPT_DIR=$SCRIPT_DIR/../resources

pushd $PACS_SCRIPT_DIR > /dev/null
    ./build_pacs.sh
popd

unzip package/pacs_package.zip -d rootfs/

docker build -t hanglok/${IMAGE_NAME}:${VERSION} --ulimit nofile=1024000:1024000 .