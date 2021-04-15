#!/bin/bash

#Load the setup for the examples.
. ../setup.sh

#Specify configuration file.
export CONFIG_FILE_NAME=$PWD/examples-config.db

#Start configuration server.
java -jar $CONFIG_JAR_FILE -a run -d $CONFIG_FILE_NAME