[![Maven Central](https://img.shields.io/maven-central/v/at.ac.ait.lablink.clients/redisclient.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22at.ac.ait.lablink.clients%22%20AND%20a:%22redisclient%22)
[![Documentation Status](https://readthedocs.org/projects/ait-lablink-redis-client/badge/?version=latest)](https://ait-lablink.readthedocs.io/projects/ait-lablink-redis-client/en/latest/)

# The Lablink Redis lient

[Lablink](https://ait-lablink.readthedocs.io) client for interfacing to a [Redis](https://redis.io) database.
Its main purpose is to interface to AIT's roadb (Redis-Opal Asynchronous Data Buffer) in order to access measurement data from a real-time simulation running on an [OPAL-RT](https://www.opal-rt.com) system.
Similarly, the client allows sending commands and set points to a running simulation.

*Note*: in order to work with OPAL-RT, the roadb service should be running on the OPAL target computer.

The client can also be used in a basic mode, to map a Lablink datapoint to a redis key.

See the [examples](./examples) section for more infos.

For more information, please refer to the [online documentation](https://ait-lablink.readthedocs.io/projects/ait-lablink-redis-client).