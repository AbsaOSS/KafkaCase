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

package za.co.absa.kafkacase.models.topics

import io.circe.{Decoder, Encoder}
import io.circe.generic.JsonCodec

@JsonCodec
case class EdlaChange(
  app_id_snow: String,
  data_definition_id: String,
  environment: String,
  format: String,
  guid: String,
  location: String,
  operation: EdlaChange.Operation,
  schema_link: String,
  source_app: String,
  timestamp_event: Long
)

object EdlaChange {
  sealed trait Operation

  object Operation {
    case class Create() extends Operation
    case class Update() extends Operation
    case class Archive() extends Operation

    implicit val operationEncoder: Encoder[Operation] = Encoder.encodeString.contramap[Operation] {
      case Create() => s"CREATE"
      case Update() => s"UPDATE"
      case Archive() => s"ARCHIVE"
    }

    implicit val operationDecoder: Decoder[Operation] = Decoder.decodeString.emap {
      case "CREATE" => Right(Create())
      case "UPDATE" => Right(Update())
      case "ARCHIVE" => Right(Archive())
    }
  }
}
