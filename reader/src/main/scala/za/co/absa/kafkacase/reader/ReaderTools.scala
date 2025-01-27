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
import io.circe.jawn.decode
import org.apache.kafka.clients.consumer.ConsumerRecord

object ReaderTools {
  def parseRecord[TType: Decoder](record: ConsumerRecord[String, String]): (String, Either[String, TType]) = {
    val maybeTyped = decode[TType](record.value()) match {
      case Left(_) => Left(s"Cannot parse ${record.value()}")
      case Right(item) => Right(item)
    }
    record.key() -> maybeTyped
  }
}
