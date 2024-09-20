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

package za.co.absa.KafkaCase.Models

import io.circe.{Decoder, Encoder}
import io.circe.generic.JsonCodec

@JsonCodec
case class EdlaChangeTopic(
  app_id_snow: String,
  data_definition_id: String,
  environment: String,
  format: String,
  guid: String,
  location: String,
  operation: EdlaChangeTopic.Operation,
  schema_link: String,
  source_app: String,
  timestamp_event: Long
)

object EdlaChangeTopic {
  sealed trait Operation

  object Operation {
    case class CREATE() extends Operation
    case class UPDATE() extends Operation
    case class ARCHIVE() extends Operation

    implicit val operationEncoder: Encoder[Operation] = Encoder.encodeString.contramap[Operation] {
      case CREATE() => s"CREATE"
      case UPDATE() => s"UPDATE"
      case ARCHIVE() => s"ARCHIVE"
    }

    implicit val operationDecoder: Decoder[Operation] = Decoder.decodeString.emap {
      case "CREATE" => Right(CREATE())
      case "UPDATE" => Right(UPDATE())
      case "ARCHIVE" => Right(ARCHIVE())
    }
  }
}
