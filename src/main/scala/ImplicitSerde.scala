package com.nequissimus.circe

import java.util.{ Map => JMap }

import com.github.ghik.silencer.silent

import io.circe.{ Decoder, Encoder }
import io.circe.parser._

import org.apache.kafka.common.serialization.{ Deserializer, Serde, Serdes, Serializer }

package object kafka {
  private[kafka] val stringSerde = Serdes.String()

  implicit def serializer[T](implicit encoder: Encoder[T]): Serializer[T] = new Serializer[T] {
    def close(): Unit                                                     = {}
    @silent def configure(configs: JMap[String, _], isKey: Boolean): Unit = {}
    def serialize(topic: String, data: T): Array[Byte]                    = stringSerde.serializer.serialize(topic, encoder(data).noSpaces)
  }

  implicit def deserializer[T](implicit decoder: Decoder[T]): Deserializer[T] = new Deserializer[T] {
    def close(): Unit                                                    = {}
    @silent def configure(config: JMap[String, _], isKey: Boolean): Unit = {}
    def deserialize(topic: String, data: Array[Byte]): T                 = decode[T](new String(data)).getOrElse(null.asInstanceOf[T])
  }

  implicit def serde[T](implicit serializer: Serializer[T], deserializer: Deserializer[T]): Serde[T] = Serdes.serdeFrom(serializer, deserializer)
}
