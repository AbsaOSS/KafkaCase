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

package za.co.absa.kafkacase.examples

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import za.co.absa.kafkacase.examples.reader.{ReaderCustomResourceHandling, ReaderManualResourceHandling, ReaderUsingsResourceHandling}
import za.co.absa.kafkacase.examples.writer.{WriterCustomResourceHandling, WriterManualResourceHandling, WriterUsingsResourceHandling}
import za.co.absa.kafkacase.models.topics.EdlaChange

import java.util.{Properties, UUID}

object KafkaCase {
  // This goes from your application logic
  private val sampleMessageToWrite = EdlaChange(
    app_id_snow = "N/A",
    data_definition_id = "TestingThis",
    environment = "DEV",
    format = "FooBar",
    guid = "DebugId",
    location = "ether",
    operation = EdlaChange.Operation.Create(),
    schema_link = "http://not.here",
    source_app = "ThisCode",
    timestamp_event = 12345
  )

  // This goes from your config / domain knowledge
  private val topicName = "KillMePleaseTopic"

  // This goes from your writer configs
  private val writerProps = new Properties()
  writerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "ZADALNRAPP00009.corp.dsarena.com:9092")
  writerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  writerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

  // This goes from your reader configs
  private val readerProps = new Properties()
  readerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "ZADALNRAPP00009.corp.dsarena.com:9092")
  readerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  readerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  readerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, s"DebugConsumer_${UUID.randomUUID()}")
  readerProps.put(ConsumerConfig.GROUP_ID_CONFIG, s"DebugGroup_${UUID.randomUUID()}")
  readerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")


  def main(args: Array[String]): Unit = {
    WriterManualResourceHandling(writerProps, topicName, sampleMessageToWrite)
    WriterCustomResourceHandling(writerProps, topicName, sampleMessageToWrite)
    WriterUsingsResourceHandling(writerProps, topicName, sampleMessageToWrite)
    WriterWriteOnce(writerProps, topicName, sampleMessageToWrite)
    ReaderManualResourceHandling[EdlaChange](readerProps, topicName)
    ReaderCustomResourceHandling[EdlaChange](readerProps, topicName)
    ReaderUsingsResourceHandling[EdlaChange](readerProps, topicName)
    ReaderReadOnce[EdlaChange](readerProps, topicName)
  }
}
