Overview
========

The configuration has to be JSON-formatted.

Basic Lablink Client Configuration
==================================

.. topic:: Required parameters:

  :*clientName*: client name
  :*groupName*: group name
  :*scenarioName*: scenario name
  :*labLinkPropertiesUrl*: URI to Lablink configuration
  :*syncHostPropertiesUrl*: URI to sync host configuration

Redis Database Connection Configuration
=======================================

.. topic:: Required parameters:

  :*redisIP*: IP address of Redis database 
  :*redisPort*: connection port of Redis database

.. topic:: Optional parameters:

  :*msTimeInterval*: time interval in milliseconds for retrieving data values (measurements) from the Redis database (default: ``5000``)

Commands / Measurements Configuration
=====================================

For interacting with the Redis database, the client defines **measurements** (i.e., data values to be read from the database) and **commands** (i.e., data values to be written to the database).
Both can be either defined in a local configuration file (with Redis keys in separate lines) or as JSON arrays.

.. topic:: Configuration via local file:

  :*cmdsFile*: path to local configuration file defining commands via Redis keys in separate lines
  :*measFile*: path to local configuration file defining measurements via Redis keys in separate lines

.. topic:: Configuration via JSON array:

  :*commands*: JSON array defining commands via Redis keys in separate entries
  :*measurements*: JSON array defining measurements via Redis keys in separate entries
  
.. note:: Redis keys may not contain slashes (``/``)!


Example Configuration
=====================

The following is an example configuration using local configuration files to define commands / measurements:

.. code-block:: json

   {
     "clientName" : "RedisOpalClient",
     "groupName" : "RedisOpalDemo",
     "scenarioName" : "RedisOpalScenario",
     "syncHostPropertiesUrl" : "http://localhost:10101/get?id=ait.example.all.sync-host.properties",
     "labLinkPropertiesUrl" : "http://localhost:10101/get?id=ait.example.all.llproperties",  
     "redisIP" : "192.168.100.200",
     "redisPort" : "6379",
     "cmdsFile" : "commands.sgnl",
     "measFile" : "measurements.sgnl"
   }

The following is an example configuration using JSON arrays to define commands / measurements:

.. code-block:: json

  {
    "clientName" : "RedisOpalClient",
    "groupName" : "RedisOpalDemo",
    "scenarioName" : "RedisOpalScenario",
    "syncHostPropertiesUrl" : "http://localhost:10101/get?id=ait.example.all.sync-host.properties",
    "labLinkPropertiesUrl" : "http://localhost:10101/get?id=ait.example.all.llproperties",  
    "redisIP" : "192.168.100.200",
    "redisPort" : "6379",
    "commands": [
      "cmd_Test.line_1_Line.Data.Points.P1",
      "cmd_Test.line_1_Line.Data.Points.Q1"
    ],
    "measurements": [
      "meas_Test.ext_el_grid_Generator.Data.Points.Vmag",
      "meas_Test.ext_el_grid_Generator.Data.Points.Vang"
    ]
  }
