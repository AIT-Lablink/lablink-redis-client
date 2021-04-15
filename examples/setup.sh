#!/bin/bash

#=============================================================
#Edit the following variables to comply with your local setup.
#=============================================================

#Connection string for configuration server.
export LLCONFIG=http://localhost:10101/get?id=

#Version of example package.
export VERSION=0.0.1

#Root directory of example package (only change this if you really know what you are doing).
export REDIS_CLIENT_ROOT_DIR=$(dirname $(cd $(dirname ${BASH_SOURCE[0]}) && pwd))

#Path to Java JAR file of the REDIS client package.
export REDIS_CLIENT_JAR_FILE=$REDIS_CLIENT_ROOT_DIR/target/assembly/lablinkredisclient-$VERSION-jar-with-dependencies.jar 

#Path to Java JAR file of config server.
export CONFIG_JAR_FILE=$REDIS_CLIENT_ROOT_DIR/target/dependency/config-0.1.0-jar-with-dependencies.jar