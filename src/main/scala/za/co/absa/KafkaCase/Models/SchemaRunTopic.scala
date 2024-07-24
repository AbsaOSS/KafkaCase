package za.co.absa.KafkaCase.Models

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
  sealed trait Status {
    case class Finished() extends Status
    case class Failed() extends Status
    case class Killed() extends Status
  }
}
