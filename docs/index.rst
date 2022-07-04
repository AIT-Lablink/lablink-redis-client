************************
AIT Lablink Redis Client
************************

.. meta::
   :description lang=en: AIT Lablink Redis Client

`Lablink <https://ait-lablink.readthedocs.io>`__ client for interfacing to a `Redis <https://redis.io/>`__ database.
Its main purpose is to interface to AIT's roadb (Redis-Opal Asynchronous Data Buffer) in order to access measurement data from a real-time simulation running on an `OPAL-RT <https://www.opal-rt.com/>`__ system.
Similarly, the client allows sending commands and set points to a running simulation.
The client can also be used in a basic mode, to map a Lablink datapoint to a Redis key.

See the :doc:`examples section <examples>` for more infos.

.. note:: in order to work with OPAL-RT, the roadb service should be running on the OPAL target computer.


Installation
============

Find information about the installation of the Lablink Redis client :doc:`here <installation>`.

.. toctree::
   :maxdepth: 2
   :hidden:
   :caption: Installation

   /installation
   
Running the clients
===================

Find basic instructions for running the client :doc:`here <running>`.

.. toctree::
   :maxdepth: 2
   :hidden:
   :caption: Running the clients

   /running

Configuration
=============

Find the reference for writing a configuration for a Lablink Redis client :doc:`here <configuration>`.

.. toctree::
   :maxdepth: 2
   :hidden:
   :caption: Configuration

   /configuration
