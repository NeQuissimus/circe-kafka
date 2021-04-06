package nequi.circe

import io.circe.{ Decoder, Encoder }
import io.circe.parser._

import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.{ Deserializer, Serde, Serdes, Serializer }

package object kafka {
  private[kafka] val stringSerde = Serdes.String()

  implicit def encoder2serializer[T <: AnyRef](implicit encoder: Encoder[T]): Serializer[T] = new Serializer[T] {
    def serialize(topic: String, data: T): Array[Byte] =
      if (data eq null) null.asInstanceOf[Array[Byte]]
      else
        stringSerde.serializer.serialize(topic, encoder(data).noSpaces)
  }

  implicit def decoder2deserializer[T <: AnyRef](implicit decoder: Decoder[T]): Deserializer[T] = new Deserializer[T] {
    def deserialize(topic: String, data: Array[Byte]): T =
      if (data eq null) null.asInstanceOf[T]
      else {
        decode[T](stringSerde.deserializer.deserialize(topic, data))
          .fold(error => throw new SerializationException(error), identity)
      }
  }

  implicit def serializerdeserializer2serde[T <: AnyRef](implicit
    serializer: Serializer[T],
    deserializer: Deserializer[T]
  ): Serde[T] =
    Serdes.serdeFrom(serializer, deserializer)
}
