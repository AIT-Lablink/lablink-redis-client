Invoking the clients from the command line
==========================================

When running the clients, the use of the ``-c`` command line flag followed by a **local configuration file** or the **URI to the configuration** is mandatory (see :doc:`here <configuration>` for more on configuring the Lablink Redis client).

For example, on Windows this could look something like this:

.. code-block:: winbatch

   SET LLCONFIG=http://localhost:10101/get?id=
   SET CONFIG_FILE_URI=%LLCONFIG%ait.test.redis.config
   
   SET REDIS_OPAL=at.ac.ait.lablink.clients.redisclient.RedisOpalClient
   SET REDIS_JAR_FILE=\path\to\lablink-redis-client\target\assembly\redisclient-<VERSION>-jar-with-dependencies.jar
   
   java.exe -cp "%REDIS_JAR_FILE%" %REDIS_OPAL% -c %CONFIG_FILE_URI%

Running with Docker
===================

The image ``ait1/lablink-redis-client:latest`` (see :doc:`installation`) starts the class named by ``REDIS_CLASS`` with ``-c`` set to ``${LLCONFIG}${REDIS_CONFIG}``.
The defaults are ``LLCONFIG=http://lablink-config:10101/get?id=``, ``REDIS_CONFIG=lablink-redisclient.config``, and ``REDIS_CLASS=at.ac.ait.lablink.clients.redisclient.RedisOpalClient``, so the image expects a configuration server reachable as the host ``lablink-config``.
``MEMORY_FLAG`` is the JVM heap setting and defaults to ``-Xmx512m``.
``LOGGER_ID`` is the logging configuration id and defaults to ``ait.all.log4j2``.

``--interactive`` and ``--tty`` attach the terminal.
The client reads shell commands from that terminal.

On Windows, a configuration server on the host is reachable as ``host.docker.internal``:

.. code-block:: winbatch

   docker run --rm --interactive --tty -e LLCONFIG=http://host.docker.internal:10101/get?id= -e REDIS_CONFIG=ait.example.redis.opal.config ait1/lablink-redis-client:latest

On Linux, add that hostname explicitly:

.. code-block:: bash

   docker run --rm --interactive --tty --add-host=host.docker.internal:host-gateway -e LLCONFIG=http://host.docker.internal:10101/get?id= -e REDIS_CONFIG=ait.example.redis.opal.config ait1/lablink-redis-client:latest

``RedisOpalClient`` reads ``cmdsFile`` and ``measFile`` from the working directory, which is ``/app`` in the image.
Mount those files there when the configuration names them.
Arguments after the image name are passed through to the client.
