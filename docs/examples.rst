Prerequisites
=============

The examples run with Docker Compose.
These images are published on Docker Hub and are pulled automatically:

* ``ait1/lablink-redis-client:latest``
* ``ait1/lablink-config-server:latest``
* ``eclipse-mosquitto:2``
* ``redis:7``
* ``redis/redisinsight:3.8``

The example setup is configured via the Compose file at :github_blob:`examples/docker-compose.yml <examples/docker-compose.yml>`.
The example setup shares the configuration database file :github_blob:`example-lablink-config.db <examples/config/example-lablink-config.db>` and the MQTT broker configuration file :github_blob:`mosquitto.conf <examples/config/mosquitto.conf>`.
Inside the containers the broker host is ``mqtt``, the configuration server host is ``lablink-config``, and the Redis host name is ``redis``.
``RedisClientBasic`` connects to ``10.111.202.11``.
The Compose file assigns that address to the Redis service.

Using a local clone of the repository, change to folder `examples` and start the broker, the configuration server, Redis, and Redis Insight:

.. code-block:: bash

   docker compose up -d

Redis Insight can be used to view the Redis strings the clients use, it is accessible at http://localhost:5540:

* Open the preconfigured database ``lablink`` in Redis Insight.
* A value written by a client shell appears there.
* A value you edit there is read by the client on its next poll, which runs every 5 seconds.

Start a client shell from the same directory.
The two shells can run one at a time or together.

Example 1: Reading and writing Redis keys
=========================================

``RedisClientBasic`` maps two Redis keys onto Lablink data services:

* ``test/1`` is read every 5 seconds.
* ``test/2`` is written when the shell sets a new value.

.. code-block:: bash

   docker compose run --rm --no-tty=false basic

The shell lists the services with ``ls`` and sets a double value with ``svd``.
For instance, ``svd test/2 1.5`` writes the string ``1.5`` to Redis key ``test/2``, which Redis Insight then shows.
Edit the string at ``test/1`` in Redis Insight.
The client reads that string on its next poll and logs the new value.


Example 2: OPAL-RT measurements and commands
============================================

``RedisOpalClient`` loads :github_blob:`examples/opal/opalconfig.json <examples/opal/opalconfig.json>`, which the configuration server stores as `ait.example.redis.opal.config <http://localhost:10101/view?id=ait.example.redis.opal.config>`_.
Measurements come from :github_blob:`measurements.sgnl <examples/opal/measurements.sgnl>` and commands from :github_blob:`commands.sgnl <examples/opal/commands.sgnl>`.
Compose mounts both files into the client working directory.

The Redis host in this configuration is the Compose service ``redis``, so this example does not require an OPAL-RT target.
To use a roadb instance, point ``redisIP`` and ``redisPort`` at that instance.

Measurement keys are the strings the client reads.
Change one in Redis Insight and the client logs the new value on its next poll.
Command keys are written when the shell sets the corresponding service, and Redis Insight shows the new string.

.. code-block:: bash

   docker compose run --rm --no-tty=false opal
