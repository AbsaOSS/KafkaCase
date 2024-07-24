package za.co.absa.KafkaCase.Reader

import za.co.absa.KafkaCase.Common.KeyValuePair

trait Reader[TKey, TValue] extends Iterator[KeyValuePair[TKey, TValue]] with AutoCloseable
