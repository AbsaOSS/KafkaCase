package za.co.absa.KafkaCase.Reader

trait Reader[TType] extends Iterator[(String, TType)] with AutoCloseable
