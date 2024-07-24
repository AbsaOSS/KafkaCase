package za.co.absa.KafkaCase.Reader

import org.slf4j.LoggerFactory
import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import za.co.absa.KafkaCase.Common.KeyValuePair
import za.co.absa.KafkaCase.Reader.ReaderImpl.log

import java.time.Duration
import java.util
import java.util.Properties

class ReaderImpl[TKey, TValue](props: Properties, topic: String, timeout: Duration = Duration.ofSeconds(3)) extends Reader[TKey, TValue] {
  private val consumer = new KafkaConsumer[TKey, TValue](props)
  consumer.subscribe(util.Arrays.asList(topic))
  private var singlePollIterator = fetchNextBatch()

  override def hasNext(): Boolean = singlePollIterator.hasNext

  override def next(): KeyValuePair[TKey, TValue] = {
    log.info("Fetching next item")
    val nextItem = singlePollIterator.next()
    if (!singlePollIterator.hasNext)
      singlePollIterator = fetchNextBatch()
    KeyValuePair(nextItem.key(), nextItem.value())
  }

  def close(): Unit = consumer.close()

  private def fetchNextBatch(): util.Iterator[ConsumerRecord[TKey, TValue]] = {
    log.info("Fetching next batch")
    consumer.poll(timeout).iterator()
  }
}

object ReaderImpl {
  private val log = LoggerFactory.getLogger(this.getClass)
}
