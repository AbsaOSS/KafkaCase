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
  event_id: String,
  tenant_id: String,
  source_app: String,
  source_app_version: String,
  environment: String,
  timestamp_event: Long,
  catalog_id: String,
  operation: EdlaChange.Operation,
  location: String,
  format: String,
  formatOptions: Map[String, String]
)

object EdlaChange {
  sealed trait Operation

  object Operation {
    case class Overwrite() extends Operation
    case class Append() extends Operation
    case class Archive() extends Operation
    case class Delete() extends Operation

    implicit val operationEncoder: Encoder[Operation] = Encoder.encodeString.contramap[Operation] {
      case Overwrite() => s"overwrite"
      case Append() => s"append"
      case Archive() => s"archive"
      case Delete() => s"delete"
    }

    implicit val operationDecoder: Decoder[Operation] = Decoder.decodeString.emap {
      case "overwrite" => Right(Overwrite())
      case "append" => Right(Append())
      case "archive" => Right(Archive())
      case "delete" => Right(Delete())
    }
  }
}
