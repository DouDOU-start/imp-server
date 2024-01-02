#!/bin/bash

NAME="algorithm_scheduling"
VERSION="v1.0.0"

SCRIPT_DIR=$(dirname "$0")

ALGORITHM_SCRIPT_DIR=$SCRIPT_DIR/../resources/${NAME}

pushd $ALGORITHM_SCRIPT_DIR > /dev/null
    ./build_*.sh
popd

unzip package/${NAME}_package.zip -d rootfs/

docker build -t hanglok/${NAME}:${VERSION} --ulimit nofile=1024000:1024000 .