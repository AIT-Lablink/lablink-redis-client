#!/bin/bash

#Load the setup for the examples.
. ../setup.sh

#Path to class implementing the main routine.
export REDIS_RW=at.ac.ait.lablink.clients.lablinkredisclient.RedisClientBasic



#Logger configuration.
export LOGGER_CONFIG=-Dlog4j.configurationFile=$(echo $LLCONFIG)ait.example.all.log4j2

#Run the example.
java $LOGGER_CONFIG -cp $REDIS_CLIENT_JAR_FILE $REDIS_RW