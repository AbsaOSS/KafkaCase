package za.co.absa.KafkaCase.Models

case class EdlaChangeTopic(
  id: String,
  app_id_snow: String,
  source_app: String,
  environment: String,
  timestamp_event: String,
  data_definition: String,
  operation: EdlaChangeTopic.Operation,
  location: String,
  format: String,
  schema_link: String
)

object EdlaChangeTopic {
  sealed trait Operation {
    case class CREATE() extends Operation
    case class UPDATE() extends Operation
    case class ARCHIVE() extends Operation
  }
}
