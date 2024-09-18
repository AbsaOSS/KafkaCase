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
case class SchemaRunTopic(
  id: String,
  job_ref: String,
  app_id_snow: String,
  source_app: String,
  environment: String,
  timestamp_start: Long,
  timestamp_end: Long,
  data_definition_id: String,
  status: SchemaRunTopic.Status,
  message: String
)

object SchemaRunTopic {
  sealed trait Status

  object Status {
    case class Finished() extends Status
    case class Failed() extends Status
    case class Killed() extends Status

    implicit val operationEncoder: Encoder[Status] = Encoder.encodeString.contramap[Status] {
      case Finished() => s"Finished"
      case Failed() => s"Failed"
      case Killed() => s"Killed"
    }

    implicit val operationDecoder: Decoder[Status] = Decoder.decodeString.emap {
      case "Finished" => Right(Finished())
      case "Failed" => Right(Failed())
      case "Killed" => Right(Killed())
    }
  }
}
