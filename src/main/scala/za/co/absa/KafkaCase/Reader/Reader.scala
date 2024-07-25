package za.co.absa.KafkaCase.Reader

import za.co.absa.KafkaCase.Common.KeyValuePair

trait Reader[TType] extends Iterator[KeyValuePair[String, TType]] with AutoCloseable
