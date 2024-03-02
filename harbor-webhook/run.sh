#!/bin/bash

docker run --name harbor-webhook \
--restart=always \
-p 8080:8080 \
-d 10.8.6.34:5000/hanglok/harbor-webhook:0.0.1