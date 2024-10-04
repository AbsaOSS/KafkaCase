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

package za.co.absa.KafkaCase.Examples

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import za.co.absa.kafkacase.models.topics.EdlaChange
import za.co.absa.kafkacase.models.utils.ResourceHandler.withResource
import za.co.absa.kafkacase.reader.ReaderImpl
import za.co.absa.kafkacase.writer.WriterImpl

import java.util.{Properties, UUID}
// scala3 only
// import scala.util.Using

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

  private def writer_use_case_scala2(): Unit = {
    val writer = new WriterImpl[EdlaChange](writerProps, topicName)
    try {
      writer.Write("sampleMessageKey1", sampleMessageToWrite)
      writer.Write("sampleMessageKey2", sampleMessageToWrite)
    } finally {
      writer.close()
    }
  }

  private def writer_use_case_scala2_custom_resource_handler(): Unit = {
    withResource(new WriterImpl[EdlaChange](writerProps, topicName))(writer => {
      writer.Write("sampleMessageKey1", sampleMessageToWrite)
      writer.Write("sampleMessageKey2", sampleMessageToWrite)
    })
  }

//  scala3 only
//  private def writer_use_case_scala3(): Unit = {
//    Using(new WriterImpl[EdlaChange](writerProps, topicName)) { writer =>
//      writer.Write("sampleMessageKey1", sampleMessageToWrite)
//      writer.Write("sampleMessageKey2", sampleMessageToWrite)
//    }
//  }

  private def reader_use_case_scala2(): Unit = {
    val reader = new ReaderImpl[EdlaChange](readerProps, topicName, neverEnding = false)
    try {
      for (item <- reader)
        println(item)
    } finally {
      reader.close()
    }
  }

  private def reader_use_case_scala2_custom_resource_handler(): Unit = {
    withResource(new ReaderImpl[EdlaChange](readerProps, topicName, neverEnding = false))(reader => {
      for (item <- reader)
        println(item)
    })
  }

//  scala3 only
//  private def reader_use_case_scala3(): Unit = {
//    Using(new ReaderImpl[EdlaChange](readerProps, topicName, neverEnding = false)) { reader =>
//      for (item <- reader)
//        println(item)
//    }
//  }

  def main(args: Array[String]): Unit = {
    writer_use_case_scala2()
    reader_use_case_scala2()

    writer_use_case_scala2_custom_resource_handler()
    reader_use_case_scala2_custom_resource_handler()

//    scala3 only
//    writer_use_case_scala3()
//    reader_use_case_scala3()
  }
}
