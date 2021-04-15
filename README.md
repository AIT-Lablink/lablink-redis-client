# lablink-redis-client

Lablink client for interfacing to a Redis database.
Its main purpose is to interface to AIT's roadb (Redis-Opal Asynchronous Data Buffer) in order to access measurement data from a real-time simulation running on an OPAL-RT system. Similarly, the client allows sending commands and set points to a running simulation.

Note: in order to work with OPAL-RT, the roadb service should be running on the OPAL target computer.

The client can also be used in a basic mode, to map a Lablink datapoint to a redis key.

See the examples section for more infos.
