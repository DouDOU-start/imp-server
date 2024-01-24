#!/bin/bash

# docker run -itd --name pacs --net=host hanglok/pacs:v1.0.0

docker run -itd --name algorithm-scheduling --restart=always --net=host hanglok/algorithm_scheduling:v1.0.0