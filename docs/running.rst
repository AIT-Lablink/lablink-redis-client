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
