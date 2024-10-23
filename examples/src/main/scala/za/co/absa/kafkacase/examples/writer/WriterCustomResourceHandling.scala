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

package za.co.absa.kafkacase.examples.writer

import io.circe.Encoder
import za.co.absa.kafkacase.models.utils.ResourceHandler.withResource
import za.co.absa.kafkacase.writer.WriterImpl

import java.util.Properties

object WriterCustomResourceHandling {
  def apply[T: Encoder](writerProps: Properties, topicName: String, sampleMessageToWrite: T): Unit = {
    withResource(new WriterImpl[T](writerProps, topicName))(writer => {
      writer.write("sampleMessageKey1", sampleMessageToWrite)
      writer.write("sampleMessageKey2", sampleMessageToWrite)
    })
  }
}
