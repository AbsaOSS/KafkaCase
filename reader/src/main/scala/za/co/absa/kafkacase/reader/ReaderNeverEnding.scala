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

import io.circe.Decoder
import org.slf4j.LoggerFactory
import org.apache.kafka.clients.consumer.KafkaConsumer
import za.co.absa.kafkacase.reader.ReaderNeverEnding.log
import za.co.absa.kafkacase.reader.ReaderTools.parseRecord

import java.time.Duration
import java.util
import java.util.Properties

class ReaderNeverEnding[TType: Decoder](props: Properties, topic: String, timeout: Duration) extends Reader[TType] {
  private val consumer = new KafkaConsumer[String, String](props)
  consumer.subscribe(util.Arrays.asList(topic))
  log.info("Fetching initial batch")
  private var singlePollIterator = consumer.poll(timeout).iterator()

  override def hasNext: Boolean = true

  override def next(): (String, Either[String, TType]) = {
    while(!singlePollIterator.hasNext) {
      log.info("(Re)Fetching next batch")
      singlePollIterator = consumer.poll(timeout).iterator()
    }
    log.info("Fetching next item")
    val nextItem = singlePollIterator.next()
    parseRecord(nextItem)
  }

  def close(): Unit = consumer.close()
}

object ReaderNeverEnding {
  private val log = LoggerFactory.getLogger(this.getClass)
}
