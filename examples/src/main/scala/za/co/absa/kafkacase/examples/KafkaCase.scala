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
import za.co.absa.kafkacase.models.topics.EdlaChange
import za.co.absa.kafkacase.reader.ReaderImpl
import za.co.absa.kafkacase.writer.WriterImpl

import java.util.{Properties, UUID}

object KafkaCase {
  private def writer_use_case(): Unit = {
    // 0 -> HAVE SOMETHING TO WRITE
    val messageToWrite = EdlaChange(
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

    // 1 -> DEFINE PROPS - kafka to treat all as string
    val writerProps = new Properties()
    writerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "ZADALNRAPP00009.corp.dsarena.com:9092")
    writerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    writerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    // 2 -> MAKE WRITER
    val writer = new WriterImpl[EdlaChange](writerProps, "KillMePleaseTopic")
    try {
      // 3 -> WRITE
      writer.Write("sampleMessageKey", messageToWrite)
    } finally {
      // Releasing resources should be handled by using block in newer versions of scala
      writer.close()
    }
  }

  private def reader_use_case(): Unit = {
    // 1 -> DEFINE PROPS - kafka to treat all as string
    val readerProps = new Properties()
    readerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "ZADALNRAPP00009.corp.dsarena.com:9092")
    readerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    readerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    readerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, s"DebugConsumer_${UUID.randomUUID()}")
    readerProps.put(ConsumerConfig.GROUP_ID_CONFIG, s"DebugGroup_${UUID.randomUUID()}")
    readerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    // 2 -> MAKE READER (should be in using block for newer versions of scala)
    val reader = new ReaderImpl[EdlaChange](readerProps, "KillMePleaseTopic")
    try {
      for (item <- reader)
        println(item)
    } finally {
      // Releasing resources should be handled by using block in newer versions of scala
      reader.close()
    }
  }

  def main(args: Array[String]): Unit = {
    writer_use_case()
    reader_use_case()
  }
}