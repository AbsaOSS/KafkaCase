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

import com.typesafe.config.ConfigFactory
import za.co.absa.kafkacase.examples.reader.{ReaderCustomResourceHandling, ReaderManualResourceHandling, ReaderReadOnce, ReaderUsingsResourceHandling}
import za.co.absa.kafkacase.examples.writer.{WriterCustomResourceHandling, WriterManualResourceHandling, WriterUsingsResourceHandling, WriterWriteOnce}
import za.co.absa.kafkacase.models.topics.EdlaChange

object KafkaCase {
  private val config = ConfigFactory.load()

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

  def main(args: Array[String]): Unit = {
    val writerConfig = config.getConfig("writer")
    val readerConfig = config.getConfig("reader")
    val topicName = config.getString("topicName")
    println("Sample how to use writer with manual resource handling")
    WriterManualResourceHandling(writerConfig, topicName, sampleMessageToWrite)
    println("Sample how to use writer with custom resource handling")
    WriterCustomResourceHandling(writerConfig, topicName, sampleMessageToWrite)
    println("Sample how to use writer with Usings in scala 3")
    WriterUsingsResourceHandling(writerConfig, topicName, sampleMessageToWrite)
    println("Sample how to use writer write-once fashion")
    WriterWriteOnce(writerConfig, topicName, sampleMessageToWrite)
    println("Sample how to use reader with manual resource handling")
    ReaderManualResourceHandling[EdlaChange](readerConfig, topicName)
    println("Sample how to use reader with custom resource handling")
    ReaderCustomResourceHandling[EdlaChange](readerConfig, topicName)
    println("Sample how to use reader with Usings in scala 3")
    ReaderUsingsResourceHandling[EdlaChange](readerConfig, topicName)
    println("Sample how to use reader read-once fashion")
    ReaderReadOnce[EdlaChange](readerConfig, topicName)
  }
}
