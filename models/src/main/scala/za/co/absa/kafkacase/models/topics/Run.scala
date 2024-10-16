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
import za.co.absa.kafkacase.models.topics.Run.Job

@JsonCodec
case class Run(
  event_id: String,
  job_ref: String,
  tenant_id: String,
  source_app: String,
  source_app_version: String,
  environment: String,
  timestamp_start: Long,
  timestamp_end: Long,
  jobs: Seq[Job]
)

object Run {
  @JsonCodec
  case class Job(
    catalog_id: String,
    status: Status,
    timestamp_start: Long,
    timestamp_end: Long,
    message: String
  )

  sealed trait Status

  object Status {
    case class Succeeded() extends Status
    case class Failed() extends Status
    case class Killed() extends Status
    case class Skipped() extends Status

    implicit val operationEncoder: Encoder[Status] = Encoder.encodeString.contramap[Status] {
      case Succeeded() => s"succeeded"
      case Failed() => s"failed"
      case Killed() => s"killed"
      case Skipped() => s"skipped"
    }

    implicit val operationDecoder: Decoder[Status] = Decoder.decodeString.emap {
      case "succeeded" => Right(Succeeded())
      case "failed" => Right(Failed())
      case "killed" => Right(Killed())
      case "skipped" => Right(Skipped())
    }
  }
}
