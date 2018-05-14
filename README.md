# Circe-Kafka

[![Build Status](https://travis-ci.org/NeQuissimus/circe-kafka.svg?branch=master)](https://travis-ci.org/NeQuissimus/circe-kafka)

Implicitly turn your `Encoder` and `Decoder` instances into `Serializer`, `Deserializer` and `Serde`.

## Artifact

`circe-kafka` is cross-compiled against Scala 2.11 and 2.12.

```scala
libraryDependencies ++= "com.nequissimus" %% "circe-kafka" % "1.0.2"
```

## Usage

```scala
import io.circe.{ Decoder, Encoder }
import org.apache.kafka.common.serialization.{ Deserializer, Serde, Serializer }

final case class Foo(i: Int)

//

import com.nequissimus.circe.kafka._

implicit val encoder: Encoder[Foo] = ... // for example by importing io.circe.generic.auto._
implicit val decoder: Decoder[Foo] = ...

val serializer: Serializer[Foo] = implicitly
val deserializer: Deserializer[Foo] = implicitly
val serde: Serde[Foo] = implicitly
```
