package za.co.absa.KafkaCase

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import za.co.absa.KafkaCase.Reader.ReaderImpl
import za.co.absa.KafkaCase.Writer.WriterImpl

import java.util.{Properties, UUID}
import scala.util.Using // !!!WARNING!!!: this seems to swallows exceptions during creation of the resource

object KafkaCase {
  def main(args: Array[String]): Unit = {
    val writerProps = new Properties()
    writerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    writerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    writerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    val writer = new WriterImpl[String, String](writerProps, "MyTopic") // must be assigned outside using, otherwise exception from constructor is swallowed by using block
    Using (writer) { writer =>
      writer.Write("messageKey", "I COMPUTE THEREFORE I AM")
      writer.Write("messageKey", "Foo + Bar = FooBar")
    }

    val readerProps = new Properties()
    readerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    readerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    readerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    readerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, s"DebugConsumer_${UUID.randomUUID()}")
    readerProps.put(ConsumerConfig.GROUP_ID_CONFIG, s"DebugGroup_${UUID.randomUUID()}")
    readerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val reader = new ReaderImpl[String, String](readerProps, "MyTopic") // must be assigned outside using, otherwise exception from constructor is swallowed by using block
    Using(reader) { reader =>
      for (item <- reader)
        println(f"${item.Key} => ${item.Value}")
    }
  }
}
