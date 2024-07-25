package za.co.absa.KafkaCase.Reader

import io.circe.Decoder
import io.circe.jawn.decode
import org.slf4j.LoggerFactory
import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import za.co.absa.KafkaCase.Common.KeyValuePair
import za.co.absa.KafkaCase.Reader.ReaderImpl.log

import java.time.Duration
import java.util
import java.util.Properties

class ReaderImpl[TType: Decoder](props: Properties, topic: String, timeout: Duration = DEFAULT_TIMEOUT, neverEnding: Boolean = true) extends Reader[TType] {
  private val consumer = new KafkaConsumer[String, String](props)
  consumer.subscribe(util.Arrays.asList(topic))
  private var singlePollIterator = fetchNextBatch()

  override def hasNext(): Boolean = singlePollIterator.hasNext

  override def next(): KeyValuePair[String, TType] = {
    log.info("Fetching next item")
    val nextItem = singlePollIterator.next()
    val nextItemTyped = decode[TType](nextItem.value()).getOrElse(throw new Exception(s"Cannot parse $nextItem"))
    if (!singlePollIterator.hasNext)
      singlePollIterator = fetchNextBatch()
    KeyValuePair(nextItem.key(), nextItemTyped)
  }

  def close(): Unit = consumer.close()

  private def fetchNextBatch(): util.Iterator[ConsumerRecord[String, String]] = {
    log.info("Fetching next batch")
    var nextIterator = consumer.poll(timeout).iterator()
    while(neverEnding && !nextIterator.hasNext)
      log.info("Re-Fetching next batch")
      nextIterator = consumer.poll(timeout).iterator()
    nextIterator
  }
}

object ReaderImpl {
  private val DEFAULT_TIMEOUT: Duration = Duration.ofSeconds(3)
  private val log = LoggerFactory.getLogger(this.getClass)
}
