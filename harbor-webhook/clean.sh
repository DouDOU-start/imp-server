#!/bin/bash

docker rm -f harbor-webhook

docker rmi $(docker images -f "dangling=true" -q)