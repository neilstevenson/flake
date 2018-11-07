# flake

Comparing `IAtomicLong` with `FlakeIdGenerator` (SnowFlake style ID generator).

## `IAtomicLong`

This is a single named counter.

It exists in one place in the cluster but you can access it from anywhere.

The increment operation generates sequential identifiers (1, 2, 3, 4, etc).

These may not be unique as the next value of the counter can be reset.

A single named counter can generated ids at a certain rate.

Increasing the size of the cluster does not increase the rate. It is
one counter on one server, regardless of whether the cluster contains
5 other servers 10 other servers or so on.

## `FlakeIdGenerator`

This is a cloned named counter.

It exists on every server in the cluster. Each server gets a block of
numbers to issue, and only liaises with the other servers when a new
block of numbers is needed.

Identifiers are not sequential but are unique.

Ten servers can issue unique identifiers at twice the rate of five
servers, it is scalable.


## Clustering

The example assumes a cluster of one or more Hazelcast servers
running on the *same* machine.

Compile and run with:

```
java -Dserver.port=8080 -jar target/flake-0.0.1-SNAPSHOT.jar
java -Dserver.port=8081 -jar target/flake-0.0.1-SNAPSHOT.jar
java -Dserver.port=8082 -jar target/flake-0.0.1-SNAPSHOT.jar
```

You can run as many instances as your machine can cope with,
they should find each other and join together.

As each in this example provides a REST interface, you need
to use the `server.port` argument to allocate them different
web ports.

## Usage

For simplicity, this makes the id generators available to REST calls.

### `IAtomicLong`

Try calls to the `IAtomicLong` based generator.

Eg.

```
curl http://localhost:8080/iatomiclong
curl http://localhost:8081/iatomiclong
curl http://localhost:8081/iatomiclong
curl http://localhost:8081/iatomiclong
curl http://localhost:8080/iatomiclong
```

You can direct the calls to any server in the cluaster and still
get the next numeric number in sequence.

### `FlakeIdGenerator`

Try calls to the `FlakeIdGenerator` alternative.

Eg.

```
curl http://localhost:8080/flakeidgenerator
curl http://localhost:8081/flakeidgenerator
curl http://localhost:8081/flakeidgenerator
curl http://localhost:8081/flakeidgenerator
curl http://localhost:8080/flakeidgenerator
```

This will generator unique ids, but their uniqueness will be
less obvious than the `IAtomicLong`. They can be generated at
a faster rate, but this is unlikely to be noticeable on
a desktop PC or laptop.

