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

import java.util.Properties

trait Reader[TType] extends Iterator[(String, Either[String, TType])] with AutoCloseable

object Reader {
  def readOnce[T: Decoder](readerProps: Properties, topicName: String, work: ((String, Either[String, T])) => Unit): Unit = {
    val reader = new ReaderImpl[T](readerProps, topicName, neverEnding = false)
    try {
      for (item <- reader)
        work(item)
    } finally {
      reader.close()
    }
  }

  def readOnce[T: Decoder](readerConf: Config, topicName: String, work: ((String, Either[String, T])) => Unit): Unit = {
    val reader = new ReaderImpl[T](readerConf, topicName, neverEnding = false)
    try {
      for (item <- reader)
        work(item)
    } finally {
      reader.close()
    }
  }
}
