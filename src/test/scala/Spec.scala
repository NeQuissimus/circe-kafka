package nequi.circe

import io.circe.{ Decoder, Encoder }
import io.circe.generic.auto._

import org.apache.kafka.common.serialization.{ Deserializer, Serde, Serializer }

import utest._

object KafkaSpec extends TestSuite {
  final case class Foo(i: Int)

  val tests = Tests {
    "Create a Serializer from an Encoder" - {
      val enc: Encoder[Foo] = implicitly[Encoder[Foo]]

      val ser: Serializer[Foo] = kafka.serializer(enc)

      ser
    }
    "Create a Deserializer from a Decoder" - {
      val dec: Decoder[Foo] = implicitly[Decoder[Foo]]
      val des: Deserializer[Foo] = kafka.deserializer(dec)

      des
    }
    "Create a Serde from Encoder and Decoder" - {
      val enc: Encoder[Foo] = implicitly
      val dec: Decoder[Foo] = implicitly
      val ser: Serializer[Foo] = kafka.serializer(enc)
      val des: Deserializer[Foo] = kafka.deserializer(dec)

      val serde: Serde[Foo] = kafka.serde(ser, des)

      serde
    }
    "Summon an implicit serde from Encoder and Decoder" - {
      import kafka._

      // io.circe.generic.auto._ makes available implicit Encoder and Decoder instances

      val ser: Serde[Foo] = implicitly

      ser
    }
  }
}
