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

import java.time.Duration
import java.util.Properties

trait Reader[TType] extends Iterator[(String, Either[String, TType])] with AutoCloseable

object Reader {
  private val DEFAULT_TIMEOUT = Duration.ofSeconds(6)
  private val DEFAULT_NEVER_ENDING = false

  def readOnce[T: Decoder](reader: Reader[T], work: ((String, Either[String, T])) => Unit): Unit = {
    try {
      for (item <- reader)
        work(item)
    } finally {
      reader.close()
    }
  }

  // note: scala can't handle default parameters together with overloading.... hence slightly exponential number of auxiliary apply methods
  // Primary method that contains default arguments
  def readOnce[T: Decoder](readerProps: Properties, topicName: String, work: ((String, Either[String, T])) => Unit, neverEnding: Boolean = DEFAULT_NEVER_ENDING): Unit =
    readOnce(apply[T](readerProps, topicName, neverEnding = neverEnding), work)

  // With never ending
  def readOnce[T: Decoder](readerConf: Config, topicName: String, work: ((String, Either[String, T])) => Unit, neverEnding: Boolean): Unit =
    readOnce(apply[T](readerConf, topicName, neverEnding = neverEnding), work)

  // Without never ending
  def readOnce[T: Decoder](readerConf: Config, topicName: String, work: ((String, Either[String, T])) => Unit): Unit =
    readOnce(apply[T](readerConf, topicName, neverEnding = DEFAULT_NEVER_ENDING), work)

  // note: scala can't handle default parameters together with overloading.... hence slightly exponential number of auxiliary apply methods
  // Primary method that contains default arguments
  def apply[TType: Decoder](props: Properties, topic: String, timeout: Duration = DEFAULT_TIMEOUT, neverEnding: Boolean = DEFAULT_NEVER_ENDING): Reader[TType] = {
    if (neverEnding)
      new ReaderNeverEnding[TType](props, topic, timeout)
    else
      new ReaderEnding[TType](props, topic, timeout)
  }

  // Overloaded method with Config and all optional arguments
  def apply[TType: Decoder](config: Config, topic: String, timeout: Duration, neverEnding: Boolean): Reader[TType] = {
    val props = convertConfigToProperties(config)
    apply[TType](props, topic, timeout, neverEnding)
  }

  // Overloaded method with Config and neverEnding optional argument
  def apply[TType: Decoder](config: Config, topic: String, neverEnding: Boolean): Reader[TType] = {
    apply[TType](config, topic, DEFAULT_TIMEOUT, neverEnding)
  }

  // Overloaded method with Config and timeout optional argument
  def apply[TType: Decoder](config: Config, topic: String, timeout: Duration): Reader[TType] = {
    apply[TType](config, topic, timeout, DEFAULT_NEVER_ENDING)
  }

  // Overloaded method with Config and none of optional arguments
  def apply[TType: Decoder](config: Config, topic: String): Reader[TType] = {
    apply[TType](config, topic, DEFAULT_TIMEOUT, DEFAULT_NEVER_ENDING)
  }

  private def convertConfigToProperties(config: Config): Properties = {
    val properties = new Properties()
    config.entrySet().forEach { entry =>
      properties.put(entry.getKey, config.getString(entry.getKey))
    }
    properties
  }
}
