package za.co.absa.KafkaCase

import io.circe.generic.auto._
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import za.co.absa.KafkaCase.Models.EdlaChangeTopic
import za.co.absa.KafkaCase.Reader.ReaderImpl
import za.co.absa.KafkaCase.Writer.WriterImpl

import java.util.{Properties, UUID}
import scala.util.Using // !!!WARNING!!!: this seems to swallows exceptions during creation of the resource

object KafkaCase {
  private def writer_use_case(): Unit = {
    // 0 -> HAVE SOMETHING TO WRITE
    val messageToWrite = EdlaChangeTopic(
      id = "DebugId",
      app_id_snow = "N/A",
      source_app = "ThisCode",
      environment = "DEV",
      timestamp_event = "RIGHT_NOW",
      data_definition = "TestingThis",
      operation = EdlaChangeTopic.Operation.CREATE(),
      location = "ether",
      format = "FooBar",
      schema_link = "http://not.here"
    )

    // 1 -> DEFINE PROPS - kafka to treat all as string
    val writerProps = new Properties()
    writerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    writerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    writerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    // 2 -> MAKE WRITER
    val writer = new WriterImpl[EdlaChangeTopic](writerProps, "MyTopic") // must be assigned outside using, otherwise exception from constructor is swallowed by using block
    Using (writer) { writer =>
      // 3 -> WRITE
      writer.Write("sampleMessageKey", messageToWrite)
    }
  }

  private def reader_use_case(): Unit = {
    // 1 -> DEFINE PROPS - kafka to treat all as string
    val readerProps = new Properties()
    readerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    readerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    readerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    readerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, s"DebugConsumer_${UUID.randomUUID()}")
    readerProps.put(ConsumerConfig.GROUP_ID_CONFIG, s"DebugGroup_${UUID.randomUUID()}")
    readerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    // 2 -> MAKE READER
    val reader = new ReaderImpl[EdlaChangeTopic](readerProps, "MyTopic") // must be assigned outside using, otherwise exception from constructor is swallowed by using block
    Using(reader) { reader =>
      for (item <- reader)
        println(f"${item.Key} => ${item.Value}")
    }
  }

  def main(args: Array[String]): Unit = {
    writer_use_case()
    reader_use_case()
  }
}
