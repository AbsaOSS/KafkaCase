package za.co.absa.KafkaCase.Writer

import io.circe.Encoder
import io.circe.syntax.*
import org.slf4j.LoggerFactory
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import za.co.absa.KafkaCase.Writer.WriterImpl.log

import java.util.Properties

class WriterImpl[TType: Encoder](props: Properties, topic: String) extends Writer[TType] {
  private val producer = new KafkaProducer[String, String](props)

  def Write(key: String, value: TType): Unit = {
    log.info(f"Writing: $key => $value")
    producer.send(new ProducerRecord[String, String](topic, key, value.asJson.noSpaces))
  }
  
  def Flush(): Unit = producer.flush()

  override def close(): Unit = producer.close()
}

object WriterImpl {
  private val log = LoggerFactory.getLogger(this.getClass)
}
