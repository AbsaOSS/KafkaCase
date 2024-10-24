/*
 * Copyright 2024 ABSA Group Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package za.co.absa.kafkacase.reader

import com.typesafe.config.Config
import io.circe.Decoder
import io.circe.jawn.decode
import org.slf4j.LoggerFactory
import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import za.co.absa.kafkacase.reader.ReaderImpl.{DEFAULT_TIMEOUT, convertConfigToProperties, log}

import java.time.Duration
import java.util
import java.util.Properties

class ReaderImpl[TType: Decoder](props: Properties, topic: String, timeout: Duration, neverEnding: Boolean) extends Reader[TType] {

  private val consumer = new KafkaConsumer[String, String](props)
  consumer.subscribe(util.Arrays.asList(topic))
  private var singlePollIterator = fetchNextBatch()

  override def hasNext: Boolean = singlePollIterator.hasNext

  override def next(): (String, Either[String, TType]) = {
    log.info("Fetching next item")
    val nextItem = singlePollIterator.next()
    if (!singlePollIterator.hasNext)
      singlePollIterator = fetchNextBatch()
    val nextItemMaybeTyped = decode[TType](nextItem.value()) match {
      case Left(_) => Left(s"Cannot parse ${nextItem.value()}")
      case Right(item) => Right(item)
    }
    nextItem.key() -> nextItemMaybeTyped
  }

  def close(): Unit = consumer.close()

  private def fetchNextBatch(): util.Iterator[ConsumerRecord[String, String]] = {
    log.info("Fetching next batch")
    var nextIterator = consumer.poll(timeout).iterator()
    while(neverEnding && !nextIterator.hasNext) {
      log.info("Re-Fetching next batch")
      nextIterator = consumer.poll(timeout).iterator()
    }
    nextIterator
  }
}

object ReaderImpl {
  private val DEFAULT_TIMEOUT: Duration = Duration.ofSeconds(3)
  private val log = LoggerFactory.getLogger(this.getClass)
private val DEFAULT_NEVERENDING: Boolean = true

// note: scala can't handle default parameters together with overloading.... hence slightly exponential number of auxiliary constructors
// Primary method that contains default arguments
  def apply[TType: Decoder](props: Properties, topic: String, timeout: Duration = DEFAULT_TIMEOUT, neverEnding: Boolean = true): ReaderImpl[TType] = {
    new ReaderImpl[TType](props, topic, timeout, neverEnding)
  }

  // Overloaded method without timeout
  def apply[TType: Decoder](props: Properties, topic: String, neverEnding: Boolean): ReaderImpl[TType] = {
    apply[TType](props, topic, DEFAULT_TIMEOUT, neverEnding)
  }

  // Overloaded method without timeout and neverEnding
  def apply[TType: Decoder](props: Properties, topic: String): ReaderImpl[TType] = {
    apply[TType](props, topic, DEFAULT_TIMEOUT, DEFAULT_NEVERENDING)
  }

  // Overloaded method with Config (converts Config to Properties)
  def apply[TType: Decoder](config: Config, topic: String, timeout: Duration = DEFAULT_TIMEOUT, neverEnding: Boolean = true): ReaderImpl[TType] = {
    val props = convertConfigToProperties(config)
    apply[TType](props, topic, timeout, neverEnding)
  }

  // Overloaded method with Config and neverEnding
  def apply[TType: Decoder](config: Config, topic: String, neverEnding: Boolean): ReaderImpl[TType] = {
    apply[TType](config, topic, DEFAULT_TIMEOUT, neverEnding)
  }

  // Overloaded method with Config only
  def apply[TType: Decoder](config: Config, topic: String): ReaderImpl[TType] = {
    apply[TType](config, topic, DEFAULT_TIMEOUT, DEFAULT_NEVERENDING)
  }

  private def convertConfigToProperties(config: Config): Properties = {
    val properties = new Properties()
    config.entrySet().forEach { entry =>
      properties.put(entry.getKey, config.getString(entry.getKey))
    }
    properties
  }
}
