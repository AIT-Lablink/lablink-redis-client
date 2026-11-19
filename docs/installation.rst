Installation
============

Maven project dependency
------------------------

The Lablink Redis client's compiled Java package is available on the |MCR|_.
Use it in your local Maven_ setup by including the following dependency into your *pom.xml*:

.. code-block:: xml

   <dependency>
     <groupId>at.ac.ait.lablink.clients</groupId>
     <artifactId>redisclient</artifactId>
     <version>0.0.2</version>
   </dependency>

.. note:: You may have to adapt this snippet to use the latest version, please check the |MCR|_.

Installation from source
------------------------

Installation from source requires a local **Java Development Kit** installation, for instance the `Oracle Java SE Development Kit 13 <https://www.oracle.com/technetwork/java/javase/downloads/index.html>`_ or the `OpenJDK <https://openjdk.java.net/>`_.

Check out the project and compile it with Maven_:

.. code-block:: winbatch

   git clone https://github.com/ait-lablink/lablink-redis-client
   cd lablink-redis-client
   mvnw clean package

This will create JAR file *redisclient-<VERSION>-jar-with-dependencies.jar* in subdirectory *target/assembly*.

Publishing to the Maven Central Repository
------------------------------------------

A release is published with ``mvnw deploy``. The build signs the artifacts and uploads them to the `Central Portal <https://central.sonatype.com/>`_. The upload uses a Central Portal user token from your Maven ``settings.xml`` and a local GPG key. Publishing is manual: the deployment stays pending until you publish it in the portal, and the build waits until that happens.

Docker image
------------

A ready-to-run image is published on `Docker Hub <https://hub.docker.com/r/ait1/lablink-redis-client>`_ as ``ait1/lablink-redis-client:latest``:

.. code-block:: winbatch

   docker pull ait1/lablink-redis-client:latest

The image starts the class named by ``REDIS_CLASS``.
The default class is ``at.ac.ait.lablink.clients.redisclient.RedisOpalClient``.
The client opens a shell, so the container needs an interactive terminal.
See :doc:`here <running>` for how to start the container.
Building the image locally with :github_blob:`docker/build.cmd <docker/build.cmd>` tags the result with the same name.

.. |MCR| replace:: Maven Central Repository
.. _MCR: https://search.maven.org/artifact/at.ac.ait.lablink.clients/redisclient
.. _Maven: https://maven.apache.org
