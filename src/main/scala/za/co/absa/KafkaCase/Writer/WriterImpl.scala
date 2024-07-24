package za.co.absa.KafkaCase.Writer

import org.slf4j.LoggerFactory
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import za.co.absa.KafkaCase.Writer.WriterImpl.log

import java.util.Properties

class WriterImpl[TKey, TValue](props: Properties, topic: String) extends Writer[TKey, TValue] {
  private val producer = new KafkaProducer[TKey, TValue](props)

  def Write(key: TKey, value: TValue): Unit = {
    log.info(f"Writing: $key => $value")
    producer.send(new ProducerRecord[TKey, TValue](topic, key, value))
  }

  def Flush(): Unit = producer.flush()

  override def close(): Unit = producer.close()
}

object WriterImpl {
  private val log = LoggerFactory.getLogger(this.getClass)
}
