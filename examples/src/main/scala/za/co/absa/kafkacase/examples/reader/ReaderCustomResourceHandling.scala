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

package za.co.absa.kafkacase.examples.reader

import io.circe.Decoder
import za.co.absa.kafkacase.models.utils.ResourceHandler.withResource
import za.co.absa.kafkacase.reader.ReaderImpl

import java.util.Properties

object ReaderCustomResourceHandling {
  def apply[T: Decoder](readerProps: Properties, topicName: String): Unit = {
    withResource(new ReaderImpl[T](readerProps, topicName, neverEnding = false))(reader => {
      for (item <- reader)
        println(item)
    })
  }
}